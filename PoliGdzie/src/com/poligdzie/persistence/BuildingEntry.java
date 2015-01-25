package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;

@DatabaseTable(tableName = "buildingEntry")
public class BuildingEntry extends PoliGdzieBaseClass
{
	@DatabaseField(id = true)
	private int				id;

	@DatabaseField
	private int				coordX;

	@DatabaseField
	private int				coordY;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "building_id")
	private Building		building;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "point_id")
	private NavigationPoint	navigationPoint;

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

	public Building getBuilding()
	{
		return building;
	}

	public void setBuilding(Building building)
	{
		this.building = building;
	}

	public NavigationPoint getNavigationPoint()
	{
		return navigationPoint;
	}

	public void setNavigationPoint(NavigationPoint navigationPoint)
	{
		this.navigationPoint = navigationPoint;
	}

	public BuildingEntry()
	{
		// nie wyrzucac !
	}

	public BuildingEntry(int coordX, int coordY, Building building,
			NavigationPoint navigationPoint)
	{
		this.coordX = coordX;
		this.coordY = coordY;
		this.building = building;
		this.navigationPoint = navigationPoint;
	}
}