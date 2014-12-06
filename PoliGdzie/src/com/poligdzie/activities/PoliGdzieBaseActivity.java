package com.poligdzie.activities;

import android.app.Activity;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.DatabaseHelper;

public abstract class PoliGdzieBaseActivity extends Activity implements
		Constants {

	protected DatabaseHelper dbHelper;
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (dbHelper != null) {
			OpenHelperManager.releaseHelper();
			dbHelper = null;
		}

	}
}
