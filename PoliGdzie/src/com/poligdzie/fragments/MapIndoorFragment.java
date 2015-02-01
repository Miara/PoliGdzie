package com.poligdzie.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.poligdzie.R;
import com.poligdzie.base.PoliGdzieMapFragment;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.NavigationPointTypes;
import com.poligdzie.route.Line;
import com.poligdzie.tasks.BitmapWorkerTask;
import com.poligdzie.widgets.BuildingImageView;

public class MapIndoorFragment extends PoliGdzieMapFragment implements
															OnClickListener
{

	private BuildingImageView	buildingImage;
	private int					floorId;
	private List<Line>			routeLines	= new ArrayList<Line>();
	private NavigationPointTypes	startPointType = null;
	private NavigationPointTypes	goalPointType = null;
	
	private int searchX;
	private int searchY;
	private boolean routeMode = false;
	private int	radius;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.map_indoor_fragment,
				container, false);

		BitmapWorkerTask bitmapWorker = new BitmapWorkerTask(this,
				getActivity(), getActivity().getResources());
		bitmapWorker.execute(getDrawableId(drawableId, getActivity()));

		return rootView;
	}

	@Override
	public void onClick(View v)
	{

	}

	public void setFloorImage(Bitmap bitmap)
	{
		ViewGroup container = (ViewGroup) getView().findViewById(
				R.id.indoor_map_container);
		container.removeAllViews();
		View inflater = LayoutInflater.from(getActivity()).inflate(
				R.layout.map_indoor_floor_scheme, container);

		Bitmap bmp = null;
		Resources res = getActivity().getResources();
		
		buildingImage = (BuildingImageView) inflater
				.findViewById(R.id.imageview_floor_scheme);
		buildingImage.setBitmap(bitmap);
		echo("TEST8");
		try
		{
			Floor floor = dbHelper.getFloorDao().queryForId(floorId);
			int radius = ROUTE_SCALE_RADIUS * floor.getPixelsPerMeter();
			buildingImage.setOriginalWidth(floor.getWidth());
			buildingImage.setOriginalHeight(floor.getHeight());
			if(!routeMode)
			{
				echo("TEST9");
				buildingImage.setSearchCustomPoint(this.searchX,this.searchY,radius);
			}
			else
			{
				echo("TEST10");
				
					
				if (routeLines.size() > 0)
				{
					buildingImage.setLines(routeLines);
					if(startPointType != null && goalPointType != null)
					{
						if(startPointType == NavigationPointTypes.ENTRY)
							bmp = BitmapFactory.decodeResource(getResources(), R.drawable.entry_icon);
						else if(startPointType == NavigationPointTypes.SPECIAL)
							bmp = BitmapFactory.decodeResource(getResources(), R.drawable.stairs_icon);
						else
							bmp = BitmapFactory.decodeResource(getResources(), R.drawable.from_icon);
						if(bmp != null) buildingImage.setStartCustomPoint(bmp,radius);
						
						if(goalPointType == NavigationPointTypes.ENTRY)
							bmp = BitmapFactory.decodeResource(getResources(), R.drawable.entry_icon);
						else if(goalPointType == NavigationPointTypes.SPECIAL)
							bmp = BitmapFactory.decodeResource(getResources(), R.drawable.stairs_icon);
						else
							bmp = BitmapFactory.decodeResource(getResources(), R.drawable.to_icon);
						if(bmp != null)	buildingImage.setGoalCustomPoint(bmp);
					}
				}
				
			}
		} catch (SQLException e)
		{
			Log.e("MAP INDOOR FRAG","Error creating view");
			e.printStackTrace();
		}
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
	
	public MapIndoorFragment(String drawableId, String name, String viewTag,
			int floorId, int searchX, int searchY,int radius)
	{
		super(drawableId, name, viewTag);
		echo("TEST5");
		this.floorId = floorId;
		
		echo("TEST6");
		this.searchX= (int) searchX;
		this.searchY= (int) searchY;
		this.radius = radius;
		this.routeMode = false;
		
	}

	public MapIndoorFragment(String drawableId, String name, String viewTag,
			int floorId, List<NavigationPoint> points)
	{
		super(drawableId, name, viewTag);
		this.floorId = floorId;
		Log.i("poligdzie", "floor:" + floorId);
		
		if (points.size() > 1)
		{
			boolean first = true;
			NavigationPoint previous = null;
			for (NavigationPoint current : points)
			{
				Log.i("poligdzie", "current:" + current.getId());
				if (first)
				{
					this.startPointType = current.getType();

					previous = current;
					first = false;
				} else
				{
					routeLines.add(new Line(previous.getCoordX(), previous
							.getCoordY(), current.getCoordX(), current
							.getCoordY()));
					previous = current;
				}

			}
			if(previous != null)
			{
				this.goalPointType = previous.getType();
			}
			
		}
		this.routeMode = true;
	}
	
	private void echo(String s)
	{
		Log.i("poligdzie",s);
	}

}