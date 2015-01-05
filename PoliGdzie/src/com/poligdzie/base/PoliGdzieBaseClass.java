package com.poligdzie.base;

import android.content.Context;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.WithDrawableId;

public abstract class PoliGdzieBaseClass implements WithDrawableId
{
	protected DatabaseHelper	dbHelper;

	@Override
	public int getDrawableId(String name, Context context)
	{
		int resId = context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
		return resId;
	}
}
