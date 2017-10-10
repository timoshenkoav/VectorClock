package com.tunebrains.vectorclocklib;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.tunebrains.vectorclocklib.bitmap.HoursPositioning;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 8/29/17.
 */

public class DigitalClockDrawer implements IClockDrawer {

    Paint helpPaint;
    //public static final int SMALL_NUMBER_PERCENT = 30;
    private int numberSpace;
    private int numberScale;
    private int scheduledUpdateHour = -1;
    private int scheduledUpdateMinutes = -1;
    private int measuredHeight;
    private int measuredWidth;
    private boolean is24h = true;
    private boolean animated = true;
    private int gravity = Gravity.CENTER;
    private int minutes;
    private int fullHours;
    private int maxNumberWidth;

    @Override
    public synchronized boolean updateTime(int hours, int minutes) {
        return updateTime(hours, minutes, true);
    }

    @Override
    public void setIs24h(boolean is24h) {
        this.is24h = is24h;
    }

    @Override
    public synchronized boolean updateTime(int fullHours, int minutes, boolean animate) {
        int hours = is24h ? fullHours : fullHours % 12;
        if (getMeasuredHeight() == 0) {
            scheduledUpdateHour = hours;
            scheduledUpdateMinutes = minutes;
            //place1.number = hours/10;
            //place2.number = hours%10;
            //place3.number = minutes/10;
            //place4.number = minutes%10;
            return true;
        }
        if (this.fullHours == fullHours && this.minutes == minutes) { return false; }
        this.fullHours = fullHours;
        this.minutes = minutes;

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

        if (animate && animated) {
            animateNewPlace();
        }
        if (!animated) { calcPlaceY(); }
        return true;
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
        placeAnimator.setDuration(vectorNumberAnimator.getDuration());
        placeAnimator.start();
    }

    private Animator updateNumber(final NumberHolder place4, int newNumber, VectorMasterDrawable p4) {
        if (animated) {

            int oldNumber = place4.number;
            place4.number = newNumber;
            place4.bgOld = place4.bgCurrent;
            place4.bgCurrent = p4;
            List<Animator> animatorList = new ArrayList<>();
            Animator goneAnimation = vectorNumberAnimator.goneAnimation(this, place4.bgOld, oldNumber);
            if (goneAnimation != null) {
                goneAnimation.setDuration(vectorNumberAnimator.getDuration());
                animatorList.add(goneAnimation);
            }
            Animator appearAnimation = vectorNumberAnimator.appearAnimation(this, place4.bgCurrent, newNumber);
            if (appearAnimation != null) {
                appearAnimation.setDuration(vectorNumberAnimator.getDuration());
                appearAnimation.setStartDelay(Math.round(vectorNumberAnimator.getDuration() * 0.9f));
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
                    synchronized (DigitalClockDrawer.this) {
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
        } else {
            place4.number = newNumber;
            place4.bgOld = null;
            place4.bgCurrent = p4;
            return null;
        }
        //}
        //return null;
    }

    @Override
    public void setNumberSpace(int numberSpace) {
        this.numberSpace = numberSpace;
    }

    @Override
    public void setNumberScale(int numberScale) {
        this.numberScale = numberScale;
    }

    public int getMeasuredHeight() {
        return measuredHeight;
    }

    @Override
    public boolean updateTime(long l) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        return updateTime(hours, minutes);
    }

    @Override
    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    @Override
    public void setSmall(boolean small) {

    }

    @Override
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public void updateSize(int clockWidth, int clockHeight) {
        measure(clockWidth, clockHeight);
        calcPlaceY();
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

    @Override
    public void setVectorNumberAnimator(IVectorNumberAnimator vectorNumberAnimator) {
        this.vectorNumberAnimator = vectorNumberAnimator;
        calcMinNumberWidth(100);
    }

    private void calcMinNumberWidth(int height) {
        maxNumberWidth = 0;
        for (int i = 0; i < 10; ++i) {
            VectorMasterDrawable drawable = vectorNumberAnimator.getNumber(i);
            int nWidth = calcWidth(drawable, height, 100);
            maxNumberWidth = Math.max(nWidth, nWidth);
        }
    }

    IVectorNumberAnimator vectorNumberAnimator;

    public DigitalClockDrawer(Context context) {
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

    @Override
    public void draw(Bitmap canvas) {
        canvas.eraseColor(Color.TRANSPARENT);
        draw(new Canvas(canvas));
    }

    @Override
    public void draw(Canvas canvas) {

        if (getMeasuredHeight() == 0) {
            return;
        }
        if (scheduledUpdateHour != -1) {
            synchronized (this) {
                //place1.number = -1;
                //place2.number = -1;
                //place3.number = -1;
                //place4.number = -1;
                updateTime(scheduledUpdateHour, scheduledUpdateMinutes, false);
                calcPlaceY();
                scheduledUpdateHour = scheduledUpdateMinutes = -1;
            }
            return;
        }

        canvas.drawColor(Color.TRANSPARENT);
        canvas.save();
        final int majorGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        if (majorGravity == Gravity.BOTTOM) {
            canvas.translate(0, canvas.getHeight() - getMeasuredHeight());
        }
        if (majorGravity == Gravity.CENTER_VERTICAL) {
            canvas.translate(0, (canvas.getHeight() - getMeasuredHeight()) / 2);
        }
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
        canvas.restore();
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

    private void calcPlaceY() {
        int left = getLeftMargin();
        place1.x = left;
        left += calcWidth(place1.bgCurrent) + HoursPositioning.getNumberSpaceMultiplier(place1.number, place2.number) * numberSpace;
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
        return calcWidth(drawable, getMeasuredHeight(), perc);
    }

    private int calcWidth(VectorMasterDrawable drawable, int height, int perc) {
        if (drawable == null) { return 0; }
        return Math.round(drawable.getIntrinsicWidth() * ((height * perc / 100) / (float) drawable.getIntrinsicHeight()));
    }

    private int getTotalWidth() {

        int left = calcWidth(place1.bgCurrent) + numberSpace;
        left += calcWidth(place2.bgCurrent) + numberSpace;
        left += calcWidth(place3.bgCurrent, numberScale) + numberSpace;
        left += calcWidth(place4.bgCurrent, numberScale);

        return left;
    }

    private int getLeftMargin() {
        final int majorGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (majorGravity) {
            case Gravity.LEFT:
                return 0;
            case Gravity.RIGHT:
                return getMeasuredWidth() - getTotalWidth();
            default:
                return (getMeasuredWidth() - getTotalWidth()) / 2;
        }
    }

    private int getMeasuredWidth() {
        return measuredWidth;
    }

    @Override
    public void measure(int width, int height) {
        measuredWidth = width;
        measuredHeight = height;
    }

    @Override
    public int getMinWidth(int height) {
        int currentNumberWidth = Math.round(height * maxNumberWidth / 100.f);
        int smallNumberWidth = Math.round((height * numberScale / 100) * maxNumberWidth / 100.f);
        int width = currentNumberWidth * 2 + (smallNumberWidth * 2) + numberSpace * 3;
        //width += calcWidth(place1.bgCurrent,height,100) + numberSpace;
        //width += calcWidth(place2.bgCurrent,height,100) + numberSpace;
        //width += calcWidth(place3.bgCurrent,height,numberScale) + numberSpace;
        //width += calcWidth(place4.bgCurrent,height,numberScale) + numberSpace;
        return width;
    }
}
