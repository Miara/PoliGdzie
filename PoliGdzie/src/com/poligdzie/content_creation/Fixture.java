package com.poligdzie.content_creation;


import java.sql.SQLException;
import java.util.ArrayList;

import android.util.Log;

import com.j256.ormlite.dao.ForeignCollection;
import com.poligdzie.persistence.*;

public class Fixture {

	private ContentCreator creator;
	
	public Fixture(DatabaseHelper dbHelper) {
		// TODO Auto-generated constructor stub
		creator = new ContentCreator();
		
		
		Building cw = new Building(52.4041748, 16.9496774, "Piotrowo 2", 150, 2);
		Building elektryk = new Building(52.401804, 16.951146, "Piotrowo 3a", 70, 10);
		Building bm = new Building(52.402357, 16.950573, "Piotrowo 3", 70, 10);
		
		
		creator.add(cw);
		creator.add(elektryk);
		creator.add(bm);
		
		this.creator.populateDatabase(dbHelper);
		
		Unit ii = new Unit("Instytut Informatyki", "http://cs.put.poznan.pl", UnitTypes.INSTITUTE, cw);
		//cw.add(ii);
		Unit wi = new Unit("Wydzia� Informatyki", "http://fc.put.poznan.pl", UnitTypes.FACULTY, bm);
		//bm.add(wi);
		Unit eit = new Unit("Wydzia� Elektroniki i Telekomunikacji", "http://et.put.poznan.pl", UnitTypes.FACULTY, elektryk);
		//elektryk.add(eit);
		
		Room cw8 = new Room(8, "Sala wyk�adowa 8", RoomFunctions.LECTURE, 30, 30, 1, cw);
		//cw.add(cw8);
		Room wc = new Room(0, "WC", RoomFunctions.RESTROOM, 10, 10, 0, cw);
		//cw.add(wc);
		Room dziekanat = new Room(503, "Dziekanat", RoomFunctions.STAFF, 15, 30, 5, bm);
		//bm.add(dziekanat);
		
		
		
		
		creator.add(cw8);
		creator.add(wc);
		creator.add(dziekanat);
		creator.add(ii);
		creator.add(wi);
		creator.add(eit);
		
	}

	public ContentCreator getCreator() {
		return creator;
	}

}
