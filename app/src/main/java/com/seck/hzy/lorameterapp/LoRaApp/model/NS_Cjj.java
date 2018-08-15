package com.seck.hzy.lorameterapp.LoRaApp.model;

/**
 * Created by limbo on 2018/4/1.
 */

public class NS_Cjj {
    public int XqId;
    public String XqName;
    public int CjjId;
    public String CjjAddr;

    public String getCjjType() {
        return CjjType;
    }

    public void setCjjType(String cjjType) {
        CjjType = cjjType;
    }

    public String CjjType;

    public NS_Cjj() {
    }

    public NS_Cjj(int XqId, String XqName, int CjjId, String CjjAddr) {
        this.XqId = XqId;
        this.XqName = XqName;
        this.CjjId = CjjId;
        this.CjjAddr = CjjAddr;

    }
}
