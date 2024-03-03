package org.guideme.generated.model;

import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
public class Include  {

	private String file = "";

	public Include(XMLStreamReader reader) {
		this.file = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "file","");
	}

	public Include() {
	}

	public void setFile(String file) {
		this.file = file;
	}
	public String getFile() {
		return file;
	}
}
