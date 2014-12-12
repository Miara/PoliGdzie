package com.poligdzie.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.poligdzie.activities.SearchActivity;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.TextEditDataProvider;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class InfoWindowCustomAdapter implements InfoWindowAdapter, OnInfoWindowClickListener{
	
	private Button startPoint;
	private Button goalPoint;
	private SearchAutoCompleteTextView startingPosition;
	private SearchAutoCompleteTextView goalPosition;
	private Activity activity;
	private Marker marker;
	private DatabaseHelper dbHelper;
	
	public InfoWindowCustomAdapter(Activity activity, DatabaseHelper db) {
		this.dbHelper = db;
		this.activity = activity;
		
		startPoint = (Button) activity.findViewById(R.id.infoWindowStartButton);
		goalPoint = (Button) activity.findViewById(R.id.infoWindowGoalButton);
		startingPosition = (SearchAutoCompleteTextView) activity.findViewById(R.id.starting_point_text_edit);
		goalPosition = (SearchAutoCompleteTextView) activity.findViewById(R.id.goal_point_text_edit);
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		marker = arg0;
		View v = activity.getLayoutInflater().inflate(R.layout.window_marker_click, null);
		return v;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		View v = activity.getLayoutInflater().inflate(R.layout.window_marker_click, null);
		return v;

	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		
		LatLng coords = new LatLng(0.0, 0.0);
		// TODO Auto-generated method stub
		coords = marker.getPosition();
		List <Building> buildings = new ArrayList <Building>();
	
			try {
				buildings.addAll(dbHelper.getBuildingDao().queryBuilder()
										 .where()
										 .eq("coordX", coords.latitude)
										 .and()
										 .eq("coordY", coords.longitude)
										 .query());
			} catch (java.sql.SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			
			TextEditDataProvider provider = TextEditDataProvider.getInstance();
			if(provider.getMode() == "from")
				provider.setFrom(buildings.get(0));
			
			if(provider.getMode() == "to")
				provider.setTo(buildings.get(0));
			
		
		Intent intent = new Intent(activity, SearchActivity.class);
		activity.startActivity(intent);
	}

}
