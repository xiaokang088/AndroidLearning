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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String TAG = "WeatherReport";
	EditText et;
	final String defaultCityName = "杭州";
	String currentCityName = "";
	TextView tvShow;
	String weatherUrl = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName?theCityName=%s";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnGet = (Button) this.findViewById(R.id.btnGet);
		btnGet.setOnClickListener(this);

		et = (EditText) this.findViewById(R.id.etCity);
		tvShow = (TextView) this.findViewById(R.id.tvShow);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View arg0) {	
		tvShow.setText("");
		new Thread(new Runnable() {
			@Override
			public void run() {
				currentCityName = et.getText().toString();
				if (currentCityName == null || currentCityName.length() <= 0)
					currentCityName = defaultCityName;
				String content = getXml(currentCityName);
				Log.i(TAG, " " + content);
				String[] parms = parseXml(content);
				getData(parms);
			}
		}).start();
	}

	String getXml(String cityName) {

		String url = String.format(weatherUrl, cityName);
		HttpGet get = new HttpGet(url);

		HttpClient client = new DefaultHttpClient();
		String content = null;
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();

			if (stream != null) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	String[] parseXml(String content) {

		String[] parms = new String[30];

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(new StringReader(content));

			int eventType = parser.getEventType();
			int j = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
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

					if (parseText.length() > 0) {
						parms[j] = parseText;
						j++;
					}

					Log.i(TAG, "Text:" + parseText);
				}

				eventType = parser.next();
				Log.i(TAG, "-----------------------------");
			}

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String str : parms) {
			Log.i(TAG, "parm:" + str);
		}

		return parms;
	}

	void getData(String[] parms) {
		/*
		 * 返回数据： 一个一维数组 String(22)，共有23个元素。 String(0) 到
		 * String(4)：省份，城市，城市代码，城市图片名称，最后更新时间。 String(5) 到 String(11)：当天的
		 * 气温，概况，风向和风力，天气趋势开始图片名称(以下称：图标一)，天气趋势结束图片名称(以下称：图标二)，现在的天气实况，天气和生活指数。
		 * String(12) 到 String(16)：第二天的 气温，概况，风向和风力，图标一，图标二。 String(17) 到
		 * String(21)：第三天的 气温，概况，风向和风力，图标一，图标二。 String(22) 被查询的城市或地区的介绍
		 */
		String province = parms[0];
		String city = parms[1];
		String cityCode = parms[2];
		String cityImageName = parms[3];
		String lastUpdateDate = parms[4];

		String temperature = parms[5];
		String summary = parms[6];
		String wind = parms[7];
		String futureStartIcon = parms[8];
		String futureEndIcon = parms[8];
		String realtimeSummary = parms[10];
		String livingIndex = parms[11];
		

		String secondTemperature = parms[12];
		String secondSummary = parms[13];
		String secondWind = parms[14];
		String secondFutureStartIcon = parms[15];
		String secondFutureEndIcon = parms[16];

		String thirdTemperature = parms[17];
		String thirdSummary = parms[18];
		String thirdWind = parms[19];
		String thirdFutureStartIcon = parms[20];
		String thirdFutureEndIcon = parms[21];

		String cityDescrption = parms[22];

		StringBuilder builder = new StringBuilder();
		builder.append("City:%s\r\n temperature:%s\r\n summary:%s\r\n wind:%s\r\n realTime:%s\r\n ");
		builder.append("\r\n---------------\r\n");
		builder.append("tomorrow Temperature:%s\r\n summary:%s\r\n wind: %s\r\n ");
		builder.append("\r\n---------------\r\n");
		builder.append("the day after tomorrow  Temperature:%s\r\n summary:%s\r\n wind: %s\r\n ");
		builder.append("\r\n---------------\r\n");
		final String content = String
				.format(builder.toString(),
						city, temperature, summary, wind, realtimeSummary,
						secondTemperature,secondSummary,secondWind,
						thirdTemperature,thirdSummary,thirdWind);

		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tvShow.setText(content);
			}
		});
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
