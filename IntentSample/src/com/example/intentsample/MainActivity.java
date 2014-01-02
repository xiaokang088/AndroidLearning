package com.example.intentsample;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String Tag = MainActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnIntent = (Button) this.findViewById(R.id.btnIntent);
		btnIntent.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View arg0) {
		Intent showIntent = new Intent(this, secondActivity.class);
		showIntent.putExtra("TestTime", "111111");
		//this.startActivity(showIntent);
		this.startActivityForResult(showIntent, 100);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(Tag, String.format("requestCode:%s resultCode:%s", requestCode,
				resultCode));

		Bundle bundle = data.getExtras();
		String number = bundle.getString("number");
		Log.i(Tag, number);
	}
}
