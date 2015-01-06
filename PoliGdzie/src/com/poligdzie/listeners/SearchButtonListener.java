package com.poligdzie.listeners;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.example.poligdzie.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.j256.ormlite.dao.ForeignCollection;
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
	private GoogleMap					map;
	private MapOutdoorFragment			outdoorMap;
	private PoliGdzieBaseFragment		fragment;

	private void showPlaceOutdoor(Object object)
	{
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
						callback, map, m, this.outdoorMap, new DatabaseHelper(
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
			return room.getBuilding().getFloors();
		}

		if (object instanceof Unit)
		{
			Unit unit = (Unit) object;
			return unit.getBuilding().getFloors();
		}
		return null;
	}

	private String getTag(Object object)
	{
		if (object instanceof Room)
		{
			Room room = (Room) object;
			return room.getFloor().getTag();
		}

		if (object instanceof Unit)
		{
			Unit unit = (Unit) object;
			return unit.getOffice().getFloor().getTag();
		}
		return null;
	}

	private void showPlaceIndoor(Object object)
	{
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

	@Override
	public void onClick(View v)
	{
		if (map == null)
		{
			map = ((MapFragment) fragment.getActivity().getFragmentManager()
					.findFragmentById(R.id.map_outdoor_googleMap)).getMap();
		}
		if (outdoorMap == null)
		{
			outdoorMap = (MapOutdoorFragment) fragment.getFragmentManager()
					.findFragmentByTag(OUTDOOR_MAP_TAG);
		}
		MapFragmentProvider mapProvider = MapFragmentProvider.getInstance();
		mapProvider.clearFragments();

		searchPosition.clearFocus();
		InputMethodManager imm = (InputMethodManager) fragment.getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchPosition.getWindowToken(), 0);

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

	public SearchButtonListener(SearchAutoCompleteTextView searchPosition,
			GoogleMap map, MapOutdoorFragment outdoorMap,
			PoliGdzieBaseFragment fragment)
	{
		this.searchPosition = searchPosition;
		this.map = map;
		this.outdoorMap = outdoorMap;
		this.fragment = fragment;
	}

}
