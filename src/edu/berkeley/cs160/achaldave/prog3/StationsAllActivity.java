package edu.berkeley.cs160.achaldave.prog3;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.view.Menu;

public class StationsAllActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stations_all);
		ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
		ActionBar.Tab listTab = ab.newTab().setText("List");
		ActionBar.Tab mapTab = ab.newTab().setText("Map");
		
		Fragment listFrag = new StationsListFragment();
		Fragment mapFrag = new InteractiveMapFragment();
		
		listTab.setTabListener(new StationsAllTabListener(listFrag));
		listTab.setTabListener(new StationsAllTabListener(listFrag));
		listTab.setTabListener(new StationsAllTabListener(listFrag));
		
		mapTab.setTabListener(new StationsAllTabListener(mapFrag));
		
		ab.addTab(listTab);
		ab.addTab(mapTab);
		
		listTab.select();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stations_all, menu);
		return true;
	}
}
