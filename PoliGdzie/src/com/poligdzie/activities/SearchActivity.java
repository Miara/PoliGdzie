package com.poligdzie.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.poligdzie.R;

public class SearchActivity extends Activity implements OnClickListener{

	private Button goToMapButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        
        goToMapButton = (Button) findViewById(R.id.goToMapButton);
        goToMapButton.setOnClickListener(this);
        
        
    }
    
    @Override
	public void onClick(View v) {
    	Log.i("TEST","TEST1");
		if( v == goToMapButton){
			Intent intent = new Intent(this, MapActivity.class);
	        startActivity(intent);
		}
		
    }
		
}
