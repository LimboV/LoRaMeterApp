package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_MeterUser;
import com.seck.hzy.lorameterapp.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2016/11/29.
 */
public class LoRa_UserActivity extends Activity {
    @BindView(R.id.UserActivity_lv_userList)
    ListView UserActivity_lv_userList;
    private int xqid, cjjid;
    private List<LoRa_MeterUser> meterList;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private String from[] = new String[]{"meterid", "data"};
    private int to[] = new int[]{R.id.meterId, R.id.data};
    private SimpleAdapter listItemAdapter;
    private int position_selection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_user);
        AndPermission.with(this)
                .requestCode(300)
                .permission(Permission.STORAGE)
                .callback(this)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(LoRa_UserActivity.this, rationale).show();
                    }
                })
                .start();
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        meterList = LoRa_DataHelper.getMeters(xqid, cjjid);//从数据库中获取列表

        UserActivity_lv_userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(LoRa_UserActivity.this, LoRa_ChangeUserMsg.class);
                i.putExtra("xqid", meterList.get(position).XqId);
                i.putExtra("cjjid", meterList.get(position).CjjId);
                i.putExtra("useraddr", meterList.get(position).UserAddr);
                i.putExtra("date", meterList.get(position).Date);
                i.putExtra("meterid", meterList.get(position).MeterId);
                i.putExtra("meternumber", meterList.get(position).MeterNumber);
                i.putExtra("freq", meterList.get(position).UserName);
                i.putExtra("flag", 1);
                startActivityForResult(i, 0);
            }
        });
        UserActivity_lv_userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoRa_UserActivity.this);
                dialog.setTitle("是否删除该数据");
                dialog.setCancelable(false);
                String num = meterList.get(position).MeterId + "";
                while (num.length() < 8) {
                    num = "0" + num;
                }
                dialog.setMessage("用户地址:" + meterList.get(position).UserAddr + "\n"
                        + "\nLoRa网络ID:" + meterList.get(position).XqId
                        + "\nLoRa网络频率:" + "495000Hz"
                        + "\n\n水表ID:" + meterList.get(position).MeterNumber
                        + "\n模块序列号:" + num
                        + "\n保存时间:" + meterList.get(position).Date);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoRa_DataHelper.deleteUser(meterList.get(position).XqId, meterList.get(position).CjjId, meterList.get(position).MeterId);
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
        meterList = LoRa_DataHelper.getMeters(xqid, cjjid);
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
            Bundle bundle = data.getExtras();
            String waterValue = bundle.getString("result");
            HintDialog.ShowHintDialog(LoRa_UserActivity.this, "数据保存成功，网络修改成功！\n表数值:" + waterValue, "提示");
            Log.d("limbo", "result == ok");
            getList();
            listItemAdapter.notifyDataSetChanged();
            Toast.makeText(LoRa_UserActivity.this, "数据保存成功，网络ID修改成功!\n表数值:" + waterValue, Toast.LENGTH_LONG).show();
        } else if (resultCode == 98) {
            getList();
            listItemAdapter.notifyDataSetChanged();
            Toast.makeText(LoRa_UserActivity.this, "数据保存成功", Toast.LENGTH_LONG).show();
        } else if (resultCode == 97) {
            getList();
            listItemAdapter.notifyDataSetChanged();
            Toast.makeText(LoRa_UserActivity.this, "数据修改成功", Toast.LENGTH_LONG).show();
        }
    }
}
