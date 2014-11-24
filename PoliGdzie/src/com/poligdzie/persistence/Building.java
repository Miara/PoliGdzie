package com.poligdzie.persistence;

import com.google.android.gms.maps.model.LatLng;

@DatabaseTable(tableName = "building")
public class Building {
	@DatabaseField (id = true)
	private int id;
	private LatLng coordinates;
	private String address;
	private int width;
	private int height;
	
	
}
