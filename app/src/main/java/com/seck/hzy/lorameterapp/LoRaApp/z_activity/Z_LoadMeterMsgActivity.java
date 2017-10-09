package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.seck.hzy.lorameterapp.LoRaApp.barcode.CaptureActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.Z_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/9/25.
 */

public class Z_LoadMeterMsgActivity extends Activity {
    @BindView(R.id.Z_LoadMeterMsgActivity_et_XqId)
    EditText Z_LoadMeterMsgActivity_et_XqId;
    @BindView(R.id.Z_LoadMeterMsgActivity_et_CjjId)
    EditText Z_LoadMeterMsgActivity_et_CjjId;
    @BindView(R.id.Z_LoadMeterMsgActivity_et_UserAddr)
    EditText Z_LoadMeterMsgActivity_et_UserAddr;
    @BindView(R.id.Z_LoadMeterMsgActivity_et_MeterId)
    EditText Z_LoadMeterMsgActivity_et_MeterId;
    @BindView(R.id.Z_LoadMeterMsgActivity_btn_GetMeterId)
    Button Z_LoadMeterMsgActivity_btn_GetMeterId;
    @BindView(R.id.Z_LoadMeterMsgActivity_btn_SaveMeterMsgToDb)
    Button Z_LoadMeterMsgActivity_btn_SaveMeterMsgToDb;

    int meterid;
    long meterNumber;
    private int xqid, cjjid,flag;
    private int X_FLAG = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.z_activity_loadmetermsg);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框
        ButterKnife.bind(this);
        String useraddr;
        Bundle bundle = getIntent().getExtras();
        flag = bundle.getInt("flag");
        if (flag == 1){
            xqid = bundle.getInt("xqid");
            cjjid = bundle.getInt("cjjid");
            meterid = bundle.getInt("meterid");
            useraddr = bundle.getString("useraddr");
            meterNumber = Integer.parseInt(bundle.getString("meternumber"));
            Log.d("limbo",meterNumber+"");
            Z_LoadMeterMsgActivity_et_XqId.setText(xqid+"");
            Z_LoadMeterMsgActivity_et_CjjId.setText(cjjid+"");
            Z_LoadMeterMsgActivity_et_UserAddr.setText(useraddr);
            Z_LoadMeterMsgActivity_et_MeterId.setText(meterNumber+"");
        }else {
            loadUser();
        }
        Z_LoadMeterMsgActivity_btn_GetMeterId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开扫描界面扫描条形码或二维码
                X_FLAG = 1;
                Intent openCameraIntent = new Intent(Z_LoadMeterMsgActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
        Z_LoadMeterMsgActivity_btn_SaveMeterMsgToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
                final String date = df.format(d);

                final String xqid1 = Z_LoadMeterMsgActivity_et_XqId.getText().toString().trim();
                final String cjjid1 = Z_LoadMeterMsgActivity_et_CjjId.getText().toString().trim();
                final String useraddr = Z_LoadMeterMsgActivity_et_UserAddr.getText().toString().trim();
                final String meternumber = Z_LoadMeterMsgActivity_et_MeterId.getText().toString().trim();
                if (HzyUtils.isEmpty(xqid1)||HzyUtils.isEmpty(cjjid1)||HzyUtils.isEmpty(meternumber)||HzyUtils.isEmpty(useraddr)){
                    HintDialog.ShowHintDialog(Z_LoadMeterMsgActivity.this,"输入信息不得为空","提示");
                    return;
                }else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Z_LoadMeterMsgActivity.this);
                    dialog.setTitle("请确认水表信息");
                    dialog.setMessage("小区号:"+xqid1+"采集机号:"+cjjid1+"\n户号:" + useraddr + "\n绑定水表ID:" + meternumber);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (flag ==1){
                                Z_DataHelper.deleteUser(xqid,cjjid,meterid);
                                Z_DataHelper.addMeter(Integer.parseInt(xqid1),Integer.parseInt(cjjid1),meterid,meternumber,useraddr,date);
                            }else {
                                int meterid = Z_DataHelper.getMaxMeterId(Integer.parseInt(xqid1),Integer.parseInt(cjjid1));
                                Z_DataHelper.addMeter(Integer.parseInt(xqid1),Integer.parseInt(cjjid1),meterid,meternumber,useraddr,date);

                            }
                            Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            resultIntent.putExtras(bundle);
                            Z_LoadMeterMsgActivity.this.setResult(RESULT_OK, resultIntent);
                            Z_LoadMeterMsgActivity.this.finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();


                }



            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            switch (X_FLAG) {
                case 1:
                    try {
                        Bundle bundle = data.getExtras();
                        String scanResult = bundle.getString("result");
                        Z_LoadMeterMsgActivity_et_MeterId.setText(scanResult);
                        meterNumber = Integer.parseInt(scanResult);
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }

                    break;
                default:
                    break;

            }

        }
    }
    /**
     * 使用SharePreferences保存用户信息
     */
    public void saveUser() {

        SharedPreferences.Editor editor = getSharedPreferences("Z_LoadMeterMsgActivity", MODE_PRIVATE).edit();
        editor.putString("etXqId", Z_LoadMeterMsgActivity_et_XqId.getText().toString());
        editor.putString("etCjjId", Z_LoadMeterMsgActivity_et_CjjId.getText().toString());
        editor.putString("etUserAddr", Z_LoadMeterMsgActivity_et_UserAddr.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("Z_LoadMeterMsgActivity", MODE_PRIVATE);
        Z_LoadMeterMsgActivity_et_XqId.setText(pref.getString("etXqId", ""));
        Z_LoadMeterMsgActivity_et_CjjId.setText(pref.getString("etCjjId", ""));
        Z_LoadMeterMsgActivity_et_UserAddr.setText(pref.getString("etUserAddr", ""));
    }

    @Override
    protected void onDestroy() {
        if (flag != 1){
            saveUser();
        }
        super.onDestroy();
    }
}
