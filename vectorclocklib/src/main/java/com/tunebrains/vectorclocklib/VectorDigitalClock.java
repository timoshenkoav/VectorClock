package com.tunebrains.vectorclocklib;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
    DigitalClockView clockView;
    VectorMasterDrawable bg1, bg2, bg3, bg4;

    private boolean is24h;
    private Calendar calendar;
    private int numberColor;
    private float numberWidth = -1;

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
        clockView.setVectorNumberAnimator(new VectorNumberAnimator());
        is24h = true;
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
        int hours = calendar.get(is24h ? Calendar.HOUR_OF_DAY : Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        clockView.updateTime(hours, minutes);
        //setupHours(hours);
        //setupMinutes(minutes);
    }

    private void setupHours(int hours) {
        int highHour = hours / 10;
        int lowHour = hours % 10;

        int highHourRes = valRes(highHour);

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

    public void setNumberColor(int color) {
        numberColor = color;
    }

    public void setNumberWidth(float numberWidth) {
        this.numberWidth = numberWidth;
    }

    public void setNumberScale(int percent) {
        clockView.setNumberScale(percent);
    }

    private class VectorNumberAnimator implements VectorDigitalNumber.IVectorNumberAnimator{

        @Override
        public VectorMasterDrawable getNumber(int number) {
            VectorMasterDrawable dr = new VectorMasterDrawable(getContext(), valRes(number));
            switch (number){
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:{
                    final PathModel outline = dr.getPathModelByName("outline");
                    outline.setStrokeColor(numberColor);
                    if (numberWidth!=-1) {
                        outline.setStrokeWidth(numberWidth);
                    }
                    break;
                }
                case 4: {
                    final PathModel outline = dr.getPathModelByName("outline1");
                    final PathModel outline2 = dr.getPathModelByName("outline2");
                    outline.setStrokeColor(numberColor);
                    if (numberWidth!=-1) {
                        outline.setStrokeWidth(numberWidth);
                    }
                    outline2.setStrokeColor(numberColor);
                    if (numberWidth!=-1) {
                        outline2.setStrokeWidth(numberWidth);
                    }
                }
            }
            return dr;
        }

        @Override
        public Animator goneAnimation(final Object obj, final VectorMasterDrawable bgOld, int oldNumber) {
            switch (oldNumber){
                case 5:
                case 6:
                case 8:

                case 7:
                case 3:{
                    // find the correct path using name
                    final PathModel outline = bgOld.getPathModelByName("outline");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathStart(0.0f);
                    outline.setTrimPathEnd(1.0f);
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
                    //valueAnimator.setStartDelay(900);
                    valueAnimator.setDuration(1000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgOld != null) {
                                    bgOld.update();
                                }
                            }
                        }
                    });
                    return valueAnimator;
                }
                case 4: {
                    // find the correct path using name
                    final PathModel outline = bgOld.getPathModelByName("outline1");
                    final PathModel outline2 = bgOld.getPathModelByName("outline2");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathEnd(1.0f);
                    ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(1.0f, 0.0f);
                    valueAnimator1.setDuration(300);
                    valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgOld != null) {
                                    bgOld.update();
                                }
                            }
                        }
                    });

                    outline2.setTrimPathEnd(1.0f);
                    ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(1.0f, 0.0f);
                    valueAnimator2.setDuration(100);
                    valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline2.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgOld != null) {
                                    bgOld.update();
                                }
                            }
                        }
                    });

                    AnimatorSet four = new AnimatorSet();
                    four.playTogether(valueAnimator2,valueAnimator1);
                    return four;
                }
                case 0:
                case 1:
                case 2:
                case 9:



                    // find the correct path using name
                    final PathModel outline = bgOld.getPathModelByName("outline");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathEnd(1.0f);
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                    valueAnimator.setDuration(1000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline.setTrimPathStart((Float) valueAnimator.getAnimatedValue());
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
        public Animator appearAnimation(final Object obj, final VectorMasterDrawable bgCurrent, int newNumber) {
            if (bgCurrent == null)
                return null;
            Log.d(TAG,"appearAnimation for "+ newNumber);
            switch (newNumber){
                case 5:
                case 6:
                case 7:

                case 8:
                case 3:{
                    // find the correct path using name
                    final PathModel outline = bgCurrent.getPathModelByName("outline");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathStart(1.0f);
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
                    //valueAnimator.setStartDelay(900);
                    valueAnimator.setDuration(1000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline.setTrimPathStart((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgCurrent != null) {
                                    bgCurrent.update();
                                }
                            }
                        }
                    });
                    return valueAnimator;
                }
                case 4: {
                    // find the correct path using name
                    final PathModel outline = bgCurrent.getPathModelByName("outline1");
                    final PathModel outline2 = bgCurrent.getPathModelByName("outline2");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathEnd(1.0f);
                    outline.setTrimPathStart(1.0f);
                    ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(1.0f, 0.0f);
                    valueAnimator1.setDuration(300);
                    valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline.setTrimPathStart((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgCurrent != null) {
                                    bgCurrent.update();
                                }
                            }
                        }
                    });

                    outline2.setTrimPathStart(1.0f);
                    ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(1.0f, 0.0f);
                    valueAnimator2.setDuration(100);
                    valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            // set trim end value and update view
                            outline2.setTrimPathStart((Float) valueAnimator.getAnimatedValue());
                            synchronized (obj) {
                                if (bgCurrent != null) {
                                    bgCurrent.update();
                                }
                            }
                        }
                    });

                    AnimatorSet four = new AnimatorSet();
                    four.playTogether(valueAnimator1,valueAnimator2);
                    return four;
                }
                case 0:
                case 1:
                case 2:
                case 9:
                 {
                    // find the correct path using name
                    final PathModel outline = bgCurrent.getPathModelByName("outline");
                    // set trim path start (values are given in fraction of length)
                    outline.setTrimPathEnd(0.0f);
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                    //valueAnimator.setStartDelay(900);
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
