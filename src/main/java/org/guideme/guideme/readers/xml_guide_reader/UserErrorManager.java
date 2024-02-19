package org.guideme.guideme.readers.xml_guide_reader;

import javax.xml.stream.Location;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserErrorManager {	
	private static Logger logger = LogManager.getLogger();

	public static void log(String message, Location location) {
		//TODO make an easy way for the user to view these errors
		logger.error("{} \n{}", message, location);

	}
}
