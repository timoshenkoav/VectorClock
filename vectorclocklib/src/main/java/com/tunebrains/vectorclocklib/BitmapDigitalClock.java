package com.tunebrains.vectorclocklib;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import com.tunebrains.vectorclocklib.bitmap.BitmapClockDrawer;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 10/2/17.
 */

public class BitmapDigitalClock extends View {
    private BitmapClockDrawer drawer;
    private VectorNumberAnimator vectorNumberAnimator;
    private int minWidth;
    private int minHeight;

    public BitmapDigitalClock(Context context) {
        super(context);
        init();
    }

    public BitmapDigitalClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BitmapDigitalClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BitmapDigitalClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setIs24h(boolean is24h) {
        drawer.setIs24h(is24h);
    }

    public boolean updateTime(final long l) {
        if (drawer.getMeasuredWidth() == 0) {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    updateTime(l);
                    return false;
                }
            });
            return false;
        }
        return drawer.updateTime(l);
    }

    private void init() {
        drawer = new BitmapClockDrawer(getContext());
        vectorNumberAnimator = new VectorNumberAnimator(getContext());
        drawer.setVectorNumberAnimator(vectorNumberAnimator);
        drawer.setIs24h(true);
        minWidth = getResources().getDimensionPixelSize(R.dimen.min_clock_width);
        minHeight = getResources().getDimensionPixelSize(R.dimen.min_clock_height);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        if (width < minWidth) {
            width = minWidth;
        }
        if (height < minHeight || height > minHeight) { height = minHeight; }
        setMeasuredDimension(width, height);

        drawer.measure(width, height);
    }
}
