package edu.berkeley.cs160.achaldave.prog3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class BartIntroActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int width = intent.getIntExtra("width", 500);
		int height = intent.getIntExtra("height", 500);
		setContentView(R.layout.activity_bart_intro);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutParams params = getWindow().getAttributes();
		params.height = height; // fixed height
		params.width = width; // fixed width
		params.alpha = 1.0f;
		params.dimAmount = 0.5f;
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
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
