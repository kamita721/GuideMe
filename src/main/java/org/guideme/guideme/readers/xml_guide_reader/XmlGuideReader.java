package org.guideme.guideme.readers.xml_guide_reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.generated.model.Include;
import org.guideme.generated.model.Page;
import org.guideme.generated.model.Parsers;
import org.guideme.generated.model.Text;
import org.guideme.guideme.model.*;
import org.guideme.guideme.readers.UnicodeBOMInputStream;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.ui.debug_shell.DebugShell;
import org.guideme.guideme.util.XMLReaderUtils;

public class XmlGuideReader {
	private static Logger LOGGER = LogManager.getLogger();
	private static ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();

	private XmlGuideReader() {
	}

	public static String loadXML(String xmlFileName, Guide guide, AppSettings appSettings,
			DebugShell debugShell) throws XMLStreamException, IOException {
		String strFlags;
		GuideSettings guideSettings;

		int intPos = xmlFileName.lastIndexOf(appSettings.getFileSeparator());
		int intPos2 = xmlFileName.lastIndexOf(".xml");
		String presName = xmlFileName.substring(intPos + 1, intPos2);
		guide.reset(presName);
		Map<String, Chapter> chapters = guide.getChapters();
		Chapter chapter = new Chapter("default");
		chapters.put("default", chapter);
		guideSettings = guide.getSettings();
		guideSettings.setPageSound(true);

		parseFile(xmlFileName, guide, presName, chapter, appSettings, debugShell);

		// Return to where we left off
		if (guideSettings.isForceStartPage()
				&& !guideSettings.getPage().equals("GuideMeVersionNotMet")) {
			guideSettings.setPage("start");
		}
		strFlags = guideSettings.getFlags();
		if (!strFlags.isEmpty()) {
			comonFunctions.setFlags(strFlags, guide.getFlags());
		}
		guide.setSettings(guideSettings);
		guide.getSettings().saveSettings();

		return guideSettings.getPage();
	}

	private static void parseFile(String xmlFileName, Guide guide, String presName, Chapter chapter,
			AppSettings appSettings, DebugShell debugShell) throws XMLStreamException, IOException {
		LOGGER.info("parseFile: {}", xmlFileName);
		GuideSettings guideSettings = guide.getSettings();

		Page page404 = new Page();
		page404.setId("GuideMe404Error");
		chapter.getPages().put(page404.getId(), page404);

		XMLStreamReader reader = null;

		try (FileInputStream fis = new FileInputStream(xmlFileName);
				UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(fis)) {
			ubis.skipBOM();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			reader = factory.createXMLStreamReader(ubis);
			parseGuideXML(reader, chapter, guideSettings, guide, appSettings, presName, debugShell);
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	private static void parseGuideXML(XMLStreamReader reader, Chapter chapter,
			GuideSettings guideSettings, Guide guide, AppSettings appSettings, String presName,
			DebugShell debugShell) throws XMLStreamException, IOException {
		Page dummyPage = new Page();
		dummyPage.setId("dummyPage");
		Text toAdd = new Text();
		toAdd.setText("If you are reading this, something went horribly wrong.");
		
		dummyPage.addText(toAdd);

		Page page = dummyPage;
		while (reader.hasNext()) {
			int eventType = reader.next();
			switch (eventType) {
			case XMLStreamConstants.START_DOCUMENT:
				break;
			case XMLStreamConstants.END_DOCUMENT:
				break;
			case XMLStreamConstants.START_ELEMENT:
				ParserState parserState = new ParserState(reader, chapter, guideSettings, guide,
						page, appSettings, presName, debugShell);
				Parsers.parseElement(parserState);
				page = parserState.getPage();
				break;
			case XMLStreamConstants.END_ELEMENT:
				if (reader.getName().getLocalPart().equals("Page")) {
					chapter = guide.getChapters().get("default");
					chapter.getPages().put(page.getId(), page);
					page = dummyPage;
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				String text = reader.getText();
				if (!text.isBlank()) {
					LOGGER.warn("Unhandled text in guide at \n{}", reader.getLocation());
				}
				break;
			case XMLStreamConstants.COMMENT:
				break;
			default:
				LOGGER.warn("Unandled event type '{}' while parsing guide XML.", eventType);
			}
		}
	}

	/*
	 * We take a localName and defaultValue to have the same type signature as
	 * XMLReaderUtils.getAttributeOrDefaultNoNS Doing this makes codegen easier.
	 */
	public static String processText(XMLStreamReader reader, String localName, String defaultValue)
			throws XMLStreamException {
		if (!defaultValue.isBlank() || !localName.equals("text")) {
			throw new NotImplementedException();
		}
		return processText(reader);
	}

	public static String processText(XMLStreamReader reader) throws XMLStreamException {
		String tagName = reader.getName().getLocalPart();
		StringBuilder text = new StringBuilder();
		ArrayList<String> tag = new ArrayList<>();
		boolean emptyTagTest = false;
		boolean finished = false;
		int tagCount = -1;
		int eventType2 = reader.next();
		while (true) {
			switch (eventType2) {
			case XMLStreamConstants.START_ELEMENT:
				if (emptyTagTest) {
					text.append('>');
				}
				emptyTagTest = true;
				tagCount++;
				if (tag.size() < tagCount + 1) {
					tag.add(reader.getName().toString());
				} else {
					tag.add(tagCount, reader.getName().toString());
				}
				text.append('<');
				text.append(tag.get(tagCount));
				for (int i = 0; i < reader.getAttributeCount(); i++) {
					text.append(' ');
					text.append(reader.getAttributeName(i));
					text.append("=\"");
					text.append(reader.getAttributeValue(i));
					text.append('"');
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				if (reader.getName().getLocalPart().equals(tagName)) {
					finished = true;
				} else {
					if (emptyTagTest) {
						text.append("/>");
					} else {
						text.append("</");
						text.append(tag.get(tagCount));
						text.append(">");
					}
					tagCount--;
					emptyTagTest = false;
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				if (emptyTagTest) {
					text.append(">");
				}
				emptyTagTest = false;
				text.append(reader.getText());
				break;
			default:
				LOGGER.warn("Unhandled event type {} while parsing guide XML.", eventType2);
			}
			if (finished) {
				break;
			}
			eventType2 = reader.next();
			if (XMLReaderUtils.isAtElementEnd(reader, tagName)) {
				break;
			}
		}
		return text.toString();
	}

	public static void handleInclude(Include incl, AppSettings appSettings, Guide guide,
			String presName, Chapter chapter, DebugShell debugShell)
			throws IOException, XMLStreamException {
		String incFileName;
		String dataDirectory;
		String prefix = "";
		String fileSeparator = appSettings.getFileSeparator();
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix
				+ comonFunctions.fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		String mediaDirectory = comonFunctions.fixSeparator(guide.getMediaDirectory(),
				fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;
		if (!dataDirectory.endsWith(fileSeparator)) {
			dataDirectory = dataDirectory + fileSeparator;
		}

		// Handle wild cards
		incFileName = comonFunctions.fixSeparator(incl.getFile(),
				fileSeparator);
		if (incFileName.toLowerCase().endsWith("*.js")) {
			ArrayList<String> filesList = new ArrayList<>();
			try (DirectoryStream<Path> dirStream = Files
					.newDirectoryStream(Paths.get(dataDirectory), incFileName)) {
				dirStream.forEach(path -> filesList.add(path.toString()));
			}
			for (String filePath : filesList) {
				includeJavaScript(guide, filePath);
			}
		} else if (incFileName.toLowerCase().endsWith("*.xml")) {
			ArrayList<String> filesList = new ArrayList<>();
			try (DirectoryStream<Path> dirStream = Files
					.newDirectoryStream(Paths.get(dataDirectory), incFileName)) {
				dirStream.forEach(path -> filesList.add(path.toString()));
			}
			for (String filePath : filesList) {
				parseFile(filePath, guide, presName, chapter, appSettings, debugShell);
			}
		} else {
			incFileName = dataDirectory + incFileName;
			if (incFileName.toLowerCase().endsWith(".js")) {
				includeJavaScript(guide, incFileName);
			} else {
				parseFile(incFileName, guide, presName, chapter, appSettings, debugShell);
			}
		}
		LOGGER.trace("loadXML {} include {}", presName, incFileName);

	}

	private static void includeJavaScript(Guide guide, String filePath) {
		String javascript = comonFunctions.readFile(filePath, StandardCharsets.UTF_8);
		String globalScript = guide.getGlobaljScript();
		javascript = globalScript.concat("\r\n").concat(javascript);
		guide.appendGlobaljScript(javascript);
	}

}
