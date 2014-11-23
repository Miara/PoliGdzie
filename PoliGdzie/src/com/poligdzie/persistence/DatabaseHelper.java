   
package com.poligdzie.persistence;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public DatabaseHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
		// TODO Auto-generated constructor stub
	}
	
	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "helloAndroid.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
	private Dao<Building, Integer> buildingDao = null;
	private Dao<Unit, Integer> unitDao = null;
	



	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.d("moje", "onCreate");
			TableUtils.createTable(connectionSource, Room.class);
			TableUtils.createTable(connectionSource, Unit.class);
			TableUtils.createTable(connectionSource, Building.class);
		} catch (SQLException e) {
			Log.e("moje", "Can't create database", e);
			throw new RuntimeException(e);
		}
		
		
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Building, Integer> getBuildingDao() throws SQLException {
		if (buildingDao == null) {
			buildingDao = getDao(Building.class);
		}
		return buildingDao;
	}

	public Dao<Unit, Integer> getUnitDao() throws SQLException {
		if (unitDao == null) {
			unitDao = getDao(Unit.class);
		}
		return unitDao;
	}
	

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		buildingDao = null;
		unitDao = null;
	}
}

