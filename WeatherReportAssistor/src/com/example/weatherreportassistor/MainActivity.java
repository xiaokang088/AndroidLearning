package com.example.weatherreportassistor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener, OnInitListener {

	String TAG = Keys.TAG;
	EditText et;
	String currentCityName = "";
	TextView tvShow;
	ImageView imgView;
	WeatherApplication application;

	boolean isRunning = false;
	public int MY_DATA_CHECK_CODE = 0;
	WeatherHelper helper = new WeatherHelper(this);
	Button btnGet;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnGet = (Button) this.findViewById(R.id.btnGet);
		btnGet.setOnClickListener(this);

		et = (EditText) this.findViewById(R.id.etCity);
		tvShow = (TextView) this.findViewById(R.id.tvShow);
		imgView = (ImageView)this.findViewById(R.id.imgView);
		
		// check for TTS data
	/*	Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);*/
		
		application = WeatherApplication.getInstance();
		String cityName = application.GetCity(this);
		et.setText(cityName);
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
		currentCityName = et.getText().toString();
		if (currentCityName.length() == 0){
			tvShow.setText(Keys.PleaseInputCity);
			return ;
		}
		 
		application.SetCity(currentCityName,this);
		
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   
		
		getWeather();
	}

	/**
	 * Handle the results from the recognition activity.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// the user has the necessary data - create the TTS
				myTTS = new TextToSpeech(this, this);
			} else {
				// no data - install it now
				Intent installTTSIntent = new Intent();
				installTTSIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		myTTS.shutdown();
	}

	void getWeather() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (isRunning) {
					return;
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tvShow.setText(Keys.IsQuery);
					}
				});
				
				isRunning = true;

				final String content = helper.GetWeather(currentCityName);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tvShow.setText(content);
						//imgView.setImageResource(R.drawable.w3);
						//myTTS.speak(content, TextToSpeech.QUEUE_FLUSH, null);
					}
				});
				isRunning = false;
			}
		}).start();
	}

	public TextToSpeech myTTS;

	@Override
	public void onInit(int initStatus) {
		// TODO Auto-generated method stub
		// check for successful instantiation
		if (initStatus == TextToSpeech.SUCCESS) {
			/*
			 * if (myTTS.isLanguageAvailable(Locale.US) ==
			 * TextToSpeech.LANG_AVAILABLE) myTTS.setLanguage(Locale.US);
			 */
			if (myTTS.isLanguageAvailable(Locale.CHINA) == TextToSpeech.LANG_AVAILABLE)
				myTTS.setLanguage(Locale.CHINA);

		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
	}

}
