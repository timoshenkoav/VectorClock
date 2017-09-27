package com.tunebrains.vectorclocklib.bitmap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 9/27/17.
 */

public class BitmapLoader {

    public BitmapNumber getNumber(int number) {
        return cacheNumbers.get(number);
    }

    public BitmapNumber createNumber(int i) {
        BitmapNumber number = new BitmapNumber(i);
        return number;
    }

    public class BitmapNumber {
        int number;
        private List<Drawable> bitmaps;

        public BitmapNumber(int i) {
            number = i;
            bitmaps = new ArrayList<>();
        }

        public int currentFrame = 0;

        public void setFrame(int frame) {
            this.currentFrame = frame;
        }

        public Drawable getFrame() {
            return loadFrame(number, currentFrame);
        }

        private Drawable loadFrame(int number, int currentFrame) {
            return getDrawable(number, currentFrame);
        }
    }

    private HashMap<Integer, BitmapNumber> cacheNumbers;

    public BitmapLoader(Context context) {
        this.context = context;
        cacheNumbers = new HashMap<>();
        load();
    }

    private void load() {
        for (int i=0;i<10;++i) {
            loadNumber(i);
        }
    }

    private void loadNumber(int i) {
        BitmapNumber number = new BitmapNumber(i);
        //loadFrames(i, 0, 80, number.bitmaps);
        cacheNumbers.put(i, number);
    }

    private void loadFrames(int number, int start, int end, List<Drawable> appearBitmaps) {
        for (int i = start; i <= end; ++i) {
            Drawable drawable = getDrawable(number, i);
            appearBitmaps.add(drawable);
        }
    }

    private Drawable getDrawable(int number, int frame) {
        String resid = String.format("h0%d_%05d", number, frame);
        int id = context.getResources().getIdentifier(resid, "drawable", context.getPackageName());
        return context.getResources().getDrawable(id);
    }

    private Context context;
}
