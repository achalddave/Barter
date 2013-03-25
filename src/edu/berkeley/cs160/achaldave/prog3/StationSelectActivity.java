package edu.berkeley.cs160.achaldave.prog3;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class StationSelectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int width = intent.getIntExtra("width", 500);
		int height = intent.getIntExtra("height", 500);
		setContentView(R.layout.activity_station_select);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutParams params = getWindow().getAttributes();
		params.height = height; // fixed height
		params.width = width; // fixed width
		params.alpha = 1.0f;
		params.dimAmount = 0.5f;
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		
		Fragment stationsListFrag = (Fragment) getFragmentManager().findFragmentById(R.id.stationSelector);
		ListView stationsList = (ListView) stationsListFrag.getView().findViewById(R.id.stationsList);
		stationsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(StationSelectActivity.this, DeparturesActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("selectedStation", ((TextView) view.findViewById(R.id.stationAdapterStationName)).getText());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bart_intro, menu);
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			Rect dialogBounds = new Rect();
			getWindow().getDecorView().getHitRect(dialogBounds);

			if (!dialogBounds.contains((int) ev.getX(), (int) ev.getY())) {
				// Tapped outside so we finish the activity
				this.finish();
			}
		}
		return super.dispatchTouchEvent(ev);
	}
}