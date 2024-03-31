package org.guideme.guideme.scripting.functions;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.model.Library;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.settings.UserSettings;
import org.guideme.guideme.util.ImageManager;
import org.imgscalr.Scalr;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeDate;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.serialize.ScriptableInputStream;
import org.mozilla.javascript.serialize.ScriptableOutputStream;
import org.springframework.extensions.webscripts.ScriptValueConverter;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/*
 * THIS FILE IS PART OF THE JAVASCRIPT API
 * 
 * Deleting any method is a breaking change!
 */
public class ComonFunctions {
	/**
	 * 
	 */
	private SecureRandom mRandom = new SecureRandom();
	private static final Logger LOGGER = LogManager.getLogger();
	private XPathFactory factory = XPathFactory.newInstance();
	private XPath xpath = factory.newXPath();
	private static final String VERSION = "0.4.5";
	private OSFamily osFamily;

	public enum OSFamily {
		Windows, Mac, Unix, Unknown
	}

	private static ComonFunctions comonFunctions;
	private Display display;

	private ComonFunctions() {
		osFamily = getOS();
	}

	public static synchronized ComonFunctions getComonFunctions() {
		if (comonFunctions == null) {
			comonFunctions = new ComonFunctions();
		}
		return comonFunctions;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private OSFamily getOS() {
		OSFamily osval = OSFamily.Unknown;
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			osval = OSFamily.Windows;
		} else if (os.indexOf("mac") >= 0) {
			osval = OSFamily.Mac;
		} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0) {
			osval = OSFamily.Unix;
		}
		return osval;
	}

	// checks to see if the flags match
	public boolean canShow(List<String> setList, String ifSet, String ifNotSet) {
		boolean icanShow = false;
		boolean blnSet = true;
		boolean blnNotSet = true;

		try {
			if (!ifSet.trim().equals("")) {
				blnSet = matchesIfSetCondition(ifSet.trim(), setList);
			}
			if (!ifNotSet.trim().equals("")) {
				blnNotSet = matchesIfNotSetCondition(ifNotSet.trim(), setList);
			}
			if (blnSet && blnNotSet) {
				icanShow = true;
			} else {
				icanShow = false;
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
		return icanShow;
	}

	public boolean canShowTime(LocalTime ifBefore, LocalTime ifAfter) {
		boolean before = true;
		boolean after = true;
		boolean show = false;
		LocalTime now = LocalTime.now();
		if (ifBefore != null && (ifBefore.isBefore(now))) {
			before = false;
		}
		if (ifAfter != null && (ifAfter.isAfter(now))) {
			after = false;
		}

		if (before && after) {
			show = true;
		} else {
			show = false;
		}
		return show;
	}

	// Overloaded function checks pages as well
	public boolean canShow(List<String> setList, String ifSet, String ifNotSet, String pageId) {
		boolean icanShow = false;
		try {
			icanShow = canShow(setList, ifSet, ifNotSet);
			if (icanShow) {
				if (pageId.equals("")) {
					icanShow = true;
				} else {
					icanShow = matchesIfNotSetCondition(pageId, setList);
				}
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
		return icanShow;
	}

	public boolean isSet(String condition, List<String> setList) {
		return matchesIfSetCondition(condition, setList);
	}

	public boolean isNotSet(String condition, List<String> setList) {
		return matchesIfNotSetCondition(condition, setList);
	}

	// checks list of flags to see if they match
	private boolean matchesIfSetCondition(String condition, List<String> setList) {
		boolean blnReturn = false;
		boolean blnAnd = false;
		boolean blnOr = false;
		String[] conditions;

		try {
			if (condition.indexOf("|") > -1) {
				blnOr = true;
				condition = condition.replace("|", ",");
				conditions = condition.split(",", -1);
				for (int i = 0; i < conditions.length; i++) {
					if (setList.contains(conditions[i].trim())) {
						blnReturn = true;
						break;
					}
				}
			}

			if (condition.indexOf("+") > -1) {
				blnAnd = true;
				blnReturn = true;
				condition = condition.replace("+", ",");
				conditions = condition.split(",", -1);
				for (int i = 0; i < conditions.length; i++) {
					if (!setList.contains(conditions[i].trim())) {
						blnReturn = false;
						break;
					}
				}
			}

			if (!blnAnd && !blnOr) {
				blnReturn = setList.contains(condition);
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		return blnReturn;
	}

	// checks a list of flags to make sure they don't match
	private boolean matchesIfNotSetCondition(String condition, List<String> setList) {
		boolean blnReturn = false;
		boolean blnAnd = false;
		boolean blnOr = false;
		String[] conditions;

		try {
			if (condition.indexOf("+") > -1) {
				blnAnd = true;
				blnReturn = true;
				condition = condition.replace("+", ",");
				conditions = condition.split(",", -1);
				for (int i = 0; i < conditions.length; i++) {
					if (setList.contains(conditions[i].trim())) {
						blnReturn = false;
						break;
					}
				}
			}

			if (condition.indexOf("|") > -1) {
				blnOr = true;
				condition = condition.replace("|", ",");
				conditions = condition.split(",", -1);
				for (int i = 0; i < conditions.length; i++) {
					if (!setList.contains(conditions[i].trim())) {
						blnReturn = true;
						break;
					}
				}
			}

			if (!blnAnd && !blnOr) {
				blnReturn = !setList.contains(condition);
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		return blnReturn;
	}

	// functions to handle set flags go here
	public void setFlags(String flagNames, List<String> setList) {
		String[] flags;
		try {
			flags = flagNames.split(",", -1);
			for (int i = 0; i < flags.length; i++) {
				if (!flags[i].trim().equals("")) {
					if (!setList.contains(flags[i].trim())) {
						setList.add(flags[i].trim());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}

	public String getFlags(List<String> setList) {
		StringBuilder strFlags = new StringBuilder();
		try {
			for (String s : setList) {
				if (strFlags.length() > 0) {
					strFlags.append(',');
				}
				strFlags.append(s);
			}

		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		return strFlags.toString();
	}

	public void unsetFlags(String flagNames, List<String> setList) {
		String[] flags;
		try {
			flags = flagNames.split(",", -1);
			for (int i = 0; i < flags.length; i++) {
				if (!flags[i].trim().equals("")) {
					if (setList.contains(flags[i].trim())) {
						setList.remove(flags[i].trim());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}

	// Get random number between x and y where Random is (x..y)
	// if just a number is passed in it returns that number
	// this is so we can just pass the parameter in for things like delays where
	// "15" would be a delay of 15 seconds but "(5..15)" would be a random delay
	// between 5 and 15 seconds
	public int getRandom(String random) {
		int intRandom = 0;
		int intPos1;
		int intPos2;
		int intPos3;
		int intMin;
		int intMax;
		String strMin;
		String strMax;

		try {
			intPos1 = random.indexOf("(");
			if (intPos1 > -1) {
				intPos2 = random.indexOf("..", intPos1);
				if (intPos2 > -1) {
					intPos3 = random.indexOf(")", intPos2);
					if (intPos3 > -1) {
						strMin = random.substring(intPos1 + 1, intPos2);
						intMin = Integer.parseInt(strMin);
						strMax = random.substring(intPos2 + 2, intPos3);
						intMax = Integer.parseInt(strMax);
						int i1 = mRandom.nextInt(intMax - intMin + 1) + intMin;
						intRandom = i1;
					}
				}
			} else {
				intRandom = Integer.parseInt(random);
			}
		} catch (NumberFormatException en) {
			intRandom = 0;
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		return intRandom;
	}

	// gets a random number between intMin and intMax inclusive
	public int getRandom(int intMin, int intMax) {
		return mRandom.nextInt(intMax - intMin + 1) + intMin;
	}

	// gets a random number between 1 and intMax inclusive
	public int getRandom(int intMax) {
		return mRandom.nextInt(intMax - 1 + 1) + 1;
	}

	public int getMilisecFromTime(String iTime) {
		int intPos1;
		int intPos2;
		String strHour;
		String strMinute;
		String strSecond;
		int intTime = 0;

		try {
			intPos1 = iTime.indexOf(":");
			if (intPos1 > -1) {
				intPos2 = iTime.indexOf(":", intPos1 + 1);
				if (intPos2 > -1) {
					strHour = iTime.substring(0, intPos1);
					strMinute = iTime.substring(intPos1 + 1, intPos2);
					strSecond = iTime.substring(intPos2 + 1, iTime.length());
					intTime = Integer.parseInt(strSecond) * 1000;
					intTime = intTime + Integer.parseInt(strMinute) * 1000 * 60;
					intTime = intTime + Integer.parseInt(strHour) * 1000 * 60 * 60;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		return intTime;
	}

	public Element getOrAddElement(String xPath, String nodeName, Element parent, Document doc) {
		// xml helper function
		try {
			Element elToSet = getElement(xPath, parent);
			if (elToSet == null) {
				elToSet = addElement(nodeName, parent, doc);
			}
			return elToSet;
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
			return null;
		}
	}

	public Element getElement(String xPath, Element parent) {
		// xml helper function
		try {
			XPathExpression expr = xpath.compile(xPath);
			Object xpathresult = expr.evaluate(parent, XPathConstants.NODESET);
			NodeList nodes = (NodeList) xpathresult;
			Element elToSet = null;
			if (nodes.getLength() > 0) {
				elToSet = (Element) nodes.item(0);
			}
			return elToSet;
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
			return null;
		}
	}

	public Element addElement(String nodeName, Element parentNode, Document doc) {
		// xml helper function
		try {
			Element elToSet;
			elToSet = doc.createElement(nodeName);
			parentNode.appendChild(elToSet);
			return elToSet;
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
			return null;
		}
	}

	public CDATASection addCdata(String cdataContent, Element parentNode, Document doc) {
		CDATASection newNode;
		newNode = doc.createCDATASection(cdataContent);
		parentNode.appendChild(newNode);
		return newNode;
	}

	public String readFile(String path, Charset encoding) {
		// returns the contents of a file as a String
		String returnVal = "";
		try {
			Thread.interrupted();
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			returnVal = encoding.decode(ByteBuffer.wrap(encoded)).toString();
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
		return returnVal;
	}

	public String jsReadFile(String path) {
		return jsReadFile(path, StandardCharsets.UTF_8.name());
	}

	public String jsReadFile(String fileName, String encoding) {
		AppSettings appSettings = AppSettings.getAppSettings();
		Guide guide = Guide.getGuide();
		String fileSeparator = appSettings.getFileSeparator();

		String dataDirectory;
		String prefix = "";
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix + fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		String mediaDirectory = fixSeparator(guide.getMediaDirectory(), fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;

		String media = fixSeparator(fileName, fileSeparator);
		LOGGER.debug("CommonFunctions fileExists getMediaFullPath {} ", media);
		int intSubDir = media.lastIndexOf(fileSeparator);
		String strSubDir;
		if (intSubDir > -1) {
			strSubDir = fixSeparator(media.substring(0, intSubDir + 1), fileSeparator);
			media = media.substring(intSubDir + 1);
		} else {
			strSubDir = "";
		}
		// String strSubDir
		// no wildcard so just use the file name
		if (strSubDir.equals("")) {
			fileName = dataDirectory + fileSeparator + media;
		} else {
			fileName = dataDirectory + fileSeparator + strSubDir + fileSeparator + media;
		}

		Charset encodeSet;
		switch (encoding) {
		case "ISO_8859_1":
			encodeSet = StandardCharsets.ISO_8859_1;
			break;
		case "US_ASCII":
			encodeSet = StandardCharsets.US_ASCII;
			break;
		case "UTF_8":
			encodeSet = StandardCharsets.UTF_8;
			break;
		case "UTF_16":
			encodeSet = StandardCharsets.UTF_16;
			break;
		case "UTF_16BE":
			encodeSet = StandardCharsets.UTF_16BE;
			break;
		case "UTF_16LE":
			encodeSet = StandardCharsets.UTF_16LE;
			break;
		default:
			LOGGER.warn("Unrecognized character encoding '{}', falling back to UTF-8", encoding);
			encodeSet = StandardCharsets.UTF_8;
			break;

		}
		String fileToReturn = "";

		try {
			fileToReturn = readFile(fileName, encodeSet);
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}

		return fileToReturn;
	}

	public String[] jsReadFileArray(String fileName, String encoding) {
		String[] retrn = null;
		AppSettings appSettings = AppSettings.getAppSettings();
		Guide guide = Guide.getGuide();
		String fileSeparator = appSettings.getFileSeparator();

		String dataDirectory;
		String prefix = "";
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix + fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		String mediaDirectory = fixSeparator(guide.getMediaDirectory(), fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;

		String media = fixSeparator(fileName, fileSeparator);
		int intSubDir = media.lastIndexOf(fileSeparator);
		String strSubDir;
		if (intSubDir > -1) {
			strSubDir = fixSeparator(media.substring(0, intSubDir + 1), fileSeparator);
			media = media.substring(intSubDir + 1);
		} else {
			strSubDir = "";
		}
		// String strSubDir
		// no wildcard so just use the file name
		if (strSubDir.equals("")) {
			fileName = dataDirectory + fileSeparator + media;
		} else {
			fileName = dataDirectory + fileSeparator + strSubDir + fileSeparator + media;
		}

		Charset encodeSet;
		switch (encoding) {
		case "ISO_8859_1":
			encodeSet = StandardCharsets.ISO_8859_1;
			break;
		case "US_ASCII":
			encodeSet = StandardCharsets.US_ASCII;
			break;
		case "UTF_16":
			encodeSet = StandardCharsets.UTF_16;
			break;
		case "UTF_16BE":
			encodeSet = StandardCharsets.UTF_16BE;
			break;
		case "UTF_16LE":
			encodeSet = StandardCharsets.UTF_16LE;
			break;
		default:
			encodeSet = StandardCharsets.UTF_8;
			break;

		}

		try {
			Path filePath = new File(fileName).toPath();
			List<String> stringList = Files.readAllLines(filePath, encodeSet);
			retrn = stringList.toArray(new String[] {});
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
		return retrn;
	}

	public String[] jsReadFileArray(String fileName) {
		return jsReadFileArray(fileName, "UTF-8");
	}

	public void jsWriteFile(String fileName, String contents) {
		jsWriteFile(fileName, "UTF-8", contents);
	}

	public void jsWriteFile(String fileName, String encoding, String contents) {
		AppSettings appSettings = AppSettings.getAppSettings();
		Guide guide = Guide.getGuide();
		String fileSeparator = appSettings.getFileSeparator();

		String dataDirectory;
		String prefix = "";
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix + fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		String mediaDirectory = fixSeparator(guide.getMediaDirectory(), fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;

		String media = fixSeparator(fileName, fileSeparator);
		LOGGER.debug("CommonFunctions fileExists getMediaFullPath {}", media);
		int intSubDir = media.lastIndexOf(fileSeparator);
		String strSubDir;
		if (intSubDir > -1) {
			strSubDir = fixSeparator(media.substring(0, intSubDir + 1), fileSeparator);
			media = media.substring(intSubDir + 1);
		} else {
			strSubDir = "";
		}
		// String strSubDir
		// no wildcard so just use the file name
		if (strSubDir.equals("")) {
			fileName = dataDirectory + fileSeparator + media;
		} else {
			fileName = dataDirectory + fileSeparator + strSubDir + fileSeparator + media;
		}

		try (BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileName), encoding))) {
			out.write(contents);
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}

	}

	public void jsWriteFileArray(String fileName, String[] contents) {
		jsWriteFileArray(fileName, "UTF-8", contents);
	}

	public void jsWriteFileArray(String fileName, String encoding, String[] contents) {
		AppSettings appSettings = AppSettings.getAppSettings();
		Guide guide = Guide.getGuide();
		String fileSeparator = appSettings.getFileSeparator();

		String dataDirectory;
		String prefix = "";
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix + fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		String mediaDirectory = fixSeparator(guide.getMediaDirectory(), fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;

		String media = fixSeparator(fileName, fileSeparator);
		LOGGER.debug("CommonFunctions fileExists getMediaFullPath {}", media);
		int intSubDir = media.lastIndexOf(fileSeparator);
		String strSubDir;
		if (intSubDir > -1) {
			strSubDir = fixSeparator(media.substring(0, intSubDir + 1), fileSeparator);
			media = media.substring(intSubDir + 1);
		} else {
			strSubDir = "";
		}
		// String strSubDir
		// no wildcard so just use the file name
		if (strSubDir.equals("")) {
			fileName = dataDirectory + fileSeparator + media;
		} else {
			fileName = dataDirectory + fileSeparator + strSubDir + fileSeparator + media;
		}

		try (BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileName), encoding))) {
			for (String line : contents) {
				out.write(line + "\r\n");
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
	}

	public String fixSeparator(String path, String fileSeparator) {
		String retrn = path;
		retrn = retrn.replace("\\", fileSeparator);
		retrn = retrn.replace("/", fileSeparator);
		if (retrn.startsWith(fileSeparator)) {
			retrn = retrn.substring(1, retrn.length());
		}
		if (retrn.endsWith(fileSeparator)) {
			retrn = retrn.substring(0, retrn.length() - 1);
		}
		return retrn;
	}

	public boolean fileExists(String fileName) {
		return fileExists(fileName, false);
	}

	public boolean directoryExists(String fileName) {
		return fileExists(fileName, true);
	}

	public boolean fileExists(String fileName, boolean directory) {
		AppSettings appSettings = AppSettings.getAppSettings();
		Guide guide = Guide.getGuide();
		String fileSeparator = appSettings.getFileSeparator();

		String dataDirectory;
		String prefix = "";
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix + fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		String mediaDirectory = fixSeparator(guide.getMediaDirectory(), fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;

		String media = fixSeparator(fileName, fileSeparator);
		LOGGER.debug("CommonFunctions fileExists getMediaFullPath {}", media);
		int intSubDir = media.lastIndexOf(fileSeparator);
		String strSubDir;
		if (intSubDir > -1) {
			strSubDir = fixSeparator(media.substring(0, intSubDir + 1), fileSeparator);
			media = media.substring(intSubDir + 1);
		} else {
			strSubDir = "";
		}
		// String strSubDir
		// no wildcard so just use the file name
		if (strSubDir.equals("")) {
			fileName = dataDirectory + fileSeparator + media;
		} else {
			fileName = dataDirectory + fileSeparator + strSubDir + fileSeparator + media;
		}
		File f = new File(fileName);
		Boolean fileexists = false;
		if (f.exists()) {
			if (f.isFile() && !directory) {
				fileexists = true;
			}
			if (f.isDirectory() && directory) {
				fileexists = true;
			}
		}
		LOGGER.debug("ComonFunctions FileExists check {} {}", fileName, fileexists);
		return fileexists;
	}

	public static String getVersion() {
		return VERSION;
	}

	public String getVarAsString(Object objPassed) {
		StringBuilder returnVal = new StringBuilder();
		try {
			ContextFactory cntxFact = new ContextFactory();
			Context cntx = cntxFact.enterContext();
			cntx.setOptimizationLevel(-1);
			cntx.getWrapFactory().setJavaPrimitiveWrap(false);
			if (objPassed == null) {
				returnVal.append("null");
			} else if (objPassed instanceof NativeArray) {
				NativeArray objArray = (NativeArray) objPassed;
				if (objArray.getLength() > 0) {
					returnVal.append('[');
					for (int i = 0; i < objArray.getLength(); i++) {
						/*
						 * Interesting notation for an array element; but we inherited it from the
						 * original codebase Not sure if this is needed for backwards compatability.
						 */
						returnVal.append('[');
						returnVal.append(objArray.get(i, objArray));
						returnVal.append(']');
					}
					returnVal.append(']');
				}
			} else if (objPassed instanceof NativeObject) {
				returnVal.append('{');
				Object[] propIds = ScriptableObject.getPropertyIds((Scriptable) objPassed);
				for (Object propId : propIds) {
					String key = propId.toString();
					String value = ScriptableObject.getProperty((Scriptable) objPassed, key)
							.toString();
					returnVal.append(key);
					returnVal.append(": ");
					returnVal.append(value);
					returnVal.append(",");
				}
				returnVal.append('}');
			} else if (objPassed instanceof NativeDate) {
				Date dateRet = (Date) Context.jsToJava(objPassed, Date.class);
				returnVal.append(dateRet);
			} else {
				returnVal.append(objPassed);
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		} finally {
			Context.exit();
		}
		return returnVal.toString();
	}

	public String listSubFolders(String folderName) {
		return listSubFolders(folderName, true);
	}

	public String listSubFolders(String folderName, boolean blnArr) {
		String folders = "";
		AppSettings appSettings = AppSettings.getAppSettings();
		Guide guide = Guide.getGuide();
		String fileSeparator = appSettings.getFileSeparator();

		String dataDirectory;
		String prefix = "";
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix + fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		String mediaDirectory = fixSeparator(guide.getMediaDirectory(), fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;

		String media = fixSeparator(folderName, fileSeparator);
		folderName = dataDirectory + fileSeparator + media;
		LOGGER.debug("CommonFunctions ListSubFolders full Path {}", folderName);
		File file = new File(folderName);
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

		StringBuffer builder = new StringBuffer();
		if (blnArr) {
			builder.append("[\"");
			for (String s : directories) {
				builder.append(s);
				builder.append("\", \"");
			}
			int length = builder.length();
			if (length > 2) {
				builder.delete(length - 3, length);
			}
			builder.append("]");
		} else {
			for (String s : directories) {
				builder.append(s);
				builder.append(",");
			}
			int length = builder.length();
			if (length > 0) {
				builder.delete(length - 1, length);
			}
		}

		folders = builder.toString();
		LOGGER.debug("CommonFunctions ListSubFolders returned {}", folders);
		return folders;

	}

	public String listFiles(String folderName) {
		String files = "";
		AppSettings appSettings = AppSettings.getAppSettings();
		Guide guide = Guide.getGuide();
		String fileSeparator = appSettings.getFileSeparator();

		String dataDirectory;
		String prefix = "";
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix + fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		String mediaDirectory = fixSeparator(guide.getMediaDirectory(), fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;

		String media = fixSeparator(folderName, fileSeparator);
		folderName = dataDirectory + fileSeparator + media;
		LOGGER.debug("CommonFunctions ListFiles full Path {}", folderName);
		File file = new File(folderName);
		String[] filesList = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});
		StringBuffer builder = new StringBuffer();
		for (String s : filesList) {
			builder.append(s);
			builder.append(",");
		}
		int length = builder.length();
		if (length > 0) {
			builder.delete(length - 1, length);
		}

		files = builder.toString();
		LOGGER.debug("CommonFunctions ListFiles returned {}", files);
		return files;

	}

	public List<Library> listGuides() {
		AppSettings appSettings = AppSettings.getAppSettings();
		String fileSeparator = appSettings.getFileSeparator();
		ArrayList<Library> guides = new ArrayList<>();

		String dataDirectory;
		String prefix = "";
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix + fixSeparator(appSettings.getDataDirectory(), fileSeparator);

		String libraryDir = dataDirectory + appSettings.getFileSeparator() + "__GuideLibrary";
		Path thumbsDir = Paths.get(libraryDir);
		if (Files.notExists(thumbsDir)) {
			try {
				Files.createDirectories(thumbsDir);
			} catch (IOException ex) {
				LOGGER.error(ex.getLocalizedMessage(), ex);
			}
		}

		LOGGER.debug("CommonFunctions ListGuides {}", dataDirectory);
		File file = new File(dataDirectory);
		String[] filesList = file
				.list((current, name) -> new File(current, name).isFile() && name.endsWith(".xml"));

		for (int i = 0; i < filesList.length; i++) {
			String errorFile = filesList[i];
			boolean addGuide = false;
			String image = "";
			String title = "";
			String fileName = dataDirectory + fileSeparator + filesList[i];
			String media = "";
			String author = "";
			boolean foundTitle = false;
			boolean foundimage = false;
			boolean foundmedia = false;
			boolean foundAuthor = false;
			String fileNameRoot = filesList[i].substring(0, filesList[i].length() - 4);
			String tumbFileName = libraryDir + fileSeparator + fileNameRoot + ".jpg";
			String titleFileName = libraryDir + fileSeparator + fileNameRoot + ".txt";
			Path thumbf = Paths.get(tumbFileName);
			Path titlef = Paths.get(titleFileName);

			if (!Files.exists(thumbf) || !Files.exists(titlef)) {
				try {
					XMLInputFactory inputFactory = XMLInputFactory.newInstance();
					inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
							false);
					InputStream in = new FileInputStream(fileName);
					XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
					while (streamReader.hasNext()) {
						if (streamReader.isStartElement()) {
							switch (streamReader.getLocalName()) {
							case "Title":
								title = streamReader.getElementText().trim();
								foundTitle = true;
								break;
							case "Name":
								author = streamReader.getElementText().trim();
								foundAuthor = true;
								break;
							case "MediaDirectory":
								media = streamReader.getElementText().trim();
								foundmedia = true;
								break;
							case "Image":
								image = streamReader.getAttributeValue(null, "id").trim();
								if (image == null)
									image = "";
								if (!image.contains("*") && image.endsWith(".jpg")) {
									File fileCheck = new File(dataDirectory + fileSeparator + media
											+ fileSeparator + image);
									if (fileCheck.isFile() && fileCheck.length() > 10000) {
										foundimage = true;
									}
								}
								break;
							}
						}
						// if we have found everything exit the while
						if (foundTitle && foundimage && foundmedia && foundAuthor)
							break;
						streamReader.next();
					}
					if (!foundimage && foundmedia) {
						image = getRandomFile("*.jpg", "", false, media);
						foundimage = true;
					}
					if (title.equals("")) {
						title = fileNameRoot;
						foundTitle = true;
					}
					if (foundTitle && foundimage && foundmedia) {
						BufferedImage img = null;
						int thumb = appSettings.getThumbnailSize();
						int oldHeight;
						int oldWidth;
						int newWidth;
						int newHeight;
						double factor;
						image = dataDirectory + fileSeparator + media + fileSeparator + image;
						ImageIO.setUseCache(false);
						img = ImageIO.read(new File(image));
						if (img.getColorModel().hasAlpha()) {
							img = dropAlphaChannel(img);
						}
						oldHeight = img.getHeight();
						oldWidth = img.getWidth();
						factor = (double) oldHeight / (double) oldWidth;
						if (factor < 1) {
							factor = (double) oldWidth / (double) oldHeight;
							newWidth = (int) (thumb * factor);
							newHeight = thumb;
						} else {
							newWidth = thumb;
							newHeight = (int) (thumb * factor);
						}

						BufferedImage imageNew = Scalr.resize(img, Scalr.Method.QUALITY,
								Scalr.Mode.AUTOMATIC, newWidth, newHeight, Scalr.OP_ANTIALIAS);
						File outputfile = new File(tumbFileName);
						ImageIO.write(imageNew, "jpg", outputfile);
						image = tumbFileName;
						String titleFile = title + "\r\n" + author;
						writeFile(titleFileName, titleFile);
						addGuide = true;
					}
				} catch (Exception ex) {
					LOGGER.error("ListGuides:" + errorFile + " " + ex.getLocalizedMessage(), ex);
				}
			} else {
				image = tumbFileName;
				String titleFile = readFile(titleFileName, StandardCharsets.UTF_8);
				String[] lines = titleFile.split("\\r?\\n");
				if (lines.length > 0)
					title = lines[0];
				if (lines.length > 1)
					author = lines[1];
				addGuide = true;
			}
			if (addGuide) {
				File file1 = new File(fileName);
				Date date = new Date(file1.lastModified());
				Library guide = new Library(image, title, fileName, author, date);
				guides.add(guide);
			}
		}

		LOGGER.debug(() -> "CommonFunctions ListGuides returned " + getVarAsString(filesList));
		return guides;

	}

	private void writeFile(String fileName, String contents) {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName))) {
			out.write(contents); // Replace with the string
		} catch (IOException ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}

	}

	public String getRandomFile(String wildcard, String strSubDir) {
		return getRandomFile(wildcard, strSubDir, false);
	}

	public String getRandomFile(String wildcard, String strSubDir, boolean fullPath) {
		Guide guide = Guide.getGuide();
		String guideMediaDirectory = guide.getMediaDirectory();
		return getRandomFile(wildcard, strSubDir, fullPath, guideMediaDirectory);
	}

	public String getRandomFile(String wildcard, String strSubDir, boolean fullPath,
			String guideMediaDirectory) {
		String mediaFound = "";
		AppSettings appSettings = AppSettings.getAppSettings();
		String fileSeparator = appSettings.getFileSeparator();

		String dataDirectory;
		String prefix = "";
		dataDirectory = appSettings.getDataDirectory();
		if (dataDirectory.startsWith("/")) {
			prefix = "/";
		}
		dataDirectory = prefix + fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		String mediaDirectory = fixSeparator(guideMediaDirectory, fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;

		// get the directory
		File f = new File(dataDirectory + fileSeparator + strSubDir);
		// wildcard filter class handles the filtering
		WildCardFileFilter wildCardfilter = new WildCardFileFilter();
		wildCardfilter.setFilePatern(wildcard);
		if (f.isDirectory()) {
			// return a list of matching files
			File[] children = f.listFiles(wildCardfilter);
			// return a random image
			int intFile = comonFunctions.getRandom(0, (children.length - 1));
			LOGGER.debug("displayPage Random Media Index {}", intFile);
			if (strSubDir.equals("")) {
				if (fullPath) {
					mediaFound = dataDirectory + fileSeparator + children[intFile].getName();
				} else {
					mediaFound = children[intFile].getName();
				}
			} else {
				if (fullPath) {
					mediaFound = dataDirectory + fileSeparator + strSubDir + fileSeparator
							+ children[intFile].getName();
				} else {
					mediaFound = strSubDir + fileSeparator + children[intFile].getName();
				}
			}
			LOGGER.debug("GetRandomFile Random Media Chosen {}", mediaFound);
		}
		return mediaFound;

	}

	// Wildecard filter
	public class WildCardFileFilter implements java.io.FileFilter {
		// Apply the wildcard filter to the file list
		private String strFilePatern;

		public void setFilePatern(String strFilePatern) {
			// regular patern to search for
			this.strFilePatern = strFilePatern;
		}

		@Override
		public boolean accept(File f) {
			try {
				// ignore hidden files and directories
				if (f.isHidden() || f.isDirectory() || f.getName().equalsIgnoreCase("thumbs.db")) {
					LOGGER.debug(() -> "WildCardFileFilter No Match {}" + f.getName());
					return false;
				}
				// convert the regular patern to regex
				String strPattern = strFilePatern.toLowerCase();
				String text = f.getName().toLowerCase();
				String strFile = text;
				// .* in regex matches any number of characters
				strPattern = strPattern.replace("*", ".*");
				// test for a match
				if (!text.matches(strPattern)) {
					LOGGER.debug("WildCardFileFilter No Match {}", strFile);
					return false;
				}

				LOGGER.debug("WildCardFileFilter Match {}", strFile);
				return true;
			} catch (Exception e) {
				LOGGER.error("WildCardFileFilter.accept Exception ", e);
				return false;
			}
		}
	}

	public OSFamily getOs() {
		return osFamily;
	}

	public boolean onWindows() {
		return osFamily == OSFamily.Windows;
	}

	public boolean onMac() {
		return osFamily == OSFamily.Mac;
	}

	public boolean onUnix() {
		return osFamily == OSFamily.Unix;
	}

	public long dateDifference(String type, Object jsdate1, Object jsdate2) {
		Date date1 = (Date) Context.jsToJava(jsdate1, Date.class);
		Date date2 = (Date) Context.jsToJava(jsdate2, Date.class);
		long diffInMillies = date2.getTime() - date1.getTime();
		long returnVal = 0;
		if (type.equals("d")) {
			returnVal = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		}
		if (type.equals("h")) {
			returnVal = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		}
		if (type.equals("m")) {
			returnVal = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
		}
		if (type.equals("s")) {
			returnVal = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		}
		return returnVal;

	}

	// TODO, weird function. Why not just use the enums?
	public org.eclipse.swt.graphics.Color getColor(String color) {
		org.eclipse.swt.graphics.Color swtColor;
		switch (color) {
		case "white":
			swtColor = display.getSystemColor(SWT.COLOR_WHITE);
			break;
		case "black":
			swtColor = display.getSystemColor(SWT.COLOR_BLACK);
			break;
		case "dark_red":
			swtColor = display.getSystemColor(SWT.COLOR_DARK_RED);
			break;
		case "dark_green":
			swtColor = display.getSystemColor(SWT.COLOR_DARK_GREEN);
			break;
		case "dark_yellow":
			swtColor = display.getSystemColor(SWT.COLOR_DARK_YELLOW);
			break;
		case "dark_blue":
			swtColor = display.getSystemColor(SWT.COLOR_DARK_BLUE);
			break;
		case "dark_magenta":
			swtColor = display.getSystemColor(SWT.COLOR_DARK_MAGENTA);
			break;
		case "dark_cyan":
			swtColor = display.getSystemColor(SWT.COLOR_DARK_CYAN);
			break;
		case "dark_gray":
			swtColor = display.getSystemColor(SWT.COLOR_DARK_GRAY);
			break;
		case "gray":
			swtColor = display.getSystemColor(SWT.COLOR_GRAY);
			break;
		case "red":
			swtColor = display.getSystemColor(SWT.COLOR_RED);
			break;
		case "green":
			swtColor = display.getSystemColor(SWT.COLOR_GREEN);
			break;
		case "yellow":
			swtColor = display.getSystemColor(SWT.COLOR_YELLOW);
			break;
		case "blue":
			swtColor = display.getSystemColor(SWT.COLOR_BLUE);
			break;
		case "magenta":
			swtColor = display.getSystemColor(SWT.COLOR_MAGENTA);
			break;
		case "cyan":
			swtColor = display.getSystemColor(SWT.COLOR_CYAN);
			break;
		default:
			swtColor = display.getSystemColor(SWT.COLOR_WHITE);
			break;
		}
		return swtColor;
	}

	public Color getSwtColor(int colorId) {
		return display.getSystemColor(colorId);
	}

	public org.eclipse.swt.graphics.Color decodeHexColor(String hexString) {
		try {
			java.awt.Color c = java.awt.Color.decode(hexString);

			return new org.eclipse.swt.graphics.Color(display, c.getRed(), c.getGreen(),
					c.getBlue());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public String getMediaFullPath(String mediaFile, String fileSeparator, AppSettings appSettings,
			Guide guide) {
		String mediaFound = "";
		String dataDirectory;
		String prefix = "";
		if (guide.getInPrefGuide()) {
			dataDirectory = appSettings.getUserDir();
		} else {
			dataDirectory = appSettings.getDataDirectory();
			if (dataDirectory.startsWith("/")) {
				prefix = "/";
			}
			dataDirectory = prefix
					+ comonFunctions.fixSeparator(appSettings.getDataDirectory(), fileSeparator);
		}
		String mediaDirectory = comonFunctions.fixSeparator(guide.getMediaDirectory(),
				fileSeparator);
		dataDirectory = dataDirectory + fileSeparator + mediaDirectory;

		String media = comonFunctions.fixSeparator(mediaFile, fileSeparator);
		LOGGER.debug("displayPage getMediaFullPath {}", media);
		int intSubDir = media.lastIndexOf(fileSeparator);
		String strSubDir;
		if (intSubDir > -1) {
			strSubDir = comonFunctions.fixSeparator(media.substring(0, intSubDir + 1),
					fileSeparator);
			media = media.substring(intSubDir + 1);
		} else {
			strSubDir = "";
		}
		// String strSubDir
		// Handle wildcard *
		if (media.indexOf("*") > -1) {
			mediaFound = comonFunctions.getRandomFile(media, strSubDir, true);
		} else {
			// no wildcard so just use the file name
			if (strSubDir.equals("")) {
				mediaFound = dataDirectory + fileSeparator + media;
			} else {
				mediaFound = dataDirectory + fileSeparator + strSubDir + fileSeparator + media;
			}
			LOGGER.debug("displayPage Non Random Media {}", mediaFound);
		}

		return mediaFound;
	}

	public String substituteTextVars(String inString, GuideSettings guideSettings,
			UserSettings userSettings) {
		String retString = inString;
		// Script Variables
		Set<String> set = guideSettings.getScriptVariables().keySet();
		String varValue;
		for (String s : set) {
			try {
				Object objVar = guideSettings.getScriptVariables().get(s);
				varValue = comonFunctions.getVarAsString(objVar);
				if (!varValue.equals("")) {
					retString = retString.replace("<span>" + s + "</span>", varValue);
				}
			} catch (Exception e) {
				LOGGER.error("displayPage BrwsText ScriptVariables Exception " + s + " "
						+ e.getLocalizedMessage(), e);
			}
		}

		// Guide Preferences
		set = guideSettings.getKeys();
		String numberRet;
		String type;
		for (String s : set) {
			try {
				type = guideSettings.getPrefType(s);
				if (type.equals("String")) {
					retString = retString.replace("<span>" + s + "</span>",
							guideSettings.getPref(s));
				}
				if (type.equals("Number")) {
					numberRet = formatNumPref(guideSettings.getPrefNumber(s));
					retString = retString.replace("<span>" + s + "</span>", numberRet);
				}
			} catch (Exception e) {
				LOGGER.error("displayPage BrwsText String Guide Preferences Exception " + s + " "
						+ e.getLocalizedMessage(), e);
			}
		}

		// String User Preferences
		set = userSettings.getStringKeys();
		for (String s : set) {
			try {
				retString = retString.replace("<span>" + s + "</span>", userSettings.getPref(s));
			} catch (Exception e) {
				LOGGER.error("displayPage BrwsText String User Preferences Exception " + s + " "
						+ e.getLocalizedMessage(), e);
			}
		}

		// Number User Preferences
		set = userSettings.getNumberKeys();
		for (String s : set) {
			try {
				retString = retString.replace("<span>" + s + "</span>",
						formatNumPref(userSettings.getPrefNumber(s)));
			} catch (Exception e) {
				LOGGER.error("displayPage BrwsText Number User Preferences Exception " + s + " "
						+ e.getLocalizedMessage(), e);
			}
		}
		return retString;
	}

	private String formatNumPref(double prefNumber) {
		if (prefNumber == (int) prefNumber)
			return String.format("%d", (int) prefNumber);
		else
			return String.format("%s", prefNumber);
	}

	// Set script variables from the scriptVar node in the guide xml
	public void processSrciptVars(String scriptVars, GuideSettings guidesettings) {
		try {
			String[] vars = scriptVars.split(",");
			for (String var : vars) {
				String[] parts = var.split("=");
				try {
					if (!parts[0].equals("")) {
						guidesettings.setScriptVar(parts[0], parts[1]);
					}
				} catch (Exception e) {
					LOGGER.error("scriptVar can't set " + var + " " + e.getLocalizedMessage(), e);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("scriptVar processSrciptVars " + ex.getLocalizedMessage(), ex);
		}
	}

	public boolean searchGuide(String searchText, String path) {
		boolean found = false;
		try {
			String file = readFile(path, StandardCharsets.UTF_8);
			found = searchText(searchText, file);
		} catch (Exception ex) {
			LOGGER.error("searchGuide " + ex.getLocalizedMessage(), ex);
		}
		return found;
	}

	// TODO, what is this?
	public boolean searchText(String searchText, String text) {
		boolean found = false;
		try {
			String[] splitSearch = searchText.split(",");
			String lowerText = text.toLowerCase();
			int includeCount = 0;
			for (String test : splitSearch) {
				if (!test.startsWith("-") && !test.startsWith("+"))
					includeCount++;
			}
			boolean include = includeCount == 0;
			boolean exclude = false;
			for (String test : splitSearch) {
				if (test.startsWith("-")) {
					exclude = lowerText.contains(test.substring(1).toLowerCase());
					if (exclude)
						break;
				} else {
					if (test.startsWith("+")) {
						exclude = !lowerText.contains(test.substring(1).toLowerCase());
						if (exclude)
							break;
					}
					include = include || lowerText.contains(test.toLowerCase());
				}
			}
			found = include && !exclude;
		} catch (Exception ex) {
			LOGGER.error("searchText " + ex.getLocalizedMessage(), ex);
		}
		return found;
	}

	public Image cropImageWidth(Image originalImage, int width) {
		if (originalImage.getBounds().width <= width) {
			return originalImage;
		}
		Image newImage = originalImage;
		try {
			int height = originalImage.getBounds().height;
			newImage = new Image(null, width, height);
			GC gc = new GC(newImage);
			int cropWidth = originalImage.getBounds().width - width;

			gc.drawImage(originalImage, (cropWidth / 2), 0,
					originalImage.getBounds().width - cropWidth, originalImage.getBounds().height,
					0, 0, width, height);
			gc.dispose();
		} catch (Exception ex) {
			LOGGER.error("cropImageWidth " + ex.getLocalizedMessage(), ex);
		}
		return newImage;
	}

	public String splitButtonText(String text, int width) {
		StringBuilder splitText = new StringBuilder();
		try {
			String remainingText = text;
			if (text.length() <= width) {
				return text;
			}
			while (remainingText.length() > 0) {
				if (splitText.length() > 0) {
					splitText = splitText.append('\n');
				}
				if (remainingText.length() > width) {
					int pos = remainingText.lastIndexOf(" ", width);
					if (pos > 0) {
						splitText = splitText.append(remainingText.substring(0, pos));
						remainingText = remainingText.substring(pos + 1);
					} else {
						splitText = splitText.append(remainingText.substring(0, width));
						remainingText = remainingText.substring(width + 1);
					}
				} else {
					splitText = splitText.append(remainingText);
					remainingText = "";
				}
			}
		} catch (Exception ex) {
			LOGGER.error("splitButtonText " + ex.getLocalizedMessage(), ex);
		}
		return splitText.toString();
	}

	public void compressGuide(String guide) {
		AppSettings appSettings = AppSettings.getAppSettings();
		String password = "Gu1deM3!";
		int pos = guide.lastIndexOf(appSettings.getFileSeparator());
		String mediaDir = guide.substring(0, pos + 1) + getMediaDirFromGuide(guide);
		File guideFile = new File(guide);
		File mediaFolder = new File(mediaDir);
		String zipFileName = guide.replace(".xml", ".zip");
		try {
			ZipFile compressedFile = new ZipFile(zipFileName);
			ZipParameters zipParam = new ZipParameters();
			zipParam.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			zipParam.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			zipParam.setEncryptFiles(true);
			zipParam.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
			zipParam.setPassword(password);
			compressedFile.addFile(guideFile, zipParam);
			compressedFile.addFolder(mediaFolder, zipParam);
		} catch (ZipException e) {
			LOGGER.error("CompressGuide " + e.getLocalizedMessage(), e);
		}
	}

	public void unCompressGuide(String guide) {
		AppSettings appSettings = AppSettings.getAppSettings();
		String password = "Gu1deM3!";
		try {
			ZipFile zipFile = new ZipFile(guide);
			if (zipFile.isEncrypted()) {
				zipFile.setPassword(password);
			}
			zipFile.extractAll(appSettings.getDataDirectory());
		} catch (ZipException e) {
			LOGGER.error("UnCompressGuide " + e.getLocalizedMessage(), e);
		}
	}

	public void resizeGuideImages(String guide, ImageManager imageManager) {
		AppSettings appSettings = AppSettings.getAppSettings();
		int pos = guide.lastIndexOf(appSettings.getFileSeparator());
		String mediaDir = guide.substring(0, pos + 1) + getMediaDirFromGuide(guide);
		File mediaFolder = new File(mediaDir);
		resizeFolderImages(mediaFolder, imageManager);
	}

	private void resizeFolderImages(File folder, ImageManager imageManager) {
		for (File fileItem : folder.listFiles()) {
			if (fileItem.isDirectory())
				resizeFolderImages(fileItem, imageManager);
			if (fileItem.isFile())
				imageManager.scaleImageOnDisk(fileItem);
		}
	}

	public String getMediaDirFromGuide(String fileName) {
		String media = "";
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			InputStream in = new FileInputStream(fileName);
			XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
			boolean foundmedia = false;
			while (streamReader.hasNext()) {
				if (streamReader.isStartElement()
						&& (streamReader.getLocalName().equals("MediaDirectory"))) {
					media = streamReader.getElementText().trim();
					foundmedia = true;
				}
				// if we have found it
				if (foundmedia)
					break;
				streamReader.next();
			}
		} catch (Exception e) {
			LOGGER.error("GetMediaDirFromGuide " + e.getLocalizedMessage(), e);
		}
		return media;
	}

	public static BufferedImage convertToAWT(ImageData data) {
		ColorModel colorModel = null;
		PaletteData palette = data.palette;
		if (palette.isDirect) {
			colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask,
					palette.blueMask);
			BufferedImage bufferedImage = new BufferedImage(colorModel,
					colorModel.createCompatibleWritableRaster(data.width, data.height), false,
					null);
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					RGB rgb = palette.getRGB(pixel);
					bufferedImage.setRGB(x, y, rgb.red << 16 | rgb.green << 8 | rgb.blue);
				}
			}
			return bufferedImage;
		} else {
			RGB[] rgbs = palette.getRGBs();
			byte[] red = new byte[rgbs.length];
			byte[] green = new byte[rgbs.length];
			byte[] blue = new byte[rgbs.length];
			for (int i = 0; i < rgbs.length; i++) {
				RGB rgb = rgbs[i];
				red[i] = (byte) rgb.red;
				green[i] = (byte) rgb.green;
				blue[i] = (byte) rgb.blue;
			}
			if (data.transparentPixel != -1) {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue,
						data.transparentPixel);
			} else {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
			}
			BufferedImage bufferedImage = new BufferedImage(colorModel,
					colorModel.createCompatibleWritableRaster(data.width, data.height), false,
					null);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					pixelArray[0] = pixel;
					raster.setPixel(x, y, pixelArray);
				}
			}
			return bufferedImage;
		}
	}

	public static ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(),
					colorModel.getGreenMask(), colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(), palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette
							.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}

	public BufferedImage dropAlphaChannel(BufferedImage src) {
		BufferedImage convertedImg = new BufferedImage(src.getWidth(), src.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		convertedImg.getGraphics().drawImage(src, 0, 0, null);

		return convertedImg;
	}

	public boolean canCreateFile(String fileName) {
		File file = new File(fileName);
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		return file.exists();
	}

	public Object getSavedObject(String attribute, String strType, Scriptable scope) {

		ContextFactory cntxFact = new ContextFactory();
		try (Context context = cntxFact.enterContext()) {
			context.setOptimizationLevel(-1);
			context.getWrapFactory().setJavaPrimitiveWrap(false);

			byte[] decodedBytes;

			switch (strType) {
			case "Scope":
				LOGGER.trace("GuideSettings getSavedObject scope");
				decodedBytes = Base64.decodeBase64(attribute.getBytes());
				try (ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
						ScriptableInputStream sis = new ScriptableInputStream(bis, scope)) {
					return sis.readObject();
				}
			case "org.mozilla.javascript.NativeArray":
				LOGGER.trace("GuideSettings getSavedObject NativeArray");
				decodedBytes = Base64.decodeBase64(attribute.getBytes());
				try (ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
						ScriptableInputStream sis = new ScriptableInputStream(bis, scope)) {
					return ScriptValueConverter.wrapValue(scope, sis.readObject());
				}
			case "org.mozilla.javascript.NativeObject":
				LOGGER.trace("GuideSettings getSavedObject NativeObject");
				decodedBytes = Base64.decodeBase64(attribute.getBytes());
				try (ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
						ScriptableInputStream sis = new ScriptableInputStream(bis, scope)) {
					return sis.readObject();
				}
			case "org.mozilla.javascript.NativeDate":
				LOGGER.trace("GuideSettings getSavedObject NativeDate");
				decodedBytes = Base64.decodeBase64(attribute.getBytes());
				try (ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
						ScriptableInputStream sis = new ScriptableInputStream(bis, scope)) {
					return sis.readObject();
				}

			case "java.lang.Double":
				LOGGER.trace("GuideSettings getSavedObject Double");
				Double restoredDouble = Double.parseDouble(attribute);
				return restoredDouble;
			case "java.lang.Boolean":
				LOGGER.trace("GuideSettings getSavedObject Boolean");
				Boolean restoredBoolean = Boolean.parseBoolean(attribute);
				return restoredBoolean;
			default:
				return attribute;
			}

		} catch (IOException | ClassNotFoundException e) {
			LOGGER.warn("Error getting saved object of type {}: '{}'", strType, attribute, e);
			return null;
		}
	}

	public String createSaveObject(Object value, String strType, Scriptable scope) {
		String returnVal = "";
		if (value != null) {
			returnVal = value.toString();
			if (strType.equals("org.mozilla.javascript.NativeArray")
					|| strType.equals("org.mozilla.javascript.NativeObject")
					|| strType.equals("org.mozilla.javascript.NativeDate")
					|| strType.equals("Scope")) {
				try {
					ContextFactory cntxFact = new ContextFactory();
					Context cntx = cntxFact.enterContext();
					cntx.setOptimizationLevel(-1);
					cntx.getWrapFactory().setJavaPrimitiveWrap(false);
					String fromApacheBytes = "";
					if (strType.equals("Scope")) {
						LOGGER.trace("GuideSettings createSaveObject Scope");
						Scriptable saveScope = (Scriptable) value;
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ScriptableOutputStream os = new ScriptableOutputStream(bos, scope);
						os.writeObject(saveScope);

						byte[] encodedBytes = Base64.encodeBase64(bos.toByteArray());
						fromApacheBytes = new String(encodedBytes);
						os.close();
					}
					if (strType.equals("org.mozilla.javascript.NativeArray")) {
						LOGGER.trace("GuideSettings createSaveObject NativeArray");
						NativeArray nativeValue = (NativeArray) value;
						Scriptable localScope = nativeValue.getParentScope();
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ScriptableOutputStream os = new ScriptableOutputStream(bos, localScope);
						os.writeObject(ScriptValueConverter.unwrapValue(nativeValue));

						byte[] encodedBytes = Base64.encodeBase64(bos.toByteArray());
						fromApacheBytes = new String(encodedBytes);
						os.close();
					}
					if (strType.equals("org.mozilla.javascript.NativeObject")) {
						LOGGER.trace("GuideSettings createSaveObject NativeObject");
						NativeObject nativeValue = (NativeObject) value;
						Scriptable localScope = nativeValue.getParentScope();
						String type = localScope.getClass().getName();
						if (type.equals("org.mozilla.javascript.NativeCall")) {
							localScope = scope;
							type = localScope.getClass().getName();
						}
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ScriptableOutputStream os = new ScriptableOutputStream(bos, localScope);
						os.writeObject(nativeValue);

						byte[] encodedBytes = Base64.encodeBase64(bos.toByteArray());
						fromApacheBytes = new String(encodedBytes);
						os.close();
					}
					if (strType.equals("org.mozilla.javascript.NativeDate")) {
						LOGGER.trace("GuideSettings createSaveObject NativeDate");
						NativeDate nativeValue = (NativeDate) value;
						Scriptable localScope = nativeValue.getParentScope();
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ScriptableOutputStream os = new ScriptableOutputStream(bos, localScope);
						os.writeObject(nativeValue);

						byte[] encodedBytes = Base64.encodeBase64(bos.toByteArray());
						fromApacheBytes = new String(encodedBytes);
						os.close();
					}
					returnVal = fromApacheBytes;
				} catch (Exception ex) {
					LOGGER.error(ex.getLocalizedMessage(), ex);
					returnVal = "ignore";
				}
				Context.exit();
			}
		}
		return returnVal;
	}

}
