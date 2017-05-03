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
 * Created by limbo on 2017/4/10.
 */
public class LoRa_DeleteSingleData extends Activity {
    private EditText etMkxh;
    private Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_deletesingledata);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        etMkxh = (EditText) findViewById(R.id.LoRa_DeleteSingleData_et_mkxh);
        btnEnter = (Button) findViewById(R.id.LoRa_DeleteSingleData_btn_enter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表

                    if (HzyUtils.isEmpty(etMkxh.getText().toString())){
                        HintDialog.ShowHintDialog(LoRa_DeleteSingleData.this,"输入信息不可为空","提示");
                        return;
                    }
                    HzyUtils.showProgressDialog1(LoRa_DeleteSingleData.this, "正在删除...");
                    String mkxh = etMkxh.getText().toString().trim();
                    while (mkxh.length() < 8) {
                        mkxh = "0" + mkxh;
                    }
                    String sendMsg = "001600d4000204"+mkxh;
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
                case 0x00://安美通LoRa表
                    String getMsg = msg.obj.toString();
                    if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                    } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表
                        if (getMsg.equals("001600852043")) {
                            HintDialog.ShowHintDialog(LoRa_DeleteSingleData.this, "删除成功", "成功");
                        } else if (getMsg.equals("00160008e026")) {
                            HintDialog.ShowHintDialog(LoRa_DeleteSingleData.this, "表信息不存在", "成功");
                        }
                        HzyUtils.closeProgressDialog();
                    } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                    } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                    }

                    break;
                case 0x01://山科LoRa表
                    getMsg = msg.obj.toString();
                    if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                    } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表

                    } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                    } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                    }
                    HzyUtils.closeProgressDialog();
                    break;

                case 0x99:
                    HintDialog.ShowHintDialog(LoRa_DeleteSingleData.this,"未收到返回信息","提示");
                    HzyUtils.closeProgressDialog();
                    break;
            }
        }
    };
}
