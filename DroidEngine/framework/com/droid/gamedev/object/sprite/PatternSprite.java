
package com.droid.gamedev.object.sprite;

import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Sprite;

/**
 * Sprite that its images is taken from another sprite (the pattern).
 * <p>
 * 
 * <code>PatternSprite</code> is used to make a number of sprite that share
 * same images and have same animation sequence. <br>
 * A sprite that not created in a same time will have a different animation
 * sequence (the new sprite will start with the first frame animation, and the
 * old one perhaps at the last animation). This kind of sprite will assure that
 * <code>PatternSprite</code> that share the same pattern will animated in the
 * same sequence.
 * <p>
 * 
 * Note: Don't forget to update the pattern sprite in order to keep the pattern
 * animate.
 */
public class PatternSprite extends Sprite {
	
	private Sprite pattern;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new <code>PatternSprite</code> with specified pattern and
	 * coordinate.
	 */
	public PatternSprite(Sprite pattern, double x, double y) {
		super(pattern.getImage(), x, y);
		
		this.pattern = pattern;
	}
	
	/**
	 * Creates new <code>PatternSprite</code> with specified pattern.
	 */
	public PatternSprite(Sprite pattern) {
		super(pattern.getImage(), 0, 0);
		
		this.pattern = pattern;
	}
	
	/** ************************************************************************* */
	/** ************************* RENDER THE PATTERN **************************** */
	/** ************************************************************************* */
	
	@Override
	public void render(GameGraphics g, int x, int y) {
		g.drawImage(this.pattern.getImage(), x, y);
	}
	
	/** ************************************************************************* */
	/** **************************** THE PATTERN ******************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns the pattern sprite associates with this sprite.
	 */
	public Sprite getPattern() {
		return this.pattern;
	}
	
	/**
	 * Sets the pattern of this sprite.
	 */
	public void setPattern(Sprite pattern) {
		this.pattern = pattern;
	}
	
}
