package edu.berkeley.cs160.achaldave.prog3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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
	ListView directionsList;

	String origin;
	String destination;
	TripData currData;
	
	// for station selector
	private boolean selectOrigin = true;
	
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
		directionsList.setAdapter(new TripDataListAdapter(this, currData.tripLegs.toArray(new TripLegData[0])));
		
		fareField.setText(data.getFareString());
		originTime.setText(data.getDepartTimeString());
		destinationTime.setText(data.getArrivalTimeString());
	}
	
	private void setupView() {
		originField = (AutoCompleteTextView) findViewById(R.id.tripOriginField);
		originTime = (TextView) findViewById(R.id.tripDepartureTime);
		
		destinationField = (AutoCompleteTextView) findViewById(R.id.tripDestinationField);
		destinationTime = (TextView) findViewById(R.id.tripArrivalTime);
		
		fareField = (TextView) findViewById(R.id.tripFare);
		
		ImageView originListStations = (ImageView) findViewById(R.id.tripSelectOrigin);
		originListStations.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectOrigin = true;
				Intent intent = new Intent(TripActivity.this, StationSelectActivity.class);
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				intent.putExtra("width", (int) (size.x * 0.9));
				intent.putExtra("height", (int) (size.y * 0.9));
				intent.putExtra("dontGoToDeparture", true);
				startActivity(intent);
			}
		});
		
		ImageView destinationListStations = (ImageView) findViewById(R.id.tripSelectDestination);
		destinationListStations.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectOrigin = false;
				Intent intent = new Intent(TripActivity.this, StationSelectActivity.class);
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				intent.putExtra("width", (int) (size.x * 0.9));
				intent.putExtra("height", (int) (size.y * 0.9));
				intent.putExtra("dontGoToDeparture", true);
				startActivity(intent);
			}
		});
		
		directionsList = (ListView) findViewById(R.id.tripDirections);
		directionsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TripActivity.this, DialogMapActivity.class);
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				intent.putExtra("width", (int) (size.x * 0.9));
				intent.putExtra("height", (int) (size.y * 0.9));
				intent.putExtra("mapId", currData.tripLegs.get(position).getRouteMap());
				intent.putExtra("routeName", currData.tripLegs.get(position).getRouteName());
				startActivity(intent);
			}
		});

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
	
	private void updateFieldsWithCurrentData() {
		if (origin != null) {
			// BECAUSE ANDROID HATES YOU THAT'S WHY
			// http://stackoverflow.com/questions/5495225/how-to-disable-autocompletetextviews-drop-down-from-showing-up
			originField.setFocusable(false);
			originField.setFocusableInTouchMode(false);
			originField.setText(origin);
			originField.setFocusable(true);
			originField.setFocusableInTouchMode(true);			
		}
		if (destination != null) {
			destinationField.setFocusable(false);
			destinationField.setFocusableInTouchMode(false);
			destinationField.setText(destination);
			destinationField.setFocusable(true);
			destinationField.setFocusableInTouchMode(true);			
		}
		updateDirections(true);
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
	
	@Override
	protected void onNewIntent(Intent intent) {
		Bundle a = intent.getExtras();
		if (a.containsKey("selectedStation")) {
			String station = a.getString("selectedStation");
			if (selectOrigin) {
				origin = station;
			} else {
				destination = station;
			}
			updateFieldsWithCurrentData();
		}
		Log.d("Achal", "Received new intent");
	}
}
