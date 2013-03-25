package edu.berkeley.cs160.achaldave.prog3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.berkeley.cs160.achaldave.prog3.Helpers.BartHandler;
import edu.berkeley.cs160.achaldave.prog3.Helpers.Callback;
import edu.berkeley.cs160.achaldave.prog3.Helpers.LocationWatcher;

public class HomeActivity extends Activity {
	public static BartHandler bartHandler;

	private LocationManager locationManager;
	private String locationProvider;
	private Location location;
	private LocationWatcher locationWatcher;
	private String closestStation;
	public static SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		setupView();
		bartHandler = new BartHandler();
		setupLocationHandling();

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (!prefs.contains("has_launched_before")) {
			openHelp();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void setLocation(Location loc) {
		location = loc;
		locationUpdateHandler();
	}

	private void openHelp() {
		Intent intent = new Intent(HomeActivity.this, BartIntroActivity.class);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		intent.putExtra("width", (int) (size.x * 0.95));
		intent.putExtra("height", (int) (size.y * 0.95));
		startActivity(intent);
		Editor prefEdit = prefs.edit();
		prefEdit.putBoolean("has_launched_before", true);
		prefEdit.commit();
	}

	private void setupLocationHandling() {
		locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPS is disabled.", Toast.LENGTH_SHORT).show();
		} else {
			locationWatcher = new LocationWatcher(this);
			locationProvider = locationManager.getBestProvider(new Criteria(),
					true);
			location = locationManager.getLastKnownLocation(locationProvider);
			if (location != null) {
				Log.d("Achal", "Location existed");
				locationUpdateHandler();
			}
			locationManager.requestLocationUpdates(locationProvider, 0, 0,
					locationWatcher);
		}
	}

	private void setupView() {
		Button help = (Button) findViewById(R.id.help);
		help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openHelp();
			}
		});
		LinearLayout departures = (LinearLayout) findViewById(R.id.closestStationContainer);
		departures.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						DeparturesActivity.class);
				intent.putExtra("origin", closestStation);
				startActivity(intent);
			}
		});

		LinearLayout allStations = (LinearLayout) findViewById(R.id.allStationsContainer);
		allStations.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						StationsAllActivity.class);
				startActivity(intent);
			}
		});
		
		LinearLayout tripPlanner = (LinearLayout) findViewById(R.id.tripPlanContainer);
		tripPlanner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, TripActivity.class);
				startActivity(intent);
			}
		});
	}

	private void locationUpdateHandler() {
		Log.d("Achal", "Looking for the closest station to: " + location);
		bartHandler.getClosestStation(location, new Callback<String, Void>() {

			@Override
			public Void call(final String station) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						Log.d("Achal", "Closest station is: " + station);
						closestStation = station;
						writeClosestStation();
					}
				});
				return null;
			}
		});
	}

	private void writeClosestStation() {
		Log.d("Achal", "Writing closest station");
		TextView closestStation = (TextView) findViewById(R.id.closestStation);
		closestStation.setText(this.closestStation);
		Log.d("Achal", "Wrote closest station");
	}
}