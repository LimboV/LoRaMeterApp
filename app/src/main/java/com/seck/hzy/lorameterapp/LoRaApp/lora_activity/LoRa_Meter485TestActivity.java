package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;

/**
 * Created by limbo on 2016/10/12.
 */
public class LoRa_Meter485TestActivity extends Activity {

    private EditText etNetId,etMkId,etFreq, etfc0, etfc1, etfc2, etfc3, etfc4;
    private Button btnGetData;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_yc485test);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        tv = (TextView) findViewById(R.id.Meter485TestActivity_tv_showMsg);
        etNetId = (EditText) findViewById(R.id.Meter485TestActivity_et_netid);
        etMkId = (EditText) findViewById(R.id.Meter485TestActivity_et_mkid);
        etFreq = (EditText) findViewById(R.id.Meter485TestActivity_et_freq);
        etfc0 = (EditText) findViewById(R.id.Meter485TestActivity_et_fc0);
        etfc1 = (EditText) findViewById(R.id.Meter485TestActivity_et_fc1);
        etfc2 = (EditText) findViewById(R.id.Meter485TestActivity_et_fc2);
        etfc3 = (EditText) findViewById(R.id.Meter485TestActivity_et_fc3);
        etfc4 = (EditText) findViewById(R.id.Meter485TestActivity_et_fc4);
        loadUser();
        btnGetData = (Button) findViewById(R.id.Meter485TestActivity_btn_getData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xxx = 0;
                tv.setText("接收数据:\n");
                String freq = etFreq.getText().toString().trim();
                String netId = etNetId.getText().toString().trim();
                String mkId = etMkId.getText().toString().trim();

                String fc0 = etfc0.getText().toString().trim();
                String fc1 = etfc1.getText().toString().trim();
                String fc2 = etfc2.getText().toString().trim();
                String fc3 = etfc3.getText().toString().trim();
                String fc4 = etfc4.getText().toString().trim();

                if (fc0.length()>0){
                    fc0 = Integer.toHexString(Integer.parseInt(fc0));
                    xxx= xxx+1;
                }
                if (fc1.length()>0){
                    fc1 = Integer.toHexString(Integer.parseInt(fc1));
                    xxx= xxx+1;
                }
                if (fc2.length()>0){
                    fc2 = Integer.toHexString(Integer.parseInt(fc2));
                    xxx= xxx+1;
                }
                if (fc3.length()>0){
                    fc3 = Integer.toHexString(Integer.parseInt(fc3));
                    xxx= xxx+1;
                }
                if (fc4.length()>0){
                    fc4 = Integer.toHexString(Integer.parseInt(fc4));
                    xxx= xxx+1;
                }
                if (freq.length() == 0 || netId.length() == 0||mkId.length() == 0){
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据不可为空", "错误");
                }
                freq = Integer.toHexString(Integer.valueOf(freq+"0"));
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

                if (fc0.length() <= 2) {
                    while (fc0.length() < 2) {
                        fc0 = "0" + fc0;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc1.length() <= 2) {
                    while (fc1.length() < 2) {
                        fc1 = "0" + fc1;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc2.length() <= 2) {
                    while (fc2.length() < 2) {
                        fc2 = "0" + fc2;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc3.length() <= 2) {
                    while (fc3.length() < 2) {
                        fc3 = "0" + fc3;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc4.length() <= 2) {
                    while (fc4.length() < 2) {
                        fc4 = "0" + fc4;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    HzyUtils.showProgressDialog(LoRa_Meter485TestActivity.this);
                    String sendMsg = "68" + freq + netId + mkId + "402aa101400" + xxx + fc0 + fc1 + fc2 + fc3 + fc4 + "000000000000000000000000000000000000000000005B5B5B3F3F7B7B7B01FE3F5D5D5D";
                    //                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    Log.d("limbo", "获取:" + sendMsg);
                    MenuActivity.sendCmd(sendMsg);

                    MenuActivity.btAuto = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (MenuActivity.btAuto) {
                                    Thread.sleep(2000);
                                    String getMsg = MenuActivity.Cjj_CB_MSG;
                                    MenuActivity.Cjj_CB_MSG = "";
                                    getMsg = getMsg.replaceAll("0x", "").replaceAll(" ","");
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
                } else if (MenuActivity.METER_STYLE.equals("W")) {//Wmrnet表

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
                    if (getMsg.trim().length()!=0){
                        getMsg = HzyUtils.toStringHex(getMsg).replaceAll("�","");
                        tv.append(getMsg+"\n");
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
        editor.putString("etNetId", etNetId.getText().toString().trim());
        editor.putString("etMkId", etMkId.getText().toString().trim());
        editor.putString("etFreq", etFreq.getText().toString().trim());
        editor.putString("etfc0", etfc0.getText().toString().trim());
        editor.putString("etfc1", etfc1.getText().toString().trim());
        editor.putString("etfc2", etfc2.getText().toString().trim());
        editor.putString("etfc3", etfc3.getText().toString().trim());
        editor.putString("etfc4", etfc4.getText().toString().trim());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("485Test", MODE_PRIVATE);
        etNetId.setText(pref.getString("etNetId", ""));
        etMkId.setText(pref.getString("etMkId", ""));
        etFreq.setText(pref.getString("etFreq", ""));
        etfc0.setText(pref.getString("etfc0", ""));
        etfc1.setText(pref.getString("etfc1", ""));
        etfc2.setText(pref.getString("etfc2", ""));
        etfc3.setText(pref.getString("etfc3", ""));
        etfc4.setText(pref.getString("etfc4", ""));

    }

    @Override
    protected void onDestroy() {
        MenuActivity.btAuto = false;
        saveUser();
        super.onDestroy();
    }
}
