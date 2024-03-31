package org.guideme.guideme.ui.main_shell;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import org.guideme.generated.model.Timer;

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
			MainShell.LOGGER.debug("pausing timers");
			mainShell.pauseAll();
			return;
		}

		if (mainShell.lblTimer.isDisposed()) {
			return;
		}

		updateCountdown();
		updateClock();
		checkTimers();

		// re run in 0.1 seconds
		mainShell.myDisplay.timerExec(100, new ShellTimer(mainShell));
	}

	private void checkTimers() {

		Calendar cal = Calendar.getInstance();
		
		Iterator<Timer> iter = mainShell.timer.values().iterator();
		while (iter.hasNext()) {
			Timer timer = iter.next();
			Calendar calTemp = timer.getTimerEnd();
			if (cal.after(calTemp)) {
				onTimerTrigger(timer);
				iter.remove();
			}
		}
	}

	private void onTimerTrigger(Timer timer) {
		MainShell.LOGGER.debug("Timer: " + timer.getId() + " Triggered");
		mainShell.setUnsetFlagsUncooked(timer.getSet(), timer.getUnSet());
		mainShell.setImageByUncooked(timer.getImageId());
		mainShell.setBrwsTextUncooked(timer.getText());
		mainShell.runJscriptUncooked(timer.getJscript(), false);
		mainShell.gotoTargetUncooked(timer.getTarget());
		
	}
	
	private void updateClock() {

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		if (mainShell.appSettings.isClock()) {
			mainShell.lblClock.setText(dateFormat.format(cal.getTime()));
		} else {
			mainShell.lblClock.setText("");
		}
	}

	private void updateCountdown() {
		Calendar cal = Calendar.getInstance();
		if (mainShell.calCountDown != null) {
			if (cal.after(mainShell.calCountDown)) {
				onDelayEnd();
			} else {
				if (mainShell.guide.getDelStyle().equals("hidden")) {
					mainShell.lblTimer.setText("");
				} else if (mainShell.guide.getDelStyle().equals("secret")) {
					mainShell.lblTimer.setText("??:??");
				} else {
					// Normal delay so display seconds left
					// (plus any offset if you are being sneaky)
					long diff = mainShell.calCountDown.getTimeInMillis() - cal.getTimeInMillis();
					diff = diff + (mainShell.guide.getDelStartAtOffSet() * 1000);
					mainShell.lblTimer.setText(formatTimeLeft(diff));
				}

			}
		}
	}

	private void onDelayEnd() {
		mainShell.calCountDown = null;
		mainShell.lblTimer.setText("");
		mainShell.comonFunctions.setFlags(mainShell.guide.getDelaySet(),
				mainShell.guide.getFlags());
		mainShell.comonFunctions.unsetFlags(mainShell.guide.getDelayUnSet(),
				mainShell.guide.getFlags());
		mainShell.comonFunctions.processSrciptVars(mainShell.guide.getDelayScriptVar(),
				mainShell.guideSettings);
		String javascript = mainShell.guide.getDelayjScript();
		if (!javascript.equals("")) {
			mainShell.runJscript(javascript, false);
		}
		mainShell.mainLogic.displayPage(mainShell.guide.getDelTarget(), false, mainShell.guide,
				mainShell, mainShell.appSettings, mainShell.userSettings, mainShell.guideSettings,
				mainShell.debugShell);

	}

	private String formatTimeLeft(long delta) {
		int intSeconds = (int) ((delta / 1000) + 1);
		int intMinutes = intSeconds / 60;
		intSeconds = intSeconds - (intMinutes * 60);
		String strSeconds = String.valueOf(intSeconds);
		strSeconds = "0" + strSeconds;
		strSeconds = strSeconds.substring(strSeconds.length() - 2);
		String strMinutes = String.valueOf(intMinutes);
		strMinutes = "0" + strMinutes;
		strMinutes = strMinutes.substring(strMinutes.length() - 2);
		return strMinutes + ":" + strSeconds;
	}
}