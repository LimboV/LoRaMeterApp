package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.P_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.model.PmeterUser;
import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BlueToothActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetworkLayer;
import com.seck.hzy.lorameterapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class P_MeterActivity extends Activity {

    private ListView listView;
    private int xqid, cjjid;
    private boolean isRead;
    private List<PmeterUser> meterList;
    public static boolean blutoothEnabled = false;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private String from[] = new String[]{"meterid", "data", "iamge"};
    private int to[] = new int[]{R.id.meterId, R.id.data, R.id.imageView1};
    private SimpleAdapter listItemAdapter;
    private Button cbbutton, finishcbbutton;
    private ProgressDialog progressDialog;
    private boolean isBreak = true;//线程是否进入
    private int position_selection = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            cbbutton.setVisibility(View.VISIBLE);
            finishcbbutton.setVisibility(View.GONE);
            isBreak = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_meter);
//        setupActionBar();
//        MainActivity.IS_TEST = true;
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        isRead = bundle.getBoolean("isRead");
        /*if (MainActivity.isStarted == false && !isRead) {
            MainActivity.netThread.start();
            MainActivity.isStarted = true;
        }*/
        meterList = P_DataHelper.getMeters(xqid, cjjid);//从数据库中获取列表
        init();
        getList();
        listItemAdapter = new SimpleAdapter(this, listItem, R.layout.list_item, from, to);
        listItemAdapter.setViewBinder(new ListViewBinder());
        listView.setAdapter(listItemAdapter);
    }

    private class ListViewBinder implements ViewBinder {
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if ((view instanceof ImageView) && (data instanceof Bitmap)) {
                ImageView imageView = (ImageView) view;
                Bitmap bmp = (Bitmap) data;
                imageView.setImageBitmap(bmp);
                return true;
            }
            return false;
        }

    }

    public void getList() {
        listItem.clear();
        meterList = P_DataHelper.getMeters(xqid, cjjid);
        for (int i = 0; i < meterList.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("meterid", "表ID:" + meterList.get(i).MeterNumber + "\n抄表时间:" + meterList.get(i).Date);
            map.put("data", "用户地址：" + meterList.get(i).UserAddr);
            map.put("iamge", meterList.get(i).Pic);
            listItem.add(map);
        }
        //		return listItem;
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
                if (isBreak) {
                    finish();
                }
                return true;
            case 1:
                //				NavUtils.navigateUpFromSameTask(this);
                if (isBreak) {
                    Intent intent = new Intent(P_MeterActivity.this, MenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    P_MeterActivity.this.startActivity(intent);
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        listView = (ListView) findViewById(R.id.meterlistView);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isBreak && !isRead) {
                    HashMap<String, Object> map = listItem.get(position);
                    Bitmap pic = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    //					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date curDateTime = new Date(System.currentTimeMillis());
                    try {
                        pic = getimagebyid(meterList.get(position).MeterNumber);
                    } catch (Exception e) {

                    }
                    map.put("iamge", pic);
                    P_DataHelper.saveMeter(xqid, cjjid, meterList.get(position).MeterId, pic, null, sdf.format(curDateTime));
                    listItemAdapter.notifyDataSetChanged();
                }
            }
        });

        cbbutton = (Button) this.findViewById(R.id.cbbutton);
        cbbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (MenuActivity.netThread == null) {
                    networkConnect();
                } else {
                    position_selection = 0;
                    isBreak = false;
                    cbbutton.setVisibility(View.GONE);
                    finishcbbutton.setVisibility(View.VISIBLE);
                    new AddImageTask().execute();
                }
            }
        });
        finishcbbutton = (Button) this.findViewById(R.id.finishcbbutton);
        finishcbbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                isBreak = true;
                cbbutton.setVisibility(View.VISIBLE);
                finishcbbutton.setVisibility(View.GONE);
            }
        });
        finishcbbutton.setVisibility(View.GONE);
        if (isRead) {
            cbbutton.setVisibility(View.GONE);
        }
    }

    public Bitmap getimagebyid(String addr_hand) {
        String addr = addr_hand.trim();
        if (addr.equals("")) {
            addr = "AAAAAAAAAAAAAA";
        }
        byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
        Bitmap rtstr = null;
        try {
            rtstr = MenuActivity.netThread.ImageTest(baddr);
        } catch (Exception e) {
            e.printStackTrace();
            //			 Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return rtstr;
    }

    private class AddImageTask extends AsyncTask<Void, HashMap<String, Object>, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            listItem.clear();
            meterList = P_DataHelper.getMeters(xqid, cjjid);
            for (int i = 0; (i < meterList.size()) && !isBreak; i++) {
                //				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date curDateTime = new Date(System.currentTimeMillis());
                HashMap<String, Object> map = new HashMap<>();
                map.put("meterid", "表ID:" + meterList.get(i).MeterNumber + "	\n抄表时间:" + sdf.format(curDateTime));
                map.put("data", "用户地址：" + meterList.get(i).UserAddr);
                Bitmap pic = null;

                try {
                    pic = getimagebyid(meterList.get(i).MeterNumber);
                    Log.d("tag", "第一次抄表");
                    if (pic == null) {
                        pic = getimagebyid(meterList.get(i).MeterNumber);
                        Log.d("tag", "第二次抄表");

                    }
                } catch (Exception e) {

                }
                map.put("iamge", pic);
                P_DataHelper.saveMeter(xqid, cjjid, meterList.get(i).MeterId, pic, null, sdf.format(curDateTime));
                publishProgress(map);
            }
            Message message = new Message();
            mHandler.sendMessage(message);
            return null;
        }

        protected void onProgressUpdate(HashMap<String, Object>... values/* 参数2 */) {
            listItem.add(values[0]);
            listItemAdapter.notifyDataSetChanged();
            listView.setSelection(position_selection);
            position_selection++;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            /*
			 * final String[] menuItems = new String[] { "1. 抄    表",
			 * "2. 查    询", "3. 其他选项", "4. 参数设置" };
			 */
            if (resultCode == BluetoothConnectThread.NETWORK_FAILED) {
                HintDialog.ShowHintDialog(this, "设备连接错误", "错误");
            } else if (resultCode == BluetoothConnectThread.NETWORK_CONNECTED) {
                HintDialog.ShowHintDialog(this, "蓝牙连接成功", "提示");
            }
        }
    }

    protected void networkConnect() {
        NetworkLayer.adapter = BluetoothAdapter.getDefaultAdapter();
        if (NetworkLayer.adapter != null) {
            blutoothEnabled = NetworkLayer.adapter.isEnabled();
        }
        Toast.makeText(getApplicationContext(), "正在查询蓝牙设备", Toast.LENGTH_LONG).show();
        if (NetworkLayer.adapter == null) {
            HintDialog.ShowHintDialog(this, "无法获得蓝牙设备", "系统错误");
        } else {
            NetworkLayer.adapter.enable();
            while (NetworkLayer.adapter.getState() != BluetoothAdapter.STATE_ON) {
                Log.v("BluetoothEvent", " 等待蓝牙开启");
                try {
                    Thread.sleep(2000);//两秒后再次检测
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(this, BlueToothActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (isBreak) {
            finish();
        }
    }


    /*@Override
    protected void onDestroy() {
        if (MainActivity.netThread == null) {
            MainActivity.IS_TEST = false;
            super.onDestroy();
        } else {
            MainActivity.IS_TEST = false;
            MainActivity.netThread.interrupt();
            super.onDestroy();
        }

    }*/
}
