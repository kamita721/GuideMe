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
public class GlobalJavascript  {

	private static final Logger LOGGER = LogManager.getLogger();
	private String text = "";

	public GlobalJavascript(XMLStreamReader reader) throws XMLStreamException {
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public GlobalJavascript() {
		/* NOP */
	}

	public void setText(String text) {
		this.text = text;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("GlobalJavascript");
		ans.setAttribute("text",ModelConverters.toString(text));
		return ans;
	}
	public String getText() {
		return text;
	}
	public GlobalJavascript(Node n) {
		if(!n.getNodeName().equals("GlobalJavascript")){
			LOGGER.warn("Error reading state file. Expected element 'GlobalJavascript', but got '{}'", n.getNodeName());
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
}
