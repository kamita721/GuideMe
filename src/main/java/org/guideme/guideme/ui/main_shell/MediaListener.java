package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

class MediaListener extends MediaPlayerEventAdapter {
	private static final Logger LOGGER = LogManager.getLogger();
	// Video event listener

	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	MediaListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// Video has finished
	@Override
	public void finished(MediaPlayer mediaPlayer) {
		// TODO, this should be displaying what media finished
		LOGGER.debug(() -> "MediaListener finished " + mediaPlayer.media().info().mrl());
		super.finished(mediaPlayer);

		if (!mainShell.videoTarget.equals("")) {
			// run on the main UI thread
			mainShell.myDisplay.asyncExec(() -> {
				mainShell.mediaPanel.setVisible(false);
				mainShell.webcamPanel.setVisible(false);
				mainShell.leftPaneBrowser.setVisible(true);
				mainShell.leftFrame.layout(true);
				LOGGER.debug("MediaListener Video Run: " + mainShell.videoJscript + " videoTarget: "
						+ mainShell.videoTarget);
				mainShell.runJscript(mainShell.videoJscript, false);
				mainShell.displayPage(mainShell.videoTarget);
			});
		}
		mainShell.comonFunctions.processSrciptVars(mainShell.videoScriptVar,
				mainShell.guideSettings);

	}

	@Override
	public void error(MediaPlayer mediaPlayer) {
		LOGGER.error("MediaPlayer error ");
		super.error(mediaPlayer);
	}
}