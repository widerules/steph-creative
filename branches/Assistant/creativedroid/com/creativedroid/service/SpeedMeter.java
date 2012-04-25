package com.creativedroid.service;

import com.creativedroid.ui.Logger;

import android.location.Location;

/**
 * this class computes and warn the user when speed limits
 */
public abstract class SpeedMeter implements ISpeedMeter, IStartable {

	/** convert a speed in m/s into km/h*/
	private static int meterPerSecondToKilometerPerHour(float mps){
		return (int) (mps * 3.6);
	}
	
	/** maximum speed in km/h */
	private int maxSpeed;	
	/** current speed in km/h */
	private int currentSpeed;
	private boolean running;
	private Location currentPosition;
	private Location lastPosition;
	private long lastLocationTime;

	private long currentLocationTime;

	public int getSpeed() {
		return this.currentSpeed;
	}

	public boolean isRunning() {
		return this.running;
	}

	public void pushLocation(Location location) {
		this.currentPosition = location;
		this.currentLocationTime = System.currentTimeMillis();
		if(this.isRunning()){
			this.computeSpeed();
		}
	}
	
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public void start() {
		this.running = true;
	}
	
	public void stop() {
		this.running = false;
	}
	
	private void computeSpeed(){
		//this is the fist time compute speed
		if (this.lastPosition == null) {
			this.lastLocationTime = this.currentLocationTime;
			this.lastPosition = this.currentPosition;
			this.currentSpeed = 0;
		}
		// effectively compute speed
		else {
			long dt = (this.currentLocationTime - this.lastLocationTime) / 1000;
			float dx = this.currentPosition.distanceTo(this.lastPosition);
			float speedMPS = dx / dt;
			int speed = SpeedMeter.meterPerSecondToKilometerPerHour(speedMPS);
			Logger.i("SpeedMeter", "compute speed dt=" + dt + " dx=" + dx
					+ " speed=" + this.currentSpeed);
			
			if (speed != this.currentSpeed) {
				// raise callback onSpeedChanged
				this.onSpeedChanged(speed);
				// raise callback speed exceed
				if (speed > this.maxSpeed && this.currentSpeed < maxSpeed) {
					Logger.i("SpeedMeter", "Speed exceed !!");
					this.onSpeedExceed(speed);
				}
			}			
			this.currentSpeed = speed;
		}

		// store current as last		
		this.lastPosition = this.currentPosition;
		this.lastLocationTime = this.currentLocationTime;
	}

}

interface ISpeedMeter {

	/** get the current speed */
	int getSpeed();

	/** callback when speed changed */
	void onSpeedChanged(int speed);

	/** callback on speed exceed */
	void onSpeedExceed(int speed);

	/** set new location, so compute speed */
	void pushLocation(Location location);

	/** set value of max speed */
	void setMaxSpeed(int maxSpeed);
}
