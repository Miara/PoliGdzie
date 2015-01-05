package com.poligdzie.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.poligdzie.base.PoliGdzieMapFragment;
import com.poligdzie.widgets.BuildingImageView;

public class MapIndoorFragment extends PoliGdzieMapFragment implements
															OnClickListener
{

	private BuildingImageView	buildingImage;
	private int					floorId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		buildingImage = new BuildingImageView(getActivity());

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		options.inDither = false; // Disable Dithering mode
		options.inPurgeable = true; // Tell to gc that whether it needs free
									// memory, the Bitmap can be cleared

		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				this.getDrawableId(this.drawableId, this.getActivity()),
				options);

		buildingImage.setBitmap(bmp);

		return buildingImage;
	}

	@Override
	public void onClick(View v)
	{

	}

	public MapIndoorFragment()
	{
		super();
	}

	public int getFloorId()
	{
		return floorId;
	}

	public void setFloorId(int floorId)
	{
		this.floorId = floorId;
	}

	public MapIndoorFragment(String drawableId, String name, String viewTag,
			int floorId)
	{
		super(drawableId, name, viewTag);
		this.floorId = floorId;
	}

}