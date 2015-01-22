package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.interfaces.Nameable;

@DatabaseTable(tableName = "room")
public class Room extends PoliGdzieBaseClass implements Nameable
{
	@DatabaseField(generatedId = true)
	private int						id;

	@DatabaseField(index = true)
	private String					number;

	@DatabaseField(index = true)
	private String					name;

	@DatabaseField
	private RoomFunctions			function;

	@DatabaseField
	private int						coordX;

	@DatabaseField
	private int						coordY;

	@DatabaseField
	private int						radius;

	@DatabaseField
	private int						doorsX;

	@DatabaseField
	private int						doorsY;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "floor_id")
	private Floor					floor;

	@DatabaseField(index = true)
	private String					aliases;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "building_id")
	private Building				building;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "navigationPointConnection_id")
	private NavigationConnection	navigationConnection;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public RoomFunctions getFunction()
	{
		return function;
	}

	public void setFunction(RoomFunctions function)
	{
		this.function = function;
	}

	public int getCoordX()
	{
		return coordX;
	}

	public void setCoordX(int coordX)
	{
		this.coordX = coordX;
	}

	public int getCoordY()
	{
		return coordY;
	}

	public void setCoordY(int coordY)
	{
		this.coordY = coordY;
	}

	public int getRadius()
	{
		return radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
	}

	public int getDoorsX()
	{
		return doorsX;
	}

	public void setDoorsX(int doorsX)
	{
		this.doorsX = doorsX;
	}

	public int getDoorsY()
	{
		return doorsY;
	}

	public void setDoorsY(int doorsY)
	{
		this.doorsY = doorsY;
	}

	public Floor getFloor()
	{
		return floor;
	}

	public void setFloor(Floor floor)
	{
		this.floor = floor;
	}

	public String getAliases()
	{
		return aliases;
	}

	public void setAliases(String aliases)
	{
		this.aliases = aliases;
	}

	public Building getBuilding()
	{
		return building;
	}

	public void setBuilding(Building building)
	{
		this.building = building;
	}

	public NavigationConnection getNavigationConnection()
	{
		return navigationConnection;
	}

	public void setNavigationConnection(
			NavigationConnection navigationConnection)
	{
		this.navigationConnection = navigationConnection;
	}

	public Room(String number, String name, RoomFunctions function,
			Floor floor, int coordX, int coordY, int radius, int doorsX,
			int doorsY, NavigationConnection navigationConnection,
			String aliases)
	{
		this.number = number;
		this.name = name;
		this.function = function;
		this.coordX = coordX;
		this.coordY = coordY;
		this.radius = radius;
		this.doorsX = doorsX;
		this.doorsY = doorsY;
		this.floor = floor;
		this.aliases = aliases;
		this.building = floor.getBuilding();
		this.navigationConnection = navigationConnection;
	}

	public Room()
	{
		// nie wyrzucac!
	}

}
