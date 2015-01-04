package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "floor")
public class Floor {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private int number;
	
	@DatabaseField
	private int drawableId;
	
	@DatabaseField
	private String name;

	@DatabaseField
	private String tag;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "building_id")
	private Building building;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Floor(int number, int drawableId, Building building) {
		this.number = number;
		this.drawableId = drawableId;
		this.building = building;
	}

	public Floor() {
	}

	public Floor(int number, int drawableId, String name, Building building) {
		this.number = number;
		this.drawableId = drawableId;
		this.name = name;
		this.building = building;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Floor(int number, int drawableId, String name, String tag,
			Building building) {
		this.number = number;
		this.drawableId = drawableId;
		this.name = name;
		this.tag = tag;
		this.building = building;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
}
