package com.example.intentsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class secondActivity extends Activity implements android.view.View.OnClickListener{
	
	String TAG = secondActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		
		Log.i(TAG, "onCreate");
		
		this.setContentView(R.layout.secondactivity);
		
		Intent sourceIntent =  this.getIntent();
		Bundle bundle = sourceIntent.getExtras();
		
		String result = bundle.getString("TestTime");
		Log.i(TAG, "testTime:" + result);
		
		Button  btn = (Button)this.findViewById(R.id.btnSetResult);
		btn.setOnClickListener(this);
	}

	@Override
	public void onStart(){
		super.onStart();
		Log.i(TAG, "onStart");
	}
	
	@Override
	public void onStop(){
		super.onStop();
		Log.i(TAG, "onStop");
	}
	
	@Override
	public void onResume(){
		Log.i(TAG, "onResume");
		super.onResume();
	}
	
	@Override
	public void onNewIntent(Intent intent){
		Log.i(TAG, "onNewIntent");
		super.onResume();
	}

	
	public void onClick(View arg0){
		Bundle bundle = new Bundle();
		bundle.putString("number", "110");
		Intent resultIntent = new Intent();
		resultIntent.putExtras(bundle);
		this.setResult(100,resultIntent);
		finish();
	}
}
