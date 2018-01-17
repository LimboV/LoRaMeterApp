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

/**
 * Created by limbo on 2017/11/16.
 */

public class LoRa_TYPTtoJDActivity extends Activity {
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_aimJDID)
    EditText LoRa_TYPTtoJDActivity_et_aimJDID;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_aimNetID)
    EditText LoRa_TYPTtoJDActivity_et_aimNetID;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_aimNetFreq)
    EditText LoRa_TYPTtoJDActivity_et_aimNetFreq;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_aimLYLJ1)
    EditText LoRa_TYPTtoJDActivity_et_aimLYLJ1;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_aimLYLJ2)
    EditText LoRa_TYPTtoJDActivity_et_aimLYLJ2;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_aimLYLJ3)
    EditText LoRa_TYPTtoJDActivity_et_aimLYLJ3;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_deviceStyle)
    EditText LoRa_TYPTtoJDActivity_et_deviceStyle;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_floor)
    EditText LoRa_TYPTtoJDActivity_et_floor;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_region)
    EditText LoRa_TYPTtoJDActivity_et_region;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_totalNum)
    EditText LoRa_TYPTtoJDActivity_et_totalNum;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_partNum)
    EditText LoRa_TYPTtoJDActivity_et_partNum;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_endSingal)
    EditText LoRa_TYPTtoJDActivity_et_endSingal;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_LYSignal1)
    EditText LoRa_TYPTtoJDActivity_et_LYSignal1;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_LYSignal2)
    EditText LoRa_TYPTtoJDActivity_et_LYSignal2;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_LYSignal3)
    EditText LoRa_TYPTtoJDActivity_et_LYSignal3;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_electricVoltage)
    EditText LoRa_TYPTtoJDActivity_et_electricVoltage;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_softwareVersion)
    EditText LoRa_TYPTtoJDActivity_et_softwareVersion;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_hardwareVersion)
    EditText LoRa_TYPTtoJDActivity_et_hardwareVersion;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_otherVersion)
    EditText LoRa_TYPTtoJDActivity_et_otherVersion;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_data)
    EditText LoRa_TYPTtoJDActivity_et_data;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_state)
    EditText LoRa_TYPTtoJDActivity_et_state;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_valveState)
    EditText LoRa_TYPTtoJDActivity_et_valveState;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_CSJDID)
    EditText LoRa_TYPTtoJDActivity_et_CSJDID;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_netID)
    EditText LoRa_TYPTtoJDActivity_et_netID;
    @BindView(R.id.LoRa_TYPTtoJDActivity_et_netFreq)
    EditText LoRa_TYPTtoJDActivity_et_netFreq;
    @BindView(R.id.LoRa_TYPTtoJDActivity_btn_reset)
    Button LoRa_TYPTtoJDActivity_btn_reset;
    @BindView(R.id.LoRa_TYPTtoJDActivity_btn_writeAll)
    Button LoRa_TYPTtoJDActivity_btn_writeAll;
    @BindView(R.id.LoRa_TYPTtoJDActivity_btn_readAll)
    Button LoRa_TYPTtoJDActivity_btn_readAll;
    @BindView(R.id.LoRa_TYPTtoJDActivity_btn_resetActivity)
    Button LoRa_TYPTtoJDActivity_btn_resetActivity;
    @BindView(R.id.LoRa_TYPTtoJDActivity_btn_setFreq)
    Button LoRa_TYPTtoJDActivity_btn_setFreq;
    @BindView(R.id.LoRa_TYPTtoJDActivity_btn_setNetId)
    Button LoRa_TYPTtoJDActivity_btn_setNetId;
    @BindView(R.id.LoRa_TYPTtoJDActivity_btn_setJDId)
    Button LoRa_TYPTtoJDActivity_btn_setJDId;

    boolean timeOut = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_typttojd);
        ButterKnife.bind(this);


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
        /**
         * 设置节点ID
         */
        LoRa_TYPTtoJDActivity_btn_setJDId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_TYPTtoJDActivity.this);
                String aimNetID = LoRa_TYPTtoJDActivity_et_aimNetID.getText().toString().trim();
                String aimNetFreq = LoRa_TYPTtoJDActivity_et_aimNetFreq.getText().toString().trim();
                String aimJDID = LoRa_TYPTtoJDActivity_et_aimJDID.getText().toString().trim();
                String JDID = LoRa_TYPTtoJDActivity_et_CSJDID.getText().toString().trim();
                String LYLJ1 = LoRa_TYPTtoJDActivity_et_aimLYLJ1.getText().toString().trim();
                String LYLJ2 = LoRa_TYPTtoJDActivity_et_aimLYLJ2.getText().toString().trim();
                String LYLJ3 = LoRa_TYPTtoJDActivity_et_aimLYLJ3.getText().toString().trim();
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(JDID)) {
                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this,
                            "参数节点ID、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                JDID = HzyUtils.isLength(JDID, 14);
                LYLJ1 = HzyUtils.isLength(LYLJ1, 2);
                LYLJ2 = HzyUtils.isLength(LYLJ2, 2);
                LYLJ3 = HzyUtils.isLength(LYLJ3, 2);
                String sendMsg = "68001e" + aimNetID + aimNetFreq + aimJDID + LYLJ1 + LYLJ2 + LYLJ3 + "8004" + JDID;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "修改节点ID" + sendMsg);
                MenuActivity.sendCmd(sendMsg);


            }
        });
        /**
         * 设置网络ID
         */
        LoRa_TYPTtoJDActivity_btn_setNetId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_TYPTtoJDActivity.this);
                String aimNetID = LoRa_TYPTtoJDActivity_et_aimNetID.getText().toString().trim();
                String aimNetFreq = LoRa_TYPTtoJDActivity_et_aimNetFreq.getText().toString().trim();
                String aimJDID = LoRa_TYPTtoJDActivity_et_aimJDID.getText().toString().trim();
                String netID = LoRa_TYPTtoJDActivity_et_netID.getText().toString().trim();
                String LYLJ1 = LoRa_TYPTtoJDActivity_et_aimLYLJ1.getText().toString().trim();
                String LYLJ2 = LoRa_TYPTtoJDActivity_et_aimLYLJ2.getText().toString().trim();
                String LYLJ3 = LoRa_TYPTtoJDActivity_et_aimLYLJ3.getText().toString().trim();
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(netID)) {
                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this,
                            "参数节点网络ID、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                netID = HzyUtils.isLength(netID, 4);
                LYLJ1 = HzyUtils.isLength(LYLJ1, 2);
                LYLJ2 = HzyUtils.isLength(LYLJ2, 2);
                LYLJ3 = HzyUtils.isLength(LYLJ3, 2);
                String sendMsg = "680019" + aimNetID + aimNetFreq + aimJDID + LYLJ1 + LYLJ2 + LYLJ3 + "8002" + netID;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "修改节点网络ID" + sendMsg);
                MenuActivity.sendCmd(sendMsg);


            }
        });
        /**
         * 设置网络频率
         */
        LoRa_TYPTtoJDActivity_btn_setFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_TYPTtoJDActivity.this);
                String aimNetID = LoRa_TYPTtoJDActivity_et_aimNetID.getText().toString().trim();
                String aimNetFreq = LoRa_TYPTtoJDActivity_et_aimNetFreq.getText().toString().trim();
                String aimJDID = LoRa_TYPTtoJDActivity_et_aimJDID.getText().toString().trim();
                String netFreq = LoRa_TYPTtoJDActivity_et_netFreq.getText().toString().trim();
                String LYLJ1 = LoRa_TYPTtoJDActivity_et_aimLYLJ1.getText().toString().trim();
                String LYLJ2 = LoRa_TYPTtoJDActivity_et_aimLYLJ2.getText().toString().trim();
                String LYLJ3 = LoRa_TYPTtoJDActivity_et_aimLYLJ3.getText().toString().trim();
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimNetID) || HzyUtils.isEmpty(netFreq)) {
                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this,
                            "参数节点网络频率、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                if (HzyUtils.isConformToRange(netFreq)) {
                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this,
                            "目标节点网络频率超出范围。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                netFreq = HzyUtils.isLength(netFreq, 6);
                LYLJ1 = HzyUtils.isLength(LYLJ1, 2);
                LYLJ2 = HzyUtils.isLength(LYLJ2, 2);
                LYLJ3 = HzyUtils.isLength(LYLJ3, 2);
                String sendMsg = "68001a" + aimNetID + aimNetFreq + aimJDID + LYLJ1 + LYLJ2 + LYLJ3 + "8003" + netFreq;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "修改节点网络频率" + sendMsg);
                MenuActivity.sendCmd(sendMsg);


            }
        });
        /**
         * 读取所有数据
         */
        LoRa_TYPTtoJDActivity_btn_readAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_TYPTtoJDActivity.this);
                String aimNetID = LoRa_TYPTtoJDActivity_et_aimNetID.getText().toString().trim();
                String aimNetFreq = LoRa_TYPTtoJDActivity_et_aimNetFreq.getText().toString().trim();
                String aimJDID = LoRa_TYPTtoJDActivity_et_aimJDID.getText().toString().trim();
                String LYLJ1 = LoRa_TYPTtoJDActivity_et_aimLYLJ1.getText().toString().trim();
                String LYLJ2 = LoRa_TYPTtoJDActivity_et_aimLYLJ2.getText().toString().trim();
                String LYLJ3 = LoRa_TYPTtoJDActivity_et_aimLYLJ3.getText().toString().trim();
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimNetID)) {
                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this,
                            "参数节点网络频率、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                if (HzyUtils.isConformToRange(aimNetFreq)) {
                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this,
                            "目标节点网络频率超出范围。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                LYLJ1 = HzyUtils.isLength(LYLJ1, 2);
                LYLJ2 = HzyUtils.isLength(LYLJ2, 2);
                LYLJ3 = HzyUtils.isLength(LYLJ3, 2);
                String time;
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
                time = df.format(d);

                String sendMsg = "680019" + aimNetID + aimNetFreq + aimJDID + LYLJ1 + LYLJ2 + LYLJ3 + "8009" + time;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "一键读取所有参数" + sendMsg);
                MenuActivity.sendCmd(sendMsg);

            }
        });
        /**
         * 修改所有数据
         */
        LoRa_TYPTtoJDActivity_btn_writeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_TYPTtoJDActivity.this);
                String aimNetID = LoRa_TYPTtoJDActivity_et_aimNetID.getText().toString().trim();
                String aimNetFreq = LoRa_TYPTtoJDActivity_et_aimNetFreq.getText().toString().trim();
                String aimJDID = LoRa_TYPTtoJDActivity_et_aimJDID.getText().toString().trim();
                String LYLJ1 = LoRa_TYPTtoJDActivity_et_aimLYLJ1.getText().toString().trim();
                String LYLJ2 = LoRa_TYPTtoJDActivity_et_aimLYLJ2.getText().toString().trim();
                String LYLJ3 = LoRa_TYPTtoJDActivity_et_aimLYLJ3.getText().toString().trim();
                String netFreq = LoRa_TYPTtoJDActivity_et_netFreq.getText().toString().trim();
                String netId = LoRa_TYPTtoJDActivity_et_netID.getText().toString().trim();
                String jdid = LoRa_TYPTtoJDActivity_et_CSJDID.getText().toString().trim();
                if (HzyUtils.isEmpty(aimJDID) || HzyUtils.isEmpty(aimNetFreq) || HzyUtils.isEmpty(aimNetID)
                        || HzyUtils.isEmpty(netFreq) || HzyUtils.isEmpty(netId) || HzyUtils.isEmpty(jdid)) {
                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this,
                            "参数节点网络频率、目标节点ID、目标节点网络ID及目标节点网络频率皆不可为空。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                if (HzyUtils.isConformToRange(aimNetFreq) || HzyUtils.isConformToRange(netFreq)) {
                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this,
                            "网络频率超出范围。", "提示");
                    HzyUtils.closeProgressDialog();
                    return;
                }
                prepareTimeStart(50);
                aimNetID = HzyUtils.isLength(aimNetID, 4);
                aimNetFreq = HzyUtils.isLength(aimNetFreq, 6);
                aimJDID = HzyUtils.isLength(aimJDID, 14);
                LYLJ1 = HzyUtils.isLength(LYLJ1, 2);
                LYLJ2 = HzyUtils.isLength(LYLJ2, 2);
                LYLJ3 = HzyUtils.isLength(LYLJ3, 2);
                netFreq = HzyUtils.isLength(netFreq, 6);
                netId = HzyUtils.isLength(netId, 4);
                jdid = HzyUtils.isLength(jdid, 14);

                String sendMsg = "680023" + aimNetID + aimNetFreq + aimJDID + LYLJ1 + LYLJ2 + LYLJ3 + "8007" + jdid + netId + netFreq;
                sendMsg = sendMsg + HzyUtils.CRC16(sendMsg) + "16";
                Log.d("limbo", "一键读取所有参数" + sendMsg);
                MenuActivity.sendCmd(sendMsg);


            }
        });
        LoRa_TYPTtoJDActivity_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(LoRa_TYPTtoJDActivity.this);
                prepareTimeStart(50);
                HzyUtils.showProgressDialog(LoRa_TYPTtoJDActivity.this);
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
        LoRa_TYPTtoJDActivity_btn_resetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                LoRa_TYPTtoJDActivity_et_aimJDID.setText("");
                //                LoRa_TYPTtoJDActivity_et_aimNetID.setText("");
                //                LoRa_TYPTtoJDActivity_et_aimNetFreq.setText("");
                //                LoRa_TYPTtoJDActivity_et_aimLYLJ1.setText("");
                //                LoRa_TYPTtoJDActivity_et_aimLYLJ2.setText("");
                //                LoRa_TYPTtoJDActivity_et_aimLYLJ3.setText("");
                LoRa_TYPTtoJDActivity_et_deviceStyle.setText("");
                LoRa_TYPTtoJDActivity_et_floor.setText("");
                LoRa_TYPTtoJDActivity_et_region.setText("");
                LoRa_TYPTtoJDActivity_et_totalNum.setText("");
                LoRa_TYPTtoJDActivity_et_partNum.setText("");
                LoRa_TYPTtoJDActivity_et_endSingal.setText("");
                LoRa_TYPTtoJDActivity_et_LYSignal1.setText("");
                LoRa_TYPTtoJDActivity_et_LYSignal2.setText("");
                LoRa_TYPTtoJDActivity_et_LYSignal3.setText("");
                LoRa_TYPTtoJDActivity_et_electricVoltage.setText("");
                LoRa_TYPTtoJDActivity_et_softwareVersion.setText("");
                LoRa_TYPTtoJDActivity_et_hardwareVersion.setText("");
                LoRa_TYPTtoJDActivity_et_otherVersion.setText("");
                LoRa_TYPTtoJDActivity_et_data.setText("");
                LoRa_TYPTtoJDActivity_et_state.setText("");
                LoRa_TYPTtoJDActivity_et_valveState.setText("");
                LoRa_TYPTtoJDActivity_et_CSJDID.setText("");
                LoRa_TYPTtoJDActivity_et_netID.setText("");
                LoRa_TYPTtoJDActivity_et_netFreq.setText("");

            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String getMsg = msg.obj.toString().toLowerCase();
                    /**
                     * 磁铁激活返回
                     */
                    String returnMsg = "";
                    getMsg = getMsg.contains("6800318000") ? getMsg.substring(getMsg.indexOf("6800318000")) : getMsg;
                    getMsg = getMsg.contains("68001a8082") ? getMsg.substring(getMsg.indexOf("68001a8082")) : getMsg;
                    if (getMsg.length() >= 98 && getMsg.contains("6800318000") &&
                            getMsg.substring(getMsg.indexOf("6800318000") + 96, getMsg.indexOf("6800318000") + 98).equals("16")) {
                        Log.d("limbo", "磁铁激活返回:" + getMsg);
                        String crc16back = getMsg.substring(92, 96);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 92));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
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
                        String data = getMsg.substring(78, 88);//直读数据
                        data = Integer.parseInt(data.substring(0, 6)) + "." + data.substring(6, 10);
                        String dataState = getMsg.substring(88, 90);//数据状态
                        String valveState = getMsg.substring(90, 92);//阀控状态
                        LoRa_TYPTtoJDActivity_et_aimJDID.setText(JDid);
                        LoRa_TYPTtoJDActivity_et_endSingal.setText(endSingal);
                        LoRa_TYPTtoJDActivity_et_electricVoltage.setText(electricVoltage);
                        LoRa_TYPTtoJDActivity_et_deviceStyle.setText(Subtype);
                        LoRa_TYPTtoJDActivity_et_aimNetID.setText(netId);
                        LoRa_TYPTtoJDActivity_et_aimNetFreq.setText(netFreq);
                        LoRa_TYPTtoJDActivity_et_floor.setText(floor);
                        LoRa_TYPTtoJDActivity_et_region.setText(region);
                        LoRa_TYPTtoJDActivity_et_totalNum.setText(totalNum);
                        LoRa_TYPTtoJDActivity_et_partNum.setText(partNum);
                        LoRa_TYPTtoJDActivity_et_softwareVersion.setText(softwareVersion);
                        LoRa_TYPTtoJDActivity_et_hardwareVersion.setText(hardwareVersion);
                        LoRa_TYPTtoJDActivity_et_otherVersion.setText(otherVersion);
                        LoRa_TYPTtoJDActivity_et_data.setText(data);
                        LoRa_TYPTtoJDActivity_et_state.setText(dataState);
                        LoRa_TYPTtoJDActivity_et_valveState.setText(valveState);
                        returnMsg = "磁激活获取数据成功";
                    } else if (getMsg.length() >= 52 && getMsg.contains("68001a8082") &&
                            getMsg.substring(getMsg.indexOf("68001a8082") + 50, getMsg.indexOf("68001a8082") + 52).equals("16")) {
                        Log.d("limbo", "修改节点网络ID返回:" + getMsg);
                        String crc16back = getMsg.substring(46, 50);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 46));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, "CRC16校验出错。", "提示");
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
                        String netId = getMsg.substring(42, 46);//目标节点网络ID
                        LoRa_TYPTtoJDActivity_et_electricVoltage.setText(electricVoltage);
                        LoRa_TYPTtoJDActivity_et_LYSignal1.setText(LYXH1);
                        LoRa_TYPTtoJDActivity_et_LYSignal2.setText(LYXH2);
                        LoRa_TYPTtoJDActivity_et_LYSignal3.setText(LYXH3);
                        LoRa_TYPTtoJDActivity_et_aimNetID.setText(netId);
                        LoRa_TYPTtoJDActivity_et_endSingal.setText(endSingal);

                        returnMsg = "修改节点网络id成功";

                    } else if (getMsg.length() >= 62 && getMsg.contains("68001f8084") &&
                            getMsg.substring(getMsg.indexOf("68001f8084") + 60, getMsg.indexOf("68001f8084") + 62).equals("16")) {
                        Log.d("limbo", "修改节点ID返回:" + getMsg);
                        String crc16back = getMsg.substring(56, 60);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 56));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, "CRC16校验出错。", "提示");
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
                        String jdid = getMsg.substring(42, 56);//目标节点ID
                        LoRa_TYPTtoJDActivity_et_electricVoltage.setText(electricVoltage);
                        LoRa_TYPTtoJDActivity_et_LYSignal1.setText(LYXH1);
                        LoRa_TYPTtoJDActivity_et_LYSignal2.setText(LYXH2);
                        LoRa_TYPTtoJDActivity_et_LYSignal3.setText(LYXH3);
                        LoRa_TYPTtoJDActivity_et_aimJDID.setText(jdid);
                        LoRa_TYPTtoJDActivity_et_endSingal.setText(endSingal);
                        returnMsg = "修改节点id成功";

                    } else if (getMsg.length() >= 54 && getMsg.contains("68001b8083") &&
                            getMsg.substring(getMsg.indexOf("68001b8083") + 52, getMsg.indexOf("68001b8083") + 54).equals("16")) {
                        Log.d("limbo", "修改节点网络频率返回:" + getMsg);
                        String crc16back = getMsg.substring(48, 52);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 48));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, "CRC16校验出错。", "提示");
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
                        String netFreq = getMsg.substring(42, 48);//目标节点频率
                        LoRa_TYPTtoJDActivity_et_electricVoltage.setText(electricVoltage);
                        LoRa_TYPTtoJDActivity_et_LYSignal1.setText(LYXH1);
                        LoRa_TYPTtoJDActivity_et_LYSignal2.setText(LYXH2);
                        LoRa_TYPTtoJDActivity_et_LYSignal3.setText(LYXH3);
                        LoRa_TYPTtoJDActivity_et_aimNetFreq.setText(netFreq);
                        LoRa_TYPTtoJDActivity_et_endSingal.setText(endSingal);
                        returnMsg = "修改节点网络频率成功";
                    } else if (getMsg.length() >= 92 && getMsg.contains("68002f8089") &&
                            getMsg.substring(getMsg.indexOf("68002f8089") + 90, getMsg.indexOf("68002f8089") + 92).equals("16")) {
                        Log.d("limbo", "一键读取返回:" + getMsg);
                        String crc16back = getMsg.substring(86, 90);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 86));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        String JDid = getMsg.substring(10, 24);//节点id
                        String Subtype = getMsg.substring(24, 26);//设备子类型
                        String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                        String electricVoltage = getMsg.substring(28, 30);//电池电压
                        electricVoltage = volt(electricVoltage);
                        String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                        String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                        String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                        String netID = getMsg.substring(42, 46);//目标节点网络ID
                        String netFreq = getMsg.substring(46, 52);//目标节点网络频率
                        String floor = getMsg.substring(52, 54);//所在层数
                        String region = getMsg.substring(54, 56);//所在区域
                        String totalNum = Integer.parseInt(getMsg.substring(56, 60), 16) + "";//全局编号
                        String partNum = Integer.parseInt(getMsg.substring(60, 64), 16) + "";//局部编号
                        String softwareVersion = getMsg.substring(64, 72);//软件版本
                        String hardwareVersion = getMsg.substring(72, 80);//硬件版本
                        String otherVersion = getMsg.substring(80, 88);//其他版本


                        LoRa_TYPTtoJDActivity_et_electricVoltage.setText(electricVoltage);
                        LoRa_TYPTtoJDActivity_et_LYSignal1.setText(LYXH1);
                        LoRa_TYPTtoJDActivity_et_LYSignal2.setText(LYXH2);
                        LoRa_TYPTtoJDActivity_et_LYSignal3.setText(LYXH3);
                        LoRa_TYPTtoJDActivity_et_endSingal.setText(endSingal);
                        LoRa_TYPTtoJDActivity_et_electricVoltage.setText(electricVoltage);
                        LoRa_TYPTtoJDActivity_et_floor.setText(floor);
                        LoRa_TYPTtoJDActivity_et_region.setText(region);
                        LoRa_TYPTtoJDActivity_et_totalNum.setText(totalNum);
                        LoRa_TYPTtoJDActivity_et_partNum.setText(partNum);
                        LoRa_TYPTtoJDActivity_et_softwareVersion.setText(softwareVersion);
                        LoRa_TYPTtoJDActivity_et_hardwareVersion.setText(hardwareVersion);
                        LoRa_TYPTtoJDActivity_et_otherVersion.setText(otherVersion);
                        LoRa_TYPTtoJDActivity_et_deviceStyle.setText(Subtype);

                        returnMsg = "一键读取成功";
                    } else if (getMsg.length() >= 72 && getMsg.contains("6800248087") &&
                            getMsg.substring(getMsg.indexOf("6800248087") + 70, getMsg.indexOf("6800248087") + 72).equals("16")) {
                        Log.d("limbo", "一键修改返回:" + getMsg);
                        String crc16back = getMsg.substring(66, 70);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 66));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        String JDid = getMsg.substring(10, 24);//旧节点id
                        String Subtype = getMsg.substring(24, 26);//设备子类型
                        String endSingal = Integer.parseInt(getMsg.substring(26, 28), 16) + "";//末端强度
                        String electricVoltage = getMsg.substring(28, 30);//电池电压
                        electricVoltage = volt(electricVoltage);
                        String LYXH1 = Integer.parseInt(getMsg.substring(36, 38), 16) + "";//路由信号1
                        String LYXH2 = Integer.parseInt(getMsg.substring(38, 40), 16) + "";//路由信号2
                        String LYXH3 = Integer.parseInt(getMsg.substring(40, 42), 16) + "";//路由信号3
                        String jdID = getMsg.substring(42, 56);//目标节点ID
                        String netid = getMsg.substring(56, 60);//目标节点网络id
                        String netFreq = getMsg.substring(60, 66);//目标节点网络频率
                        LoRa_TYPTtoJDActivity_et_electricVoltage.setText(electricVoltage);
                        LoRa_TYPTtoJDActivity_et_LYSignal1.setText(LYXH1);
                        LoRa_TYPTtoJDActivity_et_LYSignal2.setText(LYXH2);
                        LoRa_TYPTtoJDActivity_et_LYSignal3.setText(LYXH3);
                        LoRa_TYPTtoJDActivity_et_endSingal.setText(endSingal);
                        LoRa_TYPTtoJDActivity_et_electricVoltage.setText(electricVoltage);
                        LoRa_TYPTtoJDActivity_et_deviceStyle.setText(Subtype);
                        LoRa_TYPTtoJDActivity_et_aimJDID.setText(jdID);
                        LoRa_TYPTtoJDActivity_et_aimNetID.setText(netid);
                        LoRa_TYPTtoJDActivity_et_aimNetFreq.setText(netFreq);
                        returnMsg = "一键修改成功";
                    } else if (getMsg.length() >= 60 && getMsg.contains("68001e8aff") &&
                            getMsg.substring(getMsg.indexOf("68001e8aff") + 58, 60).equals("16")) {
                        Log.d("limbo", "接收到:" + getMsg + "\n");
                        String crc16back = getMsg.substring(54, 58);
                        String crc16result = HzyUtils.CRC16(getMsg.substring(0, 54));
                        if (!crc16result.equals(crc16back)) {
                            Log.d("limbo", "CRC ERROR -- " + crc16back + ":" + crc16result);
                            HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, "CRC16校验出错。", "提示");
                            break;
                        }
                        Log.d("limbo", "复位返回");
                        returnMsg = "复位成功";
                    }
                    //                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, returnMsg, "提示");
                    if (returnMsg.length() != 0) {
                        Toast.makeText(LoRa_TYPTtoJDActivity.this, returnMsg, Toast.LENGTH_LONG).show();
                    }

                    timeOut = true;
                    HzyUtils.closeProgressDialog();

                    break;

                case 0x99:
                    HintDialog.ShowHintDialog(LoRa_TYPTtoJDActivity.this, "无数据返回，请重试。", "提示");
                    break;
                default:
                    break;
            }
        }
    };

    private  String volt(String volt){
        String electricVoltage =  ((float)255 / Float.parseFloat(Integer.parseInt(volt, 16)+"") * 1.224)+"";//电池电压
        electricVoltage = electricVoltage.substring(0,electricVoltage.indexOf(".")+3) + "V";

        return electricVoltage;
    }
    /**
     * 开始协议设定时间
     * timeMax 1 = 0.1s
     */
    private void prepareTimeStart(int timeMax) {
        timeOut = false;
        if (!HzyUtils.isEmpty(LoRa_TYPTtoJDActivity_et_aimLYLJ1.getText().toString())) {
            timeMax = timeMax + 15;
        }
        if (!HzyUtils.isEmpty(LoRa_TYPTtoJDActivity_et_aimLYLJ2.getText().toString())) {
            timeMax = timeMax + 15;
        }
        if (!HzyUtils.isEmpty(LoRa_TYPTtoJDActivity_et_aimLYLJ3.getText().toString())) {
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

    @Override
    protected void onDestroy() {
        MenuActivity.btAuto = false;
        super.onDestroy();
    }
}
