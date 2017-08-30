package com.tunebrains.vectorclock;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.tunebrains.vectorclocklib.VectorDigitalClock;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    long startTime;
    private VectorDigitalClock clock;

    Handler handler;

    ScheduledExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clock = (VectorDigitalClock) findViewById(R.id.vector_digital_clock);
        clock.setNumberSpace(getResources().getDimensionPixelSize(R.dimen.number_space));
        clock.setNumberColor(getResources().getColor(R.color.number_color));
        clock.setNumberScale(50);
        //clock.setNumberWidth(getResources().getDimension(R.dimen.number_width));
        //startTime = System.currentTimeMillis();
        startTime = new Date(2017, 10, 10, 0,58).getTime();
        handler = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(new Runnable() {
            @Override
            public void run() {
                clock.updateTime(startTime);
            }
        });
        //clock.updateTime(startTime);
        //executorService = Executors.newSingleThreadScheduledExecutor();
        //executorService.scheduleAtFixedRate(new Runnable() {
        //    @Override
        //    public void run() {
        //        runOnUiThread(new Runnable() {
        //            @Override
        //            public void run() {
        //                clock.updateTime(startTime);
        //                startTime += TimeUnit.MINUTES.toMillis(1);
        //            }
        //        });
        //    }
        //},100,3000, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onPause() {
        //executorService.shutdownNow();
        super.onPause();
    }
}
