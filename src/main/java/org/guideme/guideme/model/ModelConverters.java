package org.guideme.guideme.model;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.Color;

public class ModelConverters {
	private static final Logger LOGGER = LogManager.getLogger();

	private ModelConverters() {

	}

	public static String toString(Color c) {
		if (c == null) {
			return null;
		}
		String[] data = new String[4];
		data[0] = Integer.toString(c.getRed());
		data[1] = Integer.toString(c.getGreen());
		data[2] = Integer.toString(c.getBlue());
		data[3] = Integer.toString(c.getAlpha());
		return String.join(",", data);
	}

	public static Color fromString(String data, Color defaultValue) {
		if (data == null) {
			return defaultValue;
		}
		String[] rgba = data.split("\\,");
		if (rgba.length != 4) {
			LOGGER.warn("Cannot convert saved color description '{}' to Color", data);
			return defaultValue;
		}
		int[] irgba = new int[4];
		try {
			irgba[0] = Integer.parseInt(rgba[0]);
			irgba[1] = Integer.parseInt(rgba[1]);
			irgba[2] = Integer.parseInt(rgba[2]);
			irgba[3] = Integer.parseInt(rgba[3]);
		} catch (NumberFormatException e) {
			LOGGER.warn("Cannot convert saved color description '{}' to Color", data);
			return defaultValue;
		}
		return new Color(irgba[0], irgba[1], irgba[2], irgba[3]);
	}

	public static String toString(boolean b) {
		return Boolean.toString(b);
	}

	public static boolean fromString(String data, boolean defaultValue) {
		if (data == null) {
			return defaultValue;
		}
		return Boolean.parseBoolean(data);
	}

	public static String toString(int i) {
		return Integer.toString(i);
	}

	public static int fromString(String data, int defaultValue) {
		if (data == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(data);
		} catch (NumberFormatException e) {
			LOGGER.warn("Cannot convert saved description '{}' to an integer.", data, e);
			return defaultValue;
		}
	}

	public static String toString(String str) {
		return str;
	}

	public static String fromString(String data, String defaultValue) {
		if (data == null) {
			return defaultValue;
		}
		return data;
	}

	public static String toString(LocalTime t) {
		if (t == null) {
			return null;
		}
		return Long.toString(t.toNanoOfDay());
	}

	public static LocalTime fromString(String data, LocalTime defaultValue) {
		if (data == null) {
			return defaultValue;
		}
		try {
			long l = Long.parseLong(data);
			return LocalTime.ofNanoOfDay(l);
		} catch (NumberFormatException | DateTimeException e) {
			LOGGER.warn("Cannot convert saved description '{}' to a LocalTime.", data, e);
			return defaultValue;
		}
	}

	public static String toString(Calendar cal) {
		throw new NotImplementedException();
	}

	public static Calendar fromString(String data, Calendar defaultValue) {
		throw new NotImplementedException();
	}

	public static String toString(GlobalButtonAction action) {
		if (action == null) {
			return null;
		}
		return GlobalButtonAction.toString(action);
	}

	public static GlobalButtonAction fromString(String data, GlobalButtonAction defaultValue) {
		if (data == null) {
			return defaultValue;
		}
		return GlobalButtonAction.fromString(data);
	}

	public static String toString(GlobalButtonPlacement placement) {
		if (placement == null) {
			return null;
		}
		return GlobalButtonPlacement.toString(placement);
	}

	public static GlobalButtonPlacement fromString(String data,
			GlobalButtonPlacement defaultValue) {
		if (data == null) {
			return defaultValue;
		}
		return GlobalButtonPlacement.fromString(data);
	}

	public static String toString(List<?> list) {
		throw new NotImplementedException();
	}
	public static<T> List<T> fromString(String attrValue, List<T> list) {
		throw new NotImplementedException();
	}

}
