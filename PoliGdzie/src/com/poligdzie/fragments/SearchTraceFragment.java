package com.poligdzie.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.listeners.ContextSearchTextWatcher;

public class SearchTraceFragment extends Fragment implements OnClickListener,
		Constants {

	private Button searchButton;

	private AutoCompleteTextView startingPosition;
	private AutoCompleteTextView goalPosition;
	
	private TextView startingPointAutocomplete;
	private TextView goalPointAutocomplete;
	
	private ContextSearchTextWatcher startWatcher;
	private ContextSearchTextWatcher goalWatcher;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_trace_fragment,
				container, false);

		searchButton = (Button) rootView.findViewById(R.id.button_search_trace);

		if (searchButton != null)
			searchButton.setOnClickListener(this);

		startingPosition = (AutoCompleteTextView) rootView.findViewById(R.id.starting_point_text_edit);
		startWatcher = new ContextSearchTextWatcher(startingPosition, this.getActivity());
		startingPosition.addTextChangedListener(startWatcher);
		
		goalPosition = (AutoCompleteTextView) rootView.findViewById(R.id.goal_point_text_edit);
		goalWatcher = new ContextSearchTextWatcher(goalPosition, this.getActivity());
		goalPosition.addTextChangedListener(goalWatcher);
		
	
		return rootView;
	}

	@Override
	public void onClick(View v) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		
		Editor editor = prefs.edit();
		
		if (v == searchButton) {
			Intent intent = new Intent(getActivity(), MapActivity.class);
			startActivity(intent);
		}
		
		editor.commit();

	}


}