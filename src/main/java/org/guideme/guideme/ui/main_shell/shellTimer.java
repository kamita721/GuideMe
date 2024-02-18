package org.guideme.guideme.ui.main_shell;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.guideme.guideme.model.Timer;

class shellTimer implements Runnable {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	shellTimer(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// Timer to update:
	// the clock
	// count down timer
	// handle going to new page when timer counts down to 0
	@Override
	public void run() {
		try {
			// logger.trace("Enter shellTimer");
			if (this.mainShell.pauseRequested) {
				MainShell.logger.debug("pausing timers");
				this.mainShell.pauseAll();
				return;
			}
			if (!this.mainShell.lblRight.isDisposed()) {
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				String javascript;
				long diff;
				int intSeconds;
				int intMinutes;
				String strSeconds;
				String strMinutes;
				String strTimeLeft;
				if (this.mainShell.calCountDown != null) {
					if (cal.after(this.mainShell.calCountDown)) {
						// Delay has reached zero
						try {
							this.mainShell.calCountDown = null;
							this.mainShell.lblRight.setText("");
							this.mainShell.comonFunctions.setFlags(this.mainShell.guide.getDelaySet(), this.mainShell.guide.getFlags());
							this.mainShell.comonFunctions.unsetFlags(this.mainShell.guide.getDelayUnSet(), this.mainShell.guide.getFlags());
							this.mainShell.comonFunctions.processSrciptVars(this.mainShell.guide.getDelayScriptVar(), this.mainShell.guideSettings);
							javascript = this.mainShell.guide.getDelayjScript();
							if (!javascript.equals("")) {
								this.mainShell.mainShell.runJscript(javascript, false);
							}
							this.mainShell.mainLogic.displayPage(this.mainShell.guide.getDelTarget(), false, this.mainShell.guide, this.mainShell.mainShell, this.mainShell.appSettings,
									this.mainShell.userSettings, this.mainShell.guideSettings, this.mainShell.debugShell);
						} catch (Exception ex) {
							MainShell.logger.error(" shellTimer Delay Zero " + ex.getLocalizedMessage(), ex);
						}
					} else {
						try {
							if (this.mainShell.guide.getDelStyle().equals("hidden")) {
								// a hidden one
								this.mainShell.lblRight.setText("");
							} else if (this.mainShell.guide.getDelStyle().equals("secret")) {
								// secret timer so display ?? to show there
								// is one but not how long
								this.mainShell.lblRight.setText("??:??");
							} else {
								// Normal delay so display seconds left
								// (plus any offset if you are being sneaky)
								diff = this.mainShell.calCountDown.getTimeInMillis() - cal.getTimeInMillis();
								diff = diff + (this.mainShell.guide.getDelStartAtOffSet() * 1000);
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
								this.mainShell.lblRight.setText(strTimeLeft);
							}
						} catch (Exception ex) {
							MainShell.logger.error(" shellTimer Update Count Down " + ex.getLocalizedMessage(), ex);
						}

					}
				}
				try {
					if (this.mainShell.appSettings.isClock()) {
						this.mainShell.lblLeft.setText(dateFormat.format(cal.getTime()));
					} else {
						this.mainShell.lblLeft.setText("");
					}
				} catch (Exception ex) {
					MainShell.logger.error(" shellTimer Update Clock " + ex.getLocalizedMessage(), ex);
				}

				try {
					// check timers
					if (this.mainShell.getTimerCount() > 0) {
						Iterator<Entry<String, Timer>> it = this.mainShell.timer.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry<String, Timer> pair = it.next();
							Timer objTimer = pair.getValue();
							Calendar calTemp = objTimer.getTimerEnd();
							// logger.debug("Timer: " + objTimer.getId() + "
							// End: " + calTemp.getTime());
							// logger.debug("Timer: " + objTimer.getId() + "
							// Now: " + cal.getTime());
							if (cal.after(calTemp)) {
								MainShell.logger.debug("Timer: " + objTimer.getId() + " Triggered");
								// add a year to the timer so we don't
								// trigger it again
								calTemp.add(Calendar.YEAR, 1);
								pair.getValue().setTimerEnd(calTemp);
								this.mainShell.comonFunctions.setFlags(objTimer.getSet(), this.mainShell.guide.getFlags());
								this.mainShell.comonFunctions.unsetFlags(objTimer.getUnSet(), this.mainShell.guide.getFlags());
								String strImage = objTimer.getImageId();
								if (!strImage.equals("")) {
									String imgPath = this.mainShell.comonFunctions.getMediaFullPath(strImage,
											this.mainShell.appSettings.getFileSeparator(), this.mainShell.appSettings, this.mainShell.guide);
									File flImage = new File(imgPath);
									if (flImage.exists()) {
										try {
											this.mainShell.setImage(imgPath);
										} catch (Exception e1) {
											MainShell.logger.error("Timer Image Exception " + e1.getLocalizedMessage(), e1);
										}
									}
								}
								String displayText = objTimer.getText();
								if (!displayText.equals("")) {
									try {
										// Media Directory
										try {
											String mediaPath;
											mediaPath = this.mainShell.comonFunctions.getMediaFullPath("",
													this.mainShell.appSettings.getFileSeparator(), this.mainShell.appSettings, this.mainShell.guide);
											displayText = displayText.replace("\\MediaDir\\", mediaPath);
										} catch (Exception e) {
											MainShell.logger.error("displayPage BrwsText Media Directory Exception "
													+ e.getLocalizedMessage(), e);
										}

										displayText = this.mainShell.comonFunctions.substituteTextVars(displayText, this.mainShell.guideSettings,
												this.mainShell.userSettings);

										this.mainShell.setBrwsText(displayText, "");
									} catch (Exception e) {
										MainShell.logger.error("Timer BrwsText Exception " + e.getLocalizedMessage(), e);
									}
								}
								javascript = objTimer.getjScript();
								if (!javascript.equals("")) {
									this.mainShell.mainShell.runJscript(javascript, false);
								}
								String target = objTimer.getTarget();
								if (!target.equals("")) {
									this.mainShell.lblRight.setText("");
									this.mainShell.mainLogic.displayPage(target, false, this.mainShell.guide, this.mainShell.mainShell, this.mainShell.appSettings,
											this.mainShell.userSettings, this.mainShell.guideSettings, this.mainShell.debugShell);
								}
							}
							// it.remove(); // avoids a
							// ConcurrentModificationException
						}
					}
				} catch (Exception ex) {
					MainShell.logger.error(" shellTimer Timers " + ex.getLocalizedMessage(), ex);
				}
				dateFormat = null;
				cal = null;
				javascript = null;
				strSeconds = null;
				strMinutes = null;
				strTimeLeft = null;
				// re run in 0.1 seconds
				this.mainShell.myDisplay.timerExec(100, new shellTimer(this.mainShell));
			}
		} catch (Exception ex) {
			MainShell.logger.error(" shellTimer " + ex.getLocalizedMessage(), ex);
		}
		// logger.trace("Exit shellTimer");
	}
}