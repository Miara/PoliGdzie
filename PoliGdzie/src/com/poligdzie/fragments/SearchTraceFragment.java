package com.poligdzie.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.poligdzie.R;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.RouteProvider;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchTraceFragment extends Fragment implements OnClickListener,
		Constants {

	private Button searchButton;
	private Button buttonMapStart;
	private Button buttonMapGoal;

	private SearchAutoCompleteTextView startPosition;
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


		startPosition = (SearchAutoCompleteTextView) rootView
				.findViewById(R.id.starting_point_text_edit);
		startWatcher = new ContextSearchTextWatcher(startPosition,
				this.getActivity());
		startPosition.addTextChangedListener(startWatcher);
		goalPosition = (SearchAutoCompleteTextView) rootView
				.findViewById(R.id.goal_point_text_edit);
		goalWatcher = new ContextSearchTextWatcher(goalPosition,
				this.getActivity());
		goalPosition.addTextChangedListener(goalWatcher);

		
		RouteProvider provider = RouteProvider.getInstance();
		
		
		if (provider.getStart() != null) {
			String temp = ((Nameable) provider.getStart()).getName();
			if (temp != null && temp.length() != 0)
				startPosition.setText(temp);
		}
		
		if (provider.getGoal() != null) {
			String temp = ((Nameable) provider.getGoal()).getName();
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
		
		
		if (v == searchButton) {
			if(startPosition.getText().length() != 0) 
				provider.setStart(startPosition.getAdapter().getItem(0));
			
			if(goalPosition.getText().length() != 0)
				provider.setGoal(goalPosition.getAdapter().getItem(0));
			
			provider.setDrawRoute(true);
			
			provider.drawRoute();
		}

		editor.commit();

	}
	
	public void setStartPosition(String text) {
		this.startPosition.setText(text);
	}
	
	public void setGoalPosition(String text) {
		this.goalPosition.setText(text);
	}

}