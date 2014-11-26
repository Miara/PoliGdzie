package com.poligdzie.activities;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.interfaces.Constants;

public class PromptActivity extends Activity implements OnClickListener,Constants{

	private String mode;
	private TextView promptMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prompt_activity);
        
        Context kontekst = getApplicationContext();
		mode = PreferenceManager.getDefaultSharedPreferences(kontekst)
	                .getString(PROMPT_MODE, "");
		Log.i("POLIGDZIE",mode);
		promptMode = (TextView)findViewById(R.id.textView_prompt);
		if ( promptMode != null )promptMode.setText(mode);

    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
