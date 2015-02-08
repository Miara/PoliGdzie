package com.poligdzie.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.Nameable;
import com.poligdzie.interfaces.WithDrawableId;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.singletons.MapFragmentProvider;

public abstract class PoliGdzieBaseFragment extends Fragment implements
															Constants,
															Nameable,
															WithDrawableId
{
	protected DatabaseHelper		dbHelper;
	protected MapDrawingProvider	drawingProvider;
	protected MapFragmentProvider	mapProvider;
	protected String				name;

	public PoliGdzieBaseFragment()
	{
		setUp();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		setUp();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void setUp()
	{
		dbHelper = new DatabaseHelper(this.getActivity(), DatabaseHelper.DATABASE_NAME, null,
				DatabaseHelper.DATABASE_VERSION);
		drawingProvider = MapDrawingProvider.getInstance();
		mapProvider = MapFragmentProvider.getInstance();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		setUp();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int getDrawableId(String name, Context context)
	{
		int resId = context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
		return resId;
	}
}
