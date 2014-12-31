package com.poligdzie.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.RouteProvider;
import com.poligdzie.singletons.TextEditDataProvider;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchTraceFragment extends Fragment implements OnClickListener,
		Constants {

	private Button searchButton;
	private Button buttonMapStart;
	private Button buttonMapStop;

	private SearchAutoCompleteTextView startingPosition;
	private SearchAutoCompleteTextView goalPosition;

	private ContextSearchTextWatcher startWatcher;
	private ContextSearchTextWatcher goalWatcher;

	private DatabaseHelper dbHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_trace_fragment,
				container, false);

		searchButton = (Button) rootView.findViewById(R.id.button_search_trace);
		if (searchButton != null)
			searchButton.setOnClickListener(this);
		buttonMapStart = (Button) rootView
				.findViewById(R.id.button_search_starting_point_map);
		if (buttonMapStart != null)
			buttonMapStart.setOnClickListener(this);
		buttonMapStop = (Button) rootView
				.findViewById(R.id.button_search_goal_point_map);
		if (buttonMapStop != null)
			buttonMapStop.setOnClickListener(this);

		startingPosition = (SearchAutoCompleteTextView) rootView
				.findViewById(R.id.starting_point_text_edit);
		startWatcher = new ContextSearchTextWatcher(startingPosition,
				this.getActivity());
		startingPosition.addTextChangedListener(startWatcher);
		goalPosition = (SearchAutoCompleteTextView) rootView
				.findViewById(R.id.goal_point_text_edit);
		goalWatcher = new ContextSearchTextWatcher(goalPosition,
				this.getActivity());
		goalPosition.addTextChangedListener(goalWatcher);

		TextEditDataProvider provider = TextEditDataProvider.getInstance();
		if (provider.getFrom() != null) {
			String temp = ((Nameable) provider.getFrom()).getName();
			if (temp != null && temp.length() != 0)
				startingPosition.setText(temp);
		}
		
		if (provider.getTo() != null) {
			String temp = ((Nameable) provider.getTo()).getName();
			if (temp != null && temp.length() != 0)
				goalPosition.setText(temp);
		}

		dbHelper = new DatabaseHelper(this.getActivity(), DATABASE_NAME, null,
				DATABASE_VERSION);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());

		Editor editor = prefs.edit();
		RouteProvider provider = RouteProvider.getInstance();
		TextEditDataProvider dataProvider = TextEditDataProvider.getInstance();
		
		if (v == searchButton) {
			if(startingPosition.getText().length() != 0) 
				provider.setFrom(startingPosition.getAdapter().getItem(0));
			
			if(goalPosition.getText().length() != 0)
				provider.setTo(goalPosition.getAdapter().getItem(0));
			
			Intent intent = new Intent(getActivity(), MapActivity.class);
			startActivity(intent);
		}
		if (v == buttonMapStart) {
			Intent intent = new Intent(getActivity(), MapActivity.class);
			startActivity(intent);
		}
		
		if (v == buttonMapStop) {
			Intent intent = new Intent(getActivity(), MapActivity.class);
			startActivity(intent);
		}

		editor.commit();

	}

}