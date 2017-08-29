package com.tunebrains.vectorclocklib;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 8/29/17.
 */

public class DigitalClockView extends View {

    Paint helpPaint;
    public static final int SMALL_NUMBER_PERCENT = 30;

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

    public synchronized void updateTime(int hours, int minutes) {

        VectorMasterDrawable p1 = null, p2, p3, p4;
        if (hours / 10 != 0) {
            p1 = new VectorMasterDrawable(getContext(), valRes(hours / 10));
        }
        p2 = new VectorMasterDrawable(getContext(), valRes(hours % 10));
        p3 = new VectorMasterDrawable(getContext(), valRes(minutes / 10));
        p4 = new VectorMasterDrawable(getContext(), valRes(minutes % 10));

        if (hours / 10 != place1.number) {
            updateNumber(place1, hours / 10, p1);
        }

        if (hours % 10 != place2.number) {
            updateNumber(place2, hours % 10, p2);
        }

        if (place3.number != minutes / 10) {
            updateNumber(place3, minutes / 10, p3);
        }

        if (place4.number != minutes % 10) {
            updateNumber(place4, minutes % 10, p4);
        }

        //calc new place
        AnimatorSet placeAnimator = new AnimatorSet();
        int left = 0;
        ValueAnimator x1 = ValueAnimator.ofInt(place1.x, left);
        x1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                place1.x = (int) valueAnimator.getAnimatedValue();
            }
        });
        left += calcWidth(place1.bgCurrent);
        ValueAnimator x2 = ValueAnimator.ofInt(place2.x, left);
        x2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                place2.x = (int) valueAnimator.getAnimatedValue();
            }
        });
        left += calcWidth(place2.bgCurrent);
        ValueAnimator x3 = ValueAnimator.ofInt(place3.x, left);
        x3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                place3.x = (int) valueAnimator.getAnimatedValue();
            }
        });
        left += calcWidth(place3.bgCurrent,SMALL_NUMBER_PERCENT);
        ValueAnimator x4 = ValueAnimator.ofInt(place4.x, left);
        x4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                place4.x = (int) valueAnimator.getAnimatedValue();
            }
        });
        placeAnimator.playTogether(x1,x2,x3,x4);
        placeAnimator.setDuration(1000);
        placeAnimator.start();
    }

    private Animator updateNumber(final NumberHolder place4, int newNumber, VectorMasterDrawable p4) {
        int oldNumber = place4.number;
        place4.number = newNumber;
        if (place4.bgCurrent == null) {
            place4.bgCurrent = p4;
            calcPlace();
        } else {
            place4.bgOld = place4.bgCurrent;
            place4.bgCurrent = p4;
            Animator goneAnimation = vectorNumberAnimator.goneAnimation(this, place4.bgOld, oldNumber);
            Animator appearAnimation = vectorNumberAnimator.appearAnimation(this, place4.bgCurrent, newNumber);
            goneAnimation.setDuration(1000);
            appearAnimation.setDuration(1000);
            appearAnimation.setStartDelay(900);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(goneAnimation, appearAnimation);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    synchronized (DigitalClockView.this) {
                        place4.bgOld = null;
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            set.start();
            return set;
        }
        return null;
    }

    private class NumberHolder {
        VectorMasterDrawable bgOld;
        VectorMasterDrawable bgCurrent;
        int x;
        int y;
        public int number = -1;
    }

    NumberHolder place1, place2, place3, place4;
    boolean initialSet;

    public void setVectorNumberAnimator(VectorDigitalNumber.IVectorNumberAnimator vectorNumberAnimator) {
        this.vectorNumberAnimator = vectorNumberAnimator;
    }

    VectorDigitalNumber.IVectorNumberAnimator vectorNumberAnimator;

    public DigitalClockView(Context context) {
        super(context);
        init();
    }

    private void init() {
        helpPaint = new Paint();
        helpPaint.setStyle(Paint.Style.FILL);
        place1 = new NumberHolder();
        place2 = new NumberHolder();
        place3 = new NumberHolder();
        place4 = new NumberHolder();
    }

    public DigitalClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DigitalClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DigitalClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getMeasuredHeight() == 0 )
            return;

        canvas.save();
        canvas.translate(place1.x, place1.y);
        drawNumber(canvas, place1.bgOld, 100);
        drawNumber(canvas, place1.bgCurrent, 100);
        canvas.restore();

        canvas.save();
        canvas.translate(place2.x, place2.y);
        drawNumber(canvas, place2.bgOld, 100);
        drawNumber(canvas, place2.bgCurrent, 100);
        canvas.restore();

        canvas.save();
        canvas.translate(place3.x, place3.y);
        drawNumber(canvas, place3.bgOld, SMALL_NUMBER_PERCENT);
        drawNumber(canvas, place3.bgCurrent, SMALL_NUMBER_PERCENT);
        canvas.restore();

        canvas.save();
        canvas.translate(place4.x, place4.y);

        drawNumber(canvas, place4.bgOld, SMALL_NUMBER_PERCENT);
        drawNumber(canvas, place4.bgCurrent, SMALL_NUMBER_PERCENT);

        canvas.restore();
        invalidate();
    }

    private void drawNumber(Canvas canvas, VectorMasterDrawable bgCurrent, int percent) {
        int c = canvas.save();
        if (bgCurrent != null) {
            bgCurrent.setBounds(0, 0, calcWidth(bgCurrent, percent), getMeasuredHeight() * percent / 100);
            canvas.translate(0, getMeasuredHeight() - getMeasuredHeight() * percent / 100);
            bgCurrent.draw(canvas);
            canvas.translate(0, -(getMeasuredHeight() - getMeasuredHeight() * percent / 100));
        }
        canvas.restoreToCount(c);
    }

    private void drawInstinct(Canvas canvas, VectorMasterDrawable place1, int color) {
        helpPaint.setColor(color);
        Rect bounds = place1.getBounds();
        canvas.drawRect(0, 0, bounds.width(), bounds.height(), helpPaint);
    }

    private void calcPlace() {
        int left = 0;
        place1.x = left;
        left += calcWidth(place1.bgCurrent);
        place2.x = left;
        left += calcWidth(place2.bgCurrent);
        place3.x = left;
        left += calcWidth(place3.bgCurrent, SMALL_NUMBER_PERCENT);
        place4.x = left;
    }

    private int calcWidth(VectorMasterDrawable drawable) {
        return calcWidth(drawable, 100);
    }

    private int calcWidth(VectorMasterDrawable drawable, int perc) {
        if (drawable == null) { return 0; }
        return Math.round(drawable.getIntrinsicWidth() * ((getMeasuredHeight() * perc / 100) / (float) drawable.getIntrinsicHeight()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        calcPlace();
    }
}
