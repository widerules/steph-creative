package com.droidcontamination;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TempSprite {
	private float x;
	private float y;
	private Bitmap bmp;
	private int life = 15;

	public TempSprite(GameView gameView, float x, float y, Bitmap bmp) {
		this.x = Math.min(Math.max(x - bmp.getWidth() / 2, 0),
				gameView.getWidth() - bmp.getWidth()) + 5;
		this.y = Math.min(Math.max(y - bmp.getHeight() / 2, 0),
				gameView.getHeight() - bmp.getHeight()) + 5;

		this.bmp = bmp;
	}

	public boolean done() {
		return life == 0;
	}

	public void onDraw(Canvas canvas) {
		update();
		canvas.drawBitmap(bmp, x, y, null);
	}

	private void update() {
		if (life > 0) {
			life--;
		}
	}
}
