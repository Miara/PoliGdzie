package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.interfaces.WithCoordinates;

@DatabaseTable(tableName = "buildingEntry")
public class BuildingEntry extends PoliGdzieBaseClass implements WithCoordinates
{
	@DatabaseField(id = true)
	private int				id;

	@DatabaseField
	private double				coordX;

	@DatabaseField
	private double				coordY;

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

	public double getCoordX()
	{
		return coordX;
	}

	public void setCoordX(int coordX)
	{
		this.coordX = coordX;
	}

	public double getCoordY()
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

	public BuildingEntry(Double double1, Double double2, Building building,
			NavigationPoint navigationPoint)
	{
		this.coordX = double1;
		this.coordY = double2;
		this.building = building;
		this.navigationPoint = navigationPoint;
	}
}