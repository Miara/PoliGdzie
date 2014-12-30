package com.poligdzie.callbacks;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap.CancelableCallback;

public class MarkerAnimationFinishCallback implements CancelableCallback{

	private boolean finished = false;
	
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		finished = true;
	}

	public boolean isFinished() {
		return finished;
	}

}
