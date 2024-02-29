package org.guideme.guideme.model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

public class GlobalButton extends Button {
	private Placement placement;
	private Action action;

	public GlobalButton(XMLStreamReader reader) throws XMLStreamException {
		super(reader);
	}

	@Override
	protected void initAttributes(XMLStreamReader reader) {
		String sPlacement = getAttributeOrDefaultNoNS(reader, "placement", "bottom");
		this.placement = Placement.fromString(sPlacement);

		String sAction = getAttributeOrDefaultNoNS(reader, "action", "none");
		this.action = Action.fromString(sAction);
	}

	public GlobalButton(String id, String target, String text) {
		super(target, text, "", "", "", "", "", "", "", "", 0, "", "", "", 1, "", "", false, id, "",
				false);
	}

	public GlobalButton(String id, String target, String text, Placement placement, Action action) {
		super(target, text, "", "", "", "", "", "", "", "", 0, "", "", "", 1, "", "", false, id, "",
				false);
		this.placement = placement;
		this.action = action;

	}

	public GlobalButton(String id, String target, String text, String ifSet, String ifNotSet,
			String set, String unSet, String jScript, String image, String hotKey,
			Placement placement, Action action) {
		super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, "", 0, "", "", "",
				1, "", "", false, id, "", false);
		this.placement = placement;
		this.action = action;

	}

	public GlobalButton(String id, String target, String text, String ifSet, String ifNotSet,
			String set, String unSet, String jScript, String image, String hotKey, int sortOrder,
			Placement placement, Action action) {
		super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, "", 0, "", "", "",
				sortOrder, "", "", false, id, "", false);
		this.placement = placement;
		this.action = action;
	}

	public GlobalButton(String id, String target, String text, String ifSet, String ifNotSet,
			String set, String unSet, String jScript, String image, String hotKey, String fontName,
			int fontHeight, String fontColor, String bgColor1, String bgColor2, int sortOrder,
			String ifAfter, String ifBefore, boolean disabled, String scriptVar, boolean defaultBtn,
			Placement placement, Action action) {
		super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, fontName,
				fontHeight, fontColor, bgColor1, bgColor2, sortOrder, ifAfter, ifBefore, disabled,
				id, scriptVar, defaultBtn);
		this.placement = placement;
		this.action = action;
	}

	public Placement getPlacement() {
		return placement;
	}

	public void setPlacement(Placement placement) {
		this.placement = placement;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public enum Placement {
		TOP, BOTTOM;

		public static Placement fromString(String s) {
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

	public enum Action {
		ADD, REMOVE, NONE;

		
		public static Action fromString(String s) {
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
	}
}
