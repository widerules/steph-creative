package com.droid.test.contamination;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class AdvancedSprite extends Sprite{
	
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	private SpriteInfo spriteInfo;
	private AnimationSequence currentAnimation;
	private Sprite collisionSprite;
	
	
	public AdvancedSprite(Bitmap bmp, SpriteInfo spi,  int bgWidth, int bgHeight, boolean good) {
		super(bmp, bgWidth, bgHeight, good);	
		this.spriteInfo = spi;
		setupAnimation();
	}
	
	/** change direction because of collision with another sprite*/
	public void avoidSprite(AdvancedSprite sprite2) {
		if(this.collisionSprite==null && sprite2.isActive() &&  this.collideWith(sprite2)){
			this.collisionSprite = sprite2;
			changeDirection();
		}
	}
	
	@Override
	public void changeDirection() {
		xSpeed = -xSpeed;
		ySpeed = -ySpeed;
		setupAnimation();
	}
	
	public void drawCollisionShape(Canvas canvas){
		int color = collisionSprite==null? Color.WHITE : Color.RED;
		
		Paint paint = new Paint();
		Rect dst = new Rect(x, y, x + width-1, y + height-1);
		paint.setColor(color);
		canvas.drawLine(dst.left, dst.top, dst.right, dst.top, paint);
		canvas.drawLine(dst.left, dst.top, dst.left, dst.bottom, paint);
		canvas.drawLine(dst.left, dst.bottom, dst.right, dst.bottom, paint);
		canvas.drawLine(dst.right, dst.top, dst.right, dst.bottom, paint);
	}
	
	@Override
	public void render(Canvas c) {
//		Log.i("***advancedSprite", "draw:"+currentAnimation.name);
		this.update();
		Rect src = currentAnimation.rect[currentFrame];
		Rect dst = new Rect(x, y, x + width-1, y + height-1);
		c.drawBitmap(bmp, src, dst, null);
//		canvas.drawBitmap(currentAnimation.bitmaps[currentFrame], this.x, this.y, null);
//		drawCollisionShape(canvas);
//		canvas.drawBitmap(this.currentAnimation.bitmaps[this.currentFrame], this.x, this.y, null);
	}
	
	@Override
	public String toString(){
		String str = super.toString();		
		return str.split("@")[1]; 
	}
	
	/** compute direction of the sprite*/
	private int computeDirection(){
		if (Math.abs(this.xSpeed) >= Math.abs(this.ySpeed)) {
			if (this.xSpeed >= 0)
				return RIGHT; 
			else
				return LEFT; 
		} else {
			if(ySpeed>0) 
				return DOWN; 
			else
				return UP; 
		}
	}
	
	/** called when direction or status changed (here obviously on speed changed)*/
	private void setupAnimation(){
		switch (computeDirection()) {
		case UP:
			currentAnimation = spriteInfo.animations.get("walk-up");			
			break;
		case DOWN:
			currentAnimation = spriteInfo.animations.get("walk-down");			
			break;
		case LEFT:
			currentAnimation = spriteInfo.animations.get("walk-left");			
			break;
		case RIGHT:
			currentAnimation = spriteInfo.animations.get("walk-right");			
			break;
		}
//		Log.i("AdvancedSprite", "setup animation dir= "+computeDirection());
		this.width = currentAnimation.width;
		this.height = currentAnimation.height;
		this.currentFrame = 0;
//		Log.i("***advancedSprite", "animation:"+currentAnimation.name);
	}
	
	private void cleanupCollision(){
		if(this.collisionSprite!=null && (!this.collisionSprite.isActive() || !this.collideWith(this.collisionSprite))){
			this.collisionSprite = null;
		}
	}

	@Override
	protected void update() {
		cleanupCollision();
		
		boolean speedChanged = false;
		if (x >= bgWidth - width - xSpeed || x + xSpeed <= 0) {
			xSpeed = -xSpeed;
			setupAnimation();
			speedChanged = true;
		}
		
		if (y >= bgHeight - height - ySpeed || y + ySpeed <= 0) {
			ySpeed = -ySpeed;
			setupAnimation();
			speedChanged = true;
		}
		
		if(!speedChanged){
			x = x + xSpeed;
			y = y + ySpeed;
			currentFrame = ++currentFrame % currentAnimation.rect.length;
		}
		
	}
	
	

}
