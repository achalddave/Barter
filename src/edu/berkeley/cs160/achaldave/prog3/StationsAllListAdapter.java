package edu.berkeley.cs160.achaldave.prog3;

import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StationsAllListAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final String[] stationNames;

	public StationsAllListAdapter(Context context, String[] stationNames) {
		super(context, R.layout.stations_list_adapter_item, stationNames);
		this.context = context;
		
		Arrays.sort(stationNames, new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				boolean lhsBookmarked = HomeActivity.prefs.getBoolean(lhs, false);
				boolean rhsBookmarked = HomeActivity.prefs.getBoolean(rhs, false);
				if (lhsBookmarked && !rhsBookmarked) {
					return -1;
				} else if (rhsBookmarked && !lhsBookmarked) {
					return 1;
				} else {
					return lhs.compareTo(rhs);
				}
			}
		});
		this.stationNames = stationNames;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.stations_list_adapter_item, parent, false);	
		}
		TextView name = (TextView) convertView.findViewById(R.id.stationAdapterStationName);
		final String curr = stationNames[position];
		final ImageView bookmark = (ImageView) convertView.findViewById(R.id.stationAdapterBookmarkStar);
		if (HomeActivity.prefs.getBoolean(curr, false)) {
			bookmark.setImageResource(R.drawable.bookmark_star);
		} else {
			bookmark.setImageResource(R.drawable.bookmark_star_unfilled);
		}
		
		bookmark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editPrefs = HomeActivity.prefs.edit();
				boolean wasBookmarked = HomeActivity.prefs.getBoolean(curr, false);
				editPrefs.putBoolean(curr, !wasBookmarked);
				bookmark.setImageResource(wasBookmarked ? R.drawable.bookmark_star_unfilled : R.drawable.bookmark_star);
				editPrefs.commit();
			}
		});
		name.setText(curr);
		return convertView;
	}
}
