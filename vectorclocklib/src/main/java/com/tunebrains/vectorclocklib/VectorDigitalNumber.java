package com.tunebrains.vectorclocklib;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 8/28/17.
 */

public class VectorDigitalNumber extends View {
    VectorMasterDrawable bgOld, bgCurrent;
    public VectorDigitalNumber(Context context) {
        super(context);
    }

    public VectorDigitalNumber(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VectorDigitalNumber(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VectorDigitalNumber(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bgOld!=null) {
            bgOld.draw(canvas);
        }
        if (bgCurrent!=null){
            bgCurrent.draw(canvas);
        }
        postInvalidate();
    }

    public void updateView(VectorMasterDrawable drawable){
        bgOld = bgCurrent;
        bgCurrent = drawable;

        bgCurrent.setBounds(0,0, bgCurrent.getIntrinsicWidth(), bgCurrent.getIntrinsicHeight());
    }
}
