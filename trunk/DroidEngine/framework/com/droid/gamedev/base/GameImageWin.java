package com.droid.gamedev.base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.engine.graphics.win.GraphicsWin;
import com.droid.gamedev.util.Log;


/**
 * Implementation of ImageData = BufferedImage or Image
 * @author Steph
 *
 */
public class GameImageWin implements GameImage {
	
	private BufferedImage img;
	
	public GameImageWin(BufferedImage img){
		this.img = img;
	}
	
	/** return bufferedImage object */
	public BufferedImage getBufferedImage(){
		return this.img;
	}

	public GameGraphics createGraphics() {
		Graphics2D g = this.img.createGraphics();
		return new GraphicsWin(g);
	}

	public void flush() {
		this.img.flush();
	}

	public int getWidth() {
		return this.img.getWidth();
	}

	public int getHeight() {
		return this.img.getHeight();
	}

	public int getRGB(int x, int y) {
		return this.img.getRGB(x, y);
	}

	public GameImage getSubimage(int x, int y, int width, int height) {
		BufferedImage buffImg = this.img.getSubimage(x, y, width, height);
		return new GameImageWin(buffImg);
	}
	
	{
		Log.i(this);
	}
	
}
