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
import android.widget.Toast;

import com.example.poligdzie.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.helpers.GPSHelper;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.interfaces.WithCoordinates;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.listeners.OnRouteButtonListener;
import com.poligdzie.persistence.GPSLocation;
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

	private ImageButton					startDeleteButton;
	private ImageButton					goalDeleteButton;
	private ImageButton					switchPositionButton;
	private Button						GPSButton;
	
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
			searchButton.setOnClickListener(new OnRouteButtonListener(
					startPosition, goalPosition, map, outdoorMap, this));
		switchPositionButton = (ImageButton) rootView.findViewById(R.id.search_route_switch_positions);
		if (switchPositionButton != null)
			switchPositionButton.setOnClickListener(this);
		
		GPSButton = (Button) rootView.findViewById(R.id.search_route_gps_button);
		if (GPSButton != null)
			GPSButton.setOnClickListener(this);
		
		startDeleteButton = (ImageButton) rootView.findViewById(R.id.search_route_start_ex_button);
		if (startDeleteButton != null)
			startDeleteButton.setOnClickListener(this);
		
		goalDeleteButton = (ImageButton) rootView.findViewById(R.id.search_route_goal_ex_button);
		if (goalDeleteButton != null)
			goalDeleteButton.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v)
	{
		
		drawingProvider = MapDrawingProvider.getInstance();
		
		if(v == switchPositionButton)
		{
			
			
			Object newStartObj = null;
			Object newGoalObj = null;
			String newStartName = "";
			String newGoalName = "";
			
			
			if(startPosition.getAdapter() != null)
			{
				if(!startPosition.getAdapter().isEmpty())
				{
					newGoalObj = startPosition.getAdapter().getItem(0);
					newGoalName = ((Nameable)newGoalObj).getName();
				}
			}
			
			
			if(goalPosition.getAdapter() != null)
			{
				if(!goalPosition.getAdapter().isEmpty())
				{
					newStartObj = goalPosition.getAdapter().getItem(0);
					newStartName = ((Nameable)newStartObj).getName();
					
				}
			}
			
			if(newStartObj == null)
			{
				startPosition.setText("");
				startPosition.setAdapter(null);
			}
			else
			{
				drawingProvider.setStart(newStartObj);
				this.setStartPosition(((Nameable)newStartObj).getName());
			}
			
			if(newGoalObj == null)
			{
				goalPosition.setText("");
				goalPosition.setAdapter(null);
			}
			else
			{
				drawingProvider.setGoal(newGoalObj);
				this.setGoalPosition(((Nameable)newGoalObj).getName());
			}
			
	
		}
		
		if(v == startDeleteButton)
		{
			this.setStartPosition("");
			startPosition.setAdapter(null);
		}
		
		if(v == goalDeleteButton)
		{
			this.setGoalPosition("");
			goalPosition.setAdapter(null);
		}
		
		if( v == GPSButton)
		{
			GPSHelper gpsHelper = new GPSHelper(getActivity());
			if(gpsHelper.canGetLocation())
			{
                double latitude = gpsHelper.getLatitude();
                double longitude = gpsHelper.getLongitude();
                
                if(latitude < GPS_MIN_LATITUDE || latitude > GPS_MAX_LATITUDE 
                		|| longitude < GPS_MIN_LONGITUDE || longitude > GPS_MAX_LONGITUDE)
                {
                	Toast.makeText(getActivity(), "Nie mo¿na pobraæ pozycji, poniewa¿ znajdujesz siê poza kampusem Piotrowo"
                			,Toast.LENGTH_LONG).show();
                	return;
                }
                GPSLocation location = new GPSLocation(GPS_LOCATION_STRING,longitude,latitude);
                //GPSLocation location = new GPSLocation(GPS_LOCATION_STRING,52.401816, 16.948690);
                if(goalPosition.isFocused())
                {
                	drawingProvider.setGPS(location);
    				this.setGoalPosition(location.getName());
                }
                else
                {
                	drawingProvider.setGPS(location);
    				this.setStartPosition(location.getName());
                }
			}
			else
            {
            	gpsHelper.showSettingsAlert();
            	drawingProvider.setGPS(null);
            }
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