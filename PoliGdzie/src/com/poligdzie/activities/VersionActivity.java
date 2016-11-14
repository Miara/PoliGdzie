package com.poligdzie.activities;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.poligdzie.R;

public class VersionActivity extends Activity
{
	private TextView name;
	private TextView desc;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.onResume();
		setContentView(R.layout.version_activity);
		
		name = (TextView)findViewById(R.id.version_activity_name);
		desc = (TextView)findViewById(R.id.version_activity_description);

		
		name.setText("Wersja");
		desc.setText(description);
	}
	
	private String description = "Wersja: \nSynchronizacja z baz¹ zdaln¹: NIE\n"
			+ "Wersja 1.0.6\n";
}
