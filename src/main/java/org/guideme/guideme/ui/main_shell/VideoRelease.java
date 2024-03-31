package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

class VideoRelease implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();
	// Do the release of the Video stuff (VLC) on a different thread to
	// prevent it blocking the main UI thread
	private MediaPlayerFactory mediaPlayerFactoryThread;
	private EmbeddedMediaPlayer mediaPlayerThread;

	@Override
	public void run() {
		mediaPlayerThread.controls().stop();
		mediaPlayerThread.release();
		mediaPlayerFactoryThread.release();
		LOGGER.trace("VideoRelease Exit");
	}

	public void setVideoRelease(EmbeddedMediaPlayer mediaPlayer,
			MediaPlayerFactory mediaPlayerFactory) {
		mediaPlayerFactoryThread = mediaPlayerFactory;
		mediaPlayerThread = mediaPlayer;
	}

}