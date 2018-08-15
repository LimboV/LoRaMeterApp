package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by limbo on 2018/5/7.
 * 厚膜表
 */

public class LoRa_TYPTtoHmActivity extends Activity {
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_aimJDID)
    EditText et_aimJDID;//目标节点ID
    @BindView(R.id.LoRa_TYPtoHmActivity_et_aimNetID)
    EditText et_aimNetID;//目标网络ID
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_aimNetFreq)
    EditText et_aimNetFreq;//目标网络频率
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_aimLYLJ1)
    EditText et_aimLYLJ1;//目标路由路径-一层路径
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_aimLYLJ2)
    EditText et_aimLYLJ2;//目标路由路径-二层路径
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_aimLYLJ3)
    EditText et_aimLYLJ3;//目标路由路径-三层路径

    @BindView(R.id.LoRa_TYPTtoHmActivity_et_endSingal)
    EditText et_endSingal;//末端信号
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_LYSignal1)
    EditText et_LYSignal1;//路由信号-1层
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_LYSignal2)
    EditText et_LYSignal2;//路由信号-2层
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_LYSignal3)
    EditText et_LYSignal3;//路由信号-3层
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_electric)
    EditText et_electric;//电池电压
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_data)
    EditText et_data;//表数据
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_state)
    EditText et_state;//表状态
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_meterNum)
    EditText et_meterNum;//表序号
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_meterNum1)
    EditText et_meterNum1;//表序号
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_meterValue)
    EditText et_meterValue;//底度
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_fcNum)
    EditText et_fcNum;//分采号
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_meterCount)
    EditText et_meterCount;//水表总数
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_meterNum2)
    EditText et_meterNum2;//表序号
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_meterAddr)
    EditText et_meterAddr;//表地址
    @BindView(R.id.LoRa_TYPTtoHmActivity_et_meterNum3)
    EditText et_meterNum3;//表序号

    @BindView(R.id.btn_reset1)
    Button btn_reset1;//复位界面
    @BindView(R.id.btn_reset2)
    Button btn_reset2;//复位转换器
    @BindView(R.id.btn_importMeterMsg)
    Button btn_importMeterMsg;//导入水表档案
    @BindView(R.id.btn_exportMeterMsg)
    Button btn_exportMeterMsg;//导出水表档案
    @BindView(R.id.btn_clearAllMeterMsg)
    Button btn_clearAllMeterMsg;//清空节点所有表计档案
    @BindView(R.id.btn_deleteSingleMeterMsg)
    Button btn_deleteSingleMeterMsg;//删除单个表计档案
    @BindView(R.id.btn_addSingleMeterMsg)
    Button btn_addSingleMeterMsg;//添加单个表计档案
    @BindView(R.id.btn_getALLFcMsg)
    Button btn_getALLFcMsg;//提取分采所有数据
    @BindView(R.id.btn_freezeALLFcMsg)
    Button btn_freezeALLFcMsg;//冻结分采所有数据
    @BindView(R.id.btn_get1)
    Button btn_get1;//读取1
    @BindView(R.id.btn_get2)
    Button btn_get2;//读取2
    @BindView(R.id.btn_write1)
    Button btn_write1;//写入1
    @BindView(R.id.btn_write2)
    Button btn_write2;//写入2
    @BindView(R.id.btn_setMeterValue)
    Button btn_setMeterValue;//写入底度
    @BindView(R.id.btn_getMeterData)
    Button btn_getMeterData;//实时点抄

    @BindView(R.id.ll_dis)
    LinearLayout ll_dis;

    @BindView(R.id.ll_dis2)
    LinearLayout ll_dis2;


    String aimJDID, aimNetID, aimNetFreq, aimLYLJ1,
            aimLYLJ2, aimLYLJ3, endSingal, LYSignal1,
            LYSignal2, LYSignal3, electric, meterData,
            state, meterNum, meterNum1, meterValue, fcNum,
            meterCount, meterNum2, meterAddr, meterNum3, time;

    boolean timeOut = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lora_activity_typttohm);
        ButterKnife.bind(this);
        MenuActivity.btAuto = true;
        ll_dis.setVisibility(View.GONE);
        ll_dis2.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MenuActivity.btAuto) {
                    try {
                        Thread.sleep(1500);
                        String resultMsg = "";
                        resultMsg = resultMsg + HzyUtils.GetBlueToothMsg();
                        if (resultMsg.length() != 0) {
                            resultMsg = resultMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", "get:" + resultMsg);
                            Message message = new Message();
                            message.obj = resultMsg;
                            message.what = 0x00;
                            mHandler.sendMessage(message);
                            resultMsg = "";
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        /**
         * 设置分采号
         */
        btn_write1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                if (HzyUtils.isEmpty(fcNum) || Integer.parseInt(fcNum) < 1 || Integer.parseInt(fcNum) > 255) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("分采号范围(1~255)")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }

                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                String sendMsg = "680018" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "8401" + HzyUtils.isLength(fcNum, 2);
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "设置分采号" + sendMsg);
                MenuActivity.sendCmd(sendMsg);

            }
        });
        /**
         * 读取分采号
         */
        btn_get1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                String sendMsg = "68001d" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "840d" + time;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "读取分采号" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
            }
        });

        /**
         * 设置水表总数
         */
        btn_write2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                if (HzyUtils.isEmpty(meterCount) || Integer.parseInt(meterCount) < 1 || Integer.parseInt(meterCount) > 64) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("水表总数输入错误，请重新输入，水表总数应设置在1到64之间(包含1和64)")
                            .show();
                    return;
                }
                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                meterCount = HzyUtils.isLength(HzyUtils.toHexString(meterCount), 2);
                String sendMsg = "680018" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "840b" + HzyUtils.isLength(meterCount, 2);
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "设置水表总数" + sendMsg);
                MenuActivity.sendCmd(sendMsg);

            }
        });

        /**
         * 读取水表总数
         */
        btn_get2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);

                String sendMsg = "68001d" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "840c" + time;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "读取水表总数" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 添加单个表计档案
         */
        btn_addSingleMeterMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                if (HzyUtils.isEmpty(meterCount) || Integer.parseInt(meterCount) < 1 || Integer.parseInt(meterCount) > 64 || HzyUtils.isEmpty(meterAddr)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("表地址不可为空，表序号应设置在1到64之间(包含1和64)")
                            .show();
                    return;
                }
                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(meterNum2)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID、表序号及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                String sendMsg = "680021" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "84030101" + HzyUtils.isLength(meterNum2, 2) + HzyUtils.isLength(meterAddr, 14);
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "添加单个表计档案" + sendMsg);
                MenuActivity.sendCmd(sendMsg);

            }
        });
        /**
         * 删除单个表计档案
         */
        btn_deleteSingleMeterMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(meterNum3)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID、表序号及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                String sendMsg = "68001a" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "84020101" + HzyUtils.isLength(meterNum3, 2);
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "删除单个表计档案" + sendMsg);
                MenuActivity.sendCmd(sendMsg);

            }
        });
        /**
         * 实时点抄
         */
        btn_getMeterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                if (HzyUtils.isEmpty(meterNum) || Integer.parseInt(meterNum) < 1 || Integer.parseInt(meterNum) > 64) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("表序号输入错误，请重新输入，表序号应在1和64之间(包含1和64)")
                            .show();
                    return;
                }
                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(meterNum)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID、表序号及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(100);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                String sendMsg = "68001a" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "84050101" + HzyUtils.isLength(meterNum, 2);
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "实时点抄" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 设置单个表底度
         */
        btn_setMeterValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(meterNum1)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID、表序号及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(100);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                String sendMsg = "68001e" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "84040101" + HzyUtils.isLength(meterNum1, 2) + HzyUtils.isLength(meterValue, 8);
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "设置单个表底度" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 导出水表档案
         */
        btn_exportMeterMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }

                prepareTimeStart(100);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                String sendMsg = "68001d" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "84090101" + HzyUtils.isLength(meterNum1, 2) + time;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "导出水表档案" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 清空水表档案
         */
        btn_clearAllMeterMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputMsg();
                HzyUtils.showProgressDialog(LoRa_TYPTtoHmActivity.this);
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(aimNetFreq)) {
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("参数节点ID、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空!")
                            .show();
                    HzyUtils.closeProgressDialog();
                    return;
                }

                prepareTimeStart(100);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                String sendMsg = "68001d" +
                        aimNetID + aimNetFreq + aimJDID + aimLYLJ1 + aimLYLJ2 + aimLYLJ3
                        + "840a" + time;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "修改分采号" + sendMsg);
                MenuActivity.sendCmd(sendMsg);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString().toLowerCase();
                    try {
                        String returnMsg = "";
                        getMsg = getMsg.contains("68") ? getMsg.substring(getMsg.indexOf("68")) : getMsg;
                        getMsg = getMsg.contains("6800318000") ? getMsg.substring(getMsg.indexOf("6800318000")) : getMsg;
                        getMsg = getMsg.contains("6800198481") ? getMsg.substring(getMsg.indexOf("6800198481")) : getMsg;
                        getMsg = getMsg.contains("680019848d") ? getMsg.substring(getMsg.indexOf("680019848d")) : getMsg;
                        getMsg = getMsg.contains("680019848b") ? getMsg.substring(getMsg.indexOf("680019848b")) : getMsg;
                        getMsg = getMsg.contains("680019848c") ? getMsg.substring(getMsg.indexOf("680019848c")) : getMsg;
                        getMsg = getMsg.contains("6800228483") ? getMsg.substring(getMsg.indexOf("6800228483")) : getMsg;
                        getMsg = getMsg.contains("68001984ff") ? getMsg.substring(getMsg.indexOf("68001984ff")) : getMsg;
                        getMsg = getMsg.contains("6800268485") ? getMsg.substring(getMsg.indexOf("6800268485")) : getMsg;
                        getMsg = getMsg.contains("6800268484") ? getMsg.substring(getMsg.indexOf("6800268484")) : getMsg;
                        getMsg = getMsg.contains("6800268489") ? getMsg.substring(getMsg.indexOf("6800268489")) : getMsg;
                        getMsg = getMsg.contains("680026848a") ? getMsg.substring(getMsg.indexOf("680026848a")) : getMsg;

                        if ((getMsg.length() >= 98 && getMsg.contains("6800318000") &&
                                getMsg.substring(getMsg.indexOf("6800318000") + 96,
                                        getMsg.indexOf("6800318000") + 98).equals("16")) ||
                                (getMsg.length() >= 84 && getMsg.contains("68002a8000") &&
                                        getMsg.substring(getMsg.indexOf("68002a8000") + 82,
                                                getMsg.indexOf("68002a8000") + 84).equals("16"))) {
                            Log.d("limbo", "磁铁激活返回:" + getMsg);
                     /*   String crc16back = getMsg.substring(92, 96);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 92));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }*/

                            String JDid = getMsg.substring(10, 24);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(24, 26), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(26, 28);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String Subtype = getMsg.substring(30, 32);//设备子类型
                            String netId = getMsg.substring(32, 36);//网络ID
                            String netFreq = getMsg.substring(36, 42);//网络频率
                            String floor = getMsg.substring(42, 44);//层数
                            String region = getMsg.substring(44, 46);//区域
                            String totalNum = Integer.parseInt(getMsg.substring(46, 50), 16) + "";//全局编号
                            String partNum = Integer.parseInt(getMsg.substring(50, 54), 16) + "";//局部编号
                            String softwareVersion = getMsg.substring(54, 62);//软件版本
                            String hardwareVersion = getMsg.substring(62, 70);//硬件版本
                            String otherVersion = getMsg.substring(70, 78);//附加版本
                            /**
                             * 磁铁激活返回
                             */
                            if (getMsg.substring(6, 8).equals("31")) {
                                String data = getMsg.substring(78, 88);//直读数据
                                data = Integer.parseInt(data.substring(0, 6)) + "." + data.substring(6, 10);
                                String dataState = getMsg.substring(88, 90);//数据状态
                                String valveState = getMsg.substring(90, 92);//阀控状态
                                et_data.setText(data);
                                et_state.setText(dataState);
                            }
                            et_aimJDID.setText(JDid);
                            et_endSingal.setText(endSingal);
                            et_electric.setText(electricVoltage);
                            et_aimNetID.setText(netId);
                            et_aimNetFreq.setText(netFreq);

                            returnMsg = "磁激活获取数据成功";
                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("6800198481") &&
                                getMsg.substring(getMsg.indexOf("6800198481") + 48, getMsg.indexOf("6800198481") + 50).equals("16")) {
                            //设置分采号
                            Log.d("limbo", "设置分采号返回:" + getMsg);
                            String crc16back = getMsg.substring(44, 48);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 44));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            String fc = getMsg.substring(42, 44);//分采号
                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            et_fcNum.setText(fc);
                            et_endSingal.setText(endSingal);

                            returnMsg = "设置分采号成功";

                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("680019848d") &&
                                getMsg.substring(getMsg.indexOf("680019848d") + 48, getMsg.indexOf("680019848d") + 50).equals("16")) {
                            //读取分采号
                            Log.d("limbo", "设置分采号返回:" + getMsg);
                            String crc16back = getMsg.substring(44, 48);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 44));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            String fc = getMsg.substring(42, 44);//分采号
                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            et_fcNum.setText(fc);
                            et_endSingal.setText(endSingal);

                            returnMsg = "读取分采号成功,分采号为:" + fc;
                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("680019848b") &&
                                getMsg.substring(getMsg.indexOf("680019848b") + 48, getMsg.indexOf("680019848b") + 50).equals("16")) {
                            //设置水表总数
                            Log.d("limbo", "设置分采号返回:" + getMsg);
                            String crc16back = getMsg.substring(44, 48);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 44));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            String meterCount = getMsg.substring(42, 44);//水表总数
                            meterCount = Integer.parseInt(meterCount, 16) + "";
                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            et_meterCount.setText(meterCount);
                            et_endSingal.setText(endSingal);
                            returnMsg = "设置水表总数成功";

                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("680019848c") &&
                                getMsg.substring(getMsg.indexOf("680019848c") + 48, getMsg.indexOf("680019848c") + 50).equals("16")) {
                            //读取水表总数
                            Log.d("limbo", "设置分采号返回:" + getMsg);
                            String crc16back = getMsg.substring(44, 48);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 44));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            String meterCount = getMsg.substring(42, 44);//水表总数
                            meterCount = Integer.parseInt(meterCount, 16) + "";
                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            et_meterCount.setText(meterCount);
                            et_endSingal.setText(endSingal);
                            returnMsg = "读取水表总数成功,水表总数为:" + meterCount;

                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("6800228483") &&
                                getMsg.substring(getMsg.indexOf("6800228483") + 66, getMsg.indexOf("6800228483") + 68).equals("16")) {
                            //添加单个表计档案
                            Log.d("limbo", "设置分采号返回:" + getMsg);
                            String crc16back = getMsg.substring(62, 66);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 62));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            String meterAddr = getMsg.substring(48, 62);//表号
                            String meterNum = getMsg.substring(46, 48);//表序号
                            meterNum = Integer.parseInt(meterNum, 16) + "";

                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            et_meterAddr.setText(meterAddr);
                            et_endSingal.setText(endSingal);
                            returnMsg = "添加单个表计档案成功" + "\n表序号:" + meterNum;


                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("680022848c") &&
                                getMsg.substring(getMsg.indexOf("680022848c") + 48, getMsg.indexOf("680022848c") + 50).equals("16")) {
                            //删除单个表计档案
                            Log.d("limbo", "删除单个表计档案返回:" + getMsg);
                            String crc16back = getMsg.substring(44, 48);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 62));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            String meterCount = getMsg.substring(42, 44);//删除结果
                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            if (meterCount.equals("00")) {
                                Toast.makeText(LoRa_TYPTtoHmActivity.this, "删除失败", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoRa_TYPTtoHmActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                            }
                            et_endSingal.setText(endSingal);
                            returnMsg = "删除单个表计档案成功";


                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("6800268484") &&
                                getMsg.substring(getMsg.indexOf("6800268484") + 74, getMsg.indexOf("6800268484") + 76).equals("16")) {
                            //设置单个表底度
                            Log.d("limbo", "设置单个表底度返回:" + getMsg);
                            String crc16back = getMsg.substring(70, 74);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 70));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            String fcNum = getMsg.substring(42, 44);
                            String meterNum = getMsg.substring(46, 48);
                            meterNum = Integer.parseInt(meterNum, 16) + "";
                            String meterAddr = getMsg.substring(48, 62);
                            String meterValue = getMsg.substring(62, 70);
                            meterValue = Integer.parseInt(meterValue.substring(0, 6)) + "." + meterValue.substring(6);
                            et_meterValue.setText(meterValue);
                            et_meterAddr.setText(meterAddr);
                            et_fcNum.setText(fcNum);
                            et_meterNum1.setText(meterNum);
                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            et_endSingal.setText(endSingal);
                            returnMsg = "设置单个表底度成功," +
                                    "\n表序号为:" + meterNum +
                                    "\n表ID为:" + meterAddr +
                                    "\n表读数为:" + meterValue;


                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("6800228484") &&
                                getMsg.substring(getMsg.indexOf("6800228484") + 74, getMsg.indexOf("6800228484") + 76).equals("16")) {
                            //清空水表档案
                            Log.d("limbo", "清空水表档案返回:" + getMsg);
                            String crc16back = getMsg.substring(70, 74);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 70));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            et_endSingal.setText(endSingal);
                            returnMsg = "清空水表档案成功";


                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("68") && getMsg.contains("8489") &&
                                getMsg.substring(getMsg.indexOf("68") + 2 * Integer.parseInt(getMsg.substring(2, 6), 16) - 2
                                        , getMsg.indexOf("68") + 2 * Integer.parseInt(getMsg.substring(2, 6), 16)).equals("16")) {
                            //导出水表档案
                            Log.d("limbo", "导出水表档案返回:" + getMsg);
                            int length = 2 * Integer.parseInt(getMsg.substring(2, 6), 16);
                            String crc16back = getMsg.substring(length - 6, length - 2);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, length - 6));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            String meterCount = getMsg.substring(42, 44);//表总数
                            String meterNum = getMsg.substring(44, 46);
                            meterNum = Integer.parseInt(meterNum, 16) + "";
                            String meterAddrMsg = getMsg.substring(44, 44 + 16 * Integer.parseInt(meterCount, 16));

                            et_meterNum.setText(meterNum);
                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            et_endSingal.setText(endSingal);

                            String Msg = "";
                            for (int i = 0; i < Integer.parseInt(meterCount, 16); i++) {

                                Msg = Msg + "表序号:" + Integer.parseInt(meterAddrMsg.substring(0 + i * 16, 2 + i * 16), 16) + "    表ID为:" + meterAddrMsg.substring(2 + i * 16, 16 + i * 16) + "\n";
                            }


                            returnMsg = "导出水表档案成功\n" + Msg;

                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("68002284ff") &&
                                getMsg.substring(getMsg.indexOf("68002284ff") + 48, getMsg.indexOf("68002284ff") + 50).equals("16")) {
                            //实时点抄-应答

                            returnMsg = "实时点抄-应答成功";

                        } else if (getMsg.length() >= 2 * Integer.parseInt(getMsg.substring(2, 6), 16) && getMsg.contains("6800268485") &&
                                getMsg.substring(getMsg.indexOf("6800268485") + 74, getMsg.indexOf("6800268485") + 76).equals("16")) {
                            //实时点抄
                            Log.d("limbo", "实时点抄返回:" + getMsg);
                            String crc16back = getMsg.substring(70, 74);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 70));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(LoRa_TYPTtoHmActivity.this, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String JDStyle = getMsg.substring(24, 26);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(28, 30);//电池电压
                            electricVoltage = volt(electricVoltage);
                            String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                            String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                            String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                            String meterNum = getMsg.substring(46, 48);
                            meterNum = Integer.parseInt(meterNum, 16) + "";
                            String meterAddr = getMsg.substring(48, 62);
                            String meterData = getMsg.substring(62, 68);
                            meterData = meterData.substring(0, 4) + "." + meterData.substring(4);
                            String meterState = getMsg.substring(68, 70);
                            et_state.setText(meterState);
                            et_data.setText(meterData);
                            et_meterNum.setText(meterNum);
                            et_meterAddr.setText(meterAddr);
                            et_electric.setText(electricVoltage);
                            et_LYSignal1.setText(LYXH1);
                            et_LYSignal2.setText(LYXH2);
                            et_LYSignal3.setText(LYXH3);
                            et_endSingal.setText(endSingal);
                            if (meterState.equals("00")) {
                                meterState = "正常";
                            } else {
                                meterState = "异常:" + meterState;
                            }
                            returnMsg = "实时点抄成功," +
                                    "\n表序号为:" + meterNum +
                                    "\n表ID为:" + meterAddr +
                                    "\n表读数为:" + meterData +
                                    "\n表状态为:" + meterState;


                        }


                        if (returnMsg.length() != 0) {
                            if (returnMsg.equals("磁激活获取数据成功")) {
                                Toast.makeText(LoRa_TYPTtoHmActivity.this, returnMsg, Toast.LENGTH_LONG).show();

                            } else {
                                new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("返回数据!")
                                        .setContentText(returnMsg)
                                        .show();
                            }


                        }
                    } catch (Exception e) {
                        Toast.makeText(LoRa_TYPTtoHmActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }


                    timeOut = true;
                    HzyUtils.closeProgressDialog();
                    break;
                case 0x99:
                    new SweetAlertDialog(LoRa_TYPTtoHmActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("提示!")
                            .setContentText("无数据返回，请重试!")
                            .show();
                    break;
                default:
                    break;
            }
        }
    };

    void getInputMsg() {

        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        time = df.format(d);
        aimJDID = et_aimJDID.getText().toString();
        aimNetID = et_aimNetID.getText().toString();
        aimNetFreq = et_aimNetFreq.getText().toString();
        aimNetID = et_aimNetID.getText().toString();
        aimLYLJ1 = HzyUtils.isLength(et_aimLYLJ1.getText().toString(), 2);
        aimLYLJ2 = HzyUtils.isLength(et_aimLYLJ2.getText().toString(), 2);
        aimLYLJ3 = HzyUtils.isLength(et_aimLYLJ3.getText().toString(), 2);
        endSingal = et_endSingal.getText().toString();
        LYSignal1 = et_LYSignal1.getText().toString();
        LYSignal2 = et_LYSignal2.getText().toString();
        LYSignal3 = et_LYSignal3.getText().toString();
        electric = et_electric.getText().toString();
        meterData = et_data.getText().toString();
        state = et_state.getText().toString();
        meterNum = HzyUtils.toHexString(et_meterNum.getText().toString());
        meterNum1 = HzyUtils.toHexString(et_meterNum1.getText().toString());
        meterValue = et_meterValue.getText().toString();
        if (!meterValue.contains(".")) {
            meterValue = meterValue + "00";
        } else {
            meterValue = meterValue.substring(0, meterValue.indexOf(".")) + HzyUtils.isLength(meterValue.substring(meterValue.indexOf(".") + 1), 2);
        }
        fcNum = et_fcNum.getText().toString();
        meterCount = et_meterCount.getText().toString();
        meterNum2 = HzyUtils.toHexString(et_meterNum2.getText().toString());
        meterAddr = et_meterAddr.getText().toString();
        meterNum3 = HzyUtils.toHexString(et_meterNum3.getText().toString());
    }

    private String volt(String volt) {
        String electricVoltage = ((float) 255 / Float.parseFloat(Integer.parseInt(volt, 16) + "") * 1.224) + "";//电池电压
        electricVoltage = electricVoltage.substring(0, electricVoltage.indexOf(".") + 3) + "V";

        return electricVoltage;
    }

    /**
     * 开始协议设定时间
     * timeMax 1 = 0.1s
     */
    private void prepareTimeStart(int timeMax) {
        timeOut = false;
        if (!HzyUtils.isEmpty(et_aimLYLJ1.getText().toString())) {
            timeMax = timeMax + 15;
        }
        if (!HzyUtils.isEmpty(et_aimLYLJ2.getText().toString())) {
            timeMax = timeMax + 15;
        }
        if (!HzyUtils.isEmpty(et_aimLYLJ3.getText().toString())) {
            timeMax = timeMax + 15;
        }
        final int finalTimeMax = timeMax;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < finalTimeMax; i++) {
                        Thread.sleep(100);
                        if (timeOut) {
                            break;
                        }
                    }
                    if (!timeOut) {
                        HzyUtils.closeProgressDialog();
                        Message message = new Message();
                        message.what = 0x99;
                        mHandler.sendMessage(message);
                    }
                    timeOut = true;
                } catch (Exception e) {
                    Log.d("limbo", e.toString());
                }
            }
        }).start();
    }
}
