
package com.droid.gamedev.engine.timer;

import com.droid.gamedev.util.Log;

/**
 * A utility class to calculate timer frame per seconds (FPS) in convenient way.
 * <p>
 * 
 * How to use :
 * 
 * <pre>
 * FPSCounter counter;
 * // game loop
 * while (true) {
 * 	counter.getCurrentFPS(); // returns current fps
 * 	counter.calculateFPS(); // calculating fps
 * }
 * </pre>
 */
public class FPSCounter {
	
	private long lastCount; // last time the fps is counted
	private int currentFPS, // the real fps achieved
	        frameCount;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new <code>FPSCounter</code>.
	 */
	public FPSCounter() {
	}
	
	/**
	 * Refresh the FPS counter, reset the fps to 0 and the timer counter to
	 * start counting from current time.
	 */
	public void refresh() {
		this.frameCount = 0;
		this.lastCount = System.currentTimeMillis();
	}
	
	/**
	 * The main method that calculating the frame per second.
	 */
	public void calculateFPS() {
		this.frameCount++;
		if (System.currentTimeMillis() - this.lastCount > 1000) {
			this.lastCount = System.currentTimeMillis();
			this.currentFPS = this.frameCount;
			this.frameCount = 0;
		}
	}
	
	/**
	 * Returns current FPS.
	 * @return The current FPS.
	 * @see #calculateFPS()
	 */
	public int getCurrentFPS() {
		return this.currentFPS;
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
	
}
