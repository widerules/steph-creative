package creativedroid.service;

import java.util.Timer;
import java.util.TimerTask;

import android.location.Location;


interface ISpeedLimiter{
	
	void onSpeedChanged(float speed);
	
	void onSpeedExceed(float speed);
		
	void setNewLocation(Location location);
	
	void setMaxSpeed(float maxSpeed);
	
	void setDelay(long delay);
}

/**
 * this class computes and warn the user when speed limits
 */
public abstract class SpeedLimiter implements ISpeedLimiter, IStartable {

	/** maximum speed in km/h */
	private float maxSpeed;
	/** current speed in km/h */
	private float currentSpeed;
	/** time interval to compute speed (in seconds) */
	private long delay;

	private boolean running;

	private Location newPosition;
	private Location currentPosition;

	private Timer timer = new Timer();
	private ComputeSpeed computeSpeedTask = new ComputeSpeed();

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public boolean isRunning() {
		return running;
	}

	/** Change computeSpeed delay so restart timerTask.
	 * The give delay is in millisecond and should be converted in seconds*/
	public void setDelay(long delay) {
		this.delay = delay/1000;
		if (running) {
			stop();
			start();
		}
	}

	public void start() {
		if (!running) {
			timer.scheduleAtFixedRate(computeSpeedTask, 0, delay * 1000);
			running = true;
		}
	}

	public void stop() {
		if (running) {
			timer.cancel();
			running = false;
		}
	}

	public void setNewLocation(Location location) {
		newPosition = location;
	}

	/** A task to compute the speed */
	public class ComputeSpeed extends TimerTask {

		@Override
		public void run() {
			if (currentPosition != null && currentPosition != newPosition) {
				
				float speedMPS = (newPosition.distanceTo(currentPosition)) / delay;
				float speedKPH = meterPerSecondToKilometerPerHour(speedMPS);
				
				if(speedKPH!=currentSpeed){
					currentSpeed = speedKPH;
					onSpeedChanged(speedKPH);
				}
				
				if (speedKPH > maxSpeed) {
					onSpeedExceed(speedKPH);
				}
			}
			currentPosition = newPosition;
		}

	}
	
	/** convert a speed in m/s into km/h*/
	private static float meterPerSecondToKilometerPerHour(float mps){
		return (float) (mps * 3.6);
	}

}
