package com.seck.hzy.lorameterapp.LoRaApp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Colector {
	
	public static final String FIELD_COLECT_ID = "ColectID";
	public static final String FIELD_COLECT_ADDR = "ColectAddr";
	public static final String FIELD_METER_COUNT = "MeterCount";
	public static final String FIELD_METER_TYPE = "MeterType";
	public static final String FIELD_BUILDING = "BuildingID";
	public static final String FIELD_RESIDENTIAL = "ResidentialQuarterID";
	
	@DatabaseField(generatedId = true, columnName = "_id")
	int id;

	@DatabaseField
	public String ColectID;
	
	@DatabaseField
	public String ColectAddr;
	
	@DatabaseField
	public Integer MeterCount;
	
	@DatabaseField
	public String MeterType;
	
	@DatabaseField
	public Integer BuildingID;
	
	@DatabaseField
	public Integer ResidentialQuarterID;
	
	public void setField(String name, String value) {
		try {
			if (FIELD_COLECT_ID.equalsIgnoreCase(name)) {
				ColectID = value;
			} else if (FIELD_COLECT_ADDR.equalsIgnoreCase(name)) {
				ColectAddr = value;
			} else if (FIELD_METER_COUNT.equalsIgnoreCase(name)) {
				MeterCount = Integer.valueOf(value);
			} else if (FIELD_METER_TYPE.equalsIgnoreCase(name)) {
				MeterType = value;
			} else if (FIELD_BUILDING.equalsIgnoreCase(name)) {
				BuildingID = Integer.valueOf(value);
			} else if (FIELD_RESIDENTIAL.equalsIgnoreCase(name)) {
				ResidentialQuarterID = Integer.valueOf(value);
			}
		} catch (Exception e) {
		}
	}
}
