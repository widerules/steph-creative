package com.droid.test.contamination;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class TempSprite {
	private float x;
	private float y;
	private Bitmap bmp;
	private int life = 15;

	public TempSprite(View backgr, float x, float y, Bitmap bmp) {
		this.x = Math.min(Math.max(x - bmp.getWidth() / 2, 0),
				backgr.getWidth() - bmp.getWidth()) + 5;
		this.y = Math.min(Math.max(y - bmp.getHeight() / 2, 0),
				backgr.getHeight() - bmp.getHeight()) + 5;

		this.bmp = bmp;
	}

	public boolean isActive() {
		return life > 0;
	}

	public void render(Canvas c) {
		update();
		c.drawBitmap(bmp, x, y, null);
	}

	private void update() {
		if (life > 0) {
			life--;
		}
	}
}
