package com.victorwong.tutorial;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by victorwong on 2017-09-04.
 */

public interface Scene {

    public void update();
    public void draw(Canvas canvas);
    public void terminate();
    public void receiveTouch(MotionEvent event);


}
