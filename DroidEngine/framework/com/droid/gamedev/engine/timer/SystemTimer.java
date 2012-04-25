
package com.droid.gamedev.engine.timer;

import com.droid.gamedev.engine.BaseTimer;
import com.droid.gamedev.util.Log;

/**
 * The standard timer used in Golden T Game Engine (GTGE) Frame Work.
 * <p>
 * 
 * This timer is using System.currentTimeMillis() and support for time drift
 * calculation.
 * <p>
 * 
 * See {@link com.golden.gamedev.engine.BaseTimer} for how to use timer engine
 * separated from Golden T Game Engine (GTGE) Frame Work.
 */
public class SystemTimer implements BaseTimer {
	
	/** ************************ TIMER FPS VARIABLES **************************** */
	
	// timer variables
	private int fps = 50; // requested FPS
	private long msDelay; // requested sleep time
	
	private long start, end, // start, end time
	        timeDiff, // time difference between end-start
	        sleepTime; // real sleep time
	        
	private long overSleepTime; // hold drift on Thread.sleep() method
	
	/** **************************** OTHER VARIABLES **************************** */
	
	private boolean running;
	private FPSCounter fpsCounter;
	
	/** ************************************************************************* */
	/** ************************** CONSTRUCTOR ********************************** */
	/** ************************************************************************* */
	
	/**
	 * Constructs new <code>SystemTimer</code>.
	 */
	public SystemTimer() {
		this.fpsCounter = new FPSCounter();
	}
	
	/** ************************************************************************* */
	/** ******************** START/STOP TIMER OPERATION ************************* */
	/** ************************************************************************* */
	
	public void startTimer() {
		if (this.running) {
			this.stopTimer();
		}
		this.running = true;
		
		this.msDelay = 1000 / this.fps;
		this.refresh();
		
		this.fpsCounter.refresh();
	}
	
	public void stopTimer() {
		this.running = false;
	}
	
	/** ************************************************************************* */
	/** ********************** MAIN FUNCTION: SLEEP() *************************** */
	/** ************************************************************************* */
	
	public long sleep() {
		this.end = System.currentTimeMillis();
		
		this.timeDiff = this.end - this.start;
		this.sleepTime = (this.msDelay - this.timeDiff) - this.overSleepTime;
		
		if (this.sleepTime > 0) {
			// some time left in this cycle
			try {
				Thread.sleep(this.sleepTime);
			}
			catch (InterruptedException e) {
			}
			
			this.overSleepTime = (System.currentTimeMillis() - this.end)
			        - this.sleepTime;
			
		}
		else { // sleepTime <= 0;
			// give another thread a chance to run
			try {
				Thread.sleep(1);
			}
			catch (InterruptedException e) {
			}
			// Thread.yield();
			
			this.overSleepTime = 0;
		}
		
		this.fpsCounter.calculateFPS();
		
		long end = System.currentTimeMillis();
		long elapsedTime = end - this.start;
		this.start = end;
		
		return elapsedTime;
	}
	
	/** ************************************************************************* */
	/** ************************* TIMER PROPERTIES ****************************** */
	/** ************************************************************************* */
	
	public boolean isRunning() {
		return this.running;
	}
	
	public int getCurrentFPS() {
		return this.fpsCounter.getCurrentFPS();
	}
	
	public int getFPS() {
		return this.fps;
	}
	
	public void setFPS(int fps) {
		if (this.fps == fps) {
			return;
		}
		this.fps = fps;
		
		if (this.running) {
			this.startTimer();
		}
	}
	
	public long getTime() {
		return System.currentTimeMillis();
	}
	
	public void refresh() {
		this.start = System.currentTimeMillis();
		this.overSleepTime = 0;
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
	
}
