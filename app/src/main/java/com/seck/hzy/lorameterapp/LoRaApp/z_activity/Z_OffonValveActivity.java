package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity.sendCmd;
import static com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils.countSum;

/**
 * Created by limbo on 2018/1/3.
 */

public class Z_OffonValveActivity extends Activity {
    private Button btn_on, btn_off, btn_state;
    private EditText et_addr;
    private TextView tv_1;
    boolean flag = true;
    boolean timeOut = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_offon_valve);
        btn_on = (Button) findViewById(R.id.btn_1);
        btn_off = (Button) findViewById(R.id.btn_2);
        btn_state = (Button) findViewById(R.id.btn_3);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        et_addr = (EditText) findViewById(R.id.et_addr);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String getMsg = "";
                while (flag) {
                    try {
                        Thread.sleep(500);
                        getMsg = getMsg + MenuActivity.Cjj_CB_MSG;
                        MenuActivity.Cjj_CB_MSG = "";
                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                        if (getMsg.length() > 0) {
                            Message message = new Message();
                            message.what = 0x00;
                            message.obj = getMsg;
                            getMsg = "";
                            mHandler.sendMessage(message);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        }).start();
        btn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTimeStart(150);
                String addr = et_addr.getText().toString().trim();
                if (addr.length() < 14) {
                    addr = "0" + addr;
                }
                if (addr.length() > 14) {
                    HintDialog.ShowHintDialog(Z_OffonValveActivity.this, "数据过长", "提示");
                    return;
                }
                String sendMsg = "6810" + addr + "040417a00055";
                sendMsg = sendMsg + countSum(sendMsg) + "16";
                sendCmd(sendMsg);
            }
        });
        btn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTimeStart(150);
                String addr = et_addr.getText().toString().trim();
                if (addr.length() < 14) {
                    addr = "0" + addr;
                }
                if (addr.length() > 14) {
                    HintDialog.ShowHintDialog(Z_OffonValveActivity.this, "数据过长", "提示");
                    return;
                }
                String sendMsg = "6810" + addr + "040417a00099";
                sendMsg = sendMsg + countSum(sendMsg) + "16";
                sendCmd(sendMsg);
            }
        });
        btn_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTimeStart(150);
                String addr = et_addr.getText().toString().trim();
                if (addr.length() < 14) {
                    addr = "0" + addr;
                }
                if (addr.length() > 14) {
                    HintDialog.ShowHintDialog(Z_OffonValveActivity.this, "数据过长", "提示");
                    return;
                }
                String sendMsg = "6810" + addr + "2303810000c516";
                sendMsg = sendMsg + countSum(sendMsg) + "16";
                sendCmd(sendMsg);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    if (getMsg.contains("03810a00")){
                        timeOut = true;
                        HintDialog.ShowHintDialog(Z_OffonValveActivity.this,"当前状态：开","数据");
                        tv_1.setText("当前状态：开");
                    }else if (getMsg.contains("03810a01")){
                        timeOut = true;
                        HintDialog.ShowHintDialog(Z_OffonValveActivity.this,"当前状态：关","数据");
                        tv_1.setText("当前状态：关");
                    }
                    break;
                case 0x99:
                    HintDialog.ShowHintDialog(Z_OffonValveActivity.this,"未读到返回数据","提示");
                    tv_1.setText("未读到返回数据");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 开始协议设定时间
     * timeMax 1 = 0.1s
     */
    private void prepareTimeStart(final int timeMax) {
        timeOut = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < timeMax; i++) {
                        Thread.sleep(100);
                        if (timeOut) {
                            break;
                        }
                    }
                    if (!timeOut) {
                        Message message = new Message();
                        message.what = 0x99;
                        mHandler.sendMessage(message);
                        timeOut = true;
                    }

                } catch (Exception e) {
                    Log.d("limbo", e.toString());
                }
            }
        }).start();
    }
    @Override
    protected void onDestroy() {
        flag = false;
        super.onDestroy();
    }
}
