package com.poligdzie.tasks;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.poligdzie.fragments.MapIndoorFragment;
import com.poligdzie.widgets.BuildingImageView;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap>
{
	private final WeakReference<BuildingImageView>	buildingViewReference;
	private int										data	= 0;
	private Resources								resources;
	private MapIndoorFragment						fragment;

	public BitmapWorkerTask(MapIndoorFragment frag, Context context,
			Resources res)
	{
		// Use a WeakReference to ensure the ImageView can be garbage collected
		BuildingImageView building = new BuildingImageView(context);
		buildingViewReference = new WeakReference<BuildingImageView>(building);
		resources = res;
		fragment = frag;
	}

	// Decode image in background.
	@Override
	protected Bitmap doInBackground(Integer... params)
	{
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		data = params[0];
		boolean done=false;
		int inSampleSize = 1;
		Bitmap bitmap = null;
		while(!done)
		{
			try
			{
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = inSampleSize;
				options.inDither = false;
				options.inPurgeable = true;
				Log.i("BitmapWorker", "1st step");
				bitmap = BitmapFactory.decodeResource(resources, data, options);
				Log.i("BitmapWorker", "2nd step");
				done = true;
			}
			catch(OutOfMemoryError e)
			{
				Log.e("ERROR","Out of memory exception");
				inSampleSize *= 2;
			}
		}
		return bitmap;
	}

	// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Bitmap bitmap)
	{
		if (bitmap != null)
		{
			fragment.setFloorImage(bitmap);
		}
	}
}