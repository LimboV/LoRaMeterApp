package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.IOException;
import java.util.UUID;

public class BlueToothActivity extends ListActivity {
    private static final String LOG_TAG = BlueToothActivity.class.getSimpleName();
    private ArrayAdapter<String> adapter = null;
    private BroadcastReceiver cwjReceiver = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, R.layout.lora_activity_blue_tooth);
        }
        AndPermission.with(this)
                .requestCode(300)
                .permission(Permission.LOCATION)
                .callback(this)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(BlueToothActivity.this, rationale).show();
                    }
                })
                .start();

        if (cwjReceiver == null) {
            cwjReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    Log.d("limbo", action);

                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (adapter.getPosition(device.getName() + "\n" + device.getAddress()) < 0) {
                            adapter.add("设备名："+device.getName() + "\n蓝牙地址：" + device.getAddress());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            };
        }

        setListAdapter(adapter);
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        final BlueToothActivity thisAct = this;
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NetworkLayer.adapter.cancelDiscovery();// 停止搜索

                // 创建新连接
                String str = adapter.getItem(position);
                String[] values = str.split("\n");
                String address = values[1];
                Log.v("limbo", "Connect to  " + address);
                BluetoothDevice btDev = NetworkLayer.adapter.getRemoteDevice(address);
                BluetoothSocket mmSocket = null;
                try {
                    final UUID UUID_RFCOMM_GENERIC = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//串口通用UUID
                    mmSocket = btDev.createRfcommSocketToServiceRecord(UUID_RFCOMM_GENERIC);
                    //                    Method m = btDev.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                    //                    mmSocket = (BluetoothSocket) m.invoke(btDev, 1);
                } catch (Exception e) {
                    Log.v("limbo", e.toString());
                    e.printStackTrace();
                }

                try {
                    mmSocket.connect();
                } catch (IOException connectException) {
                    Log.v("limbo", connectException.toString());
                    try {
                        mmSocket.close();
                    } catch (IOException closeException) {
                        Log.v("limbo", connectException.toString());
                    }

                    //Toast.makeText(getApplicationContext(), "无法建立有效连接", Toast.LENGTH_LONG).show();
                    Intent data = new Intent();
                    setResult(BluetoothConnectThread.NETWORK_FAILED, data);//设置返回结果-->失败
                    thisAct.finish();
                    return;
                }

                // Create network thread.
                MenuActivity.netThread = new BluetoothConnectThread(mmSocket);
                MenuActivity.netThread.deviceName = btDev.getName();
                MenuActivity.netThread.start();

                // Test a AT command.
                // netThread.write(new byte[] {'A', 'T'});
                // Toast.makeText(getApplicationContext(), "网络连接已建立", Toast.LENGTH_LONG).show();
                Intent data = new Intent();
                setResult(BluetoothConnectThread.NETWORK_CONNECTED, data);//设置返回结果-->成功
                thisAct.finish();
            }
        });

        // Discover all bluetooth devices.
        NetworkLayer.adapter.cancelDiscovery();//关闭查找
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(cwjReceiver, filter);
        NetworkLayer.adapter.startDiscovery();

    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "返回主界面");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case 1:
                Intent intent = new Intent(BlueToothActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BlueToothActivity.this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cwjReceiver);//关闭广播
    }

}
