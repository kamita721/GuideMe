package org.guideme.guideme.model;

import static org.guideme.guideme.util.XMLReaderUtils.getAttributeLocalTime;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

import java.time.LocalTime;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.guideme.guideme.scripting.functions.ComonFunctions;

public class Webcam
{
	private final String ifSet;
	private final String ifNotSet;
	private LocalTime ifBefore; //Time of day must be before this time
	private LocalTime ifAfter; //Time of day must be after this time
	private final ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private boolean webCamFound = false;

	public Webcam(XMLStreamReader reader) {
		this.ifSet = getAttributeOrDefaultNoNS(reader, "if-set", "");
		this.ifNotSet = getAttributeOrDefaultNoNS(reader, "if-not-set", "");
		this.ifBefore = getAttributeLocalTime(reader, "if-before");
		this.ifAfter = getAttributeLocalTime(reader, "if-after");
	}
	
	public boolean getWebCamFound() {
		return webCamFound;
	}

	public void setWebCamFound(Boolean webCamFound) {
		this.webCamFound = webCamFound;
	}

	public Webcam(String ifSet, String ifNotSet, String ifAfter, String ifBefore)
	{
		this.ifSet = ifSet;
		this.ifNotSet = ifNotSet;
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

	}

	public boolean canShow(List<String> setList) {
		boolean retVal = comonFunctions.canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal =  comonFunctions.canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}

	public String getIfSet() {
		return ifSet;
	}

	public String getIfNotSet() {
		return ifNotSet;
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


}