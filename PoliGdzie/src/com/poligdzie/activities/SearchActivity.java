package com.poligdzie.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.poligdzie.R;
import com.poligdzie.fragments.SearchBuildingsFragment;
import com.poligdzie.fragments.SearchPlaceFragment;
import com.poligdzie.fragments.SearchRouteFragment;

public class SearchActivity extends PoliGdzieBaseActivity implements OnClickListener {

	
	//TODO: wywalic
	private Button buttonTrace;
	private Button buttonPlace;
	private Button buttonBuilding;

	

	SearchRouteFragment fragment_trace;
	SearchPlaceFragment fragment_place;
	SearchBuildingsFragment fragment_building;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);

		fragment_trace = new SearchRouteFragment();
		switchFragment(R.id.fragment_search_container, fragment_trace, "trace");
		fragment_place = new SearchPlaceFragment();
		fragment_building = new SearchBuildingsFragment();

		buttonTrace = (Button) findViewById(R.id.button_trace);
		buttonTrace.setOnClickListener(this);
		buttonPlace = (Button) findViewById(R.id.button_place);
		buttonPlace.setOnClickListener(this);
		buttonBuilding = (Button) findViewById(R.id.button_building);
		buttonBuilding.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == buttonTrace) {
			switchFragment(R.id.fragment_search_container, fragment_trace,
					"trace");

		}
		if (v == buttonPlace) {
			switchFragment(R.id.fragment_search_container, fragment_place,
					"place");

		}
		if (v == buttonBuilding) {
			switchFragment(R.id.fragment_search_container, fragment_building,
					"building");
		}

	}

	public SearchActivity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
