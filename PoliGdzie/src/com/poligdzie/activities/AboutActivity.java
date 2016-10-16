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

public class AboutActivity extends Activity
{
	private TextView name;
	private TextView desc;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.onResume();
		setContentView(R.layout.about_activity);
		
		name = (TextView)findViewById(R.id.about_activity_name);
		desc = (TextView)findViewById(R.id.about_activity_description);

		
		name.setText("O programie");
		desc.setText(description);
	}
	
	private String description = "Program zosta³ stworzony na potrzeby pracy in¿ynierskiej "
			+ "pt \"Przewodnik mobilny po kampusie Piotrowo\".\n"
			+ "Pozwala on na znajdowanie pomieszczeñ, budynków oraz jednostek organizacyjnych w obrêbie tytu³owego kampusu."
			+ "\nAutorzy programu to: Sandra ¯ra³ka, Wojciech Miarczyñski, Mateusz Poznaniak oraz Tomasz Cichoszewski."
			+ "\nPraca wykonana pod kierunkiem dr in¿. Piotra Zielniewicza.";
}
