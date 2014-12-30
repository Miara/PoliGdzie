package com.poligdzie.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.poligdzie.activities.SearchActivity;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.RouteProvider;
import com.poligdzie.singletons.TextEditDataProvider;

public class BuildingInfoFragment extends Fragment implements OnClickListener,
		Constants {

	private int posX;
	private int posY;
	private ViewGroup container;
	private Button fromButton;
	private Button toButton;
	private Button showInfoButton;
	private TextView nameField;
	private RouteProvider routeProvider;
	private Marker marker;
	private DatabaseHelper dbHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.container = container;
		LayoutParams params = (LayoutParams) container.getLayoutParams();

		params.leftMargin = MARGIN_LEFT;
		params.topMargin = MARGIN_TOP;

		container.setLayoutParams(params);

		View rootView = inflater.inflate(R.layout.window_marker_click,
				container, false);

		fromButton = (Button) rootView.findViewById(R.id.infoWindowStartButton);
		fromButton.setOnClickListener(this);
		toButton = (Button) rootView.findViewById(R.id.infoWindowGoalButton);
		toButton.setOnClickListener(this);
		showInfoButton = (Button) rootView
				.findViewById(R.id.infoWindowInfoButton);

		LatLng coords = new LatLng(0.0, 0.0);
		
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
		
		nameField = (TextView) rootView.findViewById(R.id.infoWindowNameField);
		nameField.setText(buildings.get(0).getName());
		return rootView;
	}

	@Override
	public void onClick(View v) {
		
		if (v == fromButton) {
			
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
				provider.setFrom(buildings.get(0));
		
				Intent intent = new Intent(this.getActivity(), SearchActivity.class);
				this.getActivity().startActivity(intent);
		}
		
		if (v == toButton) {
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
				provider.setTo(buildings.get(0));
				
				Intent intent = new Intent(this.getActivity(), SearchActivity.class);
				this.getActivity().startActivity(intent);
		}

		if (v == showInfoButton) {

		}

		
		
	}

	public BuildingInfoFragment(int posX, int posY, Marker marker, DatabaseHelper dbHelper) {
		this.posX = posX;
		this.posY = posY;
		this.marker = marker;
		this.dbHelper = dbHelper;
	}

}