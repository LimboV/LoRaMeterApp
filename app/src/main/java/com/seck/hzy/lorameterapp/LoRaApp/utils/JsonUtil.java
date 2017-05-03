package com.seck.hzy.lorameterapp.LoRaApp.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtil {
	private static final Gson mGson = new Gson();
	
	public static String toJson(Object src) {
		return mGson.toJson(src);
	}
	
	public static <T> T fromJson(String json, Type typeOfT) {
		return mGson.fromJson(json, typeOfT);
	}
}
