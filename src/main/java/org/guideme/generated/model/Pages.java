package org.guideme.generated.model;

import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.guideme.guideme.model.ModelConverters;
import org.apache.logging.log4j.LogManager;
public class Pages  {

	public Pages(XMLStreamReader reader) {
	}

	public Pages() {
	}

	public Element asXml(Document doc) {
		Element ans = doc.createElement("Pages");
		return ans;
	}
	public Pages(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("Pages")){
		logger.warn("Error reading state file. Expected element 'Pages', but got '{}'", n.getNodeName());
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
