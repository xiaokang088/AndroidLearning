package com.example.shakersample;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity implements Shaker.Callback{

	public  final String TAG = "MainActivity";
	Shaker shaker ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		  shaker = new Shaker(getBaseContext(), 2.0d, 0, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause(){
		shaker.close();
	}
	
	@Override
	public void shakingStarted() {
		Log.i(TAG, "shakingStarted");
	}

	@Override
	public void shakingStopped() {
		Log.i(TAG, "shakingStopped");
	}

}
