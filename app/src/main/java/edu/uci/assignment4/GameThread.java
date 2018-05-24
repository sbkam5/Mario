package edu.uci.assignment4;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private int fps = 30;
    private double actualfps;
    private SurfaceHolder surfaceHolder;
    private Boardview boardview;
    private boolean running;
    public static Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder, Boardview boardview){
        super();
        this.surfaceHolder = surfaceHolder;
        this.boardview = boardview;
    }

    @Override
    public void run(){

        long waitTime = 0;
        long startTime = 0;
        long targetTime = 1000/30;  //time we want an update draw to execute in
        long totalTime = 0;
        int frameCount = 0;
        long millis = 1000/30;

        while(running){
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    boardview.update();
                    boardview.draw(canvas);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            millis = (System.nanoTime() - startTime)/1000000;  //time it took for try catch to execute;
            waitTime = targetTime - millis;

            try{
                if(waitTime > 0){    //If update draw took too long, program must wait a bit
                    sleep(millis);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            totalTime += System.nanoTime()-startTime;
            frameCount++;  //we finally got through a frame

            if(frameCount >= 30){  //once we have gone through 30 frames, we can calculate the actual fps the gam is running at.
                actualfps = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;         //reset framecount and total time
                System.out.println(actualfps);
            }
        }
    }

    public void setRunning(boolean b){
        running = b;
    }


}
