package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;

/**
 *
 * 水表控制页面，允许输入表地址设置等内容。
 *
 * @author l412
 *
 */
public class Z_SBControlActivity extends Activity {

	private EditText et_Addr = null;
	private EditText et_IpAddr = null;
	private EditText et_IpCommAddr = null;
	private EditText et_ApnAddr = null;
	private TextView tv_VerNo = null;
	//private TextView tv_Flow = null;

	private String addr_Broad = "";

	private Z_SBControlActivity thisView = null;

	private int checkPsw() {
		Z_PasswordDialog pswdlg = new Z_PasswordDialog(thisView,0);
		int rtn = pswdlg.showDialog();
		if( rtn != 1 )
		{
			HintDialog.ShowHintDialog(thisView, "密码输入错误","错误");
		}
		return rtn;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.z_activity_sbcontrol);

		thisView = this;
		et_Addr = (EditText)this.findViewById(R.id.EditText_Addr);
		tv_VerNo = (TextView)this.findViewById(R.id.TextView_VerNo);
		//tv_Flow = (TextView)this.findViewById(R.id.TextView_Flow);

		SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
		addr_Broad = settings.getString("BADDR", MenuActivity.DEFAULT_BADDR);	// 广播地址
		//et_Addr.setText(addr_Broad);

		final Button bn_readVer = (Button) findViewById(R.id.Button_ReadVer);
		bn_readVer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String addr = et_Addr.getText().toString().trim();
				if(addr.equals("")) {
					addr = addr_Broad.trim();
				}

				byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
				String verNo;
				try {
					verNo = MenuActivity.netThread.readVerNo(baddr);
					tv_VerNo.setText(verNo);
				} catch (NetException e) {
					HintDialog.ShowHintDialog(thisView, e.sdesc,"错误");
				} catch (IOException e) {
					// HintDialog.ShowHintDialog(thisView, e.toString(), "错误");
					finish();
					MenuActivity.uiAct.resetNetwork(thisView);
				}
			}
		});

		final Button bn_readAddr = (Button) findViewById(R.id.Button_ReadAddr);
		bn_readAddr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String addr = addr_Broad.trim();
				byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
				try {
					baddr = MenuActivity.netThread.readAddr(baddr);
					String addr_s = HzyUtils.bytes2HexString(baddr);
					et_Addr.setText(addr_s);
				} catch (NetException e) {
					HintDialog.ShowHintDialog(thisView, e.sdesc, "错误");
				} catch (IOException e) {
					finish();
					MenuActivity.uiAct.resetNetwork(thisView);
				}
			}
		});

		final Button bn_writeAddr = (Button) findViewById(R.id.Button_WriteAddr);
		bn_writeAddr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(thisView);
				final EditText et_NewAddr = new EditText(thisView);

				/*PasswordDialog pswdlg = new PasswordDialog(this,0);
				if( pswdlg.showDialog() != 1 ) 
				{
					HintDialog.ShowHintDialog(this, "密码输入错误", "错误");
					return;
				}//*/
				/*if(checkPsw() != 1) {
					return;
				}*/
				builder.setTitle("请输入新表地址");
				builder.setView(et_NewAddr);
				builder.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String addr = et_Addr.getText().toString().trim();
						if(addr.equals("")) {
							addr = addr_Broad.trim();
						}

						byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
						byte[] newaddr = MenuActivity.netThread.getSBAddr(et_NewAddr.getText().toString().trim());

						try {
							newaddr = MenuActivity.netThread.writeAddr(baddr, newaddr);
							//String addr_s = NetConnectThread.bytesToHexString(newaddr);
							HintDialog.ShowHintDialog(thisView, "写地址成功","提示");
						} catch (NetException e) {
							HintDialog.ShowHintDialog(thisView, e.sdesc,"错误");
						} catch (IOException e) {
							finish();
							MenuActivity.uiAct.resetNetwork(thisView);
						}
					}
				});
				builder.setNegativeButton("Cancel", null);
				builder.show();
			}
		});

	}

    /*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "读地址");
		menu.add(0, 1, 1, "写地址");
		menu.add(0, 2, 2, "读版本号");
		//menu.add(0, 3, 3, "读数据");
		//menu.add(0, 4, 4, "读图像");
		return super.onCreateOptionsMenu(menu);
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		super.onOptionsItemSelected(menuItem);
		switch (menuItem.getItemId()) {
		case 0: {
			String addr = addr_Broad.trim();
			byte[] baddr = MeterReader.netThread.getSBAddr(addr);
			try {
				baddr = MeterReader.netThread.readAddr(baddr);
				String addr_s = NetConnectThread.bytesToHexString(baddr);
		        et_Addr.setText(addr_s);
			} catch (NetException e) {
				HintDialog.ShowHintDialog(this, e.sdesc, "错误");
			} catch (IOException e) {
				HintDialog.ShowHintDialog(this, e.toString(), "错误");
			}
			}
	        break;
		case 1: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final EditText et_NewAddr = new EditText(this);
			
			builder.setTitle("请输入新表地址");
			builder.setView(et_NewAddr);
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					String addr = et_Addr.getText().toString().trim();
					if(addr.equals("")) {
						addr = addr_Broad.trim();
					}
					
					byte[] baddr = MeterReader.netThread.getSBAddr(addr);
					byte[] newaddr = MeterReader.netThread.getSBAddr(et_NewAddr.getText().toString().trim());
					
					try {
						newaddr = MeterReader.netThread.writeAddr(baddr, newaddr);
						//String addr_s = NetConnectThread.bytesToHexString(newaddr);
						HintDialog.ShowHintDialog(thisView, "写地址成功", "提示");
					} catch (NetException e) {
						HintDialog.ShowHintDialog(thisView, e.sdesc, "错误");
					} catch (IOException e) {
						HintDialog.ShowHintDialog(thisView, e.toString(), "错误");
					}

				}
			});
			builder.setNegativeButton("取消", null);
			builder.show();
		}
	        break;
		case 2: {
			String addr = et_Addr.getText().toString().trim();
			if(addr.equals("")) {
				addr = addr_Broad.trim();
			}
			
			byte[] baddr = MeterReader.netThread.getSBAddr(addr);
			String verNo;
			try {
				verNo = MeterReader.netThread.readVerNo(baddr);
				tv_VerNo.setText(verNo);
			} catch (NetException e) {
				HintDialog.ShowHintDialog(this, e.sdesc, "错误");
			} catch (IOException e) {
				HintDialog.ShowHintDialog(this, e.toString(), "错误");
			}
			}
		
			break;
		case 3: {
	        final String addr_Broad = et_Addr.getText().toString();
			String addr = addr_Broad.trim();
			byte[] baddr = MeterReader.netThread.getSBAddr(addr);
			Long flow = (long) 0;
			try {
				flow = MeterReader.netThread.readMeterCFlow(baddr, 1);
				tv_Flow.setText(flow+"");
			} catch (NetException e) {
				HintDialog.ShowHintDialog(this, e.sdesc, "错误");
			} catch (IOException e) {
				HintDialog.ShowHintDialog(this, e.toString(), "错误");
			}
			}
	        break;
		}
		return true;
	}
	*/
}
