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
    private int playerState = 0;  //0 = normal mario, 1 = fire mario, 2 = super mario
    private GameObject enemies[] = new GameObject[10];
    private Obstacle obstacles[] = new Obstacle[10];
    private Point playerPoint;           //player coordinates.
    private int width;                  //width and height of canvas
    private int height;
    private boolean moving_left = false, moving_right = false, gameover =false;
    private int jumping = 0, jumpDistance;    //0 means not jumping, 1 means rising during a jump, 2 means falling from a jump.
    private int score = 0;
    private int lives = 3;
    private long gameTime;
    private Paint paint = new Paint();

    private Rect fireball = null;
    Rect button = new Rect(); //for when mario is in fire mode
    int playerDirection = 0;
    int fireDirection = 0;

    public Boardview(Context c) {
        super(c);

        context = c;  //get the context of mainactivity to help with resetting dead enemies.

        getHolder().addCallback(this); //notify surface holder that you would like to receive Surfaceholder callbacks
        thread = new GameThread(getHolder(), this);

        player = new Player(c);  //initialize characters
        enemies[0] = new KoopaParatroopa(c);
        obstacles[0] = new Coin(c);

        paint.setColor(Color.BLACK);  //paint for text
        paint.setTextSize(100);

        setFocusable(true);  //Important. For some reason
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Canvas c = holder.lockCanvas();
        width = c.getWidth();
        height = c.getHeight();

        playerPoint = new Point();
        playerPoint.x = width/2;
        playerPoint.y = 300;

        button.set(width/2 - 200, 0, width/2 + 200, 100);

        holder.unlockCanvasAndPost(c);

        for(GameObject enemy: enemies){
            if(enemy instanceof Goomba){
                Goomba goomba = (Goomba)enemy;
                goomba.setLocation(1500, height - goomba.getHeight());
            } else if (enemy instanceof Plant) {
                Plant plant = (Plant)enemy;
                plant.setLocation(1500, height - plant.getPotHeight());
            } else if (enemy instanceof KoopaParatroopa){
                KoopaParatroopa koopa = (KoopaParatroopa)enemy;
                koopa.setLocation(1500, height - koopa.getHeight());
            }
        }

        obstacles[0].setLocation(1500,500);

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
        score = 0;
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
                        playerDirection = 1;
                        if(playerDirection != player.getDirection()){
                            player.setDirection(playerDirection);
                            player.flip(playerPoint.x, playerPoint.y);
                        }
                    }
                } else if (tempx > width / 2 && tempy > height / 2) {
                    if (moving_right) {   //if already moving right, stop.
                        moving_right = false;
                        moving_left = false;
                    } else {
                        moving_right = true;   //else allow move right.
                        moving_left = false;
                        playerDirection = 0;
                        if(playerDirection != player.getDirection()){
                            player.setDirection(playerDirection);
                            player.flip(playerPoint.x, playerPoint.y);
                        }
                    }
                } else if(playerState == 1 && tempx > width/2 - 200 && tempx < width/2 + 200 && tempy < 100){
                    if(fireball == null){
                        fireball = new Rect();
                        fireball.set(playerPoint.x, playerPoint.y, playerPoint.x + 50, playerPoint.y + 50);
                        fireDirection = playerDirection;
                    }
                }else if (jumping == 0) {    //if not jumping(jumping = 1 or 2), allow person to jump up.
                    jumping = 1;
                    jumpDistance = playerPoint.y - 500;
                }
            }
        }
        else{  //if it is a gameover and has been 2 seconds, clicking will reset the game.
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
            for(GameObject enemy : enemies){
                if(enemy != null){
                    enemy.draw(canvas);
                }
            }
            for(Obstacle obstacle : obstacles) {
                if (obstacle != null) {
                    obstacle.draw(canvas);
                }
            }

            if(playerState == 1){
                paint.setColor(Color.RED);
                canvas.drawRect(button, paint);
                if(fireball != null) {
                    canvas.drawRect(fireball, paint);
                }
                paint.setColor(Color.BLACK);
                canvas.drawText("Fire", width/2 -200, 100, paint);
            }

            canvas.drawText("Score: " + score, 0, 100, paint);
            canvas.drawText("Lives: " + lives, 1200, 100, paint);
        }
        else{
            canvas.drawText("Game Over", width/2-100, height/2 + 50, paint);
        }


    }

    public void update(){
        //behavior of player object
        if (jumping == 1) {
            playerPoint.y -= 10;   //if rising during a jump, decrement y.
            if (playerPoint.y <= jumpDistance) {
                jumping = 2;   //once player reaches max height, start falling state.
            }
        }
        else {
            playerPoint.y += 10;   //otherwise, gravity is always acting upon the character.
        }
        if(playerPoint.y + player.getHeight() / 2 >= height) {
            playerPoint.y = height - (player.getHeight()) / 2;  //if y is too high, it means character is already grounded.
            jumping = 0;  //also player can do jump command again.
        }

        //these movements in the x - direction will be used to obstacle collision check
        if(moving_left){
            playerPoint.x -= 10;
        }
        else if(moving_right){
            playerPoint.x += 10;
        }
        player.update(playerPoint.x, playerPoint.y);

        //behavior of obstacles
        for(int i = 0; i < 10; i++) {
            if(obstacles[i] != null) {
                if (Rect.intersects(player.getLocation(), obstacles[i].getLocation())) {
                    if(obstacles[i] instanceof FireFlower){
                        FireFlower temp = (FireFlower)obstacles[i];
                        if(temp.isBroken()){
                            obstacles[i] = null;
                            playerState = 1;
                            continue;
                        }
                    }
                    else if (obstacles[i] instanceof Coin){
                        Coin coin = (Coin)obstacles[i];
                        score += 100;
                        obstacles[i] = null;
                        continue;
                    }

                    int collisionType = obstacles[i].playerCollide(player, obstacles[i].getLocation(), playerPoint);
                    if (collisionType == 1) {
                        moving_right = false;
                        moving_left = false;
                    } else if (collisionType == 2) {  //if the y coordinate of the player was decremented back down, it means player is on top of an object.
                        jumping = 0;
                    } else if (collisionType == 3) {
                        jumping = 2;
                    }
                    player.update(playerPoint.x, playerPoint.y);
                }
            }
        }

        //Plant pots behave as obstacles too
        for(int i = 0; i < 10; i++){
            if(enemies[i] instanceof Plant){
                Plant plant = (Plant)enemies[i];
                if (Rect.intersects(player.getLocation(), plant.getPot())) {  //if player is about to intersect pot, limit his movements.
                    int collisionType = plant.playerCollide(player, plant.getPot(), playerPoint);
                    if(collisionType == 1){
                        moving_left = false;
                        moving_right = false;
                    }
                    else if (collisionType == 2) {  //if the y coordinate of the player was decremented back down, it means player is on top of an object.
                        jumping = 0;
                    } else if (collisionType == 3) {
                        jumping = 2;
                    }
                    player.update(playerPoint.x, playerPoint.y);
                }
            }
        }

        //these movements in the x - direction move player back
        if(moving_left){
            playerPoint.x += 10;
        }
        else if(moving_right){
            playerPoint.x -= 10;
        }
        player.update(playerPoint.x, playerPoint.y);

        //behavior of obstacles
        for(Obstacle obstacle: obstacles) {
            if (obstacle != null) {  //if character is supposed to be moving, move the obstacles.
                if (moving_left) {
                    obstacle.moveRight();
                } else if (moving_right) {
                    obstacle.moveLeft();
                }
            }
        }

        //behavior of fireball
        if(fireball != null){
            int tempx = fireball.left;
            int tempy = fireball.top;

            if(tempx < width && tempx > 0) {
                if (fireDirection == 0) {  //fireball's movement by itself
                    tempx += 20;
                } else if (fireDirection == 1) {
                    tempx -= 20;
                }

                if (moving_right) {  //fireball's movement when factoring in player movement
                    tempx -= 10;
                } else if (moving_left) {
                    tempx += 10;
                }

                fireball.set(tempx, tempy, tempx + 50, tempy + 50);
            }
            else{
                fireball = null;  //once fireball has reached end of screen, it disappears, and player can shoot another one.
            }

            for(int i = 0; i < 10; i++) {
                if(enemies[i] != null) {
                    if (Rect.intersects(fireball, enemies[i].getLocation())) {
                        if (enemies[i] instanceof Plant) {
                            Plant temp = (Plant) enemies[i];
                            if (temp.getState() == 1 || temp.getState() == 2) {
                                fireball = null;    //plant can only be killed while its rising or falling.
                                enemies[i] = null;
                                score += 100;
                            }
                        }
                        else{
                            enemies[i] = null; //normal enemies die on simple contact.
                            fireball = null;
                            score += 100;
                        }
                    }
                }
            }
        }

        //behavior of enemy objects
        for(int i = 0; i < 10; i++) {
            if(enemies[i] != null) {  //for all enemies.
                if (moving_right) {
                    enemies[i].moveLeft();
                } else if (moving_left) {
                    enemies[i].moveRight();
                }
            }

            if(enemies[i] instanceof Goomba) {
                Goomba goomba = (Goomba)enemies[i];
                if (goomba != null) {  //if goomba isnt dead, update goomba

                    goomba.update(playerPoint.x);

                    if (Rect.intersects(player.getLocation(), goomba.getLocation())) {  //check to see if goomba and player will intersect
                        if (goomba.killedByPlayer(player)) {            //if player and goomba intersect, check to see if goomba dies.
                            enemies[i] = null;
                            score += 100;
                        } else {    //if player dies, game is over and must be reset.
                            gameover = true;
                            lives--;
                        }
                    }
                }
            }

            //behavior of plant object
            if(enemies[i] instanceof Plant) {
                Plant plant = (Plant)enemies[i];
                if (plant != null) {
                    if (plant.getState() == 3) {
                        gameTime = System.nanoTime() / 1000000;
                        plant.update();  //tell plant we have the time it BEGAN to wait.
                    } else if (plant.getState() == 0) {  //if plant hasnt waited long enough, it isnt updated
                        if (System.nanoTime() / 1000000 - gameTime >= 5000) {
                            plant.update();
                        }
                    } else {
                        plant.update();
                    }

                    if (Rect.intersects(player.getLocation(), plant.getLocation())) {  //check to see if plant and player will intersect
                        gameover = true;
                        lives--;
                    }
                }
            }

            //behavior of koopa object
            if(enemies[i] instanceof KoopaParatroopa) {
                KoopaParatroopa koopa = (KoopaParatroopa)enemies[i];
                if (koopa != null) {
                    if (koopa.getJump() == 1) {
                        gameTime = System.nanoTime() / 1000000;
                        koopa.update();  //tell plant we have the time it BEGAN to wait.
                    } else if (koopa.getJump() == 2) {  //if plant hasnt waited long enough, it isnt updated
                        if (System.nanoTime() / 1000000 - gameTime >= 5000) {
                            koopa.update();
                        }
                    } else {
                        koopa.update();
                    }

                    if (Rect.intersects(player.getLocation(), koopa.getLocation())) {  //check to see if plant and player will intersect
                        gameover = true;
                        lives--;
                    }
                }
            }
        }

        if(gameover){
            for(int i = 0; i < 10; i++){
                enemies[i] = null;
            }
        }

    }
}
