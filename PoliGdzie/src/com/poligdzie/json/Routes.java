package com.poligdzie.json;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class Routes {
	private ArrayList <Legs> legs;

	public ArrayList <Legs> getLegs() {
		return legs;
	}

	@JsonProperty("legs")
	public void setLegs(ArrayList <Legs> legs) {
		this.legs = legs;
	}
}
