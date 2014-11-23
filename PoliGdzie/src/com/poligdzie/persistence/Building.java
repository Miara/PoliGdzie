package com.poligdzie.persistence;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "building")
public class Building {
	@DatabaseField (generatedId = true)
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
	
	@ForeignCollectionField 
	private ForeignCollection <Room> rooms;
	
	@ForeignCollectionField
	private ForeignCollection <Unit> units;
	
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
	public Building() {
		super();
	}
}
