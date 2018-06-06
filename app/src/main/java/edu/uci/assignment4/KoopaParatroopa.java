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
        x -= 100;
        loc.set(x, y, x + width, y + height);
    }

    public void moveRight(){
        x += 100;
        loc.set(x, y, x + width, y + height);
    }

    public void update(int height){
        jumph = height/2;
        if (jump == 1) {
            y -= 5;
            moveLeft();
            if (y <= jumph) {
                jump = 2;
            }
        }
        else if (jump == 2){
            y += 5;
            moveLeft();
            jump = 1;
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
        canvas.drawBitmap(koopa, null, loc, null);
    }



}
