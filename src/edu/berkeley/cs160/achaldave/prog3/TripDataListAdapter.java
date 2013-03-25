package edu.berkeley.cs160.achaldave.prog3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.berkeley.cs160.achaldave.prog3.Helpers.TripLegData;

public class TripDataListAdapter extends ArrayAdapter<TripLegData>{
	private final Context context;
	private final TripLegData[] tripLegs;

	public TripDataListAdapter(Context context, TripLegData[] tripLegs) {
		super(context, R.layout.trip_leg_adapter, tripLegs);
		this.context = context;
		this.tripLegs = tripLegs;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.trip_leg_adapter, parent, false);	
		}
		TextView originStation = (TextView) convertView.findViewById(R.id.legOriginStation);
		TextView originTime = (TextView) convertView.findViewById(R.id.legOriginTime);
		TextView destinationStation = (TextView) convertView.findViewById(R.id.legDestinationStation);
		TextView destinationTime = (TextView) convertView.findViewById(R.id.legDestinationTime);
		TextView routeText = (TextView) convertView.findViewById(R.id.legRouteName);
		
		final TripLegData curr = tripLegs[position];
		originStation.setText(curr.originStation);
		originTime.setText(curr.originTime);
		destinationStation.setText(curr.destinationStation);
		destinationTime.setText(curr.destinationTime);
		routeText.setText(curr.getRouteName());

		return convertView;
	}
}
