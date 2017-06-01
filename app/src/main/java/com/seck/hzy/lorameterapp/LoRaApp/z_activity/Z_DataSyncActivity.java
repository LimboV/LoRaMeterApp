package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.Building;
import com.seck.hzy.lorameterapp.LoRaApp.model.Colector;
import com.seck.hzy.lorameterapp.LoRaApp.model.Meter;
import com.seck.hzy.lorameterapp.LoRaApp.model.Residential;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.SkRestClient;
import com.seck.hzy.lorameterapp.R;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Z_DataSyncActivity extends Activity {

	private ProgressDialog progressBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.z_activity_data_sync_view);

		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		findViewById(R.id.download_data_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!HzyUtils.isNetworkAvailable(Z_DataSyncActivity.this)) {
					HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "本地网络未开启",
							"提醒");
					return;
				}

				startActivity(new Intent(Z_DataSyncActivity.this, Z_XQSelectActivity.class));
			}
		});

		findViewById(R.id.upload_data_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!HzyUtils.isNetworkAvailable(Z_DataSyncActivity.this)) {
					HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "本地网络未开启","提醒");
					return;
				}
				List<Meter> validMeters;
				validMeters = MenuActivity.dataSource.getAllValidMeters();
				if(validMeters.size() < 1) {
					final List<Meter> uploadedMeters = MenuActivity.dataSource.getAllUploadedMeters();
					if(uploadedMeters.size() < 1) {
						HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "没有数据需要上传","提醒");
						return;
					}

					new AlertDialog.Builder(Z_DataSyncActivity.this)
							.setTitle("提醒")
							.setMessage("已上传，是否需要重新上传")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									updateMeterData(uploadedMeters);
								}
							})
							.setNegativeButton("Cancel", null)
							.create().show();
					return;
				}

				updateMeterData(validMeters);
			}
		});

		findViewById(R.id.import_data_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDatabaseFile();
			}
		});

		findViewById(R.id.export_data_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String res = exportDatabase();
				HintDialog.ShowHintDialog(Z_DataSyncActivity.this, res, "状态");
			}
		});
	}

	private void updateMeterData(final List<Meter> data) {
		progressBar.setIndeterminate(true);
		progressBar.setMessage("上传数据");
		progressBar.show();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int re = SkRestClient.uploadMeterData(data);

					if(re < data.size()) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								progressBar.dismiss();
								HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "上传数据失败",
										"提示");
							}
						});
						return;
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progressBar.dismiss();
							HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "上传数据成功",
									"提示");
						}
					});
				} catch (UnknownHostException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progressBar.dismiss();
							HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "无法连接到服务器",
									"提示");
						}
					});
				} catch (InterruptedIOException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progressBar.dismiss();
							HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "无法连接到服务器",
									"错误");
						}
					});
				} catch (IOException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progressBar.dismiss();
							HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "服务器无响应",
									"错误");
						}
					});
				}
			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) { // File selection
			if (resultCode == Activity.RESULT_OK) { // Importing xls Database
				String filePath = data.getStringExtra(Z_FileDialog.RESULT_PATH);
				importDatabase(filePath);
			} else if (resultCode == Activity.RESULT_CANCELED) {

			}
		} else if (requestCode == 2) { // File selection
			if (resultCode == Activity.RESULT_OK) { // Importing xls Database
				String filePath = data.getStringExtra(Z_FileDialog.RESULT_PATH);
				importDatabase(filePath);
			} else if (resultCode == Activity.RESULT_CANCELED) {

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void selectDatabaseFile() {
		Intent intent = new Intent(getBaseContext(), Z_FileDialog.class);
		intent.putExtra(Z_FileDialog.START_PATH, "/sdcard");

		// can user select directories or not
		intent.putExtra(Z_FileDialog.CAN_SELECT_DIR, false);

		// alternatively you can set file filter
		intent.putExtra(Z_FileDialog.FORMAT_FILTER, new String[] { "xls" });
		startActivityForResult(intent, 1);
	}

	public void importDatabase(String path) {
		try {
			final Workbook book = Workbook.getWorkbook(new File(path));
			if (book == null) {
				HintDialog.ShowHintDialog(this, "无效的xls格式","错误");
				return;
			}

			int sheetsNum = book.getNumberOfSheets();

			if (sheetsNum == 0) {
				HintDialog.ShowHintDialog(this, "空的数据库","错误");
				return;
			}

			final Sheet sheet = book.getSheet(0);

			if( sheet == null ) {
				HintDialog.ShowHintDialog(this, "空的数据库","错误");
				return;
			}

			final int Rows = sheet.getRows();
			final int Cols = sheet.getColumns();
			final String sheetName = sheet.getName();

			if( Rows == 0 || Cols == 0 ) {
				HintDialog.ShowHintDialog(this, "空的数据库","错误");
				return;
			}

			progressBar.setCancelable(false);
			progressBar.setMessage("数据库导入进度");
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.setMax(Rows-1);
			progressBar.show();

			new Thread(new Runnable() {

				@Override
				public void run() {

					if (sheetName.equalsIgnoreCase("xq")) { // Meter type A

						// Delete Za
						MenuActivity.dataSource.deleteResidentials();

						for (int i = 1; i < Rows; i++) {
							Residential record = new Residential();
							for (int j = 0; j < Cols; j++) {
								record.setField(sheet.getCell(j, 0).getContents(), sheet
										.getCell(j, i).getContents());
							}

							MenuActivity.dataSource.insertResidential(record);

							final int gress_i = i;
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									progressBar.setProgress(gress_i);
								}
							});
						}

						//HintDialog.ShowHintDialog(this, "数据库导入成功", "提示");
					} else if (sheetName.equalsIgnoreCase("ly")) { // Meter type B
						// Delete Zb
						MenuActivity.dataSource.deleteBuildings();

						for (int i = 1; i < Rows; i++) {
							Building record = new Building();
							for (int j = 0; j < Cols; j++) {
								record.setField(sheet.getCell(j, 0).getContents(), sheet
										.getCell(j, i).getContents());
							}
							MenuActivity.dataSource.insertBuilding(record);

							final int gress_i = i;
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									progressBar.setProgress(gress_i);
								}
							});
						}

						//HintDialog.ShowHintDialog(this, "数据库导入成功", "提示");
					} else if (sheetName.equalsIgnoreCase("cjj")) { // Meter type B
						// Delete Zb
						MenuActivity.dataSource.deleteColectors();

						for (int i = 1; i < Rows; i++) {
							Colector record = new Colector();
							for (int j = 0; j < Cols; j++) {
								record.setField(sheet.getCell(j, 0).getContents(), sheet
										.getCell(j, i).getContents());
							}
							MenuActivity.dataSource.insertColector(record);

							final int gress_i = i;
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									progressBar.setProgress(gress_i);
								}
							});
						}

						//HintDialog.ShowHintDialog(this, "数据库导入成功", "提示");
					} else if (sheetName.equalsIgnoreCase("sb")) { // Meter type B
						// Delete Zb
						MenuActivity.dataSource.deleteMeters();

						for (int i = 1; i < Rows; i++) {
							Meter record = new Meter();
							for (int j = 0; j < Cols; j++) {
								record.setField(sheet.getCell(j, 0).getContents(), sheet
										.getCell(j, i).getContents());
							}
							MenuActivity.dataSource.insertMeter(record);

							final int gress_i = i;
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									progressBar.setProgress(gress_i);
								}
							});
						}

						//HintDialog.ShowHintDialog(this, "数据库导入成功", "提示");
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								progressBar.cancel();
								HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "错误的表类型",
										"错误");
							}
						});
						return;
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progressBar.cancel();
							HintDialog.ShowHintDialog(Z_DataSyncActivity.this, "数据库导入成功",
									"提示");
						}
					});

					book.close();
				}
			}).start();
		} catch (BiffException e) {
			HintDialog.ShowHintDialog(this, e.toString(), "异常");
		} catch (IOException e) {
			HintDialog.ShowHintDialog(this, e.toString(), "异常");
		} catch (Exception e) {
			HintDialog.ShowHintDialog(this, e.toString(), "异常");
		}

	}

	public String exportDatabase() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

		if( !sdCardExist )
			return "请插入SD卡，数据库将导入至/sdcard/sbout.xls";

		File sdDir = Environment.getExternalStorageDirectory();

		if( sdDir == null ) {
			return "无法获得SDCard目录";
		}

		String sdPath = sdDir.getAbsolutePath();
		if( sdPath == null ) {
			return "无法获得SDCard根目录";
		}

		String filePath = sdPath + "/" + "sbout.xls";

		File xlsFile = new File(filePath);

		try {
			xlsFile.deleteOnExit();
		}
		catch(SecurityException e) {
			return e.toString();
		}

		try {
			WritableWorkbook book = Workbook.createWorkbook(xlsFile);
			WritableSheet sheet = book.createSheet("sb", 0);

			int colNum = MenuActivity.dataSource.SB_EXPORT_COLUMNS.length-1;
			for(int i=0;i<colNum;i++) {
				Label label = new Label(i, 0, MenuActivity.dataSource.SB_EXPORT_COLUMNS[i]);
				sheet.addCell(label);
			}

			List<Meter> result = MenuActivity.dataSource.getAllMeters();
			if( result == null || result.size() < 1) {
				book.write();
				book.close();
				return "空的数据库";
			}

			for(int i=0; i<result.size(); i++) {
				for(int j=0;j<colNum;j++) {
					Label label = new Label(j, i+1, result.get(i).getField(
							MenuActivity.dataSource.SB_EXPORT_COLUMNS[j]));
					sheet.addCell(label);
				}
			}

			book.write();
			book.close();
		} catch (IOException e) {
			return e.toString();
		} catch (RowsExceededException e) {
			return e.toString();
		} catch (WriteException e) {
			return e.toString();
		}

		return "导出成功，请见sd卡下sbout.xls";
	}
	 
	/*public void selectOutDatabaseFile() {
		Intent intent = new Intent(getBaseContext(), FileDialog.class);
		intent.putExtra(FileDialog.START_PATH, "/sdcard");

		// can user select directories or not
		intent.putExtra(FileDialog.CAN_SELECT_DIR, true);

		startActivityForResult(intent, 2);
	}
	*/

}
