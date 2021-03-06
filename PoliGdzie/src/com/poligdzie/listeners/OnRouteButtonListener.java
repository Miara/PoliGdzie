package com.poligdzie.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
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
import com.j256.ormlite.stmt.QueryBuilder;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.base.PoliGdzieBaseActivity;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.fragments.MapIndoorFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.fragments.RouteDetailsFragment;
import com.poligdzie.fragments.SearchDetailsFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.interfaces.WithCoordinates;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.BuildingEntry;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.GPSLocation;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.SpecialConnection;
import com.poligdzie.persistence.Unit;
import com.poligdzie.route.IndoorRouteFinder;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class OnRouteButtonListener extends PoliGdzieBaseClass implements
															OnClickListener
{

	private PoliGdzieBaseFragment		fragment;
	private SearchAutoCompleteTextView	startPosition;
	private SearchAutoCompleteTextView	goalPosition;
	private GoogleMap					map;

	private void showIndoorRoute(Object startObject, Object goalObject,
			int entryFloorNumber, int indoorMode) throws SQLException
	{
		// TODO: Obd�uga b��d�w : je�li sciezka jest pusta to ma pokazywac plan
		// pietra punktu startowego
		List<NavigationPoint> routePoints = new ArrayList<NavigationPoint>();
		IndoorRouteFinder finder = new IndoorRouteFinder(dbHelper);
		if (startObject instanceof Room && goalObject instanceof Room)
			routePoints = finder.findRoute((Room) startObject,
					(Room) goalObject);
		if (startObject instanceof Room
				&& goalObject instanceof NavigationPoint)
			routePoints = finder.findRoute((Room) startObject,
					(NavigationPoint) goalObject);
		if (startObject instanceof NavigationPoint
				&& goalObject instanceof Room)
			routePoints = finder.findRoute((NavigationPoint) startObject,
					(Room) goalObject);
		if (startObject instanceof NavigationPoint
				&& goalObject instanceof NavigationPoint)
			routePoints = finder.findRoute((NavigationPoint) startObject,
					(NavigationPoint) goalObject);

		List<NavigationPoint> points = new ArrayList<NavigationPoint>();

		List<Floor> floors = new ArrayList<Floor>();
		Floor previousFloor = new Floor();
		for (NavigationPoint routePoint : routePoints)
		{

			Floor currentFloor = dbHelper.getFloorDao().queryForId(
					getFloor(routePoint).getId());
			if (floors.isEmpty())
			{
				points.add(routePoint);
				previousFloor = dbHelper.getFloorDao().queryForId(
						getFloor(routePoint).getId());
				floors.add(previousFloor);
				continue;
			}

			if (previousFloor.getId() != currentFloor.getId()
					&& !previousFloor.getTag().equals(""))
			{
				MapIndoorFragment indoorMap = new MapIndoorFragment(
						previousFloor.getDrawableId(), previousFloor.getName(),
						previousFloor.getTag(), previousFloor.getId(), points);
				if (previousFloor.getId() == getFloor(startObject).getId()
						&& indoorMode == INDOOR_MODE_FIRST)
				{
					((PoliGdzieBaseActivity) fragment.getActivity())
							.switchFragment(R.id.map_container, indoorMap,
									indoorMap.getViewTag());
				}
				floors.add(currentFloor);
				previousFloor = currentFloor;
				points.clear();
			}
			points.add(routePoint);

		}
		if (!previousFloor.getTag().equals("")
				&& previousFloor.getTag() != null)
		{
			MapIndoorFragment indoorMap = new MapIndoorFragment(
					previousFloor.getDrawableId(), previousFloor.getName(),
					previousFloor.getTag(), previousFloor.getId(), points);
			if (previousFloor.getId() == getFloor(startObject).getId()
					&& indoorMode == INDOOR_MODE_FIRST)
			{
				((PoliGdzieBaseActivity) fragment.getActivity())
						.switchFragment(R.id.map_container, indoorMap,
								indoorMap.getViewTag());
			}
		}

		((OnClickListener) fragment.getActivity()).onClick(fragment
				.getActivity().findViewById(R.layout.map_activity));
	}

	private void showGeneralRoute(Object startObject, Object goalObject)
			throws SQLException
	{

		if (checkIfPointsInOneIndoor(startObject, goalObject))
		{
			showIndoorRoute(startObject, goalObject, getFloor(goalObject)
					.getNumber(), INDOOR_MODE_FIRST);
		} else
		{
			int mainFloorNumber = 0;
			MapFragmentProvider mapProvider = MapFragmentProvider.getInstance();
			List<BuildingEntry> outdoorPoints = new ArrayList<BuildingEntry>();
			outdoorPoints = getEntrancesBetweenBuildings(startObject,goalObject);
			
			MapDrawingProvider drawingProvider = MapDrawingProvider.getInstance();
			map = ((MapFragment) fragment.getActivity() 
					.getFragmentManager().findFragmentById(R.id.map_outdoor_googleMap)).getMap();
			drawingProvider.setStart(outdoorPoints.get(0));
			drawingProvider.setGoal(outdoorPoints.get(1));
			drawingProvider.setDrawRoute(true);
			drawingProvider.drawRoute(map);
			
			if(startObject instanceof GPSLocation) startObject = new Building();
			if(goalObject instanceof GPSLocation) goalObject = new Building();
			
			if (startObject instanceof Building
					&& goalObject instanceof Building)
			{
				mapProvider.addGoogleMapFragment();
				((PoliGdzieBaseActivity) fragment.getActivity())
						.switchFragment(R.id.map_container, mapProvider
								.getGoogleMapFragment(), mapProvider
								.getGoogleMapFragment().getViewTag());
	

			} else if (startObject instanceof Building)
			{

				mapProvider.addGoogleMapFragment();
				((PoliGdzieBaseActivity) fragment.getActivity())
						.switchFragment(R.id.map_container, mapProvider
								.getGoogleMapFragment(), mapProvider
								.getGoogleMapFragment().getViewTag());
				
				
				NavigationPoint startIndoorPoint = dbHelper.getNavigationPointDao().
						queryForId(outdoorPoints.get(1).getNavigationPoint().getId());
				if (goalObject instanceof Room)
				{
					showIndoorRoute(startIndoorPoint, goalObject,
							getFloor(startIndoorPoint).getNumber(),
							INDOOR_MODE_LAST);
				}
			} else if (goalObject instanceof Building)
			{
				NavigationPoint goalIndoorPoint = dbHelper.getNavigationPointDao().
						queryForId(outdoorPoints.get(0).getNavigationPoint().getId()); // 2nd building entry
				if (startObject instanceof Room)
				{
					showIndoorRoute(startObject, goalIndoorPoint,
							getFloor(startObject).getNumber(),
							INDOOR_MODE_FIRST);
				}

				mapProvider.addGoogleMapFragment();
			} else
			{
				if (startObject instanceof Room)
				{
					NavigationPoint exit = outdoorPoints.get(0)
							.getNavigationPoint();
					mainFloorNumber = getFloor(exit).getNumber();
					
					showIndoorRoute(startObject, exit, mainFloorNumber,
							INDOOR_MODE_FIRST);
					mapProvider.addGoogleMapFragment();
				}

				if (goalObject instanceof Room)
				{
					NavigationPoint entry = outdoorPoints.get(1)
							.getNavigationPoint();
					mainFloorNumber = getFloor(entry).getNumber();
					showIndoorRoute(entry, goalObject, mainFloorNumber,
							INDOOR_MODE_LAST);
				}

			}
		}
		
		((MapActivity)fragment.getActivity()).setNavigationArrowsVisibility();
	}

	private List<BuildingEntry> getEntrancesBetweenBuildings(Object startObject,Object goalObject) throws SQLException
	{
		List<BuildingEntry> resultEntrances = new ArrayList<BuildingEntry>();
		double a, b, tmpLength;
		double length = Integer.MAX_VALUE;
		if(!(startObject instanceof GPSLocation) && !(goalObject instanceof GPSLocation) )
		{
			List<BuildingEntry> startEntrances = dbHelper.getBuildingEntryDao()
					.queryBuilder().where().eq("building_id", getBuilding(startObject).getId())
					.query();
			List<BuildingEntry> goalEntrances = dbHelper.getBuildingEntryDao()
					.queryBuilder().where().eq("building_id", getBuilding(goalObject).getId())
					.query();
			
			for (BuildingEntry en1 : startEntrances)
			{
				for (BuildingEntry en2 : goalEntrances)
				{
					a = en2.getCoordX() - en1.getCoordX();
					b = en2.getCoordY() - en1.getCoordY();
					tmpLength = Math.sqrt(a * a + b * b);
					if (tmpLength < length)
					{
						resultEntrances.clear();
						resultEntrances.add(en1);
						resultEntrances.add(en2);
					}
				}
			}
			
		}
		else if(startObject instanceof GPSLocation)
		{
			List<BuildingEntry> goalEntrances = dbHelper.getBuildingEntryDao()
					.queryBuilder().where().eq("building_id", getBuilding(goalObject).getId())
					.query();
			GPSLocation gps = (GPSLocation)startObject;
			BuildingEntry gpsEntry = new BuildingEntry(gps.getCoordX(),gps.getCoordY(),null,null);
			for (BuildingEntry entry : goalEntrances)
			{
				a = entry.getCoordX() - gpsEntry.getCoordX();
				b = entry.getCoordY() - gpsEntry.getCoordY();
				tmpLength = Math.sqrt(a * a + b * b);
				if (tmpLength < length)
				{
					resultEntrances.clear();
					resultEntrances.add(gpsEntry);
					resultEntrances.add(entry);
				}
			}
			
		}
		else if(goalObject instanceof GPSLocation)
		{
			List<BuildingEntry> startEntrances = dbHelper.getBuildingEntryDao()
					.queryBuilder().where().eq("building_id", getBuilding(startObject).getId())
					.query();
			GPSLocation gps = (GPSLocation)goalObject;
			BuildingEntry gpsEntry = new BuildingEntry(gps.getCoordX(),gps.getCoordY(),null,null);
			for (BuildingEntry entry : startEntrances)
			{
				a = entry.getCoordX() - gpsEntry.getCoordX();
				b = entry.getCoordY() - gpsEntry.getCoordY();
				tmpLength = Math.sqrt(a * a + b * b);
				if (tmpLength < length)
				{
					resultEntrances.clear();
					resultEntrances.add(entry);
					resultEntrances.add(gpsEntry);
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

			InputMethodManager imm = (InputMethodManager) fragment
					.getActivity().getSystemService(
							Context.INPUT_METHOD_SERVICE);

			clearFocusAndHidePromptWindow(startPosition, imm);
			clearFocusAndHidePromptWindow(goalPosition, imm);

			if (validateAdapters(startPosition, goalPosition))
			{
				Object startObject = ifUnitChangeToRoom(startPosition
						.getAdapter().getItem(0));
				Object goalObject = ifUnitChangeToRoom(goalPosition
						.getAdapter().getItem(0));
				if (validateRouteObjects(startObject, goalObject))
				{
					showGeneralRoute(startObject, goalObject);
					SearchDetailsFragment searchDetailsFrag = (SearchDetailsFragment) fragment
							.getActivity().getFragmentManager()
							.findFragmentById(R.id.search_description_frag);
					RouteDetailsFragment routeDetailsFrag = (RouteDetailsFragment) fragment
							.getActivity().getFragmentManager()
							.findFragmentById(R.id.route_details_frag);
					searchDetailsFrag.getView().setVisibility(View.GONE);
					
					String startObjectName = new String();
					startObjectName = ((Nameable) startObject).getName();
					
					String goalObjectName = new String();
					goalObjectName = ((Nameable) goalObject).getName();
					
					
					try
					{
						if(startObject instanceof Room) 
						{
						Building building = dbHelper.getBuildingDao().
								queryForId(((Room)startObject).getBuilding().getId());
						startObjectName += " (" + building.getName() + ")";
						}
						
						if(goalObject instanceof Room) 
						{
						Building building = dbHelper.getBuildingDao().
								queryForId(((Room)goalObject).getBuilding().getId());
						goalObjectName += " (" + building.getName() + ")";
						}
					} catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					routeDetailsFrag.setTextViews(startObjectName, goalObjectName);

				}
			}

		} catch (SQLException e)
		{
			Log.e("poligdzie", "ERROR RouteButtonlistener");
		}

	}

	private Object ifUnitChangeToRoom(Object item)
	{
		if (item instanceof Unit)
		{
			return ((Unit) item).getOffice();
		} else
		{
			return item;
		}

	}

	private boolean validateAdapters(SearchAutoCompleteTextView start,
			SearchAutoCompleteTextView goal)
	{
		if(start.getAdapter() == null || goal.getAdapter() == null)
		{
			makeToast("Proszę uzupełnić pola wyszukiwania",
					fragment.getActivity());
			return false;
		}
		if (start.getAdapter().isEmpty() && goal.getAdapter().isEmpty())
		{
			makeToast("Proszę uzupełnić pola wyszukiwania",
					fragment.getActivity());
			return false;
		} else if (start.getAdapter().isEmpty())
		{
			makeToast("Proszę wybrać punkt startowy", fragment.getActivity());
			return false;
		} else if (goal.getAdapter().isEmpty())
		{
			makeToast("Proszę wybrać punkt docelowy", fragment.getActivity());
			return false;
		} else
		{
			return true;
		}
	}

	private boolean validateRouteObjects(Object startObject, Object goalObject)
	{
		if ((startObject == null || goalObject == null)
				|| (startObject == goalObject))
		{
			makeToast("Pola nie mogą być takie same", fragment.getActivity());
			return false;
		}
		return true;
	}

	private boolean checkIfPointsInOneIndoor(Object startObject,
			Object goalObject)
	{
		if(startObject instanceof GPSLocation || goalObject instanceof GPSLocation) 
		{
			return false;
		}
		Building building1 = null;
		Building building2 = null;
		try
		{
			building1 = getBuilding(startObject);
			building2 = getBuilding(goalObject);
		} catch (SQLException e1)
		{
			e1.printStackTrace();
		}

		int startBuildingId = building1.getId();
		int goalBuildingId = building2.getId();

		if (startBuildingId == goalBuildingId)
		{
			if (startObject instanceof Building
					|| goalObject instanceof Building)
			{
				return false;
			} else
			{
				return true;
			}
		} else
		{

			List<SpecialConnection> specialList = new ArrayList<SpecialConnection>();
			try
			{

				QueryBuilder<NavigationPoint, Integer> lowerPoint = dbHelper
						.getNavigationPointDao().queryBuilder();
				QueryBuilder<Floor, Integer> floor = dbHelper.getFloorDao()
						.queryBuilder();
				QueryBuilder<SpecialConnection, Integer> specConn = dbHelper
						.getSpecialConnectionDao().queryBuilder();
				QueryBuilder<Building, Integer> building = dbHelper
						.getBuildingDao().queryBuilder();

				building.where().idEq(startBuildingId).or()
						.idEq(goalBuildingId);
				floor.join(building);
				lowerPoint.join(floor);
				specConn.join(lowerPoint);

				specialList = specConn.query();

				Log.d("POLIGDZIE", specialList.toString());
				for (SpecialConnection conn : specialList)
				{
					lowerPoint.reset();
					lowerPoint.where().idEq(conn.getLowerPoint().getId());
					NavigationPoint lower = lowerPoint.queryForFirst();

					floor.reset();
					Floor fl = floor.where().idEq(lower.getFloor().getId())
							.queryForFirst();
					int firstBuildingId = fl.getBuilding().getId();

					lowerPoint.reset();
					lowerPoint.where().idEq(conn.getUpperPoint().getId());
					NavigationPoint upper = lowerPoint.queryForFirst();

					floor.reset();
					fl = floor.where().idEq(upper.getFloor().getId())
							.queryForFirst();
					int lastBuildingId = fl.getBuilding().getId();
					if (((firstBuildingId == startBuildingId) && (lastBuildingId == goalBuildingId))
							|| ((firstBuildingId == goalBuildingId) && (lastBuildingId == startBuildingId)))
					{
						if (!(startObject instanceof Building)
								&& !(goalObject instanceof Building))
						{
							return true;
						}
					}
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			return false;
		}
	}

	private Building getBuilding(Object object) throws SQLException
	{

		Building building = new Building();
		if (object instanceof Room)
		{
			Room room = (Room) object;
			building = dbHelper.getBuildingDao().queryForId(
					room.getBuilding().getId());
			return building;
		} else if (object instanceof Unit)
		{
			Unit unit = (Unit) object;
			building = dbHelper.getBuildingDao().queryForId(
					unit.getBuilding().getId());
			return building;
		} else if (object instanceof Building)
		{
			return ((Building) object);
		} else
		{
			return null;
		}
	}

	
	
	
	private Floor getFloor(Object object) throws SQLException
	{

		if (object instanceof Room)
		{
			Room room = (Room) object;
			Floor floor = dbHelper.getFloorDao().queryForId(
					room.getFloor().getId());
			return floor;
		} else if (object instanceof Unit)
		{
			Unit unit = (Unit) object;
			Room room = dbHelper.getRoomDao().queryForId(
					unit.getOffice().getId());
			Floor floor = dbHelper.getFloorDao().queryForId(
					room.getFloor().getId());
			return floor;
		} else if (object instanceof NavigationPoint)
		{
			NavigationPoint point = (NavigationPoint) object;
			if (point.getFloor() == null)
			{
				point = dbHelper.getNavigationPointDao().queryForId(
						point.getId());
			}
			Floor floor = dbHelper.getFloorDao().queryForId(
					point.getFloor().getId());
			return floor;
		}
		return null;
	}

	private void clearFocusAndHidePromptWindow(
			SearchAutoCompleteTextView position, InputMethodManager imm)
	{
		if (position.hasFocus())
		{
			position.clearFocus();
			imm.hideSoftInputFromWindow(position.getWindowToken(), 0);
		}

	}

	public OnRouteButtonListener(SearchAutoCompleteTextView startPosition,
			SearchAutoCompleteTextView goalPosition, GoogleMap map,
			MapOutdoorFragment outdoorMap, PoliGdzieBaseFragment fragment)
	{
		this.startPosition = startPosition;
		this.goalPosition = goalPosition;
		this.fragment = fragment;
		this.map = map;
		this.dbHelper = new DatabaseHelper(fragment.getActivity(),
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
	}

}
