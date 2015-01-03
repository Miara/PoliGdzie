package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.poligdzie.R;
import com.poligdzie.adapters.AutocompleteCustomAdapter;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ContextSearchTextWatcher implements TextWatcher, Constants{

	private AutoCompleteTextView input;
	private DatabaseHelper dbHelper;
	private List <Building> buildings;
	private List <Unit> units;
	private List <Room> rooms;

	private Context context;
	
	private List<Object> aList;

	
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

		aList.clear();
		
		try {
			Log.i("POLIGDZIE",s.toString());
			buildings = dbHelper.getBuildingDao().queryBuilder()
												 .where()
												 .like("name", "%" + s.toString() + "%")
												 .or()
												 .like("aliases", "%" + s.toString() + "%")
												 .query();
			
			rooms = dbHelper.getRoomDao().queryBuilder()
										 .where()
										 .like("name", "%" + s.toString() + "%")
										 .or()
										 .like("aliases", "%" + s.toString() + "%")
										 .or()
										 .like("number", "%" + s.toString() + "%")
										 .query();
						
			units = dbHelper.getUnitDao().queryBuilder()
										 .where()
										 .like("name", "%" + s.toString() + "%")
										 .or()
										 .like("aliases", "%" + s.toString() + "%")
										 .query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		for(Building b : buildings)
		{
			aList.add(b);
		}
		
		for(Unit b : units)
		{
			aList.add(b);
		}
		
		for(Room b : rooms)
		{
			aList.add(b);
		}
		
		 
		AutocompleteCustomAdapter adapter = new AutocompleteCustomAdapter(this.context, R.layout.prompt_item, aList);
		
		
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

		aList = new ArrayList<Object>();
	}


}
