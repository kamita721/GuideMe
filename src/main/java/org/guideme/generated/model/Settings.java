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
public class Settings  {

	private boolean convertArgumentTypes = false;
	private boolean pageSound = true;
	private boolean forceStartPage = false;
	private boolean autoSetPageWhenSeen = true;

	public Settings(XMLStreamReader reader) throws XMLStreamException {
		final Logger logger = LogManager.getLogger();
		int depth = 1;
		while (depth > 0) {
			 int eventType = reader.next();
			 if (eventType == XMLStreamConstants.START_ELEMENT) {
				  depth++;
				  String tagName = reader.getName().getLocalPart();
				switch(tagName){
				case "PageSound":
					pageSound = XMLReaderUtils.getStringContentOrDefault(reader, true);
					break;
				case "ConvertArgumentTypes":
					convertArgumentTypes = XMLReaderUtils.getStringContentOrDefault(reader, false);
					break;
				case "ForceStartPage":
					forceStartPage = XMLReaderUtils.getStringContentOrDefault(reader, false);
					break;
				case "AutoSetPageWhenSeen":
					autoSetPageWhenSeen = XMLReaderUtils.getStringContentOrDefault(reader, true);
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

	public Settings() {
	}

	public void setConvertArgumentTypes(boolean convertArgumentTypes) {
		this.convertArgumentTypes = convertArgumentTypes;
	}
	public boolean getAutoSetPageWhenSeen() {
		return autoSetPageWhenSeen;
	}
	public void setAutoSetPageWhenSeen(boolean autoSetPageWhenSeen) {
		this.autoSetPageWhenSeen = autoSetPageWhenSeen;
	}
	public boolean getPageSound() {
		return pageSound;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Settings");
		return ans;
	}
	public boolean getForceStartPage() {
		return forceStartPage;
	}
	public boolean getConvertArgumentTypes() {
		return convertArgumentTypes;
	}
	public void setPageSound(boolean pageSound) {
		this.pageSound = pageSound;
	}
	public void setForceStartPage(boolean forceStartPage) {
		this.forceStartPage = forceStartPage;
	}
	public Settings(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("Settings")){
		logger.warn("Error reading state file. Expected element 'Settings', but got '{}'", n.getNodeName());
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
