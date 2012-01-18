package com.droidcontamination;

import android.view.MotionEvent;

/**
 * EventPool is the place where events are stored by the Main thread. To get a
 * safe data access, we do not immediately process events. Further, the game
 * loop thread will gather and process these events
 * 
 * @author Steph
 * 
 */
public class EventPool {

	/** This event pool can only store a single event */
	private MotionEvent lastEvent;

	/** Add a new event in the event pool */
	public void pushEvent(MotionEvent event) {
		this.lastEvent = event;
	}

	/** Get and remove the last event */
	public MotionEvent popEvent() {
		MotionEvent event = lastEvent;
		lastEvent = null;
		return event;
	}

}
