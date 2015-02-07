package com.poligdzie.activities;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.poligdzie.R;

public class HelpActivity extends Activity implements OnClickListener
{
	private TextView name;
	private TextView desc;
	private Button nextButton;
	private int num = 0;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.onResume();
		setContentView(R.layout.help_activity);
		
		name = (TextView)findViewById(R.id.help_activity_name);
		desc = (TextView)findViewById(R.id.help_activity_description);
		nextButton = (Button)findViewById(R.id.help_activity_button);
		nextButton.setOnClickListener(this);
		
		name.setText(names[num]);
		desc.setText(descriptions[num]);

	}

	@Override
	public void onClick(View v)
	{
		if(v == nextButton)
		{
			num = (num+1)%3;
			name.setText(names[num]);
			desc.setText(descriptions[num]);
		}
		
	}

	String[] names = new String[]
			{
				"Korzystanie z wyszukiwarki",
				"Wyznaczanie trasy",
				"Przegl�d budynku"
			};
			
			String[] descriptions = new String[]
			{
				"1)	Wpisz nazw� wyszukiwanego obiektu w pasku wyszukiwania (nie musisz wpisywa� pe�nych nazw, mo�esz wpisa� np. '8CW' zamiast 'sala nr 8 centrum wyk�adowe)'."
				+ "\n2)	Znajd� na li�cie podpowiedzi to czego szukasz.\n3)	Po klikni�ciu na dany obiekt zostaniesz do niego przeniesiony."
				+ "\n4)	Je�eli szukanym obiektem by�o pomieszczenie, mo�esz prze��cza� si� pomi�dzy "
				+ "pi�trami u�ywaj�c strza�ek znajduj�cych si� przy bocznych kraw�dziach ekranu.\n",
				
				"1)	Wybierz punkt startowy korzystaj�c z wyszukiwarki, zaznaczaj�c go na mapie lub poprzez pobranie aktualnej pozycji za pomoc� systemu GPS.\n"
				+ "2)	Wybierz punkt docelowy w analogiczny spos�b.\n"
				+ "3)	Mo�esz zmieni� miejscami powy�sze punkty klikaj�c na strza�ki znajduj�ce si� w prawym g�rnym rogu ekranu.\n"
				+ "4)	Kliknij wyznacz tras�, a zostaniesz przeniesiony do punktu startowego.\n"
				+ "5)	Nawigacja po etapach danej trasy (przechodzenie na nast�pne pi�tro lub na zewn�trz) odbywa si� za pomoc� "
				+ "strza�ek znajduj�cych si� przy bocznych kraw�dziach lub po klikni�ciu na ikon� schod�w lub wyj�cia.",

				"1)	Znajd� na mapie zewn�trznej budynek lub skorzystaj z wyszukiwarki.\n"
				+ "2)	Kliknij na wybrany budynek.\n"
				+ "3)	Aby uzyska� podstawowe informacje o budynku wybierz ikon� �i�\n"
				+ "4)	Aby przenie�� si� na parter danego budynku wybierz ostatni� ikon� symbolizuj�c� wn�trze budynku.\n"
				+ "5)	Mo�esz prze��cza� si� pomi�dzy pi�trami u�ywaj�c strza�ek znajduj�cych si� przy bocznych k"
				+ "raw�dziach ekranu.\n"

			};


}
