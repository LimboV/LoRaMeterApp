package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.seck.hzy.lorameterapp.R;

/**
 * Created by limbo on 2016/11/22.
 */
public class LoRa_SxbChooseActivity extends Activity {

    private Button btnGetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        setContentView(R.layout.lora_activity_sxbchooseactivity);
        btnGetData = (Button) findViewById(R.id.SxbChooseActivity_btn_getData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoRa_SxbChooseActivity.this,LoRa_SxbGetDataActivity.class);
                startActivity(i);
            }
        });
    }
}
