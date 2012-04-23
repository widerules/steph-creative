package com.droid.gamedev.engine;

import com.droid.gamedev.base.GameImage;

public interface BaseLoader {

	/**
	 * Loads and returns an image from the file location. If useMask is set to
	 * true, then the default masking color will be used. Images that have been
	 * previously loaded will return immediately from the image cache.
	 */
	GameImage getImage(String imagefile, boolean useMask) ;
	
	/**
	 * Loads and returns an image with specified file using masking color. Image
	 * that have been loaded before will return immediately from cache.
	 */
	GameImage getImage(String imagefile);
	
	/**
	 * Loads and returns image strip with specified file and whether using
	 * masking color or not. Images that have been loaded before will return
	 * immediately from cache.
	 * 
	 * @param imagefile the image filename to be loaded
	 * @param col image strip column
	 * @param row image strip row
	 * @param useMask true, the image is using transparent color
	 * @return Requested image.
	 */
	GameImage[] getImages(String imagefile, int col, int row, boolean useMask) ;
	
	/**
	 * Loads and returns image strip with specified file using masking color.
	 * Images that have been loaded before will return immediately from cache.
	 * 
	 * @param imagefile the image filename to be loaded
	 * @param col image strip column
	 * @param row image strip row
	 * @return Requested image.
	 */
	GameImage[] getImages(String imagefile, int col, int row) ;
	
	/**
	 * Removes specified image from cache.
	 * @param image The image to remove from cache.
	 * @return If removing the image from cache worked.
	 */
	boolean removeImage(GameImage image);
	
	/**
	 * Removes specified images from cache.
	 * @param images The images to remove from cache.
	 * @return If removing the images from cache worked.
	 */
	boolean removeImages(GameImage[] images) ;
	
	/**
	 * Removes image with specified image filename from cache.
	 * @param imagefile The file name of the image to remove.
	 * @return The removed image.
	 */
	GameImage removeImage(String imagefile) ;
	
	/**
	 * Removes images with specified image filename from cache.
	 * @param imagefile The file name of the image to remove.
	 * @return The removed images.
	 */
	GameImage[] removeImages(String imagefile) ;
	
	/**
	 * Clear all cached images.
	 */
	void clearCache();
	
	/**
	 * Stores image into cache with specified key.
	 * @param key The key used to store the image.
	 * @param image The image to store.
	 */
	void storeImage(String key, GameImage image) ;
	
	/**
	 * Stores images into cache with specified key.
	 * @param key The key used to store the images.
	 * @param images The images to store.
	 */
	void storeImages(String key, GameImage[] images);
	
	/**
	 * Returns cache image with specified key.
	 * @param key The key of the image wanted.
	 * @return The image with the given key or null.
	 */
	GameImage getStoredImage(String key);
	
	/**
	 * Returns cache images with specified key.
	 * @param key The key of the images wanted.
	 * @return The images with the given key.
	 */
	GameImage[] getStoredImages(String key);
		
	/**
	 * Sets where the image resources is loaded from.
	 */
	void setBaseIO(BaseIO base);
	
	/**
	 * Returns image loader masking color.
	 * @return The masking color.
	 */
	int getMaskColor();

	/**
	 * Sets image loader masking color.
	 */
	void setMaskColor(int c);
}
