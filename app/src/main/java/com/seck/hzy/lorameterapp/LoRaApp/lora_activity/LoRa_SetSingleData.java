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
public class LoRa_SetSingleData extends Activity {
    EditText etJbid,etDqds;
    Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_setsingledata);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        etJbid = (EditText) findViewById(R.id.LoRa_SetSingleData_et_jbid);
        etDqds = (EditText) findViewById(R.id.LoRa_SetSingleData_et_dqds);
        btnEnter = (Button) findViewById(R.id.LoRa_SetSingleData_btn_enter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表

                    if (HzyUtils.isEmpty(etJbid.getText().toString())||HzyUtils.isEmpty(etDqds.getText().toString())){
                        HintDialog.ShowHintDialog(LoRa_SetSingleData.this,"输入信息不可为空","提示");
                        return;
                    }
                    HzyUtils.showProgressDialog1(LoRa_SetSingleData.this, "正在设置...");
                    String jbid = etJbid.getText().toString().trim();
                    float dqds1 = Float.parseFloat(etDqds.getText().toString().trim());
                    String dqds = (int)(dqds1*100)/1+"";
                    while (jbid.length() < 14) {
                        jbid = "0" + jbid;
                    }
                    while (dqds.length() < 6) {
                        dqds = "0" + dqds;
                    }
                    String sendMsg = "001600d400070a" + jbid +dqds;
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

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString();
                    if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                    } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                        if (getMsg.contains("00160101") && getMsg.length() >= 40) {
                            HintDialog.ShowHintDialog(LoRa_SetSingleData.this, "模块序号:" + getMsg.substring(8, 16)
                                    + "\n基表ID:" + getMsg.substring(16, 30)
                                    + "\n当前读数" + getMsg.substring(30, 34) + "." + getMsg.substring(34, 36), "替换成功");

                        } else if (getMsg.equals("00160008e026")) {
                            HintDialog.ShowHintDialog(LoRa_SetSingleData.this, "表信息不存在", "成功");
                        }
                        HzyUtils.closeProgressDialog();
                    } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                    } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                    }

                    break;
                case 0x01:
                    getMsg = msg.obj.toString();
                    if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                    } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表

                    } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                    } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                    }
                    HzyUtils.closeProgressDialog();
                    break;

                case 0x99:
                    HintDialog.ShowHintDialog(LoRa_SetSingleData.this,"未收到返回信息","提示");
                    HzyUtils.closeProgressDialog();
                    break;
            }
        }
    };
}
