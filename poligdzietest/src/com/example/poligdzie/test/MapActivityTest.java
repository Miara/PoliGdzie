package com.example.poligdzie.test;

import org.junit.Before;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.widgets.SearchAutoCompleteTextView;
import com.robotium.solo.Solo;

public class MapActivityTest extends ActivityInstrumentationTestCase2<MapActivity>{

	private Solo solo;
	
	public MapActivityTest() {
		super(MapActivity.class);
		// TODO Auto-generated constructor stub
	}


	@Before
	public void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(),getActivity());
	}

	
	//test sprawdza, czy uda siê znaleŸæ dane pomieszczenie i czy aplikacja skieruje u¿ytkownika do odpowiedniego budynku
	//TODO: szukanie budynkow, jednostek oraz sprawdzanie numeru pietra
	
	@MediumTest
	public void testSearching() {
		searchFor("8", "Sala 8", "Sala 8", "Centrum");
		searchFor("Sala 1", "Sala 1", "Sala 1", "Centrum");
		searchFor("fresh", "freshmarket", "freshmarket", "Centrum");
		searchFor("2.7", "laboratorium 2.7.2", "laboratorium 2.7.2", "Biblioteka");
		searchFor("1.6", "laboratorium 1.6.18", "laboratorium 1.6.18", "Biblioteka");
		searchFor("00", "Sala 001A", "Sala 001A", "Chemicznej");
		searchFor("ce", "Centrum Wyk³adowe", "Centrum Wyk³adowe", "ul. Piotrowo 2, 60-965 Poznañ");
		searchFor("bt", "Biblioteka Techniczna", "Biblioteka Techniczna", "ul. Piotrowo 2, 60-965 Poznañ");
		searchFor("chemi", "Wydzia³ Technologii Chemicznej", "Wydzia³ Technologii Chemicznej", "ul. Berdychowo 4, 60-965 Poznañ");
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		//super.tearDown();
		solo.finishOpenedActivities();
	}

	
	public void searchFor(String typed, String autocomplete, String mainDescription, String detailDescription) {
		solo.sleep(1000);
		
		SearchAutoCompleteTextView vSearchEditText= (SearchAutoCompleteTextView) solo.getView(R.id.search_point_text_edit);
		solo.clearEditText(vSearchEditText);
		solo.enterText(vSearchEditText, typed);
	
		solo.waitForText(autocomplete);
		solo.clickOnText(autocomplete);

		solo.waitForFragmentById(R.id.search_description_frag);
		
		//sleep potrzebny na wstrzykniêcie wartoœci
		solo.sleep(1000);
        
		TextView mainDesc = (TextView) solo.getView(R.id.search_main_description);
		assertEquals(mainDescription, mainDesc.getText().toString());
		solo.waitForView(R.id.detail_description);
		TextView detailsDesc = (TextView) solo.getView(R.id.search_detail_description);
		//tymczasowe ze wzgledu na kodowanie
		assertTrue(detailsDesc.getText().toString().contains(detailDescription));
		
		//docelowe!
		//assertEquals("Centrum wyk³adowe", detailsDesc.getText().toString());
	}
	
}
