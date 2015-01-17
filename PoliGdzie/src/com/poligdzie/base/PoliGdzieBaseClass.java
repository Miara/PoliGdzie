package com.poligdzie.base;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.WithDrawableId;

public abstract class PoliGdzieBaseClass implements WithDrawableId, Constants
{
	protected DatabaseHelper	dbHelper;

	@Override
	public int getDrawableId(String name, Context context)
	{
		int resId = context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
		return resId;
	}

	public void makeToast(String text, Context context)
	{
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}

	public void echo(String text)
	{
		Log.i("Poligdzie", text);
	}
}
