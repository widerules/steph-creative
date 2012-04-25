
package com.droid.gamedev.object.sprite;

import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Sprite;

/**
 * <code>InvisibleSprite</code> is sprite that has no graphical image,
 * generally used in collision to make invisible block.
 */
public class InvisibleSprite extends Sprite {
	
	/**
	 * Creates new <code>InvisibleSprite</code> at specified position and
	 * specified size.
	 */
	public InvisibleSprite(double x, double y, int width, int height) {
		super(x, y);
		
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Renders nothing, the implementation of this method is blank.
	 */
	@Override
	public void render(GameGraphics g) {
	}
	
	/**
	 * Renders nothing, the implementation of this method is blank.
	 */
	@Override
	public void render(GameGraphics g, int x, int y) {
	}
	
}
