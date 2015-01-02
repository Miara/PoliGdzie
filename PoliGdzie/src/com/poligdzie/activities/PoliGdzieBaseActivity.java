package com.poligdzie.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.DatabaseHelper;

public abstract class PoliGdzieBaseActivity extends Activity implements
		Constants {

	protected DatabaseHelper dbHelper;
	protected String lastTag;
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (dbHelper != null) {
			OpenHelperManager.releaseHelper();
			dbHelper = null;
		}

	}
	
	public void switchFragment(int resource, Fragment fragment, String tag) {

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
		} 
		else 
		{
			transaction.add(resource, fragment, tag);
		}

		transaction.commit();
		setLastTag(tag);
	}
	
	public String getLastTag() {
		return lastTag;
	}

	public void setLastTag(String lastTag) {
		this.lastTag = lastTag;
	}
	
	public void debugMsg(String msg)
	{
		Log.i("POLIGDZIE",msg);
	}

	public PoliGdzieBaseActivity() {
		this.dbHelper = new DatabaseHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
	}
}
