package com.tunebrains.vectorclocklib;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
    private int currentNumber = -1;

    public interface IVectorNumberAnimator {

        Animator goneAnimation(Object obj, VectorMasterDrawable bgOld, int oldNumber);

        Animator appearAnimation(Object obj, VectorMasterDrawable bgCurrent, int newNumber);

        VectorMasterDrawable getNumber(int number);
    }

    public void setVectorNumberAnimator(IVectorNumberAnimator vectorNumberAnimator) {
        this.vectorNumberAnimator = vectorNumberAnimator;
    }

    IVectorNumberAnimator vectorNumberAnimator;
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
        if (newNumber == currentNumber) {
            updateMeasure();
            return;
        }
        int oldNumber = currentNumber;
        currentNumber = newNumber;
        bgOld = bgCurrent;
        bgCurrent = drawable;
        updateMeasure();
        if (bgOld!=null) {
            if (vectorNumberAnimator == null)
                return;

            Animator goneAnimation = vectorNumberAnimator.goneAnimation(this, bgOld, oldNumber);
            Animator appearAnimation = vectorNumberAnimator.appearAnimation(this,bgCurrent, newNumber);
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
                    synchronized (VectorDigitalNumber.this){
                        bgOld = null;
                        updateMeasure();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //updateMeasure();
    }

    private void updateMeasure() {
        //int maxW = 0;
        //if (bgOld != null)
        //    maxW = Math.max(maxW, calcWidth(bgOld));
        //if (bgCurrent != null)
        //    maxW = Math.max(maxW, calcWidth(bgCurrent));
        //setMeasuredDimension(maxW, getMeasuredHeight());
        //requestLayout();
    }

    private int calcWidth(VectorMasterDrawable bgOld) {
        return Math.round(bgOld.getIntrinsicWidth()*(getMeasuredHeight()/(float)bgOld.getIntrinsicHeight()));
    }
}
