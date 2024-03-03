package org.guideme.guideme.scripting;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.guideme.generated.model.Audio;
import org.guideme.generated.model.Audio1;
import org.guideme.generated.model.Audio2;
import org.guideme.generated.model.BasicButton;
import org.guideme.generated.model.Button;
import org.guideme.generated.model.Delay;
import org.guideme.generated.model.GlobalButton;
import org.guideme.generated.model.Metronome;
import org.guideme.generated.model.Timer;
import org.guideme.generated.model.Video;
import org.guideme.generated.model.Webcam;
import org.guideme.generated.model.WebcamButton;
import org.guideme.guideme.model.*;

public class OverRide {
	/** @exclude */
	private ArrayList<Button> button = new ArrayList<>();
	/** @exclude */
	private ArrayList<GlobalButton> globalButton = new ArrayList<>();
	/** @exclude */
	private ArrayList<WebcamButton> webcamButton = new ArrayList<>();
	/** @exclude */
	private ArrayList<Timer> timer = new ArrayList<>();
	/** @exclude */
	private Delay delay = null;
	/** @exclude */
	private Video video = null;
	/** @exclude */
	private Webcam webcam = null;
	/** @exclude */
	private Audio1 audio = null;
	/** @exclude */
	private Audio2 audio2 = null;
	/** @exclude */
	private Metronome metronome = null;
	/** @exclude */
	private String image = "";
	/** @exclude */
	private String html = "";
	/** @exclude */
	private String rightHtml = "";
	/** @exclude */
	private String page = "";
	/** @exclude */
	private String leftHtml = "";
	/** @exclude */
	private String leftBody = "";
	/** @exclude */
	private String rightCss = "";
	/** @exclude */
	private String leftCss = "";

	/**
	 * Adds a button to the page
	 *
	 * @param target the page to go to
	 * @param text   the text to be displayed on the button
	 */
	public void addButton(String target, String text) {
		addButton(target, text, "", "", "", "", "");
	}

	/**
	 * Adds a button to the page
	 * 
	 * @param target  the page to go to
	 * @param text    the text to display on the button
	 * @param set     the flags to set if the button is pressed
	 * @param unSet   the flags to clear if the button is pressed
	 * @param jScript the Java Script function to run if the button is pressed
	 * @param image   the background image for the button
	 */
	public void addButton(String target, String text, String set, String unSet, String jScript,
			String image) {
		addButton(target, text, set, unSet, jScript, image, "");
	}

	/**
	 * Adds a button to the page
	 * 
	 * @param target  the page to go to
	 * @param text    the text to be displayed on the button
	 * @param set     the flags to set if the button is pressed
	 * @param unSet   the flags to clear if the button is pressed
	 * @param jScript the Java Script function to run if the button is pressed
	 * @param image   the background image for the button
	 * @param hotKey  the hot key assigned to the button
	 */
	public void addButton(String target, String text, String set, String unSet, String jScript,
			String image, String hotKey) {
		ButtonThread buttonThread = new ButtonThread();
		buttonThread.overRide = this;
		buttonThread.target = target;
		buttonThread.text = text;
		buttonThread.set = set;
		buttonThread.unSet = unSet;
		buttonThread.jScript = jScript;
		buttonThread.image = image;
		buttonThread.hotKey = hotKey;
		Display.getDefault().syncExec(buttonThread);
	}

	private class ButtonThread implements Runnable {
		public OverRide overRide;
		public String target;
		public String text;
		public String set;
		public String unSet;
		public String jScript;
		public String image;
		public String hotKey;

		public void run() {
			BasicButton button = new BasicButton();
			button.setTarget(target);
			button.setText(text);
			button.setSet(set);
			button.setUnSet(unSet);
			button.setJScript(jScript);
			button.setImage(image);
			button.setHotkey(hotKey);
			overRide.button.add(button);
		}
	}

	/**
	 * Adds a button to the page
	 * 
	 * @param target    the page to go to
	 * @param text      the text displayed on the button
	 * @param set       the flags to set if the button is pressed
	 * @param unSet     the flags to clear if the button is pressed
	 * @param jScript   the Java Script function to run if the button is pressed
	 * @param image     the background image for the button
	 * @param hotKey    the hot key assigned to the button
	 * @param sortOrder the sort order value (used to sort the buttons)
	 * @param disabled  the disabled state of the button (true to disable it)
	 * @param id        the id to use to manipulate the button from Java Script
	 */
	public void addButton(String target, String text, String set, String unSet, String jScript,
			String image, String hotKey, String sortOrder, boolean disabled, String id) {
		int order;
		try {
			order = Integer.parseInt(sortOrder);
		} catch (Exception e) {
			order = 1;
		}

		ButtonThread2 buttonThread = new ButtonThread2();
		buttonThread.overRide = this;
		buttonThread.target = target;
		buttonThread.text = text;
		buttonThread.set = set;
		buttonThread.unSet = unSet;
		buttonThread.jScript = jScript;
		buttonThread.image = image;
		buttonThread.hotKey = hotKey;
		buttonThread.order = order;
		buttonThread.disabled = disabled;
		buttonThread.id = id;
		buttonThread.defaultBtn = false;
		Display.getDefault().syncExec(buttonThread);
	}

	/**
	 * Adds a button to the page
	 * 
	 * @param target     the page to go to
	 * @param text       the text displayed on the button
	 * @param set        the flags to set if the button is pressed
	 * @param unSet      the flags to clear if the button is pressed
	 * @param jScript    the Java Script function to run if the button is pressed
	 * @param image      the background image for the button
	 * @param hotKey     the hot key assigned to the button
	 * @param sortOrder  the sort order value (used to sort the buttons)
	 * @param disabled   the disabled state of the button (true to disable it)
	 * @param id         the id to use to manipulate the button from Java Script
	 * @param defaultBtn default button activated when enter is pressed
	 */
	public void addButton(String target, String text, String set, String unSet, String jScript,
			String image, String hotKey, String sortOrder, boolean disabled, String id,
			boolean defaultBtn) {
		int order;
		try {
			order = Integer.parseInt(sortOrder);
		} catch (Exception e) {
			order = 1;
		}

		ButtonThread2 buttonThread = new ButtonThread2();
		buttonThread.overRide = this;
		buttonThread.target = target;
		buttonThread.text = text;
		buttonThread.set = set;
		buttonThread.unSet = unSet;
		buttonThread.jScript = jScript;
		buttonThread.image = image;
		buttonThread.hotKey = hotKey;
		buttonThread.order = order;
		buttonThread.disabled = disabled;
		buttonThread.id = id;
		buttonThread.defaultBtn = defaultBtn;
		Display.getDefault().syncExec(buttonThread);
	}

	private class ButtonThread2 implements Runnable {
		public OverRide overRide;
		public String target;
		public String text;
		public String set;
		public String unSet;
		public String jScript;
		public String image;
		public String hotKey;
		public int order;
		public boolean disabled;
		public String id;
		public boolean defaultBtn;

		public void run() {
			BasicButton button = new BasicButton();
			button.setTarget(target);
			button.setText(text);
			button.setSet(set);
			button.setUnSet(unSet);
			button.setJScript(jScript);
			button.setImage(image);
			button.setHotkey(hotKey);
			button.setSortOrder(order);
			button.setDisabled(disabled);
			button.setId(id);
			button.setDefaultBtn(defaultBtn);
			overRide.button.add(button);
		}
	}

	/** @exclude */
	public Button getButton(int i) {
		return button.get(i);
	}

	public Button[] getButtons() {
		// TODO Auto-generated method stub
		return button.toArray(new Button[] {});
	}

	/** @exclude */
	public int buttonCount() {
		return button.size();
	}

	/**
	 * Adds a global button to the page
	 *
	 * @param id     the id of the button
	 * @param target the page to go to
	 * @param text   the text to be displayed on the button
	 */
	public void addGlobalButton(String id, String target, String text) {
		addGlobalButton(id, target, text, "", "", "", "", "", "bottom", "1");
	}

	/**
	 * Adds a global button to the page
	 *
	 * @param id      the id of the button
	 * @param target  the page to go to
	 * @param text    the text to be displayed on the button
	 * @param set     the flags to set if the button is pressed
	 * @param unSet   the flags to clear if the button is pressed
	 * @param jScript the Java Script function to run if the button is pressed
	 * @param image   the background image for the button
	 */
	public void addGlobalButton(String id, String target, String text, String set, String unSet,
			String jScript, String image) {
		addGlobalButton(id, target, text, set, unSet, jScript, image, "", "bottom", "1");
	}

	/**
	 * Adds a global button to the page
	 *
	 * @param id      the id of the button
	 * @param target  the page to go to
	 * @param text    the text to be displayed on the button
	 * @param set     the flags to set if the button is pressed
	 * @param unSet   the flags to clear if the button is pressed
	 * @param jScript the Java Script function to run if the button is pressed
	 * @param image   the background image for the button
	 * @param hotKey  the hot key assigned to the button
	 */
	public void addGlobalButton(String id, String target, String text, String set, String unSet,
			String jScript, String image, String hotKey) {
		addGlobalButton(id, target, text, set, unSet, jScript, image, hotKey, "bottom", "1");
	}

	/**
	 * Adds a global button to the page
	 *
	 * @param id        the id of the button
	 * @param target    the page to go to
	 * @param text      the text to be displayed on the button
	 * @param set       the flags to set if the button is pressed
	 * @param unSet     the flags to clear if the button is pressed
	 * @param jScript   the Java Script function to run if the button is pressed
	 * @param image     the background image for the button
	 * @param hotKey    the hot key assigned to the button
	 * @param placement the placement of the global button, top or bottom
	 */
	public void addGlobalButton(String id, String target, String text, String set, String unSet,
			String jScript, String image, String hotKey, String placement) {
		addGlobalButton(id, target, text, set, unSet, jScript, image, hotKey, placement, "1");
	}

	/**
	 * Adds a global button to the page
	 *
	 * @param id        the id of the button
	 * @param target    the page to go to
	 * @param text      the text to be displayed on the button
	 * @param set       the flags to set if the button is pressed
	 * @param unSet     the flags to clear if the button is pressed
	 * @param jScript   the Java Script function to run if the button is pressed
	 * @param image     the background image for the button
	 * @param hotKey    the hot key assigned to the button
	 * @param placement the placement of the global button, top or bottom
	 * @param sortOrder the sort order value (used to sort the buttons)
	 */
	public void addGlobalButton(String id, String target, String text, String set, String unSet,
			String jScript, String image, String hotKey, String placement, String sortOrder) {
		GlobalButtonThread buttonThread = new GlobalButtonThread();
		buttonThread.overRide = this;
		buttonThread.id = id;
		buttonThread.target = target;
		buttonThread.text = text;
		buttonThread.set = set;
		buttonThread.unSet = unSet;
		buttonThread.jScript = jScript;
		buttonThread.image = image;
		buttonThread.hotKey = hotKey;
		buttonThread.sortOrder = sortOrder;
		buttonThread.placement = placement;
		buttonThread.action = GlobalButtonAction.ADD;
		Display.getDefault().syncExec(buttonThread);
	}

	/**
	 * Removes a global button from the page
	 *
	 * @param id the id of the button
	 */
	public void removeGlobalButton(String id) {
		GlobalButtonThread buttonThread = new GlobalButtonThread();
		buttonThread.overRide = this;
		buttonThread.id = id;
		buttonThread.target = "";
		buttonThread.text = "";
		buttonThread.set = "";
		buttonThread.unSet = "";
		buttonThread.jScript = "";
		buttonThread.image = "";
		buttonThread.hotKey = "";
		buttonThread.sortOrder = "";
		buttonThread.placement = "";
		buttonThread.action = GlobalButtonAction.REMOVE;
		Display.getDefault().syncExec(buttonThread);
	}

	private class GlobalButtonThread implements Runnable {
		public OverRide overRide;
		public String id;
		public String target;
		public String text;
		public String set;
		public String unSet;
		public String jScript;
		public String image;
		public String hotKey;
		public String sortOrder;
		public String placement;
		public GlobalButtonAction action;

		public void run() {
			GlobalButtonPlacement placement = this.placement.equals("top")
					? GlobalButtonPlacement.TOP
					: GlobalButtonPlacement.BOTTOM;
			int iSortOrder;
			try {
				iSortOrder = Integer.parseInt(sortOrder);
			} catch (NumberFormatException e) {
				iSortOrder = 1;
			}

			GlobalButton button = new GlobalButton();
			button.setId(id);
			button.setTarget(target);
			button.setText(text);
			button.setSet(set);
			button.setUnSet(unSet);
			button.setJScript(jScript);
			button.setImage(image);
			button.setHotkey(hotKey);
			button.setSortOrder(iSortOrder);
			button.setPlacement(placement);
			button.setAction(action);
			overRide.globalButton.removeIf(btn -> btn.getId().equals(id));
			overRide.globalButton.add(button);
		}
	}

	/** @exclude */
	public GlobalButton getGlobalButton(int i) {
		return globalButton.get(i);
	}

	public GlobalButton[] getGlobalButtons() {
		return globalButton.toArray(new GlobalButton[] {});
	}

	/** @exclude */
	public int globalButtonCount() {
		return globalButton.size();
	}

	/** @exclude */
	public void clearGlobalButtons() {
		globalButton.clear();
	}

	/**
	 * Adds a webcam button to the page
	 * 
	 * @param type    Capture
	 * @param file    file name and location to save the captured image / video
	 * @param target  the page to go to
	 * @param text    the text to display on the button
	 * @param set     the flags to set if the button is pressed
	 * @param unSet   the flags to clear if the button is pressed
	 * @param jScript the Java Script function to run if the button is pressed
	 * @param image   the background image for the button
	 */
	public void addWebcamButton(String type, String file, String target, String text, String set,
			String unSet, String jScript, String image) {
		addWebcamButton(type, file, target, text, set, unSet, jScript, image, "");
	}

	/**
	 * Adds a webcam button to the page
	 * 
	 * @param type    Capture
	 * @param file    file name and location to save the captured image / video
	 * @param target  the page to go to
	 * @param text    the text to be displayed on the button
	 * @param set     the flags to set if the button is pressed
	 * @param unSet   the flags to clear if the button is pressed
	 * @param jScript the Java Script function to run if the button is pressed
	 * @param image   the background image for the button
	 * @param hotKey  the hot key assigned to the button
	 */
	public void addWebcamButton(String type, String file, String target, String text, String set,
			String unSet, String jScript, String image, String hotKey) {
		WebcamButtonThread buttonThread = new WebcamButtonThread();
		buttonThread.overRide = this;
		buttonThread.type = type;
		buttonThread.file = file;
		buttonThread.target = target;
		buttonThread.text = text;
		buttonThread.set = set;
		buttonThread.unSet = unSet;
		buttonThread.jScript = jScript;
		buttonThread.image = image;
		buttonThread.hotKey = hotKey;
		Display.getDefault().syncExec(buttonThread);
	}

	private class WebcamButtonThread implements Runnable {
		public OverRide overRide;
		public String file;
		public String type;
		public String target;
		public String text;
		public String set;
		public String unSet;
		public String jScript;
		public String image;
		public String hotKey;

		public void run() {
			WebcamButton button = new WebcamButton();
			button.setType(type);
			button.setDestination(file);
			button.setTarget(target);
			button.setText(text);
			button.setSet(set);
			button.setUnSet(unSet);
			button.setJScript(jScript);
			button.setImage(image);
			button.setHotkey(hotKey);
			overRide.webcamButton.add(button);
		}
	}

	/**
	 * Adds a webcam button to the page
	 * 
	 * @param type      Capture
	 * @param file      file name and location to save the captured image / video
	 * @param target    the page to go to
	 * @param text      the text displayed on the button
	 * @param set       the flags to set if the button is pressed
	 * @param unSet     the flags to clear if the button is pressed
	 * @param jScript   the Java Script function to run if the button is pressed
	 * @param image     the background image for the button
	 * @param hotKey    the hot key assigned to the button
	 * @param sortOrder the sort order value (used to sort the buttons)
	 * @param disabled  the disabled state of the button (true to disable it)
	 * @param id        the id to use to manipulate the button from Java Script
	 */
	public void addWebcamButton(String type, String file, String target, String text, String set,
			String unSet, String jScript, String image, String hotKey, String sortOrder,
			boolean disabled, String id) {
		int order;
		try {
			order = Integer.parseInt(sortOrder);
		} catch (Exception e) {
			order = 1;
		}

		WebcamButtonThread2 buttonThread = new WebcamButtonThread2();
		buttonThread.overRide = this;
		buttonThread.type = type;
		buttonThread.file = file;
		buttonThread.target = target;
		buttonThread.text = text;
		buttonThread.set = set;
		buttonThread.unSet = unSet;
		buttonThread.jScript = jScript;
		buttonThread.image = image;
		buttonThread.hotKey = hotKey;
		buttonThread.order = order;
		buttonThread.disabled = disabled;
		buttonThread.id = id;
		buttonThread.defaultBtn = false;
		Display.getDefault().syncExec(buttonThread);
	}

	/**
	 * Adds a webcam button to the page
	 * 
	 * @param type       Capture
	 * @param file       file name and location to save the captured image / video
	 * @param target     the page to go to
	 * @param text       the text displayed on the button
	 * @param set        the flags to set if the button is pressed
	 * @param unSet      the flags to clear if the button is pressed
	 * @param jScript    the Java Script function to run if the button is pressed
	 * @param image      the background image for the button
	 * @param hotKey     the hot key assigned to the button
	 * @param sortOrder  the sort order value (used to sort the buttons)
	 * @param disabled   the disabled state of the button (true to disable it)
	 * @param id         the id to use to manipulate the button from Java Script
	 * @param defaultBtn default button activated when enter is pressed
	 */
	public void addWebcamButton(String type, String file, String target, String text, String set,
			String unSet, String jScript, String image, String hotKey, String sortOrder,
			boolean disabled, String id, boolean defaultBtn) {
		int order;
		try {
			order = Integer.parseInt(sortOrder);
		} catch (Exception e) {
			order = 1;
		}

		WebcamButtonThread2 buttonThread = new WebcamButtonThread2();
		buttonThread.overRide = this;
		buttonThread.type = type;
		buttonThread.file = file;
		buttonThread.target = target;
		buttonThread.text = text;
		buttonThread.set = set;
		buttonThread.unSet = unSet;
		buttonThread.jScript = jScript;
		buttonThread.image = image;
		buttonThread.hotKey = hotKey;
		buttonThread.order = order;
		buttonThread.disabled = disabled;
		buttonThread.id = id;
		buttonThread.defaultBtn = defaultBtn;
		Display.getDefault().syncExec(buttonThread);
	}

	private class WebcamButtonThread2 implements Runnable {
		public OverRide overRide;
		public String type;
		public String file;
		public String target;
		public String text;
		public String set;
		public String unSet;
		public String jScript;
		public String image;
		public String hotKey;
		public int order;
		public boolean disabled;
		public String id;
		public boolean defaultBtn;

		public void run() {
			WebcamButton button = new WebcamButton();
			button.setType(type);
			button.setDestination(file);
			button.setTarget(target);
			button.setText(text);
			button.setSet(set);
			button.setUnSet(unSet);
			button.setJScript(jScript);
			button.setImage(image);
			button.setHotkey(hotKey);
			button.setSortOrder(order);
			button.setDisabled(disabled);
			button.setId(id);
			button.setDefaultBtn(defaultBtn);
			overRide.webcamButton.add(button);
		}
	}

	/** @exclude */
	public WebcamButton getWebcamButton(int i) {
		return webcamButton.get(i);
	}

	public WebcamButton[] getWebcamButtons() {
		return webcamButton.toArray(new WebcamButton[] {});
	}

	/** @exclude */
	public int webcamButtonCount() {
		return webcamButton.size();
	}

	/**
	 * Adds a timer to change various aspects of the screen / run a javascript
	 * function
	 * 
	 * @param delay   the time in seconds before the timer triggers
	 * @param jScript the Java Script function to run when the timer triggers
	 * @param imageId the image to change to when the timer triggers
	 * @param text    the html to set the right html pane to when the timer triggers
	 * @param set     the flags to set when the timer triggers
	 * @param unSet   the flags to clear when the timer triggers
	 */
	public void addTimer(String delay, String jScript, String imageId, String text, String set,
			String unSet, String id) {
		Timer timer = new Timer();
		timer.setDelay(delay);
		timer.setImageId(imageId);
		timer.setText(text);
		timer.setSet(unSet);
		timer.setUnSet(unSet);
		timer.setId(id);
		this.timer.add(timer);
	}

	/** @exclude */
	public Timer getTimer(int i) {
		return timer.get(i);
	}

	public Timer[] getTimers() {
		return timer.toArray(new Timer[] {});
	}

	/** @exclude */
	public int timerCount() {
		return timer.size();
	}

	/** @exclude */
	public void clear() {
		button = new ArrayList<Button>();
		timer = new ArrayList<Timer>();
		delay = null;
		video = null;
		audio = null;
		audio2 = null;
		metronome = null;
		image = "";
		html = "";
		page = "";
		leftHtml = "";
		leftBody = "";
		rightCss = "";
		rightHtml = "";
	}

	/** @exclude */
	public Delay getDelay() {
		return delay;
	}

	/**
	 * Add a delay / count down timer
	 * 
	 * @param target    the page to go to if the delay counts down to 0
	 * @param delay     the number of seconds for the delay Can be a range specify
	 *                  (n..n2) e.g. (5..10) for a random delay between 5 and 10
	 *                  seconds
	 * @param startWith Don't show the true value but start with a higher one e.g.
	 *                  setting this to 50 with a 5 second delay would show a count
	 *                  starting at 50 but would reach 0 while displaying 45 seconds
	 *                  to go
	 * @param style     Display style N Normal display the count down timer S Secret
	 *                  display ??:?? so user knows there is a timer H Hidden don't
	 *                  display anything
	 * @param set       the flags to set if the delay counts down to 0
	 * @param unSet     the flags to clear if delay counts down to 0
	 * @param jScript   the Java Script function to run if delay counts down to 0
	 */
	public void setDelay(String target, String delay, int startWith, String style, String set,
			String unSet, String jScript) {
		this.delay = new Delay();
		this.delay.setTarget(target);
		this.delay.setSeconds(delay);
		this.delay.setStartWith(startWith);
		this.delay.setStyle(style);
		this.delay.setSet(unSet);
		this.delay.setUnSet(unSet);
		this.delay.setJscript(jScript);
	}

	/** @exclude */
	public Video getVideo() {
		return video;
	}

	/**
	 * Play a video
	 *
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select a video in the sub directory kate with a
	 * file name starting with home
	 *
	 * @param id the file name for the video
	 */
	public void setVideo(String id) {
		this.video = new Video();
		this.video.setId(id);
	}

	/**
	 * Play a video
	 * 
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select a video in the sub directory kate with a
	 * file name starting with home
	 * 
	 * startAt : to start 90 seconds in use 00:01:30 stopAt : to stop at 95 seconds
	 * into the video 00:01:35
	 * 
	 * @param id      the file name for the video
	 * @param startAt the Start time for the video hh:mm:ss
	 * @param stopAt  the Stop time for video hh:mm:ss
	 * @param target  the page to go to when the video stops
	 * @param set     the flags to set when the video ends
	 * @param unSet   the flags to clear when the video ends
	 * @param repeat  the number of times to repeat the video
	 * @param jscript the Java Script function to run when the video stops
	 */
	public void setVideo(String id, String startAt, String stopAt, String target, String set,
			String unSet, int repeat, String jscript) {
		video = new Video();
		video.setId(id);
		video.setStartAt(startAt);
		video.setStopAt(stopAt);
		video.setTarget(target);
		video.setSet(set);
		video.setUnSet(unSet);
		video.setRepeat(Integer.toString(repeat));
		video.setJscript(jscript);
	}

	/**
	 * Play a video
	 * 
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select a video in the sub directory kate with a
	 * file name starting with home
	 * 
	 * startAt : to start 90 seconds in use 00:01:30 stopAt : to stop at 95 seconds
	 * into the video 00:01:35
	 * 
	 * @param id      the file name for the video
	 * @param startAt the Start time for the video hh:mm:ss
	 * @param stopAt  the Stop time for video hh:mm:ss
	 * @param target  the page to go to when the video stops
	 * @param set     the flags to set when the video ends
	 * @param unSet   the flags to clear when the video ends
	 * @param repeat  the number of times to repeat the video
	 * @param jscript the Java Script function to run when the video stops
	 * @param volume  number between 0 and 100 to set the volume of the audio
	 */
	public void setVideo(String id, String startAt, String stopAt, String target, String set,
			String unSet, int repeat, String jscript, int volume) {
		video = new Video();
		video.setId(id);
		video.setStartAt(startAt);
		video.setStopAt(stopAt);
		video.setTarget(target);
		video.setSet(unSet);
		video.setUnSet(unSet);
		video.setRepeat(Integer.toString(repeat));
		video.setJscript(jscript);
		video.setVolume(volume);
	}

	/** @exclude */
	public Webcam getWebcam() {
		return webcam;
	}

	/**
	 * Display Webcam output
	 * 
	 */
	public void setWebcam() {
		this.webcam = new Webcam();
	}

	/** @exclude */
	public Audio getAudio() {
		return audio;
	}

	/**
	 * Play an audio file
	 *
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select an audio file in the sub directory kate
	 * with a file name starting with home
	 *
	 * @param id the file name for the audio
	 */
	public void setAudio(String id) {
		audio = new Audio1();
		audio.setId(id);
	}

	/**
	 * Play an audio file
	 * 
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select an audio file in the sub directory kate
	 * with a file name starting with home
	 * 
	 * startAt : to start 90 seconds in 00:01:30 stopAt : to stop at 95 seconds into
	 * the video 00:01:35
	 * 
	 * 
	 * @param id      the file name for the audio
	 * @param startAt the start time for the audio hh:mm:ss
	 * @param stopAt  the stop time for audio hh:mm:ss
	 * @param target  the page to go to when the audio stops
	 * @param set     the flags to set when the audio ends
	 * @param unSet   the flags to clear when the audio ends
	 * @param repeat  the number of times to repeat the audio
	 * @param jscript the Java Script function to run when the audio stops
	 */
	public void setAudio(String id, String startAt, String stopAt, String target, String set,
			String unSet, String repeat, String jscript) {
		audio = new Audio1();
		audio.setId(id);
		audio.setStartAt(startAt);
		audio.setStopAt(stopAt);
		audio.setTarget(target);
		audio.setSet(unSet);
		audio.setUnSet(unSet);
		audio.setRepeat(repeat);
		audio.setJscript(jscript);
	}

	/**
	 * Play an audio file
	 * 
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select an audio file in the sub directory kate
	 * with a file name starting with home
	 * 
	 * startAt : to start 90 seconds in 00:01:30 stopAt : to stop at 95 seconds into
	 * the video 00:01:35
	 * 
	 * 
	 * @param id      the file name for the audio
	 * @param startAt the start time for the audio hh:mm:ss
	 * @param stopAt  the stop time for audio hh:mm:ss
	 * @param target  the page to go to when the audio stops
	 * @param set     the flags to set when the audio ends
	 * @param unSet   the flags to clear when the audio ends
	 * @param repeat  the number of times to repeat the audio
	 * @param jscript the Java Script function to run when the audio stops
	 * @param volume  number between 0 and 100 to set the volume of the audio
	 */
	public void setAudio(String id, String startAt, String stopAt, String target, String set,
			String unSet, String repeat, String jscript, int volume) {
		audio = new Audio1();
		audio.setId(id);
		audio.setStartAt(startAt);
		audio.setStopAt(stopAt);
		audio.setTarget(target);
		audio.setSet(unSet);
		audio.setUnSet(unSet);
		audio.setRepeat(repeat);
		audio.setJscript(jscript);
		audio.setVolume(volume);
	}

	/** @exclude */
	public Audio getAudio2() {
		return audio2;
	}

	/**
	 * Play an audio file on Audio2
	 *
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select an audio file in the sub directory kate
	 * with a file name starting with home
	 *
	 * @param id the file name for the audio
	 */
	public void setAudio2(String id) {
		audio2 = new Audio2();
		audio2.setId(id);
	}

	/**
	 * Play an audio file on Audio2
	 *
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select an audio file in the sub directory kate
	 * with a file name starting with home
	 *
	 * startAt : to start 90 seconds in 00:01:30 stopAt : to stop at 95 seconds into
	 * the video 00:01:35
	 *
	 *
	 * @param id      the file name for the audio
	 * @param startAt the start time for the audio hh:mm:ss
	 * @param stopAt  the stop time for audio hh:mm:ss
	 * @param target  the page to go to when the audio stops
	 * @param set     the flags to set when the audio ends
	 * @param unSet   the flags to clear when the audio ends
	 * @param repeat  the number of times to repeat the audio
	 * @param jscript the Java Script function to run when the audio stops
	 */
	public void setAudio2(String id, String startAt, String stopAt, String target, String set,
			String unSet, String repeat, String jscript) {
		audio2 = new Audio2();
		audio2.setId(id);
		audio2.setStartAt(startAt);
		audio2.setStopAt(stopAt);
		audio2.setTarget(target);
		audio2.setSet(set);
		audio2.setUnSet(unSet);
		audio2.setRepeat(repeat);
		audio2.setJscript(jscript);
	}

	/**
	 * Play an audio file on Audio2
	 *
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select an audio file in the sub directory kate
	 * with a file name starting with home
	 *
	 * startAt : to start 90 seconds in 00:01:30 stopAt : to stop at 95 seconds into
	 * the video 00:01:35
	 *
	 *
	 * @param id      the file name for the audio
	 * @param startAt the start time for the audio hh:mm:ss
	 * @param stopAt  the stop time for audio hh:mm:ss
	 * @param target  the page to go to when the audio stops
	 * @param set     the flags to set when the audio ends
	 * @param unSet   the flags to clear when the audio ends
	 * @param repeat  the number of times to repeat the audio
	 * @param jscript the Java Script function to run when the audio stops
	 * @param volume  number between 0 and 100 to set the volume of the audio
	 */
	public void setAudio2(String id, String startAt, String stopAt, String target, String set,
			String unSet, String repeat, String jscript, int volume) {
		audio2 = new Audio2();
		audio2.setId(id);
		audio2.setStartAt(startAt);
		audio2.setTarget(target);
		audio2.setSet(set);
		audio2.setUnSet(unSet);
		audio2.setRepeat(repeat);
		audio2.setJscript(jscript);
		audio2.setVolume(volume);
	}

	/** @exclude */
	public Metronome getMetronome() {
		return metronome;
	}

	/**
	 * Play a metronome beat
	 *
	 * defaults for a simple beats per minute bpm : the number of beats per minute
	 * resolution : 4 loops : 0 rhythm : ""
	 *
	 * For complex beat patterns you need to set bpm, resolution and rhythm If
	 * bpm=60 and beats=4, we have 1 bar per second with 4 beats per bar, so a beat
	 * every 0.25 seconds If rhythm="1,5,9,13" we get 4 clicks once per second If
	 * rhythm="1,3,5,7,9,11,13" we get 7 clicks once every half second
	 * 
	 * bpm : can also be a range (30..60) will give a random BPM between 30 and 60
	 * 
	 * @param bpm        Beats per minute 60 will give one beat per second
	 * @param resolution Beats per bar (for a "normal" bpm this should be set to 4)
	 * @param loops      Number of times to loop the rhythm
	 * @param rhythm     list of numbers to set the beat pattern
	 * 
	 */
	public void setMetronome(String bpm, int resolution, int loops, String rhythm) {
		metronome = new Metronome();
		metronome.setBpm(bpm);
		metronome.setResolution(resolution);
		metronome.setLoops(loops);
		metronome.setRhythm(rhythm);
	}

	/**
	 * Play a metronome beat
	 * 
	 * bpm : Beats per minute, 60 will give one beat per second can also be a range
	 * (30..60) will give a random BPM between 30 and 60
	 * 
	 * @param bpm the number of beats
	 */
	public void setMetronome(String bpm) {
		metronome = new Metronome();
		metronome.setBpm(bpm);
	}

	/** @exclude */
	public String getHtml() {
		return html;
	}

	/**
	 * Sets the html for the right hand pane
	 * 
	 * @param html the Html to put in the right pane
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/** @exclude */
	public String getImage() {
		return image;
	}

	/**
	 * Sets the image to be displayed in the left pane
	 * 
	 * File must be in the media directory (or subdirectory) Wild cards can be used
	 * e.g. kate/home*.* would select an image in the sub directory kate with a file
	 * name starting with home
	 * 
	 * @param id the file name for the image
	 */
	public void setImage(String id) {
		this.image = id;
	}

	/** @exclude */
	public String getPage() {
		return page;
	}

	/**
	 * Over rides the page to go to
	 * 
	 * Setting this will force a jump to the page
	 * 
	 * @param page the page to go to
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/** @exclude */
	public String getLeftHtml() {
		return leftHtml;
	}

	/**
	 * Will set the left pane to the html provided
	 * 
	 * Instead of displaying an image the left pane it will display the html
	 * 
	 * @param leftHtml the Html to be displayed in the left pane
	 */
	public void setLeftHtml(String leftHtml) {
		this.leftHtml = leftHtml;
	}

	/** @exclude */
	public String getRightCss() {
		return rightCss;
	}

	/**
	 * Over rides the CSS for the right pane
	 * 
	 * @param rightCss the CSS to use instead of the default
	 */
	public void setRightCss(String rightCss) {
		this.rightCss = rightCss;
	}

	/** @exclude */
	public String getLeftBody() {
		return leftBody;
	}

	/**
	 * Sets the content of the body node for the left pane
	 * 
	 * Rather than over ride the whole html this can be used to set just the body
	 * This will function in exactly the same way as setting the right html
	 * 
	 * @param leftBody html to replace the contents of the body node
	 * @param leftCSS  the CSS to use instead of the default
	 */
	public void setLeftBody(String leftBody, String leftCSS) {
		this.leftBody = leftBody;
		this.leftCss = leftCSS;
	}

	/** @exclude */
	public String getLeftCss() {
		return leftCss;
	}

	/** @exclude */
	public String getRightHtml() {
		return rightHtml;
	}

	/**
	 * Will set the right pane to the html provided
	 * 
	 * will replace the complete html in the right pane
	 * 
	 * @param rightHtml the Html to be displayed in the right pane
	 */
	public void setRightHtml(String rightHtml) {
		this.rightHtml = rightHtml;
	}

}
