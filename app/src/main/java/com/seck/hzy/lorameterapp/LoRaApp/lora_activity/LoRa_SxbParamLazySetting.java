package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HzyUtils;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/7/19.
 */

public class LoRa_SxbParamLazySetting extends Activity implements Runnable {

    @BindView(R.id.EditText_Addr)
    EditText EditText_Addr;

    @BindView(R.id.EditText_Freq)
    EditText EditText_Freq;

    @BindView(R.id.EditText_NetID)
    EditText EditText_NetID;

    @BindView(R.id.ib_08)
    ImageView ib_08;

    @BindView(R.id.ib_07)
    ImageView ib_07;

    @BindView(R.id.ib_06)
    ImageView ib_06;

    @BindView(R.id.ib_05)
    ImageView ib_05;

    @BindView(R.id.ib_04)
    ImageView ib_04;

    @BindView(R.id.ib_03)
    ImageView ib_03;

    @BindView(R.id.ib_02)
    ImageView ib_02;

    @BindView(R.id.ib_01)
    ImageView ib_01;

    @BindView(R.id.shift_08)
    TextView shift_08;

    @BindView(R.id.shift_07)
    TextView shift_07;

    @BindView(R.id.shift_06)
    TextView shift_06;

    @BindView(R.id.shift_05)
    TextView shift_05;

    @BindView(R.id.shift_04)
    TextView shift_04;

    @BindView(R.id.shift_03)
    TextView shift_03;

    @BindView(R.id.shift_02)
    TextView shift_02;

    @BindView(R.id.shift_01)
    TextView shift_01;

    @BindView(R.id.up_btn)
    ImageButton btnUp;

    @BindView(R.id.down_btn)
    ImageButton btnDown;

    @BindView(R.id.left_btn)
    ImageButton btnLeft;

    @BindView(R.id.right_btn)
    ImageButton btnRight;

    @BindView(R.id.read_pic_btn)
    Button read_pic_btn;

    @BindView(R.id.write_param_btn)
    Button write_param_btn;

    @BindView(R.id.ib_center)
    ImageView ivCenter;//中心图片

    @BindView(R.id.x_value)
    TextView tvCoorX;//X坐标值

    @BindView(R.id.y_value)
    TextView tvCoorY;//Y坐标值

    private EditText et_Addr = null;
    private static final double SCALE_VALUE_X = 1;
    private static final double SCALE_VALUE_Y = 1;
    private static final int COOR_MOVE_INTERVAL = 1;
    private List<TextView> mShiftView = new ArrayList<TextView>();
    private boolean binaryPic = true;
    private boolean isCoorValid = false;
    private double coorX;
    private double coorY;
    private String addr_Broad;
    private byte[] baddr;
    private ProgressDialog progressBar = null;

    private int[] mShift = new int[8];
    private int mCenterIndex = -1;
    private boolean isNeedMove = false;
    private boolean timeOut = false;

    @Override
    public void run() {
        while (isNeedMove) {

            picReadingHandler.sendEmptyMessage(-5);
            try {
                Thread.sleep(200l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private enum MoveType {
        LEFT, RIGHT, UP, DOWN
    }

    private MoveType mMoveType;

    private Bitmap mPicCenter;
    private Matrix mMatrix = new Matrix();

    private Thread taskThread = null;

    private boolean hasReadAllPics = false;
    private int[] paramXYAddr = {
            0x8E,    // Y
            0x90,    // X1
            0x92,    // X2
            0x94,    // X3
            0x96,    // X4
            0x98,    // X5
            0x9A,    // X6
            0x9C,    // X7
            0x9E,    // X8
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.lora_activity_sxbcsset);
        ButterKnife.bind(this);
        et_Addr = (EditText) findViewById(R.id.EditText_Addr);
        SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
        addr_Broad = "aaaaaaaaaa"; // 广播地址
        /**
         * 获取所有图片
         */
        read_pic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HzyUtils.showProgressDialog1(LoRa_SxbParamLazySetting.this, "正在读图.......");
                String addr = EditText_Addr.getText().toString().trim();
                String freq = EditText_Freq.getText().toString().trim();
                String netId = EditText_NetID.getText().toString().trim();
                if (addr.length() > 10) {
                    HintDialog.ShowHintDialog(LoRa_SxbParamLazySetting.this, "表地址过长", "提示");
                }
                if (netId.length() > 4) {
                    HintDialog.ShowHintDialog(LoRa_SxbParamLazySetting.this, "网络ID过长", "提示");
                }
                if (HzyUtils.isEmpty(freq)) {
                    freq = "0";
                }
                freq = Integer.toHexString(Integer.parseInt(freq));
                if (freq.length() > 4) {
                    HintDialog.ShowHintDialog(LoRa_SxbParamLazySetting.this, "表频率过大", "提示");
                }
                while (freq.length() < 4) {
                    freq = "0" + freq;
                }
                while (netId.length() < 4) {
                    netId = "0" + netId;
                }
                if (addr.length() == 0) {
                    addr = "aaaaaaaaaa";
                }
                while (addr.length() < 10) {
                    addr = "0" + addr;
                }
                String sendMsg = "68" +
                        freq +//频率
                        netId +//网络ID
                        addr +
                        "31" +//控制码
                        "03" +//数据长度
                        "90f0" +//数据标志
                        "00"  //序号
                        ;
                MenuActivity.sendCmd(sendMsg);
                Log.d("limbo", sendMsg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            prepareTimeStart();
                            //                                Thread.sleep(8000);
                            while (!timeOut) {
                                int msgLength = MenuActivity.Cjj_CB_MSG.length();
                                Thread.sleep(500);

                                if (MenuActivity.Cjj_CB_MSG.length() == msgLength && msgLength >= 1150) {
                                    timeOut = true;
                                }

                            }
                            String getMsg = MenuActivity.Cjj_CB_MSG;
                            if (!MenuActivity.Cjj_CB_MSG.contains("0a")) {
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
                            }
                            HzyUtils.closeProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                            HzyUtils.closeProgressDialog();
                        }

                    }
                }).start();


            }
        });

        btnUp.setOnTouchListener(onTouchListener);
        btnDown.setOnTouchListener(onTouchListener);
        btnLeft.setOnTouchListener(onTouchListener);
        btnRight.setOnTouchListener(onTouchListener);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00://山科LoRa表
                    String getMsg = msg.obj.toString();
                    String msgx;
                    String show = HzyUtils.toStringHex1(getMsg).replaceAll("�", "").replaceAll("\n", "");
                    Log.d("limbo", show);
                    int Hlength = Integer.parseInt(show.substring(2, 4), 16);
                    int Llength = Integer.parseInt(show.substring(4, 6), 16);
                    int CountLength = Integer.parseInt(show.substring(2, 6), 16);
                    Log.d("limbo", CountLength + "");
                    if (CountLength >= 575) {
                        msgx = show.substring(6 + CountLength * 2);
                        show = show.substring(6, 6 + CountLength * 2);

                        String[] showMsg = new String[5];
                        for (int i = 0; i < 5; i++) {
                            showMsg[i] = show.substring(0 + i * 230, 230 * (i + 1));
                            try {
                                Bitmap bp = null;
                                byte[] picdata = HzyUtils.getHexBytes(showMsg[i]);
                                bp = BluetoothConnectThread.binaryPicBytesToBitmap(picdata);
                                if (bp == null) {
                                    Log.e("limbo", i + ":图片为空");
                                } else {
                                    Log.d("limbo", "bitmap success");
                                    int imgboxlist[] = {R.id.ib_01, R.id.ib_02, R.id.ib_03,
                                            R.id.ib_04, R.id.ib_05, R.id.ib_06, R.id.ib_07,
                                            R.id.ib_08};

                                    ImageView iv = (ImageView) findViewById(imgboxlist[i]);

                                    try {
                                        iv.setImageBitmap(bp);
                                        if (i == 3){
                                            ivCenter.setImageBitmap(bp);
                                        }
                                    } catch (Exception e) {
                                        HintDialog.ShowHintDialog(LoRa_SxbParamLazySetting.this, "图像异常", "错误");
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("limbo", e.toString());
                            }
//                            SxbGetDataActivity_tv_showMsg.append("\n" + msgx);
                        }
                    } else {

//                        SxbGetDataActivity_tv_showMsg.append("\n" + "图片数据不完整,请重试.");
                    }

                    HzyUtils.closeProgressDialog();

                    break;
            }
        }
    };
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!isCoorValid) {
                return true;
            }

            // 按下按钮时候，启动线程，线程每200毫秒发一次消息。如果在主线程操作，UI就卡死了，所以要另起一个线程
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (v.getId()) {
                    case R.id.up_btn:
                        mMoveType = MoveType.UP;
                        break;
                    case R.id.down_btn:
                        mMoveType = MoveType.DOWN;
                        break;
                    case R.id.left_btn:
                        mMoveType = MoveType.LEFT;
                        break;
                    case R.id.right_btn:
                        mMoveType = MoveType.RIGHT;
                        break;
                }

                isNeedMove = true;
                new Thread(LoRa_SxbParamLazySetting.this).start();
            }
            // 当松手后，更改标志位，结束加法运算
            else if (event.getAction() == KeyEvent.ACTION_UP) {
                isNeedMove = false;
            }

            return false;
        }

    };
    private Handler picReadingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what >= 0) {
                progressBar.setTitle("读取第" + (msg.what)
                        + "个字符");
                progressBar.setMax(msg.arg2);
                progressBar.setProgress(msg.arg1);
            } else if (msg.what == -2) {
                int imgboxlist[] = {
                        R.id.ib_01,
                        R.id.ib_02,
                        R.id.ib_03,
                        R.id.ib_04,
                        R.id.ib_05,
                        R.id.ib_06,
                        R.id.ib_07,
                        R.id.ib_08
                };

                ImageView iv = (ImageView) findViewById(imgboxlist[msg.arg1]);
                iv.setImageBitmap((Bitmap) msg.obj);

                //Matrix matrix = new Matrix();
                //matrix.reset();
                //matrix.postScale(2.0F, 2.0F);
                //iv.setImageMatrix(matrix);

                if (msg.arg1 == mCenterIndex) {
                    mPicCenter = (Bitmap) msg.obj;
                    ivCenter = (ImageView) findViewById(R.id.ib_center);
                    ivCenter.setImageBitmap(mPicCenter);

                    float scale = LoRa_SxbParamLazySetting.this.getResources().getDisplayMetrics().density;
                    mMatrix.reset();
                    mMatrix.postScale(34.f / 25.f * scale, 48.f / 36.f * scale);
                    ivCenter.setImageMatrix(mMatrix);
                }
            } else if (msg.what == -3) {
                progressBar.hide();
            } else if (msg.what == -1) { // Error
                //progressBar.hide();
                if (progressBar != null)
                    progressBar.dismiss();
                HintDialog.ShowHintDialog(LoRa_SxbParamLazySetting.this, (String) msg.obj, "错误");
            } else if (msg.what == -4) {
                updateCoorText();
            } else if (msg.what == -5) {
                int dx = 0, dy = 0;
                /**
                 * 调整坐标
                 */
                switch (mMoveType) {
                    case UP:
                        dy = -COOR_MOVE_INTERVAL;
                        break;
                    case DOWN:
                        dy = COOR_MOVE_INTERVAL;
                        break;
                    case LEFT:
                        dx = -COOR_MOVE_INTERVAL;
                        break;
                    case RIGHT:
                        dx = COOR_MOVE_INTERVAL;
                }

                mMatrix.postTranslate(dx, dy);
                ivCenter.setImageMatrix(mMatrix);

                coorX -= dx * SCALE_VALUE_X;
                coorY -= dy * SCALE_VALUE_Y;
                updateCoorText();
            }
        }
    };

    private void updateCoorText() {
        tvCoorX.setText("X坐标" + (int) coorX);
        tvCoorY.setText("Y坐标" + (int) coorY);
    }

    /**
     * 开始协议设定时间
     */
    private void prepareTimeStart() {
        timeOut = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    timeOut = true;
                } catch (Exception e) {

                }
            }
        }).start();
    }
}