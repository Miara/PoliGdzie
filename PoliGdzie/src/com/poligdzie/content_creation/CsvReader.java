package com.poligdzie.content_creation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.BuildingEntry;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationConnection;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.NavigationPointTypes;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.RoomFunctions;
import com.poligdzie.persistence.SpecialConnection;
import com.poligdzie.persistence.Unit;
import com.poligdzie.persistence.UnitTypes;

public class CsvReader implements Constants
{
	private ContentCreator	creator;
	private DatabaseHelper	dbHelper;
	private Context			context;

	public CsvReader(DatabaseHelper dbHelper, Context context)
	{
		this.dbHelper = dbHelper;
		this.creator = new ContentCreator();
		this.context = context;

	}

	public void parseCsvToDatabase(String file, int mode)
	{
		try
		{

			// Budynki
			int number = 1;
			InputStream is = context.getAssets().open(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			String line = reader.readLine();
			while ((line = reader.readLine()) != null)
			{

				switch (mode)
				{
				case CSV_BUILDING:
					addBuilding(line, number);
					break;
				case CSV_FLOOR:
					addFloor(line, number);
					break;
				case CSV_NAVIGATION_POINT:
					addNavigationPoint(line, number);
					break;
				case CSV_BUILDING_ENTRY:
					addBuildingEntry(line, number);
					break;
				case CSV_NAVIGATION_CONNECTION:
					addNavigationConnection(line, number);
					break;
				case CSV_SPECIAL_CONNECTION:
					addSpecialConnection(line, number);
					break;
				case CSV_ROOM:
					addRoom(line, number);
					break;
				case CSV_UNIT:
					addUnit(line, number);
					break;
				}
				number++;
			}
			this.creator.populateDatabase(dbHelper);
			reader.close();

		} catch (Exception e)
		{
			Log.e("POLIGDZIE", "Wystapil blad");
		}
	}

	private void addBuilding(String line, int number)
	{
		try
		{
			String[] value = line.split(";");
			String name = value[1];
			String address = value[2];
			double coordX = toDouble(value[3]);
			double coordY = toDouble(value[4]);
			int width = toInt(value[5]);
			int height = toInt(value[6]);
			String aliases = value[7];
			String imageResource = value[8];
			String markerImageResource = value[9];

			Building building = new Building(name, address, coordX, coordY,
					width, height, aliases, imageResource, markerImageResource);
			creator.add(building);
		} catch (Exception e)
		{
			Log.e("POLIGDZIE", number + ":Nie dodano budynku do bazy");
		}
	}

	private void addFloor(String line, int number)
			throws NumberFormatException, SQLException
	{
		try
		{
			// id;name;building;floorNumber;width;height;scheme;tag
			String[] value = line.split(";");
			String name = value[1];
			Building building = getBuilding(Integer.parseInt(value[2]));
			int floorNumber = toInt(value[3]);
			int width = toInt(value[4]);
			int height = toInt(value[5]);
			String scheme = value[6];
			String tag = value[7];
			int pixelsPerMeter = toInt(value[8]);

			Floor floor = new Floor(name, building, floorNumber, width, height,
					scheme, tag, pixelsPerMeter);
			creator.add(floor);
		} catch (Exception e)
		{
			Log.e("POLIGDZIE", number + ":Nie dodano pietra do bazy");
		}
	}

	private void addNavigationPoint(String line, int number)
	{

		try
		{
			// coordX,coordY,floor,type
			String[] value = line.split(";");

			int coordX = toInt(value[1]);
			int coordY = toInt(value[2]);
			Floor floor = getFloor(Integer.parseInt(value[3]));
			NavigationPointTypes type = getNavigationPointType(value[4]);

			NavigationPoint point = new NavigationPoint(coordX, coordY, floor,
					type);
			creator.add(point);
		} catch (Exception e)
		{
			Log.e("POLIGDZIE", number
					+ ":Nie dodano punktu nawigacyjnego do bazy");
		}
	}

	private void addBuildingEntry(String line, int number)
	{
		try
		{
			// coordX,coordY,floor,type
			String[] value = line.split(";");

			int coordX = toInt(value[1]);
			int coordY = toInt(value[2]);
			Building building = getBuilding(Integer.parseInt(value[3]));
			NavigationPoint point = getNavigationPoint(Integer
					.parseInt(value[4]));

			BuildingEntry entry = new BuildingEntry(coordX, coordY, building,
					point);
			creator.add(entry);
		} catch (Exception e)
		{
			Log.e("POLIGDZIE", number
					+ ":Nie dodano wejœcia do budynku do bazy");
		}

	}

	private void addNavigationConnection(String line, int number)
	{

		try
		{
			// id;name;www;type;aliases,building,room
			String[] value = line.split(";");
<<<<<<< HEAD
			
			NavigationPoint first = getNavigationPoint(Integer.parseInt(value[1])) ;
			NavigationPoint last  = getNavigationPoint(Integer.parseInt(value[2])) ;
			int length 			  = getNavigationConnectionLength(first, last,number);
			
			NavigationConnection  connection = new NavigationConnection(first,last,length);
			echo("("+first.getId()+"->"+last.getId()+")="+length);
	    	creator.add(connection);
		}
		catch(Exception e)
=======

			NavigationPoint first = getNavigationPoint(Integer
					.parseInt(value[1]));
			NavigationPoint last = getNavigationPoint(Integer
					.parseInt(value[2]));
			int length = getNavigationConnectionLength(first, last, number);

			NavigationConnection connection = new NavigationConnection(first,
					last, length);
			creator.add(connection);
		} catch (Exception e)
>>>>>>> 3516068042f766e8805cb38b910aa213467466df
		{
			Log.e("POLIGDZIE", number
					+ ":Nie dodano polaczenia nawigacyjnego do bazy");
		}
	}

	private void addSpecialConnection(String line, int number)
	{

		try
		{
			// id;name;www;type;aliases,building,room
			String[] value = line.split(";");

			NavigationPoint lower = getNavigationPoint(Integer
					.parseInt(value[1]));
			NavigationPoint upper = getNavigationPoint(Integer
					.parseInt(value[2]));

			SpecialConnection connection = new SpecialConnection(lower, upper);
			creator.add(connection);
		} catch (Exception e)
		{
			Log.e("POLIGDZIE", number
					+ ":Nie dodano polaczenia specjalnego do bazy");
		}
	}

	private void addRoom(String line, int number)
	{
		try
		{
			// id;number;name;function;building;floor;coordX;coordY;radius;doorsX;doorsY;navigationPointConnection;aliases
			String[] value = line.split(";");

			String roomNumber 			= value[1]; 
			String name				= value[2];
			RoomFunctions function 	= getRoomFunction(value[3]);
			Floor floor 			= getFloor(Integer.parseInt(value[4]));	
			int coordX	  			= toInt(value[5]);
		    int coordY	  			= toInt(value[6]);
			int radius	  			= toInt(value[7]);
			int doorsX	  			= toInt(value[8]);
			int doorsY	  			= toInt(value[9]);
			NavigationConnection navigationConnection 
									= getNavigationConnection(Integer.parseInt(value[10]));
			echo("Conn["+name+"]="+navigationConnection.getId());
			echo("first["+name+"]="+navigationConnection.getFirstPoint());
			echo("last["+name+"]="+navigationConnection.getLastPoint());
			
			String aliases			= value[11];
	
			Room  room = new Room(roomNumber,name,function,floor,
					coordX,coordY,radius,doorsX,doorsY,navigationConnection,aliases);
	    	creator.add(room);
		}
		catch(Exception e)
		{
			Log.e("POLIGDZIE", number + ":Nie dodano pomieszczenia do bazy");
		}
	}

	private void addUnit(String line, int number)
	{

		try
		{
			// id;name;www;type;aliases,building,room
			String[] value = line.split(";");

			String name = value[1];
			String www = value[2];
			UnitTypes type = getUnitType(value[3]);
			String aliases = value[4];
			Building building = getBuilding(Integer.parseInt(value[5]));
			Room room = getRoom(Integer.parseInt(value[6]));

			Unit unit = new Unit(name, www, type, aliases, building, room);
			creator.add(unit);
		} catch (Exception e)
		{
			Log.e("POLIGDZIE", number
					+ ":Nie dodano jednostki organizacyjnej do bazy");
		}
	}

	private NavigationPoint getNavigationPoint(int id) throws SQLException
	{
<<<<<<< HEAD
		NavigationPoint point = dbHelper.getNavigationPointDao().queryForId(id);
		return point;
=======
		List<NavigationPoint> points = dbHelper.getNavigationPointDao()
				.queryBuilder().where().eq("id", id).query();
		return points.get(0);
>>>>>>> 3516068042f766e8805cb38b910aa213467466df
	}

	private NavigationConnection getNavigationConnection(int id)
			throws SQLException
	{
		List<NavigationConnection> connections = dbHelper
				.getNavigationConnectionDao().queryBuilder().where()
				.eq("id", id).query();
		return connections.get(0);
	}

	private RoomFunctions getRoomFunction(String function) throws Exception
	{
		// "LECTURE, LABORATORY, RESTROOM, GASTRONOMIC, LIBRARY, STAFF, TECH"
		if (function.equals("LECTURE"))
			return RoomFunctions.LECTURE;
		else if (function.equals("LABORATORY"))
			return RoomFunctions.LABORATORY;
		else if (function.equals("RESTROOM"))
			return RoomFunctions.RESTROOM;
		else if (function.equals("GASTRONOMIC"))
			return RoomFunctions.GASTRONOMIC;
		else if (function.equals("LIBRARY"))
			return RoomFunctions.LIBRARY;
		else if (function.equals("STAFF"))
			return RoomFunctions.STAFF;
		else if (function.equals("TECH"))
			return RoomFunctions.TECH;
		else
			return null;
	}

	private UnitTypes getUnitType(String type) throws Exception
	{
		// CHAIR,FACULTY,INSTUTUTE
		if (type.equals("CHAIR"))
			return UnitTypes.CHAIR;
		else if (type.equals("FACULTY"))
			return UnitTypes.FACULTY;
		else if (type.equals("INSTITUTE"))
			return UnitTypes.INSTITUTE;
		else
			return null;
	}

	private NavigationPointTypes getNavigationPointType(String type)
			throws Exception
	{
		// NAVIGATION,SPECIAL
		if (type.equals("NAVIGATION"))
			return NavigationPointTypes.NAVIGATION;
		if (type.equals("SPECIAL"))
			return NavigationPointTypes.SPECIAL;
		if (type.equals("ENTRY"))
			return NavigationPointTypes.ENTRY;
		else
			throw new Exception();
	}

	private Building getBuilding(int id) throws SQLException
	{
		List<Building> buildings = dbHelper.getBuildingDao().queryBuilder()
				.where().eq("id", id).query();
		return buildings.get(0);
	}

	private Floor getFloor(int id) throws SQLException
	{
		List<Floor> floors = dbHelper.getFloorDao().queryBuilder().where()
				.eq("id", id).query();
		return floors.get(0);
	}

	private Room getRoom(int id) throws SQLException
	{
		List<Room> rooms = dbHelper.getRoomDao().queryBuilder().where()
				.eq("id", id).query();
		return rooms.get(0);
	}

	public ContentCreator getCreator()
	{
		return creator;
	}

	private int toInt(String str)
	{
		try
		{
			int result = Integer.parseInt(str);
			return result;
		} catch (Exception e)
		{
			return 0;
		}
	}

	private double toDouble(String str)
	{
		try
		{
			double result = Double.parseDouble(str);
			return result;
		} catch (Exception e)
		{
			return 0;
		}
	}

	private int getNavigationConnectionLength(NavigationPoint p1,
			NavigationPoint p2, int number)
	{
		if (p1.hasEqualFloor(p2.getFloor()))
		{
<<<<<<< HEAD
			try
			{
				int scale = dbHelper.getFloorDao().queryForId(p1.getFloor().getId()).getPixelsPerMeter();
				if(scale != 0)
				{
					double a = p2.getCoordX() - p1.getCoordX();
					double b = p2.getCoordY() - p1.getCoordY();
					double length = Math.sqrt(a*a + b*b) / scale;
					return (int)length;
				}
			} 
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
=======
			int scale = p1.getFloor().getPixelsPerMeter();
			double a = p2.getCoordX() - p1.getCoordX();
			double b = p2.getCoordY() - p1.getCoordY();
			double length = Math.sqrt(a * a + b * b) / scale;
			return (int) length;
		} else
		{
			Log.e("poligdzie", number
					+ ":Polaczenie nawigacyjne dotyczy roznych pieter - blad");
			// TODO : Dodaæ do pliku z b³êdami
			return -1;
>>>>>>> 3516068042f766e8805cb38b910aa213467466df
		}
		
		Log.e("poligdzie",number+":Polaczenie nawigacyjne dotyczy roznych pieter - blad");
		// TODO : Dodaæ do pliku z b³êdami
		return -1;
		
	}

	private void echo(String tag)
	{
		Log.i("Poligdzie", tag);
	}
}
