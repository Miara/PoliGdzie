package com.poligdzie.content_creation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationConnection;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.RoomFunctions;
import com.poligdzie.persistence.SpecialPoint;
import com.poligdzie.persistence.Unit;
import com.poligdzie.persistence.UnitTypes;

import android.content.Context;
import android.util.Log;

public class CsvReader
{
	private ContentCreator creator;
	private DatabaseHelper databaseHelper ;
	
	public CsvReader(DatabaseHelper dbHelper, Context context)
	{
		this.databaseHelper = dbHelper;
		creator = new ContentCreator();
		try
		{
			
			//Budynki
			InputStream is = context.getAssets().open("Building.csv");
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
		    String line= reader.readLine();
		    while((line=reader.readLine())!=null)
		    {
		    	addBuilding(line);
		    }
		    this.creator.populateDatabase(dbHelper);
		    reader.close();
		    
		    //Pietra
		    is = context.getAssets().open("Floor.csv");
		    reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
		    line= reader.readLine();
		    while((line=reader.readLine())!=null)
		    {
		    	addFloor(line);
		    }
		    reader.close();
		    this.creator.populateDatabase(dbHelper);
		    
		    //Pomieszczenia
		    is = context.getAssets().open("Room.csv");
		    reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
		    line= reader.readLine();
		    while((line=reader.readLine())!=null)
		    {
		    	addRoom(line);
		    }
		    reader.close();
		    this.creator.populateDatabase(dbHelper);
		    
		    //Wydzialy
		    is = context.getAssets().open("Unit.csv");
		    reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
		    line= reader.readLine();
		    while((line=reader.readLine())!=null)
		    {
		    	addUnit(line);
		    }
		    reader.close();
		    this.creator.populateDatabase(dbHelper);
		    
		    //Punkty nawigacyjne
		    
		    //Punkty nawigacyjne polaczenia
		    
		    //punkty specjalne
		    
		    //punkty specjalne pion
		    
		}
		catch(Exception e)
		{
		    Log.i("POLIGDZIE","Wystapil blad");
		}
	}
	
	private void addBuilding(String line)
	{
		try
		{
			String[] value = line.split(";");
	    	String name					= value[1];
	    	String address				= value[2];
			double coordX 				= Double.parseDouble( value[3] );
	    	double coordY 				= Double.parseDouble( value[4] );
	    	int width	  				= Integer.parseInt(value[5]);
	    	int height	  				= Integer.parseInt(value[6]);
	    	String aliases 				= value[7];
	    	String imageResource		= value[8];
			String markerImageResource  = value[9];
	
			Building building = new Building(name, address, coordX, coordY, width, height, aliases, imageResource, markerImageResource);
	    	creator.add(building);
		}
		catch(Exception e)
		{
		    Log.i("POLIGDZIE","Nie dodano budynku do bazy");
		}
	}
	private void addFloor(String line) throws NumberFormatException, SQLException
	{
		try
		{
			//id;name;building;floorNumber;width;height;scheme;tag
			String[] value = line.split(";");
			String name			= value[1];
			echo("1");
			Building building 	= getBuilding(Integer.parseInt(value[2]));
			echo("2");
			int number 			= Integer.parseInt(value[3]);
			int width	  		= Integer.parseInt(value[4]);
			int height	  		= Integer.parseInt(value[5]);
			echo("3");
			String scheme		= value[6];
			String tag  		= value[7];
			echo("4");
			int pixelsPerMeter  = Integer.parseInt(value[8]);
			
			Floor  floor = new Floor(name,building,number,width,height,scheme,tag,pixelsPerMeter);
	    	creator.add(floor);
		}
		catch(Exception e)
		{
		    Log.i("POLIGDZIE","Nie dodano pietra do bazy");
		}
	}
	
	private void addRoom(String line)
	{
		try
		{
			//id;number;name;function;building;floor;coordX;coordY;radius;doorsX;doorsY;navigationPointConnection;aliases
			String[] value = line.split(";");
			echo("1");
			String number 			= value[1]; 
			String name				= value[2];
			echo("1");
			RoomFunctions function 	= getRoomFunction(value[3]);
			echo("1");
			Building building 		= getBuilding(Integer.parseInt(value[4]));
			echo("1");
			Floor floor 			= getFloor(Integer.parseInt(value[5]));	
			echo("1");
			int coordX	  			= Integer.parseInt(value[6]);
		    int coordY	  			= Integer.parseInt(value[7]);
			int radius	  			= Integer.parseInt(value[8]);
			int doorsX	  			= Integer.parseInt(value[9]);
			int doorsY	  			= Integer.parseInt(value[10]);
			NavigationConnection navigationConnection 
									= getNavigationConnection(Integer.parseInt(value[11]));
	    	String aliases			= value[12];
	
			Room  room = new Room(number,name,function,building,floor,
					coordX,coordY,radius,doorsX,doorsY,navigationConnection,aliases);
	    	creator.add(room);
		}
		catch(Exception e)
		{
		    Log.i("POLIGDZIE","Nie dodano pomieszczenia do bazy");
		}
	}
	
	private void addUnit(String line)
	{
		
		try
		{
			//id;name;www;type;aliases,building,room
			String[] value = line.split(";");
			
			String name			= value[1];
			String www			= value[2];
			UnitTypes type		= getUnitType(value[3]);
			String aliases		= value[4];
			Building building 	= getBuilding(Integer.parseInt(value[5]));
			Room room 			= getRoom(Integer.parseInt(value[6]));
			
			Unit  unit = new Unit(name,www,type,aliases,building,room);
	    	creator.add(unit);
		}
		catch(Exception e)
		{
		    Log.i("POLIGDZIE","Nie dodano pietra do bazy");
		}
	}
	
	
	
	private NavigationPoint getNavigationPoint(int id) throws SQLException
	{
		List<NavigationPoint> points = databaseHelper.getNavigationPointDao().
				queryBuilder().where().eq("id", id).query();
		return points.get(0);
	}
	
	private NavigationConnection getNavigationConnection(int id) throws SQLException
	{
		List<NavigationConnection> connections = databaseHelper.getNavigationConnectionDao().
				queryBuilder().where().eq("id", id).query();
		return connections.get(0);
	}
	
	private SpecialPoint getSpecialPoint(int id) throws SQLException
	{
		List<SpecialPoint> points = databaseHelper.getSpecialPointDao().
				queryBuilder().where().eq("id", id).query();
		return points.get(0);
	}
	

	private Floor getFloor(int id) throws SQLException
	{
		List<Floor> floors = databaseHelper.getFloorDao().queryBuilder().where().eq("id", id).query();
		return floors.get(0);
	}
	
	private Room getRoom(int id) throws SQLException
	{
		List<Room> rooms = databaseHelper.getRoomDao().queryBuilder().where().eq("id", id).query();
		return rooms.get(0);
	}

	private RoomFunctions getRoomFunction(String function)
	{
		//"LECTURE, LABORATORY, RESTROOM, GASTRONOMIC, LIBRARY, STAFF, TECH"
		if (function == "LECTURE") return RoomFunctions.LECTURE;
		else if (function == "LABORATORY") return RoomFunctions.LABORATORY;
		else if (function == "RESTROOM") return RoomFunctions.RESTROOM;
		else if (function == "GASTRONOMIC") return RoomFunctions.GASTRONOMIC;
		else if (function == "LIBRARY") return RoomFunctions.LIBRARY;
		else if (function == "STAFF") return RoomFunctions.STAFF;
		else if (function == "TECH") return RoomFunctions.TECH;
		else return null;
	}
	
	private UnitTypes getUnitType(String type)
	{
		//CHAIR,FACULTY,INSTUTUTE
		if (type == "CHAIR") return UnitTypes.CHAIR;
		if (type == "FACULTY") return UnitTypes.FACULTY;
		if (type == "INSTITUTE") return UnitTypes.INSTITUTE;
		else return null;
	}

	
	
	private Building getBuilding(int id) throws SQLException
	{
		List<Building> buildings = databaseHelper.getBuildingDao().queryBuilder().where().eq("id", id).query();
		return buildings.get(0);
	}
	
	public ContentCreator getCreator()
	{
		return creator;
	}
	
	private void echo(String tag)
	{
		Log.i("Poligdzie",tag);		
	}
}
