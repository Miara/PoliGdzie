package com.poligdzie.fragments;

import java.sql.SQLException;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poligdzie.R;
import com.poligdzie.adapters.CustomListAdapter;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;

public class SearchBuildingsFragment extends Fragment implements
		OnClickListener, Constants {

	private Button myButton;
	private String buttonText;
	private List<Building> buildings;

	ListView list;
	  String[] web = {
	    "Centrum wyk³adowe",
	      "Budynek elektryczny",
	      "Budowa maszyn"
	  } ;
	  Integer[] imageId = {
	      R.drawable.cw_ic,
	      R.drawable.we_ic,
	      R.drawable.bm_ic
	  };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_buildings_fragment,
				container, false);

		CustomListAdapter adapter = new CustomListAdapter(getActivity(), web, imageId);
		    list=(ListView)rootView.findViewById(R.id.building_list_view);
		    list.setAdapter(adapter);
		    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(getActivity(), "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                }
            });
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
