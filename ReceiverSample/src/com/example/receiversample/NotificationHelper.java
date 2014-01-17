package com.example.receiversample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationHelper {
	
	//http://www.cnblogs.com/newcj/archive/2011/03/14/1983782.html
	
	public static void showNotification(Context context, String tickertext,
			String title, String content) {

		try {
			NotificationManager manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Notification notification = new Notification(
					R.drawable.ic_launcher, tickertext,
					System.currentTimeMillis());
			
			notification.flags = Notification.FLAG_AUTO_CANCEL;
/*
			���ʹ�Լ���Notification��Android QQһ���ܳ����� ���������еġ���Ŀ����

			��ʵ�ܼ򵥣�ֻ������Notification.flags = Notification.FLAG_ONGOING_EVENT;������ˡ�*/
			
			Intent i = new Intent(context, NotificationHelper.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);

			PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name,
					i, PendingIntent.FLAG_UPDATE_CURRENT);

		 	notification.setLatestEventInfo(
					context,
					title, 
					content, 
			        contentIntent); 
			
			manager.notify(R.string.app_name, notification);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
