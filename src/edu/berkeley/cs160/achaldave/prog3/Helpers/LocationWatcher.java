package edu.berkeley.cs160.achaldave.prog3.Helpers;

import edu.berkeley.cs160.achaldave.prog3.HomeActivity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class LocationWatcher implements LocationListener {

	private HomeActivity context;
	
	public LocationWatcher(HomeActivity context) {
		this.context = context;
		Log.d("Achal", "Location watcher created");
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Log.d("Achal", "Received new location");
		context.setLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Achal", "GPS Provider disabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.d("Achal", "GPS status changed: " + status);
	}

}
