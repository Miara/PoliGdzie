package com.poligdzie.route;

import java.sql.SQLException;
import java.util.ArrayList;
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
		this.startPoint = startRoom.getNavigationConnection().getFirstPoint();
		this.goalPoint = goalRoom.getNavigationConnection().getLastPoint();
		
		if( startPoint.compareToFloor(goalPoint.getFloor() ))
		{
			Floor floor = startPoint.getFloor();
			List<NavigationPoint> tmp = dbHelper.getNavigationPointDao().queryBuilder().where().eq("floor_id", floor ).query();
			connections = dbHelper.getNavigationConnectionDao().queryBuilder().
					where().in("navigationPointFirst_id", tmp ).or().in("navigationPointLast_id", tmp).query();
		}
		
		graphSize = generatePointList();
		graph = new int[graphSize][graphSize];
		best = new int[graphSize];
		checked = new boolean[graphSize];
		previous = new int[graphSize];
		echo("graphSize:"+graphSize);
		prepareGraph();
		fillGraphWithConnectionLength();
		
		findShortestPath(startPoint,goalPoint);
		
		return route;
	}
	
	

	private void findShortestPath(NavigationPoint p1, NavigationPoint p2)
	{
		int i=0,min;
		int start = getIndex(p1);
		int goal = getIndex(p2);
		boolean finished = false;
		
		int actual = start;	
		echo ("actual:"+actual);
		echo ("goal:"+goal);
		echo ("graphSize:"+graphSize);
		best[actual] = 0;
		while(!finished)
		{
			for(i=0; i<graphSize ; i++)
			{
				if( (graph[actual][i] > 0) && (best[actual] + graph[actual][i] < best[i]))
				{
					best[i] = best[actual] + graph[actual][i];
					previous[i]=actual;
					Log.i("poligdzie","graphLen:"+graph[actual][i]);
					Log.i("poligdzie",actual+"->"+i+"="+best[i]);
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
					Log.i("poligdzie","aktualny:"+actual);
				}
			}
			
			if ( actual == goal ) 
			{
				finished = true;	
				Log.i("poligdzie","znaleziono punkt goal");
			}
			else if( allNodesChecked())		
			{
				Log.i("poligdzie","nie znaleziono punktu goal");
				break;
			}
				
		}
		i = goal;
		while(i != start && finished)
		{
			Log.i("ROUTE",""+i);
			route.add(0,points.get(i));
			i = previous[i];
		}
		route.add(0,points.get(i));
		Log.i("ROUTE",""+i);
		
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
					echo("nie dodano");
					return;
				}
					
			}

			echo("dodano "+point.getId()+":"+i);
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
