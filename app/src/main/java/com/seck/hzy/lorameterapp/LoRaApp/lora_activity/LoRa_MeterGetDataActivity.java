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
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ssHss on 2016/7/29.
 */
public class LoRa_MeterGetDataActivity extends Activity {

    @BindView(R.id.GetDataActivity_btn_getData)
    Button GetDataActivity_btn_getData;

    @BindView(R.id.GetDataActivity_btn_zt)
    Button GetDataActivity_btn_zt;

    @BindView(R.id.GetDataActivity_btn_bkf)
    Button GetDataActivity_btn_bkf;

    @BindView(R.id.GetDataActivity_btn_gf)
    Button GetDataActivity_btn_gf;

    @BindView(R.id.GetDataActivity_btn_kf)
    Button GetDataActivity_btn_kf;

    @BindView(R.id.GetDataActivity_btn_fx)
    Button GetDataActivity_btn_fx;

    @BindView(R.id.GetDataActivity_et_meterAddr)
    EditText GetDataActivity_et_meterAddr;

    @BindView(R.id.GetDataActivity_et_freq)
    EditText GetDataActivity_et_freq;

    @BindView(R.id.GetDataActivity_et_netId)
    EditText GetDataActivity_et_netId;

    @BindView(R.id.GetDataActivity_et_jd)
    EditText GetDataActivity_et_jd;

    @BindView(R.id.GetDataActivity_tv_showMsg)
    TextView GetDataActivity_tv_showMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        //        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_get_data);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        ButterKnife.bind(this);

        if (MenuActivity.METER_STYLE.equals("CS")) {
            GetDataActivity_btn_bkf.setVisibility(View.GONE);
            GetDataActivity_btn_fx.setVisibility(View.GONE);
            GetDataActivity_btn_gf.setVisibility(View.GONE);
            GetDataActivity_btn_kf.setVisibility(View.GONE);
            GetDataActivity_btn_zt.setVisibility(View.GONE);
        }
        loadUser();
        GetDataActivity_btn_getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MenuActivity.METER_STYLE.equals("L")) {//山科LoRa表
                    HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                    GetDataActivity_tv_showMsg.setText("");
                    String addr = GetDataActivity_et_meterAddr.getText().toString().trim();
                    if (addr.length() > 10) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                    }
                    while (addr.length() < 10) {
                        addr = "0" + addr;
                    }
                    String sendMsg = "68" //起始符
                            + "10" //类型
                            + addr //地址
                            + "0000" //无效地址
                            + "21"//控制码
                            + "04"//数据长度
                            + "901f"//标识符
                            + "00"//序号
                            + "00";//数据

                    MenuActivity.sendLoRaCmd(sendMsg);
                    GetDataActivity_tv_showMsg.setText("发送读取参数指令:\n水表地址:" + addr);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(4000);
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
                            } catch (Exception e) {
                                e.printStackTrace();
                                HzyUtils.closeProgressDialog();
                            }

                        }
                    }).start();
                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                    GetDataActivity_tv_showMsg.setText("");
                    String addr = GetDataActivity_et_meterAddr.getText().toString().trim();
                    if (addr.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                    }
                    String freq = GetDataActivity_et_freq.getText().toString().trim();
                    if (freq.length() == 0) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "频率不得为空", "提示");
                    } else {
                        freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                    }

                    if (freq.length() > 6) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表频率过大", "提示");
                    }
                    while (addr.length() < 8) {
                        addr = "0" + addr;
                    }
                    while (freq.length() < 6) {
                        freq = "0" + freq;
                    }
                    String sendMsg = "ff" +
                            freq +//频率
                            "1f" +
                            addr;//地址
                    Log.d("limbo", sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    GetDataActivity_tv_showMsg.setText("发送读取参数指令:\n频率" + freq + "\n水表地址:" + addr);
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
                                    message.what = 0x00;
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
                } else if (MenuActivity.METER_STYLE.equals("F")) {//WmrnetFsk表
                    HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                    GetDataActivity_tv_showMsg.setText("");
                    String addr = GetDataActivity_et_meterAddr.getText().toString().trim();
                    String jd = GetDataActivity_et_jd.getText().toString().trim();
                    if (addr.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                    }
                    String freq = GetDataActivity_et_freq.getText().toString().trim();
                    freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                    String netId = GetDataActivity_et_netId.getText().toString().trim();
                    if (jd.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "节点过大", "提示");
                    }
                    while (jd.length() < 8) {
                        jd = "0" + jd;
                    }
                    if (netId.length() > 4) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "网络ID过长", "提示");
                    }
                    while (netId.length() < 4) {
                        netId = "0" + netId;
                    }
                    if (freq.length() > 6) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表频率过大", "提示");
                    }
                    while (addr.length() < 8) {
                        addr = "0" + addr;
                    }
                    while (freq.length() < 6) {
                        freq = "0" + freq;
                    }
                    String sendMsg = "ff" +
                            freq +//频率
                            "01" +
                            netId +//网络ID
                            "0010" +
                            jd;//节点
                    Log.d("limbo", sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    GetDataActivity_tv_showMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
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
                } else if (MenuActivity.METER_STYLE.equals("CS")) {//超声表
                    HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                    GetDataActivity_tv_showMsg.setText("");
                    String addr = GetDataActivity_et_meterAddr.getText().toString().trim();
                    if (addr.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                    }
                    String freq = GetDataActivity_et_freq.getText().toString().trim();
                    if (freq.length() == 0) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "频率不得为空", "提示");
                    } else {
                        freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                    }

                    if (freq.length() > 6) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表频率过大", "提示");
                    }
                    while (addr.length() < 8) {
                        addr = "0" + addr;
                    }
                    while (freq.length() < 6) {
                        freq = "0" + freq;
                    }
                    String sendMsg = "ff" +
                            freq +//频率
                            "13" +
                            addr;//地址
                    Log.d("limbo", sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    GetDataActivity_tv_showMsg.setText("发送读取参数指令:\n频率" + freq + "\n水表地址:" + addr);
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
                                    message.what = 0x00;
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
                }

            }
        });

        /**
         * 阀控指令
         */
        GetDataActivity_btn_zt = (Button) findViewById(R.id.GetDataActivity_btn_zt);
        GetDataActivity_btn_zt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                GetDataActivity_tv_showMsg.setText("");
                String addr = GetDataActivity_et_meterAddr.getText().toString().trim();
                String jd = GetDataActivity_et_jd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = GetDataActivity_et_freq.getText().toString().trim();
                if (freq.isEmpty()) {
                    freq = "0";
                }
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = GetDataActivity_et_netId.getText().toString().trim();
                if (freq.length() > 6) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表频率过大", "提示");
                }
                while (addr.length() < 8) {
                    addr = "0" + addr;
                }
                while (freq.length() < 6) {
                    freq = "0" + freq;
                }
                String sendMsg = "ff" +
                        freq +//频率
                        "0f" +
                        addr +//地址
                        "c0";
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                GetDataActivity_tv_showMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", getMsg);

                            Message message = new Message();
                            message.what = 0x98;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                            HzyUtils.closeProgressDialog();
                        }

                    }
                }).start();
            }
        });
        GetDataActivity_btn_bkf = (Button) findViewById(R.id.GetDataActivity_btn_bkf);
        GetDataActivity_btn_bkf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                GetDataActivity_tv_showMsg.setText("");
                String addr = GetDataActivity_et_meterAddr.getText().toString().trim();
                String jd = GetDataActivity_et_jd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = GetDataActivity_et_freq.getText().toString().trim();
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = GetDataActivity_et_netId.getText().toString().trim();
                if (freq.length() > 6) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表频率过大", "提示");
                }
                while (addr.length() < 8) {
                    addr = "0" + addr;
                }
                while (freq.length() < 6) {
                    freq = "0" + freq;
                }
                String sendMsg = "ff" +
                        freq +//频率
                        "0f" +
                        addr +//地址
                        "c1";
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                GetDataActivity_tv_showMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", getMsg);

                            Message message = new Message();
                            message.what = 0x98;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                            HzyUtils.closeProgressDialog();
                        }

                    }
                }).start();
            }
        });
        GetDataActivity_btn_gf = (Button) findViewById(R.id.GetDataActivity_btn_gf);
        GetDataActivity_btn_gf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                GetDataActivity_tv_showMsg.setText("");
                String addr = GetDataActivity_et_meterAddr.getText().toString().trim();
                String jd = GetDataActivity_et_jd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = GetDataActivity_et_freq.getText().toString().trim();
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = GetDataActivity_et_netId.getText().toString().trim();
                if (freq.length() > 6) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表频率过大", "提示");
                }
                while (addr.length() < 8) {
                    addr = "0" + addr;
                }
                while (freq.length() < 6) {
                    freq = "0" + freq;
                }
                String sendMsg = "ff" +
                        freq +//频率
                        "0f" +
                        addr +//地址
                        "c2";
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                GetDataActivity_tv_showMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", getMsg);

                            Message message = new Message();
                            message.what = 0x98;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                            HzyUtils.closeProgressDialog();
                        }

                    }
                }).start();
            }
        });
        GetDataActivity_btn_kf = (Button) findViewById(R.id.GetDataActivity_btn_kf);
        GetDataActivity_btn_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                GetDataActivity_tv_showMsg.setText("");
                String addr = GetDataActivity_et_meterAddr.getText().toString().trim();
                String jd = GetDataActivity_et_jd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = GetDataActivity_et_freq.getText().toString().trim();
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = GetDataActivity_et_netId.getText().toString().trim();
                if (freq.length() > 6) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表频率过大", "提示");
                }
                while (addr.length() < 8) {
                    addr = "0" + addr;
                }
                while (freq.length() < 6) {
                    freq = "0" + freq;
                }
                String sendMsg = "ff" +
                        freq +//频率
                        "0f" +
                        addr +//地址
                        "c3";
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                GetDataActivity_tv_showMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", getMsg);

                            Message message = new Message();
                            message.what = 0x98;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                            HzyUtils.closeProgressDialog();
                        }

                    }
                }).start();
            }
        });
        GetDataActivity_btn_fx = (Button) findViewById(R.id.GetDataActivity_btn_fx);
        GetDataActivity_btn_fx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                GetDataActivity_tv_showMsg.setText("");
                String addr = GetDataActivity_et_meterAddr.getText().toString().trim();
                String jd = GetDataActivity_et_jd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = GetDataActivity_et_freq.getText().toString().trim();
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = GetDataActivity_et_netId.getText().toString().trim();
                if (freq.length() > 6) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表频率过大", "提示");
                }
                while (addr.length() < 8) {
                    addr = "0" + addr;
                }
                while (freq.length() < 6) {
                    freq = "0" + freq;
                }
                String sendMsg = "ff" +
                        freq +//频率
                        "0f" +
                        addr +//地址
                        "c4";
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                GetDataActivity_tv_showMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", getMsg);

                            Message message = new Message();
                            message.what = 0x98;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                            HzyUtils.closeProgressDialog();
                        }

                    }
                }).start();
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00://安美通表
                    String netId;
                    String getMsg = msg.obj.toString();

                    GetDataActivity_tv_showMsg.append(getMsg + "\n");
                    if (getMsg.length() < 18) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "数据缺失", "提示");
                        break;
                    }
                    String waterValue = getMsg.substring(10, 18);
                    if (MenuActivity.METER_STYLE.equals("W")) {
                        waterValue = waterValue.substring(1, 2) +
                                waterValue.substring(3, 4) +
                                waterValue.substring(5, 6) + "." +
                                waterValue.substring(7, 8);
                    } else if (MenuActivity.METER_STYLE.equals("JY")) {
                        waterValue = waterValue.substring(0, 6) + "." +
                                waterValue.substring(6, 8);
                    } else if (MenuActivity.METER_STYLE.equals("CS")) {
                        waterValue = Integer.parseInt(waterValue.substring(0, 6)) + "." +
                                waterValue.substring(6, 8);
                    }

                    String dy = getMsg.substring(20, 22);
                    String cq = getMsg.substring(22, 24);
                    dy = String.format("%.2f", (float) Integer.parseInt(dy, 16) / 100 + 2);
                    cq = Integer.parseInt(cq, 16) + "";
                    String attack = getMsg.substring(18, 19);
                    if (attack.contains("3")){
                        attack = "异常";
                    }else {
                        attack = "正常";
                    }
                    String fk = getMsg.substring(19, 20);
                    if (fk.contains("0")) {
                        fk = "不明";
                    } else if (fk.contains("1")) {
                        fk = "半开";
                    } else if (fk.contains("2")) {
                        fk = "关阀";
                    } else if (fk.contains("3")) {
                        fk = "开阀";
                    }
                    if (MenuActivity.METER_STYLE.equals("CS")) {
                        GetDataActivity_tv_showMsg.append("\n\n接收参数:" +
                                "\n水表值:" + waterValue + " m³" +
                                "\n电压:" + dy +
                                "\n场强:" + cq);
                    } else {
                        GetDataActivity_tv_showMsg.append("\n\n接收参数:" +
                                "\n水表值:" + waterValue + " m³" +
                                "\n阀控:" + fk +
                                "\n状态:" + attack +
                                "\n电压:" + dy +
                                "\n场强:" + cq);
                    }


                    break;

                case 0x01://LoRa表
                    getMsg = msg.obj.toString();
                    GetDataActivity_tv_showMsg.append(getMsg + "\n");
                    waterValue = getMsg.substring(10, 18);
                    waterValue = waterValue.substring(34, 36) +
                            waterValue.substring(32, 34) +
                            waterValue.substring(30, 32);
                    dy = getMsg.substring(getMsg.length() - 6, getMsg.length() - 4);
                    dy = ((float) Integer.parseInt(dy, 16) / 100 + 2) + "";
                    GetDataActivity_tv_showMsg.append("\n\n接收参数:" +
                            "\n水表值:" + waterValue + " m³" +
                            "\n电压:" + dy);
                    break;
                case 0x02://安美通fsk表
                    getMsg = msg.obj.toString();
                    GetDataActivity_tv_showMsg.append(getMsg + "\n");
                    netId = getMsg.substring(0, 4);
                    waterValue = getMsg.substring(16, 26);
                    waterValue = waterValue.substring(1, 2) +
                            waterValue.substring(3, 4) +
                            waterValue.substring(5, 6) + "." +
                            waterValue.substring(7, 8) +
                            waterValue.substring(9, 10);
                    dy = getMsg.substring(26, 28);
                    dy = ((float) Integer.parseInt(dy, 16) / 100 + 2) + "";
                    GetDataActivity_tv_showMsg.append("\n\n接收参数:" +
                            "\n网络ID:" + netId +
                            "\n水表值:" + waterValue + " m³" +
                            "\n阀控:" + getMsg.substring(18, 20) +
                            "\n电压:" + dy);
                    break;
                case 0x98:
                    HzyUtils.closeProgressDialog();
                    getMsg = msg.obj.toString();
                    GetDataActivity_tv_showMsg.setText(getMsg);
                    Toast.makeText(LoRa_MeterGetDataActivity.this, "命令发送完毕", Toast.LENGTH_LONG).show();
                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "未接收到数据", "提示");
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

        SharedPreferences.Editor editor = getSharedPreferences("MeterGetData", MODE_PRIVATE).edit();
        editor.putString("netId", GetDataActivity_et_netId.getText().toString());
        editor.putString("jd", GetDataActivity_et_netId.getText().toString());
        editor.putString("freq", GetDataActivity_et_freq.getText().toString());
        editor.putString("meterAddr", GetDataActivity_et_meterAddr.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("MeterGetData", MODE_PRIVATE);
        GetDataActivity_et_netId.setText(pref.getString("netId", ""));
        GetDataActivity_et_jd.setText(pref.getString("jd", ""));
        GetDataActivity_et_freq.setText(pref.getString("freq", ""));
        GetDataActivity_et_meterAddr.setText(pref.getString("meterAddr", ""));
    }

    @Override
    protected void onDestroy() {
        saveUser();
        super.onDestroy();
    }
}
