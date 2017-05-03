package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/**
	 *
	 * 判断网络是否开启
	 *
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService(
				Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if ((netInfo != null) && (netInfo.isAvailable())) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * 获得当前的系统时间
	 *
	 * @return
	 */
	public String getSysTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * from byte to int, because of byte in java is signed
	 */
	public static int[] toIntArray(byte[] bytes) {
		if(bytes == null) {
			return null;
		}
		int[] data = new int[bytes.length];
		for(int i=0; i<bytes.length; i++) {
			data[i] = bytes[i] >= 0 ? (int)bytes[i] : (int)(bytes[i] + 256);
		}
		return data;
	}
}
