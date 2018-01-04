package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_MeterUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by limbo on 2016/12/27.
 */
public class LoRa_LoadCjjMsgActivity extends Activity {

    private boolean isBreak = false;
    private String error = "已存在节点:";//用以记录错误信息
    private ListView lvList;
    private Button btnLoad, btnEnd, btnGetMsg,btnDelete;
    private int xqid;
    private List<LoRa_MeterUser> meterList;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private String from[] = new String[]{"meterid", "data"};
    private int to[] = new int[]{R.id.meterId, R.id.data};
    private SimpleAdapter listItemAdapter;
    private List<String> bagMsgList = new ArrayList<>();//分包数据;
    private List<String> getMsgList = new ArrayList<>();//返回信息列表
    private int listCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_loadcjjmsgactivity);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        meterList = LoRa_DataHelper.getAllMeter(xqid);//从数据库中获取列表
        Log.d("limbo", "列表总数:" + meterList.size());
        lvList = (ListView) findViewById(R.id.LoadCjjMsgActivity_lv_meterList);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isBreak) {
                    return;
                } else {
                    String meterID = meterList.get(position).MeterNumber;
                    int num = meterList.get(position).MeterId;
                    String number = num + "";
                    if (meterID.length() > 14 || number.length() > 8) {
                        HintDialog.ShowHintDialog(LoRa_LoadCjjMsgActivity.this, "数据库中数据过大", "错误");
                        return;
                    }
                    while (number.length() < 8) {
                        number = "0" + number;
                    }
                    while (meterID.length() < 14) {
                        meterID = "0" + meterID;
                    }
                    String sendMsg = "001600D4000111" + number + meterID;
                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                    MenuActivity.sendCmd(sendMsg);
                    try {
                        Thread.sleep(1300);
                        String getMsg = HzyUtils.GetBlueToothMsg();
                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                        if (getMsg.length() == 0) {
                            Message message = new Message();
                            message.what = 0x99;
                            message.obj = meterList.get(position).UserAddr;
                            mHandler.sendMessage(message);
                        } else if (getMsg.equals("0016000ba027")) {

                            Message message = new Message();
                            message.what = 0x01;
                            message.obj = getMsg;
                            message.arg1 = position;
                            mHandler.sendMessage(message);
                        } else if (getMsg.equals("00160020e038")) {

                            Message message = new Message();
                            message.what = 0x02;
                            message.obj = getMsg;
                            message.arg1 = position;
                            mHandler.sendMessage(message);
                        } else {
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
            }
        });
        btnGetMsg = (Button) findViewById(R.id.LoadCjjMsgActivity_btn_getMsg);
        btnGetMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog1(LoRa_LoadCjjMsgActivity.this, "正在查询......");
                String sendMsg = "001600d4000502ffff397e";
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                MenuActivity.sendCmd(sendMsg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(4000);
                            String getMsg = HzyUtils.GetBlueToothMsg();
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            if (getMsg.length() == 0) {
                                Message message = new Message();
                                message.what = 0x99;
                                mHandler.sendMessage(message);
                            } else {
                                Log.d("limbo", getMsg);

                                Message message = new Message();
                                message.what = 0x05;
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
        btnDelete = (Button) findViewById(R.id.LoadCjjMsgActivity_btn_deleteAllMeter);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendMsg = "001600d4000602ffff393a" ;
                MenuActivity.sendCmd(sendMsg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            String getMsg = HzyUtils.GetBlueToothMsg();
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            if (getMsg.length() == 0) {
                                Message message = new Message();
                                message.what = 0x99;
                                mHandler.sendMessage(message);
                            } else {
                                Log.d("limbo", getMsg);

                                Message message = new Message();
                                message.what = 0x05;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btnEnd = (Button) findViewById(R.id.LoadCjjMsgActivity_btn_EndLoad);
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEnd.setVisibility(View.GONE);
                btnLoad.setVisibility(View.VISIBLE);
                isBreak = true;
            }
        });
        btnLoad = (Button) findViewById(R.id.LoadCjjMsgActivity_btn_Load);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBreak = false;
                listCount = 0;
                btnLoad.setVisibility(View.GONE);
                btnEnd.setVisibility(View.VISIBLE);

                HzyUtils.showProgressDialog1(LoRa_LoadCjjMsgActivity.this, "正在导入......");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 分包
                         */
                        int bagAll;
                        if (meterList.size() % 50 > 0) {
                            bagAll = meterList.size() / 50 + 1;
                        } else {
                            bagAll = meterList.size() / 50;
                        }
                        Log.d("limbo", "分包:" + bagAll);
                        for (int i = 0; i < bagAll; i++) {
                            String addMsg = "";
                            for (int j = 0; j < 50; j++) {
                                if ((i * 50 + j) < meterList.size()) {
                                    String meterID = meterList.get(i*50+j).MeterNumber;
                                    int num = meterList.get(i*50+j).MeterId;
                                    String number = num + "";
                                    int data1 = (int) (meterList.get(i*50+j).Data / 1);
                                    String data = data1 + "";
                                    if (meterID.length() > 14 || number.length() > 8 || data.length() > 6) {
                                        HintDialog.ShowHintDialog(LoRa_LoadCjjMsgActivity.this, "数据库中数据过大", "错误");
                                        return;
                                    }
                                    while (number.length() < 8) {
                                        number = "0" + number;
                                    }
                                    while (meterID.length() < 14) {
                                        meterID = "0" + meterID;
                                    }
                                    while (data.length() < 6) {
                                        data = "0" + data;
                                    }
                                    addMsg = addMsg + number + meterID + data;
                                } else if (i + 50 + j > bagAll) {
                                    addMsg = addMsg + "0000000000000000000000000000";
                                }

                            }
                            /**
                             * 不足700字节补全
                             */
                            while (addMsg.length() < 1400) {
                                addMsg = addMsg + "0";
                            }
                            bagMsgList.add(i, addMsg);
                        }
                        /**
                         * 50个一包发送
                         */
                        for (int i = 0; i < bagAll; i++) {
                            if (isBreak) {
                                break;
                            }
                            String sendMsg = "001600d4000950" + bagMsgList.get(i);
                            sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                            MenuActivity.sendCmd(sendMsg);
                            try {
                                Thread.sleep(4000);
                                String getMsg = HzyUtils.GetBlueToothMsg();
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = meterList.get(i).UserAddr;
                                    mHandler.sendMessage(message);
                                } else {
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x04;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                }

                            } catch (Exception e) {
                                Log.d("limbo", e.toString());
                            }


                        }
                        /**
                         * 单个发送
                         */
                        /*for (int i = 0; i < meterList.size(); i++) {
                            if (isBreak) {
                                break;
                            }
                            String meterID = meterList.get(i).MeterNumber;
                            int num = meterList.get(i).MeterId;
                            String number = num + "";
                            if (meterID.length()>14||number.length()>8){
                                HintDialog.ShowHintDialog(LoRa_LoadCjjMsgActivity.this, "数据库中数据过大", "错误");
                                return;
                            }
                            while (number.length() < 8) {
                                number = "0" + number;
                            }
                            while (meterID.length()<14){
                                meterID = "0"+ meterID;
                            }
                            String sendMsg = "001600D4000111" + number + meterID;
                            sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                            MenuActivity.sendCmd(sendMsg);
                            try {
                                Thread.sleep(1500);
                                String getMsg = HzyUtils.GetBlueToothMsg();
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                if (getMsg.length() == 0) {
                                    Message message = new Message();
                                    message.what = 0x99;
                                    message.obj = meterList.get(i).UserAddr;
                                    mHandler.sendMessage(message);
                                } else if (getMsg.equals("0016000ba027")) {

                                    Message message = new Message();
                                    message.what = 0x01;
                                    message.obj = getMsg;
                                    message.arg1 = i;
                                    mHandler.sendMessage(message);
                                } else if (getMsg.equals("00160020e038")) {

                                    Message message = new Message();
                                    message.what = 0x02;
                                    message.obj = getMsg;
                                    message.arg1 = i;
                                    mHandler.sendMessage(message);
                                } else {
                                    Log.d("limbo", getMsg);

                                    Message message = new Message();
                                    message.what = 0x00;
                                    message.obj = getMsg;
                                    message.arg1 = i;
                                    mHandler.sendMessage(message);
                                }
                            } catch (Exception e) {
                                Log.d("limbo", e.toString());
                            }
                        }*/
                        Message message = new Message();
                        message.what = 0x03;
                        //                        message.obj = getMsg;
                        mHandler.sendMessage(message);
                    }
                }).start();
            }
        });
        btnEnd.setVisibility(View.GONE);

        getList();
        listItemAdapter = new SimpleAdapter(this, listItem, R.layout.lora_list_item, from, to);
        listItemAdapter.setViewBinder(new ListViewBinder());
        lvList.setAdapter(listItemAdapter);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "接收:" + getMsg);
                    meterList.get(msg.arg1).setLasteDate("--成功--");
                    getList();
                    listItemAdapter.notifyDataSetChanged();
                    break;
                /**
                 * 空间不足
                 */
                case 0x01:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", getMsg);
                    HzyUtils.closeProgressDialog();
                    isBreak = true;
                    HintDialog.ShowHintDialog(LoRa_LoadCjjMsgActivity.this, "采集机存储空间不足", "导入失败");
                    break;
                /**
                 * 节点已存在
                 */
                case 0x02:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", getMsg);
                    meterList.get(msg.arg1).setLasteDate("--节点已存在--");
                    getList();
                    listItemAdapter.notifyDataSetChanged();
                    break;
                /**
                 *  完成
                 */
                case 0x03:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_LoadCjjMsgActivity.this, "导入数据结束", "导入完成");
                    btnEnd.setVisibility(View.GONE);
                    btnLoad.setVisibility(View.VISIBLE);
                    isBreak = false;
                    break;
                /**
                 * 50一包
                 */
                case 0x04:
                    getMsg = msg.obj.toString();
                    /**
                     * 返回信息封包
                     */
                    if (getMsg.length() >= 614) {
                        getMsg = getMsg.substring(10, 610);
                        for (int i = 0; i < 50; i++) {
                            String msgX = getMsg.substring(i * 12, (i + 1) * 12);
                            Log.d("limbo", msgX);
                            if (msgX.substring(8, 12).equals("0020")) {
                                meterList.get(listCount).setLasteDate("--节点已存在--");
                                getList();
                                listItemAdapter.notifyDataSetChanged();
                                listCount++;
                            } else if (msgX.substring(8, 12).equals("000b")) {
                                meterList.get(listCount).setLasteDate("--内存存储空间不足--");
                                getList();
                                listItemAdapter.notifyDataSetChanged();
                                listCount++;
                            } else if (msgX.substring(8, 12).equals("00a1")) {
                                meterList.get(listCount).setLasteDate("--水表信息添加成功--");
                                getList();
                                listItemAdapter.notifyDataSetChanged();
                                listCount++;
                            } else {

                            }

                        }
                    }

                    break;
                /**
                 * 查询所有信息
                 */
                case 0x05:
                    getMsg = msg.obj.toString();
                    if (getMsg.equals("001600866042")){
                        HintDialog.ShowHintDialog(LoRa_LoadCjjMsgActivity.this,"清空成功","提示");
                    }

                    break;
                case 0x99:
                    error = error + msg.obj.toString() + "\n";
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (isBreak) {

        } else {
            super.onBackPressed();
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

    public void getList() {
        listItem.clear();
        for (int i = 0; i < meterList.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            if (meterList.get(i).getLasteDate() == null) {
                meterList.get(i).setLasteDate("");
            }
            map.put("meterid", "表ID:" + meterList.get(i).MeterNumber + "    " + meterList.get(i).getLasteDate()
                    + "\n序列号:" + meterList.get(i).MeterId
                    + "\n保存时间:" + meterList.get(i).Date);
            map.put("data", "用户地址：" + meterList.get(i).UserAddr);
            listItem.add(map);
        }
        //		return listItem;
    }

}
