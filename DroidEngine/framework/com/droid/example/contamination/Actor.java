package com.droid.example.contamination;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.object.Background;
import com.droid.gamedev.object.Timer;
import com.droid.gamedev.object.sprite.AnimatedSprite;

/**
 * An animated sprite that only moves inside a background and change direction
 * on limit reached
 * 
 * @author Steph
 * 
 */
public class Actor extends AnimatedSprite {
	
	public boolean good;

	public Actor(GameImage[] images, double x, double y, double vx,
			double vy, Background bg, boolean good) {
		super(images, x, y);
		this.setAnimate(true);
		this.setLoopAnim(true);
		this.setBackground(bg);
		this.setAnimationTimer(new Timer(200));
		this.setSpeed(vx, vy);
		updateAnimationFrame();
		this.setLayer(2);
		this.good = good;
	}

	@Override
	public void update(long elapsedTime) {
		// animate and move normally
		super.update(elapsedTime);
		// if out of background, reverse movement and change direction
		if (this.getX() < 0
				|| this.getX() > this.getBackground().getWidth()
						- this.getWidth()) {
			this.setHorizontalSpeed(-this.getHorizontalSpeed());
			this.setX(this.getOldX());
			this.updateAnimationFrame();
		}
		if (this.getY() < 0
				|| this.getY() > this.getBackground().getHeight()
						- this.getHeight()) {
			this.setVerticalSpeed(-this.getVerticalSpeed());
			this.setY(this.getOldY());
			this.updateAnimationFrame();
		}
	}

	/** Change the animation sequence according to the speeds */
	private void updateAnimationFrame() {
		int BMP_ROWS = 4;
		int BMP_COLUMNS = 3;
		int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
		double dirDouble = (Math.atan2(this.getHorizontalSpeed(),
				this.getVerticalSpeed())
				/ (Math.PI / 2) + 2);
		int direction = (int) Math.round(dirDouble) % BMP_ROWS;
		int start = DIRECTION_TO_ANIMATION_MAP[direction] * BMP_COLUMNS;
		int end = start + BMP_COLUMNS - 1;
		this.setAnimationFrame(start, end);
	}
	
	public boolean isInsideMe(int xp, int yp) {
		return this.getX() <= xp && xp < (this.getX() + this.getWidth()) 
				&& this.getY() <= yp	&& yp < (this.getY() + this.getHeight());
	}

	public void changeDirection() {		
		this.setHorizontalSpeed(-this.getHorizontalSpeed());
		this.setVerticalSpeed(-this.getVerticalSpeed());
		this.updateAnimationFrame();
	}

}
