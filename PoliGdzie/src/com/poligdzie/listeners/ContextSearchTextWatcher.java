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
import com.poligdzie.adapters.AutocompleteCustomAdapter;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class ContextSearchTextWatcher extends PoliGdzieBaseClass implements
																TextWatcher
{

	private AutoCompleteTextView	input;
	private List<Building>			buildings;
	private List<Unit>				units;
	private List<Room>				rooms;

	private Context					context;

	private List<Object>			aList;

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{

		aList.clear();
		long maxRows = 3;
		try
		{
			Log.i("POLIGDZIE", s.toString());
			buildings = dbHelper.getBuildingDao().queryBuilder().limit(maxRows).where()
					.like("name", "%" + s.toString() + "%").or()
					.like("aliases", "%" + s.toString() + "%").query();

			
			rooms = dbHelper.getRoomDao().queryBuilder().limit(maxRows).where()
					.like("name", "%" + s.toString() + "%").or()
					.like("aliases", "%" + s.toString() + "%").or()
					.like("number", "%" + s.toString() + "%").query();

			units = dbHelper.getUnitDao().queryBuilder().limit(maxRows).where()
					.like("name", "%" + s.toString() + "%").or()
					.like("aliases", "%" + s.toString() + "%").query();
		} catch (SQLException e)
		{
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

		AutocompleteCustomAdapter adapter = new AutocompleteCustomAdapter(
				this.context, R.layout.prompt_item, aList);

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
	}

}
