package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;

@DatabaseTable(tableName = "specialConnection")
public class SpecialConnection extends PoliGdzieBaseClass
{
	@DatabaseField(generatedId = true)
	private int			id;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "specialPointLower_id")
	private SpecialPoint	lowerFloor;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "specialPointUpper_id")
	private SpecialPoint	upperFloor;


	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}


	public SpecialPoint getLowerFloor()
	{
		return lowerFloor;
	}

	public void setLowerFloor(SpecialPoint lowerFloor)
	{
		this.lowerFloor = lowerFloor;
	}

	public SpecialPoint getUpperFloor()
	{
		return upperFloor;
	}

	public void setUpperFloor(SpecialPoint upperFloor)
	{
		this.upperFloor = upperFloor;
	}

	public  SpecialConnection()
	{
		// nie wyrzucac !
	}

	public SpecialConnection(SpecialPoint lower, SpecialPoint upper)
	{
		this.lowerFloor = lower;
		this.upperFloor = upper;
	}
}