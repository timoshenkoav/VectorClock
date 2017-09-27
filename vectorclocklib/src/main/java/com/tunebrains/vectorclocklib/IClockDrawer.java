package com.tunebrains.vectorclocklib;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 9/27/17.
 */

public interface IClockDrawer {
    boolean updateTime(int hours, int minutes);

    void setIs24h(boolean is24h);

    boolean updateTime(int fullHours, int minutes, boolean animate);

    void setNumberSpace(int numberSpace);

    void setNumberScale(int numberScale);

    boolean updateTime(long l);

    void setAnimated(boolean animated);

    void setGravity(int gravity);

    void updateSize(int clockWidth, int clockHeight);

    void setVectorNumberAnimator(IVectorNumberAnimator vectorNumberAnimator);

    void draw(Bitmap canvas);

    void draw(Canvas canvas);

    void measure(int width, int height);

    int getMinWidth(int height);
}
