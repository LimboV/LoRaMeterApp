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
import android.support.v4.app.ActivityCompat;
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

import com.seck.hzy.lorameterapp.LoRaApp.ns_activity.NS_XqListActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_CameraTestActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_ChooseActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_GPRSNetActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_LoadMeterIdXqListActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_PxqActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_UploadActivity;
import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_XqActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BlueToothActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetworkLayer;
import com.seck.hzy.lorameterapp.LoRaApp.utils.SkDataSource;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_DataSyncActivity;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_GPRSNetActivity;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_LoadXqListActivity;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_MeterTestActivity;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_OtherCommandsActivity;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_PasswordDialog;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_Setting1Activity;
import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_XQListActivity;
import com.seck.hzy.lorameterapp.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by ssHss on 2016/7/14.
 */
public class MenuActivity extends Activity {
    /**
     * 全局变量
     */
    static public String SECK_PARAM = "0";
    static public BluetoothConnectThread netThread = null;
    public static boolean blutoothEnabled = false, TIMEOUT = false;
    public static String METER_STYLE;
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
    @BindView(R.id.gvMain)
    GridView gvMain;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.menuActivity_btn_exit)
    Button menuActivity_btn_exit;

    @BindView(R.id.menuActivity_tv_meterStyle)
    TextView menuActivity_tv_meterStyle;

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
        AndPermission.with(this)
                .requestCode(300)
                .permission(Permission.CONTACTS)
                .permission(Permission.LOCATION)
                .permission(Permission.CAMERA)
                .permission(Permission.STORAGE)
                .callback(this)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(MenuActivity.this, rationale).show();
                    }
                })
                .start();
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(MenuActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE");
            int permission2 = ActivityCompat.checkSelfPermission(MenuActivity.this, "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(MenuActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int flag = copyFilesFassets(MenuActivity.this, "LoRaDb.db", Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckLoRaDB");
        switch (flag) {
            case 0://完成
                Toast.makeText(MenuActivity.this, "DB文件创建完成", Toast.LENGTH_LONG).show();
                break;
            case 1://不存在
                Toast.makeText(MenuActivity.this, "SD卡不存在", Toast.LENGTH_LONG).show();
                break;
            case 2://报错
                Toast.makeText(MenuActivity.this, "DB文件创建失败", Toast.LENGTH_LONG).show();
                break;
            case 3://已存在
                Toast.makeText(MenuActivity.this, "DB文件已存在", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        ButterKnife.bind(this);
        textView.setText("软件版本：" + this.getAppVersionName(this).substring(this.getAppVersionName(this).indexOf(".") + 1));
        progressBar = new ProgressDialog(this);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        uiAct = this;
        strAllPath = HzyUtils.getAllSdPaths(this);
        dataSource = new SkDataSource(this);
        /**
         * 按键设置
         */

        menuActivity_btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("请确认是否要退出?")
                        .setCancelText("取消")
                        .setConfirmText("确认")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Log.d("limboV", "sure");
                                sweetAlertDialog.cancel();
                                finish();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Log.d("limboV", "cancel");
                                new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("取消!")
                                        .setContentText("你已经取消退出!")
                                        .show();
                                sDialog.cancel();
                            }
                        }).show();
            }
        });

        loadUser();//加载表类型

        //UpdateBuilder builder = UpdateBuilder.create();
        //        builder.updateDialogCreator(new CustomNeedUpdateCreator());
        //        builder.downloadDialogCreator(new NotificationDownloadCreator());
        //        builder.check();

        gvMain.setAdapter(new imageAdapter(this));
        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                            gvMain.setAdapter(new imageAdapter(MenuActivity.this));
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
                        if (MenuActivity.netThread == null && !METER_STYLE.equals("NS")) {
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
                            } else if (METER_STYLE.equals("L") || METER_STYLE.equals("W") || METER_STYLE.equals("F") || METER_STYLE.equals("JY")) {//LoRa表对表端操作
                                i = new Intent(MenuActivity.this, LoRa_MeterChooseActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("CS")) {//超声波表
                                i = new Intent(uiAct, LoRa_MeterChooseActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("NS")) {//数字状态表
                                i = new Intent(uiAct, NS_XqListActivity.class);
                                startActivity(i);
                            } else {

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
                            } else if (METER_STYLE.equals("L") || METER_STYLE.equals("W") || METER_STYLE.equals("F") || METER_STYLE.equals("JY")) {//LoRa表对摄像表操作
                                i = new Intent(MenuActivity.this, LoRa_SxbChooseActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("CS")) {//超声波表
                                i = new Intent(uiAct, LoRa_SxbChooseActivity.class);
                                startActivity(i);
                            } else {

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
                        } else if (METER_STYLE.equals("L") || METER_STYLE.equals("W") || METER_STYLE.equals("F") || METER_STYLE.equals("JY")) {//LoRa表对采集机操作
                            i = new Intent(MenuActivity.this, LoRa_CjjChooseActivity.class);
                            startActivity(i);
                        } else if (METER_STYLE.equals("CS")) {//超声波表
                            i = new Intent(uiAct, LoRa_CjjChooseActivity.class);
                            startActivity(i);
                        } else {

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
                            } else if (METER_STYLE.equals("Z")) {//测试
                                i = new Intent(uiAct, Z_OtherCommandsActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("L") || METER_STYLE.equals("W") || METER_STYLE.equals("F") || METER_STYLE.equals("JY")) {//LoRa表对采集机操作
                                i = new Intent(MenuActivity.this, LoRa_BluetoothUtilsActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("CS")) {//超声波表
                                i = new Intent(uiAct, LoRa_BluetoothUtilsActivity.class);
                                startActivity(i);
                            } else {

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
                            } else if (METER_STYLE.equals("Z")) {//设置
                                i = new Intent(uiAct, Z_Setting1Activity.class);
                                startActivity(i);
                            }//lora表数据库文件
                            else if (METER_STYLE.equals("L") || METER_STYLE.equals("W") || METER_STYLE.equals("F") || METER_STYLE.equals("JY")) {//LoRa表对采集机操作
                                i = new Intent(MenuActivity.this, LoRa_XqListActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("CS")) {//超声波表
                                i = new Intent(uiAct, LoRa_XqListActivity.class);
                                startActivity(i);
                            } else {

                            }


                        }
                        break;
                    case 7:
                        if (METER_STYLE.equals("P")) {//物联网表设置
                            if (MenuActivity.netThread == null) {
                                networkConnect();
                                return;
                            } else {
                                i = new Intent(MenuActivity.this, P_GPRSNetActivity.class);
                                startActivity(i);
                            }

                        } else if (METER_STYLE.equals("Z")) {//数据同步
                            i = new Intent(uiAct, Z_DataSyncActivity.class);
                            startActivity(i);
                        }  //lora分采数据库文件创建及导入
                        else if (METER_STYLE.equals("L") || METER_STYLE.equals("W") || METER_STYLE.equals("F") || METER_STYLE.equals("JY")) {//LoRa表对采集机操作
                            i = new Intent(MenuActivity.this, LoRa_AddFcXqListActivity.class);
                            startActivity(i);
                        } else if (METER_STYLE.equals("CS")) {//超声波表
                            i = new Intent(uiAct, LoRa_AddFcXqListActivity.class);
                            startActivity(i);
                        } else {

                        }

                        break;
                    case 8:
                        if (MenuActivity.netThread == null) {
                            networkConnect();
                        } else {
                            if (METER_STYLE.equals("P")) {//P型表--采集机升级
                                i = new Intent(MenuActivity.this, P_UploadActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("Z")) {//直读表--物联网设置
                                i = new Intent(uiAct, Z_GPRSNetActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("L") || METER_STYLE.equals("W") || METER_STYLE.equals("F") || METER_STYLE.equals("JY")) {//LoRa表对采集机操作
                                //统一平台
                                i = new Intent(MenuActivity.this, LoRa_TYPTActivity.class);
                                startActivity(i);
                            } else if (METER_STYLE.equals("CS")) {//超声波表
                                HintDialog.ShowHintDialog(MenuActivity.this, "只有集中器可使用", "提示");
                                //                                i = new Intent(uiAct, CS_SettingActivity.class);
                                //                                startActivity(i);
                            } else {

                            }


                        }
                        break;
                    /**
                     *
                     */
                    case 9:
                        if (METER_STYLE.equals("P")) {//P型表--现场表ID导入
                            i = new Intent(MenuActivity.this, P_LoadMeterIdXqListActivity.class);
                            startActivity(i);


                        } else if (METER_STYLE.equals("Z")) {//直读表--现场表ID导入
                            i = new Intent(MenuActivity.this, Z_LoadXqListActivity.class);
                            startActivity(i);
                        } else {
                            i = new Intent(MenuActivity.this, LoRa_CbXqListActivity.class);
                            startActivity(i);
                        }
                        break;
                }
            }
        });

    }


    private class imageAdapter extends BaseAdapter {
        Context mContext;
        /**
         * LoRa表
         */
        private String LoRa_Titls[] = {"未连接", "表类型选择", "对表端操作", "对摄像表操作", "对采集机操作", "蓝牙工具", "存入表信息", "分采导入", "统一平台", "Lora小区抄表(未完成)"};
        private String LoRa_Titls2[] = {"已连接", "表类型选择", "对表端操作", "对摄像表操作", "对采集机操作", "蓝牙工具", "存入表信息", "分采导入", "统一平台", "Lora小区抄表(未完成)"};

        /**
         * P型表
         */
        private String P_Titls[] = {"未连接", "表类型选择", "单元盒抄表", "采集机抄表", "查询", "测试", "参数管理", "物联网设置", "采集机升级", "表ID导入", "检查更新"};
        private String P_Titls2[] = {"已连接", "表类型选择", "单元盒抄表", "采集机抄表", "查询", "测试", "参数管理", "物联网设置", "采集机升级", "表ID导入", "检查更新"};
        /**
         * 直读表
         */
        private String Z_Titls[] = {"未连接", "表类型选择", "抄表", "查询", "表参数", "测试", "设置", "数据同步", "物联网设置", "表ID导入", "检查更新"};
        private String Z_Titls2[] = {"已连接", "表类型选择", "抄表", "查询", "表参数", "测试", "设置", "数据同步", "物联网设置", "表ID导入", "检查更新"};
        /**
         * 超声水表
         */
        private String CS_Titls[] = {"未连接", "表类型选择", "对表端操作", "对摄像表操作", "对采集机操作", "蓝牙工具", "存入表信息", "分采导入", "测试"};
        private String CS_Titls2[] = {"已连接", "表类型选择", "对表端操作", "对摄像表操作", "对采集机操作", "蓝牙工具", "存入表信息", "分采导入", "测试"};
        /**
         * 数字状态表
         */
        private String NS_Titls[] = {"未连接", "表类型选择", "抄表"};
        private String NS_Titls2[] = {"已连接", "表类型选择", "抄表"};

        private int LoRa_Imgs[] = {R.drawable.pic_bluetoothno, R.drawable.pic_set, R.drawable.pic_meter, R.drawable.pic_sxb, R.drawable.pic_cjjcb,
                R.drawable.pic_bluetoothutils, R.drawable.pic_save, R.drawable.fcicon, R.drawable.lora_pic_cjj, R.drawable.pic_meter};
        private int P_Imgs[] = {R.drawable.pic_bluetoothno, R.drawable.pic_set, R.drawable.pic_cb, R.drawable.pic_cjjcb, R.drawable.pic_meter,
                R.drawable.pic_cb_test, R.drawable.pic_set, R.drawable.pic_gprsnet, R.drawable.pic_download, R.drawable.pic_meter, R.drawable.updater};
        private int Z_Imgs[] = {R.drawable.pic_bluetoothno, R.drawable.pic_set, R.drawable.pic_cb, R.drawable.pic_save, R.drawable.pic_meter,
                R.drawable.pic_preferences, R.drawable.pic_set, R.drawable.pic_isync, R.drawable.pic_gprsnet, R.drawable.pic_meter, R.drawable.updater};
        private int CS_Imgs[] = {R.drawable.pic_bluetoothno, R.drawable.pic_set, R.drawable.pic_meter, R.drawable.pic_sxb, R.drawable.pic_cjjcb,
                R.drawable.pic_bluetoothutils, R.drawable.pic_save, R.drawable.fcicon, R.drawable.pic_set};
        private int NS_Imgs[] = {R.drawable.pic_bluetoothno, R.drawable.pic_set, R.drawable.pic_meter};


        public imageAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            if (MenuActivity.METER_STYLE.equals("L") || MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("F")) {
                return 10;
            } else if (MenuActivity.METER_STYLE.equals("JY")) {
                return 8;
            } else if (MenuActivity.METER_STYLE.equals("P")) {
                return 10;
            } else if (MenuActivity.METER_STYLE.equals("Z")) {
                return 10;
            } else if (MenuActivity.METER_STYLE.equals("CS")) {
                return 9;
            } else if (MenuActivity.METER_STYLE.equals("NS")) {
                return 3;
            } else {
                return 9;
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
                        || MenuActivity.METER_STYLE.equals("F") || MenuActivity.METER_STYLE.equals("JY")) {

                    if (MenuActivity.netThread == null) {
                        tv.setText(LoRa_Titls[position]);
                    } else {
                        tv.setText(LoRa_Titls2[position]);
                    }

                    iv.setImageResource(LoRa_Imgs[position]);
                } else if (MenuActivity.METER_STYLE.equals("P")) {
                    if (MenuActivity.netThread == null) {
                        tv.setText(P_Titls[position]);
                    } else {
                        tv.setText(P_Titls2[position]);
                    }
                    iv.setImageResource(P_Imgs[position]);
                } else if (MenuActivity.METER_STYLE.equals("Z")) {
                    if (MenuActivity.netThread == null) {
                        tv.setText(Z_Titls[position]);
                    } else {
                        tv.setText(Z_Titls2[position]);
                    }

                    iv.setImageResource(Z_Imgs[position]);
                } else if (MenuActivity.METER_STYLE.equals("CS")) {
                    if (MenuActivity.netThread == null) {
                        tv.setText(CS_Titls[position]);
                    } else {
                        tv.setText(CS_Titls2[position]);
                    }

                    iv.setImageResource(CS_Imgs[position]);
                } else if (MenuActivity.METER_STYLE.equals("NS")) {
                    if (MenuActivity.netThread == null) {
                        tv.setText(NS_Titls[position]);
                    } else {
                        tv.setText(NS_Titls2[position]);
                    }

                    iv.setImageResource(NS_Imgs[position]);
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
        menuActivity_btn_exit.performClick();
    }

    @Override
    protected void onDestroy() {
        resetNetwork(this);
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
                gvMain.setAdapter(new imageAdapter(this));
            } else if (resultCode == BluetoothConnectThread.METER) {
                loadUser();
                gvMain.setAdapter(new imageAdapter(this));
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
        if (MenuActivity.METER_STYLE.equals("L")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:山科LoRa表");
        } else if (MenuActivity.METER_STYLE.equals("LSX")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:山科LoRa摄像表");
        } else if (MenuActivity.METER_STYLE.equals("LDZ")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:山科LoRa摄像表");
        } else if (MenuActivity.METER_STYLE.equals("LYX")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:山科有线分采LoRa");
        } else if (MenuActivity.METER_STYLE.equals("W")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:安美通LoRa表");
        } else if (MenuActivity.METER_STYLE.equals("F")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:安美通Fsk表");
        } else if (MenuActivity.METER_STYLE.equals("P")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:P型摄像表");
        } else if (MenuActivity.METER_STYLE.equals("Z")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:有线直读表");
        } else if (MenuActivity.METER_STYLE.equals("JYMC")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:隽永LoRa脉冲表");
        } else if (MenuActivity.METER_STYLE.equals("JYDZ")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:隽永LoRa电阻表");
        } else if (MenuActivity.METER_STYLE.equals("JY")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:LoRa隽永表");
        } else if (MenuActivity.METER_STYLE.equals("CS")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:超声水表");
        } else if (MenuActivity.METER_STYLE.equals("NS")) {
            menuActivity_tv_meterStyle.setText("当前表类型为:数字状态表");
        }
        pref = getSharedPreferences("user_msg1", MODE_PRIVATE);
        SECK_PARAM = pref.getString("EditText_X", "0");
        if (SECK_PARAM.length() > 2) {
            SECK_PARAM = SECK_PARAM.substring(0, 2);
        }
    }

    public static void sendCmd(String sendMsg) {
        Log.d("limbo", "sendCmd函数发送:" + sendMsg);
        if (sendMsg.length() % 2 == 1) {
            sendMsg = sendMsg + "0";
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

    /**
     * 自带解析
     */
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
        menu.add(0, 3, 3, "关于");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()) {
            case 3:
                showAbout();
                break;
        }
        return true;
    }

    public void showAbout() {

        String aboutText = "版本号 : " + this.getAppVersionName(this) + "\r\n" +
                "Copyright (C) 1999 - 2018\r\nSeck (Hangzhou)";
        new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(aboutText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

    public static int checkPsw(Activity activity) {
        Z_PasswordDialog pswdlg = new Z_PasswordDialog(activity, 0);
        int rtn = pswdlg.showDialog();
        if (rtn != 1) {
            HintDialog.ShowHintDialog(activity, "密码输入错误", "错误");
        }
        return rtn;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public int copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (sdCardExist) {
                String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
                if (fileNames.length > 0) {//如果是目录
                    File file = new File(newPath);
                    file.mkdirs();//如果文件夹不存在，则递归
                    for (String fileName : fileNames) {
                        Log.d("limbo", fileName);
                        copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                    }
                } else {//如果是文件
                    File sdDir = new File(newPath);// 获取SD卡的path
                    if (!sdDir.exists()) {
                        sdDir.mkdirs();
                    }
                    sdDir = new File(newPath + "/LoRaDb.db");
                    if (!sdDir.exists()) {
                        InputStream is = context.getAssets().open(oldPath);
                        FileOutputStream fos = new FileOutputStream(new File(newPath + "/LoRaDb.db"));
                        byte[] buffer = new byte[1024];
                        int byteCount = 0;
                        while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                            fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                        }
                        fos.flush();//刷新缓冲区
                        is.close();
                        fos.close();
                        return 0;
                    } else {
                        return 3;
                    }
                }

            } else {
                return 1;
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("limbo", e.toString());
            //如果捕捉到错误则通知UI线程
        }
        return 2;
    }
}
