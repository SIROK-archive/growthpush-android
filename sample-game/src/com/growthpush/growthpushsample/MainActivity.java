package com.growthpush.growthpushsample;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
		GrowthPush.getInstance().setDeviceTags();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		ImageView image = (ImageView) findViewById(R.id.imageView1);
		int playerSelect = 0;
		switch (v.getId()) {
		case R.id.imageButton1:
			playerSelect = 0;
			image.setImageResource(R.drawable.rock);
			break;
		case R.id.imageButton2:
			playerSelect = 1;
			image.setImageResource(R.drawable.paper);
			break;
		case R.id.imageButton3:
			playerSelect = 2;
			image.setImageResource(R.drawable.scissors);
			break;
		}
		playLocal(playerSelect);
	}

	private void playLocal(int playerSelect) {
		ImageView image = (ImageView) findViewById(R.id.imageView2);
		int[] myImageList = new int[]{R.drawable.rock, R.drawable.paper, R.drawable.scissors};
		int enemySelect = new Random(System.currentTimeMillis()).nextInt(3);
		image.setImageResource(myImageList[enemySelect]);
		((TextView) findViewById(R.id.textView1)).setText(resultsCall(playerSelect, enemySelect));
	}
	
	private String resultsCall(int playerSelect, int enemySelect) {
		String results[][] = {{"引き分け","負け","勝ち"},{"勝ち","引き分け","負け"},{"負け","勝ち","引き分け"}};
		return results[playerSelect][enemySelect];
	}
}
