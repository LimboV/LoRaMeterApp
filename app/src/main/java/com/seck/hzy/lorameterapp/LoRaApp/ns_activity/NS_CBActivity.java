package com.seck.hzy.lorameterapp.LoRaApp.ns_activity;

import android.app.Activity;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_Cjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_MeterUser;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NS_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/11/28.
 */

public class NS_CBActivity extends Activity {
    private int xqid, zcjjid, cjjid, DelayTime;//小区号 ，总采号，采集机号
    private List<LoRa_MeterUser> meterList;
    private List<LoRa_Cjj> cjjList;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
    private String from[] = new String[]{"meterid", "data"};
    private int to[] = new int[]{R.id.meterId, R.id.data};
    int size = 0, fcCount = 0;//分采个数
    private SimpleAdapter listItemAdapter;
    @BindView(R.id.NS_CBActivity_lv_meterList)
    ListView NS_CBActivity_lv_meterList;
    @BindView(R.id.NS_CBActivity_btn_cb)
    Button NS_CBActivity_btn_cb;
    @BindView(R.id.NS_CBActivity_et_freq)
    EditText NS_CBActivity_et_freq;
    @BindView(R.id.NS_CBActivity_et_netid)
    EditText NS_CBActivity_et_netid;
    @BindView(R.id.tv_1)
    TextView tv_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ns_activity_cb);
        ButterKnife.bind(this);
        loadUser();
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        zcjjid = bundle.getInt("cjjid") / 10000;
        //        meterList = NS_DataHelper.getZCMeters(xqid, zcjjid); //根据小区ID加载采集机信息列表
        cjjList = NS_DataHelper.getFCjj(xqid, zcjjid); //根据小区ID加载分采集机信息列表
        getList();
        listItemAdapter = new SimpleAdapter(this, listItem, R.layout.lora_list_item, from, to);
        listItemAdapter.setViewBinder(new ListViewBinder());
        NS_CBActivity_lv_meterList.setAdapter(listItemAdapter);

        NS_CBActivity_btn_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String freq = NS_CBActivity_et_freq.getText().toString().trim();
                String netid = NS_CBActivity_et_netid.getText().toString().trim();
                if (HzyUtils.isEmpty(freq) || HzyUtils.isEmpty(netid)) {
                    HintDialog.ShowHintDialog(NS_CBActivity.this, "频率及网络id不可为空。", "提示");
                    return;
                }
                HzyUtils.showProgressDialog1(NS_CBActivity.this, "抄表中......");

                //                freq = Integer.toHexString(Integer.valueOf(freq));
                freq = Integer.toHexString(Integer.valueOf(freq + "0"));
                freq = HzyUtils.isLength(freq, 4);
                netid = HzyUtils.isLength(netid, 4);
                size = cjjList.size();//分采个数
                fcCount = 0;
                DelayTime = 10 + 10 * size;
                HzyUtils.prepareTimeStart(DelayTime * 10);
                String fcCount = HzyUtils.isLength(HzyUtils.toHexString(size + ""), 2);
                String fcMsg = "";
                for (int i = 0; i < size; i++) {
                    fcMsg = fcMsg + HzyUtils.isLength(cjjList.get(i).CjjId % 10000 + "", 2);
                }
                for (int i = 0; i < (20 - size); i++) {
                    fcMsg = fcMsg + "00";
                }
                Log.d("limbo", fcMsg);
                String sendMsg = "68"
                        + freq
                        + netid
                        + cjjList.get(0).CjjAddr.substring(0, 10)
                        + "402aa10140"
                        + fcCount
                        + fcMsg
                        + "000000000000005B5B5B3F3F7B7B7B01FE3F5D5D5D";
                MenuActivity.sendCmd(sendMsg);
                MenuActivity.btAuto = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String getMsg = "";

                            while (true) {
                                Thread.sleep(1000);
                                MenuActivity.Cjj_CB_MSG = MenuActivity.Cjj_CB_MSG.replaceAll("0x", "").replaceAll(" ", "");
                                MenuActivity.Cjj_CB_MSG = HzyUtils.toStringHex1(MenuActivity.Cjj_CB_MSG).replaceAll("�", "").replaceAll("\n", "");
                                getMsg = getMsg + MenuActivity.Cjj_CB_MSG;
                                getMsg = getMsg.replaceAll("�", "");
                                MenuActivity.Cjj_CB_MSG = "";

                                Log.d("limbo", getMsg);
                                if (getMsg.contains("AAAAAAAAAA") && getMsg.contains("FBFB") && getMsg.contains("7B7B7B")
                                        && getMsg.contains("5D5D5D")) {
                                    getMsg = getMsg.substring(getMsg.indexOf("AAAAAAAAAA"));
                                    Message message = new Message();
                                    message.what = 0x00;
                                    message.obj = getMsg;
                                    mHandler.sendMessage(message);
                                    getMsg = "";
                                }

                            }
                        } catch (InterruptedException e) {
                            HzyUtils.closeProgressDialog();
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

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
        meterList = NS_DataHelper.getZCMeters(xqid, zcjjid); //根据小区ID加载采集机信息列表
        for (int i = 0; i < meterList.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("meterid", "表ID:" + meterList.get(i).MeterNumber
                    + "\n保存时间:" + meterList.get(i).Date);
            map.put("data", "用户地址：" + meterList.get(i).UserAddr
                    + "\n表读数" + meterList.get(i).Data);
            listItem.add(map);
        }
        //		return listItem;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString();

                    if (getMsg.trim().length() != 0) {
                        fcCount++;
                        //                        Log.d("limbo", "读到数据:" + getMsg);
                        //                        tv_1.append("\n" + getMsg);
                        int startLocation = getMsg.indexOf("AAAAAAAAAA");
                        String fcid = getMsg.substring(startLocation + 24, startLocation + 26) + getMsg.substring(startLocation + 44, startLocation + 46);
                        String hx = getMsg.substring(startLocation + 26, startLocation + 28);//户型--表数
                        String data = getMsg.substring(startLocation + 48, startLocation + 48 + Integer.parseInt(hx) * 6);//表数据
                        tv_1.append("\n分采号" + fcid);
                        tv_1.append("\n户型" + hx);
                        tv_1.append("\n表数据" + data);
                        Log.d("limbo", "分采:" + fcid);
                        Log.d("limbo", "户型:" + hx);
                        Log.d("limbo", "表数据:" + data);
                        int meterCount = 0;
                        if (hx.equals("04")) {
                            meterCount = 16;
                        } else {
                            meterCount = Integer.parseInt(hx);
                        }
                        String date;
                        Calendar c = Calendar.getInstance();
                        Date d = c.getTime();
                        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
                        date = df.format(d);
                        String cjjid = zcjjid + fcid;
                        for (int i = 0; i < meterCount; i++) {
                            String meterData = data.substring(0 + 6 * i, 6 + 6 * i);
                            meterData = meterData.substring(0, 5) + "." + meterData.substring(5, 6);
                            NS_DataHelper.changeUser(xqid, Integer.parseInt(cjjid), "" + i, i, meterData, date);
                        }
                        if (fcCount ==size){
                            Toast.makeText(NS_CBActivity.this,"抄表完成",Toast.LENGTH_LONG).show();
                            HzyUtils.closeProgressDialog();
                        }


                    }

                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(NS_CBActivity.this, "未接收到数据", "提示");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        saveUser(NS_CBActivity_et_freq.getText().toString().trim(), NS_CBActivity_et_netid.getText().toString().trim());
        super.onDestroy();
    }

    /**
     * 使用SharePreferences保存用户信息
     *
     * @param x
     * @param y
     */
    public void saveUser(String x, String y) {

        SharedPreferences.Editor editor = getSharedPreferences("nscb", MODE_PRIVATE).edit();
        editor.putString("freq", x);
        editor.putString("netid", y);
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("nscb", MODE_PRIVATE);
        NS_CBActivity_et_freq.setText(pref.getString("freq", "4935"));
        NS_CBActivity_et_netid.setText(pref.getString("netid", "0001"));
    }
}
