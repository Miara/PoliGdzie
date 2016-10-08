package com.poligdzie.fragments;

import java.sql.SQLException;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.poligdzie.base.PoliGdzieBaseActivity;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.Room;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchDetailsFragment extends PoliGdzieBaseFragment implements OnClickListener
{

	private MapOutdoorFragment			outdoorMap;
	private TextView					name;
	private TextView					details;
	private ImageButton					fromButton;
	private ImageButton					toButton;
	private ImageButton					infoButton;
	private ImageButton					insideButton;
	private SearchRouteFragment			routeFragment;
	private SearchPlaceFragment			searchPlaceFragment;
	private BuildingInfoFragment		buildingInfoFragment;
	private Object						object;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.search_description_fragment,
				container, false);

		this.name = (TextView) rootView.findViewById(R.id.search_main_description);
		this.details = (TextView) rootView.findViewById(R.id.search_detail_description);
		
		fromButton = (ImageButton) rootView.findViewById(R.id.search_description_route_from_button);
		fromButton.setOnClickListener(this);
		toButton = (ImageButton) rootView.findViewById(R.id.search_description_route_to_button);
		toButton.setOnClickListener(this);
		infoButton = (ImageButton) rootView.findViewById(R.id.search_description_route_info_button);
		infoButton.setOnClickListener(this);
		insideButton = (ImageButton) rootView.findViewById(R.id.search_description_route_inside_button);
		insideButton.setOnClickListener(this);
		
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

	public void setTextViews(String name, String details, Object obj)
	{
		this.name.setText(name);
		this.details.setText(details);
		this.getView().setVisibility(View.VISIBLE);
		this.object = obj;
		if(object instanceof Building)
		{
			if(((Building)object).getRooms().size() > 0)
				insideButton.setVisibility(View.VISIBLE);
			else
				insideButton.setVisibility(View.GONE);
			
			infoButton.setVisibility(View.VISIBLE);
			//insideButton.setVisibility(View.VISIBLE);
			
		}
		else
		{
			infoButton.setVisibility(View.GONE);
			insideButton.setVisibility(View.GONE);
		}
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
		
		if(buildingInfoFragment == null)
		{
			buildingInfoFragment = (BuildingInfoFragment) this.getActivity()
					.getFragmentManager().findFragmentById(R.id.building_info_frag);
		}
		
		drawingProvider = MapDrawingProvider.getInstance();
		
		if( v == fromButton)
		{
			routeFragment.getView().setVisibility(View.VISIBLE);
			if(this.object != null)
			{
				drawingProvider.setStart(this.object);
				String name = new String();
				name += ((Nameable) this.object).getName();
				
//				try
//				{
//					if(object instanceof Room) 
//					{
//					Building building = dbHelper.getBuildingDao().
//							queryForId(((Room)this.object).getBuilding().getId());
//					name += " (" + building.getName() + ")";
//					}
//				} catch (SQLException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
					
				
				routeFragment.setStartPosition(name);
			}
		}
		else if( v == toButton)
		{
			routeFragment.getView().setVisibility(View.VISIBLE);
			if(this.object != null)
			{
				drawingProvider.setGoal(this.object);
				
				String name = new String();
				name += ((Nameable) this.object).getName();
				
//				try
//				{
//					if(object instanceof Room) 
//					{
//					Building building = dbHelper.getBuildingDao().
//							queryForId(((Room)this.object).getBuilding().getId());
//					name += " (" + building.getName() + ")";
//					}
//				} catch (SQLException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				routeFragment.setGoalPosition(name);
			}
		}
		else if( v == infoButton)
		{
			
			buildingInfoFragment.setViews((Building)object);
		}
		else if( v == insideButton)
		{

				mapProvider.clearFragments();
				mapProvider.addGoogleMapFragment();

				int i = 0;
				Building building = (Building)object;
				MapIndoorFragment indoorMap;
				for (Floor f : building.getFloors())
				{
					Log.d("POLIGDZIE", f.getName());
					indoorMap = new MapIndoorFragment(f.getDrawableId(),
							f.getName(), f.getTag(), f.getId());
					if (i == 0)
					{
						((PoliGdzieBaseActivity) this.getActivity())
								.switchFragment(R.id.map_container, indoorMap,
										indoorMap.getViewTag());
					}
					i++;
				}
				this.getView().setVisibility(View.GONE);

				((OnClickListener) this.getActivity()).onClick(this.getActivity()
						.findViewById(R.layout.map_activity));
		}

		

		
	}
}