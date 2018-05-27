package edu.uci.assignment4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public interface GameObject {
    public abstract void draw(Canvas c);
    public abstract void update();
}
