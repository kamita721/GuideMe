package org.guideme.generated.model;

import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
public class Tease  {

	private String minimumVersion = "";

	public Tease(XMLStreamReader reader) {
		this.minimumVersion = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "minimumVersion","");
	}

	public Tease() {
	}

	public String getMinimumVersion() {
		return minimumVersion;
	}
	public void setMinimumVersion(String minimumVersion) {
		this.minimumVersion = minimumVersion;
	}
}
