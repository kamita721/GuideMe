package org.guideme.generated.model;

import org.eclipse.swt.graphics.Color;
public interface Button extends FlagSet, Filterable  {
	public abstract String getFontName();
	public abstract String getImage();
	public abstract String getText();
	public abstract void setDefaultBtn(boolean defaultBtn);
	public abstract Color getBgColor1();
	public abstract int getSortOrder();
	public abstract String getTarget();
	public abstract String getJScript();
	public abstract Color getFontColor();
	public abstract void setJScript(String jScript);
	public abstract void setSortOrder(int sortOrder);
	public abstract Color getBgColor2();
	public abstract void setText(String text);
	public abstract void setHotkey(String hotkey);
	public abstract void setBgColor1(Color bgColor1);
	public abstract void setDisabled(boolean disabled);
	public abstract void setTarget(String target);
	public abstract int getFontHeight();
	public abstract void setImage(String image);
	public abstract boolean getDefaultBtn();
	public abstract void setScriptVar(String scriptVar);
	public abstract void setFontName(String fontName);
	public abstract boolean getDisabled();
	public abstract void setId(String id);
	public abstract String getScriptVar();
	public abstract void setFontHeight(int fontHeight);
	public abstract void setFontColor(Color fontColor);
	public abstract String getId();
	public abstract String getHotkey();
	public abstract void setBgColor2(Color bgColor2);
}
