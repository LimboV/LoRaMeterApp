package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.model.LoRaFc;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/8/2.
 */

public class LoRa_AddFcMsgActivity extends Activity {
    @BindView(R.id.LoRaAddFcMsg_btn_addFc)
    Button LoRaAddFcMsg_btn_addFc;
    @BindView(R.id.LoRaAddFcMsg_lv_fcList)
    ListView LoRaAddFcMsg_lv_fcList;

    private int xqid, cjjid, fcid;
    private List<LoRaFc> fcList;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
    private int to[] = new int[]{R.id.loraFcName, R.id.loraFcId, R.id.loraFcFreq, R.id.loraFcNum};
    private String from[] = new String[]{"FcName", "FcId", "FcFreq", "FcNum"};
    private SimpleAdapter listItemAdapter;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_addfcmsg);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        fcList = LoRa_DataHelper.getFc(xqid, cjjid);//从数据库中获取列表



        LoRaAddFcMsg_btn_addFc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_AddFcMsgActivity.this,LoRa_AddFcParamMsgActivity.class);
                i.putExtra("xqid", xqid);
                i.putExtra("cjjid", cjjid);
                startActivity(i);
            }
        });


        LoRaAddFcMsg_lv_fcList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoRa_AddFcMsgActivity.this);
                dialog.setTitle("请确认是否删除此分采信息。");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fcid = fcList.get(position).FcId;
                        LoRa_DataHelper.deleteFc(xqid,cjjid,fcid);
                        Toast.makeText(LoRa_AddFcMsgActivity.this,"分采信息已删除",Toast.LENGTH_LONG).show();

                        getList();
                        listItemAdapter.setViewBinder(new ListViewBinder());
                        LoRaAddFcMsg_lv_fcList.setAdapter(listItemAdapter);
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

        getList();
        listItemAdapter = new SimpleAdapter(this, listItem, R.layout.item_fclistmsg, from, to);
        listItemAdapter.setViewBinder(new ListViewBinder());
        LoRaAddFcMsg_lv_fcList.setAdapter(listItemAdapter);
    }

    private class ListViewBinder implements SimpleAdapter.ViewBinder {
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if ((view instanceof ImageView) && (data instanceof Bitmap)) {
                ImageView imageView = (ImageView) view;
                Bitmap bmp = (Bitmap) data;
                imageView.setImageBitmap(bmp);
                return true;
            }
            return false;
        }

    }

    public void getList() {
        listItem.clear();
        fcList = LoRa_DataHelper.getFc(xqid, cjjid);
        for (int i = 0; i < fcList.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("FcName","分采名:"+fcList.get(i).getFcName());
            map.put("FcId", "分采ID：" + fcList.get(i).FcId);
            map.put("FcFreq", "分采频率：" + fcList.get(i).FcId);
            map.put("FcNum", "分采序号：" + fcList.get(i).FcId);
            listItem.add(map);
        }
    }
}
