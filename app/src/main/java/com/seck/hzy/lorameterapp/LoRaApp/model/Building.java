package com.seck.hzy.lorameterapp.LoRaApp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Building {

	public static final String FIELD_BUILDING_ID = "BuildingID";
	public static final String FIELD_BUILDING_NAME = "BuildingName";
	public static final String FIELD_BUILDING_ADDR = "BuildingAddr";
	public static final String FIELD_RESIDENTIAL = "ResidentialQuarterID";
	
	@DatabaseField(generatedId = true, columnName = "_id")
	int id;

	@DatabaseField
	public Integer BuildingID;
	
	@DatabaseField
	public String BuildingName;
	
	@DatabaseField
	public String BuildingAddr;
	
	@DatabaseField
	public Integer ResidentialQuarterID;
	
	public void setField(String name, String value) {
		try {
			if (FIELD_BUILDING_ID.equalsIgnoreCase(name)) {
				BuildingID = Integer.valueOf(value);
			} else if (FIELD_BUILDING_NAME.equalsIgnoreCase(name)) {
				BuildingName = value;
			} else if (FIELD_BUILDING_ADDR.equalsIgnoreCase(name)) {
				BuildingAddr = value;
			} else if (FIELD_RESIDENTIAL.equalsIgnoreCase(name)) {
				ResidentialQuarterID = Integer.valueOf(value);
			}
		} catch (Exception e) {
		}
	}
}
