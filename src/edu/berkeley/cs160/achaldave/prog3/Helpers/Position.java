package edu.berkeley.cs160.achaldave.prog3.Helpers;

import android.location.Location;
public class Position {

	float lat;
	float lng;

	public Position(float lat, float lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	public float distance(Position to) {
		return (float) Math.sqrt(square(to.lat - lat) + square(to.lng - lng));
	}
	
	public float distance(Location to) {
		return (float) Math.sqrt(square((float) (to.getLatitude() - lat)) + square((float) (to.getLongitude() - lng)));
	}

	private float square(float f) {
		return f*f;
	}
}
