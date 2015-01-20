package com.growthpush.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.growthpush.utils.PermissionUtils;
import com.growthpush.view.AlertActivity;

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

		if (context == null || intent == null || intent.getExtras() == null || checkFrontSelfApplication(context))
			return;

		Intent alertIntent = new Intent(context, AlertActivity.class);
		alertIntent.putExtras(intent.getExtras());
		alertIntent.putExtra("showDialog", true);
		alertIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);

		AlertActivity.setSharedCallback(callback);
		context.startActivity(alertIntent);

	}

	protected void addNotification(Context context, Intent intent) {

		if (context == null || intent == null || intent.getExtras() == null || checkFrontSelfApplication(context))
			return;

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify("GrowthPush" + context.getPackageName(), 1, generateNotification(context, intent.getExtras()));

	}

	@SuppressWarnings("deprecation")
	private boolean checkFrontSelfApplication(Context context) {

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
		String[] packages = new String[1];
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
			final Set<String> activePackages = new HashSet<String>();
			final List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
				if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					activePackages.addAll(Arrays.asList(processInfo.pkgList));
				}
			}
			packages = activePackages.toArray(new String[activePackages.size()]);
		} else {
			ActivityManager.RunningTaskInfo taskInfo = activityManager.getRunningTasks(1).get(0);
			packages[0] = taskInfo.topActivity.getPackageName();
		}

		if (packages == null || packages.length <= 0)
			return false;

		for (String packageName : packages) {
			if (context.getPackageName().equals(packageName))
				return true;
		}

		return false;

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
		boolean sound = false;
		if (extras.containsKey("sound"))
			sound = Boolean.valueOf(extras.getString("sound"));

		Intent intent = new Intent(context, AlertActivity.class);
		intent.putExtras(extras);
		intent.putExtra("showDialog", false);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		AlertActivity.setSharedCallback(callback);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setTicker(title);
		builder.setSmallIcon(icon);
		builder.setContentTitle(title);
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
