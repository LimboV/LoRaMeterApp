package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.R;

/**
 * @author Guhong.
 *         <p>
 *         2012.09.08，添加基本的设置内容.
 *         2012.12.31，添加网路服务器及端口的设置.
 */
public class Z_Setting1Activity extends Activity {

    int pswFlag = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_setting1);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//不弹出输入框
        final EditText et_TYYS = (EditText) this
                .findViewById(R.id.edittext_txys);
        final EditText et_CSSJ = (EditText) this
                .findViewById(R.id.edittext_cssj);
        final EditText et_Addr = (EditText) this
                .findViewById(R.id.edittext_gbdz);
        final EditText et_Server = (EditText) this
                .findViewById(R.id.edittext_fwqdz);
        final CheckBox cb_AutoUpdate = (CheckBox) this
                .findViewById(R.id.checkbox_autoupdate);
        final EditText et_Zxd = (EditText) findViewById(R.id.edittext_zxd);

        SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(
                MenuActivity.PREF_NAME, 0);
        Integer tyys = settings.getInt("TYYS", 500); // 通信延时
        Integer cssj = settings.getInt("CSSJ", 100); // 超时时间
        Integer zxdyz = settings.getInt("ZXD", 85); // 置信度
        String addr_Broad = settings.getString("BADDR",
                MenuActivity.DEFAULT_BADDR); // 广播地址
        String addr_Server = settings.getString("SERVER",
                MenuActivity.DEFAULT_SERVER); // 服务器地址
        boolean cbAutoUpload = settings.getBoolean("AUTOUPLOAD", false);

        et_TYYS.setText(tyys.toString());
        et_CSSJ.setText(cssj.toString());
        et_Addr.setText(addr_Broad.toString());
        et_Server.setText(addr_Server);
        et_Zxd.setText(zxdyz.toString());
        cb_AutoUpdate.setChecked(cbAutoUpload);

        Button bn_save = (Button) this.findViewById(R.id.Button01);
        bn_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = MenuActivity.uiAct
                        .getSharedPreferences(MenuActivity.PREF_NAME, 0);

                Integer tyys = Integer.parseInt(et_TYYS.getText().toString()); // 通信延时
                Integer cssj = Integer.parseInt(et_CSSJ.getText().toString()); // 超时时间
                Integer zxdyz = Integer.parseInt(et_Zxd.getText().toString()); // 置信度
                String addr = et_Addr.getText().toString();
                String server = et_Server.getText().toString();
                boolean autouploaded = cb_AutoUpdate.isChecked();

                Editor edt = settings.edit();
                edt.putInt("TYYS", tyys);
                edt.putInt("CSSJ", cssj);
                edt.putInt("ZXD", zxdyz);
                edt.putString("BADDR", addr);
                edt.putString("SERVER", server);
                edt.putBoolean("AUTOUPLOAD", autouploaded);
                edt.commit();
                finish();
            }
        });

        Button bn_cancle = (Button) this.findViewById(R.id.Button02);
        bn_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void changePwd(int flag) {

        pswFlag = flag;
        LayoutInflater inflater = LayoutInflater.from(this);
        final View layView = inflater.inflate(R.layout.z_activity_psd_setting, null);

        Builder dialog = new AlertDialog.Builder(this).setView(layView);
        dialog.setTitle("密码变更");
        if (pswFlag == 0)
            dialog.setTitle("管理员密码变更");
        else if (pswFlag == 1)
            dialog.setTitle("抄表员密码变更");

        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText et_oldPwd = (EditText) layView
                        .findViewById(R.id.EditText_PsdOld);
                EditText et_newPwd = (EditText) layView
                        .findViewById(R.id.EditText_PsdNew);
                EditText et_newPwd2 = (EditText) layView
                        .findViewById(R.id.EditText_PsdNew2);

                String inputPwd = et_newPwd.getText().toString();
                String inputPwd2 = et_newPwd2.getText().toString();
                String oldPwd = et_oldPwd.getText().toString();

                SharedPreferences settings = MenuActivity.uiAct
                        .getSharedPreferences(MenuActivity.PREF_NAME, 0);
                String dbPwd = "";
                if (pswFlag == 0)
                    dbPwd = settings.getString("pwd", "seckadmin");
                else if (pswFlag == 1)
                    dbPwd = settings.getString("cbyPwd", "12345678");

                if (!dbPwd.equals(oldPwd)) {
                    Toast.makeText(getApplicationContext(), "错误的原始密码",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (!inputPwd.equals(inputPwd2)) {
                    Toast.makeText(getApplicationContext(), "重复密码错误",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Editor edt = settings.edit();
                if (pswFlag == 0)
                    edt.putString("pwd", inputPwd);
                else if (pswFlag == 1)
                    edt.putString("cbyPwd", inputPwd);
                edt.commit();
                Toast.makeText(getApplicationContext(), "密码修改成功",
                        Toast.LENGTH_LONG).show();

                finish();
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "修改管理员密码");
        menu.add(0, 1, 1, "修改抄表员密码");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()) {
            case 0:
                changePwd(0);
                break;
            case 1:
                changePwd(1);
                break;
        }
        return true;
    }
}
