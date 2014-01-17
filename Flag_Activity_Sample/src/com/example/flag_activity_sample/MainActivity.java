package com.example.flag_activity_sample;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


//http://www.eoeandroid.com/thread-313933-1-1.html
public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	@Override
	public void onClick(View v) {
		try {
			// TODO Auto-generated method stub
			Intent newPageIntent = new Intent(this, PageA.class);
			this.startActivity(newPageIntent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void setClickListener(int btnID) {
		Button btn = (Button) this.findViewById(btnID);
		btn.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setClickListener(R.id.button1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
