package org.guideme.generated.model;

import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.guideme.guideme.model.ModelConverters;
import org.apache.logging.log4j.LogManager;
public class NOVALUE  {

	public NOVALUE(XMLStreamReader reader) {
	}

	public NOVALUE() {
	}

	public NOVALUE(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("NOVALUE")){
		logger.warn("Error reading state file. Expected element 'NOVALUE', but got '{}'", n.getNodeName());
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
	public Element asXml(Document doc) {
		Element ans = doc.createElement("NOVALUE");
		return ans;
	}
}
