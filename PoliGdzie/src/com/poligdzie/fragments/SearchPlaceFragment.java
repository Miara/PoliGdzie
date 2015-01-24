package com.poligdzie.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.listeners.FragmentSwitchListener;
import com.poligdzie.listeners.SearchButtonListener;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchPlaceFragment extends PoliGdzieBaseFragment
{

	private ImageButton						searchButton;
	private ContextSearchTextWatcher	searchWatcher;
	private ImageButton						switchFragmentButton;
	private SearchAutoCompleteTextView	searchPosition;
	private GoogleMap					map;
	private MapOutdoorFragment			outdoorMap;
	private SearchRouteFragment	routeFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.search_place_fragment,
				container, false);

		searchPosition = (SearchAutoCompleteTextView) rootView
				.findViewById(R.id.search_point_text_edit);
		searchWatcher = new ContextSearchTextWatcher(searchPosition,
				this.getActivity());
		searchPosition.addTextChangedListener(searchWatcher);

		

		switchFragmentButton = (ImageButton) rootView
				.findViewById(R.id.button_search_switch_fragment);
		switchFragmentButton.setOnClickListener(new FragmentSwitchListener(this.getActivity()));

		searchButton = (ImageButton) rootView.findViewById(R.id.button_search_place);

		if (searchButton != null)
			searchButton.setOnClickListener(new SearchButtonListener(
					searchPosition, map, outdoorMap, this));

		return rootView;
	}

	public void setFragment(MapOutdoorFragment mapOutdoorFragment)
	{
		this.outdoorMap = mapOutdoorFragment;
	}

	public SearchPlaceFragment()
	{
		super();
	}
}
