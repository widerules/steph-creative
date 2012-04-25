package com.droid.gamedev.base;

import com.droid.gamedev.engine.graphics.GameGraphics;

/**
 * Generic abstraction of an image
 * equals to Bitmap for Android
 * equals to BufferedImage for AWT
 * 
 * @author Steph
 *
 */
public interface GameImage{

	void flush();

	int getWidth();

	int getHeight();

	int getRGB(int x, int y);

	GameImage  getSubimage(int x, int y, int width, int height);

	GameGraphics createGraphics();

}
		
	


