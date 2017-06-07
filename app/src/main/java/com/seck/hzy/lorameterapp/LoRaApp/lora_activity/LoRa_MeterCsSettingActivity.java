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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;


/**
 * Created by ssHss on 2016/7/18.
 */
public class LoRa_MeterCsSettingActivity extends Activity implements View.OnClickListener {
    private EditText etMeterAddr, etNetId, etNetFreq, etWlAddr;
    private Button btnGet, btnSend, btnGetdata;
    private String oldMeterAddr, oldFreq, oldNetID;
    private TextView tvBtMsg, tvWlAddr;
    private CheckBox cbIsLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        //        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_cssettingactivity);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        /**
         * 输入框
         */
        etMeterAddr = (EditText) findViewById(R.id.CsSettingActivity_et_meterAddr);
        etNetId = (EditText) findViewById(R.id.CsSettingActivity_et_netId);
        etNetFreq = (EditText) findViewById(R.id.CsSettingActivity_et_netFreq);
        etWlAddr = (EditText) findViewById(R.id.CsSettingActivity_et_WlAddr);

        /**
         * 按键设置
         */
        btnSend = (Button) findViewById(R.id.CsSettingActivity_btn_send);
        btnSend.setOnClickListener(this);
        btnGet = (Button) findViewById(R.id.CsSettingActivity_btn_get);
        btnGetdata = (Button) findViewById(R.id.CsSettingActivity_btn_getdata);
        btnGetdata.setOnClickListener(this);

        /**
         * 信息显示
         */
        tvBtMsg = (TextView) findViewById(R.id.CsSettingActivity_tv_btMsg);
        tvWlAddr = (TextView) findViewById(R.id.CsSettingActivity_tv_WlAddr);

        cbIsLy = (CheckBox) findViewById(R.id.CsSettingActivity_cb_isLy);

        loadUser();

        if (MenuActivity.METER_STYLE.equals("L")) {//山科LoRa表
            btnGet.setText("超级写");
            tvWlAddr.setText("编号");
        } else {
            etWlAddr.setKeyListener(null);//不可编辑
            btnGetdata.setVisibility(View.GONE);
        }

        MenuActivity.btAuto = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("limbo", "start");
                while (MenuActivity.btAuto) {
                    try {
                        int z = 10;
                        if (z >= 10) {
                            byte[] bos = HzyUtils.getHexBytes("ff");
                            byte[] bos_new = new byte[bos.length];
                            int i, n;
                            n = 0;
                            for (i = 0; i < bos.length; i++) {
                                bos_new[n] = bos[i];
                                n++;
                            }
                            try {
                                MenuActivity.netThread.write(bos_new);
                            } catch (Exception e) {
                                Log.d("limbo", e.toString());
                            }
                        } else {
                            z++;
                        }
                        Thread.sleep(3000);
                        String getMsg = MenuActivity.Cjj_CB_MSG;
                        if (getMsg.length() == 0) {
                        } else {
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", "get:" + getMsg);

                            Message message = new Message();
                            message.what = 0x01;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }

                }

            }
        }).start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /**
             * 山科lora--超级写
             * 其他--获取
             */
            case R.id.CsSettingActivity_btn_get:
                HzyUtils.showProgressDialog(LoRa_MeterCsSettingActivity.this);
                tvBtMsg.setText("");//清空信息显示框
                String etContent1 = etMeterAddr.getText().toString().trim();
                String etContent2 = etNetId.getText().toString().trim();
                String etContent3 = etNetFreq.getText().toString().trim();
                String etContent4 = etWlAddr.getText().toString().trim();
                if (etContent1.length() == 0 || etContent3.length() == 0) {
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "输入不可为空", "提示");
                    break;
                }
                String freq = Integer.toHexString(Integer.parseInt(etContent3));//频率转化成16进制

                while (etContent2.length() < 4) {
                    etContent2 = "0" + etContent2;
                }
                if (etContent2.length() > 4) {
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "网络ID过长", "提示");
                    break;
                }

                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//山科LoRa表
                    //山科LoRa表的读取功能改为超级写功能
                    HzyUtils.closeProgressDialog();

                    String sendMsg;
                    if (cbIsLy.isChecked()) {
                        while (etContent1.length() < 2) {
                            etContent1 = "0" + etContent1;
                        }
                        if (etContent1.length() > 2) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "地址过长", "提示");
                            break;
                        }
                        while (freq.length() < 4) {
                            freq = "0" + freq;
                        }
                        if (freq.length() > 4) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "频率过大", "提示");
                            break;
                        }
                        while (etContent4.length() < 2) {
                            etContent4 = "0" + etContent4;
                        }
                        if (etContent4.length() > 2) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "模块编号过长", "提示");
                            break;
                        }
                        sendMsg = "68"//起始码
                                + oldFreq //原频率
                                + oldNetID  //原网络ID
                                + "0000000000" //无效数据
                                + "84"//命令码
                                + "06"//有效数据长度
                                + "00"//超级写
                                + "5540"//无效
                                + etContent2//网络ID
                                + etContent1//模块id
                                + etContent4//模块编号
                                + freq;//更改的频率
                    } else {
                        while (etContent1.length() < 10) {
                            etContent1 = "0" + etContent1;
                        }
                        if (etContent1.length() > 10) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "地址过长", "提示");
                            break;
                        }
                        while (freq.length() < 4) {
                            freq = "0" + freq;
                        }
                        if (freq.length() > 4) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "频率过大", "提示");
                            break;
                        }
                        while (etContent4.length() < 4) {
                            etContent4 = "0" + etContent4;
                        }
                        if (etContent4.length() > 4) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "模块编号过长", "提示");
                            break;
                        }
                        sendMsg = "68"//起始码
                                + oldFreq //原频率
                                + oldNetID  //原网络ID
                                + "0000000000" //超级写地址为0000000000
                                + "23"//命令码
                                + "0b"//有效数据长度
                                + "a0f0"//控制符
                                + "40"//无效
                                + etContent2//网络ID
                                + etContent1//模块id
                                + etContent4//模块编号
                                + freq;//更改的频率
                    }

                    Log.d("limbo", "设置:" + sendMsg);
                    tvBtMsg.append("\n写入设置--超级写");
                    MenuActivity.sendCmd(sendMsg);

                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                    while (etContent1.length() < 8) {
                        etContent1 = "0" + etContent1;
                    }
                    if (etContent1.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "地址过长", "提示");
                        break;
                    }
                    while (freq.length() < 6) {
                        freq = "0" + freq;
                    }
                    if (freq.length() > 6) {
                        HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "频率过大", "提示");
                        break;
                    }

                    String sendMsg = "ff" + //唤醒
                            freq + //频率
                            "00" + //命令
                            etContent1;//水表地址

                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    tvBtMsg.append("发送读取参数指令:\n频率" + freq + "\n水表地址:" + etContent1);
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

                } else {
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "未获取表类型", "提示");
                }
                break;

            /**
             * 山科lora--获取
             */
            case R.id.CsSettingActivity_btn_getdata:
                tvBtMsg.setText("");//清空信息显示框
                etContent1 = etMeterAddr.getText().toString().trim();
                etContent2 = etNetId.getText().toString().trim();
                etContent3 = etNetFreq.getText().toString().trim();
                if (etContent1.length() == 0 || etContent3.length() == 0) {
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "输入不可为空", "提示");
                    break;
                }
                freq = Integer.toHexString(Integer.parseInt(etContent3));//频率转化成16进制

                while (etContent2.length() < 4) {
                    etContent2 = "0" + etContent2;
                }
                if (etContent2.length() > 4) {
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "网络ID过长", "提示");
                    break;
                }
                while (etContent2.length() < 4) {
                    etContent2 = "0" + etContent2;
                }
                if (etContent1.length() > 10) {
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "表地址过长", "提示");
                    break;
                }
                while (etContent1.length() < 10) {
                    etContent1 = "0" + etContent1;
                }
                String sendMsg = "68" +
                        freq + //频率
                        etContent2 + //网络ID
                        etContent1 +//水表地址
                        "22"+//指令码
                        "0ba0f0400052000000007f00011338";
                Log.d("limbo", "获取:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                tvBtMsg.append("发送读取参数指令:\n频率" + freq + "\n水表地址:" + etContent1);
                break;

            /**
             * 设置
             */
            case R.id.CsSettingActivity_btn_send:
                etContent1 = etMeterAddr.getText().toString().trim();
                etContent2 = etNetId.getText().toString().trim();
                etContent3 = etNetFreq.getText().toString().trim();
                etContent4 = etWlAddr.getText().toString().trim();
                if (etContent1.length() == 0 || etContent2.length() == 0 || etContent3.length() == 0 || etContent4.length() == 0) {
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "输入不可为空", "提示");
                    break;
                }

                while (etContent2.length() < 4) {
                    etContent2 = "0" + etContent2;
                }
                freq = Integer.toHexString(Integer.parseInt(etContent3));//频率转化成16进制

                if (etContent2.length() > 4) {
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "网络ID过长", "提示");
                    break;
                }


                if (MenuActivity.METER_STYLE.equals("L")) {//山科LoRa表
                    while (etContent1.length() < 2) {
                        etContent1 = "0" + etContent1;
                    }
                    if (etContent1.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "模块ID过长", "提示");
                        break;
                    }
                    if (cbIsLy.isChecked()) {//改写路由参数
                        sendMsg = "68"//起始码
                                + oldFreq //原频率
                                + oldNetID  //原网络ID
                                + "0000000000" //无效数据段
                                + "84"//命令码
                                + "06"//有效数据长度
                                + oldMeterAddr//原路由ID
                                + "5540"//无效
                                + etContent2//网络ID
                                + etContent1//模块id
                                + etContent4//模块编号
                                + freq;//更改的频率


                    } else {
                        while (etContent1.length() < 4) {
                            etContent1 = "0" + etContent1;
                        }
                        if (etContent1.length() > 4) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "模块ID过长", "提示");
                            break;
                        }
                        while (etContent1.length() < 10) {
                            etContent1 = "0" + etContent1;
                        }
                        if (etContent1.length() > 10) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "地址过长", "提示");
                            break;
                        }
                        while (freq.length() < 4) {
                            freq = "0" + freq;
                        }
                        if (freq.length() > 4) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "频率过大", "提示");
                            break;
                        }
                        while (etContent4.length() < 4) {
                            etContent4 = "0" + etContent4;
                        }
                        if (etContent4.length() > 4) {
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "模块编号过长", "提示");
                            break;
                        }
                        sendMsg = "68"//起始码
                                + oldFreq //原频率
                                + oldNetID  //原网络ID
                                + oldMeterAddr //超级写地址为0000000000
                                + "23"//命令码
                                + "0b"//有效数据长度
                                + "a0f0"//控制符
                                + "40"//无效
                                + etContent2//网络ID
                                + etContent1//模块id
                                + etContent4//模块编号
                                + freq;//更改的频率
                    }

                    Log.d("limbo", "设置:" + sendMsg);
                    tvBtMsg.append("\n写入设置");
                    MenuActivity.sendCmd(sendMsg);
                    HzyUtils.closeProgressDialog();

                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                    while (etContent1.length() < 8) {
                        etContent1 = "0" + etContent1;
                    }
                    if (etContent1.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "地址过长", "提示");
                        break;
                    }
                    while (freq.length() < 6) {
                        freq = "0" + freq;
                    }
                    if (freq.length() > 6) {
                        HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "频率过大", "提示");
                        break;
                    }
                    while (etContent4.length() < 8) {
                        etContent4 = "0" + etContent4;
                    }
                    if (etContent4.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "物理id过大", "提示");
                        break;
                    }
                    sendMsg = "ff" + //唤醒
                            oldFreq + //原频率
                            "01" + //命令
                            etContent4 + //物理ID
                            freq + //修改后的频率
                            etContent2 + //修改后的网络ID
                            etContent1;//修改后的水表地址

                    Log.d("limbo", "设置:" + sendMsg);
                    tvBtMsg.append("\n写入设置");
                    MenuActivity.sendCmd(sendMsg);


                } else {
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "未获取表类型", "提示");
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        HzyUtils.closeProgressDialog();
        MenuActivity.btAuto = false;
        saveUser(etMeterAddr.getText().toString(), etNetId.getText().toString(), etNetFreq.getText().toString());
        super.onDestroy();
    }

    /**
     * 使用SharePreferences保存用户信息
     *
     * @param x
     * @param y
     * @param z
     */
    public void saveUser(String x, String y, String z) {

        SharedPreferences.Editor editor = getSharedPreferences("csSetting", MODE_PRIVATE).edit();
        editor.putString("csSetting1", x);
        editor.putString("csSetting2", y);
        editor.putString("csSetting3", z);
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("csSetting", MODE_PRIVATE);
        etMeterAddr.setText(pref.getString("csSetting1", ""));
        etNetId.setText(pref.getString("csSetting2", ""));
        etNetFreq.setText(pref.getString("csSetting3", ""));
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString();
                    if (MenuActivity.METER_STYLE.equals("L")) {//山科LoRa表


                    } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                        etMeterAddr.setText(getMsg.substring(16, 24));
                        etNetId.setText(getMsg.substring(12, 16));
                        etNetFreq.setText(Integer.parseInt(getMsg.substring(6, 12), 16) + "");
                        etWlAddr.setText(getMsg.substring(34, 42));

                        tvBtMsg.append("\n接收参数:\n网络ID:" + getMsg.substring(12, 16) + "\n硬件地址:" + getMsg.substring(34, 42));

                        oldFreq = Integer.parseInt(getMsg.substring(6, 12), 16) + "";//原有网络频率
                    }
                    break;
                case 0x01:
                    try {
                        getMsg = msg.obj.toString();
                        /**
                         * 根据表类型使用不同的通讯协议
                         */
                        if (MenuActivity.METER_STYLE.equals("L")) {//山科LoRa表
                            //接收数据为ascii码
                            if (cbIsLy.isChecked()) {
                                if (getMsg.length() > 50 && getMsg.contains("41414141414141414141")) {
                                    getMsg = HzyUtils.toStringHex1(getMsg).replaceAll("�", "").replaceAll(" ", "").replaceAll("\n", "");
                                    Log.d("limbo", getMsg);
                                    tvBtMsg.append(getMsg + "\n");
                                    if (getMsg.contains("Busy")) {
                                        MenuActivity.Cjj_CB_MSG = "";
                                    } else {
                                        getMsg = getMsg.substring(getMsg.indexOf("AAAAAAAAAA") + 14, 40);
                                        String sfreq = getMsg.substring(8, 12);//频率-16进制
                                        String netId = getMsg.substring(0, 4);//网络ID
                                        String mkId = getMsg.substring(4, 6);//模块ID
                                        String wlmoID = getMsg.substring(6, 8);//模块编号
                                        String bbh = getMsg.substring(12, 20);//版本号
                                        String dy = (float) (255 / Integer.parseInt(getMsg.substring(20, 22), 16) * 1.224) + "V";//电池电压
                                        oldFreq = sfreq;
                                        oldNetID = netId;
                                        oldMeterAddr = mkId;
                                        Log.d("limbo", "频率" + sfreq +
                                                "\n网络ID" + netId +
                                                "\n模块ID" + mkId +
                                                "\n物理模块ID" + wlmoID +
                                                "\n版本号" + bbh +
                                                "\n电压" + dy);
                                        tvBtMsg.append("\n" + "频率" + Integer.parseInt(sfreq, 16) / 10 + "Mhz" +
                                                "\n网络ID" + netId +
                                                "\n模块ID" + mkId +
                                                "\n物理模块ID" + wlmoID +
                                                "\n版本号" + bbh +
                                                "\n电压" + dy);
                                        etWlAddr.setText(wlmoID);
                                        etMeterAddr.setText(mkId);
                                        etNetId.setText(netId);
                                        etNetFreq.setText(Integer.parseInt(sfreq, 16) + "");
                                        MenuActivity.Cjj_CB_MSG = "";
                                    }
                                }
                            } else {
                                if (getMsg.length() >= 50 && getMsg.contains("41414141414141414141")) {
                                    getMsg = HzyUtils.toStringHex1(getMsg).replaceAll("�", "").replaceAll(" ", "").replaceAll("\n", "");
                                    Log.d("limbo", getMsg);
                                    tvBtMsg.append(getMsg + "\n");
                                    if (getMsg.contains("Busy")) {
                                        MenuActivity.Cjj_CB_MSG = "";
                                    } else {
                                        getMsg = getMsg.substring(getMsg.indexOf("AAAAAAAAAA") + 14, 50);

                                        String sfreq = getMsg.substring(18, 22);//频率-16进制
                                        String netId = getMsg.substring(0, 4);//网络ID
                                        String mkId = getMsg.substring(4, 14);//模块ID
                                        String wlmoID = getMsg.substring(14, 18);//模块编号
                                        //                        oldFreq = Integer.parseInt(sfreq, 16) + "";
                                        oldFreq = sfreq;
                                        oldNetID = netId;
                                        oldMeterAddr = mkId;
                                        Log.d("limbo", "频率" + sfreq +
                                                "\n网络ID" + netId +
                                                "\n模块ID" + mkId +
                                                "\n物理模块ID" + wlmoID);
                                        tvBtMsg.append("\n" + "频率" + Integer.parseInt(sfreq, 16) / 10 + "Mhz" +
                                                "\n网络ID" + netId +
                                                "\n模块ID" + mkId +
                                                "\n物理模块ID" + wlmoID);
                                        etWlAddr.setText(wlmoID);
                                        etMeterAddr.setText(mkId);
                                        etNetId.setText(netId);
                                        etNetFreq.setText(Integer.parseInt(sfreq, 16) + "");
                                        MenuActivity.Cjj_CB_MSG = "";
                                    }

                                } else {
                                    MenuActivity.Cjj_CB_MSG = "";
                                }
                            }


                        } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                            if (getMsg.length() >= 42) {
                                getMsg = getMsg.substring(getMsg.indexOf("a0"));
                                //                        String freq = Integer.parseInt(getMsg.substring(6, 12), 16) + "";//频率-16进制
                                String sfreq = Integer.parseInt(getMsg.substring(6, 12), 16) + "";//频率-16进制
                                String netId = getMsg.substring(12, 16);//网络ID
                                String mkId = getMsg.substring(16, 24);//模块ID
                                String bds = getMsg.substring(27, 28) +
                                        getMsg.substring(29, 30) +
                                        getMsg.substring(31, 32) +
                                        "." + getMsg.substring(33, 34);//表底数
                                String wlmoID = getMsg.substring(34, 42);//物理模块ID
                                oldFreq = sfreq;
                                Log.d("limbo", "频率" + sfreq +
                                        "\n网络ID" + netId +
                                        "\n模块ID" + mkId +
                                        "\n表底数" + bds +
                                        "\n物理模块ID" + wlmoID);
                                tvBtMsg.append("\n" + "频率" + sfreq +
                                        "\n网络ID" + netId +
                                        "\n模块ID" + mkId +
                                        "\n表底数" + bds +
                                        "\n物理模块ID" + wlmoID);
                                etWlAddr.setText(wlmoID);
                                MenuActivity.Cjj_CB_MSG = "";
                            }
                        } else {
                            HzyUtils.closeProgressDialog();
                            HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "未获取表类型", "提示");
                        }

                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }
                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_MeterCsSettingActivity.this, "未接收到数据", "提示");
                    break;
                default:
                    break;
            }
        }
    };
}
