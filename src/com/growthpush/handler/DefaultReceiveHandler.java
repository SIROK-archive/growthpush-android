package com.growthpush.handler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.growthpush.view.AlertActivity;

/**
 * 2013/08/23
 * 
 * @author Ogawa Shigeru
 * 
 */
public class DefaultReceiveHandler implements ReceiveHandler {

	@Override
	public void onReceive(Context context, Intent intent) {

		showAlert(context, intent);
		addNotification(context, intent);

	}

	public void showAlert(Context context, Intent intent) {

		if (context == null || intent == null || intent.getExtras() == null)
			return;

		Intent alertIntent = new Intent(context, AlertActivity.class);
		alertIntent.putExtra("message", intent.getExtras().getString("message"));
		alertIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alertIntent);

	}

	public void addNotification(Context context, Intent intent) {

		if (context == null || intent == null || intent.getExtras() == null)
			return;

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify("GrowthPush" + context.getPackageName(), 1, generateNotification(context, intent.getExtras()));

	}

	private Notification generateNotification(Context context, Bundle extras) {
		PackageManager packageManager = context.getPackageManager();

		int icon = 0;
		String title = "";
		try {
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
			icon = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).icon;
			title = packageManager.getApplicationLabel(applicationInfo).toString();
		} catch (NameNotFoundException e) {
		}

		String message = extras.getString("message");
		boolean sound = extras.getBoolean("sound", false);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				packageManager.getLaunchIntentForPackage(context.getPackageName()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
				PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setTicker(title).setSmallIcon(icon)
				.setContentTitle(title).setContentText(message).setContentIntent(pendingIntent).setWhen(System.currentTimeMillis())
				.setAutoCancel(true);
		if (sound) {
			builder.setDefaults(Notification.DEFAULT_SOUND);
			try {
				builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
			} catch (SecurityException e) {
			}
		}

		return builder.build();

	}
}
