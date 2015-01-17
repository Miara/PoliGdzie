package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;

@DatabaseTable(tableName = "navigationConnection")
public class NavigationConnection extends PoliGdzieBaseClass
{
	@DatabaseField(generatedId = true)
	private int				id;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "navigationPointFirst_id", useGetSet = true)
	private NavigationPoint	firstPoint;

	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "navigationPointLast_id", useGetSet = true)
	private NavigationPoint	lastPoint;

	@DatabaseField
	private int				length;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public NavigationPoint getFirstPoint()
	{
		return firstPoint;
	}

	public void setFirstPoint(NavigationPoint firstPoint)
	{
		this.firstPoint = firstPoint;
	}

	public NavigationPoint getLastPoint()
	{
		return lastPoint;
	}

	public void setLastPoint(NavigationPoint lastPoint)
	{
		this.lastPoint = lastPoint;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public NavigationConnection()
	{
		// nie wyrzucac !
	}

	public NavigationConnection(NavigationPoint first, NavigationPoint last,
			int length)
	{
		this.firstPoint = first;
		this.lastPoint = last;
		this.length = length;
	}
}