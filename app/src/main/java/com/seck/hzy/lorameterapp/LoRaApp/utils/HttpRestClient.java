package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.content.SharedPreferences;
import android.util.Log;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;

public class HttpRestClient {

	private static final String BASE_URL = "http://%s/WcfRest/Service/%s"; //www.seck.com.cn:9090

	private static final int timeoutConnection = 10000;
	private static final int timeoutSocket = 10000;

	//private static HttpClient client = new DefaultHttpClient();

	//static {
	//	HttpParams httpParameters = client.getParams();
	//	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	//	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	//}

	public static <T> T get(String url, Type typeOfT) throws IOException {
		HttpGet request = new HttpGet(getAbsoluteUrl(url));
		HttpClient client = new DefaultHttpClient();
		HttpParams httpParameters = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpResponse response = client.execute(request);

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String str = EntityUtils.toString(response.getEntity());
			Log.i("HttpRest", "rec:" + str);
			return JsonUtil.fromJson(str, typeOfT);
		}

		return null;
	}

	public static <T> T post(String url, Object param, Type typeOfT) throws IOException {
		HttpPost request = new HttpPost(getAbsoluteUrl(url));
		HttpClient client = new DefaultHttpClient();
		HttpParams httpParameters = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		//Log.i("HttpRest", "post:" + JsonUtil.toJson(param));
		StringEntity entity = new StringEntity(JsonUtil.toJson(param), HTTP.UTF_8);
		entity.setContentType("application/json; charset=utf-8");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String str = EntityUtils.toString(response.getEntity());
			Log.i("limbo", "rec:" + str);
			return JsonUtil.fromJson(str, typeOfT);
		}

		return null;
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(
				MenuActivity.PREF_NAME, 0);
		String server = settings.getString("SERVER",
				MenuActivity.DEFAULT_SERVER); // 服务器地址
		String urlout = String.format(BASE_URL, server, relativeUrl);
		return urlout;
	}
}
