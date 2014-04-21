package com.growthpush.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.growthpush.handler.DefaultReceiveHandler;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

	public AlertFragment() {
		super();
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
			onClickPositive(dialog);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			onClickNegative(dialog);
			break;
		default:
			break;
		}

	}

	private void onClickPositive(DialogInterface dialog) {

		dialog.dismiss();

		DefaultReceiveHandler.Callback callback = AlertActivity.getSharedCallback();
		if (callback != null)
			callback.onOpen(this.getActivity(), this.getActivity().getIntent());

		Activity activity = getActivity();
		if (activity != null) {
			NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
			if (notificationManager != null)
				notificationManager.cancel("GrowthPush" + getActivity().getPackageName(), 1);
		}

	}

	private void onClickNegative(DialogInterface dialog) {

		dialog.dismiss();

		Activity activity = getActivity();
		if (activity != null)
			activity.finish();

	}

}
