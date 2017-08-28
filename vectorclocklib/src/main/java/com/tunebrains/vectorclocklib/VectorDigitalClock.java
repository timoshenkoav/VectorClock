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
import android.widget.ImageView;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 8/28/17.
 */

public class VectorDigitalClock extends FrameLayout {
    ImageView place1, place2, place3, place4;

    private boolean is24h;
    private Calendar calendar;

    public VectorDigitalClock(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_vector_digital_clock, this);
        calendar = Calendar.getInstance();
        place1 = findViewById(R.id.place_1);
        place2 = findViewById(R.id.place_2);
        place3 = findViewById(R.id.place_3);
        place4 = findViewById(R.id.place_4);
        is24h = true;
        updateTime(System.currentTimeMillis());
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
        int hours = calendar.get(is24h ? Calendar.HOUR_OF_DAY : Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        setupHours(hours);
        setupMinutes(minutes);
    }

    private void setupHours(int hours) {
        int highHour = hours / 10;
        int lowHour = hours % 10;

        int highHourRes = valRes(highHour);
        place1.setImageDrawable(new VectorMasterDrawable(getContext(), highHourRes));

        int lowHourRes = valRes(lowHour);
        place2.setImageDrawable(new VectorMasterDrawable(getContext(), lowHourRes));
    }

    private void setupMinutes(int minutes) {
        int highHour = minutes / 10;
        int lowHour = minutes % 10;

        int highHourRes = valRes(highHour);
        VectorMasterDrawable dr = new VectorMasterDrawable(getContext(), highHourRes);
        place3.setImageDrawable(dr);

        int lowHourRes = valRes(lowHour);
        place4.setImageDrawable(new VectorMasterDrawable(getContext(), lowHourRes));
    }

    private int valRes(int highHour) {
        switch (highHour) {
            case 0:
                return R.drawable.zero;
            case 1:
                return R.drawable.one;
            case 2:
                return R.drawable.two;
            case 3:
                return R.drawable.three;
            case 4:
                return R.drawable.four;
            case 5:
                return R.drawable.five;
            case 6:
                return R.drawable.six;
            case 7:
                return R.drawable.seven;
            case 8:
                return R.drawable.eight;
            case 9:
                return R.drawable.nine;
        }
        return 0;
    }
}
