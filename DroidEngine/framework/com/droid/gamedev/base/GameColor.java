package com.droid.gamedev.base;

/**
 * All colors are coded in int '0xARGB'
 * 
 * @author Steph
 * 
 */
public interface GameColor {
	
	int BLACK = 0xFF000000;

	int WHITE = 0xFFFFFFFF;

	int DARK_GREEN = 0xFF008000;

	int RED = 0xFFFF0000;

	int MAGENTA = 0xFFFF00FF;

	// equivalent to java.awt.Transparency.OPAQUE
	int OPAQUE = 1;

	// equivalent to java.awt.Transparency.BITMASK
	int BITMASK = 2;

	// equivalent to java.awt.Transparency.TRANSLUCENT
	int TRANSLUCENT = 3;

	int YELLOW = 0xFFFFFF00;

}
