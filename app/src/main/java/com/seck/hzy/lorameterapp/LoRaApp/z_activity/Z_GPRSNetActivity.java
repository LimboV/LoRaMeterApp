package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;

/**
 * Created by ssHss on 2016/2/29.
 */

public class Z_GPRSNetActivity extends Activity implements View.OnClickListener {

    private EditText etIPAddr,etIPPort,etAPN,etTime,etTimeSpace,etTimeSet;
    private TextView tvStateShow,tvStateGPRS;
    private Button btn_on,btn_off,btn_openNet;
    public static String GET_SMG_BY_BT;//存放数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        setContentView(R.layout.z_activity_gprsnet);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//不弹出输入框
        findViewById(R.id.sendIPAddr).setOnClickListener(Z_GPRSNetActivity.this);//发送IP地址
        findViewById(R.id.sendIPPort).setOnClickListener(Z_GPRSNetActivity.this);//发送IP端口
        findViewById(R.id.sendApn).setOnClickListener(Z_GPRSNetActivity.this);//发送APN地址
        findViewById(R.id.sendTime).setOnClickListener(Z_GPRSNetActivity.this);//发送抄表时间
        findViewById(R.id.sendTimeSpace).setOnClickListener(Z_GPRSNetActivity.this);//发送抄表间隔
        findViewById(R.id.sendTimeSet).setOnClickListener(Z_GPRSNetActivity.this);//发送抄表间隔
        findViewById(R.id.sendTimeSet).setOnClickListener(Z_GPRSNetActivity.this);//发送定时点
        findViewById(R.id.btn_getIPAddr).setOnClickListener(Z_GPRSNetActivity.this);//获取IP地址
        findViewById(R.id.btn_getIPPort).setOnClickListener(Z_GPRSNetActivity.this);//获取IP端口
        findViewById(R.id.btn_getAPN).setOnClickListener(Z_GPRSNetActivity.this);//获取APN
        findViewById(R.id.btn_getTime).setOnClickListener(Z_GPRSNetActivity.this);//获取时间
        findViewById(R.id.btn_getTimeSpace).setOnClickListener(Z_GPRSNetActivity.this);//获取时间间隔
        findViewById(R.id.btn_getTimeSet).setOnClickListener(Z_GPRSNetActivity.this);//获取定时点
        findViewById(R.id.btn_state).setOnClickListener(Z_GPRSNetActivity.this);//获取状态
        findViewById(R.id.btn_error).setOnClickListener(Z_GPRSNetActivity.this);//获取错误
        findViewById(R.id.btn_read_all).setOnClickListener(Z_GPRSNetActivity.this);//一键获取所有信息
        findViewById(R.id.btn_restart).setOnClickListener(Z_GPRSNetActivity.this);//重启设备
        findViewById(R.id.btn_off1).setOnClickListener(Z_GPRSNetActivity.this);//灰度测试关闭
        findViewById(R.id.btn_on1).setOnClickListener(Z_GPRSNetActivity.this);//测试开启
        findViewById(R.id.btn_state1).setOnClickListener(Z_GPRSNetActivity.this);//读取

        btn_on = (Button) findViewById(R.id.btn_on);
        btn_off = (Button) findViewById(R.id.btn_off);
        btn_openNet = (Button) findViewById(R.id.btn_openNet);
        //        btn_off.setVisibility(View.GONE);
        etIPAddr = (EditText) findViewById(R.id.et_IPAddr);//acsii转16进制转byte数组发送
        etIPPort = (EditText) findViewById(R.id.et_IPPort);//转16进制转byte数组发送
        etAPN = (EditText) findViewById(R.id.et_Apn);
        etTime = (EditText) findViewById(R.id.et_Time);//直接作为16进制转byte数组发送
        etTimeSpace = (EditText) findViewById(R.id.et_TimeSpace);
        etTimeSet = (EditText) findViewById(R.id.et_TimeSet);
        tvStateShow = (TextView) findViewById(R.id.textView7);
        tvStateGPRS = (TextView) findViewById(R.id.textView8);
        loadUser();

        btn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GET_SMG_BY_BT = "";
                //                btn_on.setVisibility(View.GONE);
                //                btn_off.setVisibility(View.VISIBLE);
                String sendMsg = "6810AAAAAAAAAAAAAA2306A0F0000E0101";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")) {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "修改成功", "提示");
                } else {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "修改失败", "提示");
                }
            }
        });
        btn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GET_SMG_BY_BT = "";
                //                btn_on.setVisibility(View.GONE);
                //                btn_off.setVisibility(View.VISIBLE);
                String sendMsg = "6810AAAAAAAAAAAAAA2306A0F0000E0101";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")) {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "修改成功", "提示");
                } else {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "修改失败", "提示");
                }
            }
        });

        btn_openNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GET_SMG_BY_BT = "";
                String sendMsg = "fefefe6810AAAAAAAAAAAAAA3204A0F000553916";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")) {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "开猫成功", "提示");
                } else {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "开猫失败", "提示");
                }
            }
        });

        btn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GET_SMG_BY_BT = "";
//                btn_off.setVisibility(View.GONE);
//                btn_on.setVisibility(View.VISIBLE);
                String sendMsg ="6810AAAAAAAAAAAAAA2306A0F0000E0100";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改成功","提示");
                }else{
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改失败","提示");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendIPAddr:
                GET_SMG_BY_BT = "";
                String sendMsg,length,lengthx;
                sendMsg = etIPAddr.getText().toString().trim();
                length = (sendMsg.length())+"";
                lengthx = (sendMsg.length()+5)+"";

                length = Integer.toHexString(Integer.valueOf(length));
                lengthx = Integer.toHexString(Integer.valueOf(lengthx));
                if (length.length()<2){
                    length = "0" + length;
                }
                if (lengthx.length()<2){
                    lengthx = "0" + lengthx;
                }
                sendMsg = "6810AAAAAAAAAAAAAA23" + lengthx + "A0F00018" + length
                        + HzyUtils.convertStringToHex(sendMsg);
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改成功","提示");
                }else{
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改失败","提示");
                }
                break;

            case R.id.sendIPPort:
                sendMsg = etIPPort.getText().toString().trim();
                    if (sendMsg.length() != 4){
                        Toast.makeText(Z_GPRSNetActivity.this,"长度错误", Toast.LENGTH_LONG).show();
                    }else {
                        sendMsg = "6810AAAAAAAAAAAAAA2307A0F0002C02"
                                + Integer.toHexString(Integer.valueOf(sendMsg));
                        sendCmd(sendMsg);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        result = GET_SMG_BY_BT;
                        if (result.contains("68") && result.contains("16")){
                            HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改成功","提示");
                        }else{
                            HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改失败","提示");
                        }
                }

                break;

            case R.id.sendApn:
                GET_SMG_BY_BT = "";
                sendMsg = etAPN.getText().toString().trim();
                length = (sendMsg.length())+"";
                lengthx = (sendMsg.length()+5)+"";

                length = Integer.toHexString(Integer.valueOf(length));
                lengthx = Integer.toHexString(Integer.valueOf(lengthx));
                if (length.length()<2){
                    length = "0" + length;
                }
                if (lengthx.length()<2){
                    lengthx = "0" + lengthx;
                }
                sendMsg = "6810AAAAAAAAAAAAAA23" + lengthx + "A0F00038" + length
                        + HzyUtils.convertStringToHex(sendMsg);
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改成功","提示");
                }else{
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改失败","提示");
                }
                break;

            case  R.id.sendTime:
                GET_SMG_BY_BT = "";
                sendMsg = etTime.getText().toString().trim();
                if (sendMsg.length() > 2 || sendMsg.length() < 1){
                    Toast.makeText(Z_GPRSNetActivity.this,"日期长度错误", Toast.LENGTH_LONG).show();

                }else {
                    if ( Integer.valueOf(sendMsg) >= 29 || Integer.valueOf(sendMsg) <= 0 ){
                        Toast.makeText(Z_GPRSNetActivity.this,"设置的日期超出范围", Toast.LENGTH_LONG).show();
                    }else{
                        if (sendMsg.length() < 2){
                            sendMsg = "0" + sendMsg;
                        }
                        sendMsg = "6810AAAAAAAAAAAAAA2306A0F0000201"
                                +sendMsg;
                        sendCmd(sendMsg);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        result = GET_SMG_BY_BT;
                        if (result.contains("68") && result.contains("16")){
                            HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改成功","提示");
                        }else{
                            HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改失败","提示");
                        }
                    }
                }
                break;

            case R.id.sendTimeSpace:
                GET_SMG_BY_BT = "";
                sendMsg = etTimeSpace.getText().toString().trim();
                if (sendMsg.length() > 2 || sendMsg.length() < 1){
                    Toast.makeText(Z_GPRSNetActivity.this,"日期长度错误", Toast.LENGTH_LONG).show();
                }else{
                    if ( Integer.valueOf(sendMsg) >=11 || Integer.valueOf(sendMsg) < 0 ){
                        Toast.makeText(Z_GPRSNetActivity.this,"设置的日期超出范围", Toast.LENGTH_LONG).show();
                    }else{
                        GET_SMG_BY_BT = "";
                        if (sendMsg.length()<2){
                            sendMsg = "0" + sendMsg;
                        }
                        sendMsg = "6810AAAAAAAAAAAAAA2306A0F0000401"
                                +sendMsg;
                        sendCmd(sendMsg);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        result = GET_SMG_BY_BT;
                        if (result.contains("68") && result.contains("16")){
                            HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改成功","提示");
                        }else{
                            HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改失败","提示");
                        }
                    }
                }
                break;

            case R.id.sendTimeSet:
                GET_SMG_BY_BT = "";
                sendMsg = etTimeSet.getText().toString().trim();
                if (sendMsg.length() > 2 || sendMsg.length() < 1){
                    Toast.makeText(Z_GPRSNetActivity.this,"定时点长度错误", Toast.LENGTH_LONG).show();
                }else{
                    if ( Integer.valueOf(sendMsg) >23 || Integer.valueOf(sendMsg) < 0 ){
                        Toast.makeText(Z_GPRSNetActivity.this,"设置的定时点超出范围", Toast.LENGTH_LONG).show();
                    }else{
                        GET_SMG_BY_BT = "";
                        if (sendMsg.length()<2){
                            sendMsg = "0" + sendMsg;
                        }
                        sendMsg = "6810AAAAAAAAAAAAAA2306A0F0000601"
                                +sendMsg;
                        sendCmd(sendMsg);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        result = GET_SMG_BY_BT;
                        if (result.contains("68") && result.contains("16")){
                            HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改成功","提示");
                        }else{
                            HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,"修改失败","提示");
                        }
                    }
                }
                break;

            case R.id.btn_getIPAddr:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0001814";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){//判断接收到的信息是否完整
                    result = result.toUpperCase();
                    result = result.replaceAll(" ", "");
                    result = result.replaceAll("0X", "");
                    result = result.substring(result.indexOf("68") + 28, result.indexOf("68") + 68);
                    Log.d("limbo",result);
                    result = HzyUtils.toStringHex(result).replaceAll("�","");
                    Log.d("limbo",result);
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this,result,"数据");
                }

                break;

            case R.id.btn_getIPPort:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0002C02";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){//判断接收到的信息是否完整
                    result = result.toUpperCase();
                    result = result.replaceAll(" ", "");
                    result = result.replaceAll("0X", "");
                    result = result.substring(result.indexOf("68") + 28, result.indexOf("68") + 32);
                    Log.d("limbo",result);
                    result = (Integer.valueOf(result,16)+"").replaceAll("�", "");
                    Log.d("limbo", result);
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "数据");
                }
                break;

            case R.id.btn_getAPN:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0003810";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){//判断接收到的信息是否完整
                    result = result.toUpperCase();
                    result = result.replaceAll(" ", "");
                    result = result.replaceAll("0X", "");
                    result = result.substring(result.indexOf("68") + 28, result.indexOf("68") + 60);
                    Log.d("limbo",result);
                    result = HzyUtils.toStringHex(result).replaceAll("�", "");
                    Log.d("limbo",result);
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "数据");
                }
                break;

            case R.id.btn_getTime:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0000201";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){//判断接收到的信息是否完整
                    result = result.toUpperCase();
                    result = result.replaceAll(" ", "");
                    result = result.replaceAll("0X", "");
                    result = result.substring(result.indexOf("68") + 28, result.indexOf("68") + 30);
                    Log.d("limbo",result);
                    result = result.replaceAll("�","");
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "数据");
                }
                break;

            case R.id.btn_getTimeSpace:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0000401";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){//判断接收到的信息是否完整
                    result = result.toUpperCase();
                    result = result.replaceAll(" ", "");
                    result = result.replaceAll("0X", "");
                    result = result.substring(result.indexOf("68") + 28, result.indexOf("68") + 30);
                    Log.d("limbo",result);
                    result = result.replaceAll("�","");
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "数据");
                }
                break;

            case R.id.btn_getTimeSet:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0000601";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){//判断接收到的信息是否完整
                    result = result.toUpperCase();
                    result = result.replaceAll(" ", "");
                    result = result.replaceAll("0X", "");
                    result = result.substring(result.indexOf("68") + 28, result.indexOf("68") + 30);
                    Log.d("limbo",result);
                    result = result.replaceAll("�","");
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "数据");
                }
                break;

            case R.id.btn_state:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0000E01";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){//判断接收到的信息是否完整
                    result = result.toUpperCase();
                    result = result.replaceAll(" ", "");
                    result = result.replaceAll("0X", "");
                    result = result.substring(result.indexOf("68") + 28, result.indexOf("68") + 30);
                    Log.d("limbo",result);
                    result = result.replaceAll("�","");
                    if (result.equals("00")){
                        HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "已关闭");
                        tvStateShow.setText("设备启停状态 -- 已关闭");
                    }else if (result.equals("01")){
                        HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "已开启");
                        tvStateShow.setText("设备启停状态 -- 已开启");
                    }
                }
                break;

            case R.id.btn_state1:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0005F01";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")){//判断接收到的信息是否完整
                    result = result.toUpperCase();
                    result = result.replaceAll(" ", "");
                    result = result.replaceAll("0X", "");
                    result = result.substring(result.indexOf("68") + 28, result.indexOf("68") + 30);
                    Log.d("limbo",result);
                    result = result.replaceAll("�","");
                    if (result.equals("00")){
                        HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "已关闭");
                        tvStateShow.setText("设备启停状态 -- 已开启");
                    }else if (result.equals("01")){
                        HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "已开启");
                        tvStateShow.setText("设备启停状态 -- 已关闭");
                    }
                }
                break;

            case R.id.btn_off1:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA2306A0F0005F0101";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")) {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "修改成功", "提示");
                } else {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "修改失败", "提示");
                }
                break;
            case R.id.btn_on1:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA2306A0F0005F0100";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")) {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "修改成功", "提示");
                } else {
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "修改失败", "提示");
                }
                break;

            case R.id.btn_error:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0000F01";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")) {//判断接收到的信息是否完整
                    result = result.toUpperCase();
                    result = result.replaceAll(" ", "");
                    result = result.replaceAll("0X", "");
                    result = result.substring(result.indexOf("68") + 28, result.indexOf("68") + 30);
                    Log.d("limbo", result);
                    result = result.replaceAll("�", "");
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, result, "数据");
                }
                break;

            case R.id.btn_read_all:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA220581F0000048";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                result = result.toUpperCase();
                result = result.replaceAll(" ", "");
                result = result.replaceAll("0X", "");
                if (result.contains("68") && result.contains("16") && result.length()>180) {//判断接收到的信息是否完整
                    /**
                     * 抄图日
                     */
                    String ctr = result.substring(result.indexOf("68") + 32 , result.indexOf("68") + 34);
                    ctr = ctr.replaceAll("�", "");
                    etTime.setText(ctr);
                    /**
                     * 抄表间隔
                     */
                    String cbjg = result.substring(result.indexOf("68") + 36, result.indexOf("68") + 38);
                    cbjg = cbjg.replaceAll("�", "");
                    etTimeSpace.setText(cbjg);
                    /**
                     * 定时点
                     */
                    String dsd = result.substring(result.indexOf("68") + 40, result.indexOf("68") + 42);
                    dsd = dsd.replaceAll("�", "");
                    etTimeSet.setText(dsd);
                    /**
                     * 水表状态
                     */
                    String sbzt = result.substring(result.indexOf("68") + 56, result.indexOf("68") + 58);
                    sbzt = sbzt.replaceAll("�", "");
                    if (sbzt.equals("00")){
                        tvStateShow.setText("设备启停状态 -- 已关闭");
                    }else if (sbzt.equals("01")){
                        tvStateShow.setText("设备启停状态 -- 已开启");
                    }
                    /**
                     * gprs状态
                     */
                    String gprs = result.substring(result.indexOf("68") + 58, result.indexOf("68") + 60);
                    gprs = gprs.replaceAll("�", "");
                    tvStateGPRS.setText("GPRS状态 -- "+gprs);
                    /**
                     * IP地址
                     */
                    String ipdz = result.substring(result.indexOf("68") + 76, result.indexOf("68") + 116);
                    ipdz = HzyUtils.toStringHex(ipdz).replaceAll("�","");
                    etIPAddr.setText(ipdz);
                    /**
                     * 端口
                     */
                    String dk = result.substring(result.indexOf("68") + 116, result.indexOf("68") + 120);
                    dk = (Integer.valueOf(dk,16)+"").replaceAll("�", "");
                    etIPPort.setText(dk);
                    /**
                     * APN
                     */
                    String apn = result.substring(result.indexOf("68") + 140, result.indexOf("68") + 172);
                    apn = HzyUtils.toStringHex(apn).replaceAll("�","");
                    etAPN.setText(apn);
                    /**
                     * log
                     */
//                    Log.d("limbo", ctr + "\n" + cbjg + "\n" + dsd + "\n"
//                            + sbzt + "\n" + gprs + "\n" + ipdz + "\n" + dk + "\n" + apn);
//                    Log.d("limbo",result);
                }else{
                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "读取失败", "错误");
                }
                break;
            case R.id.btn_restart:
                GET_SMG_BY_BT = "";
                sendMsg = "6810AAAAAAAAAAAAAA3004AA050000";
                sendCmd(sendMsg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GET_SMG_BY_BT;
                if (result.contains("68") && result.contains("16")) {//判断接收到的信息是否完整

                    HintDialog.ShowHintDialog(Z_GPRSNetActivity.this, "重启成功", "数据");
                }
            default:
                break;
        }
    }

    private void sendCmd(String sendMsg){
        byte[] x = HzyUtils.getHexBytes(sendMsg.toUpperCase());
        byte y = HzyUtils.countSum(x, 0, sendMsg.length()/2);
        int vvv = y & 0xFF;
        String hv = Integer.toHexString(vvv);
        if (hv.length()<2){
            hv = "0" + hv;
        }
        byte[] bos = HzyUtils.getHexBytes("FEFEFE" + sendMsg + hv + "16");
        byte[] bos_new = new byte[bos.length];
        int i,n;
        n = 0;
        for (i = 0; i < bos.length; i++) {
            bos_new[n] = bos[i];
            n++;
        }
        Log.d("limbo", HzyUtils.bytes2HexString(bos_new));
        try {
            MenuActivity.netThread.write(bos_new);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用SharePreferences保存用户信息
     */
    private void saveUser(){

        SharedPreferences.Editor editor = getSharedPreferences("user_msg", MODE_PRIVATE).edit();
        editor.putString("IPAddr", etIPAddr.getText().toString());
        editor.putString("IPPort", etIPPort.getText().toString());
        editor.putString("APN", etAPN.getText().toString());
        editor.putString("Time", etTime.getText().toString());
        editor.putString("TimeSpace", etTimeSpace.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    private void loadUser(){
        SharedPreferences pref = getSharedPreferences("user_msg", MODE_PRIVATE);
        etIPAddr.setText(pref.getString("IPAddr", ""));
        etIPPort.setText(pref.getString("IPPort", ""));
        etAPN.setText(pref.getString("APN", ""));
        etTime.setText(pref.getString("Time", ""));
        etTimeSpace.setText(pref.getString("TimeSpace", ""));
    }

    @Override
    protected void onDestroy() {
        saveUser();
        super.onDestroy();
    }

}
