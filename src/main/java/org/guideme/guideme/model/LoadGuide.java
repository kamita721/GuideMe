package org.guideme.guideme.model;

import java.time.LocalTime;
import java.util.List;

import static org.guideme.guideme.util.XMLReaderUtils.getAttributeLocalTime;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

import javax.xml.stream.XMLStreamReader;

import org.guideme.guideme.scripting.functions.ComonFunctions;

public class LoadGuide {

	private final String guidePath; //path to guide to load
	private final String target; //page to load in new guide
	private final String returnTarget; //page to return to when this guide is loaded next
	private final String prejScript;
	private final String postjScript;
	private final String ifSet;
	private final String ifNotSet;
	private LocalTime ifBefore; //Time of day must be before this time
	private LocalTime ifAfter; //Time of day must be after this time
	private ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();

	public LoadGuide(XMLStreamReader reader) {
		this.guidePath = getAttributeOrDefaultNoNS(reader, "guidePath", "");
		this.target = getAttributeOrDefaultNoNS(reader, "target", "");
		this.returnTarget = getAttributeOrDefaultNoNS(reader, "return-target", "");
		this.prejScript = getAttributeOrDefaultNoNS(reader, "preScript", "");
		this.postjScript = getAttributeOrDefaultNoNS(reader, "postScript", "");
		this.ifSet = getAttributeOrDefaultNoNS(reader, "if-set", "");
		this.ifNotSet = getAttributeOrDefaultNoNS(reader, "if-not-set", "");
		this.ifBefore = getAttributeLocalTime(reader, "if-before");
		this.ifAfter = getAttributeLocalTime(reader, "if-after");
	}
	
	public LoadGuide(String guidePath, String target, String returnTarget, String ifSet, String ifNotSet, String ifAfter, String ifBefore, String prejScript, String postjScript) {
		this.guidePath = guidePath;
		this.target = target;
		this.returnTarget = returnTarget;
		this.prejScript = prejScript;
		this.postjScript = postjScript;
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

	public String getGuidePath() {
		return guidePath;
	}

	public String getTarget() {
		return target;
	}

	public String getReturnTarget() {
		return returnTarget;
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

	public String getPrejScript() {
		return prejScript;
	}

	public String getPostjScript() {
		return postjScript;
	}


}
