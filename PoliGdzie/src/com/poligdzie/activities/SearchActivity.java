package com.poligdzie.activities;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.content_creation.ContentCreator;
import com.poligdzie.content_creation.Fixture;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;

public class SearchActivity extends Activity implements OnClickListener{

	private Button goToMapButton;
	private TextView testBazy;
	private List<Building> buildings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        
        testBazy = (TextView) findViewById(R.id.test_label);
        
        goToMapButton = (Button) findViewById(R.id.goToMapButton);
        goToMapButton.setOnClickListener(this);
        
        Log.i("DATABASE","CREATE");
        DatabaseHelper database = new DatabaseHelper(this, "Poligdzie.db", null, 8);
        try {
			buildings = database.getBuildingDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//contentCreator.getBuildings();
        
       if(buildings.isEmpty() || buildings == null)
        {
        	testBazy.setText("Brak budynków w bazie");
        }
        else
        {
        	testBazy.setText("Budynki w bazie :\n");
        	for(Building b : buildings)
        	{
        		testBazy.append(b.getName());
        		testBazy.append("\n");
        	}
        }
    }
    
    @Override
	public void onClick(View v) {
    	Log.i("TEST","TEST1");
		if( v == goToMapButton){
			Intent intent = new Intent(this, MapActivity.class);
	        startActivity(intent);
		}
		
    }
		
}
