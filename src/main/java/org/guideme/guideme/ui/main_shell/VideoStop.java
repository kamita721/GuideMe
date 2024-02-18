package org.guideme.guideme.ui.main_shell;

import org.guideme.guideme.ui.SwtEmbeddedMediaPlayer;

class VideoStop implements Runnable {
	private SwtEmbeddedMediaPlayer mediaPlayer;
	private boolean shellClosing;

	public void setMediaPlayer(SwtEmbeddedMediaPlayer mediaPlayer, boolean shellClosing) {
		this.mediaPlayer = mediaPlayer;
		this.shellClosing = shellClosing;
	}

	@Override
	public void run() {
		try {
			if (mediaPlayer != null && mediaPlayer.status().isPlaying()) {
				MainShell.logger.debug("MainShell VideoStop run: Stopping media player " + mediaPlayer.media().info().mrl());
				mediaPlayer.controls().pause();
				if (shellClosing) {
					mediaPlayer.release();
				}
			}
		} catch (Exception e) {
			MainShell.logger.error(" MainShell VideoStop run: " + e.getLocalizedMessage(), e);
		}
	}

}