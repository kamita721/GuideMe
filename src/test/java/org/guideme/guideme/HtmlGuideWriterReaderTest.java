package org.guideme.guideme;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.guideme.generated.model.Image;
import org.guideme.generated.model.Page;
import org.guideme.guideme.model.*;
import org.guideme.guideme.readers.*;
import org.guideme.guideme.writers.*;

@Ignore
public class HtmlGuideWriterReaderTest {

	private HtmlGuideWriter writer;
	private HtmlGuideReader reader;
	private Guide originalGuide;

	@Before
	public void setUp() {
		writer = new HtmlGuideWriter();
		reader = new HtmlGuideReader();

		originalGuide = Guide.getGuide();
	}

	@Test
	public void authorName() {
		originalGuide.setAuthorName("Name of the author");

		Guide guide = reader.loadFromString(writer.write(originalGuide));

		assertEquals(originalGuide.getAuthorName(), guide.getAuthorName());
	}

	@Test
	public void authorUrl() {
		originalGuide.setAuthorUrl("http://url.to/author");

		Guide guide = reader.loadFromString(writer.write(originalGuide));

		assertEquals(originalGuide.getAuthorUrl(), guide.getAuthorUrl());
	}

	@Test
	public void description() {
		originalGuide.setDescription("Short description.");

		Guide guide = reader.loadFromString(writer.write(originalGuide));

		assertEquals(originalGuide.getDescription(), guide.getDescription());
	}

	@Test
	public void originalUrl() {
		originalGuide.setOriginalUrl("http://url.to/original");

		Guide guide = reader.loadFromString(writer.write(originalGuide));

		assertEquals(originalGuide.getOriginalUrl(), guide.getOriginalUrl());
	}

	@Test
	public void title() {
		originalGuide.setTitle("Title of the guide");

		Guide guide = reader.loadFromString(writer.write(originalGuide));

		assertEquals(originalGuide.getTitle(), guide.getTitle());
	}

	@Test
	public void thumbnail() {
		Image img = new Image();
		img.setId("http://url.to/thumbnail.jpg");
		originalGuide.setThumbnail(img);

		Guide guide = reader.loadFromString(writer.write(originalGuide));

		assertNotNull(guide.getThumbnail());
		assertEquals(originalGuide.getThumbnail().getId(), guide.getThumbnail().getId());
		// assertEquals(originalGuide.getThumbnail().getWidth(),
		// guide.getThumbnail().getWidth());
		// assertEquals(originalGuide.getThumbnail().getHeight(),
		// guide.getThumbnail().getHeight());
		// assertEquals(originalGuide.getThumbnail().getMimeType(),
		// guide.getThumbnail().getMimeType());
	}

	@Test
	public void chapters() {
		originalGuide.addChapter(new Chapter("c-1"));
		originalGuide.addChapter(new Chapter("c-2"));

		Guide guide = reader.loadFromString(writer.write(originalGuide));

		assertNotNull(guide.getChapter("c-1"));
		assertNotNull(guide.getChapter("c-2"));
	}

	@Test
	public void pages() {
		Chapter first = new Chapter("c-1");
		Page p1 = new Page();
		p1.setId("start");
		Page p2 = new Page();
		p2.setId("p-2");

		first.addPage(p1);
		first.addPage(p2);
		originalGuide.addChapter(first);

		Guide guide = reader.loadFromString(writer.write(originalGuide));

		assertEquals(2, guide.getChapter("c-1").getPageCount());
	}

}
