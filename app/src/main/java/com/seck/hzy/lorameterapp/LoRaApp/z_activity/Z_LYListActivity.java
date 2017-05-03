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
import com.seck.hzy.lorameterapp.LoRaApp.model.Building;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

import java.util.List;

public class Z_LYListActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the main view;
		createView();
	}

	private void createView() {
		final Bundle bundle = this.getIntent().getExtras();
		final List<Building> re = MenuActivity.dataSource.getBuildings(bundle.getInt("XQBHA"));
		for (Building b : re){
			Log.d("limbo", b.BuildingID + "");
		}
		if( re == null || re.size() == 0) {
			HintDialog.ShowHintDialog(this, "无法获取有效数据，请先同步基础数据！", "错误");
			return;
		}

		final String[] louyuLiebiao = new String[re.size()];
		final Z_LYListActivity thisView = this;

		for(int i=0;i<re.size();i++) {
			if (re.get(i).BuildingName == null){
				louyuLiebiao[i] = re.get(i).BuildingID + "幢";
			}else {
				louyuLiebiao[i] = re.get(i).BuildingName;
			}

		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.z_activity_listview, louyuLiebiao);
		setListAdapter(adapter);
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {

				if( re == null || position >= re.size() ) {
					HintDialog.ShowHintDialog(thisView, "无效楼宇","错误");
					return;
				}

				Intent intent = new Intent(thisView, Z_CJJListActivity.class);
				intent.putExtra("CB", bundle.getBoolean("CB"));
				intent.putExtra("XQBHA", bundle.getInt("XQBHA"));
				intent.putExtra("LYH", re.get(position).BuildingID);
				Log.d("limbo", louyuLiebiao[position] + "-----" + re.get(position).BuildingID);
				startActivity(intent);
			}
		});
	}
}
