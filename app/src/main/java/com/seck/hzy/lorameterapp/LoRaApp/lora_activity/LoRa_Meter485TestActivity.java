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

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2016/10/12.
 */
public class LoRa_Meter485TestActivity extends Activity {

    @BindView(R.id.Meter485TestActivity_tv_showMsg)
    TextView Meter485TestActivity_tv_showMsg;

    @BindView(R.id.Meter485TestActivity_et_netid)
    EditText Meter485TestActivity_et_netid;

    @BindView(R.id.Meter485TestActivity_et_mkid)
    EditText Meter485TestActivity_et_mkid;

    @BindView(R.id.Meter485TestActivity_et_freq)
    EditText Meter485TestActivity_et_freq;

    @BindView(R.id.Meter485TestActivity_et_fc0)
    EditText Meter485TestActivity_et_fc0;

    @BindView(R.id.Meter485TestActivity_et_fc1)
    EditText Meter485TestActivity_et_fc1;

    @BindView(R.id.Meter485TestActivity_et_fc2)
    EditText Meter485TestActivity_et_fc2;

    @BindView(R.id.Meter485TestActivity_et_fc3)
    EditText Meter485TestActivity_et_fc3;

    @BindView(R.id.Meter485TestActivity_et_fc4)
    EditText Meter485TestActivity_et_fc4;

    @BindView(R.id.Meter485TestActivity_et_fc5)
    EditText Meter485TestActivity_et_fc5;

    @BindView(R.id.Meter485TestActivity_et_fc6)
    EditText Meter485TestActivity_et_fc6;

    @BindView(R.id.Meter485TestActivity_et_fc7)
    EditText Meter485TestActivity_et_fc7;

    @BindView(R.id.Meter485TestActivity_et_fc8)
    EditText Meter485TestActivity_et_fc8;

    @BindView(R.id.Meter485TestActivity_et_fc9)
    EditText Meter485TestActivity_et_fc9;

    @BindView(R.id.Meter485TestActivity_et_fc10)
    EditText Meter485TestActivity_et_fc10;

    @BindView(R.id.Meter485TestActivity_et_fc11)
    EditText Meter485TestActivity_et_fc11;

    @BindView(R.id.Meter485TestActivity_et_fc12)
    EditText Meter485TestActivity_et_fc12;

    @BindView(R.id.Meter485TestActivity_et_fc13)
    EditText Meter485TestActivity_et_fc13;

    @BindView(R.id.Meter485TestActivity_et_fc14)
    EditText Meter485TestActivity_et_fc14;

    @BindView(R.id.Meter485TestActivity_et_fc15)
    EditText Meter485TestActivity_et_fc15;

    @BindView(R.id.Meter485TestActivity_et_fc16)
    EditText Meter485TestActivity_et_fc16;

    @BindView(R.id.Meter485TestActivity_et_fc17)
    EditText Meter485TestActivity_et_fc17;

    @BindView(R.id.Meter485TestActivity_et_fc18)
    EditText Meter485TestActivity_et_fc18;

    @BindView(R.id.Meter485TestActivity_et_fc19)
    EditText Meter485TestActivity_et_fc19;

    @BindView(R.id.Meter485TestActivity_btn_getData)
    Button Meter485TestActivity_btn_getData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_yc485test);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        loadUser();
        Meter485TestActivity_btn_getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xxx = 0;
                Meter485TestActivity_tv_showMsg.setText("接收数据:\n");
                String freq = Meter485TestActivity_et_freq.getText().toString().trim();
                String netId = Meter485TestActivity_et_netid.getText().toString().trim();
                String mkId = Meter485TestActivity_et_mkid.getText().toString().trim();

                String fc0 = Meter485TestActivity_et_fc0.getText().toString().trim();
                String fc1 = Meter485TestActivity_et_fc1.getText().toString().trim();
                String fc2 = Meter485TestActivity_et_fc2.getText().toString().trim();
                String fc3 = Meter485TestActivity_et_fc3.getText().toString().trim();
                String fc4 = Meter485TestActivity_et_fc4.getText().toString().trim();
                String fc5 = Meter485TestActivity_et_fc5.getText().toString().trim();
                String fc6 = Meter485TestActivity_et_fc6.getText().toString().trim();
                String fc7 = Meter485TestActivity_et_fc7.getText().toString().trim();
                String fc8 = Meter485TestActivity_et_fc8.getText().toString().trim();
                String fc9 = Meter485TestActivity_et_fc9.getText().toString().trim();
                String fc10 = Meter485TestActivity_et_fc10.getText().toString().trim();
                String fc11 = Meter485TestActivity_et_fc11.getText().toString().trim();
                String fc12 = Meter485TestActivity_et_fc12.getText().toString().trim();
                String fc13 = Meter485TestActivity_et_fc13.getText().toString().trim();
                String fc14 = Meter485TestActivity_et_fc14.getText().toString().trim();
                String fc15 = Meter485TestActivity_et_fc15.getText().toString().trim();
                String fc16 = Meter485TestActivity_et_fc16.getText().toString().trim();
                String fc17 = Meter485TestActivity_et_fc17.getText().toString().trim();
                String fc18 = Meter485TestActivity_et_fc18.getText().toString().trim();
                String fc19 = Meter485TestActivity_et_fc19.getText().toString().trim();

                if (fc0.length() > 0) {
                    fc0 = Integer.toHexString(Integer.parseInt(fc0));
                    xxx = xxx + 1;
                }
                if (fc1.length() > 0) {
                    fc1 = Integer.toHexString(Integer.parseInt(fc1));
                    xxx = xxx + 1;
                }
                if (fc2.length() > 0) {
                    fc2 = Integer.toHexString(Integer.parseInt(fc2));
                    xxx = xxx + 1;
                }
                if (fc3.length() > 0) {
                    fc3 = Integer.toHexString(Integer.parseInt(fc3));
                    xxx = xxx + 1;
                }
                if (fc4.length() > 0) {
                    fc4 = Integer.toHexString(Integer.parseInt(fc4));
                    xxx = xxx + 1;
                }
                if (fc5.length() > 0) {
                    fc5 = Integer.toHexString(Integer.parseInt(fc5));
                    xxx = xxx + 1;
                }
                if (fc6.length() > 0) {
                    fc6 = Integer.toHexString(Integer.parseInt(fc6));
                    xxx = xxx + 1;
                }
                if (fc7.length() > 0) {
                    fc7 = Integer.toHexString(Integer.parseInt(fc7));
                    xxx = xxx + 1;
                }
                if (fc8.length() > 0) {
                    fc8 = Integer.toHexString(Integer.parseInt(fc8));
                    xxx = xxx + 1;
                }
                if (fc9.length() > 0) {
                    fc9 = Integer.toHexString(Integer.parseInt(fc9));
                    xxx = xxx + 1;
                }
                if (fc10.length() > 0) {
                    fc10 = Integer.toHexString(Integer.parseInt(fc10));
                    xxx = xxx + 1;
                }
                if (fc11.length() > 0) {
                    fc11 = Integer.toHexString(Integer.parseInt(fc11));
                    xxx = xxx + 1;
                }
                if (fc12.length() > 0) {
                    fc12 = Integer.toHexString(Integer.parseInt(fc12));
                    xxx = xxx + 1;
                }
                if (fc13.length() > 0) {
                    fc13 = Integer.toHexString(Integer.parseInt(fc13));
                    xxx = xxx + 1;
                }
                if (fc14.length() > 0) {
                    fc14 = Integer.toHexString(Integer.parseInt(fc14));
                    xxx = xxx + 1;
                }
                if (fc15.length() > 0) {
                    fc15 = Integer.toHexString(Integer.parseInt(fc15));
                    xxx = xxx + 1;
                }
                if (fc16.length() > 0) {
                    fc16 = Integer.toHexString(Integer.parseInt(fc16));
                    xxx = xxx + 1;
                }
                if (fc17.length() > 0) {
                    fc17 = Integer.toHexString(Integer.parseInt(fc17));
                    xxx = xxx + 1;
                }
                if (fc18.length() > 0) {
                    fc18 = Integer.toHexString(Integer.parseInt(fc18));
                    xxx = xxx + 1;
                }
                if (fc19.length() > 0) {
                    fc19 = Integer.toHexString(Integer.parseInt(fc19));
                    xxx = xxx + 1;
                }
                if (freq.length() == 0 || netId.length() == 0 || mkId.length() == 0) {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据不可为空", "错误");
                }
                freq = Integer.toHexString(Integer.valueOf(freq + "0"));
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
                if (fc5.length() <= 2) {
                    while (fc5.length() < 2) {
                        fc5 = "0" + fc5;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc6.length() <= 2) {
                    while (fc6.length() < 2) {
                        fc6 = "0" + fc6;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc7.length() <= 2) {
                    while (fc7.length() < 2) {
                        fc7 = "0" + fc7;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc8.length() <= 2) {
                    while (fc8.length() < 2) {
                        fc8 = "0" + fc8;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc9.length() <= 2) {
                    while (fc9.length() < 2) {
                        fc9 = "0" + fc9;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }

                if (fc10.length() <= 2) {
                    while (fc10.length() < 2) {
                        fc10 = "0" + fc10;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc11.length() <= 2) {
                    while (fc11.length() < 2) {
                        fc11 = "0" + fc11;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc12.length() <= 2) {
                    while (fc12.length() < 2) {
                        fc12 = "0" + fc12;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc13.length() <= 2) {
                    while (fc13.length() < 2) {
                        fc13 = "0" + fc13;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc14.length() <= 2) {
                    while (fc14.length() < 2) {
                        fc14 = "0" + fc14;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc15.length() <= 2) {
                    while (fc15.length() < 2) {
                        fc15 = "0" + fc15;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc16.length() <= 2) {
                    while (fc16.length() < 2) {
                        fc16 = "0" + fc16;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc17.length() <= 2) {
                    while (fc17.length() < 2) {
                        fc17 = "0" + fc17;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc18.length() <= 2) {
                    while (fc18.length() < 2) {
                        fc18 = "0" + fc18;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                if (fc19.length() <= 2) {
                    while (fc19.length() < 2) {
                        fc19 = "0" + fc19;
                    }
                } else {
                    HintDialog.ShowHintDialog(LoRa_Meter485TestActivity.this, "数据过大", "错误");
                }
                /**
                 * 根据表类型使用不同的通讯协议
                 */
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    HzyUtils.showProgressDialog(LoRa_Meter485TestActivity.this);
                    String sendMsg = "68" + freq + netId + mkId + "402aa101400" + xxx + fc0 + fc1 + fc2 + fc3 + fc4 + fc5 + fc6 + fc7 + fc8 + fc9 + fc10 + fc11 + fc12 + fc13 + fc14 + fc15 + fc16 + fc17 + fc18 + fc19 + "000000000000000000000000000000000000000000005B5B5B3F3F7B7B7B01FE3F5D5D5D";
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
                    if (getMsg.trim().length() != 0) {
                        getMsg = HzyUtils.toStringHex1(getMsg).replaceAll("�", "");
                        Meter485TestActivity_tv_showMsg.append(getMsg + "\n");
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
        editor.putString("etNetId", Meter485TestActivity_et_netid.getText().toString().trim());
        editor.putString("etMkId", Meter485TestActivity_et_mkid.getText().toString().trim());
        editor.putString("etFreq", Meter485TestActivity_et_freq.getText().toString().trim());
        editor.putString("etfc0", Meter485TestActivity_et_fc0.getText().toString().trim());
        editor.putString("etfc1", Meter485TestActivity_et_fc1.getText().toString().trim());
        editor.putString("etfc2", Meter485TestActivity_et_fc2.getText().toString().trim());
        editor.putString("etfc3", Meter485TestActivity_et_fc3.getText().toString().trim());
        editor.putString("etfc4", Meter485TestActivity_et_fc4.getText().toString().trim());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("485Test", MODE_PRIVATE);
        Meter485TestActivity_et_netid.setText(pref.getString("etNetId", ""));
        Meter485TestActivity_et_mkid.setText(pref.getString("etMkId", ""));
        Meter485TestActivity_et_freq.setText(pref.getString("etFreq", ""));
        Meter485TestActivity_et_fc0.setText(pref.getString("etfc0", ""));
        Meter485TestActivity_et_fc1.setText(pref.getString("etfc1", ""));
        Meter485TestActivity_et_fc2.setText(pref.getString("etfc2", ""));
        Meter485TestActivity_et_fc3.setText(pref.getString("etfc3", ""));
        Meter485TestActivity_et_fc4.setText(pref.getString("etfc4", ""));

    }

    @Override
    protected void onDestroy() {
        MenuActivity.btAuto = false;
        saveUser();
        super.onDestroy();
    }
}
