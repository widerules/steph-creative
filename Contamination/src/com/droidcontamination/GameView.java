package com.droidcontamination;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
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

	private SurfaceHolder holder;
	private Bitmap buffer;
	private EventPool evtPool;
	private Canvas surface;
	private Paint paint;
	private boolean ready;

	public GameView(Context context, EventPool evtPool) {
		super(context);
		this.holder = getHolder();
		this.holder.addCallback(this);
		this.evtPool = evtPool;
	}

	public boolean isReady() {
		return ready;
	}

	@Override
	public void invalidate() {
		if (holder != null) {
			Canvas c = holder.lockCanvas();
			if (c != null) {
				c.drawBitmap(buffer, 0, 0, null);
				holder.unlockCanvasAndPost(c);
			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i("***GameScreen", "surface changed");
		buffer = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		surface = new Canvas(buffer);
		paint = new Paint();
		paint.setTextSize(16);
		ready = true;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		evtPool.pushEvent(event);
		return true;
	}

	public void drawText(String text, int color, float size, float x, float y) {
		paint.setColor(color);
		paint.setTextSize(size);
		surface.drawText(text, x, y, paint);
	}

	public void fillScreen(int color) {
		paint.setColor(color);
		surface.drawPaint(paint);
	}

	public Canvas getCanavas() {
		return surface;
	}

}
