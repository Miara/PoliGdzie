package com.poligdzie.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poligdzie.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.base.PoliGdzieBaseActivity;
import com.poligdzie.base.PoliGdzieMapFragment;
import com.poligdzie.fragments.BuildingInfoFragment;
import com.poligdzie.fragments.MapIndoorFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.fragments.RouteDetailsFragment;
import com.poligdzie.fragments.SearchDetailsFragment;
import com.poligdzie.fragments.SearchPlaceFragment;
import com.poligdzie.fragments.SearchRouteFragment;
import com.poligdzie.helpers.GPSHelper;
import com.poligdzie.singletons.DataProvider;
import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.tasks.DbVersionDownload;

public class MapActivity extends PoliGdzieBaseActivity implements
														OnClickListener
{

	public PolylineOptions		options;

	MapFragmentProvider			mapProvider;

	private TextView			currentText;
	private Button				previous;
	private Button				next;

	private MapOutdoorFragment	outdoorMap;
	private MapIndoorFragment	indoorMap;
	
	private SearchPlaceFragment	searchPlaceFragment;
	private SearchRouteFragment	searchRouteFragment;
	private SearchDetailsFragment	searchDetailsFragment;
	private RouteDetailsFragment	routeDetailsFragment;
	private BuildingInfoFragment	buildingInfoFragment;
	// TODO: przy pierwszym odpaleniu apki po instalacji nie ma danych przy wyszukiwaniu, przy kolejnych sas
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.onResume();
		setContentView(R.layout.map_activity);


		if(!isNetworkAvailable()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Brak po³¹czenia z internetem");
			builder.setMessage("Twoje urz¹dzenie nie jest pod³¹czone do internetu. Wyœwietlanie oraz wyszukiwanie trasy na zewn¹trz nie bêdzie dzia³aæ. Modu³y pomieszczeñ pracuj¹ normalnie.");
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			{
				

			};

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.cancel();
					
				}
			});
			builder.show();
	/*		Toast t = Toast.makeText(this, "Brak polaczenie z internetosem!", Toast.LENGTH_LONG);
			t.show();*/
		}
		
				
		Log.i("poli", "map1");

		mapProvider = MapFragmentProvider.getInstance();

		DataProvider dataProvider = DataProvider.getInstance();
		dataProvider.initialize(this, dbHelper);

		DbVersionDownload dbVerTask = new DbVersionDownload(this);
		dbVerTask.execute("http://192.168.0.100:8181/version/");
		
		outdoorMap = new MapOutdoorFragment(NO_BITMAP, "Mapa zewnêtrzna",
				OUTDOOR_MAP_TAG);


		switchFragment(R.id.map_container, outdoorMap, outdoorMap.getViewTag());


		previous = (Button) findViewById(R.id.previous_map);
		previous.setOnClickListener(this);
		previous.setVisibility(View.GONE);

		mapProvider = MapFragmentProvider.getInstance();

		next = (Button) findViewById(R.id.next_map);
		next.setOnClickListener(this);

		if (mapProvider.getFragmentsSize() == 1)
			next.setVisibility(View.GONE);

		currentText = (TextView) findViewById(R.id.current_map);

		currentText.setText(mapProvider.getCurrentFragment().getName());
		
		searchPlaceFragment =  (SearchPlaceFragment) getFragmentManager().
				findFragmentById(R.id.search_place_frag);
		searchPlaceFragment.getView().setVisibility(View.VISIBLE);
		
		searchRouteFragment = (SearchRouteFragment) getFragmentManager().
				findFragmentById(R.id.search_route_frag);
		searchRouteFragment.getView().setVisibility(View.GONE);
	
		
		searchDetailsFragment = (SearchDetailsFragment) getFragmentManager().
		findFragmentById(R.id.search_description_frag);
		searchDetailsFragment.getView().setVisibility(View.GONE);
		
		routeDetailsFragment = (RouteDetailsFragment) getFragmentManager().
		findFragmentById(R.id.route_details_frag);
		routeDetailsFragment.getView().setVisibility(View.GONE);
		
		buildingInfoFragment = (BuildingInfoFragment) getFragmentManager().
				findFragmentById(R.id.building_info_frag);
				buildingInfoFragment.getView().setVisibility(View.GONE);
	}
//TODO: refaktoryzacja w chuj!
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if(!checkPlayServices()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Brak google play services");
			builder.setMessage("Aby u¿ywaæ aplikacji nale¿y mieæ zainstalowane google play services oraz byæ zalogowanym poprzez konto google");
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			{
				

			};

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
				 MapActivity.this.finish();
					
				}
			});
			builder.show();

		}

	}

	@Override
	public void onClick(View v)
	{

		PoliGdzieMapFragment frag = null;
		String tag = "";
		if (v == next)
		{
			frag = mapProvider.getNextFragment();
			tag = mapProvider.getNextKey();

			switchFragment(R.id.map_container, frag, tag);
			searchDetailsFragment.getView().setVisibility(View.GONE);
		}
		if (v == previous)
		{
			frag = mapProvider.getPreviousFragment();
			tag = mapProvider.getPreviousKey();

			switchFragment(R.id.map_container, frag, tag);
			searchDetailsFragment.getView().setVisibility(View.GONE);
		}

		setNavigationArrowsVisibility();

	}

	public void setNavigationArrowsVisibility()
	{
		if (mapProvider.getCurrentFragmentKeyPosition() >= (mapProvider
				.getFragmentsSize() - 1))
			next.setVisibility(View.GONE);
		else
			next.setVisibility(View.VISIBLE);

		if (mapProvider.getCurrentFragmentKeyPosition() <= 0)
			previous.setVisibility(View.GONE);
		else
			previous.setVisibility(View.VISIBLE);

		String actualViewName = mapProvider.getCurrentFragment().getName();
		currentText.setText(actualViewName);
		Toast.makeText(this, actualViewName, Toast.LENGTH_SHORT).show();
	}

	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public MapActivity()
	{
		super();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	MapFragmentProvider fragProvider = MapFragmentProvider.getInstance();
	        if(searchRouteFragment.isVisible())
	        {
	        	searchRouteFragment.getView().setVisibility(View.GONE);
	        	return true;
	        }
	        else if(buildingInfoFragment.isVisible())
	        {
	        	buildingInfoFragment.getView().setVisibility(View.GONE);
	        	return true;
	        }
	        else if(routeDetailsFragment.isVisible())
	        {
	        	routeDetailsFragment.getView().setVisibility(View.GONE);
	        	return true;
	        }
	        else if(searchDetailsFragment.isVisible())
	        {
	        	searchDetailsFragment.getView().setVisibility(View.GONE);
	        	return true;
	        }
	        else if(!fragProvider.isGoogleMapInCurrentView())
	        {
	        	fragProvider.clearFragments();
	        	fragProvider.addGoogleMapFragment();
	        	this.switchFragment(R.id.map_container, 
	        			fragProvider.getGoogleMapFragment(), 
	        			fragProvider.getGoogleMapFragment().getViewTag());
	        	this.onClick(findViewById(R.layout.map_activity));
	        	return true;
	        }
	        else
	        {
	        	this.finish();
	        }
	        
	    }
	    return super.onKeyDown(keyCode, event);   
	}
	
	
	//TODO: refaktor
	private boolean checkPlayServices() {
		  int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		  if (status != ConnectionResult.SUCCESS) {
		    if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
		      showErrorDialog(status);
		    } else {
		      Toast.makeText(this, "This device is not supported.", 
		          Toast.LENGTH_LONG).show();
		      finish();
		    }
		    return false;
		  }
		  return true;
		} 

		void showErrorDialog(int code) {
		  GooglePlayServicesUtil.getErrorDialog(code, this, 
		      1001).show();
		}

		public SearchPlaceFragment getSearchPlaceFragment()
		{
			return searchPlaceFragment;
		}

		public SearchRouteFragment getSearchRouteFragment()
		{
			return searchRouteFragment;
		}

		public SearchDetailsFragment getSearchDetailsFragment()
		{
			return searchDetailsFragment;
		}

		public RouteDetailsFragment getRouteDetailsFragment()
		{
			return routeDetailsFragment;
		}

		public BuildingInfoFragment getBuildingInfoFragment()
		{
			return buildingInfoFragment;
		}

		public void setBuildingInfoFragment(BuildingInfoFragment buildingInfoFragment)
		{
			this.buildingInfoFragment = buildingInfoFragment;
		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu)
		{
			super.onCreateOptionsMenu(menu);
			MenuInflater customMenu = getMenuInflater();
			customMenu.inflate(R.menu.main, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem element)
		{
			if(element.getItemId() == R.id.menu_pomoc)
			{
				Intent intent = new Intent(this, HelpActivity.class);
				this.startActivity(intent);
			}
			return true;
		}
		
		
		
		
}
