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

	// zmiena versionChanged w celach testowych �eby nie dodawa� ci�gle tych
	// samych p�l
	public Fixture(DatabaseHelper dbHelper)
	{
		creator = new ContentCreator();

		Building cw = new Building("Centrum wyk�adowe", 52.4037039, 16.949444,
				"Piotrowo 2", 150, 2, "cw", "cw_ic", "cw_marker");
		Building elektryk = new Building("Elektryk", 52.401972, 16.951360,
				"Piotrowo 3a", 70, 10, "el;elektryk", "we_ic", "el_marker");
		Building bm = new Building("Budowa maszyn", 52.402357, 16.950573,
				"Piotrowo 3", 70, 10, "bm;budynek z zegarem", "bm_ic",
				"bm_marker");
		creator.add(cw);
		creator.add(elektryk);
		creator.add(bm);

		Floor cw0p = new Floor(0, "cw_test_parter",
				"Centrum wyk�adowe - parter", "cw0p", cw);
		Floor cw1p = new Floor(1, "cw_test_parter",
				"Centrum wyk�adowe - pierwsze pi�tro", "cw1p", cw);

		Floor el0p = new Floor(0, "elektryk_parter",
				"Budynek elektryczny - parter", "el0p", elektryk);
		Floor el1p = new Floor(1, "elektryk_pietro",
				"Budynek elektryczny - pierwsze pi�tro", "el1p", elektryk);

		Floor bm0p = new Floor(0, "bm_p", "Budowa maszyn - parter", "bm0p", bm);

		Floor bm1p = new Floor(1, "bm_1", "Budowa maszyn - pierwsze pi�tro",
				"bm1p", bm);

		Floor bm2p = new Floor(2, "bm_2", "Budowa maszyn - drugie pi�tro",
				"bm2p", bm);

		Floor bm3p = new Floor(3, "bm_3", "Budowa maszyn - trzecie pi�tro",
				"bm3p", bm);

		Floor bm4p = new Floor(4, "bm_4", "Budowa maszyn - czwarte pi�tro",
				"bm4p", bm);

		Floor bm5p = new Floor(5, "bm_5", "Budowa maszyn - piate pi�tro",
				"bm5p", bm);

		creator.add(cw0p);
		creator.add(cw1p);
		creator.add(el0p);
		creator.add(el1p);
		creator.add(bm0p);
		creator.add(bm1p);
		creator.add(bm2p);
		creator.add(bm3p);
		creator.add(bm4p);
		creator.add(bm5p);

		Room iiOffice = new Room("0", "Sekretariat Instytutu Informatyki",
				RoomFunctions.STAFF, 30, 30, cw1p, "w�glarz!", cw);
		Room dziekanat = new Room("503", "Dziekanat", RoomFunctions.STAFF, 15,
				30, bm5p, "pani kasia", bm);

		Unit ii = new Unit("Instytut Informatyki", "http://cs.put.poznan.pl",
				UnitTypes.INSTITUTE, "", cw, iiOffice);
		Unit wi = new Unit("Wydzia� Informatyki", "http://fc.put.poznan.pl",
				UnitTypes.FACULTY, "Nasz Wydzia�!", bm, dziekanat);
		Unit eit = new Unit("Wydzia� Elektroniki i Telekomunikacji",
				"http://et.put.poznan.pl", UnitTypes.FACULTY, "Wydzia� Eit",
				elektryk);

		Room cw8 = new Room("8", "Sala wyk�adowa 8", RoomFunctions.LECTURE, 30,
				30, cw1p, "�semka;cw8;8cw;8 cw; cw 8", cw);
		Room wc = new Room("0", "WC", RoomFunctions.RESTROOM, 10, 10, cw0p,
				"kibel", cw);

		creator.add(iiOffice);
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
