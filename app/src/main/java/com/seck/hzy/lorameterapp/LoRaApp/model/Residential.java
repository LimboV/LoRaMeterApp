package com.seck.hzy.lorameterapp.LoRaApp.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Residential {

	public static final String FIELD_RESIDENTIAL_ID = "ResidentialQuarterID";
	public static final String FIELD_QUARTER_NAME = "QuarterName";

	@DatabaseField(generatedId = true, columnName = "_id")
	int id;

	@DatabaseField
	public Integer ResidentialQuarterID;

	@DatabaseField
	public String QuarterName;

	@Override
	public String toString() {
		return QuarterName;
	}

	public void setField(String name, String value) {
		try {
			if (FIELD_RESIDENTIAL_ID.equalsIgnoreCase(name)) {
				ResidentialQuarterID = Integer.valueOf(value);
			} else if (FIELD_QUARTER_NAME.equalsIgnoreCase(name)) {
				QuarterName = value;
			}
		} catch (Exception e) {
		}
	}
}
