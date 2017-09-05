package com.victorwong.tutorial;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.MotionEvent;

/**
 * Created by victorwong on 2017-09-04.
 */

public class GameplayScene implements Scene {

    private SceneManager manager;

    private Rect r = new Rect();

    private RecPlayer player;
    private Point playerPoint;

    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;
    private long gameOverTime;

    private OrientationData orientationData;
    private long frameTime;

    private boolean gameOver = false;

    public GameplayScene(){
        player = new RecPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point(Constant.SCREEN_WIDTH/2,3*Constant.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200,350,75,Color.BLACK);

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();


    }

    public void reset(){
        playerPoint = new Point(Constant.SCREEN_WIDTH/2,3*Constant.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200,350,75,Color.BLACK);
        movingPlayer = false;

    }



    @Override
    public void terminate(){
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if(!gameOver && player.getRectangle().contains((int)event.getX(),(int)event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 2000)
                    reset();
                gameOver = false;
                orientationData.newGame();
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(),(int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;


        }
    }

    @Override
    public void draw(Canvas canvas){

        canvas.drawColor(Color.WHITE);

        player.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Game Over");

        }

    }

    @Override
    public void update(){

        if(!gameOver) {
            if(frameTime < Constant.INIT_TIME)
                frameTime = Constant.INIT_TIME;
            int elapsedTime = (int)(System.currentTimeMillis()-frameTime);
            frameTime = System.currentTimeMillis();
            if(orientationData.getOrientation()!=null && orientationData.getStarOrientation()!=null){
                float pitch = orientationData.getOrientation()[1] - orientationData.getStarOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStarOrientation()[2];

                float xSpeed = 2 * roll * Constant.SCREEN_WIDTH/500f;
                float ySpeed = pitch * Constant.SCREEN_WIDTH/500f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }

            if(playerPoint.x < 0)
                playerPoint.x = 0;
            else if (playerPoint.x > Constant.SCREEN_WIDTH)
                playerPoint.x = Constant.SCREEN_WIDTH;
            if(playerPoint.y < 0)
                playerPoint.y = 0;
            else if (playerPoint.y > Constant.SCREEN_HEIGHT)
                playerPoint.y = Constant.SCREEN_HEIGHT;




            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }

    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
//        paint.setColor(Color.rgb(255, 255, 255));
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);

    }


}
