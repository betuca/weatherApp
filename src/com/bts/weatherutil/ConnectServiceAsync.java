package com.bts.weatherutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.view.View;

public class ConnectServiceAsync extends AsyncTask<Void, Void, String> {

	private static String BETUCAID = "a780e451be62ab67e9799a422c80ffc7";
	private static String SERVICE_URL = "http://api.openweathermap.org/data/2.5/weather";
	private ConnectionListener listener;
	private View loadingView;
	private String city;
	private String latitude;
	private String longitude;
	private int searchType;

	public ConnectServiceAsync(int searchType, View loadingView, ConnectionListener listener) {
		super();
		this.searchType = searchType;
		this.listener = listener;
		this.loadingView = loadingView;
	}

	public ConnectServiceAsync(int searchType, String city, View loadingView, ConnectionListener listener) {
		this(searchType, loadingView, listener);
		this.city = city;
	}

	public ConnectServiceAsync(int searchType, String latitude, String longitude, View loadingView, ConnectionListener listener) {
		this(searchType, loadingView, listener);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		loadingView.setVisibility(View.VISIBLE);
	}

	@Override
	protected String doInBackground(Void... params) {

		String urlStr = SERVICE_URL + "?APPID=" + BETUCAID + "&mode=json&units=metric&lang=pt" + getParametersString();
		StringBuilder sb = new StringBuilder();

		try {

			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			int status = con.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				String error = "problem connecting... try again later.";
				System.out.println(error);
				return error;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			for (String line; (line = reader.readLine()) != null;) {
				sb.append(line);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	private String getParametersString() {
		String params = "";
		if ((searchType == ConfigActivity.CITY_TYPE) || (searchType == ConfigActivity.LIST_TYPE)) {
			params = "&q=" + city;
		} else {
			params = "&lat=" + latitude + "&lon=" + longitude;
		}
		return params;
	}

	@Override
	protected void onPostExecute(String result) {
		loadingView.setVisibility(View.GONE);
		listener.setJsonResponse(result);
	}

}
