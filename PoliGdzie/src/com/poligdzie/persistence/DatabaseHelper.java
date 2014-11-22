package com.poligdzie.persistence;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private DaoContainer daoContainer;
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		Log.d("moje", "onCreate!!!!!!!!!!!!!!!!!!!!!!11");
		// TODO Auto-generated method stub
		try {
			TableUtils.createTable(arg1, Building.class);
			TableUtils.createTable(arg1, Room.class);
			TableUtils.createTable(arg1, Unit.class);
			daoContainer = new DaoContainer(arg1);
			Log.i("moje", "dao utworzoooooooooony!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}


	public DaoContainer getDaoContainer() {
		return daoContainer;
	}

}
