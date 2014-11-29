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
import android.widget.EditText;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.activities.PromptActivity;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.listeners.ContextSearchTextWatcher;

public class SearchPlaceFragment extends Fragment implements OnClickListener,
		Constants {

	private Button searchButton;
	private ContextSearchTextWatcher searchWatcher;
	private EditText searchPosition;
	private TextView searchPointAutocomplete;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_place_fragment,
				container, false);


		
		searchPosition = (EditText) rootView.findViewById(R.id.search_point_text_edit);
		searchPointAutocomplete = (TextView) rootView.findViewById(R.id.search_point_autocomplete);
		searchWatcher = new ContextSearchTextWatcher(searchPosition, searchPointAutocomplete);
		searchPosition.addTextChangedListener(searchWatcher);
		
		searchButton = (Button) rootView.findViewById(R.id.button_search_place);
		if (searchButton != null)
			searchButton.setOnClickListener(this);

		// setCurrentDate();
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
		/*if (v == searchPoint) {
			editor.putString(PROMPT_MODE, PROMPT_MODE_SEARCH);
			Intent intent = new Intent(getActivity(), PromptActivity.class);
			startActivity(intent);
		}*/
		editor.commit();
	}
}
