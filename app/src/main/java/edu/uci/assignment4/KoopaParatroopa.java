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
    private int jumph;
    private int state; //1 means rising, 2 means falling
    private Rect loc;

    public KoopaParatroopa(Context c){
        koopa= BitmapFactory.decodeResource(c.getResources(), R.drawable.koopa);
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
        if (state == 0){
            state = 1;
        }
        else if (state == 1){
            y -= 10;
            x += 5;
            loc.set(x + 25, y, x + 25 + width, y + 25 + height);
            if ( y == jumph){
                state = 2;
            }
        }
        else if (state == 2){
            y += 10;
            x += 5;
            loc.set(x+25, y , x+25+width, y+height);
        }
        else{
            state = 0;   //state 3 is the "wait" state.
        }

    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(koopa, null, loc, null);
    }



}
