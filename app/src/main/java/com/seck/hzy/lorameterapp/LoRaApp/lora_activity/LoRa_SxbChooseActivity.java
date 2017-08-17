package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2016/11/22.
 */
public class LoRa_SxbChooseActivity extends Activity {


    @BindView(R.id.SxbChooseActivity_btn_getData)
    Button btnGetData;

    @BindView(R.id.SxbChooseActivity_btn_CsSetting)
    Button btnCsSetting;

    @BindView(R.id.SxbChooseActivity_btn_Sxtwz)
    Button btnSxtwz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        setContentView(R.layout.lora_activity_sxbchooseactivity);
        ButterKnife.bind(this);

        /**
         * 点抄数据
         */
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_SxbChooseActivity.this,LoRa_SxbGetDataActivity.class);
                startActivity(i);
            }
        });
        /**
         * 摄像表参数
         */
        btnCsSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (MenuActivity.checkPsw(LoRa_SxbChooseActivity.this) == 1) {
                    Intent intent = new Intent(LoRa_SxbChooseActivity.this, LoRa_SxbCsSetting.class);
                    startActivity(intent);

//                }
            }
        });
        /**
         * 摄像头位置
         */
        btnSxtwz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoRa_SxbChooseActivity.this, Lora_SxbParamLazySetting.class);
                startActivity(intent);
            }
        });


    }
}
