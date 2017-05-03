package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.barcode.CaptureActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by limbo on 2016/12/6.
 */
public class LoRa_UserMsgLoadActivity extends Activity {
    private int xqid, cjjid, meternum;
    private CheckBox mCheckBox;
    long meternumber;
    private EditText edId, edNum, etUserAddr, etXqId, etCjjId;
    private Button btnId, btnNum, btnSave, btnGetData;
    private int X_FLAG = 0;
    private String useraddr, date, meterid, sendMsg, getMsg, afnetID, num, waterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        setContentView(R.layout.lora_acivity_usermsgload);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        /*Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        meterid = bundle.getInt("meterid") + "";
        useraddr = bundle.getString("useraddr");
        date = bundle.getString("date");
        meternumber = Integer.parseInt(bundle.getString("meternumber"));*/

        edId = (EditText) findViewById(R.id.UserMsgLoadActivity_et_meterId);
        edNum = (EditText) findViewById(R.id.UserMsgLoadActivity_et_meterNumber);
        etUserAddr = (EditText) findViewById(R.id.UserMsgLoadActivity_et_userAddr);
        etXqId = (EditText) findViewById(R.id.UserMsgLoadActivity_et_xqId);
        etCjjId = (EditText) findViewById(R.id.UserMsgLoadActivity_et_cjjID);

        mCheckBox = (CheckBox) findViewById(R.id.UserMsgLoadActivity_cb_error);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBox.isChecked()) {
                    btnGetData.setTextColor(0x87CEEB);
                } else {
                    btnGetData.setTextColor(0xFFFFFFFF);
                }
            }
        });
        loadUser();
        etCjjId.setText("10101");
        etCjjId.setKeyListener(null);//不可编辑
        /**
         * id
         */
        btnId = (Button) findViewById(R.id.UserMsgLoadActivity_btn_idLoad);
        btnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开扫描界面扫描条形码或二维码
                X_FLAG = 1;
                Intent openCameraIntent = new Intent(LoRa_UserMsgLoadActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
        /**
         * number
         */
        btnNum = (Button) findViewById(R.id.UserMsgLoadActivity_btn_numberLoad);
        btnNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开扫描界面扫描条形码或二维码
                X_FLAG = 2;
                Intent openCameraIntent = new Intent(LoRa_UserMsgLoadActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
        /**
         * getdata
         */
        btnGetData = (Button) findViewById(R.id.UserMsgLoadActivity_btn_getdata);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBox.isChecked()) {
                    Toast.makeText(LoRa_UserMsgLoadActivity.this, "异常表无法点抄数据", Toast.LENGTH_LONG).show();
                } else {
                    /**
                     * 读取数据
                     */
                    HzyUtils.showProgressDialog1(LoRa_UserMsgLoadActivity.this, "正在读取水表数据");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String mkid = edNum.getText().toString().trim();
                                if (mkid.length() > 8) {
                                    HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "表序号不可为空", "提示");
                                    return;
                                }
                                while (mkid.length() < 8) {
                                    mkid = "0" + mkid;
                                }
                                sendMsg = "ff" + "078d98" + "1f" + mkid;
                                MenuActivity.sendCmd(sendMsg);
                                Log.d("limbo", sendMsg);
                                Thread.sleep(3000);
                                getMsg = MenuActivity.Cjj_CB_MSG;
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x02;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                            } catch (InterruptedException e) {
                                HzyUtils.closeProgressDialog();
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }
        });
        /**
         * save
         */
        btnSave = (Button) findViewById(R.id.UserMsgLoadActivity_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edId.getText().toString().trim();
                num = edNum.getText().toString().trim();
                String xqId = etXqId.getText().toString().trim();
                String cjjId = etCjjId.getText().toString().trim();
                useraddr = etUserAddr.getText().toString().trim();
                xqid = Integer.parseInt(xqId);
                cjjid = Integer.parseInt(cjjId);
                meterid = id;
                if (mCheckBox.isChecked()) {
                    if (HzyUtils.isEmpty(id) || HzyUtils.isEmpty(cjjId) || HzyUtils.isEmpty(xqId) || useraddr.length() == 0) {
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "小区ID，采集机ID，户号，表ID皆不可为空。", "提示");
                    } else if (id.length() > 14) {
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "数据过长", "提示");
                    } else {
                        HzyUtils.showProgressDialog1(LoRa_UserMsgLoadActivity.this, "请激活表端");
                        MenuActivity.sendCmd("11");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MenuActivity.btAuto = true;
                                MenuActivity.Cjj_CB_MSG = "";
                                String getMsg = "";
                                while (MenuActivity.btAuto) {
                                    try {
                                        Thread.sleep(1000);
                                        getMsg = getMsg + MenuActivity.Cjj_CB_MSG;
                                        MenuActivity.Cjj_CB_MSG = "";
                                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                        Log.d("limbo", "get : " + getMsg);
                                        if (getMsg.length() >= 54) {
                                            MenuActivity.btAuto = false;
                                            getMsg = getMsg.substring(getMsg.indexOf("a0"));
                                            Log.d("limbo", getMsg);

                                            Message message = new Message();
                                            message.what = 0x00;
                                            message.obj = getMsg;
                                            mHandler.sendMessage(message);
                                        }
                                    } catch (Exception e) {
                                        Log.d("limbo", e.toString());
                                    }
                                }

                            }
                        }).start();

                    }
                } else {
                    if (HzyUtils.isEmpty(id) || HzyUtils.isEmpty(cjjId) || HzyUtils.isEmpty(xqId) || HzyUtils.isEmpty(num) ||
                            useraddr.length() == 0) {
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "小区ID，采集机ID，户号，表ID与序列号皆不可为空。", "提示");
                    } else if (id.length() > 14 || num.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "数据过长", "提示");
                    } else {
                        HzyUtils.showProgressDialog1(LoRa_UserMsgLoadActivity.this, "请激活表端");
                        MenuActivity.sendCmd("11");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MenuActivity.btAuto = true;
                                MenuActivity.Cjj_CB_MSG = "";
                                String getMsg = "";
                                while (MenuActivity.btAuto) {
                                    try {
                                        Thread.sleep(1000);
                                        getMsg = getMsg + MenuActivity.Cjj_CB_MSG;
                                        MenuActivity.Cjj_CB_MSG = "";
                                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                        Log.d("limbo", "get : " + getMsg);
                                        if (getMsg.length() >= 54) {
                                            MenuActivity.btAuto = false;
                                            getMsg = getMsg.substring(getMsg.indexOf("a0"));
                                            Log.d("limbo", getMsg);

                                            Message message = new Message();
                                            message.what = 0x03;
                                            message.obj = getMsg;
                                            mHandler.sendMessage(message);
                                        }
                                    } catch (Exception e) {
                                        Log.d("limbo", e.toString());
                                    }
                                }

                            }
                        }).start();


                    }
                }
            }
        });

    }

    private Handler mHandler = new Handler() {
        String freq;
        String netId;
        String mkId;
        String bds;
        String wlmoID;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    getMsg = msg.obj.toString();

                    Calendar c = Calendar.getInstance();
                    Date d = c.getTime();
                    DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
                    date = df.format(d);


                    Log.d("limbo", "数据保存:\n表ID:" + meterid + "表序号:" + meternumber);
                    //                    DataHelper.saveUser(Integer.parseInt(xqId), Integer.parseInt(cjjId), userAddr, id, Integer.parseInt(num), date);


                    if (getMsg.length() >= 42) {
                        //                        String freq = Integer.parseInt(getMsg.substring(6, 12), 16) + "";//频率-16进制
                        freq = getMsg.substring(6, 12);//频率-16进制
                        netId = getMsg.substring(12, 16);//网络ID
                        mkId = getMsg.substring(16, 24);//模块ID
                        bds = getMsg.substring(26, 34);//表底数
                        wlmoID = getMsg.substring(34, 42);//物理模块ID
                        Log.d("limbo", "频率" + freq +
                                "\n网络ID" + netId +
                                "\n模块ID" + mkId +
                                "\n表底数" + bds +
                                "\n物理模块ID" + wlmoID);


                        LoRa_DataHelper.addMeter(xqid, cjjid, Integer.parseInt(mkId), meterid, useraddr, date);

                        afnetID = xqid + "";
                        while (afnetID.length() < 4) {
                            afnetID = "0" + afnetID;
                        }

                        sendMsg = "ff" +
                                freq +
                                "01" +
                                wlmoID +
                                freq +
                                afnetID;
                        MenuActivity.sendCmd(sendMsg);
                        HzyUtils.closeProgressDialog();

                        /**
                         * 读取数据
                         */
                        HzyUtils.showProgressDialog1(LoRa_UserMsgLoadActivity.this, "正在读取水表数据，请等待30秒");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sendMsg = "ff" + freq + "1f" + mkId;
                                    Thread.sleep(28000);
                                    MenuActivity.sendCmd(sendMsg);
                                    Log.d("limbo", sendMsg);
                                    Thread.sleep(2000);
                                    getMsg = MenuActivity.Cjj_CB_MSG;
                                    if (getMsg.length() == 0) {
                                        Message message = new Message();
                                        message.what = 0x99;
                                        message.obj = getMsg;
                                        mHandler.sendMessage(message);
                                    } else {
                                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                        Log.d("limbo", getMsg);

                                        Message message = new Message();
                                        message.what = 0x01;
                                        message.obj = getMsg;
                                        mHandler.sendMessage(message);
                                    }
                                } catch (InterruptedException e) {
                                    HzyUtils.closeProgressDialog();
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } else {
                        HzyUtils.closeProgressDialog();
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "网络ID设置失败,请重试", "错误");
                    }


                    break;
                case 0x01:
                    getMsg = msg.obj.toString();
                    waterValue = getMsg.substring(10, 18);
                    waterValue = waterValue.substring(1, 2) +
                            waterValue.substring(3, 4) +
                            waterValue.substring(5, 6) + "." +
                            waterValue.substring(7, 8);
                    String dy = getMsg.substring(20, 22);
                    dy = ((float)Integer.parseInt(dy, 16) / 100 + 2) + "";
                    Log.d("limbo", "\n\n接收参数:" +
                            "\n水表值:" + waterValue +
                            "\n阀控:" + getMsg.substring(18, 20) +
                            "\n电压:" + dy);

                    HzyUtils.closeProgressDialog();

                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("result", "LoRa网络:" + afnetID + "\nLoRa频率:49500Hz\n表序号:"
                            + mkId + "\n表ID:" + meterid + "\n\n水表值:" + waterValue);
                    resultIntent.putExtras(bundle);
                    LoRa_UserMsgLoadActivity.this.setResult(RESULT_OK, resultIntent);
                    LoRa_UserMsgLoadActivity.this.finish();
                    break;
                case 0x02:
                    getMsg = msg.obj.toString();
                    waterValue = getMsg.substring(10, 18);
                    waterValue = waterValue.substring(1, 2) +
                            waterValue.substring(3, 4) +
                            waterValue.substring(5, 6) + "." +
                            waterValue.substring(7, 8);
                    dy = getMsg.substring(20, 22);
                    dy = ((float)Integer.parseInt(dy, 16) / 100 + 2) + "";
                    while (dy.length()>5){
                        dy = dy.substring(0,dy.length()-1);
                    }
                    Log.d("limbo", "\n\n接收参数:" +
                            "\n水表读数:" + waterValue +
                            "\n阀控状态:" + getMsg.substring(18, 20) +
                            "\n电池电压:" + dy);

                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this,
                            "水表读数:" + waterValue +
                                    "\n阀控状态:" + getMsg.substring(18, 20) +
                                    "\n电池电压:" + dy + "V", "提示");
                    break;
                case 0x03:
                    getMsg = msg.obj.toString();

                    c = Calendar.getInstance();
                    d = c.getTime();
                    df = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
                    date = df.format(d);


                    if (getMsg.length() >= 42) {
                        //                        String freq = Integer.parseInt(getMsg.substring(6, 12), 16) + "";//频率-16进制
                        freq = getMsg.substring(6, 12);//频率-16进制
                        netId = getMsg.substring(12, 16);//网络ID
                        mkId = getMsg.substring(16, 24);//模块ID
                        bds = getMsg.substring(26, 34);//表底数
                        wlmoID = getMsg.substring(34, 42);//物理模块ID
                        Log.d("limbo", "频率" + freq +
                                "\n网络ID" + netId +
                                "\n模块ID" + mkId +
                                "\n表底数" + bds +
                                "\n物理模块ID" + wlmoID);
                        while (num.length() < 8) {
                            num = "0" + num;
                        }
                        if (mkId.equals(num)) {
                            LoRa_DataHelper.addMeter(xqid, cjjid, Integer.parseInt(mkId), meterid, useraddr, date);

                            afnetID = xqid + "";
                            while (afnetID.length() < 4) {
                                afnetID = "0" + afnetID;
                            }

                            sendMsg = "ff" +
                                    freq +
                                    "01" +
                                    wlmoID +
                                    freq +
                                    afnetID;
                            MenuActivity.sendCmd(sendMsg);

                            HzyUtils.closeProgressDialog();

                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoRa_UserMsgLoadActivity.this);
                            dialog.setTitle("水表信息正常");
                            dialog.setMessage("\n户号:" + useraddr + "\n\n写入LoRa网络ID:" + afnetID + "\n写入LoRa网络频率:49500Hz" +
                                    "\n\n绑定水表ID:" + meterid + "\n绑定模块序号:" + num + "\n读取模块序号:" + mkId + "\n\n当前水表读数:" + waterValue);
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent resultIntent = new Intent();
                                    Bundle bundle = new Bundle();
                                    resultIntent.putExtras(bundle);
                                    LoRa_UserMsgLoadActivity.this.setResult(98, resultIntent);
                                    LoRa_UserMsgLoadActivity.this.finish();
                                }
                            });
                            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.show();
                        } else {
                            HzyUtils.closeProgressDialog();
                            afnetID = xqid + "";
                            while (afnetID.length() < 4) {
                                afnetID = "0" + afnetID;
                            }
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoRa_UserMsgLoadActivity.this);
                            dialog.setTitle("水表信息异常");
                            dialog.setMessage("\n户号:" + useraddr + "\n\n写入LoRa网络ID:" + afnetID +
                                    "\n写入LoRa网络频率:495000Hz" + "\n\n绑定水表ID:" + meterid + "\n绑定模块序号:"
                                    + num + "\n读取模块序号:" + mkId + "\n\n当前水表读数:" + waterValue);
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.show();
                        }


                    }
                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "未接收到数据,请重试或检查表线路。", "提示");
                    break;
                default:
                    break;
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            switch (X_FLAG) {
                case 1:
                    try {
                        Bundle bundle = data.getExtras();
                        String scanResult = bundle.getString("result");
                        //                        scanResult = scanResult.substring(0, scanResult.length() - 1);
                        edId.setText(scanResult);
                        meterid = scanResult;
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }

                    break;
                case 2:
                    try {
                        Bundle bundle = data.getExtras();
                        String scanResult = bundle.getString("result");
                        //scanResult = scanResult.substring(0,scanResult.length()-1);
                        edNum.setText(scanResult);
                        meternumber = Integer.parseInt(scanResult);
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }
                    break;
                default:
                    break;

            }

        }
    }

    /**
     * 使用SharePreferences保存用户信息
     */
    public void saveUser() {

        SharedPreferences.Editor editor = getSharedPreferences("UserMsgLoadActivity", MODE_PRIVATE).edit();
        editor.putString("etXqId", etXqId.getText().toString());
        editor.putString("etCjjId", etCjjId.getText().toString());
        editor.putString("etUserAddr", etUserAddr.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("UserMsgLoadActivity", MODE_PRIVATE);
        etXqId.setText(pref.getString("etXqId", ""));
        etCjjId.setText(pref.getString("etCjjId", ""));
        etUserAddr.setText(pref.getString("etUserAddr", ""));
    }

    @Override
    protected void onDestroy() {
        saveUser();
        MenuActivity.btAuto = false;
        HzyUtils.closeProgressDialog();
        super.onDestroy();
    }
}
