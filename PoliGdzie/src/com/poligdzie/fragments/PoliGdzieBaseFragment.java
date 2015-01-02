package com.poligdzie.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.singletons.RouteProvider;

public abstract class PoliGdzieBaseFragment extends Fragment implements Constants{
	protected DatabaseHelper dbHelper;
	protected RouteProvider routeProvider;
	protected MapFragmentProvider mapProvider;
	
	public PoliGdzieBaseFragment() {
		setUp();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("POLIGDZIE", this.getActivity().toString());
		setUp();
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	private void setUp() {
		
		dbHelper = new DatabaseHelper(this.getActivity(), DATABASE_NAME, null, DATABASE_VERSION);
		routeProvider = RouteProvider.getInstance();
		mapProvider = MapFragmentProvider.getInstance();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setUp();
		super.onActivityCreated(savedInstanceState);
		
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
