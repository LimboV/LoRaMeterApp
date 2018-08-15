package com.seck.hzy.lorameterapp.LoRaApp.ns_activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.seck.hzy.lorameterapp.LoRaApp.model.NS_MeterUser;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NS_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/11/23.
 */

public class NS_MeterListActivity extends Activity {
    @BindView(R.id.UserActivity_lv_userList)
    ListView UserActivity_lv_userList;
    @BindView(R.id.UserActivity_btn_cb)
    Button UserActivity_btn_cb;
    private int xqid, cjjid;
    private String LoRaNum;
    private List<NS_MeterUser> meterList;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private String from[] = new String[]{"meterid", "data"};
    private int to[] = new int[]{R.id.meterId, R.id.data};
    private SimpleAdapter listItemAdapter;
    private int position_selection = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lora_activity_user);
        ButterKnife.bind(this);
        UserActivity_btn_cb.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        LoRaNum = bundle.getString("LoRaNum");
        meterList = NS_DataHelper.getMeters(xqid, cjjid);//从数据库中获取列表
        UserActivity_lv_userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getList();
        listItemAdapter = new SimpleAdapter(this, listItem, R.layout.lora_list_item, from, to);
        listItemAdapter.setViewBinder(new ListViewBinder());
        UserActivity_lv_userList.setAdapter(listItemAdapter);


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
        meterList = NS_DataHelper.getMeters(xqid, cjjid);
        for (int i = 0; i < meterList.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("meterid", "表ID:" + meterList.get(i).MeterNumber
                    + "\n保存时间:" + meterList.get(i).Date
                    + "\n抄表数据:" + meterList.get(i).Data
            );
            map.put("data", "用户地址：" + meterList.get(i).UserAddr);
            listItem.add(map);
        }
        //		return listItem;
    }
}
