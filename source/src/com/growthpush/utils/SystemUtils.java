package com.growthpush.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.os.PowerManager;

public class SystemUtils {

	public static PowerManager getPowerManager(Context context) {

		try {
			return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		} catch (SecurityException e) {
		} catch (ClassCastException e) {
		}

		return null;

	}

	public static ActivityManager getActivityManager(Context context) {

		try {
			return (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
		} catch (SecurityException e) {
		} catch (ClassCastException e) {
		}

		return null;

	}

}
