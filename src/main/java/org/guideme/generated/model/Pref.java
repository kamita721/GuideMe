package org.guideme.generated.model;

import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
public class Pref  {

	private String sortOrder = "";
	private String screen = "";
	private String type = "";
	private String value = "";
	private String key = "";

	public Pref(XMLStreamReader reader) {
		this.key = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "key","");
		this.screen = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "screen","");
		this.sortOrder = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "sortOrder","");
		this.type = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "type","");
		this.value = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "value","");
	}

	public Pref() {
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getScreen() {
		return screen;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setScreen(String screen) {
		this.screen = screen;
	}
	public String getValue() {
		return value;
	}
	public String getKey() {
		return key;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
