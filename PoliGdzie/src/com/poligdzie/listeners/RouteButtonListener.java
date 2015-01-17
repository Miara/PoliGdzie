package com.poligdzie.listeners;

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
import com.poligdzie.base.PoliGdzieBaseActivity;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.callbacks.MarkerAnimationFinishCallback;
import com.poligdzie.fragments.MapIndoorFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.tasks.AnimationClosureChecker;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class RouteButtonListener extends PoliGdzieBaseClass implements
OnClickListener
{

	private PoliGdzieBaseFragment		fragment;
	private SearchAutoCompleteTextView	startPosition;
	private SearchAutoCompleteTextView	goalPosition;
	
	private boolean error = false;



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

	private void showIndoorRoute(Object room, int indoorMode, int switchingMode)
	{
		Floor lastFloorInIndoor = null;
		String roomFloorTag = "";
		for (Floor f : getFloors(room))
		{
			
			
			if ((f.getTag().equals(getTag(room)) && indoorMode == INDOOR_MODE_FIRST) ||
					(f.getNumber() == 0 && indoorMode == INDOOR_MODE_LAST))
			{
					addIndoorFragment(f,switchingMode);
					roomFloorTag = f.getTag();
					
			}
			else if ((f.getTag().equals(getTag(room)) && indoorMode == INDOOR_MODE_LAST ) ||
					(f.getNumber() == 0 && indoorMode == INDOOR_MODE_FIRST))
			{
					lastFloorInIndoor = f;
			}
			
		}
		
		if(lastFloorInIndoor != null )
		{
			if( (lastFloorInIndoor.getTag() != roomFloorTag))
			{
				Floor f = lastFloorInIndoor;
				new MapIndoorFragment(f.getDrawableId(), f.getName(), f.getTag(), f.getNumber());
			}
		}
		

		((OnClickListener) fragment.getActivity()).onClick(fragment
				.getActivity().findViewById(R.layout.map_activity));
	}

	
	private void addIndoorFragment(Floor floor, int switchingMode)
	{
		MapIndoorFragment indoorMap = new MapIndoorFragment(
				floor.getDrawableId(), floor.getName(), floor.getTag(), floor.getNumber());
		if(switchingMode == ENABLE_SWITCHING_FRAGMENT)
		{
			((PoliGdzieBaseActivity) fragment.getActivity())
			.switchFragment(R.id.map_container, indoorMap,
					indoorMap.getViewTag());
		}	
	}

	private void showGeneralRoute(Object startObject, Object goalObject)
	{
		MapFragmentProvider mapProvider = MapFragmentProvider.getInstance();
		if(startObject instanceof Building)
		{
			mapProvider.addGoogleMapFragment();
		}
		else if (startObject instanceof Room)
		{
			showIndoorRoute(startObject, INDOOR_MODE_FIRST, ENABLE_SWITCHING_FRAGMENT);
			mapProvider.addGoogleMapFragment();
		}
		
		if (goalObject instanceof Room)
		{
			showIndoorRoute(goalObject, INDOOR_MODE_LAST, DISABLE_SWITCHING_FRAGMENT);
		}
		
	}

	@Override
	public void onClick(View v)
	{

		MapFragmentProvider mapProvider = MapFragmentProvider.getInstance();
		mapProvider.clearFragments();

		InputMethodManager imm = (InputMethodManager) fragment.getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		clearFocusAndHidePromptWindow(startPosition, imm);
		clearFocusAndHidePromptWindow(goalPosition, imm);

		if (validateAdapters(startPosition,goalPosition))
		{
			Object startObject = getRoomOrBuilding(startPosition.getAdapter().getItem(0));
			Object goalObject = getRoomOrBuilding(goalPosition.getAdapter().getItem(0));
			if(!validateRouteObjects(startObject,goalObject)) return;
			
			if ( objectsInTheSameBuilding(startObject,goalObject ))
			{
				showIndoorRoute(goalObject,INDOOR_MODE_LAST,ENABLE_SWITCHING_FRAGMENT); 
				makeToast("Ten sam budynek",fragment.getActivity());
			}
			else if(!error)
			{
				showGeneralRoute(startObject,goalObject);
				makeToast("obiekty w innych budynkach",fragment.getActivity());	
			}
			
		} 
	}

	

	

	private Object getRoomOrBuilding(Object item)
	{
		if(item instanceof Unit) 
		{
			return ((Unit)item).getOffice();
		}
		else
		{
			return item;
		}
		

	}

	private boolean validateAdapters(SearchAutoCompleteTextView start, SearchAutoCompleteTextView goal)
	{
		
		if(start.getAdapter().isEmpty()  && goal.getAdapter().isEmpty() )
		{
			makeToast("Prosz� uzupe�ni� pola wyszukiwania",fragment.getActivity());
			return false;
		}
		else if(start.getAdapter().isEmpty())
		{
			makeToast("Prosz� wybra� punkt startowy",fragment.getActivity());
			return false;
		}
		else if(goal.getAdapter().isEmpty())
		{
			makeToast("Prosz� wybra� punkt docelowy",fragment.getActivity());
			return false;
		}
		else
		{
			return true;
		}
	}

	private boolean validateRouteObjects(Object startObject, Object goalObject)
	{
		if((startObject == null || goalObject == null) || (startObject == goalObject)	) 
		{
			makeToast("Pola nie mog� by� te same",fragment.getActivity());
			return false;
		}
		return true;
	}

	private boolean objectsInTheSameBuilding(Object startObject, Object goalObject)
	{
		Building building1 = getBuilding(startObject);
		Building building2 = getBuilding(goalObject);
		
		if(building1 == null || building2 == null)
		{
			makeToast("Nie mozna zidentyfikowa� budynk�w ",fragment.getActivity());
			error = true;
			return false;
		}
		
		if(building1.getId() == building2.getId() )
		{
			if(startObject instanceof Building || goalObject instanceof Building )
			{
				// TODO: route listener
				makeToast("Do poprawy..",fragment.getActivity());
				error = true;
				return false;
			}
			return true;
		}
		return false;
	}
	
	private Building getBuilding(Object object)
	{
		
		if( object instanceof Room) 
		{
			return ((Room) object).getBuilding();
		} 
		else if( object instanceof Unit) 
		{
			return ((Unit) object).getBuilding();
		} 
		else if( object instanceof Building) 
		{
			return ((Building) object);
		}
		else
		{
			return null;
		}
	}
	
	private void clearFocusAndHidePromptWindow(
			SearchAutoCompleteTextView position, InputMethodManager imm)
	{
		if(position.hasFocus())
		{
			position.clearFocus();
			imm.hideSoftInputFromWindow(position.getWindowToken(), 0);
		}
		
	}

	public RouteButtonListener(SearchAutoCompleteTextView startPosition,
			SearchAutoCompleteTextView goalPosition,
			GoogleMap map, MapOutdoorFragment outdoorMap,
			PoliGdzieBaseFragment fragment)
	{
		this.startPosition = startPosition;
		this.goalPosition = goalPosition;
		this.fragment = fragment;
	}

}