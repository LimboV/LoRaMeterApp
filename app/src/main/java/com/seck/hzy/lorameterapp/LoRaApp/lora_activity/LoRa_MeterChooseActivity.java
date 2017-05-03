package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.seck.hzy.lorameterapp.R;

/**
 * Created by ssHss on 2016/7/18.
 */
public class LoRa_MeterChooseActivity extends Activity implements View.OnClickListener {

    private Button btnData,btnPic,btnSetting,btnLocation,btn485Test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_meterchooseactivity);

        btnData = (Button) findViewById(R.id.meterChooseActivity_btn_getData);
        btnData.setOnClickListener(this);
        btn485Test = (Button) findViewById(R.id.meterChooseActivity_btn_yc485Test);
        btn485Test.setOnClickListener(this);
        btnPic = (Button) findViewById(R.id.meterChooseActivity_btn_getPic);
        btnPic.setOnClickListener(this);
        btnSetting = (Button) findViewById(R.id.meterChooseActivity_btn_setting);
        btnSetting.setOnClickListener(this);
        btnLocation = (Button) findViewById(R.id.meterChooseActivity_btn_location);
        btnLocation.setOnClickListener(this);
        if (MenuActivity.METER_STYLE.equals("L")){

        }else {
            btnPic.setVisibility(View.GONE);
            btnLocation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.meterChooseActivity_btn_getData:
                Intent i = new Intent(LoRa_MeterChooseActivity.this,LoRa_MeterGetDataActivity.class);
                startActivity(i);
                break;

            case R.id.meterChooseActivity_btn_getPic:
                break;

            case R.id.meterChooseActivity_btn_setting:
                i = new Intent(LoRa_MeterChooseActivity.this,LoRa_MeterCsSettingActivity.class);
                startActivity(i);
                break;

            case R.id.meterChooseActivity_btn_location:
                break;

            case R.id.meterChooseActivity_btn_yc485Test:
                i = new Intent(LoRa_MeterChooseActivity.this,LoRa_Meter485TestActivity.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }
}
