package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;

/**
 * 电阻表读取测试界面。
 *
 * @author Guhong.
 */
public class Z_NBReadActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_nb_test);

        final EditText et_Addr = (EditText) this.findViewById(R.id.EditText_Addr);
        //final String addr_hand = et_Addr.getText().toString();
        final TextView tv_Flow = (TextView) this.findViewById(R.id.TextView_Flow);

        SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
        final String addr_Broad = settings.getString("BADDR", MenuActivity.DEFAULT_BADDR);    // 广播地址
        Button bn_send = (Button) this.findViewById(R.id.Button_SendCmd);
        Button bn_getAddress = (Button) this.findViewById(R.id.btn_getAddress);

        final Z_NBReadActivity thisView = this;
        bn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String addr_hand = et_Addr.getText().toString();
                String addr = addr_hand.trim();
                if (addr.equals("")) {
                    addr = addr_Broad.trim();
                }

                if (addr.equals("")) {
                    Toast.makeText(thisView,"请指定发送地址或广播地址",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                byte[] baddr;
                long cflow;

                baddr = MenuActivity.netThread.getSBAddr(addr);
                try {
                    cflow = MenuActivity.netThread.readMeterCFlow(baddr, 1);
                    Log.d("limbo", cflow + "");
                    if ((cflow+"").contains("1666666")){
                        tv_Flow.setText("FFF.FF");
                    }else {
                        tv_Flow.setText((float) cflow / 100 + "");
                    }


                } catch (NetException e) {
                    HintDialog.ShowHintDialog(thisView, e.sdesc, "错误");
                } catch (IOException e) {
                    finish();
                    MenuActivity.uiAct.resetNetwork(thisView);
                }

                /***
                 * 保存数据
                 final List<Meter> meterListx = null;

                 final int meterAddr = Integer.valueOf(addr);
                 Meter meter = MeterReader.dataSource.updateMeterData(meterAddr);
                 meterListx.add(meter);
                 Thread testThread = new Thread(new Runnable() {
                @Override public void run() {
                try {
                int uploadNum = SkRestClient.uploadMeterData(meterListx);
                final int result = uploadNum < meterListx.size() ? -1 : 0;
                if (result > -1) {
                MeterReader.dataSource.updateMeterData(meterAddr);
                }
                } catch (Exception e) {
                e.printStackTrace();
                }
                }
                });
                 testThread.start();*/


            }
        });
        bn_getAddress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String addr = addr_Broad.trim();
                byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
                try {
                    baddr = MenuActivity.netThread.readAddr(baddr);
                    String addr_s = HzyUtils.bytes2HexString(baddr);
                    et_Addr.setText(addr_s);
                } catch (NetException e) {
                    HintDialog.ShowHintDialog(thisView, e.sdesc, "错误");
                } catch (IOException e) {
                    finish();
                    MenuActivity.uiAct.resetNetwork(thisView);
                }
            }

        });
    }
}
