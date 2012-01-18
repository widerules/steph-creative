package com.droidcontamination;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Game sprite
 * @author Steph
 *
 */
public class Sprite {
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private static final int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private static final int MAX_SPEED = 3;
    private GameView gameView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed;
    private int ySpeed;
    private int currentFrame = 0;
    private int width;
    private int height;
    private boolean good;

    public Sprite(GameView gameView, Bitmap bmp, boolean good) {
          this.width = bmp.getWidth() / BMP_COLUMNS;
          this.height = bmp.getHeight() / BMP_ROWS;
          this.gameView = gameView;
          this.bmp = bmp;

          Random rnd = new Random();
          x = rnd.nextInt(gameView.getWidth() - width);
          y = rnd.nextInt(gameView.getHeight() - height);
          xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
          ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
          this.good = good;
    }
        
    public boolean isGood() {
		return good;
	}

    public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = getAnimationRow() * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bmp, src, dst, null);
	}

	private void update() {
          if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
                 xSpeed = -xSpeed;
          }
          x = x + xSpeed;
          if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
                 ySpeed = -ySpeed;
          }
          y = y + ySpeed;
          currentFrame = ++currentFrame % BMP_COLUMNS;
    }

	private int getAnimationRow() {
		double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
		int direction = (int) Math.round(dirDouble) % BMP_ROWS;
		return DIRECTION_TO_ANIMATION_MAP[direction];
	}

	public boolean isCollision(int x, int y) {
		return this.x <= x && x <= (this.x + width) && this.y <= y
				&& y <= (this.y + height);
	}

	public boolean collideWith(Sprite sprite) {
		double distance = Math.sqrt((x - sprite.x) * (x- sprite.x) 
				+ (y - sprite.y) * (y - sprite.y));
		return distance < 0.7*width;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void changeDirection() {
		xSpeed = -xSpeed;
		ySpeed = -ySpeed;
	}
	
	
}