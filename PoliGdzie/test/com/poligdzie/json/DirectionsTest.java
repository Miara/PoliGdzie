package com.poligdzie.json;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class DirectionsTest {

	static final LatLng LOCATION_PIOTROWO = new LatLng(52.4022703, 16.9495847);
	private static final LatLng MY_POINT = new LatLng(52.4014141, 16.9511311);
	private static final LatLng CENTRUM_WYKLADOWE = new LatLng(52.4037379,
			16.9498138);

	private Directions directions;
	private PolylineOptions options;
	private List<LatLng> points;

	private ObjectMapper mapper;

	@Before
	public void setUp() throws Exception {
		directions = new Directions();
		options = new PolylineOptions();

		mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String url = "https://maps.googleapis.com/maps/api/directions/json?waypoints=optimize:true|"
				+ MY_POINT.latitude
				+ ","
				+ MY_POINT.longitude
				+ "|"
				+ "|"
				+ CENTRUM_WYKLADOWE.latitude
				+ ","
				+ CENTRUM_WYKLADOWE.longitude + "&sensor=false";

		/*
		 * œci¹gniêcie niepustego drzewa z API, aby omin¹æ null pointer
		 * exception
		 */
		Directions directions = mapper
				.readValue(new URL(url), Directions.class);

		directions.getRoutes().get(0).getLegs().get(0).getSteps().get(0)
				.getPolyline().setPoints("_p~iF~ps|U_ulLnnqC_mqNvxq`@");
		/* czyszczenie pozostaloœci */

		for (int i = 1; i <= directions.getRoutes().get(0).getLegs().get(0)
				.getSteps().size(); i++) {
			directions.getRoutes().get(0).getLegs().get(0).getSteps().remove(i);
		}

		points = new ArrayList<LatLng>();
	}

	/*
	 * TODO: DO UZUPELNIENIA - teraz nie mam pojêcia z jakiej paki tam jest null
	 * pointer
	 */
	@Test
	public void testGeneratePolylineFromDirections() {
		fail("Na razie nie wiem jak to przetestowaæ i dlaczego tam jest null pointer");
		/*
		 * options = directions.generatePolylineFromDirections(options); points
		 * = options.getPoints();
		 * 
		 * assertSame(directions.getRoutes().get(0).getLegs().get(0).getSteps().get
		 * (0).getPolyline().decodePolyline(), points);
		 */
	}

}
