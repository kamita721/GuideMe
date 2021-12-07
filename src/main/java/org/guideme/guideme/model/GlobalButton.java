package org.guideme.guideme.model;

public class GlobalButton extends Button {
    private Placement placement = Placement.BOTTOM;
    private Action action = Action.NONE;

    public GlobalButton(String id, String target, String text) {
        super(target, text, "", "", "", "", "", "", "", "", "", "", "", "", 1, "", "", false, id, "", false);
    }

    public GlobalButton(String id, String target, String text, Placement placement, Action action) {
        super(target, text, "", "", "", "", "", "", "", "", "", "", "", "", 1, "", "", false, id, "", false);
        this.placement = placement;
        this.action = action;
    }

    public GlobalButton(String id, String target, String text, String ifSet, String ifNotSet, String set, String unSet, String jScript, String image, String hotKey, Placement placement, Action action) {
        super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, "", "", "", "", "", 1, "", "", false, id, "", false);
        this.placement = placement;
        this.action = action;
    }

    public GlobalButton(String id, String target, String text, String ifSet, String ifNotSet, String set, String unSet, String jScript, String image, String hotKey, int sortOrder, Placement placement, Action action) {
        super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, "", "", "", "", "", sortOrder, "", "", false, id, "", false);
        this.placement = placement;
        this.action = action;
    }

    public GlobalButton(String id, String target, String text, String ifSet, String ifNotSet, String set, String unSet, String jScript, String image, String hotKey, String fontName, String fontHeight, String fontColor, String bgColor1, String bgColor2, int sortOrder, String ifAfter, String ifBefore, boolean disabled, String scriptVar, boolean defaultBtn, Placement placement, Action action) {
        super(target, text, ifSet, ifNotSet, set, unSet, jScript, image, hotKey, fontName, fontHeight, fontColor, bgColor1, bgColor2, sortOrder, ifAfter, ifBefore, disabled, id, scriptVar, defaultBtn);
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
        TOP,
        BOTTOM
    }

    public enum Action {
        ADD,
        REMOVE,
        NONE
    }
}
