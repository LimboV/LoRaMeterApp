package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2016/11/22.
 */
public class LoRa_SxbGetDataActivity extends Activity {

    @BindView(R.id.SxbGetDataActivity_tv_showMsg)
    TextView SxbGetDataActivity_tv_showMsg;

    @BindView(R.id.SxbGetDataActivity_btn_Ksdt)
    TextView SxbGetDataActivity_btn_Ksdt;

    @BindView(R.id.SxbGetDataActivity_et_meterAddr)
    EditText SxbGetDataActivity_et_meterAddr;

    @BindView(R.id.SxbGetDataActivity_et_freq)
    EditText SxbGetDataActivity_et_freq;

    @BindView(R.id.SxbGetDataActivity_et_netId)
    EditText SxbGetDataActivity_et_netId;

    @BindView(R.id.LoRa_SxbGetDataActivity_et_y)
    EditText LoRa_SxbGetDataActivity_et_y;

    @BindView(R.id.LoRa_SxbGetDataActivity_et_x1)
    EditText LoRa_SxbGetDataActivity_et_x1;

    @BindView(R.id.LoRa_SxbGetDataActivity_et_x2)
    EditText LoRa_SxbGetDataActivity_et_x2;

    @BindView(R.id.LoRa_SxbGetDataActivity_et_x3)
    EditText LoRa_SxbGetDataActivity_et_x3;

    @BindView(R.id.LoRa_SxbGetDataActivity_et_x4)
    EditText LoRa_SxbGetDataActivity_et_x4;

    @BindView(R.id.LoRa_SxbGetDataActivity_et_x5)
    EditText LoRa_SxbGetDataActivity_et_x5;

    @BindView(R.id.LoRa_SxbGetDataActivity_btn_getLocation)
    Button LoRa_SxbGetDataActivity_btn_getLocation;

    @BindView(R.id.LoRa_SxbGetDataActivity_btn_setLocation)
    Button LoRa_SxbGetDataActivity_btn_setLocation;

    @BindView(R.id.SxbGetDataActivity_btn_getData)
    Button SxbGetDataActivity_btn_getData;

    @BindView(R.id.SxbGetDataActivity_btn_getVersion)
    Button SxbGetDataActivity_btn_getVersion;

    @BindView(R.id.SxbGetDataActivity_btn_AutoLocation)
    Button SxbGetDataActivity_btn_AutoLocation;

    @BindView(R.id.SxbGetDataActivity_btn_reBack)
    Button SxbGetDataActivity_btn_reBack;

    @BindView(R.id.SxbGetDataActivity_tv_meterSbds)
    TextView SxbGetDataActivity_tv_meterSbds;

    @BindView(R.id.SxbGetDataActivity_tv_meterSxsbds)
    TextView SxbGetDataActivity_tv_meterSxsbds;

    @BindView(R.id.SxbGetDataActivity_tv_meterZxd)
    TextView SxbGetDataActivity_tv_meterZxd;


    private String sendMsg;
    private boolean timeOut = false;
    private boolean ReadPic = false;
    final LoRa_SxbGetDataActivity thisView = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lora_activity_sxbgetdata);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框

        ButterKnife.bind(this);
        /**
         * 自动
         */
        MenuActivity.btAuto = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("limbo", "start");
                while (MenuActivity.btAuto) {

                    try {
                        Thread.sleep(3000);
                        if (ReadPic == false) {
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            MenuActivity.Cjj_CB_MSG = "";
                            if (getMsg.length() == 0) {
                            } else {
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                Log.d("limbo", "get:" + getMsg);

                                Message message = new Message();
                                message.what = 0x01;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            }
                        }
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }


                }

            }
        }).start();

        /**
         * 复位
         * 68 00 00 00 00 00 00 00 00 00 30 03 A5 05 00
         */
        SxbGetDataActivity_btn_reBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendMsg = "680000000000000000003003A50500";
                MenuActivity.sendCmd(sendMsg);
                Toast.makeText(LoRa_SxbGetDataActivity.this, "已复位", Toast.LENGTH_LONG).show();
            }
        });
        /**
         * 读版本号
         */
        SxbGetDataActivity_btn_getVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    //                    HzyUtils.showProgressDialog(LoRa_SxbGetDataActivity.this);
                    String freq = SxbGetDataActivity_et_freq.getText().toString().trim();
                    String netId = SxbGetDataActivity_et_netId.getText().toString().trim();
                    String addr = SxbGetDataActivity_et_meterAddr.getText().toString().trim();
                    if (addr.length() > 10) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表地址过长", "提示");
                    }
                    if (netId.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "网络ID过长", "提示");
                    }
                    if (HzyUtils.isEmpty(freq)) {
                        freq = "0";
                    }
                    freq = Integer.toHexString(Integer.parseInt(freq));
                    //                    freq = Integer.parseInt(freq, 16) + "";
                    if (freq.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表频率过大", "提示");
                    }
                    while (freq.length() < 4) {
                        freq = "0" + freq;
                    }
                    while (netId.length() < 4) {
                        netId = "0" + netId;
                    }
                    if (addr.length() == 0) {
                        addr = "aaaaaaaaaa";
                    }
                    while (addr.length() < 10) {
                        addr = "0" + addr;
                    }

                    sendMsg = "68" +
                            freq +//频率"1356"
                            netId +//网络ID"0113"
                            addr +
                            "20" +//控制码
                            "03" +//数据长度
                            "8100" +//数据标志
                            "00"//序号
                    ;
                    Log.d("limbo", sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    SxbGetDataActivity_tv_showMsg.setText("发送读取参数指令:\n水表地址:" + addr
                            + "\n频率:" + freq
                            + "\n网络ID:" + netId);
                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表

                }
            }
        });
        LoRa_SxbGetDataActivity_btn_setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String y = LoRa_SxbGetDataActivity_et_y.getText().toString().trim();
                String x1 = LoRa_SxbGetDataActivity_et_x1.getText().toString().trim();
                String x2 = LoRa_SxbGetDataActivity_et_x2.getText().toString().trim();
                String x3 = LoRa_SxbGetDataActivity_et_x3.getText().toString().trim();
                String x4 = LoRa_SxbGetDataActivity_et_x4.getText().toString().trim();
                String x5 = LoRa_SxbGetDataActivity_et_x5.getText().toString().trim();
                String freq = SxbGetDataActivity_et_freq.getText().toString().trim();
                String netId = SxbGetDataActivity_et_netId.getText().toString().trim();
                String addr = SxbGetDataActivity_et_meterAddr.getText().toString().trim();
                if (addr.length() > 10) {
                    HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表地址过长", "提示");
                }
                if (netId.length() > 4) {
                    HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "网络ID过长", "提示");
                }
                if (HzyUtils.isEmpty(freq)) {
                    freq = "0";
                }
                freq = Integer.toHexString(Integer.parseInt(freq));
                //                    freq = Integer.parseInt(freq, 16) + "";
                if (freq.length() > 4) {
                    HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表频率过大", "提示");
                }
                while (freq.length() < 4) {
                    freq = "0" + freq;
                }
                while (netId.length() < 4) {
                    netId = "0" + netId;
                }
                if (addr.length() == 0) {
                    addr = "aaaaaaaaaa";
                }
                while (addr.length() < 10) {
                    addr = "0" + addr;
                }
                if (HzyUtils.isEmpty(y) || HzyUtils.isEmpty(x1) || HzyUtils.isEmpty(x2)
                        || HzyUtils.isEmpty(x3) || HzyUtils.isEmpty(x4) || HzyUtils.isEmpty(x5)) {
                    HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "坐标不得为空", "错误");
                    return;
                } else {
                    y = Integer.toHexString(Integer.parseInt(y));
                    while (y.length() < 4) {
                        y = "0" + y;
                    }
                    y = HzyUtils.changeString1(y);
                    x1 = Integer.toHexString(Integer.parseInt(x1));
                    while (x1.length() < 4) {
                        x1 = "0" + x1;
                    }
                    x1 = HzyUtils.changeString1(x1);
                    x2 = Integer.toHexString(Integer.parseInt(x2));
                    while (x2.length() < 4) {
                        x2 = "0" + x2;
                    }
                    x2 = HzyUtils.changeString1(x2);
                    x3 = Integer.toHexString(Integer.parseInt(x3));
                    while (x3.length() < 4) {
                        x3 = "0" + x3;
                    }
                    x3 = HzyUtils.changeString1(x3);
                    x4 = Integer.toHexString(Integer.parseInt(x4));
                    while (x4.length() < 4) {
                        x4 = "0" + x4;
                    }
                    x4 = HzyUtils.changeString1(x4);
                    x5 = Integer.toHexString(Integer.parseInt(x5));
                    while (x5.length() < 4) {
                        x5 = "0" + x5;
                    }
                    x5 = HzyUtils.changeString1(x5);


                    sendMsg = "68" + freq + netId + addr + "2311a0f000" + "8e0c" + y + x1 + x2 + x3 + x4 + x5;
                    MenuActivity.sendCmd(sendMsg);
                    Log.d("limbo", sendMsg);


                }


            }
        });
        /**
         * 读数据
         */
        SxbGetDataActivity_btn_getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    //                    HzyUtils.showProgressDialog(LoRa_SxbGetDataActivity.this);
                    //                    SxbGetDataActivity_tv_showMsg.setText("");
                    String addr = SxbGetDataActivity_et_meterAddr.getText().toString().trim();
                    String freq = SxbGetDataActivity_et_freq.getText().toString().trim();
                    String netId = SxbGetDataActivity_et_netId.getText().toString().trim();
                    if (addr.length() > 10) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表地址过长", "提示");
                    }
                    if (netId.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "网络ID过长", "提示");
                    }
                    if (HzyUtils.isEmpty(freq)) {
                        freq = "0";
                    }
                    freq = Integer.toHexString(Integer.parseInt(freq));
                    //                    freq = Integer.parseInt(freq, 16) + "";
                    if (freq.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表频率过大", "提示");
                    }
                    while (freq.length() < 4) {
                        freq = "0" + freq;
                    }
                    while (netId.length() < 4) {
                        netId = "0" + netId;
                    }
                    if (addr.length() == 0) {
                        addr = "aaaaaaaaaa";
                    }
                    while (addr.length() < 10) {
                        addr = "0" + addr;
                    }

                    sendMsg = "68" +
                            freq +//频率
                            netId +//网络ID
                            addr +
                            "21" +//控制码
                            "03" +//数据长度
                            "901f" +//数据标志
                            "00"//序号
                    ;
                    Log.d("limbo", sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    SxbGetDataActivity_tv_showMsg.setText("发送读取参数指令:\n水表地址:" + addr
                            + "\n频率:" + freq
                            + "\n网络ID:" + netId);
                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表

                }
            }
        });
        /**
         * 自动定位
         */
        SxbGetDataActivity_btn_AutoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    //                    HzyUtils.showProgressDialog(LoRa_SxbGetDataActivity.this);
                    //                    SxbGetDataActivity_tv_showMsg.setText("");
                    String addr = SxbGetDataActivity_et_meterAddr.getText().toString().trim();
                    String freq = SxbGetDataActivity_et_freq.getText().toString().trim();
                    String netId = SxbGetDataActivity_et_netId.getText().toString().trim();
                    if (addr.length() > 10) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表地址过长", "提示");
                    }
                    if (netId.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "网络ID过长", "提示");
                    }
                    if (HzyUtils.isEmpty(freq)) {
                        freq = "0";
                    }
                    freq = Integer.toHexString(Integer.parseInt(freq));
                    //                    freq = Integer.parseInt(freq, 16) + "";
                    if (freq.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表频率过大", "提示");
                    }
                    while (freq.length() < 4) {
                        freq = "0" + freq;
                    }
                    while (netId.length() < 4) {
                        netId = "0" + netId;
                    }
                    if (addr.length() == 0) {
                        addr = "aaaaaaaaaa";
                    }
                    while (addr.length() < 10) {
                        addr = "0" + addr;
                    }

                    sendMsg = "68" +
                            freq +//频率
                            netId +//网络ID
                            addr +
                            "30" +//控制码
                            "04" +//数据长度
                            "aa04" +//数据标志
                            "0000"//序号
                    ;
                    Log.d("limbo", sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    SxbGetDataActivity_tv_showMsg.setText("发送读取参数指令:\n水表地址:" + addr
                            + "\n频率:" + freq
                            + "\n网络ID:" + netId);
                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表

                }
            }
        });
        LoRa_SxbGetDataActivity_btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String y = LoRa_SxbGetDataActivity_et_y.getText().toString().trim();
                String x1 = LoRa_SxbGetDataActivity_et_x1.getText().toString().trim();
                String x2 = LoRa_SxbGetDataActivity_et_x2.getText().toString().trim();
                String x3 = LoRa_SxbGetDataActivity_et_x3.getText().toString().trim();
                String x4 = LoRa_SxbGetDataActivity_et_x4.getText().toString().trim();
                String x5 = LoRa_SxbGetDataActivity_et_x5.getText().toString().trim();
                String freq = SxbGetDataActivity_et_freq.getText().toString().trim();
                String netId = SxbGetDataActivity_et_netId.getText().toString().trim();
                String addr = SxbGetDataActivity_et_meterAddr.getText().toString().trim();
                if (addr.length() > 10) {
                    HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表地址过长", "提示");
                }
                if (netId.length() > 4) {
                    HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "网络ID过长", "提示");
                }
                if (HzyUtils.isEmpty(freq)) {
                    freq = "0";
                }
                freq = Integer.toHexString(Integer.parseInt(freq));
                //                    freq = Integer.parseInt(freq, 16) + "";
                if (freq.length() > 4) {
                    HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表频率过大", "提示");
                }
                while (freq.length() < 4) {
                    freq = "0" + freq;
                }
                while (netId.length() < 4) {
                    netId = "0" + netId;
                }
                if (addr.length() == 0) {
                    addr = "aaaaaaaaaa";
                }
                while (addr.length() < 10) {
                    addr = "0" + addr;
                }
                sendMsg = "68" + freq + netId + addr + "220581f000" + "8e0c";
                MenuActivity.sendCmd(sendMsg);
                Log.d("limbo", sendMsg);


            }
        });
        /**
         * 快速读图
         */
        SxbGetDataActivity_btn_Ksdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    ReadPic = true;
                    HzyUtils.showProgressDialog(LoRa_SxbGetDataActivity.this);
                    String addr = SxbGetDataActivity_et_meterAddr.getText().toString().trim();
                    String freq = SxbGetDataActivity_et_freq.getText().toString().trim();
                    String netId = SxbGetDataActivity_et_netId.getText().toString().trim();
                    if (addr.length() > 10) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表地址过长", "提示");
                    }
                    if (netId.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "网络ID过长", "提示");
                    }
                    if (HzyUtils.isEmpty(freq)) {
                        freq = "0";
                    }
                    freq = Integer.toHexString(Integer.parseInt(freq));
                    //                    freq = Integer.parseInt(freq, 16) + "";
                    if (freq.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表频率过大", "提示");
                    }
                    while (freq.length() < 4) {
                        freq = "0" + freq;
                    }
                    while (netId.length() < 4) {
                        netId = "0" + netId;
                    }
                    if (addr.length() == 0) {
                        addr = "aaaaaaaaaa";
                    }
                    while (addr.length() < 10) {
                        addr = "0" + addr;
                    }

                    sendMsg = "68" +
                            freq +//频率
                            netId +//网络ID
                            addr +
                            "31" +//控制码
                            "03" +//数据长度
                            "90f0" +//数据标志
                            "00"  //序号
                    ;
                    MenuActivity.sendCmd(sendMsg);
                    Log.d("limbo", sendMsg);
                    SxbGetDataActivity_tv_showMsg.setText("发送读图指令:\n水表地址:" + addr
                            + "\n频率:" + freq
                            + "\n网络ID:" + netId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                prepareTimeStart();
                                //                                Thread.sleep(8000);
                                while (!timeOut) {
                                    int msgLength = MenuActivity.Cjj_CB_MSG.length();
                                    Thread.sleep(500);

                                    if (MenuActivity.Cjj_CB_MSG.length() == msgLength && msgLength >= 1150) {
                                        timeOut = true;
                                    }

                                }
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (!MenuActivity.Cjj_CB_MSG.contains("0a")) {
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
                                HzyUtils.closeProgressDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                                HzyUtils.closeProgressDialog();
                            }

                        }
                    }).start();
                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表

                }
            }
        });

    }

    public static int method1(String str, String key) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.contains(key)) {
                str = str.substring(str.indexOf(key) + key.length());
                count++;
            }
        }
        Log.d("limbo", count + "");
        return count;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00://安美通LoRa表
                    String getMsg = msg.obj.toString();
                    String show = HzyUtils.toStringHex1(getMsg).replaceAll("�", "");
                    SxbGetDataActivity_tv_showMsg.append("\n" + show);
                    break;
                case 0x01://山科LoRa表
                    getMsg = msg.obj.toString();
                    show = HzyUtils.toStringHex1(getMsg).replaceAll("�", "").replaceAll("\n", "");
                    String flag = show.substring(0, 2);
                    Log.d("limbo", show);

                    if (flag.contains("00")) {//磁铁
                        String dataMsg = show.substring(6, 42);
                        String addr = dataMsg.substring(4, 14);
                        String netid = dataMsg.substring(0, 4);
                        String freq = dataMsg.substring(18, 22);
                        String version = dataMsg.substring(22, 36);
                        SxbGetDataActivity_tv_showMsg.append("\n" + "编号:" + version + "\n" + show);

                        if (!HzyUtils.isEmpty(freq)) {
                            freq = Integer.parseInt(freq, 16) + "";
                        }
                        SxbGetDataActivity_et_meterAddr.setText(addr);
                        SxbGetDataActivity_et_netId.setText(netid);
                        SxbGetDataActivity_et_freq.setText(freq);
                    } else if (flag.contains("21") || flag.contains("30")) {//读数
                        SxbGetDataActivity_tv_showMsg.append("\n" + show);
                        show = show.substring(6);
                        String data = Integer.parseInt(show.substring(5, 6) + show.substring(2, 4) + show.substring(0, 2)) + "." + show.substring(17, 18)+MenuActivity.SECK_PARAM;
                        String sxsbds = show.substring(14, 15) + "." + show.substring(23, 24) + "     " +
                                show.substring(15, 16) + "." + show.substring(22, 23) + "     " +
                                show.substring(12, 13) + "." + show.substring(21, 22) + "     " +
                                show.substring(13, 14) + "." + show.substring(20, 21) + "     " +
                                show.substring(10, 11) + "." + show.substring(19, 20) + "     " +
                                show.substring(11, 12) + "." + show.substring(18, 19) + "     " +
                                show.substring(8, 9) + "." + show.substring(16, 17) + "     " +
                                show.substring(9, 10) + "." + show.substring(17, 18) + "     ";
                        String zxd = Integer.parseInt(show.substring(38, 40), 16) + "   " +
                                Integer.parseInt(show.substring(36, 38), 16) + "   " +
                                Integer.parseInt(show.substring(34, 36), 16) + "   " +
                                Integer.parseInt(show.substring(32, 34), 16) + "   " +
                                Integer.parseInt(show.substring(30, 32), 16) + "   " +
                                Integer.parseInt(show.substring(28, 30), 16) + "   " +
                                Integer.parseInt(show.substring(26, 28), 16) + "   " +
                                Integer.parseInt(show.substring(24, 26), 16) + "   ";
                        SxbGetDataActivity_tv_meterSbds.setText(data);
                        SxbGetDataActivity_tv_meterSxsbds.setText(sxsbds);
                        SxbGetDataActivity_tv_meterZxd.setText(zxd);

                    } else if ( flag.contains("23")) {
                        show = show.substring(6, 30);
                        HintDialog.ShowHintDialog2(LoRa_SxbGetDataActivity.this,
                                "Y坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(0, 4)), 16) +
                                        "\nX1坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(4, 8)), 16) +
                                        "\nX2坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(8, 12)), 16) +
                                        "\nX3坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(12, 16)), 16) +
                                        "\nX4坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(16, 20)), 16) +
                                        "\nX5坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(20, 24)), 16)
                                , "接收数据");
                    } else if(flag.contains("22")){
                        show = show.substring(6, 30);
                        /*HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this,
                                "Y坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(0, 4)), 16) +
                                        "\nX1坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(4, 8)), 16) +
                                        "\nX2坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(8, 12)), 16) +
                                        "\nX3坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(12, 16)), 16) +
                                        "\nX4坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(16, 20)), 16) +
                                        "\nX5坐标:" + Integer.parseInt(HzyUtils.changeString1(show.substring(20, 24)), 16)
                                , "接收数据");*/
                        LoRa_SxbGetDataActivity_et_y.setText(Integer.parseInt(HzyUtils.changeString1(show.substring(0, 4)), 16)+"");
                        LoRa_SxbGetDataActivity_et_x1.setText(Integer.parseInt(HzyUtils.changeString1(show.substring(4, 8)), 16)+"");
                        LoRa_SxbGetDataActivity_et_x2.setText(Integer.parseInt(HzyUtils.changeString1(show.substring(8, 12)), 16)+"");
                        LoRa_SxbGetDataActivity_et_x3.setText(Integer.parseInt(HzyUtils.changeString1(show.substring(12, 16)), 16)+"");
                        LoRa_SxbGetDataActivity_et_x4.setText(Integer.parseInt(HzyUtils.changeString1(show.substring(16, 20)), 16)+"");
                        LoRa_SxbGetDataActivity_et_x5.setText(Integer.parseInt(HzyUtils.changeString1(show.substring(20, 24)), 16)+"");

                    }


                    break;
                case 0x02:
                    getMsg = msg.obj.toString();
                    String msgx;
                    show = HzyUtils.toStringHex1(getMsg).replaceAll("�", "").replaceAll("\n", "");
                    Log.d("limbo", show);
                    int Hlength = Integer.parseInt(show.substring(2, 4), 16);
                    int Llength = Integer.parseInt(show.substring(4, 6), 16);
                    int CountLength = Integer.parseInt(show.substring(2, 6), 16);
                    Log.d("limbo", CountLength + "");
                    if (CountLength >= 575) {
                        msgx = show.substring(6 + CountLength * 2);
                        show = show.substring(6, 6 + CountLength * 2);

                        String[] showMsg = new String[5];
                        for (int i = 0; i < 5; i++) {
                            showMsg[i] = show.substring(0 + i * 230, 230 * (i + 1));
                            try {
                                Bitmap bp = null;
                                byte[] picdata = HzyUtils.getHexBytes(showMsg[i]);
                                bp = BluetoothConnectThread.binaryPicBytesToBitmap(picdata);
                                if (bp == null) {
                                    Log.e("limbo", i + ":图片为空");
                                } else {
                                    Log.d("limbo", "bitmap success");
                                    int imgboxlist[] = {R.id.ib_01, R.id.ib_02, R.id.ib_03,
                                            R.id.ib_04, R.id.ib_05, R.id.ib_06, R.id.ib_07,
                                            R.id.ib_08};

                                    ImageView iv = (ImageView) findViewById(imgboxlist[i]);
                                    try {
                                        iv.setImageBitmap((Bitmap) bp);
                                    } catch (Exception e) {
                                        HintDialog.ShowHintDialog(thisView, "图像异常", "错误");
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("limbo", e.toString());
                            }
                            SxbGetDataActivity_tv_showMsg.append("\n" + msgx);
                        }
                    } else {

                        SxbGetDataActivity_tv_showMsg.append("\n" + "图片数据不完整,请重试.");
                    }
                    ReadPic = false;
                    HzyUtils.closeProgressDialog();

                    break;
                case 0x98:
                    HzyUtils.closeProgressDialog();
                    SxbGetDataActivity_tv_showMsg.append("\n" + "读图失败,请重试.");
                    //HintDialog.ShowHintDialog(SxbGetDataActivity.this, "未接收到数据", "提示");

                    break;
                case 0x99:
                    MenuActivity.btAuto = true;
                    HzyUtils.closeProgressDialog();
                    SxbGetDataActivity_tv_showMsg.append("\n" + "数据接收失败,请重试.");
                    //                    HintDialog.ShowHintDialog(SxbGetDataActivity.this, "未接收到数据", "提示");
                    break;
            }
        }
    };

    /**
     * 开始协议设定时间
     */
    private void prepareTimeStart() {
        timeOut = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    timeOut = true;
                } catch (Exception e) {

                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        MenuActivity.btAuto = false;
        super.onDestroy();
    }
}
