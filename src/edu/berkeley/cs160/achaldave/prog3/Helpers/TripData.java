package edu.berkeley.cs160.achaldave.prog3.Helpers;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class TripData implements Parcelable {
	public String origin;
	public String destination;
	public Time departTime;
	public Time arrivalTime;
	public float fare;
	public int route;
	
	// "2:30 pm on Fri, Jan 32nd"
	private static String TIME_FORMAT = "%h:%i %a on %D, %M %j%S";

	public TripData(String origin, String destination, Time departTime, Time arrivalTime, float fare, int route) {
		this.origin = origin;
		this.destination = destination;
		this.departTime = departTime;
		this.arrivalTime = arrivalTime;
		this.fare = fare;
		this.route = route;
	}
	
	public TripData(Parcel source) {
		origin = source.readString();
		destination = source.readString();
		departTime = new Time();
		arrivalTime = new Time();
		departTime.set(source.readLong());
		arrivalTime.set(source.readLong());
		fare = source.readFloat();
		route = source.readInt();
	}
	
	public String getDepartTimeString() {
		return departTime.format(TIME_FORMAT);
	}
	
	public String getArrivalTimeString() {
		return arrivalTime.format(TIME_FORMAT);
	}
	
	public String getFareString() {
		return "$" + fare;
	}
	
	public static final Parcelable.Creator<TripData> CREATOR = new Parcelable.Creator<TripData>() {

		@Override
		public TripData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new TripData(source);
		}

		@Override
		public TripData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TripData[size];
		}
		
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(origin);
		dest.writeString(destination);
		dest.writeLong(departTime.toMillis(true));
		dest.writeLong(arrivalTime.toMillis(true));
		dest.writeFloat(fare);
		dest.writeInt(route);
	}
	
}