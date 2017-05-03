package com.seck.hzy.lorameterapp.LoRaApp.model;

import java.util.List;

public class ClientMeterData {

    /// 表唯一id Char(17) 4位小区号，4位楼宇 ，2位通信机 2为采集机，2位端口 3位表序号
    public String MeterID;

    /// 抄表数据
    public Float Data;

    /// 抄表时间
    public String DataTime;

    ///   抄表是否正常标志 true 异常  false 正常
    public Boolean IsFault;

    ///  true:为二值化图像; false:灰度图片
    public Boolean IsBinaryzation;

    /// 图片字节数据集合 集合的第0个 对应 实际水表字轮的最末（右）位
    public List<int[]> Images;

    /// 读实时数据返回  识别 结果和 置信度等
    public int[] RecognitionResult;

    /// 本月用水
    public Float WATER;

    /// 收费金额
    public Float FEE;

    /// 是否已经打印收费凭条
    public Boolean ISPRINT;
}
