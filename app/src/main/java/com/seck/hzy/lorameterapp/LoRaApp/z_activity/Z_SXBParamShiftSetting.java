package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

public class Z_SXBParamShiftSetting extends Activity {

	public static final int[] DEFAULT_SHIFT = {100, 100, 0, 100, 100, 60, 60, 60};

	private int[] mShift = new int[8];
	private List<EditText> mShiftView = new ArrayList<EditText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.z_activity_sxb_shift_set);

		int resIds[] = {
				R.id.shift_01,
				R.id.shift_02,
				R.id.shift_03,
				R.id.shift_04,
				R.id.shift_05,
				R.id.shift_06,
				R.id.shift_07,
				R.id.shift_08
		};
		SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
		for(int i=0; i< mShift.length; i++) {
			mShift[i] = settings.getInt("SHIFT" + i, DEFAULT_SHIFT[i]);
			EditText et = (EditText) findViewById(resIds[i]);
			et.setText(String.valueOf(mShift[i]));
			mShiftView.add(et);
		}
	}

	@Override
	public void onBackPressed() {
		for(int i=0; i<mShiftView.size(); i++) {
			try {
				String str = mShiftView.get(i).getText().toString().trim();
				mShift[i] = Integer.valueOf(str);
			} catch (Exception e) {
				Toast.makeText(this, "相对位移格式错误" + i+1, Toast.LENGTH_LONG).show();
				return;
			}
		}

		int index = -1;
		for(int i=0; i< mShift.length; i++) {
			if(mShift[i] == 0) {
				index = i;
				break;
			}
		}

		if(index < 0) {
			Toast.makeText(this, "未设置参考字轮", Toast.LENGTH_LONG).show();
			return;
		}

		SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
		SharedPreferences.Editor edit = settings.edit();
		for (int i = 0; i < mShift.length; i++) {
			edit.putInt("SHIFT" + i, mShift[i]);
		}
		edit.commit();

		Intent intent = new Intent();
		intent.putExtra("center", index);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

}
