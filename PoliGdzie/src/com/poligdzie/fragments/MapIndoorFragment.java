package com.poligdzie.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.poligdzie.R;
import com.poligdzie.base.PoliGdzieMapFragment;
import com.poligdzie.tasks.BitmapWorkerTask;
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
		
		View rootView = inflater.inflate(R.layout.map_indoor_fragment,
				container, false);
		
		BitmapWorkerTask bitmapWorker = new BitmapWorkerTask(this,getActivity(),getActivity().getResources());
		bitmapWorker.execute(getDrawableId(drawableId, getActivity()));
		
		return rootView;
	}

	@Override
	public void onClick(View v)
	{

	}
	
	public void setFloorImage(Bitmap bitmap)
	{
		ViewGroup container = (ViewGroup) getView().findViewById(R.id.indoor_map_container);
		   container.removeAllViews();
		   View inflater = LayoutInflater.from(getActivity()).inflate(R.layout.map_indoor_floor_scheme, container);
		   buildingImage = (BuildingImageView) inflater.findViewById(R.id.imageview_floor_scheme);
		   buildingImage.setBitmap(bitmap);
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