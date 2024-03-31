package org.guideme.guideme.ui.main_shell;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.ui.SwtEmbeddedMediaPlayer;

class VideoPlay implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	VideoPlay(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// code to start the video on a separate thread
	private SwtEmbeddedMediaPlayer mediaPlayer;
	private String video;
	private int volume;
	private ArrayList<String> vlcArgs;

	@Override
	public void run() {
		LOGGER.debug("MainShell VideoPlay new Thread {}", video);

		mediaPlayer.audio().setVolume(volume);
		if (this.vlcArgs.isEmpty()) {
			mediaPlayer.media().play(video);
		} else {
			this.mainShell.myDisplay.syncExec(() -> {
				this.mainShell.mediaPanel.setVisible(true);
				this.mainShell.webcamPanel.setVisible(false);
				this.mainShell.leftPaneBrowser.setVisible(false);
				this.mainShell.leftFrame.layout(true);
			});
			mediaPlayer.media().play(video, vlcArgs.toArray(new String[vlcArgs.size()]));
			// run on the main UI thread
		}
	}

	public void setVideoPlay(SwtEmbeddedMediaPlayer mediaPlayer, String video, int volume) {
		int mediaVolume = this.mainShell.appSettings.getVideoVolume();
		volume = Math.min(Math.max(volume, 0), 100); // Bound between 0 and
														// 100

		this.mediaPlayer = mediaPlayer;
		this.video = video;
		this.volume = (int) (mediaVolume * ((double) volume / (double) 100));

		this.vlcArgs = new ArrayList<>();
		if (this.mainShell.videoStartAt > 0) {
			this.vlcArgs.add("start-time=" + this.mainShell.videoStartAt);
		}
		if (this.mainShell.videoStopAt > 0) {
			this.vlcArgs.add("stop-time=" + this.mainShell.videoStopAt);
		}
		if (this.mainShell.videoLoops > 0) {
			this.vlcArgs.add("input-repeat=" + this.mainShell.videoLoops);
		}
	}

}