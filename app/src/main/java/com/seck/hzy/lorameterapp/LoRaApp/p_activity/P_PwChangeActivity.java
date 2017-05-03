package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

/**
 * Created by ssHss on 2016/6/6.
 */
public class P_PwChangeActivity extends Activity {

    private Button btnChangePwSure;
    private String PW,NPW;
    private EditText etOldPw,etNewPw1,etNewPw2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_pw_change);
        loadUser();
        etOldPw = (EditText) findViewById(R.id.etOldPw);
        etNewPw1 = (EditText) findViewById(R.id.etNewPw1);
        etNewPw2 = (EditText) findViewById(R.id.etNewPw2);
        btnChangePwSure = (Button) findViewById(R.id.btnChangePwSure);
        btnChangePwSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOldPw.getText().toString().equals(PW)){
                    NPW = etNewPw1.getText().toString();
                    if (NPW.equals(etNewPw2.getText().toString())){
                        saveUser();
                        AlertDialog.Builder builder = new AlertDialog.Builder(P_PwChangeActivity.this);
                        builder.setMessage("密码保存成功").setTitle("成功");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        loadUser();
                    }else {
                        HintDialog.ShowHintDialog(P_PwChangeActivity.this, "两次输入的密码不一致", "提示");
                    }
                }else {
                    HintDialog.ShowHintDialog(P_PwChangeActivity.this,"密码错误","提示");
                }
            }
        });
    }

    /**
     * 保存新密码
     */
    private void saveUser() {

        SharedPreferences.Editor editor =  MenuActivity.uiAct.getSharedPreferences("usual_pw", MODE_PRIVATE).edit();
        editor.putString("PW", NPW);
        editor.commit();
    }

    /**
     * 获取密码
     */
    private void loadUser() {
        SharedPreferences pref =  MenuActivity.uiAct.getSharedPreferences("usual_pw", MODE_PRIVATE);
        PW = pref.getString("PW", "");
    }
}
