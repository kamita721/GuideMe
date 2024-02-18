package org.guideme.guideme.ui.mainShell;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

class MediaListener extends MediaPlayerEventAdapter {
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
		MainShell.logger.debug("MediaListener finished " + mediaPlayer.media().info().mrl());
		super.finished(mediaPlayer);
		try {
			if (!this.mainShell.videoTarget.equals("")) {
				// run on the main UI thread
				this.mainShell.myDisplay.asyncExec(new Runnable() {
					public void run() {
						MediaListener.this.mainShell.mediaPanel.setVisible(false);
						MediaListener.this.mainShell.webcamPanel.setVisible(false);
						MediaListener.this.mainShell.imageLabel.setVisible(true);
						MediaListener.this.mainShell.leftFrame.layout(true);
						MainShell.logger.debug("MediaListener Video Run: " + MediaListener.this.mainShell.videoJscript + " videoTarget: " + MediaListener.this.mainShell.videoTarget);
						MediaListener.this.mainShell.mainShell.runJscript(MediaListener.this.mainShell.videoJscript, false);
						MediaListener.this.mainShell.mainShell.displayPage(MediaListener.this.mainShell.videoTarget);
					}
				});
			}
			this.mainShell.comonFunctions.processSrciptVars(this.mainShell.videoScriptVar, this.mainShell.guideSettings);
			/*
			 * //run on the main UI thread myDisplay.syncExec( new
			 * Runnable() { public void run(){ mediaPanel.setVisible(false);
			 * imageLabel.setVisible(true); leftFrame.layout(true); } });
			 */
		} catch (Exception ex) {
			MainShell.logger.error(" MediaListener finished " + ex.getLocalizedMessage(), ex);
		}
	}

	/*
	 * //newState 5 indicates the video has finished //videoPlay can be set
	 * to false outside the code to tell it to stop //if the video finishes
	 * loop round again if a number of repeats has been set
	 * 
	 * @Override public void mediaStateChanged(MediaPlayer lmediaPlayer, int
	 * newState) { super.mediaStateChanged(lmediaPlayer, newState);
	 * logger.debug("MediaListener newState: " + newState + " videoPlay: " +
	 * videoPlay + " VideoTarget: " + videoTarget + " file:" +
	 * lmediaPlayer.mrl()); try { if ((newState==5 || newState==6) &&
	 * videoPlay){ if (!videoTarget.equals("")) { //run on the main UI
	 * thread myDisplay.asyncExec( new Runnable() { public void run(){
	 * logger.debug("MediaListener Video Run: " + videoJscript +
	 * " videoTarget: " + videoTarget); mainShell.runJscript(videoJscript,
	 * false); mainShell.displayPage(videoTarget); } }); }
	 * comonFunctions.processSrciptVars(videoScriptVar, guideSettings); } }
	 * catch (Exception e) { logger.error("mediaStateChanged " +
	 * e.getLocalizedMessage(), e); } }
	 */

	@Override
	public void error(MediaPlayer mediaPlayer) {
		MainShell.logger.error("MediaPlayer error ");
		super.error(mediaPlayer);
	}
}