package edu.berkeley.cs160.achaldave.prog3.Helpers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

public class WebRequester extends AsyncTask <String, InputStream, Void> {

	WebDataReceiver receiver;
	
	public WebRequester(WebDataReceiver receiver) {
		this.receiver = receiver;
	}
	
	@Override
	protected Void doInBackground(String... urls) {
		for (String strUrl : urls) {
			URL url;
			try {
				url = new URL(strUrl);
			} catch (MalformedURLException e) {
				Log.d("Achal", "Malformed URL: " + strUrl);
				e.printStackTrace();
				continue;
			}

			try {
				Log.d("Achal", "Receiving data from " + strUrl);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				publishProgress(urlConnection.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	protected final void publishProgress(InputStream value) {
		receiver.receive(value);
	}
}
