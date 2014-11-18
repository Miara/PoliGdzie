package com.poligdzie.json;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.android.gms.maps.model.LatLng;
import com.poligdzie.json.Polyline;

public class PolylineTest {

	private Polyline polyline;
	
	@Before
	public void setUp() throws Exception {
		polyline = new Polyline();
	}

	@Test
	public void testGetPoints() {
		polyline.setPoints("`~oia@");
		assertEquals("`~oia@", polyline.getPoints());
	}

	@Test
	public void testSetPoints() {
		polyline.setPoints("`~oia@");
		assertEquals("`~oia@", polyline.getPoints());
	}

	@Test
	public void testDecodePolyline() {
		polyline.setPoints("_p~iF~ps|U");
		List <LatLng> points = new ArrayList <LatLng>(); 
		points = polyline.decodePolyline();
		assertEquals(points.get(0).latitude, 38.5, 0.0);
	}

}
