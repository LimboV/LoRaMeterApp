package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;

/**
 * 电阻表读取测试。
 *
 * @author Guhong.
 */
public class Z_SXBReadActivity extends Activity {
    String addr_Broad = "";
    private ProgressDialog progressBar = null;
    private Z_SXBReadActivity thisView = null;
    private boolean binaryPic = false;
    private EditText et_Addr;
    private Button bn_send, btnAutoLoad, btnReadAll;
    private TextView tv_Flow, tv_Recog, tv_Belief;
    private long timeRef = 0;//用于超时设定

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_sxb_test);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//不弹出输入框
        thisView = this;
        progressBar = new ProgressDialog(this);

        et_Addr = (EditText) this
                .findViewById(R.id.EditText_Addr);
        //final String addr_hand = et_Addr.getText().toString();
        tv_Flow = (TextView) this.findViewById(R.id.TextView_Flow);
        tv_Recog = (TextView) this.findViewById(R.id.TextView_Recog);
        tv_Belief = (TextView) this.findViewById(R.id.TextView_Belief);

        SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(
                MenuActivity.PREF_NAME, 0);
        addr_Broad = settings.getString("BADDR", MenuActivity.DEFAULT_BADDR); // 广播地址
        bn_send = (Button) this.findViewById(R.id.Button_SendCmd);
        btnAutoLoad = (Button) this.findViewById(R.id.Button_AutoLoad);

        final Z_SXBReadActivity thisView = this;
        bn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String addr_hand = et_Addr.getText().toString();
                String addr = addr_hand.trim();
                if (addr.equals("")) {
                    addr = addr_Broad.trim();
                }

                if (addr.equals("")) {
                    Toast.makeText(getApplicationContext(), "请指定发送地址或广播地址",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
                try {
                    BluetoothConnectThread.SXInfo cflow = MenuActivity.netThread.readSXMeter(baddr);

                    String cRecog = "";
                    for (int i = 7; i >= 0; i--)
                        cRecog += cflow.recog_data[i] + " ";

                    String cBelief = "";
                    for (int i = 7; i >= 0; i--)
                        cBelief += cflow.recog_belief[i] + " ";

                    tv_Belief.setText(cBelief);
                    tv_Recog.setText(cRecog);
                    tv_Flow.setText(cflow.cflow + "");
                    et_Addr.setText(cflow.addr);
                } catch (NetException e) {
                    HintDialog.ShowHintDialog(thisView, e.sdesc,"错误");
                } catch (IOException e) {
                    finish();
                    MenuActivity.uiAct.resetNetwork(thisView);
                }
            }
        });

        /**
         * 快速读图--一包返回所有的图片
         */
        btnReadAll = (Button) findViewById(R.id.Button_ReadAll);
        btnReadAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTimeStart();
                MenuActivity.Cjj_CB_MSG = "";
                String addr = et_Addr.getText().toString().trim();
                String sendMsg;
                if (addr.length() == 0) {
                    sendMsg = "6810aaaaaaaaaaaaaa310490f60000";
                } else {
                    while (addr.length() < 14) {
                        addr = "0" + addr;
                    }
                    sendMsg = "6810" + changeString(addr) + "310490f60000";
                }
                sendCmd(sendMsg);
                String result = MenuActivity.Cjj_CB_MSG;
//                result = result.replaceAll(" ", "");
//                result = result.replaceAll("0x", "");
//                result = result.replace(sendMsg, "");
                while (!timeRefPassed(5000)&& result.length() <= 1182){
                    try {
                        Thread.sleep(100);
                        result = MenuActivity.Cjj_CB_MSG;
                        result = result.replaceAll(" ", "");
                        result = result.replaceAll("0x", "");
                        result = result.replace(sendMsg.toLowerCase(), "");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //                Log.d("limbo", result);
                if ((result.contains("6810")) && result.length() >= 1182) {//判断接收到的信息是否完整
                    try {
                        result = result.substring(result.indexOf("6810") + 28, result.indexOf("6810") + 1178);
                        //                        Log.d("limbo", result);
                    } catch (Exception e) {
                        Log.e("limbo", e.toString());
                    }
                    String[] getMsg = new String[5];
                    for (int i = 0; i < 5; i++) {
                        getMsg[i] = result.substring(0 + i * 230, 230 * (i + 1));
                        Log.d("limbo", i + "   :   " + getMsg[i]);
                        try {
                            Bitmap bp = null;
                            byte[] picdata = getHexBytes(getMsg[i]);
                            bp =  BluetoothConnectThread.binaryPicBytesToBitmap(picdata);
                            if (bp == null) {
                                Log.e("limbo", i + ":图片为空");
                            } else {
                                Log.d("limbo", "bitmap success");
                                int imgboxlist[] = {R.id.ib_01, R.id.ib_02, R.id.ib_03,
                                        R.id.ib_04, R.id.ib_05, R.id.ib_06, R.id.ib_07,
                                        R.id.ib_08};

                                ImageView iv = (ImageView) findViewById(imgboxlist[i]);
                                try {
                                    iv.setImageBitmap((Bitmap) bp);
                                } catch (Exception e) {
                                    HintDialog.ShowHintDialog(thisView, "图像异常", "错误");
                                }
                            }
                        } catch (Exception e) {
                            Log.e("limbo", e.toString());
                        }
                    }
                } else {
                    Log.d("limbo", result);
                    Log.e("limbo", "数据太短");
                    HintDialog.ShowHintDialog(Z_SXBReadActivity.this,"数据缺失","提示");
                }



            }
        });


        btnAutoLoad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    MenuActivity.Cjj_CB_MSG = "";
                    String addr = et_Addr.getText().toString().trim();
                    if (addr.equals("")) {
                        addr = addr_Broad.trim();
                    }
                    byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
                    try {
                        BluetoothConnectThread.SXInfo cflow = MenuActivity.netThread.readscmdAutoPos(baddr);
                        String cRecog = "";
                        for (int i = 7; i >= 0; i--)
                            cRecog += cflow.recog_data[i] + " ";

                        String cBelief = "";
                        for (int i = 7; i >= 0; i--)
                            cBelief += cflow.recog_belief[i] + " ";

                        tv_Belief.setText(cBelief);
                        tv_Recog.setText(cRecog);
                        tv_Flow.setText(cflow.cflow + "");
                        et_Addr.setText(cflow.addr);

                    } catch (Exception e) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler picReadingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what >= 0) {
                progressBar.setTitle("读取第" + (msg.what)
                        + "个字符");
                progressBar.setMax(msg.arg2);
                progressBar.setProgress(msg.arg1);
            } else if (msg.what == -2) {

                if (msg.obj == null)
                    return;

                int imgboxlist[] = {R.id.ib_01, R.id.ib_02, R.id.ib_03,
                        R.id.ib_04, R.id.ib_05, R.id.ib_06, R.id.ib_07,
                        R.id.ib_08};

                ImageView iv = (ImageView) findViewById(imgboxlist[msg.arg1]);
                try {
                    iv.setImageBitmap((Bitmap) msg.obj);
                    //Matrix matrix = new Matrix();
                    //matrix.reset();
                    //matrix.postScale(2.0F, 2.0F);
                    //iv.setImageMatrix(matrix);
                } catch (Exception e) {
                    HintDialog.ShowHintDialog(thisView, "图像异常","错误");
                }
            } else if (msg.what == -3) {
                progressBar.hide();
            } else if (msg.what == -1) {
                //progressBar.hide();
                HintDialog.ShowHintDialog(thisView, (String) msg.obj,"错误");
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "读灰度图像");
        menu.add(1, 1, 1, "读二值图像");
        menu.add(2, 2, 2, "自动定位");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()) {
            case 0:
            case 1: {
                if (menuItem.getItemId() == 0)
                    binaryPic = false;
                else
                    binaryPic = true;

                final EditText et_Addr = (EditText) this.findViewById(R.id.EditText_Addr);

                String addr_hand = et_Addr.getText().toString();
                String addr = addr_hand.trim();
                if (addr.equals("")) {
                    addr = addr_Broad.trim();
                }

                if (addr.equals("")) {
                    Toast.makeText(getApplicationContext(), "请指定发送地址或广播地址",
                            Toast.LENGTH_LONG).show();
                    return true;
                }

                final byte[] baddr = MenuActivity.netThread.getSBAddr(addr);

                progressBar.setCancelable(false);
                progressBar.setMessage("数据读取进度");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(5);
                progressBar.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int retrytimes = 3;

                        if (binaryPic) {
                            try {
                                MenuActivity.netThread.readBinaryPic_Parse1(baddr);
                            } catch (NetException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        for (int i = 0; i < 5; i++) {

                            try {
                                Bitmap bp = null;

                                if (binaryPic)
                                    bp = MenuActivity.netThread.readBinaryPic_Parse2(baddr, i + 1);
                                else
                                    bp = MenuActivity.netThread.readPic(baddr, i + 1, 3, picReadingHandler);
                                if (bp == null) {
                                    if (retrytimes-- > 0)
                                        i--;
                                } else {
                                    picReadingHandler.obtainMessage(-2, i, 0, bp).sendToTarget();
                                    retrytimes = 3;
                                }
                            } catch (NetException e) {
                                picReadingHandler.obtainMessage(-1, e.sdesc)
                                        .sendToTarget();
                                continue;
                            } catch (IOException e) {
                                picReadingHandler.obtainMessage(-1, e.toString())
                                        .sendToTarget();
                                continue;
                            }

                        }

                        picReadingHandler.obtainMessage(-3).sendToTarget();
                    }
                }).start();
            }
            break;

            /**
             * 自动定位
             */
            case 2:
                try {

                    //                    bn_send.performClick();
                    //                    Thread.sleep(5000);
                    MenuActivity.Cjj_CB_MSG = "";
                    String addr = et_Addr.getText().toString().trim();
                    if (addr.equals("")) {
                        addr = addr_Broad.trim();
                    }
                    byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
                    try {
                        BluetoothConnectThread.SXInfo cflow = MenuActivity.netThread.readscmdAutoPos(baddr);
                        String cRecog = "";
                        for (int i = 7; i >= 0; i--)
                            cRecog += cflow.recog_data[i] + " ";

                        String cBelief = "";
                        for (int i = 7; i >= 0; i--)
                            cBelief += cflow.recog_belief[i] + " ";

                        tv_Belief.setText(cBelief);
                        tv_Recog.setText(cRecog);
                        tv_Flow.setText(cflow.cflow + "");
                        et_Addr.setText(cflow.addr);

                    } catch (Exception e) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

        return true;
    }

    private void sendCmd(String sendMsg) {
        byte[] x = HzyUtils.getHexBytes(sendMsg.toUpperCase());
        byte y = HzyUtils.countSum(x, 0, sendMsg.length() / 2);
        int vvv = y & 0xFF;
        String hv = Integer.toHexString(vvv);
        if (hv.length() < 2) {
            hv = "0" + hv;
        }
        byte[] bos = HzyUtils.getHexBytes("FEFEFE" + sendMsg + hv + "16");
        byte[] bos_new = new byte[bos.length];
        int i, n;
        n = 0;
        for (i = 0; i < bos.length; i++) {
            bos_new[n] = bos[i];
            n++;
        }
        Log.d("limbo", HzyUtils.bytes2HexString(bos_new));
        try {
            MenuActivity.netThread.write(bos_new);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符串调换
     */
    public static String changeString(String newID) {
        while (newID.length() != 14) {
            newID = "0" + newID;
        }
        StringBuilder ssb = new StringBuilder("");
        for (int ii = 0; ii < 14; ii += 2) {
            ssb.append(newID.substring(ii + 1, ii + 2));
            ssb.append(newID.substring(ii, ii + 1));
        }
        newID = ssb.toString();
        StringBuilder sb = new StringBuilder(newID);
        sb.reverse();
        newID = sb.toString();
        return newID;
    }

    /**
     * 从字符串到16进制byte数组转换
     * String to HEX
     */
    public static byte[] getHexBytes(String message) {
        if (message == null || message.equals("")) {
            return null;
//            Log.e("limbo","string --> null");
        }
        message = message.trim();
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }
    /**
     *
     * 开始协议设定时间
     *
     */
    private void prepareTimeStart() {
        timeRef = System.currentTimeMillis();
    }

    /**
     *
     * 判断是否超时
     *
     */
    private boolean timeRefPassed(long timeThresh) {
        if (System.currentTimeMillis() - timeRef > timeThresh)
            return true;
        else
            return false;
    }
}
