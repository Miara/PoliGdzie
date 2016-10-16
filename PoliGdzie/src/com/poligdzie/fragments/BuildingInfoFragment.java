package com.poligdzie.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.base.PoliGdzieMapFragment;
import com.poligdzie.interfaces.LoadingBitmap;
import com.poligdzie.persistence.Building;
import com.poligdzie.tasks.BitmapWorkerTask;
import com.poligdzie.widgets.BuildingImageView;

public class BuildingInfoFragment extends PoliGdzieMapFragment implements
OnClickListener,LoadingBitmap
{
	private TextView name;
	private ImageView image;
	private TextView description;
	private ImageButton closeButton;
	private Building building;
	
	
	private ScrollView scrollView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.building_info_fragment,
				container, false);

		name = (TextView)rootView.findViewById(R.id.building_info_building_name);
		description = (TextView)rootView.findViewById(R.id.building_info_building_description);
		scrollView = (ScrollView)rootView.findViewById(R.id.building_info_scroll_view);
		
		closeButton = (ImageButton)rootView.findViewById(R.id.building_info_close_button);
		closeButton.setOnClickListener(this);
		

		return rootView;
	}
	
	@Override
	public void bitmapLoaded(Bitmap bitmap)
	{
		ViewGroup container = (ViewGroup) getView().findViewById(
				R.id.building_info_photo_container);
		container.removeAllViews();
		View inflater = LayoutInflater.from(getActivity()).inflate(
				R.layout.building_info_building_photo_layout, container);
		
		image = (ImageView) inflater
				.findViewById(R.id.building_info_building_photo_layout_image);
		
		float ratio = bitmap.getHeight()/bitmap.getWidth();
		
		scrollView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int parentWidth = scrollView.getMeasuredWidth();
		
		Log.i("poligdzie","bitmapWidth:"+bitmap.getWidth());
		Log.i("poligdzie","scrollViewWidth:"+parentWidth);
		
//		if(bitmap.getWidth() > parentWidth)
//		{
//			LayoutParams params=image.getLayoutParams();
//			params.width=parentWidth;
//			params.height = (int)(parentWidth*ratio);
//			image.setLayoutParams(params);
//			
//			Log.i("poligdzie","paramsWidth:"+params.width);
//			Log.i("poligdzie","paramsHeight:"+params.height);
//		}
//		else
//		{
//			LayoutParams params=scrollView.getLayoutParams();
//			params.width=bitmap.getWidth();
//			scrollView.setLayoutParams(params);
//		}
		
		
		image.setImageBitmap(bitmap);
		this.getView().setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v)
	{
		if(v == closeButton)
		{
			this.getView().setVisibility(View.GONE);
		}
		
	}

	public void setViews(Building build)
	{
		if(build != this.building)
		{
			this.building = build;
			name.setText(building.getName());
			description.setText(building.getDescriptionOfBuilding());
			
			drawableId = building.getImageOfBuilding();
			BitmapWorkerTask bitmapWorker = new BitmapWorkerTask(this,
				getActivity(), getActivity().getResources());
			bitmapWorker.execute(getDrawableId(drawableId, getActivity()));
		}
		else
		{
			this.getView().setVisibility(View.VISIBLE);
		}
		
		
	}
	
	public BuildingInfoFragment()
	{
		super();
	}

}
