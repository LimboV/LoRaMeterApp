package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

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

    public static List<LoRa_Cjj> getXq() {

        ArrayList<LoRa_Cjj> cjjList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT distinct XqId, XqName  FROM   cjj", null);

        while (c.moveToNext()) {
            LoRa_Cjj cjj = new LoRa_Cjj();
            cjj.XqId = c.getInt(c.getColumnIndex("XqId"));
            cjj.XqName = c.getString(c.getColumnIndex("XqName"));
            cjjList.add(cjj);
        }
        c.close();
        return cjjList;
    }


    public static List<LoRa_Cjj> getCjj(int xqid) {

        ArrayList<LoRa_Cjj> cjjList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT XqId, XqName, CjjId, CjjAddr  FROM   cjj where xqid=" + xqid, null);

        while (c.moveToNext()) {
            LoRa_Cjj cjj = new LoRa_Cjj();
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
    public static List<LoRa_Cjj> getFCjj(int xqid, int zcjj) {

        ArrayList<LoRa_Cjj> cjjList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT XqId, XqName, CjjId, CjjAddr  FROM   cjj where xqid=" + xqid, null);

        while (c.moveToNext()) {
            LoRa_Cjj cjj = new LoRa_Cjj();
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

    public static List<LoRa_MeterUser> getMeters(int xqid, int cjjid) {

        ArrayList<LoRa_MeterUser> meters = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT  *  FROM   Meter where xqid=" + xqid + " and cjjid=" + cjjid, null);

        while (c.moveToNext()) {
            LoRa_MeterUser m = new LoRa_MeterUser();
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
    public static List<LoRa_MeterUser> getZCMeters(int xqid, int zcjjid) {

        ArrayList<LoRa_MeterUser> meters = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT  *  FROM   Meter where xqid=" + xqid, null);
        while (c.moveToNext()) {
            LoRa_MeterUser m = new LoRa_MeterUser();
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
}
