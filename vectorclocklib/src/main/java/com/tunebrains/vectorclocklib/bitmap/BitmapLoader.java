package com.tunebrains.vectorclocklib.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 9/27/17.
 */

public class BitmapLoader {
    private Bitmap minutesBitmap;
    private Bitmap hoursBitmap;

    public BitmapNumber createNumber(int i) {
        BitmapNumber number = new BitmapNumber(i);
        return number;
    }

    public BitmapNumber createNumberMinutes(int i) {
        BitmapNumber number = new BitmapNumber(i);
        number.minutes = true;
        return number;
    }

    public class BitmapNumber {
        int number;
        private List<Drawable> bitmaps;
        public boolean minutes;

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

        public Bitmap getBitmapFrame() {
            return loadBitmapFrame(number, currentFrame);
        }

        public Bitmap getBitmapFrame(int frame) {
            return loadBitmapFrame(number, frame);
        }

        public Drawable getFrame(int frame) {
            return loadFrame(number, frame);
        }

        private Drawable loadFrame(int number, int currentFrame) {
            if (minutes) {
                return getDrawableMinutes(number, currentFrame);
            }
            return getDrawable(number, currentFrame);
        }

        private synchronized Bitmap loadBitmapFrame(int number, int currentFrame) {
            if (minutes) {
                return getBitmapMinutes(number, currentFrame);
            }
            return getBitmap(number, currentFrame);
        }
    }

    public BitmapLoader(Context context) {
        this.context = context;
        load();
    }

    private void load() {
        BitmapNumber minutes = new BitmapNumber(0);
        minutes.minutes = true;
        Bitmap sampleMinute = minutes.getBitmapFrame(40);
        minutes.minutes = false;
        Bitmap sampleHours = minutes.getBitmapFrame(40);

        if (sampleHours != null) {
            hoursBitmap = Bitmap.createBitmap(sampleHours.getWidth(), sampleHours.getHeight(), Bitmap.Config.ARGB_8888);
        }
        if (sampleMinute != null) {
            minutesBitmap = Bitmap.createBitmap(sampleMinute.getWidth(), sampleMinute.getHeight(), Bitmap.Config.ARGB_8888);
        }
    }

    private Drawable getDrawableMinutes(int number, int currentFrame) {
        String resid = String.format("m0%d_%05d", number, currentFrame);
        int id = context.getResources().getIdentifier(resid, "drawable", context.getPackageName());
        return context.getResources().getDrawable(id);
    }

    private Drawable getDrawable(int number, int frame) {
        String resid = String.format("h0%d_%05d", number, frame);
        int id = context.getResources().getIdentifier(resid, "drawable", context.getPackageName());
        return context.getResources().getDrawable(id);
    }

    private Bitmap getBitmapMinutes(int number, int currentFrame) {
        String resid = String.format("m0%d_%05d", number, currentFrame);
        int id = context.getResources().getIdentifier(resid, "drawable", context.getPackageName());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inBitmap = minutesBitmap;
        return BitmapFactory.decodeResource(context.getResources(), id, options);
    }

    private Bitmap getBitmap(int number, int frame) {
        String resid = String.format("h0%d_%05d", number, frame);
        int id = context.getResources().getIdentifier(resid, "drawable", context.getPackageName());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inBitmap = hoursBitmap;
        return BitmapFactory.decodeResource(context.getResources(), id, options);
    }

    private Context context;
}
