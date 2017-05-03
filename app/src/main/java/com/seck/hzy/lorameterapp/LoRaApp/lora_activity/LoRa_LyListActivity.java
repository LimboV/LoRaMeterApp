package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_Cjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limbo on 2016/11/28.
 */
public class LoRa_LyListActivity extends Activity {
    private ListView lvLy;
    private int xqid;
    private Button btnLoad;
    public static List<LoRa_Cjj> cjjList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_ly_list);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        lvLy = (ListView) findViewById(R.id.LyListActivity_lv_lyList);
        cjjList= LoRa_DataHelper.getCjj(xqid); //根据小区ID加载采集机信息列表
        ListAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, getCjj());
        lvLy.setAdapter(adapter);
        lvLy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LoRa_LyListActivity.this, LoRa_UserActivity.class);
                intent.putExtra("xqid", cjjList.get(position).XqId);
                intent.putExtra("cjjid", cjjList.get(position).CjjId);
                startActivity(intent);
            }
        });
        btnLoad = (Button) findViewById(R.id.LyListActivity_btn_loadMsgToCjj);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_LyListActivity.this, LoRa_LoadCjjMsgActivity.class);
                i.putExtra("xqid", xqid);
                startActivity(i);
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
