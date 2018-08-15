package com.seck.hzy.lorameterapp.LoRaApp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limbo on 2018/3/21.
 */
public class CjjDetails {
    String CjjAddr;
    String CjjId;
    String CjjType;

    List<MeterDetails> MeterDetails;


    public List<MeterDetails> getMeterDetails() {
        return MeterDetails;
    }

    public String getCjjType() {
        return CjjType;
    }

    public void setCjjType(String cjjType) {
        CjjType = cjjType;
    }
    public void setMeterDetails(ArrayList<MeterDetails> meterDetails) {
        MeterDetails = meterDetails;
    }



    public String getCjjAddr() {
        return CjjAddr;
    }

    public void setCjjAddr(String cjjAddr) {
        CjjAddr = cjjAddr;
    }

    public String getCjjId() {
        return CjjId;
    }

    public void setCjjId(String cjjId) {
        CjjId = cjjId;
    }


}
