package com.victorwong.tutorial;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by victorwong on 2017-09-02.
 */

public class Obstacle implements GameObject{

    private Rect rectangle;
    private int color;
    private Rect rectangle2;

    public Rect getRectangle(){
        return rectangle;
    }

    public void incrementY(float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;

    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap){

        this.color = color;
        rectangle = new Rect(0,startY, startX, startY + rectHeight);
        rectangle2 = new Rect (startX + playerGap, startY, Constant.SCREEN_WIDTH, startY+rectHeight);


    }

    public boolean playerCollide(RecPlayer player){
       return Rect.intersects(rectangle,player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());

    }


    @Override
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);

        canvas.drawRect(rectangle2,paint);

    }
    @Override
    public void update(){

    }

}