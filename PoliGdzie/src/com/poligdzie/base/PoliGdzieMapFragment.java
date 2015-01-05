package com.poligdzie.base;

import com.poligdzie.singletons.MapFragmentProvider;

public class PoliGdzieMapFragment extends PoliGdzieBaseFragment
{

	protected String	drawableId;
	protected String	viewTag;

	public PoliGdzieMapFragment(String drawableId, String name, String viewTag)
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
