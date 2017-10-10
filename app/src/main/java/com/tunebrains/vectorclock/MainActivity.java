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

    IClockDrawer largeDrawer;
    IClockDrawer smallDrawer;
    private VectorNumberAnimator vectorNumberAnimator;

    int clockWidth, clockHeight;
    private Bitmap bitmap;
    private Bitmap smallBitmap;
    ImageView smallClock;
    private int smallHeight;
    private int smallWidth;

    private static final boolean DRAW_LARGE = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smallClock = (ImageView) findViewById(R.id.small_clock_image);
        startTime = System.currentTimeMillis();
        handler = new Handler();

        smallDrawer = new BitmapClockDrawer(this);
        largeDrawer = new BitmapClockDrawer(this);

        vectorNumberAnimator = new VectorNumberAnimator(this);

        smallDrawer.setVectorNumberAnimator(vectorNumberAnimator);
        smallDrawer.setNumberSpace(getResources().getDimensionPixelSize(R.dimen.number_space));
        smallDrawer.setNumberScale(50);
        smallDrawer.setIs24h(true);
        smallDrawer.setSmall(true);

        clockWidth = getResources().getDimensionPixelSize(R.dimen.clock_width);
        clockHeight = getResources().getDimensionPixelSize(R.dimen.clock_height);

        smallHeight = getResources().getDimensionPixelSize(R.dimen.small_clock_height);
        smallWidth = smallDrawer.getMinWidth(smallHeight);


        smallBitmap= Bitmap.createBitmap(smallWidth, smallHeight, Bitmap.Config.ARGB_8888);

        vectorNumberAnimator.setNumberColor(getResources().getColor(R.color.number_color));


        smallDrawer.setGravity(Gravity.RIGHT);

        smallDrawer.measure(smallWidth, smallHeight);

        if (DRAW_LARGE){
            largeDrawer.setVectorNumberAnimator(vectorNumberAnimator);
            largeDrawer.setNumberSpace(getResources().getDimensionPixelSize(R.dimen.number_space));
            largeDrawer.setNumberScale(50);
            largeDrawer.setIs24h(true);
            bitmap = Bitmap.createBitmap(clockWidth, clockHeight, Bitmap.Config.ARGB_8888);
            largeDrawer.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
            largeDrawer.measure(clockWidth, clockHeight);
            largeDrawer.updateTime(System.currentTimeMillis());
        }
        smallDrawer.updateTime(System.currentTimeMillis());

        final ImageView im = (ImageView) findViewById(R.id.digital_clock_image);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (MainActivity.this) {
                    if (DRAW_LARGE) {
                        largeDrawer.draw(bitmap);
                        im.setImageBitmap(bitmap);
                    }
                    smallDrawer.draw(smallBitmap);
                    smallClock.setImageBitmap(smallBitmap);
                }
                handler.postDelayed(this, 1);
            }
        }, 1);
        findViewById(R.id.size_increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synchronized (MainActivity.this) {
                    startTime += TimeUnit.HOURS.toMillis(1);
                    if (DRAW_LARGE) {
                        largeDrawer.updateTime(startTime);
                    }
                    smallDrawer.updateTime(startTime);
                }
            }
        });
        findViewById(R.id.add_1_minute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synchronized (MainActivity.this) {
                    startTime += TimeUnit.MINUTES.toMillis(1);
                    if (DRAW_LARGE) {
                        largeDrawer.updateTime(startTime);
                    }
                    smallDrawer.updateTime(startTime);
                }
            }
        });
        findViewById(R.id.add_10_minute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synchronized (MainActivity.this) {
                    startTime += TimeUnit.MINUTES.toMillis(10);
                    if (DRAW_LARGE) {
                        largeDrawer.updateTime(startTime);
                    }
                    smallDrawer.updateTime(startTime);
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
        //                largeDrawer.updateTime(startTime);
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
