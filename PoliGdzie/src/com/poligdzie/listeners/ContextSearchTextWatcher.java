package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.poligdzie.R;
import com.poligdzie.adapters.AutocompleteCustomAdapter;
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
	private ArrayAdapter <String> adapter;
	private Context context;
	
	private List <String> names;
	private List <String> descriptions;
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
		descriptions.clear();
		
		
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
			names.add(b.getName());
			icons.add(R.drawable.cw_icon);
			descriptions.add("Budynek");
		}
		
		for(Unit b : units)
		{
			names.add(b.getName());
			icons.add(R.drawable.cw_icon);
			descriptions.add("Jednostka organizacyjna");
		}
		
		for(Room b : rooms)
		{
			names.add(b.getName());
			icons.add(R.drawable.cw_icon);
			descriptions.add("Pomieszczenie");
		}
		
		String[] from = { "icon","name","description"};
        int[] to = { R.id.autocomplete_icon,R.id.autocomplete_name,R.id.autocomplete_description};
		
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        
        int size = names.size();
        for(int i=0;i<size;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            Log.i("POLIGDZIE",i+":"+names.get(i));
	        hm.put("name", names.get(i));
	        hm.put("icon", Integer.toString(icons.get(i)));
	        hm.put("description", descriptions.get(i));
	        aList.add(hm);
        }
        Log.i("POLIGDZIE","------");
		//SimpleAdapter adapter = new SimpleAdapter(this.context, aList, R.layout.position_prompt, from, to);
        /*OnItemClickListener itemClickListener = new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        	}
		};
		input.setOnItemClickListener(itemClickListener);*/
		AutocompleteCustomAdapter adapter = new AutocompleteCustomAdapter(this.context, R.layout.position_prompt, aList);
		for(int i=0; i<adapter.getCount(); i++)
	        Log.i("POLIGDZIE", adapter.getItem(i).toString());
		
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
		descriptions = new ArrayList<String>();
		
	}

}
