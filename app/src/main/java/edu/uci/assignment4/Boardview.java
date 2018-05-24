package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Boardview extends SurfaceView implements SurfaceHolder.Callback{

    private GameThread thread;

    public Boardview(Context c) {
        super(c);
        getHolder().addCallback(this); //notify surface holder that you would like to receive Surfaceholder callbacks
        thread = new GameThread(getHolder(), this);
        setFocusable(true);  //Important. For some reason
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
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
        return super.onTouchEvent(e);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }

    public void update(){

    }
}
