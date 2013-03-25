package edu.berkeley.cs160.achaldave.prog3.Helpers;


public class TripLegData {

	public String originStation;
	public String destinationStation;
	public String originTime;
	public String destinationTime;
	public int routeNumber;
	
	public TripLegData(String o, String d, String s, String e, int r) {
		this.originStation = o;
		this.destinationStation = d;
		this.originTime = s;
		this.destinationTime = e;
		this.routeNumber = r;
	}
	
	/*
	public TripLegData(Parcel source) {
		this.originStation = source.readString();
		this.destinationStation = source.readString();
		this.originStart = source.readString();
		this.originEnd = source.readString();
		this.routeNumber = source.readInt();
	}
	
	public static final Parcelable.Creator<TripLegData> CREATOR = new Parcelable.Creator<TripLegData>() {

		@Override
		public TripLegData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new TripLegData(source);
		}

		@Override
		public TripLegData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TripLegData[size];
		}
		
	};
	*/
	
	public String getRouteName() {
		return BartHandler.routeToName.get(routeNumber);
	}
	
	public int getRouteMap() {
		return BartHandler.routeToMap.get(routeNumber);
	}

	/*
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(originStation);
		dest.writeString(destinationStation);
		dest.writeString(originStart);
		dest.writeString(originEnd);
		dest.writeInt(routeNumber);
	}
	*/
}