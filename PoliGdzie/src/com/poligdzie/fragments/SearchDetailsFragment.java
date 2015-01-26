package com.poligdzie.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchDetailsFragment extends PoliGdzieBaseFragment implements OnClickListener
{

	private MapOutdoorFragment			outdoorMap;
	private TextView					name;
	private TextView					details;
	private ImageButton					routeButton;
	private SearchRouteFragment			routeFragment;
	private SearchPlaceFragment	searchPlaceFragment;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.search_description_fragment,
				container, false);

		this.name = (TextView) rootView.findViewById(R.id.main_description);
		this.details = (TextView) rootView.findViewById(R.id.detail_description);
		
		routeButton = (ImageButton) rootView.findViewById(R.id.search_description_route_button);
		routeButton.setOnClickListener(this);
		
		return rootView;
	}

	public void setFragment(MapOutdoorFragment mapOutdoorFragment)
	{
		this.outdoorMap = mapOutdoorFragment;
	}

	public  SearchDetailsFragment()
	{
		super();
	}

	public void setTextViews(String name, String details)
	{
		this.name.setText(name);
		this.details.setText(details);
		this.getView().setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v)
	{
		if(routeFragment == null)
		{
			routeFragment = (SearchRouteFragment) this.getActivity()
					.getFragmentManager().findFragmentById(
							R.id.search_route_frag);
		}
		if(searchPlaceFragment == null)
		{
			searchPlaceFragment = (SearchPlaceFragment) this.getActivity()
					.getFragmentManager().findFragmentById(R.id.search_place_frag);
		}

		drawingProvider = MapDrawingProvider.getInstance();

		routeFragment.getView().setVisibility(View.VISIBLE);

		Object obj = searchPlaceFragment.getSearchPosition().getAdapter().getItem(0);
		
		drawingProvider.setGoal(obj);
		routeFragment.setGoalPosition(((Nameable) obj).getName());

		
	}
}