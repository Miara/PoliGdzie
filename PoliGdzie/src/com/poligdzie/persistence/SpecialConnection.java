package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;

@DatabaseTable(tableName = "specialConnection")
public class SpecialConnection extends PoliGdzieBaseClass
{
	@DatabaseField(generatedId = true)
	private int				id;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "specialPointLower_id")
	private NavigationPoint	lowerFloor;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "specialPointUpper_id")
	private NavigationPoint	upperFloor;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public NavigationPoint getLowerPoint()
	{
		return lowerFloor;
	}

	public void setLowerFloor(NavigationPoint lowerFloor)
	{
		this.lowerFloor = lowerFloor;
	}

	public NavigationPoint getUpperPoint()
	{
		return upperFloor;
	}

	public void setUpperFloor(NavigationPoint upperFloor)
	{
		this.upperFloor = upperFloor;
	}

	public SpecialConnection()
	{
		// nie wyrzucac !
	}

	public SpecialConnection(NavigationPoint lower, NavigationPoint upper)
	{
		this.lowerFloor = lower;
		this.upperFloor = upper;
	}
}