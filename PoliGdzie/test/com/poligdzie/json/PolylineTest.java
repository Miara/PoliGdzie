package com.poligdzie.json;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.android.gms.maps.model.LatLng;

public class PolylineTest {

	private Polyline polyline;
	
	@Before
	public void setUp() throws Exception {
		polyline = new Polyline();
	}

    /* 
     * przypadki testowe ze strony 
     * https://developers.google.com/maps/documentation/utilities/polylinealgorithm
     * 
     * */
     
	@Test
	public void testDecodePolyline() {
		polyline.setPoints("_p~iF~ps|U");
		List <LatLng> points = new ArrayList <LatLng>(); 
		points = polyline.decodePolyline();
		assertEquals(38.5, points.get(0).latitude, 0.0);
		assertEquals(-120.2, points.get(0).longitude, 0.0);
		
		polyline.setPoints("_p~iF~ps|U_ulLnnqC_mqNvxq`@");
		 
		points = polyline.decodePolyline();
		assertEquals(38.5, points.get(0).latitude, 0.0);
		assertEquals(-120.2, points.get(0).longitude, 0.0);
		
		assertEquals(40.7, points.get(1).latitude, 0.0);
		assertEquals(-120.95, points.get(1).longitude, 0.0);
		
		assertEquals(43.252, points.get(2).latitude, 0.0);
		assertEquals(-126.453, points.get(2).longitude, 0.0);
	}

}
