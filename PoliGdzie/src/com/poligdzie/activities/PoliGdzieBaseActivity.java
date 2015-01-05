package com.poligdzie.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.singletons.RouteProvider;

public abstract class PoliGdzieBaseActivity extends FragmentActivity implements
																	Constants
{

	protected DatabaseHelper		dbHelper;
	protected String				lastTag;
	protected MapFragmentProvider	mapProvider;
	protected RouteProvider			routeProvider;

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (dbHelper != null)
		{
			OpenHelperManager.releaseHelper();
			dbHelper = null;
		}

	}

	public void switchFragment(int resource, Fragment fragment, String tag)
	{

		String oldTag = getLastTag();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		if (oldTag != null)
		{
			Fragment lastFragment = fragmentManager.findFragmentByTag(oldTag);
			if (lastFragment != null)
			{
				transaction.hide(lastFragment);
			}
		}

		if (fragment.isAdded())
		{
			transaction.show(fragment);
		} else
		{
			transaction.add(resource, fragment, tag);
		}
		mapProvider = MapFragmentProvider.getInstance();
		mapProvider.setCurrentKey(tag);

		transaction.commit();
		setLastTag(tag);
	}

	public String getLastTag()
	{
		return lastTag;
	}

	public void setLastTag(String lastTag)
	{
		this.lastTag = lastTag;
	}

	public PoliGdzieBaseActivity()
	{
		this.dbHelper = new DatabaseHelper(this, DATABASE_NAME, null,
				DATABASE_VERSION);
	}
}
