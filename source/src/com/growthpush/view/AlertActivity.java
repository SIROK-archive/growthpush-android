package com.growthpush.view;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.growthpush.handler.DefaultReceiveHandler;
import com.growthpush.utils.SystemUtils;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class AlertActivity extends FragmentActivity implements DialogCallback {

	private static final int WAKE_LOCK_TIMEROUT = 10 * 1000;

	private static DefaultReceiveHandler.Callback sharedCallback = null;

	public static void setSharedCallback(DefaultReceiveHandler.Callback sharedCallback) {
		AlertActivity.sharedCallback = sharedCallback;
	}

	public static DefaultReceiveHandler.Callback getSharedCallback() {
		return sharedCallback;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(android.R.style.Theme_Translucent);

		boolean showDialog = getIntent().getExtras().getBoolean("showDialog");
		if (showDialog) {
			showDialog();
		} else {
			if (sharedCallback != null)
				sharedCallback.onOpen(this, getIntent());
			finish();
		}

	}

	@Override
	public void onDestroy() {

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		super.onDestroy();

	}

	private void showDialog() {

		manageKeyguard();
		managePower();

		final AlertFragment fragment = new AlertFragment();
		fragment.setCancelable(false);

		Bundle bundle = new Bundle();
		bundle.putString("message", getIntent().getExtras().getString("message"));
		fragment.setArguments(bundle);

		if (!isFinishing()) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					fragment.show(getSupportFragmentManager(), getClass().getName());
				}
			});
		}

	}

	private void manageKeyguard() {

		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		if (!keyguardManager.inKeyguardRestrictedInputMode())
			return;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

			if (keyguardManager.isKeyguardSecure())
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
			else if (keyguardManager.isKeyguardLocked())
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

			return;

		}

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

	}

	@SuppressWarnings("deprecation")
	private void managePower() {

		PowerManager powerManager = SystemUtils.getPowerManager(getApplicationContext());
		if (powerManager == null)
			return;

		final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, getClass().getName());
		try {
			wakeLock.acquire();
		} catch (SecurityException e) {
			return;
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				wakeLock.release();
			}

		}, WAKE_LOCK_TIMEROUT);

	}

	@Override
	public void onClickPositive(DialogInterface dialog) {

		dialog.dismiss();
		if (sharedCallback != null)
			sharedCallback.onOpen(this, this.getIntent());

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		if (notificationManager != null)
			notificationManager.cancel("GrowthPush" + getPackageName(), 1);
	}

	@Override
	public void onClickNegative(DialogInterface dialog) {
		dialog.dismiss();
		finish();
	}

}
