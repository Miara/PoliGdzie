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
		
		
		if( startRoom.getBuilding().getId() == goalRoom.getBuilding().getId() )
		{
			
			this.startPoint = startRoom.getNavigationConnection().getFirstPoint();
			this.goalPoint = goalRoom.getNavigationConnection().getLastPoint();
			
			Building building = startRoom.getBuilding();
			List<Floor> floors = new ArrayList<Floor>();
			List<SpecialConnection> specialConnections = new ArrayList<SpecialConnection>();
			
			floors = dbHelper.getFloorDao().queryBuilder().where().eq("building_id", building).query();
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
			echo("test1");
			prepareGraph();
			echo("test2");
			fillGraphWithConnectionLength();
			echo("test3");
			findShortestPath(startPoint,goalPoint);
			echo("test4");
		}
		return route;
	}
	
	

	private void addToConnections( List<SpecialConnection> specialConnections)
	{
		for(SpecialConnection special : specialConnections)
		{
			NavigationConnection conn = new NavigationConnection(special.getLowerFloor(),special.getUpperFloor(),0);
			connections.add(conn);
		};
	}


	private void findShortestPath(NavigationPoint p1, NavigationPoint p2)
	{
		int i=0,min;
		int start = getIndex(p1);
		int goal = getIndex(p2);
		boolean finished = false;
		
		int actual = start;	
		int tmpActual = 0;	
		int actualCount =0 ;
		best[actual] = 0;
		echo("test5");
		while(!finished)
		{
			for(i=0; i<graphSize ; i++)
			{
				if( (graph[actual][i] > 0) && (best[actual] + graph[actual][i] < best[i]) && (!checked[i]))
				{
					best[i] = best[actual] + graph[actual][i];
					previous[i]=actual;
					//Log.i("poligdzie","graphLen:"+graph[actual][i]);
					//Log.i("poligdzie",actual+"->"+i+"="+best[i]);
				}
			}
			checked[actual] = true;
			min = Integer.MAX_VALUE;
			for(i=0; i<graphSize ; i++)
			{
				if( (best[i] < min) && !checked[i])
				{
					echo("actual:"+points.get(actual).getId());
					actual = i;
					min = best[i];
					//Log.i("poligdzie","aktualny:"+actual);
				}
			}
			echo("tessst");
			
			if ( actual == goal ) 
			{
				finished = true;	
				Log.i("poligdzie","znaleziono punkt goal");
				echo("test5 finished1");
			}
			else if( allNodesChecked())		
			{
				Log.i("poligdzie","nie znaleziono punktu goal");
				finished = true;
				echo("test5 finished2");
			}
			else if(tmpActual == actual)
			{
				if(actualCount  > 5) finished = true;
			}
			else
			{
				actualCount = 0;
			}
				
		}
		while(i != start && finished)
		{
			echo("test5 while");
			Log.i("ROUTE",""+i);
			route.add(0,points.get(i));
			i = previous[i];
		}
		route.add(0,points.get(i));
		Log.i("ROUTE",""+i);
		
	}
	
	private void showUnchecked()
	{
		String ss = "";
		for(int i=0; i< graphSize ;i++)
		{
			if(!checked[i]) ss = ss + points.get(i).getId() + "," ; 
		}
		echo(ss);
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
		int i = 0;
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
