package com.poligdzie.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.poligdzie.R;

public class SearchFieldFragment extends Fragment implements OnClickListener{
			
	private Button myButton;
	private String buttonText;
	
	public  SearchFieldFragment(String text) {
		buttonText = text;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_field_fragment, container,
				false);
		myButton = (Button) rootView.findViewById(R.id.searchButton);
		myButton.setOnClickListener(this);
		myButton.setText(buttonText);
		//setCurrentDate();
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
