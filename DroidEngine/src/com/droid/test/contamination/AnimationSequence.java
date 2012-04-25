package com.droid.test.contamination;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Class that encapsulates animation sequence
 * 
 * @author Steph
 * 
 */
public class AnimationSequence {

	/** animation name = sprite status */
	public String name;

	/** can loop */
	public boolean loop;

	/** frame delay */
	public int frameDelay;

	/** frames list */
	public Rect[] rect;

	/** animation width */
	public int width;

	/** animation height */
	public int height;
	
	/** array of bitmaps*/
	public Bitmap[] bitmaps;

}