package com.bts.weatherutil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final int MY_REQUEST_CODE = 1;
	private TextView textConfig;
	private TextView textCity;
	private TextView textCountry;
	private TextView textTemp;
	private TextView textIcon;
	private TextView textDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textConfig = (TextView) findViewById(R.id.textConfig);
		textConfig.setOnClickListener(this);
		textCity = (TextView) findViewById(R.id.textCity);
		textCountry = (TextView) findViewById(R.id.textCountry);
		textTemp = (TextView) findViewById(R.id.textTemp);
		textIcon = (TextView) findViewById(R.id.textIcon);
		textDescription = (TextView) findViewById(R.id.textDescription);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textConfig:

			Intent configIntent = new Intent(this, ConfigActivity.class);
			startActivityForResult(configIntent, MY_REQUEST_CODE);

			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				CityData cityData = (CityData) data.getSerializableExtra(ConfigActivity.CITY_DATA);
				textCity.setText(cityData.getName());
				textCountry.setText(cityData.getCountry());
				textTemp.setText(cityData.getTemp());
				textIcon.setText(cityData.getIcon());
				textDescription.setText(cityData.getDescription());
			}
		}
	}

}
