package com.poligdzie.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.poligdzie.activities.BuildingInfoActivity;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.singletons.RouteProvider;

public class BuildingInfoFragment extends Fragment implements OnClickListener,
		Constants {

	private int posX;
	private int posY;
	private ViewGroup container;
	private Button startButton;
	private Button goalButton;
	private Button showInfoButton;
	private TextView nameField;
	private RouteProvider routeProvider;
	private Marker marker;
	private DatabaseHelper dbHelper;
	private Building currentBuildingOnMarker;
	private SearchTraceFragment searchRouteFragment;
	private MapFragmentProvider mapProvider;

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

		startButton = (Button) rootView
				.findViewById(R.id.infoWindowStartButton);
		startButton.setOnClickListener(this);
		goalButton = (Button) rootView.findViewById(R.id.infoWindowGoalButton);
		goalButton.setOnClickListener(this);
		showInfoButton = (Button) rootView
				.findViewById(R.id.infoWindowInfoButton);

		showInfoButton.setOnClickListener(this);
		
		nameField = (TextView) rootView.findViewById(R.id.infoWindowNameField);
		nameField.setText(currentBuildingOnMarker.getName());
		
		
		searchRouteFragment = (SearchTraceFragment) this.getActivity().getFragmentManager().findFragmentById(R.id.search_route_frag);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		
		RouteProvider routeProvider = RouteProvider.getInstance();
		
		if (v == startButton) {
		
			routeProvider.setStart(currentBuildingOnMarker);
			searchRouteFragment.setStartPosition(currentBuildingOnMarker.getName());
		}

		if (v == goalButton) {
			
			routeProvider.setGoal(currentBuildingOnMarker);
			searchRouteFragment.setGoalPosition(currentBuildingOnMarker.getName());
		}

		if (v == showInfoButton) {
			Intent intent = new Intent(this.getActivity(), BuildingInfoActivity.class);
			this.getActivity().startActivity(intent);
		}

	}

	public BuildingInfoFragment(int posX, int posY, Marker marker,
			DatabaseHelper dbHelper) {
		this.posX = posX;
		this.posY = posY;
		this.marker = marker;
		this.dbHelper = dbHelper;

		LatLng coords = new LatLng(0.0, 0.0);

		coords = marker.getPosition();
		List<Building> buildings = new ArrayList<Building>();

		try {
			buildings.addAll(dbHelper.getBuildingDao().queryBuilder().where()
					.eq("coordX", coords.latitude).and()
					.eq("coordY", coords.longitude).query());
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		currentBuildingOnMarker = buildings.get(0);
	}

}