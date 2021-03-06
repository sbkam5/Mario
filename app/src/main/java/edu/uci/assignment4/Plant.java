package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Plant implements GameObject {

    private Bitmap head;
    private Rect pot, loc;
    private int x;
    private int headY, potY, minY;
    private int width, potWidth;
    private int height, potHeight;
    private int state;  //0 means plant is hiding, 1 means plant is rising, 2 means planet is going down.
    private Paint paint = new Paint();

    public Plant(Context c){
        state = 3;
        potWidth = 250;
        potHeight =250;
        width = 200;
        height = 200;
        pot = new Rect();
        loc = new Rect();
        head = BitmapFactory.decodeResource(c.getResources(), R.drawable.pirahna);
        head = rotateBitmap(head, 90);
        paint.setColor(Color.GREEN);
    }

    public int getPotHeight(){
        return potHeight;
    }

    public Rect getPot(){
        return pot;
    }

    public Rect getLocation(){
        return loc;
    }

    public int getState(){
        return state;
    }

    public void moveLeft(){
        this.x -= 10;
        pot.set(this.x, this.potY, this.x+potWidth, this.potY+potHeight);
        loc.set(this.x+25, this.headY, this.x+25+width, this.headY+height);
    }

    public void moveRight(){
        this.x += 10;
        pot.set(this.x, this.potY, this.x+potWidth, this.potY+potHeight);
        loc.set(this.x+25, this.headY, this.x+25+width, this.headY+height);
    }

    public void setLocation(int x, int y){
        this.x = x;
        this.potY = y;
        this.headY = y + 25;
        minY = y - height;
        pot.set(this.x, potY, this.x+potWidth, potY+potHeight);
        loc.set(this.x+25, headY, this.x+25+width, headY+height);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(head, null, loc, null);
        canvas.drawRect(pot.left, pot.top, pot.right, pot.bottom, paint);
    }

    public void update(){
        if(state == 0){
            state = 1;
        }
        else if(state == 1){
            headY -= 5;
            loc.set(x+25, headY , x+25+width, headY+height);   //if rising, decrement y.
            if(headY <= minY){
                state = 2;   //if at max height, start making head go back into pipe.
            }
        }
        else if (state == 2){
            headY += 5;   //this is the state when plant starts going back into pipe.
            if (headY - 25 >= pot.top){
                headY = pot.top + 25;  //if y is too high, it means character is already grounded.
                state = 3;  //also player can do jump command again.
            }
            loc.set(x+25, headY , x+25+width, headY+height);
        }
        else{
            state = 0;   //state 3 is the "wait" state.
        }
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

    public Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
