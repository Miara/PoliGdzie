package com.poligdzie.persistence;

import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.Nameable;

public class GPSLocation implements Nameable,Constants
{
	private String name;
	private double latitude;
	private double longitude;
	
	
	public GPSLocation(String name, double longitude, double latitude)
	{
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	

	@Override
	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public Double getCoordX()
	{
		return longitude;
	}
	
	public Double getCoordY()
	{
		return latitude;
	}

	
}
