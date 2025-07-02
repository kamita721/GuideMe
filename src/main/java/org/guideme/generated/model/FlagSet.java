package org.guideme.generated.model;

import java.util.List;
public interface FlagSet  {
	public abstract String getSet();
	public abstract void setSet(String set);
	public abstract String getUnSet();
	public abstract void setUnSet(String unSet);
	
	public void setUnSet(List<String> setList);
	
}
