package org.guideme.guideme.ui.main_shell;

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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.swing.JRootPane;

import org.eclipse.swt.widgets.*;
import org.guideme.guideme.MainLogic;
import org.guideme.guideme.model.Button;
import org.guideme.guideme.model.Chapter;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.model.Page;
import org.guideme.guideme.model.Timer;
import org.guideme.guideme.model.WebcamButton;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
import org.guideme.guideme.scripting.OverRide;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.settings.UserSettings;
import org.guideme.guideme.ui.AudioPlayer;
import org.guideme.guideme.ui.CompositeVideoSurface;
import org.guideme.guideme.ui.MetronomePlayer;
import org.guideme.guideme.ui.SwtEmbeddedMediaPlayer;
import org.guideme.guideme.ui.debug_shell.DebugShell;
import org.guideme.guideme.util.ImageManager;
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

	AppSettings appSettings;
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
	Guide guide = Guide.getGuide();
	GuideSettings guideSettings = guide.getSettings();
	UserSettings userSettings = null;
	Label lblLeft;
	private Label lblCentre;
	Label lblRight;
	Browser leftPaneBrowser;
	Label imageLabel;
	ImageManager imageManager;
	private Browser brwsText;
	SashForm sashform;
	SashForm sashform2;
	private Composite btnComp;
	Composite leftFrame;
	Calendar calCountDown = null;
	private Calendar pausedAt = null;
	Shell shell;
	Shell shell2;
	DebugShell debugShell;
	Display myDisplay;
	Font controlFont;
	Font buttonFont;
	Font timerFont;
	Composite mediaPanel;
	SwtEmbeddedMediaPlayer mediaPlayer;
	Composite webcamPanel;
	Webcam webcam;
	private WebcamPanel panel;
	private JRootPane webcamRoot;
	MainLogic mainLogic = MainLogic.getMainLogic();
	ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private SquareButton delayButton = null;
	MetronomePlayer metronome;
	AudioPlayer audioPlayer;
	Thread threadAudioPlayer;
	AudioPlayer audioPlayer2;
	Thread threadAudioPlayer2;
	private Thread threadVideoPlayer;
	String style = "";
	String defaultStyle = "";
	boolean imgOverRide = false;
	boolean multiMonitor = true;
	private boolean overlayTimer = false;
	HashMap<String, com.snapps.swt.SquareButton> hotKeys = new HashMap<>();
	private HashMap<String, com.snapps.swt.SquareButton> buttons = new HashMap<>();
	ShellKeyEventListener keyListener;
	boolean showMenu = true;
	Menu menuBar;
	HashMap<String, Timer> timer = new HashMap<>();
	boolean inPrefShell = false;
	private String rightHTML;
	String leftHTML;
	private ContextFactory factory;
	private boolean videoPlayed = false;
	String procStatusText = "";
	private boolean hasVideoDeferred = false;
	private boolean hasAudioDeferred = false;
	private boolean hasAudio2Deferred = false;
	boolean pauseRequested = false;

	public Shell createShell(final Display display) {
		ResourceBundle displayText;
		Rectangle clientArea;
		Rectangle clientArea2;
		Thread threadMetronome;
		Color colourWhite;
		Color colourBlack;
		comonFunctions.setDisplay(display);
		colourBlack = display.getSystemColor(SWT.COLOR_BLACK);
		colourWhite = display.getSystemColor(SWT.COLOR_WHITE);

		appSettings = AppSettings.getAppSettings();

		Locale locale = new Locale.Builder().setLanguage(appSettings.getLanguage())
				.setRegion(appSettings.getCountry()).build();
		displayText = ResourceBundle.getBundle("DisplayBundle", locale);
		appSettings.setDisplayText(displayText);

		// array to hold the various flags
		guideSettings.setFlags("");

		strGuidePath = appSettings.getDataDirectory();

		logger.trace("Get userSettings");

		userSettings = UserSettings.getUserSettings();

		// Create the main UI elements
		myDisplay = display;
		if (appSettings.isFullScreen()) {
			shell = new Shell(myDisplay, SWT.NO_TRIM);
		} else {
			shell = new Shell(myDisplay);
		}
		shell.addShellListener(new shellCloseListen(this));
		keyListener = new ShellKeyEventListener(this);
		myDisplay.addFilter(SWT.KeyDown, keyListener);
		int mainMonitor = appSettings.getMainMonitor();

		clientArea2 = null;
		multiMonitor = appSettings.isMultiMonitor();
		Monitor[] monitors = display.getMonitors();
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
		fD[0].setHeight(appSettings.getFontSize());
		controlFont = new Font(display, fD);
		fD[0].setHeight(appSettings.getFontSize());
		timerFont = new Font(display, fD);
		fD[0].setHeight(appSettings.getButtonFontSize());
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
		mediaPanel.setData("name", "mediaPanel");

		webcamPanel = new Composite(leftFrame, SWT.EMBEDDED);
		FormLayout layoutwebcam = new FormLayout();
		webcamPanel.setLayout(layoutwebcam);
		webcamPanel.setBackground(colourBlack);
		webcamPanel.setData("name", "webcamPanel");

		imageManager = new ImageManager(myDisplay);
		imageLabel = new Label(leftFrame, SWT.CENTER);
		imageLabel.setBackground(colourBlack);
		imageLabel.setData("name", "imageLabel");

		leftFrame.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				imageManager.setPreferedSize(leftFrame.getSize());
				imageManager.updateImageLabel(imageLabel);
				shell.layout(true);

			}
		});

		// defaultStyle

		defaultStyle = comonFunctions.readFile("./defaultCSS.TXT", StandardCharsets.UTF_8);
		defaultStyle = defaultStyle.replace("MintHtmlFontSize",
				String.valueOf(appSettings.getFontSize()));

		style = defaultStyle;

		// default HTML

		rightHTML = comonFunctions.readFile("DefaultRightHtml.txt", StandardCharsets.UTF_8);

		leftHTML = comonFunctions.readFile("DefaultLeftHtml.txt", StandardCharsets.UTF_8);

		String strHtml = rightHTML.replace("BodyContent", "");
		strHtml = strHtml.replace("DefaultStyle", defaultStyle);
		leftPaneBrowser = new Browser(leftFrame, SWT.WEBKIT);
		leftPaneBrowser.setJavascriptEnabled(true);
		leftPaneBrowser.setText(leftHTML);
		leftPaneBrowser.setBackground(colourBlack);
		leftPaneBrowser.addStatusTextListener(new EventStatusTextListener(this));
		leftPaneBrowser.setData("name", "leftPaneBrowswer");
		shell.pack();

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

		brwsText = new Browser(sashform2, SWT.NONE);
		brwsText.setText(strHtml);
		brwsText.setBackground(colourBlack);
		brwsText.addStatusTextListener(new EventStatusTextListener(this));

		btnComp = new Composite(sashform2, SWT.SHADOW_NONE);
		btnComp.setBackground(colourBlack);
		RowLayout layout2 = new RowLayout();
		btnComp.setLayout(layout2);

		if (appSettings.getVideoOn()) {
			logger.trace("Video Enter");
			libvlc_instance_t instance = LibVlc.libvlc_new(0, null);

			mediaPlayer = new SwtEmbeddedMediaPlayer(instance);
			mediaPlayer.setVideoSurface(
					new CompositeVideoSurface(mediaPanel, getVideoSurfaceAdapter()));
			mediaPlayer.events().addMediaPlayerEventListener(new MediaListener(this));

			String videoOutputDevice = appSettings.getVideoDevice();
			if (videoOutputDevice != null && !videoOutputDevice.equals("")) {
				mediaPlayer.audio().setOutputDevice(null, appSettings.getVideoDevice());
			}
			logger.trace("Video Exit");
		}

		if (appSettings.getWebcamOn()) {
			Frame frame = SWT_AWT.new_Frame(webcamPanel);

			webcamRoot = new JRootPane();
			frame.add(webcamRoot);
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
			FormData sashFormData = new FormData();
			sashFormData.top = new FormAttachment(lblLeft, 0);
			sashFormData.left = new FormAttachment(0, 0);
			sashFormData.right = new FormAttachment(100, 0);
			sashFormData.bottom = new FormAttachment(100, 0);
			sashform.setLayoutData(sashFormData);

			FormData sashFormData2 = new FormData();
			sashFormData2.top = new FormAttachment(sashform, 0);
			sashFormData2.left = new FormAttachment(sashform, 0);
			sashFormData2.right = new FormAttachment(sashform, 0);
			sashFormData2.bottom = new FormAttachment(sashform, 0);
			sashform2.setLayoutData(sashFormData2);
		} else {

			FormData leftFrameFormData = new FormData();
			leftFrameFormData.top = new FormAttachment(lblLeft, 0);
			leftFrameFormData.left = new FormAttachment(0, 0);
			leftFrameFormData.right = new FormAttachment(100, 0);
			leftFrameFormData.bottom = new FormAttachment(100, 0);
			leftFrame.setLayoutData(leftFrameFormData);

			FormData sashFormData2 = new FormData();
			sashFormData2.top = new FormAttachment(0, 0);
			sashFormData2.left = new FormAttachment(0, 0);
			sashFormData2.right = new FormAttachment(100, 0);
			sashFormData2.bottom = new FormAttachment(100, 0);
			sashform2.setLayoutData(sashFormData2);
		}

		FormData btnCompFormData = new FormData();
		btnCompFormData.top = new FormAttachment(sashform2, 0);
		btnCompFormData.left = new FormAttachment(sashform2, 0);
		btnCompFormData.right = new FormAttachment(sashform2, 0);
		btnCompFormData.bottom = new FormAttachment(sashform2, 0);
		btnComp.setLayoutData(btnCompFormData);

		FormData leftPaneElementFormData = new FormData();
		leftPaneElementFormData.top = new FormAttachment(0, 0);
		leftPaneElementFormData.left = new FormAttachment(0, 0);
		leftPaneElementFormData.right = new FormAttachment(100, 0);
		leftPaneElementFormData.bottom = new FormAttachment(100, 0);
		mediaPanel.setLayoutData(leftPaneElementFormData);
		webcamPanel.setLayoutData(leftPaneElementFormData);
		leftPaneBrowser.setLayoutData(leftPaneElementFormData);
		imageLabel.setLayoutData(leftPaneElementFormData);

		// Menu Bar
		menuBar = new Menu(shell, SWT.BAR);

		// Top Level File drop down
		MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
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
		fileExitItem.addListener(SWT.Selection, e -> shell.close());

		// Top Level Tools drop down
		MenuItem toolsItem = new MenuItem(menuBar, SWT.CASCADE);
		toolsItem.setText(displayText.getString("MainTools"));

		// Sub Menu for Tools
		Menu toolsSubMenu = new Menu(shell, SWT.DROP_DOWN);
		// Associate it with the top level File menu
		toolsItem.setMenu(toolsSubMenu);

		// Tools Compress menu item
		MenuItem compressGuideItem = new MenuItem(toolsSubMenu, SWT.PUSH);
		compressGuideItem.setText(displayText.getString("MainToolsCompress"));
		compressGuideItem.addSelectionListener(new CompressGuideListener(this));

		// Tools UnCompress menu item
		MenuItem unCompressGuideItem = new MenuItem(toolsSubMenu, SWT.PUSH);
		unCompressGuideItem.setText(displayText.getString("MainToolsUnCompress"));
		unCompressGuideItem.addSelectionListener(new UnCompressGuideListener(this));

		// Tools Resize menu item
		MenuItem resizeGuideItem = new MenuItem(toolsSubMenu, SWT.PUSH);
		resizeGuideItem.setText(displayText.getString("MainToolsResizeImage"));
		resizeGuideItem.addSelectionListener(new ResizeGuideListener(this));

		// good
		// Audio Output Menus
		MenuItem audioItem = new MenuItem(menuBar, SWT.CASCADE);
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
			MenuItem debugItem = new MenuItem(menuBar, SWT.CASCADE);
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
			jsdebugCheck.addListener(SWT.Selection,
					event -> appSettings.setJsDebug(jsdebugCheck.getSelection()));
			// Debug Javascript Debug Menu Error Item
			final MenuItem jsdebugErrorCheck = new MenuItem(debugSubMenu, SWT.CHECK);
			jsdebugErrorCheck.setText(displayText.getString("MainDebugException"));
			jsdebugErrorCheck.setSelection(appSettings.getJsDebugError());
			jsdebugErrorCheck.addListener(SWT.Selection,
					event -> appSettings.setJsDebugError(jsdebugErrorCheck.getSelection()));
			// Debug Javascript Debug Menu Enter Item
			final MenuItem jsdebugEnterCheck = new MenuItem(debugSubMenu, SWT.CHECK);
			jsdebugEnterCheck.setText(displayText.getString("MainDebugEnter"));
			jsdebugEnterCheck.setSelection(appSettings.getJsDebugEnter());
			jsdebugEnterCheck.addListener(SWT.Selection,
					event -> appSettings.setJsDebugEnter(jsdebugEnterCheck.getSelection()));

			// Debug Javascript Debug Menu Exit Item
			final MenuItem jsdebugExitCheck = new MenuItem(debugSubMenu, SWT.CHECK);
			jsdebugExitCheck.setText(displayText.getString("MainDebugExit"));
			jsdebugExitCheck.setSelection(appSettings.getJsDebugExit());
			jsdebugExitCheck.addListener(SWT.Selection,
					event -> appSettings.setJsDebugExit(jsdebugExitCheck.getSelection()));

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
		shell.setMenuBar(menuBar);

		// tell SWT to display the correct screen info
		shell.pack();
		shell.setMaximized(true);
		shell.setBounds(clientArea);
		if (!multiMonitor) {
			sashform.setWeights(appSettings.getSash1Weights());
		} else {
			shell2.pack();
			shell2.setMaximized(true);
			shell2.setBounds(clientArea2);
		}
		sashform2.setWeights(appSettings.getSash2Weights());
		// timer that updates the clock field and handles any timed events
		// when loading wait 2 seconds before running it
		myDisplay.timerExec(2000, new ShellTimer(this));
		metronome = new MetronomePlayer();
		threadMetronome = new Thread(metronome, "metronome");
		threadMetronome.setName("threadMetronome");
		threadMetronome.start();

		logger.trace("Exit createShell");
		if (!appSettings.getComandLineGuide().equals("")) {
			loadGuide(appSettings.getDataDirectory() + appSettings.getComandLineGuide());
		}
		displayPage(guideSettings.getCurrPage());
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
			if (getMultiMonitor()) {
				getShell2().open();
			}

			/*
			 * If monitorChanging is ever set to true, the below loop will terminate, and
			 * the outer loop will cycle back to here, at which point we are done responding
			 * to the new monitor settings.
			 */
			appSettings.setMonitorChanging(false);

			// loop round until the window is closed
			while (!shell.isDisposed()) {
				if (appSettings.isMonitorChanging()) {
					try {
						shell.close();
					} catch (Exception ex) {
						logger.error("Main shell close " + ex.getLocalizedMessage(), ex);
					}
				}
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} while (appSettings.isMonitorChanging());
	}

	// Load the tease
	public void loadGuide(String fileToLoad) {
		logger.trace("Loading Guide {}", fileToLoad);
		try {
			debugShell.clearPagesCombo();
		} catch (Exception ex) {
			logger.error("Clear debug pages " + ex.getLocalizedMessage(), ex);
		}
		try {
			String strPage = XmlGuideReader.loadXML(fileToLoad, guide, appSettings, debugShell);
			guideSettings = guide.getSettings();
			if (guide.getCss().equals("")) {
				style = defaultStyle;
			} else {
				style = guide.getCss();
			}
			// display the first page
			mainLogic.displayPage(strPage, false, guide, this, appSettings, userSettings,
					guideSettings, debugShell);
		} catch (Exception ex) {
			logger.error("Load Guide " + ex.getLocalizedMessage(), ex);
		}
	}

	static HashMap<String, Integer> getNewDimentions(double imageRatio, int labelHeight,
			int labelWidth, double imgOffSet, boolean maxScale, int maxHeight, int maxWidth) {
		HashMap<String, Integer> returnValue = new HashMap<>();
		double dblScreenRatio = (double) labelHeight / (double) labelWidth;
		int newHeight;
		int newWidth;
		if (((labelHeight > maxHeight) && (labelWidth > maxWidth)) && maxScale) {
			if (dblScreenRatio > imageRatio) {
				logger.trace("Scale Choice: 1.1");
				newHeight = (int) (((maxWidth) * imageRatio) * imgOffSet);
				newWidth = (int) ((maxWidth) * imgOffSet);
				logger.trace("New GT Dimentions: H: {} W: {}", newHeight, newWidth);
			} else {
				logger.trace("Scale Choice: 1.2");
				newHeight = (int) ((maxHeight) * imgOffSet);
				newWidth = (int) (((maxHeight) / imageRatio) * imgOffSet);
				logger.trace("New LT Dimentions: H: {} W: {}", newHeight, newWidth);
			}
		} else {
			if (dblScreenRatio > imageRatio) {
				logger.trace("Scale Choice: 2.1");
				newHeight = (int) ((labelWidth * imageRatio) * imgOffSet);
				newWidth = (int) (labelWidth * imgOffSet);
				logger.trace("New GT Dimentions: H: {} W: {}", newHeight, newWidth);
			} else {
				logger.trace("Scale Choice: 2.2");
				newHeight = (int) (labelHeight * imgOffSet);
				newWidth = (int) ((labelHeight / imageRatio) * imgOffSet);
				logger.trace("New LT Dimentions: H: {} W: {}", newHeight, newWidth);
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
		logger.trace("setImage: {}", imgPath);
		imgOverRide = false;
		imageManager.setCurrentImagePath(imgPath);
		imageManager.updateImageLabel(imageLabel);

		setLeftPaneVisibleElement(imageLabel);
	}

	public void setLeftPaneVisibleElement(Control c) {
		logger.trace(() -> "setLeftPaneVisible( " + c + " name = " + c.getData("name") + ")");
		Control[] elements = new Control[] { mediaPanel, webcamPanel, leftPaneBrowser, imageLabel };
		boolean found = false;
		for (Control e : elements) {
			if (e == c) {
				e.setVisible(true);
				found = true;
			} else {
				e.setVisible(false);
			}
		}
		if (!found) {
			logger.error("Invalid left pane element: {}", c);
		}
		leftFrame.layout(true);
	}

	public String getStyle() {
		return style;
	}

	public void setleftPaneHtml(String leftHtml) {
		try {
			logger.trace("setleftPaneHtml: {}", leftHtml);
			imgOverRide = true;
			leftPaneBrowser.setText(leftHtml, true);
			setLeftPaneVisibleElement(leftPaneBrowser);
		} catch (Exception ex) {
			logger.error("setleftPaneHtml error " + ex.getLocalizedMessage(), ex);
		}

	}

	public boolean showWebcam() {
		// displays the webcam in the area to the left of the screen
		if (appSettings.getWebcamOn()) {
			try {
				webcam = Webcam.getDefault();

				if (webcam != null) {
					setLeftText("", "");
					setLeftPaneVisibleElement(webcamPanel);

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
					setLeftText("No Webcam detected", "");
					Webcam.getDiscoveryService().stop();
				}

			} catch (Exception e) {
				logger.error("showWebcam " + e.getLocalizedMessage(), e);
			}
		}
		return webcam != null;
	}

	public void playVideo(String video, int startAt, int stopAt, int loops, String target,
			String jscript, String scriptVar, int volume, boolean deferStart) {
		// plays a video in the area to the left of the screen
		// sets the number of loops, start / stop time and any page to display
		// if the video finishes
		// starts the video using a non UI thread so VLC can't hang the
		// application
		if (appSettings.getVideoOn()) {
			try {
				setLeftText("", "");
				setLeftPaneVisibleElement(mediaPanel);
				leftFrame.layout(true);
				videoLoops = loops;
				videoTarget = target;
				videoStartAt = startAt;
				videoStopAt = stopAt;
				videoJscript = jscript;
				videoScriptVar = scriptVar;
				videoPlay = true;
				String mrlVideo = "file:///" + video;
				logger.debug("MainShell playVideo: {} videoLoops: {} videoTarget: videoPlay: {}",
						mrlVideo, videoLoops, videoTarget, videoPlay);
				VideoPlay videoPlayer = new VideoPlay(this);
				videoPlayer.setVideoPlay(mediaPlayer, mrlVideo, volume);
				threadVideoPlayer = new Thread(videoPlayer, "videoPlay");
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
		imageManager.setCurrentImagePath(null);
		imageManager.updateImageLabel(imageLabel);
	}

	public void playAudio(String audio, int startAt, int stopAt, int loops, String target,
			String jscript, String scriptVar, int volume, boolean deferStart) {
		// run audio on another thread
		try {
			if (audioPlayer != null) {
				audioPlayer.audioStop();
				logger.trace("playAudio audioStop");
			}
			String outputDevice = appSettings.getAudioOneDevice();
			audioPlayer = new AudioPlayer(audio, startAt, stopAt, loops, target, this, jscript,
					scriptVar, outputDevice, volume);
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

	public void playAudio2(String audio, int startAt, int stopAt, int loops, String target,
			String jscript, String scriptVar, int volume, boolean deferStart) {
		// run audio on another thread
		try {
			if (audioPlayer2 != null) {
				audioPlayer2.audioStop();
				logger.trace("playAudio2 audioStop");
			}
			String outputDevice = appSettings.getAudioTwoDevice();
			audioPlayer2 = new AudioPlayer(audio, startAt, stopAt, loops, target, this, jscript,
					scriptVar, outputDevice, volume);
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
				// copy text to clip board for use in TTS
				String htmlString = brwsText.replaceAll("\\<[^>]*\\>", " ");
				StringSelection stringSelection = new StringSelection(htmlString);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, stringSelection);

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
		// set HTML to be displayed in the browser control to the left of the screen
		if (overRideStyle.equals("")) {
			overRideStyle = style;
		}
		String strHTML;
		try {
			strHTML = leftHTML.replace("DefaultStyle", overRideStyle);
			strHTML = strHTML.replace("BodyContent", brwsText);
			this.leftPaneBrowser.setText(strHTML);
			if (appSettings.isToclipboard()) {
				// copy text to clip board for use in TTS
				String htmlString = brwsText.replaceAll("\\<[^>]*\\>", " ");
				StringSelection stringSelection = new StringSelection(htmlString);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, stringSelection);

			}
		} catch (Exception e1) {
			logger.error("displayPage Text Exception " + e1.getLocalizedMessage(), e1);
			strHTML = leftHTML.replace("DefaultStyle", overRideStyle);
			strHTML = strHTML.replace("BodyContent", "");
			this.leftPaneBrowser.setText(strHTML);
		}
		setLeftPaneVisibleElement(leftPaneBrowser);
	}

	public void setLeftHtml(String strHTML) {

		// set HTML to be displayed in the browser control to the left of the screen
		try {
			this.leftPaneBrowser.setText(strHTML);
		} catch (Exception e1) {
			logger.error("setLeftHtml Text Exception " + e1.getLocalizedMessage(), e1);
			strHTML = leftHTML.replace("DefaultStyle", style);
			strHTML = strHTML.replace("BodyContent", "");
			this.leftPaneBrowser.setText(strHTML);
		}
		setLeftPaneVisibleElement(leftPaneBrowser);

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
		boolean isWebCamButton = button instanceof WebcamButton;
		try {
			strBtnTarget = button.getTarget();
			strBtnText = button.getText();
			strBtnImage = button.getImage();
			if (!strBtnImage.equals("")) {
				String imgPath = comonFunctions.getMediaFullPath(strBtnImage,
						appSettings.getFileSeparator(), appSettings, guide);
				File flImage = new File(imgPath);
				if (flImage.exists()) {
					strBtnImage = imgPath;
				} else {
					strBtnImage = "";
				}
			}
			SquareButton btnDynamic = new SquareButton(btnComp, SWT.PUSH);

			int fntHeight = button.getFontHeight();

			if (button.getFontName().isBlank() && fntHeight == 0) {
				btnDynamic.setFont(buttonFont);
			} else {
				FontData[] fontData = buttonFont.getFontData();
				if (fntHeight > 0) {
					fontData[0].setHeight(fntHeight);
				}
				if (!button.getFontName().isBlank()) {
					fontData[0].setName(button.getFontName());
				}

				final Font newFont = new Font(myDisplay, fontData);
				btnDynamic.setFont(newFont);

				// Since you created the font, you must dispose it
				btnDynamic.addDisposeListener(event -> newFont.dispose());
			}
			Color bgColor1 = button.getbgColor1();
			Color bgColor2 = button.getbgColor2();
			Color fontColor = button.getfontColor();

			btnDynamic.setDefaultColors(bgColor1, bgColor2, btnDynamic.getBackground(), fontColor);
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
			logger.debug("displayPage Button Text {} Target {} Set {} UnSet {} ", strBtnText,
					strBtnTarget, strButtonSet, strButtonUnSet);

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
				if (webcamButton.get_type().equals("Capture")) {
					btnDynamic.addSelectionListener(new WebcamCaptureListener(this));
				}
			} else {
				btnDynamic.addSelectionListener(new DynamicButtonListner(this));
			}
			btnDynamic.setEnabled(!button.getDisabled());
		} catch (Exception e) {
			logger.error("addButton " + e.getLocalizedMessage(), e);
		}

	}

	public void runJavascript(String function, String pageJavascript, OverRide overRide,
			boolean pageLoading) {
		getFormFields();
		refreshVars();
		if (function == null)
			function = "";

		if (!function.equals("")) {
			guide.getJavascriptEngine().exec(userSettings, appSettings, pageLoading, null, overRide,
					pageJavascript, function, pageLoading);
		}
	}
	
	public void runJscript(String function, boolean pageLoading) {
		runJscript(function, null, pageLoading);
	}

	// run the javascript function passed
	public void runJscript(String function, OverRide overRide, boolean pageLoading) {
		Page objCurrPage = guide.getChapters().get(guideSettings.getChapter()).getPages()
				.get(guideSettings.getCurrPage());
		String pageJavascript = objCurrPage.getjScript();
		runJavascript(function, pageJavascript, overRide, pageLoading);
	}

	// run the javascript function passed
	public void runJscript(String function, String pageJavascript) {
		runJavascript(function, pageJavascript, null, false);
	}

	// get any fields from the html form and store them in guide settings for
	// use in the next java script call.
	private void getFormFields() {
		//@formatter:off
			String evaluateScript = "" 
					+ "var vforms = document.forms;" 
					+ "var vreturn = '';"
					+ "for (var formidx = 0; formidx < vforms.length; formidx++) {" 
						+ "var vform = vforms[formidx];"
						+ "for (var controlIdx = 0; controlIdx < vform.length; controlIdx++) {"
							+ "var control = vform.elements[controlIdx];" 
							+ "if (control.type === \"select-one\") {"
								+ "var item = control.selectedIndex;" 
								+ "var value = \"\";" 
								+ "if (item > -1)" + "{"
									+ "value = control.item(item).value;" 
								+ "}"
								+ "vreturn = vreturn + control.name + '¬' + value + '¬' + control.type + '¬' + control.checked + '|';"
							+ "} else {"
								+ "vreturn = vreturn + control.name + '¬' + control.value + '¬' + control.type + '¬' + control.checked + '|';"
							+ "}" 
						+ "}" 
					+ "}" 
					+ "return vreturn;";
			//@formatter:on
		String node = (String) brwsText.evaluate(evaluateScript);
		if (node == null) {
			return;
		}
		String[] fields = node.split("\\|");
		String[] values;
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
				if (type.equals("radio") && (checked.equals("true"))) {
					guideSettings.setFormField(name, value);
					guideSettings.setScriptVar(name, value);

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

				logger.trace("FormField: {}|{}|{}|{}", name, value, type, checked);
			}
		}
		String node2 = (String) leftPaneBrowser.evaluate(evaluateScript);
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
				if (type.equals("radio") && (checked.equals("true"))) {
					guideSettings.setFormField(name, value);
					guideSettings.setScriptVar(name, value);

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

				logger.trace("FormField: {}|{}|{}|{}", name, value, type, checked);
			}
		}
	}

	// force a redisplay of the button are
	// set focus to the last button
	public void layoutButtons() {
		btnComp.layout();
		Control[] controls = this.btnComp.getChildren();
		if (controls.length > 0) {
			controls[0].setFocus();
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

	public void setMetronomeBPM(int metronomeBPM, int loops, int resolution, String rhythm) {
		// run metronome on another thread

		logger.trace("setMetronomeBPM");
		if (appSettings.isMetronome()) {
			metronome.metronomeStart(metronomeBPM, appSettings.getMidiInstrument(), loops,
					resolution, rhythm, appSettings.getMidiVolume());
		}
	}

	public void displayPage(String target) {
		getFormFields();
		mainLogic.displayPage(target, false, guide, this, appSettings, userSettings, guideSettings,
				debugShell);
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

	public void stopWebcam() {
		if (appSettings.getWebcamOn() && webcamVisible) {
			panel.stop();
			webcamRoot.getContentPane().removeAll();
			webcamVisible = false;
		}
	}

	public void stopVideo(boolean shellClosing) {
		if (appSettings.getVideoOn() && mediaPlayer.media().info() != null) {
			try {
				videoLoops = 0;
				videoTarget = "";
				videoPlay = false;
				logger.debug(() -> "MainShell stopVideo " + mediaPlayer.media().info().mrl());
				if (mediaPlayer.media().info().mrl() != null && videoPlayed) {
					VideoStop videoStop = new VideoStop();
					videoStop.setMediaPlayer(mediaPlayer, shellClosing);
					Thread videoStopThread = new Thread(videoStop, "videoStop");
					videoStopThread.setName("videoStopThread");
					setLeftPaneVisibleElement(leftPaneBrowser);
					threadVideoPlayer = null;
					videoStopThread.start();

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
		int pausedForSeconds = (int) (resumedAt.getTimeInMillis() - pausedAt.getTimeInMillis())
				/ 1000;

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
		myDisplay.timerExec(100, new ShellTimer(this));
	}

	public void stopAll(boolean shellClosing) {
		try {
			hotKeys = new HashMap<>();
		} catch (Exception ex) {
			logger.error(" stophotKeys " + ex.getLocalizedMessage(), ex);
		}
		try {
			buttons = new HashMap<>();
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
			stopWebcam();
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
		timer = new HashMap<>();
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
	


	/*
	 * Begin "Uncooked" functions. These functions take their arguements directly from
	 * the model, and "cook" them into a more processed form before calling the underlying
	 * implementatin function.
	 */
	
	public void runJscriptUncooked(String function, boolean pageLoading) {
		if(function.isBlank()) {
			return;
		}
		runJscript(function, pageLoading);
	}
	
	public void gotoTargetUncooked(String target) {
		if(target.isBlank()) {
			return;
		}
		lblRight.setText("");
		mainLogic.displayPage(target, false, guide, this,
				appSettings, userSettings, guideSettings,
				debugShell);
	}
	
	public void setImageByUncooked(String imgId) {
		if(imgId.isBlank()) {
			return;
		}
		String imgPath = comonFunctions.getMediaFullPath(imgId,
				appSettings.getFileSeparator(), appSettings,
				guide);
		File flImage = new File(imgPath);
		if (flImage.exists()) {
			setImage(imgPath);
		}
	}
	
	public void setBrwsTextUncooked(String brwsTextUncooked) {
		if(brwsTextUncooked.isBlank()) {
			return;
		}
		// Media Directory
		String mediaPath;
		mediaPath = comonFunctions.getMediaFullPath("",
				appSettings.getFileSeparator(), appSettings,
				guide);
		String displayText = brwsTextUncooked.replace("\\MediaDir\\", mediaPath);

		displayText = comonFunctions.substituteTextVars(displayText,
				guideSettings, userSettings);

		setBrwsText(displayText, "");
	}
	
	public void setUnsetFlagsUncooked(String setFlags, String unSetFlags) {

		comonFunctions.setFlags(setFlags, guide.getFlags());
		comonFunctions.unsetFlags(unSetFlags,
				guide.getFlags());
	}

}