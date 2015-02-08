package com.poligdzie.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.example.poligdzie.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.BuildingEntry;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationConnection;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.RemoteDbVersion;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.SpecialConnection;
import com.poligdzie.persistence.Unit;
import com.poligdzie.tasks.DatabaseDownloadTask;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper implements
															Constants
{

	public DatabaseHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion)
	{
		super(context, databaseName, factory, databaseVersion);
		this.context = context;
	}

	public static String						DATABASE_NAME			= "Poligdzie.db";

	public static int							DATABASE_VERSION		= 2;

	private Context								context;
	private Dao<Building, Integer>				buildingDao				= null;
	private Dao<Unit, Integer>					unitDao					= null;
	private Dao<Room, Integer>					roomDao					= null;
	private Dao<Floor, Integer>					floorDao				= null;
	private Dao<NavigationPoint, Integer>		navigationPointDao		= null;
	private Dao<BuildingEntry, Integer>			BuildingEntryDao		= null;
	private Dao<NavigationConnection, Integer>	navigationConnectionDao	= null;
	private Dao<SpecialConnection, Integer>		specialConnectionDao	= null;
	private Dao<RemoteDbVersion, Integer>		remoteDbVersionDao		= null;

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{

		final String PREFS_NAME = "PreferencesFile";

		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);

		if (settings.getBoolean("firstLaunch", true))
		{
			try
			{
				File dbFile = new File(db.getPath());
				if (dbFile.exists())
					dbFile.delete();

				InputStream newDb = context.getResources().openRawResource(
						R.raw.poligdzie);

				Log.i("POLIGDZIE", "helper on  create");
				dbFile.createNewFile();

				FileOutputStream dst = new FileOutputStream(dbFile);
				byte bytes[] = new byte[1024];
				int len = 0;
				while ((len = newDb.read(bytes)) > 0)
				{
					dst.write(bytes);
				}
				dst.close();
				newDb.close();

			} catch (IOException e)
			{
				e.printStackTrace();
			}

			settings.edit().putBoolean("firstLaunch", false).commit();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion)
	{
		Log.i("POLIGDZIE", "upgrade in");

		DatabaseDownloadTask downloadTask = new DatabaseDownloadTask(
				db.getPath());
		downloadTask.execute("http://192.168.0.100:8181/download/");

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

	public Dao<NavigationPoint, Integer> getNavigationPointDao()
			throws SQLException
	{
		if (navigationPointDao == null)
		{
			navigationPointDao = getDao(NavigationPoint.class);
		}
		return navigationPointDao;
	}

	public Dao<BuildingEntry, Integer> getBuildingEntryDao()
			throws SQLException
	{
		if (BuildingEntryDao == null)
		{
			BuildingEntryDao = getDao(BuildingEntry.class);
		}
		return BuildingEntryDao;
	}

	public Dao<NavigationConnection, Integer> getNavigationConnectionDao()
			throws SQLException
	{
		if (navigationConnectionDao == null)
		{
			navigationConnectionDao = getDao(NavigationConnection.class);
		}
		return navigationConnectionDao;
	}

	public Dao<SpecialConnection, Integer> getSpecialConnectionDao()
			throws SQLException
	{
		if (specialConnectionDao == null)
		{
			specialConnectionDao = getDao(SpecialConnection.class);
		}
		return specialConnectionDao;
	}

	public Dao<RemoteDbVersion, Integer> getRemoteDbVersionDao()
			throws SQLException
	{
		if (remoteDbVersionDao == null)
		{
			remoteDbVersionDao = getDao(RemoteDbVersion.class);
		}
		return remoteDbVersionDao;
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
		BuildingEntryDao = null;
		navigationConnectionDao = null;
		specialConnectionDao = null;
		remoteDbVersionDao = null;
	}

}
