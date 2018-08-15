package com.seck.hzy.lorameterapp.LoRaApp.ns_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class NS_ZCjjListActivity extends Activity {
    @BindView(R.id.ZCjjListActivity_lv_lyList)
    ListView ZCjjListActivity_lv_lyList;
    private int xqid;
    public static List<NS_Cjj> cjjList;
    public static List<NS_Cjj> ZcjjList;//用以存放总采集信息

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ns_activity_zcjjlist);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjList = NS_DataHelper.getCjj(xqid); //根据小区ID加载采集机信息列表
        ZcjjList = NS_DataHelper.getCjj(xqid); //根据小区ID加载采集机信息列表
        Log.d("limbo",""+cjjList.size());
        Log.d("limbo",""+ZcjjList.size());
        ListAdapter adapter = null;
        try {
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getZCjj());
        } catch (Exception e) {
            Toast.makeText(NS_ZCjjListActivity.this, "蓝牙未连接或数据库文件数据格式有误!", Toast.LENGTH_LONG).show();
            Log.d("limbo", e.toString());
            finish();
        }
        ZCjjListActivity_lv_lyList.setAdapter(adapter);
        ZCjjListActivity_lv_lyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NS_ZCjjListActivity.this, NS_CjjListActivity.class);
                intent.putExtra("xqid", ZcjjList.get(position).XqId);
                intent.putExtra("cjjid", ZcjjList.get(position).CjjId);
                startActivity(intent);
            }
        });
        ZCjjListActivity_lv_lyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(NS_ZCjjListActivity.this);
                dialog.setTitle("是否抄读该总采下水表？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(NS_ZCjjListActivity.this, NS_CBActivity.class);
                        intent.putExtra("xqid", ZcjjList.get(position).XqId);
                        intent.putExtra("cjjid", ZcjjList.get(position).CjjId);
                        startActivity(intent);

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

    /**
     * 筛取总采
     */
    private List<String> getZCjj() {
        List<String> data = new ArrayList<>();
        int flag = 0;
        if (ZcjjList.size()>0){
            ZcjjList.clear();
        }

        Log.d("limbo", cjjList.size() + "");
        for (int i = 0; i < cjjList.size(); i++) {
            flag = 0;
            if (ZcjjList.size() == 0) {
                ZcjjList.add(cjjList.get(i));
            } else {
                for (int j = 0; j < ZcjjList.size(); j++) {
                    if (ZcjjList.get(j).CjjId / 10000 == cjjList.get(i).CjjId / 10000) {
                    } else {
                        flag++;
                    }
                }
                if (flag == ZcjjList.size()) {
                    ZcjjList.add(cjjList.get(i));
                }

            }
        }

        Log.d("limbo", ZcjjList.size() + "");

        for (NS_Cjj cjj : ZcjjList) {
            data.add("lora总采号：" + cjj.CjjId / 10000
                    + "\nLoRa模块地址：" + cjj.CjjAddr.substring(10));
        }
        return data;
    }

}
