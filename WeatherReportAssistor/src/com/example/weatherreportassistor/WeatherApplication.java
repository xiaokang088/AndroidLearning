package com.example.weatherreportassistor;

import android.app.Application;

public class WeatherApplication extends Application {

	public WeatherApplication() {

	}

	@Override
	public void onCreate() {
		PullReceiver.RegesterReceiver(this);
	}
}
