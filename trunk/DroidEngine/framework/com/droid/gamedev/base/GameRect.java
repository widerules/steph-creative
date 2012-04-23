package com.droid.gamedev.base;

/**
 * Generic rectangle object 
 * optimized for drawing
 * @author Steph
 *
 */
public class GameRect{
	
	public int	x;	
	public int	y;	
	public int	width;	
	public int	height;
	
	public GameRect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public GameRect(int width, int height) {
		this(0, 0, width, height);
	}

	public GameRect() {
		this(0, 0, 0, 0);
	}

	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setBounds(GameRect r) {
		this.x = r.x;
		this.y = r.y;
		this.width = r.width;
		this.height = r.height;
	}
}
