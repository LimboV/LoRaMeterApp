package com.seck.hzy.lorameterapp.LoRaApp.lora_activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.z_activity.Z_SXBParamShiftSetting;
import com.seck.hzy.lorameterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limbo on 2017/7/19.
 */

public class Lora_SxbParamLazySetting extends Activity{

    @BindView(R.id.ib_08) ImageView ib_08;

    @BindView(R.id.ib_07) ImageView ib_07;

    @BindView(R.id.ib_06) ImageView ib_06;

    @BindView(R.id.ib_05) ImageView ib_05;

    @BindView(R.id.ib_04) ImageView ib_04;

    @BindView(R.id.ib_03) ImageView ib_03;

    @BindView(R.id.ib_02) ImageView ib_02;

    @BindView(R.id.ib_01) ImageView ib_01;

    @BindView(R.id.ib_center) ImageView ib_center;

    @BindView(R.id.shift_08) TextView shift_08;

    @BindView(R.id.shift_07) TextView shift_07;

    @BindView(R.id.shift_06) TextView shift_06;

    @BindView(R.id.shift_05) TextView shift_05;

    @BindView(R.id.shift_04) TextView shift_04;

    @BindView(R.id.shift_03) TextView shift_03;

    @BindView(R.id.shift_02) TextView shift_02;

    @BindView(R.id.shift_01) TextView shift_01;

    @BindView(R.id.up_btn)
    ImageButton up_btn;

    @BindView(R.id.down_btn)
    ImageButton down_btn;

    @BindView(R.id.left_btn)
    ImageButton left_btn;

    @BindView(R.id.right_btn)
    ImageButton right_btn;

    @BindView(R.id.read_pic_btn)
    Button read_pic_btn;

    @BindView(R.id.write_param_btn)
    Button write_param_btn;

    @BindView(R.id.x_value)
    TextView x_value;

    @BindView(R.id.y_value)
    TextView y_value;

    private List<TextView> mShiftView = new ArrayList<TextView>();
    private boolean binaryPic = true;
    private boolean isCoorValid = false;
    private double coorX;
    private double coorY;
    private String addr_Broad;
    private byte[] baddr;

    private int[] mShift = new int[8];
    private int mCenterIndex = -1;

    private enum MoveType {
        LEFT, RIGHT, UP, DOWN
    }
    private MoveType mMoveType;

    private Bitmap mPicCenter;
    private Matrix mMatrix = new Matrix();

    private Thread taskThread = null;

    private boolean hasReadAllPics = false;
    private int[] paramXYAddr = {
            0x8E,	// Y
            0x90,	// X1
            0x92,	// X2
            0x94,	// X3
            0x96,	// X4
            0x98,	// X5
            0x9A,	// X6
            0x9C,	// X7
            0x9E,	// X8
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init(){
        setContentView(R.layout.z_activity_sxb_lazy_set);
        ButterKnife.bind(this);

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
        for(int i=0; i< mShift.length; i++) {
            mShift[i] = settings.getInt("SHIFT" + i, Z_SXBParamShiftSetting.DEFAULT_SHIFT[i]);
            TextView tv = (TextView) findViewById(resIds[i]);
            tv.setText(String.valueOf(mShift[i]));
            mShiftView.add(tv);
        }
        for(int i=0; i< mShift.length; i++) {
            if (mShift[i] == 0) {
                mCenterIndex = i;
                break;
            }
        }

    }
}
