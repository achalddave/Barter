package edu.berkeley.cs160.achaldave.prog3.Helpers;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;
import android.util.Log;

public class DepartureData implements Parcelable {
	public String origin;
	public String destination;
	public String routeHexColor;
	public String departureTime;
	public int platform;
	public Time lastUpdate;

	public DepartureData(String origin, String destination,
			String routeHexColor, String departureTime, int platform) {
		this.origin = origin;
		this.destination = destination;
		this.routeHexColor = routeHexColor;
		this.departureTime = departureTime;
		this.platform = platform;

		lastUpdate = new Time();
		lastUpdate.setToNow();
	}

	public DepartureData(Parcel source) {
		origin = source.readString();
		destination = source.readString();
		routeHexColor = source.readString();
		departureTime = source.readString();
		platform = source.readInt();
		lastUpdate = new Time();
		lastUpdate.set(source.readLong());
	}
	
	public String getDepartureTimeString() {
		try {
			int min = Integer.parseInt(this.departureTime);
			return min + " min";
		} catch (NumberFormatException e) {
			return this.departureTime;
		}
	}

	public String getDepartrueTimeDescription() {
		try {
			int min = Integer.parseInt(departureTime);
			return "Train departs in " + min + " min";
		} catch (NumberFormatException e) {
			if (departureTime.equals("Leaving")) {
				return "Train is leaving now";
			} else {
				return "Departure time: " + departureTime;
			}
		}
	}

	public static final Parcelable.Creator<DepartureData> CREATOR = new Parcelable.Creator<DepartureData>() {

		@Override
		public DepartureData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new DepartureData(source);
		}

		@Override
		public DepartureData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DepartureData[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(origin);
		dest.writeString(destination);
		dest.writeString(routeHexColor);
		dest.writeString(departureTime);
		dest.writeInt(platform);
		dest.writeLong(lastUpdate.toMillis(true));
	}
}