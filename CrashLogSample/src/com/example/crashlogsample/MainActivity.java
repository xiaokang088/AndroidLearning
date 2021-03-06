package com.example.crashlogsample;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

	String TAG = "AccountType";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnSample= (Button)this.findViewById(R.id.btnSample);
		btnSample.setOnClickListener(this); 
		
		ErrorReporter errReporter = new ErrorReporter();
		errReporter.Init(this);
		errReporter.CheckErrorAndSendMail(this);
		
		 
	 
		
	}

	private boolean isBindGmail() {
		try {
			AccountManager manager = AccountManager.get(this);
			Account[] accounts = manager.getAccountsByType("com.google");

			if (accounts.length > 0) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public void onClick(View arg0){	
		String abc= null;
		byte[] dd = abc.getBytes();
				
	}
}
