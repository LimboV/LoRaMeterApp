package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.app.Activity;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.seck.hzy.lorameterapp.R;

/**
 * Created by limbo on 2017/2/6.
 */
public class LoRa_NetTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(){
        setContentView(R.layout.lora_nettext);

        AsyncHttpClient client = new AsyncHttpClient();//使用HttpClient执行网络请求
        RequestParams params = new RequestParams();//请求参数

    }
}
