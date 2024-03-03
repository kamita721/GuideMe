package org.guideme.guideme.model;

public enum GlobalButtonPlacement {
	TOP, BOTTOM;

	public static GlobalButtonPlacement fromString(String s) {
		switch (s) {
		case "top":
			return TOP;
		case "bottom":
			return BOTTOM;
		case null:
		default:
			return BOTTOM;
		}
	}
}