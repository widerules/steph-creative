package com.droid.test.contamination;

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
    private static final int MAX_SPEED = 5;
    
    //screen dimension
    protected int bgWidth;
    protected int bgHeight;
    
    protected Bitmap bmp;
    
    protected int x = 0;
    protected int y = 0;
    protected int xSpeed;
    protected int ySpeed;
    
    protected int currentFrame = 0;
    
    protected int width;
    protected int height;
   
   
	public boolean good;
    private boolean active;
    
    
    
    public Sprite(Bitmap bmp, int bgWidth, int bgHeight, boolean good) {
          this.width = bmp.getWidth() / BMP_COLUMNS;
          this.height = bmp.getHeight() / BMP_ROWS;
          this.bgWidth = bgWidth;
          this.bgHeight = bgHeight;
          active = true;
          
          this.bmp = bmp;
          
		Random rnd = new Random();
		x = rnd.nextInt(bgWidth - width);
		y = rnd.nextInt(bgHeight - height);
		//always generate actor at screen border
//		int n = rnd.nextInt(4);
//		switch (n) {
//		case 0:
//			x = 0;
//			break;
//		case 1:
//			x = bgWidth - width-1;
//			break;
//		case 2:
//			y = 0;
//			break;
//		case 3:
//			y = bgHeight - height-1;
//			break;
//		}

          while(xSpeed==0 && ySpeed==0){
        	  xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
              ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
          }         
          
          this.good = good;
    }

	public void changeDirection() {
		xSpeed = -xSpeed;
		ySpeed = -ySpeed;
	}

	public boolean collideWith(Sprite sprite) {
//		return isInsideMe(sprite.x, sprite.y) 
//				|| isInsideMe(sprite.x + sprite.width - 1, sprite.y + sprite.height - 1);		
		
		
		
		
		return (
				sprite.x < (this.x + this.width)  /** sprite.x1 before my x2 */
				&& (sprite.x + sprite.width) > this.x /** sprite.x2 is after my x1*/
		        && this.y + this.height > sprite.y && this.y < sprite.y + sprite.height);
	}
      
    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isInsideMe(int xp, int yp) {
		return this.x <= xp && xp < (this.x + width) 
				&& this.y <= yp	&& yp < (this.y + height);
	}

	public void render(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = getAnimationRow() * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		
		//Log.i("***sprite", "draw source:"+src.left+"-"+src.top+" to "+src.width()+"x"+src.height());
		canvas.drawBitmap(bmp, src, dst, null);
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	private int getAnimationRow() {
		double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
		int direction = (int) Math.round(dirDouble) % BMP_ROWS;
		return DIRECTION_TO_ANIMATION_MAP[direction];
	}

	protected void update() {
          if (x >= bgWidth - width - xSpeed || x + xSpeed <= 0) {
                 xSpeed = -xSpeed;
          }
          x = x + xSpeed;
          if (y >= bgHeight - height - ySpeed || y + ySpeed <= 0) {
                 ySpeed = -ySpeed;
          }
          y = y + ySpeed;
          currentFrame = ++currentFrame % BMP_COLUMNS;
    }
	
	
}