package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
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
import com.poligdzie.persistence.BuildingEntry;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.SpecialConnection;
import com.poligdzie.persistence.Unit;
import com.poligdzie.route.IndoorRouteFinder;
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
	



	private ForeignCollection<Floor> getFloors(Object object) throws SQLException
	{
		if (object instanceof Room)
		{
			Room room = (Room) object;
			return room.getBuilding().getFloors();
		}
		
		if (object instanceof NavigationPoint)
		{
			NavigationPoint point = (NavigationPoint) object;
			int buildingId = dbHelper.getFloorDao().queryForId(point.getFloor().getId()).getBuilding().getId();
			Building building = dbHelper.getBuildingDao().queryForId(buildingId);
			return building.getFloors();
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

	private void showIndoorRoute(Object startObject, Object goalObject,int entryFloorNumber, int indoorMode) throws SQLException
	{
		Floor lastFloorInIndoor = null;
		String roomFloorTag = "";
		List<NavigationPoint> routePoints = new ArrayList<NavigationPoint>();
		IndoorRouteFinder finder = new IndoorRouteFinder(dbHelper);
		if(startObject instanceof Room  && goalObject instanceof Room)
			routePoints = finder.findRoute((Room)startObject, (Room)goalObject);
		if(startObject instanceof Room  && goalObject instanceof NavigationPoint)
			routePoints = finder.findRoute((Room)startObject, (NavigationPoint)goalObject);
		if(startObject instanceof NavigationPoint  && goalObject instanceof Room)
			routePoints = finder.findRoute((NavigationPoint)startObject, (Room)goalObject);
		if(startObject instanceof NavigationPoint  && goalObject instanceof NavigationPoint)
			routePoints = finder.findRoute((NavigationPoint)startObject, (NavigationPoint)goalObject);
		
		String s = ":";
		for(NavigationPoint r : routePoints)
			s= s+":"+r.getId();
		Log.i("poligdzie","trasa:"+s);
		
		List<NavigationPoint> points = new ArrayList<NavigationPoint>();
		
		List<Floor> floors = new ArrayList<Floor>();
		Floor previousFloor = new Floor();
		for(NavigationPoint routePoint : routePoints)
		{
			
			
			Floor currentFloor = dbHelper.getFloorDao().queryForId(getFloor(routePoint).getId());
			
			points.add(routePoint);
			if(floors.isEmpty())
			{
				previousFloor = dbHelper.getFloorDao().queryForId(getFloor(routePoint).getId());;
				floors.add(previousFloor);
				
				continue;
			}

			if(previousFloor.getId() != currentFloor.getId() &&  !previousFloor.getTag().equals(""))
			{
				Log.i("Poligdzie","drawable:"+previousFloor.getDrawableId());
				Log.i("Poligdzie","Tag:"+previousFloor.getTag());
				Log.i("Poligdzie","name:"+previousFloor.getName());
				Log.i("Poligdzie","id:"+previousFloor.getId());
				MapIndoorFragment indoorMap = new MapIndoorFragment(previousFloor.getDrawableId(), 
						previousFloor.getName(), previousFloor.getTag(), previousFloor.getId(),points);
				if(previousFloor.getId() == getFloor(startObject).getId())
				{
					((PoliGdzieBaseActivity) fragment.getActivity()).switchFragment(R.id.map_container,
							indoorMap, indoorMap.getViewTag());
				}
				floors.add(currentFloor);
				previousFloor = currentFloor;
				points.clear();
			}

		}
		if(!previousFloor.getTag().equals("") && previousFloor.getTag() != null)
		{
			Log.i("Poligdzie","drawable:"+previousFloor.getDrawableId());
			Log.i("Poligdzie","Tag:"+previousFloor.getTag());
			Log.i("Poligdzie","name:"+previousFloor.getName());
			Log.i("Poligdzie","id:"+previousFloor.getId());
			MapIndoorFragment indoorMap = new MapIndoorFragment(previousFloor.getDrawableId(), 
					previousFloor.getName(), previousFloor.getTag(), previousFloor.getId(),points);
			if(previousFloor.getId() == getFloor(startObject).getId())
			{
				((PoliGdzieBaseActivity) fragment.getActivity()).switchFragment(R.id.map_container,
						indoorMap, indoorMap.getViewTag());
			}
		}
		/*for (Floor f : floors)
		{
			
			
			if ((f.getTag().equals(getTag(startObject)) && indoorMode == INDOOR_MODE_FIRST) ||
					(f.getNumber() == entryFloorNumber && indoorMode == INDOOR_MODE_LAST))
			{
					points = getPointsAtFloor(routePoints, f);
					addIndoorFragment(points,f,indoorMode);
					roomFloorTag = f.getTag();
					
			}
			else if ((f.getTag().equals(getTag(startObject)) && indoorMode == INDOOR_MODE_LAST ) ||
					(f.getNumber() == entryFloorNumber && indoorMode == INDOOR_MODE_FIRST))
			{
					lastFloorInIndoor = f;
			}
			
		}
		
		if(lastFloorInIndoor != null )
		{
			if( (lastFloorInIndoor.getTag() != roomFloorTag))
			{
				Floor f = lastFloorInIndoor;
				points = getPointsAtFloor(routePoints, f);
				new MapIndoorFragment(f.getDrawableId(), f.getName(), f.getTag(), f.getId(),points);
			}
		}*/
		

		((OnClickListener) fragment.getActivity()).onClick(fragment
				.getActivity().findViewById(R.layout.map_activity));
	}
	
	private void addIndoorFragment(List<NavigationPoint> points,Floor floor, int indoorMode)
	{
		MapIndoorFragment indoorMap = new MapIndoorFragment(
				floor.getDrawableId(), floor.getName(), floor.getTag(), floor.getId(),points);
		if(indoorMode == INDOOR_MODE_FIRST)
		{
			((PoliGdzieBaseActivity) fragment.getActivity())
			.switchFragment(R.id.map_container, indoorMap,
					indoorMap.getViewTag());
		}	
	}
	
	private List<NavigationPoint> getPointsAtFloor(List<NavigationPoint> points,Floor fl)
	{
		List<NavigationPoint> list = new ArrayList<NavigationPoint>();
		for(NavigationPoint p: points)
		{
			if( p.getFloor().getId() == fl.getId() )
			{
				list.add(p);
			}
			
		}
		return list;	
	}

	private void showGeneralRoute(Object startObject, Object goalObject) throws SQLException
	{
		
		if ( checkIfPointsInOneIndoor(startObject,goalObject ))
		{
			showIndoorRoute(startObject,goalObject,getFloor(goalObject).getNumber(),INDOOR_MODE_FIRST); 
		}
		else 
		{
			int mainFloorNumber = 0;
			MapFragmentProvider mapProvider = MapFragmentProvider.getInstance();
			List<BuildingEntry> entrances = new ArrayList<BuildingEntry>();
			entrances = getEntrancesBetweenBuildings( getBuilding(startObject).getId(),
					getBuilding(goalObject).getId() );
			if(startObject instanceof Building && goalObject instanceof Building)
			{
				mapProvider.addGoogleMapFragment();
				//TODO: Draw route
				
			}
			else if( startObject instanceof Building)
			{
				
				mapProvider.addGoogleMapFragment();
				//TODO: Draw route
				
				NavigationPoint startIndoorPoint = entrances.get(1).getNavigationPoint(); // 2nd building entry
				if(goalObject instanceof NavigationPoint)
				{
					// TODO: navigation point route (clicked points on bitmap )
				}
				else if(goalObject instanceof Room)
				{
					showIndoorRoute(startIndoorPoint, goalObject, 
							getFloor(startIndoorPoint).getNumber(), INDOOR_MODE_LAST);
				}
			}
			else if( goalObject instanceof Building)
			{
				NavigationPoint goalIndoorPoint = entrances.get(0).getNavigationPoint(); // 2nd building entry
				if(startObject instanceof NavigationPoint)
				{
					// TODO: navigation point route (clicked points on bitmap )
				}
				else if(startObject instanceof Room)
				{
					showIndoorRoute(startObject, goalIndoorPoint, 
							getFloor(startObject).getNumber(), INDOOR_MODE_FIRST);
				}
				
				mapProvider.addGoogleMapFragment();
				//TODO: Draw route
			}
			else
			{
				if (startObject instanceof Room)
				{
					NavigationPoint exit = entrances.get(0).getNavigationPoint(); 
					mainFloorNumber = getFloor(exit).getNumber();
					// TODO: showGeneralRoute
					showIndoorRoute(startObject, exit ,mainFloorNumber,  INDOOR_MODE_FIRST);
					mapProvider.addGoogleMapFragment();
				}
				
				if (goalObject instanceof Room)
				{
					NavigationPoint entry = entrances.get(1).getNavigationPoint(); 
					mainFloorNumber = getFloor(entry).getNumber();
					showIndoorRoute(entry, goalObject, mainFloorNumber, INDOOR_MODE_LAST);
				}
				
				//TODO : cases when start and goal are NavigationPoints
			}
		}		
	}

	private List<BuildingEntry> getEntrancesBetweenBuildings(int startBuildingId, int goalBuildingId) throws SQLException
	{
		List<BuildingEntry> resultEntrances = new ArrayList<BuildingEntry>();
		List<BuildingEntry> startEntrances = dbHelper.getBuildingEntryDao().
				queryBuilder().where().eq("building_id", startBuildingId).query();
		List<BuildingEntry> goalEntrances = dbHelper.getBuildingEntryDao().
				queryBuilder().where().eq("building_id", goalBuildingId).query();
		double a,b,tmpLength;
		double length = Integer.MAX_VALUE;
		
		for(BuildingEntry en1 : startEntrances)
		{
			for(BuildingEntry en2 : goalEntrances)
			{
				a = en2.getCoordX() - en1.getCoordX();
				b = en2.getCoordY() - en1.getCoordY();
				tmpLength = Math.sqrt(a*a + b*b);
				if(tmpLength < length)
				{
					resultEntrances.clear();
					resultEntrances.add(en1);
					resultEntrances.add(en2);
				}
			}
		}
		return resultEntrances;
	}
	

	@Override
	public void onClick(View v)
	{
		try
		{
			MapFragmentProvider mapProvider = MapFragmentProvider.getInstance();
			mapProvider.clearFragments();
	
			InputMethodManager imm = (InputMethodManager) fragment.getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			
			clearFocusAndHidePromptWindow(startPosition, imm);
			clearFocusAndHidePromptWindow(goalPosition, imm);
	
			if (validateAdapters(startPosition,goalPosition))
			{
				Object startObject = ifUnitChangeToRoom(startPosition.getAdapter().getItem(0));
				Object goalObject = ifUnitChangeToRoom(goalPosition.getAdapter().getItem(0));
				if(validateRouteObjects(startObject,goalObject)) 
				{
					showGeneralRoute(startObject,goalObject);
				}
			}
			
		} 
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			Log.e("poligdzie","ERROR RouteButtonlistener");
		}	 
	}

	private Object ifUnitChangeToRoom(Object item)
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
			makeToast("Proszê uzupe³niæ pola wyszukiwania",fragment.getActivity());
			return false;
		}
		else if(start.getAdapter().isEmpty())
		{
			makeToast("Proszê wybraæ punkt startowy",fragment.getActivity());
			return false;
		}
		else if(goal.getAdapter().isEmpty())
		{
			makeToast("Proszê wybraæ punkt docelowy",fragment.getActivity());
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
			makeToast("Pola nie mog¹ byæ te same",fragment.getActivity());
			return false;
		}
		return true;
	}

	private boolean checkIfPointsInOneIndoor(Object startObject, Object goalObject)
	{
		Building building1 = getBuilding(startObject);
		Building building2 = getBuilding(goalObject);
				
		int startBuildingId = building1.getId();
		int goalBuildingId = building2.getId();
		
		if(startBuildingId == goalBuildingId)
		{
			if(startObject instanceof Building || goalObject instanceof Building )
			{
				return false;
			}
			else
			{
				return true;
			}	
		}
		else
		{
			List<SpecialConnection> specialList = new ArrayList<SpecialConnection>();
			try
			{
				specialList = dbHelper.getSpecialConnectionDao().queryForAll();
				for(SpecialConnection conn : specialList)
				{
					int firstBuildingId = conn.getLowerPoint().getFloor().getBuilding().getId();
					int lastBuildingId = conn.getUpperPoint().getFloor().getBuilding().getId();
					if(  ( ( firstBuildingId == startBuildingId) && (lastBuildingId == goalBuildingId) ) ||
							( ( firstBuildingId == goalBuildingId) && (lastBuildingId == startBuildingId) ) )
					{
						return true;
					}
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			return false;
		}
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
	
	private Floor getFloor(Object object) throws SQLException
	{
		
		if( object instanceof Room) 
		{
			return dbHelper.getFloorDao().queryForId(((Room) object).getFloor().getId());
		} 
		else if( object instanceof Unit) 
		{
			Room room = dbHelper.getRoomDao().queryForId(((Unit) object).getOffice().getId());
			return dbHelper.getFloorDao().queryForId(room.getFloor().getId());
		} 
		else if( object instanceof NavigationPoint) 
		{
			return dbHelper.getFloorDao().queryForId(((NavigationPoint) object).getFloor().getId()); 
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
		this.dbHelper = new DatabaseHelper(fragment.getActivity(), DATABASE_NAME, null, DATABASE_VERSION);
	}

}
