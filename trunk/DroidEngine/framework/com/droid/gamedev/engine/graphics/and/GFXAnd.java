package com.droid.gamedev.engine.graphics.and;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.base.GameRect;
import com.droid.gamedev.engine.BaseGFX;
import com.droid.gamedev.engine.BaseInput;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.util.Log;

/**
 * Android View that draw and plot the game scene 
 * on the screen. Extends for BaseGraphics
 * 
 * @author Steph
 * 
 */

public final class GFXAnd extends SurfaceView implements BaseGFX {

	private Activity activity;
	
	/** view size */
	private GameRect size;

	/** Locked canvas used to draw on the view */
	private Canvas canvas = null;

	/** current graphics */
	private GraphicsAnd currentGraphics;
	
	/** Game application listener*/
	private GameAppListner listener;

	/** true is surface has been created */
	private boolean ready = false;

	public GFXAnd(Activity activity) {
		super(activity);
		this.activity = activity;

		this.getHolder().addCallback(new Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				if(GFXAnd.this.listener!=null){
					//notify the game that the view has been destroyed
					//so stop 
					GFXAnd.this.listener.onAppClosed();
				}
			}

			public void surfaceCreated(SurfaceHolder holder) {
				// do nothing
			}

			/** Surface has been changed, also created */
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// surface becomes available for drawing
				GFXAnd.this.size = new GameRect(width, height);
				GFXAnd.this.ready = true;
				Log.ic(this, "SurfaceView changed " + width + "x" + height);
			}
		});
		
	}
	
	/** check if surfaceView has been created */
	public boolean isReady() {
		return this.ready;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//TODO process touch event
		return false;
	}

	/** Lock and return the canvas */ 
	private Canvas getCanavas() {
		if (this.canvas == null) {
			Canvas c = this.getHolder().lockCanvas();
			if (c != null) {
				this.canvas = c;
				this.currentGraphics = c != null ? new GraphicsAnd(c)
						: null;
			}
		}		
		return this.canvas;
	}
	
	public GameGraphics getGraphics() {
		this.getCanavas();
		return this.currentGraphics;
	}

	public boolean flip() {
		if (this.canvas != null) {
			this.getHolder().unlockCanvasAndPost(this.canvas);
		}

		this.canvas = null;
		this.currentGraphics = null;
		return false;
	}

	public void cleanup() {
		this.destroyDrawingCache();
	}

	public GameRect getSize() {
		return this.size;
	}

	public BaseInput getInput() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getGraphicsDescription() {
		return "Android Full Screen Mode [" + this.getSize().width + "x"
				+ this.getSize().height + "]";
	}

	public void setWindowTitle(String title) {
		this.activity.setTitle(title);
	}

	public String getWindowTitle() {
		return this.activity.getTitle().toString();
	}

	public void setWindowIcon(GameImage icon) {
		Log.ec(this, "Unable to set icon in this environment");
	}

	public GameImage getWindowIcon() {
		return null;
	}

	public void setGameAppListner(GameAppListner listner) {
		this.listener = listner;
	}

}
