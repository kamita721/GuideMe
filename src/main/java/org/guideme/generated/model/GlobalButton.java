package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import org.eclipse.swt.graphics.Color;
import javax.xml.stream.XMLStreamReader;
import org.guideme.guideme.model.GlobalButtonPlacement;
import org.guideme.guideme.model.GlobalButtonAction;
import javax.xml.stream.XMLStreamException;
import org.eclipse.swt.SWT;
import java.time.LocalTime;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class GlobalButton implements FlagSet, Button, Filterable  {

	private String image = "";
	private String set = "";
	private boolean defaultBtn = true;
	private LocalTime ifBefore;
	private String jScript = "";
	private Color bgColor1 = ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_WHITE);
	private Color bgColor2 = this.bgColor1;
	private String unSet = "";
	private String ifNotSet = "";
	private String target = "";
	private String fontName = "";
	private String ifSet = "";
	private int fontHeight = 0;
	private int sortOrder = 1;
	private String hotkey = "";
	private GlobalButtonAction action = GlobalButtonAction.NONE;
	private String scriptVar = "";
	private boolean disabled = false;
	private String id = "";
	private GlobalButtonPlacement placement = GlobalButtonPlacement.BOTTOM;
	private String text = "";
	private Color fontColor = ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK);
	private LocalTime ifAfter;

	public GlobalButton(XMLStreamReader reader) throws XMLStreamException {
		this.action = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "action",GlobalButtonAction.NONE);
		this.bgColor1 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor1",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_WHITE));
		this.defaultBtn = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "default",true);
		this.disabled = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "disabled",false);
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
		this.placement = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "placement",GlobalButtonPlacement.BOTTOM);
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.sortOrder = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "sortOrder",1);
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.bgColor2 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor2",this.bgColor1);
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public GlobalButton() {
	}

	public String getTarget() {
		return target;
	}
	public void setDefaultBtn(boolean defaultBtn) {
		this.defaultBtn = defaultBtn;
	}
	public String getId() {
		return id;
	}
	public void setBgColor1(Color bgColor1) {
		this.bgColor1 = bgColor1;
	}
	public String getHotkey() {
		return hotkey;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public String getText() {
		return text;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public GlobalButtonPlacement getPlacement() {
		return placement;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public String getIfSet() {
		return ifSet;
	}
	public GlobalButtonAction getAction() {
		return action;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	public String getJScript() {
		return jScript;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setAction(GlobalButtonAction action) {
		this.action = action;
	}
	public String getImage() {
		return image;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public Color getBgColor2() {
		return bgColor2;
	}
	public void setHotkey(String hotkey) {
		this.hotkey = hotkey;
	}
	public boolean getDisabled() {
		return disabled;
	}
	public String getFontName() {
		return fontName;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Color getBgColor1() {
		return bgColor1;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public String getSet() {
		return set;
	}
	public Color getFontColor() {
		return fontColor;
	}
	public boolean getDefaultBtn() {
		return defaultBtn;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public void setPlacement(GlobalButtonPlacement placement) {
		this.placement = placement;
	}
	public void setBgColor2(Color bgColor2) {
		this.bgColor2 = bgColor2;
	}
	public String getUnSet() {
		return unSet;
	}
	public int getFontHeight() {
		return fontHeight;
	}
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	
	public void setUnSet(List<String> setList) {
		ComonFunctions.getComonFunctions().setFlags(set, setList);
		ComonFunctions.getComonFunctions().unsetFlags(unSet, setList);
	}
	
	
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
}
