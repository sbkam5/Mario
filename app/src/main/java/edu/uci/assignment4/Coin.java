package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

public class Coin extends Obstacle{
    private Bitmap coin;

    public Coin(Context context){
        shape = new Rect();
        coin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        width = 100;
        height = 100;
    }

    public void draw(Canvas canvas){
            canvas.drawRect(shape, paint);  //if player hasnt broken contrainer, draw the container
    }
}
