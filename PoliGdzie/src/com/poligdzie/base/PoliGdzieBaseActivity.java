package com.poligdzie.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.WithDrawableId;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.singletons.MapFragmentProvider;

public abstract class PoliGdzieBaseActivity extends FragmentActivity implements
																	Constants,
																	WithDrawableId
{

	protected DatabaseHelper		dbHelper;
	protected String				lastTag;
	protected MapFragmentProvider	mapProvider;
	protected MapDrawingProvider	drawingProvider;

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

	public void clearFragments()
	{
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
		
	}

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		this.dbHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null,
				DatabaseHelper.DATABASE_VERSION);
	}

	@Override
	public int getDrawableId(String name, Context context)
	{
		int resId = context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
		return resId;
	}
}
