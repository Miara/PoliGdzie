package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.poligdzie.R;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
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
	private List <String> result;
	private ArrayAdapter <String> adapter;
	private Context context;
	
	private List <String> names;
	private List <Integer> icons;
	
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

		names.clear();
		icons.clear();
		result.clear();
		
		try {
			Log.i("POLIGDZIE",s.toString());
			buildings = dbHelper.getBuildingDao().queryBuilder().where().like("name", "%" + s.toString() + "%").query();
			rooms = dbHelper.getRoomDao().queryBuilder().where().like("name", "%" + s.toString() + "%").query();
			units = dbHelper.getUnitDao().queryBuilder().where().like("name", "%" + s.toString() + "%").query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(Building b : buildings)
		{
			names.add(b.getName());
			icons.add(R.drawable.cw_icon);
		}
		
		for(Unit b : units)
		{
			names.add(b.getName());
			icons.add(R.drawable.cw_icon);
		}
		
		for(Room b : rooms)
		{
			names.add(b.getName());
			icons.add(R.drawable.cw_icon);
		}
		
		String[] from = { "icon","name"};
        int[] to = { R.id.icon,R.id.name};
		
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        
        int size = names.size();
        for(int i=0;i<size;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            Log.i("POLIGDZIE",i+":"+names.get(i));
	        hm.put("name", names.get(i));
	        hm.put("icon", Integer.toString(icons.get(i)));
	        aList.add(hm);
        }
        Log.i("POLIGDZIE","------");
		SimpleAdapter adapter = new SimpleAdapter(this.context, aList, R.layout.position_prompt, from, to);

        /*OnItemClickListener itemClickListener = new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        	}
		};
		input.setOnItemClickListener(itemClickListener);*/
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
		
		names = new ArrayList<String>();
		icons = new ArrayList<Integer>();
		
		result = new ArrayList<String>();
	}

}
