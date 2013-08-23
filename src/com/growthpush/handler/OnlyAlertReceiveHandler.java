package com.growthpush.handler;

import android.content.Context;
import android.content.Intent;

public class OnlyAlertReceiveHandler extends DefaultReceiveHandler {

	@Override
	public void onReceive(Context context, Intent intent) {

		showAlert(context, intent);

	}

}
