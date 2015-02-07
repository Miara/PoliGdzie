package com.poligdzie.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="exchange_version")
public class RemoteDbVersion
{
	@DatabaseField(columnName="value")
	private int				value;

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public RemoteDbVersion()
	{
		//nie usuwac
	}

}
