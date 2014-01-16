package com.example.receiversample;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub

		Toast toast = Toast.makeText(arg0, "now again", Toast.LENGTH_SHORT);
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
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, ReminderReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		manager.cancel(pi);
	}

	public static void showNotification(Context context, int icon,
			String tickertext, String title, String content, int id,
			int defaults, Intent intent) {
		Notification notification = new Notification(icon, tickertext,
				System.currentTimeMillis());
		notification.defaults = defaults;
		PendingIntent pt = PendingIntent.getActivity(context, 0, intent, 0);
		notification.setLatestEventInfo(context, title, content, pt);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		((NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE)).notify(id,
				notification);
	}
	
	 

}
