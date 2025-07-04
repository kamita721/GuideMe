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
	private String target = "";
	private String ifNotSet = "";
	private String fontName = "";
	private String ifSet = "";
	private int fontHeight = 0;
	private int sortOrder = 1;
	private String hotkey = "";
	private GlobalButtonAction action = GlobalButtonAction.NONE;
	private String scriptVar = "";
	private boolean disabled = false;
	private static final Logger LOGGER = LogManager.getLogger();
	private GlobalButtonPlacement placement = GlobalButtonPlacement.BOTTOM;
	private String id = "";
	private String text = "";
	private Color fontColor = ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK);
	private LocalTime ifAfter;

	public GlobalButton(XMLStreamReader reader) throws XMLStreamException {
		this.image = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "image","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.jScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onclick","");
		this.bgColor1 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor1",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_WHITE));
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.fontName = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontName","");
		this.defaultBtn = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "default",true);
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.fontHeight = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontHeight",0);
		this.sortOrder = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "sortOrder",1);
		this.hotkey = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "hotkey","");
		this.action = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "action",GlobalButtonAction.NONE);
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.disabled = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "disabled",false);
		this.placement = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "placement",GlobalButtonPlacement.BOTTOM);
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
		this.fontColor = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontColor",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK));
		this.bgColor2 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor2",this.bgColor1);
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public GlobalButton() {
		/* NOP */
	}

	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	@Override
	public void setSet(String set) {
		this.set = set;
	}
	@Override
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	@Override
	public int getFontHeight() {
		return fontHeight;
	}
	public void setAction(GlobalButtonAction action) {
		this.action = action;
	}
	@Override
	public void setHotkey(String hotkey) {
		this.hotkey = hotkey;
	}
	@Override
	public Color getBgColor1() {
		return bgColor1;
	}
	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	@Override
	public void setDefaultBtn(boolean defaultBtn) {
		this.defaultBtn = defaultBtn;
	}
	@Override
	public String getId() {
		return id;
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
			case "fontName":
				fontName = ModelConverters.fromString(attrValue, fontName);
				break;
			case "default":
				defaultBtn = ModelConverters.fromString(attrValue, defaultBtn);
				break;
			case "if-set":
				ifSet = ModelConverters.fromString(attrValue, ifSet);
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
			case "placement":
				placement = ModelConverters.fromString(attrValue, placement);
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
	@Override
	public void setTarget(String target) {
		this.target = target;
	}
	@Override
	public boolean getDefaultBtn() {
		return defaultBtn;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	@Override
	public Color getFontColor() {
		return fontColor;
	}
	@Override
	public String getScriptVar() {
		return scriptVar;
	}
	@Override
	public boolean getDisabled() {
		return disabled;
	}
	@Override
	public String getSet() {
		return set;
	}
	@Override
	public String getUnSet() {
		return unSet;
	}
	@Override
	public String getHotkey() {
		return hotkey;
	}
	public GlobalButtonPlacement getPlacement() {
		return placement;
	}
	@Override
	public Color getBgColor2() {
		return bgColor2;
	}
	@Override
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	@Override
	public String getText() {
		return text;
	}
	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	@Override
	public String getFontName() {
		return fontName;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	@Override
	public int getSortOrder() {
		return sortOrder;
	}
	@Override
	public String getJScript() {
		return jScript;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("GlobalButton");
		ans.setAttribute("image",ModelConverters.toString(image));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("onclick",ModelConverters.toString(jScript));
		ans.setAttribute("bgColor1",ModelConverters.toString(bgColor1));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		ans.setAttribute("target",ModelConverters.toString(target));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("fontName",ModelConverters.toString(fontName));
		ans.setAttribute("default",ModelConverters.toString(defaultBtn));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("fontHeight",ModelConverters.toString(fontHeight));
		ans.setAttribute("sortOrder",ModelConverters.toString(sortOrder));
		ans.setAttribute("hotkey",ModelConverters.toString(hotkey));
		ans.setAttribute("action",ModelConverters.toString(action));
		ans.setAttribute("scriptvar",ModelConverters.toString(scriptVar));
		ans.setAttribute("disabled",ModelConverters.toString(disabled));
		ans.setAttribute("placement",ModelConverters.toString(placement));
		ans.setAttribute("id",ModelConverters.toString(id));
		ans.setAttribute("fontColor",ModelConverters.toString(fontColor));
		ans.setAttribute("bgColor2",ModelConverters.toString(bgColor2));
		ans.setAttribute("text",ModelConverters.toString(text));
		return ans;
	}
	@Override
	public void setImage(String image) {
		this.image = image;
	}
	@Override
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	@Override
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public void setPlacement(GlobalButtonPlacement placement) {
		this.placement = placement;
	}
	@Override
	public void setBgColor1(Color bgColor1) {
		this.bgColor1 = bgColor1;
	}
	public GlobalButtonAction getAction() {
		return action;
	}
	@Override
	public String getImage() {
		return image;
	}
	@Override
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	@Override
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public void setBgColor2(Color bgColor2) {
		this.bgColor2 = bgColor2;
	}
	@Override
	public String getTarget() {
		return target;
	}
	@Override
	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}
	@Override
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	
	@Override
	public void setUnSet(List<String> setList) {
		ComonFunctions.getComonFunctions().SetFlags(set, setList);
		ComonFunctions.getComonFunctions().UnSetFlags(unSet, setList);
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
