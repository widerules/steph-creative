package com.creativedroid.service;

/** Implementation of a background task */
public interface IStartable {

	/** start function */
	void start();

	/** stop function */
	void stop();

	/** is function running */
	boolean isRunning();

}
