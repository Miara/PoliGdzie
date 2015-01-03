package com.poligdzie.helpers;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.poligdzie.content_creation.Fixture;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public DatabaseHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}

	private Dao<Building, Integer> buildingDao = null;
	private Dao<Unit, Integer> unitDao = null;
	private Dao<Room, Integer> roomDao = null;

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i("DATABASE", "TABLES");
			TableUtils.createTable(connectionSource, Room.class);
			TableUtils.createTable(connectionSource, Unit.class);
			TableUtils.createTable(connectionSource, Building.class);

			Fixture fixture = new Fixture(this);
			fixture.getCreator().populateDatabase(this);
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		Log.i("DATABASE", "upgrade in");
		try {
			Log.i("DATABASE", "upgrade1");
			TableUtils.dropTable(connectionSource, Room.class, false);
			TableUtils.dropTable(connectionSource, Unit.class, false);
			TableUtils.dropTable(connectionSource, Building.class, false);
			Log.i("DATABASE", "upgrade2");
			this.onCreate(db, connectionSource);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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

	public Dao<Room, Integer> getRoomDao() throws SQLException {
		if (roomDao == null) {
			roomDao = getDao(Room.class);
		}
		return roomDao;
	}

	@Override
	public void close() {
		super.close();
		buildingDao = null;
		unitDao = null;
		roomDao = null;
	}

}
