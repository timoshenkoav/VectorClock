package com.tunebrains.vectorclocklib;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import static com.tunebrains.vectorclocklib.VectorDigitalClock.DEFAULT_ANIMATE_DURATION;

public class VectorNumberAnimator implements IVectorNumberAnimator {
    private final Context context;
    private int numberColor;
    private float numberWidth=-1;

    public VectorNumberAnimator(Context context) {
        this.context = context;
    }

    @Override
    public long getDuration() {
        return DEFAULT_ANIMATE_DURATION;
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

    @Override
    public VectorMasterDrawable getNumber(int number) {
        VectorMasterDrawable dr = new VectorMasterDrawable(getContext(), valRes(number));
        switch (number) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {
                final PathModel outline = dr.getPathModelByName("outline");
                outline.setStrokeColor(numberColor);
                if (numberWidth != -1) {
                    outline.setStrokeWidth(numberWidth);
                }
                break;
            }
            case 4: {
                final PathModel outline = dr.getPathModelByName("outline1");
                final PathModel outline2 = dr.getPathModelByName("outline2");
                outline.setStrokeColor(numberColor);
                if (numberWidth != -1) {
                    outline.setStrokeWidth(numberWidth);
                }
                outline2.setStrokeColor(numberColor);
                if (numberWidth != -1) {
                    outline2.setStrokeWidth(numberWidth);
                }
            }
        }
        return dr;
    }

    @Override
    public Animator goneAnimation(final Object obj, final VectorMasterDrawable bgOld, int oldNumber) {
        switch (oldNumber) {
            case 5:
            case 6:
            case 8:
            case 7:
            case 3: {
                // find the correct path using name
                final PathModel outline = bgOld.getPathModelByName("outline");
                // set trim path start (values are given in fraction of length)
                outline.setTrimPathStart(0.0f);
                outline.setTrimPathEnd(1.0f);
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
                valueAnimator.setDuration(DEFAULT_ANIMATE_DURATION);
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
                valueAnimator.setInterpolator(new AccelerateInterpolator());
                return valueAnimator;
            }
            case 4: {
                // find the correct path using name
                final PathModel outline = bgOld.getPathModelByName("outline1");
                final PathModel outline2 = bgOld.getPathModelByName("outline2");
                // set trim path start (values are given in fraction of length)
                outline.setTrimPathEnd(1.0f);
                ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(1.0f, 0.0f);
                valueAnimator1.setDuration(DEFAULT_ANIMATE_DURATION / 2);
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
                valueAnimator2.setDuration(DEFAULT_ANIMATE_DURATION / 2);
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
                four.setInterpolator(new AccelerateInterpolator());
                four.playTogether(valueAnimator2, valueAnimator1);
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
                valueAnimator.setDuration(DEFAULT_ANIMATE_DURATION);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        // set trim end value and update view
                        outline.setTrimPathStart((Float) valueAnimator.getAnimatedValue());
                        synchronized (obj) {
                            if (bgOld != null) {
                                bgOld.update();
                            }
                        }
                    }
                });
                valueAnimator.setInterpolator(new AccelerateInterpolator());
                return valueAnimator;
        }
        return null;
    }

    @Override
    public Animator appearAnimation(final Object obj, final VectorMasterDrawable bgCurrent, int newNumber) {
        if (bgCurrent == null) { return null; }
        switch (newNumber) {
            case 5:
            case 6:
            case 7:

            case 8:
            case 3: {
                // find the correct path using name
                final PathModel outline = bgCurrent.getPathModelByName("outline");
                // set trim path start (values are given in fraction of length)
                outline.setTrimPathStart(1.0f);
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
                //valueAnimator.setStartDelay(900);
                valueAnimator.setDuration(DEFAULT_ANIMATE_DURATION);
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
                valueAnimator.setInterpolator(new AccelerateInterpolator());
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
                valueAnimator1.setDuration(DEFAULT_ANIMATE_DURATION / 2);
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
                valueAnimator2.setDuration(DEFAULT_ANIMATE_DURATION / 2);
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
                four.setInterpolator(new AccelerateInterpolator());
                four.playTogether(valueAnimator1, valueAnimator2);
                return four;
            }
            case 0:
            case 1:
            case 2:
            case 9: {
                // find the correct path using name
                final PathModel outline = bgCurrent.getPathModelByName("outline");
                // set trim path start (values are given in fraction of length)
                outline.setTrimPathEnd(0.0f);
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                //valueAnimator.setStartDelay(900);
                valueAnimator.setDuration(DEFAULT_ANIMATE_DURATION);
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
                valueAnimator.setInterpolator(new AccelerateInterpolator());
                return valueAnimator;
            }
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public void setNumberColor(int numberColor) {
        this.numberColor = numberColor;
    }

    public void setNumberWidth(float numberWidth) {
        this.numberWidth = numberWidth;
    }
}