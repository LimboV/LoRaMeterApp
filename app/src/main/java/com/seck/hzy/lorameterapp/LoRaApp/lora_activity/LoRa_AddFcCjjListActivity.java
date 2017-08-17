package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_Cjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/8/2.
 */

public class LoRa_AddFcCjjListActivity extends Activity {
    @BindView(R.id.LoRa_AddFcCjjListActivity_lv_cjjList)
    ListView LoRa_AddFcCjjListActivity_lv_cjjList;

    private int xqid;
    public static List<LoRa_Cjj> cjjList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init(){
        setContentView(R.layout.lora_activity_addfccjjlist);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjList= LoRa_DataHelper.getCjj(xqid); //根据小区ID加载采集机信息列表
        ListAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, getCjj());
        LoRa_AddFcCjjListActivity_lv_cjjList.setAdapter(adapter);
        LoRa_AddFcCjjListActivity_lv_cjjList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LoRa_AddFcCjjListActivity.this, LoRa_AddFcMsgActivity.class);
                intent.putExtra("xqid", cjjList.get(position).XqId);
                intent.putExtra("cjjid", cjjList.get(position).CjjId);
                startActivity(intent);
            }
        });

    }

    private List<String> getCjj() {
        List<String> data = new ArrayList<String>();
        for (LoRa_Cjj cjj : cjjList) {
            data.add(cjj.CjjId + "(" + cjj.CjjAddr + ")");
        }
        return data;
    }
}
