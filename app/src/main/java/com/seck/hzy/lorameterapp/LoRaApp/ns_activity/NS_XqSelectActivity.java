package com.seck.hzy.lorameterapp.LoRaApp.ns_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.NsXq;
import com.seck.hzy.lorameterapp.LoRaApp.model.NsXqMsg;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NS_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.LoRa_TYPTActivity.timeOut;

/**
 * Created by limbo on 2018/3/8.
 */

public class NS_XqSelectActivity extends Activity {
    @BindView(R.id.NS_XqSelectActivity_lv_xqlist)
    ListView NS_XqSelectActivity_lv_xqlist;
    @BindView(R.id.NS_XqSelectActivity_btn_confirm)
    Button NS_XqSelectActivity_btn_confirm;
    @BindView(R.id.NS_XqSelectActivity_btn_cancel)
    Button NS_XqSelectActivity_btn_cancel;
    private ProgressDialog progressBar = null;
    private List<String> list = new ArrayList<>();//存放小区展示列表
    private List<String> IDlist = new ArrayList<>();//存放小区展示列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ns_activity_xqselect);
        ButterKnife.bind(this);
        NS_DataHelper.getdb();//获取数据库实例
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        NS_XqSelectActivity_lv_xqlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        getXqList();

        NS_XqSelectActivity_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadData();
            }
        });
        NS_XqSelectActivity_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 下载小区
     */
    private void downloadData() {
        final List<String> selectItems = new ArrayList<>();

        SparseBooleanArray positionArr = NS_XqSelectActivity_lv_xqlist.getCheckedItemPositions();
        for (int i = 0; i < positionArr.size(); i++) {
            if (positionArr.valueAt(i)) {
                int idx = positionArr.keyAt(i);
                selectItems.add(idx + "");
            }
        }
        for (int i = 0; i < selectItems.size(); i++) {
            Log.d("limbo", "选中项:" + selectItems.get(i).toString());
        }

        if (positionArr.size() == 0) {
            Log.d("limbo", "未选择小区的情况下点击下载");
            return;
        }

        if (selectItems.size() <= 0) {
            Toast.makeText(NS_XqSelectActivity.this, "未选择小区!", Toast.LENGTH_LONG).show();
            return;
        }
        prepareTimeStart(200);//超时处理 每个小区3秒
        progressBar.setIndeterminate(false);
        progressBar.setMax(selectItems.size());
        progressBar.setMessage("下载进度");
        progressBar.setProgress(0);
        progressBar.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < selectItems.size(); i++) {
                        progressBar.setProgress(i);
                        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                        Request request = new Request.Builder()
                                .url(MenuActivity.ns_url + "/WcfRest/Service/GetXqDetails/" + MenuActivity.ns_id + "/" + IDlist.get(Integer.parseInt(selectItems.get(i).toString())))//请求接口。如果需要传参拼接到接口后面。
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
                            Log.d("limbo", responseX);
                            Gson gson = new Gson();//使用Gson解析
                            NsXqMsg xqMsg = gson.fromJson(responseX, NsXqMsg.class);//获取单个小区完整信息
                            Log.d("limbo", xqMsg.getXqId() + xqMsg.getXqName());
                            Log.d("limbo", xqMsg.getCjjDetails().get(0).getCjjId());
                            Log.d("limbo", xqMsg.getCjjDetails().get(0).getMeterDetails().get(0).getMeterId());
                            NS_DataHelper.addXqMsg(xqMsg);

                            Thread.sleep(1000);
                            Message message = new Message();
                            message.obj = i;
                            message.what = 0x01;
                            handler.sendMessage(message);

                        }

                    }

                    Message message = new Message();
                    message.what = 0x02;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    Log.d("limbo", e.toString());
                    Message message = new Message();
                    message.what = 0x99;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /**
     * 初始化获得小区列表 http://192.168.1.61:9800
     */
    private void getXqList() {
        prepareTimeStart(30);//超时处理
        progressBar.setIndeterminate(true);
        progressBar.setMessage("获取小区信息");
        progressBar.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url(MenuActivity.ns_url + "/WcfRest/Service/GetAllXqs/" + MenuActivity.ns_id)//请求接口。如果需要传参拼接到接口后面。
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
                        Log.d("limbo", responseX);
                        Gson gson = new Gson();//使用Gson解析
                        List<NsXq> xqList = gson.fromJson(responseX, new TypeToken<List<NsXq>>() {
                        }.getType());
                        for (NsXq xq : xqList) {
                            list.add("小区名:" + xq.getXqName() + "   小区ID:" + xq.getXqId());
                            IDlist.add(xq.getXqId());
                        }
                        Message message = new Message();
                        message.what = 0x00;
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 * 下载小区列表
                 */
                case 0x00:

                    progressBar.dismiss();
                    timeOut = true;
                    NS_XqSelectActivity_lv_xqlist.setAdapter(new ArrayAdapter<String>(NS_XqSelectActivity.this,
                            android.R.layout.simple_list_item_multiple_choice, list));
                    break;
                /**
                 * 更新进度条
                 */
                case 0x01:
                    int responseX = (int) msg.obj;
                    progressBar.setProgress(responseX + 1);
                    timeOut = true;
                    break;
                /**
                 * 下载勾选小区
                 */
                case 0x02:
                    new SweetAlertDialog(NS_XqSelectActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("成功")
                            .setContentText("保存数据!")
                            .show();
                    timeOut = true;
                    progressBar.dismiss();

                    break;
                case 0x98:
                    progressBar.dismiss();
                    new SweetAlertDialog(NS_XqSelectActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("错误")
                            .setContentText("无法访问!")
                            .show();
                    break;
                case 0x99:
                    timeOut = true;
                    progressBar.dismiss();
                    new SweetAlertDialog(NS_XqSelectActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("错误")
                            .setContentText("网络异常!")
                            .show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        timeOut = true;
        super.onDestroy();
    }
}
