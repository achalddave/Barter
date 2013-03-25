package edu.berkeley.cs160.achaldave.prog3;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.berkeley.cs160.achaldave.prog3.Helpers.Callback;

public class StationsListFragment extends Fragment {
	private String[] stationNames = new String[0];
	private View myView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		myView = inflater.inflate(R.layout.stations_list_view,
				container, false);
		setStations();
		return myView;
	}

	private void setStations() {
		if (stationNames.length == 0) {
			HomeActivity.bartHandler
					.getStationNames(new Callback<String[], Void>() {

						@Override
						public Void call(String[] names) {
							stationNames = names;
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									setStations();
								}
							});
							return null;
						}
					});
		} else {
			ListView stationsList = (ListView) myView.findViewById(
					R.id.stationsList);
			stationsList.setAdapter(new StationsAllListAdapter(getActivity()
					.getBaseContext(), stationNames));
		}
		return;
	}
}
