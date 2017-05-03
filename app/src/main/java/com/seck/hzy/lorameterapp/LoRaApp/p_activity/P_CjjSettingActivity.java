package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

/**
 * Created by ssHss on 2016/5/5.
 */
public class P_CjjSettingActivity extends Activity {
    private Button btnSetting,btnUpload,btnSys,btnState,btnAddrLoad;
    private String PW,NPW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_cjj_setting);
//        setupActionBar();

        /**
         * 参数设置
         */
        btnSetting = (Button) findViewById(R.id.cjj_setting_btn_setting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(P_CjjSettingActivity.this, P_CsSettingActivity.class);
                startActivity(i);
            }
        });
        /**
         * 系统设置
         */
        btnSys = (Button) findViewById(R.id.cjj_setting_btn_system);
        btnSys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(P_CjjSettingActivity.this,P_sysSettingActivity.class);
                startActivity(i);
            }
        });
        /**
         * 状态查询
         */
        btnState = (Button) findViewById(R.id.cjj_setting_btn_state);
        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(P_CjjSettingActivity.this,P_stateActivity.class);
                startActivity(i);
            }
        });
        /**
         * 地址导入
         */
        btnAddrLoad = (Button) findViewById(R.id.cjj_setting_btn_addrLoad);
        btnAddrLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(P_CjjSettingActivity.this,P_AddrLoadActivity.class);
                startActivity(i);
            }
        });
        /**
         * 系统升级
         */
        btnUpload = (Button) findViewById(R.id.cjj_setting_btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPsw() == 1){
                    Intent i = new Intent(P_CjjSettingActivity.this, P_UploadActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private int checkPsw(){
        P_PasswordDialog pswdlg = new P_PasswordDialog(this, 1);
        int rtn = pswdlg.showDialog();
        if (rtn != 1) {
            HintDialog.ShowHintDialog(this, "密码输入错误", "错误");
        }
        return rtn;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     *多线程
     */
    class waitBtMsgThread implements Runnable {
        @Override
        public void run() {
            //等待三秒
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "返回主界面");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case 1:
                Intent intent = new Intent(P_CjjSettingActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                P_CjjSettingActivity.this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
