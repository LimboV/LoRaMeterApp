package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_PasswordDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ssHss on 2016/8/31.
 */
public class LoRa_LoRaSettingActivity extends Activity {
    @BindView(R.id.LoRaSettingActivity_btn_deleteAllJd)
    Button btnDeleteAllJd;

    @BindView(R.id.LoRaSettingActivity_btn_GetNetId)
    Button btnGetNetId;

    @BindView(R.id.LoRaSettingActivity_btn_SendNetId)
    Button btnSendNetId;

    @BindView(R.id.LoRaSettingActivity_btn_GetNetFreq)
    Button btnGetNetFreq;

    @BindView(R.id.LoRaSettingActivity_btn_SendNetFreq)
    Button btnSendNetFreq;

    @BindView(R.id.LoRaSettingActivity_btn_GetCbCount)
    Button btnGetCbCount;

    @BindView(R.id.LoRaSettingActivity_btn_AddJd)
    Button btnAddJd;

    @BindView(R.id.LoRaSettingActivity_btn_DeleteJd)
    Button btnDeleteJd;

    @BindView(R.id.LoRaSettingActivity_btn_ThJd)
    Button btnThJd;

    @BindView(R.id.LoRaSettingActivity_btn_getJdMsg)
    Button btnGetJdMsg;

    @BindView(R.id.LoRaSettingActivity_btn_AddLy)
    Button btnAddLy;

    @BindView(R.id.LoRaSettingActivity_btn_DeleteLy)
    Button btnDeleteLy;

    @BindView(R.id.LoRaSettingActivity_btn_getLyMsg)
    Button btnGetLyMsg;

    private EditText etNetId, etNetFreq, etCbCount, etAddJd, etAddFc0, etAddFc1, etAddFc2, etAddFc3, etAddFc4,
            etAddFc5, etAddFc6, etAddFc7, etAddFc8, etAddFc9, etAddFc10, etAddFc11, etAddFc12, etAddFc13, etAddFc14,
            etAddFc15, etAddFc16, etAddFc17, etAddFc18, etAddFc19,
            etDeleteJd, etThJd, etBthJd, etAddLy, etDeleteLy;
    private TextView tvJdMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        //        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_lorasettingactivity);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        ButterKnife.bind(this);
        etNetId = (EditText) findViewById(R.id.LoRaSettingActivity_et_NetId);
        etNetFreq = (EditText) findViewById(R.id.LoRaSettingActivity_et_NetFreq);
        etCbCount = (EditText) findViewById(R.id.LoRaSettingActivity_et_CbCount);
        etAddJd = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddJd);
        etAddFc0 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc0);
        etAddFc1 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc1);
        etAddFc2 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc2);
        etAddFc3 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc3);
        etAddFc4 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc4);
        etAddFc5 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc5);
        etAddFc6 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc6);
        etAddFc7 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc7);
        etAddFc8 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc8);
        etAddFc9 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc9);
        etAddFc10 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc10);
        etAddFc11 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc11);
        etAddFc12 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc12);
        etAddFc13 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc13);
        etAddFc14 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc14);
        etAddFc15 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc15);
        etAddFc16 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc16);
        etAddFc17 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc17);
        etAddFc18 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc18);
        etAddFc19 = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddFc19);
        etDeleteJd = (EditText) findViewById(R.id.LoRaSettingActivity_et_DeleteJd);
        etAddLy = (EditText) findViewById(R.id.LoRaSettingActivity_et_AddLy);
        etDeleteLy = (EditText) findViewById(R.id.LoRaSettingActivity_et_DeleteLy);
        etThJd = (EditText) findViewById(R.id.LoRaSettingActivity_et_thJd);
        etBthJd = (EditText) findViewById(R.id.LoRaSettingActivity_et_bthJd);

        tvJdMsg = (TextView) findViewById(R.id.LoRaSettingActivity_tv_jdMsg);

        /**
         * 获取网络ID
         */
        btnGetNetId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "000300d00001";
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x00;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
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
         * 设置网络ID
         */
        btnSendNetId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    String userMsg = etNetId.getText().toString().trim();
                    if (userMsg.length() <= 4) {
                        while (userMsg.length() < 4) {
                            userMsg = "0" + userMsg;
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "数据过长", "错误");
                        return;
                    }
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "001600d3006102" + userMsg;
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "设置:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(300);
                                HzyUtils.closeProgressDialog();
                            } catch (Exception e) {
                                HzyUtils.closeProgressDialog();
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
        /**
         *获取网络频率
         */
        btnGetNetFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "000300d10002";
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
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
                                HzyUtils.closeProgressDialog();
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
         *设置网络频率
         */
        btnSendNetFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    String userMsg = etNetFreq.getText().toString().trim();
                    if (userMsg.contains(".")) {

                    } else {
                        userMsg = userMsg + ".";
                    }
                    String Hmsg = userMsg.substring(0, userMsg.indexOf("."));
                    String Lmsg = userMsg.substring(userMsg.indexOf(".") + 1);

                    while (Hmsg.length() < 3) {
                        Hmsg = "0" + Hmsg;
                    }
                    while (Lmsg.length() < 3) {
                        Lmsg = Lmsg + "0";
                    }
                    userMsg = Hmsg + Lmsg;
                    Log.d("limbo", userMsg);
                    userMsg = "00" + userMsg;
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "001600d3006304" + userMsg;
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "设置:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(300);
                                HzyUtils.closeProgressDialog();
                            } catch (Exception e) {
                                HzyUtils.closeProgressDialog();
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
        /**
         *获取集抄次数
         */
        btnGetCbCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "000300d20001";
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
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
                                HzyUtils.closeProgressDialog();
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
         *添加路由
         */
        btnAddLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String ly = etAddLy.getText().toString().trim();
                    if (ly.length() == 0) {
                        ly = "0";
                    }
                    while (ly.length() < 2) {
                        ly = "0" + ly;
                    }
                    String sendMsg = "001600d9000101" + ly;

                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "添加路由:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x07;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
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
         *删除路由
         */
        btnDeleteLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String ly = etDeleteLy.getText().toString().trim();
                    if (ly.length() == 0) {
                        ly = "0";
                    }
                    while (ly.length() < 2) {
                        ly = "0" + ly;
                    }
                    String sendMsg = "001600d9000201" + ly;

                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "删除路由:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1500);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x08;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
                            }        catch (InterruptedException e) {
                                HzyUtils.closeProgressDialog();
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
        /**
         *添加节点
         */
        btnAddJd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                String jd = etAddJd.getText().toString().trim();
                if (jd.length() == 0) {
                    jd = "0";
                }
                String fc0 = etAddFc0.getText().toString().trim();

                if (fc0.length() == 0) {
                    fc0 = "0";
                }
                fc0 = Integer.toHexString(Integer.parseInt(fc0));

                String fc1 = etAddFc1.getText().toString().trim();
                if (fc1.length() == 0) {
                    fc1 = "0";
                }
                fc1 = Integer.toHexString(Integer.parseInt(fc1));

                String fc2 = etAddFc2.getText().toString().trim();
                if (fc2.length() == 0) {
                    fc2 = "0";
                }
                fc2 = Integer.toHexString(Integer.parseInt(fc2));

                String fc3 = etAddFc3.getText().toString().trim();
                if (fc3.length() == 0) {
                    fc3 = "0";
                }
                fc3 = Integer.toHexString(Integer.parseInt(fc3));

                String fc4 = etAddFc4.getText().toString().trim();
                if (fc4.length() == 0) {
                    fc4 = "0";
                }
                fc4 = Integer.toHexString(Integer.parseInt(fc4));

                String fc5 = etAddFc5.getText().toString().trim();
                if (fc5.length() == 0) {
                    fc5 = "0";
                }
                fc5 = Integer.toHexString(Integer.parseInt(fc5));

                String fc6 = etAddFc6.getText().toString().trim();
                if (fc6.length() == 0) {
                    fc6 = "0";
                }
                fc6 = Integer.toHexString(Integer.parseInt(fc6));

                String fc7 = etAddFc7.getText().toString().trim();
                if (fc7.length() == 0) {
                    fc7 = "0";
                }
                fc7 = Integer.toHexString(Integer.parseInt(fc7));

                String fc8 = etAddFc8.getText().toString().trim();
                if (fc8.length() == 0) {
                    fc8 = "0";
                }
                fc8 = Integer.toHexString(Integer.parseInt(fc8));

                String fc9 = etAddFc9.getText().toString().trim();
                if (fc9.length() == 0) {
                    fc9 = "0";
                }
                fc9 = Integer.toHexString(Integer.parseInt(fc9));

                String fc10 = etAddFc10.getText().toString().trim();
                if (fc10.length() == 0) {
                    fc10 = "0";
                }
                fc10 = Integer.toHexString(Integer.parseInt(fc10));

                String fc11 = etAddFc11.getText().toString().trim();
                if (fc11.length() == 0) {
                    fc11 = "0";
                }
                fc11 = Integer.toHexString(Integer.parseInt(fc11));

                String fc12 = etAddFc12.getText().toString().trim();
                if (fc12.length() == 0) {
                    fc12 = "0";
                }
                fc12 = Integer.toHexString(Integer.parseInt(fc12));

                String fc13 = etAddFc13.getText().toString().trim();
                if (fc13.length() == 0) {
                    fc13 = "0";
                }
                fc13 = Integer.toHexString(Integer.parseInt(fc13));

                String fc14 = etAddFc14.getText().toString().trim();
                if (fc14.length() == 0) {
                    fc14 = "0";
                }
                fc14 = Integer.toHexString(Integer.parseInt(fc14));

                String fc15 = etAddFc15.getText().toString().trim();
                if (fc15.length() == 0) {
                    fc15 = "0";
                }
                fc15 = Integer.toHexString(Integer.parseInt(fc15));

                String fc16 = etAddFc16.getText().toString().trim();
                if (fc16.length() == 0) {
                    fc16 = "0";
                }
                fc16 = Integer.toHexString(Integer.parseInt(fc16));

                String fc17 = etAddFc17.getText().toString().trim();
                if (fc17.length() == 0) {
                    fc17 = "0";
                }
                fc17 = Integer.toHexString(Integer.parseInt(fc17));

                String fc18 = etAddFc18.getText().toString().trim();
                if (fc18.length() == 0) {
                    fc18 = "0";
                }
                fc18 = Integer.toHexString(Integer.parseInt(fc18));

                String fc19 = etAddFc19.getText().toString().trim();
                if (fc19.length() == 0) {
                    fc19 = "0";
                }
                fc19 = Integer.toHexString(Integer.parseInt(fc19));


                if (jd.length() > 10) {
                    HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "数据过长", "错误");
                }
                if (fc0.length() > 2 || fc1.length() > 2 || fc2.length() > 2 || fc3.length() > 2
                        || fc4.length() > 2 || fc5.length() > 2 || fc6.length() > 2 || fc7.length() > 2
                        || fc8.length() > 2 || fc9.length() > 2 || fc10.length() > 2 || fc11.length() > 2
                        || fc12.length() > 2 || fc13.length() > 2 || fc14.length() > 2 || fc15.length() > 2
                        || fc16.length() > 2 || fc17.length() > 2 || fc18.length() > 2 || fc19.length() > 2) {
                    HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "数据过长", "错误");
                }
                while (jd.length() < 10) {
                    jd = "0" + jd;
                }
                while (fc0.length() < 2) {
                    fc0 = "0" + fc0;
                }
                while (fc1.length() < 2) {
                    fc1 = "0" + fc1;
                }
                while (fc2.length() < 2) {
                    fc2 = "0" + fc2;
                }
                while (fc3.length() < 2) {
                    fc3 = "0" + fc3;
                }
                while (fc4.length() < 2) {
                    fc4 = "0" + fc4;
                }
                while (fc5.length() < 2) {
                    fc5 = "0" + fc5;
                }
                while (fc6.length() < 2) {
                    fc6 = "0" + fc6;
                }
                while (fc7.length() < 2) {
                    fc7 = "0" + fc7;
                }
                while (fc8.length() < 2) {
                    fc8 = "0" + fc8;
                }
                while (fc9.length() < 2) {
                    fc9 = "0" + fc9;
                }
                while (fc10.length() < 2) {
                    fc10 = "0" + fc10;
                }
                while (fc11.length() < 2) {
                    fc11 = "0" + fc11;
                }
                while (fc12.length() < 2) {
                    fc12 = "0" + fc12;
                }
                while (fc13.length() < 2) {
                    fc13 = "0" + fc13;
                }
                while (fc14.length() < 2) {
                    fc14 = "0" + fc14;
                }
                while (fc15.length() < 2) {
                    fc15 = "0" + fc15;
                }
                while (fc16.length() < 2) {
                    fc16 = "0" + fc16;
                }
                while (fc17.length() < 2) {
                    fc17 = "0" + fc17;
                }
                while (fc18.length() < 2) {
                    fc18 = "0" + fc18;
                }
                while (fc19.length() < 2) {
                    fc19 = "0" + fc19;
                }
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "001600d40010" + jd + fc0 + fc1 + fc2 + fc3 + fc4 + fc5 + fc6 + fc7
                            + fc8 + fc9 + fc10 + fc11 + fc12 + fc13 + fc14 + fc15 + fc16 + fc17 + fc18 + fc19 + "00000000000000";
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x03;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
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
         *删除节点
         */
        btnDeleteJd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jd = etDeleteJd.getText().toString();
                if (jd.length() > 10) {
                    HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "数据过长", "错误");
                }
                while (jd.length() < 10) {
                    jd = "0" + jd;
                }
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "001600d50001" + jd;
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
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
                                HzyUtils.closeProgressDialog();
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
         *替换节点
         */
        btnThJd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thJd = etThJd.getText().toString().trim();
                String bthJd = etBthJd.getText().toString().trim();
                if (thJd.length() <= 10) {
                    while (thJd.length() < 10) {
                        thJd = "0" + thJd;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "数据过大", "错误");
                }
                if (bthJd.length() <= 10) {
                    while (bthJd.length() < 10) {
                        bthJd = "0" + bthJd;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "数据过大", "错误");
                }
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "001600d60002" + thJd + bthJd;
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(1000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x05;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);

                                }
                                HzyUtils.closeProgressDialog();
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
         *获取节点信息
         */
        btnGetJdMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    tvJdMsg.setText("");
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "001600d70001ffff";
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                int AllCount = Integer.parseInt(getMsg.substring(4, 6), 16);//总节点
                                while (getMsg.length() != AllCount * 76) {
                                    getMsg = MenuActivity.Cjj_CB_MSG;
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                }


                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x06;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
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
         *获取路由信息
         */
        btnGetLyMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    tvJdMsg.setText("");
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "001600d9000301ff9bc9";
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x09;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }
                                HzyUtils.closeProgressDialog();
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
         * 删除所有节点信息
         */
        btnDeleteAllJd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPsw() == 1) {
                    /**
                     * 根据表类型使用不同的通讯协议
                     */
                    if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                    } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                        HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                        String sendMsg = "001600d80001ffff47a9";
                        Log.d("limbo", "获取:" + sendMsg);
                        MenuActivity.sendCmd(sendMsg);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Thread.sleep(6000);
                                    String getMsg = MenuActivity.Cjj_CB_MSG;
                                    if (getMsg.length() == 0) {
                                        Message message = new Message();
                                        message.what = 0x99;
                                        message.obj = getMsg;
                                        mHandler.sendMessage(message);
                                    } else {
                                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                        Log.d("limbo", getMsg);

                                        Message message = new Message();
                                        message.what = 0x10;
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
            }
        });
        loadUser();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 * 读网络ID
                 */
                case 0x00:
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 10);
                        etNetId.setText(getMsg);
                    }

                    break;
                /**
                 * 读网络频率
                 */
                case 0x01:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(8, 11) + "." + getMsg.substring(11, 14);
                        etNetFreq.setText(getMsg);
                    }

                    break;
                /**
                 * 读集抄次数
                 */
                case 0x02:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 10);
                        etCbCount.setText(getMsg);
                    }

                    break;
                /**
                 * 添加节点
                 */
                case 0x03:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 71) {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(8, 72);
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this,
                                "节点号:" + getMsg.substring(0, 10)
                                        + "\n分采号1:  " + Integer.parseInt(getMsg.substring(10, 12), 16)
                                        + "\n分采号2:  " + Integer.parseInt(getMsg.substring(12, 14), 16)
                                        + "\n分采号3:  " + Integer.parseInt(getMsg.substring(14, 16), 16)
                                        + "\n分采号4:  " + Integer.parseInt(getMsg.substring(16, 18), 16)
                                        + "\n分采号5:  " + Integer.parseInt(getMsg.substring(18, 20), 16)
                                        + "\n分采号6:  " + Integer.parseInt(getMsg.substring(20, 22), 16)
                                        + "\n分采号7:  " + Integer.parseInt(getMsg.substring(22, 24), 16)
                                        + "\n分采号8:  " + Integer.parseInt(getMsg.substring(24, 26), 16)
                                        + "\n分采号9:  " + Integer.parseInt(getMsg.substring(26, 28), 16)
                                        + "\n分采号10:  " + Integer.parseInt(getMsg.substring(28, 30), 16)
                                        + "\n分采号11:  " + Integer.parseInt(getMsg.substring(30, 32), 16)
                                        + "\n分采号12:  " + Integer.parseInt(getMsg.substring(32, 34), 16)
                                        + "\n分采号13:  " + Integer.parseInt(getMsg.substring(34, 36), 16)
                                        + "\n分采号14:  " + Integer.parseInt(getMsg.substring(36, 38), 16)
                                        + "\n分采号15:  " + Integer.parseInt(getMsg.substring(38, 40), 16)
                                        + "\n分采号16:  " + Integer.parseInt(getMsg.substring(40, 42), 16)
                                        + "\n分采号17:  " + Integer.parseInt(getMsg.substring(42, 44), 16)
                                        + "\n分采号18:  " + Integer.parseInt(getMsg.substring(44, 46), 16)
                                        + "\n分采号19:  " + Integer.parseInt(getMsg.substring(46, 48), 16)
                                        + "\n分采号20:  " + Integer.parseInt(getMsg.substring(48, 50), 16)
                                , "返回");
                    }

                    break;

                /**
                 * 删除节点
                 */
                case 0x04:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.equals("001600852043")) {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "删除完毕", "成功");
                    } else {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "删除失败", "失败");
                    }

                    break;
                /**
                 * 替换节点
                 */
                case 0x05:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.equals("0016000a61e7")) {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "替换失败", "失败");
                    } else {
                        //                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "替换成功:" + getMsg.substring(10, 12), "成功");
                        getMsg = getMsg.substring(8, 72);
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this,
                                "节点号:" + getMsg.substring(0, 10)
                                        + "\n分采号1:  " + Integer.parseInt(getMsg.substring(10, 12), 16)
                                        + "\n分采号2:  " + Integer.parseInt(getMsg.substring(12, 14), 16)
                                        + "\n分采号3:  " + Integer.parseInt(getMsg.substring(14, 16), 16)
                                        + "\n分采号4:  " + Integer.parseInt(getMsg.substring(16, 18), 16)
                                        + "\n分采号5:  " + Integer.parseInt(getMsg.substring(18, 20), 16)
                                        + "\n分采号6:  " + Integer.parseInt(getMsg.substring(20, 22), 16)
                                        + "\n分采号7:  " + Integer.parseInt(getMsg.substring(22, 24), 16)
                                        + "\n分采号8:  " + Integer.parseInt(getMsg.substring(24, 26), 16)
                                        + "\n分采号9:  " + Integer.parseInt(getMsg.substring(26, 28), 16)
                                        + "\n分采号10:  " + Integer.parseInt(getMsg.substring(28, 30), 16)
                                        + "\n分采号11:  " + Integer.parseInt(getMsg.substring(30, 32), 16)
                                        + "\n分采号12:  " + Integer.parseInt(getMsg.substring(32, 34), 16)
                                        + "\n分采号13:  " + Integer.parseInt(getMsg.substring(34, 36), 16)
                                        + "\n分采号14:  " + Integer.parseInt(getMsg.substring(36, 38), 16)
                                        + "\n分采号15:  " + Integer.parseInt(getMsg.substring(38, 40), 16)
                                        + "\n分采号16:  " + Integer.parseInt(getMsg.substring(40, 42), 16)
                                        + "\n分采号17:  " + Integer.parseInt(getMsg.substring(42, 44), 16)
                                        + "\n分采号18:  " + Integer.parseInt(getMsg.substring(44, 46), 16)
                                        + "\n分采号19:  " + Integer.parseInt(getMsg.substring(46, 48), 16)
                                        + "\n分采号20:  " + Integer.parseInt(getMsg.substring(48, 50), 16)
                                , "返回");
                    }

                    break;
                /**
                 * 返回节点所有信息
                 */
                case 0x06:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    int AllCount = Integer.parseInt(getMsg.substring(4, 6), 16);//总节点
                    /*int needFor = 0;//需要分多少包
                    if (AllCount % 10 > 0) {
                        needFor = AllCount / 10 + 1;
                    } else {
                        needFor = AllCount / 10;
                    }*/
                    for (int i = 1; i <= AllCount; i++) {
                        String fc0 = Integer.parseInt(getMsg.substring(18 + (i - 1) * 76, 20 + (i - 1) * 76), 16) + "";
                        String fc1 = Integer.parseInt(getMsg.substring(20 + (i - 1) * 76, 22 + (i - 1) * 76), 16) + "";
                        String fc2 = Integer.parseInt(getMsg.substring(22 + (i - 1) * 76, 24 + (i - 1) * 76), 16) + "";
                        String fc3 = Integer.parseInt(getMsg.substring(24 + (i - 1) * 76, 26 + (i - 1) * 76), 16) + "";
                        String fc4 = Integer.parseInt(getMsg.substring(26 + (i - 1) * 76, 28 + (i - 1) * 76), 16) + "";
                        String fc5 = Integer.parseInt(getMsg.substring(28 + (i - 1) * 76, 30 + (i - 1) * 76), 16) + "";
                        String fc6 = Integer.parseInt(getMsg.substring(30 + (i - 1) * 76, 32 + (i - 1) * 76), 16) + "";
                        String fc7 = Integer.parseInt(getMsg.substring(32 + (i - 1) * 76, 34 + (i - 1) * 76), 16) + "";
                        String fc8 = Integer.parseInt(getMsg.substring(34 + (i - 1) * 76, 36 + (i - 1) * 76), 16) + "";
                        String fc9 = Integer.parseInt(getMsg.substring(36 + (i - 1) * 76, 38 + (i - 1) * 76), 16) + "";
                        String fc10 = Integer.parseInt(getMsg.substring(38 + (i - 1) * 76, 40 + (i - 1) * 76), 16) + "";
                        String fc11 = Integer.parseInt(getMsg.substring(40 + (i - 1) * 76, 42 + (i - 1) * 76), 16) + "";
                        String fc12 = Integer.parseInt(getMsg.substring(42 + (i - 1) * 76, 44 + (i - 1) * 76), 16) + "";
                        String fc13 = Integer.parseInt(getMsg.substring(44 + (i - 1) * 76, 46 + (i - 1) * 76), 16) + "";
                        String fc14 = Integer.parseInt(getMsg.substring(46 + (i - 1) * 76, 48 + (i - 1) * 76), 16) + "";
                        String fc15 = Integer.parseInt(getMsg.substring(48 + (i - 1) * 76, 50 + (i - 1) * 76), 16) + "";
                        String fc16 = Integer.parseInt(getMsg.substring(50 + (i - 1) * 76, 52 + (i - 1) * 76), 16) + "";
                        String fc17 = Integer.parseInt(getMsg.substring(52 + (i - 1) * 76, 54 + (i - 1) * 76), 16) + "";
                        String fc18 = Integer.parseInt(getMsg.substring(54 + (i - 1) * 76, 56 + (i - 1) * 76), 16) + "";
                        String fc19 = Integer.parseInt(getMsg.substring(56 + (i - 1) * 76, 58 + (i - 1) * 76), 16) + "";
                        while (fc0.length() < 3) {
                            fc0 = "0" + fc0;
                        }
                        while (fc1.length() < 3) {
                            fc1 = "0" + fc1;
                        }
                        while (fc2.length() < 3) {
                            fc2 = "0" + fc2;
                        }
                        while (fc3.length() < 3) {
                            fc3 = "0" + fc3;
                        }
                        while (fc4.length() < 3) {
                            fc4 = "0" + fc4;
                        }
                        while (fc5.length() < 3) {
                            fc5 = "0" + fc5;
                        }
                        while (fc6.length() < 3) {
                            fc6 = "0" + fc6;
                        }
                        while (fc7.length() < 3) {
                            fc7 = "0" + fc7;
                        }
                        while (fc8.length() < 3) {
                            fc8 = "0" + fc8;
                        }
                        while (fc9.length() < 3) {
                            fc9 = "0" + fc9;
                        }
                        while (fc10.length() < 3) {
                            fc10 = "0" + fc10;
                        }
                        while (fc11.length() < 3) {
                            fc11 = "0" + fc11;
                        }
                        while (fc12.length() < 3) {
                            fc12 = "0" + fc12;
                        }
                        while (fc13.length() < 3) {
                            fc13 = "0" + fc13;
                        }
                        while (fc14.length() < 3) {
                            fc14 = "0" + fc14;
                        }
                        while (fc15.length() < 3) {
                            fc15 = "0" + fc15;
                        }
                        while (fc16.length() < 3) {
                            fc16 = "0" + fc16;
                        }
                        while (fc17.length() < 3) {
                            fc17 = "0" + fc17;
                        }
                        while (fc18.length() < 3) {
                            fc18 = "0" + fc18;
                        }
                        while (fc19.length() < 3) {
                            fc19 = "0" + fc19;
                        }

                        if (fc0.equals("000")) {
                            fc0 = "";
                        } else {
                            fc0 = "   " + fc0;
                        }
                        if (fc1.equals("000")) {
                            fc1 = "";
                        } else {
                            fc1 = "   " + fc1;
                        }
                        if (fc2.equals("000")) {
                            fc2 = "";
                        } else {
                            fc2 = "   " + fc2;
                        }
                        if (fc3.equals("000")) {
                            fc3 = "";
                        } else {
                            fc3 = "   " + fc3;
                        }
                        if (fc4.equals("000")) {
                            fc4 = "";
                        } else {
                            fc4 = "   " + fc4;
                        }
                        if (fc5.equals("000")) {
                            fc5 = "";
                        } else {
                            fc5 = "   " + fc5;
                        }
                        if (fc6.equals("000")) {
                            fc6 = "";
                        } else {
                            fc6 = "   " + fc6;
                        }
                        if (fc7.equals("000")) {
                            fc7 = "";
                        } else {
                            fc7 = "   " + fc7;
                        }
                        if (fc8.equals("000")) {
                            fc8 = "";
                        } else {
                            fc8 = "   " + fc8;
                        }
                        if (fc9.equals("000")) {
                            fc9 = "";
                        } else {
                            fc9 = "   " + fc9;
                        }
                        if (fc10.equals("000")) {
                            fc10 = "";
                        } else {
                            fc10 = "   " + fc10;
                        }
                        if (fc11.equals("000")) {
                            fc11 = "";
                        } else {
                            fc11 = "   " + fc11;
                        }
                        if (fc12.equals("000")) {
                            fc12 = "";
                        } else {
                            fc12 = "   " + fc12;
                        }
                        if (fc13.equals("000")) {
                            fc13 = "";
                        } else {
                            fc13 = "   " + fc13;
                        }
                        if (fc14.equals("000")) {
                            fc14 = "";
                        } else {
                            fc14 = "   " + fc14;
                        }
                        if (fc15.equals("000")) {
                            fc15 = "";
                        } else {
                            fc15 = "   " + fc15;
                        }
                        if (fc16.equals("000")) {
                            fc16 = "";
                        } else {
                            fc16 = "   " + fc16;
                        }
                        if (fc17.equals("000")) {
                            fc17 = "";
                        } else {
                            fc17 = "   " + fc17;
                        }
                        if (fc18.equals("000")) {
                            fc18 = "";
                        } else {
                            fc18 = "   " + fc18;
                        }
                        if (fc19.equals("000")) {
                            fc19 = "";
                        } else {
                            fc19 = "   " + fc19;
                        }

                        tvJdMsg.append("\n\n节点号:" + getMsg.substring(8 + (i - 1) * 76, 18 + (i - 1) * 76)
                                + "  分采号:" + fc0 + fc1 + fc2 + fc3 + fc4 + fc5 + fc6 + fc7 + fc8 + fc9 + fc10
                                + fc11 + fc12 + fc13 + fc14 + fc15 + fc16 + fc17 + fc18 + fc19
                        );
                    }
                    break;
                /**
                 * 添加路由
                 */
                case 0x07:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.equals("00160015202f")) {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "添加失败", "失败");
                    } else {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "添加成功", "成功");
                    }
                    break;
                /**
                 * 删除路由
                 */
                case 0x08:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.equals("00160014e1ef")) {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "删除失败", "失败");
                    } else {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "删除成功", "成功");
                    }
                    break;
                /**
                 * 路由信息
                 */
                case 0x09:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.equals("00160014e1ef")) {
                        tvJdMsg.setText("路由信息为空");
                    } else {
                        AllCount = Integer.parseInt(getMsg.substring(4, 6), 16);//总路由数
                        String msgx = getMsg.substring(8, 8 + 6 * AllCount);
                        for (int i = 1; i <= AllCount; i++) {
                            String msgs = msgx.substring(i * 6 - 6, i * 6);
                            tvJdMsg.append("\n路由层数:" + msgs.substring(0, 2) +
                                    "   路由:" + msgs.substring(2, 4) +
                                    "   路由编号:" + msgs.substring(4, 6));
                        }
                    }
                    break;
                /**
                 * 删除所有节点信息
                 */
                case 0x10:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.equals("001600866042")) {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "成功删除所有节点信息", "成功");
                    } else {
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "删除所有节点信息失败", "错误");
                    }
                    break;

                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "未接收到数据", "提示");
                    break;
                default:
                    break;
            }
        }
    };

    private int checkPsw() {
        P_PasswordDialog pswdlg = new P_PasswordDialog(this, 2);
        int rtn = pswdlg.showDialog();
        if (rtn != 1) {
            HintDialog.ShowHintDialog(this, "密码输入错误", "错误");
        }
        return rtn;
    }

    /**
     * 使用SharePreferences保存用户信息
     */
    public void saveUser() {

        SharedPreferences.Editor editor = getSharedPreferences("LoRaSetting", MODE_PRIVATE).edit();
        editor.putString("netId", etNetId.getText().toString());
        editor.putString("netFreq", etNetFreq.getText().toString());
        editor.putString("CbCount", etCbCount.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("LoRaSetting", MODE_PRIVATE);
        etNetId.setText(pref.getString("netId", ""));
        etNetFreq.setText(pref.getString("netFreq", ""));
        etCbCount.setText(pref.getString("CbCount", ""));
    }

    @Override
    protected void onDestroy() {
        saveUser();
        super.onDestroy();
    }
}
