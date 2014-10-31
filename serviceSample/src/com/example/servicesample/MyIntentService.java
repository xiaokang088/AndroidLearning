package com.example.servicesample;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService{

	String tag = "MyIntentService";
	
	public MyIntentService(){
		super("MyIntentService");
	}
	
	public MyIntentService(String name) {
		super(name);
		Log.i(tag, "name:"+name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		long endTime = System.currentTimeMillis() + 5*1000;
		Log.i(tag, "on Start");
		while(System.currentTimeMillis() < endTime){
			
			synchronized(this){
			
					try {
						wait(endTime - System.currentTimeMillis());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
		}
		Log.i(tag, "on stop");
		
	}

}
