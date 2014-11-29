package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class ContextSearchTextWatcher implements TextWatcher, Constants{

	private AutoCompleteTextView input;
	private DatabaseHelper dbHelper;
	private List <Building> buildings;
	private List <Unit> units;
	private List <Room> rooms;
	private List <String> result;
	private ArrayAdapter <String> adapter;
	private Context context;
	
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

		result.clear();
		
		try {
			buildings = dbHelper.getBuildingDao().queryBuilder().where().like("name", "%" + s.toString() + "%").query();
			rooms = dbHelper.getRoomDao().queryBuilder().where().like("name", "%" + s.toString() + "%").query();
			units = dbHelper.getUnitDao().queryBuilder().where().like("name", "%" + s.toString() + "%").query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(Building b : buildings)
			result.add(b.getName());
		
		for(Unit b : units)
			result.add(b.getName());
		
		for(Room b : rooms)
			result.add(b.getName());
		
		adapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1,result);
		
		
		input.setAdapter(adapter); 
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	public ContextSearchTextWatcher(AutoCompleteTextView input, Context context) {
		dbHelper = new DatabaseHelper(input.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
		this.input = input;
		this.context = context;
		
		result = new ArrayList<String>();
	}

}
