package org.guideme.guideme.model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

public class WebcamButton extends Button {
	
	private String type;
	private String destination;
	
	public WebcamButton(XMLStreamReader reader) throws XMLStreamException {
		super(reader);
		String destination = getAttributeOrDefaultNoNS(reader, "file", "");
		String type = getAttributeOrDefaultNoNS(reader, "type", "Capture");
		SetValues(type, destination);
	}
	
	public WebcamButton(String type, String destination, String target, String text, String ifSet, String ifNotSet, String set, String unSet, String jScript, String image, String hotKey) {
		super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey);
		SetValues(type, destination);
	}
	
	
	public WebcamButton(String type, String destination, String target, String text, String ifSet, String ifNotSet, String set, String unSet, String jScript, String image, String hotKey, String fontName, int fontHeight, String fontColor, String bgColor1, String bgColor2, int sortOrder, String ifAfter, String ifBefore, boolean disabled, String id, String scriptVar, boolean defaultBtn)
	{
		super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, fontName, fontHeight, fontColor, bgColor1, bgColor2, sortOrder, ifAfter, ifBefore, disabled, id, scriptVar, defaultBtn);
		SetValues(type, destination);
	}

	private void SetValues(String type, String destination)
	{
		switch (type.toLowerCase())
		{
			case "capture":
			default:
				type = "Capture";
				break;
		}
		destination = destination;
	}
	
	
	public String get_type() {
		return type;
	}
	public void set_type(String _type) {
		this.type = _type;
	}

	public String get_destination() {
		return destination;
	}
	public void set_destination(String _destination) {
		this.destination = _destination;
	}

}
