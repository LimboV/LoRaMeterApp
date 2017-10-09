package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.model.Pcjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.P_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/9/18.
 */

public class P_LoadMeterIdCjjListActivity extends Activity {
    @BindView(R.id.LyListActivity_lv_lyList)
    ListView LyListActivity_lv_lyList;
    @BindView(R.id.LyListActivity_btn_loadMsgToCjj)
    Button LyListActivity_btn_loadMsgToCjj;
    private int xqid;
    public static List<Pcjj> cjjList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_ly_list);
        ButterKnife.bind(this);
        LyListActivity_btn_loadMsgToCjj.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjList = P_DataHelper.getPcjj(xqid); //根据小区ID加载采集机信息列表
        ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getCjj());
        LyListActivity_lv_lyList.setAdapter(adapter);
        LyListActivity_lv_lyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(P_LoadMeterIdCjjListActivity.this, P_LoadMeterIdMeterListActivity.class);
                intent.putExtra("xqid", cjjList.get(position).XqId);
                intent.putExtra("cjjid", cjjList.get(position).CjjId);
                startActivity(intent);
            }
        });
    }

    private List<String> getCjj() {
        List<String> data = new ArrayList<>();
        for (Pcjj cjj : cjjList) {
            String cjjid = (cjj.CjjId + "").replaceAll(" ", "");
            while (cjjid.length() < 6) {
                cjjid = "0" + cjjid;
            }
            int x = Integer.parseInt(cjjid.substring(0, 2));
            int y = Integer.parseInt(cjjid.substring(4, 6));
            data.add("通讯机:" + x + "   端口:" + y + "(" + cjj.CjjAddr + ")");
        }
        return data;
    }
}
