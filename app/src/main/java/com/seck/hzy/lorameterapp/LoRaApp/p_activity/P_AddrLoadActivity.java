package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.model.PdataHelper;
import com.seck.hzy.lorameterapp.LoRaApp.model.PmeterUser;
import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.util.List;

/**
 * Created by limbo on 2016/11/3.
 */
public class P_AddrLoadActivity extends Activity {

    private TextView tvMsg,tvGetMsg;
    private Button btnLoadAddr;
    private List<PmeterUser> meterList;
    private int xqid, cjjid, portid, getMsgNum;//小区号 采集机号 端口号 组合编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        setContentView(R.layout.p_activity_load_addr);
        btnLoadAddr = (Button) findViewById(R.id.AddrLoadActivity_btn_loadAddr);
        tvGetMsg = (TextView) findViewById(R.id.AddrLoadActivity_tv_getMsg);
        tvMsg = (TextView) findViewById(R.id.AddrLoadActivity_tv_msg);


        btnLoadAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog(P_AddrLoadActivity.this);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (getMsgNum()) {

                            for (int i = 0; i < meterList.size(); i++) {
                                loadAddr(i);
                                try {
                                    Thread.sleep(MenuActivity.CMD_DELAY);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            HzyUtils.closeProgressDialog();
                            Looper.prepare();
                            HintDialog.ShowHintDialog(P_AddrLoadActivity.this, "未接收到数据", "提示");
                            Looper.loop();
                        }

                        HzyUtils.closeProgressDialog();
                    }
                }).start();
            }
        });
    }

    public boolean getMsgNum() {
        boolean x = false;
        String Msg = "00030087000275f3";
//        String crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
//        String sendMsg = Msg + crc16Result;//需要发送的指令
        Log.d("limbo", "读:" + Msg);
        MenuActivity.sendCmd(Msg);
        HzyUtils.showProgressDialog(P_AddrLoadActivity.this);
        try {
            Thread.sleep(1000);
            String msg = (MenuActivity.Cjj_CB_MSG.replaceAll("0x", "")).replaceAll(" ", "");
            int length = msg.length();
            if (length == 0) {
                /*HzyUtils.closeProgressDialog();
                Looper.prepare();
                HintDialog.ShowHintDialog(AddrLoadActivity.this, "未接收到数据", "提示");
                Looper.loop();*/
                x =  false;
            } else {
                xqid = Integer.valueOf(msg.substring(8, 10));
                cjjid = Integer.valueOf(msg.substring(12, 14));
                x = true;
                Message message = new Message();
                message.what = 0x00;
                mHandler.sendMessage(message);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return x;
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
               case 0x00:
                    meterList = PdataHelper.getMeters(xqid, cjjid);//从数据库中获取列表
                    tvMsg.setText("小区号:"+xqid +"    通讯机号:"+cjjid);
//                    getList();
//                    listItemAdapter = new SimpleAdapter(AddrLoadActivity.this, listItem, R.layout.list_item, from, to);
//                    listItemAdapter.setViewBinder(new ListViewBinder());
//                    lsMsg.setAdapter(listItemAdapter);
                    break;
                default:
                    break;
            }
        }
    };


    public void loadAddr(int i) {//导入表地址
        String meterNum = Integer.toHexString(i);
        while (meterNum.length() < 2) {
            meterNum = "0" + meterNum;
        }
        String addr = meterList.get(i).MeterNumber;
        while (addr.length() < 14) {
            addr = "0" + addr;
        }
        addr = HzyUtils.changeString(addr);
        String Msg = "0010f" + portid + meterNum + "000408"
                + addr + "00";
        String crc16Result = HzyUtils.CRC16(Msg);//获取CRC16校验码
        String sendMsg = Msg + crc16Result;//需要发送的指令
        Log.d("limbo", sendMsg);
        MenuActivity.sendCmd(sendMsg);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
