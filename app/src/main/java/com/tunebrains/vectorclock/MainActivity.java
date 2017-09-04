package com.tunebrains.vectorclock;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import com.tunebrains.vectorclocklib.DigitalClockDrawer;
import com.tunebrains.vectorclocklib.VectorDigitalClock;
import com.tunebrains.vectorclocklib.VectorNumberAnimator;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    long startTime;
    private VectorDigitalClock clock;

    Handler handler;

    ScheduledExecutorService executorService;

    DigitalClockDrawer drawer;
    private VectorNumberAnimator vectorNumberAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clock = (VectorDigitalClock) findViewById(R.id.vector_digital_clock);
        clock.setNumberSpace(getResources().getDimensionPixelSize(R.dimen.number_space));
        clock.setNumberColor(getResources().getColor(R.color.number_color));
        clock.setNumberScale(50);
        clock.setGravity(Gravity.LEFT);
        clock.setIs24h(false);
        startTime = System.currentTimeMillis();
        handler = new Handler();

        drawer = new DigitalClockDrawer(this);
        vectorNumberAnimator = new VectorNumberAnimator(this);
        drawer.setVectorNumberAnimator(vectorNumberAnimator);
        drawer.setNumberSpace(getResources().getDimensionPixelSize(R.dimen.number_space));
        drawer.setNumberScale(50);

        final Bitmap bitmap = Bitmap.createBitmap(850,400, Bitmap.Config.ARGB_8888);
        vectorNumberAnimator.setNumberColor(getResources().getColor(R.color.number_color));
        drawer.setGravity(Gravity.RIGHT);
        drawer.updateTime(System.currentTimeMillis());
        drawer.measure(850,400);
        final ImageView im = (ImageView) findViewById(R.id.digital_clock_image);
        //handler.postDelayed(new Runnable() {
        //    @Override
        //    public void run() {
        //        drawer.draw(bitmap);
        //        im.setImageBitmap(bitmap);
        //        handler.postDelayed(this,1);
        //    }
        //},1);
        clock.updateTime(startTime);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //handler.post(new Runnable() {
        //    @Override
        //    public void run() {
        //        clock.updateTime(startTime);
        //    }
        //});
        //clock.updateTime(startTime);
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clock.setIs24h(true);
                        drawer.updateTime(startTime);
                        clock.updateTime(startTime);
                        startTime += TimeUnit.MINUTES.toMillis(1);
                    }
                });
            }
        },3000,3000, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onPause() {
        executorService.shutdownNow();
        super.onPause();
    }
}
