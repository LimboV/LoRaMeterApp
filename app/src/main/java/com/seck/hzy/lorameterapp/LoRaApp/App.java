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
    public static final String UPDATE_URL = "http://www.seck.com.cn/AppUpdate/AppUpdate.asmx/checkUpdate";
    public static final String UPDATE_URL1 = "http://www.seck.com.cn/AppUpdate/AppUpdate.asmx/checkUpdate?appCode=4&versionCode=";
    private int versionCode= 0 ;

    @Override
    public void onCreate() {
        super.onCreate();
        initUpdateVersion();
    }

    private void initUpdateVersion() {
        Map<String, String> params = new HashMap<>();
        versionCode = getAppVersionCode(this);
        params.put("appCode", "4");
        params.put("versionCode", String.valueOf(versionCode));
        UpdateConfig.getConfig()
//                .url(UPDATE_URL1+String.valueOf(versionCode))
                .checkEntity(new CheckEntity()
                        .setUrl(UPDATE_URL)
                        .setMethod(HttpMethod.GET)
                        .setParams(params))
                .strategy(new UpdateStrategy() {
                    @Override
                    public boolean isShowUpdateDialog(Update update) {
                        // 有新更新直接展示
                        return true;
                    }

                    @Override
                    public boolean isAutoInstall() {
                        return true;
                    }

                    @Override
                    public boolean isShowDownloadDialog() {
                        // 展示下载进度
                        return true;
                    }
                })
                .jsonParser(new UpdateParser() {
                    @Override
                    public  Update parse(String response) {
                        /* 此处根据上面url或者checkEntity设置的检查更新接口的返回数据response解析出
                         * 一个update对象返回即可。更新启动时框架内部即可根据update对象的数据进行处理
                         */
                        Update update = new Update("seck");
                        response = response.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                        response = response.replace("<string xmlns=\"http://tempuri.org/\">", "");
                        response = response.replace("</string>", "");
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);

                            Log.d("limbo", jsonObject.getInt("versionCode") + "");
                            Log.d("limbo", jsonObject.getString("versionName"));
                            Log.d("limbo", jsonObject.getString("url"));

                            // 此apk包的版本号
                            update.setVersionCode(jsonObject.getInt("versionCode"));
                            // 此apk包的版本名称
                            update.setVersionName(jsonObject.getString("versionName"));
                            // 此apk包的下载地址
                            update.setUpdateUrl(jsonObject.getString("url"));

                            if (jsonObject.getInt("versionCode") == versionCode){
                                Log.d("limbo","当前已是最新版本");

                            }else {
                                Log.d("limbo","发现新版本:"+jsonObject.getInt("versionCode"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return update;
                    }
                })

        ;
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
