package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.interfaces.Nameable;

@DatabaseTable(tableName = "unit")
public class Unit extends PoliGdzieBaseClass implements Nameable
{
	@DatabaseField(generatedId = true)
	private int			id;

	@DatabaseField(index = true)
	private String		name;

	@DatabaseField
	private String		www;

	@DatabaseField
	private UnitTypes	type;

	@DatabaseField(index = true)
	private String		aliases;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "building_id")
	private Building	building;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "room_id")
	private Room		office;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getWww()
	{
		return www;
	}

	public void setWww(String www)
	{
		this.www = www;
	}

	public UnitTypes getType()
	{
		return type;
	}

	public void setType(UnitTypes type)
	{
		this.type = type;
	}

	public Building getBuilding()
	{
		return building;
	}

	public void setBuilding(Building building)
	{
		this.building = building;
	}

	public String getAliases()
	{
		return aliases;
	}

	public void setAliases(String aliases)
	{
		this.aliases = aliases;
	}

	public Room getOffice()
	{
		return office;
	}

	public void setOffice(Room office)
	{
		this.office = office;
	}

	public Unit(String name, String www, UnitTypes type, String aliases,
			Building building)
	{
		this.name = name;
		this.www = www;
		this.type = type;
		this.aliases = aliases;
		this.building = building;
	}

	public Unit()
	{
		// nie wyrzucac!
	}

	public Unit(String name, String www, UnitTypes type, String aliases,
			Building building, Room office)
	{
		this.name = name;
		this.www = www;
		this.type = type;
		this.aliases = aliases;
		this.building = building;
		this.office = office;
	}
}
