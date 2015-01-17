package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.poligdzie.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.j256.ormlite.dao.ForeignCollection;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.base.PoliGdzieBaseActivity;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.callbacks.MarkerAnimationFinishCallback;
import com.poligdzie.fragments.MapIndoorFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.tasks.AnimationClosureChecker;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchButtonListener extends PoliGdzieBaseClass implements
															OnClickListener
{

	private SearchAutoCompleteTextView	searchPosition;
	private PoliGdzieBaseFragment		fragment;
	private MapFragmentProvider	mapFragmentProvider;

	private void showPlaceOutdoor(Object object)
	{
		
		mapFragmentProvider.addGoogleMapFragment();
		
		MapOutdoorFragment outdoorMap = (MapOutdoorFragment) mapFragmentProvider.getGoogleMapFragment();
		GoogleMap map = ((MapFragment) fragment.getActivity().getFragmentManager()
					.findFragmentById(R.id.map_outdoor_googleMap)).getMap();
		
		((PoliGdzieBaseActivity) fragment.getActivity()).switchFragment(R.id.map_container, outdoorMap,
				OUTDOOR_MAP_TAG);
		
		((MapActivity) fragment.getActivity()).setNavigationArrowsVisibility();
		
		
		LatLng pos = getCoords(object);

		MapDrawingProvider provider = MapDrawingProvider.getInstance();
		List<Marker> markers = provider.getMarkers();

		for (Marker m : markers)
		{
			if (m.getPosition().latitude == pos.latitude
					&& m.getPosition().longitude == pos.longitude)
			{
				MarkerAnimationFinishCallback callback = new MarkerAnimationFinishCallback();
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 17),
						callback);

				AnimationClosureChecker checker = new AnimationClosureChecker(
						callback, map, m, outdoorMap, new DatabaseHelper(
								fragment.getActivity(), DATABASE_NAME, null,
								DATABASE_VERSION));
				checker.execute();
				break;
			}

		}
	}

	private ForeignCollection<Floor> getFloors(Object object)
	{
		if (object instanceof Room)
		{
			Room room = (Room) object;
			Building building = new Building();
			try
			{
				building = dbHelper.getBuildingDao().queryForId(room.getBuilding().getId());
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return building.getFloors();
		}

		if (object instanceof Unit)
		{
			Unit unit = (Unit) object;
			Building building = new Building();
			try
			{
				building = dbHelper.getBuildingDao().queryForId(unit.getBuilding().getId());
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return building.getFloors();
		}
		return null;
	}

	private String getTag(Object object)
	{
		if (object instanceof Room)
		{
			Room room = (Room) object;
			Floor floor = new Floor();
			try
			{
				floor = dbHelper.getFloorDao().queryForId(room.getFloor().getId());
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return floor.getTag();
		}

		if (object instanceof Unit)
		{
			Unit unit = (Unit) object;
			Floor floor = new Floor();
			Room office = new Room();
			try
			{
				office = dbHelper.getRoomDao().queryForId(unit.getOffice().getId());
				floor = dbHelper.getFloorDao().queryForId(office.getFloor().getId());
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return floor.getTag();
		}
		return null;
	}

	private void showPlaceIndoor(Object object)
	{
		
		mapFragmentProvider.addGoogleMapFragment();
		
		for (Floor f : getFloors(object))
		{
			MapIndoorFragment indoorMap = new MapIndoorFragment(
					f.getDrawableId(), f.getName(), f.getTag(), f.getNumber());
			if (f.getTag().equals(getTag(object)))
			{
				((PoliGdzieBaseActivity) fragment.getActivity())
						.switchFragment(R.id.map_container, indoorMap,
								indoorMap.getViewTag());
			}

		}

		((OnClickListener) fragment.getActivity()).onClick(fragment
				.getActivity().findViewById(R.layout.map_activity));
	}

	private LatLng getCoords(Object object)
	{
		if (object instanceof Building)
		{
			Building temp = (Building) object;
			return new LatLng(temp.getCoordX(), temp.getCoordY());
		}
		if (object instanceof Room)
		{
			Room temp = (Room) object;
			return new LatLng(temp.getCoordX(), temp.getCoordY());
		}
		if (object instanceof Unit)
		{
			Unit temp = (Unit) object;
			if (temp.getOffice() != null)
			{
				Room office = null;
				try
				{
					office = dbHelper.getRoomDao().queryForId(temp.getOffice().getId());
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return new LatLng(office.getCoordX(), office.getCoordY());
			} else
			{
				Building building = null;
				try
				{
					building = dbHelper.getBuildingDao().queryForId(temp.getId());
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return new LatLng(building.getCoordX(), building.getCoordY());
			}
		}
		return null;
	}

	@Override
	public void onClick(View v)
	{
		
		mapFragmentProvider.clearFragments();

		searchPosition.clearFocus();
		InputMethodManager imm = (InputMethodManager) fragment.getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchPosition.getWindowToken(), 0);

		if (validateAdapter(searchPosition))
		{
			Object object = searchPosition.getAdapter().getItem(0);
			if (object != null)
			{
				if (object instanceof Building)
				{
					showPlaceOutdoor(object);

				} else if (object instanceof Room)
				{
					showPlaceIndoor(object);

				} else if (object instanceof Unit)
				{
					Unit unit = (Unit) object;
					if (unit.getOffice() != null)
					{
						showPlaceIndoor(unit);

					} else
					{
						showPlaceOutdoor(unit);
					}
				}
			}
		} 
	}
	
	private boolean validateAdapter(SearchAutoCompleteTextView searchPoint)
	{
		if(searchPoint.getAdapter().isEmpty())
		{
			makeToast("Proszê wybraæ miejsce",fragment.getActivity());
			return false;
		}
		else return true;
	}

	public SearchButtonListener(SearchAutoCompleteTextView searchPosition,
			GoogleMap map, MapOutdoorFragment outdoorMap,
			PoliGdzieBaseFragment fragment)
	{
		this.searchPosition = searchPosition;
		this.fragment = fragment;
		this.mapFragmentProvider = MapFragmentProvider.getInstance();
		dbHelper = new DatabaseHelper(fragment.getActivity(), DATABASE_NAME, null, DATABASE_VERSION);
	}

}
