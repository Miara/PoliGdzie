package com.poligdzie.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.singletons.MapDrawingProvider;

public class DatabaseDownloadTask extends AsyncTask<String, Void, String>
																			implements
																			Constants
{
	private String	path;
	private Context context;
	public DatabaseDownloadTask(String path)
	{
		super();
		this.path = path;
	}

	@Override
	protected String doInBackground(String... url)
	{
		File currentDb = new File(this.path);
		if (currentDb.exists())
			currentDb.delete();

		try
		{
			new DefaultHttpClient().execute(new HttpGet(url[0])).getEntity()
					.writeTo(new FileOutputStream(new File(this.path)));
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onPostExecute(String result)
	{
		MapDrawingProvider provider = MapDrawingProvider.getInstance();
		provider.refresh();
	}

}
