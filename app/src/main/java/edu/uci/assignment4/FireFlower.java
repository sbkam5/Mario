package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class FireFlower extends Obstacle{

    private Bitmap powerUpImage;
    private boolean broken = false;  //all power ups start up stored inside their box

    public FireFlower(Context context){
        shape = new Rect();
        paint.setColor(Color.YELLOW);
        powerUpImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireflower);
        width = 100;
        height = 100;
    }

    public boolean isBroken(){
        return broken;
    }

    public int playerCollide(Player p, Rect obstacle, Point point){
        int r = super.playerCollide(p, obstacle, point);
        if(r == 3){
            broken = true;
        }
        return r;
    }

    public void draw(Canvas canvas){
        if(!broken) {
            canvas.drawRect(shape, paint);  //if player hasnt broken contrainer, draw the container
        }
        else{
            canvas.drawBitmap(powerUpImage, null, shape, null);  // If player HAS broken container, draw the powerup;
        }
    }
}
