package org.guideme.guideme.model;

public class GotoButton extends Button {
	
	private GotoButton(String target, String text, String ifSet,
			String ifNotSet, String set, String unSet, String jScript, String image) {
		super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, "");
	}

	public static final String DEFAULT_TEXT = "continue";
	
	public GotoButton(String target) {
		this(target, DEFAULT_TEXT, "", "", "", "", "", "");
	}

}
