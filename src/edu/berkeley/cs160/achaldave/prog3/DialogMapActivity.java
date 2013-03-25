package edu.berkeley.cs160.achaldave.prog3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class DialogMapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_map);

		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int width = intent.getIntExtra("width", 500);
		int height = intent.getIntExtra("height", 500);
		Log.d("Achal", "Width: " + width + "; height: " + height);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutParams params = getWindow().getAttributes();
		params.height = height; // fixed height
		params.width = width; // fixed width
		params.alpha = 1.0f;
		params.dimAmount = 0.5f;
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		
		InteractiveMapFragment mapFrag = (InteractiveMapFragment) getFragmentManager().findFragmentById(R.id.dialogMapFrag);
		if (intent.getExtras().containsKey("mapId")) {
			int mapId = intent.getIntExtra("mapId", R.drawable.map_icon);
			String mapName = intent.getExtras().getString("routeName", "BART Map");
			mapFrag.setMap(mapId);
			mapFrag.setRouteName(mapName);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialog_map, menu);
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
