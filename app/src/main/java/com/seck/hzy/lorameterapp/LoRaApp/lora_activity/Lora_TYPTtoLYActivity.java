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
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/11/6.
 */

public class Lora_TYPTtoLYActivity extends Activity {
    @BindView(R.id.Lora_TYPTtoLY_et_aimLYID)
    EditText Lora_TYPTtoLY_et_aimLYID;
    @BindView(R.id.Lora_TYPTtoLY_et_aimNetFreq)
    EditText Lora_TYPTtoLY_et_aimNetFreq;
    @BindView(R.id.Lora_TYPTtoLY_et_aimNetID)
    EditText Lora_TYPTtoLY_et_aimNetID;
    @BindView(R.id.Lora_TYPTtoLY_et_countOfFloors)
    EditText Lora_TYPTtoLY_et_countOfFloors;
    @BindView(R.id.Lora_TYPTtoLY_et_region)
    EditText Lora_TYPTtoLY_et_region;
    @BindView(R.id.Lora_TYPTtoLY_et_number)
    EditText Lora_TYPTtoLY_et_number;
    @BindView(R.id.Lora_TYPTtoLY_et_electricVoltage)
    EditText Lora_TYPTtoLY_et_electricVoltage;
    @BindView(R.id.Lora_TYPTtoLY_et_softwareVersion)
    EditText Lora_TYPTtoLY_et_softwareVersion;
    @BindView(R.id.Lora_TYPTtoLY_et_hardwareVersion)
    EditText Lora_TYPTtoLY_et_hardwareVersion;
    @BindView(R.id.Lora_TYPTtoLY_et_parameterLYID)
    EditText Lora_TYPTtoLY_et_parameterLYID;
    @BindView(R.id.Lora_TYPTtoLY_et_parameterNetId)
    EditText Lora_TYPTtoLY_et_parameterNetId;
    @BindView(R.id.Lora_TYPTtoLY_et_parameterNetFreq)
    EditText Lora_TYPTtoLY_et_parameterNetFreq;
    @BindView(R.id.Lora_TYPTtoLY_btn_parameterLYID)
    Button Lora_TYPTtoLY_btn_parameterLYID;
    @BindView(R.id.Lora_TYPTtoLY_btn_parameterNetId)
    Button Lora_TYPTtoLY_btn_parameterNetId;
    @BindView(R.id.Lora_TYPTtoLY_btn_parameterNetFreq)
    Button Lora_TYPTtoLY_btn_parameterNetFreq;
    @BindView(R.id.Lora_TYPTtoLY_btn_readAllParameter)
    Button Lora_TYPTtoLY_btn_readAllParameter;
    @BindView(R.id.Lora_TYPTtoLY_btn_writeAllParameter)
    Button Lora_TYPTtoLY_btn_writeAllParameter;
    @BindView(R.id.Lora_TYPTtoLY_btn_reset)
    Button Lora_TYPTtoLY_btn_reset;
    @BindView(R.id.Lora_TYPTtoLY_btn_resetView)
    Button Lora_TYPTtoLY_btn_resetView;
    @BindView(R.id.Lora_TYPTtoLY_tv_show)
    TextView Lora_TYPTtoLY_tv_show;

    boolean timeOut = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_typttoly);
        ButterKnife.bind(this);
        Lora_TYPTtoLY_et_electricVoltage.setKeyListener(null);
        Lora_TYPTtoLY_et_countOfFloors.setKeyListener(null);
        Lora_TYPTtoLY_et_region.setKeyListener(null);
        Lora_TYPTtoLY_et_number.setKeyListener(null);
        Lora_TYPTtoLY_et_softwareVersion.setKeyListener(null);
        Lora_TYPTtoLY_et_hardwareVersion.setKeyListener(null);
        MenuActivity.btAuto = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MenuActivity.btAuto) {
                    try {
                        Thread.sleep(1500);
                        String resultMsg = "";
                        resultMsg = resultMsg + MenuActivity.Cjj_CB_MSG;
                        MenuActivity.Cjj_CB_MSG = "";
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
        Lora_TYPTtoLY_btn_writeAllParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HzyUtils.showProgressDialog(Lora_TYPTtoLYActivity.this);
                String aimNetID = Lora_TYPTtoLY_et_aimNetID.getText().toString().trim();
                String aimNetFreq = Lora_TYPTtoLY_et_aimNetFreq.getText().toString().trim();
                String aimLYID = Lora_TYPTtoLY_et_aimLYID.getText().toString().trim();
                String parameterLYID = Lora_TYPTtoLY_et_parameterLYID.getText().toString().trim();
                String parameterNetId = Lora_TYPTtoLY_et_parameterNetId.getText().toString().trim();
                String parameterNetFreq = Lora_TYPTtoLY_et_parameterNetFreq.getText().toString().trim();
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength1(aimNetFreq, 6);
                if (HzyUtils.isConformToRange(aimNetFreq) || HzyUtils.isConformToRange(parameterNetFreq)) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "频率超出允许范围，请重试。(范围为4000000KHz~510000KHz)", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart();
                aimLYID = HzyUtils.isLength(aimLYID, 2);
                parameterLYID = HzyUtils.isLength(parameterLYID, 2);
                parameterNetId = HzyUtils.isLength(parameterNetId, 4);
                parameterNetFreq = HzyUtils.isLength(parameterNetFreq, 6);


                String sendMsg = "680023" + aimNetID + aimNetFreq + "aa0000000000" + aimLYID +
                        "0000008a07000000000000" + parameterLYID + parameterNetId + parameterNetFreq;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Lora_TYPTtoLY_tv_show.append("\n发送:" + sendMsg + "\n");
                MenuActivity.sendCmd(sendMsg);


            }
        });
        Lora_TYPTtoLY_btn_readAllParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date;
                HzyUtils.showProgressDialog(Lora_TYPTtoLYActivity.this);
                String aimNetID = Lora_TYPTtoLY_et_aimNetID.getText().toString().trim();
                String aimNetFreq = Lora_TYPTtoLY_et_aimNetFreq.getText().toString().trim();
                String aimLYID = Lora_TYPTtoLY_et_aimLYID.getText().toString().trim();
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength1(aimNetFreq, 6);
                if (HzyUtils.isConformToRange(aimNetFreq)) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "频率超出允许范围，请重试。(范围为4000000KHz~510000KHz)", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart();
                aimLYID = HzyUtils.isLength(aimLYID, 2);
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
                date = df.format(d);

                String sendMsg = "68001d" + aimNetID + aimNetFreq + "aa0000000000" + aimLYID +
                        "0000008a08" + date;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Lora_TYPTtoLY_tv_show.append("\n发送:" + sendMsg + "\n");
                MenuActivity.sendCmd(sendMsg);
            }
        });
        Lora_TYPTtoLY_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTimeStart();
                HzyUtils.showProgressDialog(Lora_TYPTtoLYActivity.this);
                String date;
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
                date = df.format(d);
                String sendMsg = "68001d0000000000000000000000000000008aff" + date;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Lora_TYPTtoLY_tv_show.append("\n发送:" + sendMsg + "\n");
                MenuActivity.sendCmd(sendMsg);
            }
        });
        Lora_TYPTtoLY_btn_resetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lora_TYPTtoLY_tv_show.setText("");
                Lora_TYPTtoLY_et_countOfFloors.setText("");
                Lora_TYPTtoLY_et_region.setText("");
                Lora_TYPTtoLY_et_number.setText("");
                Lora_TYPTtoLY_et_electricVoltage.setText("");
                Lora_TYPTtoLY_et_softwareVersion.setText("");
                Lora_TYPTtoLY_et_hardwareVersion.setText("");
            }
        });
        /**
         * 单独写入参数路由网络频率
         */
        Lora_TYPTtoLY_btn_parameterNetFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(Lora_TYPTtoLYActivity.this);
                String aimNetID = Lora_TYPTtoLY_et_aimNetID.getText().toString().trim();
                String aimNetFreq = Lora_TYPTtoLY_et_aimNetFreq.getText().toString().trim();
                String aimLYID = Lora_TYPTtoLY_et_aimLYID.getText().toString().trim();
                String parameterNetFreq = Lora_TYPTtoLY_et_parameterNetFreq.getText().toString().trim();
                if (HzyUtils.isEmpty(aimLYID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimNetID)) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this,
                            "目标路由ID，目标路由网络ID及目标路由网络频率皆不可为空。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                if (HzyUtils.isConformToRange(aimNetFreq) || HzyUtils.isConformToRange(parameterNetFreq)) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "频率超出允许范围，请重试。(范围为4000000KHz~510000KHz)", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart();
                aimLYID = HzyUtils.isLength(aimLYID, 2);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength1(aimNetFreq, 6);
                parameterNetFreq = HzyUtils.isLength1(parameterNetFreq, 6);
                if (HzyUtils.isConformToRange(aimNetFreq) || HzyUtils.isConformToRange(parameterNetFreq)) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "频率超出允许范围，请重试。(范围为4000000KHz~510000KHz)", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                parameterNetFreq = HzyUtils.isLength(parameterNetFreq, 6);
                String sendMsg = "68001a" + aimNetID + aimNetFreq + "aa0000000000" + aimLYID + "0000008a03" + parameterNetFreq;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Lora_TYPTtoLY_tv_show.append("\n发送:" + sendMsg + "\n");
                MenuActivity.sendCmd(sendMsg);


            }
        });

        /**
         * 单独写入路由ID
         */
        Lora_TYPTtoLY_btn_parameterLYID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(Lora_TYPTtoLYActivity.this);
                String aimNetID = Lora_TYPTtoLY_et_aimNetID.getText().toString().trim();
                String aimNetFreq = Lora_TYPTtoLY_et_aimNetFreq.getText().toString().trim();
                String aimLYID = Lora_TYPTtoLY_et_aimLYID.getText().toString().trim();
                String parameterLYID = Lora_TYPTtoLY_et_parameterLYID.getText().toString().trim();

                if (HzyUtils.isEmpty(aimLYID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimNetID)) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this,
                            "目标路由ID，目标路由网络ID及目标路由网络频率皆不可为空。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart();
                aimLYID = HzyUtils.isLength(aimLYID, 2);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                parameterLYID = HzyUtils.isLength(parameterLYID, 2);
                aimNetFreq = HzyUtils.isLength1(aimNetFreq, 6);

                if (HzyUtils.isConformToRange(aimNetFreq)) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "频率超出允许范围，请重试。(范围为4000000KHz~510000KHz)", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                if (parameterLYID.length() > 2) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "参数路由ID长度为一个字节。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;

                }

                String sendMsg = "68001e" + aimNetID + aimNetFreq + "aa0000000000" + aimLYID + "0000008a04"
                        + "000000000000" + parameterLYID;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Lora_TYPTtoLY_tv_show.append("\n发送:" + sendMsg + "\n");
                MenuActivity.sendCmd(sendMsg);

            }
        });
        /**
         * 单独写入路由网络ID
         */
        Lora_TYPTtoLY_btn_parameterNetId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(Lora_TYPTtoLYActivity.this);
                String aimNetID = Lora_TYPTtoLY_et_aimNetID.getText().toString().trim();
                String aimNetFreq = Lora_TYPTtoLY_et_aimNetFreq.getText().toString().trim();
                String aimLYID = Lora_TYPTtoLY_et_aimLYID.getText().toString().trim();
                String parameterNetId = Lora_TYPTtoLY_et_parameterNetId.getText().toString().trim();
                if (HzyUtils.isEmpty(aimLYID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimNetID)) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this,
                            "目标路由ID，目标路由网络ID及目标路由网络频率皆不可为空。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart();
                aimLYID = HzyUtils.isLength(aimLYID, 2);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                parameterNetId = HzyUtils.isLength(parameterNetId, 4);
                aimNetFreq = HzyUtils.isLength1(aimNetFreq, 6);

                if (HzyUtils.isConformToRange(aimNetFreq)) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "频率超出允许范围，请重试。(范围为4000000KHz~510000KHz)", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                if (parameterNetId.length() > 4) {
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "参数路由网络ID长度为两个字节。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;

                }

                String sendMsg = "680019" + aimNetID + aimNetFreq
                        + "aa0000000000" + aimLYID + "0000008a02" + parameterNetId;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Lora_TYPTtoLY_tv_show.append("\n发送:" + sendMsg + "\n");
                MenuActivity.sendCmd(sendMsg);

            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString().toLowerCase();
                    getMsg = getMsg.contains("68002a8a00") ? getMsg.substring(getMsg.indexOf("68002a8a00")) : getMsg;
                    getMsg = getMsg.contains("6800248a87") ? getMsg.substring(getMsg.indexOf("6800248a87")) : getMsg;
                    getMsg = getMsg.contains("68002f8a87") ? getMsg.substring(getMsg.indexOf("68002f8a87")) : getMsg;
                    getMsg = getMsg.contains("68001e8aff") ? getMsg.substring(getMsg.indexOf("68001e8aff")) : getMsg;
                    getMsg = getMsg.contains("68001f8a84") ? getMsg.substring(getMsg.indexOf("68001f8a84")) : getMsg;
                    //磁铁
                    if (getMsg.length() >= 84 && getMsg.contains("68002a8a00") &&
                            getMsg.substring(getMsg.indexOf("68002a8a00") + 82, getMsg.indexOf("68002a8a00") + 84).equals("16")) {
                        Lora_TYPTtoLY_tv_show.append("接收到:" + getMsg + "\n");
                        String crc16back = getMsg.substring(78, 82);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 78));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        Log.d("limbo", "磁铁激活返回");
                        String LYID = getMsg.substring(22, 24);//路由ID
                        String electricVoltage = ((float) 255 / Integer.parseInt(getMsg.substring(26, 28), 16)) * 1.224 + "";//电池电压
                        electricVoltage = electricVoltage.substring(0, electricVoltage.indexOf(".") + 3) + "V";
                        String netId = getMsg.substring(32, 36);//网络id
                        String netFreq = getMsg.substring(36, 42);//网络频率
                        String countOfFloors = getMsg.substring(42, 44);//层数
                        String region = getMsg.substring(44, 46);//区域
                        String number = getMsg.substring(46, 50);//编号
                        String softwareVersion = getMsg.substring(54, 62);//软件版本号
                        String hardwareVersion = getMsg.substring(62, 70);//硬件版本号
                        Lora_TYPTtoLY_et_aimLYID.setText(LYID);
                        Lora_TYPTtoLY_et_electricVoltage.setText(electricVoltage);
                        Lora_TYPTtoLY_et_aimNetID.setText(netId);
                        Lora_TYPTtoLY_et_aimNetFreq.setText(netFreq);
                        Lora_TYPTtoLY_et_countOfFloors.setText(countOfFloors);
                        Lora_TYPTtoLY_et_region.setText(region);
                        Lora_TYPTtoLY_et_number.setText(number);
                        Lora_TYPTtoLY_et_softwareVersion.setText(softwareVersion);
                        Lora_TYPTtoLY_et_hardwareVersion.setText(hardwareVersion);
                    } //写入
                    else if (getMsg.length() >= 72 && getMsg.contains("6800248a87") &&
                            getMsg.substring(getMsg.indexOf("6800248a87") + 70, 72).equals("16")) {
                        Lora_TYPTtoLY_tv_show.append("接收到:" + getMsg + "\n");
                        String crc16back = getMsg.substring(66, 70);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 66));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        Log.d("limbo", "写入返回");
                        String electricVoltage = ((float) 255 / Integer.parseInt(getMsg.substring(28, 30), 16)) * 1.224 + "";//电池电压
                        electricVoltage = electricVoltage.substring(0, electricVoltage.indexOf(".") + 3) + "V";
                        String aimnetId = getMsg.substring(56, 60);//网络id
                        String aimnetFreq = getMsg.substring(60, 66);//网络频率
                        String aimLYID = getMsg.substring(54, 56);
                        Lora_TYPTtoLY_et_electricVoltage.setText(electricVoltage);
                        Lora_TYPTtoLY_et_aimNetID.setText(aimnetId);
                        Lora_TYPTtoLY_et_aimNetFreq.setText(aimnetFreq);
                        Lora_TYPTtoLY_et_aimLYID.setText(aimLYID);

                        HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "写入成功。", "提示");
                        timeOut = true;
                        HzyUtils.closeProgressDialog();
                    }//读取
                    else if (getMsg.length() >= 94 && getMsg.contains("68002f8a88") &&
                            getMsg.substring(getMsg.indexOf("68002f8a88") + 92, 94).equals("16")) {
                        Lora_TYPTtoLY_tv_show.append("接收到:" + getMsg + "\n");
                        String crc16back = getMsg.substring(88, 92);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 88));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        Log.d("limbo", "读取返回");
                        String electricVoltage = ((float) 255 / Integer.parseInt(getMsg.substring(28, 30), 16)) * 1.224 + "";//电池电压
                        electricVoltage = electricVoltage.substring(0, electricVoltage.indexOf(".") + 3) + "V";
                        String aimLYID = getMsg.substring(22, 24);
                        String aimNetID = getMsg.substring(42, 46);
                        String aimNetFreq = getMsg.substring(46, 52);
                        String countOfFloors = getMsg.substring(52, 54);//层数
                        String region = getMsg.substring(54, 56);//区域
                        String number = getMsg.substring(56, 60);//编号
                        String softwareVersion = getMsg.substring(64, 72);//软件版本号
                        String hardwareVersion = getMsg.substring(72, 80);//硬件版本号
                        Lora_TYPTtoLY_et_electricVoltage.setText(electricVoltage);
                        Lora_TYPTtoLY_et_aimLYID.setText(aimLYID);
                        Lora_TYPTtoLY_et_aimNetID.setText(aimNetID);
                        Lora_TYPTtoLY_et_aimNetFreq.setText(aimNetFreq);
                        Lora_TYPTtoLY_et_countOfFloors.setText(countOfFloors);
                        Lora_TYPTtoLY_et_region.setText(region);
                        Lora_TYPTtoLY_et_number.setText(number);
                        Lora_TYPTtoLY_et_softwareVersion.setText(softwareVersion);
                        Lora_TYPTtoLY_et_hardwareVersion.setText(hardwareVersion);

                        HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "读取成功。", "提示");
                        timeOut = true;
                        HzyUtils.closeProgressDialog();
                    }//复位
                    else if (getMsg.length() >= 60 && getMsg.contains("68001e8aff") &&
                            getMsg.substring(getMsg.indexOf("68001e8aff") + 58, 60).equals("16")) {
                        Lora_TYPTtoLY_tv_show.append("接收到:" + getMsg + "\n");
                        String crc16back = getMsg.substring(54, 58);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 54));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        Log.d("limbo", "复位返回");
                        timeOut = true;
                        HzyUtils.closeProgressDialog();
                        HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "复位成功。", "提示");
                    }//修改路由ID
                    else if (getMsg.length() >= 62 && getMsg.contains("68001f8a84") &&
                            getMsg.substring(getMsg.indexOf("68001f8a84") + 60, 62).equals("16")) {
                        Lora_TYPTtoLY_tv_show.append("接收到:" + getMsg + "\n");
                        String crc16back = getMsg.substring(56, 60);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 56));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        Log.d("limbo", "修改路由ID返回");
                        String electricVoltage = ((float) 255 / Integer.parseInt(getMsg.substring(28, 30), 16)) * 1.224 + "";//电池电压
                        electricVoltage = electricVoltage.substring(0, electricVoltage.indexOf(".") + 3) + "V";
                        String aimLYID = getMsg.substring(54, 56);
                        Lora_TYPTtoLY_et_electricVoltage.setText(electricVoltage);
                        Lora_TYPTtoLY_et_aimLYID.setText(aimLYID);

                        HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "修改路由ID成功。", "提示");
                        timeOut = true;
                        HzyUtils.closeProgressDialog();

                    }//修改路由网络ID
                    else if (getMsg.length() >= 52 && getMsg.contains("68001a8a82") &&
                            getMsg.substring(getMsg.indexOf("68001a8a82") + 50, 52).equals("16")) {
                        Lora_TYPTtoLY_tv_show.append("接收到:" + getMsg + "\n");
                        String crc16back = getMsg.substring(46, 50);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 46));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        Log.d("limbo", "修改路由网络ID返回");
                        String electricVoltage = ((float) 255 / Integer.parseInt(getMsg.substring(28, 30), 16)) * 1.224 + "";//电池电压
                        electricVoltage = electricVoltage.substring(0, electricVoltage.indexOf(".") + 3) + "V";
                        String aimNetID = getMsg.substring(42, 46);
                        Lora_TYPTtoLY_et_electricVoltage.setText(electricVoltage);
                        Lora_TYPTtoLY_et_aimNetID.setText(aimNetID);

                        HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "修改路由网络ID成功。", "提示");
                        timeOut = true;
                        HzyUtils.closeProgressDialog();
                    }//修改路由网络频率
                    else if (getMsg.length() >= 54 && getMsg.contains("68001b8a83") &&
                            getMsg.substring(getMsg.indexOf("68001b8a83") + 52, 54).equals("16")) {
                        Lora_TYPTtoLY_tv_show.append("接收到:" + getMsg + "\n");
                        String crc16back = getMsg.substring(48, 52);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 48));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        Log.d("limbo", "修改路由网络频率返回");
                        String electricVoltage = ((float) 255 / Integer.parseInt(getMsg.substring(28, 30), 16)) * 1.224 + "";//电池电压
                        electricVoltage = electricVoltage.substring(0, electricVoltage.indexOf(".") + 3) + "V";
                        String aimNetFreq = getMsg.substring(42, 48);
                        Lora_TYPTtoLY_et_electricVoltage.setText(electricVoltage);
                        Lora_TYPTtoLY_et_aimNetFreq.setText(aimNetFreq);

                        HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "修改路由网络频率成功。", "提示");
                        timeOut = true;
                        HzyUtils.closeProgressDialog();

                    }

                    break;
                case 0x99:
                    HintDialog.ShowHintDialog(Lora_TYPTtoLYActivity.this, "无数据返回，请重试。", "提示");
                    break;

                default:
                    break;

            }
        }
    };

    /**
     * 开始协议设定时间
     */
    private void prepareTimeStart() {
        timeOut = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 50; i++) {
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

    @Override
    protected void onDestroy() {
        MenuActivity.btAuto = false;
        super.onDestroy();
    }
}
