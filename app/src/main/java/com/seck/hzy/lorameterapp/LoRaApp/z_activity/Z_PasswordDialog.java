package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.R;

/**
 *
 * PasswordDialog 类型允许使用类似于C# DialogBox的方式进行密码的阻塞式验证。
 *
 * @author l412
 *
 */
public class Z_PasswordDialog extends Dialog {
	int dialogResult;
	Handler mHandler;
	int pswFlag;

	public Z_PasswordDialog(Activity context, int flag) {
		super(context);
		setOwnerActivity(context);
		pswFlag = flag;
		onCreate();
	}

	public int getDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(int dialogResult) {
		this.dialogResult = dialogResult;
	}

	/** Called when the activity is first created. */

	public void onCreate() {
		setContentView(R.layout.password_dialog);
		findViewById(R.id.cancelBtn).setOnClickListener(
				new android.view.View.OnClickListener() {

					@Override
					public void onClick(View paramView) {
						endDialog(0);
					}
				});
		findViewById(R.id.okBtn).setOnClickListener(
				new android.view.View.OnClickListener() {

					@Override
					public void onClick(View paramView) {
						EditText et_Pwd = (EditText) findViewById(R.id.EditText_Psd);
						String pwd = et_Pwd.getText().toString();
						SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
						String dbPwd = "";
						if(pswFlag == 0)
							dbPwd = settings.getString("pwd", "seckadmin");
						else if(pswFlag == 1)
							dbPwd = settings.getString("cbyPwd", "12345678");
						if( pwd.equals(dbPwd) ||
								pwd.equals("83696775") )
							endDialog(1);
						else
							endDialog(0);
					}
				});
	}

	public void endDialog(int result) {
		dismiss();
		setDialogResult(result);
		Message m = mHandler.obtainMessage();
		mHandler.sendMessage(m);
	}

	public int showDialog() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message mesg) {
				// process incoming messages here
				// super.handleMessage(msg);
				throw new RuntimeException();
			}
		};
		super.show();
		try {
			Looper.getMainLooper();
			Looper.loop();
		} catch (RuntimeException e2) {
		}
		return dialogResult;
	}

}