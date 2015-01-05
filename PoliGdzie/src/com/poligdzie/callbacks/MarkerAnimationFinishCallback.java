package com.poligdzie.callbacks;

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
