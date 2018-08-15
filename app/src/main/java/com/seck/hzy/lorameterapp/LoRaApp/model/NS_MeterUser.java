package com.seck.hzy.lorameterapp.LoRaApp.model;

import android.graphics.Bitmap;

/**
 * Created by limbo on 2018/4/1.
 */

public class NS_MeterUser {
    public int XqId;
    public int CjjId;
    public int MeterId;
    public String MeterNumber;
    public String UserName;
    public String UserAddr;

    public String getMeterType() {
        return MeterType;
    }

    public void setMeterType(String meterType) {
        MeterType = meterType;
    }

    public String MeterType;

    public float LastData;

    public String getLasteDate() {
        return LasteDate;
    }

    public void setLasteDate(String lasteDate) {
        LasteDate = lasteDate;
    }

    public String LasteDate;
    public float Data;
    public String Date;
    public Bitmap Pic;

    public NS_MeterUser() {

    }

}
