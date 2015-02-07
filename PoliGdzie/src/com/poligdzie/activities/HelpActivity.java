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
				"Przegl¹d budynku"
			};
			
			String[] descriptions = new String[]
			{
				"1)	Wpisz nazwê wyszukiwanego obiektu w pasku wyszukiwania (nie musisz wpisywaæ pe³nych nazw, mo¿esz wpisaæ np. '8CW' zamiast 'sala nr 8 centrum wyk³adowe)'."
				+ "\n2)	ZnajdŸ na liœcie podpowiedzi to czego szukasz.\n3)	Po klikniêciu na dany obiekt zostaniesz do niego przeniesiony."
				+ "\n4)	Je¿eli szukanym obiektem by³o pomieszczenie, mo¿esz prze³¹czaæ siê pomiêdzy "
				+ "piêtrami u¿ywaj¹c strza³ek znajduj¹cych siê przy bocznych krawêdziach ekranu.\n",
				
				"1)	Wybierz punkt startowy korzystaj¹c z wyszukiwarki, zaznaczaj¹c go na mapie lub poprzez pobranie aktualnej pozycji za pomoc¹ systemu GPS.\n"
				+ "2)	Wybierz punkt docelowy w analogiczny sposób.\n"
				+ "3)	Mo¿esz zmieniæ miejscami powy¿sze punkty klikaj¹c na strza³ki znajduj¹ce siê w prawym górnym rogu ekranu.\n"
				+ "4)	Kliknij wyznacz trasê, a zostaniesz przeniesiony do punktu startowego.\n"
				+ "5)	Nawigacja po etapach danej trasy (przechodzenie na nastêpne piêtro lub na zewn¹trz) odbywa siê za pomoc¹ "
				+ "strza³ek znajduj¹cych siê przy bocznych krawêdziach lub po klikniêciu na ikonê schodów lub wyjœcia.",

				"1)	ZnajdŸ na mapie zewnêtrznej budynek lub skorzystaj z wyszukiwarki.\n"
				+ "2)	Kliknij na wybrany budynek.\n"
				+ "3)	Aby uzyskaæ podstawowe informacje o budynku wybierz ikonê ‘i’\n"
				+ "4)	Aby przenieœæ siê na parter danego budynku wybierz ostatni¹ ikonê symbolizuj¹c¹ wnêtrze budynku.\n"
				+ "5)	Mo¿esz prze³¹czaæ siê pomiêdzy piêtrami u¿ywaj¹c strza³ek znajduj¹cych siê przy bocznych k"
				+ "rawêdziach ekranu.\n"

			};


}
