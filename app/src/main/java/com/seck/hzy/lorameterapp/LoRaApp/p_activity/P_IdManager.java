package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

/**
 * Created by ssHss on 2015/9/10.
 */
public class P_IdManager extends Activity implements View.OnClickListener {

    private final static int GET_BOX_ID = 1;
    private final static int CHANGE_BOX_ID = 2;
    private final static int GET_BOX_NUM = 3;
    private static int BOX_REQUEST;
    private static ProgressDialog progressDialog;
    private boolean isZNDYH;
    private int countx = 0;

    private TextView tvGetMsg,tvSFPZBM;
    private EditText etNewId, etOldId;
    private Button btnGetId, btnChangeID, btnGetNum,btnRead,btnSet;

    private Spinner mSpinner;
    private final String[] sp = {"扩展模式", "普通模式"};//
    private int spX;
    private String smsg = ""; // 显示用数据缓存
    String  newID,oldId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_id_manager);
//        setupActionBar();//在左上角显示回退按钮
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        tvGetMsg = (TextView) findViewById(R.id.tvGetMsg);
        tvSFPZBM = (TextView) findViewById(R.id.tv_SFPZBM);
        etNewId = (EditText) findViewById(R.id.etNewId);
        etOldId = (EditText) findViewById(R.id.etOldId);
        btnGetId = (Button) findViewById(R.id.btnGetId);
        btnChangeID = (Button) findViewById(R.id.btnChangeID);
        btnGetNum = (Button) findViewById(R.id.btnGetNum);
        btnRead = (Button) findViewById(R.id.IdManager_btn_Read);
        btnSet = (Button) findViewById(R.id.IdManager_btn_Set);
        findViewById(R.id.btnGetId).setOnClickListener(P_IdManager.this);
        findViewById(R.id.btnChangeID).setOnClickListener(P_IdManager.this);
        findViewById(R.id.btnGetNum).setOnClickListener(P_IdManager.this);
        findViewById(R.id.IdManager_btn_Set).setOnClickListener(P_IdManager.this);
        findViewById(R.id.IdManager_btn_Read).setOnClickListener(P_IdManager.this);

        btnRead.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        isZNDYH = bundle.getBoolean("isZNDYH");//获取到了int类型的小区ID
        if (isZNDYH == false) {
            btnGetId.setText("读取摄像表ID");
            btnGetNum.setText("读取摄像表版本号");
            btnChangeID.setText("更改摄像表ID");
            setTitle("摄像表参数");
        } else {
            etOldId.setVisibility(View.GONE);
        }
        mSpinner = (Spinner) findViewById(R.id.spPzfw);
        if (isZNDYH == false) {
            ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sp);
            ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(ada);

            /**
             * 实现选择项事件(使用匿名类实现接口)
             */
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                /**
                 * 选中某一项时
                 */
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spX = position;
                }

                /**
                 * 未选中时
                 */
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }else {
            mSpinner.setVisibility(View.GONE);
            tvSFPZBM.setVisibility(View.GONE);
            btnSet.setVisibility(View.GONE);
        }


    }

    /**
     * fefefe6810+地址+0103824000+校验+16
     */
    private void getPzfw() {

    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "返回主界面");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case 1:
                Intent intent = new Intent(P_IdManager.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                P_IdManager.this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        /**
         * 获取智能单元盒ID
         */
        if (v.getId() == R.id.btnGetId) {
            showProgressDialog();
            BOX_REQUEST = GET_BOX_ID;
            newID = etNewId.getText().toString().trim();
            oldId = etOldId.getText().toString().trim();
            new GetBoxIdTask().execute();
            tvGetMsg.setText("");
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        if (tvGetMsg.getText().equals("")) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 0x99;
                            mHandler.sendMessage(msg);
                        } else {
                            closeProgressDialog();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        /**
         * 更改智能单元盒ID
         */
        else if (v.getId() == R.id.btnChangeID) {
            showProgressDialog1();
            BOX_REQUEST = CHANGE_BOX_ID;
            if (!isZNDYH) {
                if (etOldId.getText().length() == 0 || etOldId.getText().length() > 10) {
                    HintDialog.ShowHintDialog(P_IdManager.this, "原始ID长度不符合标准.", "提示");
                    closeProgressDialog();
                    return;
                }
            }
            if (etNewId.getText().length() > 14) {
                HintDialog.ShowHintDialog(P_IdManager.this, "新ID长度不符合标准.", "提示");
                closeProgressDialog();
                return;
            } else if (etNewId.getText().length() == 0) {
                HintDialog.ShowHintDialog(P_IdManager.this, "请输入新ID", "提示");
                closeProgressDialog();
                return;
            }
            newID = etNewId.getText().toString().trim();
            oldId = etOldId.getText().toString().trim();
            new GetBoxIdTask().execute();
        }

        /**
         * 获取单元盒版本号
         */
        else if (v.getId() == R.id.btnGetNum) {
            showProgressDialog();
            BOX_REQUEST = GET_BOX_NUM;
            newID = etNewId.getText().toString().trim();
            oldId = etOldId.getText().toString().trim();
            new GetBoxIdTask().execute();
            tvGetMsg.setText("");
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        if (tvGetMsg.getText().equals("")) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 0x99;
                            mHandler.sendMessage(msg);
                        } else {
                            closeProgressDialog();
                        }
                    } catch (InterruptedException e) {
                        closeProgressDialog();
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        else if (v.getId() == R.id.IdManager_btn_Read){
            HzyUtils.showProgressDialog(P_IdManager.this);
            BOX_REQUEST = 0;
            MenuActivity.Cjj_CB_MSG = "";
            String msg;
            try {
                msg = "6810aaaaaaaaaaaaaa0103824000";

                String hv = HzyUtils.getCheckSum(msg);
                final String sengMsg = "fefefe" + msg + hv + "16";
                Log.d("limbo", "发送:" + sengMsg);
                //stateActivity.sendCmd(sengMsg);
                int i, n;
                smsg ="";
                byte[] x;
                byte[] bos;
                bos = HzyUtils.getHexBytes(sengMsg);
                byte[] bos_new = new byte[bos.length];
                n = 0;
                for (i = 0; i < bos.length; i++) {
                    bos_new[n] = bos[i];
                    n++;
                }
                BluetoothConnectThread.mmOutStream.write(bos_new);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            String msg = smsg.replaceAll("0x", "").replaceAll(" ", "");
                            msg = msg.substring(msg.indexOf("6891")).toLowerCase();
                            Log.d("limbo", msg);
                            int length = msg.length();
                            if (length == 0) {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_IdManager.this, "未接收到数据", "提示");
                                Looper.loop();
                                return;
                            }
                            String x = msg.substring(0, length - 4);
                            String y = msg.substring(length - 4, length - 2);
                            if (HzyUtils.checkSum(x, y)) {
                                int msgLength = msg.length();
                                String getMsg = msg.substring(msgLength - 6, msgLength - 4);

                                Message message = new Message();
                                message.what = 0x55;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                HzyUtils.closeProgressDialog();
                                Looper.prepare();
                                HintDialog.ShowHintDialog(P_IdManager.this, "校验错误", "提示");
                                Looper.loop();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                HzyUtils.closeProgressDialog();
                Log.d("limbo", e.toString());
            }
        }else if (v.getId() == R.id.IdManager_btn_Set){
            AlertDialog.Builder dialog = new AlertDialog.Builder(P_IdManager.this);
            dialog.setTitle("“是否确认更改摄像表抄表模式”，");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HzyUtils.showProgressDialog(P_IdManager.this);
                    BOX_REQUEST = 0;
                    MenuActivity.Cjj_CB_MSG = "";
                    String msg ;
                    try {
                        if (spX == 0){
                            msg = "6810aaaaaaaaaaaaaa2104824000aa";
                        }else {
                            msg = "6810aaaaaaaaaaaaaa2104824000ff";
                        }


                        String hv = HzyUtils.getCheckSum(msg);
                        String sengMsg = "fefefe" + msg + hv + "16";
                        Log.d("limbo", "发送" + sengMsg);
                        //                            stateActivity.sendCmd(sengMsg);
                        int i, n;
                        smsg ="";
                        byte[] x;
                        byte[] bos;
                        bos = HzyUtils.getHexBytes(sengMsg);
                        byte[] bos_new = new byte[bos.length];
                        n = 0;
                        for (i = 0; i < bos.length; i++) {
                            bos_new[n] = bos[i];
                            n++;
                        }
                        BluetoothConnectThread.mmOutStream.write(bos_new);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    String msg = smsg.replaceAll("0x", "").replaceAll(" ", "");

                                    int length = msg.length();
                                    if (length == 0 ||!msg.contains("6891")) {
                                        HzyUtils.closeProgressDialog();
                                        Looper.prepare();
//                                        HintDialog.ShowHintDialog(P_IdManager.this, "未接收到返回数据", "提示");
                                        HintDialog.ShowHintDialog(P_IdManager.this, "修改完成", "提示");
                                        Looper.loop();
                                        return;
                                    }
                                    Log.d("limbo", msg);
                                   /* msg = msg.substring(msg.indexOf("6891")).toLowerCase();

                                    String x = msg.substring(0, length - 4);
                                    String y = msg.substring(length - 4, length - 2);
                                    if (HzyUtils.checkSum(x, y)) {
                                        int msgLength = msg.length();
                                        String getMsg = msg.substring(msgLength - 6, msgLength - 4);

                                        Message message = new Message();
                                        message.what = 0x55;
                                        message.obj = getMsg;
                                        mHandler.sendMessage(message);
                                    } else {
                                        HzyUtils.closeProgressDialog();
                                        Looper.prepare();
                                        HintDialog.ShowHintDialog(P_IdManager.this, "校验错误", "提示");
                                        Looper.loop();
                                    }*/
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    HzyUtils.closeProgressDialog();
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();

        }else {

        }
    }

    private class GetBoxIdTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            smsg = "";
            if (BOX_REQUEST == GET_BOX_ID) {
                int i, n;
                try {
                    byte[] x;
                    if (isZNDYH) {
                        x = HzyUtils.getHexBytes("6810dddddddddddddd03030a8100");
                    } else {
                        x = HzyUtils.getHexBytes("6810aaaaaaaaaaaaaa03030a8100");
                    }
                    byte y = HzyUtils.countSum(x, 0, 14);
                    int vvv = y & 0xFF;
                    String hv = Integer.toHexString(vvv);
                    if (hv.length() < 2) {
                        hv = "0" + hv;
                    }
                    byte[] bos;
                    if (isZNDYH) {
                        // 将输入的信息去除前后空格后转化为byte格式
                        bos = HzyUtils.getHexBytes("fefefe6810dddddddddddddd03030a8100" + hv + "16");
                    } else {
                        bos = HzyUtils.getHexBytes("fefefe6810aaaaaaaaaaaaaa03030a8100" + hv + "16");
                    }
                    byte[] bos_new = new byte[bos.length];
                    n = 0;
                    for (i = 0; i < bos.length; i++) {
                        bos_new[n] = bos[i];
                        n++;
                    }
                    Log.d("limbo", "获取ID");
                    BluetoothConnectThread.mmOutStream.write(bos_new);
                } catch (Exception e) {

                }
            } else if (BOX_REQUEST == CHANGE_BOX_ID) {
                int i, n;
                try {

                    while (oldId.length() < 14) {
                        oldId = "0" + oldId;
                    }
                    oldId = HzyUtils.changeString(oldId);
                    newID = HzyUtils.changeString(newID);
                    byte[] x;

                    if (isZNDYH) {
                        x = HzyUtils.getHexBytes("6810dddddddddddddd150718a000" + newID);
                    } else {
                        x = HzyUtils.getHexBytes("6810" + oldId + "150aa01800" + newID);
                    }

                    byte y = HzyUtils.countSum(x, 0, 21);//截取需要计算校验和的数段
                    int vvv = y & 0xFF;
                    String hv = Integer.toHexString(vvv);
                    if (hv.length() < 2) {
                        hv = "0" + hv;
                    }
                    byte[] bos;
                    if (isZNDYH) {
                        bos = HzyUtils.getHexBytes("fefefe6810dddddddddddddd150718a000" + newID + hv + "16");
                    } else {
                        bos = HzyUtils.getHexBytes("fefefe6810" + oldId + "150aa01800" + newID + hv + "16");
                    }

                    byte[] bos_new = new byte[bos.length];
                    n = 0;
                    for (i = 0; i < bos.length; i++) {
                        bos_new[n] = bos[i];
                        n++;
                    }
                    Log.d("limbo", "改变ID");
                    BluetoothConnectThread.mmOutStream.write(bos_new);
                } catch (Exception e) {

                }
            } else if (BOX_REQUEST == GET_BOX_NUM) {
                int i, n;
                mHandler.sendMessage(mHandler.obtainMessage());
                try {
                    byte[] x;
                    if (isZNDYH) {
                        x = HzyUtils.getHexBytes("6810dddddddddddddd2003810000");
                    } else {
//                        x = HzyUtils.getHexBytes("6810aaaaaaaaaaaaaa0103901F00");
                        x = HzyUtils.getHexBytes("6810aaaaaaaaaaaaaa2003810000");
                    }
                    byte y = HzyUtils.countSum(x, 0, 14);
                    int vvv = y & 0xFF;
                    String hv = Integer.toHexString(vvv);
                    if (hv.length() < 2) {
                        hv = "0" + hv;
                    }
                    byte[] bos;
                    if (isZNDYH) {
                        bos = HzyUtils.getHexBytes("fefefe6810dddddddddddddd2003810000" + hv + "16");
                    } else {
//                        bos = HzyUtils.getHexBytes("fefefe6810aaaaaaaaaaaaaa0103901F00" + hv + "16");
                        bos = HzyUtils.getHexBytes("fefefe6810aaaaaaaaaaaaaa2003810000" + hv + "16");
                    }
                    byte[] bos_new = new byte[bos.length];
                    n = 0;
                    for (i = 0; i < bos.length; i++) {
                        bos_new[n] = bos[i];
                        n++;
                    }
                    Log.d("limbo", "获取版本号");
                    BluetoothConnectThread.mmOutStream.write(bos_new);
                } catch (Exception e) {

                }
            }
            return null;
        }
    }

    /**
     * 接收数据线程
     */
    public Thread ReadThread = new Thread() {
        public void run() {
            int num;
            byte[] buffer = new byte[1024];// 缓存区
            // 接收线程
            int i, n;
            try {
                while (true) {
                    if (BluetoothConnectThread.mmInStream.available() > 0) {
                        Thread.sleep(500);
                        num = BluetoothConnectThread.mmInStream.read(buffer); // 读入数据
                        n = 0;
                        byte[] buffer_new = new byte[num];
                        for (i = 0; i < num; i++) {
                            buffer_new[n] = buffer[i];
                            n++;
                        }
                        String s = HzyUtils.bytes2HexString(buffer_new);
                        smsg += s; // 写入接收 缓存

                        if (smsg.contains("6891") && smsg.contains("16")) {
                            // 发送显示消息，进行显示刷新
                            Message msg = mHandler.obtainMessage();
                            msg.what = BOX_REQUEST;
                            msg.obj = smsg;
                            mHandler.sendMessage(msg);
                        }

                        //                        }
                        //                    }
                    } else {
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_BOX_ID:
                    smsg = (String) msg.obj;
                    smsg = smsg.replaceAll("FE", "");
                    Log.d("limbo", "P_IdManager.class  " + smsg);
                    /**
                     * 智能单元盒ID
                     */
                    if (smsg.length() >= 32 && smsg.contains("6891") && smsg.contains("16") && isZNDYH == true) {
                        smsg = smsg.substring(smsg.indexOf("6891"));
                        String hv, fhv, IDMsg;
                        IDMsg = smsg.substring(4, 18);
                        fhv = smsg.substring(28, 30).toUpperCase();
                        /**
                         * 计算校验码
                         */
                        byte[] x = HzyUtils.getHexBytes(smsg.substring(0, 28));
                        int v = HzyUtils.countSum(x, 0, 14) & 0xFF;
                        hv = Integer.toHexString(v);
                        if (hv.length() < 2) {
                            hv = "0" + hv;
                        }
                        hv = hv.toUpperCase();
                        if (hv.equals(fhv)) {
                            StringBuilder sb = new StringBuilder("");
                            for (int i = 0; i < 14; i += 2) {
                                sb.append(IDMsg.substring(i + 1, i + 2));
                                sb.append(IDMsg.substring(i, i + 1));
                            }
                            sb.reverse();
                            IDMsg = sb.toString();
                            tvGetMsg.setText("单元盒ID为:" + IDMsg + "\n" +
                                    "小区号:" + IDMsg.substring(4, 8) + "\n" +
                                    "端口号:" + IDMsg.substring(8, 14));
                            //                            closeProgressDialog();
                        }
                    }
                    /**
                     * 水表ID
                     */
                    else if (smsg.length() >= 32 && smsg.contains("6891") && smsg.contains("16") && isZNDYH == false) {
                        smsg = smsg.substring(smsg.indexOf("6891"), smsg.indexOf("6891") + 32);
                        String hv, fhv, IDMsg;
                        IDMsg = smsg.substring(4, 18);
                        fhv = smsg.substring(28, 30).toUpperCase();
                        /**
                         * 计算校验码
                         */
                        byte[] x = HzyUtils.getHexBytes(smsg.substring(0, 28));
                        int v = HzyUtils.countSum(x, 0, 14) & 0xFF;
                        hv = Integer.toHexString(v);
                        if (hv.length() < 2) {
                            hv = "0" + hv;
                        }
                        hv = hv.toUpperCase();
                        if (hv.equals(fhv)) {
                            StringBuilder sb = new StringBuilder("");
                            for (int i = 0; i < 14; i += 2) {
                                sb.append(IDMsg.substring(i + 1, i + 2));
                                sb.append(IDMsg.substring(i, i + 1));
                            }
                            sb.reverse();
                            IDMsg = sb.toString();
                            tvGetMsg.setText("摄像表ID为:" + IDMsg.substring(4, 14));
                            //                            closeProgressDialog();
                        }
                    }

                    break;

                case CHANGE_BOX_ID:
                    tvGetMsg.setText("修改完成！");
                    String z = etNewId.getText().toString().trim();
                    etOldId.setText(z);
                    closeProgressDialog();
                    break;

                case GET_BOX_NUM:
                    smsg = (String) msg.obj;
                    smsg = smsg.replaceAll("FE", "");
                    smsg = smsg.substring(smsg.indexOf("6891"));
                    Log.d("limbo", "P_IdManager.class  " + smsg);
                    /**
                     * 智能单元盒版本号
                     */
                    if (smsg.length() >= 70 && smsg.contains("6891") && smsg.contains("16") && isZNDYH == true) {
                        smsg = smsg.substring(smsg.indexOf("6891"), smsg.indexOf("6891") + 70);
                        String hv, fhv, IDMsg;
                        IDMsg = smsg.substring(30, 40);
                        fhv = smsg.substring(66, 68).toUpperCase();
                        byte[] x = HzyUtils.getHexBytes(smsg.substring(0, 66));
                        int v = HzyUtils.countSum(x, 0, 33) & 0xFF;
                        hv = Integer.toHexString(v);
                        if (hv.length() < 2) {
                            hv = "0" + hv;
                        }
                        hv = hv.toUpperCase();
                        if (hv.equals(fhv)) {
                            tvGetMsg.setText("单元盒版本号为:" + IDMsg);
                            //                            closeProgressDialog();
                        }
                    }
                    /**
                     * 摄像表版本号
                     */
                    else if (smsg.contains("6891") && smsg.contains("16") && isZNDYH == false) {
                        smsg = smsg.substring(smsg.indexOf("6891"), smsg.indexOf("6891") + 32);
                        String hv, fhv, IDMsg;
                        IDMsg = smsg.substring(4, 18);
                        fhv = smsg.substring(28, 30).toUpperCase();
                        byte[] x = HzyUtils.getHexBytes(smsg.substring(0, 28));
                        int v = HzyUtils.countSum(x, 0, 14) & 0xFF;
                        hv = Integer.toHexString(v);
                        if (hv.length() < 2) {
                            hv = "0" + hv;
                        }
                        hv = hv.toUpperCase();
                        if (hv.equals(fhv)) {
                            tvGetMsg.setText("摄像表版本号为:" + IDMsg);
                            //                            closeProgressDialog();
                        }
                    }
                    break;
                case 0x55:
                    String getmsg = msg.obj.toString();
                    if (getmsg.equals("aa")) {
                        tvSFPZBM.setText("当前设置为拍摄编码");
                    } else {
                        tvSFPZBM.setText("当前设置为不拍摄编码");
                    }
                    HzyUtils.closeProgressDialog();
                    break;

                case 0x99:
                    tvGetMsg.setText("读取失败,请重试.");
                    closeProgressDialog();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        ReadThread.interrupt();
        super.onDestroy();
    }

    /**
     * 显示进度条
     */
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(P_IdManager.this);
            progressDialog.setMessage("正在读取...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 显示进度条1
     */
    public void showProgressDialog1() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(P_IdManager.this);
            progressDialog.setMessage("正在写入...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度条
     */
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
}
