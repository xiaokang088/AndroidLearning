package com.example.weatherreportassistor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String TAG = Keys.TAG;
	EditText et;
	final String defaultCityName = "º¼ÖÝ";
	String currentCityName = "";
	TextView tvShow;
	WeatherHelper helper = new WeatherHelper(this); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnGet = (Button) this.findViewById(R.id.btnGet);
		btnGet.setOnClickListener(this);

		et = (EditText) this.findViewById(R.id.etCity);
		tvShow = (TextView) this.findViewById(R.id.tvShow);

	
	}
 
	protected void onNewIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		int pullKey = bundle.getInt(Keys.PullKeyName);
		if (pullKey == Keys.PullKey) {
			getWeather();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View arg0) {	
		tvShow.setText("");
		getWeather();
	}

	boolean isRunning = false;
	
	void getWeather(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (isRunning){
					return ;
				}
					
				isRunning = true;
				currentCityName = et.getText().toString();
				
				if (currentCityName == null || currentCityName.length() <= 0){
					currentCityName = defaultCityName;
				}
				
				final String content = helper.GetWeather(currentCityName);
				
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						tvShow.setText(content);
					}
				});	
				isRunning = false;
			}
		}).start();
	}
	
	boolean checkWifiAvailable() {
		try {
			ConnectivityManager manager = (ConnectivityManager) this
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			return info.isConnected();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	
}
