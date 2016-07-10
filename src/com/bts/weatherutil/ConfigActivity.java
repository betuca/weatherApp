package com.bts.weatherutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigActivity extends Activity implements OnClickListener, ConnectionListener {

	public static final String CITY_DATA = "cityData";
	private TextView textCity;
	private TextView textGeoposition;
	private TextView textCityList;
	private TextView textConfirm;
	private EditText editCity;
	private View loadingPanel;
	private View cityPanel, geoPanel, listPanel;
	private Spinner citiesSpinner;

	public static int CITY_TYPE = 0;
	public static int GEO_TYPE = 1;
	public static int LIST_TYPE = 2;
	private int currentType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);
		textCity = (TextView) findViewById(R.id.textCity);
		textCity.setOnClickListener(this);

		textGeoposition = (TextView) findViewById(R.id.textGeoposition);
		textGeoposition.setOnClickListener(this);

		textCityList = (TextView) findViewById(R.id.textCityList);
		textCityList.setOnClickListener(this);

		textConfirm = (TextView) findViewById(R.id.textConfirm);
		textConfirm.setOnClickListener(this);

		editCity = (EditText) findViewById(R.id.editCity);

		loadingPanel = (View) findViewById(R.id.loadingPanel);

		cityPanel = (View) findViewById(R.id.cityPanel);
		cityPanel.setVisibility(View.VISIBLE);
		currentType = CITY_TYPE;

		geoPanel = (View) findViewById(R.id.geoPanel);
		geoPanel.setVisibility(View.GONE);

		listPanel = (View) findViewById(R.id.listPanel);
		listPanel.setVisibility(View.GONE);

		citiesSpinner = (Spinner) findViewById(R.id.citiesSpinner);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.textConfirm:

			executeSearch();

			break;

		case R.id.textCity:
			currentType = CITY_TYPE;
			cityPanel.setVisibility(View.VISIBLE);
			geoPanel.setVisibility(View.GONE);
			listPanel.setVisibility(View.GONE);
			break;

		case R.id.textGeoposition:
			currentType = GEO_TYPE;
			cityPanel.setVisibility(View.GONE);
			geoPanel.setVisibility(View.VISIBLE);
			listPanel.setVisibility(View.GONE);
			break;

		case R.id.textCityList:
			currentType = LIST_TYPE;
			cityPanel.setVisibility(View.GONE);
			geoPanel.setVisibility(View.GONE);
			listPanel.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}

	}

	private void executeSearch() {

		if (currentType == CITY_TYPE) {

			String city = editCity.getText().toString();
			if (city.trim() != "") {

				ConnectServiceAsync service = new ConnectServiceAsync(CITY_TYPE, city, loadingPanel, this);
				service.execute();
			} else {
				Toast.makeText(this, "Type a city name", Toast.LENGTH_SHORT).show();
			}
		} else if (currentType == GEO_TYPE) {

		} else if (currentType == LIST_TYPE) {

			ConnectServiceAsync service = new ConnectServiceAsync(LIST_TYPE, String.valueOf(citiesSpinner.getSelectedItem()), loadingPanel, this);
			service.execute();

		}
	}

	@Override
	public void setJsonResponse(String jsonResponse) {

		CityData cityData = new CityData();
		try {

			JSONObject jsonObject = new JSONObject(jsonResponse);
			cityData.setCountry(jsonObject.getJSONObject("sys").getString("country"));
			cityData.setName(jsonObject.getString("name"));
			cityData.setTemp(jsonObject.getJSONObject("main").getString("temp"));
			JSONArray weatherArray = jsonObject.getJSONArray("weather");
			cityData.setDescription(weatherArray.getJSONObject(0).getString("description"));
			cityData.setIcon(weatherArray.getJSONObject(0).getString("icon"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		Intent returnIntent = new Intent();
		returnIntent.putExtra(CITY_DATA, cityData);
		setResult(RESULT_OK, returnIntent);
		finish();

	}
}
