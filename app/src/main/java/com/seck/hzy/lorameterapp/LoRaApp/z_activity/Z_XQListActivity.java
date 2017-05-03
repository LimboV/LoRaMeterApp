package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.Residential;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

import java.util.List;

/**
 *
 * @author Guhong.
 *
 * 2012.09.08，添加小区列表视图.
 *
 */
public class Z_XQListActivity extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the main view;
		createView();
	}

	private void createView()
	{
		final List<Residential> ra = MenuActivity.dataSource.getResidentials();
		if( ra == null || ra.size() == 0) {
			HintDialog.ShowHintDialog(this, "无法获取有效数据","错误");
			return;
		}

		final String[] xiaoquLiebiao = new String[ra.size()];
		final Z_XQListActivity thisView = this;
		final Bundle bundle = this.getIntent().getExtras();

		for(int i=0;i<ra.size();i++) {
			xiaoquLiebiao[i] = ra.get(i).QuarterName;
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.z_activity_listview, xiaoquLiebiao);
		setListAdapter(adapter);
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {

				if( ra == null || position >= ra.size() ) {
					HintDialog.ShowHintDialog(thisView, "无效小区","错误");
					return;
				}

				Intent intent = new Intent(thisView, Z_LYListActivity.class);
				intent.putExtra("CB", bundle.getBoolean("CB"));
				intent.putExtra("XQBHA", ra.get(position).ResidentialQuarterID.intValue());
				startActivity(intent);
			}
		});
	}
}
