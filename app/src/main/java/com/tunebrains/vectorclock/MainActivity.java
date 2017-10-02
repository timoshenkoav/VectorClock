package com.tunebrains.vectorclock;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import com.tunebrains.vectorclocklib.BitmapDigitalClock;
import com.tunebrains.vectorclocklib.IClockDrawer;
import com.tunebrains.vectorclocklib.VectorNumberAnimator;
import com.tunebrains.vectorclocklib.bitmap.BitmapClockDrawer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    long startTime;

    Handler handler;

    ScheduledExecutorService executorService;

    IClockDrawer drawer;
    private VectorNumberAnimator vectorNumberAnimator;

    int clockWidth, clockHeight;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTime = System.currentTimeMillis();
        handler = new Handler();

        drawer = new BitmapClockDrawer(this);
        vectorNumberAnimator = new VectorNumberAnimator(this);
        drawer.setVectorNumberAnimator(vectorNumberAnimator);
        drawer.setNumberSpace(getResources().getDimensionPixelSize(R.dimen.number_space));
        drawer.setNumberScale(50);
        drawer.setIs24h(true);
        clockWidth = getResources().getDimensionPixelSize(R.dimen.clock_width);
        clockHeight = getResources().getDimensionPixelSize(R.dimen.clock_height);
        bitmap = Bitmap.createBitmap(clockWidth, clockHeight, Bitmap.Config.ARGB_8888);

        vectorNumberAnimator.setNumberColor(getResources().getColor(R.color.number_color));
        drawer.setGravity(Gravity.RIGHT | Gravity.BOTTOM);

        drawer.measure(clockWidth, clockHeight);

        drawer.updateTime(System.currentTimeMillis());

        final ImageView im = (ImageView) findViewById(R.id.digital_clock_image);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (MainActivity.this) {
                    drawer.draw(bitmap);
                    im.setImageBitmap(bitmap);
                }
                handler.postDelayed(this, 1);
            }
        }, 1);
        final BitmapDigitalClock bitmapDigitalClock = (BitmapDigitalClock) findViewById(R.id.bitmap_digital_clock);
        bitmapDigitalClock.updateTime(System.currentTimeMillis());
        findViewById(R.id.size_increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synchronized (MainActivity.this) {
                    //clockWidth += 10;
                    //clockHeight += 10;
                    //bitmap = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888);
                    //drawer.updateSize(clockWidth, clockHeight);
                    startTime += TimeUnit.MINUTES.toMillis(1);
                    drawer.updateTime(startTime);
                    bitmapDigitalClock.updateTime(startTime);
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        executorService = Executors.newSingleThreadScheduledExecutor();
        //executorService.scheduleAtFixedRate(new Runnable() {
        //    @Override
        //    public void run() {
        //        runOnUiThread(new Runnable() {
        //            @Override
        //            public void run() {
        //                drawer.updateTime(startTime);
        //                startTime += TimeUnit.HOURS.toMillis(1);
        //            }
        //        });
        //    }
        //}, 10000, 10000, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onPause() {
        executorService.shutdownNow();
        super.onPause();
    }
}
