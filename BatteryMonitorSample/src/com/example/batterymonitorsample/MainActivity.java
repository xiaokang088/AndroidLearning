package com.example.batterymonitorsample;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		registerBroadCast();
				

		int brightness = android.provider.Settings.System.getInt(getContentResolver(), 
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                255/2);
		Log.i("brightness",Integer.toString(brightness)) ;
		int brightnessMode = android.provider.Settings.System.getInt(getContentResolver(), 
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		Log.i("brightnessMode", Integer.toString(brightnessMode)) ;
	
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void registerBroadCast() {
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		mIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
		mIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
		this.registerReceiver(receiver, mIntentFilter);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String prompt = null;
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				int status = intent.getIntExtra("status", 0);
				int level = intent.getIntExtra("level", 0);
				prompt = String.format(
						"ACTION_BATTERY_CHANGED status:%s level:%s",
						Integer.toString(status), Integer.toString(level));
			} else if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
				int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,
						-1);
				boolean isUSB = plugged == BatteryManager.BATTERY_PLUGGED_USB;
				prompt = String.format("ACTION_POWER_CONNECTED status:%s  ",
						isUSB);
			} else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
				int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,
						-1);
				boolean isUSB = plugged == BatteryManager.BATTERY_PLUGGED_USB;
				prompt = String.format("ACTION_POWER_CONNECTED status:%s  ",
						isUSB);
			} else {
				prompt = "nothing";
			}
				
			Toast toast = Toast.makeText(context,prompt, Toast.LENGTH_LONG);
			toast.show();
		}
	};

}
