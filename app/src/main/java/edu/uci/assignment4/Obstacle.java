package edu.uci.assignment4;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Obstacle implements GameObject{

    private Rect shape;
    private int x, y, width, height;
    private Paint paint = new Paint();

    public Obstacle(){
        shape = new Rect();
        paint.setColor(Color.BLACK);
    }

    public void setShape(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        shape.set(x, y, x+width, y+height);
    }

    public void setLocation(int x, int y){
        //does nothing
    }

    public void moveRight(){
        x += 10;
        shape.set(x, y, x + width, y + height);
    }

    public void moveLeft(){
        x -= 10;
        shape.set(x, y, x + width, y + height);
    }

    public Rect getLocation(){
        return shape;
    }

    public int playerCollide(Player p, Rect obstacle, Point point){
        int distanceFromBottom = p.getLocation().top - obstacle.bottom;
        int distanceFromTop = p.getLocation().bottom - obstacle.top;
        int distanceFromRight = p.getLocation().left - obstacle.right;
        int distanceFromLeft = p.getLocation().right - obstacle.left;
        int horizontalSide;   //1 is for right, 2 is for left;
        int verticalSide;     //1 is for top, 2 is for the bottom;

        int horizontalMin = distanceFromLeft;
        horizontalSide = 2;
        if(Math.abs(distanceFromRight) < Math.abs(horizontalMin)){
            horizontalMin = distanceFromRight;
            horizontalSide = 1;
        }

        int verticalMin = distanceFromTop;
        verticalSide = 1;
        if(Math.abs(distanceFromBottom) < Math.abs(verticalMin)){
            verticalMin = distanceFromBottom;
            verticalSide = 2;
        }

        if(Math.abs(horizontalMin) <= Math.abs(verticalMin)){

            return 1;  //lets boardview know this intersection occured from the sides.
        }
        else{
            if(verticalSide == 1){  //player is on top of object
                point.y -= 10;
                return 2;  //lets boardview know intersection occured from the top.
            }
            else{
                point.y += 10;  //lets boardview know intersection occured from the bottom.
                return 3;
            }
        }
    }

    public void draw(Canvas canvas){
        canvas.drawRect(shape, paint);
    }

    public void update(){

    }
}
