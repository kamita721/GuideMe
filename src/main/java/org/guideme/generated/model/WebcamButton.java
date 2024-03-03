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
	}

	public boolean getDefaultBtn() {
		return defaultBtn;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getImage() {
		return image;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setDefaultBtn(boolean defaultBtn) {
		this.defaultBtn = defaultBtn;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public void setBgColor2(Color bgColor2) {
		this.bgColor2 = bgColor2;
	}
	public void setBgColor1(Color bgColor1) {
		this.bgColor1 = bgColor1;
	}
	public boolean getDisabled() {
		return disabled;
	}
	public String getSet() {
		return set;
	}
	public Color getBgColor1() {
		return bgColor1;
	}
	public String getIfSet() {
		return ifSet;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public void setHotkey(String hotkey) {
		this.hotkey = hotkey;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public int getFontHeight() {
		return fontHeight;
	}
	public String getTarget() {
		return target;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public String getJScript() {
		return jScript;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDestination() {
		return destination;
	}
	public String getHotkey() {
		return hotkey;
	}
	public String getUnSet() {
		return unSet;
	}
	public Color getFontColor() {
		return fontColor;
	}
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getType() {
		return type;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public String getId() {
		return id;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public String getFontName() {
		return fontName;
	}
	public Color getBgColor2() {
		return bgColor2;
	}
	public String getText() {
		return text;
	}
	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
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
