package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.seck.hzy.lorameterapp.LoRaApp.utils.P_DataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/9/18.
 */

public class P_LoadMeterIdAddXqActivity extends Activity {
    @BindView(R.id.AddXqMsgToDbActivity_et_XqNum)
    EditText AddXqMsgToDbActivity_et_XqNum;
    @BindView(R.id.AddXqMsgToDbActivity_et_XqName)
    EditText AddXqMsgToDbActivity_et_XqName;
    @BindView(R.id.AddXqMsgToDbActivity_et_CjjNum)
    EditText AddXqMsgToDbActivity_et_CjjNum;
    @BindView(R.id.AddXqMsgToDbActivity_et_CjjName)
    EditText AddXqMsgToDbActivity_et_CjjName;
    @BindView(R.id.AddXqMsgToDbActivity_btn_saveXqToDb)
    Button AddXqMsgToDbActivity_btn_saveXqToDb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lora_activity_addxqmsgtodb);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        ButterKnife.bind(this);
//        AddXqMsgToDbActivity_et_CjjNum.setText("10101");
//        AddXqMsgToDbActivity_et_CjjNum.setKeyListener(null);//不可编辑
//        AddXqMsgToDbActivity_et_CjjName.setVisibility(View.GONE);
        AddXqMsgToDbActivity_btn_saveXqToDb = (Button) findViewById(R.id.AddXqMsgToDbActivity_btn_saveXqToDb);
        AddXqMsgToDbActivity_btn_saveXqToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xqNum = AddXqMsgToDbActivity_et_XqNum.getText().toString().trim();
                String xqName = AddXqMsgToDbActivity_et_XqName.getText().toString().trim();
                String cjjNum = AddXqMsgToDbActivity_et_CjjNum.getText().toString().trim();
                String cjjName = AddXqMsgToDbActivity_et_CjjName.getText().toString().trim();

                if (HzyUtils.isEmpty(xqNum) || HzyUtils.isEmpty(xqName)|| HzyUtils.isEmpty(cjjNum)|| HzyUtils.isEmpty(cjjName)){
                    HintDialog.ShowHintDialog(P_LoadMeterIdAddXqActivity.this, "各项输入皆不可为空", "提示");
                    return;
                }else {
                    P_DataHelper.addXq(Integer.parseInt(xqNum), xqName, Integer.parseInt(cjjNum), cjjName);

                    Intent resultIntent = new Intent();
                    P_LoadMeterIdAddXqActivity.this.setResult(99, resultIntent);
                    P_LoadMeterIdAddXqActivity.this.finish();
                }
            }
        });


    }
}
