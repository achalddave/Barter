package edu.berkeley.cs160.achaldave.prog3.Helpers;

import android.location.Location;

public class Station {
	public String abbr;
	public String name;
	public Position position;
	public String address;
	public String city;
	public String county;
	public String state;
	public String zipcode;
	
	public Station(String abbr, String name, Position position, String address, String city, String county, String state, String zipcode) {
		this.abbr = abbr;
		this.name = name;
		this.position = position;
		this.address = address;
		this.city = city;
		this.county = county;
		this.state = state;
		this.zipcode = zipcode;
	}
	
	public float distance(Station to) {
		return position.distance(to.position);
	}
	
	public float distance(Position to) {
		return position.distance(to);
	}
	
	public float distance(Location to) {
		return position.distance(to);
	}
	
}
