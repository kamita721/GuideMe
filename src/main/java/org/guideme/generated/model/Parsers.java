package org.guideme.generated.model;

import org.guideme.guideme.model.Guide;
import org.guideme.guideme.util.XMLReaderUtils;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.readers.xml_guide_reader.PrefHandler;
import org.guideme.guideme.ui.debug_shell.DebugShell;
import java.io.IOException;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.readers.xml_guide_reader.AuthorHandler;
import org.guideme.guideme.readers.xml_guide_reader.SettingsHandler;
import org.guideme.guideme.readers.xml_guide_reader.TeaseHandler;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.model.Chapter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import org.guideme.guideme.readers.xml_guide_reader.ParserState;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
import org.apache.logging.log4j.LogManager;
public class Parsers  {

	private Parsers() {
		/* NOP */
	}

	public static void parseElement(ParserState parseState) throws XMLStreamException,IOException {
		final Logger LOGGER = LogManager.getLogger();
		final XMLStreamReader reader = parseState.getReader();
		final Chapter chapter = parseState.getChapter();
		final GuideSettings guideSettings = parseState.getGuideSettings();
		final Guide guide = parseState.getGuide();
		final Page page = parseState.getPage();
		final DebugShell debugShell = parseState.getDebugShell();
		final AppSettings appSettings = parseState.getAppSettings();
		final String presName = parseState.getPresName();
		final String tagName = reader.getName().getLocalPart();
		switch(tagName){
		case "Metronome":
			page.addMetronome(new Metronome(reader));
			break;
		case "Include":
			XmlGuideReader.handleInclude(new Include(reader), appSettings, guide, presName, chapter, debugShell);
			break;
		case "Page":
			parseState.setPage(new Page(reader));
			debugShell.addPagesCombo(parseState.getPage().getId());
			break;
		case "Image":
			page.addImage(new Image(reader));
			break;
		case "Tease":
			TeaseHandler.handle(new Tease(reader), chapter, guideSettings);
			break;
		case "Url":
			guide.setOriginalUrl(new Url(reader).getText());
			break;
		case "Delay":
			page.addDelay(new Delay(reader));
			break;
		case "tdata":
			XMLReaderUtils.consumeToEndOfElement(reader);
			break;
		case "GlobalButton":
			page.addGlobalButton(new GlobalButton(reader));
			break;
		case "Button":
			page.addButton(new BasicButton(reader));
			break;
		case "root":
			break;
		case "pref":
			PrefHandler.handle(new Pref(reader), guideSettings);
			break;
		case "Settings":
			SettingsHandler.handle(new Settings(reader), guide, guideSettings);
			break;
		case "CSS":
			guide.setCss(new CSS(reader).getText());
			break;
		case "WebcamButton":
			page.addWebcamButton(new WebcamButton(reader));
			break;
		case "LoadGuide":
			page.addLoadGuide(new LoadGuide(reader));
			break;
		case "Title":
			guide.setTitle(new Title(reader).getText());
			break;
		case "Webcam":
			page.addWebcam(new Webcam(reader));
			break;
		case "GlobalJavascript":
			guide.appendGlobaljScript(new GlobalJavascript(reader).getText());
			break;
		case "Text":
			page.addText(new Text(reader));
			break;
		case "javascript":
			page.setJScript(new Javascript(reader).getText());
			break;
		case "Pages":
			break;
		case "LeftText":
			page.addText(new LeftText(reader));
			break;
		case "Timer":
			page.addTimer(new Timer(reader));
			break;
		case "MediaDirectory":
			guide.setMediaDirectory(new MediaDirectory(reader).getText());
			break;
		case "Audio2":
			page.addAudio2(new Audio2(reader));
			break;
		case "Video":
			page.addVideo(new Video(reader));
			break;
		case "Author":
			AuthorHandler.handle(new Author(reader), guide);
			break;
		case "Audio":
			page.addAudio(new Audio1(reader));
			break;
		case "NOVALUE":
			break;
			default:
			LOGGER.warn("Unhandled tag '{}' at location \n{}", tagName, reader.getLocation());
			XMLReaderUtils.getStringContentUntilElementEnd(reader);
			break;
		}
	}
}
