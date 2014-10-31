package com.example.servicesample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service{

	String tag = "MyService";
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
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
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
