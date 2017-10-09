package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.model.Pcjj;
import com.seck.hzy.lorameterapp.LoRaApp.utils.P_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssHss on 2016/5/31.
 */
public class P_PxqActivity extends Activity {

    private ListView lvPXq;
    private Button btnPCjjCb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_p_xq);
        P_DataHelper.getdb();//获取数据库实例
//        setupActionBar();//在左上角显示回退按钮

        lvPXq = (ListView) findViewById(R.id.lvPXq);
        ListAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, getXq()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        lvPXq.setAdapter(adapter);
        lvPXq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(P_PxqActivity.this, P_PcjjActivity.class);
                intent.putExtra("xqid", xqList.get(position).XqId);
                startActivity(intent);
            }
        });
    }

    public List<Pcjj> xqList;

    private List<String> getXq() {
        List<String> data = new ArrayList<String>();
        try {
            xqList = P_DataHelper.getXq();//从数据库提取出
            for (Pcjj cjj : xqList) {
                data.add(cjj.XqName + "(" + cjj.XqId + ")");
            }
        }catch (Exception e){
            HintDialog.ShowHintDialog(this, "未找到数据,请检查数据库文件是否存在！", "错误");
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
        getMenuInflater().inflate(R.menu.cjj, menu);
        menu.add(0, 3, 0, "返回主界面");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //				NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case 3:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
