package com.poligdzie.persistence;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class DaoContainer {

	private Dao<Building, Integer> buildingDao;
	private Dao<Room, Integer> roomDao;
	private Dao<Unit, Integer> unitDao;

	public DaoContainer(ConnectionSource connectionSource) {
		try {
			buildingDao = DaoManager.createDao(connectionSource, Building.class);
			roomDao = DaoManager.createDao(connectionSource, Room.class);
			unitDao = DaoManager.createDao(connectionSource, Unit.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Dao<Building, Integer> getBuildingDao() {
		return buildingDao;
	}

	public Dao<Room, Integer> getRoomDao() {
		return roomDao;
	}

	public Dao<Unit, Integer> getUnitDao() {
		return unitDao;
	}
}
