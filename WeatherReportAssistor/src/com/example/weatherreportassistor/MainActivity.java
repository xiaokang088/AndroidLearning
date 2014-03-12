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
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener, OnInitListener {

	String TAG = Keys.TAG;
	EditText et;
	final String defaultCityName = "º¼ÖÝ";
	String currentCityName = "";
	TextView tvShow;
	ImageView imgView;
	WeatherApplication application;
	
	WeatherHelper helper = new WeatherHelper(this);
	public int MY_DATA_CHECK_CODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnGet = (Button) this.findViewById(R.id.btnGet);
		btnGet.setOnClickListener(this);

		et = (EditText) this.findViewById(R.id.etCity);
		tvShow = (TextView) this.findViewById(R.id.tvShow);
		imgView = (ImageView)this.findViewById(R.id.imgView);
		
		// check for TTS data
	/*	Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);*/
		
		application = WeatherApplication.getInstance();
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
 
		currentCityName = et.getText().toString();

		this.getApplication();
		
		getWeather();
	}

	void test() {
		
		File SDCardFile = Environment.getExternalStorageDirectory();
		File sourceFile = new File(SDCardFile + "/a.txt");
		
		try {
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[1024];
			int len = -1;
			
			try {
				len = fileInputStream.read(buffer);
				
				while(len!=-1){
					stream.write(buffer,0,len);
					len = fileInputStream.read(buffer);
				}
				String str = new String(stream.toByteArray());
				int length = str.length();
				
				XmlPullParserFactory factory;
				try {
					factory = XmlPullParserFactory.newInstance();
					factory.setNamespaceAware(true);
					
					XmlPullParser parse = factory.newPullParser();
					parse.setInput(new StringReader(str));
					int eventType =  parse.getEventType();
					while( eventType != XmlPullParser.START_DOCUMENT){
						 eventType = parse.next();
					}
					
					
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
		/*String url = "http://www.baidu.com";
		HttpGet get = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();

			//ByteArrayOutputStream outStream = new ByteArrayOutputStream();

			File sdCard = Environment.getExternalStorageDirectory();
			File targetFile = new File(sdCard.getAbsoluteFile() + "/a.txt");
			
			FileOutputStream outStream = new FileOutputStream(targetFile);
			byte[] buffer = new byte[512];
			int len = -1;
			len = inputStream.read(buffer);
			while (len != -1) {
				outStream.write(buffer, 0, len);
				len = inputStream.read(buffer);
			}
			inputStream.close();
			outStream.close();
			
			String str = new String(outStream.toByteArray());
			int length=  str.length();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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

	boolean isRunning = false;

	void getWeather() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (isRunning) {
					return;
				}

				isRunning = true;
				currentCityName = et.getText().toString();

				if (currentCityName == null || currentCityName.length() <= 0) {
					//currentCityName = defaultCityName;
					currentCityName = application.getCurrentCity();
				}

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
