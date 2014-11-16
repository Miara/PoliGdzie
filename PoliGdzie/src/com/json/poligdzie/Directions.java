package com.json.poligdzie;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class Directions {
	
	private ArrayList <Routes> routes;

	public ArrayList <Routes> getRoutes() {
		return routes;
	}

	@JsonProperty("routes")
	public void setRoutes(ArrayList <Routes> routes) {
		this.routes = routes;
	}
	
}
