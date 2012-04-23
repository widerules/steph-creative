package com.droid.gamedev.object.background;

import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Background;

/**
 * The very basic background type that only fill the background view port with a
 * single color.
 * <p>
 * 
 * This type of background use a fixed memory size. Memory used by small size
 * color background (e.g: 1 x 1) with an extremely large size color background
 * (e.g: 100,000,000 x 100,000,000) is equal.
 */
public class ColorBackground extends Background {
	
	private int color;
	
	/**
	 * Creates new <code>ColorBackground</code> with specified size.
	 */
	public ColorBackground(int bgColor, int w, int h) {
		super(w, h);
		
		this.color = bgColor;
	}
	
	/**
	 * Creates new <code>ColorBackground</code> as large as screen dimension.
	 */
	public ColorBackground(int c) {
		this.color = c;
	}
	
	/** ************************************************************************* */
	/** ************************** BGCOLOR GET / SET **************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns this background color.
	 */
	public int getColor() {
		return this.color;
	}
	
	/**
	 * Sets the background color.
	 */
	public void setColor(int bgColor) {
		this.color = bgColor;
	}
	
	/** ************************************************************************* */
	/** ************************ RENDER BACKGROUND ****************************** */
	/** ************************************************************************* */
	
	@Override
	public void render(GameGraphics g, int xbg, int ybg, int x, int y, int w, int h) {
		g.setColor(this.color);
		g.fillRect(x, y, w, h);
	}
	
}
