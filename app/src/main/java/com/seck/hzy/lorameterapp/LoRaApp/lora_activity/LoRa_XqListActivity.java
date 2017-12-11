package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_Cjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2016/11/28.
 */
public class LoRa_XqListActivity extends Activity {
    @BindView(R.id.XqListActivity_lv_xq)
    ListView XqListActivity_lv_xq;//小区列表存放位置
    @BindView(R.id.XqListActivity_btn_addMeter)
    Button XqListActivity_btn_addMeter;//
    @BindView(R.id.XqListActivity_btn_addXq)
    Button XqListActivity_btn_addXq;//
    public List<LoRa_Cjj> xqList;//小区列表
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_xq_list);
        ButterKnife.bind(this);
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
                Intent intent = new Intent(LoRa_XqListActivity.this, LoRa_CjjListActivity.class);
                intent.putExtra("xqid", xqList.get(position).XqId);
                startActivity(intent);
            }
        });
        XqListActivity_lv_xq.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoRa_XqListActivity.this);
                dialog.setTitle("是否删除该小区所有信息");
                dialog.setCancelable(false);
                final int xqid = xqList.get(position).XqId;
                String xqname = xqList.get(position).XqName;
                dialog.setMessage("网络ID:" + xqid
                        + "小区名:" + xqname
                );
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoRa_DataHelper.deleteXqAndMeter(xqid);
                        getXq();
                        adapter = new ArrayAdapter(LoRa_XqListActivity.this, android.R.layout.simple_list_item_1, getXq());
                        XqListActivity_lv_xq.setAdapter(adapter);
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
        XqListActivity_btn_addMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_XqListActivity.this, LoRa_UserMsgLoadActivity.class);
                i.putExtra("flag", 2);
                startActivityForResult(i, 0);
            }
        });
        XqListActivity_btn_addXq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_XqListActivity.this, LoRa_AddXqMsgToDbActivity.class);
                startActivityForResult(i, 0);
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
        } catch (Exception e) {
            Log.e("limbo", e.toString());
            HintDialog.ShowHintDialog(this, "未找到数据,请检查数据库文件是否存在！", "错误");
            finish();
        }
        return data;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String waterValue = bundle.getString("result");
            HintDialog.ShowHintDialog(LoRa_XqListActivity.this, "数据保存成功，网络ID修改成功!\n" + waterValue, "提示");
            Log.d("limbo", "result == ok");
            Toast.makeText(LoRa_XqListActivity.this, "数据保存成功，网络ID修改成功!\n" + waterValue, Toast.LENGTH_LONG).show();
        } else if (resultCode == 99) {
            getXq();
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getXq()) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    return super.getView(position, convertView, parent);
                }
            };
            XqListActivity_lv_xq.setAdapter(adapter);
            Toast.makeText(LoRa_XqListActivity.this, "小区创建成功", Toast.LENGTH_LONG).show();
        } else if (resultCode == 98) {
            Toast.makeText(LoRa_XqListActivity.this, "数据保存成功", Toast.LENGTH_LONG).show();
        } else {
            //            Toast.makeText(XqListActivity.this, "发生错误", Toast.LENGTH_LONG).show();
        }
    }
}
