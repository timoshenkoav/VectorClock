package com.tunebrains.vectorclocklib;

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

    public void updateTime(int hours, int minutes) {

        VectorMasterDrawable p1 = null, p2, p3, p4;
        if (hours / 10 != 0) {
            p1 = new VectorMasterDrawable(getContext(), valRes(hours / 10));
        }
        p2 = new VectorMasterDrawable(getContext(), valRes(hours % 10));
        p3 = new VectorMasterDrawable(getContext(), valRes(minutes / 10));
        p4 = new VectorMasterDrawable(getContext(), valRes(minutes % 10));
        place1.number = hours / 10;
        place2.number = hours % 10;
        place3.number = minutes / 10;
        place4.number = minutes % 10;
        if (place1.bgOld == null) {
            place1.bgCurrent = p1;
        }
        if (place2.bgOld == null) {
            place2.bgCurrent = p2;
        }
        if (place3.bgOld == null) {
            place3.bgCurrent = p3;
        }
        if (place4.bgOld == null) {
            place4.bgCurrent = p4;
        }
    }

    private class NumberHolder {
        VectorMasterDrawable bgOld;
        VectorMasterDrawable bgCurrent;
        int x;
        int y;
        public int number;
    }

    NumberHolder place1, place2, place3, place4;
    boolean initialSet;

    public void setNumberAnimator(VectorDigitalNumber.IVectorNumberAnimator numberAnimator) {
        this.numberAnimator = numberAnimator;
    }

    VectorDigitalNumber.IVectorNumberAnimator numberAnimator;

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

        calcPlace();

        canvas.save();
        canvas.translate(place1.x, place1.y);
        if (place1.bgCurrent != null) {
            place1.bgCurrent.setBounds(0, 0, calcWidth(place1.bgCurrent), getMeasuredHeight());

            place1.bgCurrent.draw(canvas);
        }
        canvas.restore();

        canvas.save();
        canvas.translate(place2.x, place2.y);
        if (place2.bgCurrent != null) {
            place2.bgCurrent.setBounds(0, 0, calcWidth(place2.bgCurrent), getMeasuredHeight());
            drawInstinct(canvas, place2.bgCurrent, Color.BLUE);
            place2.bgCurrent.draw(canvas);
        }
        canvas.restore();

        canvas.save();
        canvas.translate(place3.x, place3.y);
        if (place3.bgCurrent != null) {
            canvas.translate(0, getMeasuredHeight() - getMeasuredHeight() * SMALL_NUMBER_PERCENT / 100);
            place3.bgCurrent.setBounds(0, 0, calcWidth(place3.bgCurrent, SMALL_NUMBER_PERCENT), getMeasuredHeight() * SMALL_NUMBER_PERCENT / 100);
            place3.bgCurrent.draw(canvas);
        }
        canvas.restore();
        canvas.save();
        canvas.translate(place4.x, place4.y);
        if (place4.bgCurrent != null) {
            canvas.translate(0, getMeasuredHeight() - getMeasuredHeight() * SMALL_NUMBER_PERCENT / 100);
            place4.bgCurrent.setBounds(0, 0, calcWidth(place4.bgCurrent, SMALL_NUMBER_PERCENT), getMeasuredHeight() * SMALL_NUMBER_PERCENT / 100);
            place4.bgCurrent.draw(canvas);
        }
        canvas.restore();
        invalidate();
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
}
