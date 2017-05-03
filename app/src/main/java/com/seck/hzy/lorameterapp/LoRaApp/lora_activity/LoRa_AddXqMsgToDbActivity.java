package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;

/**
 * Created by limbo on 2017/1/3.
 */
public class LoRa_AddXqMsgToDbActivity extends Activity{

    private EditText etXqNum,etXqName,etCjjNum,etCjjName;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lora_activity_addxqmsgtodb);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        etXqNum = (EditText) findViewById(R.id.AddXqMsgToDbActivity_et_XqNum);
        etXqName = (EditText) findViewById(R.id.AddXqMsgToDbActivity_et_XqName);
        etCjjNum = (EditText) findViewById(R.id.AddXqMsgToDbActivity_et_CjjNum);
        etCjjNum.setText("10101");
        etCjjNum.setKeyListener(null);//不可编辑
        etCjjName = (EditText) findViewById(R.id.AddXqMsgToDbActivity_et_CjjName);
        etCjjName.setVisibility(View.GONE);
        btnSave = (Button) findViewById(R.id.AddXqMsgToDbActivity_btn_saveXqToDb);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xqNum = etXqNum.getText().toString().trim();
                String xqName = etXqName.getText().toString().trim();
                String cjjNum = etCjjNum.getText().toString().trim();
                String cjjName = "采集机编号";

                if (HzyUtils.isEmpty(xqNum) || HzyUtils.isEmpty(xqName)|| HzyUtils.isEmpty(cjjNum)|| HzyUtils.isEmpty(cjjName)){
                    HintDialog.ShowHintDialog(LoRa_AddXqMsgToDbActivity.this, "输入不可为空", "提示");
                    return;
                }else {
                    LoRa_DataHelper.addXq(Integer.parseInt(xqNum), xqName, Integer.parseInt(cjjNum), cjjName);

                    Intent resultIntent = new Intent();
                    LoRa_AddXqMsgToDbActivity.this.setResult(99, resultIntent);
                    LoRa_AddXqMsgToDbActivity.this.finish();
                }
            }
        });
    }
}
