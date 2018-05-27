package edu.uci.assignment4;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject {

    private Rect shape;
    private int color;

    public Obstacle(Rect r, int c){
        shape = r;
        color = c;
    }

    public Rect getLoc(){
        return shape;
    }

    public boolean playerCollide(Player p){
        if(shape.contains(p.getLocation().left, p.getLocation().top)
                || shape.contains(p.getLocation().right, p.getLocation().top)
                || shape.contains(p.getLocation().left,  p.getLocation().bottom)
                || shape.contains(p.getLocation().right, p.getLocation().bottom)){

            return true;  //if the obstacle contains any portion of the player, a collision occurs and return true

        }

        return false;  //otherwise return false to signal no collision.
    }

    public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setColor(color);
        canvas.drawRect(shape, p);
    }

    public void update(){

    }
}
