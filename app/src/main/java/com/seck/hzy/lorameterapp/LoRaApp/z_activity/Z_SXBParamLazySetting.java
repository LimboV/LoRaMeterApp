package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.BluetoothConnectThread;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Z_SXBParamLazySetting extends Activity implements Runnable {

    private static final int COOR_MOVE_INTERVAL = 1;
    private static final double SCALE_VALUE_X = 1;
    private static final double SCALE_VALUE_Y = 1;

    private EditText et_Addr = null;
    private ProgressDialog progressBar = null;

    private List<TextView> mShiftView = new ArrayList<TextView>();
    private TextView tvCoorX;
    private TextView tvCoorY;
    private ImageView ivCenter;

    private String addr_Broad;
    private byte[] baddr;

    private int[] mShift = new int[8];
    private int mCenterIndex = -1;

    private boolean binaryPic = true;
    private boolean isCoorValid = false;
    private double coorX;
    private double coorY;

    private boolean isNeedMove = false;

    private enum MoveType {
        LEFT, RIGHT, UP, DOWN
    }

    private MoveType mMoveType;

    private Bitmap mPicCenter;
    private Matrix mMatrix = new Matrix();

    private Thread taskThread = null;

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

    private boolean hasReadAllPics = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_sxb_lazy_set);

        et_Addr = (EditText) findViewById(R.id.EditText_Addr);

        SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
        addr_Broad = settings.getString("BADDR", MenuActivity.DEFAULT_BADDR); // 广播地址

        int resIds[] = {
                R.id.shift_01,
                R.id.shift_02,
                R.id.shift_03,
                R.id.shift_04,
                R.id.shift_05,
                R.id.shift_06,
                R.id.shift_07,
                R.id.shift_08
        };
        for (int i = 0; i < mShift.length; i++) {
            mShift[i] = settings.getInt("SHIFT" + i, Z_SXBParamShiftSetting.DEFAULT_SHIFT[i]);
            TextView tv = (TextView) findViewById(resIds[i]);
            tv.setText(String.valueOf(mShift[i]));
            mShiftView.add(tv);
        }

        for (int i = 0; i < mShift.length; i++) {
            if (mShift[i] == 0) {
                mCenterIndex = i;
                break;
            }
        }

        tvCoorX = (TextView) findViewById(R.id.x_value);
        tvCoorY = (TextView) findViewById(R.id.y_value);
        ivCenter = (ImageView) findViewById(R.id.ib_center);

        ImageButton btnUp = (ImageButton) findViewById(R.id.up_btn);
        btnUp.setOnTouchListener(onTouchListener);

        ImageButton btnDown = (ImageButton) findViewById(R.id.down_btn);
        btnDown.setOnTouchListener(onTouchListener);

        ImageButton btnLeft = (ImageButton) findViewById(R.id.left_btn);
        btnLeft.setOnTouchListener(onTouchListener);

        ImageButton btnRight = (ImageButton) findViewById(R.id.right_btn);
        btnRight.setOnTouchListener(onTouchListener);

        //ImageView iv = (ImageView) findViewById(R.id.ib_center);
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!isCoorValid) {
                return true;
            }

            // 按下按钮时候，启动线程，线程没200毫秒发一次消息。如果在主线程操作，UI就卡死了，所以要另起一个线程
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
                new Thread(Z_SXBParamLazySetting.this).start();
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

                    float scale = Z_SXBParamLazySetting.this.getResources().getDisplayMetrics().density;
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
                HintDialog.ShowHintDialog(Z_SXBParamLazySetting.this, (String) msg.obj, "错误");
            } else if (msg.what == -4) {
                updateCoorText();
            } else if (msg.what == -5) {
                int dx = 0, dy = 0;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "二值化图像");
        menu.add(0, 1, 1, "灰度图像");
        menu.add(0, 2, 2, "设置相位位移");
        menu.add(0, 3, 3, "水表读数");
        menu.add(0, 4, 4, "自动定位");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                binaryPic = true;
                break;
            case 1:
                binaryPic = false;
                break;
            case 2:
                startActivityForResult(new Intent(this, Z_SXBParamShiftSetting.class), 0);
                break;
            /**
             * 自动定位
             */
            case 4: {
                String addr = et_Addr.getText().toString().trim();
                if (addr.equals("")) {
                    addr = addr_Broad.trim();
                }

                byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
                try {
                    MenuActivity.netThread.scmdAutoPos(baddr);
                } catch (Exception e) {
                }
            }
            break;
            case 3: {
                String addr = et_Addr.getText().toString().trim();
                if (addr.equals("")) {
                    addr = addr_Broad.trim();
                }

                byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
                try {
                    BluetoothConnectThread.SXInfo cflow = MenuActivity.netThread.readSXMeter(baddr);
                    MenuActivity.netThread.scmdCloseDsp(baddr);

                    String cRecog = "";
                    for (int i = 7; i >= 0; i--) {
                        cRecog += cflow.recog_data[i] + " ";

                        while (cRecog.length() < 4 * (8 - i))
                            cRecog += " ";
                    }

                    String cBelief = "";
                    for (int i = 7; i >= 0; i--) {
                        cBelief += cflow.recog_belief[i] + " ";

                        if (i >= 4)
                            while (cBelief.length() < 5 * (8 - i))
                                cBelief += " ";
                        else
                            while (cBelief.length() < 5 * (8 - i) - 1)
                                cBelief += " ";
                    }

                    String hintMsg = "水表读数:" + cflow.cflow +
                            "r单个结果" + cRecog
                            + "r置信度" + cBelief;
                    HintDialog.ShowHintDialog(Z_SXBParamLazySetting.this, hintMsg, "读数结果");
                } catch (NetException e) {
                    try {
                        MenuActivity.netThread.scmdCloseDsp(baddr);
                    } catch (IOException e1) {
                    }
                    HintDialog.ShowHintDialog(Z_SXBParamLazySetting.this, e.sdesc, "错误");
                } catch (IOException e) {
                    finish();
                    MenuActivity.uiAct.resetNetwork(Z_SXBParamLazySetting.this);
                }

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

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

        //picReadingHandler.sendEmptyMessage(-6);
    }

    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (isFinishing()) {
                    return true;
                }
                if (null != taskThread) {
                    taskThread.interrupt();
                    taskThread = null;
                }

                if (null != progressBar && progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
            return false;
        }
    };

    private Bitmap createPesudoBitmap() {
        Bitmap msgobj = Bitmap.createBitmap(25, 36, Config.ARGB_8888);

        for (int j = 0; j < 36; j++)
            for (int i = 0; i < 25; i++) {
                if (i == 0 || j == 0 ||
                        i == 24 || j == 35 || i == 12 || j == 18)
                    msgobj.setPixel(i, j, 0xffffffff);
                else
                    msgobj.setPixel(i, j, 0x99999999);
            }

        return msgobj;
    }

    public void showAllPic(View v) {
        String addr = et_Addr.getText().toString().trim();
        if (addr.equals("")) {
            addr = addr_Broad.trim();
        }
        if (MenuActivity.netThread != null)
            baddr = MenuActivity.netThread.getSBAddr(addr);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("数据读取进度");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(5);
        progressBar.show();
        progressBar.setOnKeyListener(onKeyListener);

        taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (binaryPic) {
                    try {
                        if (MenuActivity.netThread != null)
                            MenuActivity.netThread.readBinaryPic_Parse1(baddr);
                    } catch (NetException e) {
                        // TODO Auto-generated catch block
                        try {
                            MenuActivity.netThread.scmdCloseDsp(baddr);
                        } catch (IOException e1) {
                        }

                        e.printStackTrace();
                        picReadingHandler.obtainMessage(-1, e.sdesc).sendToTarget();
                        return;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                        picReadingHandler.obtainMessage(-1, e.toString()).sendToTarget();
                        return;
                    }
                }

                for (int i = 0; i < 5; i++) {
                    if (taskThread == null)
                        return;
                    boolean picloaded = loadPic(i + 1);
                    if (!picloaded)
                        return;
                }
                if (mCenterIndex > -1 && MenuActivity.netThread != null) {
                    boolean xywrited = readCoorXY(mCenterIndex + 1);
                    if (!xywrited)
                        return;
                    isCoorValid = true;
                }

                try {
                    if (MenuActivity.netThread != null)
                        MenuActivity.netThread.scmdCloseDsp(baddr);
                } catch (IOException e) {
                }
                hasReadAllPics = true;
                picReadingHandler.obtainMessage(-3).sendToTarget();
            }
        });
        taskThread.start();
    }

    public void writeCoorXY(View v) {
        if (!hasReadAllPics) {
            HintDialog.ShowHintDialog(this, "请先首次读取图片数据", "提示");
            return;
        }

        String addr = et_Addr.getText().toString().trim();
        if (addr.equals("")) {
            addr = addr_Broad.trim();
        }
        baddr = MenuActivity.netThread.getSBAddr(addr);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("写入数据");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setOnKeyListener(onKeyListener);
        progressBar.show();

        taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int value = (int) coorX;
                for (int i = mCenterIndex + 1; i < 5; i++) {
                    value -= mShift[i];
                    if (taskThread == null)
                        return;
                    if (!writeCoorX(i + 1, value))
                        return;
                }
                value = (int) coorX;
                for (int i = mCenterIndex; i >= 0; i--) {
                    value += mShift[i];
                    if (taskThread == null)
                        return;
                    if (!writeCoorX(i + 1, value))
                        return;
                }
                if (!writeCoorY((int) coorY))
                    return;

                //picReadingHandler.obtainMessage(-1, "写入坐标成功").sendToTarget();
                if (!readCoorXY(mCenterIndex + 1))
                    return;

                if (binaryPic) {
                    /*try {
						MeterReader.netThread.readBinaryPic_Parse1(baddr);
					} catch (NetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
                } else {
                    if (!loadPic(mCenterIndex + 1))
                        return;
                }

                picReadingHandler.obtainMessage(-3).sendToTarget();
            }
        });
        taskThread.start();
    }

    private boolean readCoorXY(int picNo) {
        try {
            coorX = MenuActivity.netThread.readParam(baddr, paramXYAddr[picNo], 2);
            coorY = MenuActivity.netThread.readParam(baddr, paramXYAddr[0], 2);
            picReadingHandler.obtainMessage(-4).sendToTarget();
            return true;
        } catch (NetException e) {
            picReadingHandler.obtainMessage(-1, e.sdesc).sendToTarget();
        } catch (IOException e) {
            picReadingHandler.obtainMessage(-1, e.toString()).sendToTarget();
        }
        return false;
    }

    private boolean writeCoorY(int value) {
        int paramPos = paramXYAddr[0];
        return writeCoorParam(paramPos, value);
    }

    private boolean writeCoorX(int picNo, int value) {
        int paramPos = paramXYAddr[picNo];

        value = Math.min(value, 660);
        value = Math.max(value, 0);
        return writeCoorParam(paramPos, value);
    }

    private boolean writeCoorParam(int pos, int value) {
        byte[] params = new byte[2];
        params[0] = (byte) (value & 0xFF);
        params[1] = (byte) ((value >> 8) & 0xFF);

        try {
            boolean writeSuc = MenuActivity.netThread.writeParam(baddr, pos, params);
            return writeSuc;
			/*if ( writeSuc ) {
			}*/
        } catch (NetException e) {
            picReadingHandler.obtainMessage(-1, e.sdesc).sendToTarget();
        } catch (IOException e) {
            picReadingHandler.obtainMessage(-1, e.toString()).sendToTarget();
        }
        return false;
    }

    private boolean loadPic(int picNo) {

        int retrytimes = 2;
        try {
            while (retrytimes > 0) {
                Bitmap bp = null;
                if (binaryPic) {
                    if (MenuActivity.netThread != null)
                        bp = MenuActivity.netThread.readBinaryPic_Parse2(baddr, picNo);
                    else
                        bp = this.createPesudoBitmap();

                    picReadingHandler.obtainMessage(picNo, picNo, 5).sendToTarget();
                } else {
                    bp = MenuActivity.netThread.readPic(baddr, picNo, 3, picReadingHandler);
                }

                if (bp != null) {
                    picReadingHandler.obtainMessage(-2, picNo - 1, 0, bp).sendToTarget();
                    return true;
                }

                retrytimes--;
            }
        } catch (NetException e) {
            try {
                MenuActivity.netThread.scmdCloseDsp(baddr);
            } catch (IOException e1) {
            }

            picReadingHandler.obtainMessage(-1, e.sdesc).sendToTarget();
            return false;
        } catch (IOException e) {
            picReadingHandler.obtainMessage(-1, e.toString()).sendToTarget();
            return false;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
            for (int i = 0; i < mShift.length; i++) {
                mShift[i] = settings.getInt("SHIFT" + i, Z_SXBParamShiftSetting.DEFAULT_SHIFT[i]);
                mShiftView.get(i).setText(String.valueOf(mShift[i]));
            }
            mCenterIndex = data.getIntExtra("center", -1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
