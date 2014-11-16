package com.json.poligdzie;

import org.codehaus.jackson.annotate.JsonProperty;

public class Location {
	
	private float lat;
	private float lng;
	
	public float getLat() {
		return lat;
	}
	
	@JsonProperty("lat")
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLng() {
		return lng;
	}
	
	@JsonProperty("lng")
	public void setLng(float lng) {
		this.lng = lng;
	}
}
