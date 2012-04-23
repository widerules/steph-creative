package com.droid.gamedev.object.background;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Background;

/**
 * Background that use a single image as the background.
 */
public class ImageBackground extends Background {
	
	private transient GameImage image;
	
	/**
	 * Creates new <code>ImageBackground</code> with specified image and
	 * background size.
	 */
	public ImageBackground(GameImage image, int w, int h) {
		super(w, h);		
		this.image = image;
	}
	
	/**
	 * Creates new <code>ImageBackground</code> with specified image and the
	 * background size is as large as the image.
	 */
	public ImageBackground(GameImage image) {
		super(image.getWidth(), image.getHeight());
		
		this.image = image;
	}
	
	/** ************************************************************************* */
	/** ************************ IMAGE GET / SET ******************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns this background image.
	 */
	public GameImage getImage() {
		return this.image;
	}
	
	/**
	 * Sets this background image, and the size of this background is set to the
	 * image size.
	 */
	public void setImage(GameImage image) {
		this.image = image;
		
		this.setSize(image.getWidth(), image.getHeight());
	}
	
	/** ************************************************************************* */
	/** ************************ RENDER BACKGROUND ****************************** */
	/** ************************************************************************* */
	
	@Override
	public void render(GameGraphics g, int xbg, int ybg, int x, int y, int w, int h) {
		g.drawImage(this.image, x, y, x + w, y + h, // destination (screen area)
		        xbg, ybg, xbg + w, ybg + h); // source (image area)
	}
	
}
