package org.guideme.guideme.model;

import java.time.LocalTime;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.swt.SWT;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeLocalTime;

public class Button implements Comparable<Button> {
	private final String ifSet;
	private final String ifNotSet;
	private final String set;
	private final String unSet;
	private String text;
	private String target;
	private final String jScript;
	private final String image;
	private final String hotKey;
	private String fontName;
	private int fontHeight;
	private final int sortOrder;
	private LocalTime ifBefore; // Time of day must be before this time
	private LocalTime ifAfter; // Time of day must be after this time
	private boolean disabled;
	private final String id;
	private org.eclipse.swt.graphics.Color bgColor1;
	private org.eclipse.swt.graphics.Color bgColor2;
	private org.eclipse.swt.graphics.Color fontColor;
	private final ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private final String scriptVar;
	private boolean defaultBtn; // button activated by enter

	public Button(XMLStreamReader reader) throws XMLStreamException {
		this.target = getAttributeOrDefaultNoNS(reader, "target", "");
		this.set = getAttributeOrDefaultNoNS(reader, "set", "");
		this.unSet = getAttributeOrDefaultNoNS(reader, "unset", "");
		this.ifSet = getAttributeOrDefaultNoNS(reader, "if-set", "");
		this.ifNotSet = getAttributeOrDefaultNoNS(reader, "if-not-set", "");
		this.ifBefore = getAttributeLocalTime(reader, "ifBefore");
		this.ifAfter = getAttributeLocalTime(reader, "ifAfter");
		this.jScript = getAttributeOrDefaultNoNS(reader, "onclick", "");
		this.image = getAttributeOrDefaultNoNS(reader, "image", "");
		this.hotKey = getAttributeOrDefaultNoNS(reader, "hotkey", "");
		this.scriptVar = getAttributeOrDefaultNoNS(reader, "scriptvar", "");
		this.fontName = getAttributeOrDefaultNoNS(reader, "fontName", "");
		this.fontHeight = getAttributeOrDefaultNoNS(reader, "fontHeight", 0);
		this.bgColor1 = getAttributeOrDefaultNoNS(reader, "bgColor1", comonFunctions.getSwtColor(SWT.COLOR_WHITE));
		this.bgColor2 = getAttributeOrDefaultNoNS(reader, "bgColor2", this.bgColor1);
		this.fontColor = getAttributeOrDefaultNoNS(reader, "fontColor", comonFunctions.getSwtColor(SWT.COLOR_BLACK));
		this.sortOrder = getAttributeOrDefaultNoNS(reader, "sortOrder", 1);
		this.disabled = getAttributeOrDefaultNoNS(reader, "disabled", false);
		this.defaultBtn = getAttributeOrDefaultNoNS(reader, "default", true);
		this.id = getAttributeOrDefaultNoNS(reader, "id", "");
		this.text = XmlGuideReader.processText(reader);


	}

	public Button(String target, String text) {
		this(target, text, "", "", "", "", "", "", "");
	}

	public Button(String target, String text, String ifSet, String ifNotSet, String set, String unSet, String jScript,
			String image, String hotKey) {
		this(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, "", 0, "", "", "", 1, "", "", false,
				"", "", false);
	}

	public Button(String target, String text, String ifSet, String ifNotSet, String set, String unSet, String jScript,
			String image, String hotKey, String fontName, int fontHeight, String fontColor, String bgColor1,
			String bgColor2, int sortOrder, String ifAfter, String ifBefore, boolean disabled, String id,
			String scriptVar, boolean defaultBtn) {
		this.target = target;
		this.text = text;
		this.ifNotSet = ifNotSet;
		this.ifSet = ifSet;
		this.set = set;
		this.unSet = unSet;
		this.jScript = jScript;
		this.image = image;
		this.hotKey = hotKey;
		this.fontName = fontName;
		this.fontHeight = fontHeight;
		this.sortOrder = sortOrder;

		if (bgColor1.equals("")) {
			this.bgColor1 = comonFunctions.getSwtColor(SWT.COLOR_WHITE);
		} else if (bgColor1.startsWith("#")) {
			this.bgColor1 = comonFunctions.decodeHexColor(bgColor1);
		} else {
			this.bgColor1 = comonFunctions.getColor(bgColor1);
		}

		if (bgColor2.equals("")) {
			this.bgColor2 = this.bgColor1;
		} else if (bgColor2.startsWith("#")) {
			this.bgColor2 = comonFunctions.decodeHexColor(bgColor2);
		} else {
			this.bgColor2 = comonFunctions.getColor(bgColor2);
		}

		if (fontColor.equals("")) {
			this.fontColor = comonFunctions.getColor("black");
		} else if (fontColor.startsWith("#")) {
			this.fontColor = comonFunctions.decodeHexColor(fontColor);
		} else {
			this.fontColor = comonFunctions.getColor(fontColor);
		}

		if (ifBefore.equals("")) {
			this.ifBefore = null;
		} else {
			this.ifBefore = LocalTime.parse(ifBefore);
		}
		if (ifAfter.equals("")) {
			this.ifAfter = null;
		} else {
			this.ifAfter = LocalTime.parse(ifAfter);
		}
		this.disabled = disabled;
		this.id = id;
		this.scriptVar = scriptVar;
		this.defaultBtn = false;
		this.setDefaultBtn(defaultBtn);
	}

	public void setUnSet(List<String> setList) {
		comonFunctions.setFlags(this.set, setList);
		comonFunctions.unsetFlags(this.unSet, setList);
	}

	public String getSet() {
		return this.set;
	}

	public String getUnSet() {
		return this.unSet;
	}

	public boolean canShow(List<String> setList) {
		boolean retVal = comonFunctions.canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = comonFunctions.canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getjScript() {
		return jScript;
	}

	public String getImage() {
		return image;
	}

	public String getHotKey() {
		return hotKey;
	}

	public String getIfSet() {
		return ifSet;
	}

	public String getIfNotSet() {
		return ifNotSet;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontHeight() {
		return fontHeight;
	}

	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}

	public org.eclipse.swt.graphics.Color getbgColor1() {
		return bgColor1;
	}

	public void setbgColor1(String bgColor1) {
		this.bgColor1.dispose();
		if (bgColor1.equals("")) {
			this.bgColor1 = comonFunctions.getSwtColor(SWT.COLOR_WHITE);
		} else if (bgColor1.startsWith("#")) {
			this.bgColor1 = comonFunctions.decodeHexColor(bgColor1);
		} else {
			this.bgColor1 = comonFunctions.getColor(bgColor1);
		}
	}

	public org.eclipse.swt.graphics.Color getbgColor2() {
		return bgColor2;
	}

	public void setbgColor2(String bgColor2) {
		this.bgColor2.dispose();
		if (bgColor2.equals("")) {
			this.bgColor2 = comonFunctions.getSwtColor(SWT.COLOR_WHITE);
		} else if (bgColor2.startsWith("#")) {
			this.bgColor2 = comonFunctions.decodeHexColor(bgColor2);
		} else {
			this.bgColor2 = comonFunctions.getColor(bgColor2);
		}
	}

	public org.eclipse.swt.graphics.Color getfontColor() {
		return fontColor;
	}

	public void setfontColor(String fontColor) {
		this.fontColor = comonFunctions.getColor(fontColor);
		this.fontColor.dispose();
		if (fontColor.equals("")) {
			this.fontColor = comonFunctions.getColor("black");
		} else if (fontColor.startsWith("#")) {
			this.fontColor = comonFunctions.decodeHexColor(fontColor);
		} else {
			this.fontColor = comonFunctions.getColor(fontColor);
		}
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public LocalTime getIfBefore() {
		return ifBefore;
	}

	public void setIfBefore(String ifBefore) {
		if (ifBefore.equals("")) {
			this.ifBefore = null;
		} else {
			this.ifBefore = LocalTime.parse(ifBefore);
		}
	}

	public LocalTime getIfAfter() {
		return ifAfter;
	}

	public void setIfAfter(String ifAfter) {
		if (ifAfter.equals("")) {
			this.ifAfter = null;
		} else {
			this.ifAfter = LocalTime.parse(ifAfter);
		}
	}

	@Override
	public int compareTo(Button compareButton) {
		int compareOrder = compareButton.getSortOrder();
		return compareOrder - this.sortOrder;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getId() {
		return id;
	}

	public String getScriptVar() {
		return scriptVar;
	}

	public boolean isDefaultBtn() {
		return defaultBtn;
	}

	public void setDefaultBtn(boolean defaultBtn) {
		this.defaultBtn = defaultBtn;
	}

}