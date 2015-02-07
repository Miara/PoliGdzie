package com.poligdzie.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class DbVersion
{
	
	private int value;
	
	public int getValue()
	{
		return value;
	}

	@JsonProperty("dbVersion")
	public void setValue(int value)
	{
		this.value = value;
	}

	
	
}
