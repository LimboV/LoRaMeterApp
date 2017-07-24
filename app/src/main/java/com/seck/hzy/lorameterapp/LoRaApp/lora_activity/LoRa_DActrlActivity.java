package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.seck.hzy.lorameterapp.R;

/**
 * Created by limbo on 2017/3/31.
 */
public class LoRa_DActrlActivity extends Activity {
    private Button btnAddAll, btnAddSingle, btnSearchSingle, btnDeleteSingle, btnReplaceSingle, btnReadSingle,btnSetSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.lora_activity_dactrl);
        btnAddAll = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_addAllData);
        btnAddAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_XqListActivity.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_XqListActivity.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }


            }
        });
        btnAddSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_addSingleData);
        btnAddSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_AddSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }

            }
        });
        btnSearchSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_searchSingleData);
        btnSearchSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_SearchSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        btnDeleteSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_deleteSingleData);
        btnDeleteSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_DeleteSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        btnReplaceSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_replaceSingleData);
        btnReplaceSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_ReplaceSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        btnReadSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_readSingleData);
        btnReadSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_ReadSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
        btnSetSingle = (Button) findViewById(R.id.LoRa_DActrlActivity_btn_setSingleData);
        btnSetSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuActivity.METER_STYLE.equals("L")) {//LoRa表

                } else if (MenuActivity.METER_STYLE.equals("W")||MenuActivity.METER_STYLE.equals("JY")) {//Wmrnet表
                    Intent i = new Intent(LoRa_DActrlActivity.this, LoRa_SetSingleData.class);
                    startActivity(i);
                } else if (MenuActivity.METER_STYLE.equals("P")) {//P型表

                } else if (MenuActivity.METER_STYLE.equals("Z")) {//直读表

                }
            }
        });
    }

}
