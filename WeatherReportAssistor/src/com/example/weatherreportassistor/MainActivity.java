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

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements android.view.View.OnClickListener {

	String TAG = "WeatherReport";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnGet = (Button)this.findViewById(R.id.btnGet);
		btnGet.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View arg0) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				String content = getWeatherReport("º¼ÖÝ");
				Log.i(TAG, " " + content) ;
				parseXml(content);
			}
		}).start();
	}
		
	String getWeatherReport(String cityName){
		
		String weatherUrl = 
				"http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName?theCityName=%s";  
		String url = String.format(weatherUrl, cityName);
		HttpGet get = new HttpGet(url);
		
		HttpClient client = new DefaultHttpClient();
		String content = null;
		try {
			HttpResponse response =  client.execute(get);
			HttpEntity entity = response.getEntity();	 
			InputStream stream = entity.getContent();
			 
			if (stream!=null){			
				ByteArrayOutputStream outputStream = new  ByteArrayOutputStream();
				byte[] buf = new byte[120];
				int ch = -1;
				while ((ch = stream.read(buf)) != -1) {
					outputStream.write(buf, 0, ch);
				}
				content = new String(outputStream.toByteArray());
				return content;
			}			 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return content;
	}

	void parseXml(String content) {
 
		List<String> parms = new ArrayList<String>();
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(new StringReader(content));

			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name =parser.getName();
				Log.i(TAG, "name: " + name);	
				
				if (eventType == XmlPullParser.START_DOCUMENT) {
					Log.i(TAG, "START_DOCUMENT");	
					
				} else if (eventType == XmlPullParser.END_DOCUMENT) {
					Log.i(TAG, "END_DOCUMENT");	
					
				} else if (eventType == XmlPullParser.START_TAG) {
					String parseName = parser.getName();
					Log.i(TAG, "START_TAG  Name:" + parseName);
					int size = parser.getAttributeCount();
					for (int i = 0; i < size; i++) {
						String attributeName = parser.getAttributeName(i);
						String attributeValue = parser.getAttributeValue(i);

						Log.i(TAG, "AttributeName:" + attributeName);
						Log.i(TAG, "AttributeValue:" + attributeValue);
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					String parseName = parser.getName();
					Log.i(TAG, "END_TAG  Name:" + parseName);
				} else if (eventType == XmlPullParser.TEXT) {
					String parseText = parser.getText();
					parseText = parseText.trim();
					
					if(parseText.length() >0){
						parms.add(parseText);
					}
					
					Log.i(TAG, "Text:" + parseText);
				}

				eventType = parser.next();
				Log.i(TAG,"-----------------------------");
			}

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		for(String str: parms) {
			Log.i(TAG, "parm:" + str);
		}
	}
}
