package com.droid.test.contamination;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * GameSCreen is the View that draw and plot the game scene on the screen
 * handle basic drawing primitives: filling, text, image 
 * @author Steph
 *
 */
public class GameView extends SurfaceView implements Callback {
		
	private Canvas canvas;

	private EventPool evtPool;
	
	private boolean ready;

	private GameView(Context context, EventPool evtPool) {
		super(context);
		this.getHolder().addCallback(this);
		this.evtPool = evtPool;
	}

	public boolean isReady() {
		return ready;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i("***GameScreen", "surface changed resolution: "+width+"x"+height);
		
		ready = true;
	}
	
	public Canvas getCanavas() {
		this.canvas = this.getHolder().lockCanvas();
		return this.canvas;
	}
	
	public void flip(){
		if (this.canvas != null) {
			this.getHolder().unlockCanvasAndPost(this.canvas);
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		evtPool.pushEvent(event);
		return true;
	}

}
