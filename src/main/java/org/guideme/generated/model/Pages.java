package org.guideme.generated.model;

import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.apache.logging.log4j.LogManager;
public class Pages  {

	private static final Logger LOGGER = LogManager.getLogger();

	public Pages(XMLStreamReader reader) {
		/* NOP */
	}

	public Pages() {
		/* NOP */
	}

	public Element asXml(Document doc) {
		return doc.createElement("Pages");
	}
	public Pages(Node n) {
		if(!n.getNodeName().equals("Pages")){
			LOGGER.warn("Error reading state file. Expected element 'Pages', but got '{}'", n.getNodeName());
		}
	}
}
