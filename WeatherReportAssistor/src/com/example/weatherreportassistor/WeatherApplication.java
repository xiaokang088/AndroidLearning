package com.example.weatherreportassistor;

import android.app.Application;

public class WeatherApplication extends Application {

	protected static WeatherApplication mInstance; 
	
	String currentCity = "º¼ÖÝ";
	
	
	public WeatherApplication() {

	}

	public static WeatherApplication getInstance(){
		return (WeatherApplication)mInstance;
	}
	
	public String getCurrentCity(){
		return currentCity;
	}
	
	@Override
	public void onCreate() {
		PullReceiver.RegesterReceiver(this);
	}
}
