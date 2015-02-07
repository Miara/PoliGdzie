package com.poligdzie.helpers;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public  class GPSHelper extends Service implements LocationListener
{

	private Context context;
	
    boolean isGPSEnabled = false;
 
    boolean isNetworkEnabled = false;
 
    boolean canGetLocation = false;
 
    Location location; 
    double latitude; 
    double longitude; 
 
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
 
    protected LocationManager locationManager;
	
	public GPSHelper(Context ctx)
	{
		this.context = ctx;
		this.getLocation();
	}
	
	
	public Location getLocation() 
	{
        try 
        {
        	String providerType = null;
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
 
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            
            if(isGPSEnabled)
            {
            	providerType = LocationManager.GPS_PROVIDER;
            	this.canGetLocation = true;
            }
            
            if(isNetworkEnabled)
            {
            	providerType = LocationManager.NETWORK_PROVIDER;
            }
 
            if (isGPSEnabled || isNetworkEnabled) 
            {
            	
            	if (location == null) 
            	{
                    locationManager.requestLocationUpdates(providerType,
                            MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) 
                    {
                        location = locationManager
                                .getLastKnownLocation(providerType);
                        if (location != null) 
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
 
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
 
        return location;
    }
	
	public double getLatitude()
	{
	        if(location != null)
	        {
	            latitude = location.getLatitude();
	        }
	         
	        return latitude;
	 }
	     
	 public double getLongitude()
	 {
	        if(location != null)
	        {
	            longitude = location.getLongitude();
	        }
	         
	        return longitude;
	 }
	
	 public boolean canGetLocation() 
	 {
		 return this.canGetLocation;
	 }
	 
	 public void setCanGetLocation(boolean value)
	 {
		 this.canGetLocation = value;
	 }
	     
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
      
        alertDialog.setTitle("Ustawienia GPS");
  
        alertDialog.setMessage("GPS jest wy³¹czony. Czy chcesz go w³¹czyæ w ustawieniach?");
  
        alertDialog.setPositiveButton("Ustawienia", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog,int which) 
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
  
        alertDialog.setNegativeButton("Anuluj", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) 
            {
            	dialog.cancel();
            }
        });
  
        alertDialog.show();
    }
	    
	    
	@Override
	public void onLocationChanged(Location location){}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){}
	@Override
	public void onProviderEnabled(String provider){}
	@Override
	public void onProviderDisabled(String provider){}
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
