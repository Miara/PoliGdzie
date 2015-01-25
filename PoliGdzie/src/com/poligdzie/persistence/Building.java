package com.poligdzie.persistence;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.interfaces.Imageable;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.interfaces.WithCoordinates;

@DatabaseTable(tableName = "building")
public class Building extends PoliGdzieBaseClass implements Nameable,
												Imageable, WithCoordinates
{
	@DatabaseField(id = true)
	private int							id;

	@DatabaseField(index = true)
	private String						name;

	@DatabaseField
	private double						coordX;

	@DatabaseField
	private double						coordY;

	@DatabaseField
	private String						address;

	@DatabaseField
	private int							width;

	@DatabaseField
	private int							height;

	@DatabaseField(index = true)
	private String						aliases;

	@DatabaseField
	private String						imageResource;

	@DatabaseField
	private String						markerImageResource;

	@ForeignCollectionField(columnName = "rooms")
	private ForeignCollection<Room>		rooms;

	@ForeignCollectionField(columnName = "units")
	private ForeignCollection<Unit>		units;

	@ForeignCollectionField(columnName = "floors")
	private ForeignCollection<Floor>	floors;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public double getCoordX()
	{
		return coordX;
	}

	public void setCoordX(double coordX)
	{
		this.coordX = coordX;
	}

	public double getCoordY()
	{
		return coordY;
	}

	public void setCoordY(double coordY)
	{
		this.coordY = coordY;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public ForeignCollection<Room> getRooms()
	{
		return rooms;
	}

	public void setRooms(ForeignCollection<Room> rooms)
	{
		this.rooms = rooms;
	}

	public ForeignCollection<Unit> getUnits()
	{
		return units;
	}

	public void setUnits(ForeignCollection<Unit> units)
	{
		this.units = units;
	}

	public void add(Object value)
	{
		if (value.getClass() == Unit.class)
		{
			this.units.add((Unit) value);
		}

		if (value.getClass() == Room.class)
		{
			this.rooms.add((Room) value);
		}
	}

	public String getAliases()
	{
		return aliases;
	}

	public void setAliases(String aliases)
	{
		this.aliases = aliases;
	}

	public String getImageResource()
	{
		return imageResource;
	}

	public void setImageResource(String image)
	{
		this.imageResource = image;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMarkerImageResource()
	{
		return markerImageResource;
	}

	public void setMarkerImageResource(String markerImageResource)
	{
		this.markerImageResource = markerImageResource;
	}

	public ForeignCollection<Floor> getFloors()
	{
		return floors;
	}

	public void setFloors(ForeignCollection<Floor> floors)
	{
		this.floors = floors;
	}

	public Building(String name, String address, double coordX, double coordY,
			int width, int height, String aliases, String imageResource,
			String markerImageResource)
	{
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

	public Building(double coordX, double coordY)
	{
		this.coordX = coordX;
		this.coordY = coordY;
	}

	public Building()
	{
		// nie wyrzucac!
	}
}
