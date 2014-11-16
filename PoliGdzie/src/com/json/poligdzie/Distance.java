package com.json.poligdzie;

import org.codehaus.jackson.annotate.JsonProperty;

public class Distance {
	
	private String text;
	private int value;
	
	public String getText() {
		return text;
	}
	
	@JsonProperty("text")
	public void setText(String text) {
		this.text = text;
	}
	public int getValue() {
		return value;
	}
	
	@JsonProperty("value")
	public void setValue(int value) {
		this.value = value;
	}
}
