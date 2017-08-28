package com.tunebrains.vectorclocklib;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 8/28/17.
 */

public class VectorDigitalNumber extends View {
    private int currentNumber = -1;

    public interface VectorNumberAnimator{

    }
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

        synchronized (this) {
            if (bgOld != null) {
                bgOld.setBounds(0,0, getMeasuredWidth(), getMeasuredHeight());
                bgOld.draw(canvas);
            }
            if (bgCurrent != null) {
                bgCurrent.setBounds(0,0, getMeasuredWidth(), getMeasuredHeight());
                bgCurrent.draw(canvas);
            }
        }
        postInvalidate();
    }

    public synchronized void updateView(VectorMasterDrawable drawable, int newNumber){
        if (newNumber == currentNumber)
            return;
        currentNumber = newNumber;
        bgOld = bgCurrent;
        bgCurrent = drawable;

        if (bgOld!=null) {
            // find the correct path using name
            final PathModel outline = bgOld.getPathModelByName("outline");
            //final PathModel outline2 = heartVector.getPathModelByName("outline2");

            // set trim path start (values are given in fraction of length)
            outline.setTrimPathEnd(1.0f);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    // set trim end value and update view
                    outline.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                    synchronized (VectorDigitalNumber.this) {
                        if (bgOld!=null) {
                            bgOld.update();
                        }
                    }
                }
            });

            // find the correct path using name
            final PathModel outline2 = bgCurrent.getPathModelByName("outline");
            //final PathModel outline2 = heartVector.getPathModelByName("outline2");

            // set trim path start (values are given in fraction of length)
            outline2.setTrimPathEnd(0.0f);
            ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            valueAnimator2.setDuration(1000);
            valueAnimator2.setStartDelay(900);
            valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    // set trim end value and update view
                    outline2.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                    synchronized (VectorDigitalNumber.this) {
                        if (bgCurrent!=null) {
                            bgCurrent.update();
                        }
                    }
                }
            });

            AnimatorSet set = new AnimatorSet();
            set.playTogether(valueAnimator, valueAnimator2);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    synchronized (VectorDigitalNumber.this){
                        bgOld = null;
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

        }
    }
}
