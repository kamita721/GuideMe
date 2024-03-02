package org.guideme.guideme.util;

public class StringUtil {
	private StringUtil() {
		
	}
	
	public static String capitalizeFirstChar(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
