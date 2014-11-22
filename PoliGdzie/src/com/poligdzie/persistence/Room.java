package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "room") 
public class Room {
	@DatabaseField (id = true)
	private int id;
	
	@DatabaseField
	private int number;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private RoomFunctions function;
	
	@DatabaseField
	private int coordX;
	
	@DatabaseField
	private int coordY;
	
	@DatabaseField
	private int floorNr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RoomFunctions getFunction() {
		return function;
	}

	public void setFunction(RoomFunctions function) {
		this.function = function;
	}

	public int getCoordX() {
		return coordX;
	}

	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

	public int getCoordY() {
		return coordY;
	}

	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}

	public int getFloorNr() {
		return floorNr;
	}

	public void setFloorNr(int floorNr) {
		this.floorNr = floorNr;
	}

	public Room() {
		super();
	}
}
