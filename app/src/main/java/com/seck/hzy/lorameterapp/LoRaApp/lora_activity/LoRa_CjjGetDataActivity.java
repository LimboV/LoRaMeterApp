package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
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
 * Created by ssHss on 2016/8/23.
 */
public class LoRa_CjjGetDataActivity extends Activity {

    private EditText etJd, etFc0, etFc1, etFc2, etFc3, etFc4, etFc5, etFc6, etFc7, etFc8, etFc9, etFc10
            , etFc11, etFc12, etFc13, etFc14, etFc15, etFc16, etFc17, etFc18, etFc19;
    private Button btnGetData;
    private TextView tv;
    private CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_cjjgetdata);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        tv = (TextView) findViewById(R.id.CjjGetDataActivity_tv_showMsg);
        etJd = (EditText) findViewById(R.id.CjjGetDataActivity_et_jd);
        etFc0 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc0);
        etFc1 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc1);
        etFc2 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc2);
        etFc3 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc3);
        etFc4 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc4);
        etFc5 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc5);
        etFc6 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc6);
        etFc7 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc7);
        etFc8 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc8);
        etFc9 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc9);
        etFc10 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc10);
        etFc11 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc11);
        etFc12 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc12);
        etFc13 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc13);
        etFc14 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc14);
        etFc15 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc15);
        etFc16 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc16);
        etFc17 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc17);
        etFc18 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc18);
        etFc19 = (EditText) findViewById(R.id.CjjGetDataActivity_et_fc19);
        etFc0.setText("0");
        cb = (CheckBox) findViewById(R.id.checkBox2);
        btnGetData = (Button) findViewById(R.id.CjjGetDataActivity_btn_getData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("接收数据:\n");
                String jd = etJd.getText().toString().trim();
                String fc0 = etFc0.getText().toString().trim();
                String fc1 = etFc1.getText().toString().trim();
                String fc2 = etFc2.getText().toString().trim();
                String fc3 = etFc3.getText().toString().trim();
                String fc4 = etFc4.getText().toString().trim();
                String fc5 = etFc5.getText().toString().trim();
                String fc6 = etFc6.getText().toString().trim();
                String fc7 = etFc7.getText().toString().trim();
                String fc8 = etFc8.getText().toString().trim();
                String fc9 = etFc9.getText().toString().trim();
                String fc10 = etFc10.getText().toString().trim();
                String fc11 = etFc11.getText().toString().trim();
                String fc12 = etFc12.getText().toString().trim();
                String fc13 = etFc13.getText().toString().trim();
                String fc14 = etFc14.getText().toString().trim();
                String fc15 = etFc15.getText().toString().trim();
                String fc16 = etFc16.getText().toString().trim();
                String fc17 = etFc17.getText().toString().trim();
                String fc18 = etFc18.getText().toString().trim();
                String fc19 = etFc19.getText().toString().trim();
                fc0 = HzyUtils.toHexString(fc0);
                fc1 = HzyUtils.toHexString(fc1);
                fc2 = HzyUtils.toHexString(fc2);
                fc3 = HzyUtils.toHexString(fc3);
                fc4 = HzyUtils.toHexString(fc4);
                fc5 = HzyUtils.toHexString(fc5);
                fc6 = HzyUtils.toHexString(fc6);
                fc7 = HzyUtils.toHexString(fc7);
                fc8 = HzyUtils.toHexString(fc8);
                fc9 = HzyUtils.toHexString(fc9);
                fc10 = HzyUtils.toHexString(fc10);
                fc11 = HzyUtils.toHexString(fc11);
                fc12 = HzyUtils.toHexString(fc12);
                fc13 = HzyUtils.toHexString(fc13);
                fc14 = HzyUtils.toHexString(fc14);
                fc15 = HzyUtils.toHexString(fc15);
                fc16 = HzyUtils.toHexString(fc16);
                fc17 = HzyUtils.toHexString(fc17);
                fc18 = HzyUtils.toHexString(fc18);
                fc19 = HzyUtils.toHexString(fc19);
                if (jd.length() <= 10) {
                    while (jd.length() < 10) {
                        jd = "0" + jd;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");

                }
                if (fc0.length() <= 2) {
                    while (fc0.length() < 2) {
                        fc0 = "0" + fc0;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                    Log.d("limbo", "数据过大：" + fc0);
                }
                if (fc1.length() <= 2) {
                    while (fc1.length() < 2) {
                        fc1 = "0" + fc1;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc2.length() <= 2) {
                    while (fc2.length() < 2) {
                        fc2 = "0" + fc2;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc3.length() <= 2) {
                    while (fc3.length() < 2) {
                        fc3 = "0" + fc3;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc4.length() <= 2) {
                    while (fc4.length() < 2) {
                        fc4 = "0" + fc4;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc5.length() <= 2) {
                    while (fc5.length() < 2) {
                        fc5 = "0" + fc5;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc6.length() <= 2) {
                    while (fc6.length() < 2) {
                        fc6 = "0" + fc6;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc7.length() <= 2) {
                    while (fc7.length() < 2) {
                        fc7 = "0" + fc7;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc8.length() <= 2) {
                    while (fc8.length() < 2) {
                        fc8 = "0" + fc8;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc9.length() <= 2) {
                    while (fc9.length() < 2) {
                        fc9 = "0" + fc9;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc10.length() <= 2) {
                    while (fc10.length() < 2) {
                        fc10 = "0" + fc10;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc11.length() <= 2) {
                    while (fc11.length() < 2) {
                        fc11 = "0" + fc11;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc12.length() <= 2) {
                    while (fc12.length() < 2) {
                        fc12 = "0" + fc12;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc13.length() <= 2) {
                    while (fc13.length() < 2) {
                        fc13 = "0" + fc13;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc14.length() <= 2) {
                    while (fc14.length() < 2) {
                        fc14 = "0" + fc14;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc15.length() <= 2) {
                    while (fc15.length() < 2) {
                        fc15 = "0" + fc15;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc16.length() <= 2) {
                    while (fc16.length() < 2) {
                        fc16 = "0" + fc16;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc17.length() <= 2) {
                    while (fc17.length() < 2) {
                        fc17 = "0" + fc17;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc18.length() <= 2) {
                    while (fc18.length() < 2) {
                        fc18 = "0" + fc18;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                if (fc19.length() <= 2) {
                    while (fc19.length() < 2) {
                        fc19 = "0" + fc19;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "数据过大", "错误");
                }
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                    HzyUtils.showProgressDialog(LoRa_CjjGetDataActivity.this);


                    String sendMsg = "001600fe888820" + jd + fc0 + fc1 + fc2 + fc3 + fc4
                            + fc5 + fc6 + fc7 + fc8 + fc9
                            + fc10 + fc11 + fc12 + fc13 + fc14
                            + fc15 + fc16 + fc17 + fc18 + fc19
                            +"00000000000000"
                            ;
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int i = 0;i<60;i++){
                                    Thread.sleep(2000);
                                    String getMsg = MenuActivity.Cjj_CB_MSG;
                                    MenuActivity.Cjj_CB_MSG = "";
//                                    if (getMsg.length() == 0) {
//                                        Message message = new Message();
//                                        message.what = 0x99;
//                                        message.obj = getMsg;
//                                        mHandler.sendMessage(message);
//                                    } else {
                                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                        Log.d("limbo", getMsg);

                                        Message message = new Message();
                                        message.what = 0x00;
                                        message.obj = getMsg;
                                        mHandler.sendMessage(message);
//                                    }
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
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
//                    if (getMsg.trim().length()!=0) {
//                        tv.append(getMsg+"\n");
//                    }

//                    if (getMsg.trim().length()!=0){
//                        getMsg = HzyUtils.toStringHex(getMsg).replaceAll("�","");
//                        tv.append(getMsg+"\n");
//                    }
                    if (getMsg.trim().length()!=0){

                        if (cb.isChecked()){
                            tv.append(getMsg+"\n");
                        }else {
                            getMsg = HzyUtils.toStringHex1(getMsg).replaceAll("�","");
                            tv.append(getMsg+"\n");
                        }
                    }
                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_CjjGetDataActivity.this, "未接收到数据", "提示");
                    break;
                default:
                    break;
            }
        }
    };
}
