package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Goomba implements GameObject {
    protected Bitmap pic;
    private int x;
    private int y;
    private int width;
    private int height;
    private Rect loc;

    public Goomba(Context context){
        pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.goomba);
        x = 1000;
        y = 200;
        width = 200;
        height = 200;
        loc = new Rect(x, y, x + width, y + height);
    }

    public int getHeight(){
        return  height;
    }

    public Rect getLocation(){
        return loc;
    }

    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
        loc.set(x, y, x + width, y + height);
    }

    public void moveLeft(){
        x -= 10;
        loc.set(x, y, x + width, y + height);
    }

    public void moveRight(){
        x += 10;
        loc.set(x, y, x + width, y + height);
    }

    public void update(int player_x){
        if(x < player_x){
            x += 5;
        }
        else{
            x -= 5;
        }
        loc.set(x, y, x + width, y + height);
    }

    public boolean killedByPlayer(Player p){
        int distanceFromBottom = p.getLocation().top - loc.bottom;
        int distanceFromTop = p.getLocation().bottom - loc.top;
        int distanceFromRight = p.getLocation().left - loc.right;
        int distanceFromLeft = p.getLocation().right - loc.left;
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

            return false;  //lets boardview know this intersection occured from the sides.
        }
        else{
            if(verticalSide == 1){  //player is on top of object
                return true;
            }
            else{
                //lets boardview know intersection occured from the bottom.
                return false;
            }
        }
    }

    @Override
    public void update(){

    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(pic, null, loc, null);
    }
}
