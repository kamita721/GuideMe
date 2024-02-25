package org.guideme.guideme.scripting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.debugger.Main;

public class Debug {
	private final Jscript parent;

	private static Logger logger = LogManager.getLogger();
	private static final Marker JSCRIPT_MARKER = MarkerManager.getMarker("JSCRIPT");
	
	public Debug(Jscript parent) {
		this.parent = parent;
	}

	public void log(String msg) {
		logger.info(JSCRIPT_MARKER, msg);
		parent.getGuide().updateJConsole(msg);
	}
	
}
