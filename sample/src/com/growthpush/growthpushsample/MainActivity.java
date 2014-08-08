package com.growthpush.growthpushsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.growthpush.GrowthPush;
import com.growthpush.model.Environment;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GrowthPush.getInstance().initialize(getApplicationContext(), "dy6VlRMnN3juhW9L", "NuvkVhQtRDG2nrNeDzHXzZO5c6j0Xu5t",
				BuildConfig.DEBUG ? Environment.development : Environment.production, "955057365401");
		GrowthPush.getInstance().trackEvent("Launch");
		GrowthPush.getInstance().setDeviceTags();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
