package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Boardview extends SurfaceView implements SurfaceHolder.Callback{

    private GameThread thread;
    private Player player;
    private Point playerPoint;
    private int width;
    private int height;
    private boolean moving_left = false, moving_right = false;
    private int jumping = 0;

    public Boardview(Context c) {
        super(c);
        getHolder().addCallback(this); //notify surface holder that you would like to receive Surfaceholder callbacks
        thread = new GameThread(getHolder(), this);

        player = new Player(c);
        playerPoint = new Point();
        playerPoint.x = 300;
        playerPoint.y = 300;

        setFocusable(true);  //Important. For some reason
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Canvas c = holder.lockCanvas();
        width = c.getWidth();
        height = c.getHeight();
        holder.unlockCanvasAndPost(c);

        thread = new GameThread(holder, this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder , int format , int width , int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(true) {
            try {
                thread.setRunning(false);
                thread.join();

            }catch (Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        int tempx = (int)e.getX();
        int tempy = (int)e.getY();
        if( e.getAction() == MotionEvent.ACTION_DOWN){
            if (tempx < width / 2 && tempy > height / 2) {
                if(moving_left){   //if already moving left, stop them.
                    moving_left = false;
                    moving_right = false;
                }
                else{
                    moving_left = true;  //otherwise allow them to move left.
                    moving_right = false;
                }
            }
            else if (tempx > width / 2 && tempy > height / 2) {
                if(moving_right){   //if already moving right, stop.
                    moving_right = false;
                    moving_left = false;
                }
                else{
                    moving_right = true;   //else allow move right.
                    moving_left = false;
                }
            }
            else if(jumping == 0) {    //if not jumping(jumping = 1 or 2), allow person to jump up.
                jumping = 1;
            }
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        player.draw(canvas);
    }

    public void update(){
        if(moving_right){
            playerPoint.x += 10;   //if moving right, increment right
        }
        else if(moving_left){
            playerPoint.x -= 10;   //if moving left, decrement x.
        }

        if(jumping == 1){
            playerPoint.y -= 10;   //if rising during a jump, decrement y.
            if(playerPoint.y <= 300){
                jumping = 2;
            }
        }
        else{
            playerPoint.y += 10;   //otherwise, gravity is always acting upon the character.
            if (playerPoint.y + player.getHeight()/2 >= height){
                playerPoint.y = height - (player.getHeight())/2;  //if y is too high, it means character is already grounded.
                jumping = 0;  //also player can do jump command again.
            }
        }
        player.update(playerPoint.x, playerPoint.y);
    }
}
