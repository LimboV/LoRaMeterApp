package com.seck.hzy.lorameterapp.LoRaApp.ns_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.model.NS_Cjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NS_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/11/23.
 */

public class NS_CjjListActivity extends Activity {
    @BindView(R.id.LyListActivity_btn_loadMsgToCjj)
    Button LyListActivity_btn_loadMsgToCjj;
    @BindView(R.id.LyListActivity_lv_lyList)
    ListView LyListActivity_lv_lyList;
    private int xqid, cjjid;
    public static List<NS_Cjj> cjjList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lora_activity_ly_list);
        ButterKnife.bind(this);
        LyListActivity_btn_loadMsgToCjj.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid") / 10000;
        cjjList = NS_DataHelper.getCjj(xqid); //根据小区ID加载采集机信息列表
        ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getCjj());
        LyListActivity_lv_lyList.setAdapter(adapter);
        LyListActivity_lv_lyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NS_CjjListActivity.this, NS_MeterListActivity.class);
                intent.putExtra("xqid", cjjList.get(position).XqId);
                intent.putExtra("cjjid", cjjList.get(position).CjjId);
                intent.putExtra("LoRaNum", cjjList.get(position).CjjAddr.substring(0, 10));
                startActivity(intent);
            }
        });
    }

    private List<String> getCjj() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < cjjList.size(); i++) {
            Log.d("limbo", "1--" + cjjList.get(i).CjjId + "");
            if (cjjList.get(i).CjjId / 10000 != cjjid) {
                Log.d("limbo", "删除" + cjjList.get(i).CjjId + "");
                cjjList.remove(i);
                i--;
            }
        }
        for (NS_Cjj cjj : cjjList) {
            data.add("分采号：" + cjj.CjjId % 10000 + "\n模块序列号：" + cjj.CjjAddr.substring(0, 10)
                    + "\n分采地址：" + cjj.CjjAddr.substring(10));
        }
        return data;
    }
}

