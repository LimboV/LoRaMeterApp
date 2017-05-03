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
 * Created by ssHss on 2015/10/20.
 */
public class P_ChooseActivity extends Activity {
    private Button btnSetZNDYH,btnSetWaterMeter,btnSetCjj;
    private String PW;
    boolean X = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_choose);
        btnSetWaterMeter = (Button) findViewById(R.id.btnSetWatermeter);
        btnSetWaterMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(P_ChooseActivity.this, P_IdManager.class);
                i1.putExtra("isZNDYH", false);
                startActivity(i1);
            }
        });
        btnSetZNDYH = (Button) findViewById(R.id.btnSetZNDYH);
        btnSetZNDYH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(P_ChooseActivity.this, P_IdManager.class);
                i.putExtra("isZNDYH", true);
                startActivity(i);
            }
        });
        btnSetCjj = (Button) findViewById(R.id.btnSetCjj);
        btnSetCjj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPsw() == 1){
                    Intent i = new Intent(P_ChooseActivity.this, P_CjjSettingActivity.class);
                    startActivity(i);
                }
            }
        });
//        setupActionBar();
    }

    private int checkPsw(){
        P_PasswordDialog pswdlg = new P_PasswordDialog(this, 0);
        int rtn = pswdlg.showDialog();
        if (rtn != 1) {
            HintDialog.ShowHintDialog(this, "密码输入错误", "错误");
        }
        return rtn;
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
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case 1:
                Intent intent = new Intent(P_ChooseActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                P_ChooseActivity.this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
