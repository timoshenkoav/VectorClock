package com.tunebrains.vectorclocklib;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 8/28/17.
 */

public class VectorDigitalClock extends FrameLayout {
    public static final String TAG = "VectorDigitalClock";
    public static final int DEFAULT_ANIMATE_DURATION = 700;

    public void updateTime(int hours, int minutes) {
        clockView.updateTime(hours, minutes);
    }

    public void updateTime(int hours, int minutes, boolean animate) {
        clockView.updateTime(hours, minutes, animate);
    }

    public void setVectorNumberAnimator(IVectorNumberAnimator vectorNumberAnimator) {
        clockView.setVectorNumberAnimator(vectorNumberAnimator);
    }

    public void setIs24h(boolean is24h) {
        clockView.setIs24h(is24h);
    }

    DigitalClockView clockView;

    private Calendar calendar;
    private int numberColor;
    private float numberWidth = -1;
    private VectorNumberAnimator vectorNumberAnimator;

    public void setNumberSpace(int numberSpace) {
        clockView.setNumberSpace(numberSpace);
    }
    public VectorDigitalClock(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_vector_digital_clock, this);
        clockView = findViewById(R.id.clock_view);
        calendar = Calendar.getInstance();
        vectorNumberAnimator = new VectorNumberAnimator(getContext());
        clockView.setVectorNumberAnimator(vectorNumberAnimator);
        //updateTime(System.currentTimeMillis());
    }

    public VectorDigitalClock(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VectorDigitalClock(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VectorDigitalClock(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void updateTime(long time) {
        updateTime(new Date(time));
    }

    public void updateTime(Date date) {
        calendar.setTime(date);
        updateView();
    }

    private void updateView() {
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        clockView.updateTime(hours, minutes);
        //setupHours(hours);
        //setupMinutes(minutes);
    }

    private void setupHours(int hours) {
        int highHour = hours / 10;
        int lowHour = hours % 10;

        //int highHourRes = valRes(highHour);

        //if (highHour == 0){
        //    place1.setVisibility(View.GONE);
        //}else {
        //    place1.setVisibility(VISIBLE);
        //    bg1 = new VectorMasterDrawable(getContext(), highHourRes);
        //    place1.updateView(bg1, highHour);
        //}
        //
        //int lowHourRes = valRes(lowHour);
        //bg2 = new VectorMasterDrawable(getContext(), lowHourRes);
        //place2.updateView(bg2, lowHour);
        //Log.d(TAG, "Update Hours to: " + highHour + " " + lowHour);
    }

    private void setupMinutes(int minutes) {
        //int highHour = minutes / 10;
        //int lowHour = minutes % 10;
        //
        //int highHourRes = valRes(highHour);
        //bg3 = new VectorMasterDrawable(getContext(), highHourRes);
        //place3.updateView(bg3, highHour);
        //
        //int lowHourRes = valRes(lowHour);
        //
        //bg4 = new VectorMasterDrawable(getContext(), lowHourRes);
        //place4.updateView(bg4, lowHour);
        //Log.d(TAG, "Update Minutes to: " + highHour + " " + lowHour);
    }


    public void setNumberColor(int color) {
        numberColor = color;
        vectorNumberAnimator.setNumberColor(color);
    }

    public void setNumberWidth(float numberWidth) {
        this.numberWidth = numberWidth;
        vectorNumberAnimator.setNumberWidth(numberWidth);
    }

    public void setNumberScale(int percent) {
        clockView.setNumberScale(percent);
    }


}
