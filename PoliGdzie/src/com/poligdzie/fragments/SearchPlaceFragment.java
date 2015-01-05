package com.poligdzie.fragments;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.poligdzie.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.poligdzie.callbacks.MarkerAnimationFinishCallback;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.WithCoordinates;
import com.poligdzie.listeners.ContextSearchTextWatcher;
import com.poligdzie.singletons.RouteProvider;
import com.poligdzie.tasks.AnimationClosureChecker;
import com.poligdzie.widgets.SearchAutoCompleteTextView;

public class SearchPlaceFragment extends PoliGdzieBaseFragment
																implements
																OnClickListener,
																Constants
{

	private Button						searchButton;
	private ContextSearchTextWatcher	searchWatcher;
	private SearchAutoCompleteTextView	searchPosition;
	private GoogleMap					map;
	private MapOutdoorFragment			outdoorMap;

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

		searchButton = (Button) rootView.findViewById(R.id.button_search_place);

		if (searchButton != null)
			searchButton.setOnClickListener(this);

		return rootView;
	}

	// TODO: sprawdzic editora

	@Override
	public void onClick(View v)
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		Editor editor = prefs.edit();
		if (v == searchButton)
		{
			map = ((MapFragment) this.getActivity().getFragmentManager()
					.findFragmentById(R.id.map_outdoor_googleMap)).getMap();

			searchPosition.clearFocus();
			InputMethodManager imm = (InputMethodManager) this.getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchPosition.getWindowToken(), 0);

			Object object = searchPosition.getAdapter().getItem(0);
			if (object instanceof WithCoordinates)
			{
				LatLng pos = new LatLng(((WithCoordinates) object).getCoordX(),
						((WithCoordinates) object).getCoordY());

				RouteProvider provider = RouteProvider.getInstance();
				List<Marker> markers = provider.getMarkers();

				for (Marker m : markers)
				{
					if (m.getPosition().latitude == pos.latitude
							&& m.getPosition().longitude == pos.longitude)
					{
						MarkerAnimationFinishCallback callback = new MarkerAnimationFinishCallback();
						map.animateCamera(
								CameraUpdateFactory.newLatLngZoom(pos, 17),
								callback);

						AnimationClosureChecker checker = new AnimationClosureChecker(
								callback, map, m, this.outdoorMap,
								new DatabaseHelper(this.getActivity(),
										DATABASE_NAME, null, DATABASE_VERSION));
						checker.execute();
						break;
					}

				}

			}
		}

		editor.commit();
	}

	public void setFragment(MapOutdoorFragment mapOutdoorFragment)
	{
		this.outdoorMap = mapOutdoorFragment;
	}

	public SearchPlaceFragment()
	{
		super();
	}
}
