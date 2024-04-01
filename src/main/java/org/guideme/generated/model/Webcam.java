package org.guideme.generated.model;

import java.util.List;
import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.time.LocalTime;
import org.w3c.dom.NamedNodeMap;
import org.guideme.guideme.model.ModelConverters;
import org.apache.logging.log4j.LogManager;
public class Webcam implements Filterable  {

	private String ifSet = "";
	private LocalTime ifBefore;
	private static final Logger LOGGER = LogManager.getLogger();
	private String ifNotSet = "";
	private LocalTime ifAfter;

	public Webcam(XMLStreamReader reader) {
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
	}

	public Webcam() {
		/* NOP */
	}

	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Webcam");
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		return ans;
	}
	public Webcam(Node n) {
		if(!n.getNodeName().equals("Webcam")){
			LOGGER.warn("Error reading state file. Expected element 'Webcam', but got '{}'", n.getNodeName());
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
				default:
				LOGGER.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	
	@Override
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
}
