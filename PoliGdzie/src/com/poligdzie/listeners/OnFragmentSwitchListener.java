package com.poligdzie.listeners;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.poligdzie.R;
import com.poligdzie.fragments.SearchPlaceFragment;
import com.poligdzie.fragments.SearchRouteFragment;
//TODO: czy ta klasa jest uzywana?
public class OnFragmentSwitchListener implements OnClickListener
{

	private SearchPlaceFragment	searchPlaceFragment;
	private SearchRouteFragment	searchRouteFragment;
	private Activity			activity;

	@Override
	public void onClick(View v)
	{
		if (searchPlaceFragment == null)
		{
			searchPlaceFragment = (SearchPlaceFragment) activity
					.getFragmentManager().findFragmentById(
							R.id.search_place_frag);
		}

		if (searchRouteFragment == null)
		{
			searchRouteFragment = (SearchRouteFragment) activity
					.getFragmentManager().findFragmentById(
							R.id.search_route_frag);
		}

		if (searchPlaceFragment.isVisible() && !searchRouteFragment.isVisible())
		{
			searchPlaceFragment.getView().setVisibility(View.GONE);
			searchRouteFragment.getView().setVisibility(View.VISIBLE);
		} else if (searchRouteFragment.isVisible()
				&& !searchPlaceFragment.isVisible())
		{
			searchRouteFragment.getView().setVisibility(View.GONE);
			searchPlaceFragment.getView().setVisibility(View.VISIBLE);
		}

	}

	public OnFragmentSwitchListener(Activity activity)
	{
		this.activity = activity;
	}

}
