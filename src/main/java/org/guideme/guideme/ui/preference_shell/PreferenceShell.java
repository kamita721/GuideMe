package org.guideme.guideme.ui.preference_shell;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;

import com.snapps.swt.SquareButton;

public class PreferenceShell {
	private Shell shell = null;

	private AppSettings myAppSettings;
	private static final Logger LOGGER = LogManager.getLogger();
	private Font controlFont;
	private HashMap<String, FormData> appFormdata = new HashMap<>();
	private HashMap<String, Control> appWidgets = new HashMap<>();
	private boolean isFullScreen;
	private boolean isMultiMonitor;
	private int mainMonitor;

	public PreferenceShell() {
		super();
	}

	public Shell createShell(final Display display, AppSettings appSettings) {
		Display myDisplay;
		LOGGER.trace("Enter createShell");
		// Create the main UI elements
		myDisplay = display;
		myAppSettings = appSettings;
		ResourceBundle displayText = appSettings.getDisplayText();

		shell = new Shell(myDisplay, SWT.APPLICATION_MODAL + SWT.DIALOG_TRIM + SWT.RESIZE);

		shell.setText(displayText.getString("FileAppPrefTitle"));
		FormLayout layout = new FormLayout();
		shell.setLayout(layout);
		Font sysFont = display.getSystemFont();
		FontData[] fD = sysFont.getFontData();
		fD[0].setHeight(appSettings.getFontSize());
		controlFont = new Font(display, fD);

		ScrolledComposite sc = new ScrolledComposite(shell,
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		FormData scFormData = new FormData();
		scFormData.top = new FormAttachment(0, 5);
		scFormData.left = new FormAttachment(0, 5);
		scFormData.right = new FormAttachment(100, -5);
		scFormData.bottom = new FormAttachment(100, -5);
		sc.setLayoutData(scFormData);

		Composite composite = new Composite(sc, SWT.NONE);
		composite.setLayout(new FormLayout());

		// App Settings Group
		Group grpApp = new Group(composite, SWT.SHADOW_IN);
		FormData grpAppFormData = new FormData();
		grpAppFormData.top = new FormAttachment(0, 5);
		grpAppFormData.left = new FormAttachment(0, 5);
		grpAppFormData.right = new FormAttachment(100, -5);
		grpApp.setLayoutData(grpAppFormData);
		grpApp.setText("Application (" + ComonFunctions.getVersion() + ")");
		FormLayout layout5 = new FormLayout();
		grpApp.setLayout(layout5);

		// Font Size
		addTextField(grpApp, displayText.getString("FileAppPrefFontSize"), grpApp, grpApp,
				String.valueOf(myAppSettings.getFontSize()), "AppFontSize", true);

		// HTML Font Size
		addTextField(grpApp, displayText.getString("FileAppPrefHTMLFontSize"),
				appWidgets.get("AppFontSizeNumCtrl"), appWidgets.get("AppFontSizeNumCtrl"),
				String.valueOf(myAppSettings.getHtmlFontSize()), "AppHtmlFontSize", true);

		// Timer Font Size
		addTextField(grpApp, displayText.getString("FileAppPrefTimerFontSize"),
				appWidgets.get("AppHtmlFontSizeNumCtrl"), appWidgets.get("AppHtmlFontSizeNumCtrl"),
				String.valueOf(myAppSettings.getTimerFontSize()), "AppTimerFontSize", true);

		// Button Font Size
		addTextField(grpApp, displayText.getString("FileAppPrefButtonFontSize"),
				appWidgets.get("AppTimerFontSizeNumCtrl"),
				appWidgets.get("AppTimerFontSizeNumCtrl"),
				String.valueOf(myAppSettings.getButtonFontSize()), "AppButtonFontSize", true);

		// Image Scale
		addTextField(grpApp, displayText.getString("FileAppPrefImageScale"),
				appWidgets.get("AppButtonFontSizeNumCtrl"),
				appWidgets.get("AppButtonFontSizeNumCtrl"),
				String.valueOf(myAppSettings.getMaxImageScale()), "AppImageScale", true);

		// Language
		addTextField(grpApp, displayText.getString("FileAppPrefLanguage"),
				appWidgets.get("AppImageScaleNumCtrl"), appWidgets.get("AppImageScaleNumCtrl"),
				String.valueOf(myAppSettings.getLanguage()), "AppLanguage", false);

		// Country
		addTextField(grpApp, displayText.getString("FileAppPrefCountry"),
				appWidgets.get("AppLanguageCtrl"), appWidgets.get("AppLanguageCtrl"),
				String.valueOf(myAppSettings.getCountry()), "AppCountry", false);

		// Debug
		addBooleanField(grpApp, displayText.getString("FileAppPrefDebug"),
				appWidgets.get("AppCountryCtrl"), appWidgets.get("AppCountryCtrl"),
				myAppSettings.getDebug(), "AppDebug");

		// Javascript Debug
		addBooleanField(grpApp, displayText.getString("FileAppPrefJsDug"),
				appWidgets.get("AppDebugBlnCtrl"), appWidgets.get("AppDebugBlnCtrl"),
				myAppSettings.getJsDebug(), "AppJsDebug");

		// Javascript window height
		addTextField(grpApp, displayText.getString("FileAppPrefJsHeight"),
				appWidgets.get("AppJsDebugBlnCtrl"), appWidgets.get("AppJsDebugBlnCtrl"),
				String.valueOf(myAppSettings.getJsDebugHeight()), "AppJsDebugHeight", true);

		// Javascript window width
		addTextField(grpApp, displayText.getString("FileAppPrefJsWidth"),
				appWidgets.get("AppJsDebugHeightNumCtrl"),
				appWidgets.get("AppJsDebugHeightNumCtrl"),
				String.valueOf(myAppSettings.getJsDebugWidth()), "AppJsDebugWidth", true);

		// Video
		addBooleanField(grpApp, displayText.getString("FileAppPrefVideo"),
				appWidgets.get("AppJsDebugWidthNumCtrl"), appWidgets.get("AppJsDebugWidthNumCtrl"),
				myAppSettings.getVideoOn(), "AppVideo");

		// Webcam
		addBooleanField(grpApp, displayText.getString("FileAppPrefWebcam"),
				appWidgets.get("AppVideoBlnCtrl"), appWidgets.get("AppVideoBlnCtrl"),
				myAppSettings.getWebcamOn(), "AppWebcam");

		// Main Monitor
		addTextField(grpApp, displayText.getString("FileAppPrefMainMonitor"),
				appWidgets.get("AppWebcamBlnCtrl"), appWidgets.get("AppWebcamBlnCtrl"),
				String.valueOf(myAppSettings.getMainMonitor()), "AppMainMonitor", true);
		mainMonitor = myAppSettings.getMainMonitor();

		// Full Screen
		addBooleanField(grpApp, displayText.getString("FileAppPrefFullScreen"),
				appWidgets.get("AppMainMonitorNumCtrl"), appWidgets.get("AppMainMonitorNumCtrl"),
				myAppSettings.isFullScreen(), "AppFullScreen");
		isFullScreen = myAppSettings.isFullScreen();

		// Multi Monitor
		addBooleanField(grpApp, displayText.getString("FileAppPrefDualMonitor"),
				appWidgets.get("AppFullScreenBlnCtrl"), appWidgets.get("AppFullScreenBlnCtrl"),
				myAppSettings.isMultiMonitor(), "AppMultiMonitor");
		isMultiMonitor = myAppSettings.isMultiMonitor();

		// Clock
		addBooleanField(grpApp, displayText.getString("FileAppPrefClock"),
				appWidgets.get("AppMultiMonitorBlnCtrl"), appWidgets.get("AppMultiMonitorBlnCtrl"),
				myAppSettings.isClock(), "AppClock");

		// Metronome
		addBooleanField(grpApp, displayText.getString("FileAppPrefMetronome"),
				appWidgets.get("AppClockBlnCtrl"), appWidgets.get("AppClockBlnCtrl"),
				myAppSettings.isMetronome(), "AppMetronome");

		// Page Sound
		addBooleanField(grpApp, displayText.getString("FileAppPrefPageSound"),
				appWidgets.get("AppMetronomeBlnCtrl"), appWidgets.get("AppMetronomeBlnCtrl"),
				myAppSettings.isPageSound(), "AppPageSound");

		// To Clipboard (used for TTS)
		addBooleanField(grpApp, displayText.getString("FileAppPrefTTSClipboard"),
				appWidgets.get("AppPageSoundBlnCtrl"), appWidgets.get("AppPageSoundBlnCtrl"),
				myAppSettings.isToclipboard(), "AppToClipboard");

		// Store state in data directory
		addBooleanField(grpApp, displayText.getString("FileAppPrefDataDirState"),
				appWidgets.get("AppToClipboardBlnCtrl"), appWidgets.get("AppToClipboardBlnCtrl"),
				myAppSettings.isStateInDataDir(), "AppStateInDataDir");

		// Ask for confirmation on reloading or restarting guide
		addBooleanField(grpApp, displayText.getString("FileAppPrefFileActionConfirmations"),
				appWidgets.get("AppStateInDataDirBlnCtrl"),
				appWidgets.get("AppStateInDataDirBlnCtrl"),
				myAppSettings.isFileActionConfirmations(), "FileActionConfirmations");

		// midiInstrument
		addTextField(grpApp, displayText.getString("FileAppPrefMidiInstrument"),
				appWidgets.get("FileActionConfirmationsBlnCtrl"),
				appWidgets.get("FileActionConfirmationsBlnCtrl"),
				String.valueOf(myAppSettings.getMidiInstrument()), "AppMidiInstrument", true);

		// midiVolume
		addTextField(grpApp, displayText.getString("FileAppPrefMidiVol"),
				appWidgets.get("AppMidiInstrumentNumCtrl"),
				appWidgets.get("AppMidiInstrumentNumCtrl"),
				String.valueOf(myAppSettings.getMidiVolume()), "AppMidiVolume", true);

		// Music Volume
		addTextField(grpApp, displayText.getString("FileAppPrefMusicVol"),
				appWidgets.get("AppMidiVolumeNumCtrl"), appWidgets.get("AppMidiVolumeNumCtrl"),
				String.valueOf(myAppSettings.getMusicVolume()), "AppMusicVolume", true);

		// Video Volume
		addTextField(grpApp, displayText.getString("FileAppPrefVideoVol"),
				appWidgets.get("AppMusicVolumeNumCtrl"), appWidgets.get("AppMusicVolumeNumCtrl"),
				String.valueOf(myAppSettings.getVideoVolume()), "AppVideoVolume", true);

		// Thumb Size for the Library Screen
		addTextField(grpApp, displayText.getString("FileAppPrefThumbSize"),
				appWidgets.get("AppVideoVolumeNumCtrl"), appWidgets.get("AppVideoVolumeNumCtrl"),
				String.valueOf(myAppSettings.getThumbnailSize()), "AppThumbnailSize", true);

		// Image Scale Factor (hack to cope with some screens / graphic cards not
		// displaying pictures the same size
		addTextField(grpApp, displayText.getString("FileAppPrefImageScaleFactor"),
				appWidgets.get("AppThumbnailSizeNumCtrl"),
				appWidgets.get("AppThumbnailSizeNumCtrl"),
				String.valueOf(myAppSettings.getImgOffset()), "AppImgOffset", true);

		SquareButton btnCancel = new SquareButton(composite, SWT.PUSH);
		btnCancel.setText(displayText.getString("ButtonCancel"));
		btnCancel.setFont(controlFont);
		FormData btnCancelFormData = new FormData();
		btnCancelFormData.top = new FormAttachment(grpApp, 5);
		btnCancelFormData.right = new FormAttachment(100, -5);
		btnCancel.setLayoutData(btnCancelFormData);
		btnCancel.addSelectionListener(new CancelButtonListener());

		SquareButton btnSave = new SquareButton(composite, SWT.PUSH);
		btnSave.setText(displayText.getString("ButtonSave"));
		btnSave.setFont(controlFont);
		FormData btnSaveFormData = new FormData();
		btnSaveFormData.top = new FormAttachment(grpApp, 5);
		btnSaveFormData.right = new FormAttachment(btnCancel, -5);
		btnSave.setLayoutData(btnSaveFormData);
		btnSave.addSelectionListener(new SaveButtonListener());

		sc.setContent(composite);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		shell.addShellListener(new ShellCloseListen());

		shell.layout();

		LOGGER.trace("Exit createShell");
		return shell;
	}

	class VerifyDoubleListener implements VerifyListener {

		@Override
		public void verifyText(VerifyEvent event) {
			Text text = (Text) event.widget;

			// get old text and create new text by using the VerifyEvent.text
			final String previous = text.getText();
			String edited = previous.substring(0, event.start) + event.text
					+ previous.substring(event.end);

			boolean isDouble = true;
			try {
				Double.parseDouble(edited);
			} catch (NumberFormatException ex) {
				isDouble = false;
			}

			if (!isDouble)
				event.doit = false;
		}

	}

	// Click event code for the dynamic buttons
	class SaveButtonListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent event) {
			LOGGER.trace("Enter SaveButtonListner");
			Text txtTmp;
			Button btnTmp;

			txtTmp = (Text) appWidgets.get("AppFontSizeNumCtrl");
			myAppSettings.setFontSize(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppHtmlFontSizeNumCtrl");
			myAppSettings.setHtmlFontSize(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppTimerFontSizeNumCtrl");
			myAppSettings.setTimerFontSize(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppButtonFontSizeNumCtrl");
			myAppSettings.setButtonFontSize(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppImageScaleNumCtrl");
			myAppSettings.setMaxImageScale(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppLanguageCtrl");
			myAppSettings.setLanguage(txtTmp.getText());

			txtTmp = (Text) appWidgets.get("AppCountryCtrl");
			myAppSettings.setCountry(txtTmp.getText());

			btnTmp = (Button) appWidgets.get("AppDebugBlnCtrl");
			myAppSettings.setDebug(btnTmp.getSelection());

			btnTmp = (Button) appWidgets.get("AppJsDebugBlnCtrl");
			myAppSettings.setJsDebug(btnTmp.getSelection());

			txtTmp = (Text) appWidgets.get("AppJsDebugHeightNumCtrl");
			myAppSettings.setJsDebugHeight(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppJsDebugWidthNumCtrl");
			myAppSettings.setJsDebugWidth(Integer.parseInt(txtTmp.getText()));

			btnTmp = (Button) appWidgets.get("AppVideoBlnCtrl");
			myAppSettings.setVideoOn(btnTmp.getSelection());

			btnTmp = (Button) appWidgets.get("AppWebcamBlnCtrl");
			myAppSettings.setWebcamOn(btnTmp.getSelection());

			txtTmp = (Text) appWidgets.get("AppMainMonitorNumCtrl");
			myAppSettings.setMainMonitor(Integer.parseInt(txtTmp.getText()));
			if (mainMonitor != myAppSettings.getMainMonitor()) {
				myAppSettings.setMonitorChanging(true);
			}

			btnTmp = (Button) appWidgets.get("AppFullScreenBlnCtrl");
			myAppSettings.setFullScreen(btnTmp.getSelection());
			if (isFullScreen != myAppSettings.isFullScreen()) {
				myAppSettings.setMonitorChanging(true);
			}

			btnTmp = (Button) appWidgets.get("AppMultiMonitorBlnCtrl");
			myAppSettings.setMultiMonitor(btnTmp.getSelection());
			if (isMultiMonitor != myAppSettings.isMultiMonitor()) {
				myAppSettings.setMonitorChanging(true);
			}

			btnTmp = (Button) appWidgets.get("AppClockBlnCtrl");
			myAppSettings.setClock(btnTmp.getSelection());

			btnTmp = (Button) appWidgets.get("AppMetronomeBlnCtrl");
			myAppSettings.setMetronome(btnTmp.getSelection());

			btnTmp = (Button) appWidgets.get("AppPageSoundBlnCtrl");
			myAppSettings.setPageSound(btnTmp.getSelection());

			btnTmp = (Button) appWidgets.get("AppToClipboardBlnCtrl");
			myAppSettings.setToclipboard(btnTmp.getSelection());

			btnTmp = (Button) appWidgets.get("AppStateInDataDirBlnCtrl");
			myAppSettings.setStateInDataDir(btnTmp.getSelection());

			btnTmp = (Button) appWidgets.get("FileActionConfirmationsBlnCtrl");
			myAppSettings.setFileActionConfirmations(btnTmp.getSelection());

			txtTmp = (Text) appWidgets.get("AppMidiInstrumentNumCtrl");
			myAppSettings.setMidiInstrument(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppMidiVolumeNumCtrl");
			myAppSettings.setMidiVolume(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppMusicVolumeNumCtrl");
			myAppSettings.setMusicVolume(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppVideoVolumeNumCtrl");
			myAppSettings.setVideoVolume(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppThumbnailSizeNumCtrl");
			myAppSettings.setThumbnailSize(Integer.parseInt(txtTmp.getText()));

			txtTmp = (Text) appWidgets.get("AppImgOffsetNumCtrl");
			myAppSettings.setImgOffset(Double.parseDouble(txtTmp.getText()));

			Locale locale = new Locale.Builder().setLanguage(myAppSettings.getLanguage())
					.setRegion(myAppSettings.getCountry()).build();
			ResourceBundle displayText = ResourceBundle.getBundle("DisplayBundle", locale);
			myAppSettings.setDisplayText(displayText);

			myAppSettings.saveSettings();
			shell.close();
			LOGGER.trace("Exit SaveButtonListner");
		}
	}

	// Click event code for the dynamic buttons
	class CancelButtonListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent event) {
			LOGGER.trace("Enter CancelButtonListener");
			shell.close();
			LOGGER.trace("Exit CancelButtonListener");
		}
	}

	private void addTextField(Group group, String labelText, Control prevControl,
			Control prevControl2, String value, String key, boolean addNewmeric) {
		Label lblTmp;
		Text txtTmp;
		String lblSufix;
		String ctrlSufix;
		FormData lblTmpFormData;
		FormData txtTmpFormData;

		lblTmp = new Label(group, SWT.LEFT);
		lblTmp.setText(labelText);
		lblTmp.setFont(controlFont);
		lblTmpFormData = new FormData();
		lblTmpFormData.top = new FormAttachment(prevControl, 5);
		lblTmpFormData.left = new FormAttachment(0, 5);
		lblTmp.setLayoutData(lblTmpFormData);
		txtTmp = new Text(group, SWT.SINGLE);
		txtTmp.setFont(controlFont);
		txtTmp.setText(value);
		txtTmpFormData = new FormData();
		txtTmpFormData.top = new FormAttachment(prevControl2, 5);
		txtTmpFormData.left = new FormAttachment(50, 5);
		txtTmpFormData.right = new FormAttachment(100, -5);
		txtTmp.setLayoutData(txtTmpFormData);
		if (addNewmeric) {
			txtTmp.addVerifyListener(new VerifyDoubleListener());
			lblSufix = "NumLbl";
			ctrlSufix = "NumCtrl";
		} else {
			lblSufix = "Lbl";
			ctrlSufix = "Ctrl";
		}
		appFormdata.put(key + lblSufix, lblTmpFormData);
		appFormdata.put(key + ctrlSufix, txtTmpFormData);
		appWidgets.put(key + lblSufix, lblTmp);
		appWidgets.put(key + ctrlSufix, txtTmp);
	}

	private void addBooleanField(Group group, String labelText, Control prevControl,
			Control prevControl2, boolean value, String key) {
		Label lblTmp;
		Button btnTmp;
		FormData lblTmpFormData;
		FormData txtTmpFormData;

		lblTmp = new Label(group, SWT.LEFT);
		lblTmp.setText(labelText);
		lblTmp.setFont(controlFont);
		lblTmpFormData = new FormData();
		lblTmpFormData.top = new FormAttachment(prevControl, 5);
		lblTmpFormData.left = new FormAttachment(0, 5);
		lblTmp.setLayoutData(lblTmpFormData);
		btnTmp = new Button(group, SWT.CHECK);
		btnTmp.setFont(controlFont);
		btnTmp.setText("");
		btnTmp.setSelection(value);
		txtTmpFormData = new FormData();
		txtTmpFormData.top = new FormAttachment(prevControl2, 5);
		txtTmpFormData.left = new FormAttachment(50, 5);
		txtTmpFormData.right = new FormAttachment(100, -5);
		btnTmp.setLayoutData(txtTmpFormData);
		appFormdata.put(key + "BlnLbl", lblTmpFormData);
		appFormdata.put(key + "BlnCtrl", txtTmpFormData);
		appWidgets.put(key + "BlnLbl", lblTmp);
		appWidgets.put(key + "BlnCtrl", btnTmp);
	}

	class ShellCloseListen extends ShellAdapter {
		// Clean up stuff when the application closes
		@Override
		public void shellClosed(ShellEvent e) {
			controlFont.dispose();
			super.shellClosed(e);
		}

	}

}
