package com.poligdzie.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.example.poligdzie.R;
import com.poligdzie.interfaces.Constants;

public class BuildingInfoFragment extends Fragment implements OnClickListener,
Constants {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) 
	{
	View rootView = inflater.inflate(R.layout.window_marker_click,
			container, false);
	
	return rootView;
	}
	
	@Override
	public void onClick(View v) 
	{
	
	}
}