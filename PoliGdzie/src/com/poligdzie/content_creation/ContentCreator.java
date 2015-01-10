package com.poligdzie.content_creation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationConnection;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.SpecialConnection;
import com.poligdzie.persistence.SpecialPoint;
import com.poligdzie.persistence.Unit;

public class ContentCreator
{

	private List<Building>				buildings;
	private List<Unit>					units;
	private List<Room>					rooms;
	private List<Floor>					floors;
	private List<NavigationPoint>		navigationPoints;
	private List<NavigationConnection>	navigationConnections;
	private List<SpecialPoint>			specialPoints;
	private List<SpecialConnection>		specialConnections;

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
	
	public List<Floor> getFloors()
	{
		return floors;
	}

	public void setFloors(ArrayList<Floor> floors)
	{
		this.floors = floors;
	}
	
	

	public List<NavigationPoint> getNavigationPoints()
	{
		return navigationPoints;
	}

	public void setNavigationPoints(ArrayList<NavigationPoint> navigationPoints)
	{
		this.navigationPoints = navigationPoints;
	}

	public List<NavigationConnection> getNavigationConnections()
	{
		return navigationConnections;
	}

	public void setNavigationConnections(
			ArrayList<NavigationConnection> navigationConnections)
	{
		this.navigationConnections = navigationConnections;
	}

	public List<SpecialPoint> getSpecialPoints()
	{
		return specialPoints;
	}

	public void setSpecialPoints(ArrayList<SpecialPoint> specialPoints)
	{
		this.specialPoints = specialPoints;
	}

	public List<SpecialConnection> getSpecialConnections()
	{
		return specialConnections;
	}

	public void setSpecialConnections(ArrayList<SpecialConnection> specialConnections)
	{
		this.specialConnections = specialConnections;
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
		
		if (value.getClass() == NavigationPoint.class)
		{
			this.addNavigationPoint((NavigationPoint) value);
		}
		if (value.getClass() == NavigationConnection.class)
		{
			this.addNavigationConnection((NavigationConnection) value);
		}
		if (value.getClass() == SpecialPoint.class)
		{
			this.addSpecialPoint((SpecialPoint) value);
		}

		if (value.getClass() == SpecialConnection.class)
		{
			this.addSpecialConnection((SpecialConnection) value);
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
	
	private void addNavigationPoint(NavigationPoint value)
	{
		this.navigationPoints.add(value);	
	}
	
	private void addNavigationConnection(NavigationConnection value)
	{
		this.navigationConnections.add(value);	
	}
	
	private void addSpecialPoint(SpecialPoint value)
	{
		this.specialPoints.add(value);		
	}

	
	private void addSpecialConnection(SpecialConnection value)
	{
		this.specialConnections.add(value);	
	}

	public ContentCreator()
	{
		buildings = new ArrayList<Building>();
		units = new ArrayList<Unit>();
		rooms = new ArrayList<Room>();
		floors = new ArrayList<Floor>();
		navigationPoints = new ArrayList<NavigationPoint>();
		navigationConnections = new ArrayList<NavigationConnection>();
		specialPoints = new ArrayList<SpecialPoint>();
		specialConnections = new ArrayList<SpecialConnection>();
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
		
		for (NavigationPoint navigationPoint : navigationPoints)
		{
			try
			{
				dbHelper.getNavigationPointDao().createOrUpdate(navigationPoint);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		for (NavigationConnection navigationConnection : navigationConnections)
		{
			try
			{
				dbHelper.getNavigationConnectionDao().createOrUpdate(navigationConnection);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		for (SpecialPoint specialPoint : specialPoints)
		{
			try
			{
				dbHelper.getSpecialPointDao().createOrUpdate(specialPoint);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		for (SpecialConnection specialConnection : specialConnections)
		{
			try
			{
				dbHelper.getSpecialConnectionDao().createOrUpdate(specialConnection);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	
}
