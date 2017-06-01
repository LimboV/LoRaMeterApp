package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.Building;
import com.seck.hzy.lorameterapp.LoRaApp.model.Colector;
import com.seck.hzy.lorameterapp.LoRaApp.model.Meter;
import com.seck.hzy.lorameterapp.LoRaApp.model.Residential;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.SkRestClient;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Z_XQSelectActivity extends Activity {

    private ProgressDialog progressBar = null;

    private ListView xqListView;

    private List<Residential> residentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_xq_select);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        xqListView = (ListView) findViewById(R.id.xq_list);
        xqListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Button btnConfirm = (Button) findViewById(R.id.confirm);
        btnConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                downloadData();
            }
        });

        getAllResidentials();
    }

    private void getAllResidentials() {

        progressBar.setIndeterminate(true);
        progressBar.setMessage("获取小区信息");
        progressBar.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Residential> result = SkRestClient.getAllResidentials();
                    if (result == null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.dismiss();
                                HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "获取小区信息","提示");
                            }
                        });
                        return;
                    }

                    residentials = result;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            xqListView.setAdapter(new ArrayAdapter<Residential>(Z_XQSelectActivity.this,
                                    android.R.layout.simple_list_item_multiple_choice, result));
                        }
                    });
                } catch (UnknownHostException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "无法连接到服务器",
                                    "错误");
                        }
                    });
                } catch (InterruptedIOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "服务器无响应",
                                    "错误");
                        }
                    });
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "网络异常",
                                    "错误");
                        }
                    });
                }
            }
        }).start();
    }

    private void downloadData() {
        final List<Residential> selectItems = new ArrayList<Residential>();
        SparseBooleanArray positionArr = xqListView.getCheckedItemPositions();
        for (int i = 0; i < positionArr.size(); i++) {
            if (positionArr.valueAt(i)) {
                int idx = positionArr.keyAt(i);
                selectItems.add(residentials.get(idx));
            }
        }

        if (positionArr.size() == 0) {
            finish();
            return;
        }

        if (selectItems.size() <= 0) {
            Toast.makeText(Z_XQSelectActivity.this, "未选择小区！", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setIndeterminate(false);
        progressBar.setMax(5);
        progressBar.setMessage("下载进度");
        progressBar.setProgress(0);
        progressBar.show();


        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    MenuActivity.dataSource.clearData();
                    MenuActivity.dataSource.saveResidentials(selectItems);
                    List<Integer> residentialQuarterIDs = new ArrayList<Integer>();
                    for (Residential residential : selectItems) {
                        residentialQuarterIDs.add(residential.ResidentialQuarterID);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(1);
                            progressBar.setMessage("下载楼宇信息");
                        }
                    });
                    List<Building> buildings = SkRestClient.getBuildingsByResidentialQuarterID(residentialQuarterIDs);
                    if (buildings == null || buildings.size() <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.dismiss();
                                HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "下载数据失败",
                                        "提示");
                            }
                        });
                        return;
                    }
                    MenuActivity.dataSource.saveBuildings(buildings);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(2);
                            progressBar.setMessage("下载采集机信息");
                        }
                    });
                    List<Colector> colectors = SkRestClient.getColectorsByResidentialQuarterID(residentialQuarterIDs);
                    if (colectors == null || colectors.size() <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.dismiss();
                                HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "下载数据失败","提示");
                            }
                        });
                        return;
                    }
                    MenuActivity.dataSource.saveColectors(colectors);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(3);
                            progressBar.setMessage("下载水表基本信息");
                        }
                    });
                    List<Meter> meters = SkRestClient.getMetersByResidentialQuarterID(residentialQuarterIDs);
                    if (meters == null || meters.size() <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.dismiss();
                                HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "下载数据失败","提示");
                            }
                        });
                        return;
                    }
                    for (Meter meter : meters) {
                        meter.ColectID = meter.MeterID.substring(8, 14);
                    }
                    MenuActivity.dataSource.saveMeters(meters);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            new AlertDialog.Builder(Z_XQSelectActivity.this).setTitle("提示").setMessage("下载数据成功")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).create().show();
                        }
                    });
                } catch (UnknownHostException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "无法连接到服务器","错误");
                        }
                    });
                } catch (InterruptedIOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "服务器无响应","错误");
                        }
                    });
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            HintDialog.ShowHintDialog(Z_XQSelectActivity.this, "网络异常","错误");
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        if(progressBar != null) {
            progressBar.dismiss();
        }
        super.onDestroy();
    }
}
