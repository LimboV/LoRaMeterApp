package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
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

import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.LoRa_TYPTActivity.timeOut;

/**
 * Created by ssHss on 2018/1/22.
 */

public class LoRa_TYPTJd_CameraDebug extends Activity {
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_aimJDID)
    EditText LoRa_TYPTJdCameraActivity_et_aimJDID;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_aimNetID)
    EditText LoRa_TYPTJdCameraActivity_et_aimNetID;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_aimNetFreq)
    EditText LoRa_TYPTJdCameraActivity_et_aimNetFreq;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_aimLYLJ1)
    EditText LoRa_TYPTJdCameraActivity_et_aimLYLJ1;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_aimLYLJ2)
    EditText LoRa_TYPTJdCameraActivity_et_aimLYLJ2;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_aimLYLJ3)
    EditText LoRa_TYPTJdCameraActivity_et_aimLYLJ3;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_endSingal)
    EditText LoRa_TYPTJdCameraActivity_et_endSingal;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_electricVoltage)
    EditText LoRa_TYPTJdCameraActivity_et_electricVoltage;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_LYSignal1)
    EditText LoRa_TYPTJdCameraActivity_et_LYSignal1;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_LYSignal2)
    EditText LoRa_TYPTJdCameraActivity_et_LYSignal2;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_LYSignal3)
    EditText LoRa_TYPTJdCameraActivity_et_LYSignal3;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_data)
    EditText LoRa_TYPTJdCameraActivity_et_data;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_believe1)
    EditText LoRa_TYPTJdCameraActivity_et_believe1;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_believe2)
    EditText LoRa_TYPTJdCameraActivity_et_believe2;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_believe3)
    EditText LoRa_TYPTJdCameraActivity_et_believe3;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_believe4)
    EditText LoRa_TYPTJdCameraActivity_et_believe4;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_believe5)
    EditText LoRa_TYPTJdCameraActivity_et_believe5;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_believe6)
    EditText LoRa_TYPTJdCameraActivity_et_believe6;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_Y)
    EditText LoRa_TYPTJdCameraActivity_et_Y;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_X5)
    EditText LoRa_TYPTJdCameraActivity_et_X5;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_X4)
    EditText LoRa_TYPTJdCameraActivity_et_X4;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_X3)
    EditText LoRa_TYPTJdCameraActivity_et_X3;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_X2)
    EditText LoRa_TYPTJdCameraActivity_et_X2;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_et_X1)
    EditText LoRa_TYPTJdCameraActivity_et_X1;


    @BindView(R.id.LoRa_TYPTJdCameraActivity_iv_Z1)
    ImageView LoRa_TYPTJdCameraActivity_iv_Z1;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_iv_Z2)
    ImageView LoRa_TYPTJdCameraActivity_iv_Z2;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_iv_Z3)
    ImageView LoRa_TYPTJdCameraActivity_iv_Z3;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_iv_Z4)
    ImageView LoRa_TYPTJdCameraActivity_iv_Z4;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_iv_Z5)
    ImageView LoRa_TYPTJdCameraActivity_iv_Z5;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_iv_Z6)
    ImageView LoRa_TYPTJdCameraActivity_iv_Z6;


    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_rtcData)
    Button LoRa_TYPTJdCameraActivity_btn_rtcData;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_autoLocate)
    Button LoRa_TYPTJdCameraActivity_btn_autoLocate;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_readLocation)
    Button LoRa_TYPTJdCameraActivity_btn_readLocation;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_writeLocation)
    Button LoRa_TYPTJdCameraActivity_btn_writeLocation;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_now)
    Button LoRa_TYPTJdCameraActivity_btn_now;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_his)
    Button LoRa_TYPTJdCameraActivity_btn_his;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_nowadd)
    Button LoRa_TYPTJdCameraActivity_btn_nowadd;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_hisnow)
    Button LoRa_TYPTJdCameraActivity_btn_hisnow;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_reset)
    Button LoRa_TYPTJdCameraActivity_btn_reset;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_sd)
    Button LoRa_TYPTJdCameraActivity_btn_sd;
    @BindView(R.id.LoRa_TYPTJdCameraActivity_btn_resetx)
    Button LoRa_TYPTJdCameraActivity_btn_resetx;


    String aimJdId, aimNetId, aimNetFreq, aimLyLj1, aimLyLj2, aimLyLj3, endSingle, electricVoltage, LYSignal1, LYSignal2, LYSignal3,
            data, believe1, believe2, believe3, believe4, believe5, believe6, Y, X5, X4, X3, X2, X1;
    Context activity;
    String resultMsg = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lora_activity_typt_jd_camera);
        ButterKnife.bind(this);
        activity = LoRa_TYPTJd_CameraDebug.this;
        MenuActivity.btAuto = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MenuActivity.btAuto) {
                    try {

                        Thread.sleep(1500);
                        resultMsg = resultMsg + HzyUtils.GetBlueToothMsg();
                        if (resultMsg.length() != 0) {
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
        LoRa_TYPTJdCameraActivity_btn_rtcData.setOnClickListener(new View.OnClickListener() {
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
                LoRa_TYPTJdCameraActivity_et_data.setText("");
                LoRa_TYPTJdCameraActivity_et_believe1.setText("");
                LoRa_TYPTJdCameraActivity_et_believe2.setText("");
                LoRa_TYPTJdCameraActivity_et_believe3.setText("");
                LoRa_TYPTJdCameraActivity_et_believe4.setText("");
                LoRa_TYPTJdCameraActivity_et_believe5.setText("");
                LoRa_TYPTJdCameraActivity_et_believe6.setText("");
                resultMsg = "";
                prepareTimeStart(60);
                String sendMsg = "68" + "001d"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "82" + "01" + getTime();
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 点抄实时字轮图片
         */
        LoRa_TYPTJdCameraActivity_btn_now.setOnClickListener(new View.OnClickListener() {
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
                LoRa_TYPTJdCameraActivity_iv_Z1.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z2.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z3.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z4.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z5.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z6.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_et_data.setText("");
                LoRa_TYPTJdCameraActivity_et_believe1.setText("");
                LoRa_TYPTJdCameraActivity_et_believe2.setText("");
                LoRa_TYPTJdCameraActivity_et_believe3.setText("");
                LoRa_TYPTJdCameraActivity_et_believe4.setText("");
                LoRa_TYPTJdCameraActivity_et_believe5.setText("");
                LoRa_TYPTJdCameraActivity_et_believe6.setText("");
                resultMsg = "";
                prepareTimeStart(60);
                String sendMsg = "68" + "001d"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "82" + "02" + getTime();
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        LoRa_TYPTJdCameraActivity_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoRa_TYPTJdCameraActivity_et_endSingal.setText("");
                LoRa_TYPTJdCameraActivity_et_electricVoltage.setText("");
                LoRa_TYPTJdCameraActivity_et_LYSignal1.setText("");
                LoRa_TYPTJdCameraActivity_et_LYSignal2.setText("");
                LoRa_TYPTJdCameraActivity_et_LYSignal3.setText("");
                LoRa_TYPTJdCameraActivity_iv_Z1.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z2.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z3.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z4.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z5.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_iv_Z6.setImageBitmap(null);
                LoRa_TYPTJdCameraActivity_et_data.setText("");
                LoRa_TYPTJdCameraActivity_et_believe1.setText("");
                LoRa_TYPTJdCameraActivity_et_believe2.setText("");
                LoRa_TYPTJdCameraActivity_et_believe3.setText("");
                LoRa_TYPTJdCameraActivity_et_believe4.setText("");
                LoRa_TYPTJdCameraActivity_et_believe5.setText("");
                LoRa_TYPTJdCameraActivity_et_believe6.setText("");
                LoRa_TYPTJdCameraActivity_et_Y.setText("");
                LoRa_TYPTJdCameraActivity_et_X1.setText("");
                LoRa_TYPTJdCameraActivity_et_X2.setText("");
                LoRa_TYPTJdCameraActivity_et_X3.setText("");
                LoRa_TYPTJdCameraActivity_et_X4.setText("");
                LoRa_TYPTJdCameraActivity_et_X5.setText("");
            }
        });
        LoRa_TYPTJdCameraActivity_btn_resetx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTimeStart(50);
                String date;
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
                date = df.format(d);
                String sendMsg = "68001d0000000000000000000000000000008aff" + date;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "\n发送:" + sendMsg + "\n");
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 点抄实时附加图片
         */
        LoRa_TYPTJdCameraActivity_btn_nowadd.setOnClickListener(new View.OnClickListener() {
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
                        + "82" + "04" + getTime();
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 自动定位
         */
        LoRa_TYPTJdCameraActivity_btn_autoLocate.setOnClickListener(new View.OnClickListener() {
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
                        + "82" + "06" + getTime();
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 一键读取所有坐标
         */
        LoRa_TYPTJdCameraActivity_btn_readLocation.setOnClickListener(new View.OnClickListener() {
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
                LoRa_TYPTJdCameraActivity_et_Y.setText("");
                LoRa_TYPTJdCameraActivity_et_X1.setText("");
                LoRa_TYPTJdCameraActivity_et_X2.setText("");
                LoRa_TYPTJdCameraActivity_et_X3.setText("");
                LoRa_TYPTJdCameraActivity_et_X4.setText("");
                LoRa_TYPTJdCameraActivity_et_X5.setText("");
                resultMsg = "";
                prepareTimeStart(60);
                String sendMsg = "68" + "002b"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "80" + "05" + "8e12";
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                MenuActivity.sendCmd(sendMsg);
            }
        });
        /**
         * 一键写入所有坐标
         */
        LoRa_TYPTJdCameraActivity_btn_writeLocation.setOnClickListener(new View.OnClickListener() {
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
                if (HzyUtils.isEmpty(Y) || HzyUtils.isEmpty(X1) || HzyUtils.isEmpty(X2) || HzyUtils.isEmpty(X3) || HzyUtils.isEmpty(X4) || HzyUtils.isEmpty(X5)) {
                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示!")
                            .setContentText("坐标数据不可为空!")
                            .show();
                    return;
                }
                resultMsg = "";
                prepareTimeStart(60);
                String sendMsg = "68" + "002b"
                        + HzyUtils.isLength(aimNetId, 4)
                        + HzyUtils.isLength(aimNetFreq, 6)
                        + HzyUtils.isLength(aimJdId, 14)
                        + HzyUtils.isLength(aimLyLj1, 2)
                        + HzyUtils.isLength(aimLyLj2, 2)
                        + HzyUtils.isLength(aimLyLj3, 2)
                        + "80" + "06" + "8e12"
                        + HzyUtils.changeString1(HzyUtils.isLength(HzyUtils.toHexString(Y), 4))
                        + HzyUtils.changeString1(HzyUtils.isLength(HzyUtils.toHexString(X1), 4))
                        + HzyUtils.changeString1(HzyUtils.isLength(HzyUtils.toHexString(X2), 4))
                        + HzyUtils.changeString1(HzyUtils.isLength(HzyUtils.toHexString(X3), 4))
                        + HzyUtils.changeString1(HzyUtils.isLength(HzyUtils.toHexString(X4), 4))
                        + HzyUtils.changeString1(HzyUtils.isLength(HzyUtils.toHexString(X5), 4)) + "000000000000";
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
                            String valveState = getMsg.substring(90, 92);//阀控状态
                            LoRa_TYPTJdCameraActivity_et_aimJDID.setText(JDid);
                            LoRa_TYPTJdCameraActivity_et_endSingal.setText(endSingal);
                            LoRa_TYPTJdCameraActivity_et_electricVoltage.setText(electricVoltage);
                            LoRa_TYPTJdCameraActivity_et_aimNetID.setText(netId);
                            LoRa_TYPTJdCameraActivity_et_aimNetFreq.setText(netFreq);
                            LoRa_TYPTJdCameraActivity_et_data.setText(data);
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
                                if (command.equals("82")) {
                                    if (Zcommand.equals("ff")) {//应答接收
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//末端信号
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String delayTime = getMsg.substring(42, 44);//延时时间

                                        LoRa_TYPTJdCameraActivity_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_electricVoltage.setText(volt);

                                    } else if (Zcommand.equals("81")) {//数据接收
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//信号强度
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String ZdData = getMsg.substring(42, 52);//直读数据
                                        ZdData = Integer.parseInt(ZdData.substring(0, 8)) + "." + ZdData.substring(8, 10);
                                        String believe = getMsg.substring(52, 64);//置信度

                                        LoRa_TYPTJdCameraActivity_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_electricVoltage.setText(volt);
                                        LoRa_TYPTJdCameraActivity_et_believe6.setText(Integer.parseInt(believe.substring(0, 2), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe5.setText(Integer.parseInt(believe.substring(2, 4), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe4.setText(Integer.parseInt(believe.substring(4, 6), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe3.setText(Integer.parseInt(believe.substring(6, 8), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe2.setText(Integer.parseInt(believe.substring(8, 10), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe1.setText(Integer.parseInt(believe.substring(10, 12), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_data.setText(ZdData);
                                        LoRa_TYPTJdCameraActivity_et_LYSignal1.setText(LyXh.substring(0, 2));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal2.setText(LyXh.substring(2, 4));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal3.setText(LyXh.substring(4, 6));
                                    } else if (Zcommand.equals("82")) {
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//信号强度
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String picData = getMsg.substring(54, 1204);//二值化图片数据
                                        String SoftWare = getMsg.substring(1204, 1212);
                                        String DspVersion = getMsg.substring(1212, 1220);
                                        String ZdData = getMsg.substring(1220, 1230);//直读数据
                                        ZdData = Integer.parseInt(ZdData.substring(0, 8)) + "." + ZdData.substring(8, 10);
                                        String believe = getMsg.substring(1230, 1242);//置信度
                                        String[] showMsg = new String[5];
                                        for (int i = 0; i < 5; i++) {
                                            showMsg[i] = picData.substring(0 + i * 230, 230 * (i + 1));
                                            try {
                                                Bitmap bp;
                                                byte[] picdata = HzyUtils.getHexBytes(showMsg[i]);
                                                bp = BluetoothConnectThread.binaryPicBytesToBitmap(picdata);
                                                if (bp == null) {
                                                    Log.e("limbo", i + ":图片为空");
                                                } else {
                                                    Log.d("limbo", "bitmap success");
                                                    int imgboxlist[] = {R.id.LoRa_TYPTJdCameraActivity_iv_Z6,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z5,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z4,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z3,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z2,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z1};

                                                    ImageView iv = (ImageView) findViewById(imgboxlist[i]);
                                                    try {
                                                        iv.setImageBitmap(bp);
                                                    } catch (Exception e) {
                                                        HintDialog.ShowHintDialog(activity, "图像异常", "错误");
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Log.e("limbo", e.toString());
                                            }
                                        }
                                        LoRa_TYPTJdCameraActivity_et_believe6.setText(Integer.parseInt(believe.substring(0, 2), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe5.setText(Integer.parseInt(believe.substring(2, 4), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe4.setText(Integer.parseInt(believe.substring(4, 6), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe3.setText(Integer.parseInt(believe.substring(6, 8), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe2.setText(Integer.parseInt(believe.substring(8, 10), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe1.setText(Integer.parseInt(believe.substring(10, 12), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_data.setText(ZdData);
                                        LoRa_TYPTJdCameraActivity_et_LYSignal1.setText(LyXh.substring(0, 2));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal2.setText(LyXh.substring(2, 4));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal3.setText(LyXh.substring(4, 6));
                                        LoRa_TYPTJdCameraActivity_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_electricVoltage.setText(volt);


                                    } else if (Zcommand.equals("84")) {
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//信号强度
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String picData = getMsg.substring(54, 1434);//二值化图片数据
                                        String SoftWare = getMsg.substring(1434, 1442);
                                        String DspVersion = getMsg.substring(1442, 1450);
                                        String[] showMsg = new String[6];
                                        for (int i = 0; i < 6; i++) {
                                            showMsg[i] = picData.substring(0 + i * 230, 230 * (i + 1));
                                            try {
                                                Bitmap bp;
                                                byte[] picdata = HzyUtils.getHexBytes(showMsg[i]);
                                                bp = BluetoothConnectThread.binaryPicBytesToBitmap(picdata);
                                                if (bp == null) {
                                                    Log.e("limbo", i + ":图片为空");
                                                } else {
                                                    Log.d("limbo", "bitmap success");
                                                    int imgboxlist[] = {R.id.LoRa_TYPTJdCameraActivity_iv_F6,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_F5,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_F4,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_F3,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_F2,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_F1};

                                                    ImageView iv = (ImageView) findViewById(imgboxlist[i]);
                                                    try {
                                                        iv.setImageBitmap(bp);
                                                    } catch (Exception e) {
                                                        HintDialog.ShowHintDialog(activity, "图像异常", "错误");
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Log.e("limbo", e.toString());
                                            }
                                        }
                                        LoRa_TYPTJdCameraActivity_et_LYSignal1.setText(LyXh.substring(0, 2));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal2.setText(LyXh.substring(2, 4));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal3.setText(LyXh.substring(4, 6));
                                        LoRa_TYPTJdCameraActivity_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_electricVoltage.setText(volt);


                                    } else if (Zcommand.equals("86")) {
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//信号强度
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String picData = getMsg.substring(54, 1204);//二值化图片数据
                                        String SoftWare = getMsg.substring(1204, 1212);
                                        String DspVersion = getMsg.substring(1212, 1220);
                                        String ZdData = getMsg.substring(1220, 1230);//直读数据
                                        ZdData = Integer.parseInt(ZdData.substring(0, 8)) + "." + ZdData.substring(8, 10);
                                        String believe = getMsg.substring(1230, 1242);
                                        String[] showMsg = new String[5];
                                        for (int i = 0; i < 5; i++) {
                                            showMsg[i] = picData.substring(0 + i * 230, 230 * (i + 1));
                                            try {
                                                Bitmap bp;
                                                byte[] picdata = HzyUtils.getHexBytes(showMsg[i]);
                                                bp = BluetoothConnectThread.binaryPicBytesToBitmap(picdata);
                                                if (bp == null) {
                                                    Log.e("limbo", i + ":图片为空");
                                                } else {
                                                    Log.d("limbo", "bitmap success");
                                                    int imgboxlist[] = {R.id.LoRa_TYPTJdCameraActivity_iv_Z6,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z5,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z4,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z3,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z2,
                                                            R.id.LoRa_TYPTJdCameraActivity_iv_Z1};

                                                    ImageView iv = (ImageView) findViewById(imgboxlist[i]);
                                                    try {
                                                        iv.setImageBitmap(bp);
                                                    } catch (Exception e) {
                                                        HintDialog.ShowHintDialog(activity, "图像异常", "错误");
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Log.e("limbo", e.toString());
                                            }
                                        }
                                        LoRa_TYPTJdCameraActivity_et_data.setText(ZdData);
                                        LoRa_TYPTJdCameraActivity_et_believe6.setText(Integer.parseInt(believe.substring(0, 2), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe5.setText(Integer.parseInt(believe.substring(2, 4), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe4.setText(Integer.parseInt(believe.substring(4, 6), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe3.setText(Integer.parseInt(believe.substring(6, 8), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe2.setText(Integer.parseInt(believe.substring(8, 10), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_believe1.setText(Integer.parseInt(believe.substring(10, 12), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_LYSignal1.setText(LyXh.substring(0, 2));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal2.setText(LyXh.substring(2, 4));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal3.setText(LyXh.substring(4, 6));
                                        LoRa_TYPTJdCameraActivity_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_electricVoltage.setText(volt);

                                    }
                                } else if (command.equals("80")) {
                                    if (Zcommand.equals("85") || Zcommand.equals("86")) {
                                        if (Zcommand.equals("86")){
                                            HintDialog.ShowHintDialog(activity,"写入成功","提示");
                                        }
                                        String JdId = getMsg.substring(10, 24);//节点ID
                                        String JdStyle = getMsg.substring(24, 26);//节点类型
                                        String endSingal = getMsg.substring(26, 28);//信号强度
                                        String volt = getMsg.substring(28, 30);//电池电压
                                        volt = getVolt(volt);
                                        String LyLj = getMsg.substring(30, 36);//路由路径
                                        String LyXh = getMsg.substring(36, 42);//路由信号
                                        String eeprom = getMsg.substring(42, 44);//eeprom起始地址
                                        String parameterLen = getMsg.substring(44, 46);//参数长度
                                        String parameter = getMsg.substring(46, 82);//具体参数

                                        LoRa_TYPTJdCameraActivity_et_LYSignal1.setText(LyXh.substring(0, 2));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal2.setText(LyXh.substring(2, 4));
                                        LoRa_TYPTJdCameraActivity_et_LYSignal3.setText(LyXh.substring(4, 6));
                                        LoRa_TYPTJdCameraActivity_et_endSingal.setText(Integer.parseInt(endSingal, 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_electricVoltage.setText(volt);
                                        LoRa_TYPTJdCameraActivity_et_Y.setText(Integer.parseInt(HzyUtils.changeString1(parameter.substring(0, 4)), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_X1.setText(Integer.parseInt(HzyUtils.changeString1(parameter.substring(4, 8)), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_X2.setText(Integer.parseInt(HzyUtils.changeString1(parameter.substring(8, 12)), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_X3.setText(Integer.parseInt(HzyUtils.changeString1(parameter.substring(12, 16)), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_X4.setText(Integer.parseInt(HzyUtils.changeString1(parameter.substring(16, 20)), 16) + "");
                                        LoRa_TYPTJdCameraActivity_et_X5.setText(Integer.parseInt(HzyUtils.changeString1(parameter.substring(20, 24)), 16) + "");
                                    }
                                }else if (command.equals("8a")){
                                    if (Zcommand.equals("ff")){
                                        Log.d("limbo", "复位返回");
                                        Toast.makeText(activity,"复位成功",Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                timeOut = true;
                                HintDialog.ShowHintDialog(activity, "CRC16校验出错", "提示");
                                break;
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
     * 开始协议设定时间
     * timeMax 1 = 0.1s
     */
    private void prepareTimeStart(int timeMax) {
        timeOut = false;
        if (!HzyUtils.isEmpty(LoRa_TYPTJdCameraActivity_et_aimLYLJ1.getText().toString())) {
            timeMax = timeMax + 15;
        }
        if (!HzyUtils.isEmpty(LoRa_TYPTJdCameraActivity_et_aimLYLJ2.getText().toString())) {
            timeMax = timeMax + 15;
        }
        if (!HzyUtils.isEmpty(LoRa_TYPTJdCameraActivity_et_aimLYLJ3.getText().toString())) {
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

    public static String getTime() {
        String time;
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        time = df.format(date);
        return time;
    }

    public static String getVolt(String sVolt) {
        float x = Integer.parseInt(sVolt, 16);
        float y = (float) ((255 / x) * 1.224);
        String yy = y + "";
        if (!yy.contains(".")) {
            yy = yy + ".000";
        }
        return yy.substring(0, yy.indexOf(".") + 2) + "V";
    }

    private void getEditTextContext() {
        aimJdId = LoRa_TYPTJdCameraActivity_et_aimJDID.getText().toString();
        aimNetId = LoRa_TYPTJdCameraActivity_et_aimNetID.getText().toString();
        aimNetFreq = LoRa_TYPTJdCameraActivity_et_aimNetFreq.getText().toString();
        aimLyLj1 = LoRa_TYPTJdCameraActivity_et_aimLYLJ1.getText().toString();
        aimLyLj2 = LoRa_TYPTJdCameraActivity_et_aimLYLJ2.getText().toString();
        aimLyLj3 = LoRa_TYPTJdCameraActivity_et_aimLYLJ3.getText().toString();
        endSingle = LoRa_TYPTJdCameraActivity_et_endSingal.getText().toString();
        electricVoltage = LoRa_TYPTJdCameraActivity_et_electricVoltage.getText().toString();
        LYSignal1 = LoRa_TYPTJdCameraActivity_et_LYSignal1.getText().toString();
        LYSignal2 = LoRa_TYPTJdCameraActivity_et_LYSignal2.getText().toString();
        LYSignal3 = LoRa_TYPTJdCameraActivity_et_LYSignal3.getText().toString();
        data = LoRa_TYPTJdCameraActivity_et_data.getText().toString();
        believe1 = LoRa_TYPTJdCameraActivity_et_believe1.getText().toString();
        believe2 = LoRa_TYPTJdCameraActivity_et_believe2.getText().toString();
        believe3 = LoRa_TYPTJdCameraActivity_et_believe3.getText().toString();
        believe4 = LoRa_TYPTJdCameraActivity_et_believe4.getText().toString();
        believe5 = LoRa_TYPTJdCameraActivity_et_believe5.getText().toString();
        believe6 = LoRa_TYPTJdCameraActivity_et_believe6.getText().toString();
        Y = LoRa_TYPTJdCameraActivity_et_Y.getText().toString();
        X5 = LoRa_TYPTJdCameraActivity_et_X5.getText().toString();
        X4 = LoRa_TYPTJdCameraActivity_et_X4.getText().toString();
        X3 = LoRa_TYPTJdCameraActivity_et_X3.getText().toString();
        X2 = LoRa_TYPTJdCameraActivity_et_X2.getText().toString();
        X1 = LoRa_TYPTJdCameraActivity_et_X1.getText().toString();


    }

    @Override
    protected void onDestroy() {
        MenuActivity.btAuto = false;
        super.onDestroy();
    }
}
