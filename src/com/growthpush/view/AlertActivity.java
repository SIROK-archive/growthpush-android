package com.growthpush.view;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class AlertActivity extends FragmentActivity {

	private static final int WAKE_LOCK_TIMEROUT = 10 * 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(android.R.style.Theme_Translucent);

		manageKeyguard();
		managePower();

		AlertFragment fragment = new AlertFragment(getIntent().getExtras().getString("message"));
		fragment.show(getSupportFragmentManager(), getClass().getName());

	}

	@Override
	public void onDestroy() {

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		super.onDestroy();

	}

	private void manageKeyguard() {

		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		if (!keyguardManager.inKeyguardRestrictedInputMode())
			return;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			return;
		}

		if (keyguardManager.isKeyguardSecure())
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		else if (keyguardManager.isKeyguardLocked())
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

	}

	private void managePower() {

		try {

			PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
			@SuppressWarnings("deprecation")
			final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP, getClass().getName());
			wakeLock.acquire();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					wakeLock.release();
				}

			}, WAKE_LOCK_TIMEROUT);

		} catch (SecurityException e) {
		}

	}

}
