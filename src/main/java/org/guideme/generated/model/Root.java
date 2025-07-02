package org.guideme.generated.model;

import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.apache.logging.log4j.LogManager;
public class Root  {

	private static final Logger LOGGER = LogManager.getLogger();

	public Root(XMLStreamReader reader) {
		/* NOP */
	}

	public Root() {
		/* NOP */
	}

	public Element asXml(Document doc) {
		return doc.createElement("root");
	}
	public Root(Node n) {
		if(!n.getNodeName().equals("root")){
			LOGGER.warn("Error reading state file. Expected element 'root', but got '{}'", n.getNodeName());
		}
	}
}
