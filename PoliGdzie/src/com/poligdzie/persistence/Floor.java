package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.interfaces.Nameable;

@DatabaseTable(tableName = "floor")
public class Floor extends PoliGdzieBaseClass implements Nameable
{
	@DatabaseField(generatedId = true)
	private int			id;

	@DatabaseField
	private int			number;

	@DatabaseField
	private String		drawableId;

	@DatabaseField
	private int			width;
	
	@DatabaseField
	private int			height;
	
	@DatabaseField
	private String		name;

	@DatabaseField
	private String		tag;
	
	@DatabaseField
	private int	pixelsPerMeter;

	
	@DatabaseField(foreign = true, foreignAutoRefresh = false, columnName = "building_id", useGetSet = true)
	private Building	building;

	

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getDrawableId()
	{
		return drawableId;
	}

	public void setDrawableId(String drawableId)
	{
		this.drawableId = drawableId;
	}
	
	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public Building getBuilding()
	{
		return building;
	}

	public void setBuilding(Building building)
	{
		this.building = building;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTag()
	{
		return tag;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	public int getPixelsPerMeter()
	{
		return pixelsPerMeter;
	}

	public void setPixelsPerMeter(int pixelsPerMeter)
	{
		this.pixelsPerMeter = pixelsPerMeter;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public Floor()
	{
		// nie wyrzucac!
	}
	//id;name;building;floorNumber;width;height;scheme;tag
	public Floor(String name,Building building,  int number,int width, int height, String drawableId, 
			String tag,int pixelsPerMeter)
	{
		this.number = number;
		this.drawableId = drawableId;
		this.width=width;
		this.height=height;
		this.name = name;
		this.tag = tag;
		this.building = building;
		this.pixelsPerMeter = pixelsPerMeter;
		
	}

}
