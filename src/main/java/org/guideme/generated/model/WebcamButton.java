package org.guideme.generated.model;

import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import org.guideme.guideme.model.ModelConverters;
import java.util.List;
import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.stream.XMLStreamException;
import org.eclipse.swt.SWT;
import java.time.LocalTime;
import org.w3c.dom.NamedNodeMap;
import org.apache.logging.log4j.LogManager;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class WebcamButton implements FlagSet, Button, Filterable  {

	private String image = "";
	private String set = "";
	private String destination = "";
	private boolean defaultBtn = true;
	private LocalTime ifBefore;
	private String jScript = "";
	private Color bgColor1 = ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_WHITE);
	private String type = "Capture";
	private Color bgColor2 = this.bgColor1;
	private String unSet = "";
	private String ifNotSet = "";
	private String target = "";
	private String fontName = "";
	private String ifSet = "";
	private int fontHeight = 0;
	private int sortOrder = 1;
	private String hotkey = "";
	private String scriptVar = "";
	private boolean disabled = false;
	private String id = "";
	private String text = "";
	private Color fontColor = ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK);
	private LocalTime ifAfter;

	public WebcamButton(XMLStreamReader reader) throws XMLStreamException {
		this.bgColor1 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor1",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_WHITE));
		this.defaultBtn = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "default",true);
		this.disabled = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "disabled",false);
		this.destination = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "file","");
		this.fontColor = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontColor",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK));
		this.fontHeight = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontHeight",0);
		this.fontName = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontName","");
		this.hotkey = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "hotkey","");
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.image = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "image","");
		this.jScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onclick","");
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.sortOrder = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "sortOrder",1);
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.type = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "type","Capture");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.bgColor2 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor2",this.bgColor1);
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public WebcamButton() {
		/* NOP */
	}

	@Override
	public void setSet(String set) {
		this.set = set;
	}
	@Override
	public void setText(String text) {
		this.text = text;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("WebcamButton");
		ans.setAttribute("bgColor1",ModelConverters.toString(bgColor1));
		ans.setAttribute("default",ModelConverters.toString(defaultBtn));
		ans.setAttribute("disabled",ModelConverters.toString(disabled));
		ans.setAttribute("file",ModelConverters.toString(destination));
		ans.setAttribute("fontColor",ModelConverters.toString(fontColor));
		ans.setAttribute("fontHeight",ModelConverters.toString(fontHeight));
		ans.setAttribute("fontName",ModelConverters.toString(fontName));
		ans.setAttribute("hotkey",ModelConverters.toString(hotkey));
		ans.setAttribute("id",ModelConverters.toString(id));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("image",ModelConverters.toString(image));
		ans.setAttribute("onclick",ModelConverters.toString(jScript));
		ans.setAttribute("scriptvar",ModelConverters.toString(scriptVar));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("sortOrder",ModelConverters.toString(sortOrder));
		ans.setAttribute("target",ModelConverters.toString(target));
		ans.setAttribute("type",ModelConverters.toString(type));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		ans.setAttribute("bgColor2",ModelConverters.toString(bgColor2));
		ans.setAttribute("text",ModelConverters.toString(text));
		return ans;
	}
	@Override
	public void setTarget(String target) {
		this.target = target;
	}
	@Override
	public Color getBgColor1() {
		return bgColor1;
	}
	@Override
	public void setHotkey(String hotkey) {
		this.hotkey = hotkey;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	@Override
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	@Override
	public Color getFontColor() {
		return fontColor;
	}
	@Override
	public String getText() {
		return text;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	public String getDestination() {
		return destination;
	}
	@Override
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	@Override
	public String getSet() {
		return set;
	}
	public String getType() {
		return type;
	}
	@Override
	public void setImage(String image) {
		this.image = image;
	}
	@Override
	public boolean getDisabled() {
		return disabled;
	}
	@Override
	public String getImage() {
		return image;
	}
	@Override
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	@Override
	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}
	public WebcamButton(Node n) {
		Logger LOGGER = LogManager.getLogger();
		if(!n.getNodeName().equals("WebcamButton")){
			LOGGER.warn("Error reading state file. Expected element 'WebcamButton', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
			case "image":
				image = ModelConverters.fromString(attrValue, image);
				break;
			case "set":
				set = ModelConverters.fromString(attrValue, set);
				break;
			case "if-after":
				ifAfter = ModelConverters.fromString(attrValue, ifAfter);
				break;
			case "onclick":
				jScript = ModelConverters.fromString(attrValue, jScript);
				break;
			case "bgColor1":
				bgColor1 = ModelConverters.fromString(attrValue, bgColor1);
				break;
			case "type":
				type = ModelConverters.fromString(attrValue, type);
				break;
			case "bgColor2":
				bgColor2 = ModelConverters.fromString(attrValue, bgColor2);
				break;
			case "unSet":
				unSet = ModelConverters.fromString(attrValue, unSet);
				break;
			case "target":
				target = ModelConverters.fromString(attrValue, target);
				break;
			case "if-not-set":
				ifNotSet = ModelConverters.fromString(attrValue, ifNotSet);
				break;
			case "default":
				defaultBtn = ModelConverters.fromString(attrValue, defaultBtn);
				break;
			case "fontName":
				fontName = ModelConverters.fromString(attrValue, fontName);
				break;
			case "if-set":
				ifSet = ModelConverters.fromString(attrValue, ifSet);
				break;
			case "file":
				destination = ModelConverters.fromString(attrValue, destination);
				break;
			case "if-before":
				ifBefore = ModelConverters.fromString(attrValue, ifBefore);
				break;
			case "fontHeight":
				fontHeight = ModelConverters.fromString(attrValue, fontHeight);
				break;
			case "sortOrder":
				sortOrder = ModelConverters.fromString(attrValue, sortOrder);
				break;
			case "hotkey":
				hotkey = ModelConverters.fromString(attrValue, hotkey);
				break;
			case "disabled":
				disabled = ModelConverters.fromString(attrValue, disabled);
				break;
			case "scriptvar":
				scriptVar = ModelConverters.fromString(attrValue, scriptVar);
				break;
			case "id":
				id = ModelConverters.fromString(attrValue, id);
				break;
			case "text":
				text = ModelConverters.fromString(attrValue, text);
				break;
			case "fontColor":
				fontColor = ModelConverters.fromString(attrValue, fontColor);
				break;
				default:
				LOGGER.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public Color getBgColor2() {
		return bgColor2;
	}
	@Override
	public void setDefaultBtn(boolean defaultBtn) {
		this.defaultBtn = defaultBtn;
	}
	@Override
	public int getFontHeight() {
		return fontHeight;
	}
	@Override
	public void setBgColor2(Color bgColor2) {
		this.bgColor2 = bgColor2;
	}
	@Override
	public boolean getDefaultBtn() {
		return defaultBtn;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	@Override
	public String getJScript() {
		return jScript;
	}
	@Override
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	@Override
	public String getHotkey() {
		return hotkey;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	@Override
	public void setBgColor1(Color bgColor1) {
		this.bgColor1 = bgColor1;
	}
	@Override
	public String getScriptVar() {
		return scriptVar;
	}
	@Override
	public String getFontName() {
		return fontName;
	}
	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	@Override
	public String getTarget() {
		return target;
	}
	@Override
	public int getSortOrder() {
		return sortOrder;
	}
	@Override
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	@Override
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	@Override
	public String getId() {
		return id;
	}
	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	@Override
	public String getUnSet() {
		return unSet;
	}
	
	@Override
	public void setUnSet(List<String> setList) {
		ComonFunctions.getComonFunctions().setFlags(set, setList);
		ComonFunctions.getComonFunctions().unsetFlags(unSet, setList);
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
