package com.poligdzie.callbacks;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap.CancelableCallback;

public class MarkerAnimationFinishCallback implements CancelableCallback
{

	private boolean	finished	= false;

	@Override
	public void onCancel()
	{

	}

	@Override
	public void onFinish()
	{
		finished = true;
	}

	public boolean isFinished()
	{
		return finished;
	}

}
