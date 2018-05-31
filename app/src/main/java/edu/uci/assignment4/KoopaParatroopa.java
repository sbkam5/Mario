package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class KoopaParatroopa implements GameObject {
    protected Bitmap winged;
    protected Bitmap wingless;
    protected Bitmap shell;
    private int x;
    private int y;
    private int width;
    private int height;
    private Rect loc;

    public KoopaParatroopa(Context c){
        winged = BitmapFactory.decodeResource(c.getResources(), R.drawable.koopaparatroopa);
        x = 500;
        y = 300;
        width = 200;
        height = 300;
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

    public void update(int player_x){
        for (int i = 0; i < 100; i++){
            if (y == 300){

            }
        }
    }


    public boolean killedByPlayer(Player p){
        int deltaX = Math.abs((p.getX() + p.getWidth())  - (x + width));
        int deltaY = Math.abs((p.getY() + p.getHeight()) - (y + height));

        if(deltaY < deltaX){
            return false;  //if the player runs into enemy such that player dies.
        }

        return true;  //if the player runs into enemy such that  enemy dies.
    }

    @Override
    public void update(){

    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(pic, null, loc, null);
    }



}
