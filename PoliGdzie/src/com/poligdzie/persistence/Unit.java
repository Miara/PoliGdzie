package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "unit")
public class Unit {
	@DatabaseField (id = true)
	private int id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String www;
	
	@DatabaseField
	private UnitTypes type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}

	public UnitTypes getType() {
		return type;
	}

	public void setType(UnitTypes type) {
		this.type = type;
	}

	public Unit() {
		super();
	}
}
