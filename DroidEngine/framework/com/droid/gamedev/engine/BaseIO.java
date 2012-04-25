package com.droid.gamedev.engine;

import java.io.File;
import java.net.URL;

import com.droid.gamedev.engine.io.BaseIOWin;

/**
 * I/O engine interface
 * 
 * @author Steph
 */
public interface BaseIO {
	
/** ************************* IO MODE CONSTANTS ***************************** */
	
	/**
	 * IO mode constant for class url.
	 */
	int CLASS_URL = 1;
	
	/**
	 * IO mode constant for working directory.
	 */
	int WORKING_DIRECTORY = 2;
	
	/**
	 * IO mode constant for class loader.
	 */
	int CLASS_LOADER = 3;
	
	/**
	 * IO mode constant for system loader.
	 */
	int SYSTEM_LOADER = 4;
	
	/** ************************************************************************* */
	/** ***************************** INPUT URL ********************************* */
	/** ************************************************************************* */
	
	/**
	 * Returns URL from specified path with specified mode.
	 * @param path The path to get the URL from.
	 * @param mode The mode to retrieve the URL.
	 * @return The {@link URL}.
	 * @see #CLASS_LOADER
	 * @see #CLASS_URL
	 * @see #SYSTEM_LOADER
	 * @see #WORKING_DIRECTORY
	 */
	URL getURL(String path, int mode);
	
	/**
	 * Returns URL from specified path with this {@link BaseIOWin} default mode.
	 * @param path The path to retrieve the URL from.
	 * @return The {@link URL} of the given path.
	 * @see #getMode()
	 */
	URL getURL(String path);
	
	/** ************************************************************************* */
	/** **************************** INPUT STREAM ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Returns {@link InputStream} from specified path with specified mode.
	 * @param path The path to retrieve an {@link InputStream} from.
	 * @param mode The mode to use for retrieving the {@link InputStream}.
	 * @return The {@link InputStream}.
	 * @see #CLASS_LOADER
	 * @see #CLASS_URL
	 * @see #SYSTEM_LOADER
	 * @see #WORKING_DIRECTORY
	 */
//	InputStream getStream(String path, int mode);
	
	/**
	 * Returns input stream from specified path with this <code>BaseIO</code>
	 * default mode.
	 * @param path The path to retrieve an {@link InputStream} from.
	 * @return The {@link InputStream}.
	 * @see #getMode()
	 */
//	InputStream getStream(String path);
	
	/** ************************************************************************* */
	/** ***************************** INPUT FILE ******************************** */
	/** ************************************************************************* */
	
	/**
	 * Return file from specified path with specified mode.
	 * @param path The path to retrieve an {@link File} from.
	 * @param mode The mode to use for retrieving the {@link File}.
	 * @return The {@link File}.
	 * @see #CLASS_LOADER
	 * @see #CLASS_URL
	 * @see #SYSTEM_LOADER
	 * @see #WORKING_DIRECTORY
	 */
	File getFile(String path, int mode) ;
	
	/**
	 * Returns file from specified path with this <code>BaseIO</code> default
	 * mode.
	 * <p>
	 * 
	 * File object usually used only for writing to disk.
	 * <p>
	 * 
	 * <b>Caution:</b> always try to avoid using <code>java.io.File</code>
	 * object (this method), because <code>java.io.File</code> is system
	 * dependent and not working inside jar file, use <code>java.net.URL</code>
	 * OR <code>java.io.InputStream</code> instead.
	 * <p>
	 * @param path The path to retrieve an {@link File} from.
	 * @return The {@link File}.
	 * @see #getURL(String)
	 * @see #getStream(String)
	 * @see #setFile(String)
	 * @see #getMode()
	 */
	File getFile(String path);
	
	/** ************************************************************************* */
	/** *************************** OUTPUT FILE ********************************* */
	/** ************************************************************************* */
	
	/**
	 * Returns file on specified path with specified mode for processing.
	 * @param path The path to retrieve a {@link File} from.
	 * @param mode The mode to use for retrieving the {@link File}.
	 * @return The {@link File}.
	 * @see #CLASS_LOADER
	 * @see #CLASS_URL
	 * @see #SYSTEM_LOADER
	 * @see #WORKING_DIRECTORY
	 */
//	File setFile(String path, int mode);
	
	/**
	 * Returns file on specified path with this <code>BaseIO</code> default
	 * mode for processing.
	 * @param path The path to retrieve an {@link File} from.
	 * @return The {@link File}.
	 */
//	File setFile(String path);
	
	/** ************************************************************************* */
	/** *********************** IO MODE CONSTANTS ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Returns the root path of this {@link BaseIOWin} if using specified mode. The
	 * root path is the root where all the resources will be taken from.
	 * <p>
	 * 
	 * For example : <br>
	 * The root path = "c:\games\spaceinvader" <br>
	 * The resource name = "images\background.png" <br>
	 * The resource then will be taken from = <br>
	 * "c:\games\spaceinvader\images\background.png"
	 * @param mode The mode to retrieve root path for.
	 * @return The root path of the given mode.
	 */
	String getRootPath(int mode) ;
	
	/**
	 * Returns the official statement of specified IO mode, or
	 * <code>[UNKNOWN-MODE]</code> if the IO mode is undefined.
	 * @param mode The mode to get a string representation for.
	 * @return The {@link String} representation fo the given mode.
	 * @see #getMode()
	 */
//	String getModeString(int mode);
	
	/**
	 * Returns the default IO mode used for getting the resources.
	 * @return The default IO mode.
	 * @see #setMode(int)
	 * @see #getModeString(int)
	 */
//	int getMode();
	
	/**
	 * Sets the default IO mode used for getting the resources.
	 * @param mode The new default io mode.
	 * @see #getMode()
	 * @see #CLASS_URL
	 * @see #WORKING_DIRECTORY
	 * @see #CLASS_LOADER
	 * @see #SYSTEM_LOADER
	 */
	void setMode(int mode) ;
			
	/** ************************************************************************* */
	/** *********************** BASE CLASS LOADER ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Sets the base class where the resources will be taken from.
	 * @param base The base {@link Class}.
	 * @see #getBase()
	 */
	void setBase(Class base);
	
	/**
	 * Returns the base class where the resources will be taken from.
	 * @return The base {@link Class}.
	 * @see #setBase(Class)
	 */
//	Class getBase();
	
	/**
	 * Returns the class loader associated with this {@link BaseIOWin}.
	 * @return The {@link ClassLoader}.
	 * @see #setBase(Class)
	 */
//	ClassLoader getLoader() ;
	

}
