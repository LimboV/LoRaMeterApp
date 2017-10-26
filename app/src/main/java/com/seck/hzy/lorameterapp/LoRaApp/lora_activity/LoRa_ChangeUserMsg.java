package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.LoRa_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/10/24.
 */

public class LoRa_ChangeUserMsg extends Activity {

    @BindView(R.id.UserMsgLoadActivity_btn_setBds)
    Button UserMsgLoadActivity_btn_setBds;

    @BindView(R.id.UserMsgLoadActivity_et_meterId)
    EditText UserMsgLoadActivity_et_meterId;

    @BindView(R.id.UserMsgLoadActivity_et_meterNumber)
    EditText UserMsgLoadActivity_et_meterNumber;

    @BindView(R.id.UserMsgLoadActivity_et_userAddr)
    EditText UserMsgLoadActivity_et_userAddr;

    @BindView(R.id.UserMsgLoadActivity_et_xqId)
    EditText UserMsgLoadActivity_et_xqId;

    @BindView(R.id.UserMsgLoadActivity_et_cjjID)
    EditText UserMsgLoadActivity_et_cjjID;

    @BindView(R.id.UserMsgLoadActivity_et_meterFreq)
    EditText UserMsgLoadActivity_et_meterFreq;

    @BindView(R.id.UserMsgLoadActivity_et_bds)
    EditText UserMsgLoadActivity_et_bds;

    @BindView(R.id.UserMsgLoadActivity_cb_error)
    CheckBox UserMsgLoadActivity_cb_error;

    @BindView(R.id.UserMsgLoadActivity_btn_save)
    Button UserMsgLoadActivity_btn_save;

    @BindView(R.id.UserMsgLoadActivity_tv_bds)
    TextView UserMsgLoadActivity_tv_bds;

    private int xqid, cjjid, meterid;
    private String useraddr, meternumber,date,freq;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lora_acivity_usermsgload);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        xqid = bundle.getInt("xqid");
        cjjid = bundle.getInt("cjjid");
        meterid = bundle.getInt("meterid");//序列号
        useraddr = bundle.getString("useraddr");
        meternumber = Long.parseLong(bundle.getString("meternumber"))+"";//表ID
        freq = bundle.getString("freq");
        if (!HzyUtils.isEmpty(freq)){
            freq = Integer.parseInt(freq,16)+"";
        }

        UserMsgLoadActivity_et_xqId.setText(xqid + "");
        UserMsgLoadActivity_et_cjjID.setText(cjjid + "");
        UserMsgLoadActivity_et_userAddr.setText(useraddr);
        UserMsgLoadActivity_et_meterNumber.setText(meterid + "");
        UserMsgLoadActivity_et_meterId.setText(meternumber + "");
        UserMsgLoadActivity_et_meterFreq.setText(freq);

        UserMsgLoadActivity_et_xqId.setEnabled(false);
        UserMsgLoadActivity_et_cjjID.setEnabled(false);
        UserMsgLoadActivity_et_meterId.setEnabled(false);
        UserMsgLoadActivity_et_meterNumber.setEnabled(false);
        UserMsgLoadActivity_et_meterFreq.setEnabled(false);

        UserMsgLoadActivity_cb_error.setVisibility(View.GONE);
        UserMsgLoadActivity_btn_setBds.setVisibility(View.GONE);
        UserMsgLoadActivity_et_bds.setVisibility(View.GONE);
        UserMsgLoadActivity_tv_bds.setVisibility(View.GONE);

        UserMsgLoadActivity_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
                date = df.format(d);

                useraddr = UserMsgLoadActivity_et_userAddr.getText().toString().trim();

                LoRa_DataHelper.deleteUser(xqid,cjjid,meterid);
                LoRa_DataHelper.addMeter(xqid,cjjid,meterid,meternumber,useraddr,date,freq);

                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                resultIntent.putExtras(bundle);
                LoRa_ChangeUserMsg.this.setResult(97, resultIntent);
                LoRa_ChangeUserMsg.this.finish();

            }
        });

    }
}
