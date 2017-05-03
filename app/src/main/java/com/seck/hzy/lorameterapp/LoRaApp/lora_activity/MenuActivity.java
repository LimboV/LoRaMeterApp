package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_CameraTestActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_ChooseActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_GPRSNetActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_PxqActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_UploadActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_XqActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BlueToothActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetworkLayer;
import com.seck.hzy.lorameterapp.LoRaApp.utils.SkDataSource;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_MeterTestActivity;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_XQListActivity;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;

/**
 * Created by ssHss on 2016/7/14.
 */
public class MenuActivity extends Activity implements View.OnClickListener {
    /**
     * 全局变量
     */
    static public BluetoothConnectThread netThread = null;
    public static boolean blutoothEnabled = false;
    public static String METER_STYLE;
    public static String CITY_NAME;
    public static String Cjj_CB_MSG = "";//存放数据
    public static boolean btFlag = false;//蓝牙接受完成标志位
    public static boolean btAuto = false;//蓝牙自动接收标志位
    public static int timeDelayMax = 8000;//超时时间长度
    public static int CMD_DELAY = 500;//指令延时长度ms
    public static int PIC_DELAY = 2000;//图片延时长度ms

    static public SkDataSource dataSource = null;//直读表
    final static public String PREF_NAME = "SK_SETTING";
    final static public String DEFAULT_BADDR = "AAAAAAAAAAAAAA";
    final static public String DEFAULT_SERVER = "www.seck.com.cn:9200";

    public static String[] strAllPath;
    public static String moblieStorgePath = "";
    public static String sDStorgePath = "";
    /**
     * 私有变量
     */
    private GridView mGridView;
    private Button btnExit;
    private TextView tvMeterStyle;
    private ProgressDialog progressBar = null;
    static public MenuActivity uiAct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        //        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_menuactivity);
        progressBar = new ProgressDialog(this);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        uiAct = this;
        strAllPath = HzyUtils.getAllSdPaths(this);
        for (int i = 0; i < strAllPath.length; i++) {
            Log.e("limbo", strAllPath[i]);
        }
        //        moblieStorgePath = strAllPath[0];// 手机
        //        sDStorgePath = strAllPath[1];// sd卡
        //        Log.e("moblieStorgePath", moblieStorgePath + "");
        //        Log.e("strAllPath", strAllPath.length + "");
        //        Log.e("strAllPath[0]", strAllPath[0]);
        //        Log.e("strAllPath[1]", strAllPath[1]);
        Log.e("limbo", Environment.getExternalStorageDirectory().getAbsolutePath());

        /**
         * 按键设置
         */
        btnExit = (Button) findViewById(R.id.menuActivity_btn_exit);
        btnExit.setOnClickListener(this);

        tvMeterStyle = (TextView) findViewById(R.id.menuActivity_tv_meterStyle);
        loadUser();//加载表类型

        mGridView = (GridView) findViewById(R.id.gvMain);
        mGridView.setAdapter(new imageAdapter(this));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i;
                switch (position) {
                    /**
                     * 蓝牙连接及断开
                     */
                    case 0:
                        if (MenuActivity.netThread == null) {
                            MenuActivity.resetNetwork(MenuActivity.this);
                            networkConnect();
                            return;
                        } else {
                            MenuActivity.resetNetwork(MenuActivity.this);
                            HintDialog.ShowHintDialog(MenuActivity.this, "蓝牙连接已断开", "提示");
                            mGridView.setAdapter(new imageAdapter(MenuActivity.this));
                        }
                        break;
                    /**
                     * 表类型选择
                     */
                    case 1:
                        i = new Intent(MenuActivity.this, MeterStyleActivity.class);
                        startActivityForResult(i, 0);
                        break;
                    case 2:
                        if (MenuActivity.netThread == null) {
                            networkConnect();
                            return;
                        } else {
                            if (METER_STYLE.equals("P")) {//P型表水表抄表
                                MenuActivity.timeDelayMax = 7000;
                                i = new Intent(MenuActivity.this, P_XqActivity.class);
                                i.putExtra("isRead", false);
                                startActivity(i);
                            } else if (METER_STYLE.equals("Z")) {//直读表抄表
                                i = new Intent(uiAct, Z_XQListActivity.class);
                                i.putExtra("CB", true);
                                startActivity(i);
                            } else {//LoRa表对表端操作
                                i = new Intent(MenuActivity.this, LoRa_MeterChooseActivity.class);
                                startActivity(i);
                            }
                        }

                        break;
                    case 3:
                        if (MenuActivity.netThread == null) {
                            networkConnect();
                            return;
                        } else {
                            if (METER_STYLE.equals("P")) {//P型表采集机抄表
                                i = new Intent(MenuActivity.this, P_PxqActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("Z")) {//直读表查询
                                i = new Intent(uiAct, Z_XQListActivity.class);
                                i.putExtra("CB", false);
                                startActivity(i);
                            } else {//LoRa表对摄像表操作
                                i = new Intent(MenuActivity.this, LoRa_SxbChooseActivity.class);
                                startActivity(i);
                            }


                        }

                        break;
                    case 4:
                        if (METER_STYLE.equals("P")) {//P型表查询
                            i = new Intent(MenuActivity.this, P_XqActivity.class);
                            i.putExtra("isRead", true);
                            startActivity(i);
                        } else if (METER_STYLE.equals("Z")) {//直读表表参数
                            i = new Intent(uiAct, Z_MeterTestActivity.class);
                            startActivity(i);
                        } else {//LoRa表对采集机操作
                            i = new Intent(MenuActivity.this, LoRa_CjjChooseActivity.class);
                            startActivity(i);
                        }

                        break;
                    case 5:
                        if (MenuActivity.netThread == null) {
                            networkConnect();
                            return;
                        } else {
                            if (METER_STYLE.equals("P")) {//P型表测试
                                i = new Intent(MenuActivity.this, P_CameraTestActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("Z")) {

                            } else {
                                i = new Intent(MenuActivity.this, LoRa_BluetoothUtilsActivity.class);
                                startActivity(i);
                            }


                        }
                        break;
                    case 6:
                        if (MenuActivity.netThread == null) {
                            networkConnect();
                            return;
                        } else {
                            if (METER_STYLE.equals("P")) {//参数设置
                                i = new Intent(MenuActivity.this, P_ChooseActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("Z")) {

                            } else {
                                i = new Intent(MenuActivity.this, LoRa_XqListActivity.class);
                                startActivity(i);
                            }


                        }
                        break;
                    case 7:
                        if (MenuActivity.netThread == null) {
                            networkConnect();
                            return;
                        } else {
                            if (METER_STYLE.equals("P")) {//物联网表设置
                                i = new Intent(MenuActivity.this, P_GPRSNetActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("Z")) {

                            } else {

                            }


                        }
                        break;
                    case 8:
                        if (MenuActivity.netThread == null) {
                            networkConnect();
                        } else {
                            if (METER_STYLE.equals("P")) {//
                                i = new Intent(MenuActivity.this, P_UploadActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("Z")) {

                            } else {

                            }

                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /**
             * exit
             */
            case R.id.menuActivity_btn_exit:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MenuActivity.this);
                dialog.setTitle("请确认是否退出");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MenuActivity.resetNetwork(MenuActivity.this);
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }

    private class imageAdapter extends BaseAdapter {
        Context mContext;
        /**
         * LoRa表
         */
        private String LoRa_Titls[] = {"未连接", "表类型选择", "对表端操作", "对摄像表操作", "对采集机操作", "蓝牙工具", "存入表信息"};
        private String LoRa_Titls2[] = {"已连接", "表类型选择", "对表端操作", "对摄像表操作", "对采集机操作", "蓝牙工具", "存入表信息"};
        /**
         * P型表
         */
        private String P_Titls[] = {"未连接", "表类型选择", "单元盒抄表", "采集机抄表", "查询", "测试", "参数管理", "物联网设置", "采集机升级"};
        private String P_Titls2[] = {"已连接", "表类型选择", "单元盒抄表", "采集机抄表", "查询", "测试", "参数管理", "物联网设置", "采集机升级"};
        /**
         * 直读表
         */
        private String Z_Titls[] = {"未连接", "表类型选择", "查询", "表参数", "测试", "设置", "数据同步", "物联网设置"};
        private String Z_Titls2[] = {"已连接", "表类型选择", "查询", "表参数", "测试", "设置", "数据同步", "物联网设置"};

        private int LoRa_Imgs[] = {R.drawable.pic_bluetoothno, R.drawable.pic_set, R.drawable.pic_meter, R.drawable.pic_sxb, R.drawable.pic_cjjcb,
                R.drawable.pic_bluetoothutils, R.drawable.pic_save};
        private int P_Imgs[] = {R.drawable.pic_bluetoothno, R.drawable.pic_set, R.drawable.pic_cb, R.drawable.pic_cjjcb, R.drawable.pic_meter,
                R.drawable.pic_cb_test, R.drawable.pic_set, R.drawable.pic_gprsnet, R.drawable.pic_download};
        private int Z_Imgs[] = {R.drawable.pic_bluetoothno, R.drawable.pic_set, R.drawable.pic_cb, R.drawable.pic_save, R.drawable.pic_meter,
                R.drawable.pic_preferences, R.drawable.pic_set, R.drawable.pic_isync, R.drawable.pic_gprsnet};


        public imageAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            if (MenuActivity.METER_STYLE.equals("L") || MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("F")) {
                return 7;
            } else if (MenuActivity.METER_STYLE.equals("P")) {
                return 9;
            } else if (MenuActivity.METER_STYLE.equals("Z")) {
                return 8;
            } else {
                return 7;
            }
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View v;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                v = li.inflate(R.layout.lora_startview, null);
                TextView tv = (TextView) v.findViewById(R.id.icon_text);
                ImageView iv = (ImageView) v.findViewById(R.id.icon_image);
                if (MenuActivity.METER_STYLE.equals("L") || MenuActivity.METER_STYLE.equals("W")
                        || MenuActivity.METER_STYLE.equals("F")) {

                    if (MenuActivity.netThread == null) {
                        tv.setText(LoRa_Titls[position]);
                    } else {
                        tv.setText(LoRa_Titls2[position]);
                    }

                    iv.setImageResource(LoRa_Imgs[position]);
                } else if (MenuActivity.METER_STYLE.equals("P")) {
                    if (MenuActivity.netThread == null)
                        tv.setText(P_Titls[position]);
                    else
                        tv.setText(P_Titls2[position]);

                    iv.setImageResource(P_Imgs[position]);
                } else if (MenuActivity.METER_STYLE.equals("Z")) {
                    if (MenuActivity.netThread == null) {
                        tv.setText(Z_Titls[position]);
                    } else {
                        tv.setText(Z_Titls2[position]);
                    }

                    iv.setImageResource(Z_Imgs[position]);
                }

                if (MenuActivity.netThread != null && position == 0)
                    iv.setImageResource(R.drawable.pic_bluetooth);
            } else {
                v = convertView;
            }
            return v;
        }
    }

    @Override
    public void onBackPressed() {
        btnExit.performClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 重新设置网络状态
     */
    public static void resetNetwork(Context cont) {
        if (netThread != null) {
            try {
                netThread.cancel();
            } catch (IOException e) {
            }

            netThread = null;
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
                MenuActivity.resetNetwork(MenuActivity.this);
            } else if (resultCode == BluetoothConnectThread.NETWORK_CONNECTED) {
                HintDialog.ShowHintDialog(this, "蓝牙连接成功", "提示");
                mGridView.setAdapter(new imageAdapter(this));
            } else if (resultCode == BluetoothConnectThread.METER) {
                loadUser();
                mGridView.setAdapter(new imageAdapter(this));
            }
        }
    }

    public void networkConnect() {
        NetworkLayer.adapter = BluetoothAdapter.getDefaultAdapter();
        if (NetworkLayer.adapter != null) {
            MenuActivity.blutoothEnabled = NetworkLayer.adapter.isEnabled();
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

    /**
     * 加载用户信息
     */
    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("meterStyle", MODE_PRIVATE);
        METER_STYLE = pref.getString("meterStyle", "L");
        CITY_NAME = pref.getString("cityName", "YC");
        if (MenuActivity.METER_STYLE.equals("L")) {
            tvMeterStyle.setText("当前表类型为:山科LoRa表");
        } else if (MenuActivity.METER_STYLE.equals("W")) {
            tvMeterStyle.setText("当前表类型为:安美通LoRa表");
        } else if (MenuActivity.METER_STYLE.equals("F")) {
            tvMeterStyle.setText("当前表类型为:安美通Fsk表");
        } else if (MenuActivity.METER_STYLE.equals("P")) {
            tvMeterStyle.setText("当前表类型为:P型摄像表");
        } else if (MenuActivity.METER_STYLE.equals("Z")) {
            tvMeterStyle.setText("当前表类型为:直读表");
        }
    }

    public static void sendCmd(String sendMsg) {
        Log.d("limbo", "sendCmd函数发送:" + sendMsg);
        if (sendMsg.length() % 2 == 1) {
            sendMsg = "0" + sendMsg;
        }
        MenuActivity.Cjj_CB_MSG = "";//清空记录
        byte[] bos = HzyUtils.getHexBytes(sendMsg);
        byte[] bos_new = new byte[bos.length];
        int i, n;
        n = 0;
        for (i = 0; i < bos.length; i++) {
            bos_new[n] = bos[i];
            n++;
        }
        try {
            MenuActivity.netThread.write(bos_new);
        } catch (Exception e) {
            Log.d("limbo", e.toString());
        }
    }

    public static void sendLoRaCmd(String sendMsg) {
        byte[] x = HzyUtils.getHexBytes(sendMsg);
        byte y = HzyUtils.countSum(x, 0, sendMsg.length() / 2);
        int vvv = y & 0xFF;
        String hv = Integer.toHexString(vvv);
        if (hv.length() < 2) {
            hv = "0" + hv;
        }
        byte[] bos = HzyUtils.getHexBytes("fe" + sendMsg + hv + "16");
        byte[] bos_new = new byte[bos.length];
        int i, n;
        n = 0;
        for (i = 0; i < bos.length; i++) {
            bos_new[n] = bos[i];
            n++;
        }
        Log.d("limbo", HzyUtils.bytes2HexString(bos_new));
        try {
            MenuActivity.netThread.write(bos_new);
        } catch (Exception e) {
            Log.d("limbo", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 2, 2, "网络状态");
        menu.add(0, 3, 3, "关于");
        menu.add(0, 4, 4, "退出");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()) {
            case 2:
                if (MenuActivity.netThread == null) {
                    HintDialog.ShowHintDialog(this, "蓝牙未连接", "状态");
                } else {
                    HintDialog.ShowHintDialog(this, "蓝牙已连接", "状态");
                }
                break;
            case 3:
                showAbout();
                break;
            case 4:
                finish();
                break;
        }
        return true;
    }

    public void showAbout() {

        String aboutText = "版本号 : " + this.getAppVersionName(this) + "\r\n" +
                "Copyright (C) 1999 - 2014\r\nSeck (Hangzhou)";

        new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(aboutText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                }).show();
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);

            versionName = pi.versionName;

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }

        return versionName;
    }

    public static int getInt(String name) {
        SharedPreferences settings = uiAct.getSharedPreferences(
                MenuActivity.PREF_NAME, 0);
        return settings.getInt(name, 500); // 通信延时
    }
}
