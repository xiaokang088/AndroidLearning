package com.example.configchangesample;

 
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView textView1;
	String TAG = "configchangesample";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textView1 = (TextView)this.findViewById(com.example.configchangesample.R.id.textView1);
		textView1.setText("init");
		Log.i(TAG, "onCreate");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		textView1.setText("onConfigurationChanged");
		Log.i(TAG, "onConfigurationChanged:" + newConfig.orientation);
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
			textView1.setText("ORIENTATION_LANDSCAPE");
		} else {
			 
			textView1.setText("ORIENTATION_PORTRAIT");
		}
		
		 
		
	}
}
