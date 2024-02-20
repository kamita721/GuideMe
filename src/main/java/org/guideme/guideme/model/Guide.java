package org.guideme.guideme.model;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.ui.main_shell.MainShell;

public class Guide {
	/** @exclude */
	private String title;
	/** @exclude */
	private String authorName;
	/** @exclude */
	private List<String> keywords = new ArrayList<>();
	/** @exclude */
	private String description;
	/** @exclude */
	private String originalUrl;
	/** @exclude */
	private String authorUrl;
	/** @exclude */
	private Image thumbnail;
	/** @exclude */
	private Map<String, Chapter> chapters = new HashMap<>();

	/** @exclude */
	private String mediaDirectory; // Media subdirectory for current xml file
	/** @exclude */
	private String delStyle; // style for currently running delay
	/** @exclude */
	private String delTarget; // target for currently running delay
	/** @exclude */
	private List<String> flags = new ArrayList<>(); // current flags
	/** @exclude */
	private boolean autoSetPage;
	/** @exclude */
	private String delaySet; // flags to set for currently running delay
	/** @exclude */
	private String delayUnSet; // flags to clear for currently running delay
	/** @exclude */
	private int delStartAtOffSet; // offset for currently running delay
	/** @exclude */
	private String delayScriptVar;
	/** @exclude */
	private String id; // name for current xml that is running
	/** @exclude */
	private GuideSettings settings = new GuideSettings("startup"); // state for the currently running xml
	/** @exclude */
	private String jScript;
	/** @exclude */
	private String delayjScript;
	/** @exclude */
	private String globaljScript;
	/** @exclude */
	private static Guide guide;
	/** @exclude */
	private String css; // css style sheet
	/** @exclude */
	private boolean inPrefGuide;
	/** @exclude */
	private HashMap<String, GlobalButton> globalButtons = new HashMap<>();
	/** @exclude */
	private static Logger logger = LogManager.getLogger();
	/** @exclude */
	private static ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	/** @exclude */
	private MainShell mainshell;

	/** @exclude */
	private Guide() {

	}

	/** @exclude */
	public static synchronized Guide getGuide() {
		if (guide == null) {
			AppSettings appSettings = AppSettings.getAppSettings();
			guide = new Guide();
			Map<String, Chapter> chapters = guide.getChapters();
			Chapter chapter = new Chapter("default");
			chapters.put("default", chapter);
			Page page404 = new Page("GuideMe404Error", "", "", "", "", false, "", "");
			chapter.getPages().put(page404.getId(), page404);
			Page start = new Page("start", "", "", "", "", false, "", "");

			String appDir = appSettings.getUserDir().replace("\\", "\\\\");
			String fileName = "Welcome_" + appSettings.getLanguage() + "_" + appSettings.getCountry() + ".txt";
			File f = new File(appDir + appSettings.getFileSeparator() + fileName);
			if (!f.exists()) {
				fileName = "Welcome_" + appSettings.getLanguage() + ".txt";
				f = new File(appDir + appSettings.getFileSeparator() + fileName);
				if (!f.exists()) {
					fileName = "Welcome.txt";
				}
			}
			String strHtml2 = comonFunctions.readFile(fileName, StandardCharsets.UTF_8);
			strHtml2 = strHtml2.replace("appDir", appDir);
			strHtml2 = strHtml2.replace("\n", " ").replace("\r", "");

			String strLoadScript = "function pageLoad() {";
			strLoadScript = strLoadScript + "\toverRide.setLeftHtml(\"" + strHtml2 + "\");";
			strLoadScript = strLoadScript + "}";
			start.setjScript(strLoadScript);
			guide.globaljScript = "";
			guide.inPrefGuide = false;
			guide.css = "";
			guide.autoSetPage = false;
			guide.title = "";
			chapter.getPages().put(start.getId(), start);
			guide.setMediaDirectory(appSettings.getUserDir() + appSettings.getFileSeparator() + "userSettings"
					+ appSettings.getFileSeparator());
		}
		return guide;
	}

	/** @exclude */
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/** @exclude */
	public void setMainshell(MainShell mainshell) {
		this.mainshell = mainshell;
	}

	/**
	 * Gets the Title of the Guide
	 * 
	 * @return the Guide Title
	 */
	public String getTitle() {
		return title;
	}

	/** @exclude */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the Author of the Guide
	 * 
	 * @return the Author's Name
	 */
	public String getAuthorName() {
		return authorName;
	}

	/** @exclude */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/** @exclude */
	public Collection<String> getKeywords() {
		return keywords;
	}

	/** @exclude */
	public void setKeywords(Collection<String> keywords) {
		this.keywords.clear();
		this.keywords.addAll(keywords);
	}

	/** @exclude */
	public void setKeywords(String... keywords) {
		setKeywords(Arrays.asList(keywords));
	}

	/** @exclude */
	public void setKeywordsString(String keywords) {
		this.keywords.clear();
		String[] tmp = keywords.split(",");
		for (int i = 0; i < tmp.length; i++) {
			this.keywords.add(tmp[i].trim());
		}
	}

	/** @exclude */
	public String getKeywordsString() {
		String tmp = this.keywords.toString();
		return tmp.substring(1, tmp.length() - 1);
	}

	/** @exclude */
	public String getDescription() {
		return description;
	}

	/** @exclude */
	public void setDescription(String description) {
		this.description = description;
	}

	/** @exclude */
	public String getOriginalUrl() {
		return originalUrl;
	}

	/** @exclude */
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	/** @exclude */
	public String getAuthorUrl() {
		return authorUrl;
	}

	/** @exclude */
	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

	/** @exclude */
	public Image getThumbnail() {
		return thumbnail;
	}

	/** @exclude */
	public void setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
	}

	/** @exclude */
	public Map<String, Chapter> getChapters() {
		return chapters;
	}

	/** @exclude */
	public void setChapters(Map<String, Chapter> chapters) {
		this.chapters = chapters;
	}

	/**
	 * Gets the media directory of the guide
	 * 
	 * @return the Media Directory
	 */
	public String getMediaDirectory() {
		return mediaDirectory;
	}

	/** @exclude */
	public void setMediaDirectory(String mediaDirectory) {
		this.mediaDirectory = mediaDirectory;
	}

	/** @exclude */
	public String getDelStyle() {
		return delStyle;
	}

	/** @exclude */
	public void setDelStyle(String delStyle) {
		this.delStyle = delStyle;
	}

	/** @exclude */
	public String getDelTarget() {
		return delTarget;
	}

	/** @exclude */
	public void setDelTarget(String delTarget) {
		this.delTarget = delTarget;
	}

	/** @exclude */
	public List<String> getFlags() {
		return flags;
	}

	/** @exclude */
	public void setFlags(List<String> flags) {
		this.flags = flags;
	}

	/** @exclude */
	public boolean getAutoSetPage() {
		return autoSetPage;
	}

	/** @exclude */
	public void setAutoSetPage(Boolean autoSetPage) {
		this.autoSetPage = autoSetPage;
	}

	/** @exclude */
	public String getDelaySet() {
		return delaySet;
	}

	/** @exclude */
	public void setDelaySet(String delaySet) {
		this.delaySet = delaySet;
	}

	/** @exclude */
	public String getDelayUnSet() {
		return delayUnSet;
	}

	/** @exclude */
	public void setDelayUnSet(String delayUnSet) {
		this.delayUnSet = delayUnSet;
	}

	/** @exclude */
	public int getDelStartAtOffSet() {
		return delStartAtOffSet;
	}

	/** @exclude */
	public void setDelStartAtOffSet(int delStartAtOffSet) {
		this.delStartAtOffSet = delStartAtOffSet;
	}

	/** @exclude */
	public String getId() {
		return id;
	}

	// we are loading a new xml so clear old settings
	/** @exclude */
	public void reset(String id) {
		logger.trace("Guide reset id: {}", id);
		try {
			this.id = id;
			settings = new GuideSettings(id);
			mediaDirectory = "";
			delStyle = "";
			delTarget = "";
			flags = new ArrayList<>();
			autoSetPage = true;
			delaySet = "";
			delayUnSet = "";
			title = "";
			chapters = new HashMap<>();
			delStartAtOffSet = 0;
			jScript = "";
			css = "";
			inPrefGuide = false;
			globaljScript = "";
			globalButtons = new HashMap<>();
		} catch (Exception e) {
			logger.error("Guide reset " + e.getLocalizedMessage(), e);
		}
	}

	/** @exclude */
	public GuideSettings getSettings() {
		return settings;
	}

	/** @exclude */
	public String getjScript() {
		return jScript;
	}

	/** @exclude */
	public void setjScript(String jScript) {
		this.jScript = jScript;
	}

	/** @exclude */
	public void setSettings(GuideSettings settings) {
		this.settings = settings;
	}

	/** @exclude */
	public String getDelayjScript() {
		return delayjScript;
	}

	/** @exclude */
	public void setDelayjScript(String delayjScript) {
		this.delayjScript = delayjScript;
	}

	/**
	 * Gets the custom CSS for the Guide
	 * 
	 * @return the Guide CSS
	 */
	public String getCss() {
		return css;
	}

	/** @exclude */
	public void setCss(String css) {
		String mediaPath;
		AppSettings appSettings = AppSettings.getAppSettings();
		mediaPath = comonFunctions.getMediaFullPath("", appSettings.getFileSeparator(), appSettings, guide);
		mediaPath = mediaPath.replace("\\", "/");
		css = css.replace("\\MediaDir\\", mediaPath);
		this.css = css;
	}

	/** @exclude */
	public boolean getInPrefGuide() {
		return inPrefGuide;
	}

	/** @exclude */
	public void setInPrefGuide(Boolean inPrefGuide) {
		this.inPrefGuide = inPrefGuide;
	}

	/**
	 * Gets the Global Java Script for the Guide
	 * 
	 * @return the Guide global Java Script
	 */
	public String getGlobaljScript() {
		return globaljScript;
	}

	/** @exclude */
	public void appendGlobaljScript(String globaljScript) {
		this.globaljScript = this.globaljScript + "\n" + globaljScript;
	}

	public void clearGlobaljScript() {
		this.globaljScript = "";
	}

	// Guide Setting Wrapper FOR JSCRIPT

	/**
	 * Gets a text guide preference
	 * 
	 * @param key the name of the preference
	 * @return the value currently stored
	 */
	public String getPref(String key) {
		return settings.getPref(key);
	}

	/**
	 * Sets an existing text guide preference
	 * 
	 * @param key   the name of the preference
	 * @param value the value to store in that preference
	 */
	public void setPref(String key, String value) {
		settings.setPref(key, value);
	}

	/**
	 * Adds a new text guide preference
	 * 
	 * @param key        the name of the preference
	 * @param value      the default value
	 * @param screenDesc the text displayed on the screen in guide preferences
	 */
	public void addPref(String key, String value, String screenDesc, int sortOrder) {
		settings.addPref(key, value, screenDesc, sortOrder);
	}

	/**
	 * Gets a true / false guide preference
	 * 
	 * @param key the name of the preference
	 * @return the value currently stored
	 */
	public boolean isPref(String key) {
		return settings.isPref(key);
	}

	/**
	 * Sets an existing true / false guide preference
	 * 
	 * @param key the name of the preference
	 * @return the new value to store
	 */
	public void setPref(String key, boolean value) {
		settings.setPref(key, value);
	}

	/**
	 * Adds a new true / false guide preference
	 * 
	 * @param key        the name of the preference
	 * @param value      the default value
	 * @param screenDesc the text displayed on the screen in guide preferences
	 */
	public void addPref(String key, boolean value, String screenDesc, int sortOrder) {
		settings.addPref(key, value, screenDesc, sortOrder);
	}

	/**
	 * Gets a numeric guide preference
	 * 
	 * @param key the name of the preference
	 * @return the value currently stored
	 */
	public Double getPrefNumber(String key) {
		return settings.getPrefNumber(key);
	}

	/**
	 * Sets an existing numeric guide preference
	 * 
	 * @param key the name of the preference
	 * @return the new value to store
	 */
	public void setPref(String key, Double value) {
		settings.setPref(key, value);
	}

	/**
	 * Adds a new numeric guide preference
	 * 
	 * @param key        the name of the preference
	 * @param value      the default value
	 * @param screenDesc the text displayed on the screen in guide preferences
	 */
	public void addPref(String key, Double value, String screenDesc, int sortOrder) {
		settings.addPref(key, value, screenDesc, sortOrder);
	}

	/**
	 * Gets the value of an html form field from either the left or right pane
	 * 
	 * @param key the value in the name attribute on the html input field in the
	 *            form
	 * @return the value the user entered in the field
	 */
	public String getFormField(String key) {
		return settings.getFormField(key);
	}

	/**
	 * Gets the current page name
	 * 
	 * @return the name of the current page
	 */
	public String getCurrPage() {
		return settings.getCurrPage();
	}

	/**
	 * Gets the previous page name
	 * 
	 * @return the name of the page you just came from
	 */
	public String getPrevPage() {
		return settings.getPrevPage();
	}

	// comonFunctions wrapper for javascript
	/**
	 * Checks the current flags against a list of flags passed in
	 * 
	 * @param ifSet    Flags in this list must have been set in the tease
	 * @param ifNotSet Flags in this list must not be set
	 * @return true if the set and unset flags match the current flags
	 */
	public boolean canShow(String ifSet, String ifNotSet) {
		return comonFunctions.canShow(flags, ifSet, ifNotSet);
	}

	/**
	 * Sets a list of flags
	 * 
	 * @param flagNames comma separated list of flags to set
	 */
	public void setFlags(String flagNames) {
		comonFunctions.setFlags(flagNames, flags);
	}

	/**
	 * Clears a list of flags
	 * 
	 * @param flagNames comma separated list of flags to clear
	 */
	public void unsetFlags(String flagNames) {
		comonFunctions.unsetFlags(flagNames, flags);
	}

	/**
	 * Checks if a list of flags is set
	 * 
	 * @param flagNames List of flags to check
	 * @return true if all flags are set
	 */
	public boolean isSet(String flagNames) {
		return comonFunctions.isSet(flagNames, flags);
	}

	/**
	 * Checks if a list of flags is not set
	 * 
	 * @param flagNames List of flags to check
	 * @return true if none of the flags is set
	 */
	public boolean isNotSet(String flagNames) {
		return comonFunctions.isNotSet(flagNames, flags);
	}

	/**
	 * Amount of time between 2 dates
	 * 
	 * @param type    d,h,m or s to return days, hours, minutes or seconds
	 * @param jsdate1 (JavaScript date object) Date one (normally the earlier date)
	 * @param jsdate2 (JavaScript date object) Date two
	 * @return a whole number of the type chosen, between the two dates
	 */
	public long dateDifference(String type, Object jsdate1, Object jsdate2) {
		return comonFunctions.dateDifference(type, jsdate1, jsdate2);
	}

	/**
	 * Gets a random number
	 * 
	 * @param random (nn..nn) so for a number between 5 and 20 (5..20)
	 * @return random a whole number between the two values (inclusive of the two
	 *         values)
	 */
	public int getRandom(String random) {
		return comonFunctions.getRandom(random);
	}

	/**
	 * Gets a random number
	 * 
	 * @param Min minimum value to be returned
	 * @param Max maximum value to be returned
	 * @return random a whole number between the two values (inclusive of the two
	 *         values)
	 */
	public int getRandom(int intMin, int intMax) {
		return comonFunctions.getRandom(intMin, intMax);
	}

	/**
	 * Gets a random number
	 * 
	 * @param Max maximum value to be returned
	 * @return random a whole number between one and the value passed in inclusive
	 *         of the value passsed in
	 */
	public int getRandom(int intMax) {
		return comonFunctions.getRandom(intMax);
	}

	/**
	 * Gets the number of milliseconds after midnight for a time.
	 * 
	 * @param iTime 00:00:00 (09:30:00 would give the number of milliseconds to
	 *              9:30am)
	 * @return a whole number of milliseconds
	 */
	public int getMilisecFromTime(String iTime) {
		return comonFunctions.getMilisecFromTime(iTime);
	}

	/**
	 * Gets the version of GuideMe the user is running
	 * 
	 * @return the version in the format n.n.n
	 */
	public String getVersion() {
		return ComonFunctions.getVersion();
	}

	/**
	 * Gets a comma separated list of files for a folder (directory)
	 * 
	 * @param folderName name of the folder to find files in
	 * @return a list of the files
	 */
	public String listFiles(String folderName) {
		return comonFunctions.listFiles(folderName);
	}

	/**
	 * Gets a comma separated list of sub folders for a folder
	 * 
	 * @param folderName name of the folder to find sub folders in
	 * @return a list of the folders
	 */
	public String listSubFolders(String folderName) {
		return comonFunctions.listSubFolders(folderName, false);
	}

	/**
	 * Gets a random file
	 * 
	 * @param wildcard  a file pattern (a*.jpg will return a file starting with a
	 *                  and ending in .jpg)
	 * @param strSubDir sub folder to use if any
	 * @return the path and name of the file
	 */
	public String getRandomFile(String wildcard, String strSubDir) {
		return comonFunctions.getRandomFile(wildcard, strSubDir);
	}

	/**
	 * Checks to see if a file exists
	 * 
	 * @param fileName name of file to check for
	 * @return true if the file exists
	 */
	public boolean fileExists(String fileName) {
		return comonFunctions.fileExists(fileName);
	}

	/**
	 * Checks if a folder exists
	 * 
	 * @param fileName folder to check for
	 * @return true if the folder exists
	 */
	public boolean directoryExists(String fileName) {
		return comonFunctions.directoryExists(fileName);
	}

	/* main shell functions to update screen directly from javascript */

	/**
	 * Sets the text where the clock is displayed
	 * 
	 * @param lblLeft text to over write the clock with
	 */
	public void setClockText(String lblLeft) {
		Display.getDefault().asyncExec(() -> mainshell.setLblLeft(lblLeft));
	}

	/**
	 * Sets the text where the title / author are displayed
	 * 
	 * @param lblCentre text to over write the title
	 */
	public void setTitleText(String lblCentre) {
		Display.getDefault().asyncExec(() -> mainshell.setLblCentre(lblCentre));
	}

	/**
	 * Sets the text where the timer is usually displayed
	 * 
	 * @param lblRight text to over write the timer with
	 */
	public void setTimerText(String lblRight) {
		Display.getDefault().asyncExec(() -> mainshell.setLblRight(lblRight));
	}

	/**
	 * Replaces the complete html in the left (Image) pane
	 * 
	 * @param leftHtml html to over write the current html
	 */
	public void setLeftHtml(String leftHtml) {
		Display.getDefault().asyncExec(() -> mainshell.setLeftHtml(leftHtml));
	}

	/**
	 * Clears the image in the left pane
	 * 
	 */
	public void clearImage() {
		Display.getDefault().asyncExec(() -> mainshell.clearImage());
	}

	/**
	 * Replaces the complete html in the right (text) pane
	 * 
	 * @param brwsText      html to over write the current html
	 * @param overRideStyle CSS to style the html (will use the default if this is
	 *                      blank)
	 */
	public void setRightHtml(String rightHtml) {
		Display.getDefault().asyncExec(() -> mainshell.setRightHtml(rightHtml));
	}

	/**
	 * Replaces the body section within the html on the right (text) pane
	 * 
	 * @param brwsText      html fragment to over write the current body contents
	 * @param overRideStyle CSS to style the html (will use the default if this is
	 *                      blank)
	 */
	public void setRightBody(String brwsText, String overRideStyle) {
		Display.getDefault().asyncExec(() -> mainshell.setBrwsText(brwsText, overRideStyle));
	}

	/**
	 * Writes a string to a file
	 * 
	 * @param path     the path to the file
	 * @param contents a string containing the contents of the file
	 */
	public void jsWriteFile(String path, String contents) {
		comonFunctions.jsWriteFile(path, contents);
	}

	/**
	 * Reads the contents of a file into an array of strings each line will be read
	 * into a new element into the array
	 * 
	 * @param fileName the path to the file
	 * @return a string array containing the lines from the file
	 */
	public String[] jsReadFileArray(String fileName) {
		return comonFunctions.jsReadFileArray(fileName);
	}

	/**
	 * Reads the contents of a file into an array of strings each line will be read
	 * into a new element into the array
	 * 
	 * @param fileName the path to the file
	 * @param contents a string array containing the lines for the file
	 */
	public void jsWriteFileArray(String path, String[] contents) {
		comonFunctions.jsWriteFileArray(path, contents);
	}

	/** @exclude */
	public String[] jsReadFileArray(String fileName, String encoding) {
		return comonFunctions.jsReadFileArray(fileName, encoding);
	}

	/**
	 * Enables a button on the screen
	 * 
	 * @param id the id set when the button was defined
	 */
	public void enableButton(String id) {
		Display.getDefault().asyncExec(() -> mainshell.enableButton(id));
	}

	/**
	 * Disables a button on the screen
	 * 
	 * @param id the id set when the button was defined
	 */
	public void disableButton(String id) {
		Display.getDefault().asyncExec(() -> mainshell.disableButton(id));
	}

	/**
	 * Adds a timer to change various aspects of the screen / run a javascript
	 * function
	 * 
	 * @param delay   a time in seconds before the timer triggers
	 * @param jScript a javascript function to run when the timer triggers
	 * @param imageId the filename of the image to change to when the timer triggers
	 * @param text    the html text to set the right html pane to when the timer
	 *                triggers
	 * @param set     the flags to set when the timer triggers
	 * @param unSet   the flags to clear when the timer triggers
	 * @param id      the identifier (name) of the new timer to manipulate the timer
	 *                later
	 */
	public void addTimer(String delay, String jScript, String imageId, String text, String set, String unSet,
			String id) {
		Timer timer = new Timer(delay, jScript, imageId, text, "", "", set, unSet, "", "", id);
		Calendar timCountDown = Calendar.getInstance();
		timCountDown.add(Calendar.SECOND, timer.getTimerSec());
		timer.setTimerEnd(timCountDown);
		mainshell.addTimer(timer);
	}

	/**
	 * Adds a timer to change various aspects of the screen / run a javascript
	 * function
	 * 
	 * @param delay   a time in seconds before the timer triggers
	 * @param jScript a javascript function to run when the timer triggers
	 * @param set     the flags to set when the timer triggers
	 * @param unSet   the flags to clear when the timer triggers
	 * @param id      the identifier (name) of the new timer to manipulate the timer
	 *                later
	 * @param target  the page to go to when the timer triggers
	 */
	public void addTimer(String delay, String jScript, String set, String unSet, String id, String target) {
		Timer timer = new Timer(delay, jScript, "", "", "", "", set, unSet, "", "", id, target);
		Calendar timCountDown = Calendar.getInstance();
		timCountDown.add(Calendar.SECOND, timer.getTimerSec());
		timer.setTimerEnd(timCountDown);
		mainshell.addTimer(timer);
	}

	/**
	 * Resets the count on a timer
	 * 
	 * @param id    the id set when the timer was created
	 * @param delay the time in seconds (from now) before the timer triggers
	 */
	public void resetTimer(String id, String delay) {
		mainshell.resetTimer(id, comonFunctions.getRandom(delay));
	}

	/**
	 * Writes to the java script console in the debug window
	 * 
	 * This also gets called by the jscriptLog function which will write to the log
	 * and the console
	 * 
	 * @param logText text to display in the debug window
	 */
	public void updateJConsole(String logText) {
		Display.getDefault().syncExec(() -> mainshell.updateJConsole(logText));
	}

	/**
	 * Clears the java script console in the debug window
	 * 
	 */
	public void clearJConsole() {
		Display.getDefault().syncExec(() -> mainshell.clearJConsole());
	}


	/** @exclude */
	public void refreshVars() {
		mainshell.refreshVars();
	}

	/** @exclude */
	public String getDelayScriptVar() {
		return delayScriptVar;
	}

	/** @exclude */
	public void setDelayScriptVar(String delayScriptVar) {
		this.delayScriptVar = delayScriptVar;
	}

	/** @exclude */
	public boolean pageExists(String chapter, String page) {
		try {
			return chapters.get(chapter).getPages().containsKey(page);
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Checks to see if a page exists
	 * 
	 * @param page the page id to check for
	 * @return true if the page exists
	 */
	public boolean pageExists(String page) {
		return pageExists("default", page);
	}

	/**
	 * Corrects a path so it contains the correct directory separator
	 * 
	 * \ for Windows / for Mac and Linux
	 * 
	 * @param pathIn the path to be corrected
	 * @return the path with the correct separators
	 */
	public String fixPath(String pathIn) {
		AppSettings appSettings = AppSettings.getAppSettings();
		String pathOut;
		try {
			pathOut = comonFunctions.fixSeparator(pathIn, appSettings.getFileSeparator());
		} catch (Exception ex) {
			pathOut = pathIn;
			logger.error("Guide.fixPath: " + ex.getLocalizedMessage(), ex);
		}
		logger.debug("Guide.fixPath: pathIn={} return={}", pathIn, pathOut);
		return pathOut;
	}

	/**
	 * Play an audio file
	 * 
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select an audio file in the sub directory kate
	 * with a file name starting with home
	 * 
	 * startAt : to start 90 seconds in 00:01:30 stopAt : to stop at 95 seconds into
	 * the video 00:01:35 scriptVar: set script variables e.g.
	 * audio=finished,stage=5
	 * 
	 * 
	 * @param id        the file name for the audio
	 * @param startAt   the start time for the audio hh:mm:ss
	 * @param stopAt    the stop time for audio hh:mm:ss
	 * @param repeat    the number of times to repeat the audio
	 * @param target    the page to go to when the audio stops
	 * @param jscript   the Java Script function to run when the audio stops
	 * @param scriptVar set script variables
	 */
	public void playAudio(String audio, String startAt, String stopAt, int loops, String target, String jscript,
			String scriptVar) {
		int startAtSeconds;
		if (!startAt.equals("")) {
			startAtSeconds = comonFunctions.getMilisecFromTime(startAt) / 1000;
		} else {
			startAtSeconds = 0;
		}
		int stopAtSeconds;
		if (!stopAt.equals("")) {
			stopAtSeconds = comonFunctions.getMilisecFromTime(stopAt) / 1000;
		} else {
			stopAtSeconds = 0;
		}

		AppSettings appSettings = AppSettings.getAppSettings();

		String imgPath = comonFunctions.getMediaFullPath(audio, appSettings.getFileSeparator(), appSettings, guide);
		mainshell.playAudio(imgPath, startAtSeconds, stopAtSeconds, loops, target, jscript, scriptVar, 100, false);
	}

	/**
	 * Play an audio file
	 * 
	 * id : File must be in the media directory (or subdirectory) Wild cards can be
	 * used e.g. kate/home*.* would select an audio file in the sub directory kate
	 * with a file name starting with home
	 * 
	 * startAt : to start 90 seconds in 00:01:30 stopAt : to stop at 95 seconds into
	 * the video 00:01:35 scriptVar: set script variables e.g.
	 * audio=finished,stage=5
	 * 
	 * 
	 * @param id        the file name for the audio
	 * @param startAt   the start time for the audio hh:mm:ss
	 * @param stopAt    the stop time for audio hh:mm:ss
	 * @param repeat    the number of times to repeat the audio
	 * @param target    the page to go to when the audio stops
	 * @param jscript   the Java Script function to run when the audio stops
	 * @param scriptVar set script variables
	 * @param volume    value between 0 and 100 to set the volume
	 */
	public void playAudio(String audio, String startAt, String stopAt, int loops, String target, String jscript,
			String scriptVar, int volume) {
		int startAtSeconds;
		if (!startAt.equals("")) {
			startAtSeconds = comonFunctions.getMilisecFromTime(startAt) / 1000;
		} else {
			startAtSeconds = 0;
		}
		int stopAtSeconds;
		if (!stopAt.equals("")) {
			stopAtSeconds = comonFunctions.getMilisecFromTime(stopAt) / 1000;
		} else {
			stopAtSeconds = 0;
		}

		AppSettings appSettings = AppSettings.getAppSettings();

		String imgPath = comonFunctions.getMediaFullPath(audio, appSettings.getFileSeparator(), appSettings, guide);
		mainshell.playAudio(imgPath, startAtSeconds, stopAtSeconds, loops, target, jscript, scriptVar, volume, false);
	}

	/**
	 * Gets the absolute path to the current tease directory
	 * 
	 */
	public String getDataDirectory() {
		return AppSettings.getAppSettings().getDataDirectory();
	}

	/**
	 * Get all global buttons
	 * 
	 * @return Global buttons
	 */
	public List<GlobalButton> getGlobalButtons() {
		return new ArrayList<>(globalButtons.values());
	}

	/**
	 * Get a specific global button
	 * 
	 * @param id ID of button
	 * @return Global button object
	 */
	public GlobalButton getGlobalButton(String id) {
		return globalButtons.get(id);
	}

	/**
	 * Adds a new global button
	 * 
	 * @param id     ID of button
	 * @param button Button object
	 */
	public void addGlobalButton(String id, GlobalButton button) {
		globalButtons.put(id, button);
	}

	/**
	 * Removes a global button by ID
	 * 
	 * @param id ID to remove
	 */
	public void removeGlobalButton(String id) {
		globalButtons.remove(id);
	}
}
