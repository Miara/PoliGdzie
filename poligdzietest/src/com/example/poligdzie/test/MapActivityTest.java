package com.example.poligdzie.test;

import org.junit.Before;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.EditText;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.poligdzie.activities.MapActivity;
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

	@MediumTest
	public void testSearching() {
		searchFor("8", "Sala 8", "Sala 8", "Centrum");
		searchFor("Sala 1", "Sala 1", "Sala 1", "Centrum");
		searchFor("fresh", "freshmarket", "freshmarket", "Centrum");
		searchFor("2.7", "laboratorium 2.7.2", "laboratorium 2.7.2", "Biblioteka");
		searchFor("1.6", "laboratorium 1.6.18", "laboratorium 1.6.18", "Biblioteka");
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		//super.tearDown();
		solo.finishOpenedActivities();
	}

	
	public void searchFor(String typed, String autocomplete, String mainDescription, String detailDescription) {
		EditText vSearchEditText= (EditText) solo.getView(R.id.search_point_text_edit);
		solo.clearEditText(vSearchEditText);
		solo.enterText(vSearchEditText, typed);
	
		
		
		solo.waitForText(autocomplete);
		solo.clickOnText(autocomplete);

		
		
		solo.waitForFragmentById(R.id.search_description_frag);
		
		//sleep potrzebny na wstrzykniêcie wartoœci
		solo.sleep(10000);
		
        
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
