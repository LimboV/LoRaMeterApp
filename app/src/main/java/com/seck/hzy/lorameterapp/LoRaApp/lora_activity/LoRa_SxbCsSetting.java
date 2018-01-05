package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity.uiAct;

/**
 * Created by limbo on 2017/6/21.
 */

public class LoRa_SxbCsSetting extends Activity {

    @BindView(R.id.EditText_Addr)
    EditText EditText_Addr;

    @BindView(R.id.EditText_NetID)
    EditText EditText_NetID;

    @BindView(R.id.EditText_Freq)
    EditText EditText_Freq;

    @BindView(R.id.EditText_X)
    EditText EditText_X;
    @BindView(R.id.ParamValue1)
    EditText ParamValue1;
    @BindView(R.id.ParamValue2)
    EditText ParamValue2;
    @BindView(R.id.ParamValue3)
    EditText ParamValue3;
    @BindView(R.id.ParamValue4)
    EditText ParamValue4;
    @BindView(R.id.ParamValue5)
    EditText ParamValue5;
    @BindView(R.id.ParamValue6)
    EditText ParamValue6;
    @BindView(R.id.ParamValue7)
    EditText ParamValue7;
    @BindView(R.id.ParamValue8)
    EditText ParamValue8;
    @BindView(R.id.ParamValue9)
    EditText ParamValue9;
    @BindView(R.id.ParamValue10)
    EditText ParamValue10;
    @BindView(R.id.ParamValue11)
    EditText ParamValue11;
    @BindView(R.id.ParamValue12)
    EditText ParamValue12;
    @BindView(R.id.ParamValue13)
    EditText ParamValue13;
    @BindView(R.id.ParamValue14)
    EditText ParamValue14;
    @BindView(R.id.ParamValue15)
    EditText ParamValue15;
    @BindView(R.id.ParamValue16)
    EditText ParamValue16;
    @BindView(R.id.ParamValue17)
    EditText ParamValue17;

    @BindView(R.id.ReadParam1)
    Button ReadParam1;
    @BindView(R.id.ReadParam2)
    Button ReadParam2;
    @BindView(R.id.ReadParam3)
    Button ReadParam3;
    @BindView(R.id.ReadParam4)
    Button ReadParam4;
    @BindView(R.id.ReadParam5)
    Button ReadParam5;
    @BindView(R.id.ReadParam6)
    Button ReadParam6;
    @BindView(R.id.ReadParam7)
    Button ReadParam7;
    @BindView(R.id.ReadParam8)
    Button ReadParam8;
    @BindView(R.id.ReadParam9)
    Button ReadParam9;
    @BindView(R.id.ReadParam10)
    Button ReadParam10;
    @BindView(R.id.ReadParam11)
    Button ReadParam11;
    @BindView(R.id.ReadParam12)
    Button ReadParam12;
    @BindView(R.id.ReadParam13)
    Button ReadParam13;
    @BindView(R.id.ReadParam14)
    Button ReadParam14;
    @BindView(R.id.ReadParam15)
    Button ReadParam15;
    @BindView(R.id.ReadParam16)
    Button ReadParam16;
    @BindView(R.id.ReadParam17)
    Button ReadParam17;

    @BindView(R.id.WriteParam1)
    Button WriteParam1;
    @BindView(R.id.WriteParam2)
    Button WriteParam2;
    @BindView(R.id.WriteParam3)
    Button WriteParam3;
    @BindView(R.id.WriteParam4)
    Button WriteParam4;
    @BindView(R.id.WriteParam5)
    Button WriteParam5;
    @BindView(R.id.WriteParam6)
    Button WriteParam6;
    @BindView(R.id.WriteParam7)
    Button WriteParam7;
    @BindView(R.id.WriteParam8)
    Button WriteParam8;
    @BindView(R.id.WriteParam9)
    Button WriteParam9;
    @BindView(R.id.WriteParam10)
    Button WriteParam10;
    @BindView(R.id.WriteParam11)
    Button WriteParam11;
    @BindView(R.id.WriteParam12)
    Button WriteParam12;
    @BindView(R.id.WriteParam13)
    Button WriteParam13;
    @BindView(R.id.WriteParam14)
    Button WriteParam14;
    @BindView(R.id.WriteParam15)
    Button WriteParam15;
    @BindView(R.id.WriteParam16)
    Button WriteParam16;
    @BindView(R.id.WriteParam17)
    Button WriteParam17;

    int location = 0;
    private String addr_Broad;
    private LoRa_SxbCsSetting thisView = null;
    private boolean timeOut1 = false;

    String len;
    private int id[] = {
            R.id.ParamValue1,
            R.id.ParamValue2,
            R.id.ParamValue3,
            R.id.ParamValue4,
            R.id.ParamValue5,
            R.id.ParamValue6,
            R.id.ParamValue7,
            R.id.ParamValue8,
            R.id.ParamValue9,
            R.id.ParamValue10,
            R.id.ParamValue11,
            R.id.ParamValue12,
            R.id.ParamValue13,
            R.id.ParamValue14,
            R.id.ParamValue15,
            R.id.ParamValue16,
            R.id.ParamValue17,
    };
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
        setContentView(R.layout.lorasxbsettheader);
        HintDialog.ShowHintDialog(this, "谨慎设置！如您不确定参数的用途，请勿改动原设置！", "提示");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//默认不弹出输入框

        ButterKnife.bind(this);
        loadUser();

        SharedPreferences settings = uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
        addr_Broad = "aaaaaaaaaa";    // 广播地址


        thisView = this;
        /**
         * 自动
         */
        MenuActivity.btFlag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("limbo", "start");
                while (MenuActivity.btFlag) {

                    try {
                        Thread.sleep(1000);
                        String getMsg = MenuActivity.Cjj_CB_MSG;
                        MenuActivity.Cjj_CB_MSG = "";
                        if (getMsg.length() == 0) {
                        } else {
                            getMsg = getMsg.replaceAll("0x", "").replaceAll(" ", "");
                            Log.d("limbo", "get:" + getMsg);

                            Message message = new Message();
                            message.what = 0x02;
                            message.obj = getMsg;
                            mHandler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        Log.d("limbo", e.toString());
                    }


                }

            }
        }).start();

        ReadParam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(0);
            }
        });
        ReadParam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(1);
            }
        });
        ReadParam3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(2);
            }
        });
        ReadParam4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(3);
            }
        });
        ReadParam5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(4);
            }
        });
        ReadParam6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(5);
            }
        });
        ReadParam7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(6);
            }
        });
        ReadParam8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(7);
            }
        });
        ReadParam9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(8);
            }
        });
        ReadParam10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(9);
            }
        });
        ReadParam11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(10);
            }
        });
        ReadParam12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(11);
            }
        });
        ReadParam13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(12);
            }
        });
        ReadParam14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(13);
            }
        });
        ReadParam15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(14);
            }
        });
        ReadParam16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(15);
            }
        });
        ReadParam17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read(16);
            }
        });
        WriteParam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(0);
            }
        });
        WriteParam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(1);
            }
        });
        WriteParam3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(2);
            }
        });
        WriteParam4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(3);
            }
        });
        WriteParam5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(4);
            }
        });
        WriteParam6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(5);
            }
        });
        WriteParam7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(6);
            }
        });
        WriteParam8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(7);
            }
        });
        WriteParam9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(8);
            }
        });
        WriteParam10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(9);
            }
        });
        WriteParam11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(10);
            }
        });
        WriteParam12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(11);
            }
        });
        WriteParam13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(12);
            }
        });
        WriteParam14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(13);
            }
        });
        WriteParam15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(14);
            }
        });
        WriteParam16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(15);
            }
        });
        WriteParam17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(16);
            }
        });


    }

    void read(int x) {
        location = x;
        String addr = EditText_Addr.getText().toString().trim();
        String freq = EditText_Freq.getText().toString().trim();
        if (freq.length() == 0) {
            freq = "0000";
        } else {
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
        len = paramLen[x] + "";
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
                + dataAddr[x]//起始地址
                + len//读取长度
                ;
        MenuActivity.sendCmd(sendMsg);
    }

    void write(int x) {
        location = x;


        EditText v = (EditText) findViewById(id[x]);
        if (v.getText().toString().trim().length() == 0) {
            HintDialog.ShowHintDialog(LoRa_SxbCsSetting.this, "输入不可为空", "提示");
            return;
        }
        String msg = v.getText().toString().trim();

        String addr = EditText_Addr.getText().toString().trim();
        String freq = EditText_Freq.getText().toString().trim();
        if (freq.length() == 0) {
            freq = "0000";
        } else {
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
        len = paramLen[x] + "";
        while (len.length() < 2) {
            len = "0" + len;
        }


        if (HzyUtils.toHexString(msg).length() > (paramLen[x] * 2)) {
            HintDialog.ShowHintDialog(thisView, "数据过长", "提示");
            return;
        }

        if (x > 3 && x < 13) {
            if (msg.length() == 4) {
                msg = HzyUtils.toHexString(msg);
                while (msg.length() < (paramLen[x] * 2)) {
                    msg = "0" + msg;
                }
                msg = HzyUtils.changeString1(msg);
            }
        } else if (x == 3) {
            msg = HzyUtils.toHexString(msg);
        }

        String sendMsg;
        String leng = HzyUtils.toHexString(paramLen[x] + 5 + "");
        if (leng.length() < 2) {
            leng = "0" + leng;
        }
        if (x == 0 || x == 3) {
            sendMsg = "68"//起始位
                    + freq //频率
                    + netid//网络ID
                    + addr//地址
                    + "23"//控制码
                    + leng//数据长度
                    + "a0f1"//数据标识
                    + "00"//序号
                    + dataAddr[x]//起始地址
                    + len//读取长度
                    + msg;
        } else if (x == 1) {
            sendMsg = "68"//起始位
                    + freq //频率
                    + netid//网络ID
                    + addr//地址
                    + "15"//控制码
                    + leng//数据长度
                    + "a018"//数据标识
                    + "00"//序号
                    + dataAddr[x]//起始地址
                    + len//读取长度
                    + msg;
        } else {
            sendMsg = "68"//起始位
                    + freq //频率
                    + netid//网络ID
                    + addr//地址
                    + "23"//控制码
                    + leng//数据长度
                    + "a0f0"//数据标识
                    + "00"//序号
                    + dataAddr[x]//起始地址
                    + len//读取长度
                    + msg;
        }

        MenuActivity.sendCmd(sendMsg);
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
                case 0x02://自动接收返回数据

                    getMsg = msg.obj.toString();
                    show = HzyUtils.toStringHex1(getMsg).replaceAll("�", "").replaceAll("\n", "");
                    if (show.length() > 30) {
                        String flag = show.substring(0, 2);
                        Log.d("limbo", show);

                        if (flag.contains("00")) {//磁铁
                            String dataMsg = show.substring(6, 42);
                            String addr = dataMsg.substring(4, 14);
                            String netid = dataMsg.substring(0, 4);
                            String freq = dataMsg.substring(18, 22);
                            String version = dataMsg.substring(22, 36);

                            if (!HzyUtils.isEmpty(freq)) {
                                freq = Integer.parseInt(freq, 16) + "";
                            }

                            Toast.makeText(thisView, "接收到返回数据", Toast.LENGTH_LONG).show();

                            EditText_Addr.setText(addr);
                            EditText_NetID.setText(netid);
                            EditText_Freq.setText(freq);
                        } else if (flag.contains("22")) {
                            Toast.makeText(thisView, "接收到返回数据", Toast.LENGTH_LONG).show();
                            String returnMsg;
                            len = paramLen[location] + "";
                            int length = Integer.parseInt(len) * 2;

                            returnMsg = show.substring(6, 6 + length);
                            Log.d("limbo", "返回:" + returnMsg);
                            EditText v = (EditText) findViewById(id[location]);
                            if (location == 0) {
                                EditText_NetID.setText(returnMsg);
                            } else if (location == 1) {
                                EditText_Addr.setText(returnMsg);
                            } else if (location == 3) {
                                returnMsg = Integer.parseInt(returnMsg, 16) + "";
                                EditText_Freq.setText(returnMsg);
                            } else if (location > 3 && location < 13) {
                                returnMsg = HzyUtils.changeString1(returnMsg);
                                returnMsg = Integer.parseInt(returnMsg, 16) + "";
                            } else if (location >= 13) {
                                returnMsg = Integer.parseInt(returnMsg, 16) + "";
                            }
                            v.setText(returnMsg);
                        } else if (flag.contains("23")) {
                            Toast.makeText(thisView, "接收到返回数据", Toast.LENGTH_LONG).show();
                            String returnMsg;
                            len = paramLen[location] + "";
                            int length = Integer.parseInt(len) * 2;

                            returnMsg = show.substring(6, 6 + length);
                            Log.d("limbo", "返回:" + returnMsg);
                            HintDialog.ShowHintDialog(thisView, returnMsg, "返回");
                            EditText v = (EditText) findViewById(id[location]);
                            if (location == 0) {
                                EditText_NetID.setText(returnMsg);
                            } else if (location == 3) {
                                returnMsg = Integer.parseInt(returnMsg, 16) + "";
                                EditText_Freq.setText(returnMsg);
                            } else if (location > 3 && location < 13) {
                                returnMsg = HzyUtils.changeString1(returnMsg);
                                returnMsg = Integer.parseInt(returnMsg, 16) + "";
                            } else if (location >= 13) {
                                returnMsg = Integer.parseInt(returnMsg, 16) + "";
                            }
                            v.setText(returnMsg);
                        } else if (flag.contains("15")) {
                            Toast.makeText(thisView, "接收到返回数据", Toast.LENGTH_LONG).show();
                            String returnMsg;
                            len = paramLen[location] + "";
                            int length = Integer.parseInt(len) * 2;

                            returnMsg = show.substring(6, 6 + length);
                            Log.d("limbo", "返回:" + returnMsg);
                            HintDialog.ShowHintDialog(thisView, returnMsg, "返回");
                            EditText_Addr.setText(returnMsg);
                        }
                        HzyUtils.closeProgressDialog();
                    }


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
        MenuActivity.btFlag = false;
        MenuActivity.SECK_PARAM = EditText_X.getText().toString();
        saveUser();
        super.onDestroy();
    }
}
