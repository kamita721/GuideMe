package org.guideme.guideme.readers;

import java.io.File;
import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.generated.model.Image;
import org.guideme.generated.model.Page;
import org.guideme.guideme.model.*;

public class HtmlGuideReader {
	private static final Logger LOGGER = LogManager.getLogger();
	
	public Guide loadFromFile(File file) {
		try {
			Document doc = Jsoup.parse(file, "UTF-8", "");
			return createFromDocument(doc);
		} catch (IOException err) {
			LOGGER.error(err);
			return null;
		}
	}
	
	public Guide loadFromString(String html) { 
		Document doc = Jsoup.parse(html, "");
		return createFromDocument(doc);
	}
	
	private static Guide createFromDocument(Document doc) {
		Guide guide = Guide.getGuide();
		
		readGeneralInformation(guide, doc);
		readChapters(guide, doc);
		
		return guide;
	}
	
	private static void readGeneralInformation(Guide guide, Document doc) {
		guide.setTitle(doc.select("head title").text());
		guide.setAuthorName(doc.select("head meta[name=author]").attr("content"));
		guide.setKeywordsString(doc.select("head meta[name=keywords]").attr("content"));
		guide.setDescription(doc.select("head meta[name=description]").attr("content"));
		if (!doc.select("head link[rel=alternate]").isEmpty()) {
			guide.setOriginalUrl(doc.select("head link[rel=alternate]").attr("href"));
		}
		if (!doc.select("head link[rel=author]").isEmpty()) {
			guide.setAuthorUrl(doc.select("head link[rel=author]").attr("href"));
		}
		if (!doc.select("head link[rel=icon]").isEmpty()) {
			String url = doc.select("head link[rel=icon]").attr("href");
			Image toAdd = new Image();
			toAdd.setId(url);
			guide.setThumbnail(toAdd);
		}
	}

	private static void readChapters(Guide guide, Document doc) {
		Elements articles = doc.select("body article");
		for (int i = 0; i < articles.size(); i++) {
			Element article = articles.get(i);
			Chapter chapter = new Chapter(articles.attr("id"));
			
			readPages(chapter, article);
			
			guide.addChapter(chapter);
		}
	}
	
	private static void readPages(Chapter chapter, Element articleElement) {
		Elements sections = articleElement.select("section");
		for (int i = 0; i < sections.size(); i++) {
			Element section = sections.get(i);
			
			//TODO need to add in the rest of the page stuff
			Page page = new Page();
			page.setId(section.attr("id"));
			
			chapter.addPage(page);
		}
		
	}
}
