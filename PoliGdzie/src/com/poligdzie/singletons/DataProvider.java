package com.poligdzie.singletons;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class DataProvider extends PoliGdzieBaseClass
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
	
	public void initialize(Context cnt) {
		dbHelper = new DatabaseHelper(cnt, DATABASE_NAME, null, DATABASE_VERSION);
		try
		{
			long start = System.nanoTime();
			this.setBuildings(dbHelper.getBuildingDao().queryForAll());
			float time = (float)(System.nanoTime() - start)/1000000000;
			Log.d("POLIGDZIE", "Czas wykonania - budynki: " + time);
			start = System.nanoTime();
			this.setRooms(dbHelper.getRoomDao().queryForAll());
			time = (float)(System.nanoTime() - start)/1000000000;
			Log.d("POLIGDZIE", "Czas wykonania - pomieszczenia: " + time);
			this.setUnits(dbHelper.getUnitDao().queryForAll());
			start = System.nanoTime();
			time = (float)(System.nanoTime() - start)/1000000000;
			Log.d("POLIGDZIE", "Czas wykonania - unity: " + time);
			
			
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
