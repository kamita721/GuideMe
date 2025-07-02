package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

class VideoStop implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();
	private MediaPlayer mediaPlayer;
	private boolean shellClosing;

	public void setMediaPlayer(MediaPlayer mediaPlayer, boolean shellClosing) {
		this.mediaPlayer = mediaPlayer;
		this.shellClosing = shellClosing;
	}

	@Override
	public void run() {
		if (mediaPlayer != null && mediaPlayer.status().isPlaying()) {
			LOGGER.debug(() -> "MainShell VideoStop run: Stopping media player "
					+ mediaPlayer.media().info().mrl());
			mediaPlayer.controls().pause();
			if (shellClosing) {
				mediaPlayer.release();
			}
		}
	}

}