package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.model.Pcjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.P_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

public class P_CjjActivity extends Activity {
    public static int xqid;
    private boolean isRead;
    public static List<Pcjj> cjjList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_cjj);
//        setupActionBar();
        Bundle bundle = getIntent().getExtras();

        xqid = bundle.getInt("xqid");
        isRead = bundle.getBoolean("isRead");

        init();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "返回主界面");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //				NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case 1:
                Intent intent = new Intent(P_CjjActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                P_CjjActivity.this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<String> getCjj() {
        List<String> data = new ArrayList<String>();
        for (Pcjj cjj : cjjList) {
            data.add(cjj.CjjId + "(" + cjj.CjjAddr + ")");
        }
        return data;
    }

    private void init() {
        SharedPreferences pref = getSharedPreferences("isCheck", MODE_PRIVATE);
        if (pref.getBoolean("isCheck", false)) {
            setTitle("单元盒列表");
        } else {
            setTitle("单元盒列表");
        }
        cjjList = P_DataHelper.getPcjj(xqid); //根据小区ID加载采集机信息列表
        listView = (ListView) findViewById(R.id.cjjlistView);
        ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getCjj());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(P_CjjActivity.this, P_MeterActivity.class);
                intent.putExtra("xqid", cjjList.get(position).XqId);
                intent.putExtra("cjjid", cjjList.get(position).CjjId);
                intent.putExtra("isRead", isRead);
                startActivity(intent);
            }
        });
    }

}
