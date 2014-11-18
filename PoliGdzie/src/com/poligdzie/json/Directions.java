package com.poligdzie.json;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

import com.google.android.gms.maps.model.PolylineOptions;

public class Directions {
	
	private ArrayList <Routes> routes;

	public ArrayList <Routes> getRoutes() {
		return routes;
	}

	@JsonProperty("routes")
	public void setRoutes(ArrayList <Routes> routes) {
		this.routes = routes;
	}
	
	public PolylineOptions generatePolylineFromDirections(PolylineOptions options) {
		for(Routes route : this.getRoutes()) {
			for(Legs leg : route.getLegs()) {
				for(Steps step : leg.getSteps()) {
					options.addAll(step.getPolyline().decodePolyline());
				}
			}
		}
		return options;
	}
	
}
