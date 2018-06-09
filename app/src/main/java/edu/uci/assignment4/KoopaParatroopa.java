package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class KoopaParatroopa implements GameObject {
    protected Bitmap koopa;
    private int x;
    private int y;
    private int width;
    private int height;
    private int jump = 1;
    private int jumph;
    private Rect loc;

    public KoopaParatroopa(Context c){
        koopa = BitmapFactory.decodeResource(c.getResources(), R.drawable.koopaparatroopa);
        x = 500;
        y = 300;
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

    public int getJump(){
        return jump;
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

    public boolean update(int height){
        jumph = height/2;
        x -= 10;
        if (x <= -300){
            return true;
        }
        if (jump == 1) {
            y -= 10;
            setLocation(x, y);
            if (y <= jumph) {
                jump = 2;
            }
        }
        else if (jump == 2){
            y += 10;
            setLocation(x,y);
            if (y >= height - this.height){
                jump = 1;
            }
        }
        return false;
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
        canvas.drawBitmap(koopa, null, loc, null);
    }



}
