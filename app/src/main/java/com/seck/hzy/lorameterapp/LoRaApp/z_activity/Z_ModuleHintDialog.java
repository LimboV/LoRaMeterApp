package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.R;

/**
 *
 * PasswordDialog 类型允许使用类似于C# DialogBox的方式进行密码的阻塞式验证。
 *
 * @author l412
 *
 */
public class Z_ModuleHintDialog extends Dialog {
	int dialogResult;
	String mHintmsg;
	String mTitle;
	Handler mHandler;

	public Z_ModuleHintDialog(Activity context, String hintMessage, String title) {
		super(context);
		mHintmsg = hintMessage;
		mTitle = title;
		this.setTitle(title);
		setOwnerActivity(context);
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
		setContentView(R.layout.z_activity_dialoghint);
		TextView hintBox = (TextView) findViewById(R.id.hintText);
		hintBox.setText(mHintmsg);
		findViewById(R.id.okBtn).setOnClickListener(
				new android.view.View.OnClickListener() {

					@Override
					public void onClick(View paramView) {
						endDialog(1);
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