package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import org.eclipse.swt.graphics.Color;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import org.eclipse.swt.SWT;
import java.time.LocalTime;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class WebcamButton implements FlagSet, Button, Filterable  {

	private String image = "";
	private String set = "";
	private String destination = "";
	private boolean defaultBtn = true;
	private String jScript = "";
	private LocalTime ifBefore;
	private Color bgColor1 = ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_WHITE);
	private String type = "Capture";
	private Color bgColor2 = ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK);
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
		this.fontName = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontName","");
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.disabled = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "disabled",false);
		this.destination = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "file","");
		this.hotkey = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "hotkey","");
		this.type = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "type","Capture");
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.bgColor1 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor1",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_WHITE));
		this.defaultBtn = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "default",true);
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.sortOrder = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "sortOrder",1);
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.jScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onclick","");
		this.bgColor2 = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bgColor2",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK));
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.fontColor = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontColor",ComonFunctions.getComonFunctions().getSwtColor(SWT.COLOR_BLACK));
		this.fontHeight = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "fontHeight",0);
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
		this.image = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "image","");
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public WebcamButton() {
	}

	public String getIfNotSet() {
		return ifNotSet;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public String getJScript() {
		return jScript;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public boolean getDisabled() {
		return disabled;
	}
	public String getSet() {
		return set;
	}
	public String getFontName() {
		return fontName;
	}
	public String getTarget() {
		return target;
	}
	public String getUnSet() {
		return unSet;
	}
	public void setBgColor1(Color bgColor1) {
		this.bgColor1 = bgColor1;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	public void setDefaultBtn(boolean defaultBtn) {
		this.defaultBtn = defaultBtn;
	}
	public void setBgColor2(Color bgColor2) {
		this.bgColor2 = bgColor2;
	}
	public String getIfSet() {
		return ifSet;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public String getType() {
		return type;
	}
	public Color getFontColor() {
		return fontColor;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public Color getBgColor2() {
		return bgColor2;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}
	public String getDestination() {
		return destination;
	}
	public Color getBgColor1() {
		return bgColor1;
	}
	public String getId() {
		return id;
	}
	public String getHotkey() {
		return hotkey;
	}
	public void setHotkey(String hotkey) {
		this.hotkey = hotkey;
	}
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	public int getFontHeight() {
		return fontHeight;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public boolean getDefaultBtn() {
		return defaultBtn;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public String getImage() {
		return image;
	}
	public String getText() {
		return text;
	}
	public void setType(String type) {
		this.type = type;
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
