package com.droid.gamedev.engine.loader;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.BaseIO;
import com.droid.gamedev.engine.BaseLoader;
import com.droid.gamedev.util.Log;



/**
 * Class for loading and masking images, and also behave as storage of the
 * loaded images.
 * 
 * 
 * Supported image format: png (*.png), gif (*.gif), and jpeg (*.jpg).
 * 
 * @author Steph
 */
public final class BaseLoaderWin implements BaseLoader {	
	
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
	public BaseLoaderWin(BaseIO base, int maskColor) {
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
