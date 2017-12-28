package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by limbo on 2016/10/9.
 */
public class LoRa_BluetoothUtilsActivity extends Activity {

    private TextView tvMsg;
    private Button btnSend, btnTest, btnSave, btnJcTest, btnCTS, btnCTS2;
    private EditText etSend;
    private Spinner spTest;
    private CheckBox mCheckBox, mCheckBox1;
    private int spinnerX;
    private final String[] sp = {"测试表1", "测试表2", "测试表3", "测试表4", "测试表5", "测试表6",
            "测试表7", "测试表8", "测试表9", "测试表10"};

    private String txtMsg = "";
    private long mPressedTime = 0;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_bluetooth_utils);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        tvMsg = (TextView) findViewById(R.id.BluetoothUtilsActivity_tv_msg);
        etSend = (EditText) findViewById(R.id.BluetoothUtilsActivity_et_send);
        btnSend = (Button) findViewById(R.id.BluetoothUtilsActivity_btn_send);
        btnTest = (Button) findViewById(R.id.BluetoothUtilsActivity_btn_cbTest);
        btnSave = (Button) findViewById(R.id.BluetoothUtilsActivity_btn_saveAsTxt);
        btnJcTest = (Button) findViewById(R.id.BluetoothUtilsActivity_btn_JcTest);
        btnCTS = (Button) findViewById(R.id.BluetoothUtilsActivity_btn_ConnectToService);
        btnCTS2 = (Button) findViewById(R.id.BluetoothUtilsActivity_btn_ConnectToService2);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mCheckBox1 = (CheckBox) findViewById(R.id.checkBox1);
        mScrollView = (ScrollView) findViewById(R.id.BluetoothUtilsActivity_sv_msg);

        spTest = (Spinner) findViewById(R.id.BluetoothUtilsActivity_sp_cbTest);
        ArrayAdapter<String> ada = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sp);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTest.setAdapter(ada);
        /**
         * 实现选择项事件(使用匿名类实现接口)
         */
        spTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * 选中某一项时
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerX = position;
            }

            /**
             * 未选中时
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (System.currentTimeMillis() - mPressedTime < 2000) {
                    tvMsg.setText("");
                } else {
                    mPressedTime = System.currentTimeMillis();
                    Toast.makeText(LoRa_BluetoothUtilsActivity.this, "再次点击清屏", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etSend.getText().toString().trim().replaceAll(" ", "");
                if (content.length() > 0) {
                    MenuActivity.btAuto = true;
                    HzyUtils.showProgressDialog(LoRa_BluetoothUtilsActivity.this);
                    Log.d("limbo", "获取:" + content);
                    MenuActivity.sendCmd(content);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (MenuActivity.btAuto) {
                                    Thread.sleep(2500);
                                    String getMsg = HzyUtils.GetBlueToothMsg();
                                    getMsg = getMsg.replaceAll("0x", "");
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x00;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }

                            } catch (Exception e) {
                                Log.d("limbo", e.toString());
                            }
                        }
                    }).start();
                }
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity.btAuto = false;
                tvMsg.setText("");
                HzyUtils.showProgressDialog(LoRa_BluetoothUtilsActivity.this);
                String sendMsg = (spinnerX + 1) + "";
                while (sendMsg.length() < 8) {
                    sendMsg = "0" + sendMsg;
                }
                sendMsg = "ff078D981f" + sendMsg;
                Log.d("limbo", "水表测试:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            if (getMsg.length() == 0) {
                                Message message = new Message();
                                message.what = 0x99;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else if (getMsg.contains("a1")) {
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                Log.d("limbo", getMsg);

                                Message message = new Message();
                                message.what = 0x01;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.what = 0x98;
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
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckTest";
                    //                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() ;
                    String filePath = path + "/YCtest" + ".txt";
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File f = new File(filePath);
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        Log.d("limbo", "file create error");
                    }
                    FileOutputStream outStream = new FileOutputStream(f);
                    OutputStreamWriter writer = new OutputStreamWriter(outStream);
                    writer.write(txtMsg);
                    //            writer.write("/n");
                    writer.flush();
                    writer.close();//记得关闭

                    outStream.close();
                    HintDialog.ShowHintDialog(LoRa_BluetoothUtilsActivity.this, "save success", "tip");
                    Log.d("limbo", "file write success");
                } catch (Exception e) {
                    Log.d("limbo", "file write error");
                }
            }
        });

        btnJcTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "000600fd77777e3d";
                MenuActivity.btAuto = true;
                HzyUtils.showProgressDialog(LoRa_BluetoothUtilsActivity.this);
                Log.d("limbo", "获取:" + content);
                MenuActivity.sendCmd(content);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (MenuActivity.btAuto) {
                                Thread.sleep(1500);
                                String getMsg = HzyUtils.GetBlueToothMsg();
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                Log.d("limbo", getMsg);

                                Message message = new Message();
                                message.what = 0x00;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            }

                        } catch (Exception e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();
            }
        });

        btnCTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "000600fd0a0a9f4c";
                HzyUtils.showProgressDialog(LoRa_BluetoothUtilsActivity.this);
                Log.d("limbo", "发送:" + content);
                MenuActivity.sendCmd(content);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            MenuActivity.Cjj_CB_MSG = "";
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", getMsg);

                            Message message = new Message();
                            message.what = 0x02;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);

                        } catch (Exception e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();
            }
        });

        btnCTS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "000600fd0b0b5f1c";
                HzyUtils.showProgressDialog(LoRa_BluetoothUtilsActivity.this);
                Log.d("limbo", "发送:" + content);
                MenuActivity.sendCmd(content);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            MenuActivity.Cjj_CB_MSG = "";
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", getMsg);

                            Message message = new Message();
                            message.what = 0x03;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);

                        } catch (Exception e) {
                            Log.d("limbo", e.toString());
                        }
                    }
                }).start();
            }
        });

    }

    public static void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }

                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 *
                 */
                case 0x00:
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);

                    if (getMsg.trim().length() != 0) {

                        if (mCheckBox.isChecked()) {
                            HzyUtils.addPlace(getMsg);
                        } else {
                            getMsg = getMsg.replaceAll(" ", "");
                            getMsg = HzyUtils.toStringHex1(getMsg).replaceAll("�", "");
                        }

                        tvMsg.append(getMsg);
                    }
                    if (mCheckBox1.isChecked()) {
                        scrollToBottom(mScrollView, tvMsg);
                    } else {

                    }

                    HzyUtils.closeProgressDialog();

                    break;
                case 0x01:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    String value = getMsg.substring(11, 12) + getMsg.substring(13, 14) +
                            getMsg.substring(15, 16) + "." + getMsg.substring(17, 18);
                    String power = ((float) Integer.parseInt(getMsg.substring(20, 22), 16) + 200) / 100 + "V";
                    String xh = Integer.parseInt(getMsg.substring(20, 22), 16) + "";
                    tvMsg.append("数据:" + value + "吨\n电量:" + power + "\n信号强度:" + xh + "dBm");

                    Calendar c = Calendar.getInstance();
                    Date date = c.getTime();
                    DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
                    txtMsg = txtMsg + "\n\n" + df.format(date) + "\n表" + (spinnerX + 1) + "\n数据:" + value + "吨\n电量:" + power + "\n信号强度:" + xh + "dBm";
                    HzyUtils.closeProgressDialog();
                    break;
                case 0x02:
                    HzyUtils.closeProgressDialog();
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    if (getMsg.equals("000600a0e05d")) {
                        HintDialog.ShowHintDialog(LoRa_BluetoothUtilsActivity.this, "开始连接服务器", "提示");
                    } else {
                        HintDialog.ShowHintDialog(LoRa_BluetoothUtilsActivity.this, "连接服务器失败", "提示");
                    }
                    break;
                case 0x03:
                    HzyUtils.closeProgressDialog();
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    if (getMsg.equals("000600a0e05d")) {
                        HintDialog.ShowHintDialog(LoRa_BluetoothUtilsActivity.this, "开始连接服务器", "提示");
                    } else {
                        HintDialog.ShowHintDialog(LoRa_BluetoothUtilsActivity.this, "连接服务器失败", "提示");
                    }
                    break;
                case 0x98:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_BluetoothUtilsActivity.this, "数据缺失", "提示");
                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_BluetoothUtilsActivity.this, "未接收到数据", "提示");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        MenuActivity.btAuto = false;
        super.onDestroy();
    }
}

