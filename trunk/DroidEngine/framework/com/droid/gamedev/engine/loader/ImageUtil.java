package com.droid.gamedev.engine.loader;

import java.io.File;
import java.net.URL;

import com.droid.gamedev.GameLoader;
import com.droid.gamedev.GameLoader.GamePlatform;
import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.util.Log;

/**
 * Utility class for creating, loading, and manipulating image.
 * Depends on platform
 * 
 * @author Steph
 *
 */
public abstract class ImageUtil {
	
	public static ImageUtil singleton;
	
	static {
		if (GameLoader.platform == GamePlatform.AWT) {
			Log.info("Creating ImageUtil for WINDOWED platform");
			ImageUtil.singleton = new ImageUtilWin();
		}
	}

	public abstract GameImage getImage(URL url);

	public abstract GameImage resize(GameImage orig, int width, int height);

	public abstract void saveImage(GameImage image, File imagefile);

	public abstract GameImage getImage(URL url, int maskColor);

	public abstract GameImage[] getImages(URL url, int col, int row, int maskColor);

	public abstract GameImage createImage(int width, int height, int opaque);

	public abstract GameImage createImage(int width, int height);

	public abstract GameImage[] getImages(URL url, int col, int row);

	public abstract GameImage applyMask(GameImage image, int backgr);

	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
	
}
