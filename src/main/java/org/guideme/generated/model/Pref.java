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
public class Pref  {

	private String sortOrder = "";
	private String screen = "";
	private static final Logger LOGGER = LogManager.getLogger();
	private String type = "";
	private String value = "";
	private String key = "";

	public Pref(XMLStreamReader reader) {
		this.sortOrder = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "sortOrder","");
		this.screen = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "screen","");
		this.type = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "type","");
		this.value = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "value","");
		this.key = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "key","");
	}

	public Pref() {
		/* NOP */
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getKey() {
		return key;
	}
	public String getScreen() {
		return screen;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("pref");
		ans.setAttribute("sortOrder",ModelConverters.toString(sortOrder));
		ans.setAttribute("screen",ModelConverters.toString(screen));
		ans.setAttribute("type",ModelConverters.toString(type));
		ans.setAttribute("value",ModelConverters.toString(value));
		ans.setAttribute("key",ModelConverters.toString(key));
		return ans;
	}
	public String getValue() {
		return value;
	}
	public Pref(Node n) {
		if(!n.getNodeName().equals("pref")){
			LOGGER.warn("Error reading state file. Expected element 'pref', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
			case "sortOrder":
				sortOrder = ModelConverters.fromString(attrValue, sortOrder);
				break;
			case "screen":
				screen = ModelConverters.fromString(attrValue, screen);
				break;
			case "type":
				type = ModelConverters.fromString(attrValue, type);
				break;
			case "value":
				value = ModelConverters.fromString(attrValue, value);
				break;
			case "key":
				key = ModelConverters.fromString(attrValue, key);
				break;
				default:
				LOGGER.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	public void setScreen(String screen) {
		this.screen = screen;
	}
}
