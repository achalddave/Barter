package edu.berkeley.cs160.achaldave.prog3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import edu.berkeley.cs160.achaldave.prog3.Helpers.Callback;
import edu.berkeley.cs160.achaldave.prog3.Helpers.TripData;
import edu.berkeley.cs160.achaldave.prog3.Helpers.TripLegData;

public class TripActivity extends Activity {
	AutoCompleteTextView originField;
	AutoCompleteTextView destinationField;
	TextView originTime;
	TextView destinationTime;
	TextView fareField;

	String origin;
	String destination;
	TripData currData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip);
		setupView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trip, menu);
		return true;
	}

	private void updateDirections() {
		updateDirections(false);
	}
	
	private void updateDirections(boolean forceUpdate) {
		if (origin != null && destination != null && (currData == null || forceUpdate)) {
			HomeActivity.bartHandler.getTripData(origin, destination, new Callback<TripData, Void>() {

				@Override
				public Void call(TripData data) {
					final TripData fData = data;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							setDirections(fData);
						}
					});
					return null;
				}
			});
		}
	}
	
	private void setDirections(TripData data) {
		currData = data;
		ListView directionsList = (ListView) findViewById(R.id.tripDirections);
		directionsList.setAdapter(new TripDataListAdapter(this, currData.tripLegs.toArray(new TripLegData[0])));
	}
	
	private void setupView() {
		originField = (AutoCompleteTextView) findViewById(R.id.tripOriginField);
		originTime = (TextView) findViewById(R.id.tripDepartureTime);
		
		destinationField = (AutoCompleteTextView) findViewById(R.id.tripDestinationField);
		destinationTime = (TextView) findViewById(R.id.tripArrivalTime);
		
		fareField = (TextView) findViewById(R.id.tripFare);
		
		HomeActivity.bartHandler.getStationNames(new Callback<String[], Void>() {
			@Override
			public Void call(String[] stations) {
				final String[] fStations = stations;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setupAutocomplete(fStations);
					}
				});
				return null;
			}
		});
	}
	
	private void setupAutocomplete(String[] stations) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, stations);
		originField.setThreshold(1);
		originField.setAdapter(adapter);
		destinationField.setThreshold(1);
		destinationField.setAdapter(adapter);
		
		
		destinationField.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				destination = parent.getItemAtPosition(position).toString();
				updateDirections(true);
			}
		});
		
		originField.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				origin = parent.getItemAtPosition(position).toString();
				updateDirections(true);
			}
		});
	}
}
