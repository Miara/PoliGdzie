package com.poligdzie.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.example.poligdzie.R;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.widgets.BuildingImageView;

public class MapIndoorFragment extends PoliGdzieMapFragment implements OnClickListener,
Constants {
	
	private BuildingImageView buildingImage;
	private int floorId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) 
	{
		super.onCreateView(inflater, container, savedInstanceState);
		buildingImage = new BuildingImageView(getActivity());
		
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 2;
		options.inDither=false;                     //Disable Dithering mode
		options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.cw_test_parter, options);
		
		buildingImage.setBitmap(bmp);
			
	return buildingImage;
	}
	
	@Override
	public void onClick(View v) 
	{
	
	}

	public MapIndoorFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getFloorId() {
		return floorId;
	}

	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}

	public MapIndoorFragment(int drawableId, String name) {
		super(drawableId, name);
		// TODO Auto-generated constructor stub
	}
	
}