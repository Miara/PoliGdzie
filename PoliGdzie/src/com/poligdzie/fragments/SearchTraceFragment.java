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

public class SearchTraceFragment extends Fragment implements OnClickListener{
	
	private Button myButton;
	private String buttonText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.search_trace_fragment, container,
			false);

	FragmentTransaction transaction = getFragmentManager().beginTransaction();
	SearchFieldFragment searchField = new SearchFieldFragment("Punkt startowy"); 
	SearchFieldFragment searchField2 = new SearchFieldFragment("Punkt docelowy"); 
    transaction.add(R.id.fragment_search_trace_container, searchField); 
    transaction.add(R.id.fragment_search_trace_container, searchField2); 
    transaction.commit();
	//setCurrentDate();
	return rootView;
	}
	
	@Override
	public void onClick(View v) {
	// TODO Auto-generated method stub
	
	}
}