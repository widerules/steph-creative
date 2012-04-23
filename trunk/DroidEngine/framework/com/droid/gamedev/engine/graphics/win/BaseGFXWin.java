package com.droid.gamedev.engine.graphics.win;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.base.GameImageWin;
import com.droid.gamedev.base.GameRect;
import com.droid.gamedev.engine.BaseGFX;
import com.droid.gamedev.engine.BaseInput;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.engine.input.AWTInput;
import com.droid.gamedev.util.Log;

/**
 * Generic windowed mode for Graphics Engine implementation
 * @author Steph
 *
 */
abstract class BaseGFXWin implements BaseGFX{
	
	/** engine name*/
	protected static final String ENGINE_NAME = "Droid Game Engine v1.0";
	
	/** The graphics device that constructs this graphics engine. */
	protected static final GraphicsDevice DEVICE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	
	/** The graphics configuration that constructs this graphics engine. */
	protected static final GraphicsConfiguration CONFIG = BaseGFXWin.DEVICE.getDefaultConfiguration();
	
	/** AWT component */
	protected Frame frame = null;
	
	protected Dimension size;
	
	protected VolatileImage offscreen; 
	
	protected BufferStrategy strategy;
	
	/** current graphics context */
	private Graphics2D graphics2D;
	
	/** generic object returned to other classes */
	private GraphicsWin currentGraphics;
	
	/** listener which raise game Event 
	 * when application is closed by the user 
	 * or focus changed */
	private GameAppListner gameAppListner = null;

	/**
	 * Returns the component where the rendering perform.
	 * @return The component that is rendered on.
	 */
	abstract protected Component getComponent();
	
	/** Create generic object each time currentGraphics change*/
	private void setCurrentGraphics(Graphics2D g) {		
		this.graphics2D = g;
		this.currentGraphics = g!=null? new GraphicsWin(g) : null;
	}
		
	protected final Graphics2D getCurrentGraphics() {		
		if (this.graphics2D == null) {
			
			// graphics context is not created yet,
			// or have been dispose by calling flip()
			if (this.strategy == null) {
				
				// using volatile image
				// let see if the volatile image is still validate or not
				if (this.offscreen.validate(BaseGFXWin.CONFIG) == VolatileImage.IMAGE_INCOMPATIBLE) {
					// volatile image is not valid
					this.createBackBuffer();
				}
				
				this.setCurrentGraphics(this.offscreen.createGraphics());
				
			} 
			else {
				// using buffer strategy
				this.setCurrentGraphics((Graphics2D) this.strategy.getDrawGraphics());
			}
		}

		return this.graphics2D;
	}
	
	public final GameGraphics getGraphics() {
		getCurrentGraphics();
		return this.currentGraphics;
	}
	
	
	protected void createBackBuffer() {
		if (this.offscreen != null) {
			// backbuffer is already created,
			// but not validate with current graphics context
			this.offscreen.flush();
			
			// clear old backbuffer
			this.offscreen = null;
		}
		
		this.offscreen = BaseGFXWin.CONFIG.createCompatibleVolatileImage(
		        this.size.width, this.size.height);
	}
	
	public final GameRect getSize() {
		return new GameRect(this.size.width, this.size.height);
	}
	
	public  boolean flip() {
		// disposing current graphics context
		this.graphics2D.dispose();
		
//		this.currentGraphics = null;
		this.setCurrentGraphics(null);
		
		// show to screen
		if (this.strategy == null) {
			this.getComponent().getGraphics().drawImage(this.offscreen, 0, 0, null);
			
			// sync the display on some systems.
			// (on linux, this fixes event queue problems)
			Toolkit.getDefaultToolkit().sync();
			
			return (!this.offscreen.contentsLost());
			
		}
		else {
			this.strategy.show();
			
			// sync the display on some systems.
			// (on linux, this fixes event queue problems)
			Toolkit.getDefaultToolkit().sync();
			
			return (!this.strategy.contentsLost());
		}
	}
	
	public void cleanup() {
		try {
			Thread.sleep(200L);
		}
		catch (InterruptedException e) {
		}
		
		try {
			// dispose the frame
			if (this.frame != null) {
				this.frame.dispose();
			}
		}
		catch (Exception e) {
			System.err.println("ERROR: Shutting down graphics context " + e);
			System.exit(-1);
		}
	}
	
	/**
	 * Returns the top level frame where this graphics engine is being put on.
	 * @return The top level frame.
	 */
	public final Frame getFrame() {
		return this.frame;
	}
	
	/**
	 * Returns whether this graphics engine is using buffer strategy or volatile
	 * image.
	 * @return If a buffer strategy or a volatile image is used.
	 */
	public final boolean isBufferStrategy() {
		return (this.strategy != null);
	}
	
	public final void setWindowTitle(String st) {
		this.frame.setTitle(st);
	}

	public final String getWindowTitle() {
		return this.frame.getTitle();
	}

	public final void setWindowIcon(GameImage icon) {
		try {
			BufferedImage img = ((GameImageWin)icon).getBufferedImage();
			this.frame.setIconImage(img);
		} 
		catch (Exception e) {
			Log.ec(this, "unable to set game window icon");
		}
	}

	public final GameImage getWindowIcon() {
		try{
			BufferedImage img = (BufferedImage) this.frame.getIconImage();
			return new GameImageWin(img);
		}
		catch(Exception e){
			Log.ec(this, "unable to get game window icon");
		}
		return null;
	}
	
	/**
	 * Setting up game application listeners	  
	 */
	public void setGameAppListner(GameAppListner listner) {
		Log.ic(this, "GameAppListner successfully added");
		this.gameAppListner = listner;
	}
	
	protected void setupWindowExitListener(){
		if(frame!=null){
			Log.ic(this, "WindowExitListner successfully added");
			frame.addWindowListener(new WindowExitListener() {
				
				public void windowClosing(WindowEvent e) {
					if(BaseGFXWin.this.gameAppListner!=null){
						BaseGFXWin.this.gameAppListner.onAppClosed();
					}
				}
			});
			
		}
	}
	
	protected void setupFocusListener(){
		try {
			this.getComponent().addFocusListener(
					new FocusListener() {

						public void focusGained(FocusEvent e) {
							if(BaseGFXWin.this.gameAppListner!=null){
								BaseGFXWin.this.gameAppListner.onFocusGained();
							}
						}

						public void focusLost(FocusEvent e) {
							if(BaseGFXWin.this.gameAppListner!=null){
								BaseGFXWin.this.gameAppListner.onFocusLost();
							}
						}
					});
		} 
		catch (Exception e) {
		}
	}
	
	public final BaseInput getInput(){
		return new AWTInput(this.getComponent());
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
}
