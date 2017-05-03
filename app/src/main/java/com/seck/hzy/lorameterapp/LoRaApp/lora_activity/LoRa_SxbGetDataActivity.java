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
import android.widget.TextView;

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;

/**
 * Created by limbo on 2016/11/22.
 */
public class LoRa_SxbGetDataActivity extends Activity {

    private TextView tvShowMsg;
    private EditText etMeterAddr, etFreq;
    private Button btnGetData;
    private String sendMsg;
    private boolean timeOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lora_activity_sxbgetdata);
        tvShowMsg = (TextView) findViewById(R.id.SxbGetDataActivity_tv_showMsg);
        etMeterAddr = (EditText) findViewById(R.id.SxbGetDataActivity_et_meterAddr);
        etFreq = (EditText) findViewById(R.id.SxbGetDataActivity_et_freq);
        etFreq.setVisibility(View.GONE);
        btnGetData = (Button) findViewById(R.id.SxbGetDataActivity_btn_getData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    HzyUtils.showProgressDialog(LoRa_SxbGetDataActivity.this);
                    tvShowMsg.setText("");
                    String addr = etMeterAddr.getText().toString().trim();
                    if (addr.length() > 14) {
                        HintDialog.ShowHintDialog(LoRa_SxbGetDataActivity.this, "表地址过长", "提示");
                    }
                    if (addr.length() == 0) {
                        addr = "aaaaaaaaaaaaaa";
                    }
                    while (addr.length() < 14) {
                        addr = "0" + addr;
                    }

                    sendMsg = "6810" +
                            HzyUtils.changeString(addr) + "2103a0f140000000";
                    Log.d("limbo", sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    tvShowMsg.setText("发送读取参数指令:\n水表地址:" + addr);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                prepareTimeStart();
                                while (!timeOut) {
//                                    Log.d("limbo", timeOut + "");
                                    Thread.sleep(300);
                                    if (MenuActivity.Cjj_CB_MSG.contains("04")) {
                                        timeOut = true;
                                    }
                                }
                                String getMsg = MenuActivity.Cjj_CB_MSG;
                                if (!MenuActivity.Cjj_CB_MSG.contains("04")) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                } else {
                                    //getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "").replace(sendMsg,"");
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

                }
            }
        });

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00://安美通LoRa表
                    String getMsg = msg.obj.toString();
                    break;
                case 0x01://山科LoRa表
                    getMsg = msg.obj.toString();
                    HzyUtils.addPlace(getMsg);
                    String show = HzyUtils.toStringHex1(getMsg).replaceAll("�", "");
                    tvShowMsg.append("\n" + show);

                    break;

                case 0x99:
                    HzyUtils.closeProgressDialog();
                    tvShowMsg.append("\n" + "数据接收失败,请重试.");
//                    HintDialog.ShowHintDialog(SxbGetDataActivity.this, "未接收到数据", "提示");
                    break;
            }
        }
    };

    /**
     *
     * 开始协议设定时间
     *
     */
    private void prepareTimeStart() {
        timeOut = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10000);
                    timeOut = true;
                }catch (Exception e){

                }
            }
        }).start();
    }

}
