package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;

/*
 * 
 * ComtestActivity为通讯测试界面，将接受的数据回至TextView_recv中。
 * 
 */
public class Z_ComtestActivity extends Activity {
	private Z_ComtestActivity thisView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.z_activity_comtest);

		thisView = this;

		final EditText et_Addr = (EditText)this.findViewById(R.id.EditText_Addr);
		// final String addr_hand = et_Addr.getText().toString();
		final TextView tv_Rstr = (TextView)this.findViewById(R.id.TextView_recv);

		SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);

		final String addr_Broad =
				settings.getString("BADDR", MenuActivity.DEFAULT_BADDR);	// 广播地址

		Button bn_send = (Button) this.findViewById(R.id.Button_SendCmd);
		bn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String addr_hand = et_Addr.getText().toString();
				String addr = addr_hand.trim();
				if(addr.equals("")) {
					addr = addr_Broad.trim();
				}

				if(addr.equals("")) {
					Toast.makeText(thisView,  "请指定发送地址或广播地址",
							Toast.LENGTH_LONG).show();
					return;
				}

				byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
				String rtstr = "";
				try {
					rtstr = MenuActivity.netThread.comTest(baddr);
				} catch (InterruptedException e) {
					Toast.makeText(thisView,  e.toString(),
							Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					finish();
					MenuActivity.uiAct.resetNetwork(thisView);
				} catch (NetException e) {
					Toast.makeText(thisView,  e.toString(),
							Toast.LENGTH_LONG).show();
				}
				String tvRtstr = (String) tv_Rstr.getText();
				tvRtstr += "\n";
				tvRtstr += rtstr;
				tv_Rstr.setText(tvRtstr);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "清空接收区");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		super.onOptionsItemSelected(menuItem);
		switch (menuItem.getItemId()) {
			case 0:
				TextView tv_Rstr = (TextView)this.findViewById(R.id.TextView_recv);
				tv_Rstr.setText("");
				break;
		}
		return true;
	}

}
