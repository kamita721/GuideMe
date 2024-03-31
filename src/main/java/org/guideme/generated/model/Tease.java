package org.guideme.generated.model;

import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.guideme.guideme.model.ModelConverters;
import org.apache.logging.log4j.LogManager;
public class Tease  {

	private String minimumVersion = "";

	public Tease(XMLStreamReader reader) {
		this.minimumVersion = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "minimumVersion","");
	}

	public Tease() {
		/* NOP */
	}

	public String getMinimumVersion() {
		return minimumVersion;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Tease");
		ans.setAttribute("minimumVersion",ModelConverters.toString(minimumVersion));
		return ans;
	}
	public Tease(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("Tease")){
			logger.warn("Error reading state file. Expected element 'Tease', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			if(attrName.equals("minimumVersion")){
				minimumVersion = ModelConverters.fromString(attrValue, minimumVersion);
			} else {
				logger.warn("Unhandled attribute '{}'", attrName);
			}
		}
	}
	public void setMinimumVersion(String minimumVersion) {
		this.minimumVersion = minimumVersion;
	}
}
