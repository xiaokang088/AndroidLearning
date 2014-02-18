package com.example.weatherreportassistor;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class PullReceiver extends BroadcastReceiver {

	String TAG = Keys.TAG;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 Log.i(TAG, "OnReceiver");
		 Intent newIntent = new Intent(context,MainActivity.class);
		 newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 newIntent.putExtra(Keys.PullKeyName, Keys.PullKey);
		 //context.startActivity(newIntent);
	}
 
	static final long internal_one_minutes = 1  * 60 * 1000;
	
	public static void RegesterReceiver(Context context){		
		Intent intent = new Intent(context,PullReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);		
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), internal_one_minutes, pendingIntent);
	}
	
	public static void UnRegisterReceiver(Context context){
		Intent intent = new Intent(context,PullReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pendingIntent);
	}
}
