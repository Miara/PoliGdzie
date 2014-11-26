package com.poligdzie.content_creation;


import java.sql.SQLException;
import java.util.ArrayList;

import android.util.Log;

import com.j256.ormlite.dao.ForeignCollection;
import com.poligdzie.persistence.*;

public class Fixture {

	private ContentCreator creator;
	
	//zmiena versionChanged w celach testowych ¿eby nie dodawaæ ci¹gle tych samych pól
	public Fixture(DatabaseHelper dbHelper) {
		// TODO Auto-generated constructor stub
		creator = new ContentCreator();
		
		
		Building cw = new Building("Centrum wyk³adowee",52.4041748, 16.9496774, "Piotrowo 2", 150, 2,"","cw");
		Building elektryk = new Building("Elektryk",52.401804, 16.951146, "Piotrowo 3a", 70, 10,"","el");
		Building bm = new Building("Budowa maszyn",52.402357, 16.950573, "Piotrowo 3", 70, 10,"","bm");
		
		
		creator.add(cw);
		creator.add(elektryk);
		creator.add(bm);
		
		Unit ii = new Unit("Instytut Informatyki", "http://cs.put.poznan.pl", UnitTypes.INSTITUTE, cw);
		Unit wi = new Unit("Wydzia³ Informatyki", "http://fc.put.poznan.pl", UnitTypes.FACULTY, bm);
		Unit eit = new Unit("Wydzia³ Elektroniki i Telekomunikacji", "http://et.put.poznan.pl", UnitTypes.FACULTY, elektryk);
		
		
		Room cw8 = new Room(8, "Sala wyk³adowa 8", RoomFunctions.LECTURE, 30, 30, 1, cw);
		Room wc = new Room(0, "WC", RoomFunctions.RESTROOM, 10, 10, 0, cw);
		Room dziekanat = new Room(503, "Dziekanat", RoomFunctions.STAFF, 15, 30, 5, bm);
		
		creator.add(cw8);
		creator.add(wc);
		creator.add(dziekanat);
		creator.add(ii);
		creator.add(wi);
		creator.add(eit);
		
		this.creator.populateDatabase(dbHelper);
		
	}

	public ContentCreator getCreator() {
		return creator;
	}

}
