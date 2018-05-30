package edu.uci.assignment4;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Obstacle{

    private Rect shape;
    private int color;

    public Obstacle(Rect r, int c){
        shape = r;
        color = c;
    }

    public Rect getLoc(){
        return shape;
    }

    public Point playerCollide(Player p, Rect obstacle, Point point){
        Point temp = point;
        int xFinal = obstacle.right;
        int yFinal = obstacle.bottom;

        int deltaX = Math.abs((p.getX() + p.getWidth())  - xFinal);
        int deltaY = Math.abs((p.getY() + p.getHeight()) - yFinal);

        if(deltaY <= deltaX){
            if (deltaX < 0) { //player is to left of object
                temp.x -= 10;  //if the player runs into obstacle from the side.
            }
            else{
                temp.x += 10;
            }
        }
        else{
            if(deltaY < 0){  //player is on top of object
                temp.y -= 10;
            }
            else{
                temp.y += 10;
            }
        }

        return temp;
    }

    public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setColor(color);
        canvas.drawRect(shape, p);
    }

    public void update(){

    }
}
