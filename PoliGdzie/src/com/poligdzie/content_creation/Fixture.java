package com.poligdzie.content_creation;

import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Floor;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.RoomFunctions;
import com.poligdzie.persistence.Unit;
import com.poligdzie.persistence.UnitTypes;

public class Fixture
{

	private ContentCreator	creator;

	// zmiena versionChanged w celach testowych ¿eby nie dodawaæ ci¹gle tych
	// samych pól
	public Fixture(DatabaseHelper dbHelper)
	{
		creator = new ContentCreator();

		Building cw = new Building("Centrum wyk³adowe", 52.4037039, 16.949444,
				"Piotrowo 2", 150, 2, "cw", "cw_ic", "cw_marker");
		Building elektryk = new Building("Elektryk", 52.401972, 16.951360,
				"Piotrowo 3a", 70, 10, "el;elektryk", "we_ic", "el_marker");
		Building bm = new Building("Budowa maszyn", 52.402357, 16.950573,
				"Piotrowo 3", 70, 10, "bm;budynek z zegarem", "bm_ic",
				"bm_marker");
		creator.add(cw);
		creator.add(elektryk);
		creator.add(bm);

		Unit ii = new Unit("Instytut Informatyki", "http://cs.put.poznan.pl",
				UnitTypes.INSTITUTE, "", cw);
		Unit wi = new Unit("Wydzia³ Informatyki", "http://fc.put.poznan.pl",
				UnitTypes.FACULTY, "Nasz Wydzia³!", bm);
		Unit eit = new Unit("Wydzia³ Elektroniki i Telekomunikacji",
				"http://et.put.poznan.pl", UnitTypes.FACULTY, "Wydzia³ Eit",
				elektryk);

		Room cw8 = new Room("8", "Sala wyk³adowa 8", RoomFunctions.LECTURE, 30,
				30, 1, "ósemka;cw8;8cw;8 cw; cw 8", cw);
		Room wc = new Room("0", "WC", RoomFunctions.RESTROOM, 10, 10, 0,
				"kibel", cw);
		Room dziekanat = new Room("503", "Dziekanat", RoomFunctions.STAFF, 15,
				30, 5, "pani kasia", bm);

		Floor cw0p = new Floor(0, "cw_test_parter",
				"Centrum wyk³adowe - parter", "cw0p", cw);
		Floor cw1p = new Floor(1, "cw_test_parter",
				"Centrum wyk³adowe - pierwsze piêtro", "cw1p", cw);

		Floor el0p = new Floor(0, "elektryk_parter",
				"Budynek elektryczny - parter", "el0p", elektryk);
		Floor el1p = new Floor(1, "elektryk_pietro",
				"Budynek elektryczny - pierwsze piêtro", "el1p", elektryk);

		creator.add(cw0p);
		creator.add(cw1p);
		creator.add(el0p);
		creator.add(el1p);
		creator.add(cw8);
		creator.add(wc);
		creator.add(dziekanat);
		creator.add(ii);
		creator.add(wi);
		creator.add(eit);

		this.creator.populateDatabase(dbHelper);

	}

	public ContentCreator getCreator()
	{
		return creator;
	}

}
