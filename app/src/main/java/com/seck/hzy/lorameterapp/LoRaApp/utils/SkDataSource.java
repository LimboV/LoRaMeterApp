package com.seck.hzy.lorameterapp.LoRaApp.utils;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.seck.hzy.lorameterapp.LoRaApp.lora_activity.MenuActivity;
import com.seck.hzy.lorameterapp.LoRaApp.model.Building;
import com.seck.hzy.lorameterapp.LoRaApp.model.Colector;
import com.seck.hzy.lorameterapp.LoRaApp.model.Meter;
import com.seck.hzy.lorameterapp.LoRaApp.model.Residential;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * SkDataSource 所有数据库操作被封装在该类中。
 *
 * @author Guhong.
 *
 */
public class SkDataSource {

	public static final String[] SB_EXPORT_COLUMNS = { Meter.FIELD_RESIDENTIAL,
			Meter.FIELD_BUILDING, Meter.FIELD_COLECTOR, Meter.FIELD_METER_ID,
			Meter.FIELD_SERIAL, Meter.FIELD_PAYNO, Meter.FIELD_NAME,
			Meter.FIELD_ADDRESS, Meter.FIELD_LAST_DATA,
			Meter.FIELD_LAST_DATA_TIME, Meter.FIELD_LAST_QUANTITY,
			Meter.FIELD_LAST_FEE, Meter.FIELD_PRICE_TYPE, Meter.FIELD_OWE_DATE,
			Meter.FIELD_OWE_MONS, Meter.FIELD_OWE_FEE, Meter.FIELD_PRECISION,
			Meter.FIELD_FIX_QUANTITY, Meter.FIELD_METER_TYPE, Meter.FIELD_DATA,
			Meter.FIELD_DATA_TIME, Meter.FIELD_ISFAULT,
			Meter.FIELD_ISBINARYZATION, Meter.FIELD_IMAGES,
			Meter.FIELD_RECOGNITION_RESULT, Meter.FIELD_WATER, Meter.FIELD_FEE,
			Meter.FIELD_ISPRINT };

	private MA_SQLiteHelper dbHelper;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public SkDataSource(Context context) {
		dbHelper = MA_SQLiteHelper.getInstance(context);
	}

	public void clearData() {
		ConnectionSource connectionSource = dbHelper.getConnectionSource();
		try {
			TableUtils.dropTable(connectionSource, Residential.class, true);
			TableUtils.dropTable(connectionSource, Building.class, true);
			TableUtils.dropTable(connectionSource, Colector.class, true);
			TableUtils.dropTable(connectionSource, Meter.class, true);

			TableUtils.createTableIfNotExists(connectionSource, Residential.class);
			TableUtils.createTableIfNotExists(connectionSource, Building.class);
			TableUtils.createTableIfNotExists(connectionSource, Colector.class);
			TableUtils.createTableIfNotExists(connectionSource, Meter.class);
		} catch (SQLException e) {
			Log.e(MA_SQLiteHelper.class.getName(), "Can't reset databases", e);
			throw new RuntimeException(e);
		}
	}

	public void deleteResidentials() {
		ConnectionSource connectionSource = dbHelper.getConnectionSource();
		try {
			TableUtils.dropTable(connectionSource, Residential.class, true);
			TableUtils.createTableIfNotExists(connectionSource, Residential.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void saveResidentials(final List<Residential> residentials) {
		final RuntimeExceptionDao<Residential, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Residential.class);
		dao.callBatchTasks(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				for(Residential data : residentials) {
					dao.createOrUpdate(data);
				}
				return null;
			}
		});
	}

	public void insertResidential(Residential residential) {
		RuntimeExceptionDao<Residential, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Residential.class);
		dao.createOrUpdate(residential);
		dao = null;
	}

	/**
	 *
	 * 获得小区编号列表
	 *
	 * Guhong. 2012.09.22.
	 *
	 * @return
	 */
	public List<Residential> getResidentials() {
		RuntimeExceptionDao<Residential, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Residential.class);
		return dao.queryForAll();
	}

	public void deleteBuildings() {
		ConnectionSource connectionSource = dbHelper.getConnectionSource();
		try {
			TableUtils.dropTable(connectionSource, Building.class, true);
			TableUtils.createTableIfNotExists(connectionSource, Building.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void saveBuildings(final List<Building> buildings) {
		final RuntimeExceptionDao<Building, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Building.class);
		dao.callBatchTasks(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				for(Building data : buildings) {
					dao.createOrUpdate(data);
				}
				return null;
			}
		});
	}

	public void insertBuilding(Building building) {
		RuntimeExceptionDao<Building, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Building.class);
		dao.createOrUpdate(building);
		dao = null;
	}


	/**
	 * 获取楼宇号列表
	 */
	public List<Building> getBuildings(Integer residentialId) {
		RuntimeExceptionDao<Building, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Building.class);
		return dao.queryForEq(Building.FIELD_RESIDENTIAL, residentialId);
	}

	public void deleteColectors() {
		ConnectionSource connectionSource = dbHelper.getConnectionSource();
		try {
			TableUtils.dropTable(connectionSource, Colector.class, true);
			TableUtils.createTableIfNotExists(connectionSource, Colector.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void saveColectors(final List<Colector> colectors) {
		final RuntimeExceptionDao<Colector, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Colector.class);
		dao.callBatchTasks(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				for(Colector data : colectors) {
					dao.createOrUpdate(data);
				}
				return null;
			}
		});
	}

	public void insertColector(Colector colector) {
		RuntimeExceptionDao<Colector, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Colector.class);
		dao.createOrUpdate(colector);
		dao = null;
	}

	/**
	 *
	 * 获得小区采集机编号
	 *
	 * Guhong. 2012.09.22.
	 *
	 * @return
	 */
	public List<Colector> getColectors(Integer residentialId, Integer buildingId) {
		RuntimeExceptionDao<Colector, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Colector.class);
		Colector match = new Colector();
		match.ResidentialQuarterID = residentialId;
		match.BuildingID = buildingId;
		return dao.queryForMatching(match);
	}

	public void deleteMeters() {
		ConnectionSource connectionSource = dbHelper.getConnectionSource();
		try {
			TableUtils.dropTable(connectionSource, Meter.class, true);
			TableUtils.createTableIfNotExists(connectionSource, Meter.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void saveMeters(final List<Meter> meters) {
		final RuntimeExceptionDao<Meter, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Meter.class);
		dao.callBatchTasks(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				for(Meter data : meters) {
					dao.createOrUpdate(data);
				}
				return null;
			}
		});
	}

	public void insertMeter(Meter meter) {
		RuntimeExceptionDao<Meter, Integer> dao =
				dbHelper.getRuntimeExceptionDao(Meter.class);
		dao.createOrUpdate(meter);
	}

	/**
	 *  获取水表数据
	 * @param _id
	 * @return
	 */
	public Meter getMeter(Integer _id) {
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		return dao.queryForId(_id);
	}

	/**
	 *
	 * getSBCursor 获得小区水表某采集机下的所有水表项。
	 *
	 * @return
	 */
	public List<Meter> getMeters(Integer residentialId, Integer buildingId, String colectId) {
		List<Meter> result = new ArrayList<Meter>();
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		QueryBuilder<Meter, Integer> qb = dao.queryBuilder();
		try {
			qb.where().eq(Meter.FIELD_RESIDENTIAL, residentialId).and()
					.eq(Meter.FIELD_BUILDING, buildingId).and()
					.eq(Meter.FIELD_COLECTOR, colectId);
			result.addAll(dao.query(qb.prepare()));
		} catch (SQLException e) {
		}

		return result;
	}

	/**
	 *
	 * getSBCursor 获得小区水表某采集机下的所有异常水表项。
	 *
	 * @return
	 */
	public List<Meter> getMeterErrors(Integer residentialId, Integer buildingId, String colectId) {
		List<Meter> result = new ArrayList<Meter>();
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		QueryBuilder<Meter, Integer> qb = dao.queryBuilder();
		try {
			qb.where().eq(Meter.FIELD_RESIDENTIAL, residentialId).and()
					.eq(Meter.FIELD_BUILDING, buildingId).and()
					.eq(Meter.FIELD_ISFAULT, true).and()
					.eq(Meter.FIELD_COLECTOR, colectId);
			result.addAll(dao.query(qb.prepare()));
		} catch (SQLException e) {
			// TODO: handle exception
		}

		return result;
	}

	public List<Meter> getAllValidMeters() {
		List<Meter> result = new ArrayList<Meter>();
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		QueryBuilder<Meter, Integer> qb = dao.queryBuilder();
		try {
			qb.where().eq(Meter.FIELD_STATE, 1);
			//qb.limit(5L);
			result.addAll(dao.query(qb.prepare()));
		} catch (SQLException e) {
			// TODO: handle exception
		}
		return result;
	}

	public List<Meter> getAllUploadedMeters() {
		List<Meter> result = new ArrayList<Meter>();
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		QueryBuilder<Meter, Integer> qb = dao.queryBuilder();
		try {
			qb.where().eq(Meter.FIELD_STATE, 2);
			result.addAll(dao.query(qb.prepare()));
		} catch (SQLException e) {
			// TODO: handle exception
		}
		return result;
	}

	public List<Meter> getAllMeters() {
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		return dao.queryForAll();
	}

	public void updateMeterStatusUploaded(Integer residentialId, Integer buildingId, String colectId, Boolean fault) {
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		UpdateBuilder<Meter, Integer> ub = dao.updateBuilder();
		try {
			ub.updateColumnValue(Meter.FIELD_STATE, 2);
			ub.where().eq(Meter.FIELD_RESIDENTIAL, residentialId).and()
					.eq(Meter.FIELD_BUILDING, buildingId).and()
					.eq(Meter.FIELD_ISFAULT, fault).and()
					.eq(Meter.FIELD_COLECTOR, colectId).and()
					.eq(Meter.FIELD_STATE, 1);
			ub.update();
		} catch (SQLException e) {

		}
	}

	public void updateAllMeterStatusUploaded() {
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		UpdateBuilder<Meter, Integer> ub = dao.updateBuilder();
		try {
			ub.updateColumnValue(Meter.FIELD_STATE, 2);
			ub.where().eq(Meter.FIELD_STATE, 1);
			ub.update();
		} catch (SQLException e) {

		}
	}

	public int updateMeterRecord(Meter meter) {
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		return dao.update(meter);
	}

	/**
	 *
	 * 更新B表的某项记录，并返回RecordB。
	 */
	public Meter updateMeterData(Integer _id) throws IOException {
		RuntimeExceptionDao<Meter, Integer> dao = dbHelper
				.getRuntimeExceptionDao(Meter.class);
		Meter meter = dao.queryForId(_id);

		if (meter == null)
			return null;

		byte addr[] = MenuActivity.netThread.getSBAddr(meter.Serial);

		try {
			BluetoothConnectThread.SXInfo info;
			float bccj;
			if(meter.metertype == '2') {
				info = MenuActivity.netThread.readSXMeter(addr);
				bccj = info.cflow;
			} else {
				info = new BluetoothConnectThread.SXInfo();
				info.cflow = MenuActivity.netThread.readMeterCFlow(addr, 1);
				bccj = info.cflow / 100.0f;
			}

			// Right process
			//float bccj = info.cflow / 100.0f;

			float sycj = 0;
			try {
				sycj = meter.LastData;
			} catch (NumberFormatException e) {

			}

			float byys = bccj - sycj;

			// Update database
			meter.Data = bccj;
			meter.WATER = byys;
			meter.IsFault = false;
			meter.DataTime = formatter.format(new Date());
			meter.RecognitionResult = info.recog_belief;
			meter.state = 1;
			dao.update(meter);

			// Requery
			//Meter result = dao.queryForId(_id);
			//if (result == null)
			//	return null;

			return meter;
		} catch (NetException e1) {
			// Error process
			final String SBZT_s = e1.zt+"";

			// Update database
			meter.Data = meter.LastData;
			meter.WATER = 0;
			meter.IsFault = true;
			meter.DataTime = formatter.format(new Date());
			meter.state = 1;
			dao.update(meter);

			// Requery
			//Meter result = dao.queryForId(_id);
			//if (result == null)
			//	return null;

			return meter;
		}
	}
}
