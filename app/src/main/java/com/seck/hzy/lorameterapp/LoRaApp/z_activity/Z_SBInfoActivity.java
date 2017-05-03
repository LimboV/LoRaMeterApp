package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.Meter;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * SBInfoActivity 用于显示单个水表的详细信息，手工修改数据以及单表抄写。
 *
 * @author Guhong.
 *
 */
public class Z_SBInfoActivity extends Activity {
	private ProgressDialog progressBar = null;
	private Z_SBInfoActivity thisView = null;

	public EditText et_NAME = null;
	public EditText et_ADDR = null;
	public EditText et_SFBH = null;
	public EditText et_SBGH = null;
	public EditText et_SYCJ = null;
	public EditText et_BCCJ = null;
	public EditText et_BYYS = null;
	public EditText et_CBRQ = null;
	public EditText et_SYCBRQ = null;
	public TextView tv_SBZT = null;

	private boolean binaryPic = false;
	private Meter meter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.z_activity_sbinfoview);

		thisView = this;
		progressBar = new ProgressDialog(this);

		et_NAME = (EditText)this.findViewById(R.id.sbinfo_NAME);
		et_ADDR = (EditText)this.findViewById(R.id.sbinfo_ADDR);
		et_SFBH = (EditText)this.findViewById(R.id.sbinfo_SFBH);
		et_SBGH = (EditText)this.findViewById(R.id.sbinfo_SBGH);
		et_SYCJ = (EditText)this.findViewById(R.id.sbinfo_SYCJ);
		et_BCCJ = (EditText)this.findViewById(R.id.sbinfo_BCCJ);
		et_BYYS = (EditText)this.findViewById(R.id.sbinfo_BYYS);
		et_CBRQ = (EditText)this.findViewById(R.id.sbinfo_CBRQ);
		et_SYCBRQ = (EditText)this.findViewById(R.id.sbinfo_SYCBRQ);
		tv_SBZT = (TextView) this.findViewById(R.id.sbinfo_SBZT);

		final Bundle bundle = this.getIntent().getExtras();
		Integer _id = bundle.getInt("_id", -1);
		meter = MenuActivity.dataSource.getMeter(_id);

		et_NAME.setText(meter.Name);
		et_ADDR.setText(meter.Address);
		et_SFBH.setText(meter.PayNo);
		et_SBGH.setText(meter.Serial);
		et_SYCJ.setText(String.valueOf(meter.LastData));
		et_BCCJ.setText(String.valueOf(meter.Data));
		et_BYYS.setText(String.valueOf(meter.WATER));
		et_CBRQ.setText(meter.DataTime);
		et_SYCBRQ.setText(meter.LastDataTime);

		// 修改数据的地方
		et_BCCJ.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

				try {
					float dbccj = Float.valueOf(et_BCCJ.getText().toString());
					float dsycj = Float.valueOf(et_SYCJ.getText().toString());
					float dbyys = dbccj - dsycj;
					//et_BYYS.setText(String.valueOf(dbyys));
					DecimalFormat df = new DecimalFormat("###.0");
					et_BYYS.setText(df.format(dbyys));
				}
				catch( NumberFormatException e)
				{
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
				// TODO Auto-generated method stub

			}

		});

		if(meter.state > 0) {
			if(meter.IsFault) {
				tv_SBZT.setText("抄表异常");
			}else {
				tv_SBZT.setText("已抄表");
			}
		}
		else {
			tv_SBZT.setText("未抄表");
		}

		int imgboxlist[] = {
				R.id.ib_01,
				R.id.ib_02,
				R.id.ib_03,
				R.id.ib_04,
				R.id.ib_05,
				R.id.ib_06,
				R.id.ib_07,
				R.id.ib_08
		};

		if (meter.Images != null) {
			for (int i = 0; i < imgboxlist.length; i++) {
				if (i >= meter.Images.size()) {
					break;
				}

				byte[] picdata = meter.Images.get(i);
				if (picdata == null) {
					continue;
				}
				Bitmap bp;
				if (meter.IsBinaryzation) {
					bp = BluetoothConnectThread.binaryPicBytesToBitmap(picdata);
				} else {
					bp = BitmapFactory.decodeByteArray(picdata, 0, picdata.length);
				}

				ImageView iv = (ImageView) findViewById(imgboxlist[i]);
				iv.setImageBitmap(bp);
				Matrix matrix = new Matrix();
				matrix.reset();
				matrix.postScale(2.0F, 2.0F);
				iv.setImageMatrix(matrix);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final Bundle bundle = this.getIntent().getExtras();
		menu.add(0, 0, 0, "手工修改数据");
		if( bundle.getBoolean("CB") == true ) {
			menu.add(0, 1, 1, "单表抄数据");
			//menu.add(0, 2, 2, R.string.menu_read_pic);			
			menu.add(0, 3, 3, "读取二值图像");
		}
		return super.onCreateOptionsMenu(menu);
	}

	private Handler picReadingHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			if( msg.what >= 0 ) {
				progressBar.setTitle("读取第"+(msg.what)
						+"个字符");
				progressBar.setMax(msg.arg2);
				progressBar.setProgress(msg.arg1);
			}
			else if( msg.what == -2 ) {
				int imgboxlist[] = {
						R.id.ib_01,
						R.id.ib_02,
						R.id.ib_03,
						R.id.ib_04,
						R.id.ib_05,
						R.id.ib_06,
						R.id.ib_07,
						R.id.ib_08
				};

				ImageView iv = (ImageView) findViewById(imgboxlist[msg.arg1]);
				iv.setImageBitmap((Bitmap) msg.obj);
				Matrix matrix = new Matrix();
				matrix.reset();
				matrix.postScale(2.0F, 2.0F);
				iv.setImageMatrix(matrix);
			}
			else if( msg.what == -3 ) {
				progressBar.hide();
			}
			else if( msg.what == -1 ) {
				//progressBar.hide();
				HintDialog.ShowHintDialog(thisView, (String) msg.obj, "错误");
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		super.onOptionsItemSelected(menuItem);
		switch (menuItem.getItemId()) {
			case 0:
				if( et_NAME.isEnabled() ) {

					Z_PasswordDialog dlg = new Z_PasswordDialog(this,1);
					if( dlg.showDialog() == 1 ) {

						et_NAME.setEnabled(false);
						et_ADDR.setEnabled(false);
						et_SFBH.setEnabled(false);
						et_SBGH.setEnabled(false);
						et_SYCJ.setEnabled(false);
						et_BCCJ.setEnabled(false);
						et_BYYS.setEnabled(false);
						et_CBRQ.setEnabled(false);
						et_SYCBRQ.setEnabled(false);

						// Save database
						try {
							meter.Name = et_NAME.getText().toString();
							meter.Address = et_ADDR.getText().toString();
							//meter.PayNo = et_SFBH.getText().toString();
							meter.Serial = et_SBGH.getText().toString();
							//meter.LastData = Float.valueOf(et_SYCJ.getText().toString());
							meter.Data = Float.valueOf(et_BCCJ.getText().toString());
							meter.WATER = Float.valueOf(et_BYYS.getText().toString());
							//meter.DataTime = et_CBRQ.getText().toString();
							//meter.LastDataTime = et_SYCBRQ.getText().toString();
							int n = MenuActivity.dataSource.updateMeterRecord(meter);
							if( n == 1) {
								HintDialog.ShowHintDialog(this, "更新成功","消息");
							}
							else {
								HintDialog.ShowHintDialog(this, "数据库写入失败","错误");
							}
						}catch(Exception e) {
							HintDialog.ShowHintDialog(this, "更新失败,请检查数据类型是否正确","错误");
						}
						//
						menuItem.setTitle("手工修改数据");
					}
					else {
						HintDialog.ShowHintDialog(thisView, "密码输入错误", "错误");
					}
				}
				else {
					et_NAME.setEnabled(true);
					et_ADDR.setEnabled(true);
					//et_SFBH.setEnabled(true);
					et_SBGH.setEnabled(true);
					//et_SYCJ.setEnabled(true);
					et_BCCJ.setEnabled(true);
					//et_BYYS.setEnabled(true);
					//et_CBRQ.setEnabled(true);
					//et_SYCBRQ.setEnabled(true);
					menuItem.setTitle("保存数据");
				}
				break;
			case 1:	// Single Meter query.

				final Bundle bundle = this.getIntent().getExtras();
				int _id = bundle.getInt("_id", -1);
				if( _id < 0 ) {
					Toast.makeText(getApplicationContext(), "数据库错误", Toast.LENGTH_LONG).show();
					break;
				}

				try {
					meter = MenuActivity.dataSource.updateMeterData(_id);
				} catch (IOException e1) { //IO
					// Log.e("MeterReader.dataSource.updateMeterData", e1.toString());
					finish();
					MenuActivity.uiAct.resetNetwork(thisView);
				}

				if( meter == null ) {
					Toast.makeText(this, "读取失败", Toast.LENGTH_LONG).show();
					break;
				}

				et_BCCJ.setText(String.valueOf(meter.Data));
				et_BYYS.setText(String.valueOf(meter.WATER));

				if( meter.IsFault) {
					tv_SBZT.setText("异常");
				}
			
			/*int errorCode = 0;
			try {
				errorCode = (int) Long.parseLong(rb.SBZT);
				tv_SBZT.setText(NetExp_Builder.getExpMessage(errorCode));
			}
			catch(Exception e) {
			}*/
				break;
			case 2:
			case 3:
			{
				if( menuItem.getItemId() == 2)
					binaryPic = false;
				else if(menuItem.getItemId() == 3)
					binaryPic = true;

				meter.IsBinaryzation = binaryPic;

				if(meter.Images == null) {
					meter.Images = new ArrayList<byte[]>();
				}
				meter.Images.clear();

				String sbgh = meter.Serial;
				final byte addr[] = MenuActivity.netThread.getSBAddr(sbgh);

				progressBar.setCancelable(false);
				progressBar.setMessage("数据读取进度");
				progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressBar.setProgress(0);
				progressBar.setMax(5);
				progressBar.show();

				new Thread(new Runnable() {
					@Override
					public void run() {
						int retrytimes = 3;

						if( binaryPic ) {
							try {
								MenuActivity.netThread.readBinaryPic_Parse1(addr);
							} catch (NetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						for(int i=0;i<5;i++) {
							try {
								Bitmap bp = null;
								byte[] picdata;

								if( binaryPic ) {
									picdata = MenuActivity.netThread.readBinaryPicBytes(addr, i+1);
									if(picdata != null) {
										bp  = MenuActivity.netThread.binaryPicBytesToBitmap(picdata);
									}
								} else {
									picdata = MenuActivity.netThread.readPicBytes(addr, i+1, 3, picReadingHandler);
									if(picdata != null) {
										bp  = BitmapFactory.decodeByteArray(picdata, 0, picdata.length);
									}
								}

								if( bp == null ) {
									if( retrytimes-- > 0) {
										i--;
									} else {
										meter.Images.add(picdata);
										picReadingHandler.obtainMessage(i+1, i + 1, 5).sendToTarget();
									}
								}
								else {
									meter.Images.add(picdata);
									picReadingHandler.obtainMessage(-2, i, 0, bp).sendToTarget();
									picReadingHandler.obtainMessage(i+1, i + 1, 5).sendToTarget();
									retrytimes = 3;
								}
							} catch (NetException e) {
								picReadingHandler.obtainMessage(-1, e.sdesc).sendToTarget();
								continue;
							} catch (IOException e) {
								picReadingHandler.obtainMessage(-1, e.toString()).sendToTarget();
								continue;
							}
						}
						MenuActivity.dataSource.updateMeterRecord(meter);
						picReadingHandler.obtainMessage(-3).sendToTarget();
					}}).start();
			}
			break;
		}
		return true;
	}

}
