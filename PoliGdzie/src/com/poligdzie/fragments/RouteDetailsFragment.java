package com.poligdzie.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.singletons.MapDrawingProvider;

public class RouteDetailsFragment extends PoliGdzieBaseFragment implements OnClickListener
{

	private MapOutdoorFragment			outdoorMap;
	private TextView					startName;
	private TextView					goalName;
	private ImageButton					deleteButton;
	private Button						backToRouteButton;
	private SearchRouteFragment			routeFragment;
	private SearchDetailsFragment		searchDetailFragment;
	private SearchPlaceFragment	searchPlaceFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.route_description_fragment,
				container, false);

		this.startName = (TextView) rootView.findViewById(R.id.main_description);
		this.goalName = (TextView) rootView.findViewById(R.id.detail_description);
		
		deleteButton = (ImageButton) rootView.findViewById(R.id.route_description_ex_button);
		deleteButton.setOnClickListener(this);
		
		backToRouteButton = (Button) rootView.findViewById(R.id.route_description_back_to_route_button);
		backToRouteButton.setOnClickListener(this);
		
		
		return rootView;
	}

	public void setFragment(MapOutdoorFragment mapOutdoorFragment)
	{
		this.outdoorMap = mapOutdoorFragment;
	}

	public  RouteDetailsFragment()
	{
		super();
	}

	public void setTextViews(String startName, String goalName)
	{
		this.startName.setText(startName);
		this.goalName.setText(goalName);
		this.getView().setVisibility(View.VISIBLE);
		
		initFragments();
		routeFragment.getView().setVisibility(View.GONE);
		searchDetailFragment.getView().setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v)
	{
		initFragments();
		if(v == deleteButton)
		{
			this.getView().setVisibility(View.GONE);
			searchPlaceFragment.resetInput();
			
			drawingProvider = MapDrawingProvider.getInstance();
			drawingProvider.setGoal(null);
			drawingProvider.setStart(null);
			routeFragment.setGoalPosition("");
			routeFragment.setStartPosition("");
		}
		else if( v == backToRouteButton)
		{
			routeFragment.getView().setVisibility(View.VISIBLE);
		}
		
		
		
	}
	
	private void initFragments()
	{
		if(routeFragment == null)
		{
			routeFragment = (SearchRouteFragment) this.getActivity()
					.getFragmentManager().findFragmentById(
							R.id.search_route_frag);
		}
		if(searchDetailFragment == null)
		{
			searchDetailFragment = (SearchDetailsFragment) this.getActivity()
					.getFragmentManager().findFragmentById(R.id.search_description_frag);
		}
		if(searchPlaceFragment == null)
		{
			searchPlaceFragment = (SearchPlaceFragment) this.getActivity()
					.getFragmentManager().findFragmentById(R.id.search_place_frag);
		}
	}
}