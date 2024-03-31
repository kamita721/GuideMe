package org.guideme.guideme.settings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class UserSettings {
	private String userSettingsLocation = "";
	private static final Logger LOGGER = LogManager.getLogger();
	private HashMap<String, String> userStringPrefs = new HashMap<>();
	private HashMap<String, String> userStringDesc = new HashMap<>();
	private LinkedHashSet<String> userStringKeys = new LinkedHashSet<>();
	private HashMap<String, Boolean> userBooleanPrefs = new HashMap<>();
	private HashMap<String, String> userBooleanDesc = new HashMap<>();
	private LinkedHashSet<String> userBooleanKeys = new LinkedHashSet<>();
	private HashMap<String, Double> userNumericPrefs = new HashMap<>();
	private HashMap<String, String> userNumericDesc = new HashMap<>();
	private LinkedHashSet<String> userNumericKeys = new LinkedHashSet<>();
	private ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private static UserSettings userSettings;

	public static synchronized UserSettings getUserSettings() {
		if (userSettings == null) {
			userSettings = new UserSettings();
		}
		return userSettings;
	}
	
	@SuppressWarnings("unchecked")
	public UserSettings(UserSettings base) {
		userSettingsLocation = base.userSettingsLocation;
		userStringPrefs = (HashMap<String, String>) base.userStringPrefs.clone();
		userStringDesc = (HashMap<String, String>) base.userStringDesc.clone();
		userStringKeys = (LinkedHashSet<String>) base.userStringKeys.clone();
		userBooleanPrefs = (HashMap<String, Boolean>) base.userBooleanPrefs.clone();
		userBooleanDesc = (HashMap<String, String>) base.userBooleanDesc.clone();
		userBooleanKeys = (LinkedHashSet<String>) base.userBooleanKeys.clone();
		userNumericPrefs = (HashMap<String, Double>) base.userNumericPrefs.clone();
		userNumericDesc = (HashMap<String, String>) base.userNumericDesc.clone();
		userNumericKeys = (LinkedHashSet<String>) base.userNumericKeys.clone();
		userStringPrefs = (HashMap<String, String>) base.userStringPrefs.clone();
	}

	private UserSettings() {
		super();
		try {

			AppSettings appSettings = AppSettings.getAppSettings();
			userSettingsLocation = "userSettings_" + appSettings.getLanguage() + "_" + appSettings.getCountry()
					+ ".xml";
			File xmlFile = new File(userSettingsLocation);
			if (!xmlFile.exists()) {
				userSettingsLocation = "userSettings_" + appSettings.getLanguage() + ".xml";
				xmlFile = new File(userSettingsLocation);
				if (!xmlFile.exists()) {
					userSettingsLocation = "userSettings.xml";
					xmlFile = new File(userSettingsLocation);
				}
			}

			Element elProp;
			String key;
			String value;
			String type;
			String desc;

			if (xmlFile.exists()) {
				DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(xmlFile);
				Element rootElement = doc.getDocumentElement();
				rootElement.normalize();

				for (Node childNode = rootElement.getFirstChild(); childNode != null; childNode = childNode
						.getNextSibling()) {
					if (childNode.getNodeType() == Node.ELEMENT_NODE) {
						elProp = (Element) childNode;
						key = elProp.getAttribute("key");
						value = elProp.getAttribute("value");
						type = elProp.getAttribute("type");
						desc = elProp.getAttribute("screen");
						if (type.equals("String")) {
							userStringPrefs.put(key, value);
							userStringDesc.put(key, desc);
							userStringKeys.add(key);
						}
						if (type.equals("Boolean")) {
							userBooleanPrefs.put(key, Boolean.parseBoolean(value));
							userBooleanDesc.put(key, desc);
							userBooleanKeys.add(key);
						}
						if (type.equals("Number")) {
							userNumericPrefs.put(key, Double.parseDouble(value));
							userNumericDesc.put(key, desc);
							userNumericKeys.add(key);
						}
					}
				}

			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOGGER.error(e);
		}
		saveUserSettings();
	}

	public Set<String> getStringKeys() {
		return userStringKeys;
	}

	public Set<String> getNumberKeys() {
		return userNumericKeys;
	}

	public Set<String> getBooleanKeys() {
		return userBooleanKeys;
	}

	public String getPref(String key) {
		String value;
		if (userStringPrefs.containsKey(key)) {
			value = userStringPrefs.get(key);
		} else {
			value = "";
		}
		return value;
	}

	public Double getPrefNumber(String key) {
		Double value;
		if (userNumericPrefs.containsKey(key)) {
			value = userNumericPrefs.get(key);
		} else {
			value = (double) 0;
		}
		return value;
	}

	public void setPref(String key, String value) {
		userStringPrefs.computeIfPresent(key, (k,v) -> value);
	}

	public boolean isPref(String key) {
		Boolean value;
		if (userBooleanPrefs.containsKey(key)) {
			value = userBooleanPrefs.get(key);
		} else {
			value = false;
		}
		return value;
	}

	public void setPref(String key, boolean value) {
		userBooleanPrefs.computeIfPresent(key, (k, v) -> value);
	}

	public void setPref(String key, Double value) {
		userNumericPrefs.computeIfPresent(key, (k, v) -> value);
	}

	public String getScreenDesc(String key, String type) {
		String desc = "";
		if (type.equals("String")) {
			desc = userStringDesc.get(key);
		}
		if (type.equals("Boolean")) {
			desc = userBooleanDesc.get(key);
		}
		if (type.equals("Number")) {
			desc = userNumericDesc.get(key);
		}
		return desc;
	}

	public void addPref(String key, String value, String screenDesc) {
		userStringPrefs.put(key, value);
		userStringDesc.put(key, screenDesc);
	}

	public void addPref(String key, boolean value, String screenDesc) {
		userBooleanPrefs.put(key, value);
		userBooleanDesc.put(key, screenDesc);
	}

	public void addPref(String key, Double value, String screenDesc) {
		userNumericPrefs.put(key, value);
		userNumericDesc.put(key, screenDesc);
	}

	public boolean keyExists(String key, String type) {
		Boolean exists = false;
		if (type.equals("String")) {
			exists = userStringDesc.containsKey(key);
		}
		if (type.equals("Boolean")) {
			exists = userBooleanDesc.containsKey(key);
		}
		if (type.equals("Number")) {
			exists = userNumericDesc.containsKey(key);
		}
		return exists;
	}

	public void saveUserSettings() {
		try {
			File xmlFile = new File(userSettingsLocation);
			Element rootElement;
			Document doc;

			// if the file exists then use the current one, otherwise create a new one.
			// if nodes do not exist it will add them
			if (xmlFile.exists()) {
				DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				doc = docBuilder.parse(xmlFile);
				rootElement = doc.getDocumentElement();
				rootElement.normalize();
			} else {
				DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				doc = docBuilder.newDocument();
				rootElement = doc.createElement("SETTINGS");
				doc.appendChild(rootElement);
			}

			String keyVal;
			String desc;
			if (rootElement.hasChildNodes()) {
				while (rootElement.getFirstChild() != null) {
					rootElement.removeChild(rootElement.getFirstChild());
				}
			}
			for (Map.Entry<String, String> entry : userStringPrefs.entrySet()) {
				keyVal = entry.getKey();
				desc = userStringDesc.get(keyVal);
				Element elPref = comonFunctions.addElement("pref", rootElement, doc);
				elPref.setAttribute("key", keyVal);
				elPref.setAttribute("screen", desc);
				elPref.setAttribute("type", "String");
				elPref.setAttribute("value", entry.getValue());
			}

			for (Map.Entry<String, Boolean> entry : userBooleanPrefs.entrySet()) {
				keyVal = entry.getKey();
				desc = userBooleanDesc.get(keyVal);
				Element elPref = comonFunctions.addElement("pref", rootElement, doc);
				elPref.setAttribute("key", keyVal);
				elPref.setAttribute("screen", desc);
				elPref.setAttribute("type", "Boolean");
				elPref.setAttribute("value", String.valueOf(entry.getValue()));
			}

			for (Map.Entry<String, Double> entry : userNumericPrefs.entrySet()) {
				keyVal = entry.getKey();
				desc = userNumericDesc.get(keyVal);
				Element elPref = comonFunctions.addElement("pref", rootElement, doc);
				elPref.setAttribute("key", keyVal);
				elPref.setAttribute("screen", desc);
				elPref.setAttribute("type", "Number");
				elPref.setAttribute("value", String.valueOf(entry.getValue()));
			}

			// write the content into xml file
			TransformerFactory transformerFactory = XMLReaderUtils.getTransformFactory();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(userSettingsLocation));
			transformer.transform(source, result);

		} catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
			LOGGER.error(e);
		}
	}

}
