package org.guideme.guideme.model;

public enum GlobalButtonAction {
	ADD, REMOVE, NONE;

	public static GlobalButtonAction fromString(String s) {
		switch (s) {
		case "add":
			return ADD;
		case "remove":
			return REMOVE;
		case "none":
			return NONE;
		case null:
		default:
			return NONE;
		}
	}

	public static String toString(GlobalButtonAction action) {
		switch (action) {
		case ADD:
			return "add";
		case REMOVE:
			return "remove";
		case NONE:
			return "none";
		default:
			return "none";
		}
	}
}