package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity.METER_STYLE;
import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity.uiAct;

/**
 * Created by limbo on 2017/6/21.
 */

public class LoRa_SxbCsSetting extends ListActivity {

    @BindView(R.id.EditText_Addr)
    EditText EditText_Addr;

    @BindView(R.id.EditText_NetID)
    EditText EditText_NetID;

    @BindView(R.id.EditText_Freq)
    EditText EditText_Freq;

    @BindView(R.id.EditText_X)
    EditText EditText_X;

    private String addr_Broad;
    private LoRa_SxbCsSetting thisView = null;


    private String[] paramNames = {
            "网络ID",
            "水表ID",
            "序号",
            "频率",
            "字轮宽度",
            "高度",
            "半高度",
            "Y坐标",
            "X1坐标",
            "X2坐标",
            "X3坐标",
            "X4坐标",
            "X5坐标",
            "字轮个数",
            "置信度阈值",
            "二值化系数",
            "使用代码段"
    };
    private String[] dataAddr = {
            "00",
            "02",
            "07",
            "09",
            "80",
            "82",
            "84",
            "8e",
            "90",
            "92",
            "94",
            "96",
            "98",
            "a0",
            "a1",
            "a2",
            "af"
    };

    private int[] paramLen = {
            2,
            5,
            2,
            2,
            2,
            2,
            2,
            2,
            2,
            2,
            2,
            2,
            2,
            1,
            1,
            1,
            1
    };

    private String[] paramStr = new String[paramNames.length];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    void init() {
        HintDialog.ShowHintDialog(this, "谨慎设置！如您不确定参数的用途，请勿改动原设置！", "提示");

        final ListView listView = getListView();
        View header = LayoutInflater.from(this).inflate(R.layout.lorasxbsettheader, null);
        listView.addHeaderView(header, null, false);
        listView.setDividerHeight(3);
        ButterKnife.bind(this);
        loadUser();

        SharedPreferences settings = uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
//        addr_Broad = settings.getString("BADDR", "aaaaaaaaaa");    // 广播地址
        addr_Broad = "aaaaaaaaaa";    // 广播地址


        listView.setAdapter(new MySetAdapter(this));

        thisView = this;

    }

    private class MySetAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MySetAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking f    or a new one each time.
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return paramNames.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.z_activity_sxbset, null);
                holder = new ViewHolder();

                holder.tv_Name = (TextView) convertView.findViewById(R.id.ParamName);
                holder.et_Param = (EditText) convertView.findViewById(R.id.ParamValue);
                holder.bt_Read = (Button) convertView.findViewById(R.id.ReadParam);
                holder.bt_Write = (Button) convertView.findViewById(R.id.WriteParam);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final int viewPos = position;

            final ViewHolder fholder = holder;

            holder.tv_Name.setText(paramNames[position]);

            holder.bt_Read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    HzyUtils.showProgressDialog1(LoRa_SxbCsSetting.this,"正在读取......");
                    if (METER_STYLE.equals("P")) {//P型表采集机抄表
                    } else if (METER_STYLE.equals("Z")) {//直读表查询


                    } else {//LoRa表对摄像表操作
                        String addr = EditText_Addr.getText().toString().trim();
                        String freq = EditText_Freq.getText().toString().trim();
                        if ( freq.length() == 0){
                            freq ="0000";
                        }else {
                            freq = Integer.toHexString(Integer.parseInt(EditText_Freq.getText().toString().trim()));
                        }

                        String netid = EditText_NetID.getText().toString().trim();
                        while (freq.length() < 4) {
                            freq = "0" + freq;
                        }
                        while (netid.length() < 4) {
                            netid = "0" + netid;
                        }
                        if (addr.equals("")) {
                            addr = addr_Broad.trim();
                        }
                        String len = paramLen[position] + "";
                        while (len.length() < 2) {
                            len = "0" + len;
                        }

                        String sendMsg = "68"//起始位
                                + freq //频率
                                + netid//网络ID
                                + addr//地址
                                + "22"//控制码
                                + "05"//数据长度
                                + "81f0"//数据标识
                                + "00"//序号
                                + dataAddr[position]//起始地址
                                + len//读取长度
                                 ;
                        Log.d("limbo", "发送：" + sendMsg);
                        Log.d("limbo", dataAddr[position] + len);
                        MenuActivity.sendCmd(sendMsg);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(4000);
                                    String getMsg = MenuActivity.Cjj_CB_MSG;
                                    if (getMsg.length() == 0) {
                                        Message message = new Message();
                                        message.what = 0x99;
                                        message.obj = getMsg;
                                        mHandler.sendMessage(message);
                                    } else {
                                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                        Log.d("limbo", "get:" + getMsg);

                                        Message message = new Message();
                                        message.what = 0x01;
                                        message.obj = getMsg;
                                        mHandler.sendMessage(message);
                                    }
                                } catch (Exception e) {
                                    Log.d("limbo", e.toString());
                                }


                            }
                        }).start();
                    }


                }
            });

            holder.bt_Write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (METER_STYLE.equals("P")) {//P型表采集机抄表
                    } else if (METER_STYLE.equals("Z")) {//直读表查询


                    } else {//LoRa表对摄像表操作
                        String addr = EditText_Addr.getText().toString().trim();
                        String freq = Integer.toHexString(Integer.parseInt(EditText_Freq.getText().toString().trim()));
                        String netid = EditText_NetID.getText().toString().trim();
                        while (freq.length() < 4) {
                            freq = "0" + freq;
                        }
                        while (netid.length() < 4) {
                            netid = "0" + netid;
                        }
                        if (addr.equals("")) {
                            addr = addr_Broad.trim();
                        }
                        String data = fholder.et_Param.getText().toString().trim();
                        while (data.length() < paramLen[position] * 2) {
                            data = "0" + data;

                        }
                        String len = paramLen[position] + "";
                        while (len.length() < 2) {
                            len = "0" + len;
                        }

                        String sendMsg = "68"//起始位
                                + freq//频率
                                + netid//网络ID
                                + addr//地址
                                + "23"//控制码
                                + "05"//数据长度
                                + "81f0"//数据标识
                                + "00"//序号
                                + dataAddr[position]//起始地址
                                + len//读取数据长度
                                + data//数据
                                ;
                        Log.d("limbo", "发送：" + sendMsg);
                        MenuActivity.sendCmd(sendMsg);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    String getMsg = MenuActivity.Cjj_CB_MSG;
                                    if (getMsg.length() == 0) {
                                    } else {
                                        getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                                        Log.d("limbo", "get:" + getMsg);

                                        Message message = new Message();
                                        message.what = 0x01;
                                        message.obj = getMsg;
                                        mHandler.sendMessage(message);
                                    }
                                } catch (Exception e) {
                                    Log.d("limbo", e.toString());
                                }


                            }
                        }).start();
                    }


                }
            });

            //fholder.et_Param.setText(paramStr[viewPos]);
            return convertView;
        }

    }


    static class ViewHolder {
        TextView tv_Name;
        EditText et_Param;
        Button bt_Read;
        Button bt_Write;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00://安美通LoRa表
                    HzyUtils.closeProgressDialog();
                    String getMsg = msg.obj.toString();
                    break;
                case 0x01://山科LoRa表
                    HzyUtils.closeProgressDialog();
                    getMsg = msg.obj.toString();
                    String show = HzyUtils.toStringHex1(getMsg).replaceAll("�", "");
                    HintDialog.ShowHintDialog(LoRa_SxbCsSetting.this, show, "收到数据");

                    break;

                case 0x99:
                    HintDialog.ShowHintDialog(LoRa_SxbCsSetting.this, "", "未到数据");
                    HzyUtils.closeProgressDialog();
                    break;
            }


        }
    };
    /**
     * 使用SharePreferences保存用户信息
     */
    private void saveUser() {

        SharedPreferences.Editor editor = getSharedPreferences("user_msg1", MODE_PRIVATE).edit();
        editor.putString("EditText_Addr", EditText_Addr.getText().toString());
        editor.putString("EditText_Freq", EditText_Freq.getText().toString());
        editor.putString("EditText_NetID", EditText_NetID.getText().toString());
        editor.putString("EditText_X", EditText_X.getText().toString());
        editor.commit();
    }

    /**
     * 加载用户信息
     */

    private void loadUser() {
        SharedPreferences pref = getSharedPreferences("user_msg1", MODE_PRIVATE);
        EditText_Addr.setText(pref.getString("EditText_Addr", ""));
        EditText_Freq.setText(pref.getString("EditText_Freq", ""));
        EditText_NetID.setText(pref.getString("EditText_NetID", ""));
        EditText_X.setText(pref.getString("EditText_X", "0"));

    }

    @Override
    protected void onDestroy() {
        saveUser();
        super.onDestroy();
    }
}
