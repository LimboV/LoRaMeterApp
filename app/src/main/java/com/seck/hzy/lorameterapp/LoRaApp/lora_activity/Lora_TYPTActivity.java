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
 * Created by limbo on 2017/11/6.
 */

public class Lora_TYPTActivity extends Activity {
    @BindView(R.id.Lora_TYPTActivity_btn_toJDControl)
    Button Lora_TYPTActivity_btn_toJDControl;
    @BindView(R.id.Lora_TYPTActivity_btn_toJZQControl)
    Button Lora_TYPTActivity_btn_toJZQControl;
    @BindView(R.id.Lora_TYPTActivity_btn_toLYControl)
    Button Lora_TYPTActivity_btn_toLYControl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init(){
        setContentView(R.layout.lora_activity_typt);
        ButterKnife.bind(this);
        Lora_TYPTActivity_btn_toJDControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Lora_TYPTActivity_btn_toJZQControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Lora_TYPTActivity_btn_toLYControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Lora_TYPTActivity.this,Lora_TYPTtoLYActivity.class);
                startActivity(i);
            }
        });
    }
}
