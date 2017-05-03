package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.model.PdataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.model.PmeterUser;
import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ssHss on 2016/5/31.
 */
public class P_PmeterActivity extends Activity implements View.OnClickListener {
    private List<PmeterUser> meterList;
    private boolean isBreak;//是否允许退出
    private ListView lvPMeter;
    private int xqid, cjjid, portid, getMsgNum;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter listItemAdapter;
    private String from[] = new String[]{"meterid", "data", "iamge"};
    private int to[] = new int[]{R.id.meterId, R.id.data, R.id.imageView1};
    private String CBTime = "";//用以记录获取到采集机执行抄表任务时的时间
    private TextView tvCBTime;
    private Button btnPCjjCbHis, btnPCjjCbNow, btnLoadBAddr;
    private int positionListOnClick;
    private int position_selection = 0;
    private Spinner spCbMonth;
    private int spinnerX;
    private final String[] sp = {"抄读月份:1月", "抄读月份:2月", "抄读月份:3月", "抄读月份:4月", "抄读月份:5月",
            "抄读月份:6月", "抄读月份:7月", "抄读月份:8月", "抄读月份:9月", "抄读月份:10月", "抄读月份:11月", "抄读月份:12月"};
    private boolean isNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_cjj_cb);
        lvPMeter = (ListView) findViewById(R.id.lvPMeter);
        tvCBTime = (TextView) findViewById(R.id.tvCBTime);
        btnPCjjCbHis = (Button) findViewById(R.id.btnPCjjCbHis);
        btnPCjjCbHis.setOnClickListener(this);
        btnPCjjCbNow = (Button) findViewById(R.id.btnPCjjCbNow);
        btnPCjjCbNow.setOnClickListener(this);
        btnLoadBAddr = (Button) findViewById(R.id.btnLoadBAddr);
        btnLoadBAddr.setOnClickListener(this);

        spCbMonth = (Spinner) findViewById(R.id.spCbMonth);
        ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sp);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCbMonth.setAdapter(ada);
        /**
         * 实现选择项事件(使用匿名类实现接口)
         */
        spCbMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * 选中某一项时
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerX = position + 1;
            }

            /**
             * 未选中时
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");//小区号
        getMsgNum = bundle.getInt("cjjid");//组合编号
        String getcjjid = getMsgNum + "";
        while (getcjjid.length() < 6) {
            getcjjid = "0" + getcjjid;
        }
        cjjid = Integer.parseInt(getcjjid.substring(0, 2));//通讯机号
        portid = Integer.parseInt(getcjjid.substring(4, 6));//端口号
        isBreak = true;
        /*MainActivity.IS_TEST = true;
        if (MainActivity.isStarted == false) {
            MainActivity.netThread.start();
            MainActivity.isStarted = true;
        }*/
        meterList = PdataHelper.getMeters(xqid, getMsgNum);//从数据库中获取列表
        getList();
        listItemAdapter = new SimpleAdapter(this, listItem, R.layout.list_item, from, to);
        listItemAdapter.setViewBinder(new ListViewBinder());
        lvPMeter.setAdapter(listItemAdapter);
//        setupActionBar();
        lvPMeter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionListOnClick = position;

                AlertDialog.Builder dialog = new AlertDialog.Builder(P_PmeterActivity.this);
                dialog.setTitle("请选择功能");
                dialog.setCancelable(false);
                dialog.setPositiveButton("抄单表图片", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CB(positionListOnClick + 1);
                    }
                });
                dialog.setNeutralButton("导入表地址", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadAddr(positionListOnClick);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();

            }
        });
        getTime();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    CBTime = msg.obj.toString();
                    tvCBTime.setText("抄表时间:" + CBTime);
                    break;
                case 0x01:
                    HashMap<String, Object> map = listItem.get(positionListOnClick);
                    map.put("iamge", (Bitmap) msg.obj);
                    PdataHelper.saveMeter(xqid, getMsgNum, meterList.get(positionListOnClick).MeterId, (Bitmap) msg.obj, null, CBTime);
                    listItemAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    public void getList() {
        listItem.clear();
        meterList = PdataHelper.getMeters(xqid, getMsgNum);
        for (int i = 0; i < meterList.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("meterid", "表ID:" + meterList.get(i).MeterNumber);
            map.put("data", "用户地址：" + meterList.get(i).UserAddr);
            map.put("iamge", meterList.get(i).Pic);
            listItem.add(map);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPCjjCbHis:
                isNow = false;
                QCB();
                break;
            case R.id.btnPCjjCbNow:
                isNow = true;
                QCB();
                break;
            case R.id.btnLoadBAddr:
                HzyUtils.showProgressDialog(P_PmeterActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < meterList.size(); i++) {
                            loadAddr(i);
                            try {
                                Thread.sleep(MenuActivity.CMD_DELAY);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        HzyUtils.closeProgressDialog();
                    }
                }).start();

                break;
            default:
                break;
        }
    }

    public void loadAddr(int i) {//导入表地址
        String meterNum = Integer.toHexString(i);
        while (meterNum.length() < 2) {
            meterNum = "0" + meterNum;
        }
        String addr = meterList.get(i).MeterNumber;
        while (addr.length()<14){
            addr = "0" + addr;
        }
        addr = HzyUtils.changeString(addr);
        String Msg = "0010f" + portid + meterNum + "000408"
                + addr + "00";
        String crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
        String sendMsg = Msg + crc16Result;//需要发送的指令
        Log.d("limbo", sendMsg);
        MenuActivity.sendCmd(sendMsg);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void QCB() {//群抄
        if (CBTime.equals("")) {
            getTime();
        } else {
            position_selection = 0;
            HzyUtils.showProgressDialog(P_PmeterActivity.this);
            new AddImageTask().execute();
        }
    }

    private class AddImageTask extends AsyncTask<Void, HashMap<String, Object>, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            listItem.clear();
            meterList = PdataHelper.getMeters(xqid, getMsgNum);
            for (int i = 0; i < meterList.size(); i++) {
                String portString = portid + "";
                if (isNow) {//抄表月份+端口号

                } else {
                    portString = spinnerX + portString;
                }
                String meterNum = Integer.toHexString(i + 1) + "";//表号 --> 十六进制
                if (meterNum.length() < 2) {
                    meterNum = "0" + meterNum;
                }
                String Msg = "000600ff" + portString + meterNum;
                String crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                String sendMsg = Msg + crc16Result;//需要发送的指令
                Log.d("limbo", "读图片:" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
                try {
                    Thread.sleep(MenuActivity.PIC_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                msg = msg.replace(sendMsg, "");//
                int length = msg.length();
                String x = msg.substring(0, length - 4);//数据区
                String y = msg.substring(length - 4, length);//数据结尾
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("meterid", "表ID:" + meterList.get(i).MeterNumber);
                map.put("data", "用户地址：" + meterList.get(i).UserAddr);
                Bitmap bp = null;
                int msgLength = Integer.valueOf(msg.substring(10, 14), 16);//得到数据区长度
                String getMsg = msg.substring(18, 18 + msgLength * 2 - 8);//(数据开头)数据(数据结尾）
                Log.d("limbo", "数据部分:" + getMsg);

                byte[] frame_data = HzyUtils.getHexBytes(getMsg);

                String hexStringToBytes = "FFD8FFDB010600100B0C0E0C0A100E0D0E1211101318281A181616183123251D283A333D3C3933383740485C4E404457453738506D51575F626768673E4D71797064785C656763011112121815182F1A1A2F634238426363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363023021242A241E302A272A3633303948784E4842424893696F5778AE99B7B4AB99A8A5C0D8FFEAC0CCFFCFA5A8F0FFF3FFFFFFFFFFFFBAE7FFFFFFFFFFFFFFFFFF03333636483F488D4E4E8DFFC6A8C6FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC401A20000010501010101010100000000000000000102030405060708090A0B0100030101010101010101010000000000000102030405060708090A0B100002010303020403050504040000017D01020300041105122131410613516107227114328191A1082342B1C11552D1F02433627282090A161718191A25262728292A3435363738393A434445464748494A535455565758595A636465666768696A737475767778797A838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE1E2E3E4E5E6E7E8E9EAF1F2F3F4F5F6F7F8F9FA1100020102040403040705040400010277000102031104052131061241510761711322328108144291A1B1C109233352F0156272D10A162434E125F11718191A262728292A35363738393A434445464748494A535455565758595A636465666768696A737475767778797A82838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE2E3E4E5E6E7E8E9EAF2F3F4F5F6F7F8F9FA";
                byte[] headArray = HzyUtils.getHexBytes(hexStringToBytes);

                byte[] jpegSource = new byte[headArray.length + getMsg.length()];
                System.arraycopy(headArray, 0, jpegSource, 0, headArray.length);

                System.arraycopy(frame_data, 0, jpegSource, headArray.length, frame_data.length);

                bp = BitmapFactory.decodeByteArray(jpegSource, 0, jpegSource.length);
                map.put("iamge", bp);

                PdataHelper.saveMeter(xqid, getMsgNum, meterList.get(position_selection).MeterId, bp, null, CBTime);
                publishProgress(map);
            }
            HzyUtils.closeProgressDialog();
            return null;
        }

        protected void onProgressUpdate(HashMap<String, Object>... values) {
            listItem.add(values[0]);
            listItemAdapter.notifyDataSetChanged();
            lvPMeter.setSelection(position_selection);
            position_selection++;
        }

    }

    private void CB(final int position) {//单抄
        if (CBTime.equals("")) {
            getTime();
        } else {
            HzyUtils.showProgressDialog(P_PmeterActivity.this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String portString = portid + "";
                        {//抄表月份+端口号
                            portString = "0" + portString;
                        }
                        String meterNum = Integer.toHexString(position) + "";//表号 --> 十六进制
                        if (meterNum.length() < 2) {
                            meterNum = "0" + meterNum;
                        }
                        String Msg = "000600ff" + portString + meterNum;
                        String crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
                        String sendMsg = Msg + crc16Result;//需要发送的指令
                        Log.d("limbo", "读图片:" + sendMsg);
                        MenuActivity.sendCmd(sendMsg);
                        Thread.sleep(MenuActivity.PIC_DELAY);
                        String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                        msg = msg.replace(sendMsg, "");//
                        int length = msg.length();
                        if (length == 0) {
                            HzyUtils.closeProgressDialog();
                            Looper.prepare();
                            HintDialog.ShowHintDialog(P_PmeterActivity.this, "未接收到图片", "提示");
                            Looper.loop();
                            return;
                        }
                        String x = msg.substring(0, length - 4);//数据区
                        String y = msg.substring(length - 4, length);//数据结尾
                        if (y.equals("ffd9")) {
                            int msgLength = Integer.valueOf(msg.substring(10, 14), 16);//得到数据区长度
                            String getMsg = msg.substring(18, 18 + msgLength * 2 - 8);//(数据开头)数据(数据结尾）
                            Log.d("limbo", "数据部分:" + getMsg);

                            byte[] frame_data = HzyUtils.getHexBytes(getMsg);

                            String hexStringToBytes = "FFD8FFDB010600100B0C0E0C0A100E0D0E1211101318281A181616183123251D283A333D3C3933383740485C4E404457453738506D51575F626768673E4D71797064785C656763011112121815182F1A1A2F634238426363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363023021242A241E302A272A3633303948784E4842424893696F5778AE99B7B4AB99A8A5C0D8FFEAC0CCFFCFA5A8F0FFF3FFFFFFFFFFFFBAE7FFFFFFFFFFFFFFFFFF03333636483F488D4E4E8DFFC6A8C6FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC401A20000010501010101010100000000000000000102030405060708090A0B0100030101010101010101010000000000000102030405060708090A0B100002010303020403050504040000017D01020300041105122131410613516107227114328191A1082342B1C11552D1F02433627282090A161718191A25262728292A3435363738393A434445464748494A535455565758595A636465666768696A737475767778797A838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE1E2E3E4E5E6E7E8E9EAF1F2F3F4F5F6F7F8F9FA1100020102040403040705040400010277000102031104052131061241510761711322328108144291A1B1C109233352F0156272D10A162434E125F11718191A262728292A35363738393A434445464748494A535455565758595A636465666768696A737475767778797A82838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE2E3E4E5E6E7E8E9EAF2F3F4F5F6F7F8F9FA";
                            byte[] headArray = HzyUtils.getHexBytes(hexStringToBytes);

                            byte[] jpegSource = new byte[headArray.length + getMsg.length()];
                            System.arraycopy(headArray, 0, jpegSource, 0, headArray.length);

                            System.arraycopy(frame_data, 0, jpegSource, headArray.length, frame_data.length);

                            Bitmap bp = BitmapFactory.decodeByteArray(jpegSource, 0, jpegSource.length);

                            Message message = new Message();
                            message.what = 0x01;
                            message.obj = bp;
                            mHandler.sendMessage(message);
                        } else {
                            HzyUtils.closeProgressDialog();
                            Looper.prepare();
                            HintDialog.ShowHintDialog(P_PmeterActivity.this, "返回数据不完整", "提示");
                            Looper.loop();
                        }
                        HzyUtils.closeProgressDialog();
                    } catch (InterruptedException e) {
                        Log.d("limbo", e.toString());
                    }
                }
            }).start();
        }
    }


    private void getTime() {
        /**
         * 第一次抄表时获取SD卡中抄表日期
         */
        if (CBTime.equals("")) {
            String sendMsg = "000300320004E417";
            Log.d("limbo", "读抄表时的日期:" + sendMsg);
            MenuActivity.sendCmd(sendMsg);
            HzyUtils.showProgressDialog(P_PmeterActivity.this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(MenuActivity.PIC_DELAY);
                        String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
                        int length = msg.length();
                        if (length == 0) {
                            HzyUtils.closeProgressDialog();
                            Looper.prepare();
                            HintDialog.ShowHintDialog(P_PmeterActivity.this, "未接收到数据", "提示");
                            Looper.loop();
                            return;
                        }
                        String x = msg.substring(0, length - 4);
                        String y = msg.substring(length - 4, length);
                        if (HzyUtils.CRC16(x).equals(y)) {
                            int msgLength = Integer.valueOf(msg.substring(4, 6));
                            String getMsg = msg.substring(6, 6 + msgLength * 2);
                            getMsg = getMsg.substring(0, 4) + "-" +
                                    getMsg.substring(4, 6) + "-" +
                                    getMsg.substring(6, 8) + " " +
                                    getMsg.substring(8, 10) + ":" +
                                    getMsg.substring(10, 12) + ":" +
                                    getMsg.substring(12, 14);
                            //tvStateRjcs.setText(getMsg);
                            Message message = new Message();
                            message.what = 0x00;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        } else {
                            HzyUtils.closeProgressDialog();
                            Looper.prepare();
                            HintDialog.ShowHintDialog(P_PmeterActivity.this, "CRC16校验码错误", "提示");
                            Looper.loop();
                        }
                        HzyUtils.closeProgressDialog();
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }
                }
            }).start();
        }
    }

    private class ListViewBinder implements SimpleAdapter.ViewBinder {
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if ((view instanceof ImageView) && (data instanceof Bitmap)) {
                ImageView imageView = (ImageView) view;
                Bitmap bmp = (Bitmap) data;
                imageView.setImageBitmap(bmp);
                return true;
            }
            return false;
        }

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
                if (isBreak) {
                    finish();
                }
                return true;
            case 1:
                if (isBreak) {
                    Intent intent = new Intent(P_PmeterActivity.this, MenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    P_PmeterActivity.this.startActivity(intent);
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

}
