package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.model.Pcjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.P_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/9/18.
 */

public class P_LoadMeterIdXqListActivity extends Activity {
    @BindView(R.id.P_LoadMeterIdXqListActivity_lv_xqList)
    ListView P_LoadMeterIdXqListActivity_lv_xqList;
    @BindView(R.id.P_LoadMeterIdXqListActivity_btn_AddXq)
    Button P_LoadMeterIdXqListActivity_btn_AddXq;
    public List<Pcjj> xqList;//小区列表
    private ListAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.p_activity_loadmeteridxqlist);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        P_DataHelper.getdb();//获取P型表数据库实例
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getXq()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        P_LoadMeterIdXqListActivity_lv_xqList.setAdapter(adapter);
        P_LoadMeterIdXqListActivity_lv_xqList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(P_LoadMeterIdXqListActivity.this, P_LoadMeterIdCjjListActivity.class);
                intent.putExtra("xqid", xqList.get(position).XqId);
                startActivity(intent);
            }
        });
        P_LoadMeterIdXqListActivity_btn_AddXq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P_LoadMeterIdXqListActivity.this, P_LoadMeterIdAddXqActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<String> getXq() {
        List<String> data = new ArrayList<>();
        try {
            xqList = P_DataHelper.getXq();//从数据库提取出
            for (Pcjj cjj : xqList) {
                data.add(cjj.XqName + "(" + cjj.XqId + ")");
            }
        } catch (Exception e) {
            HintDialog.ShowHintDialog(this, "未找到数据,请检查数据库文件是否存在！", "错误");
        }
        return data;
    }
}
