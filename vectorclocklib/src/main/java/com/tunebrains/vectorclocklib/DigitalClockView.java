package com.tunebrains.vectorclocklib;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 8/29/17.
 */

public class DigitalClockView extends View {

    Paint helpPaint;
    //public static final int SMALL_NUMBER_PERCENT = 30;
    private int numberSpace;
    private int numberScale;
    private int scheduledUpdateHour = -1;
    private int scheduledUpdateMinutes = -1;

    public synchronized void updateTime(int hours, int minutes) {

        if (getMeasuredHeight() == 0){
            scheduledUpdateHour = hours;
            scheduledUpdateMinutes = minutes;
            return;
        }
        VectorMasterDrawable p1 = null, p2, p3, p4;
        if (hours / 10 != 0) {
            p1 = vectorNumberAnimator.getNumber(hours / 10);
        }
        p2 = vectorNumberAnimator.getNumber(hours % 10);
        p3 = vectorNumberAnimator.getNumber(minutes / 10);
        p4 = vectorNumberAnimator.getNumber(minutes % 10);

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
        animateNewPlace();
    }

    private void animateNewPlace() {
        if (getMeasuredHeight() == 0) { return; }
        AnimatorSet placeAnimator = new AnimatorSet();
        int left = getLeftMargin();
        ValueAnimator x1 = ValueAnimator.ofInt(place1.x, left);
        x1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                place1.x = (int) valueAnimator.getAnimatedValue();
            }
        });
        left += calcWidth(place1.bgCurrent) + numberSpace;
        ValueAnimator x2 = ValueAnimator.ofInt(place2.x, left);
        x2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                place2.x = (int) valueAnimator.getAnimatedValue();
            }
        });
        left += calcWidth(place2.bgCurrent) + numberSpace;
        ValueAnimator x3 = ValueAnimator.ofInt(place3.x, left);
        x3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                place3.x = (int) valueAnimator.getAnimatedValue();
            }
        });
        left += calcWidth(place3.bgCurrent, numberScale) + numberSpace;
        ValueAnimator x4 = ValueAnimator.ofInt(place4.x, left);
        x4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                place4.x = (int) valueAnimator.getAnimatedValue();
            }
        });
        placeAnimator.playTogether(x1, x2, x3, x4);
        placeAnimator.setDuration(1000);
        placeAnimator.start();
    }

    private Animator updateNumber(final NumberHolder place4, int newNumber, VectorMasterDrawable p4) {
        int oldNumber = place4.number;
        place4.number = newNumber;
        //if (place4.bgCurrent == null) {
        //    place4.bgCurrent = p4;
        //    calcPlace();
        //} else {
        place4.bgOld = place4.bgCurrent;
        place4.bgCurrent = p4;
        List<Animator> animatorList = new ArrayList<>();
        Animator goneAnimation = vectorNumberAnimator.goneAnimation(this, place4.bgOld, oldNumber);
        if (goneAnimation != null) {
            goneAnimation.setDuration(1000);
            animatorList.add(goneAnimation);
        }
        Animator appearAnimation = vectorNumberAnimator.appearAnimation(this, place4.bgCurrent, newNumber);
        if (appearAnimation != null) {
            appearAnimation.setDuration(1000);
            appearAnimation.setStartDelay(900);
            animatorList.add(appearAnimation);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorList);
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
        //}
        //return null;
    }

    public void setNumberSpace(int numberSpace) {
        this.numberSpace = numberSpace;
    }

    public void setNumberScale(int numberScale) {
        this.numberScale = numberScale;
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

        if (getMeasuredHeight() == 0) { return; }

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
        drawNumber(canvas, place3.bgOld, numberScale);
        drawNumber(canvas, place3.bgCurrent, numberScale);
        canvas.restore();

        canvas.save();
        canvas.translate(place4.x, place4.y);

        drawNumber(canvas, place4.bgOld, numberScale);
        drawNumber(canvas, place4.bgCurrent, numberScale);

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
        int left = getLeftMargin();
        place1.x = left;
        left += calcWidth(place1.bgCurrent) + numberSpace;
        place2.x = left;
        left += calcWidth(place2.bgCurrent) + numberSpace;
        place3.x = left;
        left += calcWidth(place3.bgCurrent, numberScale) + numberSpace;
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

        if (scheduledUpdateHour!=-1){
            updateTime(scheduledUpdateHour, scheduledUpdateMinutes);
            calcPlace();
            scheduledUpdateHour = scheduledUpdateMinutes = -1;
            return;
        }
        calcPlace();
    }

    private int getTotalWidth(){

        int left = calcWidth(place1.bgCurrent) + numberSpace;
        left += calcWidth(place2.bgCurrent) + numberSpace;
        left += calcWidth(place3.bgCurrent, numberScale) + numberSpace;
        left += calcWidth(place4.bgCurrent, numberScale);

        return left;
    }

    private int getLeftMargin(){
        return (getMeasuredWidth() - getTotalWidth())/2;
    }
}
