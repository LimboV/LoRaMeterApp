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

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2016/10/12.
 */
public class LoRa_Meter485TestActivity extends Activity {

    @BindView(R.id.Meter485TestActivity_tv_showMsg)
    TextView Meter485TestActivity_tv_showMsg;

    @BindView(R.id.Meter485TestActivity_et_netid)
    EditText Meter485TestActivity_et_netid;

    @BindView(R.id.Meter485TestActivity_et_mkid)
    EditText Meter485TestActivity_et_mkid;

    @BindView(R.id.Meter485TestActivity_et_freq)
    EditText Meter485TestActivity_et_freq;

    @BindView(R.id.Meter485TestActivity_et_fc0)
    EditText Meter485TestActivity_et_fc0;

    @BindView(R.id.Meter485TestActivity_et_fc1)
    EditText Meter485TestActivity_et_fc1;

    @BindView(R.id.Meter485TestActivity_et_fc2)
    EditText Meter485TestActivity_et_fc2;

    @BindView(R.id.Meter485TestActivity_et_fc3)
    EditText Meter485TestActivity_et_fc3;

    @BindView(R.id.Meter485TestActivity_et_fc4)
    EditText Meter485TestActivity_et_fc4;

    @BindView(R.id.Meter485TestActivity_et_fc5)
    EditText Meter485TestActivity_et_fc5;

    @BindView(R.id.Meter485TestActivity_et_fc6)
    EditText Meter485TestActivity_et_fc6;

    @BindView(R.id.Meter485TestActivity_et_fc7)
    EditText Meter485TestActivity_et_fc7;

    @BindView(R.id.Meter485TestActivity_et_fc8)
    EditText Meter485TestActivity_et_fc8;

    @BindView(R.id.Meter485TestActivity_et_fc9)
    EditText Meter485TestActivity_et_fc9;

    @BindView(R.id.Meter485TestActivity_et_fc10)
    EditText Meter485TestActivity_et_fc10;

    @BindView(R.id.Meter485TestActivity_et_fc11)
    EditText Meter485TestActivity_et_fc11;

    @BindView(R.id.Meter485TestActivity_et_fc12)
    EditText Meter485TestActivity_et_fc12;

    @BindView(R.id.Meter485TestActivity_et_fc13)
    EditText Meter485TestActivity_et_fc13;

    @BindView(R.id.Meter485TestActivity_et_fc14)
    EditText Meter485TestActivity_et_fc14;

    @BindView(R.id.Meter485TestActivity_et_fc15)
    EditText Meter485TestActivity_et_fc15;

    @BindView(R.id.Meter485TestActivity_et_fc16)
    EditText Meter485TestActivity_et_fc16;

    @BindView(R.id.Meter485TestActivity_et_fc17)
    EditText Meter485TestActivity_et_fc17;

    @BindView(R.id.Meter485TestActivity_et_fc18)
    EditText Meter485TestActivity_et_fc18;

    @BindView(R.id.Meter485TestActivity_et_fc19)
    EditText Meter485TestActivity_et_fc19;

    @BindView(R.id.Meter485TestActivity_btn_getData)
    Button Meter485TestActivity_btn_getData;

    @BindView(R.id.Meter485TestActivity_btn_setData)
    Button Meter485TestActivity_btn_setData;

    @BindView(R.id.Meter485TestActivity_btn_test)
    Button Meter485TestActivity_btn_test;

    List<String> fcList = new ArrayList<>();

    int[] idList = {
            R.id.Meter485TestActivity_et_fc0,
            R.id.Meter485TestActivity_et_fc1,
            R.id.Meter485TestActivity_et_fc2,
            R.id.Meter485TestActivity_et_fc3,
            R.id.Meter485TestActivity_et_fc4,
            R.id.Meter485TestActivity_et_fc5,
            R.id.Meter485TestActivity_et_fc6,
            R.id.Meter485TestActivity_et_fc7,
            R.id.Meter485TestActivity_et_fc8,
            R.id.Meter485TestActivity_et_fc9,
            R.id.Meter485TestActivity_et_fc10,
            R.id.Meter485TestActivity_et_fc11,
            R.id.Meter485TestActivity_et_fc12,
            R.id.Meter485TestActivity_et_fc13,
            R.id.Meter485TestActivity_et_fc14,
            R.id.Meter485TestActivity_et_fc15,
            R.id.Meter485TestActivity_et_fc16,
            R.id.Meter485TestActivity_et_fc17,
            R.id.Meter485TestActivity_et_fc18,
            R.id.Meter485TestActivity_et_fc19,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_yc485test);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        loadUser();
        Meter485TestActivity_btn_getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xxx = 0;
                Meter485TestActivity_tv_showMsg.setText("接收数据:\n");
                String freq = Meter485TestActivity_et_freq.getText().toString().trim();
                String netId = Meter485TestActivity_et_netid.getText().toString().trim();
                String mkId = Meter485TestActivity_et_mkid.getText().toString().trim();
                fcList.clear();
                for (int i = 0; i < 20; i++) {
                    EditText view = (EditText) findViewById(idList[i]);
                    fcList.add(i, view.getText().toString().trim());
                }

                for (int i = 0; i < 20; i++) {
                    if (fcList.get(i).length() > 0) {
                        xxx++;
                    }
                }
                /**
                 * 将分采个数转为1字节-16进制
                 */
                String strx = HzyUtils.toHexString(xxx + "");
                if (xxx < 10) {
                    strx = "0" + strx;
                } else {
                    strx = "" + strx;
                }
                if (freq.length() == 0 || netId.length() == 0 || mkId.length() == 0) {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据不可为空", "错误");
                    return;
                }
                freq = Integer.toHexString(Integer.valueOf(freq + "0"));
                if (freq.length() <= 4) {
                    freq = HzyUtils.isLength(freq, 4);
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (netId.length() <= 4) {
                    while (netId.length() < 4) {
                        netId = "0" + netId;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (mkId.length() <= 10) {
                    while (mkId.length() < 10) {
                        mkId = "0" + mkId;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }

                for (int i = 0; i < 20; i++) {
                    if (fcList.get(i).length() <= 2) {
                        while (fcList.get(i).length() < 2) {
                            fcList.set(i, "0" + fcList.get(i));
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                    }
                }
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    HzyUtils.showProgressDialog(LoRa_Meter485TestActivity.this);
                    String fcMsg = "";
                    for (String fc : fcList) {
                        fc = HzyUtils.toHexString(fc);
                        if (fc.length() < 2)
                            fc = "0" + fc;
                        fcMsg = fcMsg + fc;
                    }
                    String sendMsg = "68" + freq + netId + mkId + "402aa10140" + strx + fcMsg + "000000000000005B5B5B3F3F7B7B7B01FE3F5D5D5D";
                    //                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    MenuActivity.btAuto = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (MenuActivity.btAuto) {
                                    Thread.sleep(3000);
                                    String getMsg = MenuActivity.Cjj_CB_MSG;
                                    MenuActivity.Cjj_CB_MSG = "";
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
                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")|| MenuActivity.METER_STYLE.equals("CS")) {//Wmrnet表

                }
            }
        });

        Meter485TestActivity_btn_setData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xxx = 0;
                Meter485TestActivity_tv_showMsg.setText("接收数据:\n");
                String freq = Meter485TestActivity_et_freq.getText().toString().trim();
                String netId = Meter485TestActivity_et_netid.getText().toString().trim();
                String mkId = Meter485TestActivity_et_mkid.getText().toString().trim();
                fcList.clear();
                for (int i = 0; i < 20; i++) {
                    EditText view = (EditText) findViewById(idList[i]);
                    fcList.add(i, view.getText().toString().trim());
                }

                for (int i = 0; i < 20; i++) {
                    if (fcList.get(i).length() > 0) {
                        xxx++;
                    }
                }
                /**
                 * 将分采个数转为1字节-16进制
                 */
                String strx = HzyUtils.toHexString(xxx + "");
                if (xxx < 10) {
                    strx = "0" + strx;
                } else {
                    strx = "" + strx;
                }
                /**
                 * 频率，网络id，模块id不得为0
                 */
                if (freq.length() == 0 || netId.length() == 0 || mkId.length() == 0) {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据不可为空", "错误");
                    return;
                }
                freq = Integer.toHexString(Integer.valueOf(freq + "0"));
                if (freq.length() <= 4) {
                    while (freq.length() < 4) {
                        freq = "0" + freq;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (netId.length() <= 4) {
                    while (netId.length() < 4) {
                        netId = "0" + netId;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (mkId.length() <= 10) {
                    while (mkId.length() < 10) {
                        mkId = "0" + mkId;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }

                for (int i = 0; i < 20; i++) {
                    if (fcList.get(i).length() <= 2) {
                        while (fcList.get(i).length() < 2) {
                            fcList.set(i, "0" + fcList.get(i));
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                    }
                }

                /**
                 * 表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    HzyUtils.showProgressDialog(LoRa_Meter485TestActivity.this);
                    String fcMsg = "";
                    for (String fc : fcList) {
                        fcMsg = fcMsg + fc;
                    }
                    /**
                     * 《写入》功能：
                     * 68+频率（2）+网络id（2）+模块id（5）+命令号4C+15A0F040+分采个数+分采号（20）分采号没填的补成00
                     */
                    String sendMsg = "68" + freq + netId + mkId + "4c15a0f040" + strx + fcMsg + "000000000000005B5B5B3F3F7B7B7B01FE3F5D5D5D";
                    //                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    MenuActivity.btAuto = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (MenuActivity.btAuto) {
                                    Thread.sleep(5000);
                                    String getMsg = MenuActivity.Cjj_CB_MSG;
                                    MenuActivity.Cjj_CB_MSG = "";
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

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")|| MenuActivity.METER_STYLE.equals("CS")) {//Wmrnet表

                }
            }
        });

        /**
         * 《读取》的发送指令：68 1338 0814 0000000021 4B 02A0F0401401
         68+频率（2）+网络id（2）+模块id（5）+命令号4B+02A0F0401401
         */
        Meter485TestActivity_btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xxx = 0;
                Meter485TestActivity_tv_showMsg.setText("接收数据:\n");
                String freq = Meter485TestActivity_et_freq.getText().toString().trim();
                String netId = Meter485TestActivity_et_netid.getText().toString().trim();
                String mkId = Meter485TestActivity_et_mkid.getText().toString().trim();
                fcList.clear();
                for (int i = 0; i < 20; i++) {
                    EditText view = (EditText) findViewById(idList[i]);
                    fcList.add(i, view.getText().toString().trim());
                }

                for (int i = 0; i < 20; i++) {
                    if (fcList.get(i).length() > 0) {
                        xxx++;
                    }
                }
                /**
                 * 将分采个数转为1字节-16进制
                 */
                String strx = HzyUtils.toHexString(xxx + "");
                if (xxx < 10) {
                    strx = "0" + strx;
                } else {
                    strx = "" + strx;
                }
                /**
                 * 频率，网络id，模块id不得为0
                 */
                if (freq.length() == 0 || netId.length() == 0 || mkId.length() == 0) {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据不可为空", "错误");
                    return;
                }
                freq = Integer.toHexString(Integer.valueOf(freq + "0"));
                if (freq.length() <= 4) {
                    while (freq.length() < 4) {
                        freq = "0" + freq;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (netId.length() <= 4) {
                    while (netId.length() < 4) {
                        netId = "0" + netId;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (mkId.length() <= 10) {
                    while (mkId.length() < 10) {
                        mkId = "0" + mkId;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }

                for (int i = 0; i < 20; i++) {
                    if (fcList.get(i).length() <= 2) {
                        while (fcList.get(i).length() < 2) {
                            fcList.set(i, "0" + fcList.get(i));
                        }
                    } else {
                        HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                    }
                }
                /**
                 * 表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    HzyUtils.showProgressDialog(LoRa_Meter485TestActivity.this);
                    String fcMsg = "";
                    for (String fc : fcList) {
                        fcMsg = fcMsg + fc;
                    }
                    /**
                     * 《读取》的发送指令：68 1338 0814 0000000021 4B 02A0F0401401
                     68+频率（2）+网络id（2）+模块id（5）+命令号4B+02A0F0401401
                     */
                    String sendMsg = "68" + freq + netId + mkId + "4b02a0f0401401" + strx + fcMsg + "000000000000005B5B5B3F3F7B7B7B01FE3F5D5D5D";
                    //                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    MenuActivity.btAuto = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (MenuActivity.btAuto) {
                                    Thread.sleep(5000);
                                    String getMsg = MenuActivity.Cjj_CB_MSG;
                                    MenuActivity.Cjj_CB_MSG = "";
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

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")|| MenuActivity.METER_STYLE.equals("CS")) {//Wmrnet表

                }

            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.trim().length() != 0) {
                        getMsg = HzyUtils.toStringHex1(getMsg).replaceAll("�", "");
                        Meter485TestActivity_tv_showMsg.append(getMsg + "\n");
                    }

                    break;
                /**
                 * 读取
                 */
                case 0x01:
                    getMsg = msg.obj.toString();
                    HzyUtils.closeProgressDialog();
                    if (getMsg.trim().length() != 0) {
                        getMsg = HzyUtils.toStringHex1(getMsg).replaceAll("�", "");
                        Log.d("limbo", "读到数据:" + getMsg);
                        Meter485TestActivity_tv_showMsg.append(getMsg + "\n");
                        getMsg = getMsg.replaceAll("\n", "");
                        if(getMsg.contains("AAAAAAAAAA")&& getMsg.contains("4B")){
                            int resultLen = Integer.parseInt(getMsg.substring(getMsg.indexOf("AAAAAAAAAA") + 12, getMsg.indexOf("AAAAAAAAAA") + 14)) - 2;
                            String resultMsg = getMsg.substring(getMsg.indexOf("AAAAAAAAAA") + 14, getMsg.indexOf("AAAAAAAAAA") + 14 + resultLen * 2);
                            for (int i = 0; i < resultLen; i++) {
                                EditText view = (EditText) findViewById(idList[i]);
                                view.setText(resultMsg.substring(2 * i,     2 + 2 * i));
                            }
                        }
                    }

                    break;
                /**
                 * 写入
                 */
                case 0x02:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.trim().length() != 0) {
                        getMsg = HzyUtils.toStringHex1(getMsg).replaceAll("�", "");
                        Log.d("limbo", "读到数据:" + getMsg);
                        Meter485TestActivity_tv_showMsg.append(getMsg + "\n");
                        getMsg = getMsg.replaceAll("\n", "");
                        if (getMsg.contains("AAAAAAAAAA")){
                            String resultMsg = getMsg.substring(getMsg.indexOf("AAAAAAAAAA")+14, getMsg.indexOf("AAAAAAAAAA")+16);
                            if (resultMsg.equals("AA")) {
                                HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "写入成功", "提示");
                            } else {
                                HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "写入失败", "提示");
                            }
                        }

                    }

                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "未接收到数据", "提示");
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
        SharedPreferences.Editor editor = getSharedPreferences("485Test", MODE_PRIVATE).edit();
        editor.putString("etNetId", Meter485TestActivity_et_netid.getText().toString().trim());
        editor.putString("etMkId", Meter485TestActivity_et_mkid.getText().toString().trim());
        editor.putString("etFreq", Meter485TestActivity_et_freq.getText().toString().trim());
        editor.putString("etfc0", Meter485TestActivity_et_fc0.getText().toString().trim());
        editor.putString("etfc1", Meter485TestActivity_et_fc1.getText().toString().trim());
        editor.putString("etfc2", Meter485TestActivity_et_fc2.getText().toString().trim());
        editor.putString("etfc3", Meter485TestActivity_et_fc3.getText().toString().trim());
        editor.putString("etfc4", Meter485TestActivity_et_fc4.getText().toString().trim());
        editor.putString("etfc5", Meter485TestActivity_et_fc5.getText().toString().trim());
        editor.putString("etfc6", Meter485TestActivity_et_fc6.getText().toString().trim());
        editor.putString("etfc7", Meter485TestActivity_et_fc7.getText().toString().trim());
        editor.putString("etfc8", Meter485TestActivity_et_fc8.getText().toString().trim());
        editor.putString("etfc9", Meter485TestActivity_et_fc9.getText().toString().trim());
        editor.putString("etfc10", Meter485TestActivity_et_fc10.getText().toString().trim());
        editor.putString("etfc11", Meter485TestActivity_et_fc11.getText().toString().trim());
        editor.putString("etfc12", Meter485TestActivity_et_fc12.getText().toString().trim());
        editor.putString("etfc13", Meter485TestActivity_et_fc13.getText().toString().trim());
        editor.putString("etfc14", Meter485TestActivity_et_fc14.getText().toString().trim());
        editor.putString("etfc15", Meter485TestActivity_et_fc15.getText().toString().trim());
        editor.putString("etfc16", Meter485TestActivity_et_fc16.getText().toString().trim());
        editor.putString("etfc17", Meter485TestActivity_et_fc17.getText().toString().trim());
        editor.putString("etfc18", Meter485TestActivity_et_fc18.getText().toString().trim());
        editor.putString("etfc19", Meter485TestActivity_et_fc19.getText().toString().trim());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("485Test", MODE_PRIVATE);
        Meter485TestActivity_et_netid.setText(pref.getString("etNetId", ""));
        Meter485TestActivity_et_mkid.setText(pref.getString("etMkId", ""));
        Meter485TestActivity_et_freq.setText(pref.getString("etFreq", ""));
        Meter485TestActivity_et_fc0.setText(pref.getString("etfc0", ""));
        Meter485TestActivity_et_fc1.setText(pref.getString("etfc1", ""));
        Meter485TestActivity_et_fc2.setText(pref.getString("etfc2", ""));
        Meter485TestActivity_et_fc3.setText(pref.getString("etfc3", ""));
        Meter485TestActivity_et_fc4.setText(pref.getString("etfc4", ""));
        Meter485TestActivity_et_fc5.setText(pref.getString("etfc5", ""));
        Meter485TestActivity_et_fc6.setText(pref.getString("etfc6", ""));
        Meter485TestActivity_et_fc7.setText(pref.getString("etfc7", ""));
        Meter485TestActivity_et_fc8.setText(pref.getString("etfc8", ""));
        Meter485TestActivity_et_fc9.setText(pref.getString("etfc9", ""));
        Meter485TestActivity_et_fc10.setText(pref.getString("etfc10", ""));
        Meter485TestActivity_et_fc11.setText(pref.getString("etfc11", ""));
        Meter485TestActivity_et_fc12.setText(pref.getString("etfc12", ""));
        Meter485TestActivity_et_fc13.setText(pref.getString("etfc13", ""));
        Meter485TestActivity_et_fc14.setText(pref.getString("etfc14", ""));
        Meter485TestActivity_et_fc15.setText(pref.getString("etfc15", ""));
        Meter485TestActivity_et_fc16.setText(pref.getString("etfc16", ""));
        Meter485TestActivity_et_fc17.setText(pref.getString("etfc17", ""));
        Meter485TestActivity_et_fc18.setText(pref.getString("etfc18", ""));
        Meter485TestActivity_et_fc19.setText(pref.getString("etfc19", ""));

    }

    @Override
    protected void onDestroy() {
        MenuActivity.btAuto = false;
        saveUser();
        super.onDestroy();
    }
}
