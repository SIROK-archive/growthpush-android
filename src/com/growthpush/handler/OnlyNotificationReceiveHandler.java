package com.growthpush.handler;

import android.content.Context;
import android.content.Intent;

public class OnlyNotificationReceiveHandler extends DefaultReceiveHandler {

	public OnlyNotificationReceiveHandler() {
		super();
	}

	public OnlyNotificationReceiveHandler(DefaultReceiveHandler.Callback callback) {
		this();
		setCallback(callback);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		addNotification(context, intent);

	}

}
