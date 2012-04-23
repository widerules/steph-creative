
package com.droid.gamedev.engine.audio;

import java.net.URL;

import com.droid.gamedev.util.Log;


interface IAudioPlayer {
	
/** ********************** RENDERER STATUS CONSTANTS ************************ */
	
	/**
	 * Audio renderer status indicates that the audio is currently playing.
	 */
	int PLAYING = 1;
	
	/**
	 * Audio renderer status indicates that the audio is currently stopped.
	 */
	int STOPPED = 2;
	
	/**
	 * Audio renderer status indicates that the audio has finished played.
	 */
	int END_OF_SOUND = 3;
	
	/**
	 * Audio renderer status indicates that the audio is failed to play.
	 */
	int ERROR = 4;
	
	
	/** ************************************************************************* */
	/** ******************* MAIN ABSTRACT FUNCTION ****************************** */
	/** ************************************************************************* */
	
	/**
	 * Plays sound with specified audio file.
	 * @param audiofile The audio file to play.
	 */
	void playSound(URL audiofile);
	
	/**
	 * Replays last played sound.
	 * @param audiofile The audio file to replay.
	 */
	void replaySound(URL audiofile);
	
	/**
	 * Stops any playing sound.
	 */
	void stopSound();
	
	/**
	 * Sets audio sound volume.
	 * @param volume The new volume. The volume can be a value between 0.0f (min
	 *        volume) and 1.0f (max volume).
	 */
	void setSoundVolume(float volume);
	
	/** ************************************************************************* */
	/** ********************** AUDIO PLAYBACK FUNCTION ************************** */
	/** ************************************************************************* */
	
	/**
	 * Stops currently played audio and begins playback of specified audio file.
	 * 
	 * @param audiofile the audio file to be played by this renderer.
	 */
	void play(URL audiofile);
	
	/**
	 * Restarts last or currently played audio.
	 */
	void play() ;
	
	/**
	 * Stops currently played audio.
	 */
	void stop();
	
	/**
	 * Sets whether the sound should be playing continuously until stop method
	 * is called or not.
	 * <p>
	 * 
	 * Note: {@linkplain com.droid.gamedev.engine.audio.BaseAudioWin the sound manager}
	 * is the one that taking care the audio loop.
	 * 
	 * @param loop true, the audio will be playing continously
	 */
	void setLoop(boolean loop);
	
	/**
	 * Returns whether the audio is playing continuosly or not.
	 * @return If the audio is played continuosly.
	 */
	boolean isLoop();
	
	/** ************************************************************************* */
	/** ************************ AUDIO VOLUME SETTINGS ************************** */
	/** ************************************************************************* */
	
	/**
	 * Sets audio volume.
	 * @param volume The new volume. The volume can be a value between 0.0f (min
	 *        volume) and 1.0f (max volume).
	 */
	void setVolume(float volume);
	
	/**
	 * Returns audio volume.
	 * @return The volume. The value can lay between 0.0f and 1.0f
	 * @see #setVolume(float)
	 */
	float getVolume() ;
	
	/**
	 * Returns whether setting audio volume is supported or not.
	 * @return If setting the volume is supported.
	 */
	boolean isVolumeSupported();
	
	/** ************************************************************************* */
	/** ************************* AUDIO PROPERTIES ****************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns the audio resource URL associated with this audio renderer.
	 * @return The URL of the audio resource associated.
	 */
	URL getAudioFile();
	
	/**
	 * Returns the audio renderer status.
	 * @return The status.
	 * @see #PLAYING
	 * @see #STOPPED
	 * @see #END_OF_SOUND
	 * @see #ERROR
	 */
	int getStatus();
	
	/**
	 * Returns true, if this audio renderer is available to use or false if this
	 * renderer is not available to use (failed to initialized).
	 * @return If the renderer is available for use or not.
	 */
	boolean isAvailable();
}


/**
 * A simple abstraction for playing audio sound.
 * <p>
 * 
 * <code>BaseAudioRenderer</code> <b>must</b> have one empty constructor.
 * <br>
 * For example :
 * 
 * <pre>
 * public class MP3AudioRenderer extends BaseAudioRenderer {
 * 	
 * 	public MP3Renderer() { // you should provide an empty constructor
 * 		// init the class here
 * 	}
 * }
 * </pre>
 * 
 * <p>
 * 
 * The empty constructor is used by <code>BaseAudio</code> to create a new
 * renderer instance to play new sound using <code>Class.newInstance()</code>.
 */
public abstract class AudioPlayer implements IAudioPlayer{
	
	/** ************************* RENDERER VARIABLES **************************** */
	
	private URL audiofile;
	
	private boolean loop;
	
	/**
	 * The audio renderer status.
	 * <p>
	 * 
	 * Use this to manage renderer's {@link #END_OF_SOUND} status when the audio
	 * has finished played or {@link #ERROR} status if the audio is failed to
	 * play in {@link #playSound(URL)} method.
	 */
	protected int status;
	
	/**
	 * The audio sound volume.
	 */
	protected float volume;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new audio renderer.
	 * 
	 * @see #play(URL)
	 * @see #setLoop(boolean)
	 */
	public AudioPlayer() {
		this.volume = 1.0f;
		this.status = IAudioPlayer.STOPPED;
	}
	
	public void play(URL audiofile) {
		this.status = IAudioPlayer.PLAYING;
		if (this.audiofile == audiofile) {
			this.replaySound(audiofile);
			return;
		}
		
		this.audiofile = audiofile;
		this.playSound(audiofile);
	}
	
	public void play() {
		this.status = IAudioPlayer.PLAYING;
		this.replaySound(this.audiofile);
	}
	
	public void stop() {
		if (this.audiofile != null && this.status == IAudioPlayer.PLAYING) {
			this.status = IAudioPlayer.STOPPED;
			this.stopSound();
		}
	}
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public boolean isLoop() {
		return this.loop;
	}
	
	public void setVolume(float volume) {
		if (volume < 0.0f) {
			volume = 0.0f;
		}
		if (volume > 1.0f) {
			volume = 1.0f;
		}
		
		if (this.volume != volume && this.isVolumeSupported()) {
			this.volume = volume;
			
			this.setSoundVolume(volume);
		}
	}
	
	public float getVolume() {
		return this.volume;
	}
	
	public boolean isVolumeSupported() {
		return true;
	}
	
	public URL getAudioFile() {
		return this.audiofile;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
}
