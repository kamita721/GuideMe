package org.guideme.generated.model;

import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import org.guideme.guideme.model.GlobalButtonPlacement;
import org.guideme.guideme.model.GlobalButtonAction;
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
public class GlobalButton implements FlagSet, Button, Filterable  {

	private String image = "";
	private String set = "";
	private boolean defaultBtn = true;
	private String jScript = "";
	private LocalTime ifBefore;
	private Color bgColor1 = ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_WHITE);
	private Color bgColor2 = this.bgColor1;
	private String unSet = "";
	private String ifNotSet = "";
	private String target = "";
	private String ifSet = "";
	private String fontName = "";
	private int fontHeight = 0;
	private int sortOrder = 1;
	private String hotkey = "";
	private String scriptVar = "";
	private GlobalButtonAction action = GlobalButtonAction.NONE;
	private boolean disabled = false;
	private static final Logger LOGGER = LogManager.getLogger();
	private String id = "";
	private GlobalButtonPlacement placement = GlobalButtonPlacement.BOTTOM;
	private String text = "";
	private Color fontColor = ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK);
	private LocalTime ifAfter;

	public GlobalButton(XMLStreamReader reader) throws XMLStreamException {
		this.bgColor2 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor2",this.bgColor1);
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.image = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "image","");
		this.fontColor = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontColor",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK));
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.action = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "action",GlobalButtonAction.NONE);
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.defaultBtn = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "default",true);
		this.fontName = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontName","");
		this.placement = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "placement",GlobalButtonPlacement.BOTTOM);
		this.jScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onclick","");
		this.sortOrder = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "sortOrder",1);
		this.fontHeight = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontHeight",0);
		this.hotkey = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "hotkey","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.disabled = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "disabled",false);
		this.text = XmlGuideReader.processText(reader, "text","");
		this.bgColor1 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor1",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_WHITE));
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
	}

	public GlobalButton() {
		/* NOP */
	}

	public GlobalButtonAction getAction() {
		return action;
	}
	@Override
	public String getJScript() {
		return jScript;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("GlobalButton");
		ans.setAttribute("bgColor2",ModelConverters.toString(bgColor2));
		ans.setAttribute("scriptvar",ModelConverters.toString(scriptVar));
		ans.setAttribute("image",ModelConverters.toString(image));
		ans.setAttribute("fontColor",ModelConverters.toString(fontColor));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("action",ModelConverters.toString(action));
		ans.setAttribute("id",ModelConverters.toString(id));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("default",ModelConverters.toString(defaultBtn));
		ans.setAttribute("fontName",ModelConverters.toString(fontName));
		ans.setAttribute("placement",ModelConverters.toString(placement));
		ans.setAttribute("onclick",ModelConverters.toString(jScript));
		ans.setAttribute("sortOrder",ModelConverters.toString(sortOrder));
		ans.setAttribute("fontHeight",ModelConverters.toString(fontHeight));
		ans.setAttribute("hotkey",ModelConverters.toString(hotkey));
		ans.setAttribute("target",ModelConverters.toString(target));
		ans.setAttribute("disabled",ModelConverters.toString(disabled));
		ans.setAttribute("text",ModelConverters.toString(text));
		ans.setAttribute("bgColor1",ModelConverters.toString(bgColor1));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		return ans;
	}
	@Override
	public String getFontName() {
		return fontName;
	}
	@Override
	public void setBgColor2(Color bgColor2) {
		this.bgColor2 = bgColor2;
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
	public boolean getDefaultBtn() {
		return defaultBtn;
	}
	@Override
	public Color getFontColor() {
		return fontColor;
	}
	@Override
	public Color getBgColor2() {
		return bgColor2;
	}
	@Override
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public void setPlacement(GlobalButtonPlacement placement) {
		this.placement = placement;
	}
	@Override
	public int getSortOrder() {
		return sortOrder;
	}
	public GlobalButtonPlacement getPlacement() {
		return placement;
	}
	@Override
	public void setSet(String set) {
		this.set = set;
	}
	@Override
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String getImage() {
		return image;
	}
	@Override
	public void setBgColor1(Color bgColor1) {
		this.bgColor1 = bgColor1;
	}
	public void setAction(GlobalButtonAction action) {
		this.action = action;
	}
	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	@Override
	public String getUnSet() {
		return unSet;
	}
	@Override
	public String getTarget() {
		return target;
	}
	@Override
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	@Override
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	@Override
	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}
	@Override
	public String getScriptVar() {
		return scriptVar;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public GlobalButton(Node n) {
		if(!n.getNodeName().equals("GlobalButton")){
			LOGGER.warn("Error reading state file. Expected element 'GlobalButton', but got '{}'", n.getNodeName());
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
			case "if-set":
				ifSet = ModelConverters.fromString(attrValue, ifSet);
				break;
			case "default":
				defaultBtn = ModelConverters.fromString(attrValue, defaultBtn);
				break;
			case "fontName":
				fontName = ModelConverters.fromString(attrValue, fontName);
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
			case "action":
				action = ModelConverters.fromString(attrValue, action);
				break;
			case "scriptvar":
				scriptVar = ModelConverters.fromString(attrValue, scriptVar);
				break;
			case "disabled":
				disabled = ModelConverters.fromString(attrValue, disabled);
				break;
			case "id":
				id = ModelConverters.fromString(attrValue, id);
				break;
			case "placement":
				placement = ModelConverters.fromString(attrValue, placement);
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
	@Override
	public String getIfSet() {
		return ifSet;
	}
	@Override
	public int getFontHeight() {
		return fontHeight;
	}
	@Override
	public String getText() {
		return text;
	}
	@Override
	public void setDefaultBtn(boolean defaultBtn) {
		this.defaultBtn = defaultBtn;
	}
	@Override
	public String getHotkey() {
		return hotkey;
	}
	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	@Override
	public String getSet() {
		return set;
	}
	@Override
	public void setHotkey(String hotkey) {
		this.hotkey = hotkey;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public Color getBgColor1() {
		return bgColor1;
	}
	@Override
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	@Override
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	@Override
	public String getId() {
		return id;
	}
	@Override
	public boolean getDisabled() {
		return disabled;
	}
	@Override
	public void setTarget(String target) {
		this.target = target;
	}
	@Override
	public void setImage(String image) {
		this.image = image;
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
