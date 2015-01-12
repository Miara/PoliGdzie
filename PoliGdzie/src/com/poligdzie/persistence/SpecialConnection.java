package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "specialConnection")
public class SpecialConnection 
{
	@DatabaseField(generatedId = true)
	private int			id;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "specialPointLower_id")
	private NavigationPoint	lowerFloor;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "specialPointUpper_id")
	private NavigationPoint	upperFloor;


	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}


	public NavigationPoint getLowerFloor()
	{
		return lowerFloor;
	}

	public void setLowerFloor(NavigationPoint lowerFloor)
	{
		this.lowerFloor = lowerFloor;
	}

	public NavigationPoint getUpperFloor()
	{
		return upperFloor;
	}

	public void setUpperFloor(NavigationPoint upperFloor)
	{
		this.upperFloor = upperFloor;
	}

	public  SpecialConnection()
	{
		// nie wyrzucac !
	}

	public SpecialConnection(NavigationPoint lower, NavigationPoint upper)
	{
		this.lowerFloor = lower;
		this.upperFloor = upper;
	}
}