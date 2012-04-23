package com.droid.gamedev.engine.audio;

import java.net.URL;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;


/**
 * Play midi sound (*.mid).
 * <p>
 * 
 * Note: Midi sound use soundbank that not delivered in JRE, only JDK can play
 * midi sound properly. <br>
 * In order to play midi sound properly in JRE you must explicitly install
 * soundbank. <br>
 * Download soundbank from java sun website (<a
 * href="http://java.sun.com/products/java-media/sound/soundbanks.html">
 * http://java.sun.com/products/java-media/sound/soundbanks.html</a>) and refer
 * to the manual how to install it.
 */
class MidiPlayer extends AudioPlayer implements
        MetaEventListener {
	
	/** ************************* MIDI CONSTANTS ******************************** */
	
	// end of song event
	private static final int MIDI_EOT_MESSAGE = 47;
	
	// volume
	private static final int GAIN_CONTROLLER = 7;
	
	/** *************************** MIDI SEQUENCER ****************************** */
	
	private Sequencer sequencer;
	
	/** ************************************************************************* */
	/** ******************** VALIDATING MIDI SEQUENCER ************************** */
	/** ************************************************************************* */
	
	private static boolean available;
	private static boolean volumeSupported;
	
	private static final int UNINITIALIZED = 0;
	private static final int INITIALIZING = 1;
	private static final int INITIALIZED = 2;
	
	private static int rendererStatus = MidiPlayer.UNINITIALIZED;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new midi audio renderer.
	 */
	public MidiPlayer() {
		if (MidiPlayer.rendererStatus == MidiPlayer.UNINITIALIZED) {
			MidiPlayer.rendererStatus = MidiPlayer.INITIALIZING;
			
			Thread thread = new Thread() {
				
				@Override
				public final void run() {
					try {
						Sequencer sequencer = MidiSystem.getSequencer();
						sequencer.open();
						MidiPlayer.volumeSupported = (sequencer instanceof Synthesizer);
						sequencer.close();
						
						MidiPlayer.available = true;
					}
					catch (Throwable e) {
						System.err
						        .println("WARNING: Midi audio playback is not available!");
						MidiPlayer.available = false;
					}
					
					MidiPlayer.rendererStatus = MidiPlayer.INITIALIZED;
				}
			};
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	public boolean isAvailable() {
		if (MidiPlayer.rendererStatus != MidiPlayer.INITIALIZED) {
			int i = 0;
			while (MidiPlayer.rendererStatus != MidiPlayer.INITIALIZED
			        && i++ < 50) {
				try {
					Thread.sleep(50L);
				}
				catch (InterruptedException e) {
				}
			}
			if (MidiPlayer.rendererStatus != MidiPlayer.INITIALIZED) {
				MidiPlayer.rendererStatus = MidiPlayer.INITIALIZED;
				MidiPlayer.available = false;
			}
		}
		
		return MidiPlayer.available;
	}
	
	/** ************************************************************************* */
	/** ********************** AUDIO PLAYBACK FUNCTION ************************** */
	/** ************************************************************************* */
	
	public void playSound(URL audiofile) {
		try {
			if (this.sequencer == null) {
				this.sequencer = MidiSystem.getSequencer();
				if (!this.sequencer.isOpen()) {
					this.sequencer.open();
				}
			}
			
			Sequence seq = MidiSystem.getSequence(this.getAudioFile());
			this.sequencer.setSequence(seq);
			this.sequencer.start();
			this.sequencer.addMetaEventListener(MidiPlayer.this);
			
			// the volume of newly loaded audio is always 1.0f
			if (this.volume != 1.0f) {
				this.setSoundVolume(this.volume);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			this.status = IAudioPlayer.ERROR;
		}
	}
	
	public void replaySound(URL audiofile) {
		this.sequencer.start();
		this.sequencer.addMetaEventListener(this);
	}
	
	public void stopSound() {
		this.sequencer.stop();
		this.sequencer.setMicrosecondPosition(0);
		this.sequencer.removeMetaEventListener(this);
	}
	
	/** ************************************************************************* */
	/** *********************** MIDI EVENT LISTENER ***************************** */
	/** ************************************************************************* */
	
	/**
	 * Notified when the sound has finished playing.
	 */
	public void meta(MetaMessage msg) {
		if (msg.getType() == MidiPlayer.MIDI_EOT_MESSAGE) {
			this.status = IAudioPlayer.END_OF_SOUND;
			this.sequencer.setMicrosecondPosition(0);
			this.sequencer.removeMetaEventListener(this);
		}
	}
	
	/** ************************************************************************* */
	/** ************************ AUDIO VOLUME SETTINGS ************************** */
	/** ************************************************************************* */
	
	public void setSoundVolume(float volume) {
		if (this.sequencer == null) {
			return;
		}
		
		MidiChannel[] channels = ((Synthesizer) this.sequencer).getChannels();
		for (int i = 0; i < channels.length; i++) {
			channels[i].controlChange(MidiPlayer.GAIN_CONTROLLER,
			        (int) (volume * 127));
		}
	}
	
	@Override
	public boolean isVolumeSupported() {
		return MidiPlayer.volumeSupported;
	}
		
}
