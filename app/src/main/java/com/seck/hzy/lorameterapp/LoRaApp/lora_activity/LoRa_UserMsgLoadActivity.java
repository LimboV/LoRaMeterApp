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

import com.seck.hzy.lorameterapp.LoRaApp.barcode.CaptureActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2016/12/6.
 */
public class LoRa_UserMsgLoadActivity extends Activity {

    @BindView(R.id.UserMsgLoadActivity_btn_setBds)
    Button UserMsgLoadActivity_btn_setBds;

    @BindView(R.id.UserMsgLoadActivity_et_meterId)
    EditText UserMsgLoadActivity_et_meterId;

    @BindView(R.id.UserMsgLoadActivity_et_meterNumber)
    EditText UserMsgLoadActivity_et_meterNumber;

    @BindView(R.id.UserMsgLoadActivity_et_userAddr)
    EditText UserMsgLoadActivity_et_userAddr;

    @BindView(R.id.UserMsgLoadActivity_et_xqId)
    EditText UserMsgLoadActivity_et_xqId;

    @BindView(R.id.UserMsgLoadActivity_et_cjjID)
    EditText UserMsgLoadActivity_et_cjjID;

    @BindView(R.id.UserMsgLoadActivity_et_meterFreq)
    EditText UserMsgLoadActivity_et_meterFreq;

    @BindView(R.id.UserMsgLoadActivity_et_bds)
    EditText UserMsgLoadActivity_et_bds;

    @BindView(R.id.UserMsgLoadActivity_cb_error)
    CheckBox UserMsgLoadActivity_cb_error;


    private int xqid, cjjid, meternum;
    long meternumber;
    private Button btnId, btnNum, btnSave, btnGetData;
    private int X_FLAG = 0;
    private String useraddr, date, meterid, sendMsg, getMsg, afnetID, num, waterValue, Newfreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        setContentView(R.layout.lora_acivity_usermsgload);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        ButterKnife.bind(this);
        if (!MenuActivity.METER_STYLE.equals("JY")) {
            UserMsgLoadActivity_btn_setBds.setVisibility(View.GONE);
            UserMsgLoadActivity_et_bds.setVisibility(View.GONE);
        }
        /*Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        meterid = bundle.getInt("meterid") + "";
        useraddr = bundle.getString("useraddr");
        date = bundle.getString("date");
        meternumber = Integer.parseInt(bundle.getString("meternumber"));*/


        UserMsgLoadActivity_cb_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserMsgLoadActivity_cb_error.isChecked()) {
                    btnGetData.setTextColor(0x87CEEB);
                } else {
                    btnGetData.setTextColor(0xFFFFFFFF);
                }
            }
        });
        loadUser();
        UserMsgLoadActivity_et_cjjID.setText("10101");
        UserMsgLoadActivity_et_cjjID.setKeyListener(null);//不可编辑
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
         * 设置表底数
         */
        UserMsgLoadActivity_btn_setBds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (
                        HzyUtils.isEmpty(UserMsgLoadActivity_et_meterFreq.getText().toString()) ||
                                HzyUtils.isEmpty(UserMsgLoadActivity_et_bds.getText().toString()) ||
                                HzyUtils.isEmpty(UserMsgLoadActivity_et_meterId.getText().toString())
                        ) {
                    HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "输入信息不得为空", "提示");
                    return;
                } else {
                    HzyUtils.showProgressDialog(LoRa_UserMsgLoadActivity.this);
                    Newfreq = UserMsgLoadActivity_et_meterFreq.getText().toString().trim();
                    if (!HzyUtils.isEmpty(Newfreq)) {
                        Newfreq = Integer.toHexString(Integer.parseInt(Newfreq));
                    }
                    while (Newfreq.length() < 6) {
                        Newfreq = "0" + Newfreq;
                    }
                    String bds = UserMsgLoadActivity_et_bds.getText().toString().trim();
                    String Hbds, Lbds;
                    if (bds.contains(".")) {
                        Hbds = bds.substring(0, bds.indexOf("."));
                        Lbds = bds.substring(bds.indexOf(".") + 1);
                        while (Hbds.length() < 6) {
                            Hbds = "0" + Hbds;
                        }
                        while (Lbds.length() < 2) {
                            Lbds = Lbds + "0";
                        }
                        bds = Hbds + Lbds;
                    } else {
                        while (bds.length() < 8) {
                            bds = "0" + bds;
                        }
                    }

                    String id = UserMsgLoadActivity_et_meterNumber.getText().toString().trim();
                    meterid = id;
                    while (meterid.length() < 8) {
                        meterid = "0" + meterid;
                    }
                    if (HzyUtils.isEmpty(id) || HzyUtils.isEmpty(Newfreq)) {
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "频率，表ID皆不可为空。", "提示");
                    } else if (id.length() > 14 || Newfreq.length() > 6) {
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "数据过长", "提示");
                    } else {

                    }
                    String sendMsg = "ff" +
                            Newfreq +
                            "02" +
                            meterid +
                            bds//表初值
                            ;
                    MenuActivity.sendCmd(sendMsg);
                    Log.d("limbo", sendMsg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MenuActivity.btAuto = true;
                            MenuActivity.Cjj_CB_MSG = "";
                            String getMsg = "";
                            try {
                                Thread.sleep(4000);
                                getMsg = getMsg + MenuActivity.Cjj_CB_MSG;
                                MenuActivity.Cjj_CB_MSG = "";
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                Log.d("limbo", "get : " + getMsg);
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x04;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                            } catch (Exception e) {
                                Log.d("limbo", e.toString());
                            }

                        }
                    }).start();

                }


                /**
                 * 点抄功能，已弃用
                 */
                /*if (mCheckBox.isChecked()) {
                    Toast.makeText(LoRa_UserMsgLoadActivity.this, "异常表无法点抄数据", Toast.LENGTH_LONG).show();
                } else {
                    *//**
                 * 读取数据
                 *//*
                    HzyUtils.showProgressDialog1(LoRa_UserMsgLoadActivity.this, "正在读取水表数据");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Newfreq = etFreq.getText().toString().trim();
                                String mkid = edNum.getText().toString().trim();
                                if (!HzyUtils.isEmpty(Newfreq)) {
                                    Newfreq = Integer.toHexString(Integer.parseInt(Newfreq));
                                }
                                while (Newfreq.length() < 6) {
                                    Newfreq = "0" + Newfreq;
                                }
                                if (mkid.length() > 8) {
                                    HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "表序号不可为空", "提示");
                                    return;
                                }
                                while (mkid.length() < 8) {
                                    mkid = "0" + mkid;
                                }
                                sendMsg = "ff" + Newfreq + "1f" + mkid;
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
                }*/

            }
        });
        /**
         * 表数据保存及表端设置
         */
        btnSave = (Button) findViewById(R.id.UserMsgLoadActivity_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Newfreq = UserMsgLoadActivity_et_meterFreq.getText().toString().trim();
                if (!HzyUtils.isEmpty(Newfreq)) {
                    Newfreq = Integer.toHexString(Integer.parseInt(Newfreq));
                }
                while (Newfreq.length() < 6) {
                    Newfreq = "0" + Newfreq;
                }
                String id = UserMsgLoadActivity_et_meterId.getText().toString().trim();
                num = UserMsgLoadActivity_et_meterNumber.getText().toString().trim();
                String xqId = UserMsgLoadActivity_et_xqId.getText().toString().trim();
                String cjjId = UserMsgLoadActivity_et_cjjID.getText().toString().trim();
                useraddr = UserMsgLoadActivity_et_userAddr.getText().toString().trim();
                xqid = Integer.parseInt(xqId);
                cjjid = Integer.parseInt(cjjId);
                meterid = id;
                if (UserMsgLoadActivity_cb_error.isChecked()) {
                    if (HzyUtils.isEmpty(id) || HzyUtils.isEmpty(cjjId) ||
                            HzyUtils.isEmpty(xqId) || useraddr.length() == 0 || HzyUtils.isEmpty(Newfreq)) {
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "小区ID，采集机ID，户号，频率，表ID皆不可为空。", "提示");
                    } else if (id.length() > 14 || Newfreq.length() > 6) {
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
                            useraddr.length() == 0 || HzyUtils.isEmpty(Newfreq)) {
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "小区ID，采集机ID，户号，表ID，频率与序列号皆不可为空。", "提示");
                    } else if (id.length() > 14 || num.length() > 8 || Newfreq.length() > 6) {
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
                        Log.d("limbo", "原频率" + freq +
                                "\n网络ID" + netId +
                                "\n模块ID" + mkId +
                                "\n表底数" + bds +
                                "\n物理模块ID" + wlmoID);


                        LoRa_DataHelper.addMeter(xqid, cjjid, Integer.parseInt(mkId),
                                meterid, useraddr, date, Newfreq);

                        afnetID = xqid + "";
                        while (afnetID.length() < 4) {
                            afnetID = "0" + afnetID;
                        }

                        sendMsg = "ff" +
                                freq +
                                "01" +
                                wlmoID +
                                Newfreq +
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
                    dy = ((float) Integer.parseInt(dy, 16) / 100 + 2) + "";
                    Log.d("limbo", "\n\n接收参数:" +
                            "\n水表值:" + waterValue +
                            "\n阀控:" + getMsg.substring(18, 20) +
                            "\n电压:" + dy);

                    HzyUtils.closeProgressDialog();

                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("result", "LoRa网络:" + afnetID + "\nLoRa频率:" + Integer.parseInt(Newfreq, 16) + "\n表序号:"
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
                    dy = ((float) Integer.parseInt(dy, 16) / 100 + 2) + "";
                    while (dy.length() > 5) {
                        dy = dy.substring(0, dy.length() - 1);
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
                        Log.d("limbo", "原频率" + freq +
                                "\n网络ID" + netId +
                                "\n模块ID" + mkId +
                                "\n表底数" + bds +
                                "\n物理模块ID" + wlmoID);
                        waterValue = bds;
                        if (MenuActivity.METER_STYLE.equals("W")) {
                            waterValue = waterValue.substring(1, 2) +
                                    waterValue.substring(3, 4) +
                                    waterValue.substring(5, 6) + "." +
                                    waterValue.substring(7, 8);
                        } else if (MenuActivity.METER_STYLE.equals("JY")) {
                            waterValue = waterValue.substring(0, 6) + "." +
                                    waterValue.substring(6, 8);
                        }
                        while (num.length() < 8) {
                            num = "0" + num;
                        }
                        if (mkId.equals(num)) {
                            LoRa_DataHelper.addMeter(xqid, cjjid, Integer.parseInt(mkId), meterid,
                                    useraddr, date, Newfreq);

                            afnetID = xqid + "";
                            while (afnetID.length() < 4) {
                                afnetID = "0" + afnetID;
                            }

                            sendMsg = "ff" +
                                    freq +//原频率
                                    "01" +
                                    wlmoID +
                                    Newfreq +
                                    afnetID;
                            MenuActivity.sendCmd(sendMsg);

                            HzyUtils.closeProgressDialog();

                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoRa_UserMsgLoadActivity.this);
                            dialog.setTitle("水表信息正常");
                            dialog.setMessage("\n户号:" + useraddr + "\n\n写入LoRa网络ID:" + afnetID + "\n写入LoRa网络频率:" + Integer.parseInt(Newfreq, 16) +
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
                case 0x04:
                    getMsg = msg.obj.toString();
                    if (getMsg.contains("a0")) {
                        String mkxh = getMsg.substring(2, 4);//型号
                        String bb = getMsg.substring(4, 6);//版本
                        String freq = Integer.parseInt(getMsg.substring(6, 12),16)  + "";
                        String netid = getMsg.substring(12, 16);
                        String mkid = getMsg.substring(16, 24);
                        String zt = getMsg.substring(24, 26);
                        String bds = getMsg.substring(26, 34);
                        bds = Integer.parseInt(bds.substring(0, 6)) + "." + bds.substring(6, 8);
                        String wlid = getMsg.substring(34, 42);


                        String result = "模块型号:" + mkxh +
                                "\n版本:" + bb +
                                "\n频率:" + freq +
                                "\n网络ID:" + netid +
                                "\n模块ID:" + mkid +
                                "\n状态:" + zt +
                                "\n表底数:" + bds +
                                "\n物理ID:" + wlid;
                        Log.d("limbo", result);
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this,result,"返回");


                        HzyUtils.closeProgressDialog();
                    } else {
                        HintDialog.ShowHintDialog(LoRa_UserMsgLoadActivity.this, "未接收到完整返回", "错误");
                        HzyUtils.closeProgressDialog();
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
                        UserMsgLoadActivity_et_meterId.setText(scanResult);
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
                        UserMsgLoadActivity_et_meterNumber.setText(scanResult);
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
        editor.putString("etXqId", UserMsgLoadActivity_et_xqId.getText().toString());
        editor.putString("etCjjId", UserMsgLoadActivity_et_cjjID.getText().toString());
        editor.putString("etUserAddr", UserMsgLoadActivity_et_userAddr.getText().toString());
        editor.putString("etFreq", UserMsgLoadActivity_et_meterFreq.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("UserMsgLoadActivity", MODE_PRIVATE);
        UserMsgLoadActivity_et_xqId.setText(pref.getString("etXqId", ""));
        UserMsgLoadActivity_et_cjjID.setText(pref.getString("etCjjId", ""));
        UserMsgLoadActivity_et_userAddr.setText(pref.getString("etUserAddr", ""));
        UserMsgLoadActivity_et_meterFreq.setText(pref.getString("etFreq", ""));
    }

    @Override
    protected void onDestroy() {
        saveUser();
        MenuActivity.btAuto = false;
        HzyUtils.closeProgressDialog();
        super.onDestroy();
    }
}
