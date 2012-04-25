
package com.droid.gamedev.engine.graphics.win;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * <code>WindowExitListener</code> class is a dummy window listener class that
 * that forcing Java Virtual Machine to quit by calling
 * <code>System.exit()</code>.
 * <p>
 * 
 * This window listener is used by all <code>java.awt.Frame</code> class in
 * this graphics engine package.
 */
abstract class WindowExitListener implements WindowListener {
	
//	private static final WindowListener singleton = new WindowExitListener();
	
//	/**
//	 * Returns <code>WindowExitListener</code> singleton instance.
//	 * @return The singleton instance.
//	 */
//	private static WindowListener getInstance() {
//		return WindowExitListener.singleton;
//	}
	
//	public WindowExitListener() {
//	}
	
	/**
	 * Calls <code>System.exit(0)</code> to force Java Virtual Machine to quit
	 * when the close button of the parent object is pressed.
	 */
//	public void windowClosing(WindowEvent e) {
//		System.exit(0);
//	}
	
	/** Do nothing. */
	public void windowOpened(WindowEvent e) {
	}
	
	/** Do nothing. */
	public void windowClosed(WindowEvent e) {
	}
	
	/** Do nothing. */
	public void windowIconified(WindowEvent e) {
	}
	
	/** Do nothing. */
	public void windowDeiconified(WindowEvent e) {
	}
	
	/** Do nothing. */
	public void windowActivated(WindowEvent e) {
	}
	
	/** Do nothing. */
	public void windowDeactivated(WindowEvent e) {
	}
	
}
