package com.seck.hzy.lorameterapp.LoRaApp;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.model.CheckEntity;
import org.lzh.framework.updatepluginlib.model.HttpMethod;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2017/3/1.
 */

public class App extends Application {
    public static final String UPDATE_URL =
            "http://www.seck.com.cn/AppUpdate/AppUpdate.asmx/checkUpdate";

    @Override
    public void onCreate() {
        super.onCreate();
        initUpdateVersion();
    }

    private void initUpdateVersion() {
        Map<String, String> params = new HashMap<>();
        params.put("appCode", "4");
        params.put("versionCode", String.valueOf(getAppVersionCode(this)));
        UpdateConfig.getConfig()
                .checkEntity(new CheckEntity()
                        .setUrl(UPDATE_URL)
                        .setMethod(HttpMethod.GET)
                        .setParams(params))
//                .url("http://www.seck.com.cn/AppUpdate/AppUpdate.asmx/download?appCode=4")
                .strategy(new UpdateStrategy() {
                    @Override
                    public boolean isShowUpdateDialog(Update update) {
                        return true;
                    }

                    @Override
                    public boolean isAutoInstall() {
                        return true;
                    }

                    @Override
                    public boolean isShowDownloadDialog() {
                        return true;
                    }
                })
                .jsonParser(new UpdateParser() {
                    @Override
                    public <T extends Update> T parse(String response) {
                        Log.d("limbo",response);
                        Update update = new Update("seck");
                        response = response.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                        response = response.replace("<string xmlns=\"http://tempuri.org/\">", "");
                        response = response.replace("</string>", "");
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            update.setVersionCode(jsonObject.getInt("versionCode"));
                            update.setVersionName(jsonObject.getString("versionName"));
                            update.setUpdateUrl(jsonObject.getString("url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return (T) update;
                    }
                });
    }

    private int getAppVersionCode(Context context) {
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
