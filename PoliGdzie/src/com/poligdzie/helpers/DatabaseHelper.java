package com.poligdzie.helpers;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.example.poligdzie.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.poligdzie.content_creation.CsvReader;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationConnection;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.SpecialConnection;
import com.poligdzie.persistence.Unit;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper implements Constants
{

	public DatabaseHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion)
	{
		super(context, databaseName, factory, databaseVersion, R.raw.ormlite_config);
		this.context = context;
	}
	private Context context;
	private Dao<Building, Integer>				buildingDao				= null;
	private Dao<Unit, Integer>					unitDao					= null;
	private Dao<Room, Integer>					roomDao					= null;
	private Dao<Floor, Integer>					floorDao				= null;
	private Dao<NavigationPoint, Integer>		navigationPointDao		= null;
	private Dao<NavigationConnection, Integer>	navigationConnectionDao	= null;
	private Dao<SpecialConnection, Integer>		specialConnectionDao	= null;

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			Log.i("DATABASE", "TABLES");
			
			TableUtils.createTable(connectionSource, Room.class);
			TableUtils.createTable(connectionSource, Unit.class);
			TableUtils.createTable(connectionSource, Building.class);
			TableUtils.createTable(connectionSource, Floor.class);
			TableUtils.createTable(connectionSource, NavigationPoint.class);
			TableUtils.createTable(connectionSource, NavigationConnection.class);
			TableUtils.createTable(connectionSource, SpecialConnection.class);
		
			
			CsvReader csvReader = new CsvReader(this,context);
			csvReader.parseCsvToDatabase("Building.csv",CSV_BUILDING);
			csvReader.parseCsvToDatabase("Floor.csv", CSV_FLOOR);
			csvReader.parseCsvToDatabase("NavigationPoint.csv", CSV_NAVIGATION_POINT);
			csvReader.parseCsvToDatabase("NavigationConnection.csv", CSV_NAVIGATION_CONNECTION);
			csvReader.parseCsvToDatabase("SpecialConnection.csv", CSV_SPECIAL_CONNECTION);
			csvReader.parseCsvToDatabase("Room.csv", CSV_ROOM);
			csvReader.parseCsvToDatabase("Unit.csv", CSV_UNIT);
			
			
		
		} catch (SQLException e)
		{

			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion)
	{
		Log.i("DATABASE", "upgrade in");
		try
		{
			Log.i("DATABASE", "upgrade1");
			TableUtils.dropTable(connectionSource, Room.class, false);
			TableUtils.dropTable(connectionSource, Unit.class, false);
			TableUtils.dropTable(connectionSource, Building.class, false);
			TableUtils.dropTable(connectionSource, Floor.class, false);
			TableUtils.dropTable(connectionSource, NavigationPoint.class,false);
			TableUtils.dropTable(connectionSource, NavigationConnection.class,false);
			TableUtils.dropTable(connectionSource, SpecialConnection.class,false);
			Log.i("DATABASE", "upgrade2");
			this.onCreate(db, connectionSource);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	public Dao<Building, Integer> getBuildingDao() throws SQLException
	{
		if (buildingDao == null)
		{
			buildingDao = getDao(Building.class);
		}
		return buildingDao;
	}

	public Dao<Unit, Integer> getUnitDao() throws SQLException
	{
		if (unitDao == null)
		{
			unitDao = getDao(Unit.class);
		}
		return unitDao;
	}

	public Dao<Room, Integer> getRoomDao() throws SQLException
	{
		if (roomDao == null)
		{
			roomDao = getDao(Room.class);
		}
		return roomDao;
	}

	public Dao<Floor, Integer> getFloorDao() throws SQLException
	{
		if (floorDao == null)
		{
			floorDao = getDao(Floor.class);
		}
		return floorDao;
	}
	
	

	public Dao<NavigationPoint, Integer> getNavigationPointDao() throws SQLException
	{
		if (navigationPointDao == null)
		{
			navigationPointDao = getDao(NavigationPoint.class);
		}
		return navigationPointDao;
	}

	public Dao<NavigationConnection, Integer> getNavigationConnectionDao() throws SQLException
	{
		if (navigationConnectionDao == null)
		{
			navigationConnectionDao = getDao(NavigationConnection.class);
		}
		return navigationConnectionDao;
	}

	public Dao<SpecialConnection, Integer> getSpecialConnectionDao() throws SQLException
	{
		if (specialConnectionDao == null)
		{
			specialConnectionDao = getDao(SpecialConnection.class);
		}
		return specialConnectionDao;
	}

	@Override
	public void close()
	{
		super.close();
		buildingDao = null;
		unitDao = null;
		roomDao = null;
		floorDao = null;
		navigationPointDao = null;
		navigationConnectionDao = null;
		specialConnectionDao = null;
	}

}
