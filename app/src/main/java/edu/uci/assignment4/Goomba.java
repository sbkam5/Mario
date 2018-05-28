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

    public void set_y(int c_height){
        y = c_height - height;
        loc = new Rect(x, y, x + width, y + height);
    }

    public Rect getLocation(){
        return loc;
    }

    public void update(int player_x){
        if(x < player_x){
            x += 5;
        }
        else{
            x -= 5;
        }
        loc = new Rect(x, y, x + width, y + height);
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
