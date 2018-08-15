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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.NS_Cjj;
import com.seck.hzy.lorameterapp.LoRaApp.model.NsXqMsg;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NS_DataHelper;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.LoRa_TYPTActivity.timeOut;

/**
 * Created by limbo on 2018/3/22.
 */

public class NS_UploadActivity extends Activity {
    @BindView(R.id.NS_XqSelectActivity_lv_xqlist)
    ListView NS_XqSelectActivity_lv_xqlist;
    @BindView(R.id.NS_XqSelectActivity_btn_confirm)
    Button NS_XqSelectActivity_btn_confirm;
    @BindView(R.id.NS_XqSelectActivity_btn_cancel)
    Button NS_XqSelectActivity_btn_cancel;
    public List<NS_Cjj> xqList = new ArrayList<>();//小区列表
    private ListAdapter adapter;
    private ProgressDialog progressBar = null;

    public final static int CONNECT_TIMEOUT = 600;
    public final static int READ_TIMEOUT = 1000;
    public final static int WRITE_TIMEOUT = 600;
    public static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
            .build();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ns_activity_xqselect);
        ButterKnife.bind(this);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        NS_DataHelper.getdb();//获取数据库实例
        adapter = new ArrayAdapter<String>(NS_UploadActivity.this, android.R.layout.simple_list_item_multiple_choice, getXq());
        NS_XqSelectActivity_lv_xqlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        NS_XqSelectActivity_lv_xqlist.setAdapter(adapter);
        NS_XqSelectActivity_lv_xqlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new SweetAlertDialog(NS_UploadActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("请确认是否要删除该小区?")
                        .setCancelText("取消")
                        .setConfirmText("确认")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Log.d("limboV", "sure");
                                sweetAlertDialog.cancel();
                                NS_DataHelper.deleteXqAndMeter(xqList.get(position).XqId);
                                getXq();
                                adapter = new ArrayAdapter(NS_UploadActivity.this, android.R.layout.simple_list_item_1, getXq());
                                NS_XqSelectActivity_lv_xqlist.setAdapter(adapter);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Log.d("limboV", "cancel");
                                new SweetAlertDialog(NS_UploadActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("取消!")
                                        .setContentText("你已经取消!")
                                        .show();
                                sDialog.cancel();
                            }
                        }).show();
                return false;
            }
        });
        /**
         * 上传
         */
        NS_XqSelectActivity_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
        /**
         * 退出
         */
        NS_XqSelectActivity_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 上传小区
     */
    private void uploadData() {
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
        final int count = selectItems.size();

        if (positionArr.size() == 0) {
            Log.d("limbo", "未选择小区的情况下点击上传");
            return;
        }

        if (selectItems.size() <= 0) {
            Toast.makeText(NS_UploadActivity.this, "未选择小区!", Toast.LENGTH_LONG).show();
            return;
        }
//        prepareTimeStart(300);//超时处理 每个小区3秒
        progressBar.setIndeterminate(false);
        progressBar.setMax(selectItems.size());
        progressBar.setMessage("上传  进度");
        progressBar.setProgress(0);
        progressBar.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String errorMsg = "勾选的第";
                    boolean msgErrorFlag = false;
                    for (int x = 0; x < selectItems.size(); x++) {
                        final int i = Integer.parseInt(selectItems.get(x).toString());
                        int xqid = xqList.get(i).XqId;
                        String xqname = xqList.get(i).XqName;
                        NsXqMsg nsXqMsg = NS_DataHelper.uploadXqMsg(xqid, xqname);
                        //申明给服务端传递一个json串
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                                .build();
                        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                        //json为String类型的json数据
                        String gsonString = null;
                        Gson gson = new Gson();
                        if (gson != null) {
                            gsonString = "[" + gson.toJson(nsXqMsg) + "]";
                            //                            gsonString = "[{\"CjjDetails\":[{\"CjjAddr\":\"60\",\"CjjId\":\"60\",\"CjjType\":\"\",\"MeterDetails\":[{\"Data\":\"1\",\"Date\":\"12\",\"LastData\":\"\",\"LastDate\":\"\",\"MeterId\":\"1\",\"MeterNumber\":\"\",\"MeterType\":\"\",\"UserAddr\":\"\",\"UserName\":\"\"}]}],\"XqId\":\"21\",\"XqName\":\"小区21\"}]";
                        }
                        Log.d("limbo", "length:" + gsonString.length());
                        Log.d("limbo", gsonString);
                        MediaType mediaType = MediaType.parse("application/json");//"application/json; charset=utf-8")
                        RequestBody requestBody = RequestBody.create(mediaType, gsonString);
                        //创建一个请求对象
                        Request request = new Request.Builder()
                                .url(MenuActivity.ns_url + "/WcfRest/Service/UpLoadReadData")// + MenuActivity.ns_id
                                .post(requestBody)
                                .build();
                        //发送请求获取响应
                        Log.d("limbo", "url:" + MenuActivity.ns_url + "/WcfRest/Service/UpLoadReadData");

                        Response response = okHttpClient.newCall(request).execute();

                        //判断请求是否成功
                        if (response.isSuccessful()) {
                            //打印服务端返回结果
                            String xx = response.body().string();
                            Log.i("limbo", "success:" + xx);
                            if (xx.equals("0")||xx.contains("-")) {
                                errorMsg = errorMsg + i + "、";
                                msgErrorFlag = true;
                            } else {
                                Message message = new Message();
                                message.what = 0x00;
                                message.obj = i;
                                handler.sendMessage(message);
                            }
                        } else {
                            Message message = new Message();
                            message.what = 0x98;
                            handler.sendMessage(message);
                            throw new IOException("limbo:" + response);
                        }
                        Thread.sleep(1000);

                    }
                    if (msgErrorFlag) {
                        errorMsg = errorMsg + "小区上传失败。";
                        Log.d("limbo", errorMsg);
                        Message message = new Message();
                        message.what = 0x97;
                        message.obj = errorMsg;
                        handler.sendMessage(message);
                    }else {
                        Message message = new Message();
                        message.what = 0x01;
                        handler.sendMessage(message);
                    }

                } catch (
                        Exception e)

                {
                    Log.d("limbo", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 0x99;
                            handler.sendMessage(message);
                        }
                    });
                }
            }
        }).

                start();
    }

    private List<String> getXq() {
        List<String> data = new ArrayList<>();
        try {
            xqList = NS_DataHelper.getXq();//从数据库提取出
            for (NS_Cjj cjj : xqList) {
                data.add(cjj.XqName + "(" + cjj.XqId + ")");
            }
        } catch (Exception e) {
            Log.e("limbo", e.toString());
            HintDialog.ShowHintDialog(this, "未找到数据,请检查数据库文件是否存在！", "错误");
        }
        return data;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 *
                 */
                case 0x00:
                    timeOut = true;
                    int i = (int) msg.obj;
                    progressBar.setProgress(i + 1);
                    break;
                /**
                 *
                 */
                case 0x01:
                    timeOut = true;
                    new SweetAlertDialog(NS_UploadActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("成功")
                            .setContentText("上传完成!")
                            .show();
                    progressBar.dismiss();

                    break;
                case 0x97:
                    String x = (String) msg.obj;
                    timeOut = true;
                    progressBar.dismiss();
                    new SweetAlertDialog(NS_UploadActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("失败")
                            .setContentText(x)
                            .show();
                    break;
                case 0x98:
                    timeOut = true;
                    progressBar.dismiss();
                    try {
                        new SweetAlertDialog(NS_UploadActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("错误")
                                .setContentText("上传失败!")
                                .show();
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                        Toast.makeText(NS_UploadActivity.this, "上传失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 0x99:
                    timeOut = true;
                    progressBar.dismiss();
                    try {
                        new SweetAlertDialog(NS_UploadActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("错误")
                                .setContentText("网络异常!")
                                .show();

                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                        Toast.makeText(NS_UploadActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }

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
}
