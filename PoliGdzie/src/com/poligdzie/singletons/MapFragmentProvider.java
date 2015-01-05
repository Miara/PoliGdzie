package com.poligdzie.singletons;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.fragments.PoliGdzieMapFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.helpers.FragmentMapHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.WithDrawableId;

public class MapFragmentProvider implements Constants, WithDrawableId
{

	private static MapFragmentProvider	instance			= null;
	private DatabaseHelper				dbHelper;
	private PolylineOptions				options;
	private static Context				context;

	private FragmentMapHelper			fragments;
	private List<String>				keys;
	private String						currentKey;

	static final LatLng					LOCATION_PIOTROWO	= new LatLng(
																	52.4022703,
																	16.9495847);

	protected MapFragmentProvider()
	{
		// konstruktor zas³aniaj¹cy domyœlny publiczny konstruktor
		fragments = new FragmentMapHelper();
		keys = new ArrayList<String>();
	}

	// implementacja singletona
	public static MapFragmentProvider getInstance()
	{
		if (instance == null)
		{
			instance = new MapFragmentProvider();
		}
		return instance;
	}

	public void addFragment(String tag, PoliGdzieMapFragment fragment)
	{
		fragments.addFragment(tag, fragment);
		keys.add(tag);
	}

	private PoliGdzieMapFragment getFragmentPreviousOrNext(int key_position)
	{
		String key = keys.get(key_position);
		if (key != null)
		{
			return fragments.getFragmentByTag(key);
		}
		return null;
	}

	public PoliGdzieMapFragment getPreviousFragment()
	{
		int previous_key_position = keys.indexOf(currentKey) - 1;
		return getFragmentPreviousOrNext(previous_key_position);
	}

	public PoliGdzieMapFragment getNextFragment()
	{
		int next_key_position = keys.indexOf(currentKey) + 1;
		return getFragmentPreviousOrNext(next_key_position);
	}

	public String getNextKey()
	{
		int resultKey = keys.indexOf(currentKey) + 1;
		if (resultKey >= keys.size())
			return null;

		String result = keys.get(resultKey);

		if (result != null)
			return result;
		else
			return null;
	}

	public String getPreviousKey()
	{
		int resultKey = keys.indexOf(currentKey) - 1;
		if (resultKey < 0)
			return null;

		String result = keys.get(resultKey);

		if (result != null)
			return result;
		else
			return null;
	}

	@Override
	public int getDrawableId(String name, Context context)
	{
		int resId = context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
		return resId;
	}

	public PoliGdzieMapFragment getCurrentFragment()
	{
		return fragments.getFragmentByTag(currentKey);
	}

	public int getCurrentFragmentKeyPosition()
	{
		return keys.indexOf(currentKey);
	}

	public int getFragmentsSize()
	{
		return fragments.getSize();
	}

	public String getCurrentKey()
	{
		return currentKey;
	}

	public void setCurrentKey(String currentKey)
	{
		this.currentKey = currentKey;
	}

	public void clearFragments()
	{
		PoliGdzieMapFragment temp = fragments.getFragmentByTag(OUTDOOR_MAP_TAG);
		fragments.clear();
		keys.clear();
		keys.add(OUTDOOR_MAP_TAG);
		fragments.addFragment(OUTDOOR_MAP_TAG, temp);
	}

}
