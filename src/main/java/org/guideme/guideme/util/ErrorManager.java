package org.guideme.guideme.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO, make a nice UI for showing errors
public class ErrorManager {
	private static ErrorManager instance;

	private static final Logger LOGGER = LogManager.getLogger();
	
	synchronized public static ErrorManager getInstance() {
		if (instance == null) {
			instance = new ErrorManager();
		}
		return instance;
	}
	
	public void recordError(Exception e, String description) {
		LOGGER.error(description, e);
	}
}
