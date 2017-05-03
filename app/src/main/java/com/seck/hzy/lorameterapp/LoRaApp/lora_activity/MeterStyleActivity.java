package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

/**
 * Created by ssHss on 2016/7/19.
 */
public class MeterStyleActivity extends Activity {

    /**
     * 表类型
     */
    private Button btnLoRa, btnWmrnet, btnWmrnetFsk, btnP,btnZD;
    /**
     * 地区
     */
    private Button btnXj, btnYc;

    private TextView tv0, tv1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void init() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_meterstyleactivity);

        tv0 = (TextView) findViewById(R.id.meterStyleActivity_tv_tip0);
        tv1 = (TextView) findViewById(R.id.meterStyleActivity_tv_tip1);

        btnLoRa = (Button) findViewById(R.id.meterStyleActivity_btn_loRaMeter);
        btnLoRa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser("L");//山科LoRa表
                loadUser();
                HintDialog.ShowHintDialog(MeterStyleActivity.this, "设置完成", "提示");
                Intent data = new Intent();
                setResult(BluetoothConnectThread.METER, data);//设置返回结果-->表类型更新
                finish();
            }
        });
        btnWmrnet = (Button) findViewById(R.id.meterStyleActivity_btn_wmrnetMeter);
        btnWmrnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser("W");//安美通LoRa表
                loadUser();
                HintDialog.ShowHintDialog(MeterStyleActivity.this, "设置完成", "提示");
                Intent data = new Intent();
                setResult(BluetoothConnectThread.METER, data);//设置返回结果-->表类型更新
                finish();
            }
        });
        btnWmrnetFsk = (Button) findViewById(R.id.meterStyleActivity_btn_wmrnetFSKMeter);
        btnWmrnetFsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser("F");//安美通Fsk表
                loadUser();
                HintDialog.ShowHintDialog(MeterStyleActivity.this, "设置完成", "提示");
                Intent data = new Intent();
                setResult(BluetoothConnectThread.METER, data);//设置返回结果-->表类型更新
                finish();
            }
        });
        btnP = (Button) findViewById(R.id.meterStyleActivity_btn_PMeter);
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser("P");//P型摄像表
                loadUser();
                HintDialog.ShowHintDialog(MeterStyleActivity.this, "设置完成", "提示");
                Intent data = new Intent();
                setResult(BluetoothConnectThread.METER, data);//设置返回结果-->表类型更新
                finish();
            }
        });
        btnZD = (Button) findViewById(R.id.meterStyleActivity_btn_ZDMeter);
        btnZD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser("Z");//直读表
                loadUser();
                HintDialog.ShowHintDialog(MeterStyleActivity.this, "设置完成", "提示");
                Intent data = new Intent();
                setResult(BluetoothConnectThread.METER, data);//设置返回结果-->表类型更新
                finish();
            }
        });

        /**
         * 地区
         */
        btnXj = (Button) findViewById(R.id.meterStyleActivity_btn_Xj);
        btnXj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser0("XJ");//新疆
                loadUser0();
                HintDialog.ShowHintDialog(MeterStyleActivity.this, "设置完成", "提示");
            }
        });
        btnYc = (Button) findViewById(R.id.meterStyleActivity_btn_Yc);
        btnYc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser0("YC");//宜春
                loadUser0();
                HintDialog.ShowHintDialog(MeterStyleActivity.this, "设置完成", "提示");
            }
        });
        loadUser();
        loadUser0();
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
        } else if (MenuActivity.METER_STYLE.equals("W")) {
            tv0.setText("当前表类型为:安美通LoRa表");
        } else if (MenuActivity.METER_STYLE.equals("F")) {
            tv0.setText("当前表类型为:安美通Fsk表");
        } else if (MenuActivity.METER_STYLE.equals("P")) {
            tv0.setText("当前表类型为:P型摄像表");
        } else if (MenuActivity.METER_STYLE.equals("Z")) {
            tv0.setText("当前表类型为:直读表");
        }

    }

    /**
     * 使用SharePreferences保存用户信息
     */
    public void saveUser0(String x) {

        SharedPreferences.Editor editor = getSharedPreferences("meterStyle", MODE_PRIVATE).edit();
        editor.putString("cityName", x);
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser0() {
        SharedPreferences pref = getSharedPreferences("meterStyle", MODE_PRIVATE);
        MenuActivity.CITY_NAME = pref.getString("cityName", "");
        if (MenuActivity.CITY_NAME.equals("XJ")) {
            tv1.setText("当前地区为:新疆");
        } else if (MenuActivity.CITY_NAME.equals("YC")) {
            tv1.setText("当前地区为:宜春");
        }
    }

}
