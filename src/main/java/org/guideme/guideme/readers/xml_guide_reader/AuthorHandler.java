package org.guideme.guideme.readers.xml_guide_reader;

import javax.xml.stream.XMLStreamException;

import org.guideme.generated.model.Author;
import org.guideme.guideme.model.Guide;

public class AuthorHandler {
	private AuthorHandler() {
	}

	public static void handle(Author author, Guide guide) throws XMLStreamException {
		guide.setAuthorName(author.getName());
	}
}
