package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.List;

import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class ContextSearchTextWatcher implements TextWatcher, Constants{

	private EditText input;
	private TextView output;
	private DatabaseHelper dbHelper;
	private List <Building> buildings;
	private List <Unit> units;
	private List <Room> rooms;
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

		try {
			buildings = dbHelper.getBuildingDao().queryBuilder().where().like("name", "%" + s.toString() + "%").query();
			rooms = dbHelper.getRoomDao().queryBuilder().where().like("name", "%" + s.toString() + "%").query();
			units = dbHelper.getUnitDao().queryBuilder().where().like("name", "%" + s.toString() + "%").query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Building b : buildings)
			output.setText(b.getName());
		
		for(Unit b : units)
			output.setText(b.getName());
		
		for(Room b : rooms)
			output.setText(b.getName());
		
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	public ContextSearchTextWatcher(EditText input, TextView output) {
		dbHelper = new DatabaseHelper(input.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
		this.input = input;
		this.output = output;
	}

}
