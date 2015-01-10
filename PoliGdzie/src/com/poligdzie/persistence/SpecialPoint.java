package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;

@DatabaseTable(tableName = "specialPoint")
public class SpecialPoint extends PoliGdzieBaseClass
{
	@DatabaseField(generatedId = true)
	private int			id;
	
	@DatabaseField
	private int	coordX;
	
	@DatabaseField
	private int	coordY;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "floor_id")
	private Floor	floor;


	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
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

	public Floor getFloor()
	{
		return floor;
	}

	public void setFloor(Floor floor)
	{
		this.floor = floor;
	}

	public  SpecialPoint()
	{
		// nie wyrzucac !
	}

	public SpecialPoint(int coordX, int coordY, Floor floor)
	{
		this.coordX = coordX;
		this.coordY = coordY;
		this.floor = floor;
	}
}