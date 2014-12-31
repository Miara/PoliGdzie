package com.poligdzie.persistence;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.interfaces.Imageable;
import com.poligdzie.interfaces.Nameable;

@DatabaseTable(tableName = "building")
public class Building implements Nameable, Imageable {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String name;

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

	@DatabaseField
	private String imageResource;
	
	@DatabaseField
	private String markerImageResource;

	@ForeignCollectionField(eager = true, columnName = "rooms")
	private ForeignCollection<Room> rooms;

	@ForeignCollectionField(eager = true, columnName = "units")
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
		if (value.getClass() == Unit.class) {
			this.units.add((Unit) value);
		}

		if (value.getClass() == Room.class) {
			this.rooms.add((Room) value);
		}
	}

	public Building() {

	}

	public Building(String name, double coordX, double coordY, String address,
			int width, int height, String image, String aliases) {
		this.name = name;
		this.coordX = coordX;
		this.coordY = coordY;
		this.address = address;
		this.width = width;
		this.height = height;
		this.imageResource = image;
		this.aliases = aliases;
	}

	public Building(String name, double coordX, double coordY, String address,
			int width, int height, String image, String aliases,
			ForeignCollection<Room> rooms, ForeignCollection<Unit> units) {
		this.name = name;
		this.coordX = coordX;
		this.coordY = coordY;
		this.address = address;
		this.width = width;
		this.height = height;
		this.imageResource = image;
		this.aliases = aliases;
		this.rooms = rooms;
		this.units = units;
	}

	public String getAliases() {
		return aliases;
	}

	public void setAliases(String aliases) {
		this.aliases = aliases;
	}

	public String getImageResource() {
		return imageResource;
	}

	public void setImageResource(String image) {
		this.imageResource = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMarkerImageResource() {
		return markerImageResource;
	}

	public void setMarkerImageResource(String markerImageResource) {
		this.markerImageResource = markerImageResource;
	}

	public Building(String name, double coordX, double coordY, String address,
			int width, int height, String aliases, String imageResource,
			String markerImageResource) {
		this.name = name;
		this.coordX = coordX;
		this.coordY = coordY;
		this.address = address;
		this.width = width;
		this.height = height;
		this.aliases = aliases;
		this.imageResource = imageResource;
		this.markerImageResource = markerImageResource;
	}

	public Building(double coordX, double coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}

}
