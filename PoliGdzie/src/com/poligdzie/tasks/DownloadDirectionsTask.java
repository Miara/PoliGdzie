package com.poligdzie.tasks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.json.Directions;

public class DownloadDirectionsTask extends AsyncTask<String, Void, PolylineOptions>
{

	private GoogleMap		map;
	

	public DownloadDirectionsTask(GoogleMap map)
	{
		this.map = map;
	}

	@Override
	protected PolylineOptions doInBackground(String... url)
	{

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		PolylineOptions	options = null; 
		try
		{
			
			Directions directions = mapper.readValue(new URL(url[0]),
					Directions.class);
			
			options = new PolylineOptions();
			options = directions.generatePolylineFromDirections(options);
			

		} catch (JsonParseException e)
		{
			e.printStackTrace();
		} catch (JsonMappingException e)
		{
			e.printStackTrace();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
			Log.e("POLIGDZIE", "NAPEWNO KURWA TEN WYJATEK?");
		}
		return options;
	}

	@Override
	protected void onPostExecute(PolylineOptions options)
	{
		super.onPostExecute(options);

		if(options == null) {
			Log.d("POLIGDZIE", "CHUUUUUJ");
		}
		if (options == null)
			options = new PolylineOptions();
		options.color(Color.RED);
		options.width(10);
		map.addPolyline(options);
	}
}
