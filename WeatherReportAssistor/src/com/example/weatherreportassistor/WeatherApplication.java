package com.example.weatherreportassistor;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class WeatherApplication extends Application {

	protected static WeatherApplication mInstance; 
	
	String defaultCity = "º¼ÖÝ";

	public WeatherApplication() {

	}

	public static WeatherApplication getInstance(){
		if (mInstance == null){
			mInstance = new WeatherApplication();
		}		
		return mInstance;
	}

	@Override
	public void onCreate() {
		//PullReceiver.RegesterReceiver(this);
	}
	
	public String GetCity(Context context) {
		try {
			SharedPreferences prefs = context.getSharedPreferences(Keys.Pref_App,
					Context.MODE_PRIVATE);
			String cityName = prefs.getString(Keys.Pref_CityName, "");
			if (cityName.length() == 0)
				cityName = defaultCity;
			return cityName;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	public void SetCity(String cityName,Context context){	
		SharedPreferences prefs = context.getSharedPreferences(Keys.Pref_App ,
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(Keys.Pref_CityName, cityName);
		editor.commit();
	}
}
