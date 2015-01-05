package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.interfaces.Nameable;

@DatabaseTable(tableName = "floor")
public class Floor extends PoliGdzieBaseClass implements Nameable
{
	@DatabaseField(generatedId = true)
	private int			id;

	@DatabaseField
	private int			number;

	@DatabaseField
	private String		drawableId;

	@DatabaseField
	private String		name;

	@DatabaseField
	private String		tag;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "building_id")
	private Building	building;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getDrawableId()
	{
		return drawableId;
	}

	public void setDrawableId(String drawableId)
	{
		this.drawableId = drawableId;
	}

	public Building getBuilding()
	{
		return building;
	}

	public void setBuilding(Building building)
	{
		this.building = building;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTag()
	{
		return tag;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public Floor()
	{
		// nie wyrzucac!
	}

	public Floor(int number, String drawableId, String name, String tag,
			Building building)
	{
		this.number = number;
		this.drawableId = drawableId;
		this.name = name;
		this.tag = tag;
		this.building = building;
	}

}
