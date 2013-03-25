package edu.berkeley.cs160.achaldave.prog3.Helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.berkeley.cs160.achaldave.prog3.R;

public class BartHandler {

	private final static String apiKey = "MW9S-E7SL-26DU-VV8V";
	private final static String keyStr = "key=" + apiKey;
	private final static String baseBartUrl = "http://api.bart.gov/api";
	private HashMap<String, Station> stations = new HashMap<String, Station>();
	private HashMap<String, Integer> colorToImage = new HashMap<String, Integer>(6);

	public BartHandler() {
		colorToImage.put("#ffff33", R.drawable.pittsburg_sfo_map);
		colorToImage.put("#ff9933", R.drawable.fremont_richmond_map);
		colorToImage.put("#339933", R.drawable.fremont_daly_map);
		colorToImage.put("#ff0000", R.drawable.richmond_millbrae_map);
		colorToImage.put("#0099cc", R.drawable.dublin_daly_map);
	}

	// this will be called on the home view, so BartHandler should
	// always have a list of stations
	public void getClosestStation(final Location location,
			final Callback<String, Void> callback) {
		if (stations.keySet().size() == 0) {
			getStationInfo(new Callback<Void, Void>() {

				@Override
				public Void call(Void data) {
					getClosestStation(location, callback);
					return null;
				}
			});
		} else {
			float closestStationDistance = Float.MAX_VALUE;
			String closestStation = "";
			for (Station station : stations.values()) {
				float distance = station.distance(location);
				if (distance < closestStationDistance) {
					closestStation = station.name;
					closestStationDistance = distance;
				}
			}
			callback.call(closestStation);
		}
	}

	public void getDepartures(String stationName,
			Callback<ArrayList<DepartureData>, Void> callback) {
		final Station station = stations.get(stationName);
		final Callback<ArrayList<DepartureData>, Void> fCallback = callback;
		WebRequester requester = new WebRequester(new WebDataReceiver() {
			@Override
			public void receive(InputStream in) {
				parseDepartures(parse(in), fCallback);
			}
		});
		requester.execute(baseBartUrl + "/etd.aspx?cmd=etd&orig="
				+ station.abbr + "&" + keyStr);
	}

	public void getFare(String station1, String station2, Callback<Float, Void> callback) {
		
	}
	
	public void getStationNames(final Callback<String[], Void> callback) {
		if (stations.keySet().size() == 0) {
			getStationInfo(new Callback<Void, Void>() {

				@Override
				public Void call(Void data) {
					getStationNames(callback);
					return null;
				}
			});
		} else {
			ArrayList<String> stationNames = new ArrayList<String>(stations.keySet());
			Collections.sort(stationNames);
			callback.call(stationNames.toArray(new String[0]));
		}
	}
	
	private void parseDepartures(Document doc,
			Callback<ArrayList<DepartureData>, Void> callback) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();

			// get origin station name
			String originExpression = "//station/abbr";
			Element originElem = (Element) xpath.evaluate(originExpression,
					doc, XPathConstants.NODE);
			String origin = originElem.getTextContent();

			// get all departures
			String expression = "//etd";
			NodeList etdNodes = (NodeList) xpath.evaluate(expression, doc,
					XPathConstants.NODESET);

			ArrayList<DepartureData> departures = new ArrayList<DepartureData>();
			
			// get details for each departure
			for (int i = 0; i < etdNodes.getLength(); i++) {
				Element etdNode = (Element) etdNodes.item(i);
				String destinationName = getTagChildText(etdNode, "destination");
				Element estimate = getTagChild(etdNode, "estimate");
				
				String departureTime = getTagChildText(estimate, "minutes");
				int platform = Integer.parseInt(getTagChildText(estimate,
						"platform"));
				String color = getTagChildText(estimate, "hexcolor");
				
				departures.add(new DepartureData(origin, destinationName, color, departureTime, platform));
			}
			
			Collections.sort(departures, new Comparator<DepartureData>() {

				@Override
				public int compare(DepartureData lhs, DepartureData rhs) {
					boolean lhsLeaving = lhs.departureTime.equals("Leaving");
					boolean rhsLeaving = rhs.departureTime.equals("Leaving");
					if (lhsLeaving && rhsLeaving) {
						return 0;
					} else if (lhsLeaving) {
						return -1;
					} else if (rhsLeaving) {
						return 1;
					}

					int lhsTime = Integer.parseInt(lhs.departureTime);
					int rhsTime = Integer.parseInt(rhs.departureTime);
					if (lhsTime < rhsTime){
						return -1;
					} else if (lhsTime == rhsTime) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			
			callback.call(departures);
		} catch (Exception e) {
			Log.e("Achal", "Error parsing XML: " + e);
			e.printStackTrace();
		}
	}

	private Document parse(InputStream in) {
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document out = builder.parse(in);
			in.close();
			return out;
		} catch (Exception e) {
			Log.e("Achal", "Couldn't parse xml");
			e.printStackTrace();
			try {
				in.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
	};

	private Element getTagChild(Element parent, String tagName) {
		return (Element) parent.getElementsByTagName(tagName).item(0);
	}

	private String getTagChildText(Element parent, String tagName) {
		return getTagChild(parent, tagName).getTextContent();
	}
	
	private void getStationInfo(final Callback<Void, Void> callback) {
		WebRequester requester = new WebRequester(new WebDataReceiver() {
			@Override
			public void receive(InputStream in) {
				parseStationInfo(parse(in));
				callback.call(null);
			}
		});
		requester.execute(baseBartUrl + "/stn.aspx?cmd=stns&" + keyStr);
	}
	
	private void parseStationInfo(Document doc) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "//stations/station";
			NodeList stationNodes = (NodeList) xpath.evaluate(expression, doc,
					XPathConstants.NODESET);
			for (int i = 0; i < stationNodes.getLength(); i++) {
				Element station = (Element) stationNodes.item(i);
				String name = station.getElementsByTagName("name").item(0)
						.getTextContent();
				String abbr = station.getElementsByTagName("abbr").item(0)
						.getTextContent();
				String address = station.getElementsByTagName("address")
						.item(0).getTextContent();
				String city = station.getElementsByTagName("city").item(0)
						.getTextContent();
				String county = station.getElementsByTagName("county").item(0)
						.getTextContent();
				String state = station.getElementsByTagName("state").item(0)
						.getTextContent();
				String zipcode = station.getElementsByTagName("zipcode")
						.item(0).getTextContent();
				float lat = Float.parseFloat(station
						.getElementsByTagName("gtfs_latitude").item(0)
						.getTextContent());
				float lng = Float.parseFloat(station
						.getElementsByTagName("gtfs_longitude").item(0)
						.getTextContent());
				stations.put(name, new Station(abbr, name, new Position(lat,
						lng), address, city, county, state, zipcode));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
