package com.poligdzie.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import com.example.poligdzie.R;

public class SearchFieldFragment extends Fragment implements OnClickListener{
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_field_fragment, container,
				false);
		
		//setCurrentDate();
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
