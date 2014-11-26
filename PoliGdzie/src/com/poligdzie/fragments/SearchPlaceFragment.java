package com.poligdzie.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.poligdzie.R;

public class SearchPlaceFragment extends Fragment implements OnClickListener{
	
	private Button searchButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.search_place_fragment, container,
			false);
	
	FragmentTransaction transaction = getFragmentManager().beginTransaction();
	SearchFieldFragment searchField = new SearchFieldFragment("Budynek/sala"); 
    transaction.add(R.id.fragment_search_place_container, searchField); 
    transaction.commit();
    
    searchButton = (Button)rootView.findViewById(R.id.button_search_trace);
	//setCurrentDate();
	return rootView;
	}
	
	@Override
	public void onClick(View v) {
		if( v == searchButton)
		{
			
		}
	
	}
}
