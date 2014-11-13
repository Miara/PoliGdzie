package com.json.poligdzie;

public class Steps {
	private Location start_location;
	private Location end_location;
	private Polyline polyline;
	private Distance distance;
	
	public Location getStart_location() {
		return start_location;
	}
	public void setStart_location(Location start_location) {
		this.start_location = start_location;
	}
	public Location getEnd_location() {
		return end_location;
	}
	public void setEnd_location(Location end_location) {
		this.end_location = end_location;
	}
	public Polyline getPolyline() {
		return polyline;
	}
	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}
	public Distance getDistance() {
		return distance;
	}
	public void setDistance(Distance distance) {
		this.distance = distance;
	}
	
}
