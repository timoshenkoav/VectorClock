package com.tunebrains.vectorclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.tunebrains.vectorclocklib.VectorDigitalClock;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    long startTime;
    private VectorDigitalClock clock;

    ScheduledExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clock = (VectorDigitalClock) findViewById(R.id.vector_digital_clock);
        startTime = System.currentTimeMillis();


    }

    @Override
    protected void onResume() {
        super.onResume();
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                startTime += TimeUnit.MINUTES.toMillis(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clock.updateTime(startTime);
                    }
                });
            }
        },3,3, TimeUnit.SECONDS);
    }

    @Override
    protected void onPause() {
        executorService.shutdownNow();
        super.onPause();
    }
}
