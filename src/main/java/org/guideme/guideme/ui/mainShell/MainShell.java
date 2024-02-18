package org.guideme.guideme.ui.mainShell;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.snapps.swt.SquareButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.eclipse.swt.widgets.*;
import org.guideme.guideme.MainLogic;
import org.guideme.guideme.model.Button;
import org.guideme.guideme.model.Chapter;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.model.Page;
import org.guideme.guideme.model.Timer;
import org.guideme.guideme.model.WebcamButton;
import org.guideme.guideme.readers.XmlGuideReader;
import org.guideme.guideme.scripting.Jscript;
import org.guideme.guideme.scripting.OverRide;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.ComonFunctions;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.settings.UserSettings;
import org.guideme.guideme.ui.AudioPlayer;
import org.guideme.guideme.ui.CompositeVideoSurface;
import org.guideme.guideme.ui.DebugShell;
import org.guideme.guideme.ui.MetronomePlayer;
import org.guideme.guideme.ui.SwtEmbeddedMediaPlayer;
import org.imgscalr.Scalr;
import org.mozilla.javascript.ContextFactory;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.lib.LibVlc;
import uk.co.caprica.vlcj.binding.support.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.player.base.AudioDevice;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.LinuxVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.OsxVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.WindowsVideoSurfaceAdapter;

public class MainShell {
	/*
	 * Main screen and UI thread. exposes methods that allow other components to
	 * update the screen components and play video and music
	 */
	static Logger logger = LogManager.getLogger();
	private static org.eclipse.swt.graphics.Color colourBlack;
	private static org.eclipse.swt.graphics.Color colourWhite;
	AppSettings appSettings;
	private int MintFontSize;
	private int MintButtonFontSize;
	private int MintHtmlFontSize;
	private int MintTimerFontSize;
	String strGuidePath;
	String guideFile;
	int videoLoops = 0;
	int videoStartAt = 0;
	int videoStopAt = 0;
	String videoTarget = "";
	String videoJscript = "";
	String videoScriptVar = "";
	private boolean videoPlay = false;
	private boolean webcamVisible = false;
	// private boolean webcamRecording = false;
	Guide guide = Guide.getGuide();
	GuideSettings guideSettings = guide.getSettings();
	UserSettings userSettings = null;
	Label lblLeft;
	private Label lblCentre;
	Label lblRight;
	Browser imageLabel;
	private Browser brwsText;
	SashForm sashform;
	SashForm sashform2;
	private Composite btnComp;
	Composite leftFrame;
	Calendar calCountDown = null;
	private Calendar pausedAt = null;
	Shell shell;
	Shell shell2;
	// private Shell shell3;
	DebugShell debugShell;
	Display myDisplay;
	Font controlFont;
	Font buttonFont;
	Font timerFont;
	Composite mediaPanel;
	// private MediaPlayerFactory mediaPlayerFactory;
	SwtEmbeddedMediaPlayer mediaPlayer;
	// private Frame videoFrame;
	// private Canvas videoSurfaceCanvas;
	// private CanvasVideoSurface videoSurface;
	Composite webcamPanel;
	Webcam webcam;
	private WebcamPanel panel;
	private JRootPane webcamRoot;
	MainShell mainShell;
	MainLogic mainLogic = MainLogic.getMainLogic();
	ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	XmlGuideReader xmlGuideReader = XmlGuideReader.getXmlGuideReader();
	private SquareButton delayButton = null;
	MetronomePlayer metronome;
	private Thread threadMetronome;
	AudioPlayer audioPlayer;
	Thread threadAudioPlayer;
	AudioPlayer audioPlayer2;
	Thread threadAudioPlayer2;
	private Thread threadVideoPlayer;
	boolean videoOn = true;
	private boolean webcamOn = true;
	String style = "";
	String defaultStyle = "";
	boolean imgOverRide = false;
	boolean multiMonitor = true;
	private boolean overlayTimer = false;
	HashMap<String, com.snapps.swt.SquareButton> hotKeys = new HashMap<String, com.snapps.swt.SquareButton>();
	private HashMap<String, com.snapps.swt.SquareButton> buttons = new HashMap<String, com.snapps.swt.SquareButton>();
	shellKeyEventListener keyListener;
	// private shellMouseMoveListener mouseListen;
	boolean showMenu = true;
	Menu MenuBar;
	HashMap<String, Timer> timer = new HashMap<String, Timer>();
	// private ArrayList<Timer> timer = new ArrayList<Timer>();
	private Rectangle clientArea;
	private Rectangle clientArea2;
	boolean inPrefShell = false;
	private String rightHTML;
	String leftHTML;
	private ContextFactory factory;
	private File oldImage;
	private File oldImage2;
	private boolean videoPlayed = false;
	private ResourceBundle displayText;
	String ProcStatusText = "";
	private boolean hasVideoDeferred = false;
	private boolean hasAudioDeferred = false;
	private boolean hasAudio2Deferred = false;
	boolean pauseRequested = false;
	// private boolean exitTriggered = false;

	public Shell createShell(final Display display) {
		logger.trace("Enter createShell");
		comonFunctions.setDisplay(display);
		// Initialise variable
		int[] intWeights1 = new int[2];
		int[] intWeights2 = new int[2];
		colourBlack = display.getSystemColor(SWT.COLOR_BLACK);
		colourWhite = display.getSystemColor(SWT.COLOR_WHITE);

		try {
			logger.trace("Get appSettings");
			appSettings = AppSettings.getAppSettings();

			Locale locale = new Locale.Builder().setLanguage(appSettings.getLanguage())
					.setRegion(appSettings.getCountry()).build();
			displayText = ResourceBundle.getBundle("DisplayBundle", locale);
			appSettings.setDisplayText(displayText);

			// video flag
			videoOn = appSettings.getVideoOn();

			// webcam flag
			webcamOn = appSettings.getWebcamOn();

			// font size
			MintFontSize = appSettings.getFontSize();

			// font size
			MintHtmlFontSize = appSettings.getHtmlFontSize();

			// font size
			MintTimerFontSize = appSettings.getTimerFontSize();

			// font size
			MintButtonFontSize = appSettings.getButtonFontSize();

			// path to the xml files
			strGuidePath = appSettings.getDataDirectory();

			// array to hold the various flags
			guideSettings.setFlags("");

			// width and height of the sizable containers on the screen
			intWeights1 = appSettings.getSash1Weights();
			intWeights2 = appSettings.getSash2Weights();

			logger.trace("Get userSettings");

			userSettings = UserSettings.getUserSettings();

		} catch (NumberFormatException e) {
			logger.error("OnCreate NumberFormatException ", e);
		} catch (Exception e) {
			logger.error("OnCreate Exception ", e);
		}

		try {
			// Create the main UI elements
			logger.trace("display");
			myDisplay = display;
			logger.trace("shell");
			if (appSettings.isFullScreen()) {
				shell = new Shell(myDisplay, SWT.NO_TRIM);
			} else {
				shell = new Shell(myDisplay);
			}
			logger.trace("shell add listener");
			shell.addShellListener(new shellCloseListen(this));
			// Good
			logger.trace("key filter");
			keyListener = new shellKeyEventListener(this);
			myDisplay.addFilter(SWT.KeyDown, keyListener);
			// mouseListen = new shellMouseMoveListener();
			// myDisplay.addFilter(SWT.MouseMove, mouseListen);
			int mainMonitor = appSettings.getMainMonitor();

			clientArea2 = null;
			multiMonitor = appSettings.isMultiMonitor();
			Monitor monitors[] = display.getMonitors();
			if (multiMonitor) {
				// multi monitor
				if (monitors.length > 1) {
					shell2 = new Shell(myDisplay, SWT.NO_TRIM);
					if (mainMonitor > 1) {
						if (appSettings.isFullScreen()) {
							clientArea2 = monitors[0].getBounds();
						} else {
							clientArea2 = monitors[0].getClientArea();
						}
					} else {
						if (appSettings.isFullScreen()) {
							clientArea2 = monitors[1].getBounds();
						} else {
							clientArea2 = monitors[1].getClientArea();
						}
					}
					FormLayout layout2 = new FormLayout();
					shell2.setLayout(layout2);
				} else {
					multiMonitor = false;
				}
			}

			// debug shell
			debugShell = DebugShell.getDebugShell();
			debugShell.createShell(myDisplay, this);

			// get primary monitor and its size
			clientArea = monitors[0].getClientArea();
			if (mainMonitor == 1) {
				if (appSettings.isFullScreen()) {
					clientArea = monitors[0].getBounds();
				} else {
					clientArea = monitors[0].getClientArea();
				}
			} else {
				if (monitors.length > 1) {
					if (appSettings.isFullScreen()) {
						clientArea = monitors[1].getBounds();
					} else {
						clientArea = monitors[1].getClientArea();
					}
				} else {
					if (appSettings.isFullScreen()) {
						clientArea = monitors[0].getBounds();
					} else {
						clientArea = monitors[0].getClientArea();
					}
				}
			}
			shell.setText("Guide Me (" + ComonFunctions.getVersion() + ")");
			FormLayout layout = new FormLayout();
			shell.setLayout(layout);

			Font sysFont = display.getSystemFont();
			FontData[] fD = sysFont.getFontData();
			fD[0].setHeight(MintFontSize);
			controlFont = new Font(display, fD);
			fD[0].setHeight(MintTimerFontSize);
			timerFont = new Font(display, fD);
			fD[0].setHeight(MintButtonFontSize);
			buttonFont = new Font(display, fD);

			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			lblLeft = new Label(shell, SWT.LEFT);
			lblLeft.setBackground(colourBlack);
			lblLeft.setForeground(colourWhite);
			lblLeft.setFont(controlFont);
			lblLeft.setText(dateFormat.format(cal.getTime()));

			lblCentre = new Label(shell, SWT.RIGHT);
			lblCentre.setBackground(colourBlack);
			lblCentre.setForeground(colourWhite);
			lblCentre.setFont(controlFont);
			lblCentre.setText("Title, Author");

			if (!multiMonitor) {
				sashform = new SashForm(shell, SWT.HORIZONTAL);
				sashform.setBackground(colourBlack);
				leftFrame = new Composite(sashform, SWT.SHADOW_NONE);
			} else {
				leftFrame = new Composite(shell, SWT.SHADOW_NONE);
			}

			leftFrame.setBackground(colourBlack);
			FormLayout layoutLF = new FormLayout();
			leftFrame.setLayout(layoutLF);

			mediaPanel = new Composite(leftFrame, SWT.EMBEDDED);
			FormLayout layout3 = new FormLayout();
			mediaPanel.setLayout(layout3);
			mediaPanel.setBackground(colourBlack);
			mediaPanel.addControlListener(new mediaPanelListener(this));

			webcamPanel = new Composite(leftFrame, SWT.EMBEDDED);
			FormLayout layoutwebcam = new FormLayout();
			webcamPanel.setLayout(layoutwebcam);
			webcamPanel.setBackground(colourBlack);

			// defaultStyle
			try {
				defaultStyle = comonFunctions.readFile("./defaultCSS.TXT", StandardCharsets.UTF_8);
				defaultStyle = defaultStyle.replace("MintHtmlFontSize", String.valueOf(MintHtmlFontSize));
			} catch (Exception ex2) {
				defaultStyle = "";
				logger.error("Load defaultCSS.txt error:" + ex2.getLocalizedMessage(), ex2);
			}

			style = defaultStyle;

			// default HTML
			try {
				rightHTML = comonFunctions.readFile("DefaultRightHtml.txt", StandardCharsets.UTF_8);
			} catch (Exception ex2) {
				rightHTML = "";
				logger.error("Load DefaultRightHtml.txt error:" + ex2.getLocalizedMessage(), ex2);
			}

			try {
				leftHTML = comonFunctions.readFile("DefaultLeftHtml.txt", StandardCharsets.UTF_8);
			} catch (Exception ex2) {
				leftHTML = "";
				logger.error("Load DefaultLeftHtml.txt error:" + ex2.getLocalizedMessage(), ex2);
			}

			String strHtml = rightHTML.replace("BodyContent", "");
			strHtml = strHtml.replace("DefaultStyle", defaultStyle);
			imageLabel = new Browser(leftFrame, 0);
			imageLabel.setText("");
			imageLabel.setBackground(colourBlack);
			imageLabel.addStatusTextListener(new EventStatusTextListener(this));
			// imageLabel.setAlignment(SWT.CENTER);

			if (!multiMonitor) {
				sashform2 = new SashForm(sashform, SWT.VERTICAL);
			} else {
				sashform2 = new SashForm(shell2, SWT.VERTICAL);
			}
			sashform2.setBackground(colourBlack);

			if (!overlayTimer) {
				lblRight = new Label(sashform2, SWT.RIGHT);
			} else {
				if (!multiMonitor) {
					lblRight = new Label(shell, SWT.RIGHT);
				} else {
					lblRight = new Label(leftFrame, SWT.RIGHT);
				}
			}
			lblRight.setBackground(colourBlack);
			lblRight.setForeground(colourWhite);
			lblRight.setFont(timerFont);
			lblRight.setAlignment(SWT.CENTER);

			// brwsText = new Browser(sashform2, SWT.MOZILLA);
			brwsText = new Browser(sashform2, SWT.NONE);
			// brwsText = new Browser(sashform2, SWT.WEBKIT);
			brwsText.setText(strHtml);
			brwsText.setBackground(colourBlack);
			brwsText.addStatusTextListener(new EventStatusTextListener(this));

			btnComp = new Composite(sashform2, SWT.SHADOW_NONE);
			btnComp.setBackground(colourBlack);
			RowLayout layout2 = new RowLayout();
			btnComp.setLayout(layout2);

			// Good
			if (videoOn) {
				logger.trace("Video Enter");
				try {
					/*
					 * videoFrame = SWT_AWT.new_Frame(mediaPanel);
					 * videoSurfaceCanvas = new Canvas();
					 * 
					 * videoSurfaceCanvas.setBackground(java.awt.Color.black);
					 * videoFrame.add(videoSurfaceCanvas);
					 * 
					 * mediaPlayerFactory = new
					 * MediaPlayerFactory("--no-video-title-show"); mediaPlayer
					 * = mediaPlayerFactory.newEmbeddedMediaPlayer();
					 * 
					 * videoSurface =
					 * mediaPlayerFactory.newVideoSurface(videoSurfaceCanvas);
					 * 
					 * mediaPlayer.setVideoSurface(videoSurface);
					 * 
					 * mediaPlayer.addMediaPlayerEventListener(new
					 * MediaListener());
					 */

					libvlc_instance_t instance = LibVlc.libvlc_new(0, null);

					mediaPlayer = new SwtEmbeddedMediaPlayer(instance);
					mediaPlayer.setVideoSurface(new CompositeVideoSurface(mediaPanel, getVideoSurfaceAdapter()));
					mediaPlayer.events().addMediaPlayerEventListener(new MediaListener(this));

					String videoOutputDevice = appSettings.getVideoDevice();
					if (videoOutputDevice != null && !videoOutputDevice.equals("")) {
						// TODO this doesn't seem quite right
						mediaPlayer.audio().setOutputDevice(null, appSettings.getVideoDevice());
					}
				} catch (Exception vlcex) {
					logger.error("VLC intialisation error " + vlcex.getLocalizedMessage(), vlcex);
				}
				logger.trace("Video Exit");
			}

			if (webcamOn) {
				try {
					Frame frame = SWT_AWT.new_Frame(webcamPanel);

					webcamRoot = new JRootPane();
					frame.add(webcamRoot);

				} catch (Exception webcamex) {
					logger.error("Webcam intialisation error " + webcamex.getLocalizedMessage(), webcamex);
				}

			}

			// Set the layout and how it responds to screen resize
			FormData lblLeftFormData = new FormData();
			lblLeftFormData.top = new FormAttachment(0, 0);
			lblLeftFormData.left = new FormAttachment(0, 0);
			lblLeftFormData.right = new FormAttachment(33, 0);
			lblLeft.setLayoutData(lblLeftFormData);

			FormData lblCentreFormData = new FormData();
			lblCentreFormData.top = new FormAttachment(0, 0);
			lblCentreFormData.left = new FormAttachment(lblLeft, 0);
			lblCentreFormData.right = new FormAttachment(100, 0);
			lblCentre.setLayoutData(lblCentreFormData);

			if (overlayTimer) {
				FormData lblRightFormData = new FormData();
				lblRightFormData.top = new FormAttachment(leftFrame, 0);
				lblRightFormData.left = new FormAttachment(leftFrame, 0);
				lblRight.setLayoutData(lblRightFormData);
				lblRight.moveAbove(null);
			}

			if (!multiMonitor) {
				FormData SashFormData = new FormData();
				SashFormData.top = new FormAttachment(lblLeft, 0);
				SashFormData.left = new FormAttachment(0, 0);
				SashFormData.right = new FormAttachment(100, 0);
				SashFormData.bottom = new FormAttachment(100, 0);
				sashform.setLayoutData(SashFormData);

				FormData SashFormData2 = new FormData();
				SashFormData2.top = new FormAttachment(sashform, 0);
				SashFormData2.left = new FormAttachment(sashform, 0);
				SashFormData2.right = new FormAttachment(sashform, 0);
				SashFormData2.bottom = new FormAttachment(sashform, 0);
				sashform2.setLayoutData(SashFormData2);
			} else {

				FormData leftFrameFormData = new FormData();
				leftFrameFormData.top = new FormAttachment(lblLeft, 0);
				leftFrameFormData.left = new FormAttachment(0, 0);
				leftFrameFormData.right = new FormAttachment(100, 0);
				leftFrameFormData.bottom = new FormAttachment(100, 0);
				leftFrame.setLayoutData(leftFrameFormData);

				FormData SashFormData2 = new FormData();
				SashFormData2.top = new FormAttachment(0, 0);
				SashFormData2.left = new FormAttachment(0, 0);
				SashFormData2.right = new FormAttachment(100, 0);
				SashFormData2.bottom = new FormAttachment(100, 0);
				sashform2.setLayoutData(SashFormData2);
			}

			FormData btnCompFormData = new FormData();
			btnCompFormData.top = new FormAttachment(sashform2, 0);
			btnCompFormData.left = new FormAttachment(sashform2, 0);
			btnCompFormData.right = new FormAttachment(sashform2, 0);
			btnCompFormData.bottom = new FormAttachment(sashform2, 0);
			btnComp.setLayoutData(btnCompFormData);

			FormData MediaPanelFormData = new FormData();
			MediaPanelFormData.top = new FormAttachment(0, 0);
			MediaPanelFormData.left = new FormAttachment(0, 0);
			MediaPanelFormData.right = new FormAttachment(100, 0);
			MediaPanelFormData.bottom = new FormAttachment(100, 0);
			mediaPanel.setLayoutData(MediaPanelFormData);

			FormData WebcamPanelFormData = new FormData();
			WebcamPanelFormData.top = new FormAttachment(0, 0);
			WebcamPanelFormData.left = new FormAttachment(0, 0);
			WebcamPanelFormData.right = new FormAttachment(100, 0);
			WebcamPanelFormData.bottom = new FormAttachment(100, 0);
			webcamPanel.setLayoutData(WebcamPanelFormData);

			FormData imageLabelFormData = new FormData();
			imageLabelFormData.top = new FormAttachment(0, 0);
			imageLabelFormData.left = new FormAttachment(0, 0);
			imageLabelFormData.right = new FormAttachment(100, 0);
			imageLabelFormData.bottom = new FormAttachment(100, 0);
			imageLabel.setLayoutData(imageLabelFormData);

			// Menu Bar
			MenuBar = new Menu(shell, SWT.BAR);

			// Top Level File drop down
			MenuItem fileItem = new MenuItem(MenuBar, SWT.CASCADE);
			fileItem.setText(displayText.getString("MainFile"));

			// Sub Menu for File
			Menu fileSubMenu = new Menu(shell, SWT.DROP_DOWN);
			// Associate it with the top level File menu
			fileItem.setMenu(fileSubMenu);

			// File Load menu item
			MenuItem fileLoadItem = new MenuItem(fileSubMenu, SWT.PUSH);
			fileLoadItem.setText(displayText.getString("MainLoad"));
			fileLoadItem.addSelectionListener(new FileLoadListener(this));

			// File Library menu item
			MenuItem fileLibraryItem = new MenuItem(fileSubMenu, SWT.PUSH);
			fileLibraryItem.setText(displayText.getString("MainLibrary"));
			fileLibraryItem.addSelectionListener(new FileLibraryListener(this));

			// File Restart menu item
			MenuItem fileRestartItem = new MenuItem(fileSubMenu, SWT.PUSH);
			fileRestartItem.setText(displayText.getString("MainRestart"));
			fileRestartItem.addSelectionListener(new FileRestartListener(this));

			// File Reload menu item
			MenuItem fileReloadItem = new MenuItem(fileSubMenu, SWT.PUSH);
			fileReloadItem.setText(displayText.getString("MainReload"));
			fileReloadItem.addSelectionListener(new FileReloadListener(this));

			// File Preferences menu item
			MenuItem filePreferencesItem = new MenuItem(fileSubMenu, SWT.PUSH);
			filePreferencesItem.setText(displayText.getString("MainAppPref"));
			filePreferencesItem.addSelectionListener(new FilePreferences(this));

			// good

			// File Preferences Guide menu item
			MenuItem filePreferencesGuideItem = new MenuItem(fileSubMenu, SWT.PUSH);
			filePreferencesGuideItem.setText(displayText.getString("MainUserPref"));
			filePreferencesGuideItem.addSelectionListener(new FilePreferencesGuide(this));

			// File Guide Preferences menu item
			MenuItem fileGuidePreferencesItem = new MenuItem(fileSubMenu, SWT.PUSH);
			fileGuidePreferencesItem.setText(displayText.getString("MainGuidePref"));
			fileGuidePreferencesItem.addSelectionListener(new FileGuidePreferences(this));

			// File Exit menu item
			MenuItem fileExitItem = new MenuItem(fileSubMenu, SWT.PUSH);
			fileExitItem.setText(displayText.getString("MainExit"));
			fileExitItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					logger.trace("Enter Menu Exit");
					shell.close();
					logger.trace("Exit Menu Exit");
				}
			});

			// Top Level Tools drop down
			MenuItem toolsItem = new MenuItem(MenuBar, SWT.CASCADE);
			toolsItem.setText(displayText.getString("MainTools"));

			// Sub Menu for Tools
			Menu toolsSubMenu = new Menu(shell, SWT.DROP_DOWN);
			// Associate it with the top level File menu
			toolsItem.setMenu(toolsSubMenu);

			// Tools Compress menu item
			MenuItem CompressGuideItem = new MenuItem(toolsSubMenu, SWT.PUSH);
			CompressGuideItem.setText(displayText.getString("MainToolsCompress"));
			CompressGuideItem.addSelectionListener(new CompressGuideListener(this));

			// Tools UnCompress menu item
			MenuItem UnCompressGuideItem = new MenuItem(toolsSubMenu, SWT.PUSH);
			UnCompressGuideItem.setText(displayText.getString("MainToolsUnCompress"));
			UnCompressGuideItem.addSelectionListener(new UnCompressGuideListener(this));

			// Tools Resize menu item
			MenuItem ResizeGuideItem = new MenuItem(toolsSubMenu, SWT.PUSH);
			ResizeGuideItem.setText(displayText.getString("MainToolsResizeImage"));
			ResizeGuideItem.addSelectionListener(new ResizeGuideListener(this));

			// good
			// Audio Output Menus
			MenuItem audioItem = new MenuItem(MenuBar, SWT.CASCADE);
			audioItem.setText(displayText.getString("MainAudio"));

			Menu audioSubMenu = new Menu(shell, SWT.DROP_DOWN);
			audioItem.setMenu(audioSubMenu);

			MenuItem videoOutputItem = new MenuItem(audioSubMenu, SWT.CASCADE);
			videoOutputItem.setText(displayText.getString("VideoDev"));

			MenuItem audioOneOutputItem = new MenuItem(audioSubMenu, SWT.CASCADE);
			audioOneOutputItem.setText(displayText.getString("AudioDevOne"));

			MenuItem audioTwoOutputItem = new MenuItem(audioSubMenu, SWT.CASCADE);
			audioTwoOutputItem.setText(displayText.getString("AudioDevTwo"));

			Menu videoSubMenu = new Menu(shell, SWT.DROP_DOWN);
			videoOutputItem.setMenu(videoSubMenu);

			Menu audioOneSubMenu = new Menu(shell, SWT.DROP_DOWN);
			audioOneOutputItem.setMenu(audioOneSubMenu);

			Menu audioTwoSubMenu = new Menu(shell, SWT.DROP_DOWN);
			audioTwoOutputItem.setMenu(audioTwoSubMenu);

			// good
			boolean userDeviceVideoAvailable = false;
			boolean userDeviceOneAvailable = false;
			boolean userDeviceTwoAvailable = false;

			List<AudioDevice> outputs = mediaPlayer.audio().outputDevices();

			// bad
			for (AudioDevice device : outputs) {
				boolean userDeviceVideo = device.getDeviceId().equals(appSettings.getVideoDevice());
				boolean userDeviceOne = device.getDeviceId().equals(appSettings.getAudioOneDevice());
				boolean userDeviceTwo = device.getDeviceId().equals(appSettings.getAudioTwoDevice());

				MenuItem videoDevice = new MenuItem(videoSubMenu, SWT.RADIO);
				videoDevice.setText(device.getLongName());
				videoDevice.setData("device-id", device.getDeviceId());
				videoDevice.setSelection(userDeviceVideo);
				videoDevice.addSelectionListener(new VideoDeviceChangedListener(this));

				MenuItem audioOneDevice = new MenuItem(audioOneSubMenu, SWT.RADIO);
				audioOneDevice.setText(device.getLongName());
				audioOneDevice.setData("device-id", device.getDeviceId());
				audioOneDevice.setSelection(userDeviceOne);
				audioOneDevice.addSelectionListener(new AudioOneDeviceChangedListener(this));

				MenuItem audioTwoDevice = new MenuItem(audioTwoSubMenu, SWT.RADIO);
				audioTwoDevice.setText(device.getLongName());
				audioTwoDevice.setData("device-id", device.getDeviceId());
				audioTwoDevice.setSelection(userDeviceTwo);
				audioTwoDevice.addSelectionListener(new AudioTwoDeviceChangedListener(this));

				userDeviceVideoAvailable |= userDeviceVideo;
				userDeviceOneAvailable |= userDeviceOne;
				userDeviceTwoAvailable |= userDeviceTwo;
			}
//bad
			if (!userDeviceVideoAvailable) {
				logger.info("User selected Video device not available. Resetting to default");
				MenuItem videoDefault = videoSubMenu.getItem(0);
				videoDefault.setSelection(true);
				appSettings.setVideoDevice(videoDefault.getData("device-id").toString());
			}
			if (!userDeviceOneAvailable) {
				logger.info("User selected Audio device not available. Resetting to default");
				MenuItem oneDefault = audioOneSubMenu.getItem(0);
				oneDefault.setSelection(true);
				appSettings.setAudioOneDevice(oneDefault.getData("device-id").toString());
			}
			if (!userDeviceTwoAvailable) {
				logger.info("User selected Audio2 device not available. Resetting to default");
				MenuItem twoDefault = audioTwoSubMenu.getItem(0);
				twoDefault.setSelection(true);
				appSettings.setAudioTwoDevice(twoDefault.getData("device-id").toString());
			}

			if (appSettings.getDebug()) {
				// Top Level Debug drop down
				MenuItem debugItem = new MenuItem(MenuBar, SWT.CASCADE);
				debugItem.setText(displayText.getString("MainDebug"));

				// Sub Menu for Debug
				Menu debugSubMenu = new Menu(shell, SWT.DROP_DOWN);
				// Associate it with the top level File menu
				debugItem.setMenu(debugSubMenu);

				// Debug Debug Menu Item
				final MenuItem debugCheck = new MenuItem(debugSubMenu, SWT.CHECK);
				debugCheck.setText(displayText.getString("MainDebugDebug"));
				debugCheck.setSelection(appSettings.getDebug());
				debugCheck.addListener(SWT.Selection, event -> {
					appSettings.setDebug(debugCheck.getSelection());
					if (delayButton != null && !delayButton.isDisposed()) {
						delayButton.setVisible(appSettings.getDebug() && appSettings.getShowDelayBtn());
					}
				});

				// Debug Javascript Debug Menu Item
				final MenuItem jsdebugCheck = new MenuItem(debugSubMenu, SWT.CHECK);
				jsdebugCheck.setText(displayText.getString("MainDebugJava"));
				jsdebugCheck.setSelection(appSettings.getJsDebug());
				jsdebugCheck.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						appSettings.setJsDebug(jsdebugCheck.getSelection());
					}
				});
				// Debug Javascript Debug Menu Error Item
				final MenuItem jsdebugErrorCheck = new MenuItem(debugSubMenu, SWT.CHECK);
				jsdebugErrorCheck.setText(displayText.getString("MainDebugException"));
				jsdebugErrorCheck.setSelection(appSettings.getJsDebugError());
				jsdebugErrorCheck.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						appSettings.setJsDebugError(jsdebugErrorCheck.getSelection());
					}
				});
				// Debug Javascript Debug Menu Enter Item
				final MenuItem jsdebugEnterCheck = new MenuItem(debugSubMenu, SWT.CHECK);
				jsdebugEnterCheck.setText(displayText.getString("MainDebugEnter"));
				jsdebugEnterCheck.setSelection(appSettings.getJsDebugEnter());
				jsdebugEnterCheck.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						appSettings.setJsDebugEnter(jsdebugEnterCheck.getSelection());
					}
				});
				// Debug Javascript Debug Menu Exit Item
				final MenuItem jsdebugExitCheck = new MenuItem(debugSubMenu, SWT.CHECK);
				jsdebugExitCheck.setText(displayText.getString("MainDebugExit"));
				jsdebugExitCheck.setSelection(appSettings.getJsDebugExit());
				jsdebugExitCheck.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						appSettings.setJsDebugExit(jsdebugExitCheck.getSelection());
					}
				});
				// Debug Pause Item
				final MenuItem debugPause = new MenuItem(debugSubMenu, SWT.CHECK);
				debugPause.setText(displayText.getString("MainDebugPause"));
				debugPause.addListener(SWT.Selection, event -> {
					pauseRequested = debugPause.getSelection();
					if (!pauseRequested) {
						resumeAll();
					}
				});
				new MenuItem(debugSubMenu, SWT.SEPARATOR);
				// Debug ShowDelay Item
				final MenuItem showDelay = new MenuItem(debugSubMenu, SWT.CHECK);
				showDelay.setText(displayText.getString("MainDebugShowDelay"));
				showDelay.setSelection(appSettings.getShowDelayBtn());
				showDelay.addListener(SWT.Selection, event -> {
					appSettings.setShowDelayBtn(showDelay.getSelection());
					if (delayButton != null && !delayButton.isDisposed()) {
						delayButton.setVisible(appSettings.getDebug() && appSettings.getShowDelayBtn());
					}
				});
			}
			// Add the menu bar to the shell
			shell.setMenuBar(MenuBar);

			// Resize the image if the control containing it changes size
			imageLabel.addControlListener(new ImageControlAdapter(this));

			// tell SWT to display the correct screen info
			shell.pack();
			shell.setMaximized(true);
			shell.setBounds(clientArea);
			if (!multiMonitor) {
				try {
					sashform.setWeights(intWeights1);
				} catch (Exception ex2) {
					logger.error(ex2.getLocalizedMessage(), ex2);
				}
			} else {
				shell2.pack();
				shell2.setMaximized(true);
				shell2.setBounds(clientArea2);
			}
			try {
				sashform2.setWeights(intWeights2);
			} catch (Exception ex2) {
				logger.error(ex2.getLocalizedMessage(), ex2);
			}
			// timer that updates the clock field and handles any timed events
			// when loading wait 2 seconds before running it
			myDisplay.timerExec(2000, new shellTimer(this));
			metronome = new MetronomePlayer();
			threadMetronome = new Thread(metronome, "metronome");
			threadMetronome.setName("threadMetronome");
			threadMetronome.start();
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		}
		logger.trace("Exit createShell");
		mainShell = this;
		if (!appSettings.getComandLineGuide().equals("")) {
			loadGuide(appSettings.getDataDirectory() + appSettings.getComandLineGuide());
		}
		mainShell.displayPage(guideSettings.getCurrPage());
		Chapter chapter = guide.getChapters().get("default");
		if (chapter != null) {
			for (Page page : chapter.getPages().values()) {
				debugShell.addPagesCombo(page.getId());
			}
			debugShell.setPage(guide.getCurrPage(), true);
		}
		return shell;
	}

	public void run(Display display) {
		do {
			logger.trace("create shell");
			shell = createShell(display);
			logger.trace("open shell");
			shell.open();
			if (mainShell.getMultiMonitor()){
				mainShell.getShell2().open();
			}
			
			//loop round until the window is closed
			while (!shell.isDisposed()) {
				if (appSettings.isMonitorChanging()) {
					try {
						shell.close();
					}
					catch (Exception ex) {
						logger.error("Main shell close " + ex.getLocalizedMessage(), ex);
					}					
				}
				if (!display.readAndDispatch())
				{
					display.sleep();
				}
			}
		} while (appSettings.isMonitorChanging());
	}
	
	// Load the tease
	public void loadGuide(String fileToLoad) {
		try {
			debugShell.clearPagesCombo();
		} catch (Exception ex) {
			logger.error("Clear debug pages " + ex.getLocalizedMessage(), ex);
		}
		try {
			String strPage = xmlGuideReader.loadXML(fileToLoad, guide, appSettings, debugShell);
			guideSettings = guide.getSettings();
			if (guide.getCss().equals("")) {
				style = defaultStyle;
			} else {
				style = guide.getCss();
			}
			// display the first page
			mainLogic.displayPage(strPage, false, guide, mainShell, appSettings, userSettings, guideSettings,
					debugShell);
		} catch (Exception ex) {
			logger.error("Load Guide " + ex.getLocalizedMessage(), ex);
		}
	}

	static int testI = 0;
	/*
	 * // Returns the image passed in resized to the width and height passed in
	 * private static Image resize(Image image, int width, int height) { try {
	 * logger.trace("Enter resize"); Image scaled = new
	 * Image(Display.getDefault(), width, height); GC gc = new GC(scaled);
	 * gc.setAntialias(SWT.ON); gc.setInterpolation(SWT.HIGH);
	 * gc.drawImage(image, 0, 0, image.getBounds().width,
	 * image.getBounds().height, 0, 0, width, height); gc.dispose();
	 * logger.trace("Exit resize"); return scaled; } catch (Exception ex) {
	 * logger.error(" resize " + ex.getLocalizedMessage(), ex);
	 * logger.trace("Exit resize"); return null; }
	 * 
	 * }
	 */

	static HashMap<String, Integer> GetNewDimentions(double imageRatio, int labelHeight, int labelWidth,
			double imgOffSet, boolean maxScale, int maxHeight, int maxWidth) {
		HashMap<String, Integer> returnValue = new HashMap<String, Integer>();
		double dblScreenRatio = (double) labelHeight / (double) labelWidth;
		int newHeight;
		int newWidth;
		if (((labelHeight > maxHeight) && (labelWidth > maxWidth)) && maxScale) {
			if (dblScreenRatio > imageRatio) {
				logger.trace("Scale Choice: 1.1");
				newHeight = (int) (((double) (maxWidth) * imageRatio) * imgOffSet);
				newWidth = (int) ((double) (maxWidth) * imgOffSet);
				logger.trace("New GT Dimentions: H: " + newHeight + " W: " + newWidth);
			} else {
				logger.trace("Scale Choice: 1.2");
				newHeight = (int) ((double) (maxHeight) * imgOffSet);
				newWidth = (int) (((double) (maxHeight) / imageRatio) * imgOffSet);
				logger.trace("New LT Dimentions: H: " + newHeight + " W: " + newWidth);
			}
		} else {
			if (dblScreenRatio > imageRatio) {
				logger.trace("Scale Choice: 2.1");
				newHeight = (int) (((double) labelWidth * imageRatio) * imgOffSet);
				newWidth = (int) ((double) labelWidth * imgOffSet);
				logger.trace("New GT Dimentions: H: " + newHeight + " W: " + newWidth);
			} else {
				logger.trace("Scale Choice: 2.2");
				newHeight = (int) ((double) labelHeight * imgOffSet);
				newWidth = (int) (((double) labelHeight / imageRatio) * imgOffSet);
				logger.trace("New LT Dimentions: H: " + newHeight + " W: " + newWidth);
			}
		}
		returnValue.put("newHeight", newHeight);
		returnValue.put("newWidth", newWidth);
		return returnValue;
	}

	public Calendar getCalCountDown() {
		// return the time a delay should end
		return calCountDown;
	}

	public void setCalCountDown(Calendar calCountDown) {
		// set the time a delay should end
		this.calCountDown = calCountDown;
	}

	public void setLblLeft(String lblLeft) {
		// header to the left of the screen
		// usually displays teh delay
		this.lblLeft.setText(lblLeft);
	}

	public void setLblCentre(String lblCentre) {
		// header to the centre of the screen
		// usually displays the title and author
		this.lblCentre.setText(lblCentre);
	}

	public void setLblRight(String lblRight) {
		// header to the right of the screen
		// usually displays the clock
		this.lblRight.setText(lblRight);
	}

	public void setImage(String imgPath) {
		// delete old image if it exists
		if (oldImage != null && oldImage.exists()) {
			oldImage.delete();
			oldImage = null;
		}
		if (oldImage2 != null && oldImage2.exists()) {
			oldImage2.delete();
			oldImage2 = null;
		}

		// display an image in the area to the left of the screen
		HashMap<String, Integer> imageDimentions;
		imgOverRide = false;

		if (imgPath.lastIndexOf(".") == -1) {
			try {
				boolean newFile = false;
				String extension = "";
				File tmpFile = new File(imgPath);
				URLConnection con = tmpFile.toURI().toURL().openConnection();
				String mimeType = con.getContentType();
				con = null;

				switch (mimeType) {
				case "image/jpeg":
					extension = ".jpg";
					newFile = true;
					break;
				case "image/bmp":
					extension = ".bmp";
					newFile = true;
					break;
				case "image/gif":
					extension = ".gif";
					newFile = true;
					break;
				case "image/png":
					extension = ".png";
					newFile = true;
					break;
				case "image/tiff":
					extension = ".tiff";
					newFile = true;
					break;
				}
				if (newFile) {
					String tmpPath = appSettings.getTempDir();
					oldImage2 = File.createTempFile("TempImage2", extension, new File(tmpPath));
					oldImage2.deleteOnExit();
					FileInputStream strSrc = new FileInputStream(tmpFile);
					FileChannel src = strSrc.getChannel();
					FileOutputStream strDest = new FileOutputStream(oldImage2);
					FileChannel dest = strDest.getChannel();
					dest.transferFrom(src, 0, src.size());
					strSrc.close();
					strDest.close();
					src.close();
					dest.close();
					imgPath = oldImage2.getAbsolutePath();
				}
			} catch (IOException e1) {
			}
		}

		Image memImage = new Image(myDisplay, imgPath);
		imageLabel.setData("imgPath", imgPath);
		try {
			String tmpImagePath;
			String strHtml;
			ImageData imgData = memImage.getImageData();
			Rectangle RectImage = imageLabel.getBounds();
			double dblScreenRatio = (double) RectImage.height / (double) RectImage.width;
			logger.trace("dblScreenRatio: " + dblScreenRatio);
			double dblImageRatio = (double) imgData.height / (double) imgData.width;
			imageLabel.setData("imageRatio", Double.valueOf(dblImageRatio));
			logger.trace("Lable Height: " + RectImage.height);
			logger.trace("Lable Width: " + RectImage.width);
			logger.trace("Image Height: " + imgData.height);
			logger.trace("Image Width: " + imgData.width);

			int maxImageScale = appSettings.getMaxImageScale();
			int maxheight = (int) (imgData.height * ((double) (maxImageScale / 100)));
			int maxwidth = (int) (imgData.width * ((double) (maxImageScale / 100)));

			imageLabel.setData("maxImageScale", maxImageScale);
			imageLabel.setData("maxheight", maxheight);
			imageLabel.setData("maxwidth", maxwidth);

			imageDimentions = GetNewDimentions(dblImageRatio, RectImage.height, RectImage.width,
					appSettings.getImgOffset(), maxImageScale != 0, maxheight, maxwidth);

			if (imgPath.endsWith(".gif")) {
				memImage.dispose();
				memImage = null;
				tmpImagePath = imgPath;
				strHtml = leftHTML.replace("DefaultStyle", defaultStyle + " body { overflow:hidden }");
				strHtml = strHtml.replace("BodyContent",
						"<table id=\"wrapper\"><tr><td><img src=\"" + tmpImagePath + "\" height=\""
								+ imageDimentions.get("newHeight") + "\" width=\"" + imageDimentions.get("newWidth")
								+ "\" /></td></tr></table>");
			} else {
				BufferedImage img = null;
				try {
					ImageIO.setUseCache(false);
					img = ImageIO.read(new File(imgPath));
				} catch (IOException e) {
				}
				if (img.getColorModel().hasAlpha()) {
					img = comonFunctions.dropAlphaChannel(img);
				}
				BufferedImage imageNew = Scalr.resize(img, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC,
						imageDimentions.get("newWidth"), imageDimentions.get("newHeight"), Scalr.OP_ANTIALIAS);
				String imgType = "";
				int pos = imgPath.lastIndexOf(".");
				if (pos > -1) {
					imgType = imgPath.substring(pos + 1);
				}
				String tmpPath = appSettings.getTempDir();
				File newImage = File.createTempFile("tmpImage", "." + imgType, new File(tmpPath));
				oldImage = newImage;
				newImage.deleteOnExit();
				tmpImagePath = newImage.getAbsolutePath();
				ImageIO.write(imageNew, imgType.toLowerCase(), newImage);
				// tmpImagePath = System.getProperty("user.dir") +
				// File.pathSeparator + "tmpImage." + imgType;
				// ImageIO.write(imagenew, imgType, new File(tmpImagePath));
				// Image tmpImage2 = imageLabel.getImage();
				// imageLabel.setImage(resize(memImage, newWidth, newHeight));
				memImage.dispose();
				memImage = null;
				// if (tmpImage2 != null) {
				// tmpImage2.dispose();
				// }
				strHtml = leftHTML.replace("DefaultStyle", defaultStyle + " body { overflow:hidden }");
				strHtml = strHtml.replace("BodyContent",
						"<table id=\"wrapper\"><tr><td><img src=\"" + tmpImagePath + "\" height=\""
								+ imageDimentions.get("newHeight") + "\" width=\"" + imageDimentions.get("newWidth")
								+ "\" /></td></tr></table>");
			}
			imageLabel.setText(strHtml, true);
			logger.trace("Open: " + imgPath);
		} catch (Exception ex6) {
			logger.error("Process Image error " + ex6.getLocalizedMessage(), ex6);
		}
		mediaPanel.setVisible(false);
		webcamPanel.setVisible(false);
		this.imageLabel.setVisible(true);
		leftFrame.layout(true);
	}

	public String getStyle() {
		return style;
	}

	public void setImageHtml(String leftHtml) {
		try {
			logger.trace("setImageHtml: " + leftHtml);
			imgOverRide = true;
			imageLabel.setText(leftHtml, true);
			mediaPanel.setVisible(false);
			webcamPanel.setVisible(false);
			this.imageLabel.setVisible(true);
			leftFrame.layout(true);
		} catch (Exception ex) {
			logger.error("setImageHtml error " + ex.getLocalizedMessage(), ex);
		}
	}

	public boolean showWebcam() {
		// displays the webcam in the area to the left of the screen
		if (webcamOn) {
			try {
				webcam = Webcam.getDefault();

				if (webcam != null) {
					mainShell.setLeftText("", "");
					this.imageLabel.setVisible(false);
					this.mediaPanel.setVisible(false);
					this.webcamPanel.setVisible(true);

					Dimension[] dimensions = webcam.getViewSizes();
					Dimension size = dimensions[dimensions.length - 1];
					webcam.setViewSize(size);
					panel = new WebcamPanel(webcam, size, false);
					panel.setMirrored(true);
					webcamRoot.getContentPane().add(panel);
					panel.start();
					leftFrame.layout(true);
					webcamRoot.validate();
					webcamVisible = true;
					logger.debug("MainShell playVideo: ShowWebcam");

				} else {
					mainShell.setLeftText("No Webcam detected", "");
					Webcam.getDiscoveryService().stop();
				}

			} catch (Exception e) {
				logger.error("showWebcam " + e.getLocalizedMessage(), e);
			}
		}
		return webcam != null;
	}

	public void playVideo(String video, int startAt, int stopAt, int loops, String target, String jscript,
			String scriptVar, int volume, boolean deferStart) {
		// plays a video in the area to the left of the screen
		// sets the number of loops, start / stop time and any page to display
		// if the video finishes
		// starts the video using a non UI thread so VLC can't hang the
		// application
		if (videoOn) {
			try {
				mainShell.setLeftText("", "");
				this.imageLabel.setVisible(false);
				this.webcamPanel.setVisible(false);
				this.mediaPanel.setVisible(true);
				leftFrame.layout(true);
				videoLoops = loops;
				videoTarget = target;
				videoStartAt = startAt;
				videoStopAt = stopAt;
				videoJscript = jscript;
				videoScriptVar = scriptVar;
				videoPlay = true;
				String mrlVideo = "file:///" + video;
				logger.debug("MainShell playVideo: " + mrlVideo + " videoLoops: " + videoLoops + " videoTarget: "
						+ videoTarget + " videoPlay: " + videoPlay);
				VideoPlay videoPlay = new VideoPlay(this);
				videoPlay.setVideoPlay(mediaPlayer, mrlVideo, volume);
				threadVideoPlayer = new Thread(videoPlay, "videoPlay");
				threadVideoPlayer.setName("videoPlayThread");
				if (deferStart) {
					hasVideoDeferred = true;
				} else {
					threadVideoPlayer.start();
					videoPlayed = true;
				}
			} catch (Exception e) {
				logger.error("playVideo " + e.getLocalizedMessage(), e);
			}
		}
	}

	public void clearImage() {
		// imageLabel.setImage(null);
		String strHTML = leftHTML.replace("DefaultStyle", defaultStyle + " body { overflow:hidden }");
		imageLabel.setText(strHTML, true);
	}

	public void playAudio(String audio, int startAt, int stopAt, int loops, String target, String jscript,
			String scriptVar, int volume, boolean deferStart) {
		// run audio on another thread
		try {
			if (audioPlayer != null) {
				audioPlayer.audioStop();
				logger.trace("playAudio audioStop");
			}
			String outputDevice = appSettings.getAudioOneDevice();
			audioPlayer = new AudioPlayer(audio, startAt, stopAt, loops, target, mainShell, jscript, scriptVar,
					outputDevice, volume);
			threadAudioPlayer = new Thread(audioPlayer, "audioPlayer");
			threadAudioPlayer.setName("threadAudioPlayer");
			if (deferStart) {
				hasAudioDeferred = true;
			} else {
				threadAudioPlayer.start();
			}
		} catch (Exception e) {
			logger.error("playAudio " + e.getLocalizedMessage(), e);
		}
	}

	public void playAudio2(String audio, int startAt, int stopAt, int loops, String target, String jscript,
			String scriptVar, int volume, boolean deferStart) {
		// run audio on another thread
		try {
			if (audioPlayer2 != null) {
				audioPlayer2.audioStop();
				logger.trace("playAudio2 audioStop");
			}
			String outputDevice = appSettings.getAudioTwoDevice();
			audioPlayer2 = new AudioPlayer(audio, startAt, stopAt, loops, target, mainShell, jscript, scriptVar,
					outputDevice, volume);
			threadAudioPlayer2 = new Thread(audioPlayer2, "audioPlayer");
			threadAudioPlayer2.setName("threadAudioPlayer2");
			if (deferStart) {
				hasAudio2Deferred = true;
			} else {
				threadAudioPlayer2.start();
			}
		} catch (Exception e) {
			logger.error("playAudio2 " + e.getLocalizedMessage(), e);
		}
	}

	public void startDeferredMedia() {
		if (hasVideoDeferred) {
			threadVideoPlayer.start();
			videoPlayed = true;
			hasVideoDeferred = false;
		}
		if (hasAudioDeferred) {
			threadAudioPlayer.start();
			hasAudioDeferred = false;
		}
		if (hasAudio2Deferred) {
			threadAudioPlayer2.start();
			hasAudio2Deferred = false;
		}
	}

	public void setBrwsText(String brwsText, String overRideStyle) {
		// set HTML to be displayed in the browser control to the right of the
		// screen
		if (overRideStyle.equals("")) {
			overRideStyle = style;
		}
		String strHTML;
		try {
			strHTML = rightHTML.replace("DefaultStyle", overRideStyle);
			strHTML = strHTML.replace("BodyContent", brwsText);
			this.brwsText.setText(strHTML);
			if (appSettings.isToclipboard()) {
				try {
					// copy text to clip board for use in TTS
					String htmlString = brwsText.replaceAll("\\<.*?\\>", " ");
					StringSelection stringSelection = new StringSelection(htmlString);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, stringSelection);
				} catch (Exception e2) {
					logger.error("copy to clip board " + e2.getLocalizedMessage(), e2);
				}
			}
		} catch (Exception e1) {
			logger.error("displayPage Text Exception " + e1.getLocalizedMessage(), e1);
			strHTML = rightHTML.replace("DefaultStyle", overRideStyle);
			strHTML = strHTML.replace("BodyContent", "");
			this.brwsText.setText(strHTML);
		}
	}

	public void setRightHtml(String strHTML) {
		// set HTML to be displayed in the browser control to the left of the
		// screen
		try {
			this.brwsText.setText(strHTML);
		} catch (Exception e1) {
			logger.error("displayPage Text Exception " + e1.getLocalizedMessage(), e1);
			strHTML = rightHTML.replace("DefaultStyle", style);
			strHTML = strHTML.replace("BodyContent", "");
			this.brwsText.setText(strHTML);
		}
	}

	public void setLeftText(String brwsText, String overRideStyle) {
		// set HTML to be displayed in the browser control to the left of the
		// screen
		if (overRideStyle.equals("")) {
			overRideStyle = style;
		}
		String strHTML;
		try {
			strHTML = leftHTML.replace("DefaultStyle", overRideStyle);
			strHTML = strHTML.replace("BodyContent", brwsText);
			this.imageLabel.setText(strHTML);
			if (appSettings.isToclipboard()) {
				try {
					// copy text to clip board for use in TTS
					String htmlString = brwsText.replaceAll("\\<.*?\\>", " ");
					StringSelection stringSelection = new StringSelection(htmlString);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, stringSelection);
				} catch (Exception e2) {
					logger.error("copy to clip board " + e2.getLocalizedMessage(), e2);
				}
			}
		} catch (Exception e1) {
			logger.error("displayPage Text Exception " + e1.getLocalizedMessage(), e1);
			strHTML = leftHTML.replace("DefaultStyle", overRideStyle);
			strHTML = strHTML.replace("BodyContent", "");
			this.imageLabel.setText(strHTML);
		}
		mediaPanel.setVisible(false);
		webcamPanel.setVisible(false);
		this.imageLabel.setVisible(true);
		leftFrame.layout(true);
	}

	public void setLeftHtml(String strHTML) {
		// set HTML to be displayed in the browser control to the left of the
		// screen
		try {
			this.imageLabel.setText(strHTML);
		} catch (Exception e1) {
			logger.error("setLeftHtml Text Exception " + e1.getLocalizedMessage(), e1);
			strHTML = leftHTML.replace("DefaultStyle", style);
			strHTML = strHTML.replace("BodyContent", "");
			this.imageLabel.setText(strHTML);
		}
		mediaPanel.setVisible(false);
		webcamPanel.setVisible(false);
		this.imageLabel.setVisible(true);
		leftFrame.layout(true);
	}

	public void removeButtons() {
		// remove all the buttons displayed for the previous page
		try {
			for (Control kid : btnComp.getChildren()) {
				Image bkgrndImage = kid.getBackgroundImage();
				if (bkgrndImage != null) {
					bkgrndImage.dispose();
				}
				kid.dispose();
			}
		} catch (Exception e) {
			logger.error("removeButtons " + e.getLocalizedMessage(), e);
		}
	}

	public void addDelayButton(Guide guide) {
		// add a delay button
		// used when debug is on to simulate the delay without actually waiting
		// for it
		try {
			delayButton = new com.snapps.swt.SquareButton(btnComp, SWT.PUSH);
			delayButton.setFont(buttonFont);
			delayButton.setText("Delay");

			// record any button set / unset
			if (!guide.getDelaySet().equals("")) {
				delayButton.setData("Set", guide.getDelaySet());
			} else {
				delayButton.setData("Set", "");
			}
			if (!guide.getDelayUnSet().equals("")) {
				delayButton.setData("UnSet", guide.getDelayUnSet());
			} else {
				delayButton.setData("UnSet", "");
			}
			delayButton.setData("Target", guide.getDelTarget());
			delayButton.setData("scriptVar", guide.getDelayScriptVar());
			delayButton.setData("javascript", guide.getDelayjScript());
			delayButton.addSelectionListener(new DynamicButtonListner(this));

			delayButton.setVisible(appSettings.getDebug() && appSettings.getShowDelayBtn());
		} catch (Exception e) {
			logger.error("addDelayButton " + e.getLocalizedMessage(), e);
		}
	}

	public void addButton(Button button, String javascript) {
		// add a normal button to the screen
		String strBtnTarget;
		String strBtnText;
		String strBtnImage;
		Boolean isWebCamButton = button instanceof WebcamButton;
		try {
			strBtnTarget = button.getTarget();
			strBtnText = button.getText();
			strBtnImage = button.getImage();
			if (!strBtnImage.equals("")) {
				String imgPath = comonFunctions.getMediaFullPath(strBtnImage, appSettings.getFileSeparator(),
						appSettings, guide);
				File flImage = new File(imgPath);
				if (flImage.exists()) {
					strBtnImage = imgPath;
				} else {
					strBtnImage = "";
				}
			}
			com.snapps.swt.SquareButton btnDynamic = new com.snapps.swt.SquareButton(btnComp, SWT.PUSH);

			int fntHeight = 0;
			try {
				fntHeight = Integer.parseInt(button.getFontHeight());
			} catch (Exception e) {
				fntHeight = 0;
			}
			try {
				if (button.getFontName() == "" && fntHeight == 0) {
					btnDynamic.setFont(buttonFont);
				} else {
					FontData[] fontData = buttonFont.getFontData();
					if (fntHeight > 0) {
						fontData[0].setHeight(fntHeight);
					}
					if (button.getFontName() != "") {
						fontData[0].setName(button.getFontName());
					}

					final Font newFont = new Font(myDisplay, fontData);
					btnDynamic.setFont(newFont);

					// Since you created the font, you must dispose it
					btnDynamic.addDisposeListener(new DisposeListener() {
						@Override
						public void widgetDisposed(DisposeEvent e) {
							newFont.dispose();
						}
					});
				}
			} catch (Exception e) {
				logger.error("addButton set font" + e.getLocalizedMessage(), e);
			}

			try {
				org.eclipse.swt.graphics.Color bgColor1 = button.getbgColor1();
				org.eclipse.swt.graphics.Color bgColor2 = button.getbgColor2();
				org.eclipse.swt.graphics.Color fontColor = button.getfontColor();

				btnDynamic.setDefaultColors(bgColor1, bgColor2, btnDynamic.getBackground(), fontColor);
			} catch (Exception e) {
				logger.error("addButton set colors" + e.getLocalizedMessage(), e);
			}

			btnDynamic.setText(strBtnText);
			if (!strBtnImage.equals("")) {
				btnDynamic.setBackgroundImage(new Image(myDisplay, strBtnImage));
				btnDynamic.setBackgroundImageStyle(com.snapps.swt.SquareButton.BG_IMAGE_FIT);
			}

			// record any button set / unset
			String strButtonSet;
			String strButtonUnSet;
			strButtonSet = button.getSet();
			if (!strButtonSet.equals("")) {
				btnDynamic.setData("Set", strButtonSet);
			} else {
				btnDynamic.setData("Set", "");
			}
			strButtonUnSet = button.getUnSet();
			if (!strButtonUnSet.equals("")) {
				btnDynamic.setData("UnSet", strButtonUnSet);
			} else {
				btnDynamic.setData("UnSet", "");
			}
			btnDynamic.setData("scriptVar", button.getScriptVar());
			btnDynamic.setData("javascript", javascript);
			logger.debug("displayPage Button Text " + strBtnText + " Target " + strBtnTarget + " Set " + strButtonSet
					+ " UnSet " + strButtonUnSet);

			String hotKey = button.getHotKey();
			if (!hotKey.equals("")) {
				hotKeys.put(hotKey, btnDynamic);
			}

			String btnId = button.getId();
			if (!btnId.equals("")) {
				buttons.put(btnId, btnDynamic);
			}

			btnDynamic.setData("Target", strBtnTarget);
			if (isWebCamButton) {
				WebcamButton webcamButton = (WebcamButton) button;
				btnDynamic.setData("webcamFile", webcamButton.get_destination());
				switch (webcamButton.get_type()) {
				case "Capture":
					btnDynamic.addSelectionListener(new WebcamCaptureListener(mainShell));
					break;
				}
			} else {
				btnDynamic.addSelectionListener(new DynamicButtonListner(this));
			}
			btnDynamic.setEnabled(!button.getDisabled());
		} catch (Exception e) {
			logger.error("addButton " + e.getLocalizedMessage(), e);
		}

	}

	public void runJscript(String function, boolean pageLoading) {
		runJscript(function, null, pageLoading);
	}

	// run the javascript function passed
	public void runJscript(String function, OverRide overRide, boolean pageLoading) {
		try {
			getFormFields();
			refreshVars();
			if (function == null)
				function = "";
			if (!function.equals("")) {
				Page objCurrPage = guide.getChapters().get(guideSettings.getChapter()).getPages()
						.get(guideSettings.getCurrPage());
				String pageJavascript = objCurrPage.getjScript();
				Jscript jscript = new Jscript(guide, userSettings, appSettings, guide.getInPrefGuide(), mainShell,
						overRide, pageJavascript, function, pageLoading);
				SwingUtilities.invokeLater(jscript);
				while (jscript.isRunning()) {
					Display.getCurrent().readAndDispatch();
				}
			}
		} catch (Exception ex) {
			logger.error(" run java script " + ex.getLocalizedMessage(), ex);
		}
	}

	// run the javascript function passed
	public void runJscript(String function, String pageJavascript) {
		try {
			getFormFields();
			refreshVars();
			if (function == null)
				function = "";
			if (!function.equals("")) {
				Jscript jscript = new Jscript(guide, userSettings, appSettings, guide.getInPrefGuide(), mainShell, null,
						pageJavascript, function, false);
				SwingUtilities.invokeLater(jscript);
				while (jscript.isRunning()) {
					Display.getCurrent().readAndDispatch();
				}
			}
		} catch (Exception ex) {
			logger.error(" run java script " + ex.getLocalizedMessage(), ex);
		}
	}

	// get any fields from the html form and store them in guide settings for
	// use in the next java script call.
	private void getFormFields() {
		try {
			String evaluateScript = "" + "var vforms = document.forms;" + "var vreturn = '';"
					+ "for (var formidx = 0; formidx < vforms.length; formidx++) {" + "var vform = vforms[formidx];"
					+ "for (var controlIdx = 0; controlIdx < vform.length; controlIdx++) {"
					+ "var control = vform.elements[controlIdx];" + "if (control.type === \"select-one\") {"
					+ "var item = control.selectedIndex;" + "var value = \"\";" + "if (item > -1)" + "{"
					+ "	value = control.item(item).value;" + "}"
					+ "vreturn = vreturn + control.name + '¬' + value + '¬' + control.type + '¬' + control.checked + '|';"
					+ "} else {"
					+ "vreturn = vreturn + control.name + '¬' + control.value + '¬' + control.type + '¬' + control.checked + '|';"
					+ "}" + "}" + "}" + "return vreturn;";
			String node = (String) brwsText.evaluate(evaluateScript);
			if (node == null) {
				return;
			}
			String fields[] = node.split("\\|");
			String values[];
			String name;
			String value;
			String type;
			String checked;
			for (int i = 0; i < fields.length; i++) {
				values = fields[i].split("¬");
				if (!fields[i].equals("")) {
					name = values[0];
					value = values[1];
					type = values[2];
					checked = values[3];
					if (type.equals("checkbox")) {
						guideSettings.setFormField(name, checked);
						guideSettings.setScriptVar(name, checked);
					}
					if (type.equals("radio")) {
						if (checked.equals("true")) {
							guideSettings.setFormField(name, value);
							guideSettings.setScriptVar(name, value);
						}
					}
					if (type.equals("text") || type.equals("textarea")) {
						guideSettings.setFormField(name, value);
						guideSettings.setScriptVar(name, value);
					}
					if (type.equals("file")) {
						guideSettings.setFormField(name, value);
						guideSettings.setScriptVar(name, value);
					}

					if (type.equals("select-one")) {
						guideSettings.setFormField(name, value);
						guideSettings.setScriptVar(name, value);
					}

					logger.trace(name + "|" + value + "|" + type + "|" + checked);
				}
			}
			String node2 = (String) imageLabel.evaluate(evaluateScript);
			fields = node2.split("\\|");
			for (int i = 0; i < fields.length; i++) {
				values = fields[i].split("¬");
				if (!fields[i].equals("")) {
					name = values[0];
					value = values[1];
					type = values[2];
					checked = values[3];
					if (type.equals("checkbox")) {
						guideSettings.setFormField(name, checked);
						guideSettings.setScriptVar(name, checked);
					}
					if (type.equals("radio")) {
						if (checked.equals("true")) {
							guideSettings.setFormField(name, value);
							guideSettings.setScriptVar(name, value);
						}
					}
					if (type.equals("text") || type.equals("textarea")) {
						guideSettings.setFormField(name, value);
						guideSettings.setScriptVar(name, value);
					}

					if (type.equals("file")) {
						guideSettings.setFormField(name, value);
						guideSettings.setScriptVar(name, value);
					}

					if (type.equals("select-one")) {
						guideSettings.setFormField(name, value);
						guideSettings.setScriptVar(name, value);
					}

					logger.trace(name + "|" + value + "|" + type + "|" + checked);
				}
			}
		} catch (Exception ex) {
			logger.error(" get form fields " + ex.getLocalizedMessage(), ex);
		}
	}

	// force a redisplay of the button are
	// set focus to the last button
	public void layoutButtons() {
		try {
			btnComp.layout();
			Control[] controls = this.btnComp.getChildren();
			if (controls.length > 0) {
				controls[0].setFocus();
			}
			controls = null;
		} catch (Exception ex) {
			logger.error(" layoutButtons " + ex.getLocalizedMessage(), ex);
		}
	}

	public void enableButton(String id) {
		try {
			com.snapps.swt.SquareButton button;
			button = buttons.get(id);
			if (button != null) {
				button.setEnabled(true);
			}
		} catch (Exception ex) {
			logger.error(" enable Button " + ex.getLocalizedMessage(), ex);
		}
	}

	public void changeButton(String id, boolean enable, String text, String target) {
		try {
			com.snapps.swt.SquareButton button;
			button = buttons.get(id);
			if (button != null) {
				button.setEnabled(enable);
				button.setText(text);
				button.setData("Target", target);
			}
		} catch (Exception ex) {
			logger.error(" change Button " + ex.getLocalizedMessage(), ex);
		}
	}

	public void disableButton(String id) {
		try {
			com.snapps.swt.SquareButton button;
			button = buttons.get(id);
			if (button != null) {
				button.setEnabled(false);
			}
		} catch (Exception ex) {
			logger.error(" disable Button " + ex.getLocalizedMessage(), ex);
		}
	}

	public void setMetronomeBPM(int metronomeBPM, int loops, int resolution, String Rhythm) {
		// run metronome on another thread
		try {
			logger.trace("setMetronomeBPM");
			if (appSettings.isMetronome()) {
				metronome.metronomeStart(metronomeBPM, appSettings.getMidiInstrument(), loops, resolution, Rhythm,
						appSettings.getMidiVolume());
			}
		} catch (Exception e) {
			logger.error(" setMetronomeBPM " + e.getLocalizedMessage(), e);
		}
	}

	public void displayPage(String target) {
		getFormFields();
		mainLogic.displayPage(target, false, guide, mainShell, appSettings, userSettings, guideSettings, debugShell);
	}

	public void stopMetronome() {
		metronome.metronomeStop();
	}

	public void stopAudio() {
		if (audioPlayer != null) {
			audioPlayer.audioStop();
		}
		audioPlayer = null;
		threadAudioPlayer = null;

		if (audioPlayer2 != null) {
			audioPlayer2.audioStop();
		}
		audioPlayer2 = null;
		threadAudioPlayer2 = null;
	}

	public void stopWebcam(boolean shellClosing) {
		if (webcamOn && webcamVisible) {
			panel.stop();
			webcamRoot.getContentPane().removeAll();
			webcamVisible = false;
		}
	}

	public void stopVideo(boolean shellClosing) {
		if (videoOn) {
			try {
				if (mediaPlayer != null) {
					videoLoops = 0;
					videoTarget = "";
					videoPlay = false;
					logger.debug("MainShell stopVideo " + mediaPlayer.media().info().mrl());
					if (mediaPlayer.media().info().mrl() != null && videoPlayed) {
						VideoStop videoStop = new VideoStop();
						videoStop.setMediaPlayer(mediaPlayer, shellClosing);
						Thread videoStopThread = new Thread(videoStop, "videoStop");
						videoStopThread.setName("videoStopThread");
						mediaPanel.setVisible(false);
						webcamPanel.setVisible(false);
						imageLabel.setVisible(true);
						leftFrame.layout(true);
						threadVideoPlayer = null;
						videoStopThread.start();
					}
				}
			} catch (Exception e) {
				logger.error(" stopVideo " + e.getLocalizedMessage(), e);
			}
		}
	}

	public void stopDelay() {
		try {
			calCountDown = null;
			if (!lblRight.isDisposed()) {
				lblRight.setText("");
			}
		} catch (Exception ex) {
			logger.error(" stopDelay " + ex.getLocalizedMessage(), ex);
		}
	}

	public void pauseAll() {
		pausedAt = Calendar.getInstance();

		if (audioPlayer != null) {
			audioPlayer.audioPause();
		}
		if (audioPlayer2 != null) {
			audioPlayer2.audioPause();
		}
		if (mediaPlayer != null && mediaPlayer.status().isPlaying()) {
			mediaPlayer.controls().pause();
		}
		if (metronome != null) {
			metronome.metronomePause();
		}
	}

	public void resumeAll() {
		Calendar resumedAt = Calendar.getInstance();
		int pausedForSeconds = (int) (resumedAt.getTimeInMillis() - pausedAt.getTimeInMillis()) / 1000;

		// Add to countdown timer
		if (calCountDown != null) {
			calCountDown.add(Calendar.SECOND, pausedForSeconds);
		}

		// Update any Timer entities
		for (Timer t : timer.values()) {
			Calendar endTime = t.getTimerEnd();
			endTime.add(Calendar.SECOND, pausedForSeconds);
			t.setTimerEnd(endTime);
		}

		// Resume media
		if (audioPlayer != null) {
			audioPlayer.audioResume();
		}
		if (audioPlayer2 != null) {
			audioPlayer2.audioResume();
		}
		if (mediaPlayer != null && mediaPlayer.status().isPlayable()) {
			mediaPlayer.controls().play();
		}
		if (metronome != null && metronome.isPaused()) {
			metronome.metronomeResume();
		}

		// Start shell timer
		myDisplay.timerExec(100, new shellTimer(this));
	}

	public void stopAll(boolean shellClosing) {
		try {
			hotKeys = new HashMap<String, com.snapps.swt.SquareButton>();
		} catch (Exception ex) {
			logger.error(" stophotKeys " + ex.getLocalizedMessage(), ex);
		}
		try {
			buttons = new HashMap<String, com.snapps.swt.SquareButton>();
		} catch (Exception ex) {
			logger.error(" stopbuttons " + ex.getLocalizedMessage(), ex);
		}
		try {
			stopDelay();
		} catch (Exception ex) {
			logger.error(" stopDelay " + ex.getLocalizedMessage(), ex);
		}
		try {
			stopMetronome();
		} catch (Exception ex) {
			logger.error(" stopMetronome " + ex.getLocalizedMessage(), ex);
		}
		try {
			stopAudio();
		} catch (Exception ex) {
			logger.error(" stopAudio " + ex.getLocalizedMessage(), ex);
		}
		try {
			stopVideo(shellClosing);
		} catch (Exception ex) {
			logger.error(" stopVideo " + ex.getLocalizedMessage(), ex);
		}
		try {
			stopWebcam(shellClosing);
		} catch (Exception ex) {
			logger.error(" stopWebcam " + ex.getLocalizedMessage(), ex);
		}
		try {
			timerReset();
		} catch (Exception ex) {
			logger.error(" timerReset " + ex.getLocalizedMessage(), ex);
		}
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setDefaultStyle() {
		this.style = defaultStyle;
	}

	public Shell getShell2() {
		return shell2;
	}

	public boolean getMultiMonitor() {
		return multiMonitor;
	}

	public Timer getTimer(String timKey) {
		return timer.get(timKey);
	}

	public void addTimer(Timer timer) {
		String tmrId = timer.getId();
		if (tmrId.equals("")) {
			tmrId = java.util.UUID.randomUUID().toString();
		}
		this.timer.put(tmrId, timer);
	}

	public int getTimerCount() {
		return timer.size();
	}

	private void timerReset() {
		timer = new HashMap<String, Timer>();
	}

	public void resetTimer(String id, int delay) {
		Timer objTimer = timer.get(id);
		Calendar timCountDown = Calendar.getInstance();
		timCountDown.add(Calendar.SECOND, delay);
		objTimer.setTimerEnd(timCountDown);
	}

	public void updateJConsole(String logText) {
		debugShell.updateJConsole(logText);
	}

	public void clearJConsole() {
		debugShell.clearJConsole();
	}

	public void refreshVars() {
		debugShell.refreshVars();
	}

	public GuideSettings getGuideSettings() {
		return guideSettings;

	}

	public void showDebug() {
		debugShell.showDebug();
	}

	public ContextFactory getContextFactory() {
		return factory;
	}

	private static VideoSurfaceAdapter getVideoSurfaceAdapter() {
		VideoSurfaceAdapter videoSurfaceAdapter;
		if (RuntimeUtil.isNix()) {
			videoSurfaceAdapter = new LinuxVideoSurfaceAdapter();
		} else if (RuntimeUtil.isWindows()) {
			videoSurfaceAdapter = new WindowsVideoSurfaceAdapter();
		} else if (RuntimeUtil.isMac()) {
			videoSurfaceAdapter = new OsxVideoSurfaceAdapter();
		} else {
			throw new RuntimeException(
					"Unable to create a media player - failed to detect a supported operating system");
		}
		return videoSurfaceAdapter;
	}

	public void setGuideSettings(GuideSettings guideSettings) {
		this.guideSettings = guideSettings;
	}

}