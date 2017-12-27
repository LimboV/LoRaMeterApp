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

import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_Cjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2016/11/28.
 */
public class LoRa_CjjListActivity extends Activity {
    @BindView(R.id.LyListActivity_lv_lyList)
    ListView LyListActivity_lv_lyList;
    @BindView(R.id.LyListActivity_btn_loadMsgToCjj)
    Button LyListActivity_btn_loadMsgToCjj;
    private int xqid;
    public static List<LoRa_Cjj> cjjList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_ly_list);
        AndPermission.with(this)
                .requestCode(300)
                .permission(Permission.STORAGE)
                .callback(this)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(LoRa_CjjListActivity.this, rationale).show();
                    }
                })
                .start();
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjList= LoRa_DataHelper.getCjj(xqid); //根据小区ID加载采集机信息列表
        ListAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, getCjj());
        LyListActivity_lv_lyList.setAdapter(adapter);
        LyListActivity_lv_lyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LoRa_CjjListActivity.this, LoRa_UserActivity.class);
                intent.putExtra("xqid", cjjList.get(position).XqId);
                intent.putExtra("cjjid", cjjList.get(position).CjjId);
                startActivity(intent);
            }
        });
        LyListActivity_btn_loadMsgToCjj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_CjjListActivity.this, LoRa_LoadCjjMsgActivity.class);
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
