package com.poligdzie.activities;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.poligdzie.R;
import com.poligdzie.fragments.SearchBuildingsFragment;
import com.poligdzie.fragments.SearchPlaceFragment;
import com.poligdzie.fragments.SearchTraceFragment;
import com.poligdzie.interfaces.Constants;

public class SearchActivity extends Activity implements OnClickListener,Constants{

	private Button goToMapButton;
	private Button buttonTrace;
	private Button buttonPlace;
	private Button buttonBuilding;
	
	private String lastTag;
	
	SearchTraceFragment fragment_trace;
	SearchPlaceFragment fragment_place;
	SearchBuildingsFragment fragment_building;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        
        
        fragment_trace = new SearchTraceFragment();
        switchFragment(R.id.fragment_search_container, fragment_trace, "trace");
        fragment_place = new SearchPlaceFragment();		
		fragment_building = new SearchBuildingsFragment(); 
            
        buttonTrace = (Button) findViewById(R.id.button_trace);
        buttonTrace.setOnClickListener(this);
        buttonPlace = (Button) findViewById(R.id.button_place);
        buttonPlace.setOnClickListener(this);
        buttonBuilding = (Button) findViewById(R.id.button_building);
        buttonBuilding.setOnClickListener(this);
        goToMapButton = (Button) findViewById(R.id.goToMapButton);
        goToMapButton.setOnClickListener(this);
        
    }
    
    @Override
	public void onClick(View v) {
		if( v == goToMapButton)
		{
			Intent intent = new Intent(this, MapActivity.class);
	        startActivity(intent);
		}
		if( v == buttonTrace)
		{
			switchFragment(R.id.fragment_search_container, fragment_trace, "trace");

		}
		if( v == buttonPlace)
		{
			switchFragment(R.id.fragment_search_container, fragment_place, "place");
			
		}
		if( v == buttonBuilding)
		{
			switchFragment(R.id.fragment_search_container, fragment_building, "building");
		}
		
    }
    
    protected void switchFragment( int resource, Fragment fragment, String tag ) {

    	String oldTag = getLastTag();
    	boolean addToBackStack=false;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if ( oldTag != null ) 
        {
            Fragment lastFragment = fragmentManager.findFragmentByTag( oldTag );
            if ( lastFragment != null ) 
            {
                transaction.hide( lastFragment );
            }
        }

        if ( fragment.isAdded() ) 
        {
            transaction.show( fragment );
        }
        else 
        {
            transaction.add( resource, fragment, tag );
            addToBackStack=true;
        }

        if ( addToBackStack ) 
        {
            transaction.addToBackStack( tag );
        }

        transaction.commit();
        setLastTag(tag);
    }

	public String getLastTag() {
		return lastTag;
	}

	public void setLastTag(String lastTag) {
		this.lastTag = lastTag;
	}
		
}
