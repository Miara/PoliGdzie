package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.interfaces.Nameable;

@DatabaseTable(tableName = "room")
public class Room implements Nameable{
	@DatabaseField(generatedId = true)
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

	@DatabaseField
	private String aliases;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "building_id")
	private Building building;

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

	}

	public Room(int number, String name, RoomFunctions function, int coordX,
			int coordY, int floorNr, Building building) {
		this.number = number;
		this.name = name;
		this.function = function;
		this.coordX = coordX;
		this.coordY = coordY;
		this.floorNr = floorNr;
		this.building = building;
	}

	public String getAliases() {
		return aliases;
	}

	public void setAliases(String aliases) {
		this.aliases = aliases;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Room(int number, String name, RoomFunctions function,
			int coordX, int coordY, int floorNr, String aliases,
			Building building) {
		this.number = number;
		this.name = name;
		this.function = function;
		this.coordX = coordX;
		this.coordY = coordY;
		this.floorNr = floorNr;
		this.aliases = aliases;
		this.building = building;
	}
}
