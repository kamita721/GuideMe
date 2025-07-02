package org.guideme.generated.model;

import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.apache.logging.log4j.LogManager;
public class Tdata  {

	private static final Logger LOGGER = LogManager.getLogger();

	public Tdata(XMLStreamReader reader) {
		/* NOP */
	}

	public Tdata() {
		/* NOP */
	}

	public Element asXml(Document doc) {
		return doc.createElement("tdata");
	}
	public Tdata(Node n) {
		if(!n.getNodeName().equals("tdata")){
			LOGGER.warn("Error reading state file. Expected element 'tdata', but got '{}'", n.getNodeName());
		}
	}
}
