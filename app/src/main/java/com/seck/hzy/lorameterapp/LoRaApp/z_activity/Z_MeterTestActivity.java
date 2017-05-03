package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.os.Handler;
/**
 * 表参数栏
 *
 * @author Guhong.
 *         <p/>
 *         2013.01.11，在表参数下添加摄像表参数及表参数.
 */
public class Z_MeterTestActivity extends ListActivity {
    private ProgressDialog progressBar = null;
    private Handler progressBarHandler = new Handler();

    private void createView() {
        final String[] menuItems = new String[]{
                "1.通用表参数",
                "2.摄像表参数",
                "3.摄像头位置",
                "4.MCU升级",
                "5.DSP升级"};

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.z_activity_listview, menuItems);
        setListAdapter(adapter);

        final Z_MeterTestActivity thisView = this;

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = null;

                switch (position) {
                    case 0:
                        intent = new Intent(thisView, SBControlActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        if (checkPsw() == 1) {
                            intent = new Intent(thisView, SXBParamSetting.class);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        intent = new Intent(thisView, SXBParamLazySetting.class);
                        startActivity(intent);
                        break;
                    case 3: // MCU 升级
                        if (checkPsw() == 1)
                            onMcuUpdate();
                        break;
                    case 4: // DSP 升级
                        if (checkPsw() == 1)
                            onDspUpdate();
                        break;
                }
            }
        });
    }

    private int checkPsw() {
        Z_PasswordDialog pswdlg = new Z_PasswordDialog(this, 0);
        int rtn = pswdlg.showDialog();
        if (rtn != 1) {
            HintDialog.ShowHintDialog(this, "密码输入错误", "错误");
        }
        return rtn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
    }

    // Select DSP file;
    public void selectDspFile() {
        //HintDialog.ShowHintDialog(this, "请确认表端指示灯已关闭！", "提示");
        Z_ModuleHintDialog dlg = new Z_ModuleHintDialog(this, "请确认表端指示灯已关闭", "提示");
        dlg.showDialog();

        Intent intent = new Intent(getBaseContext(), Z_FileDialog.class);
        intent.putExtra(Z_FileDialog.START_PATH, "/sdcard");

        // can user select directories or not
        intent.putExtra(Z_FileDialog.CAN_SELECT_DIR, false);

        // alternatively you can set file filter
        intent.putExtra(Z_FileDialog.FORMAT_FILTER, new String[]{"bin"});
        startActivityForResult(intent, 1);
    }

    // Select DSP file;
    public void selectMcuFile() {
        //HintDialog.ShowHintDialog(this, "请先给表断电！（不要关闭蓝牙转换器！）", "提示");
        Z_ModuleHintDialog dlg = new Z_ModuleHintDialog(this, "请先给表断电！（不要关闭蓝牙转换器）", "提示");
        dlg.showDialog();
        Intent intent = new Intent(getBaseContext(), Z_FileDialog.class);
        intent.putExtra(Z_FileDialog.START_PATH, "/sdcard");

        // can user select directories or not
        intent.putExtra(Z_FileDialog.CAN_SELECT_DIR, false);

        // alternatively you can set file filter
        intent.putExtra(Z_FileDialog.FORMAT_FILTER, new String[]{"hex"});
        startActivityForResult(intent, 2);
    }

    /**
     * DSP 软件升级
     */
    private void onDspUpdate() {
        // Select DSp file
       /* Z_PasswordDialog pswdlg = new Z_PasswordDialog(this, 0);
        if (pswdlg.showDialog() != 1) {
            HintDialog.ShowHintDialog(this, "密码输入错误", "错误");
            return;
        }*/
        selectDspFile();
    }

    private void onMcuUpdate() {
        // Select DSp file
       /* PasswordDialog pswdlg = new PasswordDialog(this, 0);
        if (pswdlg.showDialog() != 1) {
            HintDialog.ShowHintDialog(this, "密码输入错误", "错误");
            return;
        }*/
        selectMcuFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) { // DSP Update
            if (resultCode == Activity.RESULT_OK) {
                final int WDSP_BYTES = 128;

                // upload dsp code
                String filePath = data.getStringExtra(Z_FileDialog.RESULT_PATH);
                File pf = new File(filePath);
                long F_Size = pf.length();
                final long times = F_Size / WDSP_BYTES;
                //final long left = F_Size - times * WDSP_BYTES;	//2014.04.01 zhqh
                final long tmp = F_Size - times * WDSP_BYTES;        //2014.04.01 zhqh
                final int left = (int) tmp;                            //2014.04.01 zhqh

                Log.v("DSP Update", "F_Size = " + F_Size);

                progressBar = new ProgressDialog(this);
                progressBar.setCancelable(false);
                progressBar.setMessage("DSP烧写进度");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax((int) times);
                progressBar.show();

                InputStream in = null;
                try {
                    in = new FileInputStream(filePath);
                } catch (FileNotFoundException e) {
                    HintDialog.ShowHintDialog(this, "文件读取失败","系统错误");
                    return;
                }

                byte[] tempbytes = new byte[8];
                try {
                    in.read(tempbytes);
                } catch (IOException e) {
                    HintDialog.ShowHintDialog(this, "文件读取失败","系统错误`");
                    return;
                }

                long ProgAddr_offset = tempbytes[3] & 0xFF;
                ProgAddr_offset = ProgAddr_offset << 15;

                try {
                    in.close();
                } catch (IOException e1) {
                    HintDialog.ShowHintDialog(this,"文件读取失败","系统错误");
                    return;
                }

                final String pfPath = filePath;
                final long fProgAddr_offset = ProgAddr_offset;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream in = null;
                        try {
                            in = new FileInputStream(pfPath);
                        } catch (FileNotFoundException e) {
                            HintDialog.ShowHintDialog(Z_MeterTestActivity.this, "文件读取失败","系统错误");
                            return;
                        }

                        byte[] dspData = new byte[WDSP_BYTES + 6];
                        byte broadAddr[] = new byte[]{(byte) 0xaa, (byte) 0xaa, (byte) 0xaa,
                                (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa};

                        for (long i = 0; i <= times; i++) {
                            long addr = fProgAddr_offset + i * WDSP_BYTES;

                            //if( i == times && left == 0 )		//2014.04.01 zhqh
                            //	continue;

                            final int progresscal = (int) i;
                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(progresscal);
                                }
                            });

                            dspData[0] = (byte) (addr & 0xFF);
                            dspData[1] = (byte) ((addr >> 8) & 0xFF);
                            dspData[2] = (byte) ((addr >> 16) & 0xFF);
                            dspData[3] = (byte) ((addr >> 24) & 0xFF);
                            dspData[4] = (byte) (WDSP_BYTES & 0xFF);
                            dspData[5] = (byte) ((WDSP_BYTES >> 8) & 0xFF);

                            try {
                                //in.read(dspData, 6, WDSP_BYTES);	//2014.04.01 zhqh
                                if (i < times)                        //2014.04.01 zhqh
                                {
                                    in.read(dspData, 6, WDSP_BYTES);
                                } else if (i == times && left != 0) {
                                    in.read(dspData, 6, left);
                                } else {
                                    continue;
                                }
                            } catch (IOException e1) {
                                try {
                                    in.close();
                                } catch (IOException e) {
                                }
                                HintDialog.ShowHintDialog(Z_MeterTestActivity.this, "文件读取失败","系统错误");
                                return;
                            }

                            try {
                                MenuActivity.netThread.SCMD_WFlash(broadAddr, dspData, 0, WDSP_BYTES + 6);
                            } catch (IOException e) {
                                try {
                                    in.close();
                                } catch (IOException e1) {
                                }

                                progressBarHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        MenuActivity.uiAct.resetNetwork(Z_MeterTestActivity.this);
                                    }
                                });
                                return;
                            }

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        progressBarHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.cancel();
                                HintDialog.ShowHintDialog(MeterTestActivity.this, getResources().getString(R.string.dsp_load_success),
                                        getResources().getString(R.string.tips));
                            }
                        });

                        try {
                            in.close();
                        } catch (IOException e) {
                            HintDialog.ShowHintDialog(MeterTestActivity.this, getResources().getString(R.string.read_file_fail),
                                    getResources().getString(R.string.system_error));
                            return;
                        }

                    }
                }).start();
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        } else if (requestCode == 2) { // MCU Update
            if (resultCode == Activity.RESULT_OK) {
                ModuleHintDialog dlg = new ModuleHintDialog(this, getResources().getString(R.string.press_to_confirm),
                        getResources().getString(R.string.tips));
                dlg.showDialog();
                String filePath = data.getStringExtra(FileDialog.RESULT_PATH);

                // upload mcu code
                final int WMCU_BYTES = 128;
                File pf = new File(filePath);
                long F_Size = pf.length();
                final long times = F_Size / WMCU_BYTES;
                //final long left = F_Size - times * WDSP_BYTES;	//2014.04.01 zhqh
                final long tmp = F_Size - times * WMCU_BYTES;        //2014.04.01 zhqh
                final int left = (int) tmp;                            //2014.04.01 zhqh
                Log.v("MCU Update", "F_Size = " + F_Size);

                progressBar = new ProgressDialog(this);
                progressBar.setCancelable(false);
                progressBar.setMessage(getResources().getString(R.string.wait_dsp_load));
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(4);
                progressBar.show();

                final String pfPath = filePath;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream in = null;
                        try {
                            in = new FileInputStream(pfPath);
                        } catch (FileNotFoundException e) {
                            HintDialog.ShowHintDialog(MeterTestActivity.this, getResources().getString(R.string.read_file_fail),
                                    getResources().getString(R.string.system_error));
                            return;
                        }

                        try {
                            MeterReader.netThread.WhileSend(6000, (byte) '$');
                        } catch (IOException e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace();
                        }
                        progressBarHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(1);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        progressBarHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(2);
                            }
                        });

                        try {
                            MeterReader.netThread.WhileSend(200, (byte) '@');
                        } catch (IOException e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace();
                        }
                        progressBarHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(3);
                            }
                        });
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                        }
                        progressBarHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(4);
                            }
                        });

                        progressBarHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setMessage(getResources().getString(R.string.mcu_load));
                                progressBar.setProgress(0);
                                progressBar.setMax((int) times);
                            }
                        });

                        byte[] mcuData = new byte[WMCU_BYTES];

                        for (long i = 0; i <= times; i++) {
                            final int progresscal = (int) i;
                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(progresscal);
                                }
                            });

                            try {
                                if (i < times) {
                                    in.read(mcuData, 0, WMCU_BYTES);
                                } else if (i == times && left != 0) {
                                    in.read(mcuData, 0, left);
                                } else {
                                    continue;
                                }
                            } catch (IOException e1) {
                                try {
                                    in.close();
                                } catch (IOException e) {
                                }
                                HintDialog.ShowHintDialog(MeterTestActivity.this, getResources().getString(R.string.read_file_fail),
                                        getResources().getString(R.string.system_error));
                                return;
                            }

                            try {
                                MeterReader.netThread.write(mcuData);
                            } catch (IOException e) {
                                try {
                                    in.close();
                                } catch (IOException e1) {
                                }

                                progressBarHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        MeterReader.uiAct.resetNetworkAndBackToMainFace(MeterTestActivity.this);
                                    }
                                });
                                return;
                            }

                            try {
                                Thread.sleep(750);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        progressBarHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.cancel();
                                HintDialog.ShowHintDialog(MeterTestActivity.this, getResources().getString(R.string.input_mcu),
                                        getResources().getString(R.string.tips));
                            }
                        });

                        try {
                            in.close();
                        } catch (IOException e) {
                            HintDialog.ShowHintDialog(MeterTestActivity.this, getResources().getString(R.string.read_file_fail),
                                    getResources().getString(R.string.system_error));
                            return;
                        }

                    }
                }).start();


                // upload mcu code
                progressBar = new ProgressDialog(this);
                progressBar.setCancelable(false);
                progressBar.setMessage("请等待MCU烧写完成");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();

                final String pfPath = filePath;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MeterReader.netThread.ComQF(pfPath);

                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.cancel();
                                    HintDialog.ShowHintDialog(MeterTestActivity.this, "MCU写入成功", "提示");
                                }
                            });
                        } catch (FileNotFoundException e) {
                            HintDialog.ShowHintDialog(MeterTestActivity.this, "文件读取失败", "系统错误");
                            return;
                        } catch (IOException e) {
                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    MeterReader.uiAct.resetNetworkAndBackToMainFace(MeterTestActivity.this);
                                }
                            });
                        }
                    }
                }).start();
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
