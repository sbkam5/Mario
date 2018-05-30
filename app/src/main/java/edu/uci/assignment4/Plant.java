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
    private int y, minY;
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

    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
        minY = y - height;
        pot.set(this.x, this.y, this.x+potWidth, this.y+potHeight);
        loc.set(this.x+25, this.y+25, this.x+25+width, this.y+25+height);
    }

    public void draw(Canvas canvas){
        canvas.drawRect(pot.left, pot.top, pot.right, pot.bottom, paint);
        canvas.drawBitmap(head, null, loc, null);
    }

    public void update(){
        if(state == 0){
            state = 1;
        }
        else if(state == 1){
            y -= 5;
            loc.set(x+25, y , x+25+width, y+height);   //if rising, decrement y.
            if(y <= minY){
                state = 2;   //if at max height, start making head go back into pipe.
            }
        }
        else if (state == 2){
            y += 5;   //this is the state when plant starts going back into pipe.
            if (y - 25 >= pot.top){
                y = pot.top + 25;  //if y is too high, it means character is already grounded.
                state = 3;  //also player can do jump command again.
            }
            loc.set(x+25, y , x+25+width, y+height);
        }
        else{
            state = 0;   //state 3 is the "wait" state.
        }
    }

    public Point playerCollide(Player p, Rect obstacle, Point point){
        Point temp = point;
        int xFinal = obstacle.right;
        int yFinal = obstacle.bottom;

        int deltaX = (p.getX() + p.getWidth())  - xFinal;
        int deltaY = (p.getY() + p.getHeight()) - yFinal;

        if(Math.abs(deltaY) <= Math.abs(deltaX)){
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

    public Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
