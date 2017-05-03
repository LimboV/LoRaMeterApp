package com.seck.hzy.lorameterapp.LoRaApp.utils;


import com.google.gson.reflect.TypeToken;
import com.seck.hzy.lorameterapp.LoRaApp.model.Building;
import com.seck.hzy.lorameterapp.LoRaApp.model.ClientMeterData;
import com.seck.hzy.lorameterapp.LoRaApp.model.Colector;
import com.seck.hzy.lorameterapp.LoRaApp.model.Meter;
import com.seck.hzy.lorameterapp.LoRaApp.model.Residential;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SkRestClient {

	public static List<Residential> getAllResidentials() throws IOException {
		Type type = new TypeToken<List<Residential>>() {}.getType();
		return HttpRestClient.get("getAllResidentials", type);
	}
	
	public static List<Building> getBuildingsByResidentialQuarterID(List<Integer> residentialQuarterIDs) throws IOException {
		if(residentialQuarterIDs == null || residentialQuarterIDs.size() <= 0) {
			return null;
		}
		Type type = new TypeToken<List<Building>>() {}.getType();
		return HttpRestClient.post("getBuildingsByResidentialQuarterID", residentialQuarterIDs, type);
	}
	
	public static List<Colector> getColectorsByResidentialQuarterID(List<Integer> residentialQuarterIDs) throws IOException {
		if(residentialQuarterIDs == null || residentialQuarterIDs.size() <= 0) {
			return null;
		}
		Type type = new TypeToken<List<Colector>>() {}.getType();
		return HttpRestClient.post("getColectorsByResidentialQuarterID", residentialQuarterIDs, type);
	}
	
	public static List<Meter> getMetersByResidentialQuarterID(List<Integer> residentialQuarterIDs) throws IOException {
		if(residentialQuarterIDs == null || residentialQuarterIDs.size() <= 0) {
			return null;
		}
		Type type = new TypeToken<List<Meter>>() {}.getType();
		return HttpRestClient.post("getMetersByResidentialQuarterID", residentialQuarterIDs, type);
	}
	
	public static int uploadClientMeterData(List<ClientMeterData> meterdataList) throws IOException {
		if(meterdataList == null || meterdataList.size() <= 0) {
			return 0;
		}
		Integer result = HttpRestClient.post("uploadMeterData", meterdataList, Integer.class);
		if(result == null) {
			return 0;
		}
		
		return result;
	}
	
	public static int uploadMeterData(List<Meter> meterList) throws IOException {
		if(meterList == null) {
			return 0;
		}
		
		List<ClientMeterData> dataList = new ArrayList<ClientMeterData>();
		for(Meter meter : meterList) {
			ClientMeterData data = new ClientMeterData();
			data.MeterID = meter.MeterID;
			data.Data = meter.Data;
			data.DataTime = meter.DataTime;
			data.IsFault = meter.IsFault;
			data.IsBinaryzation = meter.IsBinaryzation;
			data.Images = new ArrayList<int[]>();
			if(meter.Images != null) {
				for(byte[] b : meter.Images) {
					data.Images.add(Utils.toIntArray(b));
				}
			}
			data.RecognitionResult = Utils.toIntArray(meter.RecognitionResult);
			data.WATER = meter.WATER;
			dataList.add(data);
		}
		
		int count = 0;
		int len = 5;
		for(int i = 0; i < dataList.size(); i += len) {
			int re = uploadClientMeterData(dataList.subList(i, Math.min(i + len, dataList.size())));
			count += re;
		}
		return count;
	}
}
