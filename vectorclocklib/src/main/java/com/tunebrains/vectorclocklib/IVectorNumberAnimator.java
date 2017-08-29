package com.tunebrains.vectorclocklib;

import android.animation.Animator;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;

public interface IVectorNumberAnimator {

        Animator goneAnimation(Object obj, VectorMasterDrawable bgOld, int oldNumber);

        Animator appearAnimation(Object obj, VectorMasterDrawable bgCurrent, int newNumber);

        VectorMasterDrawable getNumber(int number);

        long getDuration();
    }