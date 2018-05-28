package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Player implements GameObject {

    protected Bitmap pic;
    private int x;
    private int y;
    private int width;
    private int height;
    private Rect loc;

    public Player(Context context){

        pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        x = 250;
        y = 250;
        width = 200;
        height = 200;
        loc = new Rect(x, y, x + width, y + height);
    }

    public Rect getLocation(){
        return loc;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void update(int x, int y){
        this.x = x - width/2;
        this.y = y - height/2;
        loc.set(this.x, this.y, this.x + width, this.y + height);
    }

    @Override
    public void update(){

    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(pic, null, loc, null);
    }
}
