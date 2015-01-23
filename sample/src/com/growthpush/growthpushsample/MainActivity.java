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
		GrowthPush.getInstance().initialize(getApplicationContext(), "OyVa3zboPjHVjsDC", "3EKydeJ0imxJ5WqS22FJfdVamFLgu7XA",
				BuildConfig.DEBUG ? Environment.development : Environment.production, "955057365401");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
