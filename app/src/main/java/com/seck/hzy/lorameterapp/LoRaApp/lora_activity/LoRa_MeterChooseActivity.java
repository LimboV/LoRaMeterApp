package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ssHss on 2016/7/18.
 */
public class LoRa_MeterChooseActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.meterChooseActivity_btn_getData)
    Button meterChooseActivity_btn_getData;

    @BindView(R.id.meterChooseActivity_btn_yc485Test)
    Button meterChooseActivity_btn_yc485Test;

    @BindView(R.id.meterChooseActivity_btn_getPic)
    Button meterChooseActivity_btn_getPic;

    @BindView(R.id.meterChooseActivity_btn_setting)
    Button meterChooseActivity_btn_setting;

    @BindView(R.id.meterChooseActivity_btn_location)
    Button meterChooseActivity_btn_location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_meterchooseactivity);
        ButterKnife.bind(this);

        meterChooseActivity_btn_getData.setOnClickListener(this);
        meterChooseActivity_btn_yc485Test.setOnClickListener(this);
        meterChooseActivity_btn_getPic.setOnClickListener(this);
        meterChooseActivity_btn_setting.setOnClickListener(this);
        meterChooseActivity_btn_location.setOnClickListener(this);
        if (MenuActivity.METER_STYLE.equals("L")){

        }else {
            meterChooseActivity_btn_getPic.setVisibility(View.GONE);
            meterChooseActivity_btn_location.setVisibility(View.GONE);
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
