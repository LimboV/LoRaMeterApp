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
import android.widget.Spinner;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

/**
 * Created by ssHss on 2016/5/10.
 */
public class P_stateActivity extends Activity implements View.OnClickListener {

    private Button btnReadTxj, btnReadTxmkxh, btnReadTcpJl, btnReadTcpDk, btnReadDk, btnReadDydy, btnReadSbms, btnReadYjcs, btnReadRjcs;
    private TextView tvStateTxj, tvStateTxmkxh, tvStateTcpJl, tvStateTcpDk, tvStateDk, tvStateDydy, tvStateSbms, tvStateYjcs, tvStateRjcs;
    private Spinner spinnerStateDk;
    private int spinnerX;
    private final String[] sp = {"1号端口", "2号端口", "3号端口", "4号端口", "5号端口", "6号端口"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_state);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        tvStateTxj = (TextView) findViewById(R.id.tvStateTxj);
        tvStateTcpJl = (TextView) findViewById(R.id.tvStateTcpJl);
        tvStateTcpDk = (TextView) findViewById(R.id.tvStateTcpDk);
        tvStateDk = (TextView) findViewById(R.id.tvStateDk);
        tvStateDydy = (TextView) findViewById(R.id.tvStateDydy);
        tvStateSbms = (TextView) findViewById(R.id.tvStateSbms);
        tvStateYjcs = (TextView) findViewById(R.id.tvStateYjcs);
        tvStateRjcs = (TextView) findViewById(R.id.tvStateRjcs);
        tvStateTxmkxh = (TextView) findViewById(R.id.tvStateTxmkxh);

        spinnerStateDk = (Spinner) findViewById(R.id.spinnerStateDk);
        ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sp);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStateDk.setAdapter(ada);
        /**
         * 实现选择项事件(使用匿名类实现接口)
         */
        spinnerStateDk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        btnReadTxj = (Button) findViewById(R.id.btnReadTxj);
        btnReadTxj.setOnClickListener(this);
        btnReadTxmkxh = (Button) findViewById(R.id.btnReadTxmkxh);
        btnReadTxmkxh.setOnClickListener(this);
        btnReadTcpJl = (Button) findViewById(R.id.btnReadTcpJl);
        btnReadTcpJl.setOnClickListener(this);
        btnReadTcpDk = (Button) findViewById(R.id.btnReadTcpDk);
        btnReadTcpDk.setOnClickListener(this);
        btnReadDk = (Button) findViewById(R.id.btnReadDk);
        btnReadDk.setOnClickListener(this);
        btnReadDydy = (Button) findViewById(R.id.btnReadDydy);
        btnReadDydy.setOnClickListener(this);
        btnReadSbms = (Button) findViewById(R.id.btnReadSbms);
        btnReadSbms.setOnClickListener(this);
        btnReadYjcs = (Button) findViewById(R.id.btnReadYjcs);
        btnReadYjcs.setOnClickListener(this);
        btnReadRjcs = (Button) findViewById(R.id.btnReadRjcs);
        btnReadRjcs.setOnClickListener(this);
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
            HzyUtils.showProgressDialog(P_stateActivity.this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HzyUtils.showProgressDialog(P_stateActivity.this);
                        Thread.sleep(MenuActivity.PIC_DELAY);
                        String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                        int length = msg.length();
                        if (length == 0) {
                            HzyUtils.closeProgressDialog();
                            Looper.prepare();
                            HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                            Looper.loop();
                            return;
                        } else {
                            String x = msg.substring(0, length - 4);//数据部分
                            String y = msg.substring(length - 4, length);//CRC16部分
                            int msgLength = Integer.valueOf(msg.substring(4, 6), 16);
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
            case R.id.btnReadTxj://读通讯机状态
                String Msg = "000300890001";
                String crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                String sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读通讯机状态:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_stateActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HzyUtils.showProgressDialog(P_stateActivity.this);
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                if (getMsg.substring(2, 4).equals("00")) {
                                    getMsg = "信号强度:" + Integer.parseInt(getMsg.substring(0, 2), 16);
                                } else {
                                    getMsg = "读取信号强度失败";
                                }
                                //tvStateTxj.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x00;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);

                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }

                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                        HzyUtils.closeProgressDialog();
                    }
                }).start();

                break;

            case R.id.btnReadTxmkxh:
                Msg = "0003008a0004";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读通讯模块型号:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_stateActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = HzyUtils.toStringHex(getMsg).replaceAll("�", "");

                                Message message = new Message();
                                message.what = 0x08;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }

                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                        HzyUtils.closeProgressDialog();
                    }
                }).start();
                break;

            case R.id.btnReadTcpJl://读Tcp建立时间
                Msg = "000300c00004";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读Tcp建立时间:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_stateActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(0, 4) + "-" +
                                        getMsg.substring(4, 6) + "-" +
                                        getMsg.substring(6, 8) + " " +
                                        getMsg.substring(8, 10) + ":" +
                                        getMsg.substring(10, 12) + ":" +
                                        getMsg.substring(12, 14);
                                //tvStateTcpJl.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x01;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }

                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                        HzyUtils.closeProgressDialog();
                    }
                }).start();

                break;
            case R.id.btnReadTcpDk://读Tcp断开时间
                Msg = "000300c40004";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读Tcp断开时间:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_stateActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(0, 4) + "-" +
                                        getMsg.substring(4, 6) + "-" +
                                        getMsg.substring(6, 8) + " " +
                                        getMsg.substring(8, 10) + ":" +
                                        getMsg.substring(10, 12) + ":" +
                                        getMsg.substring(12, 14);
                                //tvStateTcpDk.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x02;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }

                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                        HzyUtils.closeProgressDialog();
                    }
                }).start();

                break;
            case R.id.btnReadDk://读端口状态
                int xx = spinnerX;
                if (xx < 0 || xx > 5) {
                    HintDialog.ShowHintDialog(P_stateActivity.this, "端口范围为1~6", "错误");
                    return;
                }
                Msg = "000300" + (20 + xx) + "0001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读端口状态:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_stateActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg.substring(0, 2);
                                //tvStateDk.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x03;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }

                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                        HzyUtils.closeProgressDialog();
                    }
                }).start();

                break;
            case R.id.btnReadDydy://读电源电压
                Msg = "000300050001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读电池电压:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_stateActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = "电池电压:" + Integer.parseInt(getMsg.substring(0, 2), 16) + "."
                                        + Integer.parseInt(getMsg.substring(2, 4), 16) + "V";
                                //tvStateDydy.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x04;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnReadSbms://读设备模式
                Msg = "000300090001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读设备模式:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_stateActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                //tvStateSbms.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x05;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnReadYjcs://读硬件参数
                Msg = "0003000e0001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读硬件参数:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_stateActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);

                                Message message = new Message();
                                message.what = 0x06;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            case R.id.btnReadRjcs://读软件参数
                Msg = "0003000f0001";
                crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读软件参数:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                HzyUtils.showProgressDialog(P_stateActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(MenuActivity.CMD_DELAY);
                            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length);
                            if (HzyUtils.CRC16(x).equals(y)) {
                                int msgLength = Integer.valueOf(msg.substring(4, 6));
                                String getMsg = msg.substring(6, 6 + msgLength * 2);
                                getMsg = getMsg;

                                //tvStateRjcs.setText(getMsg);
                                Message message = new Message();
                                message.what = 0x07;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_stateActivity.this, "CRC16校验码错误", "提示");
                                Looper.loop();
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (InterruptedException e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();

                break;
            default:
                break;
        }
    }


   /* public static void sendCmd0(String sendMsg) {
        Log.d("limbo","发送:" +sendMsg);
        CjjSettingActivity.Cjj_CB_MSG = "";//清空记录
        byte[] bos = sendMsg.getBytes();
        byte[] bos_new = new byte[bos.length];
        int i, n;
        n = 0;
        for (i = 0; i < bos.length; i++) {
            bos_new[n] = bos[i];
            n++;
        }
        try {
            MainActivity.netThread.write(bos_new);
        } catch (IOException e) {
            Log.d("limbo", "发送信息出错"+e.toString());
        }
    }
*/


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    tvStateTxj.setText(msg.obj.toString());
                    break;
                case 0x01:
                    tvStateTcpJl.setText(msg.obj.toString());
                    break;
                case 0x02:
                    tvStateTcpDk.setText(msg.obj.toString());
                    break;
                case 0x03:
                    tvStateDk.setText(msg.obj.toString());
                    break;
                case 0x04:
                    tvStateDydy.setText(msg.obj.toString());
                    break;
                case 0x05:
                    if (msg.obj.toString().equals("0009")) {
                        tvStateSbms.setText("P型主分一体机");
                    } else {
                        tvStateSbms.setText("R型主分一体机");
                    }

                    break;
                case 0x06:
                    tvStateYjcs.setText(msg.obj.toString());
                    break;
                case 0x07:
                    tvStateRjcs.setText(msg.obj.toString());
                    break;
                case 0x08:
                    tvStateTxmkxh.setText(msg.obj.toString());
                    break;
                case 0x09:
                    String result = msg.obj.toString();
                    Log.d("limbo", result);
                    if (result.length()>780){
                        String result1 = result.substring(548, 552);
                        if (result1.substring(2,4).equals("00")) {
                            tvStateTxj.setText("信号强度:" + Integer.parseInt(result1.substring(0, 2), 16));
                        } else {
                            tvStateTxj.setText("读取信号强度失败");
                        }
                        result1 = result.substring(768,784);
                        result1 = result1.substring(0, 4) + "-" +
                                result1.substring(4, 6) + "-" +
                                result1.substring(6, 8) + " " +
                                result1.substring(8, 10) + ":" +
                                result1.substring(10, 12) + ":" +
                                result1.substring(12, 14);
                        tvStateTcpJl.setText(result1);
                        result1 = result.substring(784,800);
                        result1 = result1.substring(0, 4) + "-" +
                                result1.substring(4, 6) + "-" +
                                result1.substring(6, 8) + " " +
                                result1.substring(8, 10) + ":" +
                                result1.substring(10, 12) + ":" +
                                result1.substring(12, 14);
                        tvStateTcpDk.setText(result1);
                        result1 = result.substring(552,568);
                        tvStateTxmkxh.setText(HzyUtils.toStringHex(result1).replaceAll("�", ""));
                        result1 = result.substring((20+spinnerX)*4,(20+spinnerX)*4+2);
                        if (result1.equals("ff")){
                            tvStateDk.setText("短路");
                        }else {
                            tvStateDk.setText(Integer.parseInt(result1, 16)+"");
                        }
                        result1 = result.substring(20,24);
                        tvStateDydy.setText("电池电压:" + Integer.parseInt(result1.substring(0, 2), 16) + "."
                                + Integer.parseInt(result1.substring(2, 4), 16) + "V");
                        result1 = result.substring(36,40);
                        if (result1.equals("0009")){
                            tvStateSbms.setText("P型主分一体机");
                        } else {
                            tvStateSbms.setText("R型主分一体机");
                        }
                        result1 = result.substring(56,60);
                        tvStateYjcs.setText(result1);
                        result1 = result.substring(60,64);
                        tvStateRjcs.setText(result1);
                    }else {
                        HintDialog.ShowHintDialog(P_stateActivity.this,"返回数据缺失","错误");
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

        SharedPreferences.Editor editor = getSharedPreferences("user_msg1", MODE_PRIVATE).edit();
        editor.putString("tvStateTxj", tvStateTxj.getText().toString());
        editor.putString("tvStateTcpJl", tvStateTcpJl.getText().toString());
        editor.putString("tvStateTcpDk", tvStateTcpDk.getText().toString());
        editor.putString("tvStateDk", tvStateDk.getText().toString());
        editor.putString("tvStateDydy", tvStateDydy.getText().toString());
        editor.putString("tvStateSbms", tvStateSbms.getText().toString());
        editor.putString("tvStateYjcs", tvStateYjcs.getText().toString());
        editor.putString("tvStateRjcs", tvStateRjcs.getText().toString());
        editor.putString("tvStateTxmkxh", tvStateTxmkxh.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    private void loadUser() {
        SharedPreferences pref = getSharedPreferences("user_msg1", MODE_PRIVATE);
        tvStateTxj.setText(pref.getString("tvStateTxj", ""));
        tvStateTcpJl.setText(pref.getString("tvStateTcpJl", ""));
        tvStateTcpDk.setText(pref.getString("tvStateTcpDk", ""));
        tvStateDk.setText(pref.getString("tvStateDk", ""));
        tvStateDydy.setText(pref.getString("tvStateDydy", ""));
        tvStateSbms.setText(pref.getString("tvStateSbms", ""));
        tvStateYjcs.setText(pref.getString("tvStateYjcs", ""));
        tvStateRjcs.setText(pref.getString("tvStateRjcs", ""));
        tvStateTxmkxh.setText(pref.getString("tvStateTxmkxh", ""));
    }
}
