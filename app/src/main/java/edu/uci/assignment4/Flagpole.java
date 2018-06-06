package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

public class Flagpole extends Obstacle{
    private Bitmap image;
    private boolean broken = false;  //all power ups start up stored inside their box

    public Flagpole(Context context){
        shape = new Rect();
        paint.setColor(Color.YELLOW);
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.flagpole);
        width = 400;
        height = 400;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, null, shape, null);  // If player HAS broken container, draw the powerup;
    }

}
