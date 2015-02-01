package com.poligdzie.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.poligdzie.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.interfaces.WithCoordinates;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.listeners.RouteButtonListener;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchRouteFragment extends PoliGdzieBaseFragment implements
																OnClickListener
{
//TODO : przy wpisaniu "!" w wyszukiwaniu trasy wyskakuje b³¹d
	private Button						searchButton;

	private SearchAutoCompleteTextView	startPosition;
	private SearchAutoCompleteTextView	goalPosition;

	private ContextSearchTextWatcher	startWatcher;
	private ContextSearchTextWatcher	goalWatcher;

	private BuildingInfoFragment		buildingInfoFragment;
	private ImageButton					switchFragmentButton;
	private ImageButton					deleteTextButton;
	private ImageButton					switchPositionButton;
	private GoogleMap					map;
	private MapOutdoorFragment			outdoorMap;
	private SearchPlaceFragment			searchFragment;

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

		
		startPosition.clearFocus();
		goalPosition.clearFocus();
		
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
		switchPositionButton = (ImageButton) rootView.findViewById(R.id.search_route_switch_positions);
		if (switchPositionButton != null)
			switchPositionButton.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v)
	{
		if(v == switchPositionButton)
		{
			drawingProvider = MapDrawingProvider.getInstance();
			
			Object newStartObj = null;
			String newStartName = "";
			Object newGoalObj = null;
			String newGoalName = "";
			//TODO: poprawic switcha przy pustych znakach
			if(!goalPosition.getAdapter().isEmpty())
			{
				newStartObj = goalPosition.getAdapter().getItem(0);
				newStartName = ((Nameable)newStartObj).getName();
			}
			
			if(!startPosition.getAdapter().isEmpty())
			{
				newGoalObj = startPosition.getAdapter().getItem(0);
				newGoalName = ((Nameable)newGoalObj).getName();
			}
			
			drawingProvider.setStart(newStartObj);
			drawingProvider.setGoal(newGoalObj);
			
			
			this.setGoalPosition(newGoalName);
			this.setStartPosition(newStartName);
		}

		
		   

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