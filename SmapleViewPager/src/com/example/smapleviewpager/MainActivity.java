package com.example.smapleviewpager;

 

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
 

public class MainActivity extends Activity implements android.view.View.OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initButton(R.id.btnCloud);
		initButton(R.id.btnWifi);		
	}

	void initButton(int btnID){
		Button btn = (Button)this.findViewById(btnID);
		btn.setOnClickListener(this);
	}
	
	 
	int TourFromCloudKey = 101;
	int TourFromWifiKey = 102;
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if (id == R.id.btnCloud){
			Intent intent = new Intent(this,TourActivity.class);
			intent.putExtra("backupType",1);
			this.startActivityForResult(intent, TourFromCloudKey);
		}
		
		if (id == R.id.btnWifi){
			Intent intent = new Intent(this,TourActivity.class);
			intent.putExtra("backupType",2);
			this.startActivityForResult(intent, TourFromWifiKey);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == TourFromCloudKey){
			Log.i("tour", "back: " + resultCode);
		}
		
		if(requestCode == TourFromWifiKey){
			Log.i("tour", "back: " + resultCode);
		}
    }

 
}
