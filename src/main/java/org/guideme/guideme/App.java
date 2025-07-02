package org.guideme.guideme;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;
import org.bridj.NativeLibrary;
import org.apache.logging.log4j.LogManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.ui.main_shell.MainShell;

import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

public class App {
	/*
	 * This is where it all starts from main will create and display the first shell
	 * (window)
	 */
	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) throws IOException {

		System.setProperty("org.eclipse.swt.browser.IEVersion", "11000");

		LOGGER.trace("Enter main");
		LOGGER.error("GuideMe Version - " + ComonFunctions.getVersion());
		// Sleak will help diagnose SWT memory leaks
		// if you set this to true you will get an additional window
		// that allows you to track resources that are created and not destroyed
		// correctly
		boolean loadSleak = false;
		if (args.length > 0 && args[0].equals("sleak")) {
			loadSleak = true;
		}

		AppSettings appSettings = AppSettings.getAppSettings();

		Path directory = Paths.get(appSettings.getTempDir());

		try (Stream<Path> st = Files.list(directory)) {
			st.filter(filePath -> {
				if (filePath.toFile().isFile()) {
					return filePath.toFile().getName().startsWith("tmpImage");
				} else {
					return false;
				}
			}).forEach(toDelete -> {
				try {
					java.nio.file.Files.delete(toDelete);
				} catch (IOException e) {
					LOGGER.error("Failed to delete file: " + toDelete, e);
				}
			});
		}

		if (args.length > 1) {
			appSettings.setDataDirectory(args[0]);
			appSettings.setComandLineGuide(args[1]);
		}

		Display display;
		// user debug setting
		if (appSettings.getDebug()) {
			Properties properties = java.lang.System.getProperties();
			Iterator<Object> it = properties.keySet().iterator();
			// display all the jvm properties in the log file
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				String value = String.valueOf(properties.get(key));
				// write out at error level even though it is a debug message
				// so we can turn it on, on a users machine
				LOGGER.error("{} - {}", key, value);
			}
		}

		if (loadSleak) {
			DeviceData data = new DeviceData();
			data.tracking = true;
			display = new Display(data);
			Sleak sleak = new Sleak();
			sleak.open();
		} else {
			display = new Display();
		}

		DisplayKeyEventListener keylistener = new DisplayKeyEventListener();
		display.addFilter(SWT.KeyDown, keylistener);
		
		NativeDiscovery nativeDiscovery = new NativeDiscovery();
		boolean vlcFound = nativeDiscovery.discover();
		LOGGER.trace("test for vlc: {}", vlcFound);

		appSettings.setMonitorChanging(false);
		LOGGER.trace("create main shell");
		MainShell mainShell = new MainShell();
		keylistener.setMainShell(mainShell);
		mainShell.run(display);
		display.dispose();

		LOGGER.trace("Exit main");
	}
}
