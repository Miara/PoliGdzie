package com.poligdzie.singletons;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.Marker;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class DataProvider
{
	
	private static DataProvider instance = null;
	private List <Building> buildings;
	private List <Room> rooms;
	private List <Unit> units;
	
	protected DataProvider()
	{
		// konstruktor zas³aniaj¹cy domyœlny publiczny konstruktor
		buildings = new ArrayList<Building>();
		rooms = new ArrayList<Room>();
		units = new ArrayList<Unit>();
	}

	// implementacja singletona
	public static DataProvider getInstance()
	{
		if (instance == null)
		{
			instance = new DataProvider();
		}
		return instance;
	}

	public List<Building> getBuildings()
	{
		return buildings;
	}

	public void setBuildings(List<Building> buildings)
	{
		this.buildings = buildings;
	}

	public List<Room> getRooms()
	{
		return rooms;
	}

	public void setRooms(List<Room> rooms)
	{
		this.rooms = rooms;
	}

	public List<Unit> getUnits()
	{
		return units;
	}

	public void setUnits(List<Unit> units)
	{
		this.units = units;
	}
}
