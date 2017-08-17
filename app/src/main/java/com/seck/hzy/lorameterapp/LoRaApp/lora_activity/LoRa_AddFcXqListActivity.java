package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
 * Created by limbo on 2017/8/1.
 */

public class LoRa_AddFcXqListActivity extends Activity{

    @BindView(R.id.LoRaAddFcXqListActivity_lv_xqList)
    ListView Lv_LoRaAddFcXqListActivity;


    public List<LoRa_Cjj> xqList;//小区列表
    private ListAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init(){
        setContentView(R.layout.loraaddfcxqlistactivity);
        ButterKnife.bind(this);
        LoRa_DataHelper.getdb();//获取数据库实例
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, getXq()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        Lv_LoRaAddFcXqListActivity.setAdapter(adapter);
        Lv_LoRaAddFcXqListActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LoRa_AddFcXqListActivity.this, LoRa_AddFcCjjListActivity.class);
                intent.putExtra("xqid", xqList.get(position).XqId);
                startActivity(intent);
            }
        });
        Lv_LoRaAddFcXqListActivity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoRa_AddFcXqListActivity.this);
                dialog.setTitle("是否删除该小区所有信息");
                dialog.setCancelable(false);
                final int xqid = xqList.get(position).XqId;
                String xqname = xqList.get(position).XqName;
                dialog.setMessage("网络ID:" + xqid
                        + "小区名:" +xqname
                );
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoRa_DataHelper.deleteXqAndMeter(xqid);
                        getXq();
                        adapter = new ArrayAdapter(LoRa_AddFcXqListActivity.this,android.R.layout.simple_list_item_1, getXq());
                        Lv_LoRaAddFcXqListActivity.setAdapter(adapter);
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
    }

    private List<String> getXq() {
        List<String> data = new ArrayList<>();
        try {
            xqList = LoRa_DataHelper.getXq();//从数据库提取出
            for (LoRa_Cjj cjj : xqList) {
                data.add(cjj.XqName + "(" + cjj.XqId + ")");
            }
        }catch (Exception e){
            Log.e("limbo",e.toString());
            HintDialog.ShowHintDialog(this, "未找到数据,请检查数据库文件是否存在！", "错误");
        }
        return data;
    }
}
