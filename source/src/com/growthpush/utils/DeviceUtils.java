package com.growthpush.utils;

import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class DeviceUtils {

	public static String getDevice() {
		return Build.MODEL;
	}

	public static String getOs() {
		return "Android " + Build.VERSION.RELEASE;
	}

	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

	public static String getTimeZone() {
		return TimeZone.getDefault().getID();
	}

	public static String getVersion(Context context) {

		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			return null;
		}

	}

	public static String getBuild(Context context) {

		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return String.valueOf(packageInfo.versionCode);
		} catch (NameNotFoundException e) {
			return null;
		}

	}
}
