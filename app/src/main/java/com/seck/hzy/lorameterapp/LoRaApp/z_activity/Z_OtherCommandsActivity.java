package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.Residential;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.SkRestClient;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 水表调试内容命令列表
 *
 * @author Guhong.
 *         <p>
 *         2012.09.08，添加基本的设置内容.
 */
public class Z_OtherCommandsActivity extends ListActivity {

    private void createView() {
        final String[] menuItems = new String[]{
                "1.通用表读数",
                "2.摄像表读数",
                "3.电阻表读数",
                "4.开关阀控制",
                "5.通讯测试",
                "6.服务器测试"};

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.z_activity_listview, menuItems);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框

        setListAdapter(adapter);

        final Z_OtherCommandsActivity thisView = this;

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent;

                switch (position) {
                    case 0:
                        if (MenuActivity.netThread == null) {
                            Toast.makeText(getApplicationContext(), "请先建立网络连接",
                                    Toast.LENGTH_LONG).show();
                            break;
                        }
                        intent = new Intent(thisView, Z_NBReadActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        if (MenuActivity.netThread == null) {
                            Toast.makeText(getApplicationContext(), "请先建立网络连接",
                                    Toast.LENGTH_LONG).show();
                            break;
                        }
                        intent = new Intent(thisView, Z_SXBReadActivity.class);
                        startActivity(intent);
                        break;
                    case 2: // 读电阻表数据
                        if (MenuActivity.netThread == null) {
                            Toast.makeText(getApplicationContext(), "请先建立网络连接",
                                    Toast.LENGTH_LONG).show();
                            break;
                        }
                        intent = new Intent(thisView, Z_DZBReadActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(thisView, Z_OffonValveActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        if (MenuActivity.netThread == null) {
                            Toast.makeText(getApplicationContext(), "请先建立网络连接",
                                    Toast.LENGTH_LONG).show();
                            break;
                        }
                        intent = new Intent(thisView, Z_ComtestActivity.class);
                        startActivity(intent);
                        break;
                    case 5: {
                        final ProgressDialog progressBar = new ProgressDialog(Z_OtherCommandsActivity.this);
                        progressBar.setCancelable(false);
                        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressBar.setIndeterminate(true);
                        progressBar.setMessage("获取小区信息");
                        progressBar.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    List<Residential> result = SkRestClient.getAllResidentials();
                                    if (result == null) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.dismiss();
                                                HintDialog.ShowHintDialog(Z_OtherCommandsActivity.this, "服务器测试失败",
                                                        "提示");
                                            }
                                        });
                                        return;
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.dismiss();
                                            HintDialog.ShowHintDialog(Z_OtherCommandsActivity.this, "服务器测试成功", "提示");
                                        }
                                    });
                                } catch (UnknownHostException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.dismiss();
                                            HintDialog.ShowHintDialog(Z_OtherCommandsActivity.this, "无法连接到服务器", "错误");
                                        }
                                    });
                                } catch (InterruptedIOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.dismiss();
                                            HintDialog.ShowHintDialog(Z_OtherCommandsActivity.this, "服务器无响应", "错误");
                                        }
                                    });
                                } catch (IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.dismiss();
                                            HintDialog.ShowHintDialog(Z_OtherCommandsActivity.this, "本地网络未开启", "错误");
                                        }
                                    });
                                }

                            }
                        }).start();

                    }

                    break;
                }
            }
        });
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
    }
}
