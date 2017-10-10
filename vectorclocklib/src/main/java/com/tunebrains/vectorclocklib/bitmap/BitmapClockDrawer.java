package com.tunebrains.vectorclocklib.bitmap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import com.tunebrains.vectorclocklib.IClockDrawer;
import com.tunebrains.vectorclocklib.IVectorNumberAnimator;
import com.tunebrains.vectorclocklib.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 9/27/17.
 */

public class BitmapClockDrawer implements IClockDrawer {
    private final BitmapLoader bitmapLoader;
    private final Paint helpPaint;
    private int numberScale;
    private int measuredWidth;
    private int measuredHeight;
    private boolean is24h;
    private int fullHours;
    private int minutes;
    private boolean animated = true;
    private int gravity;
    private int numberSpace;
    private int hours;
    private int minResHeight;
    private Context context;
    private boolean small = false;

    public int getMeasuredHeight() {
        return measuredHeight;
    }

    public int getMeasuredWidth() {
        return measuredWidth;
    }

    private class NumberHolder {
        BitmapLoader.BitmapNumber bgOld;
        BitmapLoader.BitmapNumber bgCurrent;
        float x = -1;

        public void setX(float x) {
            this.x = x;
        }

        float y;
        public int number = -1;
    }

    NumberHolder place1, place2;

    NumberHolder place3, place4;

    public BitmapClockDrawer(Context context) {
        this.context = context;
        bitmapLoader = new BitmapLoader(context);

        helpPaint = new Paint();
        helpPaint.setStyle(Paint.Style.FILL);
        place1 = new NumberHolder();
        place2 = new NumberHolder();
        place3 = new NumberHolder();
        place4 = new NumberHolder();
        minResHeight = context.getResources().getDimensionPixelSize(R.dimen.min_clock_height);
    }

    @Override
    public boolean updateTime(int hours, int minutes) {
        return updateTime(hours, minutes, true);
    }

    @Override
    public void setIs24h(boolean is24h) {
        this.is24h = is24h;
    }

    @Override
    public boolean updateTime(int fullHours, int minutes, boolean animate) {
        hours = is24h ? fullHours : fullHours % 12;
        if (getMeasuredHeight() == 0) {
            throw new RuntimeException("call measure before");
        }

        if (this.fullHours == fullHours && this.minutes == minutes) { return false; }
        this.fullHours = fullHours;
        this.minutes = minutes;

        BitmapLoader.BitmapNumber p1 = null, p2, p3, p4;
        if (hours / 10 != 0) {
            p1 = getNumber(hours / 10);
        }
        p2 = getNumber(hours % 10);
        p3 = getNumberMinutes(minutes / 10);
        p4 = getNumberMinutes(minutes % 10);

        AnimatorSet hoursSet = new AnimatorSet();
        List<Animator> hoursAnims = new ArrayList<>();
        List<HoursPositioning.Position> hoursPosition = HoursPositioning.hoursPositions.get(hours);
        List<HoursPositioning.Position> minutesPosition = HoursPositioning.minutesPositions.get(minutes);
        HoursPositioning.Position minutesOffset = HoursPositioning.minutesOffset.get(hours);

        if (hours / 10 != place1.number) {
            Animator e = updateNumber(place1, hours / 10, p1);
            if (e != null) { hoursAnims.add(e); }
            float newX = initialOffset() + getX(hoursPosition.get(0));
            ObjectAnimator e1 = placeAnimation(place1, newX);
            if (e1 != null) { hoursAnims.add(e1); }
        } else {
            float newX = initialOffset() + getX(hoursPosition.get(0));
            ObjectAnimator e1 = placeAnimation(place1, newX);
            if (e1 != null) { hoursAnims.add(e1); }
        }

        if (hours % 10 != place2.number) {
            Animator e = updateNumber(place2, hours % 10, p2);
            if (e != null) { hoursAnims.add(e); }
            float newX = initialOffset() + getX(hoursPosition.get(1));
            ObjectAnimator e1 = placeAnimation(place2, newX);
            if (e1 != null) { hoursAnims.add(e1); }
        } else {
            float newX = initialOffset() + getX(hoursPosition.get(1));
            ObjectAnimator e1 = placeAnimation(place2, newX);
            if (e1 != null) { hoursAnims.add(e1); }
        }

        if (minutes / 10 != place3.number) {
            Animator e1 = updateNumber(place3, minutes / 10, p3);
            if (e1 != null) { hoursAnims.add(e1); }
            float newX = initialOffset() + getX(minutesOffset, minutesPosition.get(0));
            ObjectAnimator e = placeAnimation(place3, newX);
            if (e != null) { hoursAnims.add(e); }
        } else {
            float newX = initialOffset() + getX(minutesOffset, minutesPosition.get(0));
            ObjectAnimator e = placeAnimation(place3, newX);
            if (e != null) { hoursAnims.add(e); }
        }

        if (minutes % 10 != place4.number) {
            Animator e1 = updateNumber(place4, minutes % 10, p4);
            if (e1 != null) { hoursAnims.add(e1); }
            float newX = initialOffset() + getX(minutesOffset, minutesPosition.get(1));
            ObjectAnimator e = placeAnimation(place4, newX);
            if (e != null) { hoursAnims.add(e); }
        } else {
            float newX = initialOffset() + getX(minutesOffset, minutesPosition.get(1));
            ObjectAnimator e = placeAnimation(place4, newX);
            if (e != null) { hoursAnims.add(e); }
        }

        hoursSet.playTogether(hoursAnims);
        hoursSet.start();
        //if (animate && animated) {
        //    animateNewPlace();
        //}

        return true;
    }

    private float initialOffset() {
        if (small) {
            List<HoursPositioning.Position> position = HoursPositioning.hoursPositions.get(hours);
            float x = 0;
            x -= getLeftOffset(position);
            switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.LEFT:
                    return x;
                case Gravity.RIGHT:
                    x += measuredWidth - (minWidth(hours, minutes, measuredHeight));
                    return x;
            }
            return x;
        }
        return 0;
    }

    private float getLeftOffset(List<HoursPositioning.Position> position) {
        float x = 0;
        if (position.get(0).number == -1) {
            x += getX(position.get(1).x);
        } else {
            x += getX(position.get(0).x);
        }
        if (hours >= 10) {
            x += getX(HoursPositioning.clockPads.get(hours / 10).first);
        } else {
            x += getX(HoursPositioning.clockPads.get(hours).first);
        }
        return x;
    }

    private ObjectAnimator placeAnimation(NumberHolder place1, float newX) {
        if (place1.x < 0) {
            place1.x = newX;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(place1, "x", place1.x, newX);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(1000);
        return objectAnimator;
    }

    //private void calcPlaceY() {
    //    int left = getLeftMargin();
    //    place1.x = left;
    //    left += calcWidth(place1.bgCurrent) + numberSpace;
    //    place2.x = left;
    //    left += calcWidth(place2.bgCurrent) + numberSpace;
    //    place3.x = left;
    //    left += calcWidth(place3.bgCurrent, numberScale) + numberSpace;
    //    place4.x = left;
    //}

    private void animateNewPlace() {

    }

    private Animator getMoveAnimation(NumberHolder place, int oldNumber) {
        if (place != null) {
            //List<HoursPositioning.Position> oldPosition = HoursPositioning.hoursPositions.get(oldNumber);
            //List<HoursPositioning.Position> oldPosition = HoursPositioning.hoursPositions.get(oldNumber);
        }
        return null;
    }

    private Animator getFrameAnimation(BitmapLoader.BitmapNumber number, int from, int to) {
        if (number == null) { return null; }
        return ObjectAnimator.ofInt(number, "frame", from, to);
    }

    private Animator updateNumber(final NumberHolder place, int newNumber, BitmapLoader.BitmapNumber p4) {
        if (animated) {
            List<Animator> animatorList = new ArrayList<>();

            int oldNumber = place.number;
            place.number = newNumber;
            place.bgOld = place.bgCurrent;
            place.bgCurrent = p4;

            AnimatorSet set = new AnimatorSet();
            Animator goneAnimation = getFrameAnimation(place.bgOld, 40, 80);
            if (goneAnimation != null) {
                goneAnimation.setInterpolator(new LinearInterpolator());
                goneAnimation.setDuration(1000);
                animatorList.add(goneAnimation);
            }
            Animator appearAnimation = getFrameAnimation(place.bgCurrent, 0, 40);
            if (appearAnimation != null) {
                appearAnimation.setInterpolator(new LinearInterpolator());
                if (goneAnimation != null) {
                    appearAnimation.setStartDelay(500);
                }
                appearAnimation.setDuration(1000);
                animatorList.add(appearAnimation);
            }

            set.playTogether(animatorList);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    synchronized (BitmapClockDrawer.this) {
                        place.bgOld = null;
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            //set.start();
            return set;
        } else {
            place.number = newNumber;
            place.bgOld = null;
            place.bgCurrent = p4;
            p4.setFrame(40);
            return null;
        }
    }

    //private int getLeftMargin() {
    //    final int majorGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
    //    switch (majorGravity) {
    //        case Gravity.LEFT:
    //            return 0;
    //        case Gravity.RIGHT:
    //            return getMeasuredWidth() - getTotalWidth();
    //        default:
    //            return (getMeasuredWidth() - getTotalWidth()) / 2;
    //    }
    //}
    //private int getTotalWidth() {
    //
    //    int left = calcWidth(place1.bgCurrent) + numberSpace;
    //    left += calcWidth(place2.bgCurrent) + numberSpace;
    //    left += calcWidth(place3.bgCurrent, numberScale) + numberSpace;
    //    left += calcWidth(place4.bgCurrent, numberScale);
    //
    //    return left;
    //}
    private Animator appearAnimation(BitmapClockDrawer bitmapClockDrawer, BitmapLoader.BitmapNumber bgCurrent, int newNumber) {
        return null;
    }

    private long getDuration() {
        return 1000;
    }

    private Animator goneAnimation(BitmapClockDrawer bitmapClockDrawer, BitmapLoader.BitmapNumber bgOld, int oldNumber) {
        return null;
    }

    private BitmapLoader.BitmapNumber getNumberMinutes(int i) {
        return bitmapLoader.createNumberMinutes(i);
    }

    private BitmapLoader.BitmapNumber getNumber(int i) {
        return bitmapLoader.createNumber(i);
    }

    @Override
    public void setNumberSpace(int numberSpace) {

    }

    @Override
    public void setNumberScale(int numberScale) {
        this.numberScale = numberScale;
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
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public void updateSize(int clockWidth, int clockHeight) {
        measure(clockWidth, clockHeight);
        //calcPlaceY();
    }

    @Override
    public void setVectorNumberAnimator(IVectorNumberAnimator vectorNumberAnimator) {

    }

    @Override
    public void draw(Bitmap canvas) {
        canvas.eraseColor(Color.TRANSPARENT);
        draw(new Canvas(canvas));
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.save();

        canvas.save();

        //draw hours
        canvas.translate(place1.x, 0);
        drawNumber(canvas, place1, place1.bgOld, 100);
        drawNumber(canvas, place1, place1.bgCurrent, 100);
        canvas.restore();

        canvas.save();
        canvas.translate(place2.x, 0);

        drawNumber(canvas, place2, place2.bgOld, 100);
        drawNumber(canvas, place2, place2.bgCurrent, 100);
        canvas.restore();

        //draw minutes
        canvas.save();
        canvas.translate(place3.x, 0);
        canvas.translate(0, -context.getResources().getDimensionPixelSize(R.dimen.minute_bottom_padding));
        drawNumber(canvas, place3, place3.bgOld, numberScale);
        drawNumber(canvas, place3, place3.bgCurrent, numberScale);
        canvas.restore();

        canvas.save();
        canvas.translate(place4.x, 0);
        canvas.translate(0, -context.getResources().getDimensionPixelSize(R.dimen.minute_bottom_padding));
        drawNumber(canvas, place4, place4.bgOld, numberScale);
        drawNumber(canvas, place4, place4.bgCurrent, numberScale);
        canvas.restore();

        canvas.restore();
        //canvas.save();
        //canvas.translate(place2.x, place2.y);
        //drawNumber(canvas, place2, place2.bgOld, 100);
        //drawNumber(canvas, place2, place2.bgCurrent, 100);
        //canvas.restore();
        //
        //canvas.save();
        //canvas.translate(place3.x, place3.y);
        //drawNumber(canvas, place3,place3.bgOld, numberScale);
        //drawNumber(canvas, place3, place3.bgCurrent, numberScale);
        //canvas.restore();
        //
        //canvas.save();
        //canvas.translate(place4.x, place4.y);
        //
        //drawNumber(canvas, place4, place4.bgOld, numberScale);
        //drawNumber(canvas, place4, place4.bgCurrent, numberScale);
        //canvas.restore();
    }

    private float getMinutesTranslate() {
        List<HoursPositioning.Position> pos = HoursPositioning.hoursPositions.get(hours);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (pos.get(0).number == -1) {
            return pos.get(1).x * metrics.density;
        }
        return pos.get(0).x * metrics.density;
    }

    private float getX(HoursPositioning.Position minutesOffset, HoursPositioning.Position position) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return getMinutesTranslate() + minutesOffset.x * metrics.density + position.x * metrics.density;
        //return objectAnimator;
    }

    private float getX(HoursPositioning.Position position) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return position.x * metrics.density;
    }

    private float getX(float x) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return x * metrics.density;
    }

    private void drawNumber(Canvas canvas, NumberHolder holder, BitmapLoader.BitmapNumber bitmap, int percent) {
        int c = canvas.save();
        if (bitmap != null) {
            Paint p = new Paint();
            p.setColor(Color.argb(50, 255, 0, 0));
            p.setStyle(Paint.Style.FILL);

            Drawable frame = bitmap.getFrame();
            frame.setBounds(0, 0, frame.getIntrinsicWidth(), frame.getIntrinsicHeight());
            canvas.translate(0, getMeasuredHeight() - frame.getIntrinsicHeight());
            canvas.drawRect(0, 0, frame.getIntrinsicWidth(), frame.getIntrinsicHeight(), p);
            frame.draw(canvas);
            canvas.translate(0, -(getMeasuredHeight() - frame.getIntrinsicHeight()));
        }
        canvas.restoreToCount(c);
    }

    private int calcWidth(Drawable bitmap, int percent) {
        return calcWidth(bitmap, getMeasuredHeight(), percent);
    }

    private int calcWidth(Drawable drawable) {
        return calcWidth(drawable, 100);
    }

    private int calcWidth(Drawable drawable, int height, int perc) {
        if (drawable == null) { return 0; }
        return Math.round(drawable.getIntrinsicWidth() * ((height * perc / 100) / (float) drawable.getIntrinsicHeight()));
    }

    @Override
    public void measure(int width, int height) {
        measuredWidth = width;
        measuredHeight = height;
    }

    @Override
    public void setSmall(boolean small) {
        this.small = small;
    }

    @Override
    public int getMinWidth(int height) {
        return Math.round(minWidth(24, 0) * (height / (float) minResHeight));
    }

    public float minWidth(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return minWidth(hours, minutes);
    }

    public float minWidth(int hours, int minutes, int height) {
        return minWidth(hours, minutes) * height / minResHeight;
    }

    public float minWidth(int hours, int minutes) {
        float width = 0;

        List<HoursPositioning.Position> hourPosition = HoursPositioning.hoursPositions.get(hours);
        List<HoursPositioning.Position> minutesPosition = HoursPositioning.minutesPositions.get(minutes);
        HoursPositioning.Position minutesOffset = HoursPositioning.minutesOffset.get(hours);
        if (hourPosition.get(0).number != -1) {
            width = width + getX(hourPosition.get(0).x);
        } else {
            width = width + getX(hourPosition.get(1).x);
        }
        width += getX(minutesOffset.x);
        width += getX(minutesPosition.get(1).x) + getNumberMinutes(minutesPosition.get(1).number).getFrame(40).getIntrinsicWidth();
        if (small) {
            if (hours == 24) {
                hours = 0;
            }
            if (hours >= 10) {
                width -= (getX(HoursPositioning.clockPads.get(hours / 10).first));
                if (hourPosition.get(0).number != -1) {
                    width -=getX(hourPosition.get(0).x);
                } else {
                    width -= getX(hourPosition.get(1).x);
                }
            } else {
                width -= getX(HoursPositioning.clockPads.get(hours).first);
                if (hourPosition.get(0).number != -1) {
                    width -=getX(hourPosition.get(0).x);
                } else {
                    width -= getX(hourPosition.get(1).x);
                }
            }
            width -= getX(HoursPositioning.clockPads.get(minutes % 10).second);
        }
        return width;
    }

    public Pair<Integer, Integer> minClockPads() {
        int hours = 24;
        int minutes = 0;
        List<HoursPositioning.Position> hoursPosition = HoursPositioning.hoursPositions.get(hours);
        List<HoursPositioning.Position> minutesPosition = HoursPositioning.minutesPositions.get(minutes);
        HoursPositioning.Position minutesOffset = HoursPositioning.minutesOffset.get(hours);

        return Pair.create(0, 0);
    }
}
