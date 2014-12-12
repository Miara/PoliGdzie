package com.poligdzie.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.interfaces.Constants;

public class BuildingInfoActivity extends PoliGdzieBaseActivity implements OnClickListener,
Constants {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.building_info_activity);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
