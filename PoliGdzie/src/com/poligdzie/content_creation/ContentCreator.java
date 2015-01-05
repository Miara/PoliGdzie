package com.poligdzie.content_creation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class ContentCreator
{

	private List<Building>	buildings;
	private List<Unit>		units;
	private List<Room>		rooms;
	private List<Floor>		floors;

	public List<Building> getBuildings()
	{
		return buildings;
	}

	public void setBuildings(ArrayList<Building> buildings)
	{
		this.buildings = buildings;
	}

	public List<Unit> getUnits()
	{
		return units;
	}

	public void setUnits(ArrayList<Unit> units)
	{
		this.units = units;
	}

	public List<Room> getRooms()
	{
		return rooms;
	}

	public void setRooms(ArrayList<Room> rooms)
	{
		this.rooms = rooms;
	}

	public void add(Object value)
	{

		if (value.getClass() == Building.class)
		{
			this.addBuilding((Building) value);
		}
		if (value.getClass() == Unit.class)
		{
			this.addUnit((Unit) value);
		}
		if (value.getClass() == Room.class)
		{
			this.addRoom((Room) value);
		}

		if (value.getClass() == Floor.class)
		{
			this.addFloor((Floor) value);
		}

	}

	private void addBuilding(Building value)
	{
		this.buildings.add(value);
	}

	private void addUnit(Unit value)
	{
		this.units.add(value);
	}

	private void addRoom(Room value)
	{
		this.rooms.add(value);
	}

	private void addFloor(Floor value)
	{
		this.floors.add(value);
	}

	public ContentCreator()
	{
		buildings = new ArrayList<Building>();
		units = new ArrayList<Unit>();
		rooms = new ArrayList<Room>();
		floors = new ArrayList<Floor>();
	}

	public void populateDatabase(DatabaseHelper dbHelper)
	{

		for (Unit unit : units)
		{
			try
			{
				dbHelper.getUnitDao().createOrUpdate(unit);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		for (Room room : rooms)
		{
			try
			{
				dbHelper.getRoomDao().createOrUpdate(room);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		for (Building building : buildings)
		{
			try
			{
				dbHelper.getBuildingDao().createOrUpdate(building);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		for (Floor floor : floors)
		{
			try
			{
				dbHelper.getFloorDao().createOrUpdate(floor);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public List<Floor> getFloors()
	{
		return floors;
	}

	public void setFloors(List<Floor> floors)
	{
		this.floors = floors;
	}
}
