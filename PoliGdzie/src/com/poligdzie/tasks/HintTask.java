package com.poligdzie.tasks;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.poligdzie.adapters.AutocompleteCustomAdapter;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class HintTask extends AsyncTask<String, Void, String>
{

	private MapOutdoorFragment			mapOutdoorFragment;
	private DatabaseHelper				dbHelper;
	private String						str;
	private List<Building>				buildings;
	private List<Unit>					units;
	private List<Room>					rooms;
	private ArrayList<Object>			aList;
	private AutoCompleteTextView		input;
	private AutocompleteCustomAdapter	adapter;

	@Override
	protected String doInBackground(String... params)
	{
		aList.clear();
		buildings.clear();
		rooms.clear();
		units.clear();

		try
		{
			buildings = dbHelper.getBuildingDao().queryForAll();
			units = dbHelper.getUnitDao().queryForAll();
			rooms = dbHelper.getRoomDao().queryForAll();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Building b : buildings)
		{
			aList.add(b);
		}

		for (Unit b : units)
		{
			aList.add(b);
		}

		for (Room b : rooms)
		{
			aList.add(b);
		}

		input.setAdapter(adapter);
		return null;
	}

	@Override
	protected void onPostExecute(String result)
	{

	}

	public HintTask(AutoCompleteTextView input,
			AutocompleteCustomAdapter adapter, DatabaseHelper dbHelper,
			String str, Context context)
	{

		this.adapter = adapter;
		this.aList = new ArrayList<Object>();
		this.buildings = new ArrayList<Building>();
		this.rooms = new ArrayList<Room>();
		this.units = new ArrayList<Unit>();
		this.str = str;
		this.input = input;
		this.dbHelper = dbHelper;
	}

	public String getStr()
	{
		return str;
	}

	public void setStr(String str)
	{
		this.str = str;
	}

}
