package com.poligdzie.activities;

import com.example.poligdzie.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.json.DownloadDirectionsTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MapActivity extends Activity implements OnMarkerClickListener {

	public GoogleMap map;
	public PolylineOptions options;
	
	//Przyk³adowe punkty
	static final LatLng LOCATION_PIOTROWO = new LatLng(52.4022703,16.9495847);
	private static final LatLng MY_POINT = new LatLng(52.4014141,16.9511311);
	private static final LatLng CENTRUM_WYKLADOWE = new LatLng(52.4037379,16.9498138);
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        
        map.setInfoWindowAdapter(new InfoWindowAdapter() {

	        // Use default InfoWindow frame
	        @Override
	        public View getInfoWindow(Marker arg0) 
	        {
	            return null;
	        }
	        @Override
	        public View getInfoContents(Marker arg0) 
	        {
	            View v = getLayoutInflater().inflate(R.layout.window_marker_click, null);
	            return v;

	        }
	    });
        
        map.setMyLocationEnabled(true);
        map.addMarker(new MarkerOptions().position(LOCATION_PIOTROWO).title("Tutaj jest kampus Piotrowo!"));
        
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(MY_POINT);
        markerOptions.position(CENTRUM_WYKLADOWE)
	        .title("Centrum Wyk³adowe")
	        .snippet("ul. Piotrowo 3")
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cw_icon));
        
		map.addMarker(markerOptions);
		
		

		DownloadDirectionsTask downloadTask = new DownloadDirectionsTask(map,options);
    	downloadTask.execute(getMapsApiDirectionsUrl());
    	
    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_PIOTROWO,16));
		addMarkers();

    }
    
    public void onClick_Piotrowo(View v) {
    	map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_PIOTROWO, 16);
		map.animateCamera(update);
		
	}

        
	
	

    private String getMapsApiDirectionsUrl() {
    	String waypoints = "waypoints=optimize:true|"
		+ MY_POINT.latitude + "," + MY_POINT.longitude
		+ "|" + "|" + CENTRUM_WYKLADOWE.latitude + ","
		+ CENTRUM_WYKLADOWE.longitude;
    	
		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}

    
	private void addMarkers() {
		if (map != null) {
			map.addMarker(new MarkerOptions().position(MY_POINT)
					.title("First Point"));
			map.addMarker(new MarkerOptions().position(CENTRUM_WYKLADOWE)
					.title("Second Point"));
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		arg0.showInfoWindow();
		return false;
	}

}
