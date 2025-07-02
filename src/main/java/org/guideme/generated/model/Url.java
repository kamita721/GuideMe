package org.guideme.generated.model;

import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.NamedNodeMap;
import org.guideme.guideme.model.ModelConverters;
import org.apache.logging.log4j.LogManager;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class Url  {

	private static final Logger LOGGER = LogManager.getLogger();
	private String text = "";

	public Url(XMLStreamReader reader) throws XMLStreamException {
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public Url() {
		/* NOP */
	}

	public Url(Node n) {
		if(!n.getNodeName().equals("Url")){
			LOGGER.warn("Error reading state file. Expected element 'Url', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			if(attrName.equals("text")){
				text = ModelConverters.fromString(attrValue, text);
			} else {
				LOGGER.warn("Unhandled attribute '{}'", attrName);
			}
		}
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Url");
		ans.setAttribute("text",ModelConverters.toString(text));
		return ans;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
