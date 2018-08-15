package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.seck.hzy.lorameterapp.LoRaApp.model.CjjDetails;
import com.seck.hzy.lorameterapp.LoRaApp.model.MeterDetails;
import com.seck.hzy.lorameterapp.LoRaApp.model.NS_Cjj;
import com.seck.hzy.lorameterapp.LoRaApp.model.NS_MeterUser;
import com.seck.hzy.lorameterapp.LoRaApp.model.NsXqMsg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by limbo on 2017/11/23.
 */

public class NS_DataHelper {

    public static String dbName = "NumStateDb.db";
    public static SQLiteDatabase db;

    public static SQLiteDatabase getdb() {
        try {
            String fileName = getSDPath() + "/" + dbName;
            File x = new File(fileName.toString());
            if (x.exists()) {
                Log.d("limbo", "数据库文件存在");
            } else {
                Log.d("limbo", "数据库文件不存在");
            }
            db = SQLiteDatabase.openOrCreateDatabase(fileName, null);
        } catch (Exception e) {
            Log.d("limbo", e.toString());
        }
        return db;
    }

    public static List<NS_Cjj> getXq() {

        ArrayList<NS_Cjj> cjjList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT distinct XqId, XqName  FROM   cjj", null);

        while (c.moveToNext()) {
            NS_Cjj cjj = new NS_Cjj();
            cjj.XqId = c.getInt(c.getColumnIndex("XqId"));
            cjj.XqName = c.getString(c.getColumnIndex("XqName"));
            cjjList.add(cjj);
        }
        c.close();
        return cjjList;
    }


    public static List<NS_Cjj> getCjj(int xqid) {

        ArrayList<NS_Cjj> cjjList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT XqId, XqName, CjjId, CjjAddr  FROM   cjj where xqid=" + xqid, null);

        while (c.moveToNext()) {
            NS_Cjj cjj = new NS_Cjj();
            cjj.XqId = c.getInt(c.getColumnIndex("XqId"));

            cjj.XqName = c.getString(c.getColumnIndex("XqName"));

            cjj.CjjId = c.getInt(c.getColumnIndex("CjjId"));

            cjj.CjjAddr = c.getString(c.getColumnIndex("CjjAddr"));
            cjjList.add(cjj);
        }
        c.close();
        return cjjList;
    }

    /**
     * 根据总采号获取下辖分采信息
     */
    public static List<NS_Cjj> getFCjj(int xqid, int zcjj) {

        ArrayList<NS_Cjj> cjjList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT XqId, XqName, CjjId, CjjAddr  FROM   cjj where xqid=" + xqid, null);

        while (c.moveToNext()) {
            NS_Cjj cjj = new NS_Cjj();
            cjj.XqId = c.getInt(c.getColumnIndex("XqId"));

            cjj.XqName = c.getString(c.getColumnIndex("XqName"));

            cjj.CjjId = c.getInt(c.getColumnIndex("CjjId"));

            cjj.CjjAddr = c.getString(c.getColumnIndex("CjjAddr"));
            if (cjj.CjjId / 10000 == zcjj) {
                cjjList.add(cjj);
            }
        }
        c.close();
        return cjjList;
    }

    public static List<NS_MeterUser> getMeters(int xqid, int cjjid) {

        ArrayList<NS_MeterUser> meters = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT  *  FROM   Meter where xqid=" + xqid + " and cjjid=" + cjjid, null);

        while (c.moveToNext()) {
            NS_MeterUser m = new NS_MeterUser();
            m.XqId = c.getInt(c.getColumnIndex("XqId"));
            m.CjjId = c.getInt(c.getColumnIndex("CjjId"));
            m.MeterId = c.getInt(c.getColumnIndex("MeterId"));
            m.MeterNumber = c.getString(c.getColumnIndex("MeterNumber"));
            m.UserName = c.getString(c.getColumnIndex("UserName"));
            m.UserAddr = c.getString(c.getColumnIndex("UserAddr"));
            m.LastData = c.getFloat(c.getColumnIndex("LastData"));
            m.LasteDate = c.getString(c.getColumnIndex("LastDate"));
            m.Data = c.getFloat(c.getColumnIndex("Data"));
            m.Date = c.getString(c.getColumnIndex("Date"));
            byte[] picBytes = c.getBlob(c.getColumnIndex("Pic"));
            if (picBytes == null) {
                m.Pic = null;
            } else {
                m.Pic = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
            }
            meters.add(m);
        }
        c.close();
        return meters;
    }

    /**
     * 根据总采号获取所有分采下水表
     */
    public static List<NS_MeterUser> getZCMeters(int xqid, int zcjjid) {

        ArrayList<NS_MeterUser> meters = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT  *  FROM   Meter where xqid=" + xqid, null);
        while (c.moveToNext()) {
            NS_MeterUser m = new NS_MeterUser();
            m.XqId = c.getInt(c.getColumnIndex("XqId"));
            m.CjjId = c.getInt(c.getColumnIndex("CjjId"));
            m.MeterId = c.getInt(c.getColumnIndex("MeterId"));
            m.MeterNumber = c.getString(c.getColumnIndex("MeterNumber"));
            m.UserName = c.getString(c.getColumnIndex("UserName"));
            m.UserAddr = c.getString(c.getColumnIndex("UserAddr"));
            m.LastData = c.getFloat(c.getColumnIndex("LastData"));
            m.LasteDate = c.getString(c.getColumnIndex("LastDate"));
            m.Data = c.getFloat(c.getColumnIndex("Data"));
            m.Date = c.getString(c.getColumnIndex("Date"));
            byte[] picBytes = c.getBlob(c.getColumnIndex("Pic"));
            if (picBytes == null) {
                m.Pic = null;
            } else {
                m.Pic = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
            }
            Log.d("limbo", m.CjjId / 10000 + "--" + zcjjid);
            if (m.CjjId / 10000 == zcjjid) {
                meters.add(m);
            } else {

            }

        }
        c.close();
        return meters;
    }

    public static void changeUser(int xqid, int cjjid, String meterid, long meterNum, String
            Data, String Date) {

        try {
            db.beginTransaction();
            try {
                ContentValues cv = new ContentValues();//图片以byte数组字符串形式放入数据库
                cv.put("Date", Date);
                //                cv.put("MeterId", meterNum);
                //                cv.put("MeterNumber", meterid);
                cv.put("Data", Data);
                String[] args = {String.valueOf(xqid), String.valueOf(cjjid), String.valueOf(meterNum)};//将需要更新的数据放入
                db.update("Meter", cv, "Xqid=? and Cjjid=? and Meterid=?", args);//更新数据表中特定数据
                db.setTransactionSuccessful();
                // 设置事务的标志为true，调用此方法会在执行到endTransaction()方法是提交事务，
                // 若没有调用此方法会在执行到endTransaction()方法回滚事务。
            } finally {
                db.endTransaction();

            }
        } catch (Exception e) {
            Log.d("limbo", e.toString());
        }
    }


    public static String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断SD卡是否存在,及是否具有读写的权利
        if (sdCardExist) {
            Log.d("limbo", "exist");
            sdDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckLoRaDB");// 获取SD卡的path

            if (!sdDir.exists()) {
                sdDir.mkdir();
            } else {
                //                sdDir = new File(Environment.getExternalStorageDirectory().getPath() + "/SeckLoRaDB");// 获取SD卡的path
            }
            Log.d("limbo", "return sdPath:" + sdDir.toString());

            //            return "/storage/sdcard0/SeckLoRaDB";//将path转化为string类型返回
            //            return "/mnt/sdcard/SeckLoRaDB";//将path转化为string类型返回
            return sdDir.toString();//将path转化为string类型返回
        } else {
            Log.d("limbo", "no exist");
            //            return "/mnt/sdcard2/SeckLoRaDB";
            return "/storage/sdcard0" + "/SeckLoRaDB";
        }

    }

    /**
     * 网络部分完整小区
     */
    public static void addXqMsg(NsXqMsg nsXqMsg) {
        try {
            db.beginTransaction();
            String xqid = nsXqMsg.getXqId();
            String xqname = "";
            if (!HzyUtils.isEmpty(nsXqMsg.getXqName())) {
                xqname = nsXqMsg.getXqName();
            }
            Log.d("limbo", "xqid:" + xqid + " xqname:" + xqname);
            List<CjjDetails> cjjlist = nsXqMsg.getCjjDetails();
            Log.d("limbo", cjjlist.size() + "");

            for (int i = 0; i < cjjlist.size(); i++) {
                ContentValues cv = new ContentValues();
                String cjjid = cjjlist.get(i).getCjjId();
                String cjjtype = "";
                String cjjaddr = "";
                if (!HzyUtils.isEmpty(cjjlist.get(i).getCjjAddr())) {
                    cjjaddr = cjjlist.get(i).getCjjAddr();
                }
                if (!HzyUtils.isEmpty(cjjlist.get(i).getCjjType())) {
                    cjjtype = cjjlist.get(i).getCjjType();
                }
                cv.put("XqId", Integer.parseInt(xqid));
                cv.put("XqName", xqname);
                cv.put("CjjId", Integer.parseInt(cjjid));
                cv.put("CjjAddr", cjjaddr);
                cv.put("CjjType", cjjtype);
                db.insert("Cjj", null, cv);//添加采集机表数据
                List<MeterDetails> meterlist = cjjlist.get(i).getMeterDetails();

                for (int j = 0; j < meterlist.size(); j++) {
                    ContentValues cvx = new ContentValues();
                    cvx.put("XqId", Integer.parseInt(xqid));
                    cvx.put("CjjId", Integer.parseInt(cjjid));
                    int meterid = Integer.parseInt(meterlist.get(j).getMeterId());
                    String meternumber = "";
                    String metertype = "";
                    String username = "";
                    String useraddr = "";
                    String usernumber = "";
                    String lastdata = "";
                    String lastdate = "";
                    String data = "";
                    String date = "";
                    if (!HzyUtils.isEmpty(meterlist.get(j).getMeterNumber())) {
                        meternumber = meterlist.get(j).getMeterNumber();
                    }
                    if (!HzyUtils.isEmpty(meterlist.get(j).getMeterType())) {
                        metertype = meterlist.get(j).getMeterType();
                    }
                    if (!HzyUtils.isEmpty(meterlist.get(j).getUserName())) {
                        username = meterlist.get(j).getUserName();
                    }
                    if (!HzyUtils.isEmpty(meterlist.get(j).getUserAddr())) {
                        useraddr = meterlist.get(j).getUserAddr();
                    }
                    if (!HzyUtils.isEmpty(meterlist.get(j).getUserNumber())) {
                        usernumber = meterlist.get(j).getUserNumber();
                    }
                    if (!HzyUtils.isEmpty(meterlist.get(j).getLastData())) {
                        lastdata = meterlist.get(j).getLastData();
                    }
                    if (!HzyUtils.isEmpty(meterlist.get(j).getLastDate())) {
                        lastdate = meterlist.get(j).getLastDate();
                    }
                    if (!HzyUtils.isEmpty(meterlist.get(j).getData())) {
                        data = meterlist.get(j).getData();
                    }
                    if (!HzyUtils.isEmpty(meterlist.get(j).getDate())) {
                        date = meterlist.get(j).getDate();
                    }

                    cvx.put("MeterId", meterid);
                    cvx.put("MeterNumber", meternumber);
                    cvx.put("MeterType", metertype);
                    cvx.put("UserName", username);
                    cvx.put("UserAddr", useraddr);
                    cvx.put("LastData", lastdata);
                    cvx.put("LastDate", lastdate);
                    cvx.put("Data", data);
                    cvx.put("Date", date);
                    db.insert("Meter", null, cvx);//添加采集机表数据
                }

            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("limbo", e.toString());
        } finally {
            db.endTransaction();
        }
    }


    public static NsXqMsg uploadXqMsg(int xqid, String xqname) {

        NsXqMsg xq = new NsXqMsg();
        xq.setXqId(xqid + "");
        xq.setXqName(xqname);
        Cursor c = db.rawQuery("SELECT XqId, XqName, CjjId, CjjAddr,CjjType  FROM   Cjj where xqid=" + xqid, null);
        ArrayList<CjjDetails> cjjDetailses = new ArrayList<>();
        while (c.moveToNext()) {
            CjjDetails cjjDetails = new CjjDetails();
            int cjjid = c.getInt(c.getColumnIndex("CjjId"));
            String cjjtype = c.getString(c.getColumnIndex("CjjType"));
            cjjDetails.setCjjId(cjjid + "");
            cjjDetails.setCjjType(cjjtype);

            cjjDetails.setCjjAddr(c.getString(c.getColumnIndex("CjjAddr")));


            Cursor c1 = db.rawQuery("SELECT  *  FROM   Meter where xqid=" + xqid + " and cjjid=" + cjjid, null);
            ArrayList<MeterDetails> meterDetailses = new ArrayList<>();
            while (c1.moveToNext()) {
                MeterDetails meterDetails = new MeterDetails();
                meterDetails.setMeterId(c1.getInt(c1.getColumnIndex("MeterId")) + "");
                meterDetails.setMeterNumber(c1.getString(c1.getColumnIndex("MeterNumber")));
                meterDetails.setMeterType(c1.getString(c1.getColumnIndex("MeterType")));
                meterDetails.setUserName(c1.getString(c1.getColumnIndex("UserName")));
                meterDetails.setUserAddr(c1.getString(c1.getColumnIndex("UserAddr")));
                meterDetails.setLastData(c1.getString(c1.getColumnIndex("LastData")));
                meterDetails.setLastDate(c1.getString(c1.getColumnIndex("LastDate")));
                meterDetails.setData(c1.getString(c1.getColumnIndex("Data")));
                meterDetails.setDate(c1.getString(c1.getColumnIndex("Date")));
//                meterDetails.setData("12345");
//                meterDetails.setDate("2018-03-30 13:00:00");


                meterDetailses.add(meterDetails);
            }
            cjjDetails.setMeterDetails(meterDetailses);
            cjjDetailses.add(cjjDetails);
        }
        xq.setCjjDetails(cjjDetailses);

        c.close();
        return xq;
    }
    public static void deleteXqAndMeter(int xqid) {
        try {
            db.beginTransaction();
            try {
                String[] args = {String.valueOf(xqid)};//将需要更新的数据放入
                db.delete("Cjj", "XqId=?", args);
                db.delete("Meter", "Xqid=?", args);
                db.setTransactionSuccessful();
                // 设置事务的标志为true，调用此方法会在执行到endTransaction()方法是提交事务，
                // 若没有调用此方法会在执行到endTransaction()方法回滚事务。
            } finally {
                db.endTransaction();

            }
        } catch (Exception e) {
            Log.d("limbo", e.toString());
        }
    }
}
