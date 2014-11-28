package com.poligdzie.json;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolylineOptions;

public class DownloadDirectionsTask extends AsyncTask<String, Void, String> {

	public GoogleMap map;
	public PolylineOptions options;

	public DownloadDirectionsTask(GoogleMap map, PolylineOptions options) {
		this.map = map;
		this.options = options;
	}

	@Override
	protected String doInBackground(String... url) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {

			Directions directions = mapper.readValue(new URL(url[0]),
					Directions.class);

			options = new PolylineOptions();
			options = directions.generatePolylineFromDirections(options);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return "Not sure what should I return";
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (options == null)
			options = new PolylineOptions();
		options.color(Color.RED);
		options.width(10);
		map.addPolyline(options);
	}
}
