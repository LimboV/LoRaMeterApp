package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.seck.hzy.lorameterapp.LoRaApp.model.Pcjj;
import com.seck.hzy.lorameterapp.LoRaApp.model.PmeterUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class P_DataHelper {

    public static String dbName = "CameraDb.db";
    public static SQLiteDatabase db;

    public static SQLiteDatabase getdb() {
        try {
            String fileName = getSDPath() + "/" + dbName;// 锟斤拷name锟斤拷锟斤拷目录锟斤拷
            db = SQLiteDatabase.openOrCreateDatabase(fileName, null);
        } catch (Exception e) {
        }
        return db;
    }

    /**
     * 获取P型表采集机
     */
    public static List<Pcjj> getPcjj(int xqid) {

        ArrayList<Pcjj> cjjList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT XqId, XqName, CjjId, CjjAddr  FROM   cjj where xqid=" + xqid, null);

        while (c.moveToNext()) {
            Pcjj cjj = new Pcjj();
            cjj.XqId = c.getInt(c.getColumnIndex("XqId"));

            cjj.XqName = c.getString(c.getColumnIndex("XqName"));

            cjj.CjjId = c.getInt(c.getColumnIndex("CjjId"));

            cjj.CjjAddr = c.getString(c.getColumnIndex("CjjAddr"));
            cjjList.add(cjj);
        }
        c.close();
        return cjjList;
    }


    public static List<Pcjj> getXq() {

        ArrayList<Pcjj> cjjList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT distinct XqId, XqName  FROM   cjj", null);

        while (c.moveToNext()) {
            Pcjj cjj = new Pcjj();
            cjj.XqId = c.getInt(c.getColumnIndex("XqId"));
            cjj.XqName = c.getString(c.getColumnIndex("XqName"));
            cjjList.add(cjj);
        }
        c.close();
        return cjjList;
    }

    public static List<PmeterUser> getMeters(int xqid, int cjjid) {

        ArrayList<PmeterUser> meters = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT  *  FROM   Meter where xqid=" + xqid + " and cjjid=" + cjjid, null);

        while (c.moveToNext()) {
            PmeterUser m = new PmeterUser();
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

    /**
     * 保存表信息
     */
    public static void saveMeter(int xqid, int cjjid, int meterid, Bitmap pic, Float data, String Date) {

        try {
            if (pic == null) {
                return;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            pic.compress(Bitmap.CompressFormat.JPEG, 100, bos);//无损耗压缩图片为输出流

			/*byte[] picBytes = bos.toByteArray();
            ContentValues cv = new ContentValues();//图片以byte数组字符串形式放入数据库
			cv.put("Pic", picBytes);
//			cv.put("Data", data);
			cv.put("Date", Date);
			String[] args = {String.valueOf(xqid),String.valueOf(cjjid),String.valueOf(meterid)};//将需要更新的数据放入
			db.update("Meter", cv, "Xqid=? and cjjid=? and meterid=?", args);//更新数据表中特定数据*/
            db.beginTransaction();
            try {
                byte[] picBytes = bos.toByteArray();
                ContentValues cv = new ContentValues();//图片以byte数组字符串形式放入数据库
                cv.put("Pic", picBytes);
                //cv.put("Data", data);
                cv.put("Date", Date);
                String[] args = {String.valueOf(xqid), String.valueOf(cjjid), String.valueOf(meterid)};//将需要更新的数据放入
                db.update("Meter", cv, "Xqid=? and cjjid=? and meterid=?", args);//更新数据表中特定数据
                db.setTransactionSuccessful();
                // 设置事务的标志为true，调用此方法会在执行到endTransaction()方法是提交事务，
                // 若没有调用此方法会在执行到endTransaction()方法回滚事务。
            } finally {
                if (bos != null) {
                    bos.close();
                }
                db.endTransaction();

            }
        } catch (Exception e) {
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

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断SD卡是否存在,及是否具有读写的权利
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取SD卡的path
            return sdDir.toString();//将path转化为string类型返回
        } else {
            return null;
        }

    }

    public static void closeDB() {
        db.close();//关闭数据库
    }

}
