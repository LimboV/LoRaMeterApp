package com.seck.hzy.lorameterapp.LoRaApp.ns_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.seck.hzy.lorameterapp.LoRaApp.utils.Utils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by limbo on 2018/3/8.
 */

public class NS_DataSyncActivity extends Activity {
    @BindView(R.id.NS_DataSyncActivity_btn_download_data)
    Button NS_DataSyncActivity_btn_download_data;
    @BindView(R.id.NS_DataSyncActivity_btn_upload_data)
    Button NS_DataSyncActivity_btn_upload_data;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    void init() {
        setContentView(R.layout.ns_activity_datasync);
        /**
         * 下载
         */
        NS_DataSyncActivity_btn_download_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkAvailable(NS_DataSyncActivity.this)) {
                    new SweetAlertDialog(NS_DataSyncActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("取消!")
                            .setContentText("本地网络未开启!")
                            .show();
                    return;
                }
                startActivity(new Intent(NS_DataSyncActivity.this,NS_XqSelectActivity.class));

            }
        });
        /**
         * 上传
         */
        NS_DataSyncActivity_btn_upload_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkAvailable(NS_DataSyncActivity.this)) {
                    new SweetAlertDialog(NS_DataSyncActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("取消!")
                            .setContentText("本地网络未开启!")
                            .show();
                    return;
                }


            }
        });
    }
}
