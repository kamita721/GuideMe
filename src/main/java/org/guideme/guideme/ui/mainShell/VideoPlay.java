package org.guideme.guideme.ui.mainShell;

import java.util.ArrayList;

import org.guideme.guideme.ui.SwtEmbeddedMediaPlayer;

class VideoPlay implements Runnable {
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
			try {
				MainShell.logger.debug("MainShell VideoPlay new Thread " + video);

				mediaPlayer.audio().setVolume(volume);
				// TODO this setting seems to have disappeared
//				mediaPlayer.setPlaySubItems(true);
				if (this.vlcArgs.isEmpty()) {
					mediaPlayer.media().play(video);
				} else {
					this.mainShell.myDisplay.syncExec(() -> {
						this.mainShell.mediaPanel.setVisible(true);
						this.mainShell.webcamPanel.setVisible(false);
						this.mainShell.imageLabel.setVisible(false);
						this.mainShell.leftFrame.layout(true);
					});
					mediaPlayer.media().play(video, vlcArgs.toArray(new String[vlcArgs.size()]));
					// run on the main UI thread
				}
			} catch (Exception e) {
				MainShell.logger.error("VideoPlay run " + e.getLocalizedMessage(), e);
			}
		}

		public void setVideoPlay(SwtEmbeddedMediaPlayer mediaPlayer, String video, int volume) {
			int mediaVolume = this.mainShell.appSettings.getVideoVolume();
			volume = Math.min(Math.max(volume, 0), 100); // Bound between 0 and
															// 100

			this.mediaPlayer = mediaPlayer;
			this.video = video;
			this.volume = (int) ((double) mediaVolume * ((double) volume / (double) 100));

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