package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ssHss on 2016/8/24.
 */
public class LoRa_CjjAboutActivity extends Activity implements View.OnClickListener {

    private EditText etXqh, etCjjNum, etCbjg, etRjbbh, etYjbbh, etSbwyh, etSfxx, etRtcTime, etSysConnect, etDsd;
    private Button btnGetXqh, btnSendXqh, btnGetCjjNum, btnSendCjjNum, btnGetCbjg, btnSysConnect, btnSetSysConnect,
            btnSendCbjg, btnGetRjbbh, btnGetYjbbh, btnGetSbwyh, btnGetSfxx, btnGetRTCTime, btnGetDsd, btnSendDsd,
            btnSetRTCTime;
    private final String[] sp = {"系统短链接", "系统长链接"};
    private int spinnerX;
    private Spinner spSysConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        //        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_cjjabout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框

        spSysConnect = (Spinner) findViewById(R.id.CjjAboutActivity_sp_SysConnect);
        ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sp);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSysConnect.setAdapter(ada);
        /**
         * 实现选择项事件(使用匿名类实现接口)
         */
        spSysConnect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * 选中某一项时
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerX = position;
            }

            /**
             * 未选中时
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etXqh = (EditText) findViewById(R.id.CjjAboutActivity_et_Xqh);
        etCjjNum = (EditText) findViewById(R.id.CjjAboutActivity_et_CjjNum);
        etCbjg = (EditText) findViewById(R.id.CjjAboutActivity_et_Cbjg);
        etRjbbh = (EditText) findViewById(R.id.CjjAboutActivity_et_Rjbbh);
        etYjbbh = (EditText) findViewById(R.id.CjjAboutActivity_et_Yjbbh);
        etSbwyh = (EditText) findViewById(R.id.CjjAboutActivity_et_Sbwyh);
        etSfxx = (EditText) findViewById(R.id.CjjAboutActivity_et_Sfxx);
        etRtcTime = (EditText) findViewById(R.id.CjjAboutActivity_et_RTCTime);
//        etSysConnect = (EditText) findViewById(R.id.CjjAboutActivity_et_SysConnect);
        etDsd = (EditText) findViewById(R.id.CjjAboutActivity_et_Dsd);

        /**
         * 按键设置
         */
        btnGetXqh = (Button) findViewById(R.id.CjjAboutActivity_btn_GetXqh);
        btnGetXqh.setOnClickListener(this);
        btnSendXqh = (Button) findViewById(R.id.CjjAboutActivity_btn_SendXqh);
        btnSendXqh.setOnClickListener(this);
        btnGetCjjNum = (Button) findViewById(R.id.CjjAboutActivity_btn_GetCjjNum);
        btnGetCjjNum.setOnClickListener(this);
        btnSendCjjNum = (Button) findViewById(R.id.CjjAboutActivity_btn_SendCjjNum);
        btnSendCjjNum.setOnClickListener(this);
        btnGetCbjg = (Button) findViewById(R.id.CjjAboutActivity_btn_GetCbjg);
        btnGetCbjg.setOnClickListener(this);
        btnSendCbjg = (Button) findViewById(R.id.CjjAboutActivity_btn_SendCbjg);
        btnSendCbjg.setOnClickListener(this);
        btnGetRjbbh = (Button) findViewById(R.id.CjjAboutActivity_btn_GetRjbbh);
        btnGetRjbbh.setOnClickListener(this);
        btnGetYjbbh = (Button) findViewById(R.id.CjjAboutActivity_btn_GetYjbbh);
        btnGetYjbbh.setOnClickListener(this);
        btnGetSbwyh = (Button) findViewById(R.id.CjjAboutActivity_btn_GetSbwyh);
        btnGetSbwyh.setOnClickListener(this);
        btnGetSfxx = (Button) findViewById(R.id.CjjAboutActivity_btn_GetSfxx);
        btnGetSfxx.setOnClickListener(this);
        btnGetRTCTime = (Button) findViewById(R.id.CjjAboutActivity_btn_GetRTCTime);
        btnGetRTCTime.setOnClickListener(this);
        btnSetRTCTime = (Button) findViewById(R.id.CjjAboutActivity_btn_SetRTCTime);
        btnSetRTCTime.setOnClickListener(this);
        btnSysConnect = (Button) findViewById(R.id.CjjAboutActivity_btn_GetSysConnect);
        btnSysConnect.setOnClickListener(this);
        btnSetSysConnect = (Button) findViewById(R.id.CjjAboutActivity_btn_SetSysConnect);
        btnSetSysConnect.setOnClickListener(this);
        btnGetDsd = (Button) findViewById(R.id.CjjAboutActivity_btn_GetDsd);
        btnGetDsd.setOnClickListener(this);
        btnSendDsd = (Button) findViewById(R.id.CjjAboutActivity_btn_SendDsd);
        btnSendDsd.setOnClickListener(this);

        loadUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 获取小区号
             */
            case R.id.CjjAboutActivity_btn_GetXqh:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "000300870001";
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
                break;
            /**
             * 设置小区号
             */
            case R.id.CjjAboutActivity_btn_SendXqh:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    String userMsg = etXqh.getText().toString();
                    if (userMsg.length() <= 4) {
                        while (userMsg.length() < 4) {
                            userMsg = "0" + userMsg;
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "数据过长", "错误");
                        break;
                    }
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "001600d3001002" + userMsg;
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
                break;
            /**
             * 获取采集机编号
             */
            case R.id.CjjAboutActivity_btn_GetCjjNum:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "000300070001";
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
                break;
            /**
             * 设置采集机编号
             */
            case R.id.CjjAboutActivity_btn_SendCjjNum:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    String userMsg = etCjjNum.getText().toString();
                    if (userMsg.length() <= 4) {
                        while (userMsg.length() < 4) {
                            userMsg = "0" + userMsg;
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "数据过长", "错误");
                        break;
                    }
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "001600d3001202" + userMsg;
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
                break;
            /**
             * 获取抄表间隔
             */
            case R.id.CjjAboutActivity_btn_GetCbjg:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "000300360001";
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
                break;
            /**
             * 设置抄表间隔
             */
            case R.id.CjjAboutActivity_btn_SendCbjg:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    String userMsg = etCbjg.getText().toString();
                    if (userMsg.length() <= 2) {
                        while (userMsg.length() < 2) {
                            userMsg = "0" + userMsg;
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "数据过长", "错误");
                        break;
                    }
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "001600d3001601" + userMsg;
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
                break;
            /**
             * 获取软件版本号
             */
            case R.id.CjjAboutActivity_btn_GetRjbbh:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "0003000f0002";
                    sendMsg = sendMsg + HzyUtils.CRC16("0003000f0002");
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
                break;
            /**
             * 获取硬件版本号
             */
            case R.id.CjjAboutActivity_btn_GetYjbbh:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "0003000e0002";
                    sendMsg = sendMsg + HzyUtils.CRC16("0003000e0002");
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
                break;
            /**
             * 获取设备唯一号
             */
            case R.id.CjjAboutActivity_btn_GetSbwyh:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "0003000a0004";
                    sendMsg = sendMsg + HzyUtils.CRC16("0003000a0004");
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
                break;
            /**
             * 获取身份信息
             */
            case R.id.CjjAboutActivity_btn_GetSfxx:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "00030080000c";
                    sendMsg = sendMsg + HzyUtils.CRC16("00030080000c");
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
                break;

            /**
             * 获取RTC时间
             */
            case R.id.CjjAboutActivity_btn_GetRTCTime:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "000300010004";
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
                break;
            /**
             * 设置RTC时间
             */
            case R.id.CjjAboutActivity_btn_SetRTCTime:
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = etRtcTime.getText().toString().trim();
                    if (sendMsg.length()<18){
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this,"输入格式不正确","提示");
                    }else {
                        String regEx = "[^0-9]";//匹配指定范围内的数字
                        //Pattern是一个正则表达式经编译后的表现模式
                        Pattern p = Pattern.compile(regEx);
                        // 一个Matcher对象是一个状态机器，它依据Pattern对象做为匹配模式对字符串展开匹配检查。
                        Matcher m = p.matcher(sendMsg);
                        //将输入的字符串中非数字部分删除
                        String string = m.replaceAll("").trim();
                        Log.d("limbo",string);
                        sendMsg = "001600d3006a07"+string;
                        sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                        Log.d("limbo", "设置:" + sendMsg);
                        MenuActivity.sendCmd(sendMsg);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
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
                                        message.what = 0x0b;
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
                break;
            /**
             * 获取集抄定时点
             */
            case R.id.CjjAboutActivity_btn_GetDsd:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "000300d90002";
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
                break;
            /**
             * 设置集抄定时点
             */
            case R.id.CjjAboutActivity_btn_SendDsd:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    String userMsg = etDsd.getText().toString().replaceAll(":", "");
                    if (userMsg.length() <= 6) {
                        while (userMsg.length() < 6) {
                            userMsg = "0" + userMsg;
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "数据过长", "错误");
                        break;
                    }
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "001600d3006903" + userMsg;
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
                break;
            /**
             * 获取系统链接方式
             */
            case R.id.CjjAboutActivity_btn_GetSysConnect:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String sendMsg = "000300d60001";
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
                break;
            /**
             * 设置链接方式
             */
            case R.id.CjjAboutActivity_btn_SetSysConnect:
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjAboutActivity.this);
                    String x = spinnerX + "";
                    while (x.length() < 2) {
                        x = "0" + x;
                    }
                    String sendMsg = "001600d3006601" + x ;
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
                                    message.what = 0x0a;
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
                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 * 读小区号
                 */
                case 0x00:
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 10);
                        etXqh.setText(getMsg);
                    }

                    break;
                /**
                 * 读采集机编号
                 */
                case 0x01:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 10);
                        etCjjNum.setText(getMsg);
                    }

                    break;
                /**
                 * 读自动抄表间隔
                 */
                case 0x02:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 10);
                        etCbjg.setText(getMsg);
                    }

                    break;
                /**
                 * 读软件版本号
                 */
                case 0x03:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 14);
                        etRjbbh.setText(getMsg);
                    }

                    break;
                /**
                 * 读硬件版本号
                 */
                case 0x04:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 14);
                        etYjbbh.setText(getMsg);
                    }

                    break;
                /**
                 * 读设备唯一号
                 */
                case 0x05:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 22);
                        etSbwyh.setText(getMsg);
                    }

                    break;
                /**
                 * 读身份信息
                 */
                case 0x06:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 54);
                        etSfxx.setText("小区编号:" + getMsg.substring(0, 4) + "\n"
                                + "采集机编号:" + getMsg.substring(4, 8) + "\n"
                                + "SIM卡的CCID:" + getMsg.substring(8));
                    }

                    break;
                /**
                 * 读内部RTC时间
                 */
                case 0x07:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 22);
                        etRtcTime.setText(getMsg.substring(2, 6) + "-" +
                                        getMsg.substring(6, 8) + "-" +
                                        getMsg.substring(8, 10) + " " +
                                        getMsg.substring(10, 12) + ":" +
                                        getMsg.substring(12, 14) + ":" +
                                        getMsg.substring(14, 16)
                        );
                    }

                    break;
                /**
                 * 设置内部RTC时间
                 */
                case 0x0b:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 20);
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this,"RTC时间更新为:\n" +
                                getMsg.substring(0, 4) + "-" +
                                getMsg.substring(4, 6) + "-" +
                                getMsg.substring(6, 8) + " " +
                                getMsg.substring(8, 10) + ":" +
                                getMsg.substring(10, 12) + ":" +
                                getMsg.substring(12, 14),"设置完成");
                    }
                    break;
                /**
                 * 读系统链接方式
                 */
                case 0x08:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(8, 10);
                        if (getMsg.equals("00")) {
                            //                            etSysConnect.setText("系统短连接");
                            spSysConnect.setSelection(0);
                        } else if (getMsg.equals("01")) {
                            //                            etSysConnect.setText("系统长连接");
                            spSysConnect.setSelection(1);
                        } else {
                            //                            etSysConnect.setText("读取错误");
                            HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "读取错误", "提示");
                        }

                    }

                    break;
                /**
                 * 读集抄定时点
                 */
                case 0x09:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(8, 14);
                        etDsd.setText(getMsg.substring(0, 2) + ":" + getMsg.substring(2, 4) + ":" + getMsg.substring(4, 6));

                    }

                    break;
                /**
                 * 写系统链接方式
                 */
                case 0x0a:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 10) {
                        HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 8);
                        if (getMsg.equals("00")) {
                            //                            etSysConnect.setText("系统短连接");
                            spSysConnect.setSelection(0);
                            HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "设置成功", "提示");
                        } else if (getMsg.equals("01")) {
                            //                            etSysConnect.setText("系统长连接");
                            spSysConnect.setSelection(1);
                            HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "设置成功", "提示");
                        } else {
                            //                            etSysConnect.setText("读取错误");
                            HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "设置失败", "提示");
                        }

                    }
                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_CjjAboutActivity.this, "未接收到数据", "提示");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 使用SharePreferences保存用户信息
     */
    public void saveUser() {

        SharedPreferences.Editor editor = getSharedPreferences("cjjAbout", MODE_PRIVATE).edit();
        editor.putString("xqh", etXqh.getText().toString());
        editor.putString("cjjNum", etCjjNum.getText().toString());
        editor.putString("cbjg", etCbjg.getText().toString());
        editor.putString("rjbbh", etRjbbh.getText().toString());
        editor.putString("yjbbh", etYjbbh.getText().toString());
        editor.putString("sbwyh", etSbwyh.getText().toString());
        editor.putString("rtc", etRtcTime.getText().toString());
        editor.putInt("sysc", spinnerX);
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("cjjAbout", MODE_PRIVATE);
        etXqh.setText(pref.getString("xqh", ""));
        etCjjNum.setText(pref.getString("cjjNum", ""));
        etCbjg.setText(pref.getString("cbjg", ""));
        etRjbbh.setText(pref.getString("rjbbh", ""));
        etRjbbh.setText(pref.getString("yjbbh", ""));
        etSbwyh.setText(pref.getString("sbwyh", ""));
        etRtcTime.setText(pref.getString("rtc", ""));
        spSysConnect.setSelection(pref.getInt("sysc", 0));
    }

    @Override
    protected void onDestroy() {
        saveUser();
        super.onDestroy();
    }
}
