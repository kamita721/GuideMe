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

	public static String toString(GlobalButtonPlacement placement) {
		switch (placement) {
		case TOP:
			return "top";
		case BOTTOM:
			return "bottom";
		default:
			return "bottom";
		}
	}
}