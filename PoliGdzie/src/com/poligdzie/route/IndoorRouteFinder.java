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
	private DatabaseHelper		dbHelper;

	List<NavigationConnection>	connections;
	List<NavigationPoint>		points;
	List<NavigationPoint>		currentRoute;
	List<NavigationPoint>		mainRoute;

	private int[][]				graph;
	private boolean[]			checked;
	private int[]				previous;
	private int[]				best;

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
	{
		List<NavigationConnection> connList;
		NavigationPoint first = new NavigationPoint(start.getDoorsX(),
				start.getDoorsY(), start.getFloor(),
				NavigationPointTypes.NAVIGATION);
		NavigationPoint last = new NavigationPoint(goal.getDoorsX(),
				goal.getDoorsY(), goal.getFloor(),
				NavigationPointTypes.NAVIGATION);
		if (init(first, last) != ERROR_CODE)
		{
			connList = getNearestPointAndMakeConnection(first,
					start.getNavigationConnection());
			if (connList != null)
				connections.addAll(connList);
			connList = getNearestPointAndMakeConnection(last,
					goal.getNavigationConnection());
			if (connList != null)
				connections.addAll(connList);
			return findRouteBetweenPoints(first, last);
		} else
		{
			return null;
		}
	}

	public List<NavigationPoint> findRoute(Room start, NavigationPoint goal)
	{
		List<NavigationConnection> connList;
		NavigationPoint first = new NavigationPoint(start.getDoorsX(),
				start.getDoorsY(), start.getFloor(),
				NavigationPointTypes.NAVIGATION);
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
	{
		List<NavigationConnection> connList;
		NavigationPoint last = new NavigationPoint(goal.getDoorsX(),
				goal.getDoorsY(), goal.getFloor(),
				NavigationPointTypes.NAVIGATION);
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
		if (checkIfPointsInOneIndoor(startPoint, goalPoint))
		{
			Building startBulding = startPoint.getFloor().getBuilding();
			Building goalBuilding = goalPoint.getFloor().getBuilding();
			List<Floor> floors = new ArrayList<Floor>();
			List<SpecialConnection> specialConnections = new ArrayList<SpecialConnection>();

			try
			{
				floors = dbHelper.getFloorDao().queryBuilder().where()
						.eq("building_id", startBulding).or()
						.eq("building_id", goalBuilding).query();
				List<NavigationPoint> tempPoints = dbHelper
						.getNavigationPointDao().queryBuilder().where()
						.in("floor_id", floors).query();
				connections = dbHelper.getNavigationConnectionDao()
						.queryBuilder().where()
						.in("navigationPointFirst_id", tempPoints).or()
						.in("navigationPointLast_id", tempPoints).query();
				specialConnections = dbHelper.getSpecialConnectionDao()
						.queryBuilder().where()
						.in("specialPointLower_id", tempPoints).or()
						.in("specialPointUpper_id", tempPoints).query();
			} catch (SQLException e)
			{
				return ERROR_CODE;
			}
			addToConnections(specialConnections);
			return 1;
		} else
		{
			return ERROR_CODE;
		}
	}

	private List<NavigationConnection> getNearestPointAndMakeConnection(
			NavigationPoint point, NavigationConnection connection)
	{
		int connX1, connY1, connX2, connY2, pointX, pointY;
		List<NavigationConnection> newConnections = new ArrayList<NavigationConnection>();

		NavigationConnection finalConnection = addConnectionIfPointOutOfConnection(
				point, connection);
		if (finalConnection != null)
		{
			newConnections.add(finalConnection);
			return newConnections;
		}

		connX1 = connection.getFirstPoint().getCoordX();
		connY1 = connection.getFirstPoint().getCoordY();
		connX2 = connection.getLastPoint().getCoordX();
		connY2 = connection.getLastPoint().getCoordY();

		pointX = point.getCoordX();
		pointY = point.getCoordY();

		if (connX1 == connX2)
		{
			return addConnectionsInsideOtherConnection(connX1, pointY, point,
					connection);
		} else if (connY1 == connY2)
		{
			return addConnectionsInsideOtherConnection(pointX, connY1, point,
					connection);
		} else
		{
			double connFactorB = (connY2 - connY1) / (connY2 - connY1);
			double connFactorA = (connY1 - connFactorB) / connX1;

			double pointFactorA = (-1) / connFactorA;
			double pointFactorB = pointY - pointFactorA * pointX;

			int finalX = (int) ((connFactorA - pointFactorA) / (pointFactorB - connFactorB));
			int finalY = (int) (pointFactorA * finalX + pointFactorB);

			return addConnectionsInsideOtherConnection(finalX, finalY, point,
					connection);
		}
	}

	private NavigationConnection addConnectionIfPointOutOfConnection(
			NavigationPoint point, NavigationConnection connection)
	{
		NavigationPoint first = connection.getFirstPoint();
		NavigationPoint last = connection.getLastPoint();

		int firstX = first.getCoordX();
		int firstY = first.getCoordY();
		int lastX = last.getCoordX();
		int lastY = last.getCoordY();
		int pointX = point.getCoordX();
		int pointY = point.getCoordY();

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
		if (lineMinX < lineMaxX && lineMinY < lineMaxY)
		{
			if ((pointX < lineMinX || pointX > lineMaxX)
					&& (pointY < lineMinY || pointY > lineMaxY))
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
			NavigationConnection connection)
	{
		List<NavigationConnection> conns = new ArrayList<NavigationConnection>();
		NavigationPoint finalPoint = new NavigationPoint(addPointX, addPointY,
				point.getFloor(), NavigationPointTypes.NAVIGATION);
		conns.add(new NavigationConnection(finalPoint, point,
				getConnectionLength(finalPoint, point)));
		conns.add(new NavigationConnection(finalPoint, connection
				.getFirstPoint(), getConnectionLength(finalPoint, point)));
		conns.add(new NavigationConnection(finalPoint, connection
				.getFirstPoint(), getConnectionLength(finalPoint, point)));
		return conns;
	}

	private int getConnectionLength(NavigationPoint p1, NavigationPoint p2)
	{
		int scale = p1.getFloor().getPixelsPerMeter();
		double a = p2.getCoordX() - p1.getCoordX();
		double b = p2.getCoordY() - p1.getCoordY();
		double length = Math.sqrt(a * a + b * b) / scale;
		return (int) length;
	}

	private List<NavigationPoint> findRouteBetweenPoints(
			NavigationPoint startPoint, NavigationPoint goalPoint)
	{
		graphSize = generatePointList();
		prepareGraph();
		fillGraphWithConnectionLength();

		return findShortestPath(startPoint, goalPoint);
	}

	private boolean checkIfPointsInOneIndoor(NavigationPoint startPoint2,
			NavigationPoint goalPoint2)
	{
		Floor floorStart = new Floor();
		Floor floorGoal = new Floor();
		try
		{
			floorStart = dbHelper.getFloorDao().queryForId(
					startPoint2.getFloor().getId());
			floorGoal = dbHelper.getFloorDao().queryForId(
					goalPoint2.getFloor().getId());
		} catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int startBuildingId = floorStart.getBuilding().getId();
		int goalBuildingId = floorGoal.getBuilding().getId();

		if (startBuildingId == goalBuildingId)
		{
			return true;
		} else
		{
			Log.d("POLIGDZIE", "else");
			List<SpecialConnection> specialList = new ArrayList<SpecialConnection>();
			try
			{
				specialList = dbHelper.getSpecialConnectionDao().queryForAll();
				Log.d("POLIGDZIE", specialList.toString());
				for (SpecialConnection conn : specialList)
				{
					int firstBuildingId = conn.getLowerFloor().getFloor()
							.getBuilding().getId();
					int lastBuildingId = conn.getUpperFloor().getFloor()
							.getBuilding().getId();
					if (((firstBuildingId == startBuildingId) && (lastBuildingId == goalBuildingId))
							|| ((firstBuildingId == goalBuildingId) && (lastBuildingId == startBuildingId)))
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

	private List<NavigationPoint> findShortestPath(NavigationPoint p1,
			NavigationPoint p2)
	{
		int i = 0, min;
		int start = getIndex(p1);
		int goal = getIndex(p2);
		boolean finished = false;

		long startTime = System.currentTimeMillis();
		int actual = start;
		best[actual] = 0;

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
			echo("specialConnection:" + special.getId() + ":"
					+ special.getLowerFloor().getId() + ":"
					+ special.getUpperFloor().getId());
			NavigationConnection conn = new NavigationConnection(
					special.getLowerFloor(), special.getUpperFloor(),
					SPECIAL_CONNECTION_LENGTH);
			echo("Connection:" + conn.getFirstPoint().getId() + ":::"
					+ conn.getLastPoint().getId());
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

	private int generatePointList()
	{

		for (NavigationConnection con : connections)
		{
			addPoint(con.getFirstPoint());
			addPoint(con.getLastPoint());
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

	private void echo(String s)
	{
		Log.i("Poligdzie", s);
	}

}
