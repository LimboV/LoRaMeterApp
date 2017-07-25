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

    @BindView(R.id.SxbGetDataActivity_btn_getData)
    Button SxbGetDataActivity_btn_getData;

    @BindView(R.id.SxbGetDataActivity_btn_getVersion)
    Button SxbGetDataActivity_btn_getVersion;

    @BindView(R.id.SxbGetDataActivity_btn_AutoLocation)
    Button SxbGetDataActivity_btn_AutoLocation;

    @BindView(R.id.SxbGetDataActivity_btn_GetLocation)
    Button SxbGetDataActivity_btn_GetLocation;

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
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                prepareTimeStart();
                                Thread.sleep(6000);
                                while (!timeOut) {
                                    int msgLength = MenuActivity.Cjj_CB_MSG.length();
                                    Thread.sleep(500);

                                    if (MenuActivity.Cjj_CB_MSG.length() == msgLength) {
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
                                    message.what = 0x01;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                                HzyUtils.closeProgressDialog();
                            }

                        }
                    }).start();*/
                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表

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
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                prepareTimeStart();
                                Thread.sleep(6000);
                                while (!timeOut) {
                                    int msgLength = MenuActivity.Cjj_CB_MSG.length();
                                    Thread.sleep(500);

                                    if (MenuActivity.Cjj_CB_MSG.length() == msgLength) {
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
                                    message.what = 0x01;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                                HzyUtils.closeProgressDialog();
                            }

                        }
                    }).start();*/
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
                            "00"//序号
                    ;
                    Log.d("limbo", sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    SxbGetDataActivity_tv_showMsg.setText("发送读取参数指令:\n水表地址:" + addr
                            + "\n频率:" + freq
                            + "\n网络ID:" + netId);
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                prepareTimeStart();
                                Thread.sleep(6000);
                                while (!timeOut) {
                                    int msgLength = MenuActivity.Cjj_CB_MSG.length();
                                    Thread.sleep(500);

                                    if (MenuActivity.Cjj_CB_MSG.length() == msgLength) {
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
                                    message.what = 0x01;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                                HzyUtils.closeProgressDialog();
                            }

                        }
                    }).start();*/
                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表

                }
            }
        });
        /**
         * 读取坐标
         */
        SxbGetDataActivity_btn_GetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    HzyUtils.showProgressDialog(LoRa_SxbGetDataActivity.this);
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
                    //                    freq = Integer.parseInt(netId, 16) + "";
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
                            "22" +//控制码
                            "05" +//数据长度
                            "81f0" +//数据标志
                            "00" + //序号
                            "8e0c"//数据
                    ;
                    Log.d("limbo", sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    SxbGetDataActivity_tv_showMsg.setText("发送读取参数指令:\n水表地址:" + addr
                            + "\n频率:" + freq
                            + "\n网络ID:" + netId);
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                prepareTimeStart();
                                Thread.sleep(6000);
                                while (!timeOut) {
                                    int msgLength = MenuActivity.Cjj_CB_MSG.length();
                                    Thread.sleep(500);

                                    if (MenuActivity.Cjj_CB_MSG.length() == msgLength) {
                                        timeOut = true;
                                    }

                                    //                                    if (method1(MenuActivity.Cjj_CB_MSG, "0a") == 7) {
                                    //
                                    //                                        timeOut = true;
                                    //                                    }
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
                                    message.what = 0x01;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                                HzyUtils.closeProgressDialog();
                            }

                        }
                    }).start();*/
                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表

                }
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
                    } else if (flag.contains("21")) {//读数
                        SxbGetDataActivity_tv_showMsg.append("\n" + show);
                        show = show.substring(6);
                        String data = Integer.parseInt(show.substring(5, 6) + show.substring(2, 4) + show.substring(0, 2))+"."+show.substring(16, 17);
                        String sxsbds = show.substring(14, 15) + "." + show.substring(23, 24) + "     " +
                                show.substring(15, 16) + "." + show.substring(22, 23) + "     " +
                                show.substring(12, 13) + "." + show.substring(21, 22) + "     " +
                                show.substring(13, 14) + "." + show.substring(20, 21) + "     " +
                                show.substring(10, 11) + "." + show.substring(19, 20) + "     " +
                                show.substring(11, 12) + "." + show.substring(18, 19) + "     " +
                                show.substring(8, 9) + "." + show.substring(17, 18) + "     " +
                                show.substring(9, 10) + "." + show.substring(16, 17) + "     ";
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

                    }


                    break;
                case 0x02:
                    getMsg = msg.obj.toString();
                    String msgx;
                    show = HzyUtils.toStringHex1(getMsg).replaceAll("�", "").replaceAll("\n","");
                    Log.d("limbo", show);
                    int Hlength = Integer.parseInt(show.substring(2, 4), 16);
                    int Llength = Integer.parseInt(show.substring(4, 6), 16);
                    int CountLength = Integer.parseInt(show.substring(2,6),16);
                    Log.d("limbo",CountLength+"");
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
