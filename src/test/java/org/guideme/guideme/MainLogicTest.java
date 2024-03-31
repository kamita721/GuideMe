package org.guideme.guideme;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.guideme.generated.model.Page;
import org.guideme.guideme.model.Chapter;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.settings.UserSettings;
import org.guideme.guideme.ui.debug_shell.DebugShell;
import org.guideme.guideme.ui.main_shell.MainShell;
import org.guideme.guidme.mock.AppSettingsMock;
import org.guideme.guidme.mock.MainShellMock;
import org.junit.Test;

public class MainLogicTest {
	private MainLogic mainLogic = MainLogic.getMainLogic();
	private Guide guide = Guide.getGuide();
	private MainShell mainShell = new MainShellMock(); 
	private DebugShell debugShell;
	private AppSettings appSettings = AppSettingsMock.getAppSettings();
	private UserSettings userSettings = UserSettings.getUserSettings();
	private ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private GuideSettings guideSettings;
	private String dataDirectory = "Teases";
	private boolean singlePage = false;
	private boolean allGuides = false;
	private boolean oneGuide = true;
	private boolean scriptedTest = false;
	
	@Test
	public void testDisplayPageStringBooleanGuideMainShellAppSettings() throws XMLStreamException, IOException {
		if (singlePage) {
			String guideFileName = "\\A tribute to Jurgita Valts.xml";
			String pageId = "page21";
			debugShell = DebugShell.getDebugShell(); 
			appSettings.setDataDirectory(dataDirectory);
			XmlGuideReader.loadXML(dataDirectory + guideFileName, guide, appSettings, debugShell);
			guideSettings = guide.getSettings();
			mainLogic.displayPage(pageId, false, guide, mainShell, appSettings, userSettings, guideSettings, debugShell);
		}
		assertTrue(true);
	}

	@Test
	public void testDisplayPageOneGuide() throws XMLStreamException, IOException {
		if (oneGuide) {
			appSettings.setDataDirectory(dataDirectory);
			appSettings.setPageSound(false); // Don't try to play sound, this can break testing on headless machines
			String guideId = "SWTPortTest";
			String guideFileName = "\\" + guideId + ".xml";
			guide.reset(guideId);
			debugShell = DebugShell.getDebugShell(); 
			XmlGuideReader.loadXML(dataDirectory + guideFileName, guide, appSettings, debugShell);
			guideSettings = guide.getSettings();
			for (Chapter chapter : guide.getChapterCollection()) {
				for (Page page : chapter.getPageCollection()) {
					mainLogic.displayPage(chapter.getId(), page.getId(), false, guide, mainShell, appSettings, userSettings, guideSettings, debugShell);		
				}
			}
		}
		assertTrue(true);
	}

		@Test
	public void testDisplayPageStringStringBooleanGuideMainShellAppSettings() throws XMLStreamException, IOException {
		if (allGuides) {
			appSettings.setDataDirectory(dataDirectory);
			debugShell = DebugShell.getDebugShell(); 
			File f = new File(dataDirectory);
			// wildcard filter class handles the filtering
			ComonFunctions.WildCardFileFilter WildCardfilter = comonFunctions.new WildCardFileFilter();
			WildCardfilter.setFilePatern("*.xml");
			if (f.isDirectory()) {
				// return a list of matching files
				File[] children = f.listFiles(WildCardfilter);
				for (File file : children) {
					guide.reset(file.getName().substring(0, file.getName().length() - 4));
					XmlGuideReader.loadXML(file.getAbsolutePath(), guide, appSettings, debugShell);
					guideSettings = guide.getSettings();
					for (Chapter chapter : guide.getChapterCollection()) {
						for (Page page : chapter.getPageCollection()) {
							mainLogic.displayPage(chapter.getId(), page.getId(), false, guide, mainShell, appSettings, userSettings, guideSettings, debugShell);		
						}
					}
				}
			}
		}
		assertTrue(true);
	}
	
	@Test
	public void scriptedTest() throws XMLStreamException {
		if (scriptedTest) {
			debugShell = DebugShell.getDebugShell(); 
			String script = "data\\pageScript.txt";
			appSettings.setDataDirectory(dataDirectory);
			 try {
				BufferedReader instructions = new BufferedReader(new FileReader(script));
				String guideFile = instructions.readLine();
				if (!guideFile.equals(null)) {
					guide.reset(guideFile);
					XmlGuideReader.loadXML(dataDirectory + "\\" + guideFile + ".xml", guide, appSettings, debugShell);
					guideSettings = guide.getSettings();
				}
				String instruction = instructions.readLine();
				String fields[];
				while (!(instruction == null)) {
					fields = instruction.split(",");
					mainLogic.displayPage(fields[0], fields[1], false, guide, mainShell, appSettings, userSettings, guideSettings, debugShell);
					instruction = instructions.readLine();
				}
				instructions.close();
			} catch (FileNotFoundException e) {
				fail("scriptedTest input file not found: " + script);
			} catch (IOException e) {
				fail("IO Error on script file " + script);
			}
		}
		assertTrue(true);
	}

}
