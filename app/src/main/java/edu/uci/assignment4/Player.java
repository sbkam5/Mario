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

public class Player implements GameObject {

    protected Bitmap pic;
    private int x;
    private int y;
    private int width;
    private int height;
    private int superWidth;
    private int superHeight;
    private Rect loc;
    private int direction = 0;
    private int state;

    public Player(Context context){

        pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        x = 250;
        y = 250;
        width = 150;
        height = 150;
        superWidth = superHeight = width+50;
        loc = new Rect(x, y, x + width, y + height);
    }

    public void setState(int x){
        state = x;
    }

    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
        if(state == 0) {
            loc.set(x, y, x + width, y + height);
        }
        else{
            loc.set(x, y, x + superWidth, y + superHeight);
        }
    }

    public void moveLeft(){
        //does nothing
    }

    public void moveRight(){
        //does nothing
    }

    public void setDirection(int x){
        direction = x;
    }

    public int getDirection(){
        return direction;
    }

    public void flip(int x, int y){
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, x, y);
        pic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(), matrix, true);
    }

    public Rect getLocation(){
        return loc;
    }

    public int getHeight(){
        if(state == 0) {
            return height;
        }
        else{
            return superHeight;
        }
    }

    public int getWidth(){
        if(state == 0) {
            return width;
        }
        else{
            return superWidth;
        }
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void update(int x, int y){
        int tempw, temph;
        if(state == 0){
            tempw = width;
            temph = height;
        }
        else{
            tempw = superWidth;
            temph = superHeight;
        }
        this.x = x - tempw/2;
        this.y = y - temph/2;
        loc.set(this.x, this.y, this.x + tempw, this.y + temph);
    }

    @Override
    public void update(){

    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(pic, null, loc, null);
    }
}
