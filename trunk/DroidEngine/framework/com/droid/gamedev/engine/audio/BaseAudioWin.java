package com.droid.gamedev.engine.audio;

import com.droid.gamedev.engine.BaseAudio;
import com.droid.gamedev.engine.BaseIO;
import com.droid.gamedev.engine.io.BaseIOWin;
import com.droid.gamedev.util.Log;
import com.droid.gamedev.util.Utility;

/**
 * Audio manager that manages playing, stopping, looping of multiple audio
 * sounds (<code>BaseAudioRenderer</code>s).
 * <p>
 * 
 * Audio manager takes up a single base renderer parameter. The base is used to
 * create new instance of <code>BaseAudioRenderer</code> to play new audio
 * sound.
 * <p>
 * 
 * Audio manager also take care any idle renderer and looping audio renderer.
 * <p>
 * 
 * This class is using {@link BaseIOWin} to get the external resources.
 */
public final class BaseAudioWin implements BaseAudio {
	
	private int audioPolicy = BaseAudioWin.MULTIPLE; // default audio policy
	
	private int maxSimultaneous; // max simultaneous audio sound
	// played at a time
	
	/** ************************** AUDIO RENDERER ******************************* */
	
	private AudioPlayer baseRenderer;
	
	private AudioPlayer[] renderer;
	
	private String[] rendererFile; // store the filename of
	// the rendered audio
	
	private String lastAudioFile; // last played audio
	
	/** ************************* MANAGER PROPERTIES **************************** */
	
	private BaseIO base;
	
	private boolean exclusive; // only one clip can be played at a time
	
	private boolean loop; // ALL clips are played continously or not
	
	private float volume;
	
	private boolean active;
	
	private int buffer; // total audio renderer instances before
	
	// attempting to replace idle renderer
	
	/** ************************************************************************* */
	/** ************************** CONSTRUCTOR ********************************** */
	/** ************************************************************************* */
	
	/**
	 * Creates new audio manager with specified renderer as the base renderer of
	 * all audio sounds created by this audio manager.
	 * <p>
	 * 
	 * @param base the BaseIO to get audio resources
	 * @param baseRenderer the base renderer of this audio manager
	 */
	public BaseAudioWin(BaseIO base) {
		this.base = base;
		//the AudioPlayer is directly a WavePlayer
		this.baseRenderer = new WavePlayer();
		
		this.active = baseRenderer.isAvailable();
		this.volume = 1.0f;
		this.buffer = 10;
		this.maxSimultaneous = 6;
		
		this.renderer = new AudioPlayer[0];
		this.rendererFile = new String[0];
		
		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	/** Thread implementation for managing audio renderers looping. */
	public void run() {
		while (true) {
			try {
				Thread.sleep(100L);
			}
			catch (InterruptedException e) {
			}
			
			for (int i = 0; i < this.renderer.length; i++) {
				if (this.renderer[i].isLoop()
				        && this.renderer[i].getStatus() == IAudioPlayer.END_OF_SOUND) {
					this.renderer[i].play();
				}
			}
		}
	}
	
	public int play(String audiofile) {
		return this.play(audiofile, this.audioPolicy);
	}
	
	public int play(String audiofile, int policy) {
		this.lastAudioFile = audiofile;
		
		if (!this.active) {
			return -1;
		}
		
		// -2 means attempt to replace idle renderer
		// since total renderer has exceed buffer size
		int emptyslot = (this.renderer.length <= this.buffer) ? -1 : -2;
		
		// to count simultaneous playing sound
		int playedSound = 0;
		
		for (int i = 0; i < this.renderer.length; i++) {
			if (this.rendererFile[i].equals(audiofile)) {
				if (this.renderer[i].getStatus() == IAudioPlayer.PLAYING) {
					playedSound++;
				}
				
				if (policy == BaseAudio.MULTIPLE && !this.exclusive) {
					if (this.renderer[i].getStatus() != IAudioPlayer.PLAYING) {
						this.renderer[i].setVolume(this.volume);
						this.renderer[i].play();
						return i;
					}
					
				}
				else if (policy == BaseAudio.SINGLE_REPLAY) {
					// replay the sound
					if (this.exclusive) {
						this.stopAll();
					}
					else {
						this.renderer[i].stop();
					}
					
					this.renderer[i].setVolume(this.volume);
					this.renderer[i].play();
					
					return i;
					
				}
				else {
					// single policy no replay OR
					// multiple policy and exclusive mode
					if (this.exclusive) {
						// stop all except this audio renderer
						this.stopAll(this.renderer[i]);
					}
					
					if (this.renderer[i].getStatus() != IAudioPlayer.PLAYING) {
						this.renderer[i].setVolume(this.volume);
						this.renderer[i].play();
					}
					
					return i;
				}
			}
			
			// replace this idle slot
			if (emptyslot == -2
			        && this.renderer[i].getStatus() != IAudioPlayer.PLAYING) {
				emptyslot = i;
			}
		}
		
		// ////// attempt to play sound in new slot ////////
		
		// check for simultaneous sounds
		if (playedSound >= this.maxSimultaneous) {
			// too many simultaneous sounds!
			return -1;
		}
		
		if (emptyslot < 0) {
			// no empty slot, expand the renderer array
			this.renderer = (AudioPlayer[]) Utility.expand(this.renderer,
			        1);
			this.rendererFile = (String[]) Utility.expand(this.rendererFile, 1);
			emptyslot = this.renderer.length - 1;
		}
		
		if (this.renderer[emptyslot] == null) {
			// create new renderer in the empty slot
			this.renderer[emptyslot] = this.createRenderer();
			this.renderer[emptyslot].setLoop(this.loop);
		}
		
		if (this.exclusive) {
			// in exclusive mode, only one clip can be played at a time
			this.stopAll();
		}
		else {
			// to be sure the renderer is not playing
			this.stop(emptyslot);
		}
		
		this.renderer[emptyslot].setVolume(this.volume);
		this.renderer[emptyslot].play(this.base.getURL(audiofile));
		this.rendererFile[emptyslot] = audiofile;
		
		return emptyslot;
	}
	
	public void stop(int slot) {
		if (this.renderer[slot].getStatus() == IAudioPlayer.PLAYING) {
			this.renderer[slot].stop();
		}
	}
	
	public void stop(String audiofile) {
		AudioPlayer audio = this.getAudioRenderer(audiofile);
		
		if (audio != null) {
			audio.stop();
		}
	}
	
	public void stopAll() {
		int count = this.renderer.length;
		for (int i = 0; i < count; i++) {
			this.stop(i);
		}
	}
	
	public void stopAll(AudioPlayer except) {
		int count = this.renderer.length;
		for (int i = 0; i < count; i++) {
			if (this.renderer[i] != except) {
				this.stop(i);
			}
		}
	}
	
	public AudioPlayer getAudioRenderer(int slot) {
		return this.renderer[slot];
	}
	
	public AudioPlayer getAudioRenderer(String audiofile) {
		int count = this.renderer.length;
		for (int i = 0; i < count; i++) {
			// find renderer with specified audio file
			if (this.rendererFile[i].equals(audiofile)) {
				return this.renderer[i];
			}
		}
		
		return null;
	}
	
	public String getLastAudioFile() {
		return this.lastAudioFile;
	}
	
	public AudioPlayer[] getRenderers() {
		return this.renderer;
	}
	
	public int getCountRenderers() {
		return this.renderer.length;
	}
	
	public float getVolume() {
		return this.volume;
	}
	
	public void setVolume(float volume) {
		if (volume < 0.0f) {
			volume = 0.0f;
		}
		if (volume > 1.0f) {
			volume = 1.0f;
		}
		
		if (this.baseRenderer.isVolumeSupported() == false
		        || this.volume == volume) {
			return;
		}
		
		this.volume = volume;
		
		int count = this.renderer.length;
		for (int i = 0; i < count; i++) {
			this.renderer[i].setVolume(volume);
		}
	}
	
	public boolean isVolumeSupported() {
		return this.baseRenderer.isVolumeSupported();
	}
	
	public int getAudioPolicy() {
		return this.audioPolicy;
	}
	
	public void setAudioPolicy(int i) {
		this.audioPolicy = i;
	}
	
	public int getMaxSimultaneous() {
		return this.maxSimultaneous;
	}
	
	public void setMaxSimultaneous(int i) {
		this.maxSimultaneous = i;
	}
	
	public boolean isExclusive() {
		return this.exclusive;
	}
	
	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
		
		if (exclusive) {
			this.stopAll();
		}
	}
	
	public int getBuffer() {
		return this.buffer;
	}
	
	public void setBuffer(int i) {
		this.buffer = i;
	}
	
	public boolean isLoop() {
		return this.loop;
	}
	
	public void setLoop(boolean loop) {
		if (this.loop == loop) {
			return;
		}
		
		this.loop = loop;
		
		int count = this.renderer.length;
		for (int i = 0; i < count; i++) {
			this.renderer[i].setLoop(loop);
		}
	}
		
	public boolean isActive() {
		return this.active;
	}
	
	public void setActive(boolean b) {
		this.active = (this.isAvailable()) ? b : false;
		
		if (!this.active) {
			this.stopAll();
		}
	}
	
	public boolean isAvailable() {
		return this.baseRenderer.isAvailable();
	}
	
	public AudioPlayer getBaseRenderer() {
		return this.baseRenderer;
	}
	
	public void setBaseRenderer(AudioPlayer renderer) {
		this.baseRenderer = renderer;
		
		if (this.active) {
			this.active = this.baseRenderer.isAvailable();
		}
	}
	
	/**
	 * Constructs new audio renderer to play new audio sound.
	 * <p>
	 * 
	 * The new audio renderer is created using {@link Class#forName(String)}
	 * from the {@linkplain #getBaseRenderer() base renderer} class name.
	 * @return The new created renderer.
	 * @see #getBaseRenderer()
	 */
	protected AudioPlayer createRenderer() {
		try {
			return (AudioPlayer) Class.forName(
			        this.baseRenderer.getClass().getName()).newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(
			        "Unable to create new instance of audio renderer on "
			                + this
			                + " audio manager caused by: "
			                + e.getMessage()
			                + "\n"
			                + "Make sure the base renderer has one empty constructor!");
		}
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}

}
