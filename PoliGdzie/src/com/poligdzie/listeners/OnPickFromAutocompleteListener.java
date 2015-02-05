package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

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
import com.poligdzie.fragments.SearchDetailsFragment;
import com.poligdzie.fragments.SearchPlaceFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.WithCoordinates;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.tasks.AnimationClosureChecker;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class OnPickFromAutocompleteListener extends PoliGdzieBaseClass implements
															OnItemClickListener
{

	private SearchAutoCompleteTextView	searchPosition;
	private SearchPlaceFragment		fragment;
	private MapFragmentProvider			mapFragmentProvider;
	private DatabaseHelper 				dbHelper;

	private void showPlaceOutdoor(Object object)
	{

		mapFragmentProvider.addGoogleMapFragment();

		MapOutdoorFragment outdoorMap = (MapOutdoorFragment) mapFragmentProvider
				.getGoogleMapFragment();
		GoogleMap map = ((MapFragment) fragment.getActivity()
				.getFragmentManager()
				.findFragmentById(R.id.map_outdoor_googleMap)).getMap();

		((PoliGdzieBaseActivity) fragment.getActivity()).switchFragment(
				R.id.map_container, outdoorMap, OUTDOOR_MAP_TAG);

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

	private List<Floor> getFloors(Object object) throws SQLException
	{
		List<Floor> floors ;
		if (object instanceof Room)
		{
			Room room = dbHelper.getRoomDao().queryForId(((Room) object).getId());
			floors = dbHelper.getFloorDao().queryBuilder().where().
					eq("building_id", room.getBuilding().getId()).query();
			return floors;
		}

		if (object instanceof Unit)
		{
			Unit unit = dbHelper.getUnitDao().queryForId(((Unit) object).getId());
			floors = dbHelper.getFloorDao().queryBuilder().where().
					eq("building_id", unit.getBuilding().getId()).query();
			return floors;
		}
		return null;
	}

	private String getTag(Object object) throws SQLException
	{
		if (object instanceof Room)
		{
			Room room = dbHelper.getRoomDao().queryForId(((Room) object).getId());
			Floor floor = dbHelper.getFloorDao().queryForId(room.getFloor().getId());
			return floor.getTag();
		}

		if (object instanceof Unit)
		{
			Unit unit = dbHelper.getUnitDao().queryForId(((Unit) object).getId());
			Room room = dbHelper.getRoomDao().queryForId(unit.getOffice().getId());
			Floor floor = dbHelper.getFloorDao().queryForId(room.getFloor().getId());
			return floor.getTag();
		}
		return null;
	}

	private void showPlaceIndoor(Object object)
	{
		try
		{
		mapFragmentProvider.addGoogleMapFragment();

		for (Floor f : getFloors(object))
		{
			echo("test");
			echo(f.getTag());
			if (f.getTag().equals(getTag(object)))
			{
				echo("TEST");
				Room room = dbHelper.getRoomDao().queryForId(((Room)object).getId());
				int x = room.getCoordX();
				int y = room.getCoordY();
				int radius = room.getRadius()*2;
				echo("TEST2");
				MapIndoorFragment indoorMap = new MapIndoorFragment(
						f.getDrawableId(), f.getName(), f.getTag(), f.getId(),x,y,radius);
				((PoliGdzieBaseActivity) fragment.getActivity())
						.switchFragment(R.id.map_container, indoorMap,
								indoorMap.getViewTag());
				echo("TEST3");
			}
			else
			{
				echo("TEST4");
				MapIndoorFragment indoorMap = new MapIndoorFragment(
						f.getDrawableId(), f.getName(), f.getTag(), f.getId());
			}

		}

		((OnClickListener) fragment.getActivity()).onClick(fragment
				.getActivity().findViewById(R.layout.map_activity));
		}
		catch(Exception e)
		{
			Log.e("Poligdzie","Search Button listener ERROR");
		}
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
				return new LatLng(temp.getOffice().getCoordX(), temp
						.getOffice().getCoordY());
			} else
			{
				return new LatLng(temp.getBuilding().getCoordX(), temp
						.getBuilding().getCoordY());
			}
		}
		return null;
	}

	private boolean validateAdapter(SearchAutoCompleteTextView searchPoint)
	{
		if (searchPoint.getAdapter().isEmpty())
		{
			makeToast("Proszê wybraæ miejsce", fragment.getActivity());
			return false;
		} else
			return true;
	}

	public OnPickFromAutocompleteListener(SearchAutoCompleteTextView searchPosition,
			GoogleMap map, MapOutdoorFragment outdoorMap,
			SearchPlaceFragment fragment)
	{
		this.searchPosition = searchPosition;
		this.fragment = fragment;
		this.mapFragmentProvider = MapFragmentProvider.getInstance();
		this.dbHelper = new DatabaseHelper(fragment.getActivity(),
				DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		try
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
					String name = "";
					String details = "";
					if (object instanceof Building)
					{
						showPlaceOutdoor(object);
						name = ((Building)object).getName();
						details = ((Building)object).getAddress();
	
					} else if (object instanceof Room)
					{
						showPlaceIndoor(object);
						name = ((Room)object).getName();
						Building building = dbHelper.getBuildingDao().
									queryForId(((Room)object).getBuilding().getId());
						
						details = building.getName() ;
					} else if (object instanceof Unit)
					{
						Unit unit = (Unit) object;
						Room room = dbHelper.getRoomDao().queryForId(((Unit)object).getOffice().getId());
						
						name = ((Room)object).getName();
						Building building = dbHelper.getBuildingDao().
									queryForId(room.getBuilding().getId());
						details = building.getName() ;
						
						if (unit.getOffice() != null)
						{
							showPlaceIndoor(unit);
	
						} else
						{
							showPlaceOutdoor(unit);
						}
					}
					SearchDetailsFragment descriptionFragment = 
							fragment.getSearchDescriptionFragment();
					if(descriptionFragment != null)
					{
						descriptionFragment.setTextViews(name,details);
					}
				}
			}
		} catch (SQLException e)
		{
			Log.e("SearchButtonListener","Bledne wykonanie!");
		} 
	}

}
