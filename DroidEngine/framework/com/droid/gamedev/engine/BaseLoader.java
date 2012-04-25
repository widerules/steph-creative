package com.droid.gamedev.engine;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.loader.ImageUtil;
import com.droid.gamedev.util.Log;


interface IBaseLoader {

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

/**
 * Class for loading and masking images, and also behave as storage of the
 * loaded images.
 * 
 * 
 * Supported image format: png (*.png), gif (*.gif), and jpeg (*.jpg).
 * 
 * @author Steph
 */
public final class BaseLoader implements IBaseLoader {	
	
	/**  Base IO to get external resources */
	private BaseIO base;
	
	/** masking color */
	private int maskColor;
	
	/** **************************** IMAGE STORAGE ****************************** */
	
	/** store single image */
	private Map<String, GameImage> imageBank;
	
	/** store multiple images */
	private Map<String, GameImage[]> imagesBank;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Constructs new <code>BaseLoader</code> with specified I/O loader, and
	 * masking color.
	 * 
	 * 
	 * Masking color is the color of the images that will be converted to
	 * transparent.
	 * 
	 * @param base I/O resource loader
	 * @param maskColor the mask color
	 */
	public BaseLoader(BaseIO base, int maskColor) {
		this.base = base;
		this.maskColor = maskColor;
		
		this.imageBank = new HashMap<String, GameImage>(5);
		this.imagesBank = new HashMap<String, GameImage[]>(30);
	}
	
	public GameImage getImage(String imagefile, boolean useMask) {
		GameImage image = this.imageBank.get(imagefile);
		
		if (image == null) {
			URL url = this.base.getURL(imagefile);
			
			image = (useMask) ? ImageUtil.singleton.getImage(url, this.maskColor)
			        : ImageUtil.singleton.getImage(url);
			
			this.imageBank.put(imagefile, image);
		}
		
		return image;
	}
	
	public GameImage getImage(String imagefile) {
		return this.getImage(imagefile, true);
	}
	
	public GameImage[] getImages(String imagefile, int col, int row, boolean useMask) {
		GameImage[] image = this.imagesBank.get(imagefile);
		
		if (image == null) {
			URL url = this.base.getURL(imagefile);
			
			image = (useMask) ? 
					ImageUtil.singleton.getImages(url, col, row, this.maskColor) 
					: ImageUtil.singleton.getImages(url, col, row);
			
			this.imagesBank.put(imagefile, image);
		}
		
		return image;
	}
	
	public GameImage[] getImages(String imagefile, int col, int row) {
		return this.getImages(imagefile, col, row, true);
	}
	
	public boolean removeImage(GameImage image) {
		Iterator<GameImage> it = this.imageBank.values().iterator();
		
		while (it.hasNext()) {
			if (it.next() == image) {
				it.remove();
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeImages(GameImage[] images) {
		Iterator<GameImage[]> it = this.imagesBank.values().iterator();
		
		while (it.hasNext()) {
			if (it.next() == images) {
				it.remove();
				return true;
			}
		}
		
		return false;
	}
	
	public GameImage removeImage(String imagefile) {
		return this.imageBank.remove(imagefile);
	}
	
	public GameImage[] removeImages(String imagefile) {
		return this.imagesBank.remove(imagefile);
	}
	
	public void clearCache() {
		this.imageBank.clear();
		this.imagesBank.clear();
	}
	
	public void storeImage(String key, GameImage image) {
		if (this.imageBank.get(key) != null) {
			throw new ArrayStoreException("Key -> " + key + " is bounded to "
			        + this.imageBank.get(key));
		}
		
		this.imageBank.put(key, image);
	}
	
	public void storeImages(String key, GameImage[] image) {
		if (this.imagesBank.get(key) != null) {
			throw new ArrayStoreException("Key -> " + key + " is bounded to "
			        + this.imagesBank.get(key));
		}
		
		this.imagesBank.put(key, image);
	}
	
	public GameImage getStoredImage(String key) {
		return this.imageBank.get(key);
	}
	
	public GameImage[] getStoredImages(String key) {
		return this.imagesBank.get(key);
	}
	
	public void setBaseIO(BaseIO base) {
		this.base = base;
	}
	
	public int getMaskColor() {
		return this.maskColor;
	}
	
	public void setMaskColor(int c) {
		this.maskColor = c;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer imageKey = new StringBuffer(), imagesKey = new StringBuffer();
		
		Iterator<String> imageIt = this.imageBank.keySet().iterator(), imagesIt = this.imagesBank
		        .keySet().iterator();
		
		imageKey.append("\"");
		while (imageIt.hasNext()) {
			imageKey.append(imageIt.next());
			
			if (imageIt.hasNext()) {
				imageKey.append(",");
			}
		}
		imageKey.append("\"");
		
		imagesKey.append("\"");
		while (imagesIt.hasNext()) {
			String key = imagesIt.next();
			GameImage[] image = this.imagesBank.get(key);
			int len = (image == null) ? -1 : image.length;
			imagesKey.append(key).append("(").append(len).append(")");
			
			if (imagesIt.hasNext()) {
				imagesKey.append(",");
			}
		}
		imagesKey.append("\"");
		
		return super.toString() + " " + "[maskColor=" + this.maskColor
		        + ", BaseIO=" + this.base + ", imageLoaded=" + imageKey
		        + ", imagesLoaded=" + imagesKey + "]";
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
	
}
