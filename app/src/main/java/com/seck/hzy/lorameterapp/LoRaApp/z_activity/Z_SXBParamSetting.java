package com.seck.hzy.lorameterapp.LoRaApp.z_activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.utils.HintDialog;
import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;
import com.seck.hzy.lorameterapp.R;

import java.io.IOException;

/**
 * 摄像表参数设置界面，使用定制的List显示。
 *
 * @author l412
 */
public class Z_SXBParamSetting extends ListActivity {

    private EditText et_Addr = null;
    private String addr_Broad;
    private Z_SXBParamSetting thisView = null;
    private boolean passwordInputed = false;

    private String[] paramNames = {
            "宽度",
            "高度",
            "半高度",
            "模板半高度",
            "Y坐标",
            "X1坐标",
            "X2坐标",
            "X3坐标",
            "X4坐标",
            "X5坐标",
            "X6坐标",
            "X7坐标",
            "X8坐标",
            "字轮数",
            "置信度阈值",
            "二值化系数",
            "代码段",
            "待机时间"
    };

    private int[] paramAddr = {
            0x80,
            0x82,
            0x84,
            0x8A,
            0x8E,
            0x90,
            0x92,
            0x94,
            0x96,
            0x98,
            0x9A,
            0x9C,
            0x9E,
            0xA0,
            0xA1,
            0xA2,
            0xAF,
            0xC1
    };

    private int[] paramLen = {
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
            2,
            2,
            1,
            1,
            1,
            1,
            1
    };

    private String[] paramStr = new String[paramAddr.length];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		/*PasswordDialog pswdlg = new PasswordDialog(this,0);
        if( pswdlg.showDialog() != 1 )
		{
			HintDialog.ShowHintDialog(this, "密码输入错误", "错误");
			return;
		}*/
        //		ModuleHintDialog dlg = new ModuleHintDialog(this, "谨慎设置！如您不确定参数的用途，请勿改动原设置！", "提示");
        //		dlg.showDialog();
        HintDialog.ShowHintDialog(this, "谨慎设置！如您不确定参数的用途，请勿改动原设置！", "提示");
        //		HintDialog.ShowHintDialog(this,"谨慎设置！如您不确定参数的用途，请勿改动原设置！", "提示");

        final ListView listView = getListView();
        View header = LayoutInflater.from(this).inflate(R.layout.z_activity_sxbsetheader, null);
        listView.addHeaderView(header, null, false);
        listView.setDividerHeight(3);

        et_Addr = (EditText) listView.findViewById(R.id.EditText_Addr);

        SharedPreferences settings = MenuActivity.uiAct.getSharedPreferences(MenuActivity.PREF_NAME, 0);
        addr_Broad = settings.getString("BADDR", MenuActivity.DEFAULT_BADDR);    // 广播地址

        listView.setAdapter(new MySetAdapter(this));

        thisView = this;
    }

    static class ViewHolder {
        TextView tv_Name;
        EditText et_Param;
        Button bt_Read;
        Button bt_Write;
    }

    private class MySetAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MySetAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
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
        public View getView(int position, View convertView, ViewGroup parent) {
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

            final int paramPos = paramAddr[position];
            final int paLen = paramLen[position];
            final int viewPos = position;

            final ViewHolder fholder = holder;

            holder.tv_Name.setText(paramNames[position]);

            holder.bt_Read.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    String addr = et_Addr.getText().toString().trim();
                    if (addr.equals("")) {
                        addr = addr_Broad.trim();
                    }

                    byte[] baddr = MenuActivity.netThread.getSBAddr(addr);

                    try {
                        int paramVal = MenuActivity.netThread.readParam(baddr, paramPos, paLen);
                        fholder.et_Param.setText(paramVal + "");
                        paramStr[viewPos] = paramVal + "";
                    } catch (NetException e) {
                        HintDialog.ShowHintDialog(thisView, e.sdesc, "错误");
                    } catch (IOException e) {
                        finish();
                        MenuActivity.uiAct.resetNetwork(thisView);
                    }

                }
            });

            holder.bt_Write.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    paramStr[viewPos] = fholder.et_Param.getText().toString();

                    //PasswordDialog dlg = new PasswordDialog(thisView);
                    //if( passwordInputed ||
                    //	dlg.showDialog() == 1 )
                    {

                        //passwordInputed = true;
                        String addr = et_Addr.getText().toString().trim();
                        if (addr.equals("")) {
                            addr = addr_Broad.trim();
                        }

                        byte[] baddr = MenuActivity.netThread.getSBAddr(addr);
                        byte[] params = new byte[paLen];

                        int paramVal = 0;
                        try {
                            paramVal = Integer.parseInt(paramStr[viewPos]);
                        } catch (NumberFormatException e) {
                            HintDialog.ShowHintDialog(thisView, "请输入有效数据", "错误");
                            return;
                        }

                        if (paLen == 1)
                            params[0] = (byte) paramVal;
                        else {
                            params[0] = (byte) (paramVal & 0xFF);
                            params[1] = (byte) ((paramVal >> 8) & 0xFF);
                        }

                        try {
                            boolean writeSuc = MenuActivity.netThread.writeParam(baddr, paramPos, params);

                            if (writeSuc) {
                                HintDialog.ShowHintDialog(thisView, "写入成功", "提示");
                            }
                        } catch (NetException e) {
                            HintDialog.ShowHintDialog(thisView, e.sdesc, "错误");
                        } catch (IOException e) {
                            finish();
                            MenuActivity.uiAct.resetNetwork(thisView);
                        }
                    }
                    //else {
                    //	HintDialog.ShowHintDialog(thisView, "密码输入错误", "错误");
                    //}
                }
            });

            fholder.et_Param.setText(paramStr[viewPos]);
            return convertView;
        }
    }
}
