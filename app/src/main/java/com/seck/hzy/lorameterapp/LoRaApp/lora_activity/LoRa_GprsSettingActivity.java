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

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;

/**
 * Created by ssHss on 2016/8/15.
 */
public class LoRa_GprsSettingActivity extends Activity implements View.OnClickListener {

    private Button GetGprsApn, SendGprsApn, GetGprsDkh, SendGprsDkh, GetGprsIp, SendGprsIp, GetSIMCCID, SendSIMCCID,
            btnGetTcpCut, btnGetTcpBuild, btnGetGPRSError;
    private EditText etGprsApn, etGprsDkh, etGprsIp, etSIMCCID, etTcpCut, etTcpBuild, etGPRSError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_gprssettingactivity);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        etGprsApn = (EditText) findViewById(R.id.GprsSettingActivity_et_GprsApn);
        etGprsDkh = (EditText) findViewById(R.id.GprsSettingActivity_et_GprsDkh);
        etGprsIp = (EditText) findViewById(R.id.GprsSettingActivity_et_GprsIp);
        etSIMCCID = (EditText) findViewById(R.id.GprsSettingActivity_et_SIMCCID);
        etTcpCut = (EditText) findViewById(R.id.LoRaSettingActivity_et_TCPCut);
        etTcpBuild = (EditText) findViewById(R.id.LoRaSettingActivity_et_TCPBuild);
        etGPRSError = (EditText) findViewById(R.id.LoRaSettingActivity_et_GPRSError);

        GetGprsApn = (Button) findViewById(R.id.GprsSettingActivity_btn_GetGprsApn);
        GetGprsApn.setOnClickListener(this);
        SendGprsApn = (Button) findViewById(R.id.GprsSettingActivity_btn_SendGprsApn);
        SendGprsApn.setOnClickListener(this);
        GetGprsDkh = (Button) findViewById(R.id.GprsSettingActivity_btn_GetGprsDkh);
        GetGprsDkh.setOnClickListener(this);
        SendGprsDkh = (Button) findViewById(R.id.GprsSettingActivity_btn_SendGprsDkh);
        SendGprsDkh.setOnClickListener(this);
        GetGprsIp = (Button) findViewById(R.id.GprsSettingActivity_btn_GetGprsIp);
        GetGprsIp.setOnClickListener(this);
        SendGprsIp = (Button) findViewById(R.id.GprsSettingActivity_btn_SendGprsIp);
        SendGprsIp.setOnClickListener(this);
        GetSIMCCID = (Button) findViewById(R.id.GprsSettingActivity_btn_GetSIMCCID);
        GetSIMCCID.setOnClickListener(this);
        btnGetTcpCut = (Button) findViewById(R.id.LoRaSettingActivity_btn_GetTCPCut);
        btnGetTcpCut.setOnClickListener(this);
        btnGetTcpBuild = (Button) findViewById(R.id.LoRaSettingActivity_btn_GetTCPBuild);
        btnGetTcpBuild.setOnClickListener(this);
        btnGetGPRSError = (Button) findViewById(R.id.LoRaSettingActivity_btn_GetGPRSError);
        btnGetGPRSError.setOnClickListener(this);

        loadUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 读GPRS的APN
             */
            case R.id.GprsSettingActivity_btn_GetGprsApn:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "000300c80008";
                    sendMsg = sendMsg + HzyUtils.CRC16("000300c80008");
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
             * 写GPRS的APN
             */
            case R.id.GprsSettingActivity_btn_SendGprsApn:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    String userMsg = etGprsApn.getText().toString().trim();
                    userMsg = HzyUtils.convertStringToHex(userMsg);
                    if (userMsg.length() <= 10) {
                        while (userMsg.length() < 10) {
                            userMsg = userMsg + "0";
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "数据过长", "错误");
                        break;
                    }
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "001600d3003605" + userMsg;
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
             * 读GPRS的端口号
             */
            case R.id.GprsSettingActivity_btn_GetGprsDkh:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "000300900001";
                    sendMsg = sendMsg + HzyUtils.CRC16("000300900001");
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
             * 设置GPRS端口号
             */
            case R.id.GprsSettingActivity_btn_SendGprsDkh:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    String userMsg = Integer.toHexString(Integer.valueOf(etGprsDkh.getText().toString().trim()));
                    if (userMsg.length() <= 4) {
                        while (userMsg.length() < 4) {
                            userMsg = "0" + userMsg;
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "数据过长", "错误");
                        break;
                    }
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "001600d3003402" + userMsg;
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
             * 读GPRS的IP
             */
            case R.id.GprsSettingActivity_btn_GetGprsIp:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "00030091000a";
                    sendMsg = sendMsg + HzyUtils.CRC16("00030091000a");
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
             * 设置GPRS的IP地址
             */
            case R.id.GprsSettingActivity_btn_SendGprsIp:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    String userMsg = etGprsIp.getText().toString().trim();
                    userMsg = HzyUtils.convertStringToHex(userMsg);
                    if (userMsg.length() <= 40) {
                        while (userMsg.length() < 40) {
                            userMsg = userMsg + "0";
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "数据过长", "错误");
                        break;
                    }
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "001600d3002014" + userMsg;
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
             * 读GPRS的SIMCCID
             */
            case R.id.GprsSettingActivity_btn_GetSIMCCID:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "000300d3000a";
                    sendMsg = sendMsg + HzyUtils.CRC16("000300d3000a");
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
             * 获取TCP断开时间
             */
            case R.id.LoRaSettingActivity_btn_GetTCPCut:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "000300c40004";
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
                break;
            /**
             * 获取TCP建立时间
             */
            case R.id.LoRaSettingActivity_btn_GetTCPBuild:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "000300c00004";
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
                break;
            /**
             * 获取GPRS异常代码
             *
             */
            case R.id.LoRaSettingActivity_btn_GetGPRSError:
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_GprsSettingActivity.this);
                    String sendMsg = "000300d50001";
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
            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 * 读GPRS的APN
                 */
                case 0x00:
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 22);
                        getMsg = HzyUtils.toStringHex(getMsg).replaceAll("�", "");
                        etGprsApn.setText(getMsg);
                    }

                    break;
                /**
                 * 读GPRS的端口号
                 */
                case 0x01:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 10);
                        getMsg = Integer.parseInt(getMsg, 16) + "";
                        etGprsDkh.setText(getMsg);
                    }
                    break;
                /**
                 * 读GPRS的IP
                 */
                case 0x02:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 46);
                        getMsg = HzyUtils.toStringHex(getMsg).replaceAll("�", "");
                        etGprsIp.setText(getMsg);
                    }
                    break;
                /**
                 * 读GPRS的SIMCCID
                 */
                case 0x03:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 46);
                        getMsg = HzyUtils.toStringHex(getMsg).replaceAll("�", "");
                        etSIMCCID.setText(getMsg);
                    }
                    break;
                /**
                 * 读TCP断开时间
                 */
                case 0x04:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 22);
                        etTcpCut.setText(getMsg.substring(0, 4) + "-" +
                                        getMsg.substring(4, 6) + "-" +
                                        getMsg.substring(6, 8) + " " +
                                        getMsg.substring(8, 10) + ":" +
                                        getMsg.substring(10, 12) + ":" +
                                        getMsg.substring(12, 14)
                        );
                    }

                    break;
                /**
                 * 读TCP建立时间
                 */
                case 0x05:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 22);
                        etTcpBuild.setText(getMsg.substring(0, 4) + "-" +
                                        getMsg.substring(4, 6) + "-" +
                                        getMsg.substring(6, 8) + " " +
                                        getMsg.substring(8, 10) + ":" +
                                        getMsg.substring(10, 12) + ":" +
                                        getMsg.substring(12, 14) + ":"
                        );
                    }

                    break;
                /**
                 * 读GPRS异常代码
                 */
                case 0x06:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 10);
                        if (getMsg.equals("0000")) {
                            etGPRSError.setText(getMsg);
                        } else {
                            etGPRSError.setText(getMsg);
                        }
                    }

                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_GprsSettingActivity.this, "未接收到数据", "提示");
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

        SharedPreferences.Editor editor = getSharedPreferences("GPRSSetting", MODE_PRIVATE).edit();
        editor.putString("gprsApn", etGprsApn.getText().toString());
        editor.putString("gprsDkh", etGprsDkh.getText().toString());
        editor.putString("gprsIp", etGprsIp.getText().toString());
        editor.putString("simCCID", etSIMCCID.getText().toString());
        editor.putString("tcpCut", etTcpCut.getText().toString());
        editor.putString("tcpBuild", etTcpBuild.getText().toString());
        editor.putString("gprsError", etGPRSError.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("GPRSSetting", MODE_PRIVATE);
        etGprsApn.setText(pref.getString("gprsApn", ""));
        etGprsDkh.setText(pref.getString("gprsDkh", ""));
        etGprsIp.setText(pref.getString("gprsIp", ""));
        etSIMCCID.setText(pref.getString("simCCID", ""));
        etTcpCut.setText(pref.getString("tcpCut", ""));
        etTcpBuild.setText(pref.getString("tcpBuild", ""));
        etGPRSError.setText(pref.getString("gprsError", ""));
    }

    @Override
    protected void onDestroy() {
        saveUser();
        super.onDestroy();
    }
}