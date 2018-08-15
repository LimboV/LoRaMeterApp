package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.LoRa_TYPTActivity.timeOut;
import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.LoRa_TYPTJd_CameraDebug.getTime;
import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.LoRa_TYPTJd_CameraDebug.getVolt;

/**
 * Created by limbo on 2018/4/10.
 */

public class LoRa_TYPTJd_Pulse extends Activity {
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_aimJDID)
    EditText et_aimJDID;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_aimNetID)
    EditText et_aimNetID;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_aimNetFreq)
    EditText et_aimNetFreq;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_aimLYLJ1)
    EditText et_aimLYLJ1;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_aimLYLJ2)
    EditText et_aimLYLJ2;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_aimLYLJ3)
    EditText et_aimLYLJ3;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_endSingal)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_endSingal;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_electricVoltage)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_electricVoltage;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_LYSignal1)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_LYSignal1;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_LYSignal2)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_LYSignal2;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_LYSignal3)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_LYSignal3;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_data)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_data;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_state)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_state;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_valveCtrl)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_valveCtrl;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_meterInitData)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_meterInitData;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_meterJD)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_meterJD;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_et_meterPulseCount)
    EditText LoRa_LoRa_TYPTJd_Pulse_et_meterPulseCount;
    @BindView(R.id.LoRa_LoRa_TYPTJd_Pulse_btn_rtcData)
    Button LoRa_LoRa_TYPTJd_Pulse_btn_rtcData;
    @BindView(R.id.btn_read1)
    Button btn_read1;
    @BindView(R.id.btn_set1)
    Button btn_set1;
    @BindView(R.id.btn_read2)
    Button btn_read2;
    @BindView(R.id.btn_set2)
    Button btn_set2;
    @BindView(R.id.btn_read3)
    Button btn_read3;
    @BindView(R.id.btn_set3)
    Button btn_set3;
    @BindView(R.id.btn_reset)
    Button btn_reset;
    @BindView(R.id.btn_resetChanger)
    Button btn_resetChanger;

    Context activity;
    String resultMsg = "";

    String aimJdId, aimNetId, aimNetFreq, aimLyLj1, aimLyLj2, aimLyLj3,
            endSingle, electricVoltage, LYSignal1, LYSignal2, LYSignal3,
            data, state, valveCtrl, meterDd, meterJd, pulseCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lora_activity_typt_jd_pulse);
        ButterKnife.bind(this);
        activity = LoRa_TYPTJd_Pulse.this;
        MenuActivity.btAuto = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MenuActivity.btAuto) {
                    try {
                        Thread.sleep(2000);
                        resultMsg = resultMsg + HzyUtils.GetBlueToothMsg();
                        if (resultMsg.length() > 10) {
                            resultMsg = resultMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", "get:" + resultMsg);
                            Message message = new Message();
                            message.obj = resultMsg;
                            message.what = 0x00;
                            mHandler.sendMessage(message);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        /**
         * 点抄实时数据
         */
        LoRa_LoRa_TYPTJd_Pulse_btn_rtcData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextContext();
                if (HzyUtils.isEmpty(aimNetId) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimJdId)) {
                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("目标频率，目标网络ID及目标节点ID皆不可为空!")
                            .show();
                    return;
                }
                LoRa_LoRa_TYPTJd_Pulse_et_data.setText("");
                resultMsg = "";
                prepareTimeStart(60);
                String sendMsg = "68" + "001d"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "87" + "01" + getTime();
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 设置表底度
         */
        btn_set1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextContext();
                if (HzyUtils.isEmpty(aimNetId) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimJdId)) {
                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("目标频率，目标网络ID及目标节点ID皆不可为空!")
                            .show();
                    return;
                }
                resultMsg = "";
                prepareTimeStart(60);
                String data = LoRa_LoRa_TYPTJd_Pulse_et_meterInitData.getText().toString().trim();
                if (data.contains(".")) {
                    data = HzyUtils.isLength(data.substring(0, data.indexOf(".")), 6)
                            + HzyUtils.isLength1(data.substring(data.indexOf(".") + 1), 2);
                } else {
                    data = HzyUtils.isLength(data, 8);
                }
                String sendMsg = "68" + "001b"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "87" + "02"
                        + data;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 读取表底度
         */
        btn_read1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextContext();
                if (HzyUtils.isEmpty(aimNetId) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimJdId)) {
                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("目标频率，目标网络ID及目标节点ID皆不可为空!")
                            .show();
                    return;
                }
                resultMsg = "";
                prepareTimeStart(60);
                String sendMsg = "68" + "001d"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "87" + "07" + getTime();
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 设置表精度
         */
        btn_set2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextContext();
                if (HzyUtils.isEmpty(aimNetId) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimJdId)) {
                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("目标频率，目标网络ID及目标节点ID皆不可为空!")
                            .show();
                    return;
                }
                resultMsg = "";
                prepareTimeStart(60);
                String data = LoRa_LoRa_TYPTJd_Pulse_et_meterJD.getText().toString().trim();
                String sendMsg = "68" + "0018"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "87" + "03"
                        + HzyUtils.isLength(data,2);
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 读取表精度
         */
        btn_read2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextContext();
                if (HzyUtils.isEmpty(aimNetId) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimJdId)) {
                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("目标频率，目标网络ID及目标节点ID皆不可为空!")
                            .show();
                    return;
                }
                resultMsg = "";
                prepareTimeStart(60);
                String sendMsg = "68" + "001d"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "87" + "05"
                        + getTime();
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 设置表脉冲数
         */
        btn_set3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextContext();
                if (HzyUtils.isEmpty(aimNetId) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimJdId)) {
                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("目标频率，目标网络ID及目标节点ID皆不可为空!")
                            .show();
                    return;
                }
                resultMsg = "";
                prepareTimeStart(60);
                String data = LoRa_LoRa_TYPTJd_Pulse_et_meterPulseCount.getText().toString().trim();
                String sendMsg = "68" + "0018"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "87" + "04"
                        + HzyUtils.isLength(data,2);
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 读取表脉冲数
         */
        btn_read3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextContext();
                if (HzyUtils.isEmpty(aimNetId) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimJdId)) {
                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("目标频率，目标网络ID及目标节点ID皆不可为空!")
                            .show();
                    return;
                }
                resultMsg = "";
                prepareTimeStart(60);
                String sendMsg = "68" + "001d"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "87" + "06"
                        + getTime();
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString().toLowerCase();
                    if (getMsg.contains("68") && getMsg.length() > 20) {
                        getMsg = getMsg.substring(getMsg.indexOf("68"));
                        int length = Integer.parseInt(getMsg.substring(2, 6), 16);
                        /**
                         * 磁铁激活
                         */
                        if (getMsg.length() >= 98 && getMsg.contains("6800318000") &&
                                getMsg.substring(getMsg.indexOf("6800318000") + 96, getMsg.indexOf("6800318000") + 98).equals("16")) {
                            Log.d("limbo", "磁铁激活返回:" + getMsg);
                            resultMsg = "";
                            String crc16back = getMsg.substring(92, 96);
                            String crc16result = HzyUtils.CRC16(getMsg.substring(0, 92));
                            if (!crc16result.equals(crc16back)) {
                                Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                                HintDialog.ShowHintDialog(activity, "CRC16校验出错。", "提示");
                                break;
                            }
                            String JDid = getMsg.substring(10, 24);//节点id
                            String endSingal = Integer.parseInt(getMsg.substring(24, 26), 16) + "";//末端强度
                            String electricVoltage = getMsg.substring(26, 28);//电池电压
                            electricVoltage = getVolt(electricVoltage);
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
                            String data = getMsg.substring(78, 88);//直读数据
                            data = Integer.parseInt(data.substring(0, 8)) + "." + data.substring(8, 10);
                            String dataState = getMsg.substring(88, 90);//数据状态
                            String valveCtrl = getMsg.substring(90, 92);//阀控状态
                            et_aimJDID.setText(JDid);
                            LoRa_LoRa_TYPTJd_Pulse_et_state.setText(dataState);
                            LoRa_LoRa_TYPTJd_Pulse_et_endSingal.setText(endSingal);
                            LoRa_LoRa_TYPTJd_Pulse_et_electricVoltage.setText(electricVoltage);
                            et_aimNetID.setText(netId);
                            et_aimNetFreq.setText(netFreq);
                            LoRa_LoRa_TYPTJd_Pulse_et_data.setText(data);
                            LoRa_LoRa_TYPTJd_Pulse_et_valveCtrl.setText(valveCtrl);
                            Toast.makeText(activity, "磁激活获取数据成功", Toast.LENGTH_LONG).show();
                        }
                        /**
                         * 如果接收完整报文
                         */
                        else if (getMsg.length() >= length * 2) {
                            getMsg = getMsg.substring(0, length * 2);
                            resultMsg = "";
                            /**
                             * 判断CRC16校验
                             */
                            String CRC16 = getMsg.substring(length * 2 - 6, length * 2 - 2);
                            if (HzyUtils.CRC16(getMsg.substring(0, length * 2 - 6)).equals(CRC16)) {
                                /**
                                 * CRC16验证成功,开始判断命令号和子功能码
                                 */
                                timeOut = true;
                                String command = getMsg.substring(6, 8);
                                String Zcommand = getMsg.substring(8, 10);
                                if (command.equals("87")) {
                                    if (Zcommand.equals("81")) {//点抄单个表实时返回接收
                                        Toast.makeText(LoRa_TYPTJd_Pulse.this, "成功", Toast.LENGTH_LONG).show();
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//信号强度
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String ZdData = getMsg.substring(42, 50);//直读数据
                                        ZdData = Integer.parseInt(ZdData.substring(0, 6)) + "." + ZdData.substring(6, 8);
                                        String dataState = getMsg.substring(50, 52);//数据状态
                                        String valveCtrl = getMsg.substring(52, 54);//阀控状态

                                        LoRa_LoRa_TYPTJd_Pulse_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_LoRa_TYPTJd_Pulse_et_electricVoltage.setText(volt);
                                        LoRa_LoRa_TYPTJd_Pulse_et_data.setText(ZdData);
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal1.setText(LyXh.substring(0, 2));
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal2.setText(LyXh.substring(2, 4));
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal3.setText(LyXh.substring(4, 6));
                                        LoRa_LoRa_TYPTJd_Pulse_et_state.setText(dataState);
                                        LoRa_LoRa_TYPTJd_Pulse_et_valveCtrl.setText(valveCtrl);
                                    } else if (Zcommand.equals("82") || Zcommand.equals("87")) {//设置/读取 底度 接收
                                        Toast.makeText(LoRa_TYPTJd_Pulse.this, "成功", Toast.LENGTH_LONG).show();
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//末端强度
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String meterDD = getMsg.substring(42, 50);//底度
                                        meterDD = Integer.parseInt(meterDD.substring(0, 6)) + "." + meterDD.substring(6, 8);

                                        LoRa_LoRa_TYPTJd_Pulse_et_meterInitData.setText(meterDD);
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal1.setText(LyXh.substring(0, 2));
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal2.setText(LyXh.substring(2, 4));
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal3.setText(LyXh.substring(4, 6));
                                        LoRa_LoRa_TYPTJd_Pulse_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_LoRa_TYPTJd_Pulse_et_electricVoltage.setText(volt);
                                    } else if (Zcommand.equals("83") || Zcommand.equals("85")) {//设置/读取 精度接收
                                        Toast.makeText(LoRa_TYPTJd_Pulse.this, "成功", Toast.LENGTH_LONG).show();
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//末端强度
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String meterJD = getMsg.substring(42, 44);//精度

                                        LoRa_LoRa_TYPTJd_Pulse_et_meterJD.setText(meterJD);
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal1.setText(LyXh.substring(0, 2));
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal2.setText(LyXh.substring(2, 4));
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal3.setText(LyXh.substring(4, 6));
                                        LoRa_LoRa_TYPTJd_Pulse_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_LoRa_TYPTJd_Pulse_et_electricVoltage.setText(volt);
                                    } else if (Zcommand.equals("84") || Zcommand.equals("86")) {//设置/读取 脉冲接收
                                        Toast.makeText(LoRa_TYPTJd_Pulse.this, "成功", Toast.LENGTH_LONG).show();
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//末端强度
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String pulseCount = getMsg.substring(42, 44);//脉冲位数

                                        LoRa_LoRa_TYPTJd_Pulse_et_meterPulseCount.setText(pulseCount);
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal1.setText(LyXh.substring(0, 2));
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal2.setText(LyXh.substring(2, 4));
                                        LoRa_LoRa_TYPTJd_Pulse_et_LYSignal3.setText(LyXh.substring(4, 6));
                                        LoRa_LoRa_TYPTJd_Pulse_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_LoRa_TYPTJd_Pulse_et_electricVoltage.setText(volt);
                                    }
                                }
                            }
                        }


                    }
                    break;
                case 0x99:
                    new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("提示!")
                            .setContentText("无数据返回，请重试!")
                            .show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取各项输入
     */
    private void getEditTextContext() {
        aimJdId = et_aimJDID.getText().toString();
        aimNetId = et_aimNetID.getText().toString();
        aimNetFreq = et_aimNetFreq.getText().toString();
        aimLyLj1 = et_aimLYLJ1.getText().toString();
        aimLyLj2 = et_aimLYLJ2.getText().toString();
        aimLyLj3 = et_aimLYLJ3.getText().toString();
        endSingle = LoRa_LoRa_TYPTJd_Pulse_et_endSingal.getText().toString();
        electricVoltage = LoRa_LoRa_TYPTJd_Pulse_et_electricVoltage.getText().toString();
        LYSignal1 = LoRa_LoRa_TYPTJd_Pulse_et_LYSignal1.getText().toString();
        LYSignal2 = LoRa_LoRa_TYPTJd_Pulse_et_LYSignal2.getText().toString();
        LYSignal3 = LoRa_LoRa_TYPTJd_Pulse_et_LYSignal3.getText().toString();
        data = LoRa_LoRa_TYPTJd_Pulse_et_data.getText().toString();
        state = LoRa_LoRa_TYPTJd_Pulse_et_state.getText().toString();
        valveCtrl = LoRa_LoRa_TYPTJd_Pulse_et_valveCtrl.getText().toString();
        meterDd = LoRa_LoRa_TYPTJd_Pulse_et_meterInitData.getText().toString();
        meterJd = LoRa_LoRa_TYPTJd_Pulse_et_meterJD.getText().toString();
        pulseCount = LoRa_LoRa_TYPTJd_Pulse_et_meterPulseCount.getText().toString();


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
