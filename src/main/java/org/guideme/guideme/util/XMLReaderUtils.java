package org.guideme.guideme.util;

import java.time.DateTimeException;
import java.time.LocalTime;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import org.guideme.guideme.readers.xml_guide_reader.UserErrorManager;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.w3c.dom.Element;

public class XMLReaderUtils {
	private XMLReaderUtils() {
	}

	private static Logger logger = LogManager.getLogger();
	private static ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();

	public static DocumentBuilderFactory getDocumentBuilderFactory() {
		DocumentBuilderFactory ans = DocumentBuilderFactory.newInstance();

		try {
			ans.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
		ans.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		ans.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

		return ans;
	}

	public static TransformerFactory getTransformFactory() {
		TransformerFactory ans = TransformerFactory.newInstance();
		try {
			ans.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
		ans.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		ans.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

		return ans;
	}

	public static String getAttributeOrDefaultNoNS(XMLStreamReader reader, String localName,
			String defaultValue) {
		String ans = reader.getAttributeValue(null, localName);
		if (ans == null) {
			ans = defaultValue;
		}
		return ans;
	}

	public static int getAttributeOrDefaultNoNS(XMLStreamReader reader, String localName,
			int defaultValue) {
		String sAns = reader.getAttributeValue(null, localName);
		if (sAns == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(sAns);
		} catch (NumberFormatException e) {
			logger.warn("Error parsing int '{}' from xml", sAns, e);
		}
		return defaultValue;
	}

	public static boolean getAttributeOrDefaultNoNS(XMLStreamReader reader, String localName,
			boolean defaultValue) {
		String sAns = reader.getAttributeValue(null, localName);
		if (sAns == null || sAns.trim().isEmpty()) {
			return defaultValue;
		}
		return Boolean.valueOf(sAns);
	}

	public static LocalTime getAttributeOrDefaultNoNS(XMLStreamReader reader, String localName,
			LocalTime defaultValue) {
		LocalTime ans = getAttributeLocalTime(reader, localName);
		if (ans == null) {
			return defaultValue;
		}
		return ans;
	}

	public static LocalTime getAttributeLocalTime(XMLStreamReader reader, String localName) {
		return getAttributeLocalTimeDefaultable(reader, localName, null);
	}

	public static LocalTime getAttributeLocalTimeDefaultable(XMLStreamReader reader,
			String localName, LocalTime defaultValue) {
		String sAns = reader.getAttributeValue(null, localName);
		if (sAns == null) {
			return defaultValue;
		}
		try {
			return LocalTime.parse(sAns);
		} catch (DateTimeException e) {
			logger.warn("Error parsing time '{}' from xml", sAns, e);
		}
		return defaultValue;
	}

	public static Color getAttributeOrDefaultNoNS(XMLStreamReader reader, String localName,
			Color defaultValue) {
		String sAns = reader.getAttributeValue(null, localName);
		if (sAns == null || sAns.equals("")) {
			return defaultValue;
		} else if (sAns.startsWith("#")) {
			return comonFunctions.decodeHexColor(sAns);
		} else {
			return comonFunctions.getColor(sAns);
		}
	}

	public static int getAttributeOrDefault(Element e, String attributeName, int defaultValue) {
		String sAns = e.getAttribute(attributeName);
		if (sAns == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(sAns);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public static boolean isAtElementEnd(XMLStreamReader reader, String localName) {
		return reader.getEventType() == XMLStreamConstants.END_ELEMENT
				&& (reader.getName().getLocalPart().equals(localName));
	}

	public static boolean isAtElementStart(XMLStreamReader reader, String localName) {
		return reader.getEventType() == XMLStreamConstants.START_ELEMENT
				&& (reader.getName().getLocalPart().equals(localName));
	}

	public static String getStringContent(XMLStreamReader reader) throws XMLStreamException {
		reader.next();
		if (reader.getEventType() == XMLStreamConstants.CHARACTERS) {
			return reader.getText();
		}
		UserErrorManager.log("Cannot read text content of xml. \n{}", reader.getLocation());
		return "";
	}

	public static String getStringContentUntilElementEnd(XMLStreamReader reader)
			throws XMLStreamException {
		int depth = 0;
		StringBuilder ans = new StringBuilder();
		do {
			switch (reader.getEventType()) {
			case XMLStreamConstants.START_ELEMENT:
				depth++;
				break;
			case XMLStreamConstants.END_ELEMENT:
				depth--;
				break;
			case XMLStreamConstants.CHARACTERS:
				ans.append(reader.getText());
				break;
			default:
				break;
			}
			reader.next();
		} while (depth > 0);
		return ans.toString();
	}

	public static boolean getStringContentOrDefault(XMLStreamReader reader, boolean defaultValue)
			throws XMLStreamException {
		reader.next();
		if (reader.getEventType() == XMLStreamConstants.CHARACTERS) {
			return Boolean.valueOf(reader.getText());
		}
		return defaultValue;
	}
}
