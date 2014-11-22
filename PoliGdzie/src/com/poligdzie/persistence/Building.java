package com.poligdzie.persistence;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "building")
public class Building {
	@DatabaseField (id = true)
	private int id;
	
	@DatabaseField
	private LatLng coordinates;
	
	@DatabaseField
	private String address;
	
	@DatabaseField
	private int width;
	
	@DatabaseField
	private int height;
	
	@DatabaseField (foreign = true)
	private ArrayList <Room> rooms;
	
	@DatabaseField (foreign = true)
	private ArrayList <Unit> units;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LatLng getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(LatLng coordinates) {
		this.coordinates = coordinates;
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
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}
	public ArrayList<Unit> getUnits() {
		return units;
	}
	public void setUnits(ArrayList<Unit> units) {
		this.units = units;
	}
	public Building() {
		super();
	}
}
