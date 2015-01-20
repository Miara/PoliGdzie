package com.poligdzie.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.poligdzie.singletons.DataProvider;

public class PromptDataTask extends AsyncTask<String, Void, String>
{

	private DataProvider provider;
	private Context cnt;
	
	@Override
	protected String doInBackground(String... params)
	{
		// TODO Auto-generated method stub
		provider = DataProvider.getInstance();
		provider.initialize(cnt);
		return null;
	}

	@Override
	protected void onPostExecute(String result)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Toast.makeText(cnt, "Zaladowano", Toast.LENGTH_SHORT); 
	}

	public PromptDataTask(Context cnt)
	{
		this.cnt = cnt;
	}

}
