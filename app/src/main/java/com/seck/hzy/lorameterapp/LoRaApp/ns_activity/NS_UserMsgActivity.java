package com.seck.hzy.lorameterapp.LoRaApp.ns_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.LoRa_TYPTActivity.timeOut;

/**
 * Created by limbo on 2018/3/26.
 */

public class NS_UserMsgActivity extends Activity {
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.btn_1)
    Button btn_1;
    private ProgressDialog progressBar = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ns_activity_usermsg);
        ButterKnife.bind(this);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loadUser();
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTimeStart(100);//超时处理
                progressBar.setIndeterminate(true);
                progressBar.setMessage("正在验证用户信息...");
                progressBar.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                            Request request = new Request.Builder()
                                    .url(editText3.getText().toString() + "/WcfRest/Service/Login/" + editText.getText().toString() + "/" + editText2.getText().toString())//请求接口。如果需要传参拼接到接口后面。
                                    //                                    .url("http://192.168.1.61:9800/WcfRest/Service/GetXqDetails/1/153")//请求接口。如果需要传参拼接到接口后面。
                                    .build();//创建Request 对象
                            Response response = null;
                            response = client.newCall(request).execute();//得到Response 对象
                            if (response.isSuccessful()) {
                                Log.d("limboV", "response.code()==" + response.code());
                                Log.d("limboV", "response.message()==" + response.message());
                                //                        Log.d("limboV", "res==" + response.body().string());
                                //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                                String responseX = response.body().string().replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                                responseX = responseX.replace("<string xmlns=\"http://tempuri.org/\">", "");
                                responseX = responseX.replace("</string>", "");
                                responseX = responseX.replaceAll("\"","");
                                Message message = new Message();
                                message.what = 0x00;
                                message.obj = responseX;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            Log.d("limbo", e.toString());
                            Message message = new Message();
                            message.what = 0x99;
                            handler.sendMessage(message);
                        }
                    }
                }).start();


            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 * 下载小区列表
                 */
                case 0x00:
                    timeOut = true;
                    progressBar.dismiss();
                    String userID = (String) msg.obj;
                    if (userID.equals("-1")) {
                        new SweetAlertDialog(NS_UserMsgActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("错误")
                                .setContentText("账号密码验证错误，请重试!")
                                .show();

                        Log.d("limbo", "-1");
                    } else {
                        new SweetAlertDialog(NS_UserMsgActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("成功")
                                .setContentText("账号验证通过!")
                                .show();
                        saveUser(userID);
                        Log.d("limbo", userID);
                    }

                    //                finish();
                    break;
                case 0x98:
                    progressBar.dismiss();
                    new SweetAlertDialog(NS_UserMsgActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("错误")
                            .setContentText("无法访问!")
                            .show();
                    break;
                case 0x99:
                    progressBar.dismiss();
                    timeOut = true;
                    new SweetAlertDialog(NS_UserMsgActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("错误")
                            .setContentText("网络异常!")
                            .show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 开始协议设定时间
     */
    private void prepareTimeStart(final int timeMax) {
        timeOut = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < timeMax && !timeOut; i++) {
                        Thread.sleep(100);
                    }
                    if (!timeOut) {
                        Message message = new Message();
                        message.what = 0x98;
                        handler.sendMessage(message);
                    }
                    timeOut = true;
                } catch (Exception e) {

                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        timeOut = true;
        super.onDestroy();
    }

    /**
     * 使用SharePreferences保存用户信息
     */
    public void saveUser(String userID) {
        SharedPreferences.Editor editor = getSharedPreferences("userMsg", MODE_PRIVATE).edit();
        editor.putString("editText", editText.getText().toString());
        editor.putString("editText2", editText2.getText().toString());
        editor.putString("editText3", editText3.getText().toString());
        editor.putString("userID", userID);
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    public void loadUser() {
        SharedPreferences pref = getSharedPreferences("userMsg", MODE_PRIVATE);
        editText.setText(pref.getString("editText", ""));
        editText2.setText(pref.getString("editText2", ""));
        editText3.setText(pref.getString("editText3", ""));

    }
}
