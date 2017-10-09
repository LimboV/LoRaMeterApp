package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.model.Pcjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.P_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

public class P_XqActivity extends Activity {

	private ListView listView;
	private CheckBox cb;
	private boolean isRead = false;
	private boolean isOver = false;
	private String smsg = ""; // 显示用数据缓存
	private boolean IsRunning2 ;
	public static List<Pcjj> cjjList;
	private int cjjid;
	private int xqid;
	public ProgressDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.p_activity_xq);
		init();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cjj, menu);
		menu.add(0, 3, 0, "返回主界面");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				//				NavUtils.navigateUpFromSameTask(this);
				finish();
				return true;
			case 3:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public List<Pcjj> xqList;

	private List<String> getXq() {
		List<String> data = new ArrayList<String>();
		try {
			xqList = P_DataHelper.getXq();//从数据库提取出
			for (Pcjj cjj : xqList) {
				data.add(cjj.XqName + "(" + cjj.XqId + ")");
			}
		}catch (Exception e){
			HintDialog.ShowHintDialog(this, "未找到数据,请检查数据库文件是否存在！", "错误");
		}
		return data;
	}

	private void init() {
		getIdThread.start();
		P_DataHelper.getdb();//获取数据库实例
//		setupActionBar();//在左上角显示回退按钮
		cb = (CheckBox) findViewById(R.id.cbIsZNDYH);
		loadUser();
		cb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (cb.isChecked()){
					saveIsCheck(true);
				}else{
					saveIsCheck(false);
				}
			}
		});
		Bundle bundle=	getIntent().getExtras();
		isRead = bundle.getBoolean("isRead");
		if (isRead){
			cb.setVisibility(View.GONE);
		}
		listView = (ListView) findViewById(R.id.xqlistView);
		ListAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, getXq()){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return super.getView(position, convertView, parent);
			}
		};
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent;
				IsRunning2 = true;
				if (isRead){
					intent = new Intent(P_XqActivity.this, P_CjjActivity.class);
					intent.putExtra("isRead", true);
					IsRunning2 = false;
					intent.putExtra("xqid", xqList.get(position).XqId);
					startActivity(intent);
				}else{
					if (cb.isChecked()) {
						showProgressDialog();
						/**
						 * 自动识别单元盒ID
						 */
						new Thread(){
							@Override
							public void run() {
								try {
									Thread.sleep(3000);
										Message msg = xHandler.obtainMessage();
										msg.what = 0x99;
										xHandler.sendMessage(msg);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
						isOver = true;
						IsRunning2 = true;
						cjjList = P_DataHelper.getPcjj(xqList.get(position).XqId); //根据小区ID加载采集机信息列表
						xqid = xqList.get(position).XqId;
						smsg ="";
						int i,n;
						try{
							byte[] x = HzyUtils.getHexBytes("6810dddddddddddddd03030a8100");
							byte y = HzyUtils.countSum(x, 0, 14);
							int vvv = y & 0xFF;
							String hv = Integer.toHexString(vvv);
							if (hv.length()<2){
								hv = "0" + hv;
							}
							byte[] bos = HzyUtils.getHexBytes("fefefe6810dddddddddddddd03030a8100" + hv + "16");// 将输入的信息去除前后空格后转化为byte格式
							byte[] bos_new = new byte[bos.length];
							n = 0;
							for (i = 0; i < bos.length; i++) {
								bos_new[n] = bos[i];
								n++;
							}
							BluetoothConnectThread.mmOutStream.write(bos_new);
						}catch(Exception e){
							e.printStackTrace();
						}
					} else {
						intent = new Intent(P_XqActivity.this, P_CjjActivity.class);
						intent.putExtra("isRead", false);
						IsRunning2 = false;
						intent.putExtra("xqid", xqList.get(position).XqId);
						startActivity(intent);
					}

				}

			}
		});
	}

	@Override
	public void onBackPressed() {
		closeProgressDialog();
		finish();
	}

	@Override
	protected void onDestroy() {
		getIdThread.interrupt();
		super.onDestroy();
	}

	/**
	 * 使用SharePreferences记住上次的选择情况
	 */
	public void saveIsCheck(boolean isCheck){
		SharedPreferences.Editor editor = getSharedPreferences("isCheck", MODE_PRIVATE).edit();
		editor.putBoolean("isCheck", isCheck);
		editor.commit();
	}

	public void loadUser(){
		SharedPreferences pref = getSharedPreferences("isCheck", MODE_PRIVATE);
		cb.setChecked(pref.getBoolean("isCheck", false));
	}

	private Thread getIdThread = new Thread(){
		public void  run(){
			int num;
			byte[] buffer = new byte[1024];// 缓存区
			// 接收线程
			int i,n;
			try {
				while (true) {
					if (IsRunning2 && BluetoothConnectThread.mmInStream.available() > 0){
						Thread.sleep(50);
						num = BluetoothConnectThread.mmInStream.read(buffer); // 读入数据
						n = 0;
						byte[] buffer_new = new byte[num];
						for (i = 0; i < num; i++) {
							buffer_new[n] = buffer[i];
							n++;
						}
						String s = HzyUtils.bytes2HexString(buffer_new);
						smsg += s; // 写入接收 缓存
						// 发送显示消息，进行显示刷新
						Message msg = xHandler.obtainMessage();
						msg.obj = smsg;
						msg.what = 0x01;
						xHandler.sendMessage(msg);
					}else {
						Thread.sleep(1000);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	Handler xHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0x99:
					closeProgressDialog();
					break;
				case 0x01:
					smsg = (String) msg.obj;
					smsg = smsg.replaceAll("FE", "");
					if (smsg.length() > 63 && IsRunning2 && isOver){
						IsRunning2 = false;
						String hv,fhv,IDMsg;
						IDMsg = smsg.substring(36,50);
						fhv = smsg.substring(60, 62).toUpperCase();
						/**
						 * 计算校验码
						 */
						byte[] x = HzyUtils.getHexBytes(smsg.substring(32, 60));
						int v = HzyUtils.countSum(x, 0, 14) & 0xFF;
						hv = Integer.toHexString(v);
						if (hv.length() < 2) {
							hv = "0" + hv;
						}
						hv = hv.toUpperCase();
						if(hv.equals(fhv) && isOver){
							StringBuilder sb = new StringBuilder("");
							for (int i = 0; i < 14; i += 2) {
								sb.append(IDMsg.substring(i + 1, i + 2));
								sb.append(IDMsg.substring(i, i + 1));
							}
							sb.reverse();
							IDMsg = sb.toString();
							try{
								cjjid = Integer.parseInt(IDMsg.substring(8, 14));
							}catch (Exception e){
								e.printStackTrace();
							}
							for (Pcjj cjj : cjjList) {
								/*if (isOver == false){
									break;
								}*/
								if (cjj.CjjId % 1000000 == cjjid){
									Intent i = new Intent(P_XqActivity.this, P_MeterActivity.class);
									i.putExtra("xqid", xqid);
									i.putExtra("cjjid", cjj.CjjId);
									isOver = false;
									closeProgressDialog();
									IsRunning2 = false;
									startActivity(i);
//									finish();
								}else{
									isOver = false;
								}
							}
							closeProgressDialog();
						}else{
							isOver = false;
						}

					}

					break;
			}
		}
	};

	/**
	 * 显示进度条
	 */
	public void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(P_XqActivity.this);
			progressDialog.setMessage("正在连接...");
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
		progressDialog = null;
	}
}
