
package com.droid.gamedev.engine.io;


import java.io.File;
import java.io.InputStream;
import java.net.URL;

import com.droid.gamedev.engine.BaseIO;
import com.droid.gamedev.util.Log;

/**
 * Class to get external resources object, such as <code>java.io.File</code>,
 * <code>java.io.InputStream</code>, and <code>java.net.URL</code>.
 * <p>
 * 
 * There are four types mode of how <code>BaseIO</code> getting the external
 * resources object : <br>
 * <ul>
 * <li>{@link #CLASS_URL}</li>
 * <li>{@link #WORKING_DIRECTORY}</li>
 * <li>{@link #SYSTEM_LOADER}, and</li>
 * <li>{@link #CLASS_LOADER}</li>
 * </ul>
 * <p>
 * 
 * By default <code>BaseIO</code> class is using <code>CLASS_URL</code>.
 */
public class BaseIOWin implements BaseIO {
	
	/** ************************* BASE CLASS LOADER ***************************** */
	
	private Class base;
	private ClassLoader loader;
	private int mode;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Construct new <code>BaseIO</code> with specified class as the base
	 * loader, and specified IO mode (one of {@link #CLASS_URL},
	 * {@link #WORKING_DIRECTORY}, {@link #CLASS_LOADER}, or
	 * {@link #SYSTEM_LOADER}).
	 * 
	 * @param base the base class loader
	 * @param mode one of IO mode constants
	 * @see #CLASS_URL
	 * @see #WORKING_DIRECTORY
	 * @see #CLASS_LOADER
	 * @see #SYSTEM_LOADER
	 */
	public BaseIOWin(Class base, int mode) {
		this.base = base;
		this.loader = base.getClassLoader();
		this.mode = mode;
				
		for(int i=1; i<=4; i++){
			Log.info(" BaseIO root path "+ this.getModeString(i)+ " : "+ this.getRootPath(i));
		}
	}
	
	/**
	 * Construct new <code>BaseIO</code> with specified class as the base
	 * loader using {@link #CLASS_URL} mode as the default.
	 * 
	 * @param base the base class loader
	 */
	public BaseIOWin(Class base) {
		this(base, BaseIO.CLASS_URL);
	}
	
	public URL getURL(String path, int mode) {
		URL url = null;
		
		try {
			switch (mode) {
				case CLASS_URL:
					url = this.base.getResource(path);
					break;
				
				case WORKING_DIRECTORY:
					File f = new File(path);
					if (f.exists()) {
						url = f.toURL();
					}
					break;
				
				case CLASS_LOADER:
					url = this.loader.getResource(path);
					break;
				
				case SYSTEM_LOADER:
					url = ClassLoader.getSystemResource(path);
					break;
			}
		}
		catch (Exception e) {			
		}
		
		if (url == null) {			
//			throw new RuntimeException(this.getException(path, mode, "getURL"));
			throw new RuntimeException("Error getting URL on "
					+ getModeString(mode) + " mode for resource " + path);
		}

		return url;
	}
	
	public URL getURL(String path) {
		URL url = null;
		
		try {
			url = this.getURL(path, this.mode);
		}
		catch (Exception e) {
		}
		
		if (url == null) {
			// smart resource locater
			int smart = 0;
			while (url == null
			        && !this.getModeString(++smart).equals("[UNKNOWN-MODE]")) {
				try {
					url = this.getURL(path, smart);
				}
				catch (Exception e) {
				}
			}
			
			if (url == null) {
//				throw new RuntimeException(this.getException(path, this.mode,
//				        "getURL"));
				throw new RuntimeException(
						"Error getting URL on smart mode for resource " + path);
			}
			
			this.mode = smart;
		}
		
		return url;
	}
	
	public InputStream getStream(String path, int mode) {
		InputStream stream = null;
		
		try {
			switch (mode) {
				case CLASS_URL:
					stream = this.base.getResourceAsStream(path);
					break;
				
				case WORKING_DIRECTORY:
					stream = new File(path).toURL().openStream();
					break;
				
				case CLASS_LOADER:
					stream = this.loader.getResourceAsStream(path);
					break;
				
				case SYSTEM_LOADER:
					stream = ClassLoader.getSystemResourceAsStream(path);
					break;
			}
		}
		catch (Exception e) {
		}
		
		if (stream == null) {
			throw new RuntimeException(this.getException(path, mode,
			        "getStream"));
		}
		
		return stream;
	}
	
	public InputStream getStream(String path) {
		InputStream stream = null;
		
		try {
			stream = this.getStream(path, this.mode);
		}
		catch (Exception e) {
		}
		
		if (stream == null) {
			// smart resource locater
			int smart = 0;
			while (stream == null
			        && !this.getModeString(++smart).equals("[UNKNOWN-MODE]")) {
				try {
					stream = this.getStream(path, smart);
				}
				catch (Exception e) {
				}
			}
			
			if (stream == null) {
				throw new RuntimeException(this.getException(path, this.mode,
				        "getStream"));
			}
			
			this.mode = smart;
		}
		
		return stream;
	}
	
	public File getFile(String path, int mode) {
		File file = null;
		
		try {
			switch (mode) {
				case CLASS_URL:
					file = new File(this.base.getResource(path).getFile()
					        .replaceAll("%20", " "));
					break;
				
				case WORKING_DIRECTORY:
					file = new File(path);
					break;
				
				case CLASS_LOADER:
					file = new File(this.loader.getResource(path).getFile()
					        .replaceAll("%20", " "));
					break;
				
				case SYSTEM_LOADER:
					file = new File(ClassLoader.getSystemResource(path)
					        .getFile().replaceAll("%20", " "));
					break;
			}
		}
		catch (Exception e) {
		}
		
		if (file == null) {
			throw new RuntimeException(this.getException(path, mode, "getFile"));
		}
		
		return file;
	}
	
	public File getFile(String path) {
		File file = null;
		
		try {
			file = this.getFile(path, this.mode);
		}
		catch (Exception e) {
		}
		
		if (file == null) {
			// smart resource locater
			int smart = 0;
			while (file == null
			        && !this.getModeString(++smart).equals("[UNKNOWN-MODE]")) {
				try {
					file = this.getFile(path, smart);
				}
				catch (Exception e) {
				}
			}
			
			if (file == null) {
				throw new RuntimeException(this.getException(path, this.mode,
				        "getFile"));
			}
			
			this.mode = smart;
		}
		
		return file;
	}
	
	public String getRootPath(int mode) {
		switch (mode) {
			case CLASS_URL:
				return this.base.getResource("").toString();
				
			case WORKING_DIRECTORY:
				return System.getProperty("user.dir") + File.separator;
				
			case CLASS_LOADER:
				return this.loader.getResource("").toString();
//				
			case SYSTEM_LOADER:
				return ClassLoader.getSystemResource("").toString();
		}
		
		return "[UNKNOWN-MODE]";
	}
	
	public String getModeString(int mode) {
		switch (mode) {
			case CLASS_URL:
				return "Class-URL";
			case WORKING_DIRECTORY:
				return "Working-Directory";
			case CLASS_LOADER:
				return "Class-Loader";
			case SYSTEM_LOADER:
				return "System-Loader";
		}
		
		return "[UNKNOWN-MODE]";
	}
	
	public int getMode() {
		return this.mode;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setBase(Class base) {
		this.base = base;
		this.loader = base.getClassLoader();
	}
	
	public Class getBase() {
		return this.base;
	}
	
	public ClassLoader getLoader() {
		return this.loader;
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + "[mode="
		        + this.getModeString(this.mode) + ", baseClass=" + this.base
		        + ", classLoader=" + this.loader + "]";
	}

	/**
	 * Returns exception string used whenever resource can not be found.
	 * @param path The path that was retrived.
	 * @param mode The io mode used during the exception occured.
	 * @param method The method the exception occured in.
	 * @return The exception description string.
	 */
	protected String getException(String path, int mode, String method) {
		return "Resource not found (" + this + "): " + this.getRootPath(mode)
		        + path;
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
	
}
