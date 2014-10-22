package com.growthpush.growthpushsample;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.growthpush.GrowthPush;
import com.growthpush.model.Environment;

public class MainActivity extends Activity implements OnClickListener{

    private int[] myImageList = new int[]{R.drawable.rock, R.drawable.paper, R.drawable.scissors};
    private String results[][] = {{"引き分け","負け","勝ち"},{"勝ち","引き分け","負け"},{"負け","勝ち","引き分け"}};

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
        case R.id.selectRock:
            playLocal(0);
            break;
        case R.id.selectPaper:
            playLocal(1);
            break;
        case R.id.selectScissors:
            playLocal(2);
            break;
        }
    }

    private void playLocal(int playerSelect) {
        ImageView playerImage = (ImageView) findViewById(R.id.imageView1);
        playerImage.setImageResource(this.myImageList[playerSelect]);

        ImageView enemyImage = (ImageView) findViewById(R.id.imageView2);
        int enemySelect = new Random(System.currentTimeMillis()).nextInt(this.myImageList.length);
        enemyImage.setImageResource(this.myImageList[enemySelect]);
        ((TextView) findViewById(R.id.textView1)).setText(results[playerSelect][enemySelect]);
        
        //Event,Tag Post
        GrowthPush.getInstance().trackEvent("GameResult", results[playerSelect][enemySelect]);
        GrowthPush.getInstance().setDeviceTags();
    }
}