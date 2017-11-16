package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

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

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssHss on 2016/6/15.
 */
public class P_UploadActivity extends Activity {

    private Button btnUpload, btnChooseFile;
    private TextView tvUploadMsg;

    private int allLen = 0;//字节数
    private int bag = 0;//包数
    private List<String> dateList = new ArrayList<String>();//升级数据分包
    private boolean sucFlag = false;//记录握手是否成功
    private boolean sucFlag2 = false;//记录包数信息是否接收
    private boolean sucFlag3 = true;//记录数据包是否成功接收

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.p_activity_upload);
        btnUpload = (Button) findViewById(R.id.UploadActivity_btn_btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendHello();
            }
        });
        btnChooseFile = (Button) findViewById(R.id.UploadActivity_btn_chooseFile);
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile();
            }
        });
        tvUploadMsg = (TextView) findViewById(R.id.tvUploadMsg);
        btnUpload.setVisibility(View.GONE);
    }

    /**
     * 获取字节流
     */
    private void getFile() {
        /**
         * 查找SeckUpdate文件夹，如果没有，创建并提醒
         */
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckUpdate");
        if (!file.exists()) {
            file.mkdir();
            HintDialog.ShowHintDialog(P_UploadActivity.this, "未找到SeckUpdate升级文件夹", "未找到文件");
        } else {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckUpdate/LPG280S.bin");
            if (file.exists()) {
                tvUploadMsg.append("确认升级文件位置\n");
                btnUpload.setVisibility(View.VISIBLE);
            } else {
                tvUploadMsg.append("未找到升级文件，请将文件放入SeckUpdate文件夹中\n");
            }
        }

        /**
         * 解析出升级文件放入msg备用
         */
        String msg;
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckUpdate/LPG280S.bin");
        try {
            InputStream in = new FileInputStream(file);
            allLen = 0;//字节数
            bag = 0;//包数
            allLen = (int) file.length();
            if (allLen % 512 == 0) {
                bag = allLen / 512;
            } else {
                bag = allLen / 512 + 1;
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

                if (msg.length() > 1024) {
                    x = msg.substring(0, 1024);
                    msg = msg.substring(1024);
                } else {
                    x = msg.substring(0);
                    while (x.length() < 1024) {
                        x = x + "0";
                    }
                }
                dateList.add(i, x);//分包完毕
            }
            /*for (int i = 0; i<dateList.size();i++){
                Log.d("limbo", dateList.get(i));
            }*/
        } catch (Exception e) {
            Log.d("limbo", e.toString());
        }

    }

    private void sendHello() {
        HzyUtils.showProgressDialog2(P_UploadActivity.this, bag,"正在进行程序升级......");


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sendMsg;
                    /**
                     * 进入升级指令
                     */
                    sendMsg = "0006000024249300";
//                    tvUploadMsg.append("发送升级指令\n");
                    MenuActivity.sendCmd(sendMsg);
                    Thread.sleep(1000);
                    /**
                     * 发送握手信息
                     */
                    sendMsg = HzyUtils.convertStringToHex("Is Bootloader ?");
                    Log.d("limbo", "发送握手信息");
//                    tvUploadMsg.append("发送握手信息\n");
                    MenuActivity.sendCmd(sendMsg);
                    /**
                     * 1.5秒后判断握手是否成功
                     */
                    Thread.sleep(1500);
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
                        /**
                         * 发送总包数信息
                         */
                        sendMsg = HzyUtils.convertStringToHex("Total Page:" + bag);
                        Log.d("limbo", "总包数信息");
//                        tvUploadMsg.append("发送总包数\n");
                        MenuActivity.sendCmd(sendMsg);
                        Thread.sleep(1500);
                        getMsg = MenuActivity.Cjj_CB_MSG;
                        if (getMsg.length() == 0) {
                            sucFlag2 = false;
                            Message message = new Message();
                            message.what = 0x99;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        } else {
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", getMsg);

                            Message message = new Message();
                            message.what = 0x04;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        }

                        /**
                         * 根据包数结果
                         */
                        Thread.sleep(1000);
                        if (sucFlag2) {
                            /**
                             * 循环发送包
                             */
                            for (int i = 1; i <= bag && sucFlag3; i++) {
                                String nowNum = Integer.toHexString(i);//当前是第nowNum包
                                if (nowNum.length() < 2) {
                                    while (nowNum.length() < 2) {
                                        nowNum = "0" + nowNum;
                                    }
                                }

                                if (i == bag) {
                                    int x = dateList.get(i-1).length() / 2;
                                    String len = Integer.toHexString(x);
                                    while (len.length() < 4) {
                                        len = "0" + len;
                                    }

                                    sendMsg = nowNum + len + dateList.get(i-1) ;
                                } else {
                                    sendMsg = nowNum +"0200" + dateList.get(i-1) ;
                                }
                                sendMsg = sendMsg + HzyUtils.countSum(sendMsg);

                                Message message = new Message();
                                message.what = 0x05;
                                message.obj = "正在发送第" + i + "包数据\n";
                                mHandler.sendMessage(message);
                                HzyUtils.progressDialog.setProgress(i);//更新进度条
                                Log.d("limbo", "第" + i + "包:    " + sendMsg);
                                MenuActivity.sendCmd(sendMsg);
                                /**
                                 * 判断数据包接收情况
                                 */
                                Thread.sleep(2000);
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
                                    message.what = 0x00;
                                    message.obj = getMsg;
                                    message.arg1 = i;
                                    mHandler.sendMessage(message);
                                }
                                Thread.sleep(1000);
                            }
                        } else {
                            //数据包接收失败
                        }

                    } else {
                        //握手失败
                    }

                } catch (Exception e) {
                    Log.d("limbo", e.toString());
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    int error = msg.arg1;
                    String getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    if (getMsg.contains("557064617465")){
                        HzyUtils.closeProgressDialog();
                        tvUploadMsg.append("第" + error + "包数据接收成功\n");
                        tvUploadMsg.append("数据升级完成!");
                    }
                    else if (getMsg.contains("4f4b3a20")) {
                        sucFlag3 = true;
                        tvUploadMsg.append("第" + error + "包数据接收成功\n");
                    } else {//Fail:
                        //接收失败
                        sucFlag3 = false;
                        HzyUtils.closeProgressDialog();
                        Log.d("limbo", "第" + error + "包数据接收失败");
                        tvUploadMsg.append("第" + error + "包数据接收失败\n");
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
                        if (getMsg.equals("5945530d")) {//YES
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
                 * 包数信息
                 */
                case 0x04:
                    getMsg = msg.obj.toString();
                    Log.d("limbo", "读到数据:" + getMsg);
                    if (getMsg.length() == 0) {
                        tvUploadMsg.append("握手失败,未接收到任何数据\n");
                    } else {
                        if (getMsg.equals("5265616479210d")) {//Ready!
                            tvUploadMsg.append("包总数信息接收成功\n");
                            sucFlag2 = true;
                        } else {
                            tvUploadMsg.append("包总数信息接收失败\n");
                            sucFlag2 = false;
                            HzyUtils.closeProgressDialog();
                        }
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
                case 0x97:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(P_UploadActivity.this, "包数信息接收失败", "提示");
                    break;
                case 0x98:
                    error = msg.arg1;
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(P_UploadActivity.this, "第" + error + "包数据没有接收到返回信息", "提示");
                    break;
                case 0x99:
                    HzyUtils.closeProgressDialog();
                    HintDialog.ShowHintDialog(P_UploadActivity.this, "未接收到数据", "提示");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取外置SD卡路径
     *
     * @return 应该就一条记录或空
     */
    public List<String> getExtSDCardPath() {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }

}
