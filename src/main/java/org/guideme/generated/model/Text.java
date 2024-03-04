package org.guideme.generated.model;

import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import org.guideme.guideme.model.ModelConverters;
import java.util.List;
import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.stream.XMLStreamException;
import java.time.LocalTime;
import org.w3c.dom.NamedNodeMap;
import org.apache.logging.log4j.LogManager;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class Text implements Filterable, IText  {

	private String ifSet = "";
	private LocalTime ifBefore;
	private String text = "";
	private LocalTime ifAfter;
	private String ifNotSet = "";

	public Text(XMLStreamReader reader) throws XMLStreamException {
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public Text() {
		/* NOP */
	}

	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public Text(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("Text")){
			logger.warn("Error reading state file. Expected element 'Text', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
			case "if-not-set":
				ifNotSet = ModelConverters.fromString(attrValue, ifNotSet);
				break;
			case "if-set":
				ifSet = ModelConverters.fromString(attrValue, ifSet);
				break;
			case "if-before":
				ifBefore = ModelConverters.fromString(attrValue, ifBefore);
				break;
			case "if-after":
				ifAfter = ModelConverters.fromString(attrValue, ifAfter);
				break;
			case "text":
				text = ModelConverters.fromString(attrValue, text);
				break;
				default:
				logger.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Text");
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("text",ModelConverters.toString(text));
		return ans;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getText() {
		return text;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
}
