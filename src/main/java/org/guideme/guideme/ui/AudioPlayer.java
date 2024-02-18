package org.guideme.guideme.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.ComonFunctions;

import uk.co.caprica.vlcj.binding.lib.LibVlc;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

public class AudioPlayer  implements Runnable {
	//Class to play audio on a separate thread
	private static Logger logger = LogManager.getLogger();
	private MediaListener mediaListener = new MediaListener();
	private MediaPlayer mediaPlayer;
	private Boolean isPlaying = true;
	private String audioFile = "";
	private int loops = 0;
	private int startAt = 0;
	private int stopAt = 0;
	private String target;
	private MainShell mainShell;
	private String jscript;
	private String scriptVar;
	private String outputDevice;
	private int volume;
	private ArrayList<String> vlcArgs;

	public AudioPlayer(String audioFile, int startAt, int stopAt, int loops, String target, MainShell mainShell, String jscript, String scriptVar, String outputDevice, int volume) {
		//function to allow us to pass stuff to the new thread
		int mediaVolume = AppSettings.getAppSettings().getMusicVolume();
		volume = Math.min(Math.max(volume, 0), 100); // Bound between 0 and 100

		this.audioFile = audioFile;
		this.loops = loops;
		this.mainShell = mainShell;
		this.target = target;
		this.jscript = jscript;
		this.startAt = startAt;
		this.stopAt = stopAt;
		this.scriptVar = scriptVar;
		this.outputDevice = outputDevice;
		this.volume = (int) ((double) mediaVolume * ((double) volume / (double) 100));

		this.vlcArgs = new ArrayList<>();
		if (startAt > 0) {
			this.vlcArgs.add("start-time=" + startAt);
		}
		if (stopAt > 0) {
			this.vlcArgs.add("stop-time=" + stopAt);
		}
		if (loops > 0) {
			this.vlcArgs.add("input-repeat=" + loops);
		}
	}

	public void audioStop() {
		//stop the audio 
		if (mediaPlayer != null) {
			if (mediaPlayer.status().isPlaying()) {
				mediaPlayer.controls().stop();
			}
		}
		synchronized(this){
			isPlaying = false;
			//Thread.currentThread().interrupt();
			notifyAll();
		}
	}

	public void audioPause() {
		if (mediaPlayer != null && mediaPlayer.status().isPlaying()) {
			mediaPlayer.controls().pause();
		}
		logger.trace("AudioPlayer Pause");
	}

	public void audioResume() {
		if (mediaPlayer != null && mediaPlayer.status().isPlayable()) {
			mediaPlayer.controls().play();
		}
		logger.trace("AudioPlayer Resume");
	}

	public void run() {
		try {
			//Play the audio set up by AudioPlayer
			//use a media list to play loops
			//TODO should probably use the factory
			mediaPlayer =  new MediaPlayer(LibVlc.libvlc_new(0, null));
			if (this.outputDevice != null && !this.outputDevice.equals("")) {
				//TODO this doesn't seem quite right
				mediaPlayer.audio().setOutputDevice(null, this.outputDevice);
			}
			mediaPlayer.events().addMediaPlayerEventListener(mediaListener);
			mediaPlayer.audio().setVolume(volume);
			if (this.vlcArgs.isEmpty()) {
				mediaPlayer.media().play(audioFile);
			} else {
				mediaPlayer.media().play(audioFile, vlcArgs.toArray(new String[vlcArgs.size()]));
			}
			synchronized(this) {
				while (isPlaying) {
					// while the audio is still running carry on looping
					//Thread.sleep(5000);
					wait();
				}
			}
		} catch (Exception e) {
			logger.error("AudioPlayer run ", e);
		}
		if (mediaPlayer != null) {
			if (mediaPlayer.status().isPlaying()) {
				mediaPlayer.controls().stop();
			}
			mediaPlayer.events().removeMediaPlayerEventListener(mediaListener);
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public synchronized void setAudioDevice(String device)
	{
		mediaPlayer.audio().setOutputDevice(null, device);
	}


	class MediaListener extends MediaPlayerEventAdapter {

		@Override
		public void stopped(MediaPlayer mediaPlayer) {
			super.stopped(mediaPlayer);
			logger.debug("Stopped ");
			Display display = Display.getDefault();
			//listener to handle displaying a new page when the audio ends
			if (isPlaying){
				if (!target.equals(""))  {
					//run on the main UI thread
					display.syncExec(
							new Runnable() {
								public void run(){
									mainShell.runJscript(jscript, false);
									mainShell.displayPage(target);
								}
							});											
				}
				ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
				comonFunctions.processSrciptVars(scriptVar, mainShell.getGuideSettings());
				isPlaying = false;
			}
			display = null;
		}

	}

}
