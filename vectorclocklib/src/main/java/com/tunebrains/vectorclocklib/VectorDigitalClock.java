package com.tunebrains.vectorclocklib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 8/28/17.
 */

public class VectorDigitalClock extends FrameLayout {
    public static final String TAG = "VectorDigitalClock";
    VectorDigitalNumber place1, place2, place3, place4;
    VectorMasterDrawable bg1, bg2, bg3, bg4;

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
        place1.setVectorNumberAnimator(new VectorNumberAnimator());
        place2 = findViewById(R.id.place_2);
        place2.setVectorNumberAnimator(new VectorNumberAnimator());
        place3 = findViewById(R.id.place_3);
        place3.setVectorNumberAnimator(new VectorNumberAnimator());
        place4 = findViewById(R.id.place_4);
        place4.setVectorNumberAnimator(new VectorNumberAnimator());
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
        if (highHour == 0){
            place1.setVisibility(View.GONE);
        }else {
            place1.setVisibility(VISIBLE);
            bg1 = new VectorMasterDrawable(getContext(), highHourRes);
            place1.updateView(bg1, highHour);
        }

        int lowHourRes = valRes(lowHour);
        bg2 = new VectorMasterDrawable(getContext(), lowHourRes);
        place2.updateView(bg2, lowHour);
        Log.d(TAG, "Update Hours to: " + highHour + " " + lowHour);
    }

    private void setupMinutes(int minutes) {
        int highHour = minutes / 10;
        int lowHour = minutes % 10;

        int highHourRes = valRes(highHour);
        bg3 = new VectorMasterDrawable(getContext(), highHourRes);
        place3.updateView(bg3, highHour);

        int lowHourRes = valRes(lowHour);

        bg4 = new VectorMasterDrawable(getContext(), lowHourRes);
        place4.updateView(bg4, lowHour);
        Log.d(TAG, "Update Minutes to: " + highHour + " " + lowHour);
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

    private class VectorNumberAnimator implements VectorDigitalNumber.IVectorNumberAnimator{

        @Override
        public Animator goneAnimation(final VectorDigitalNumber obj, final VectorMasterDrawable bgOld, int oldNumber) {
            switch (oldNumber){
                case 4: {
                    // find the correct path using name
                    final PathModel outline = bgOld.getPathModelByName("outline1");
                    final PathModel outline2 = bgOld.getPathModelByName("outline2");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathEnd(1.0f);
                    outline2.setTrimPathEnd(1.0f);
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
                    valueAnimator.setStartDelay(900);
                    valueAnimator.setDuration(1000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                            outline2.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgOld != null) {
                                    bgOld.update();
                                }
                            }
                        }
                    });
                    return valueAnimator;
                }
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    // find the correct path using name
                    final PathModel outline = bgOld.getPathModelByName("outline");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathEnd(1.0f);
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
                    valueAnimator.setDuration(1000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgOld!=null) {
                                    bgOld.update();
                                }
                            }
                        }
                    });
                    return valueAnimator;
            }
            return null;
        }

        @Override
        public Animator appearAnimation(final VectorDigitalNumber obj, final VectorMasterDrawable bgCurrent, int newNumber) {
            switch (newNumber){
                case 4: {
                    // find the correct path using name
                    final PathModel outline = bgCurrent.getPathModelByName("outline1");
                    final PathModel outline2 = bgCurrent.getPathModelByName("outline2");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathEnd(0.0f);
                    outline2.setTrimPathEnd(0.0f);
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                    valueAnimator.setStartDelay(900);
                    valueAnimator.setDuration(1000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                            outline2.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgCurrent != null) {
                                    bgCurrent.update();
                                }
                            }
                        }
                    });
                    return valueAnimator;
                }
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9: {
                    // find the correct path using name
                    final PathModel outline = bgCurrent.getPathModelByName("outline");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathEnd(0.0f);
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                    valueAnimator.setStartDelay(900);
                    valueAnimator.setDuration(1000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgCurrent != null) {
                                    bgCurrent.update();
                                }
                            }
                        }
                    });
                    return valueAnimator;
                }
            }
            return null;
        }
    }
}
