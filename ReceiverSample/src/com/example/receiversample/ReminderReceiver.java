package com.example.receiversample;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {

	static String TAG = "ReminderReceiver";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat();		
		Toast toast = Toast.makeText(arg0, "now again: " + format.format(date), Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void registerReminder(Context context, Calendar cal,
			long interval) {
		Intent intent = new Intent(context, ReminderReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(
				context.getApplicationContext(), 0, intent, 0);
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				interval, pi);
	}

	public static void unRegisterReminder(Context context) {
		Log.i(TAG, "unRegisterReminder: ");
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, ReminderReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		manager.cancel(pi);
	}

}
