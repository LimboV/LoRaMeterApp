package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

/**
 * Created by limbo on 2017/4/11.
 */
public class LoRa_ReplaceSingleData extends Activity {
    private EditText etJmkxh, etXmkxh, etJjbid, etXjbid;
    private Button btnEnter, btnEnter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_replacesingledata);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        etJmkxh = (EditText) findViewById(R.id.LoRa_ReplaceSingleData_et_jmkxh);
        etXmkxh = (EditText) findViewById(R.id.LoRa_ReplaceSingleData_et_xmkxh);
        etJjbid = (EditText) findViewById(R.id.LoRa_ReplaceSingleData_et_jjbid);
        etXjbid = (EditText) findViewById(R.id.LoRa_ReplaceSingleData_et_xjbid);
        btnEnter = (Button) findViewById(R.id.LoRa_ReplaceSingleData_btn_enter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表

                    if (HzyUtils.isEmpty(etJmkxh.getText().toString())||HzyUtils.isEmpty(etXmkxh.getText().toString())){
                        HintDialog.ShowHintDialog(LoRa_ReplaceSingleData.this,"输入信息不可为空","提示");
                        return;
                    }

                    HzyUtils.showProgressDialog1(LoRa_ReplaceSingleData.this, "正在删除...");
                    String jmkxh = etJmkxh.getText().toString().trim();
                    String xmkxh = etXmkxh.getText().toString().trim();
                    while (jmkxh.length() < 8) {
                        jmkxh = "0" + jmkxh;
                    }
                    while (xmkxh.length() < 8) {
                        xmkxh = "0" + xmkxh;
                    }
                    String sendMsg = "001600d4000308" + jmkxh + xmkxh;
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    Log.d("limbo", sendMsg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (MenuActivity.Cjj_CB_MSG.length() == 0) {
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

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    }).start();

                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        btnEnter1 = (Button) findViewById(R.id.LoRa_ReplaceSingleData_btn_enter1);
        btnEnter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                    if (HzyUtils.isEmpty(etJjbid.getText().toString())||HzyUtils.isEmpty(etXjbid.getText().toString())){
                        HintDialog.ShowHintDialog(LoRa_ReplaceSingleData.this,"输入信息不可为空","提示");
                        return;
                    }
                    HzyUtils.showProgressDialog1(LoRa_ReplaceSingleData.this, "正在替换...");
                    String jjbid = etJjbid.getText().toString().trim();
                    String xjbid = etXjbid.getText().toString().trim();
                    while (jjbid.length() < 14) {
                        jjbid = "0" + jjbid;
                    }
                    while (xjbid.length() < 14) {
                        xjbid = "0" + xjbid;
                    }
                    String sendMsg = "001600d4000a0e" + jjbid + xjbid;
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    Log.d("limbo", sendMsg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (MenuActivity.Cjj_CB_MSG.length() == 0) {
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

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    }).start();

                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString();
                    if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                    } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                        if (getMsg.contains("00160101") && getMsg.length() >= 40) {
                            HintDialog.ShowHintDialog(LoRa_ReplaceSingleData.this, "模块序号:" + getMsg.substring(8, 16)
                                    + "\n基表ID:" + getMsg.substring(16, 30)
                                    + "\n当前读数" + getMsg.substring(30, 34) + "." + getMsg.substring(34, 36), "替换成功");

                        } else if (getMsg.equals("00160008e026")) {
                            HintDialog.ShowHintDialog(LoRa_ReplaceSingleData.this, "表信息不存在", "替换失败");
                        }
                        HzyUtils.closeProgressDialog();
                    } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                    } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                    }

                    break;
                case 0x01:
                    getMsg = msg.obj.toString();
                    if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                    } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                        if (getMsg.contains("00160101") && getMsg.length() >= 40) {
                            HintDialog.ShowHintDialog(LoRa_ReplaceSingleData.this, "模块序号:" + getMsg.substring(8, 16)
                                    + "\n基表ID:" + getMsg.substring(16, 30)
                                    + "\n当前读数" + getMsg.substring(30, 34) + "." + getMsg.substring(34, 36), "替换成功");

                        } else if (getMsg.equals("00160008e026")) {
                            HintDialog.ShowHintDialog(LoRa_ReplaceSingleData.this, "表信息不存在", "替换失败");
                        }
                        HzyUtils.closeProgressDialog();
                    } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                    } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                    }
                    HzyUtils.closeProgressDialog();
                    break;

                case 0x99:
                    HintDialog.ShowHintDialog(LoRa_ReplaceSingleData.this, "未收到返回信息", "提示");
                    HzyUtils.closeProgressDialog();
                    break;
            }
        }
    };
}
