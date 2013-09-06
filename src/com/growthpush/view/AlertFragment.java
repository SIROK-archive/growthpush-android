package com.growthpush.view;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.growthpush.utils.SystemUtils;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

	public AlertFragment() {
		super();
	}

	public AlertFragment(String message) {

		Bundle bundle = new Bundle();
		bundle.putString("message", message);
		this.setArguments(bundle);

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Dialog dialog = generateAlertDialog();
		if (dialog == null)
			return super.onCreateDialog(savedInstanceState);

		return dialog;

	}

	private Dialog generateAlertDialog() {

		PackageManager packageManager = getActivity().getPackageManager();
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = packageManager.getApplicationInfo(getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return null;
		}

		Dialog dialog = new AlertDialog.Builder(getActivity()).setIcon(packageManager.getApplicationIcon(applicationInfo))
				.setTitle(packageManager.getApplicationLabel(applicationInfo)).setMessage(getArguments().getString("message"))
				.setPositiveButton("OK", this).setNegativeButton("Cancel", this).create();

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);

		return dialog;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:

			dialog.dismiss();
			startActivity(getIntent());

			NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel("GrowthPush" + getActivity().getPackageName(), 1);
			break;

		case DialogInterface.BUTTON_NEGATIVE:

			dialog.dismiss();
			getActivity().finish();
			break;

		default:
			break;
		}

	}

	private Intent getIntent() {

		ActivityManager activityManager = SystemUtils.getActivityManager(getActivity().getApplicationContext());
		if (activityManager != null) {
			Intent intent = getTopIntent(activityManager);
			if (intent != null)
				return intent;
		}

		return getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName())
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	}

	private Intent getTopIntent(ActivityManager activityManager) {

		for (ActivityManager.RunningTaskInfo taskInfo : activityManager.getRunningTasks(10)) {

			if (!getActivity().getPackageName().equals(taskInfo.topActivity.getPackageName()))
				continue;
			if (taskInfo.topActivity.getClassName().equals(getActivity().getClass().getName()))
				continue;

			return new Intent().setComponent(taskInfo.topActivity);

		}

		return null;

	}
}
