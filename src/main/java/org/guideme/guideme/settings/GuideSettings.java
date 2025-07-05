package org.guideme.guideme.settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;

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
import org.guideme.generated.model.GlobalButton;
import org.guideme.guideme.model.Preference;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.CharacterData;
import org.xml.sax.SAXException;

public class GuideSettings {
	/**
	 * 
	 */
	// State information for xml file, stored in a .state file in xml format
	private String chapter; // current chapter
	private String page; // current page
	private String currPage; // current page
	private String prevPage; // current page
	private String flags; // current flags
	private String filename; // name of file to store persistent state
	private String name; // GuideId for these settings
	private HashMap<String, String> formFields;
	private Map<String, Object> scriptVariables; // variables used by javascript
	private HashMap<String, Preference> prefs;
	private HashMap<String, GlobalButton> globalButtons = new HashMap<>();
	private boolean pageSound;
	private boolean forceStartPage;
	private boolean globalScriptLogged;
	private boolean convertArgumentTypes;
	private static final Logger LOGGER = LogManager.getLogger();
	private ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private Scriptable scope;
	private Scriptable globalScope;

	public GuideSettings(String guideId) {
		reset();
		name = guideId;
		String dataDirectory;
		AppSettings appSettings = AppSettings.getAppSettings();
		if (appSettings.isStateInDataDir()) {
			dataDirectory = appSettings.getTempDir();
			filename = dataDirectory + guideId + ".state";
		} else {
			dataDirectory = appSettings.getDataDirectory();
			String prefix = "";
			if (dataDirectory.startsWith("/")) {
				prefix = "/";
			}
			dataDirectory = prefix
					+ comonFunctions.fixSeparator(dataDirectory, appSettings.getFileSeparator());
			filename = dataDirectory + appSettings.getFileSeparator() + guideId + ".state";
		}
		LOGGER.debug("GuideSettings appSettings.getDataDirectory(): {}",
				appSettings.getDataDirectory());
		LOGGER.debug("GuideSettings dataDirectory: {}", dataDirectory);
		LOGGER.debug("GuideSettings appSettings.getFileSeparator(): {}",
				appSettings.getFileSeparator());
		LOGGER.debug("GuideSettings GuideId: {}", guideId);
		LOGGER.debug("GuideSettings filename: {}", filename);

		if (new File(filename).exists()) {
			loadSettings(filename);
		}

		saveSettings();
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String GetFlags() {
		return flags;
	}

	public void SetFlags(String flags) {
		this.flags = flags;
	}

	public Map<String, Object> getScriptVariables() {
		return scriptVariables;
	}

	public void setScriptVariables(Map<String, Object> scriptVariables) {
		this.scriptVariables = scriptVariables;
	}

	public Set<String> getKeys() {
		return prefs.keySet();
	}

	public String getPrefType(String key) {
		String value = "";
		if (prefs.containsKey(key)) {
			value = prefs.get(key).getType();
		}
		return value;
	}

	public String getPref(String key) {
		String value = "";
		if (prefs.containsKey(key)) {
			Preference pref = prefs.get(key);
			if (pref.getType().equals("String")) {
				value = pref.getstrValue();
			}
		}
		return value;
	}

	public Double getPrefNumber(String key) {
		Double value = (double) 0;
		if (prefs.containsKey(key)) {
			Preference pref = prefs.get(key);
			if (pref.getType().equals("Number")) {
				value = pref.getDblValue();
			}
		}
		return value;
	}

	public boolean isPref(String key) {
		Boolean value = false;
		if (prefs.containsKey(key)) {
			Preference pref = prefs.get(key);
			if (pref.getType().equals("Boolean")) {
				value = pref.getBlnValue();
			}
		}
		return value;
	}

	public void setPref(String key, String value) {
		if (prefs.containsKey(key)) {
			Preference pref = prefs.get(key);
			if (pref.getType().equals("String")) {
				pref.setstrValue(value);
			}
		}
	}

	public void setPref(String key, boolean value) {
		if (prefs.containsKey(key)) {
			Preference pref = prefs.get(key);
			if (pref.getType().equals("Boolean")) {
				pref.setBlnValue(value);
			}
		}
	}

	public void setPref(String key, Double value) {
		if (prefs.containsKey(key)) {
			Preference pref = prefs.get(key);
			if (pref.getType().equals("Number")) {
				pref.setDblValue(value);
			}
		}
	}

	public void setPrefOrder(String key, Integer value) {
		if (prefs.containsKey(key)) {
			Preference pref = prefs.get(key);
			pref.setSortOrder(value);
		}
	}

	public void addPref(String key, String value, String screenDesc, int sortOrder) {
		Preference pref = new Preference(key, "String", sortOrder, screenDesc, null, null, value);
		prefs.put(key, pref);
	}

	public void addPref(String key, boolean value, String screenDesc, int sortOrder) {
		Preference pref = new Preference(key, "Boolean", sortOrder, screenDesc, value, null, null);
		prefs.put(key, pref);
	}

	public void addPref(String key, Double value, String screenDesc, int sortOrder) {
		Preference pref = new Preference(key, "Number", sortOrder, screenDesc, null, value, null);
		prefs.put(key, pref);
	}

	public String getScreenDesc(String key) {
		String desc = "";
		if (prefs.containsKey(key)) {
			Preference pref = prefs.get(key);
			desc = pref.getScreenDesc();
		}
		return desc;
	}

	public List<Preference> getPrefArray() {
		ArrayList<Preference> ans = new ArrayList<>();
		Preference pref;
		Set<String> keys = prefs.keySet();
		for (String s : keys) {
			pref = prefs.get(s);
			ans.add(pref);
		}
		Collections.sort(ans);
		return ans;
	}

	public boolean keyExists(String key, String type) {
		// comment
		Boolean exists = false;
		if (prefs.containsKey(key)) {
			Preference pref = prefs.get(key);
			if (pref.getType().equals(type)) {
				exists = true;
			}
		}
		return exists;
	}

	public String getName() {
		return name;
	}

	public void reset() {
		chapter = "";
		page = "start";
		currPage = "start";
		prevPage = "start";
		flags = "";
		filename = null;
		name = null;
		formFields = new HashMap<>();
		scriptVariables = new HashMap<>();
		prefs = new HashMap<>();
		globalButtons = new HashMap<>();
		pageSound = true;
		forceStartPage = false;
		globalScriptLogged = false;
		convertArgumentTypes = false;
		scope = null;
		globalScope = null;
	}

	public void loadSettings(String filename) {
		this.filename = filename;

		Document doc;
		try {
			DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(new File(filename));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOGGER.warn("Error loading guide state {}", filename, e);
			reset();
			return;
		}

		Element rootElement = doc.getDocumentElement();
		rootElement.normalize();
		if (!rootElement.getNodeName().equals(SettingsNames.ROOT_ELEMENT)) {
			LOGGER.warn("Error loading guide state {} because root element is {} not {}", filename,
					rootElement.getNodeName(), SettingsNames.ROOT_ELEMENT);
			reset();
			return;

		}
		NodeList children = rootElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			loadSettingsElement(children.item(i));
		}
	}

	private void loadSettingsElement(Node n) {
		switch (n.getNodeType()) {
		case Node.ELEMENT_NODE:
			switch (n.getNodeName()) {
			case SettingsNames.PAGE:
				setPage(n.getTextContent());
				break;
			case SettingsNames.CURR_PAGE:
				setCurrPage(n.getTextContent());
				break;
			case SettingsNames.PREV_PAGE:
				setPrevPage(n.getTextContent());
				break;
			case SettingsNames.FLAGS:
				SetFlags(n.getTextContent());
				break;
			case SettingsNames.SCRIPT_VARIABLES:
				loadScriptVariables(n);
				break;
			case SettingsNames.SCOPE:
				String txtContent = n.getTextContent();
				if (txtContent.isBlank()) {
					scope = null;
				} else {
					scope = (Scriptable) comonFunctions.getSavedObject(n.getTextContent(), "Scope",
							getGlobalScope());
				}
				break;
			case SettingsNames.SCRIPT_PREFERENCES:
				loadScriptPreferences(n);
				break;
			case SettingsNames.GLOBAL_BUTTONS:
				loadGlobalButtons(n);
				break;
			default:
				LOGGER.warn("Unrecognized field '{}' in '{}'", n.getNodeName(), filename);
			}
			break;
		case Node.COMMENT_NODE:
			break;
		default:
			LOGGER.warn("Unexpected node type ({}) while parsing guide state {}:\n{}",
					n.getNodeType(), filename);
			break;
		}
	}

	private void loadScriptVariables(Node n) {
		if (n.getNodeType() != Node.ELEMENT_NODE
				|| !n.getNodeName().equals(SettingsNames.SCRIPT_VARIABLES)) {
			throw new IllegalStateException();
		}
		NodeList nodeList = n.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() != Node.ELEMENT_NODE
					|| !currentNode.getNodeName().equals(SettingsNames.VAR)) {
				LOGGER.warn(
						"Unexpected node type {} and name {} inside scriptVariables section of {}",
						currentNode.getNodeType(), currentNode.getNodeName(), filename);
				continue;
			}
			Element elVar = (Element) currentNode;
			String strName = elVar.getAttribute("id");
			String strType = elVar.getAttribute("type");
			Object objValue;
			LOGGER.trace("GuideSettings scriptVariables strName {} strType {}", strName, strType);
			CharacterData elChar;
			elChar = (CharacterData) elVar.getFirstChild();
			if (elChar != null) {
				String strValue = elChar.getData();
				objValue = comonFunctions.getSavedObject(strValue, strType, getGlobalScope());
			} else {
				objValue = null;
			}
			scriptVariables.put(strName, objValue);

		}
	}

	private void saveScriptVariables(Element el, Document doc) {

		LOGGER.trace("GuideSettings saveSettings scriptVariables");
		Iterator<String> it = scriptVariables.keySet().iterator();
		Element elVar;
		while (it.hasNext()) {
			String key = it.next();
			Object value = scriptVariables.get(key);
			String strType;
			String strValue;
			if (value == null) {
				strType = "Null";
				strValue = "";
			} else {
				strType = value.getClass().getName();
				strValue = comonFunctions.createSaveObject(value, strType, getGlobalScope());
			}
			if (!strValue.equals("ignore")) {
				elVar = comonFunctions.addElement("Var", el, doc);
				elVar.setAttribute("id", key);
				comonFunctions.addCdata(strValue, elVar, doc);
				elVar.setAttribute("type", strType);
			}
		}
	}

	private void loadScriptPreferences(Node n) {
		if (n.getNodeType() != Node.ELEMENT_NODE
				|| !n.getNodeName().equals(SettingsNames.SCRIPT_PREFERENCES)) {
			throw new IllegalStateException();
		}

		NodeList nodeList = n.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (childNode.getNodeType() != Node.ELEMENT_NODE) {
				LOGGER.warn("Unexpected node type {} inside scriptVariables section of {}",
						childNode.getNodeType(), filename);
				continue;
			}
			Element elProp = (Element) childNode;
			String key = elProp.getAttribute("key");
			String value = elProp.getAttribute("value");
			String type = elProp.getAttribute("type");
			String desc = elProp.getAttribute("screen");
			int sortOrder = XMLReaderUtils.getAttributeOrDefault(elProp, "sortOrder", 0);
			elProp.getAttribute("sortOrder");
			if (type.equals("String")) {
				Preference pref = new Preference(key, type, sortOrder, desc, null, null, value);
				prefs.put(key, pref);
			}
			if (type.equals("Boolean")) {
				Preference pref = new Preference(key, type, sortOrder, desc,
						Boolean.parseBoolean(value), null, null);
				prefs.put(key, pref);
			}
			if (type.equals("Number")) {
				Preference pref = new Preference(key, type, sortOrder, desc, null,
						Double.parseDouble(value), null);
				prefs.put(key, pref);
			}
		}

	}

	private void saveScriptPreferences(Element el, Document doc) {
		for (Map.Entry<String, Preference> entry : prefs.entrySet()) {
			Preference pref = entry.getValue();
			Element elPref = comonFunctions.addElement("pref", el, doc);
			elPref.setAttribute("key", pref.getKey());
			elPref.setAttribute("screen", pref.getScreenDesc());
			elPref.setAttribute("type", pref.getType());
			elPref.setAttribute("sortOrder", String.valueOf(pref.getSortOrder()));
			if (pref.getType().equals("String")) {
				elPref.setAttribute("value", pref.getstrValue());
			}
			if (pref.getType().equals("Boolean")) {
				elPref.setAttribute("value", String.valueOf(pref.getBlnValue()));
			}
			if (pref.getType().equals("Number")) {
				elPref.setAttribute("value", String.valueOf(pref.getDblValue()));
			}
		}
	}

	private void loadGlobalButtons(Node n) {
		NodeList nl = n.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			String nodeName = child.getNodeName();
			if (!nodeName.equals(SettingsNames.GLOBAL_BUTTON)) {
				LOGGER.warn(
						"Error reading in state file. Found element '{}' where '{}' was expected",
						nodeName, SettingsNames.GLOBAL_BUTTON);
				continue;
			}
			GlobalButton toAdd = new GlobalButton(child);
			addGlobalButton(toAdd.getId(), toAdd);
		}
	}

	private void saveGlobalButtons(Element elGlobalButtons, Document doc) {
		for (Entry<String, GlobalButton> entry : globalButtons.entrySet()) {
			Element toAdd = entry.getValue().asXml(doc);
			elGlobalButtons.appendChild(toAdd);
		}
	}

	public void saveSettings() {
		try {
			LOGGER.trace("GuideSettings saveSettings filename: {}", filename);

			LOGGER.trace("GuideSettings saveSettings does not file exist ");
			DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("SETTINGS");
			doc.appendChild(rootElement);

			comonFunctions.addElement(SettingsNames.PAGE, rootElement, doc)
					.setTextContent(getPage());
			comonFunctions.addElement(SettingsNames.CURR_PAGE, rootElement, doc)
					.setTextContent(getCurrPage());
			comonFunctions.addElement(SettingsNames.PREV_PAGE, rootElement, doc)
					.setTextContent(getPrevPage());
			comonFunctions.addElement(SettingsNames.FLAGS, rootElement, doc)
					.setTextContent(GetFlags());

			Element elScriptVariables = comonFunctions.addElement(SettingsNames.SCRIPT_VARIABLES,
					rootElement, doc);
			saveScriptVariables(elScriptVariables, doc);

			Element elScope = comonFunctions.addElement(SettingsNames.SCOPE, rootElement, doc);
			String strValue = comonFunctions.createSaveObject(scope, "Scope", getGlobalScope());
			comonFunctions.addCdata(strValue, elScope, doc);

			Element elscriptPreferences = comonFunctions
					.addElement(SettingsNames.SCRIPT_PREFERENCES, rootElement, doc);
			saveScriptPreferences(elscriptPreferences, doc);

			Element elGlobalButtons = comonFunctions.addElement(SettingsNames.GLOBAL_BUTTONS,
					rootElement, doc);
			saveGlobalButtons(elGlobalButtons, doc);

			// write the content into xml file
			TransformerFactory transformerFactory = XMLReaderUtils.getTransformFactory();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			LOGGER.trace("GuideSettings saveSettings save file: {}", filename);
			StreamResult result = new StreamResult(new File(filename));
			transformer.transform(source, result);
		} catch (TransformerException | ParserConfigurationException ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
	}

	public void formFieldsReset() {
		formFields = new HashMap<>();
	}

	public String getFormField(String key) {
		String value = formFields.get(key);
		if (value == null) {
			value = "";
		}
		return value;
	}

	public void setFormField(String key, String value) {
		formFields.put(key, value);
	}

	public boolean isPageSound() {
		return pageSound;
	}

	public void setPageSound(boolean pageSound) {
		this.pageSound = pageSound;
	}

	public String getCurrPage() {
		return currPage;
	}

	public void setCurrPage(String currPage) {
		this.currPage = currPage;
	}

	public String getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(String prevPage) {
		this.prevPage = prevPage;
	}

	public String getFilename() {
		return filename;
	}

	public boolean isForceStartPage() {
		return forceStartPage;
	}

	public void setForceStartPage(boolean forceStartPage) {
		this.forceStartPage = forceStartPage;
	}

	public boolean isGlobalScriptLogged() {
		return globalScriptLogged;
	}

	public void setGlobalScriptLogged(boolean globalScriptLogged) {
		this.globalScriptLogged = globalScriptLogged;
	}

	public boolean isConvertArgumentTypes() {
		return convertArgumentTypes;
	}

	public void setConvertArgumentTypes(boolean convertArgumentTypes) {
		this.convertArgumentTypes = convertArgumentTypes;
	}

	public void setScriptVar(String key, Object val) {
		scriptVariables.put(key, val);
	}

	public Scriptable getScope() {
		return scope;
	}

	public void setScope(Scriptable scope) {
		this.scope = scope;
	}

	public Scriptable getGlobalScope() {
		if (globalScope == null) {
			ContextFactory factory = new ContextFactory();
			Context context = factory.enterContext();
			globalScope = context.initStandardObjects();
			Context.exit();
		}
		return globalScope;
	}

	/**
	 * Get all global buttons
	 * 
	 * @return Global buttons
	 */
	public GlobalButton[] getGlobalButtons() {
		return globalButtons.values().toArray(new GlobalButton[] {});
	}

	/**
	 * Adds a new global button
	 * 
	 * @param id     ID of button
	 * @param button Button object
	 */
	public void addGlobalButton(String id, GlobalButton button) {
		globalButtons.put(id, button);
	}

	/**
	 * Removes a global button by ID
	 * 
	 * @param id ID to remove
	 */
	public void removeGlobalButton(String id) {
		globalButtons.remove(id);
	}

	private class SettingsNames {
		public static final String ROOT_ELEMENT = "SETTINGS";
		public static final String VAR = "Var";

		public static final String PAGE = "Page";
		public static final String CURR_PAGE = "CurrPage";
		public static final String PREV_PAGE = "PrevPage";
		public static final String FLAGS = "Flags";
		public static final String SCRIPT_VARIABLES = "scriptVariables";
		public static final String SCOPE = "scope";
		public static final String SCRIPT_PREFERENCES = "scriptPreferences";
		public static final String GLOBAL_BUTTONS = "GlobalButtons";
		public static final String GLOBAL_BUTTON = "GlobalButton";

	}

	public void restart() {
		setPage("start");
		setScriptVariables(new HashMap<>());
		setScope(null);
		globalButtons.clear();
		saveSettings();
	}

}
