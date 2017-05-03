package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.seck.hzy.lorameterapp.LoRaApp.model.Building;
import com.seck.hzy.lorameterapp.LoRaApp.model.Colector;
import com.seck.hzy.lorameterapp.LoRaApp.model.Meter;
import com.seck.hzy.lorameterapp.LoRaApp.model.Residential;

import java.sql.SQLException;

/**
 *
 * Za，Zb数据库操作的Helper类。
 *
 * @author l412
 *
 */
public class MA_SQLiteHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "sk_meter.db";
	private static final int DATABASE_VERSION = 3;

	private static MA_SQLiteHelper databaseHelper;

	public MA_SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static MA_SQLiteHelper getInstance(Context context) {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context, MA_SQLiteHelper.class);
		}
		return databaseHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(MA_SQLiteHelper.class.getName(), "onCreate");

			// not drop if table not exist
			TableUtils.createTableIfNotExists(connectionSource, Residential.class);
			TableUtils.createTableIfNotExists(connectionSource, Building.class);
			TableUtils.createTableIfNotExists(connectionSource, Colector.class);
			TableUtils.createTableIfNotExists(connectionSource, Meter.class);

		} catch (SQLException e) {
			Log.e(MA_SQLiteHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
						  int oldVersion, int newVersion) {
		try {
			Log.i(MA_SQLiteHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Residential.class, true);
			TableUtils.dropTable(connectionSource, Building.class, true);
			TableUtils.dropTable(connectionSource, Colector.class, true);
			TableUtils.dropTable(connectionSource, Meter.class, true);

			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(MA_SQLiteHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
		super.close();
	}
}
