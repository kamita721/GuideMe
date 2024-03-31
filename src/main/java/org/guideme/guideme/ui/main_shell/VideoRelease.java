package org.guideme.guideme.ui.main_shell;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

class VideoRelease implements Runnable {
	// Do the release of the Video stuff (VLC) on a different thread to
	// prevent it blocking the main UI thread
	private MediaPlayerFactory mediaPlayerFactoryThread;
	private EmbeddedMediaPlayer mediaPlayerThread;

	@Override
	public void run() {
		try {
			mediaPlayerThread.controls().stop();
			mediaPlayerThread.release();
			mediaPlayerFactoryThread.release();
			MainShell.LOGGER.trace("VideoRelease Exit");
		} catch (Exception ex) {
			MainShell.LOGGER.error("Video release " + ex.getLocalizedMessage(), ex);
		}
	}

	public void setVideoRelease(EmbeddedMediaPlayer mediaPlayer, MediaPlayerFactory mediaPlayerFactory) {
		try {
			mediaPlayerFactoryThread = mediaPlayerFactory;
			mediaPlayerThread = mediaPlayer;
		} catch (Exception ex) {
			MainShell.LOGGER.error("Video release " + ex.getLocalizedMessage(), ex);
		}
	}

}