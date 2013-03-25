package edu.berkeley.cs160.achaldave.prog3;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.berkeley.cs160.achaldave.prog3.Helpers.DepartureData;

public class DeparturesListAdapter extends ArrayAdapter<DepartureData>{
	private final Context context;
	private final DepartureData[] departures;

	public DeparturesListAdapter(Context context, ArrayList<DepartureData> departures) {
		super(context, R.layout.departure_adapter_item, departures);
		this.context = context;
		this.departures = departures.toArray(new DepartureData[0]);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.departure_adapter_item, parent, false);	
		}
		TextView name = (TextView) convertView.findViewById(R.id.listAdapterStationName);
		TextView time = (TextView) convertView.findViewById(R.id.departureTime);
		TextView platform = (TextView) convertView.findViewById(R.id.departurePlatform);
		DepartureData curr = departures[position];
		name.setText(curr.destination);
		time.setText(curr.getDepartureTimeString());
		platform.setText("Platform " + curr.platform);
		return convertView;
	}

}
