package com.droid.gamedev.engine;

/**
 * BaseTimer interface is an interface for running a loop constantly in a
 * requested frame per second.
 * 
 * @author Steph
 *
 */
public interface BaseTimer {

	/**
	 * Starts the timer, please set appropriate frame per second first before
	 * calling this method.
	 */
	public void startTimer();

	/**
	 * Stops this timer.
	 */
	public void stopTimer();

	/**
	 * Sleeps for awhile to achieve requested frame per second and returns the
	 * elapsed time since last sleep.
	 */
	public long sleep();

	/**
	 * Returns timer current time in milliseconds.
	 * 
	 * @return The current time.
	 */
	public long getTime();

	/**
	 * Refresh timer elapsed time.
	 */
	public void refresh();

	/**
	 * Returns whether the timer is currently running or not.
	 */
	public boolean isRunning();

	/**
	 * Returns timer <b>current</b> frame per second.
	 */
	public int getCurrentFPS();
	
	/**
	 * Returns the <b>requested</b> frame per second.
	 */
	public int getFPS();
	
	/**
	 * Sets this timer target frame per second to specified frame per second.
	 */
	public void setFPS(int fps);
}
