package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;


/**
 * Created by ssHss on 2016/5/16.
 */
public class P_CsSettingActivity extends Activity implements View.OnClickListener {
    private Button btnReadSettingXqNum, btnSendSettingXqNum, btnReadSettingTxjNum, btnSendSettingTxjNum, btnReadSettingBjdz,
            btnSendSettingBjdz, btnReadSettingCbr, btnSendSettingCbr, btnReadSettingPzdk, btnSendSettingPzdk, btnReadSettingPzb,
            btnSendSettingPzb, btnReadSettingSzcbsj, btnSendSettingSzcbsj, btnReadSettingSzcbzq, btnSendSettingSzcbzq;
    private EditText etSettingXqNum, etSettingTxjNum, etSettingBjdz, etSettingCbr, etSettingBsl,
            etSettingSzcbsj, etSettingSzcbzq;
    private Spinner spinnerStateDkh;
    private int spinnerX;
    private final String[] sp = {"1号端口", "2号端口", "3号端口", "4号端口", "5号端口", "6号端口"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_cs_setting);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        etSettingXqNum = (EditText) findViewById(R.id.etSettingXqNum);
        etSettingTxjNum = (EditText) findViewById(R.id.etSettingTxjNum);
        etSettingBjdz = (EditText) findViewById(R.id.etSettingBjdz);
        etSettingCbr = (EditText) findViewById(R.id.etSettingCbr);
        //        etSettingPzdk = (EditText) findViewById(R.id.etSettingPzdk);
        etSettingBsl = (EditText) findViewById(R.id.etSettingBsl);
        etSettingSzcbsj = (EditText) findViewById(R.id.etSettingSzcbsj);
        etSettingSzcbzq = (EditText) findViewById(R.id.etSettingSzcbzq);

        spinnerStateDkh = (Spinner) findViewById(R.id.spinnerStateDkh);
        ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sp);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStateDkh.setAdapter(ada);
        /**
         * 实现选择项事件(使用匿名类实现接口)
         */
        spinnerStateDkh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        btnReadSettingXqNum = (Button) findViewById(R.id.btnReadSettingXqNum);
        btnReadSettingXqNum.setOnClickListener(this);
        btnSendSettingXqNum = (Button) findViewById(R.id.btnSendSettingXqNum);
        btnSendSettingXqNum.setOnClickListener(this);
        btnReadSettingTxjNum = (Button) findViewById(R.id.btnReadSettingTxjNum);
        btnReadSettingTxjNum.setOnClickListener(this);
        btnSendSettingTxjNum = (Button) findViewById(R.id.btnSendSettingTxjNum);
        btnSendSettingTxjNum.setOnClickListener(this);
        btnReadSettingBjdz = (Button) findViewById(R.id.btnReadSettingBjdz);
        btnReadSettingBjdz.setOnClickListener(this);
        btnSendSettingBjdz = (Button) findViewById(R.id.btnSendSettingBjdz);
        btnSendSettingBjdz.setOnClickListener(this);
        btnSendSettingBjdz.setVisibility(View.GONE);
        btnReadSettingCbr = (Button) findViewById(R.id.btnReadSettingCbr);
        btnReadSettingCbr.setOnClickListener(this);
        btnSendSettingCbr = (Button) findViewById(R.id.btnSendSettingCbr);
        btnSendSettingCbr.setOnClickListener(this);
        btnReadSettingPzb = (Button) findViewById(R.id.btnReadSettingPzb);
        btnReadSettingPzb.setOnClickListener(this);
        btnSendSettingPzb = (Button) findViewById(R.id.btnSendSettingPzb);
        btnSendSettingPzb.setOnClickListener(this);
        btnReadSettingSzcbsj = (Button) findViewById(R.id.btnReadSettingSzcbsj);
        btnReadSettingSzcbsj.setOnClickListener(this);
        btnSendSettingSzcbsj = (Button) findViewById(R.id.btnSendSettingSzcbsj);
        btnSendSettingSzcbsj.setOnClickListener(this);
        btnReadSettingSzcbzq = (Button) findViewById(R.id.btnReadSettingSzcbzq);
        btnReadSettingSzcbzq.setOnClickListener(this);
        btnSendSettingSzcbzq = (Button) findViewById(R.id.btnSendSettingSzcbzq);
        btnSendSettingSzcbzq.setOnClickListener(this);
        loadUser();
        readAll();
    }

    private void readAll() {
        try {
            String Msg = "0003000000D0";
            String crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
            String sendMsg = Msg + crc16Result;//需要发送的指令
            Log.d("limbo", "读所有数据:" + sendMsg);
            MenuActivity.sendCmd(sendMsg);
            HzyUtils.showProgressDialog(P_CsSettingActivity.this);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        Looper.prepare();
//                        HzyUtils.showProgressDialog(P_CsSettingActivity.this);
//                        Looper.loop();
                        Thread.sleep(MenuActivity.PIC_DELAY);
                        String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                        int length = msg.length();
                        if (length == 0) {
                            HzyUtils.closeProgressDialog();
                            Looper.prepare();
                            HintDialog.ShowHintDialog(P_CsSettingActivity.this, "未接收到数据", "提示");
                            Looper.loop();
                            return;
                        } else {
                            String getMsg = msg.substring(6, length - 4);

                            Message message = new Message();
                            message.what = 0x09;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);

                        }
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }
                    HzyUtils.closeProgressDialog();
                }
            }).start();
        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReadSettingXqNum:
                String Msg = "000300870001";
                String crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                String sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读小区号:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_CsSettingActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int lengthInt = msg.length();
                            if (lengthInt == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = Integer.parseInt(getMsg, 16) + "";

                                Message message = new Message();
                                message.what = 0x00;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnSendSettingXqNum:
                String etContent = etSettingXqNum.getText().toString().trim();
                int getInt = Integer.parseInt(etContent);
                etContent = Integer.toHexString(getInt);
                if (etContent.length() == 0) {
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                /**
                 * 计算字节位数
                 */
                int length = etContent.length();
                if (length % 2 == 1) {
                    length = length / 2 + 1;
                } else {
                    length = length / 2;
                }
                if (etContent.length() > 4) {
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < 4) {
                    while (etContent.length() < 4) {
                        etContent = "0" + etContent;
                    }
                }
                Msg = "00060087" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写小区号:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnReadSettingTxjNum:
                Msg = "000300880001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读通信机号:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_CsSettingActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int lengthInt = msg.length();
                            if (lengthInt == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = Integer.parseInt(getMsg, 16) + "";

                                //etSettingTxjNum.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x01;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnSendSettingTxjNum:
                etContent = etSettingTxjNum.getText().toString().trim();
                getInt = Integer.parseInt(etContent);
                etContent = Integer.toHexString(getInt);
                if (etContent.length() == 0) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                if (etContent.length() > 4) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < 4) {
                    while (etContent.length() < 4) {
                        etContent = "0" + etContent;
                    }
                }
                Msg = "00060088" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写通信机号:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnReadSettingBjdz:
                Msg = "000300070001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读本机地址:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_CsSettingActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int lengthInt = msg.length();
                            if (lengthInt == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = Integer.parseInt(getMsg.substring(2, 4), 16) + "";

                                Message message = new Message();
                                message.what = 0x02;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnSendSettingBjdz:
                /*etContent = etSettingBjdz.getText().toString().trim().replaceAll(".","");
                if (etContent.length() == 0){
                    HintDialog.ShowHintDialog(CsSettingActivity.this,"输入数据不能为空","错误");
                    return;
                }
                *//**
             * 计算字节位数
             *//*
                length = etContent.length();
                if (length%2 ==1){
                    length = length/2 + 1;
                }else {
                    length = length/2;
                }
                if (etContent.length() > 4){
                    HintDialog.ShowHintDialog(CsSettingActivity.this,"数据长度过长","错误");
                    return;
                }else if(etContent.length() < (length*2)){
                    while (etContent.length() < (length*2)){
                        etContent = "0" + etContent;
                    }
                }
                Msg = "00100007" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写本机地址:" + sendMsg);
                stateActivity.sendCmd(sendMsg);*/
                break;
            case R.id.btnReadSettingCbr:
                Msg = "000300300001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读每月抄表日:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_CsSettingActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int lengthInt = msg.length();
                            if (lengthInt == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(2,4);
                                getMsg = Integer.parseInt(getMsg, 16) + "";
                                //etSettingCbr.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x03;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnSendSettingCbr:
                etContent = etSettingCbr.getText().toString().trim();
                getInt = Integer.parseInt(etContent);
                etContent = Integer.toHexString(getInt);
                if (etContent.length() == 0) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                if (etContent.length() > 4) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < 4) {
                    while (etContent.length() < 4) {
                        etContent = "0" + etContent;
                    }
                }
                Msg = "00060030" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "每月抄表日:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            /*case R.id.btnReadSettingPzdk:
                Msg = "000300900001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读TCP端口号:" + sendMsg);
                stateActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(CsSettingActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(stateActivity.CMD_DELAY);
                            String msg = (CjjSettingActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int lengthInt = msg.length();
                            if (lengthInt == 0) {
                                Looper.prepare();                                 HintDialog.ShowHintDialog(CsSettingActivity.this, "未接收到数据", "提示");                                 Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);

                                //etSettingPzdk.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x04;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                Looper.prepare();HintDialog.ShowHintDialog(CsSettingActivity.this, "CRC16校验码错误", "提示");Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo",e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnSendSettingPzdk:
                etContent = etSettingPzdk.getText().toString().trim();
                if (etContent.length() == 0) {
                    HintDialog.ShowHintDialog(CsSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                if (etContent.length() > 4) {
                    HintDialog.ShowHintDialog(CsSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < 4) {
                    while (etContent.length() < 4) {
                        etContent = "0" + etContent;
                    }
                }
                Msg = "00060090" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写TCP端口号:" + sendMsg);
                stateActivity.sendCmd(sendMsg);
                break;*/
            case R.id.btnReadSettingPzb:
                int portNum = spinnerX;
                if (portNum > 5 || portNum < 0) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "输入的端口号超出范围(1~6)", "错误");
                    return;
                }
                Msg = "0003001" + (portNum) + "0001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读配置表:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_CsSettingActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int lengthInt = msg.length();
                            if (lengthInt == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();

                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(0, 2);
                                getMsg = Integer.parseInt(getMsg, 16) + "";

                                Message message = new Message();
                                message.what = 0x05;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "CRC16校验码错误", "提示");
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnSendSettingPzb:
                etContent = etSettingBsl.getText().toString().trim();
                getInt = Integer.parseInt(etContent);
                etContent = Integer.toHexString(getInt);
                portNum = spinnerX;
                if (portNum > 5 || portNum < 0) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "输入的端口号超出范围(1~6)", "错误");
                    return;
                }
                Msg = "0006001" + (portNum) + etContent + "0001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写配置表:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnReadSettingSzcbsj:
                Msg = "000300310001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读每月抄表日的抄表时间:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_CsSettingActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int lengthInt = msg.length();
                            if (lengthInt == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(2,4);
                                getMsg = Integer.parseInt(getMsg, 16) + "";

                                Message message = new Message();
                                message.what = 0x06;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnSendSettingSzcbsj:
                etContent = etSettingSzcbsj.getText().toString().trim();
                getInt = Integer.parseInt(etContent);
                etContent = Integer.toHexString(getInt);
                if (etContent.length() == 0) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                if (etContent.length() > 4) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < 2) {
                    while (etContent.length() < 2) {
                        etContent = "0" + etContent;
                    }
                }
                if(etContent.length() < 4){
                    while (etContent.length() < 2) {
                        etContent = etContent + "0";
                    }
                }
                Msg = "0006003100" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读每月抄表日的抄表时间:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnReadSettingSzcbzq:
                Msg = "000300360001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读自动抄表间隔:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_CsSettingActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int lengthInt = msg.length();
                            if (lengthInt == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(2,4);
                                getMsg = Integer.parseInt(getMsg, 16) + "";

                                Message message = new Message();
                                message.what = 0x07;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_CsSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnSendSettingSzcbzq:
                etContent = etSettingSzcbzq.getText().toString().trim();
                getInt = Integer.parseInt(etContent);
                etContent = Integer.toHexString(getInt);
                if (etContent.length() == 0) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                if (etContent.length() > 4) {
                    HintDialog.ShowHintDialog(P_CsSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < 2) {
                    while (etContent.length() < 2) {
                        etContent = "0" + etContent;
                    }
                }
                if(etContent.length() < 4){
                    while (etContent.length() < 2) {
                        etContent = etContent + "0";
                    }
                }
                Msg = "0006003600" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读自动抄表间隔:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    etSettingXqNum.setText(msg.obj.toString());
                    break;
                case 0x01:
                    etSettingTxjNum.setText(msg.obj.toString());
                    break;
                case 0x02:
                    etSettingBjdz.setText(msg.obj.toString());
                    break;
                case 0x03:
                    etSettingCbr.setText(msg.obj.toString());
                    break;
                /*case 0x04:
                    etSettingPzdk.setText(msg.obj.toString());
                    break;*/
                case 0x05:
                    etSettingBsl.setText(msg.obj.toString());
                    break;
                case 0x06:
                    etSettingSzcbsj.setText(msg.obj.toString());
                    break;
                case 0x07:
                    etSettingSzcbzq.setText(msg.obj.toString());
                    break;
                case 0x09:
                    String result = msg.obj.toString();
                    Log.d("limbo", result);
                    String result1 = result.substring(540, 544);
                    result1 = Integer.parseInt(result1, 16) + "";
                    etSettingXqNum.setText(result1);
                    result1 = result.substring(544,548);
                    result1 = Integer.parseInt(result1, 16) + "";
                    etSettingTxjNum.setText(result1);
                    result1 = result.substring(28,32);
                    etSettingBjdz.setText(Integer.parseInt(result1.substring(2, 4), 16) + "");
                    result1 = result.substring(194,196);
                    result1 = Integer.parseInt(result1, 16) + "";
                    etSettingCbr.setText(result1);
                    result1 = result.substring(198,200);
                    result1 = Integer.parseInt(result1, 16) + "";
                    etSettingSzcbsj.setText(result1);
                    result1 = result.substring(218,220);
                    result1 = Integer.parseInt(result1, 16) + "";
                    etSettingSzcbzq.setText(result1);
                    result1 = result.substring((16+spinnerX)*4,(16+spinnerX)*4+2);
                    etSettingBsl.setText(Integer.parseInt(result1, 16)+"");

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 使用SharePreferences保存用户信息
     */
    private void saveUser() {

        SharedPreferences.Editor editor = getSharedPreferences("user_msg3", MODE_PRIVATE).edit();
        editor.putString("etSettingXqNum", etSettingXqNum.getText().toString());
        editor.putString("etSettingTxjNum", etSettingTxjNum.getText().toString());
        editor.putString("etSettingBjdz", etSettingBjdz.getText().toString());
        editor.putString("etSettingCbr", etSettingCbr.getText().toString());
        //        editor.putString("etSettingPzdk", etSettingPzdk.getText().toString());
        editor.putString("etSettingBsl", etSettingBsl.getText().toString());
        editor.putString("etSettingSzcbsj", etSettingSzcbsj.getText().toString());
        editor.putString("etSettingSzcbzq", etSettingSzcbzq.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */
    private void loadUser() {
        SharedPreferences pref = getSharedPreferences("user_msg3", MODE_PRIVATE);
        etSettingXqNum.setText(pref.getString("etSettingXqNum", ""));
        etSettingTxjNum.setText(pref.getString("etSettingTxjNum", ""));
        etSettingBjdz.setText(pref.getString("etSettingBjdz", ""));
        etSettingCbr.setText(pref.getString("etSettingCbr", ""));
        //        etSettingPzdk.setText(pref.getString("etSettingPzdk", ""));
        etSettingBsl.setText(pref.getString("etSettingBsl", ""));
        etSettingSzcbsj.setText(pref.getString("etSettingSzcbsj", ""));
        etSettingSzcbzq.setText(pref.getString("etSettingSzcbzq", ""));
    }
}
