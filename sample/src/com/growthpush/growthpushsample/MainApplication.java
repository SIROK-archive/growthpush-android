package com.growthpush.growthpushsample;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.growthpush.GrowthPush;
import com.growthpush.handler.BaseReceiveHandler.Callback;
import com.growthpush.handler.DefaultReceiveHandler;

public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		((DefaultReceiveHandler) GrowthPush.getInstance().getReceiveHandler()).setCallback(new Callback() {
			@Override
			public void onOpen(Context context, Intent intent) {

				GrowthPush.getInstance().trackEvent("Launch via push notification");
				String url = intent.getExtras().getString("url");
				if (url == null) {
					super.onOpen(context, intent);
					return;
				}

				Uri uri = Uri.parse(url);
				Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);
				urlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(urlIntent);

			}
		});

	}
}
