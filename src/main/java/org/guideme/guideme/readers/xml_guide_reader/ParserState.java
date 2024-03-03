package org.guideme.guideme.readers.xml_guide_reader;

import javax.xml.stream.XMLStreamReader;

import org.guideme.generated.model.Page;
import org.guideme.guideme.model.Chapter;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.ui.debug_shell.DebugShell;

public class ParserState {
	private final XMLStreamReader reader;
	private final Chapter chapter;
	private final GuideSettings guideSettings;
	private final Guide guide;
	private Page page;
	private final AppSettings appSettings;
	private final String presName;
	private final DebugShell debugShell;

	public ParserState(XMLStreamReader reader, Chapter chapter, GuideSettings guideSettings,
			Guide guide, Page page, AppSettings appSettings, String presName,
			DebugShell debugShell) {
		this.reader = reader;
		this.chapter = chapter;
		this.guideSettings = guideSettings;
		this.guide = guide;
		this.page = page;
		this.appSettings = appSettings;
		this.presName = presName;
		this.debugShell = debugShell;
	}

	public XMLStreamReader getReader() {
		return reader;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public GuideSettings getGuideSettings() {
		return guideSettings;
	}

	public Guide getGuide() {
		return guide;
	}

	public Page getPage() {
		return page;
	}
	
	public void setPage(Page page) {
		this.page = page;
	}

	public AppSettings getAppSettings() {
		return appSettings;
	}

	public String getPresName() {
		return presName;
	}

	public DebugShell getDebugShell() {
		return debugShell;
	}
}
