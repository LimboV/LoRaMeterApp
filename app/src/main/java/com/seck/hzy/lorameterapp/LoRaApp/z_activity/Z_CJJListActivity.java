package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.Colector;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

import java.util.Arrays;
import java.util.List;

/**
 * CJJListActivity显示采集机的列表。为一个标准的ListActivity。
 *
 * @author l412
 */
public class Z_CJJListActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the main view;
        createView();
    }

    private void createView() {
        final Bundle bundle = this.getIntent().getExtras();
        int lyh = bundle.getInt("LYH", 0);
        final List<Colector> cjjSet =
                MenuActivity.dataSource.getColectors(bundle.getInt("XQBHA"), lyh);
        Log.d("limbo", cjjSet.size() + "");
        for (int i = 0; i < cjjSet.size(); i++) {
            Log.d("limbo", i + "列表:" + cjjSet.get(i).ColectID + "楼宇号:" + cjjSet.get(i).BuildingID);
            if (lyh != cjjSet.get(i).BuildingID ) {
                Log.d("limbo", "删除:" + cjjSet.get(i).ColectID + "楼宇号:" + cjjSet.get(i).BuildingID);
                cjjSet.remove(i);
                i--;
            }
        }
        Log.d("limbo", cjjSet.size() + "");


        if (cjjSet == null || cjjSet.size() == 0) {
            HintDialog.ShowHintDialog(this, "无对应采集机","错误");
            // Toast.makeText(getApplicationContext(), "无对应采集机", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        final String[] cjjBH = new String[cjjSet.size()];
        for (int i = 0; i < cjjSet.size(); i++) {
            cjjBH[i] = cjjSet.get(i).ColectID + (cjjSet.get(i).ColectAddr == null ?
                    "" : "---" + cjjSet.get(i).ColectAddr);
        }
        Arrays.sort(cjjBH);


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.z_activity_listview, cjjBH);
        setListAdapter(adapter);

        final Z_CJJListActivity thisView = this;

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(thisView, Z_SBListActivity.class);
                intent.putExtra("CB", bundle.getBoolean("CB"));
                intent.putExtra("XQBHA", bundle.getInt("XQBHA"));
                intent.putExtra("LYH", bundle.getInt("LYH"));
                //                intent.putExtra("CJJH", cjjSet.get(position).ColectID);
                intent.putExtra("CJJH", cjjBH[position]);
                Log.d("limbo", "主采号+分采号+端口号:" + cjjBH[position]);
                startActivity(intent);
            }
        });
    }
}
