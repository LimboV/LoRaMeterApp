package com.seck.hzy.lorameterapp.LoRaApp.p_activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class P_CameraTestActivity extends Activity {
//	private CameraTestActivity thisView = null;
	private Bitmap rtstr;//用以保存图片
	private Thread getPicThread;
	private ImageView mImageView;
	private ProgressDialog progressDialog;
	private Handler mHandler = new Handler() {//关闭progressDialog的handler
		public void handleMessage(Message msg){
			switch (msg.what){
				case 0x00:
					mImageView.setImageBitmap(rtstr);
					closeProgressDialog();
					Toast.makeText(P_CameraTestActivity.this, "成功获取图片！", Toast.LENGTH_LONG).show();
//					HintDialog.ShowHintDialog(P_CameraTestActivity.this, CjjSettingActivity.Cjj_CB_MSG, "success " + CjjSettingActivity.Cjj_CB_MSG.length()/5);
					break;
				case 0x01:
					closeProgressDialog();
					Toast.makeText(P_CameraTestActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
					Toast.makeText(P_CameraTestActivity.this, "未能获取图片！", Toast.LENGTH_LONG).show();
//					HintDialog.ShowHintDialog(P_CameraTestActivity.this,CjjSettingActivity.Cjj_CB_MSG,"error" + CjjSettingActivity.Cjj_CB_MSG.length()/5);
					break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.p_activity_camera_test);
		// Show the Up button in the action bar.
//		setupActionBar();//在左上角显示回退按钮
		mImageView = (ImageView) findViewById(R.id.imageView1);
		final EditText et_Addr = (EditText)this.findViewById(R.id.EditText_Addr);

		Button bn_send = (Button) this.findViewById(R.id.Button_SendCmd);
		bn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MenuActivity.Cjj_CB_MSG = "";
				if (et_Addr.getText().length() == 0){
					AlertDialog.Builder dialog = new AlertDialog.Builder(P_CameraTestActivity.this);
					dialog.setTitle("请确认总线上只接了一块表。是否继续？");
					dialog.setCancelable(false);
					dialog.setPositiveButton("继续", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								showProgressDialog(P_CameraTestActivity.this, "获取图片中...");
							} catch (Exception e) {
								e.printStackTrace();
							}
							String addr_hand = et_Addr.getText().toString();
							String addr = addr_hand.trim();//去掉前后空格
							if (addr.equals("")) {
								addr = "AAAAAAAAAAAAAA";
							}
							rtstr = null;
							final byte[] baddr = MenuActivity.netThread.getSBAddr(addr);//将addr传入，补全后返回他的byte数组
							getPicThread = new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										rtstr = MenuActivity.netThread.ImageTest(baddr);
										Message message = new Message();
										message.what = 0x00;
										mHandler.sendMessage(message);
									} catch (Exception e) {
										Message message = new Message();
										message.what = 0x01;
										message.obj = e;
										mHandler.sendMessage(message);
										e.printStackTrace();
									}
								}
							});
							getPicThread.start();
						}
					});
					dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
					dialog.show();
				}else{
					try {
						showProgressDialog(P_CameraTestActivity.this, "获取图片中...");
					} catch (Exception e) {
						e.printStackTrace();
					}
					String addr_hand = et_Addr.getText().toString();
					String addr = addr_hand.trim();//去掉前后空格
					rtstr = null;
					final byte[] baddr = MenuActivity.netThread.getSBAddr(addr);//将addr传入，补全后返回他的byte数组
					getPicThread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								rtstr = MenuActivity.netThread.ImageTest(baddr);
								Message message = new Message();
								message.what = 0x00;
								mHandler.sendMessage(message);
							} catch (Exception e) {
								Message message = new Message();
								message.what = 0x01;
								message.obj = e;
								mHandler.sendMessage(message);
								e.printStackTrace();
							}
						}
					});
					getPicThread.start();
				}
			}
		});

		/**
		 * 保存图片
		 */
		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					if (rtstr != null) {
						Calendar c = Calendar.getInstance();
						Date date = c.getTime();
						DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
						saveMyBitmap(df.format(date), rtstr);
					}
				}
			}
		});
	}


	/**
	 * Save Bitmap to a file.保存图片到SD卡。
	 */
	public void saveMyBitmap(String bitName,Bitmap mBitmap){
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckPic");
		if (!file.exists()){
			file.mkdir();
		}
		File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckPic/" + bitName + ".png");
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try{
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			HintDialog.ShowHintDialog(this, "图片保存成功!", "提示");
		}catch (Exception e){
			HintDialog.ShowHintDialog(this, "图片保存失败！", "错误");
		}
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "返回主界面");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
//				NavUtils.navigateUpFromSameTask(this);
				finish();
				return true;
			case 1:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 显示进度条
	 */
	public void showProgressDialog(Context context,String string){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(string);
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度条
	 */
	public void closeProgressDialog(){
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
}
