package com.droid.gamedev.engine.graphics.win;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.base.GameImageWin;
import com.droid.gamedev.engine.loader.ImageUtil;

/**
 * Graphics engine for Full Screen Exclusive Environment.
 * 
 * @author Steph
 * 
 */
public final class GFXWinFullScreen extends BaseGFXWin implements
		Comparator<DisplayMode> {
	
	/**
	 * Creates new instance of Full Screen Graphics Engine with specified size,
	 * and whether want to use bufferstrategy or volatile image.
	 */
	public GFXWinFullScreen(int width, int height, boolean bufferstrategy) {
		this.size = new Dimension(width, height);
		
		// checking for FSEM hardware support
		if (!BaseGFXWin.DEVICE.isFullScreenSupported()) {
			throw new RuntimeException(
			        "Full Screen Exclusive Mode is not supported");
		}
		
		// sets the game frame
		this.frame = new Frame(ENGINE_NAME, BaseGFXWin.CONFIG);
		
		try {
			GameImage img = ImageUtil.singleton.getImage(GFXWinFullScreen.class.getResource("Icon.png"));
			BufferedImage buffImg = ((GameImageWin)img).getBufferedImage();
			this.frame.setIconImage(buffImg);
		}
		catch (Exception e) {
		}
		
//		this.frame.addWindowListener(WindowExitListener.getInstance());
		this.frame.setResizable(false); // non resizable frame
		this.frame.setIgnoreRepaint(true); // turn off all paint events
		// since we doing active rendering
		this.frame.setLayout(null);
		this.frame.setUndecorated(true); // no menu bar, borders, etc
		this.frame.dispose();
		
		// enter fullscreen exclusive mode
		BaseGFXWin.DEVICE.setFullScreenWindow(this.frame);
		
		// check whether changing display mode is supported or not
		if (!BaseGFXWin.DEVICE.isDisplayChangeSupported()) {
			BaseGFXWin.DEVICE.setFullScreenWindow(null);
			this.frame.dispose();
			throw new RuntimeException("Changing Display Mode is not supported");
		}
		
		DisplayMode bestDisplay = this.getBestDisplay(this.size);
		if (bestDisplay == null) {
			BaseGFXWin.DEVICE.setFullScreenWindow(null);
			this.frame.dispose();
			throw new RuntimeException("Changing Display Mode to "
			        + this.size.width + "x" + this.size.height
			        + " is not supported");
		}
		
		// change screen display mode
		BaseGFXWin.DEVICE.setDisplayMode(bestDisplay);
		
		// sleep for a while, let awt do her job
		try {
			Thread.sleep(1000L);
		}
		catch (InterruptedException e) {
		}
		
		// create backbuffer
		if (bufferstrategy) {
			bufferstrategy = this.createBufferStrategy();
		}
		
		if (!bufferstrategy) {
			this.createBackBuffer();
		}
		
		this.frame.requestFocus();
		
		this.setupFocusListener();
		this.setupWindowExitListener();
	}
	
	private boolean createBufferStrategy() {
		// create bufferstrategy
		boolean bufferCreated;
		int num = 0;
		do {
			bufferCreated = true;
			try {
				// create bufferstrategy
				this.frame.createBufferStrategy(2);
			}
			catch (Exception e) {
				// unable to create bufferstrategy!
				bufferCreated = false;
				try {
					Thread.sleep(200);
				}
				catch (InterruptedException excp) {
				}
			}
			
			if (num++ > 5) {
				break;
			}
		} while (!bufferCreated);
		
		if (!bufferCreated) {
			System.err.println("BufferStrategy is not available!");
			return false;
		}
		
		// wait until bufferstrategy successfully setup
		while (this.strategy == null) {
			try {
				this.strategy = this.frame.getBufferStrategy();
			}
			catch (Exception e) {
			}
		}
		
		// wait until backbuffer successfully setup
		Graphics2D gfx = null;
		while (gfx == null) {
			// this process will throw an exception
			// if the backbuffer has not been created yet
			try {
//				gfx = this.getBackBuffer();				
				gfx = getCurrentGraphics();
			}
			catch (Exception e) {
			}
		}
		
		return true;
	}

	@Override
	protected Component getComponent() {
		return this.frame;
	}

	public String getGraphicsDescription() {
		return "Full Screen Mode [" + this.getSize().width + "x"
		        + this.getSize().height + "]"
		        + ((this.strategy != null) ? " with BufferStrategy" : "");
	}
	
	/** FIND THE BEST DISPLAY MODE */
	private DisplayMode getBestDisplay(Dimension size) {
		// get display mode for width x height x 32 with the optimum HZ
		DisplayMode mode[] = BaseGFXWin.DEVICE.getDisplayModes();
		
		ArrayList<DisplayMode> modeList = new ArrayList<DisplayMode>();
		for (int i = 0; i < mode.length; i++) {
			if (mode[i].getWidth() == size.width
			        && mode[i].getHeight() == size.height) {
				modeList.add(mode[i]);
			}
		}
		
		if (modeList.size() == 0) {
			// request display mode for 'size' is not found!
			return null;
		}
		
		DisplayMode[] match = modeList.toArray(new DisplayMode[0]);
		Arrays.sort(match, this);
		
		return match[0];
	}
	
	/**
	 * Sorts display mode, display mode in the first stack will be used by this
	 * graphics engine. The <code>o1</code> and <code>o2</code> are instance
	 * of <code>java.awt.DisplayMode</code>.
	 * <p>
	 * 
	 * In this comparator, the first stack (the one that this graphics engine
	 * will be used) will be display mode that has the biggest bits per pixel
	 * (bpp) and has the biggest but limited to 75Hz frequency (refresh rate).
	 */
	public int compare(DisplayMode mode1, DisplayMode mode2) {
		
		int removed1 = (mode1.getRefreshRate() > 75) ? 
				5000 * mode1.getRefreshRate() : 0;
		int removed2 = (mode2.getRefreshRate() > 75) ? 
				5000 * mode2.getRefreshRate() : 0;
		
		return ((mode2.getBitDepth() - mode1.getBitDepth()) * 1000)
		        + (mode2.getRefreshRate() - mode1.getRefreshRate())
		        - (removed2 - removed1);
	}
	
}
