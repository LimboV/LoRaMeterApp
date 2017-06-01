package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;

/**
 *
 * 电阻表读取测试界面。
 *
 * @author Guhong.
 *
 */
public class Z_DZBReadActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.z_activity_dzb_read);

		final EditText et_Addr = (EditText)this.findViewById(R.id.EditText_Addr);
		// final String addr_hand = et_Addr.getText().toString();
		final TextView tv_Flow = (TextView)this.findViewById(R.id.TextView_ds);
		final TextView tv_resV1 = (TextView)this.findViewById(R.id.TextView_gw);
		final TextView tv_resV2 = (TextView)this.findViewById(R.id.TextView_sw);
		final TextView tv_resV3 = (TextView)this.findViewById(R.id.TextView_bw);

		SharedPreferences settings =MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);

		final String addr_Broad =
				settings.getString("BADDR", MenuActivity.DEFAULT_BADDR);	// 广播地址

		Button bn_send = (Button) this.findViewById(R.id.Button_SendCmd);
		final Z_DZBReadActivity thisView = this;
		bn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String addr_hand = et_Addr.getText().toString();
				String addr = addr_hand.trim();
				if(addr.equals("")) {
					addr = addr_Broad.trim();
				}

				if(addr.equals("")) {
					Toast.makeText(getApplicationContext(), "请指定发送地址或广播地址!!",
							Toast.LENGTH_LONG).show();
					return;
				}

				byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
				BluetoothConnectThread.ResMeter_Info info;

				try {
					info = MenuActivity.netThread.readResMeter(baddr);
					tv_Flow.setText((float) info.CFlow / 100 +"");
					tv_resV1.setText(info.resV1+"");
					tv_resV2.setText(info.resV2+"");
					tv_resV3.setText(info.resV3+"");
				} catch (NetException e) {
					HintDialog.ShowHintDialog(thisView, e.sdesc, "错误");
				} catch (IOException e) {
					finish();
					MenuActivity.uiAct.resetNetwork(thisView);
				}
			}
		});
	}
}
