package com.poligdzie.fragments;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
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

public class MapIndoorFragment extends Fragment implements OnClickListener,
Constants {
	
	private BuildingImageView buildingImage;
	private ActivityManager am;
	
	public MapIndoorFragment(ActivityManager m)
	{
		super();
		am=m;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) 
	{
		buildingImage = new BuildingImageView(getActivity());
		Log.i("Poligdzie","test");
		BitmapFactory.Options o=new BitmapFactory.Options();
		o.inSampleSize = 2;
		o.inDither=false;                     //Disable Dithering mode
		o.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
		
		Log.i("Poligdzie","Memory before(M):"+am.getMemoryClass());
		Log.i("Poligdzie","Memory before(L):"+am.getLargeMemoryClass());
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.cw_test_parter, o);
		
		Log.i("Poligdzie","Memory after(M):"+am.getMemoryClass());
		Log.i("Poligdzie","Memory after(L):"+am.getLargeMemoryClass());
		
		int imageHeight = o.outHeight;
		int imageWidth = o.outWidth;
		String imageType = o.outMimeType;
		Log.i("Poligdzie",imageHeight+"."+imageWidth+"."+imageType);
		//Bitmap bmp = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.cw_test_parter);
		Log.i("Poligdzie","test1");
		buildingImage.setBitmap(bmp);
		Log.i("Poligdzie","test2");
		//getActivity().setContentView(buildingImage);
		buildingImage.requestFocus();
		
		
		
		
		
		buildingImage.invalidate();
		
	//View rootView = inflater.inflate(R.layout.map_indoor_fragment,container, false);
	
	return buildingImage;
	}
	
	@Override
	public void onClick(View v) 
	{
	
	}
}