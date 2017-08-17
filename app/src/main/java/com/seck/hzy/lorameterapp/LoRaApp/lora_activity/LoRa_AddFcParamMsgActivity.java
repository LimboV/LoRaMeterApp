package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.model.LoRaFc;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/8/3.
 */

public class LoRa_AddFcParamMsgActivity extends Activity {

    @BindView(R.id.Lora_AddFcParamMsgActivity_btn_save)
    Button Lora_AddFcParamMsgActivity_btn_save;

    @BindView(R.id.Lora_AddFcParamMsgActivity_et_fcId)
    EditText Lora_AddFcParamMsgActivity_et_fcId;

    @BindView(R.id.Lora_AddFcParamMsgActivity_et_fcName)
    EditText Lora_AddFcParamMsgActivity_et_fcName;

    @BindView(R.id.Lora_AddFcParamMsgActivity_et_fcFreq)
    EditText Lora_AddFcParamMsgActivity_et_fcFreq;

    @BindView(R.id.Lora_AddFcParamMsgActivity_et_fcNum)
    EditText Lora_AddFcParamMsgActivity_et_fcNum;

    int xqid,cjjid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init(){
        setContentView(R.layout.lora_activity_addfcparammsg);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");

        Lora_AddFcParamMsgActivity_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fcName = Lora_AddFcParamMsgActivity_et_fcName.getText().toString().trim();
                String fcId = Lora_AddFcParamMsgActivity_et_fcId.getText().toString().trim();
                String fcFreq = Lora_AddFcParamMsgActivity_et_fcFreq.getText().toString().trim();
                String fcNum = Lora_AddFcParamMsgActivity_et_fcNum.getText().toString().trim();
                if (HzyUtils.isEmpty(fcId)||HzyUtils.isEmpty(fcFreq)||HzyUtils.isEmpty(fcName)
                ||HzyUtils.isEmpty(fcNum)){
                    HintDialog.ShowHintDialog(LoRa_AddFcParamMsgActivity.this,"分采信息不得为空","错误");
                    return;
                }
                final LoRaFc loRaFc = new LoRaFc();
                loRaFc.setXqId(xqid);
                loRaFc.setCjjId(cjjid);
                loRaFc.setFcName(fcName);
                loRaFc.setFcId(Integer.parseInt(fcId));
                loRaFc.setFcFreq(fcFreq);
                loRaFc.setFcNum(fcNum);

                AlertDialog.Builder dialog = new AlertDialog.Builder(LoRa_AddFcParamMsgActivity.this);
                dialog.setTitle("请确认是否添加分采信息。");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoRa_DataHelper.addFcjj(loRaFc);
                        Toast.makeText(LoRa_AddFcParamMsgActivity.this,"分采信息已保存",Toast.LENGTH_LONG).show();
                    }

                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            }
        });



    }
}
