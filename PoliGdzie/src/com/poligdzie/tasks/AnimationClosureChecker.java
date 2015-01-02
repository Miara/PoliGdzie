package com.poligdzie.tasks;

import android.app.FragmentTransaction;
import android.graphics.Point;
import android.os.AsyncTask;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.Marker;
import com.poligdzie.callbacks.MarkerAnimationFinishCallback;
import com.poligdzie.fragments.BuildingInfoFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.persistence.DatabaseHelper;

public class AnimationClosureChecker extends AsyncTask <String, Void, String> {

	private MarkerAnimationFinishCallback callback;
	private GoogleMap map;
	private Marker marker;
	private MapOutdoorFragment mapOutdoorFragment;
	private DatabaseHelper dbHelper;
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		while(!callback.isFinished());
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub

		Projection projection = map.getProjection();
		Point point = projection.toScreenLocation(marker.getPosition());
		

		
		BuildingInfoFragment buildingInfoFragment = new BuildingInfoFragment(point.x, point.y, marker, dbHelper);

		FragmentTransaction transaction = mapOutdoorFragment
				.getFragmentManager().beginTransaction();

		transaction.add(R.id.window_info_container, buildingInfoFragment,
				marker.getId());

		transaction.commit();
	}

	public AnimationClosureChecker(MarkerAnimationFinishCallback callback,
			GoogleMap map, Marker marker, MapOutdoorFragment mapOutdoorFragment, DatabaseHelper dbHelper) {
		this.callback = callback;
		this.map = map;
		this.marker = marker;
		this.mapOutdoorFragment = mapOutdoorFragment;
		this.dbHelper = dbHelper;
	}
	

}