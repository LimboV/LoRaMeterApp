package com.seck.hzy.lorameterapp.LoRaApp.model;

/**
 * Created by limbo on 2018/3/21.
 */

public class MeterDetails {
    String MeterId;//表号
    String MeterNumber;//
    String MeterType;//
    String UserName;
    String UserAddr;
    String UserNumber;
    String LastData;
    String LastDate;
    String Data;
    String Date;

    public String getMeterType() {
        return MeterType;
    }

    public void setMeterType(String meterType) {
        MeterType = meterType;
    }
    public String getUserNumber() {
        return UserNumber;
    }

    public void setUserNumber(String userNumber) {
        UserNumber = userNumber;
    }

    public String getMeterId() {
        return MeterId;
    }

    public void setMeterId(String meterId) {
        MeterId = meterId;
    }

    public String getMeterNumber() {
        return MeterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        MeterNumber = meterNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserAddr() {
        return UserAddr;
    }

    public void setUserAddr(String userAddr) {
        UserAddr = userAddr;
    }

    public String getLastData() {
        return LastData;
    }

    public void setLastData(String lastData) {
        LastData = lastData;
    }

    public String getLastDate() {
        return LastDate;
    }

    public void setLastDate(String lastDate) {
        LastDate = lastDate;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

}
