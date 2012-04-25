package com.creativedroid.ui;
	  
/**
 * Utilities for array
 * @author Steph
 *
 */
public  class Util {
		
	public static int indexOf(long value, long[] values){		
		int index = -1;
		for (int i = 0; i < values.length; i++) {
			if(values[i]==value) {
				index = i;
				break;
			}
		}
		return index;
	}
		
	public static int indexOf(int value, int[] values){		
		int index = -1;
		for (int i = 0; i < values.length; i++) {
			if(values[i]==value) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public static int indexOf(float value, float[] values){		
		int index = -1;
		for (int i = 0; i < values.length; i++) {
			if(values[i]==value) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public static int indexOf(String value, CharSequence[] values){		
		int index = -1;
		for (int i = 0; i < values.length; i++) {
			if(values[i].equals(value)) {
				index = i;
				break;
			}
		}
		return index;
	}
}
