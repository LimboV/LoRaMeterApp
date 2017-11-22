package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.seck.hzy.lorameterapp.LoRaApp.p_activity.P_PasswordDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

/**
 * Created by limbo on 2017/3/31.
 */
public class LoRa_DActrlActivity extends Activity {
    private Button btnAddAll, LoRa_DActrlActivity_btn_deleteAll, btnAddSingle, btnSearchSingle, btnDeleteSingle, btnReplaceSingle, btnReadSingle, btnSetSingle;
    boolean timeOut = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_dactrl);
        btnAddAll = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_addAllData);
        btnAddAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_XqListActivity.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_XqListActivity.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }


            }
        });
        btnAddSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_addSingleData);
        btnAddSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_AddSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }

            }
        });
        btnSearchSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_searchSingleData);
        btnSearchSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_SearchSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        btnDeleteSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_deleteSingleData);
        btnDeleteSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_DeleteSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        btnReplaceSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_replaceSingleData);
        btnReplaceSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_ReplaceSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        btnReadSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_readSingleData);
        btnReadSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_ReadSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        btnSetSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_setSingleData);
        btnSetSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_SetSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        LoRa_DActrlActivity_btn_deleteAll = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_deleteAll);
        LoRa_DActrlActivity_btn_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W") || MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    if (checkPsw() == 1) {
                        prepareTimeStart(150);
                        String sendMsg = "001600d4000602ffff393a";
                        MenuActivity.sendCmd(sendMsg);
                        HzyUtils.showProgressDialog(LoRa_DActrlActivity.this);

                        MenuActivity.btAuto = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (MenuActivity.btAuto) {
                                    try {
                                        Thread.sleep(1000);
                                        String resultMsg = "";
                                        resultMsg = resultMsg + MenuActivity.Cjj_CB_MSG;
                                        MenuActivity.Cjj_CB_MSG = "";
                                        Log.d("limbo",resultMsg);
                                        if (resultMsg.length() != 0) {
                                            resultMsg = resultMsg.replaceAll("0x", "").replaceAll(" ", "");
                                            Log.d("limbo", "get:" + resultMsg);
                                            Message message = new Message();
                                            message.obj = resultMsg;
                                            message.what = 0x00;
                                            mHandler.sendMessage(message);
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }).start();
                    }



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
                    String getMsg = msg.obj.toString().toLowerCase();
                    if (getMsg.equals("001600866042")){
                        HintDialog.ShowHintDialog(LoRa_DActrlActivity.this, "删除成功。", "提示");
                        HzyUtils.closeProgressDialog();
                        timeOut = true;
                        MenuActivity.btAuto = false;
                    }

                    break;
                case 0x99:
                    MenuActivity.btAuto = false;
                    HintDialog.ShowHintDialog(LoRa_DActrlActivity.this, "无数据返回，请重试。", "提示");
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
                        HzyUtils.closeProgressDialog();
                        Message message = new Message();
                        message.what = 0x99;
                        mHandler.sendMessage(message);
                    }
                    timeOut = true;
                } catch (Exception e) {
                    Log.d("limbo", e.toString());
                }
            }
        }).start();
    }

    private int checkPsw() {
        P_PasswordDialog pswdlg = new P_PasswordDialog(this, 2);
        int rtn = pswdlg.showDialog();
        if (rtn != 1) {
            HintDialog.ShowHintDialog(this, "密码输入错误", "错误");
        }
        return rtn;
    }

    @Override
    protected void onDestroy() {
        MenuActivity.btAuto = false;
        timeOut = true;
        super.onDestroy();
    }
}
