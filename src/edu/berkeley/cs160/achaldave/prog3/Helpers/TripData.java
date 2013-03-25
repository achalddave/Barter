package edu.berkeley.cs160.achaldave.prog3.Helpers;

import java.util.ArrayList;

public class TripData {
	public String origin;
	public String destination;
	public String departTime;
	public String arrivalTime;
	public float fare;
	public ArrayList<TripLegData> tripLegs;
	
	// "2:30 pm on Fri, Jan 32nd"
	private static String TIME_FORMAT = "%h:%i %a on %D, %M %j%S";

	public TripData(String origin, String destination, String departTime, String arrivalTime, float fare, ArrayList<TripLegData> tripLegs) {
		this.origin = origin;
		this.destination = destination;
		this.departTime = departTime;
		this.arrivalTime = arrivalTime;
		this.fare = fare;
		this.tripLegs = tripLegs;
	}
	
	/*
	public TripData(Parcel source) {
		origin = source.readString();
		destination = source.readString();
		departTime = source.readString();
		arrivalTime = source.readString();
		fare = source.readFloat();
		tripLegs = source.readArrayList(TripLegData.class.getClassLoader());
	}
	*/
	
	public String getDepartTimeString() {
		return departTime.format(TIME_FORMAT);
	}
	
	public String getArrivalTimeString() {
		return arrivalTime.format(TIME_FORMAT);
	}
	
	public String getFareString() {
		return "$" + fare;
	}
	
	/*
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
		dest.writeString(departTime);
		dest.writeString(arrivalTime);
		dest.writeFloat(fare);
		dest.writeTypedList(tripLegs);
	}
	*/
	
}