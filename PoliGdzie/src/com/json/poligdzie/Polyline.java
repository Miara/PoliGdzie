package com.json.poligdzie;

import org.codehaus.jackson.annotate.JsonProperty;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Polyline {
	private String points;

	public String getPoints() {
		return points;
	}

	@JsonProperty("points")
	public void setPoints(String points) {
		this.points = points;
	}
	
	public LatLng decodePolyline() {
		char array_points[] = new char[points.length()];
		int lat_array[] = new int[6];
		int lng_array[] = new int[6];
		int int_temp_array[] = new int[points.length()];
		int sum = 0;
		int lat =0 , lng =0;
		array_points = points.toCharArray();
		int counter = 0;
		int shift = 0;
		int flag = 0;
		
		for(int i=0; i<points.length(); i++) {
			if(flag == 1)
				break;
			int ascii = (int) array_points[i];
			ascii -= 63;
			
			if(ascii < 0x20) {
				counter = i;
				flag = 1;
			}
			
			ascii = ascii & 31;
			ascii = ascii << shift;
			
			lat += ascii;
			
			
		}
		int index = 0;
		shift = 0;
		flag= 0;
		
		for(int i=counter; i<points.length(); i++) {
			if(flag == 1) 
				break;
			int ascii = (int) array_points[i];
			ascii -= 63;
			
			if(ascii < 0x20) {
				counter = i;
				flag = 1;
			}
			ascii = ascii & 31;
			ascii = ascii << shift;
			
			lng += ascii;
		}
		
		if((lat & 1) == 1) {
			lat = ~ lat;
		}
		
		if((lng & 1) == 1) {
			lng = ~ lng;
		}
		
	
		double double_lng = 0.0, double_lat = 0.0;
		double_lng = (double) lng / 1e5;
		double_lat = (double) lat / 1e5;
		
		
		
		return new LatLng(double_lat, double_lng);
		
	
		
		
	}
}
