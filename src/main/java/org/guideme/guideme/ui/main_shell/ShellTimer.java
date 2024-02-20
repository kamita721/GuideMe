package org.guideme.guideme.ui.main_shell;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.guideme.guideme.model.Timer;

class ShellTimer implements Runnable {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	ShellTimer(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// Timer to update:
	// the clock
	// count down timer
	// handle going to new page when timer counts down to 0
	@Override
	public void run() {
		if (mainShell.pauseRequested) {
			MainShell.logger.debug("pausing timers");
			mainShell.pauseAll();
			return;
		}

		if (mainShell.lblRight.isDisposed()) {
			return;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String javascript;
		long diff;
		int intSeconds;
		int intMinutes;
		String strSeconds;
		String strMinutes;
		String strTimeLeft;
		if (mainShell.calCountDown != null) {
			if (cal.after(mainShell.calCountDown)) {
				// Delay has reached zero
				mainShell.calCountDown = null;
				mainShell.lblRight.setText("");
				mainShell.comonFunctions.setFlags(mainShell.guide.getDelaySet(), mainShell.guide.getFlags());
				mainShell.comonFunctions.unsetFlags(mainShell.guide.getDelayUnSet(), mainShell.guide.getFlags());
				mainShell.comonFunctions.processSrciptVars(mainShell.guide.getDelayScriptVar(),
						mainShell.guideSettings);
				javascript = mainShell.guide.getDelayjScript();
				if (!javascript.equals("")) {
					mainShell.runJscript(javascript, false);
				}
				mainShell.mainLogic.displayPage(mainShell.guide.getDelTarget(), false, mainShell.guide, mainShell,
						mainShell.appSettings, mainShell.userSettings, mainShell.guideSettings, mainShell.debugShell);

			} else {
				if (mainShell.guide.getDelStyle().equals("hidden")) {
					// a hidden one
					mainShell.lblRight.setText("");
				} else if (mainShell.guide.getDelStyle().equals("secret")) {
					// secret timer so display ?? to show there
					// is one but not how long
					mainShell.lblRight.setText("??:??");
				} else {
					// Normal delay so display seconds left
					// (plus any offset if you are being sneaky)
					diff = mainShell.calCountDown.getTimeInMillis() - cal.getTimeInMillis();
					diff = diff + (mainShell.guide.getDelStartAtOffSet() * 1000);
					intSeconds = (int) ((diff / 1000) + 1);
					intMinutes = intSeconds / 60;
					intSeconds = intSeconds - (intMinutes * 60);
					strSeconds = String.valueOf(intSeconds);
					strSeconds = "0" + strSeconds;
					strSeconds = strSeconds.substring(strSeconds.length() - 2);
					strMinutes = String.valueOf(intMinutes);
					strMinutes = "0" + strMinutes;
					strMinutes = strMinutes.substring(strMinutes.length() - 2);
					strTimeLeft = strMinutes + ":" + strSeconds;
					mainShell.lblRight.setText(strTimeLeft);
				}

			}
		}
		if (mainShell.appSettings.isClock()) {
			mainShell.lblLeft.setText(dateFormat.format(cal.getTime()));
		} else {
			mainShell.lblLeft.setText("");
		}

		// check timers
		if (mainShell.getTimerCount() > 0) {
			Iterator<Entry<String, Timer>> it = mainShell.timer.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Timer> pair = it.next();
				Timer objTimer = pair.getValue();
				Calendar calTemp = objTimer.getTimerEnd();
				if (cal.after(calTemp)) {
					MainShell.logger.debug("Timer: " + objTimer.getId() + " Triggered");
					// add a year to the timer so we don't
					// trigger it again
					calTemp.add(Calendar.YEAR, 1);
					pair.getValue().setTimerEnd(calTemp);
					mainShell.comonFunctions.setFlags(objTimer.getSet(), mainShell.guide.getFlags());
					mainShell.comonFunctions.unsetFlags(objTimer.getUnSet(), mainShell.guide.getFlags());
					String strImage = objTimer.getImageId();
					if (!strImage.equals("")) {
						String imgPath = mainShell.comonFunctions.getMediaFullPath(strImage,
								mainShell.appSettings.getFileSeparator(), mainShell.appSettings, mainShell.guide);
						File flImage = new File(imgPath);
						if (flImage.exists()) {
							mainShell.setImage(imgPath);
						}
					}
					String displayText = objTimer.getText();
					if (!displayText.equals("")) {
						// Media Directory
						String mediaPath;
						mediaPath = mainShell.comonFunctions.getMediaFullPath("",
								mainShell.appSettings.getFileSeparator(), mainShell.appSettings, mainShell.guide);
						displayText = displayText.replace("\\MediaDir\\", mediaPath);

						displayText = mainShell.comonFunctions.substituteTextVars(displayText, mainShell.guideSettings,
								mainShell.userSettings);

						mainShell.setBrwsText(displayText, "");
					}
					javascript = objTimer.getjScript();
					if (!javascript.equals("")) {
						mainShell.runJscript(javascript, false);
					}
					String target = objTimer.getTarget();
					if (!target.equals("")) {
						mainShell.lblRight.setText("");
						mainShell.mainLogic.displayPage(target, false, mainShell.guide, mainShell,
								mainShell.appSettings, mainShell.userSettings, mainShell.guideSettings,
								mainShell.debugShell);
					}
				}
			}
		}
		// re run in 0.1 seconds
		mainShell.myDisplay.timerExec(100, new ShellTimer(mainShell));
	}
}