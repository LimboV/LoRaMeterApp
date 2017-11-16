package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.R;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssHss on 2016/9/2.
 */
public class LoRa_UploadActivity extends Activity {

    private TextView tvVersion, tvXqNum, tvCjjNum, tvFile, tvUploadMsg;
    private Button btnChooseFile, btnSureUpload;
    private boolean RECEPTION = false;//更新返回
    //    private String msg = "";//文件解析后存放
    private String xqNum, CjjNum;//小区号，采集机编号
    private boolean sucFlag = false;//记录握手是否成功
    private int allLen = 0;//字节数
    private int bag = 0;//包数
    private List<String> dateList = new ArrayList<String>();//升级数据分包


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        //        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.lora_activity_upload);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框

        btnChooseFile = (Button) findViewById(R.id.UploadActivity_btn_chooseFile);
        btnSureUpload = (Button) findViewById(R.id.UploadActivity_btn_sureUpload);

        tvVersion = (TextView) findViewById(R.id.UploadActivity_tv_version);
        tvXqNum = (TextView) findViewById(R.id.UploadActivity_tv_xqNum);
        tvCjjNum = (TextView) findViewById(R.id.UploadActivity_tv_cjjNum);
        tvFile = (TextView) findViewById(R.id.UploadActivity_tv_file);
        tvUploadMsg = (TextView) findViewById(R.id.UploadActivity_tv_uploadMsg);

        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateList.clear();
                String msg;
                /**
                 * 查找SeckUpdate文件夹，如果没有，创建并提醒
                 */
//                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckUpdate");
                File file = new File("/sdcard" + "/SeckUpdate");
                read0();
                if (!file.exists()) {
                    file.mkdir();
                    HintDialog.ShowHintDialog(LoRa_UploadActivity.this, "未找到SeckUpdate升级文件夹", "未找到文件");
                } else {
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckUpdate/update.bin");
                    if (file.exists()) {
                        tvUploadMsg.append("确认升级文件位置\n");
                        btnSureUpload.setVisibility(View.VISIBLE);
                    } else {
                        tvUploadMsg.append("未找到升级文件，请将文件放入SeckUpdate文件夹中\n");
                        //                        HintDialog.ShowHintDialog(UploadActivity.this, "未找到升级文件，请将文件放入SeckUpdate文件夹中", "未找到文件");
                    }
                }
                /**
                 * 解析出升级文件放入msg备用
                 */
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckUpdate/update.bin");
                try {
                    InputStream in = new FileInputStream(file);
                    allLen = 0;//字节数
                    bag = 0;//包数
                    allLen = (int) file.length();
                    if (allLen % 1024 == 0) {
                        bag = allLen / 1024;
                    } else {
                        bag = allLen / 1024 + 1;
                    }
                    byte all[] = new byte[allLen];//全文件字节流
                    in.read(all);//将字节流放入

                    msg = HzyUtils.bytes2HexString(all);
                    /**
                     * 升级数据装包
                     */
                    String x;
                    for (int i = 0; i < bag; i++) {
                        /*if(i<bag-1){
                            dateList.add(msg.substring(512*i,512*i+512));
                        }else {
                            dateList.add(msg.substring(512*i));
                        }*/

                        if (msg.length() > 2048) {
                            x = msg.substring(0, 2048);
                            msg = msg.substring(2048);
                        } else {
                            x = msg.substring(0);
                            while (x.length() < 2048) {
                                x = x + "0";
                            }
                        }
                        dateList.add(i, x);

                    }

                    tvUploadMsg.append("升级文件解析完毕，文件大小为:" + allLen + "字节。总包数:" + bag + "\n");
                    /*for (int i = 0; i < bag; i++) {
                        Log.d("limbo", "第" + (i + 1) + "包数据:" + dateList.get(i));
                        tvUploadMsg.append("第" + (i + 1) + "包数据:" + dateList.get(i)+"\n");
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("limbo", e.toString());
                }
            }
        });

        btnSureUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 发送握手信息
                 */
                HzyUtils.showProgressDialog2(LoRa_UploadActivity.this, bag,"正在进行程序升级......");
                String sendMsg = "00060000a55a7370";
                Log.d("limbo", "握手信息:" + sendMsg);
                tvUploadMsg.append("发送握手信息\n");
                MenuActivity.sendCmd(sendMsg);
                /**
                 * 2秒后判断握手是否成功
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            if (getMsg.length() == 0) {
                                sucFlag = false;
                                Message message = new Message();
                                message.what = 0x99;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            } else {
                                getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                Log.d("limbo", getMsg);

                                Message message = new Message();
                                message.what = 0x03;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            }
                            /**
                             * 根据握手结果
                             */
                            Thread.sleep(1000);
                            if (sucFlag) {
                                String fileLen = Integer.toHexString(Integer.valueOf(allLen));
                                String bagCount = Integer.toHexString(Integer.valueOf(bag));
                                if (fileLen.length() < 8) {
                                    while (fileLen.length() < 8) {
                                        fileLen = "0" + fileLen;
                                    }
                                }
                                if (bagCount.length() < 4) {
                                    while (bagCount.length() < 4) {
                                        bagCount = "0" + bagCount;
                                    }
                                }

                                /**
                                 * 循环发送包
                                 */
                                for (int i = 0; i < bag; i++) {
                                    RECEPTION = false;
                                    String nowNum = Integer.toHexString(i);
                                    if (nowNum.length() < 4) {
                                        while (nowNum.length() < 4) {
                                            nowNum = "0" + nowNum;
                                        }
                                    }
                                    String sendMsg = xqNum + CjjNum + fileLen + bagCount + nowNum + dateList.get(i);
                                    sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
                                    Log.d("limbo", "第" + i + "包:" + sendMsg);
                                    Message message = new Message();
                                    message.what = 0x05;
                                    message.obj = "正在发送第" + i + "包数据\n";
                                    mHandler.sendMessage(message);
                                    HzyUtils.progressDialog.setProgress(i + 1);//更新进度条
                                    MenuActivity.sendCmd(sendMsg);
                                    /**
                                     * 判断数据包接收情况
                                     */
//                                    while (!RECEPTION) {
                                        Thread.sleep(1500);
                                        getMsg = MenuActivity.Cjj_CB_MSG;
                                        if (getMsg.length() == 0) {
                                            sucFlag = false;
                                            message = new Message();
                                            message.what = 0x98;
                                            message.obj = getMsg;
                                            message.arg1 = i;
                                            mHandler.sendMessage(message);
                                        } else {
                                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");

                                            message = new Message();
                                            message.what = 0x04;
                                            message.obj = getMsg;
                                            message.arg1 = i;
                                            mHandler.sendMessage(message);
                                        }
                                            /**
                                             * 接收失败处理
                                            Thread.sleep(2000);
                                            if (RECEPTION){
                                                //包接收成功，继续下一包
                                            }else {
                                                break;
                                                //包接收失败
                                            }*/
                                    }

//                                }

                            } else {
                                Message message = new Message();
                                message.what = 0x03;
                                message.obj = getMsg;
                                mHandler.sendMessage(message);
                            }

                        } catch (InterruptedException e) {
                            HzyUtils.closeProgressDialog();
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        btnSureUpload.setVisibility(View.GONE);

    }

    private void read0() {
        HzyUtils.showProgressDialog(LoRa_UploadActivity.this);
        String sendMsg = "0003000f0002";
        sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
        Log.d("limbo", "获取:" + sendMsg);
        MenuActivity.sendCmd(sendMsg);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    String getMsg = MenuActivity.Cjj_CB_MSG;
                    if (getMsg.length() == 0) {
                        Message message = new Message();
                        message.what = 0x99;
                        message.obj = getMsg;
                        mHandler.sendMessage(message);
                    } else {
                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                        Log.d("limbo", getMsg);

                        Message message = new Message();
                        message.what = 0x00;
                        message.obj = getMsg;
                        mHandler.sendMessage(message);
                        HzyUtils.closeProgressDialog();
                    }
                } catch (InterruptedException e) {
                    HzyUtils.closeProgressDialog();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void read1() {
        HzyUtils.showProgressDialog(LoRa_UploadActivity.this);
        String sendMsg = "000300870001";
        sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
        Log.d("limbo", "获取:" + sendMsg);
        MenuActivity.sendCmd(sendMsg);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    String getMsg = MenuActivity.Cjj_CB_MSG;
                    if (getMsg.length() == 0) {
                        Message message = new Message();
                        message.what = 0x99;
                        message.obj = getMsg;
                        mHandler.sendMessage(message);
                    } else {
                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                        Log.d("limbo", getMsg);

                        Message message = new Message();
                        message.what = 0x01;
                        message.obj = getMsg;
                        mHandler.sendMessage(message);
                        HzyUtils.closeProgressDialog();
                    }
                } catch (InterruptedException e) {
                    HzyUtils.closeProgressDialog();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void read2() {
        HzyUtils.showProgressDialog(LoRa_UploadActivity.this);
        String sendMsg = "000300070001";
        sendMsg = sendMsg + HzyUtils.CRC16(sendMsg);
        Log.d("limbo", "获取:" + sendMsg);
        MenuActivity.sendCmd(sendMsg);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                    String getMsg = MenuActivity.Cjj_CB_MSG;
                    if (getMsg.length() == 0) {
                        Message message = new Message();
                        message.what = 0x99;
                        message.obj = getMsg;
                        mHandler.sendMessage(message);
                    } else {
                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                        Log.d("limbo", getMsg);

                        Message message = new Message();
                        message.what = 0x02;
                        message.obj = getMsg;
                        mHandler.sendMessage(message);
                    }
                    HzyUtils.closeProgressDialog();
                } catch (InterruptedException e) {
                    HzyUtils.closeProgressDialog();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 * 读软件版本号
                 */
                case 0x00:
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_UploadActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 14);
                        tvVersion.setText("软件版本号:" + getMsg);
                        read1();
                    }

                    break;
                /**
                 * 读小区号
                 */
                case 0x01:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_UploadActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 10);
                        xqNum = getMsg;
                        tvXqNum.setText("小区编号:" + getMsg);
                        read2();
                    }

                    break;
                /**
                 * 读采集机编号
                 */
                case 0x02:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    HzyUtils.closeProgressDialog();
                    if (getMsg.length() <= 12) {
                        HintDialog.ShowHintDialog(LoRa_UploadActivity.this, "参数错误", "错误");
                    } else {
                        getMsg = getMsg.substring(6, 10);
                        CjjNum = getMsg;
                        tvCjjNum.setText("采集机编号:" + getMsg);
                    }

                    break;
                /**
                 * 握手判断
                 */
                case 0x03:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    if (getMsg.length() == 0) {
                        tvUploadMsg.append("握手失败,未接收到任何数据\n");
                    } else {
                        if (getMsg.equals("000600892183")) {
                            tvUploadMsg.append("握手成功\n");
                            sucFlag = true;
                        } else {
                            tvUploadMsg.append("握手失败\n");
                            sucFlag = false;
                            HzyUtils.closeProgressDialog();
                        }
                    }

                    break;
                /**
                 * 数据包接收情况
                 */
                case 0x04:
                    int error = msg.arg1;
                    getMsg = msg.obj.toString().toUpperCase();
                    Log.d("limbo", "读到数据:" + getMsg);
                    if (getMsg.contains("00AA")) {
                        //接收完成
                        RECEPTION = true;
                        Log.d("limbo", "第" + error + "包数据接收完成");
                        tvUploadMsg.append("第" + error + "包数据接收完成\n");
                        if (getMsg.length() > 20 && getMsg.contains("00CC")) {
                            //更新完成
                            RECEPTION = true;
                            HzyUtils.closeProgressDialog();
                            Log.d("limbo", "更新完成");
                            tvUploadMsg.append("更新完成\n");
                        }
                    } else {
                        //接收失败
                        RECEPTION = false;
                        HzyUtils.closeProgressDialog();
                        Log.d("limbo", "第" + error + "包数据接收失败");
                        tvUploadMsg.append("第" + error + "包数据接收失败\n");
                    }

                    break;
                /**
                 * 当前第几包
                 */
                case 0x05:
                    getMsg = msg.obj.toString();
                    tvUploadMsg.append(getMsg);
                    break;

                /**
                 * 错误
                 */
                case 0x98:
                    error = msg.arg1;
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_UploadActivity.this, "第" + error + "包数据没有接收到返回信息", "提示");
                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(LoRa_UploadActivity.this, "未接收到数据", "提示");
                    break;
                default:
                    break;
            }
        }
    };


}
