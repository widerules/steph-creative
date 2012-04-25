package com.creativedroid.ui;

import android.util.Log;

/**
 * Common object to write log
 * @author Steph
 *
 */
public class Logger {

	public static String prefix = "==> ";
	public static boolean enabled = true;

	public static void i(String flag, String message) {
		if(Logger.enabled){
			Log.i(Logger.prefix + flag, message);
		}		
	}

}
