package com.growthpush.growthpushsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.growthpush.GrowthPush;
import com.growthpush.model.Environment;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GrowthPush
				.getInstance()
				.initialize(getApplicationContext(), 1071, "Ou3DgCwmMS2tBocWXGKSnRUUTyVA078n",
						BuildConfig.DEBUG ? Environment.development : Environment.production, true).register("955057365401");
		GrowthPush.getInstance().trackEvent("Launch");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			//Event Post
			GrowthPush.getInstance().trackEvent(
					((EditText) findViewById(R.id.editText1)).getText().toString(),
					((EditText) findViewById(R.id.editText2)).getText().toString());
			break;
		case R.id.button2:
			//Tag Post
			GrowthPush.getInstance().setTag(
					((EditText) findViewById(R.id.editText3)).getText().toString(),
					((EditText) findViewById(R.id.editText4)).getText().toString());
			break;
		case R.id.button3:
			//setDeviceTags
			GrowthPush.getInstance().setDeviceTags();
			break;
		default:
			break;
		}
	}
}
