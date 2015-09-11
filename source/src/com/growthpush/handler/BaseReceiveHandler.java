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

import com.growthpush.GrowthPush;
import com.growthpush.utils.PermissionUtils;
import com.growthpush.view.AlertActivity;
import com.growthpush.view.DialogType;

public class BaseReceiveHandler implements ReceiveHandler {

	private Callback callback = new Callback();

	public BaseReceiveHandler() {
		super();
	}

	public BaseReceiveHandler(BaseReceiveHandler.Callback callback) {
		this();
		setCallback(callback);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
	}

	protected void showAlert(Context context, Intent intent) {

		if (context == null || intent == null || intent.getExtras() == null)
			return;

		if (!intent.getExtras().containsKey("message") && !intent.getExtras().containsKey("dialogType"))
			return;

		if (intent.getExtras().containsKey("message")) {
			String message = intent.getExtras().getString("message");
			if (message == null || message.length() <= 0 || message.equals(""))
				return;
		}

		DialogType dialogType = DialogType.none;
		if (intent.getExtras().containsKey("dialogType")) {
			try {
				dialogType = DialogType.valueOf(intent.getExtras().getString("dialogType"));
			} catch (IllegalArgumentException e) {
			} catch (NullPointerException e) {
			}
		}

		if (dialogType == DialogType.none)
			return;

		Intent alertIntent = new Intent(context, AlertActivity.class);
		alertIntent.putExtras(intent.getExtras());
		alertIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(alertIntent);
	}

	protected void addNotification(Context context, Intent intent) {

		if (context == null || intent == null || intent.getExtras() == null)
			return;

		String message = intent.getExtras().getString("message");
		if (message == null || message.length() == 0 || message.equals(""))
			return;

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify("GrowthPush" + context.getPackageName(), 1, generateNotification(context, intent.getExtras()));

	}

	private Notification generateNotification(Context context, Bundle extras) {
		PackageManager packageManager = context.getPackageManager();

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		try {
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			
			int icon = packageManager.getApplicationInfo(context.getPackageName(), 0).icon;
			if (applicationInfo.metaData != null && applicationInfo.metaData.containsKey(GrowthPush.NOTIFICATION_ICON_META_KEY))
				icon = Integer.valueOf(applicationInfo.metaData.getInt(GrowthPush.NOTIFICATION_ICON_META_KEY));
			String title = packageManager.getApplicationLabel(applicationInfo).toString();
			
			builder.setTicker(title);
			builder.setSmallIcon(icon);
			builder.setContentTitle(title);
			if (applicationInfo.metaData != null
					&& applicationInfo.metaData.containsKey(GrowthPush.NOTIFICATION_ICON_BACKGROUND_COLOR_META_KEY))
				builder.setColor(Integer.valueOf(applicationInfo.metaData.getInt(GrowthPush.NOTIFICATION_ICON_BACKGROUND_COLOR_META_KEY)));

		} catch (NameNotFoundException e) {
		}

		String message = extras.getString("message");
		boolean sound = false;
		if (extras.containsKey("sound"))
			sound = Boolean.valueOf(extras.getString("sound"));

		Intent intent = new Intent(context, AlertActivity.class);
		intent.putExtras(extras);
		intent.putExtra("showDialog", false);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		builder.setContentText(message);
		builder.setContentIntent(pendingIntent);
		builder.setWhen(System.currentTimeMillis());
		builder.setAutoCancel(true);

		if (sound && PermissionUtils.permitted(context, "android.permission.VIBRATE"))
			builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

		return builder.build();
	}

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public static class Callback {

		public void onOpen(Context context, Intent intent) {
			context.startActivity(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName())
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}

	}

}
