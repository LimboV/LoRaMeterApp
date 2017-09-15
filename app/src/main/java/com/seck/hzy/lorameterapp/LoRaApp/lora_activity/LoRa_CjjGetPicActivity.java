package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/9/13.
 */

public class LoRa_CjjGetPicActivity extends Activity {
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.et_jbid)
    EditText et_jbid;
    @BindView(R.id.et_lylj1)
    EditText et_lylj1;
    @BindView(R.id.et_lylj2)
    EditText et_lylj2;
    @BindView(R.id.et_lylj3)
    EditText et_lylj3;
    @BindView(R.id.btn_getPic)
    Button btn_getPic;

    boolean timeOut1 = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_cjjgetpic);
        ButterKnife.bind(this);
        btn_getPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTimeStart1();
                HzyUtils.showProgressDialog(LoRa_CjjGetPicActivity.this);
                String jbid = et_jbid.getText().toString().trim();
                String lylj1 = et_lylj1.getText().toString().trim();
                String lylj2 = et_lylj2.getText().toString().trim();
                String lylj3 = et_lylj3.getText().toString().trim();
                jbid = HzyUtils.isLength(jbid, 14);
                while (lylj1.length() < 2) {
                    lylj1 = lylj1 + "F";
                }
                while (lylj2.length() < 2) {
                    lylj2 = lylj2 + "F";
                }
                while (lylj3.length() < 2) {
                    lylj3 = lylj3 + "F";
                }
                String sendMsg = "001600fe88930a"
                        + jbid
                        + lylj1 + lylj2 + lylj3;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                MenuActivity.sendCmd(sendMsg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String getMsg = "";
                            while (!timeOut1) {
                                Thread.sleep(500);
                                getMsg = getMsg + MenuActivity.Cjj_CB_MSG;
                                MenuActivity.Cjj_CB_MSG = "";
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                if (getMsg.length() > 1240) {
                                    timeOut1 = true;
                                }
                            }

                            if (getMsg.length() == 0) {
                                Message message = new Message();
                                message.what = 0x99;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
//                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                Log.d("limbo", "get:" + getMsg);

                                Message message = new Message();
                                message.what = 0x00;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }).start();


            }
        });

    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00://安美通LoRa表
                    String getMsg = msg.obj.toString();
                    if (getMsg.length() <= 1200) {
                        HzyUtils.closeProgressDialog();
                        HintDialog.ShowHintDialog(LoRa_CjjGetPicActivity.this, "图片接收失败", "提示");
                    } else if (getMsg.length() > 1200) {
                        HzyUtils.closeProgressDialog();
                        String zdsj = Integer.parseInt(getMsg.substring(42, 48)) + "." + getMsg.substring(48, 50);
                        String dcdy = getMsg.substring(1202, 1204) + "." + getMsg.substring(1204, 1206) + "v";
                        String xhqd = Integer.parseInt(getMsg.substring(1212, 1214), 16) + "";

                        tv_msg.setText("\n直读数据:" + zdsj +
                                "\n电池电压:" + dcdy
                                + "\n信号强度:" + xhqd);


                        String show = getMsg.substring(50, 1200);

                        String[] showMsg = new String[5];
                        for (int i = 0; i < 5; i++) {
                            showMsg[i] = show.substring(0 + i * 230, 230 * (i + 1));
                            try {
                                Bitmap bp;
                                byte[] picdata = HzyUtils.getHexBytes(showMsg[i]);
                                bp = BluetoothConnectThread.binaryPicBytesToBitmap(picdata);
                                if (bp == null) {
                                    Log.e("limbo", i + ":图片为空");
                                } else {
                                    Log.d("limbo", "bitmap success");
                                    int imgboxlist[] = {R.id.cjjib_01, R.id.cjjib_02, R.id.cjjib_03,
                                            R.id.cjjib_04, R.id.cjjib_05, R.id.cjjib_06, R.id.cjjib_07,
                                            R.id.cjjib_08};

                                    ImageView iv = (ImageView) findViewById(imgboxlist[i]);
                                    try {
                                        iv.setImageBitmap(bp);
                                    } catch (Exception e) {
                                        HintDialog.ShowHintDialog(LoRa_CjjGetPicActivity.this, "图像异常", "错误");
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("limbo", e.toString());
                            }
                        }

                    }


                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_CjjGetPicActivity.this, "图片接收失败", "提示");
                    break;

            }
        }
    };

    /**
     * 开始协议设定时间
     */
    private void prepareTimeStart1() {
        timeOut1 = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15000);
                    timeOut1 = true;
                    HzyUtils.closeProgressDialog();
                } catch (Exception e) {

                }
            }
        }).start();
    }
}
