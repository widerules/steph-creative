package com.droid.gamedev.util;

import java.util.HashMap;

/**
 * Logger
 * show log each time a platform dependent class is instantiated
 * 
 * @author Steph
 *
 */
public class Log {
	
	private static HashMap<String, Integer> instantiatedClass
		 = new HashMap<String, Integer>();

	private Log() {
	}

	/** Log information */
	public static void info(String text) {		
		System.out.println("# " + text);
	}
	
	/** Log method call from an object */
	public static void ic(Object obj, String method){
		String name = obj.getClass().getName();
		System.out.println(name.substring(name.lastIndexOf('.')+1) + "\t" + method);
	}

	/** Log error method call from an object */
	public static void ec(Object obj, String method) {
		String name = obj.getClass().getName();
		System.err.println(name + ": " + method);

	}

	/** Log error method call from an object 
	 * and exit immediately.
	 * Usefull for under construction method */
	public static void ex(Object obj, String method) {
		String name = obj.getClass().getName();
		System.err.println("x "+name+" "+method);
		System.exit(-1);
	}

	/** Log a class instantiation */
	public static void i(Object obj) {
		String name = obj.getClass().getName();
		
		if(instantiatedClass.get(name)==null){
			instantiatedClass.put(name, 1);
			System.out.println("* "+ name);
		}
	}
}



