package edu.berkeley.cs160.achaldave.prog3;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import edu.berkeley.cs160.achaldave.prog3.Helpers.Callback;
import edu.berkeley.cs160.achaldave.prog3.Helpers.DepartureData;

public class DeparturesActivity extends Activity {

	private ArrayList<DepartureData> departures = new ArrayList<DepartureData>();
	private String origin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			origin = extras.getString("origin");
		}

		setContentView(R.layout.activity_departures);
		setupView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.departures, menu);
		return true;
	}

	private void setupView() {
		setOrigin();
		if (origin != null) { setDepartures(); }
		
		ImageView listStations = (ImageView) findViewById(R.id.selectStation);
		listStations.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DeparturesActivity.this, StationsAllActivity.class);
				startActivity(intent);
			}
		});
		
		// setup autocomplete
		HomeActivity.bartHandler.getStationNames(new Callback<String[], Void>() {
			@Override
			public Void call(final String[] data) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setupAutocomplete(data);	
					};
				});
				return null;
			}
		});
	}

	private void setOrigin() {
		if (origin != null) {
			AutoCompleteTextView departingField = (AutoCompleteTextView) findViewById(R.id.departingStation);
			departingField.setText(origin);
		}
	}
	
	private void setDepartures() { setDepartures(false); }
	
	private void setDepartures(boolean forceUpdate) {
		if (departures.size() == 0 || forceUpdate) {
			HomeActivity.bartHandler.getDepartures(origin,
					new Callback<ArrayList<DepartureData>, Void>() {
						@Override
						public Void call(ArrayList<DepartureData> data) {
							departures = data;
							Log.d("Achal", "Departures is " + departures);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									setDepartures();
								}
							});
							return null;
						}
					});
			return;
		} else {
			Log.d("Achal", "Received departures: " + departures);
			ListView departuresList = (ListView) findViewById(R.id.departuresList);
			departuresList.setAdapter(new DeparturesListAdapter(this, departures));
		}
	}

	private void setupAutocomplete(String[] stations) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, stations);
		AutoCompleteTextView originStationInput = (AutoCompleteTextView) findViewById(R.id.departingStation);
		originStationInput.setThreshold(1);
		originStationInput.setAdapter(adapter);
		originStationInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				origin = parent.getItemAtPosition(position).toString();
				Log.d("Achal", "Updated origin: " + origin);
				setDepartures(true);
			}
		});
	}
}
