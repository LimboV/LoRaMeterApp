package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ssHss on 2016/7/19.
 */
public class MeterStyleActivity extends Activity {

    /**
     * 表类型
     */
    @BindView(R.id.meterStyleActivity_btn_JYMeter)
    Button meterStyleActivity_btn_JYMeter;

    @BindView(R.id.meterStyleActivity_btn_loRaMeter)
    Button meterStyleActivity_btn_loRaMeter;

    @BindView(R.id.meterStyleActivity_btn_wmrnetMeter)
    Button meterStyleActivity_btn_wmrnetMeter;

    @BindView(R.id.meterStyleActivity_btn_wmrnetFSKMeter)
    Button meterStyleActivity_btn_wmrnetFSKMeter;

    @BindView(R.id.meterStyleActivity_btn_PMeter)
    Button meterStyleActivity_btn_PMeter;

    @BindView(R.id.meterStyleActivity_btn_ZDMeter)
    Button meterStyleActivity_btn_ZDMeter;

    @BindView(R.id.meterStyleActivity_btn_CSMeter)
    Button meterStyleActivity_btn_CSMeter;

    @BindView(R.id.meterStyleActivity_btn_NumStateMeter)
    Button meterStyleActivity_btn_NumStateMeter;

    @BindView(R.id.meterStyleActivity_btn_loRaSXMeter)
    Button meterStyleActivity_btn_loRaSXMeter;
    @BindView(R.id.meterStyleActivity_btn_loRaDZMeter)
    Button meterStyleActivity_btn_loRaDZMeter;

    private TextView tv0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_meterstyleactivity);
        ButterKnife.bind(this);


        tv0 = (TextView) findViewById(R.id.meterStyleActivity_tv_tip0);

        meterStyleActivity_btn_loRaMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeter("L");
            }
        });
        meterStyleActivity_btn_wmrnetMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeter("W");
            }
        });
        meterStyleActivity_btn_wmrnetFSKMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeter("F");
            }
        });
        meterStyleActivity_btn_PMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeter("P");
            }
        });
        meterStyleActivity_btn_ZDMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeter("Z");
            }
        });
        meterStyleActivity_btn_JYMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeter("JY");
            }
        });
        meterStyleActivity_btn_CSMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeter("CS");
            }
        });
        meterStyleActivity_btn_NumStateMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeter("NS");
            }
        });


        loadUser();
    }

    private void saveMeter(String s) {
        saveUser(s);//直读表
        loadUser();
        HintDialog.ShowHintDialog(MeterStyleActivity.this, "设置完成", "提示");
        Intent data = new Intent();
        setResult(BluetoothConnectThread.METER, data);//设置返回结果-->表类型更新
        finish();
    }

    /**
     * 使用SharePreferences保存用户信息
     */
    public void saveUser(String x) {

        SharedPreferences.Editor editor = getSharedPreferences("meterStyle", MODE_PRIVATE).edit();
        editor.putString("meterStyle", x);
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("meterStyle", MODE_PRIVATE);
        MenuActivity.METER_STYLE = pref.getString("meterStyle", "");
        if (MenuActivity.METER_STYLE.equals("L")) {
            tv0.setText("当前表类型为:山科LoRa表");
        } else if (MenuActivity.METER_STYLE.equals("LSX")) {
            tv0.setText("当前表类型为:山科LoRa摄像表");
        } else if (MenuActivity.METER_STYLE.equals("LDZ")) {
            tv0.setText("当前表类型为:山科LoRa电阻表");
        } else if (MenuActivity.METER_STYLE.equals("LYX")) {
            tv0.setText("当前表类型为:山科有线分采LoRa");
        } else if (MenuActivity.METER_STYLE.equals("W")) {
            tv0.setText("当前表类型为:安美通LoRa表");
        } else if (MenuActivity.METER_STYLE.equals("F")) {
            tv0.setText("当前表类型为:安美通Fsk表");
        } else if (MenuActivity.METER_STYLE.equals("JY")) {
            tv0.setText("当前表类型为:LoRa隽永表");
        } else if (MenuActivity.METER_STYLE.equals("JYMC")) {
            tv0.setText("当前表类型为:隽永LoRa脉冲表");
        } else if (MenuActivity.METER_STYLE.equals("JYDZ")) {
            tv0.setText("当前表类型为:隽永LoRa电阻表");
        } else if (MenuActivity.METER_STYLE.equals("P")) {
            tv0.setText("当前表类型为:P型摄像表");
        } else if (MenuActivity.METER_STYLE.equals("Z")) {
            tv0.setText("当前表类型为:有线直读表");
        } else if (MenuActivity.METER_STYLE.equals("CS")) {
            tv0.setText("当前表类型为:超声水表");
        } else if (MenuActivity.METER_STYLE.equals("NS")) {
            tv0.setText("当前表类型为:数字状态表");
        } else if (MenuActivity.METER_STYLE.equals("NS")) {
            tv0.setText("当前表类型为:无");
        }

    }

}
