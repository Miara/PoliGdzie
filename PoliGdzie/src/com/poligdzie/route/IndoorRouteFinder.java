package com.poligdzie.route;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationConnection;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.NavigationPointTypes;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.SpecialConnection;

public class IndoorRouteFinder implements Constants
{
	private DatabaseHelper		dbHelper;
	List<NavigationConnection>	connections;
	List<NavigationPoint>		points;
	List<NavigationPoint>		currentRoute;
	List<NavigationPoint>		mainRoute;

	private int[][]				graph;
	private boolean[]			checked;
	private int[]				previous;
	private int[]				best;

	private int					point_id	= NEW_NAVIGATION_POINT_ID;

	private int					graphSize;

	public IndoorRouteFinder(DatabaseHelper dbHelper)
	{
		this.dbHelper = dbHelper;
		connections = new ArrayList<NavigationConnection>();
		points = new ArrayList<NavigationPoint>();
		currentRoute = new ArrayList<NavigationPoint>();
		mainRoute = new ArrayList<NavigationPoint>();

	}

	public List<NavigationPoint> findRoute(Room start, Room goal)
			throws SQLException
	{
		List<NavigationConnection> connList;
		NavigationPoint first = new NavigationPoint(start.getDoorsX(),
				start.getDoorsY(), start.getFloor(),
				NavigationPointTypes.NAVIGATION);
		first.setId(point_id++);
		NavigationPoint last = new NavigationPoint(goal.getDoorsX(),
				goal.getDoorsY(), goal.getFloor(),
				NavigationPointTypes.NAVIGATION);
		last.setId(point_id++);
		// TODO: wywalic error code i sprawdzanie go za kazdym razem - lepiej
		// swignac wyjatkiem
		if (init(first, last) != ERROR_CODE)
		{

			NavigationConnection goalNavigationConnection = dbHelper
					.getNavigationConnectionDao().queryForId(
							goal.getNavigationConnection().getId());
			NavigationConnection startNavigationConnection = dbHelper
					.getNavigationConnectionDao().queryForId(
							start.getNavigationConnection().getId());

			goal.setNavigationConnection(goalNavigationConnection);
			start.setNavigationConnection(startNavigationConnection);

			connList = getNearestPointAndMakeConnection(first,
					startNavigationConnection);
			if (connList != null)
				connections.addAll(connList);
			connList = getNearestPointAndMakeConnection(last,
					goalNavigationConnection);
			if (connList != null)
				connections.addAll(connList);
			return findRouteBetweenPoints(first, last);
		} else
		{
			return null;
		}
	}

	public List<NavigationPoint> findRoute(Room start, NavigationPoint goal)
			throws SQLException
	{
		List<NavigationConnection> connList;
		NavigationPoint first = new NavigationPoint(start.getDoorsX(),
				start.getDoorsY(), start.getFloor(),
				NavigationPointTypes.NAVIGATION);
		first.setId(point_id++);
		if (init(first, goal) != ERROR_CODE)
		{
			connList = getNearestPointAndMakeConnection(first,
					start.getNavigationConnection());
			if (connList != null)
				connections.addAll(connList);
			return findRouteBetweenPoints(first, goal);
		} else
		{
			return null;
		}
	}

	public List<NavigationPoint> findRoute(NavigationPoint start, Room goal)
			throws SQLException
	{
		List<NavigationConnection> connList;
		NavigationPoint last = new NavigationPoint(goal.getDoorsX(),
				goal.getDoorsY(), goal.getFloor(),
				NavigationPointTypes.NAVIGATION);
		last.setId(point_id++);
		if (init(start, last) != ERROR_CODE)
		{
			connList = getNearestPointAndMakeConnection(last,
					goal.getNavigationConnection());
			if (connList != null)
				connections.addAll(connList);
			return findRouteBetweenPoints(start, last);
		} else
		{
			return null;
		}
	}

	public List<NavigationPoint> findRoute(NavigationPoint start,
			NavigationPoint goal)
	{
		if (init(start, goal) != ERROR_CODE)
		{
			return findRouteBetweenPoints(start, goal);
		} else
		{
			return null;
		}
	}

	private int init(NavigationPoint startPoint, NavigationPoint goalPoint)
	{
		int startBuildingId = 0;
		int goalBuildingId = 0;
		try
		{
			startBuildingId = dbHelper.getFloorDao()
					.queryForId(startPoint.getFloor().getId()).getBuilding()
					.getId();
			goalBuildingId = dbHelper.getFloorDao()
					.queryForId(goalPoint.getFloor().getId()).getBuilding()
					.getId();

			List<Floor> floors = new ArrayList<Floor>();
			List<SpecialConnection> specialConnections = new ArrayList<SpecialConnection>();
			floors = dbHelper.getFloorDao().queryBuilder().where()
					.eq("building_id", startBuildingId).or()
					.eq("building_id", goalBuildingId).query();
			List<NavigationPoint> tempPoints = dbHelper.getNavigationPointDao()
					.queryBuilder().where().in("floor_id", floors).query();
			connections = dbHelper.getNavigationConnectionDao().queryBuilder()
					.where().in("navigationPointFirst_id", tempPoints).or()
					.in("navigationPointLast_id", tempPoints).query();
			specialConnections = dbHelper.getSpecialConnectionDao()
					.queryBuilder().where()
					.in("specialPointLower_id", tempPoints).or()
					.in("specialPointUpper_id", tempPoints).query();

			addToConnections(specialConnections);
			return 1;
		} catch (SQLException e)
		{
			return ERROR_CODE;
		}

	}

	private List<NavigationConnection> getNearestPointAndMakeConnection(
			NavigationPoint point, NavigationConnection connection)
			throws SQLException
	{
		double connX1, connY1, connX2, connY2, pointX, pointY;

		connection = dbHelper.getNavigationConnectionDao().queryForId(
				connection.getId());

		NavigationPoint firstPoint = dbHelper.getNavigationPointDao()
				.queryForId(connection.getFirstPoint().getId());
		NavigationPoint lastPoint = dbHelper.getNavigationPointDao()
				.queryForId(connection.getLastPoint().getId());
		;

		connection.setFirstPoint(firstPoint);
		connection.setLastPoint(lastPoint);

		connX1 = firstPoint.getCoordX();
		connY1 = firstPoint.getCoordY();
		connX2 = lastPoint.getCoordX();
		connY2 = lastPoint.getCoordY();

		pointX = point.getCoordX();
		pointY = point.getCoordY();

		if (connX1 == connX2)
		{
			return addConnectionsInsideOtherConnection((int) connX1,
					(int) pointY, point, connection);
		} else if (connY1 == connY2)
		{
			return addConnectionsInsideOtherConnection((int) pointX,
					(int) connY1, point, connection);
		} else
		{
			double one = (connY2 - connY1);
			double two = (connX2 - connX1);
			double connFactorA = one / two;
			double connFactorB = connY1 - (connFactorA * connX1);
			double pointFactorA = (-1) / connFactorA;
			double pointFactorB = pointY - pointFactorA * pointX;
			double finalX = (connFactorB - pointFactorB)
					/ (pointFactorA - connFactorA);
			double finalY = pointFactorA * finalX + pointFactorB;

			return addConnectionsInsideOtherConnection((int) finalX,
					(int) finalY, point, connection);
		}
	}

	private NavigationConnection addConnectionIfPointOutOfConnection(
			int pointX, int pointY, NavigationPoint point,
			NavigationConnection connection) throws SQLException
	{
		NavigationPoint first = dbHelper.getNavigationPointDao().queryForId(
				connection.getFirstPoint().getId());
		NavigationPoint last = dbHelper.getNavigationPointDao().queryForId(
				connection.getLastPoint().getId());
		;

		connection.setFirstPoint(first);
		connection.setLastPoint(last);

		int firstX = first.getCoordX();
		int firstY = first.getCoordY();
		int lastX = last.getCoordX();
		int lastY = last.getCoordY();

		NavigationConnection conn = null;

		conn = getConnectionIfpointOutOfLine(pointX, pointY, lastX, firstX,
				lastY, firstY, first, last, point);
		if (conn != null)
			return conn;
		conn = getConnectionIfpointOutOfLine(pointX, pointY, lastX, firstX,
				firstY, lastY, first, last, point);
		if (conn != null)
			return conn;
		conn = getConnectionIfpointOutOfLine(pointX, pointY, firstX, lastX,
				lastY, firstY, first, last, point);
		if (conn != null)
			return conn;
		conn = getConnectionIfpointOutOfLine(pointX, pointY, firstX, lastX,
				firstY, lastY, first, last, point);
		if (conn != null)
			return conn;

		return null;

	}

	private NavigationConnection getConnectionIfpointOutOfLine(int pointX,
			int pointY, int lineMinX, int lineMaxX, int lineMinY, int lineMaxY,
			NavigationPoint first, NavigationPoint last, NavigationPoint point)
	{
		if (lineMinX <= lineMaxX && lineMinY <= lineMaxY)
		{
			if ((pointX <= lineMinX || pointX >= lineMaxX)
					&& (pointY <= lineMinY || pointY >= lineMaxY))
			{
				if (getConnectionLength(first, point) < getConnectionLength(
						last, point))
				{
					return new NavigationConnection(first, point,
							getConnectionLength(first, point));
				} else
				{
					return new NavigationConnection(last, point,
							getConnectionLength(last, point));
				}
			}
		}
		return null;
	}

	private List<NavigationConnection> addConnectionsInsideOtherConnection(
			int addPointX, int addPointY, NavigationPoint point,
			NavigationConnection connection) throws SQLException
	{
		List<NavigationConnection> newConnections = new ArrayList<NavigationConnection>();

		NavigationConnection finalConnection = addConnectionIfPointOutOfConnection(
				addPointX, addPointY, point, connection);
		if (finalConnection != null)
		{
			newConnections.add(finalConnection);
			return newConnections;
		}

		List<NavigationConnection> conns = new ArrayList<NavigationConnection>();
		NavigationPoint finalPoint = new NavigationPoint(addPointX, addPointY,
				point.getFloor(), NavigationPointTypes.NAVIGATION);
		finalPoint.setId(point_id++);
		conns.add(new NavigationConnection(finalPoint, point,
				getConnectionLength(finalPoint, point)));
		conns.add(new NavigationConnection(finalPoint, connection
				.getFirstPoint(), getConnectionLength(finalPoint,
				connection.getFirstPoint())));
		conns.add(new NavigationConnection(finalPoint, connection
				.getLastPoint(), getConnectionLength(finalPoint,
				connection.getLastPoint())));
		return conns;
	}

	private int getConnectionLength(NavigationPoint p1, NavigationPoint p2)
	{

		int scale;
		try
		{
			scale = dbHelper.getFloorDao().queryForId(p1.getFloor().getId())
					.getPixelsPerMeter();
			double a = p2.getCoordX() - p1.getCoordX();
			double b = p2.getCoordY() - p1.getCoordY();
			double length = Math.sqrt(a * a + b * b) / scale;
			return (int) length;
		} catch (SQLException e)
		{
			return 0;
		}
	}

	private List<NavigationPoint> findRouteBetweenPoints(
			NavigationPoint startPoint, NavigationPoint goalPoint)
	{
		try
		{
			graphSize = generatePointList();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		prepareGraph();
		fillGraphWithConnectionLength();

		return findShortestPath(startPoint, goalPoint);
	}

	private List<NavigationPoint> findShortestPath(NavigationPoint p1,
			NavigationPoint p2)
	{
		int i = 0, min;
		int start = getIndex(p1);
		int goal = getIndex(p2);

		boolean finished = false;

		long startTime = System.currentTimeMillis();
		int actual = start;
		try
		{
			best[actual] = 0;
		} catch (ArrayIndexOutOfBoundsException e)
		{
			Log.e("poligdzie", "ARRAY INDEX OUT !!");
			return new ArrayList<NavigationPoint>();
		}

		while (!finished)
		{
			for (i = 0; i < graphSize; i++)
			{
				if ((graph[actual][i] > 0)
						&& (best[actual] + graph[actual][i] < best[i])
						&& (!checked[i]))
				{
					best[i] = best[actual] + graph[actual][i];
					previous[i] = actual;
				}
			}
			checked[actual] = true;
			min = Integer.MAX_VALUE;
			for (i = 0; i < graphSize; i++)
			{
				if ((best[i] < min) && !checked[i])
				{
					actual = i;
					min = best[i];
				}
			}

			if (actual == goal)
			{
				finished = true;
				Log.i("poligdzie", "znaleziono punkt goal");
			} else if (allNodesChecked()
					|| ((System.currentTimeMillis() - startTime) > 5000))
			{
				Log.i("poligdzie", "nie znaleziono punktu goal");
				break;
			}

		}

		while ((actual != start) && finished)
		{
			currentRoute.add(0, points.get(actual));
			if (previous[actual] != -1)
			{
				actual = previous[actual];
			} else
			{
				break;
			}
		}
		currentRoute.add(0, points.get(actual));

		return currentRoute;

	}

	private void addToConnections(List<SpecialConnection> specialConnections)
	{
		for (SpecialConnection special : specialConnections)
		{
			NavigationConnection conn = new NavigationConnection(
					special.getLowerPoint(), special.getUpperPoint(),
					SPECIAL_CONNECTION_LENGTH);
			connections.add(conn);
		}
		;
	}

	private boolean allNodesChecked()
	{
		for (int i = 0; i < graphSize; i++)
		{
			if (!checked[i])
				return false;
		}
		return true;
	}

	private void prepareGraph()
	{
		graph = new int[graphSize][graphSize];
		best = new int[graphSize];
		checked = new boolean[graphSize];
		previous = new int[graphSize];
		for (int i = 0; i < graphSize; i++)
		{
			best[i] = Integer.MAX_VALUE;
			previous[i] = -1;
			checked[i] = false;
			for (int j = 0; j < graphSize; j++)
			{
				graph[i][j] = 0;
			}
		}
	}

	private void fillGraphWithConnectionLength()
	{
		for (NavigationConnection con : connections)
		{
			int x = getIndex(con.getFirstPoint());
			int y = getIndex(con.getLastPoint());
			graph[x][y] = con.getLength();
			graph[y][x] = con.getLength();
		}
	}

	private int generatePointList() throws SQLException
	{

		NavigationPoint first = null, last = null;
		for (NavigationConnection con : connections)
		{
			first = con.getFirstPoint();
			last = con.getLastPoint();
			if (first.getFloor() == null || last.getFloor() == null)
			{
				first = dbHelper.getNavigationPointDao().queryForId(
						con.getFirstPoint().getId());
				last = dbHelper.getNavigationPointDao().queryForId(
						con.getLastPoint().getId());
			}
			addPoint(first);
			addPoint(last);
		}
		return points.size();
	}

	private void addPoint(NavigationPoint point)
	{
		if (point != null)
		{
			for (NavigationPoint p : points)
			{
				if (p.getId() == point.getId())
				{
					return;
				}

			}

			points.add(point);
		} else
		{
			Log.i("IndoorRouteFinder", "nie dodano punktu");
		}
	}

	private int getIndex(NavigationPoint point)
	{
		for (NavigationPoint p : points)
		{
			if (p.getId() == point.getId())
			{
				return points.indexOf(p);
			}
		}
		return -1;
	}

}
