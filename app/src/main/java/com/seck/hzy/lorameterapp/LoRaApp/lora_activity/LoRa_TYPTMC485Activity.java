package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ssHss on 2018/8/14.
 */

public class LoRa_TYPTMC485Activity extends Activity {
    @BindView(R.id.btn_getSingleData)
    Button btn_getSingleData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lora_activity_typt_mc485);
        ButterKnife.bind(this);
    }
}
