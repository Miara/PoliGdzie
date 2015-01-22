package com.poligdzie.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.listeners.RouteButtonListener;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchRouteFragment extends PoliGdzieBaseFragment implements
																OnClickListener
{

	private Button						searchButton;

	private SearchAutoCompleteTextView	startPosition;
	private SearchAutoCompleteTextView	goalPosition;

	private ContextSearchTextWatcher	startWatcher;
	private ContextSearchTextWatcher	goalWatcher;

	private BuildingInfoFragment		buildingInfoFragment;

	private GoogleMap					map;
	private MapOutdoorFragment			outdoorMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.search_trace_fragment,
				container, false);

		startPosition = (SearchAutoCompleteTextView) rootView
				.findViewById(R.id.starting_point_text_edit);
		startWatcher = new ContextSearchTextWatcher(startPosition,
				this.getActivity());
		startPosition.addTextChangedListener(startWatcher);
		if (startPosition == null)
			Log.i("poligdzie", "test");
		goalPosition = (SearchAutoCompleteTextView) rootView
				.findViewById(R.id.goal_point_text_edit);
		goalWatcher = new ContextSearchTextWatcher(goalPosition,
				this.getActivity());
		goalPosition.addTextChangedListener(goalWatcher);
		if (goalPosition == null)
			Log.i("poligdzie", "test2");

		MapDrawingProvider provider = MapDrawingProvider.getInstance();

		if (provider.getStart() != null)
		{
			String temp = ((Nameable) provider.getStart()).getName();
			if (temp != null && temp.length() != 0)
				startPosition.setText(temp);
		}

		if (provider.getGoal() != null)
		{
			String temp = ((Nameable) provider.getGoal()).getName();
			if (temp != null && temp.length() != 0)
				goalPosition.setText(temp);
		}

		searchButton = (Button) rootView.findViewById(R.id.button_search_trace);
		if (searchButton != null)
			searchButton.setOnClickListener(new RouteButtonListener(
					startPosition, goalPosition, map, outdoorMap, this));

		return rootView;
	}

	@Override
	public void onClick(View v)
	{

		/*
		 * drawingProvider = MapDrawingProvider.getInstance();
		 * 
		 * if (v == searchButton) { if (startPosition.getText().length() != 0)
		 * drawingProvider.setStart(startPosition.getAdapter().getItem(0));
		 * 
		 * if (goalPosition.getText().length() != 0)
		 * drawingProvider.setGoal(goalPosition.getAdapter().getItem(0));
		 * 
		 * goalPosition.clearFocus(); startPosition.clearFocus();
		 * 
		 * InputMethodManager imm = (InputMethodManager) this.getActivity()
		 * .getSystemService(Context.INPUT_METHOD_SERVICE);
		 * 
		 * imm.hideSoftInputFromWindow(startPosition.getWindowToken(), 0);
		 * 
		 * imm.hideSoftInputFromWindow(goalPosition.getWindowToken(), 0);
		 * 
		 * buildingInfoFragment = (BuildingInfoFragment) this.getActivity()
		 * .getFragmentManager() .findFragmentById(R.id.window_info_container);
		 * 
		 * this.getActivity().getFragmentManager().beginTransaction()
		 * .remove(buildingInfoFragment).commit(); GoogleMap map =
		 * ((MapFragment) this.getActivity() .getFragmentManager()
		 * .findFragmentById(R.id.map_outdoor_googleMap)).getMap(); LatLng zoom
		 * = new LatLng( ((WithCoordinates)
		 * drawingProvider.getStart()).getCoordX(), ((WithCoordinates)
		 * drawingProvider.getStart()).getCoordY());
		 * map.animateCamera(CameraUpdateFactory.newLatLngZoom(zoom, 17));
		 * 
		 * drawingProvider.setDrawRoute(true);
		 * 
		 * drawingProvider.drawRoute(); }
		 */

	}

	public void setStartPosition(String text)
	{
		this.startPosition.setText(text);
	}

	public void setGoalPosition(String text)
	{
		this.goalPosition.setText(text);
	}

	public SearchRouteFragment()
	{
		super();
	}

}