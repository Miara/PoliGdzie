package com.poligdzie.route;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationConnection;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.NavigationPointTypes;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.SpecialConnection;

public class IndoorRouteFinder implements Constants
{
	private DatabaseHelper dbHelper;
	List<NavigationConnection> connections;
	List<NavigationPoint> points;
	List<NavigationPoint> currentRoute;
	List<NavigationPoint> mainRoute;
	
	private int[][] graph;
	private boolean[] checked;
	private int[] previous;
	private int[] best;
	
	private int point_id = NEW_NAVIGATION_POINT_ID;

	private int graphSize;
		
	public IndoorRouteFinder(DatabaseHelper dbHelper)
	{
		this.dbHelper = dbHelper;
		connections = new ArrayList<NavigationConnection>();
		points = new ArrayList<NavigationPoint>();
		currentRoute = new ArrayList<NavigationPoint>();
		mainRoute = new ArrayList<NavigationPoint>();
		
	}

	public List<NavigationPoint> findRoute(Room start, Room goal)
	{
		List<NavigationConnection> connList;
		NavigationPoint first = new NavigationPoint(start.getDoorsX(),start.getDoorsY(),
				start.getFloor(),NavigationPointTypes.NAVIGATION);
		first.setId(point_id++);
		NavigationPoint last  = new NavigationPoint(goal.getDoorsX(),goal.getDoorsY(),
				goal.getFloor(),NavigationPointTypes.NAVIGATION);
		last.setId(point_id++);
		
		if(init(first,last) != ERROR_CODE )
		{
			connList = getNearestPointAndMakeConnection(first, start.getNavigationConnection());
			echo("conn1:"+connList.size());
			if(connList != null) connections.addAll(connList);
			connList = getNearestPointAndMakeConnection(last, goal.getNavigationConnection());
			echo("conn1:"+connList.size());
			if(connList != null) connections.addAll(connList);
			return findRouteBetweenPoints(first, last);
		}
		else
		{
			return null;
		}
	}
	
	public List<NavigationPoint> findRoute(Room start, NavigationPoint goal)
	{
		List<NavigationConnection> connList;
		NavigationPoint first = new NavigationPoint(start.getDoorsX(),start.getDoorsY(),
				start.getFloor(),NavigationPointTypes.NAVIGATION);
		if(init(first,goal) != ERROR_CODE )
		{
			connList = getNearestPointAndMakeConnection(first, start.getNavigationConnection());
			if(connList != null) connections.addAll(connList);
			return findRouteBetweenPoints(first, goal);
		}
		else
		{
			return null;
		}
	}
	
	public List<NavigationPoint> findRoute(NavigationPoint start, Room goal)
	{
		List<NavigationConnection> connList;
		NavigationPoint last = new NavigationPoint(goal.getDoorsX(),goal.getDoorsY(),
				goal.getFloor(),NavigationPointTypes.NAVIGATION);
		if(init(start,last) != ERROR_CODE )
		{
			connList = getNearestPointAndMakeConnection(last, goal.getNavigationConnection());
			if(connList != null) connections.addAll(connList);
			return findRouteBetweenPoints(start, last);
		}
		else
		{
			return null;
		}
	}
	
	public List<NavigationPoint> findRoute(NavigationPoint start, NavigationPoint goal)
	{
		if(init(start,goal) != ERROR_CODE )
		{
			return findRouteBetweenPoints(start, goal);
		}
		else
		{
			return null;
		}
	}
	
	private int init(NavigationPoint startPoint, NavigationPoint goalPoint) 
	{
		int startBuildingId = 0;
		int goalBuildingId =0;
		try
		{
			startBuildingId = dbHelper.getFloorDao().
					queryForId(startPoint.getFloor().getId()).getBuilding().getId();
			goalBuildingId = dbHelper.getFloorDao().
						queryForId(goalPoint.getFloor().getId()).getBuilding().getId();
		} catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		if( checkIfPointsInOneIndoor(startBuildingId,goalBuildingId) )
		{
			List<Floor> floors = new ArrayList<Floor>();
			List<SpecialConnection> specialConnections = new ArrayList<SpecialConnection>();
			
			try
			{
				floors = dbHelper.getFloorDao().queryBuilder().where().
						eq("building_id", startBuildingId).or().eq("building_id", goalBuildingId).query();
				List<NavigationPoint> tempPoints = dbHelper.getNavigationPointDao().queryBuilder()
						.where().in("floor_id", floors ).query();
				connections = dbHelper.getNavigationConnectionDao().queryBuilder()
						.where().in("navigationPointFirst_id", tempPoints ).or().in("navigationPointLast_id", tempPoints).query();
				specialConnections = dbHelper.getSpecialConnectionDao().queryBuilder()
						.where().in("specialPointLower_id", tempPoints ).or().in("specialPointUpper_id", tempPoints).query();
			} catch (SQLException e)
			{
				return ERROR_CODE;
			}
			addToConnections(specialConnections);
			return 1;
		}
		else
		{
			return ERROR_CODE;
		}
	}
	
	private List<NavigationConnection> getNearestPointAndMakeConnection(NavigationPoint point, NavigationConnection connection)
	{
		double connX1,connY1,connX2,connY2,pointX,pointY;

		connX1 = connection.getFirstPoint().getCoordX();
		connY1 = connection.getFirstPoint().getCoordY();
		connX2 = connection.getLastPoint().getCoordX();
		connY2 = connection.getLastPoint().getCoordY();
		
		pointX = point.getCoordX();
		pointY = point.getCoordY();
		
		if(connX1 == connX2)
		{
			return addConnectionsInsideOtherConnection((int)connX1, (int)pointY, point, connection);
		}
		else if(connY1 == connY2)
		{
			return addConnectionsInsideOtherConnection((int)pointX, (int)connY1, point, connection);
		}
		else
		{
			double one = (connY2 - connY1);
			double two =(connX2 - connX1);
			double connFactorA = one/two;
			double connFactorB = connY1 - (connFactorA*connX1);
			double pointFactorA = (-1)/connFactorA;
			double pointFactorB = pointY - pointFactorA*pointX;
			double finalX = (connFactorB - pointFactorB)/(pointFactorA - connFactorA);
			double finalY = pointFactorA*finalX + pointFactorB;
		
			
			
			return addConnectionsInsideOtherConnection((int)finalX, (int)finalY, point, connection);
		}
	}
	
	private  NavigationConnection  addConnectionIfPointOutOfConnection(int pointX,int pointY,NavigationPoint point, NavigationConnection connection)
	{
		NavigationPoint first = connection.getFirstPoint();
		NavigationPoint last = connection.getLastPoint();
		
		int firstX = first.getCoordX();
		int firstY  = first.getCoordY();
		int lastX  =  last.getCoordX();
		int lastY = last.getCoordY();
		
		NavigationConnection conn = null;
		
		conn = getConnectionIfpointOutOfLine(pointX,pointY,lastX,firstX,lastY,firstY,first,last,point);
		if(conn != null) return conn;
		conn = getConnectionIfpointOutOfLine(pointX,pointY,lastX,firstX,firstY,lastY,first,last,point);
		if(conn != null) return conn;
		conn = getConnectionIfpointOutOfLine(pointX,pointY,firstX,lastX,lastY,firstY,first,last,point);
		if(conn != null) return conn;
		conn = getConnectionIfpointOutOfLine(pointX,pointY,firstX,lastX,firstY,lastY,first,last,point);
		if(conn != null) return conn;
		
		return null;

	}
	
	private NavigationConnection getConnectionIfpointOutOfLine(int pointX,int pointY,int lineMinX,int lineMaxX,int lineMinY,int lineMaxY
			,NavigationPoint first,NavigationPoint last,NavigationPoint point)
	{
		if(lineMinX <= lineMaxX && lineMinY <= lineMaxY)
		{
			if( (pointX <= lineMinX || pointX >= lineMaxX) 
					&& (pointY <= lineMinY || pointY >= lineMaxY) )
			{
				if(getConnectionLength(first, point) < getConnectionLength(last, point))
				{
					return new NavigationConnection(first,point,getConnectionLength(first, point));
				}
				else
				{
					return new NavigationConnection(last,point,getConnectionLength(last, point));
				}
			}	
		}
		return null;
	}
	
	private List<NavigationConnection> addConnectionsInsideOtherConnection(int addPointX, int addPointY, 
			NavigationPoint point,NavigationConnection connection)
	{
		List<NavigationConnection> newConnections = new ArrayList<NavigationConnection>();
		
		NavigationConnection  finalConnection  = addConnectionIfPointOutOfConnection(addPointX,addPointY,point,connection);
		if( finalConnection != null)
		{
			newConnections.add(finalConnection);
			return newConnections;
		}
		
		List<NavigationConnection> conns = new ArrayList<NavigationConnection>();
		NavigationPoint finalPoint = new NavigationPoint(addPointX,addPointY,
				point.getFloor(),NavigationPointTypes.NAVIGATION);
		finalPoint.setId(point_id++);
		conns.add(new NavigationConnection(finalPoint,point, 
				getConnectionLength(finalPoint, point)));
		conns.add(new NavigationConnection(finalPoint,connection.getFirstPoint(), 
				getConnectionLength(finalPoint, connection.getFirstPoint())));
		conns.add(new NavigationConnection(finalPoint,connection.getLastPoint(), 
				getConnectionLength(finalPoint, connection.getLastPoint())));
		return conns;
	}
	
	private int getConnectionLength(NavigationPoint p1, NavigationPoint p2)
	{
			int scale = p1.getFloor().getPixelsPerMeter();
			double a = p2.getCoordX() - p1.getCoordX();
			double b = p2.getCoordY() - p1.getCoordY();
			double length = Math.sqrt(a*a + b*b) / scale;
			return (int)length;
	}

	private List<NavigationPoint>  findRouteBetweenPoints(NavigationPoint startPoint, NavigationPoint goalPoint) 
	{
			graphSize = generatePointList();
			prepareGraph();
			fillGraphWithConnectionLength();
			
			return findShortestPath(startPoint,goalPoint);
	}
	
	private boolean checkIfPointsInOneIndoor(int startBuildingId, int goalBuildingId)
	{
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

	private List<NavigationPoint> findShortestPath(NavigationPoint p1, NavigationPoint p2)
	{
		int i=0,min;
		int start = getIndex(p1);
		int goal = getIndex(p2);
		echo("p1:"+p1.getId());
		echo("p2:"+p2.getId());
		
		boolean finished = false;
		
		long startTime = System.currentTimeMillis() ;
		int actual = start;	
		try
		{
			best[actual] = 0;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			echo("ARRAY INDEX OUT !!");
			return new ArrayList<NavigationPoint>();
		}
		
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
			
			
			if ( actual == goal ) 
			{
				finished = true;	
				Log.i("poligdzie","znaleziono punkt goal");
			}
			else if( allNodesChecked()  || (  (System.currentTimeMillis() - startTime) > 5000))		
			{
				Log.i("poligdzie","nie znaleziono punktu goal");
				break;	
			}
			
				
		}
		
		while( (actual != start) && finished)
		{
			currentRoute.add(0,points.get(actual));
			if(previous[actual] != -1)
			{
				echo("ROUTE:"+points.get(actual).getId());
				actual = previous[actual];
			}
			else
			{
				break;
			}
		}
		currentRoute.add(0,points.get(actual));
		
		return currentRoute;
			
		
	}
	
	private void addToConnections( List<SpecialConnection> specialConnections)
	{
		for(SpecialConnection special : specialConnections)
		{
			NavigationConnection conn = new NavigationConnection(special.getLowerPoint(),
					special.getUpperPoint(),SPECIAL_CONNECTION_LENGTH);
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
		graph = new int[graphSize][graphSize];
		best = new int[graphSize];
		checked = new boolean[graphSize];
		previous = new int[graphSize];
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
