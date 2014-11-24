package com.poligdzie.json;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class Legs {
	private ArrayList<Steps> steps;

	public ArrayList<Steps> getSteps() {
		return steps;
	}

	@JsonProperty("steps")
	public void setSteps(ArrayList<Steps> steps) {
		this.steps = steps;
	}
}
