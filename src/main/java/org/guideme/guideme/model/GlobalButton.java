package org.guideme.guideme.model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

public class GlobalButton extends Button {
	private GlobalButtonPlacement placement;
	private GlobalButtonAction action;

	public GlobalButton(XMLStreamReader reader) throws XMLStreamException {
		super(reader);
	}

	@Override
	protected void initAttributes(XMLStreamReader reader) {
		String sPlacement = getAttributeOrDefaultNoNS(reader, "placement", "bottom");
		this.placement = GlobalButtonPlacement.fromString(sPlacement);

		String sAction = getAttributeOrDefaultNoNS(reader, "action", "none");
		this.action = GlobalButtonAction.fromString(sAction);
	}

	public GlobalButton(String id, String target, String text) {
		super(target, text, "", "", "", "", "", "", "", "", 0, "", "", "", 1, "", "", false, id, "",
				false);
	}

	public GlobalButton(String id, String target, String text, GlobalButtonPlacement placement, GlobalButtonAction action) {
		super(target, text, "", "", "", "", "", "", "", "", 0, "", "", "", 1, "", "", false, id, "",
				false);
		this.placement = placement;
		this.action = action;

	}

	public GlobalButton(String id, String target, String text, String ifSet, String ifNotSet,
			String set, String unSet, String jScript, String image, String hotKey,
			GlobalButtonPlacement placement, GlobalButtonAction action) {
		super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, "", 0, "", "", "",
				1, "", "", false, id, "", false);
		this.placement = placement;
		this.action = action;

	}

	public GlobalButton(String id, String target, String text, String ifSet, String ifNotSet,
			String set, String unSet, String jScript, String image, String hotKey, int sortOrder,
			GlobalButtonPlacement placement, GlobalButtonAction action) {
		super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, "", 0, "", "", "",
				sortOrder, "", "", false, id, "", false);
		this.placement = placement;
		this.action = action;
	}

	public GlobalButton(String id, String target, String text, String ifSet, String ifNotSet,
			String set, String unSet, String jScript, String image, String hotKey, String fontName,
			int fontHeight, String fontColor, String bgColor1, String bgColor2, int sortOrder,
			String ifAfter, String ifBefore, boolean disabled, String scriptVar, boolean defaultBtn,
			GlobalButtonPlacement placement, GlobalButtonAction action) {
		super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, fontName,
				fontHeight, fontColor, bgColor1, bgColor2, sortOrder, ifAfter, ifBefore, disabled,
				id, scriptVar, defaultBtn);
		this.placement = placement;
		this.action = action;
	}

	public GlobalButtonPlacement getPlacement() {
		return placement;
	}

	public void setPlacement(GlobalButtonPlacement placement) {
		this.placement = placement;
	}

	public GlobalButtonAction getAction() {
		return action;
	}

	public void setAction(GlobalButtonAction action) {
		this.action = action;
	}

	@Override
	public int getMetaSortOrder() {
		switch (placement) {
		case TOP:
			return -1;
		case BOTTOM:
			return 1;
		default:
			return 2;
		}
	}
}
