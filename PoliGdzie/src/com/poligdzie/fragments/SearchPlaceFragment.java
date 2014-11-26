package com.poligdzie.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.poligdzie.R;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.activities.PromptActivity;
import com.poligdzie.interfaces.Constants;

public class SearchPlaceFragment extends Fragment implements OnClickListener,Constants{
	
	private Button searchButton;
	private Button searchPoint ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.search_place_fragment, container,
			false);
	   
    searchPoint = (Button)rootView.findViewById(R.id.button_search_point);
    if(searchPoint != null)searchPoint.setOnClickListener(this);
    searchButton = (Button)rootView.findViewById(R.id.button_search_place);
    if(searchButton != null)searchButton.setOnClickListener(this);
    
	//setCurrentDate();
	return rootView;
	}
	
	@Override
	public void onClick(View v) {
		SharedPreferences prefs = PreferenceManager.
				getDefaultSharedPreferences(getActivity().getApplicationContext());
		Editor editor = prefs.edit();
		if( v == searchButton)
		{
			Intent intent = new Intent(getActivity(), MapActivity.class);
	        startActivity(intent);
		}
		if( v == searchPoint)
		{
			editor.putString(PROMPT_MODE,PROMPT_MODE_SEARCH);
			Intent intent = new Intent(getActivity(), PromptActivity.class);
	        startActivity(intent);
		}
		editor.commit();
	}
}
