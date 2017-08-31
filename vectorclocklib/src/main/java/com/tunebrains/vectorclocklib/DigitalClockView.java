package com.tunebrains.vectorclocklib;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 8/31/17.
 */

public class DigitalClockView extends View {

    private DigitalClockDrawer drawer;

    public DigitalClockView(Context context) {
        super(context);
        init();
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

    private void init() {
        drawer = new DigitalClockDrawer(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawer.draw(canvas);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        drawer.measure(getMeasuredWidth(), getMeasuredHeight());
    }

    public void updateTime(int hours, int minutes) {
        drawer.updateTime(hours, minutes);
    }

    public void updateTime(int hours, int minutes, boolean animate) {
        drawer.updateTime(hours, minutes, animate);
    }

    public void setNumberSpace(int numberSpace) {
        drawer.setNumberSpace(numberSpace);
    }

    public void setNumberScale(int numberScale) {
        drawer.setNumberScale(numberScale);
    }

    public void setVectorNumberAnimator(IVectorNumberAnimator vectorNumberAnimator) {
        drawer.setVectorNumberAnimator(vectorNumberAnimator);
    }
}
