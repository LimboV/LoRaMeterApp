package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.seck.hzy.lorameterapp.R;

/**
 * Created by ssHss on 2016/8/9.
 */
public class LoRa_CjjChooseActivity extends Activity{

    private Button btnGetData,btnGetPic,btnGetHis,btnSetting,btnDActrl,btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        setContentView(R.layout.lora_activity_cjjchooseactivity);
        btnGetData = (Button) findViewById(R.id.cjjChooseActivity_btn_getData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_CjjChooseActivity.this, LoRa_CjjGetDataActivity.class);
                startActivity(i);
            }
        });
        btnGetPic = (Button) findViewById(R.id.cjjChooseActivity_btn_getPic);
        btnGetPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnGetHis = (Button) findViewById(R.id.cjjChooseActivity_btn_getHis);
        btnGetHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSetting = (Button) findViewById(R.id.cjjChooseActivity_btn_setting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_CjjChooseActivity.this,LoRa_CjjCsSettingActivity.class);
                startActivity(i);
            }
        });
        btnDActrl = (Button) findViewById(R.id.cjjChooseActivity_btn_DActrl);
        btnDActrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_CjjChooseActivity.this,LoRa_DActrlActivity.class);
                startActivity(i);
            }
        });
        btnUpload = (Button) findViewById(R.id.cjjChooseActivity_btn_Upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_CjjChooseActivity.this,LoRa_UploadActivity.class);
                startActivity(i);
            }
        });
    }
}
