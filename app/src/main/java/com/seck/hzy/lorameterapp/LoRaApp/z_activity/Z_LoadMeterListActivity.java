package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.Z_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.Z_MeterUser;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/9/25.
 */

public class Z_LoadMeterListActivity extends Activity {
    @BindView(R.id.UserActivity_lv_userList)
    ListView UserActivity_lv_userList;
    private int xqid, cjjid;
    private List<Z_MeterUser> meterList;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
    private String from[] = new String[]{"meterid", "data"};
    private int to[] = new int[]{R.id.meterId, R.id.data};
    private SimpleAdapter listItemAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init(){
        setContentView(R.layout.lora_activity_user);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        meterList = Z_DataHelper.getMeters(xqid, cjjid);//从数据库中获取列表

        UserActivity_lv_userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Z_LoadMeterListActivity.this, Z_LoadMeterMsgActivity.class);
                i.putExtra("flag", 1);
                i.putExtra("xqid", meterList.get(position).XqId);
                i.putExtra("cjjid", meterList.get(position).CjjId);
                i.putExtra("useraddr", meterList.get(position).UserAddr);
                i.putExtra("date", meterList.get(position).Date);
                i.putExtra("meterid", meterList.get(position).MeterId);
                i.putExtra("meternumber", meterList.get(position).MeterNumber);
                startActivityForResult(i, 0);
            }
        });

        UserActivity_lv_userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Z_LoadMeterListActivity.this);
                dialog.setTitle("是否删除该数据");
                dialog.setCancelable(false);
                String num = meterList.get(position).MeterId + "";
                while (num.length() < 8) {
                    num = "0" + num;
                }
                dialog.setMessage("用户地址:" + meterList.get(position).UserAddr + "\n"
                        + "\n小区ID:" + meterList.get(position).XqId
                        + "\n\n水表ID:" + meterList.get(position).MeterNumber
                        + "\n保存时间:" + meterList.get(position).Date);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Z_DataHelper.deleteUser(meterList.get(position).XqId, meterList.get(position).CjjId, meterList.get(position).MeterId);
                        getList();
                        listItemAdapter.notifyDataSetChanged();
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
        listItemAdapter = new SimpleAdapter(this, listItem, R.layout.lora_list_item, from, to);
        listItemAdapter.setViewBinder(new ListViewBinder());
        UserActivity_lv_userList.setAdapter(listItemAdapter);
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
        meterList = Z_DataHelper.getMeters(xqid, cjjid);
        for (int i = 0; i < meterList.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("meterid", "表ID:" + meterList.get(i).MeterNumber
                    + "\n序列号:" + meterList.get(i).MeterId
                    + "\n保存时间:" + meterList.get(i).Date);
            map.put("data", "用户地址：" + meterList.get(i).UserAddr);
            listItem.add(map);
        }
        //		return listItem;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
//            Bundle bundle = data.getExtras();
//            String waterValue = bundle.getString("result");
            HintDialog.ShowHintDialog(Z_LoadMeterListActivity.this, "数据保存成功", "提示");
            Log.d("limbo", "result == ok");
            getList();
            listItemAdapter.notifyDataSetChanged();
            Toast.makeText(Z_LoadMeterListActivity.this, "数据保存成功" , Toast.LENGTH_LONG).show();
        } else  {
            Toast.makeText(Z_LoadMeterListActivity.this, "数据保存失败", Toast.LENGTH_LONG).show();
        }
    }
}
