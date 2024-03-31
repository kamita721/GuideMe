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
public class Include  {

	private String file = "";
	private static final Logger LOGGER = LogManager.getLogger();

	public Include(XMLStreamReader reader) {
		this.file = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "file","");
	}

	public Include() {
		/* NOP */
	}

	public Include(Node n) {
		if(!n.getNodeName().equals("Include")){
			LOGGER.warn("Error reading state file. Expected element 'Include', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			if(attrName.equals("file")){
				file = ModelConverters.fromString(attrValue, file);
			} else {
				LOGGER.warn("Unhandled attribute '{}'", attrName);
			}
		}
	}
	public void setFile(String file) {
		this.file = file;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Include");
		ans.setAttribute("file",ModelConverters.toString(file));
		return ans;
	}
	public String getFile() {
		return file;
	}
}
