package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_MeterUser;
import com.seck.hzy.lorameterapp.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/11/14.
 */

public class LoRa_CbMeterListActivity extends Activity {
    @BindView(R.id.UserActivity_lv_userList)
    ListView UserActivity_lv_userList;
    @BindView(R.id.UserActivity_btn_cb)
    Button UserActivity_btn_cb;
    private int xqid, cjjid;
    private List<LoRa_MeterUser> meterList;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
    private String from[] = new String[]{"meterid", "data"};
    private int to[] = new int[]{R.id.meterId, R.id.data};
    private SimpleAdapter listItemAdapter;

    boolean BreakFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_user);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        meterList = LoRa_DataHelper.getMeters(xqid, cjjid);//从数据库中获取列表
        UserActivity_lv_userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
        UserActivity_btn_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int count = meterList.size();
                HzyUtils.showProgressDialog2(LoRa_CbMeterListActivity.this, count, "正在抄表......");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i < count && !BreakFlag; i++) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date curDateTime = new Date(System.currentTimeMillis());
                                String time = "抄表时间:" + sdf.format(curDateTime);
                                int meterNumber = meterList.get(i).MeterId;//表序号
                                String meterid = meterList.get(i).MeterNumber + "";//表ID

                                meterMsg meterMsg = new meterMsg();
                                meterMsg.xqid = xqid;
                                meterMsg.cjjid = cjjid;
                                meterMsg.meterid = meterNumber;
                                meterMsg.meternum = meterid;
                                meterMsg.time = time;

                                String sendMsg;
                                Thread.sleep(500);

                                Message message = new Message();
                                message.what = 0x00;
                                message.arg1 = i;
                                message.obj = meterMsg;
                                mHandler.sendMessage(message);


                            }
                            Message message = new Message();
                            message.what = 0x01;
                            message.arg2 = 0;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            Message message = new Message();
                            message.what = 0x01;
                            message.arg2 = -1;
                            mHandler.sendMessage(message);
                        }
                    }
                }).start();

            }
        });
        getList();
        listItemAdapter = new SimpleAdapter(this, listItem, R.layout.lora_list_item, from, to);
        listItemAdapter.setViewBinder(new ListViewBinder());
        UserActivity_lv_userList.setAdapter(listItemAdapter);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    meterMsg meterMsg = (LoRa_CbMeterListActivity.meterMsg) msg.obj;
                    int i = msg.arg1;


                    LoRa_DataHelper.changeUser(meterMsg.xqid, meterMsg.cjjid,
                            meterMsg.meternum, meterMsg.meterid, meterMsg.time);

                    HzyUtils.progressDialog.setProgress(i + 1);//更新进度条
                    listItemAdapter.notifyDataSetChanged();
                    UserActivity_lv_userList.setSelection(i);
                    break;
                case 0x01:
                    int flag2 = msg.arg2;
                    if (flag2 == 0) {
                        HintDialog.ShowHintDialog(LoRa_CbMeterListActivity.this, "成功", "提示");
                        HzyUtils.closeProgressDialog();
                    } else {
                        HintDialog.ShowHintDialog(LoRa_CbMeterListActivity.this, "失败", "提示");
                        HzyUtils.closeProgressDialog();
                    }

                    break;
                default:
                    break;
            }
        }
    };

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
        meterList = LoRa_DataHelper.getMeters(xqid, cjjid);
        for (int i = 0; i < meterList.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("meterid", "表ID:" + meterList.get(i).MeterNumber
                    + "\n序列号:" + meterList.get(i).MeterId
                    + "\n表读数:" + meterList.get(i).Data
                    + "\n保存时间:" + meterList.get(i).Date);
            map.put("data", "用户地址：" + meterList.get(i).UserAddr);
            listItem.add(map);
        }
    }

    class meterMsg {
        int xqid;
        int cjjid;
        int meterid;
        String meternum;
        String time;
    }
}
