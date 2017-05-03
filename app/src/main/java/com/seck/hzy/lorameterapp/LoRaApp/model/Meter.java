package com.seck.hzy.lorameterapp.LoRaApp.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Field;
import java.util.ArrayList;

@DatabaseTable
public class Meter {

	public static final String FIELD_RESIDENTIAL = "ResidentialQuarterID";
	public static final String FIELD_BUILDING = "BuildingID";
	public static final String FIELD_COLECTOR = "ColectID";
	public static final String FIELD_METER_ID = "MeterID";
	public static final String FIELD_SERIAL = "Serial";
	public static final String FIELD_PAYNO = "PayNo";
	public static final String FIELD_NAME = "Name";
	public static final String FIELD_ADDRESS = "Address";
	public static final String FIELD_LAST_DATA = "LastData";
	public static final String FIELD_LAST_DATA_TIME = "LastDataTime";
	public static final String FIELD_LAST_QUANTITY = "LastQuantity";
	public static final String FIELD_LAST_FEE = "lastFee";
	public static final String FIELD_PRICE_TYPE = "PriceType";
	public static final String FIELD_OWE_DATE = "OweDate";
	public static final String FIELD_OWE_MONS = "OweMons";
	public static final String FIELD_OWE_FEE = "OweFee";
	public static final String FIELD_PRECISION = "Precision";
	public static final String FIELD_FIX_QUANTITY = "FixQuantity";
	public static final String FIELD_METER_TYPE = "metertype";
	public static final String FIELD_DATA = "Data";
	public static final String FIELD_DATA_TIME = "DataTime";
	public static final String FIELD_ISFAULT = "IsFault";
	public static final String FIELD_ISBINARYZATION = "IsBinaryzation";
	public static final String FIELD_IMAGES = "Images";
	public static final String FIELD_RECOGNITION_RESULT = "RecognitionResult";
	public static final String FIELD_WATER = "WATER";
	public static final String FIELD_FEE = "FEE";
	public static final String FIELD_ISPRINT = "ISPRINT";
	public static final String FIELD_STATE = "state";

	@DatabaseField(generatedId = true, columnName = "_id")
	public int id;

	/// 小区Id
	@DatabaseField
	public int ResidentialQuarterID;

	/// 楼宇id
	@DatabaseField
	public int BuildingID;

	/// 采集机id
	@DatabaseField
	public String ColectID;

	/// Char(17) 4位小区号，4位楼宇 ，2位通信机 2为采集机，2位端口 3位表序号
	@DatabaseField
	public String MeterID;

	/// 直读表号
	@DatabaseField
	public String Serial;

	/// 收费编号
	@DatabaseField
	public String PayNo;

	/// 姓名
	@DatabaseField
	public String Name;

	/// 地址
	@DatabaseField
	public String Address;

	/// 上次抄见
	@DatabaseField
	public float LastData;

	/// 上次抄表日期
	@DatabaseField
	public String LastDataTime;

	/// 上次用水量
	@DatabaseField
	public Double LastQuantity;

	/// 上次收费金额
	@DatabaseField
	public Double lastFee;

	/// 收费类型
	@DatabaseField
	public int PriceType;

	/// 欠费起始日期
	@DatabaseField
	public String OweDate;

	/// 欠费月份
	@DatabaseField
	public int OweMons;

	/// 欠费金额
	@DatabaseField
	public double OweFee;

	/// 水表精度
	@DatabaseField
	public double Precision;

	/// 改表后的修正水量
	@DatabaseField
	public double FixQuantity;

	/// 水表状态‘0’普通表‘1’制度表‘2’摄像表
	@DatabaseField
	public char metertype;

	/// 抄表数据
	@DatabaseField
	public float Data;

	/// 抄表时间
	@DatabaseField
	public String DataTime;

	///   抄表是否正常标志 true 异常  false 正常
	@DatabaseField
	public boolean IsFault;

	///  true:为二值化图像; false:灰度图片
	@DatabaseField
	public boolean IsBinaryzation;

	/// 图片字节数据集合 集合的第0个 对应 实际水表字轮的最末（右）位
	@DatabaseField(dataType= DataType.SERIALIZABLE)
	public ArrayList<byte[]> Images;

	/// 读实时数据返回  识别 结果和 置信度等
	@DatabaseField(dataType= DataType.BYTE_ARRAY)
	public byte[] RecognitionResult;

	/// 本月用水
	@DatabaseField
	public float WATER;

	/// 收费金额
	@DatabaseField
	public float FEE;

	/// 是否已经打印收费凭条
	@DatabaseField
	public boolean ISPRINT;

	@DatabaseField
	public int state;

	public void setField(String name, String value) {
		try {
			if (FIELD_RESIDENTIAL.equalsIgnoreCase(name)) {
				ResidentialQuarterID = Integer.valueOf(value);
			} else if (FIELD_BUILDING.equalsIgnoreCase(name)) {
				BuildingID = Integer.valueOf(value);
			} else if (FIELD_METER_ID.equalsIgnoreCase(name)) {
				MeterID = value;
				ColectID = MeterID.substring(8, 14);
			} else if (FIELD_SERIAL.equalsIgnoreCase(name)) {
				Serial = value;
			} else if (FIELD_PAYNO.equalsIgnoreCase(name)) {
				PayNo = value;
			} else if (FIELD_NAME.equalsIgnoreCase(name)) {
				Name = value;
			} else if (FIELD_ADDRESS.equalsIgnoreCase(name)) {
				Address = value;
			} else if (FIELD_LAST_DATA.equalsIgnoreCase(name)) {
				LastData = Float.valueOf(value);
			} else if (FIELD_LAST_DATA_TIME.equalsIgnoreCase(name)) {
				LastDataTime = value;
			} else if (FIELD_LAST_QUANTITY.equalsIgnoreCase(name)) {
				LastQuantity = Double.valueOf(value);
			} else if (FIELD_LAST_FEE.equalsIgnoreCase(name)) {
				lastFee = Double.valueOf(value);
			} else if (FIELD_PRICE_TYPE.equalsIgnoreCase(name)) {
				PriceType = Integer.valueOf(value);
			} else if (FIELD_OWE_DATE.equalsIgnoreCase(name)) {
				OweDate = value;
			} else if (FIELD_OWE_MONS.equalsIgnoreCase(name)) {
				OweMons = Integer.valueOf(value);
			} else if (FIELD_OWE_FEE.equalsIgnoreCase(name)) {
				OweFee = Double.valueOf(value);
			} else if (FIELD_PRECISION.equalsIgnoreCase(name)) {
				Precision = Double.valueOf(value);
			} else if (FIELD_FIX_QUANTITY.equalsIgnoreCase(name)) {
				FixQuantity = Double.valueOf(value);
			} else if (FIELD_METER_TYPE.equalsIgnoreCase(name)) {
				metertype = value.trim().charAt(0);
			}
		} catch (Exception e) {
		}
	}

	public String getField(String name) {
		if(FIELD_IMAGES.equalsIgnoreCase(name)) {
			return null;
		}

		try {
			Field field = Meter.class.getDeclaredField(name);
			field.setAccessible(true);
			Object value = field.get(this);
			if(value == null) {
				return null;
			}

			return value.toString();
		} catch (Exception e) {
		}

		return null;
	}
}