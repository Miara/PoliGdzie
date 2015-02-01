package com.poligdzie.fragments;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.poligdzie.base.PoliGdzieBaseFragment;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.listeners.FragmentSwitchListener;
import com.poligdzie.listeners.SearchButtonListener;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchPlaceFragment extends PoliGdzieBaseFragment implements OnClickListener
{

	private ImageButton						searchButton;
	private ContextSearchTextWatcher	searchWatcher;
	private ImageButton						switchFragmentButton;
	private SearchAutoCompleteTextView	searchPosition;
	

	private SearchRouteFragment			routeFragment;
	private ImageButton					deleteTextButton;
	private SearchDetailsFragment	searchDescriptionFragment;
	private GoogleMap					map;
	private MapOutdoorFragment outdoorMap;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.search_place_fragment,
				container, false);
		
		searchPosition = (SearchAutoCompleteTextView) rootView
				.findViewById(R.id.search_point_text_edit);
		searchWatcher = new ContextSearchTextWatcher(searchPosition,
				this.getActivity());
		searchPosition.addTextChangedListener(searchWatcher);
		searchPosition.setOnItemClickListener(new SearchButtonListener(
					searchPosition, map, outdoorMap, this));

		switchFragmentButton = (ImageButton) rootView
				.findViewById(R.id.button_search_switch_fragment);
		switchFragmentButton.setOnClickListener(this);

		
		deleteTextButton = (ImageButton) rootView.findViewById(R.id.search_place_delete_text);
		deleteTextButton.setOnClickListener(this);

		return rootView;
	}

	public SearchPlaceFragment()
	{
		super();
	}
	
	private void initFragments() throws Exception
	{
		routeFragment = (SearchRouteFragment) this.getActivity()
				.getFragmentManager().findFragmentById(
						R.id.search_route_frag);
		searchDescriptionFragment = (SearchDetailsFragment)  this.getActivity()
				.getFragmentManager().findFragmentById(
						R.id.search_description_frag);
		if(routeFragment == null || searchDescriptionFragment == null) throw new Exception();
	}

	@Override
	public void onClick(View v)
	{
		try
		{
			if(routeFragment == null || searchDescriptionFragment == null)
			{
				initFragments();
			}
			
			if(v == deleteTextButton)
			{
				resetInput();
			}
			else if( v == switchFragmentButton)
			{
				routeFragment.getView().setVisibility(View.VISIBLE);
			}
		}
		catch(Exception e)
		{
			Log.e("poligdzie","Nie udalo sie obsluzyc klikniecia!");
		}
	}
	
	public void resetInput()
	{
		this.searchPosition.setText("");
		searchDescriptionFragment.getView().setVisibility(View.GONE);
		if (searchPosition.hasFocus())
		{
			InputMethodManager imm = (InputMethodManager) this.getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			searchPosition.clearFocus();
			imm.hideSoftInputFromWindow(searchPosition.getWindowToken(), 0);
		}
	}

	public void setFragment(MapOutdoorFragment mapOutdoorFragment)
	{
		this.outdoorMap = mapOutdoorFragment;
		
	}

	public SearchDetailsFragment getSearchDescriptionFragment() 
	{
		if(searchDescriptionFragment == null)
		{
			try
			{
				initFragments();
			} catch (Exception e)
			{
				return null;
			}
		}
		return searchDescriptionFragment;
	}
	
	public SearchAutoCompleteTextView getSearchPosition()
	{
		return searchPosition;
	}
	
}
