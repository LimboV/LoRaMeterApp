package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ssHss on 2018/1/22.
 */

public class LoRa_TYPTJdFucationActivity extends Activity{

    /**
     * 读写通用参数
     */
    @BindView(R.id.LoRaJdFucationg_btn_commonParameter)
    Button LoRaJdFucationg_btn_commonParameter;
    /**
     * 单表摄像节点调试
     */
    @BindView(R.id.LoRaJdFucationg_btn_CameraDebug)
    Button LoRaJdFucationg_btn_CameraDebug;
    /**
     * 单表脉冲节点调试
     */
    @BindView(R.id.LoRaJdFucationg_btn_singleMeterPulse)
    Button LoRaJdFucationg_btn_singleMeterPulse;
    /**
     * 单表厚膜节点调试
     */
    @BindView(R.id.LoRaJdFucationg_btn_singleMeterHm)
    Button LoRaJdFucationg_btn_singleMeterHm;
    /**
     * 485总线厚膜节点调试
     */
    @BindView(R.id.LoRaJdFucationg_btn_485Hm)
    Button LoRaJdFucationg_btn_485Hm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lora_activity_typtjd_fucation);
        ButterKnife.bind(this);
        LoRaJdFucationg_btn_commonParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_TYPTJdFucationActivity.this,LoRa_TYPTtoJDActivity.class);
                startActivity(i);
            }
        });
        LoRaJdFucationg_btn_CameraDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_TYPTJdFucationActivity.this,LoRa_TYPTJd_CameraDebug.class);
                startActivity(i);
            }
        });
        LoRaJdFucationg_btn_singleMeterPulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_TYPTJdFucationActivity.this,LoRa_TYPTJd_Pulse.class);
                startActivity(i);
            }
        });
        LoRaJdFucationg_btn_485Hm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_TYPTJdFucationActivity.this,LoRa_TYPTtoHmActivity.class);
                startActivity(i);
            }
        });

    }
}
