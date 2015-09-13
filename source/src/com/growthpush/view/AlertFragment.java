package com.growthpush.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;

import com.growthpush.GrowthPush;

/**
 * Created by Shigeru Ogawa on 13/08/12.
 */
public class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

	protected DialogCallback listener;

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

	protected Dialog generateAlertDialog() {

		PackageManager packageManager = getActivity().getPackageManager();
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = packageManager.getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			return null;
		}

		int icon = applicationInfo.icon;
		if (applicationInfo.metaData != null && applicationInfo.metaData.containsKey(GrowthPush.DIALOG_ICON_META_KEY))
			icon = applicationInfo.metaData.getInt(GrowthPush.DIALOG_ICON_META_KEY);

		Dialog dialog = new AlertDialog.Builder(getActivity()).setIcon(icon).setTitle(packageManager.getApplicationLabel(applicationInfo))
				.setMessage(getArguments().getString("message")).setPositiveButton("OK", this).setNegativeButton("Cancel", this).create();

		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK)
					if (listener != null)
						listener.onClickNegative(dialog);

				return false;
			}
		});

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);

		return dialog;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof AlertActivity)
			this.listener = (DialogCallback) activity;

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			if (this.listener != null)
				listener.onClickPositive(dialog);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			if (this.listener != null)
				listener.onClickNegative(dialog);
			break;
		default:
			break;
		}

	}

}
