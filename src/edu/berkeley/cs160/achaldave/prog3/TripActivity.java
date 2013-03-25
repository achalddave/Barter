package edu.berkeley.cs160.achaldave.prog3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import edu.berkeley.cs160.achaldave.prog3.Helpers.TripData;

public class TripActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trip, menu);
		return true;
	}

	private void setupView() {
		Bundle data = getIntent().getExtras();
		if (data.containsKey("map")) {
			int mapId = data.getInt("map");
			InteractiveMapFragment map = (InteractiveMapFragment) getFragmentManager().findFragmentById(R.id.map);
			map.setMap(data.getInt("map"));
		} else {
			View mapFrag = findViewById(R.id.map_fragment);
			mapFrag.setVisibility(View.INVISIBLE);
		}
		TripData trip = data.getParcelable("trip");
		TextView originText = (TextView) findViewById(R.id.tripOrigin);
		TextView departureTimeDetails = (TextView) findViewById(R.id.tripDepartureTime);
		
		originText.setText(trip.origin);
		departureTimeDetails.setText(trip.getDepartTimeString());
		
		TextView destinationText = (TextView) findViewById(R.id.tripDestination);
		TextView arrivalTimeDetails = (TextView) findViewById(R.id.tripArrivalTime);
		
		destinationText.setText(trip.destination);
		arrivalTimeDetails.setText(trip.getArrivalTimeString());
		
		TextView fareText = (TextView) findViewById(R.id.tripFare);
		fareText.setText(trip.getFareString());
	}
}
