package com.poligdzie.tasks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.json.DbVersion;
import com.poligdzie.singletons.DataProvider;

public class DbVersionDownload extends AsyncTask<String, Void, DbVersion> implements Constants
{

	private Context context; 
	
	@Override
	protected DbVersion doInBackground(String... url)
	{

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DbVersion version = new DbVersion();
		try 
		{
			URL urlObject = new URL("url");
			HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();
			if(urlConnection.getResponseCode() == 200)
				version = mapper.readValue(new URL(url[0]), DbVersion.class);

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
			Log.e("POLIGDZIE", "Usluga niedostepna");
		}
		return version;
	}

	@Override
	protected void onPostExecute(DbVersion version)
	{
		DataProvider provider = DataProvider.getInstance();
		Log.d("POLIGDZIE", "Pobrano wartosc wersji: " + version.getValue());
		if(version.getValue() == 0)
			return;
		
		if(version.getValue() > provider.getRemoteDbVersion()) {
			Log.d("POLIGDZIE", "Zmiana wersji bazy na " + version.getValue() + " z " + provider.getRemoteDbVersion());
			provider.setRemoteDbVersion(version.getValue());
			DatabaseHelper dbHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, version.getValue());
			dbHelper.getReadableDatabase();
		}
	}

	public DbVersionDownload(Context context)
	{
		super();
		this.context = context;
	}
}
