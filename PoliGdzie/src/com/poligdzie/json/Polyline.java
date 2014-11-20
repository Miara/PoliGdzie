package com.poligdzie.json;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.google.android.gms.maps.model.LatLng;

public class Polyline {
	private String points;

	public String getPoints() {
		return points;
	}

	@JsonProperty("points")
	public void setPoints(String points) {
		this.points = points;
	}
	
	public List<LatLng> decodePolyline() {
			List<LatLng> poly = new ArrayList<LatLng>();
			int index = 0, len = points.length();
			int lat = 0, lng = 0;

			int result_tab[] = new int[2];
			while (index < len) {
				
				
				result_tab = getValueAfterBitOperations(index);
				lat += result_tab[0]; 
				index = result_tab[1];
				
				result_tab = getValueAfterBitOperations(index);
				
				lng += result_tab[0]; 
				index = result_tab[1];

				LatLng p = new LatLng((((double) lat / 1E5)),
						(((double) lng / 1E5)));
				poly.add(p);
			}
			return poly;
		}
	
	private int[] getValueAfterBitOperations(int index) {
		int shift = 0, value = 0, result = 0;
		int initialAscii = 0, asciiAfterMask = 0;
		do {
			initialAscii = points.charAt(index++) - 63;
			asciiAfterMask = initialAscii & 31;
			asciiAfterMask = asciiAfterMask << shift;
			value |= asciiAfterMask;
			shift += 5;
		} while (initialAscii >= 0x20);
		
		
		if((value & 1) != 0) {
			result = ~(value >> 1);
		} else { 
			result = value >> 1;
		}
		int result_tab[] = new int[2]; 
		result_tab[0] = result;
		result_tab[1] = index;
		
		return result_tab;
	}
}
