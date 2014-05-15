package com.example.gasample;

import java.util.Map;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String trackID = "UA-50870695-1";
	private Tracker tracker;
	String CATEGORY = "Category";
	String ACTION_LOGIN = "Action!!!";
	String ACTION_PLAY = "Play!!!";
	String TAG = "GASample";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tracker = GoogleAnalytics.getInstance(this).getTracker(trackID);
		// Set the dispatch period in seconds.
		GAServiceManager.getInstance().setLocalDispatchPeriod(5);
		Button btnAction = (Button) this.findViewById(R.id.btnAction);
		btnAction.setOnClickListener(this);
		Button btnPlay = (Button) this.findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	
	@Override
	public void onStop(){
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	String[] colors = new String[] { "Red", "Blue", "Black", "White", "Yellow" };

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.btnAction) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sendGAData();
				}
			}).start();
		}
		
		if (id == R.id.btnPlay) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sendPlayGAData();
				}
			}).start();
		}
	}

	void sendGAData() {
		double random = Math.random();
		int index = (int) (10 * random) / 2;

		String color = colors[index];
		String value = index + "";
		Map<String, String> parms = MapBuilder.createEvent(CATEGORY,
				ACTION_LOGIN, "Label",Long.MIN_VALUE).build();
		parms.put(Fields.customDimension(1), "Red");
		parms.put(Fields.customMetric(1), value);
		Log.i(TAG, "Color:" + color + "		Value:" + value);
		tracker.send(parms);
	}

	void sendPlayGAData() {
		double random = Math.random();
		int index = (int) (10 * random) / 2;

		String color = colors[index];
		String value = index + "";
		Map<String, String> parms = MapBuilder.createEvent(CATEGORY,
				ACTION_PLAY, "Label",Long.MAX_VALUE).build();
		parms.put(Fields.customDimension(1), "Red");
		parms.put(Fields.customMetric(1), value);
		
		parms.put(Fields.SCREEN_NAME, value);
		
		Log.i(TAG, "Color:" + color + "		Value:" + value);
		tracker.send(parms);
	}
}
