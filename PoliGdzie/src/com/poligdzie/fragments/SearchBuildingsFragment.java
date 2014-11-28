package com.poligdzie.fragments;

import java.sql.SQLException;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;

public class SearchBuildingsFragment extends Fragment implements
		OnClickListener, Constants {

	private Button myButton;
	private String buttonText;
	private List<Building> buildings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_buildings_fragment,
				container, false);

		TextView testBazy = (TextView) rootView.findViewById(R.id.test_label);
		DatabaseHelper database = new DatabaseHelper(this.getActivity(),
				DATABASE_NAME, null, DATABASE_VERSION);
		try {
			buildings = database.getBuildingDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// contentCreator.getBuildings();

		if (buildings.isEmpty() || buildings == null) {
			testBazy.setText("Brak budynków w bazie");
		} else {
			testBazy.setText("Budynki w bazie :\n");
			for (Building b : buildings) {
				testBazy.append(b.getName());
				testBazy.append("\n");
			}
		}
		// setCurrentDate();
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
