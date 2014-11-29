package com.poligdzie.fragments;

import java.sql.SQLException;
import java.util.List;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
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
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class SearchTraceFragment extends Fragment implements OnClickListener,
		Constants {

	private Button searchButton;
/*	private Button startPoint;
	private Button stopPoint;*/
	

	private EditText startingPosition;
	private EditText goalPosition;
	
	private TextView startingPointAutocomplete;
	private TextView goalPointAutocomplete;
	
	private List <Building> buildings;
	private List <Unit> units;
	private List <Room> rooms;
	private DatabaseHelper dbHelper;
	private ContextSearchTextWatcher startWatcher;
	private ContextSearchTextWatcher goalWatcher;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_trace_fragment,
				container, false);

		
		
		
	/*	startPoint = (Button) rootView.findViewById(R.id.button_start_point);
		if (startPoint != null)
			startPoint.setOnClickListener(this);
		stopPoint = (Button) rootView.findViewById(R.id.button_stop_point);
		if (stopPoint != null)
			stopPoint.setOnClickListener(this);*/
		searchButton = (Button) rootView.findViewById(R.id.button_search_trace);
		if (searchButton != null)
			searchButton.setOnClickListener(this);

		startingPosition = (EditText) rootView.findViewById(R.id.starting_point_text_edit);
		startingPointAutocomplete = (TextView) rootView.findViewById(R.id.starting_point_autocomplete);
		startWatcher = new ContextSearchTextWatcher(startingPosition, startingPointAutocomplete);
		startingPosition.addTextChangedListener(startWatcher);
		
		goalPosition = (EditText) rootView.findViewById(R.id.goal_point_text_edit);
		goalPointAutocomplete = (TextView) rootView.findViewById(R.id.goal_point_autocomplete);
		goalWatcher = new ContextSearchTextWatcher(goalPosition, goalPointAutocomplete);
		goalPosition.addTextChangedListener(goalWatcher);
		
		
		/*int width = startingPosition.getWidth();
		Log.i("POLIGDZIE","width:"+ width);
		int height = startingPosition.getHeight();
		Log.i("POLIGDZIE","height:"+ height);
		Drawable icon = getActivity().getResources().getDrawable(R.drawable.z_gps);
		icon.setBounds(0, 0, width, height);
		startingPosition.setCompoundDrawables(icon,null,null,null);*/
		
		// startPoint = (Button)rootView.findViewById(R.id.button_search_trace);
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
		
	/*	if (v == startPoint) {
			editor.putString(PROMPT_MODE, PROMPT_MODE_START);
			Intent intent = new Intent(getActivity(), PromptActivity.class);
			startActivity(intent);
		}
		
		if (v == stopPoint) {
			editor.putString(PROMPT_MODE, PROMPT_MODE_STOP);
			Intent intent = new Intent(getActivity(), PromptActivity.class);
			startActivity(intent);
		}*/
		
		editor.commit();

	}


}