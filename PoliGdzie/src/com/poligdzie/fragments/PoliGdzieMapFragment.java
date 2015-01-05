package com.poligdzie.fragments;

import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.singletons.RouteProvider;

public class PoliGdzieMapFragment extends PoliGdzieBaseFragment
{
	// TODO: zmienic na string
	protected int		drawableId;
	protected String	viewTag;

	public PoliGdzieMapFragment(int drawableId, String name, String viewTag)
	{
		this();
		this.drawableId = drawableId;
		this.name = name;
		this.viewTag = viewTag;
		mapProvider = MapFragmentProvider.getInstance();
		mapProvider.addFragment(viewTag, this);
	}

	public PoliGdzieMapFragment()
	{
		super();
	}

	public String getViewTag()
	{
		return viewTag;
	}

}
