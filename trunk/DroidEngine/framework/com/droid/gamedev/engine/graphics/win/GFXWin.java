package com.droid.gamedev.engine.graphics.win;


import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.base.GameImageWin;
import com.droid.gamedev.engine.loader.ImageUtil;

/**
 * Graphics engine for Windowed Environment.
 * <p>
 * 
 * See {@link com.BaseGFX.gamedev.engine.BaseGraphics} for how to use graphics
 * engine separated from Golden T Game Engine (GTGE) Frame Work.
 */
public final class GFXWin extends BaseGFXWin {
			
	private Canvas canvas;
	
	/**
	 * Creates new instance of Windowed Graphics Engine with specified size, and
	 * whether want to use bufferstrategy or volatile image.
	 */
	public GFXWin(int width, int height, boolean bufferstrategy) {
		this.size = new Dimension(width, height);
		
		// sets game frame
		this.frame = new Frame(ENGINE_NAME, BaseGFXWin.CONFIG);
		
		try {
			GameImage img = ImageUtil.singleton.getImage(GFXWin.class.getResource("Icon.png"));
			BufferedImage buffImg = ((GameImageWin)img).getBufferedImage();
			this.frame.setIconImage(buffImg);
		}
		catch (Exception e) {
		}
		
//		this.frame.addWindowListener(WindowExitListener.getInstance());
		this.frame.setResizable(false); // non resizable frame
		this.frame.setIgnoreRepaint(true); // turn off all paint events
		// since we doing active rendering
		
		// the active component where the game drawn
		this.canvas = new Canvas(BaseGFXWin.CONFIG);
		this.canvas.setIgnoreRepaint(true);
		this.canvas.setSize(this.size);
		
		// frame title bar and border (frame insets) makes
		// game screen smaller than requested size
		// we must enlarge the frame by it's insets size
		this.frame.setVisible(true);
		Insets inset = this.frame.getInsets();
		this.frame.setVisible(false);
		this.frame.setSize(this.size.width + inset.left + inset.right,
		        this.size.height + inset.top + inset.bottom);
		this.frame.add(this.canvas);
		this.frame.pack();
		this.frame.setLayout(null);
		this.frame.setLocationRelativeTo(null); // centering game frame
		if (this.frame.getX() < 0) {
			this.frame.setLocation(0, this.frame.getY());
		}
		if (this.frame.getY() < 0) {
			this.frame.setLocation(this.frame.getX(), 0);
		}
		this.frame.setVisible(true);
		
		// create backbuffer
		if (bufferstrategy) {
			bufferstrategy = this.createBufferStrategy();
		}
		
		if (!bufferstrategy) {
			this.createBackBuffer();
		}
		
		this.canvas.requestFocus();
		
		this.setupFocusListener();
		this.setupWindowExitListener();
		
	}
	
	private boolean createBufferStrategy() {
		boolean bufferCreated;
		int num = 0;
		do {
			bufferCreated = true;
			try {
				// create bufferstrategy
				this.canvas.createBufferStrategy(2);
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
				this.strategy = this.canvas.getBufferStrategy();
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
				gfx = this.getCurrentGraphics();
			}
			catch (Exception e) {
			}
		}
		
		return true;
	}
	
	@Override
	protected Component getComponent() {
		return this.canvas;
	}

	public String getGraphicsDescription() {
		return "Windowed Mode [" + this.getSize().width + "x"
		        + this.getSize().height + "]"
		        + ((this.strategy != null) ? " with BufferStrategy" : "");
	}
	
}
