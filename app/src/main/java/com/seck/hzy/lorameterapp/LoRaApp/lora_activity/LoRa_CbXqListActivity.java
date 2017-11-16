package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_Cjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/11/14.
 */

public class LoRa_CbXqListActivity extends Activity {

    @BindView(R.id.XqListActivity_btn_addXq)
    Button XqListActivity_btn_addXq;
    @BindView(R.id.XqListActivity_btn_addMeter)
    Button XqListActivity_btn_addMeter;
    @BindView(R.id.XqListActivity_lv_xq)
    ListView XqListActivity_lv_xq;//小区列表存放位置
    public List<LoRa_Cjj> xqList;//小区列表
    private ListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_xq_list);
        ButterKnife.bind(this);
        XqListActivity_btn_addXq.setVisibility(View.GONE);
        XqListActivity_btn_addMeter.setVisibility(View.GONE);

        LoRa_DataHelper.getdb();//获取数据库实例
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getXq()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        XqListActivity_lv_xq.setAdapter(adapter);
        XqListActivity_lv_xq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LoRa_CbXqListActivity.this, LoRa_CbCjjListActivity.class);
                intent.putExtra("xqid", xqList.get(position).XqId);
                startActivity(intent);

            }
        });
    }

    private List<String> getXq() {
        List<String> data = new ArrayList<>();
        try {
            xqList = LoRa_DataHelper.getXq();//从数据库提取出
            for (LoRa_Cjj cjj : xqList) {
                data.add(cjj.XqName + "(小区号:" + cjj.XqId + ")");
            }
        } catch (Exception e) {
            Log.e("limbo", e.toString());
            HintDialog.ShowHintDialog(this, "未找到数据,请检查数据库文件是否存在！", "错误");
        }
        return data;
    }

}
