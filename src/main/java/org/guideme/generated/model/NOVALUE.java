package org.guideme.generated.model;

import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.apache.logging.log4j.LogManager;
public class NOVALUE  {

	private static final Logger LOGGER = LogManager.getLogger();

	public NOVALUE(XMLStreamReader reader) {
		/* NOP */
	}

	public NOVALUE() {
		/* NOP */
	}

	public Element asXml(Document doc) {
		return doc.createElement("NOVALUE");
	}
	public NOVALUE(Node n) {
		if(!n.getNodeName().equals("NOVALUE")){
			LOGGER.warn("Error reading state file. Expected element 'NOVALUE', but got '{}'", n.getNodeName());
		}
	}
}
