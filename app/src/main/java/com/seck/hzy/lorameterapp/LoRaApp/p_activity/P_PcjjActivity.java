package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.model.Pcjj;
import com.seck.hzy.lorameterapp.LoRaApp.model.PdataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssHss on 2016/6/1.
 */
public class P_PcjjActivity extends Activity {

    private ListView lvPCjj;
    public static List<Pcjj> cjjList;
    public static int xqid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_p_cjj);
        Bundle bundle=	getIntent().getExtras();

        xqid=bundle.getInt("xqid");
        cjjList= PdataHelper.getPcjj(xqid); //根据小区ID加载采集机信息列表
        lvPCjj = (ListView) findViewById(R.id.lvPCjj);
        ListAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, getCjj());
        lvPCjj.setAdapter(adapter);
        lvPCjj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(P_PcjjActivity.this, P_PmeterActivity.class);
                intent.putExtra("xqid", cjjList.get(position).XqId);
                intent.putExtra("cjjid", cjjList.get(position).CjjId);
                startActivity(intent);
            }
        });
//        setupActionBar();
    }

    private List<String> getCjj() {
        List<String> data = new ArrayList<String>();
        for (Pcjj cjj : cjjList) {
            String cjjid = (cjj.CjjId + "").replaceAll(" ","");
            while (cjjid.length() < 6){
                cjjid = "0" + cjjid;
            }
            int x = Integer.parseInt(cjjid.substring(0, 2));
            int y = Integer.parseInt(cjjid.substring(4, 6));
            data.add("通讯机:" + x +"   端口:" + y + "(" + cjj.CjjAddr + ")");
        }
        return data;
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
                Intent intent = new Intent(P_PcjjActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                P_PcjjActivity.this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
