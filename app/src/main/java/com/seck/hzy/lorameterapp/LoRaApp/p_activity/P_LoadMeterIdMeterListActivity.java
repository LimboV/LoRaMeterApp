package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.seck.hzy.lorameterapp.LoRaApp.utils.P_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.model.PmeterUser;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/9/18.
 */

public class P_LoadMeterIdMeterListActivity extends Activity{
    @BindView(R.id.P_LoadMeterIdMeterListActivity_lv_MeterList)
    ListView P_LoadMeterIdMeterListActivity_lv_MeterList;
    @BindView(R.id.P_LoadMeterIdMeterListActivity_btn_AddMeter)
    Button P_LoadMeterIdMeterListActivity_btn_AddMeter;
    @BindView(R.id.P_LoadMeterIdMeterListActivity_btn_DataLoad)
    Button P_LoadMeterIdMeterListActivity_btn_DataLoad;
    private List<PmeterUser> meterList;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
    private String from[] = new String[]{"meterid", "data"};
    private int to[] = new int[]{R.id.meterId, R.id.data};
    private SimpleAdapter listItemAdapter;
    private int xqid, cjjid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init(){
        setContentView(R.layout.p_activity_loadmeteridmeterlist);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        meterList = P_DataHelper.getMeters(xqid, cjjid);//从数据库中获取列表

        P_LoadMeterIdMeterListActivity_lv_MeterList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(P_LoadMeterIdMeterListActivity.this);
                dialog.setTitle("是否删除该数据");
                dialog.setCancelable(false);
                dialog.setMessage("用户地址:" + meterList.get(position).UserAddr + "\n"
                        + "\n小区ID:" + meterList.get(position).XqId
                        + "\n采集机ID:" + meterList.get(position).CjjId
                        + "\n用户名:" + meterList.get(position).UserName
                        + "\n\n水表ID:" + meterList.get(position).MeterNumber
                        + "\n保存时间:" + meterList.get(position).Date);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        P_DataHelper.deleteUser(meterList.get(position).XqId, meterList.get(position).CjjId, meterList.get(position).MeterId);
                        getList();
                        listItemAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                return false;
            }
        });
        P_LoadMeterIdMeterListActivity_btn_AddMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getList();
        listItemAdapter = new SimpleAdapter(this, listItem, R.layout.lora_list_item, from, to);
        listItemAdapter.setViewBinder(new ListViewBinder());
        P_LoadMeterIdMeterListActivity_lv_MeterList.setAdapter(listItemAdapter);



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
        meterList = P_DataHelper.getMeters(xqid, cjjid);
        for (int i = 0; i < meterList.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("meterid", "表ID:" + meterList.get(i).MeterNumber
                    + "\n序列号:" + meterList.get(i).MeterId
                    + "\n保存时间:" + meterList.get(i).Date);
            map.put("data", "用户地址：" + meterList.get(i).UserAddr);
            listItem.add(map);
        }
    }
}
