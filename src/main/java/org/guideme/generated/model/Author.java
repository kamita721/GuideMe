package org.guideme.generated.model;

import javax.xml.stream.XMLStreamConstants;
import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.NamedNodeMap;
import org.guideme.guideme.model.ModelConverters;
import org.apache.logging.log4j.LogManager;
public class Author  {

	private String name = "";

	public Author(XMLStreamReader reader) throws XMLStreamException {
		final Logger logger = LogManager.getLogger();
		int depth = 1;
		while (depth > 0) {
			 int eventType = reader.next();
			 if (eventType == XMLStreamConstants.START_ELEMENT) {
				  depth++;
				  String tagName = reader.getName().getLocalPart();
				switch(tagName){
				case "Name":
					name = XMLReaderUtils.getStringContentOrDefault(reader, "");
					break;
					default:
			logger.warn("Unhandled tag '{}' at location \n{}", tagName, reader.getLocation());
					XMLReaderUtils.getStringContentUntilElementEnd(reader);
					break;
				}
				  eventType = reader.next();
				  if (XMLReaderUtils.isAtElementEnd(reader, "Settings")) {
					   return;
				  }
			 }
			 if (eventType == XMLStreamConstants.END_ELEMENT) {
				  depth--;
			 }
		}
	}

	public Author() {
	}

	public void setName(String name) {
		this.name = name;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Author");
		return ans;
	}
	public String getName() {
		return name;
	}
	public Author(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("Author")){
		logger.warn("Error reading state file. Expected element 'Author', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
				default:
			logger.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
		
		
		
		
	}
}
