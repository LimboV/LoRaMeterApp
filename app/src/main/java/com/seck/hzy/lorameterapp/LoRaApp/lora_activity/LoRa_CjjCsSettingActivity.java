package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.seck.hzy.lorameterapp.R;

/**
 * Created by ssHss on 2016/8/12.
 */
public class LoRa_CjjCsSettingActivity extends Activity{

    private Button btnGprs,btnLoRa,btnCjjSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        setContentView(R.layout.lora_activity_cjjcssettingactivity);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        /**
         * 读写GPRS参数
         */
        btnGprs = (Button) findViewById(R.id.CjjCsSettingActivity_btn_Gprs);
        btnGprs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_CjjCsSettingActivity.this,LoRa_GprsSettingActivity.class);
                startActivity(i);
            }
        });
        /**
         * 读写LoRa参数
         */
        btnLoRa = (Button) findViewById(R.id.CjjCsSettingActivity_btn_LoRa);
        btnLoRa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_CjjCsSettingActivity.this,LoRa_LoRaSettingActivity.class);
                startActivity(i);
            }
        });
        /**
         * 读写采集机参数
         */
        btnCjjSetting = (Button) findViewById(R.id.CjjCsSettingActivity_btn_CjjSetting);
        btnCjjSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_CjjCsSettingActivity.this,LoRa_CjjAboutActivity.class);
                startActivity(i);
            }
        });
    }
}
