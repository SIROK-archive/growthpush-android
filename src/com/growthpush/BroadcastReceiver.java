package com.growthpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.growthpush.view.AlertActivity;

/**
 * Created by Shigeru Ogawa on 13/08/09.
 */
public class BroadcastReceiver extends android.content.BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(context, intent);
		} else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			handleReceive(context, intent);
		}

	}

	private void handleRegistration(Context context, Intent intent) {

		for (String key : intent.getExtras().keySet()) {
			if (key.equals("registration_id")) {
				GrowthPush.getInstance().registerClient(intent.getExtras().getString(key));
				return;
			}
		}

	}

	private void handleReceive(Context context, Intent intent) {

		String messageType = GoogleCloudMessaging.getInstance(context).getMessageType(intent);
		if (messageType != null && messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE)) {
			showAlert(context, intent);
			addNotification(context, intent);
		}

	}

	private void showAlert(Context context, Intent intent) {

		if (context == null || intent == null || intent.getExtras() == null)
			return;

		Intent alertIntent = new Intent(context, AlertActivity.class);
		alertIntent.putExtra("message", intent.getExtras().getString("message"));
		alertIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alertIntent);

	}

	private void addNotification(Context context, Intent intent) {

		if (context == null || intent == null || intent.getExtras() == null)
			return;

		PackageManager packageManager = context.getPackageManager();

		int icon = 0;
		String title = "";
		try {
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
			icon = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).icon;
			title = packageManager.getApplicationLabel(applicationInfo).toString();
		} catch (NameNotFoundException e) {
		}

		String message = intent.getExtras().getString("message");
		boolean sound = intent.getExtras().getBoolean("sound", false);

		Intent notificationIntent = packageManager.getLaunchIntentForPackage(context.getPackageName());
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setTicker(title);
		builder.setSmallIcon(icon);
		builder.setContentTitle(title);
		builder.setContentText(message);
		builder.setContentIntent(pendingIntent);
		builder.setWhen(System.currentTimeMillis());
		builder.setAutoCancel(true);
		if (sound) {
			builder.setDefaults(Notification.DEFAULT_SOUND);
			try {
				builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
			} catch (SecurityException e) {
			}
		}

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(null, 1, builder.build());

	}
}
