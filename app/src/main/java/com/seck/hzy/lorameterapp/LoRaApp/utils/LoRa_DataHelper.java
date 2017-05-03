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

public class LoRa_DataHelper {

    public static String dbName = "LoRaDb.db";
    public static SQLiteDatabase db;

    public static SQLiteDatabase getdb() {
        try {
            String fileName = getSDPath() + "/" + dbName;
            db = SQLiteDatabase.openOrCreateDatabase(fileName, null);
        } catch (Exception e) {
        }
        return db;
    }

    public static List<LoRa_Cjj> getCjj(int xqid) {

        ArrayList<LoRa_Cjj> cjjList = new ArrayList<LoRa_Cjj>();
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


    public static List<LoRa_Cjj> getXq() {

        ArrayList<LoRa_Cjj> cjjList = new ArrayList<LoRa_Cjj>();
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

    public static List<LoRa_MeterUser> getMeters(int xqid, int cjjid) {

        ArrayList<LoRa_MeterUser> meters = new ArrayList<LoRa_MeterUser>();
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

    public static List<LoRa_MeterUser> getAllMeter(int xqid) {
        ArrayList<LoRa_MeterUser> meters = new ArrayList<LoRa_MeterUser>();
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
            if (m.UserAddr.length() == 0) {

            } else {
                meters.add(m);
            }

        }
        c.close();
        return meters;
    }

    public static void addXq(int xqid, String xqname, int cjjid, String cjjname) {
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("XqId", xqid);
            cv.put("XqName", xqname);
            cv.put("CjjId", cjjid);
            cv.put("CjjAddr", cjjname);
            db.insert("Cjj", null, cv);//添加数据
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("limbo", e.toString());
        } finally {
            db.endTransaction();
        }

    }

    public static void addMeter(int xqid, int cjjid, int meterid, String meterNumber, String userAddr, String date) {

        try {
            db.beginTransaction();
            try {
                ContentValues cv = new ContentValues();//图片以byte数组字符串形式放入数据库
                cv.put("XqId", xqid);
                cv.put("CjjId", cjjid);
                cv.put("MeterId", meterid);
                cv.put("MeterNumber", meterNumber);
                cv.put("UserAddr", userAddr);
                cv.put("Date", date);
                //				db.update("Meter",cv, "Xqid=? and cjjid=? and meterid=?", args);//更新数据表中特定数据
                db.insert("Meter", null, cv);//添加数据
                db.setTransactionSuccessful();
                // 设置事务的标志为true，调用此方法会在执行到endTransaction()方法是提交事务，
                // 若没有调用此方法会在执行到endTransaction()方法回滚事务。
            } finally {
                //				if (bos != null){
                //					bos.close();
                //				}
                db.endTransaction();

            }
        } catch (Exception e) {
        }
    }

    public static void changeUser(int xqid, int cjjid, String userAddr, String meterid, long meterNum, String Date) {

        try {
            db.beginTransaction();
            try {
                ContentValues cv = new ContentValues();//图片以byte数组字符串形式放入数据库
                cv.put("Date", Date);
                cv.put("MeterId", meterNum);
                cv.put("MeterNumber", meterid);
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

    public static void deleteUser(int xqid, int cjjid, long meterNum) {
        try {
            db.beginTransaction();
            try {
                String[] args = {String.valueOf(xqid), String.valueOf(cjjid), String.valueOf(meterNum)};//将需要更新的数据放入
                db.delete("Meter", "Xqid=? and cjjid=? and meterid=?", args);
                db.setTransactionSuccessful();
                // 设置事务的标志为true，调用此方法会在执行到endTransaction()方法是提交事务，
                // 若没有调用此方法会在执行到endTransaction()方法回滚事务。
            } finally {
                db.endTransaction();

            }
        } catch (Exception e) {
            Log.e("limbo", e.toString());
        }
    }

    public static void deleteXqAndMeter(int xqid){
        try {
            db.beginTransaction();
            try {
                String[] args = {String.valueOf(xqid)};//将需要更新的数据放入
                db.delete("Cjj","XqId=?",args);
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

    public static String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断SD卡是否存在,及是否具有读写的权利
        if (sdCardExist) {
            sdDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckLoRaDB");// 获取SD卡的path
//            sdDir = new File("/mnt/shell/emulated/0/SeckLoRaDB");// 获取SD卡的path
            if (!sdDir.exists()) {
                sdDir.mkdir();
            } else {
                sdDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SeckLoRaDB");// 获取SD卡的path
//                sdDir = new File("/mnt/shell/emulated/0/SeckLoRaDB");// 获取SD卡的path
            }
            Log.e("limbo","return sdPath:"+sdDir.toString());
            return sdDir.toString().replace("/storage/emulated/0", "/sdcard");//将path转化为string类型返回
        } else {
            return null;
        }

    }

    public static void closeDB() {
        db.close();//关闭数据库
    }

}
