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

    @BindView(R.id.LoRaSettingActivity_et_AddJd)
    EditText etAddJd;

    private EditText etNetId, etNetFreq, etCbCount, etAddFc0, etAddFc1, etAddFc2, etAddFc3, etAddFc4,
            etAddFc5, etAddFc6, etAddFc7, etAddFc8, etAddFc9, etAddFc10, etAddFc11, etAddFc12, etAddFc13, etAddFc14,
            etAddFc15, etAddFc16, etAddFc17, etAddFc18, etAddFc19,
            etDeleteJd, etThJd, etBthJd, etAddLy, etDeleteLy;
    private TextView tvJdMsg;

    private int id[] = {
            R.id.LoRaSettingActivity_et_AddFc0,
            R.id.LoRaSettingActivity_et_AddFc1,
            R.id.LoRaSettingActivity_et_AddFc2,
            R.id.LoRaSettingActivity_et_AddFc3,
            R.id.LoRaSettingActivity_et_AddFc4,
            R.id.LoRaSettingActivity_et_AddFc5,
            R.id.LoRaSettingActivity_et_AddFc6,
            R.id.LoRaSettingActivity_et_AddFc7,
            R.id.LoRaSettingActivity_et_AddFc8,
            R.id.LoRaSettingActivity_et_AddFc9,
            R.id.LoRaSettingActivity_et_AddFc10,
            R.id.LoRaSettingActivity_et_AddFc11,
            R.id.LoRaSettingActivity_et_AddFc12,
            R.id.LoRaSettingActivity_et_AddFc13,
            R.id.LoRaSettingActivity_et_AddFc14,
            R.id.LoRaSettingActivity_et_AddFc15,
            R.id.LoRaSettingActivity_et_AddFc16,
            R.id.LoRaSettingActivity_et_AddFc17,
            R.id.LoRaSettingActivity_et_AddFc18,
            R.id.LoRaSettingActivity_et_AddFc19
    };
    private EditText etId[] = {
            etAddFc0,
            etAddFc1,
            etAddFc2,
            etAddFc3,
            etAddFc4,
            etAddFc5,
            etAddFc6,
            etAddFc7,
            etAddFc8,
            etAddFc9,
            etAddFc10,
            etAddFc11,
            etAddFc12,
            etAddFc13,
            etAddFc14,
            etAddFc15,
            etAddFc16,
            etAddFc17,
            etAddFc18,
            etAddFc19
    };

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
        for (int i = 0; i < 20; i++) {
            etId[i] = (EditText) findViewById(id[i]);
        }
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
                                String getMsg = HzyUtils.GetBlueToothMsg();
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
                                String getMsg = HzyUtils.GetBlueToothMsg();
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
                                String getMsg = HzyUtils.GetBlueToothMsg();
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
                                String getMsg = HzyUtils.GetBlueToothMsg();
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
                                String getMsg = HzyUtils.GetBlueToothMsg();
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
                String AllFcMsg = "";
                for (int i = 0; i < 20; i++) {
                    String fcMsg = etId[i].getText().toString().trim();
                    if (HzyUtils.isEmpty(fcMsg))
                        fcMsg = "0";
                    fcMsg = Integer.toHexString(Integer.parseInt(fcMsg));
                    if (fcMsg.length() > 2)
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, "数据过长", "错误");
                    fcMsg = HzyUtils.isLength(fcMsg, 2);
                    AllFcMsg = AllFcMsg + fcMsg;
                }
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_LoRaSettingActivity.this);
                    String sendMsg = "001600d40010" + jd + AllFcMsg + "00000000000000";
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                String getMsg = HzyUtils.GetBlueToothMsg();
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
                                String getMsg = HzyUtils.GetBlueToothMsg();
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
                                String getMsg = HzyUtils.GetBlueToothMsg();
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
                                String getMsg = HzyUtils.GetBlueToothMsg();
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                int AllCount = Integer.parseInt(getMsg.substring(4, 6), 16);//总节点
                                while (getMsg.length() != AllCount * 76) {
                                    getMsg = HzyUtils.GetBlueToothMsg();
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
                                String getMsg = HzyUtils.GetBlueToothMsg();
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
                                    String getMsg = HzyUtils.GetBlueToothMsg();
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
                        String x = "节点号:" + getMsg.substring(0, 10);
                        for (int i = 1; i < 21; i++) {
                            x = x + "\n分采号" + i + ":" + Integer.parseInt(getMsg.substring(8 + i * 2, 10 + i * 2), 16);
                        }

                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, x, "返回");

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
                        String x = "节点号:" + getMsg.substring(0, 10);
                        for (int i = 1; i < 21; i++) {
                            x = x + "\n分采号" + i + ":" + Integer.parseInt(getMsg.substring(8 + i * 2, 10 + i * 2), 16);
                        }
                        HintDialog.ShowHintDialog(LoRa_LoRaSettingActivity.this, x, "返回");
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
                        String fc;
                        tvJdMsg.append("\n\n节点号:" + getMsg.substring(8 + (i - 1) * 76, 18 + (i - 1) * 76) + "  分采号:");
                        for (int j = 0; j < 20; j++) {
                            fc = Integer.parseInt(getMsg.substring(18 + (i - 1) * 76 + 2 * j, 20 + (i - 1) * 76 + 2 * j), 16) + "";
                            fc = HzyUtils.isLength(fc, 3);
                            fc = fc.equals("000") ? "" : "   " + fc;
                            tvJdMsg.append(fc);
                        }
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
