package com.poligdzie.fragments;

import java.util.ArrayList;
import java.util.List;

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
import com.poligdzie.base.PoliGdzieBaseActivity;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.singletons.MapDrawingProvider;

public class BuildingInfoFragment extends PoliGdzieBaseFragment implements
																OnClickListener
{

	private int					posX;
	private int					posY;
	private ViewGroup			container;
	private Button				startButton;
	private Button				goalButton;
	private Button				showInfoButton;
	private Button				showIndoorMapButton;
	private TextView			nameField;
	private Marker				marker;
	private MapIndoorFragment	indoorMap;
	private Building			currentBuildingOnMarker;
	private SearchRouteFragment	searchRouteFragment;
	private SearchPlaceFragment	searchPlaceFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
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

		showIndoorMapButton = (Button) rootView
				.findViewById(R.id.indoorMapWindowInfoButton);

		showIndoorMapButton.setOnClickListener(this);

		nameField = (TextView) rootView.findViewById(R.id.infoWindowNameField);
		nameField.setText(currentBuildingOnMarker.getName());

		searchRouteFragment = (SearchRouteFragment) this.getActivity()
				.getFragmentManager().findFragmentById(R.id.search_route_frag);
		searchPlaceFragment = (SearchPlaceFragment) this.getActivity()
				.getFragmentManager().findFragmentById(R.id.search_place_frag);

		return rootView;
	}

	@Override
	public void onClick(View v)
	{

		drawingProvider = MapDrawingProvider.getInstance();

		if (v == startButton)
		{
			switchPlaceSearchingToRouteSearching();

			drawingProvider.setStart(currentBuildingOnMarker);
			searchRouteFragment.setStartPosition(currentBuildingOnMarker
					.getName());

		}

		if (v == goalButton)
		{
			switchPlaceSearchingToRouteSearching();

			drawingProvider.setGoal(currentBuildingOnMarker);
			searchRouteFragment.setGoalPosition(currentBuildingOnMarker
					.getName());
		}

		if (v == showInfoButton)
		{
			Intent intent = new Intent(this.getActivity(),
					BuildingInfoActivity.class);
			this.getActivity().startActivity(intent);
		}

		if (v == showIndoorMapButton)
		{
			mapProvider.clearFragments();
			int i = 0;
			Log.d("POLIGDZIE", currentBuildingOnMarker.getName());

			for (Floor f : currentBuildingOnMarker.getFloors())
			{
				Log.d("POLIGDZIE", f.getName());
				indoorMap = new MapIndoorFragment(f.getDrawableId(),
						f.getName(), f.getTag(), f.getNumber());
				if (i == 0)
				{
					((PoliGdzieBaseActivity) this.getActivity())
							.switchFragment(R.id.map_container, indoorMap,
									indoorMap.getViewTag());
				}
				i++;
			}

			((OnClickListener) this.getActivity()).onClick(this.getActivity()
					.findViewById(R.layout.map_activity));
		}

	}

	private void switchPlaceSearchingToRouteSearching()
	{
		if (searchPlaceFragment.isVisible())
		{

			searchPlaceFragment.getView().setVisibility(View.GONE);
			searchRouteFragment.getView().setVisibility(View.VISIBLE);
		}
	}

	public BuildingInfoFragment(int posX, int posY, Marker marker,
			DatabaseHelper dbHelper)
	{

		this.posX = posX;
		this.posY = posY;
		this.marker = marker;

		LatLng coords = new LatLng(0.0, 0.0);

		coords = marker.getPosition();
		List<Building> buildings = new ArrayList<Building>();

		try
		{
			buildings.addAll(dbHelper.getBuildingDao().queryBuilder().where()
					.eq("coordX", coords.latitude).and()
					.eq("coordY", coords.longitude).query());
		} catch (java.sql.SQLException e)
		{
			e.printStackTrace();
		}

		currentBuildingOnMarker = buildings.get(0);
	}

}