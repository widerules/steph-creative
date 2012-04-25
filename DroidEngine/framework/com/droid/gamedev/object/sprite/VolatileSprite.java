package com.droid.gamedev.object.sprite;

import com.droid.gamedev.base.GameImage;

/**
 * One time animation sprite, the sprite is animated once, and then disappeared,
 * suitable for explosion type sprite.
 */
public class VolatileSprite extends AdvanceSprite {
	
	/**
	 * Creates new <code>VolatileSprite</code>.
	 */
	public VolatileSprite(GameImage[] image, double x, double y) {
		super(image, x, y);
		
		this.setAnimate(true);
	}
	
	/** ************************************************************************* */
	/** ************************* UPDATE SPRITE ********************************* */
	/** ************************************************************************* */
	
	@Override
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		
		if (!this.isAnimate()) {
			this.setActive(false);
		}
	}
	
}
