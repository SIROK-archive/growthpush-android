package com.growthpush;

import android.util.Log;

public class Logger {

	private static final String TAG = "GrowthPush";

	private boolean debug = false;

	public Logger() {
		super();
	}

	public void debug(String message) {

		if (debug)
			Log.d(TAG, message);

	}

	public void info(String message) {

		if (debug)
			Log.i(TAG, message);

	}

	public void warning(String message) {

		if (debug)
			Log.w(TAG, message);

	}

	public void error(String message) {

		if (debug)
			Log.e(TAG, message);

	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
