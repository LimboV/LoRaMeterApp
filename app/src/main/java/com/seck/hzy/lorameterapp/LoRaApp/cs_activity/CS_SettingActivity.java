package com.seck.hzy.lorameterapp.LoRaApp.cs_activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/9/19.
 */

public class CS_SettingActivity extends Activity {
    @BindView(R.id.CS_Setting_btn_testMode)
    Button CS_Setting_btn_testMode;
    @BindView(R.id.CS_Setting_btn_userMode)
    Button CS_Setting_btn_userMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.cs_activity_setting);
        ButterKnife.bind(this);
        CS_Setting_btn_testMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendMsg = "6810aaaaaaaaaaaaaa330401830001da16";
                MenuActivity.sendCmd(sendMsg);

            }
        });
        CS_Setting_btn_userMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendMsg = "6810aaaaaaaaaaaaaa330401830000d916";
                MenuActivity.sendCmd(sendMsg);

            }
        });
    }
}
