package com.poligdzie.persistence;

import android.util.Log;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "building")
public class Building {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private double coordX;

	@DatabaseField
	private double coordY;

	@DatabaseField
	private String address;

	@DatabaseField
	private int width;

	@DatabaseField
	private int height;

	@DatabaseField
	private String aliases;
	
	@ForeignCollectionField (eager = true, columnName = "rooms")
	private ForeignCollection<Room> rooms;

	@ForeignCollectionField (eager = true, columnName = "units")
	private ForeignCollection<Unit> units;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getCoordX() {
		return coordX;
	}

	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public double getCoordY() {
		return coordY;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ForeignCollection<Room> getRooms() {
		return rooms;
	}

	public void setRooms(ForeignCollection<Room> rooms) {
		this.rooms = rooms;
	}

	public ForeignCollection<Unit> getUnits() {
		return units;
	}

	public void setUnits(ForeignCollection<Unit> units) {
		this.units = units;
	}

	public void add(Object value) {
		if(value.getClass() == Unit.class) {
			this.units.add((Unit) value);
		}
		
		if(value.getClass() == Room.class) {
			this.rooms.add((Room) value);
		}
	}
	
	public Building() {

	}

	
	public Building(double coordX, double coordY, String address,
			int width, int height) {
		this.coordX = coordX;
		this.coordY = coordY;
		this.address = address;
		this.width = width;
		this.height = height;
	}

	public Building(double coordX, double coordY, String address,
			int width, int height, ForeignCollection<Room> rooms,
			ForeignCollection<Unit> units) {
		this.coordX = coordX;
		this.coordY = coordY;
		this.address = address;
		this.width = width;
		this.height = height;
		this.rooms = rooms;
		this.units = units;
	}

	public String getAliases() {
		return aliases;
	}

	public void setAliases(String aliases) {
		this.aliases = aliases;
	}

	

	
}
