package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.ClientMeterData;
import com.seck.hzy.lorameterapp.LoRaApp.model.Meter;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;
import com.seck.hzy.lorameterapp.LoRaApp.utils.SkRestClient;
import com.seck.hzy.lorameterapp.LoRaApp.utils.Utils;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用定制的列表方式显示水表抄表数据，抄表及查询公用。
 *
 * @author l412
 */
public class Z_SBListActivity extends ListActivity {

    final static int wait_time_interval = 200;

    private ProgressDialog progressBar = null;
    private int iXQBH = 0;
    private int iLYH = 0;
    private int iZXDThresh = 85;
    private String sCJJH;
    private Z_SBListActivity thisView = null;
    private Button btnMenu,btnCXQB;

    private static int SBRESULT_CONTENT = 2;

    private List<Meter> meterList;

    private boolean binaryPic = false;
    private Thread taskThread = null;
    String x = "显示异常";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SBRESULT_CONTENT) {
            meterList = MenuActivity.dataSource.getMeters(iXQBH, iLYH, sCJJH);
            adapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisView = this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Create the main view;
        createView();
    }

    private void createView() {
        final ListView listView = getListView();
        View header = LayoutInflater.from(this).inflate(R.layout.z_activity_sbinfoheader,
                null);
        listView.addHeaderView(header, null, false);

        final Bundle bundle = this.getIntent().getExtras();
        iXQBH = bundle.getInt("XQBHA");
        iLYH = bundle.getInt("LYH");
        sCJJH = bundle.getString("CJJH");
        //iCJJH = Integer.parseInt(cjjh);

        SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(
                MenuActivity.PREF_NAME, 0);
        iZXDThresh = settings.getInt("ZXD", 85); // 置信度

        listView.setDividerHeight(3);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setOnKeyListener(onKeyListener);

        // bar = new ProgressBar(this, null,
        // android.R.attr.progressBarStyleHorizontal);
        // bar.setMax(listView.getCount());
        // bar.setProgress(0);
        // bar.setSecondaryProgress(0);
        // listView.addHeaderView(bar);
        // Do another init to set content for header and add event handler.
        Log.d("limbo", iXQBH + "   " + iLYH + "    " + sCJJH);
        meterList = MenuActivity.dataSource.getMeters(iXQBH, iLYH, sCJJH);
        for(Meter meter : meterList){
            Log.d("limbo", meter.ColectID + "\n" + meter.MeterID);
        }

        if (meterList.size() <= 0) {
            HintDialog.ShowHintDialog(this, "无法获得水表集合","错误");
            return;
        }

        setListAdapter(adapter);

        final Z_SBListActivity thisView = this;
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                position -= listView.getHeaderViewsCount();
                Meter meter = meterList.get(position);

                Intent intent = new Intent(thisView, Z_SBInfoActivity.class);
                intent.putExtra("CB", bundle.getBoolean("CB"));
                intent.putExtra("_id", meter.id);
                startActivityForResult(intent, SBRESULT_CONTENT);
            }
        });
        btnCXQB = (Button) findViewById(R.id.btnCXQB);
        btnCXQB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    ListView listView = getListView();
                    progressBar.setMessage("抄表进度");
                    progressBar.setIndeterminate(false);
                    progressBar.setProgress(0);

                    int listC = listView.getCount();
                    int headC = listView.getHeaderViewsCount();
                    progressBar.setMax(Math.max(listC - headC, 1));
                    progressBar.show();

                    final List<Meter> cbMeters = selectCBMeter();

                    taskThread = new Thread(new Runnable() {

                        private int progress_val = 0;

                        @Override
                        public void run() {

                            progress_val = 0;
                            //int minSBNum = 0;//Math.min(4, cbMeters.size());

					/*// 打开前4个水表
					for(int i=0;i<minSBNum;i++)
					{
						if( MeterReader.netThread == null )
							return;

						byte addr[] = MeterReader.netThread.getSBAddr(cbMeters.get(i).Serial);

						if( MeterReader.netThread == null )
							return;

						try {
							MeterReader.netThread.scmdOpenDsp(addr);
						} catch (IOException e) {
							e.printStackTrace();
						}

						try {
							Thread.sleep(wait_time_interval);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
							return;
						}
					}

					// Sleep 6s

					/*try {
						Thread.sleep(6000);
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}//*/

                            List<ClientMeterData> upload = new ArrayList<ClientMeterData>();
                            for (int i = 0; i < cbMeters.size(); i++) {
                                if (taskThread == null)
                                    return;

                                int _id = cbMeters.get(i).id;
                                String sSBBH = cbMeters.get(i).MeterID;
                                String sSBGH = cbMeters.get(i).Serial;

                                try {
                                    Meter meter = MenuActivity.dataSource.updateMeterData(_id);

                                    ClientMeterData data = new ClientMeterData();
                                    data.MeterID = meter.MeterID;
                                    data.Data = meter.Data;
                                    data.DataTime = meter.DataTime;
                                    data.IsFault = meter.IsFault;
                                    data.IsBinaryzation = meter.IsBinaryzation;
                                    data.Images = new ArrayList<int[]>();
                                    if (meter.Images != null) {
                                        for (byte[] b : meter.Images) {
                                            data.Images.add(Utils.toIntArray(b));
                                        }
                                    }
                                    data.RecognitionResult = Utils.toIntArray(meter.RecognitionResult);
                                    data.WATER = meter.WATER;
                                    upload.add(data);
                                } catch (IOException e) { //catch (IOException e)
                                    //final IOException e1 = e;
                                    progressBarHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                            MenuActivity.uiAct.resetNetwork(thisView);
                                        }
                                    });
                                }

                                progress_val++;
                                // Update the progress bar
                                progressBarHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setProgress(progress_val);
                                    }
                                });

                                try {
                                    Thread.sleep(wait_time_interval);
                                } catch (InterruptedException e2) {
                                    // TODO Auto-generated catch block
                                    e2.printStackTrace();
                                    return;
                                }

                                // 关闭当前水表
                                try {
                                    if (MenuActivity.netThread == null)
                                        return;

                                    MenuActivity.netThread.scmdCloseDsp(MenuActivity.netThread.getSBAddr(sSBGH));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // 延时
                                try {
                                    Thread.sleep(wait_time_interval);
                                } catch (InterruptedException e2) {
                                    // TODO Auto-generated catch block
                                    e2.printStackTrace();
                                    return;
                                }

                                // 打开后面一个
						/*if( i < cbMeters.size() - minSBNum )
						{
							if( MeterReader.netThread == null )
								return;
							byte addr[] = MeterReader.netThread.getSBAddr(cbMeters.get(i+4).Serial);
							try {
								MeterReader.netThread.scmdOpenDsp(addr);
							} catch (IOException e) {
								e.printStackTrace();
							}

							// 延时
							try {
								Thread.sleep(wait_time_interval);
							} catch (InterruptedException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
								return;
							}
						}//*/
                            }

                            // 进程结束
                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    meterList = MenuActivity.dataSource.getMeters(iXQBH, iLYH, sCJJH);
                                    adapter.notifyDataSetChanged();
                                    progressBar.dismiss();

                                    // 网络上传部分
                                    SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(
                                            MenuActivity.PREF_NAME, 0);
                                    boolean cbAutoUpload = settings.getBoolean("AUTOUPLOAD", true);
                                    if (cbAutoUpload) {
                                        uploadMeterData();
                                    }
                                }
                            });
                        }
                    });
                    taskThread.start();

                }
            }
        });

        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Z_SBListActivity.this);
                dialog.setTitle("请选择功能");
                dialog.setCancelable(false);
                dialog.setPositiveButton(x, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!showErrors) {
                            showErrors = true;
                            x = "显示所有";

                            meterList = MenuActivity.dataSource.getMeterErrors(iXQBH, iLYH, sCJJH);
                            adapter.notifyDataSetChanged();
                        } else {
                            showErrors = false;
                            x = "显示异常";

                            meterList = MenuActivity.dataSource.getMeters(iXQBH, iLYH, sCJJH);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
                dialog.setNeutralButton("上传数据", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadMeterData();
                    }
                });
                dialog.setNegativeButton("获取二值图像", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binaryPic = true;

                        progressBar.setMessage("数据读取进度");
                        progressBar.setIndeterminate(false);
                        progressBar.setProgress(0);
                        progressBar.setMax(meterList.size());
                        progressBar.show();

                        final List<Meter> cbMeters = selectCBMeter();

                        taskThread = new Thread(new Runnable() {
                            private int progress_val = 0;

                            @Override
                            public void run() {

                                progress_val = 0;

                                //int minSBNum = Math.min(4, cbMeters.size());

					/*// 打开前4个水表
					for(int i=0;i<minSBNum;i++)
					{
						if( MeterReader.netThread == null )
							return;
						byte addr[] = MeterReader.netThread.getSBAddr(cbMeters.get(i).Serial);

						try {
							MeterReader.netThread.scmdOpenDsp(addr);
						} catch (IOException e) {
							e.printStackTrace();
						}

						try {
							Thread.sleep(wait_time_interval);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
							return;
						}
					}

					// Sleep 6s
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}*/

                                List<ClientMeterData> upload = new ArrayList<ClientMeterData>();
                                for (int i = 0; i < cbMeters.size(); i++) {
                                    if (taskThread == null)
                                        return;

                                    Meter meter = cbMeters.get(i);
                                    meter.IsBinaryzation = binaryPic;
                                    if (meter.Images == null) {
                                        meter.Images = new ArrayList<byte[]>();
                                    }
                                    meter.Images.clear();

                                    String sbgh = meter.Serial;
                                    if (MenuActivity.netThread == null)
                                        return;
                                    final byte addr[] = MenuActivity.netThread.getSBAddr(sbgh);

                                    int retrytimes = 3;
                                    if (binaryPic) {
                                        try {
                                            if (MenuActivity.netThread == null)
                                                return;
                                            MenuActivity.netThread.readBinaryPic_Parse1(addr);
                                        } catch (NetException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }

                                    for (int j = 0; j < 5; j++) {
                                        try {
                                            byte[] picdata;

                                            if (MenuActivity.netThread == null)
                                                return;

                                            if (binaryPic)
                                                picdata = MenuActivity.netThread.readBinaryPicBytes(addr, j + 1);
                                            else
                                                picdata = MenuActivity.netThread.readPicBytes(addr, j + 1, 3, progressBarHandler);

                                            if (picdata == null) {
                                                if (retrytimes-- > 0) {
                                                    j--;
                                                    progress_val--;
                                                } else {
                                                    meter.Images.add(picdata);
                                                }
                                            } else {
                                                meter.Images.add(picdata);
                                                retrytimes = 3;
                                            }
                                        } catch (NetException e) {
                                            continue;
                                        } catch (IOException e) {
                                            continue;
                                        } finally {
                                            progress_val++;
                                            // Update the progress bar
                                        }
                                    }
                                    MenuActivity.dataSource.updateMeterRecord(meter);

                                    //
                                    try {
                                        Thread.sleep(wait_time_interval);
                                    } catch (InterruptedException e2) {
                                        // TODO Auto-generated catch block
                                        e2.printStackTrace();
                                        return;
                                    }

                                    // 关闭当前水表
                                    try {
                                        MenuActivity.netThread.scmdCloseDsp(MenuActivity.netThread.getSBAddr(sbgh));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    // 延时
                                    try {
                                        Thread.sleep(wait_time_interval);
                                    } catch (InterruptedException e2) {
                                        // TODO Auto-generated catch block
                                        e2.printStackTrace();
                                        return;
                                    }

                                    final int progressfinal = i + 1;
                                    progressBarHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setProgress(progressfinal);
                                        }
                                    });

						/*// 打开后面一个
						if( i < cbMeters.size() - minSBNum )
						{
							if( MeterReader.netThread == null )
								return;
							byte addr2[] = MeterReader.netThread.getSBAddr(cbMeters.get(i+4).Serial);
							try {
								MeterReader.netThread.scmdOpenDsp(addr2);
							} catch (IOException e) {
								e.printStackTrace();
							}

							// 延时
							try {
								Thread.sleep(wait_time_interval);
							} catch (InterruptedException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
								return;
							}
						}*/
                                }

                                // 进程结束
                                progressBarHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.dismiss();

                                        // 网络上传部分
                                        SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(
                                                MenuActivity.PREF_NAME, 0);
                                        boolean cbAutoUpload = settings.getBoolean("AUTOUPLOAD", true);
                                        if (cbAutoUpload) {
                                            uploadMeterData();
                                        }
                                    }
                                });
                            }
                        });
                        taskThread.start();
                    }
                });
                dialog.show();
            }
        });

        btnMenu.setVisibility(View.GONE);
        btnCXQB.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final Bundle bundle = this.getIntent().getExtras();
        if (bundle.getBoolean("CB") == true) {
            menu.add(0, 0, 0, "抄写全部");
            //menu.add(0, 3, 3, "批量获取灰度图像");
            menu.add(0, 4, 4, "批量获取二值图像");
        }
        menu.add(0, 1, 1, "显示异常");
        menu.add(0, 2, 2, "上传数据");
        return super.onCreateOptionsMenu(menu);
    }

    private Handler progressBarHandler = new Handler();
    private boolean showErrors = false;

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

    private List<Meter> selectCBMeter() {
        List<Meter> result = new ArrayList<Meter>();
        for (Meter meter : meterList) {
            if (meter.Serial != null) {
                result.add(meter);
            }
        }

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()) {
            case 0: // Query all watermeters
            {
                ListView listView = getListView();
                progressBar.setMessage("抄表进度");
                progressBar.setIndeterminate(false);
                progressBar.setProgress(0);

                int listC = listView.getCount();
                int headC = listView.getHeaderViewsCount();
                progressBar.setMax(Math.max(listC - headC, 1));
                progressBar.show();

                final List<Meter> cbMeters = selectCBMeter();

                taskThread = new Thread(new Runnable() {

                    private int progress_val = 0;

                    @Override
                    public void run() {

                        progress_val = 0;
                        //int minSBNum = 0;//Math.min(4, cbMeters.size());

					/*// 打开前4个水表
					for(int i=0;i<minSBNum;i++)
					{
						if( MeterReader.netThread == null )
							return;
						
						byte addr[] = MeterReader.netThread.getSBAddr(cbMeters.get(i).Serial);
				
						if( MeterReader.netThread == null )
							return;

						try {
							MeterReader.netThread.scmdOpenDsp(addr);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						try {
							Thread.sleep(wait_time_interval);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
							return;
						}
					}
					
					// Sleep 6s
					
					/*try {
						Thread.sleep(6000);
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}//*/

                        List<ClientMeterData> upload = new ArrayList<ClientMeterData>();
                        for (int i = 0; i < cbMeters.size(); i++) {
                            if (taskThread == null)
                                return;

                            int _id = cbMeters.get(i).id;
                            String sSBBH = cbMeters.get(i).MeterID;
                            String sSBGH = cbMeters.get(i).Serial;

                            try {
                                Meter meter = MenuActivity.dataSource.updateMeterData(_id);

                                ClientMeterData data = new ClientMeterData();
                                data.MeterID = meter.MeterID;
                                data.Data = meter.Data;
                                data.DataTime = meter.DataTime;
                                data.IsFault = meter.IsFault;
                                data.IsBinaryzation = meter.IsBinaryzation;
                                data.Images = new ArrayList<int[]>();
                                if (meter.Images != null) {
                                    for (byte[] b : meter.Images) {
                                        data.Images.add(Utils.toIntArray(b));
                                    }
                                }
                                data.RecognitionResult = Utils.toIntArray(meter.RecognitionResult);
                                data.WATER = meter.WATER;
                                upload.add(data);
                            } catch (IOException e) { //catch (IOException e)
                                //final IOException e1 = e;
                                progressBarHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        MenuActivity.uiAct.resetNetwork(thisView);
                                    }
                                });
                            }

                            progress_val++;
                            // Update the progress bar
                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(progress_val);
                                }
                            });

                            try {
                                Thread.sleep(wait_time_interval);
                            } catch (InterruptedException e2) {
                                // TODO Auto-generated catch block
                                e2.printStackTrace();
                                return;
                            }

                            // 关闭当前水表
                            try {
                                if (MenuActivity.netThread == null)
                                    return;

                                MenuActivity.netThread.scmdCloseDsp(MenuActivity.netThread.getSBAddr(sSBGH));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // 延时
                            try {
                                Thread.sleep(wait_time_interval);
                            } catch (InterruptedException e2) {
                                // TODO Auto-generated catch block
                                e2.printStackTrace();
                                return;
                            }

                            // 打开后面一个
						/*if( i < cbMeters.size() - minSBNum )
						{
							if( MeterReader.netThread == null )
								return;
							byte addr[] = MeterReader.netThread.getSBAddr(cbMeters.get(i+4).Serial);
							try {
								MeterReader.netThread.scmdOpenDsp(addr);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							// 延时
							try {
								Thread.sleep(wait_time_interval);
							} catch (InterruptedException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
								return;
							}
						}//*/
                        }

                        // 进程结束
                        progressBarHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                meterList = MenuActivity.dataSource.getMeters(iXQBH, iLYH, sCJJH);
                                adapter.notifyDataSetChanged();
                                progressBar.dismiss();

                                // 网络上传部分
                                SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(
                                        MenuActivity.PREF_NAME, 0);
                                boolean cbAutoUpload = settings.getBoolean("AUTOUPLOAD", true);
                                if (cbAutoUpload) {
                                    uploadMeterData();
                                }
                            }
                        });
                    }
                });
                taskThread.start();

                break;
            }
            case 1: {
                if (!showErrors) {
                    showErrors = true;
                    menuItem.setTitle("显示所有");

                    meterList = MenuActivity.dataSource.getMeterErrors(iXQBH, iLYH, sCJJH);
                    adapter.notifyDataSetChanged();
                } else {
                    showErrors = false;
                    menuItem.setTitle("显示异常");

                    meterList = MenuActivity.dataSource.getMeters(iXQBH, iLYH, sCJJH);
                    adapter.notifyDataSetChanged();
                }

                break;
            }
            case 2: {    // 手动上传数据
                uploadMeterData();
                break;
            }
            case 3:
            case 4: {
                if (menuItem.getItemId() == 3)
                    binaryPic = false;
                else if (menuItem.getItemId() == 4)
                    binaryPic = true;

                progressBar.setMessage("数据读取进度");
                progressBar.setIndeterminate(false);
                progressBar.setProgress(0);
                progressBar.setMax(meterList.size());
                progressBar.show();

                final List<Meter> cbMeters = selectCBMeter();

                taskThread = new Thread(new Runnable() {
                    private int progress_val = 0;

                    @Override
                    public void run() {

                        progress_val = 0;

                        //int minSBNum = Math.min(4, cbMeters.size());
					
					/*// 打开前4个水表
					for(int i=0;i<minSBNum;i++)
					{
						if( MeterReader.netThread == null )
							return;
						byte addr[] = MeterReader.netThread.getSBAddr(cbMeters.get(i).Serial);

						try {
							MeterReader.netThread.scmdOpenDsp(addr);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						try {
							Thread.sleep(wait_time_interval);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
							return;
						}
					}
					
					// Sleep 6s
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}*/

                        List<ClientMeterData> upload = new ArrayList<ClientMeterData>();
                        for (int i = 0; i < cbMeters.size(); i++) {
                            if (taskThread == null)
                                return;

                            Meter meter = cbMeters.get(i);
                            meter.IsBinaryzation = binaryPic;
                            if (meter.Images == null) {
                                meter.Images = new ArrayList<byte[]>();
                            }
                            meter.Images.clear();

                            String sbgh = meter.Serial;
                            if (MenuActivity.netThread == null)
                                return;
                            final byte addr[] = MenuActivity.netThread.getSBAddr(sbgh);

                            int retrytimes = 3;
                            if (binaryPic) {
                                try {
                                    if (MenuActivity.netThread == null)
                                        return;
                                    MenuActivity.netThread.readBinaryPic_Parse1(addr);
                                } catch (NetException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            for (int j = 0; j < 5; j++) {
                                try {
                                    byte[] picdata;

                                    if (MenuActivity.netThread == null)
                                        return;

                                    if (binaryPic)
                                        picdata = MenuActivity.netThread.readBinaryPicBytes(addr, j + 1);
                                    else
                                        picdata = MenuActivity.netThread.readPicBytes(addr, j + 1, 3, progressBarHandler);

                                    if (picdata == null) {
                                        if (retrytimes-- > 0) {
                                            j--;
                                            progress_val--;
                                        } else {
                                            meter.Images.add(picdata);
                                        }
                                    } else {
                                        meter.Images.add(picdata);
                                        retrytimes = 3;
                                    }
                                } catch (NetException e) {
                                    continue;
                                } catch (IOException e) {
                                    continue;
                                } finally {
                                    progress_val++;
                                    // Update the progress bar
                                }
                            }
                            MenuActivity.dataSource.updateMeterRecord(meter);

                            //
                            try {
                                Thread.sleep(wait_time_interval);
                            } catch (InterruptedException e2) {
                                // TODO Auto-generated catch block
                                e2.printStackTrace();
                                return;
                            }

                            // 关闭当前水表
                            try {
                                MenuActivity.netThread.scmdCloseDsp(MenuActivity.netThread.getSBAddr(sbgh));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // 延时
                            try {
                                Thread.sleep(wait_time_interval);
                            } catch (InterruptedException e2) {
                                // TODO Auto-generated catch block
                                e2.printStackTrace();
                                return;
                            }

                            final int progressfinal = i + 1;
                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(progressfinal);
                                }
                            });

						/*// 打开后面一个
						if( i < cbMeters.size() - minSBNum )
						{
							if( MeterReader.netThread == null )
								return;
							byte addr2[] = MeterReader.netThread.getSBAddr(cbMeters.get(i+4).Serial);
							try {
								MeterReader.netThread.scmdOpenDsp(addr2);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							// 延时
							try {
								Thread.sleep(wait_time_interval);
							} catch (InterruptedException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
								return;
							}
						}*/
                        }

                        // 进程结束
                        progressBarHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.dismiss();

                                // 网络上传部分
                                SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(
                                        MenuActivity.PREF_NAME, 0);
                                boolean cbAutoUpload = settings.getBoolean("AUTOUPLOAD", true);
                                if (cbAutoUpload) {
                                    uploadMeterData();
                                }
                            }
                        });
                    }
                });
                taskThread.start();
            }
            break;
        }
        return true;
    }

    public void uploadMeterData() {
        progressBar.setMessage("上传数据");
        progressBar.setIndeterminate(true);
        progressBar.show();
        taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int uploadNum = SkRestClient.uploadMeterData(meterList);
                    final int result = uploadNum < meterList.size() ? -1 : 0;
                    if (result > -1) {
                        MenuActivity.dataSource.updateMeterStatusUploaded(iXQBH, iLYH, sCJJH, showErrors);
                    }
                    progressBarHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            if (result < 0) {
                                HintDialog.ShowHintDialog(Z_SBListActivity.this, "上传数据失败","提示");
                            } else {
                                HintDialog.ShowHintDialog(Z_SBListActivity.this, "上传数据成功","提示");
                            }
                        }
                    });
                } catch (UnknownHostException e) {
                    progressBarHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            HintDialog.ShowHintDialog(Z_SBListActivity.this, "无法连接到服务器","错误");
                        }
                    });
                } catch (InterruptedIOException e) {
                    progressBarHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            HintDialog.ShowHintDialog(Z_SBListActivity.this, "服务器无响应","错误");
                        }
                    });
                } catch (IOException e) {
                    progressBarHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            HintDialog.ShowHintDialog(Z_SBListActivity.this, "网络异常","错误");
                        }
                    });
                }
            }
        });
        taskThread.start();
    }

    @Override
    public void onDestroy() {
        meterList = null;

        super.onDestroy();
    }

    private BaseAdapter adapter = new BaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HandleView hv;

            if (convertView != null) {
                hv = (HandleView) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(Z_SBListActivity.this).inflate(
                        R.layout.sbinfo, null);
                hv = new HandleView();
                hv.tvSbbh = (TextView) convertView.findViewById(R.id.sbxh);
                hv.tvBccj = (TextView) convertView.findViewById(R.id.bccj);
                hv.tvSbzt = (TextView) convertView.findViewById(R.id.cbzt);

                convertView.setTag(hv);
            }

            Meter data = meterList.get(position);
            hv.tvSbbh.setText(data.MeterID);
            hv.tvBccj.setText(String.valueOf(data.Data));

            int avgBelief = 0;
            if (data.RecognitionResult != null) {
                for (int i = 0; i < 5; i++) {
                    avgBelief += data.RecognitionResult[i];
                }
                avgBelief /= 5;
            }

            if (data.state > 0) {
                if (!data.IsFault) {
                    if (avgBelief >= iZXDThresh) {
                        hv.tvSbzt.setText("正常");
                        hv.tvSbzt.setBackgroundColor(0xFF00FF00);
                    } else {
                        hv.tvSbzt.setText("置信低");
                        hv.tvSbzt.setBackgroundColor(0xFFB8860B);
                    }
                } else {
				/*long errorCode = -1;
				try {
					//errorCode = Long.parseLong(sbzt);
				}catch(Exception e1) {
					
				}
				hv.tvSbzt.setText(NetExp_Builder.getExpMessage((int) errorCode));*/
                    hv.tvSbzt.setText("异常");
                    hv.tvSbzt.setBackgroundColor(0xFFFF0000);
                }
            } else {
                hv.tvSbzt.setText("未抄表");
                hv.tvSbzt.setBackgroundColor(0x00000000);
            }

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            if (meterList == null) {
                return 0;
            }
            return meterList.size();
        }

        class HandleView {
            TextView tvSbzt;
            TextView tvSbbh;
            TextView tvBccj;
        }
    };
}
