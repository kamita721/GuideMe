package org.guideme.guideme.readers;

import java.io.IOException;

import org.apache.commons.lang.CharSetUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.generated.model.Page;
import org.guideme.generated.model.Text;
import org.guideme.guideme.model.Chapter;
import org.guideme.guideme.model.Guide;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MilovanaHtmlReader {
	private static final Logger LOGGER = LogManager.getLogger();

	public Guide loadFromUrl(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Guide guide = createFromDocument(doc);

			int pageNr = 1;

			while (!doc.select("#continue").isEmpty()) {
				String nextPage = "http://www.milovana.com/"
						+ doc.select("#continue").first().attr("href");
				pageNr += 1;

				doc = Jsoup.connect(nextPage).get();
				addPage(guide.getChapter("chapter-1"), doc, pageNr);
			}

			return guide;
		} catch (IOException err) {
			LOGGER.error(err);
			return null;
		}
	}

	private Guide createFromDocument(Document doc) {
		Guide guide = Guide.getGuide();

		readGeneralInformation(guide, doc);
		Chapter chapter = createChapter(guide);

		addPage(chapter, doc, 1);

		return guide;
	}

	@SuppressWarnings("unused")
	private void readGeneralInformation(Guide guide, Document doc) {
		// TODO
	}

	private static Chapter createChapter(Guide guide) {
		Chapter chapter = new Chapter("chapter-1");
		guide.addChapter(chapter);
		return chapter;
	}

	private static void addPage(Chapter chapter, Document doc, int pageNr) {

		Page page = new Page();
		page.setId(String.valueOf(pageNr));

		Elements elm = doc.select("#tease_content").select(".text");
		if (!elm.isEmpty()) {
			String text = elm.first().text().replace('\r', ' ').replace('\n', ' ');
			text = CharSetUtils.squeeze(text, " ");
			Text textToAdd = new Text();
			textToAdd.setText(text);
			page.addText(textToAdd);
		}

		chapter.addPage(page);
	}

}