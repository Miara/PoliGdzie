package com.poligdzie.listeners;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import com.example.poligdzie.R;
import com.poligdzie.adapters.AutocompleteCustomAdapter;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;
import com.poligdzie.singletons.DataProvider;

public class ContextSearchTextWatcher extends PoliGdzieBaseClass implements
																TextWatcher
{

	private AutoCompleteTextView		input;
	private List<Building>				buildings;
	private List<Unit>					units;
	private List<Room>					rooms;

	private Context						context;
	private AutocompleteCustomAdapter	adapter;
	private List<Object>				aList;

	private static final int			MAX_PROMPTS	= 5;

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		String regex = "(?i:.*" + s.toString() + ".*)";

		buildings.clear();
		rooms.clear();
		units.clear();
		aList.clear();
		int currentPrompts = 0;

		DataProvider provider = DataProvider.getInstance();
		List<Building> bs = provider.getBuildings();
		for (Building b : bs)
		{
			if (currentPrompts < MAX_PROMPTS)
			{
				if (b.getName().matches(regex) || b.getAliases().matches(regex))
				{
					buildings.add(b);
					currentPrompts++;
				}
			} else
			{
				break;
			}
		}

		List<Room> rs = provider.getRooms();
		for (Room b : rs)
		{
			if (currentPrompts < MAX_PROMPTS)
			{
				if (b.getName().matches(regex) || b.getAliases().matches(regex))
				{
					rooms.add(b);
					currentPrompts++;
				}
			} else
			{
				break;
			}
		}

		List<Unit> us = provider.getUnits();
		for (Unit b : us)
		{
			if (currentPrompts < MAX_PROMPTS)
			{
				if (b.getName().matches(regex) || b.getAliases().matches(regex))
				{
					units.add(b);
					currentPrompts++;
				}
			} else
			{
				break;
			}
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

	}

	@Override
	public void afterTextChanged(Editable s)
	{

	}
	

	public ContextSearchTextWatcher(AutoCompleteTextView input, Context context)
	{
		dbHelper = new DatabaseHelper(input.getContext(), DatabaseHelper.DATABASE_NAME, null,
				DatabaseHelper.DATABASE_VERSION);
		this.input = input;
		this.context = context;

		aList = new ArrayList<Object>();
		rooms = new ArrayList<Room>();
		units = new ArrayList<Unit>();
		buildings = new ArrayList<Building>();

		adapter = new AutocompleteCustomAdapter(this.context, dbHelper,
				R.layout.prompt_item, aList);
		input.setAdapter(adapter);

	}

}
