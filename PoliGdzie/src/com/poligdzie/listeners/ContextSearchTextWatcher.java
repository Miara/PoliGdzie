package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.example.poligdzie.R;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.SelectArg;
import com.poligdzie.adapters.AutocompleteCustomAdapter;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class ContextSearchTextWatcher extends PoliGdzieBaseClass implements
																TextWatcher
{

	private AutoCompleteTextView		input;
	private List<Building>				buildings;
	private List<Unit>					units;
	private List<Room>					rooms;

	private PreparedQuery<Building>		buildingQuery;
	private PreparedQuery<Unit>			unitQuery;
	private PreparedQuery<Room>			roomQuery;

	private List<SelectArg>				arg;

	private int							maxAListCapacity	= 5;

	private Context						context;
	private AutocompleteCustomAdapter	adapter;

	private List<Object>				aList;
	private int							argCount			= 7;

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		for (int i = 0; i < arg.size(); i++)
		{
			SelectArg a = arg.get(i);
			a.setValue(s + "%");
			arg.set(i, a);
		}

		buildings.clear();
		rooms.clear();
		units.clear();
		aList.clear();
		try
		{
			long start_t = System.nanoTime();
			buildings = dbHelper.getBuildingDao().query(buildingQuery);
			units = dbHelper.getUnitDao().query(unitQuery);
			rooms = dbHelper.getRoomDao().query(roomQuery);
			long end = System.nanoTime();
			Log.d("POLIGDZIE", "Czas selectow: "
					+ ((float) (end - start_t) / 1000000000));
		} catch (java.sql.SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Building b : buildings)
		{
			if (aList.size() >= maxAListCapacity)
				break;
			aList.add(b);
		}

		for (Unit b : units)
		{
			if (aList.size() >= maxAListCapacity)
				break;
			aList.add(b);
		}

		for (Room b : rooms)
		{
			if (aList.size() >= maxAListCapacity)
				break;
			aList.add(b);
		}

		input.setAdapter(adapter);

	}

	@Override
	public void afterTextChanged(Editable s)
	{

	}

	public ContextSearchTextWatcher(AutoCompleteTextView input, Context context)
	{
		dbHelper = new DatabaseHelper(input.getContext(), DATABASE_NAME, null,
				DATABASE_VERSION);
		this.input = input;
		this.context = context;

		aList = new ArrayList<Object>();
		rooms = new ArrayList<Room>();
		units = new ArrayList<Unit>();
		buildings = new ArrayList<Building>();

		adapter = new AutocompleteCustomAdapter(dbHelper, this.context,
				R.layout.prompt_item, aList);

		arg = new ArrayList<SelectArg>();

		for (int i = 0; i < argCount; i++)
		{
			arg.add(new SelectArg());
		}
		int location = 0;
		try
		{
			buildingQuery = dbHelper.getBuildingDao().queryBuilder().where()
					.like("name", arg.get(location++)).or()
					.like("aliases", arg.get(location++)).prepare();

			roomQuery = dbHelper.getRoomDao().queryBuilder().where()
					.like("name", arg.get(location++)).or()
					.like("aliases", arg.get(location++)).or()
					.like("number", arg.get(location++)).prepare();

			unitQuery = dbHelper.getUnitDao().queryBuilder().where()
					.like("name", arg.get(location++)).or()
					.like("aliases", arg.get(location++)).prepare();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
