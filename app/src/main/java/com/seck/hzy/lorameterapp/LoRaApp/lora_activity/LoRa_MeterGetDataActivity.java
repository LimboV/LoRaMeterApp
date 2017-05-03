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

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;

/**
 * Created by ssHss on 2016/7/29.
 */
public class LoRa_MeterGetDataActivity extends Activity {

    private Button btnGetData,btnZt,btnBkf,btnGf,btnkf,btnFx;
    private EditText etMeterAddr, etFreq ,etNetId,etJd;
    private TextView tvShowMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_get_data);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框

        etMeterAddr = (EditText) findViewById(R.id.GetDataActivity_et_meterAddr);
        etFreq = (EditText) findViewById(R.id.GetDataActivity_et_freq);
        etNetId = (EditText) findViewById(R.id.GetDataActivity_et_netId);
        etJd = (EditText) findViewById(R.id.GetDataActivity_et_jd);
        tvShowMsg = (TextView) findViewById(R.id.GetDataActivity_tv_showMsg);
        loadUser();
        btnGetData = (Button) findViewById(R.id.GetDataActivity_btn_getData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MenuActivity.METER_STYLE.equals("L")) {//山科LoRa表
                    HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                    tvShowMsg.setText("");
                    String addr = etMeterAddr.getText().toString().trim();
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
                    tvShowMsg.setText("发送读取参数指令:\n水表地址:" + addr);
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
                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                    tvShowMsg.setText("");
                    String addr = etMeterAddr.getText().toString().trim();
                    if (addr.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                    }
                    String freq = etFreq.getText().toString().trim();
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
                    tvShowMsg.setText("发送读取参数指令:\n频率" + freq + "\n水表地址:" + addr);
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
                    tvShowMsg.setText("");
                    String addr = etMeterAddr.getText().toString().trim();
                    String jd = etJd.getText().toString().trim();
                    if (addr.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                    }
                    String freq = etFreq.getText().toString().trim();
                    freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                    String netId = etNetId.getText().toString().trim();
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
                    tvShowMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
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
                }

            }
        });

        /**
         * 阀控指令
         */
        btnZt = (Button) findViewById(R.id.GetDataActivity_btn_zt);
        btnZt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                tvShowMsg.setText("");
                String addr = etMeterAddr.getText().toString().trim();
                String jd = etJd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = etFreq.getText().toString().trim();
                if (freq.isEmpty()){
                    freq = "0";
                }
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = etNetId.getText().toString().trim();
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
                        "c0" ;
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                tvShowMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
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
        btnBkf = (Button) findViewById(R.id.GetDataActivity_btn_bkf);
        btnBkf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                tvShowMsg.setText("");
                String addr = etMeterAddr.getText().toString().trim();
                String jd = etJd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = etFreq.getText().toString().trim();
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = etNetId.getText().toString().trim();
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
                        "c1" ;
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                tvShowMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
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
        btnGf = (Button) findViewById(R.id.GetDataActivity_btn_gf);
        btnGf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                tvShowMsg.setText("");
                String addr = etMeterAddr.getText().toString().trim();
                String jd = etJd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = etFreq.getText().toString().trim();
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = etNetId.getText().toString().trim();
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
                        "c2" ;
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                tvShowMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
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
        btnkf = (Button) findViewById(R.id.GetDataActivity_btn_kf);
        btnkf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                tvShowMsg.setText("");
                String addr = etMeterAddr.getText().toString().trim();
                String jd = etJd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = etFreq.getText().toString().trim();
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = etNetId.getText().toString().trim();
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
                        "c3" ;
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                tvShowMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
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
        btnFx = (Button) findViewById(R.id.GetDataActivity_btn_fx);
        btnFx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_MeterGetDataActivity.this);
                tvShowMsg.setText("");
                String addr = etMeterAddr.getText().toString().trim();
                String jd = etJd.getText().toString().trim();
                if (addr.length() > 8) {
                    HintDialog.ShowHintDialog(LoRa_MeterGetDataActivity.this, "表地址过长", "提示");
                }
                String freq = etFreq.getText().toString().trim();
                freq = Integer.toHexString(Integer.parseInt(freq));//频率转化成16进制
                String netId = etNetId.getText().toString().trim();
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
                        "c4" ;
                Log.d("limbo", sendMsg);
                MenuActivity.sendCmd(sendMsg);
                tvShowMsg.setText("发送读取参数指令:\n频率" + freq + "\n网络ID:" + netId + "\n节点:" + jd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
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
                    String waterValue = getMsg.substring(10, 18);
                    waterValue = waterValue.substring(1, 2) +
                            waterValue.substring(3, 4) +
                            waterValue.substring(5, 6) + "." +
                            waterValue.substring(7, 8);
                    String dy = getMsg.substring(20, 22);
                    dy = (Integer.parseInt(dy, 16) / 100 + 2) + "";
                    tvShowMsg.append("\n\n接收参数:" +
                            "\n水表值:" + waterValue +
                            "\n阀控:" + getMsg.substring(18, 20) +
                            "\n电压:" + dy);

                    break;

                case 0x01://LoRa表
                    getMsg = msg.obj.toString();
                    waterValue = getMsg.substring(10, 18);
                    waterValue = waterValue.substring(34, 36) +
                            waterValue.substring(32, 34) +
                            waterValue.substring(30, 32) ;
                    dy = getMsg.substring(getMsg.length()-6, getMsg.length()-4);
                    dy = ((float)Integer.parseInt(dy, 16) / 100 + 2) + "";
                    tvShowMsg.append("\n\n接收参数:" +
                            "\n水表值:" + waterValue +
                            "\n电压:" + dy);
                    break;
                case 0x02://安美通fsk表
                    getMsg = msg.obj.toString();
                    netId =getMsg.substring(0, 4);
                    waterValue = getMsg.substring(16, 26);
                    waterValue = waterValue.substring(1, 2) +
                            waterValue.substring(3, 4) +
                            waterValue.substring(5, 6) + "." +
                            waterValue.substring(7, 8) +
                            waterValue.substring(9, 10);
                    dy = getMsg.substring(26, 28);
                    dy = ((float)Integer.parseInt(dy, 16) / 100 + 2) + "";
                    tvShowMsg.append("\n\n接收参数:" +
                            "\n网络ID:" + netId +
                            "\n水表值:" + waterValue +
                            "\n阀控:" + getMsg.substring(18, 20) +
                            "\n电压:" + dy);
                    break;
                case 0x98:
                    HzyUtils.closeProgressDialog();
                    getMsg = msg.obj.toString();
                    tvShowMsg.setText(getMsg);
                    Toast.makeText(LoRa_MeterGetDataActivity.this,"命令发送完毕",Toast.LENGTH_LONG).show();
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
        editor.putString("netId", etNetId.getText().toString());
        editor.putString("jd", etJd.getText().toString());
        editor.putString("freq", etFreq.getText().toString());
        editor.putString("meterAddr", etMeterAddr.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("MeterGetData", MODE_PRIVATE);
        etNetId.setText(pref.getString("netId", ""));
        etJd.setText(pref.getString("jd", ""));
        etFreq.setText(pref.getString("freq", ""));
        etMeterAddr.setText(pref.getString("meterAddr", ""));
    }

    @Override
    protected void onDestroy() {
        saveUser();
        super.onDestroy();
    }
}
