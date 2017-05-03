package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.content.Intent;
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

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by ssHss on 2016/5/20.
 */
public class P_sysSettingActivity extends Activity implements View.OnClickListener {

    private Button btnReadTime, btnSendTime, btnReadAPN, btnPwChange, btnSendAPN, btnReadIP, btnSendIP, btnReadTCP, btnSendTCP,
            btnReadZXTXCS, btnSendZXTXCS, btnReadGPRS, btnSendGPRS, btnRestore,
            btnFlash, btnSystem, btnTurnOff;
    private EditText etSetTime, etSetAPN, etSetIP, etSetTCP;
    private Spinner spBTL, spGprs;
    private int spinnerX;
    private int spinnerX2;
    private final String[] sp = {"波特率:2400", "波特率:9600"};//波特率
    private final String[] sp2 = {"GPRS:短链接", "GPRS:长链接"};//PGRS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_sys_setting);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        etSetTime = (EditText) findViewById(R.id.etSetTime);
        etSetAPN = (EditText) findViewById(R.id.etSetAPN);
        etSetIP = (EditText) findViewById(R.id.etSetIP);
        etSetTCP = (EditText) findViewById(R.id.etSetTCP);

        spBTL = (Spinner) findViewById(R.id.spBTL);
        ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sp);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBTL.setAdapter(ada);

        /**
         * 实现选择项事件(使用匿名类实现接口)
         */
        spBTL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spGprs = (Spinner) findViewById(R.id.spGprs);
        ArrayAdapter<String> ada2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sp2);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGprs.setAdapter(ada2);
        /**
         * 实现选择项事件(使用匿名类实现接口)
         */
        spGprs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * 选中某一项时
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerX2 = position;
            }

            /**
             * 未选中时
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnReadTime = (Button) findViewById(R.id.btnReadTime);
        btnReadTime.setOnClickListener(this);
        btnSendTime = (Button) findViewById(R.id.btnSendTime);
        btnSendTime.setOnClickListener(this);
        btnReadAPN = (Button) findViewById(R.id.btnReadAPN);
        btnReadAPN.setOnClickListener(this);
        btnSendAPN = (Button) findViewById(R.id.btnSendAPN);
        btnSendAPN.setOnClickListener(this);
        btnReadIP = (Button) findViewById(R.id.btnReadIP);
        btnReadIP.setOnClickListener(this);
        btnSendIP = (Button) findViewById(R.id.btnSendIP);
        btnSendIP.setOnClickListener(this);
        btnReadTCP = (Button) findViewById(R.id.btnReadTCP);
        btnReadTCP.setOnClickListener(this);
        btnSendTCP = (Button) findViewById(R.id.btnSendTCP);
        btnSendTCP.setOnClickListener(this);
        btnReadZXTXCS = (Button) findViewById(R.id.btnReadZXTXCS);
        btnReadZXTXCS.setOnClickListener(this);
        btnSendZXTXCS = (Button) findViewById(R.id.btnSendZXTXCS);
        btnSendZXTXCS.setOnClickListener(this);
        btnReadGPRS = (Button) findViewById(R.id.btnReadGPRS);
        btnReadGPRS.setOnClickListener(this);
        btnSendGPRS = (Button) findViewById(R.id.btnSendGPRS);
        btnSendGPRS.setOnClickListener(this);
        btnRestore = (Button) findViewById(R.id.btnRestore);
        btnRestore.setOnClickListener(this);
        btnFlash = (Button) findViewById(R.id.btnFlash);
        btnFlash.setOnClickListener(this);
        btnSystem = (Button) findViewById(R.id.btnSystem);
        btnSystem.setOnClickListener(this);
        btnTurnOff = (Button) findViewById(R.id.btnTurnOff);
        btnTurnOff.setOnClickListener(this);
        btnPwChange = (Button) findViewById(R.id.btnPwChange);
        btnPwChange.setOnClickListener(this);
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
            HzyUtils.showProgressDialog(P_sysSettingActivity.this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HzyUtils.showProgressDialog(P_sysSettingActivity.this);
                        Thread.sleep(MenuActivity.PIC_DELAY);
                        String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                        int length = msg.length();
                        if (length == 0) {
                            HzyUtils.closeProgressDialog();
                            Looper.prepare();
                            HintDialog.ShowHintDialog(P_sysSettingActivity.this, "未接收到数据", "提示");
                            Looper.loop();
                            return;
                        } else {
                            String getMsg = msg.substring(6, length - 4);

                            Message message = new Message();
                            message.what = 0x09;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);

                        }
                    } catch (InterruptedException e) {
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
            case R.id.btnReadTime:
                String etContent;
                String Msg = "000300010004";
                String crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                String sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读内部时钟:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_sysSettingActivity.this);
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
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(0, 4) + "-" +
                                        getMsg.substring(4, 6) + "-" +
                                        getMsg.substring(6, 8) + " "
                                        + getMsg.substring(8, 10) + ":" +
                                        getMsg.substring(10, 12) + ":" +
                                        getMsg.substring(12, 14);

                                //etSetTime.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x00;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.btnSendTime:
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                if (str.length() == 0) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "获取系统时间失败", "错误");
                    return;
                }
                /**
                 * 计算字节位数
                 */
                int length = str.length();
                if (length % 2 == 1) {
                    length = length / 2 + 1;
                } else {
                    length = length / 2;
                }
                if (str.length() > 16) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (str.length() < 16) {
                    while (str.length() < 16) {
                        str =  str + "0" ;
                    }
                }
                Msg = "00100001000408" + str;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写内部时钟:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnReadAPN:
                Msg = "000300C80008";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读取APN:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_sysSettingActivity.this);
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
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = HzyUtils.toStringHex(getMsg).replaceAll("�", "");

                                Message message = new Message();
                                message.what = 0x01;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.btnSendAPN:
                etContent = HzyUtils.convertStringToHex(etSetAPN.getText().toString().trim());
                if (etContent.length() == 0) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                /**
                 * 计算字节位数
                 */
                length = etContent.length();
                if (length % 2 == 1) {
                    length = length / 2 + 1;
                } else {
                    length = length / 2;
                }
                if (etContent.length() > 32) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < length * 2) {
                    while (etContent.length() < length * 2) {
                        etContent = etContent + "0";
                    }
                }
                while (etContent.length() < 32) {
                    etContent =  etContent + "0";
                }
                Msg = "001000C8000810" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写APN:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnReadIP:
                Msg = "000300910015";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读取IP:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_sysSettingActivity.this);
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
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6), 16);
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = HzyUtils.toStringHex(getMsg).replaceAll("�", "");
                                //etSetIP.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x02;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.btnSendIP:
                etContent = HzyUtils.convertStringToHex(etSetIP.getText().toString().trim());
                if (etContent.length() == 0) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                /**
                 * 计算字节位数
                 */
                length = etContent.length();
                if (length % 2 == 1) {
                    length = length / 2 + 1;
                } else {
                    length = length / 2;
                }
                if (etContent.length() > 60) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < length * 2) {
                    while (etContent.length() < length * 2) {
                        etContent = etContent + "0";
                    }
                }
                while (etContent.length() < 60) {
                    etContent = etContent + "0";
                }
                Msg = "00100091000f1e" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写IP:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnReadTCP:
                Msg = "000300900001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "TCP端口号:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_sysSettingActivity.this);
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
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = Integer.parseInt(getMsg, 16) + "";

                                //etSetTCP.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x03;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.btnSendTCP:
                etContent = etSetTCP.getText().toString().trim();
                int getInt = Integer.parseInt(etContent);
                etContent = Integer.toHexString(getInt);
                if (etContent.length() == 0) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                /**
                 * 计算字节位数
                 */
                length = etContent.length();
                if (length % 2 == 1) {
                    length = length / 2 + 1;
                } else {
                    length = length / 2;
                }
                if (etContent.length() > 4) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < 4) {
                    while (etContent.length() < 4) {
                        etContent = etContent + "0";
                    }
                }
                Msg = "00060090" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写IP:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnReadZXTXCS:
                Msg = "000300060001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读MBUS通讯参数:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_sysSettingActivity.this);
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
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(0, 2);

                                Message message = new Message();
                                message.what = 0x04;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.btnSendZXTXCS:
                if (spinnerX == 0) {
                    etContent = "0003";
                } else {
                    etContent = "0103";
                }
                /**
                 * 计算字节位数
                 */
                length = etContent.length();
                if (length % 2 == 1) {
                    length = length / 2 + 1;
                } else {
                    length = length / 2;
                }
                if (etContent.length() > 4) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < 4) {
                    while (etContent.length() < 4) {
                        etContent = etContent + "0";
                    }
                }
                Msg = "00060006" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写MBUS通讯参数:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnPwChange:
                Intent i = new Intent(P_sysSettingActivity.this, P_PwChangeActivity.class);
                startActivity(i);
                break;
            case R.id.btnReadGPRS:
                Msg = "0003008e0001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读GPRS工作模式:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_sysSettingActivity.this);
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
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, lengthInt - 4);
                            String y = msg.substring(lengthInt - 4, lengthInt);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(2, 4);
                                //etSetGPRS.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x05;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_sysSettingActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.btnSendGPRS:
                if (spinnerX2 == 0) {//短链接
                    etContent = "0000";

                } else {//长链接
                    etContent = "0001";
                }
                if (etContent.length() == 0) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "输入数据不能为空", "错误");
                    return;
                }
                /**
                 * 计算字节位数
                 */
                length = etContent.length();
                if (length % 2 == 1) {
                    length = length / 2 + 1;
                } else {
                    length = length / 2;
                }
                if (etContent.length() > 4) {
                    HintDialog.ShowHintDialog(P_sysSettingActivity.this, "数据长度过长", "错误");
                    return;
                } else if (etContent.length() < 4) {
                    while (etContent.length() < 4) {
                        etContent = etContent + "0";
                    }
                }
                Msg = "0006008e" + etContent;
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "写GPRS工作模式:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnRestore:
                Msg = "00060000AAAA";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "系统复位:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnFlash:
                Msg = "000600005555";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "Flash格式化:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnSystem:
                Msg = "0006000055AA";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "系统复位:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;
            case R.id.btnTurnOff:
                Msg = "00060000AA55";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "系统关机:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                break;

            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    etSetTime.setText(msg.obj.toString());
                    break;
                case 0x01:
                    etSetAPN.setText(msg.obj.toString());
                    break;
                case 0x02:
                    etSetIP.setText(msg.obj.toString());
                    break;
                case 0x03:
                    etSetTCP.setText(msg.obj.toString());
                    break;
                case 0x04:
                    Log.d("limbo", msg.obj.toString());
                    if (msg.obj.toString().equals("00")) {
                        spBTL.setSelection(0);
                    } else if (msg.obj.toString().equals("01")) {
                        spBTL.setSelection(1);
                    }
                    break;
                case 0x05:
                    if (msg.obj.toString().equals("00")) {
                        spGprs.setSelection(0);
                    } else if (msg.obj.toString().equals("01")) {
                        spGprs.setSelection(1);
                    }
                    break;
                case 0x09:
                    String result = msg.obj.toString();
                    Log.d("limbo", result);
                    String result1 = result.substring(4, 20);
                    result1 = result1.substring(0, 4) + "-" +
                            result1.substring(4, 6) + "-" +
                            result1.substring(6, 8) + " " +
                            result1.substring(8, 10) + ":" +
                            result1.substring(10, 12) + ":" +
                            result1.substring(12, 14);
                    etSetTime.setText(result1);
                    result1 = result.substring(800, 832);
                    result1 = HzyUtils.toStringHex(result1).replaceAll("�", "");
                    etSetAPN.setText(result1);
                    result1 = result.substring(580, 640);
                    result1 = HzyUtils.toStringHex(result1).replaceAll("�", "");
                    etSetIP.setText(result1);
                    result1 = result.substring(576, 580);
                    result1 = Integer.parseInt(result1, 16) + "";
                    etSetTCP.setText(result1);
                    result1 = result.substring(24, 26);
                    if (result1.equals("00")) {
                        spBTL.setSelection(0);
                    } else if (result1.equals("01")) {
                        spBTL.setSelection(1);
                    }
                    result1 = result.substring(570, 572);
                    if (result1.equals("00")) {
                        spGprs.setSelection(0);
                    } else if (result1.equals("01")) {
                        spGprs.setSelection(1);
                    }
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

        SharedPreferences.Editor editor = getSharedPreferences("user_msg2", MODE_PRIVATE).edit();
        editor.putString("etSetTime", etSetTime.getText().toString());
        editor.putString("etSetAPN", etSetAPN.getText().toString());
        editor.putString("etSetIP", etSetIP.getText().toString());
        editor.putString("etSetTCP", etSetTCP.getText().toString());
        editor.putString("spGPRS", spinnerX2 + "");
        editor.putString("spBTL", spinnerX + "");
        editor.commit();
    }

    /**
     * 加载用户信息
     */
    private void loadUser() {
        SharedPreferences pref = getSharedPreferences("user_msg2", MODE_PRIVATE);
        etSetTime.setText(pref.getString("etSetTime", ""));
        etSetAPN.setText(pref.getString("etSetAPN", ""));
        etSetIP.setText(pref.getString("etSetIP", ""));
        etSetTCP.setText(pref.getString("etSetTCP", ""));
        spGprs.setSelection(Integer.valueOf(pref.getString("spGPRS", "0")));
        spBTL.setSelection(Integer.valueOf(pref.getString("spBTL", "0")));
    }
}
