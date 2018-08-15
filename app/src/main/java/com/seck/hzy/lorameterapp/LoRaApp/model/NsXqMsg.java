package com.seck.hzy.lorameterapp.LoRaApp.model;

import java.util.List;

/**
 * Created by limbo on 2018/3/12.
 */

public class NsXqMsg {
    List<CjjDetails> CjjDetails;
    String XqName;
    String XqId;

    public List<CjjDetails> getCjjDetails() {
        return CjjDetails;
    }

    public void setCjjDetails(List<CjjDetails> cjjDetails) {
        CjjDetails = cjjDetails;
    }

    public String getXqName() {
        return XqName;
    }

    public void setXqName(String xqName) {
        XqName = xqName;
    }

    public String getXqId() {
        return XqId;
    }

    public void setXqId(String xqId) {
        XqId = xqId;
    }


}
