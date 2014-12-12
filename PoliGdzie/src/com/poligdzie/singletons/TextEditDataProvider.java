package com.poligdzie.singletons;

import android.view.LayoutInflater;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.persistence.DatabaseHelper;

public class TextEditDataProvider {
	private static TextEditDataProvider instance = null;
	private GoogleMap map;
	private DatabaseHelper dbHelper;
	private LayoutInflater layoutInflater;
	private PolylineOptions options;
	
	private Object from;
	private Object to;
	private String mode;
	

	
	protected TextEditDataProvider() {
		// konstruktor zas³aniaj¹cy domyœlny publiczny konstruktor
	}
	
	//implementacja singletona
	public static TextEditDataProvider getInstance() {
		if(instance == null) {
			instance =  new TextEditDataProvider();
		}
		return instance;
	}

	public Object getFrom() {
		return from;
	}

	public void setFrom(Object from) {
		this.from = from;
	}

	public Object getTo() {
		return to;
	}

	public void setTo(Object to) {
		this.to = to;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
	
}
