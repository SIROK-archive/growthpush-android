package com.growthpush.handler;

import android.content.Context;
import android.content.Intent;

public class OnlyAlertReceiveHandler extends DefaultReceiveHandler {

	public OnlyAlertReceiveHandler() {
		super();
	}

	public OnlyAlertReceiveHandler(DefaultReceiveHandler.Callback callback) {
		this();
		setCallback(callback);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		showAlert(context, intent);

	}

}
