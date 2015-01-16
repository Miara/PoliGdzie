package com.poligdzie.route;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.hardware.Camera.Size;
import android.util.Log;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationConnection;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.SpecialConnection;
import com.poligdzie.persistence.Unit;

public class IndoorRouteFinder implements Constants
{
	private DatabaseHelper dbHelper;
	
	private NavigationPoint startPoint;
	private NavigationPoint goalPoint;
	
	List<NavigationConnection> connections;
	List<NavigationPoint> points;
	List<NavigationPoint> route;
	
	private int[][] graph;
	private boolean[] checked;
	private int[] previous;
	private int[] best;
	
	private int graphSize;
		
	public IndoorRouteFinder(DatabaseHelper dbHelper)
	{
		this.dbHelper = dbHelper;
		connections = new ArrayList<NavigationConnection>();
		points = new ArrayList<NavigationPoint>();
		route = new ArrayList<NavigationPoint>();

		
	}

	
	public List<NavigationPoint> findRoute(Room startRoom, Room goalRoom) throws SQLException 
	{
		
		
		if( checkIfRoomsInOneIndoor(startRoom,goalRoom) )
		{
			
			this.startPoint = startRoom.getNavigationConnection().getFirstPoint();
			this.goalPoint = goalRoom.getNavigationConnection().getFirstPoint();
			
			Building startBulding = startRoom.getBuilding();
			Building goalBuilding = goalRoom.getBuilding();
			List<Floor> floors = new ArrayList<Floor>();
			List<SpecialConnection> specialConnections = new ArrayList<SpecialConnection>();
			
			floors = dbHelper.getFloorDao().queryBuilder().where().eq("building_id", startBulding).
					or().eq("building_id", goalBuilding).query();
			echo("building_id:"+startBulding.getId());
			echo("floors size :"+ floors.size());
			for(Floor f : floors)
			{
				echo("Floor:"+f.getId());
			}
			List<NavigationPoint> tempPoints = dbHelper.getNavigationPointDao().queryBuilder().where().in("floor_id", floors ).query();
			connections = dbHelper.getNavigationConnectionDao().queryBuilder().
					where().in("navigationPointFirst_id", tempPoints ).or().in("navigationPointLast_id", tempPoints).query();
			specialConnections = dbHelper.getSpecialConnectionDao().queryBuilder().
					where().in("specialPointLower_id", tempPoints ).or().in("specialPointUpper_id", tempPoints).query();
			
			addToConnections(specialConnections);
			
			
			graphSize = generatePointList();
			graph = new int[graphSize][graphSize];
			best = new int[graphSize];
			checked = new boolean[graphSize];
			previous = new int[graphSize];
			prepareGraph();
			fillGraphWithConnectionLength();
			findShortestPath(startPoint,goalPoint);
		}
		return route;
	}
	
	




	private boolean checkIfRoomsInOneIndoor(Room startRoom, Room goalRoom)
	{
		int startBuildingId =startRoom.getBuilding().getId(); 
		int goalBuildingId =goalRoom.getBuilding().getId(); 
		if(startBuildingId == goalBuildingId)
		{
			return true;
		}
		else
		{
			
			List<SpecialConnection> specialList = new ArrayList<SpecialConnection>();
			try
			{
				specialList = dbHelper.getSpecialConnectionDao().queryForAll();
				for(SpecialConnection conn : specialList)
				{
					int firstBuildingId = conn.getLowerFloor().getFloor().getBuilding().getId();
					int lastBuildingId = conn.getUpperFloor().getFloor().getBuilding().getId();
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


	private void findShortestPath(NavigationPoint p1, NavigationPoint p2)
	{
		int i=0,min;
		int start = getIndex(p1);
		int goal = getIndex(p2);
		boolean finished = false;
		
		long startTime = System.currentTimeMillis() ;
		long endTime;
		int actual = start;	
		int tmpActual = -1;
		int countActual=0;
		best[actual] = 0;
		echo("test");
		while(!finished)
		{
			for(i=0; i<graphSize ; i++)
			{
				if( (graph[actual][i] > 0) && (best[actual] + graph[actual][i] < best[i]) && (!checked[i]))
				{
					best[i] = best[actual] + graph[actual][i];
					previous[i]=actual;
				}
			}
			checked[actual] = true;
			min = Integer.MAX_VALUE;
			for(i=0; i<graphSize ; i++)
			{
				if( (best[i] < min) && !checked[i])
				{
					actual = i;
					min = best[i];
				}
			}
			
			if(tmpActual == actual)
			{
				countActual ++;
				if(countActual > 5) break;
			}
			else
			{
				countActual = 0;
			}
			tmpActual = actual;
			
			endTime = System.currentTimeMillis() - startTime;
			echo(""+endTime+":"+points.get(actual).getId()+":"+points.get(previous[actual]).getId());
			
			if ( actual == goal ) 
			{
				finished = true;	
				Log.i("poligdzie","znaleziono punkt goal");
			}
			else if( allNodesChecked()  || (  endTime > 5000))		
			{
				Log.i("poligdzie","nie znaleziono punktu goal");
				break;	
			}
			
				
		}
		
		while( (actual != start) && finished)
		{
			route.add(0,points.get(actual));
			if(previous[actual] != -1)
			{
				actual = previous[actual];
			}
			else
			{
				break;
			}
		}
		route.add(0,points.get(actual));
		
	}
	
	private void addToConnections( List<SpecialConnection> specialConnections)
	{
		for(SpecialConnection special : specialConnections)
		{
			echo("specialConnection:"+special.getId()+":"+special.getLowerFloor().getId()+
					":"+special.getUpperFloor().getId());
			NavigationConnection conn = new NavigationConnection(special.getLowerFloor(),
					special.getUpperFloor(),SPECIAL_CONNECTION_LENGTH);
			echo("Connection:"+conn.getFirstPoint().getId()+":::"+conn.getLastPoint().getId());
			connections.add(conn);
		};
	}
	
	private boolean allNodesChecked()
	{
		for(int i=0; i< graphSize ;i++)
		{
			if(!checked[i]) return false;
		}
		return true;
	}

	private void prepareGraph()
	{
		for(int i=0; i< graphSize ;i++)
		{
			best[i] = Integer.MAX_VALUE;
			previous[i] = -1;
			checked[i] = false;
			for(int j=0; j<graphSize ;j++)
			{
					graph[i][j] = 0;
			}
		}
	}
	
	private void fillGraphWithConnectionLength()
	{
		for(NavigationConnection con: connections)
		{
			int x = getIndex(con.getFirstPoint());
			int y = getIndex(con.getLastPoint());
			graph[x][y] = con.getLength();
			graph[y][x] = con.getLength();
		}
	}
	
	private int generatePointList()
	{

		for(NavigationConnection con : connections)
		{
			addPoint(con.getFirstPoint());
			addPoint(con.getLastPoint());
		}	
		return points.size();
	}
	
	private void addPoint(NavigationPoint point)
	{
		if(point != null)
		{
			for(NavigationPoint p : points)
			{
				if(p.getId() == point.getId() )
				{
					return;
				}
					
			}

			points.add(point);
		}
		else
		{
			Log.i("IndoorRouteFinder","nie dodano punktu");
		}
	}

	private int getIndex(NavigationPoint point)
	{
		for(NavigationPoint p :points)
		{
			if ( p.getId() == point.getId())
			{
				return points.indexOf(p);
			}
		}
		return -1;
	}
	
	private void echo(String s)
	{
		Log.i("Poligdzie",s);
	}
}
