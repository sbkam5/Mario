package edu.uci.assignment4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Boardview extends SurfaceView implements SurfaceHolder.Callback{

    private GameThread thread;
    public Context context;
    private Player player;          //characters
    private Goomba goomba;
    private Plant plant;
    private Point playerPoint;
    private int width;                  //width and height of canvas
    private int height;
    private boolean moving_left = false, moving_right = false, gameover =false;
    private int jumping = 0;    //0 means not jumping, 1 means rising during a jump, 2 means falling from a jump.
    private int score = 0;
    private int lives = 3;
    private long gameTime;
    private Paint paint = new Paint();

    public Boardview(Context c) {
        super(c);

        context = c;  //get the context of mainactivity to help with resetting dead enemies.

        getHolder().addCallback(this); //notify surface holder that you would like to receive Surfaceholder callbacks
        thread = new GameThread(getHolder(), this);

        player = new Player(c);  //initialize characters
        //goomba = new Goomba(c);
        goomba = null;
        plant = new Plant(c);
        playerPoint = new Point();
        playerPoint.x = 300;
        playerPoint.y = 300;

        paint.setColor(Color.BLACK);  //paint for text
        paint.setTextSize(100);

        setFocusable(true);  //Important. For some reason
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Canvas c = holder.lockCanvas();
        width = c.getWidth();
        height = c.getHeight();
        holder.unlockCanvasAndPost(c);

        //goomba.setLocation(1000, height - goomba.getHeight());
        plant.setLocation(1000, height - plant.getPotHeight());

        thread = new GameThread(holder, this);
        thread.setRunning(true);
        thread.start();
    }

    public void reset(){
        playerPoint.x = 300;
        playerPoint.y = 300;
        moving_left = false;
        moving_right = false;
        jumping = 0;

        goomba = new Goomba(context);
        goomba.setLocation(1000, height - goomba.getHeight());
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
        if(!gameover) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                if (tempx < width / 2 && tempy > height / 2) {
                    if (moving_left) {   //if already moving left, stop them.
                        moving_left = false;
                        moving_right = false;
                    } else {
                        moving_left = true;  //otherwise allow them to move left.
                        moving_right = false;
                    }
                } else if (tempx > width / 2 && tempy > height / 2) {
                    if (moving_right) {   //if already moving right, stop.
                        moving_right = false;
                        moving_left = false;
                    } else {
                        moving_right = true;   //else allow move right.
                        moving_left = false;
                    }
                } else if (jumping == 0) {    //if not jumping(jumping = 1 or 2), allow person to jump up.
                    jumping = 1;
                }
            }
        }
        else{  //if it is a gameover and has been 2 seconds, clicking will reset the game.
            long x = System.currentTimeMillis();
            if(e.getAction() == MotionEvent.ACTION_DOWN) {
                reset();
                gameover = false;
            }
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        if(!gameover) {
            player.draw(canvas);
            if (goomba != null) {  //if goomba isn't dead, draw it too
                goomba.draw(canvas);
            }
            if(plant != null) {
                plant.draw(canvas);
            }

            canvas.drawText("Score: " + score, 0, 100, paint);
            canvas.drawText("Lives: " + lives, 1000, 100, paint);
        }
        else{
            canvas.drawText("Game Over", width/2-100, height/2 + 50, paint);
        }


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

        if(goomba != null) {  //if goomba isnt dead, update goomba
            goomba.update(playerPoint.x);

            if(Rect.intersects(player.getLocation(), goomba.getLocation())){  //check to see if goomba and player will intersect
                if(goomba.killedByPlayer(player)){            //if player and goomba intersect, check to see if goomba dies.
                    goomba = null;
                    score += 100;
                }
                else{    //if player dies, game is over and must be reset.
                    gameover = true;
                }
            }
        }

        if(plant != null) {
            if(plant.getState() == 3) {
                gameTime = System.nanoTime()/1000000;
                plant.update();  //tell plant we have the time it BEGAN to wait.
            }
            else if(plant.getState() == 0) {  //if plant hasnt waited long enough, it isnt updated
                if (System.nanoTime() / 1000000 - gameTime >= 5000) {
                    plant.update();
                }
            }
            else{
                plant.update();
            }

            if(Rect.intersects(player.getLocation(), plant.getPot())){  //if player is about to interect pot, limit his movements.
                playerPoint = plant.playerCollide(player, plant.getPot(), playerPoint);
                player.update(playerPoint.x, playerPoint.y);
            }

            if(Rect.intersects(player.getLocation(), plant.getLocation())){  //check to see if plant and player will intersect
                    gameover = true;
            }
        }
    }
}
