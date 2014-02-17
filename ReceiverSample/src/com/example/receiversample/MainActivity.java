package com.example.receiversample;

import java.util.Calendar;

import com.example.receiversample.R.id;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

	final long			MS_IN_DAY					= 86400000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		setClickListener(R.id.btnNotification);
		setClickListener(R.id.btnRegisterReceiver);
		setClickListener(R.id.btnUnRegister);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	void setClickListener(int btnID){
		Button btn = (Button)this.findViewById(btnID);
		btn.setOnClickListener(this);
	}
	
	
	public void onClick(View view){
		int viewID = view.getId();
		
		switch(viewID){
		case R.id.btnRegisterReceiver:
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			ReminderReceiver.registerReminder(this.getApplicationContext(), cal, MS_IN_DAY);
			break;
		case R.id.btnUnRegister:
			ReminderReceiver.unRegisterReminder(this);
			break;
		case R.id.btnNotification:
			NotificationHelper.showNotification(this, "tickertext", "title", "content");
			break;
		
		}
	}

}
